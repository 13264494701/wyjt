package com.jxf.web.admin.manage;


import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jxf.loan.entity.NfsLoanApply;
import com.jxf.loan.entity.NfsLoanApplyDetail;
import com.jxf.loan.entity.NfsLoanPartialAndDelay;
import com.jxf.loan.entity.NfsLoanRecord;
import com.jxf.loan.service.NfsLoanPartialAndDelayService;
import com.jxf.loan.service.NfsLoanRecordService;

import com.jxf.svc.config.Global;
import com.jxf.svc.excel.ExportExcel;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.utils.DateUtils;
import com.jxf.web.admin.sys.BaseController;



/**
 * 借条记录Controller
 * @author wo
 * @version 2018-10-10
 */
@Controller
@RequestMapping(value = "${adminPath}/manage")
public class ManageLoanController extends BaseController {

	@Autowired
	private NfsLoanRecordService loanRecordService;
	@Autowired
	private NfsLoanPartialAndDelayService loanPartialAndDelayService;
	
	
	@RequiresPermissions("manage:view")
	@RequestMapping(value = "loanList")
	public String loanList(NfsLoanRecord loanRecord, HttpServletRequest request, HttpServletResponse response, Model model) {

		Date beginDate = loanRecord.getStatus().equals(NfsLoanRecord.Status.repayed)?loanRecord.getBeginCompleteDate():loanRecord.getBeginDueRepayDate();
		Date endDate = loanRecord.getStatus().equals(NfsLoanRecord.Status.repayed)?loanRecord.getEndCompleteDate():loanRecord.getEndDueRepayDate();
	
		if (beginDate != null) {
			Calendar calendar = DateUtils.toCalendar(beginDate);
			calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMinimum(Calendar.HOUR_OF_DAY));
			calendar.set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE));
			calendar.set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND));
			beginDate = calendar.getTime();
		}
		if (endDate != null) {
			Calendar calendar = DateUtils.toCalendar(endDate);
			calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMaximum(Calendar.HOUR_OF_DAY));
			calendar.set(Calendar.MINUTE, calendar.getActualMaximum(Calendar.MINUTE));
			calendar.set(Calendar.SECOND, calendar.getActualMaximum(Calendar.SECOND));
			endDate = calendar.getTime();
		}
		
		loanRecord.setRepayType(NfsLoanApply.RepayType.oneTimePrincipalAndInterest);
		loanRecord.setMinAmount(new BigDecimal(100));
		loanRecord.setMaxAmount(new BigDecimal(4999));
		loanRecord.setMinTerm(3);
		loanRecord.setMaxTerm(8);
		
		if(loanRecord.getStatus().equals(NfsLoanRecord.Status.repayed)) {
			loanRecord.setBeginCompleteDate(beginDate);
			loanRecord.setEndCompleteDate(endDate);	
		}else if(loanRecord.getStatus().equals(NfsLoanRecord.Status.penddingRepay)) {
			loanRecord.setBeginDueRepayDate(beginDate);
			loanRecord.setEndDueRepayDate(endDate);
		}
		
		Page<NfsLoanRecord> page = loanRecordService.findPage(new Page<NfsLoanRecord>(request, response), loanRecord);
		model.addAttribute("page", page);
		model.addAttribute("loanStatus", loanRecord.getStatus());
		return "admin/manage/loanRecordList";
	}

	@RequiresPermissions("manage:view")
	@RequestMapping(value = "partialAndDelayLoanList")
	public String delayLoanList(NfsLoanPartialAndDelay loanPartialAndDelay, HttpServletRequest request, HttpServletResponse response, Model model) {

		Date beginDate = loanPartialAndDelay.getBeginTime();
		Date endDate = loanPartialAndDelay.getEndTime();
		if (beginDate != null) {
			Calendar calendar = DateUtils.toCalendar(beginDate);
			calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMinimum(Calendar.HOUR_OF_DAY));
			calendar.set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE));
			calendar.set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND));
			beginDate = calendar.getTime();
		}
		if (endDate != null) {
			Calendar calendar = DateUtils.toCalendar(endDate);
			calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMaximum(Calendar.HOUR_OF_DAY));
			calendar.set(Calendar.MINUTE, calendar.getActualMaximum(Calendar.MINUTE));
			calendar.set(Calendar.SECOND, calendar.getActualMaximum(Calendar.SECOND));
			endDate = calendar.getTime();
		}
		loanPartialAndDelay.setStatus(NfsLoanPartialAndDelay.Status.agreed);
		loanPartialAndDelay.setMinAmount(new BigDecimal(100));
		loanPartialAndDelay.setMaxAmount(new BigDecimal(4999));
		loanPartialAndDelay.setBeginTime(beginDate);
		loanPartialAndDelay.setEndTime(endDate);
		Page<NfsLoanPartialAndDelay> page = loanPartialAndDelayService.findPage(new Page<NfsLoanPartialAndDelay>(request, response), loanPartialAndDelay);
		model.addAttribute("page", page);
		return "admin/manage/loanPartialAndDelayList";
	}

	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("record:loanRecord:view")
	@RequestMapping(value = "query")
	public String query(NfsLoanRecord loanRecord, Model model) {
		model.addAttribute("loanRecord", loanRecord);
		return "admin/loan/record/loanRecordQuery";
	}

	 
	
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("record:loanRecord:edit")
	@RequestMapping(value = "delete")
	public String delete(NfsLoanRecord loanRecord, RedirectAttributes redirectAttributes) {
		loanRecordService.delete(loanRecord);
		addMessage(redirectAttributes, "删除借条记录成功");
		return "redirect:"+Global.getAdminPath()+"/loanRecord/?repage";
	}
	
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("record:loanRecord:view")
	@RequestMapping(value = "queryLoanRecordByDetailId")
	public String queryLoanRecordByDetailId(String id, Model model) {
		NfsLoanRecord param = new NfsLoanRecord();
		NfsLoanApplyDetail detail = new NfsLoanApplyDetail();
		detail.setId(Long.valueOf(id));
		param.setLoanApplyDetail(detail);
		List<NfsLoanRecord> loanRecords = loanRecordService.findList(param);
		NfsLoanRecord loanRecord = loanRecords.get(0);
		model.addAttribute("loanRecord", loanRecord);
		return "admin/loan/record/loanRecordQuery";
	}
	
	/**
	 * 导出借条数据
	 * @param loanRecord
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("manage:view")
    @RequestMapping(value = "exportLoanData", method=RequestMethod.POST)
    public String exportLoanFile(NfsLoanRecord loanRecord, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			
			Date beginDate = loanRecord.getStatus().equals(NfsLoanRecord.Status.repayed)?loanRecord.getBeginCompleteDate():loanRecord.getBeginDueRepayDate();
			Date endDate = loanRecord.getStatus().equals(NfsLoanRecord.Status.repayed)?loanRecord.getEndCompleteDate():loanRecord.getEndDueRepayDate();
			
            if(beginDate==null||endDate==null) {
				addMessage(redirectAttributes, "起始时间和结束时间不能为空");
				return "redirect:"+Global.getAdminPath()+"/manage/loanList?repage&status="+loanRecord.getStatus();	
            }
            
			int days = DateUtils.getDifferenceOfTwoDate(beginDate,endDate);
			if(Math.abs(days)>30){
				addMessage(redirectAttributes, "导出数据的时间跨度不能超过30天");
				return "redirect:"+Global.getAdminPath()+"/manage/loanList?repage&status="+loanRecord.getStatus();	
			}

			if (beginDate != null) {
				Calendar calendar = DateUtils.toCalendar(beginDate);
				calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMinimum(Calendar.HOUR_OF_DAY));
				calendar.set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE));
				calendar.set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND));
				beginDate = calendar.getTime();
			}
			if (endDate != null) {
				Calendar calendar = DateUtils.toCalendar(endDate);
				calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMaximum(Calendar.HOUR_OF_DAY));
				calendar.set(Calendar.MINUTE, calendar.getActualMaximum(Calendar.MINUTE));
				calendar.set(Calendar.SECOND, calendar.getActualMaximum(Calendar.SECOND));
				endDate = calendar.getTime();
			}
			
			loanRecord.setRepayType(NfsLoanApply.RepayType.oneTimePrincipalAndInterest);
			loanRecord.setMinAmount(new BigDecimal(100));
			loanRecord.setMaxAmount(new BigDecimal(4999));
			loanRecord.setMinTerm(3);
			loanRecord.setMaxTerm(8);
			if(loanRecord.getStatus().equals(NfsLoanRecord.Status.repayed)) {
				loanRecord.setBeginCompleteDate(beginDate);
				loanRecord.setEndCompleteDate(endDate);	
			}else if(loanRecord.getStatus().equals(NfsLoanRecord.Status.penddingRepay)) {
				loanRecord.setBeginDueRepayDate(beginDate);
				loanRecord.setEndDueRepayDate(endDate);
			}
			
            List<NfsLoanRecord> loanRecordList = loanRecordService.findList(loanRecord);
            
    		loanRecordService.filter(loanRecordList);
    		
            String fileName = "借条数据"+DateUtils.getDate("yyyy-MM-dd")+".xlsx";
            ExportExcel exprotExcel = new ExportExcel("借条数据", NfsLoanRecord.class);
            exprotExcel.setDataList(loanRecordList);
            exprotExcel.write(response, fileName);
            exprotExcel.dispose();
            return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出借条数据失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/manage/loanList?repage&status="+loanRecord.getStatus();	
    }
	
	/**
	 * 导出延期借条数据
	 * @param loanRecord
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("manage:view")
    @RequestMapping(value = "exportPartialAndDelayLoanData", method=RequestMethod.POST)
    public String exportPartialAndDelayLoanFile(NfsLoanPartialAndDelay loanPartialAndDelay, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			Date beginDate = loanPartialAndDelay.getBeginTime();
			Date endDate = loanPartialAndDelay.getEndTime();
            if(beginDate==null||endDate==null) {
				addMessage(redirectAttributes, "起始时间和结束时间不能为空");
				return "redirect:"+Global.getAdminPath()+"/manage/partialAndDelayLoanList?repage";	
            }
			int days = DateUtils.getDifferenceOfTwoDate(beginDate,endDate);
			if(Math.abs(days)>7){
				addMessage(redirectAttributes, "导出数据的时间跨度不能超过7天");
				return "redirect:"+Global.getAdminPath()+"/manage/partialAndDelayLoanList?repage";
			}

			if (beginDate != null) {
				Calendar calendar = DateUtils.toCalendar(beginDate);
				calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMinimum(Calendar.HOUR_OF_DAY));
				calendar.set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE));
				calendar.set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND));
				beginDate = calendar.getTime();
			}
			if (endDate != null) {
				Calendar calendar = DateUtils.toCalendar(endDate);
				calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMaximum(Calendar.HOUR_OF_DAY));
				calendar.set(Calendar.MINUTE, calendar.getActualMaximum(Calendar.MINUTE));
				calendar.set(Calendar.SECOND, calendar.getActualMaximum(Calendar.SECOND));
				endDate = calendar.getTime();
			}
			
			loanPartialAndDelay.setStatus(NfsLoanPartialAndDelay.Status.agreed);
			loanPartialAndDelay.setMinAmount(new BigDecimal(100));
			loanPartialAndDelay.setMaxAmount(new BigDecimal(4999));
			loanPartialAndDelay.setBeginTime(beginDate);
			loanPartialAndDelay.setEndTime(endDate);
            List<NfsLoanPartialAndDelay> delayLoanRecordList = loanPartialAndDelayService.findList(loanPartialAndDelay);
            loanPartialAndDelayService.filter(delayLoanRecordList);
            String fileName = "延期借条数据"+DateUtils.getDate("yyyy-MM-dd")+".xlsx";
            ExportExcel exprotExcel = new ExportExcel("延期借条数据", NfsLoanPartialAndDelay.class);
            exprotExcel.setDataList(delayLoanRecordList);
            exprotExcel.write(response, fileName);
            exprotExcel.dispose();
            return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出延期借条数据失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/manage/partialAndDelayLoanList?repage";
    }


}