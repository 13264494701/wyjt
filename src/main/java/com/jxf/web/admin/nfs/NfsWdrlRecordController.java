package com.jxf.web.admin.nfs;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONObject;
import com.jxf.check.service.NfsCheckRecordService;
import com.jxf.mem.entity.Member;
import com.jxf.nfs.entity.NfsWdrlRecord;
import com.jxf.nfs.entity.NfsWdrlRecord.Status;
import com.jxf.nfs.service.NfsWdrlRecordService;
import com.jxf.pay.utils.HttpPoster;
import com.jxf.pwithdraw.entity.QueryPaymentRequestBean;
import com.jxf.pwithdraw.entity.QueryPaymentResponseBean;
import com.jxf.pwithdraw.entity.fuiou.FuiouMerchantDataBean;
import com.jxf.pwithdraw.entity.fuiou.FuiouQueryPaymentRequestBean;
import com.jxf.pwithdraw.entity.fuiou.FuiouQueryPaymentResponseBean;
import com.jxf.pwithdraw.service.FuiouWithdrawService;
import com.jxf.pwithdraw.service.LianlianPayService;
import com.jxf.svc.config.Constant;
import com.jxf.svc.config.Global;
import com.jxf.svc.excel.ExportExcel;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.user.entity.User;
import com.jxf.svc.sys.util.UserUtils;
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.EncryptUtils;
import com.jxf.svc.utils.MD5Code;
import com.jxf.svc.utils.StringUtils;
import com.jxf.svc.utils.XMLParser;
import com.jxf.web.admin.sys.BaseController;

/**
 * 提现记录Controller
 * @author gaobo
 * @version 2018-12-20
 */
@Controller
@RequestMapping(value = "${adminPath}/wdrlRecord")
public class NfsWdrlRecordController extends BaseController {

	@Autowired
	private NfsWdrlRecordService wdrlRecordService;
	@Autowired
	private LianlianPayService lianlianPayService;
	@Autowired
	private NfsCheckRecordService checkRecordService;
	@Autowired
	private FuiouWithdrawService fuiouWithdrawService;
	
	@ModelAttribute
	public NfsWdrlRecord get(@RequestParam(required=false) Long id) {
		NfsWdrlRecord entity = null;
		if (id!=null){
			entity = wdrlRecordService.get(id);
		}
		if (entity == null){
			entity = new NfsWdrlRecord();
		}
		return entity;
	}
	
	@RequiresPermissions("wdrl:wdrlRecord:view")
	@RequestMapping(value = {"list", ""})
	public String list(NfsWdrlRecord wdrlRecord, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<NfsWdrlRecord> page = wdrlRecordService.findPage(new Page<NfsWdrlRecord>(request, response), wdrlRecord); 
		List<NfsWdrlRecord> wdrlRecords = page.getList();
		for (NfsWdrlRecord wdrlRecord2 : wdrlRecords) {
			Member member = wdrlRecord2.getMember();
			String username = EncryptUtils.encryptString(member.getUsername(), EncryptUtils.Type.PHONE);
			member.setUsername(username);
			wdrlRecord2.setMember(member);
			wdrlRecord2.setCardNo(StringUtils.replacePattern(wdrlRecord2.getCardNo(), "(?<=[\\d]{4})\\d(?=[\\d]{4})", "*"));
		}
		model.addAttribute("page", page);
		return "admin/nfs/wdrl/wdrlRecordList";
	}
	@RequiresPermissions("wdrl:wdrlRecord:view")
	@RequestMapping(value = "wdrlList")
	public String wdrlList(NfsWdrlRecord wdrlRecord, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<NfsWdrlRecord> page = wdrlRecordService.findPage(new Page<NfsWdrlRecord>(request, response), wdrlRecord); 
		List<NfsWdrlRecord> wdrlRecords = page.getList();
		for (NfsWdrlRecord wdrlRecord2 : wdrlRecords) {
			Member member = wdrlRecord2.getMember();
			String username = EncryptUtils.encryptString(member.getUsername(), EncryptUtils.Type.PHONE);
			member.setUsername(username);
			wdrlRecord2.setMember(member);
			wdrlRecord2.setCardNo(StringUtils.replacePattern(wdrlRecord2.getCardNo(), "(?<=[\\d]{4})\\d(?=[\\d]{4})", "*"));
		}
		model.addAttribute("page", page);
		return "admin/nfs/account/wdrlRecordList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("wdrl:wdrlRecord:view")
	@RequestMapping(value = "add")
	public String add(NfsWdrlRecord wdrlRecord, Model model) {
		model.addAttribute("wdrlRecord", wdrlRecord);
		return "admin/nfs/wdrl/wdrlRecordAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("wdrl:wdrlRecord:view")
	@RequestMapping(value = "query")
	public String query(NfsWdrlRecord wdrlRecord, Model model) {
		model.addAttribute("wdrlRecord", wdrlRecord);
		return "admin/nfs/wdrl/wdrlRecordQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("wdrl:wdrlRecord:view")
	@RequestMapping(value = "update")
	public String update(NfsWdrlRecord wdrlRecord, Model model) {
		model.addAttribute("wdrlRecord", wdrlRecord);
		return "admin/nfs/wdrl/wdrlRecordUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("wdrl:wdrlRecord:edit")
	@RequestMapping(value = "save")
	public String save(NfsWdrlRecord wdrlRecord, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, wdrlRecord)){
			return add(wdrlRecord, model);
		}
		wdrlRecordService.save(wdrlRecord);
		addMessage(redirectAttributes, "保存提现记录成功");
		return "redirect:"+Global.getAdminPath()+"/wdrlRecord/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("wdrl:wdrlRecord:edit")
	@RequestMapping(value = "delete")
	public String delete(NfsWdrlRecord wdrlRecord, RedirectAttributes redirectAttributes) {
		wdrlRecordService.delete(wdrlRecord);
		addMessage(redirectAttributes, "删除提现记录成功");
		return "redirect:"+Global.getAdminPath()+"/wdrlRecord/?repage";
	}

	/**
	 * 查询待审核提现记录
	 */
	@RequiresPermissions("wdrl:wdrlRecord:edit")
	@RequestMapping(value = "findPendingAuditRecord")
	public String findPendingAuditRecord(NfsWdrlRecord wdrlRecord, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<NfsWdrlRecord> page = wdrlRecordService.findPendingAuditRecord(new Page<NfsWdrlRecord>(request, response), wdrlRecord); 
		List<NfsWdrlRecord> wdrlRecords = page.getList();
		for (NfsWdrlRecord wdrlRecord2 : wdrlRecords) {
			Member member = wdrlRecord2.getMember();
			String username = EncryptUtils.encryptString(member.getUsername(), EncryptUtils.Type.PHONE);
			member.setUsername(username);
			wdrlRecord2.setMember(member);
			wdrlRecord2.setCardNo(StringUtils.replacePattern(wdrlRecord2.getCardNo(), "(?<=[\\d]{4})\\d(?=[\\d]{4})", "*"));
		}
		model.addAttribute("page", page);
		return "admin/nfs/wdrl/pendingAuditWdrlRecordList";
	}
	
	
	/**
	 * 查询已审核提现记录
	 */
	@RequiresPermissions("wdrl:wdrlRecord:edit")
	@RequestMapping(value = "findAuditedRecord")
	public String findAuditedRecord(NfsWdrlRecord wdrlRecord, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<NfsWdrlRecord> page = wdrlRecordService.findAuditedRecord(new Page<NfsWdrlRecord>(request, response), wdrlRecord); 
		List<NfsWdrlRecord> wdrlRecords = page.getList();
		for (NfsWdrlRecord wdrlRecord2 : wdrlRecords) {
			Member member = wdrlRecord2.getMember();
			String username = EncryptUtils.encryptString(member.getUsername(), EncryptUtils.Type.PHONE);
			member.setUsername(username);
			wdrlRecord2.setMember(member);
			wdrlRecord2.setCardNo(StringUtils.replacePattern(wdrlRecord2.getCardNo(), "(?<=[\\d]{4})\\d(?=[\\d]{4})", "*"));
		}
		model.addAttribute("page", page);
		return "admin/nfs/wdrl/auditedWdrlRecordList";
	}
	

	/**
	 * 提现审核通过
	 */
	@RequiresPermissions("wdrl:wdrlRecord:edit")
	@RequestMapping(value = "checkPass")
	public String checkPass(NfsWdrlRecord wdrlRecord,RedirectAttributes redirectAttributes) {
		NfsWdrlRecord nfsWdrlRecord = wdrlRecordService.get(wdrlRecord.getId());
		Status status = nfsWdrlRecord.getStatus();
		if(!(Status.auditing.equals(status) || Status.mayRepeatOrder.equals(status) 
				|| Status.retrial.equals(status) || Status.questionOrder.equals(status))) {
			addMessage(redirectAttributes, "记录状态已改变，请勿重复操作！");
			return "redirect:"+Global.getAdminPath()+"/wdrlRecord/?repage";
		}
		int code = wdrlRecordService.checkPass(wdrlRecord);
		if(code==Constant.UPDATE_SUCCESS) {
			addMessage(redirectAttributes, "提现审核操作成功");
		}else {
			addMessage(redirectAttributes, "提现审核操作失败");
		}
		return "redirect:"+Global.getAdminPath()+"/wdrlRecord/?repage";
	}
	/**
	 * 拒绝提现
	 */
	@RequiresPermissions("wdrl:wdrlRecord:edit")
	@RequestMapping(value = "refuse")
	public String refuse(NfsWdrlRecord wdrlRecord,String remark,RedirectAttributes redirectAttributes) {
		NfsWdrlRecord nfsWdrlRecord = wdrlRecordService.get(wdrlRecord.getId());
		Status status = nfsWdrlRecord.getStatus();
		if(status.ordinal() > Status.submited.ordinal()) {
			addMessage(redirectAttributes, "该记录状态已更新，请确认后再操作！");
			return "redirect:"+Global.getAdminPath()+"/wdrlRecord/?repage";
		}
		int code = wdrlRecordService.refuse(wdrlRecord);
		if(code==Constant.UPDATE_SUCCESS) {
			addMessage(redirectAttributes, "拒绝提现成功");
		}else {
			addMessage(redirectAttributes, "拒绝提现失败");
		}
		return "redirect:"+Global.getAdminPath()+"/wdrlRecord/?repage";
	}
	/**
	 * 撤销提现页面跳转
	 */
	@RequiresPermissions("wdrl:wdrlRecord:view")
	@RequestMapping(value = "cancelWdrlRecord")
	public String cancelWdrlRecord(NfsWdrlRecord wdrlRecord, Model model) {
		model.addAttribute("wdrlRecord", wdrlRecord);
		return "admin/nfs/wdrl/cancelWdrlRecord";
	}
	
	/**
	 * 撤销提现
	 */
	@RequiresPermissions("wdrl:wdrlRecord:edit")
	@RequestMapping(value = "cancel")
	public String cancelWdrl(NfsWdrlRecord wdrlRecord,String remark,HttpServletRequest request,RedirectAttributes redirectAttributes) {
		NfsWdrlRecord nfsWdrlRecord = wdrlRecordService.get(wdrlRecord.getId());
		Status status = nfsWdrlRecord.getStatus();
		if(status.ordinal() > Status.submited.ordinal()) {
			addMessage(redirectAttributes,"该记录状态已更新，请确认后再操作！");
			return "redirect:"+Global.getAdminPath()+"/wdrlRecord/?repage";
		}
		wdrlRecord.setStatus(NfsWdrlRecord.Status.cancel);
		wdrlRecord.setRmk(remark);
		wdrlRecord.setCheckTime(new Date());
		int respCode = wdrlRecordService.cancel(wdrlRecord);
		checkRecordService.saveWdrlCheckLog(wdrlRecord);
		if(respCode == Constant.UPDATE_FAILED) {
			logger.error("提现订单{}撤销提现失败!",wdrlRecord.getId());
		}else if(respCode == Constant.UPDATE_SUCCESS) {
			logger.error("提现订单{}撤销提现成功!",wdrlRecord.getId());
		}
		return "admin/nfs/wdrl/wdrlRecordList";
	}
	/**
	 * 汇款成功
	 */
	@RequiresPermissions("wdrl:wdrlRecord:edit")
	@RequestMapping(value = "successConfirm")
	public String successConfirm(NfsWdrlRecord wdrlRecord,RedirectAttributes redirectAttributes) {
		NfsWdrlRecord nfsWdrlRecord = wdrlRecordService.get(wdrlRecord.getId());
		Status status = nfsWdrlRecord.getStatus();
		if(Status.madeMoney.equals(status)) {
			addMessage(redirectAttributes, "提现记录状态已更新，请勿重复操作！");
			return "redirect:"+Global.getAdminPath()+"/wdrlRecord/?repage";
		}
		String withdrawType = Global.getConfig("withdrawType");
		if(StringUtils.equals(withdrawType, "fuiou")) {
			Map<String, String> queryResult = fuiouWithdrawService.queryFuiouPayment(wdrlRecord);
			addMessage(redirectAttributes, queryResult.get("message"));
		}else {
			QueryPaymentRequestBean queryRequestBean = new QueryPaymentRequestBean();
			queryRequestBean.setNo_order(wdrlRecord.getId()+"");
			String result = lianlianPayService.queryOrder(queryRequestBean);
			QueryPaymentResponseBean queryPaymentResponseBean = JSONObject.parseObject(result, QueryPaymentResponseBean.class);
			if(StringUtils.equals(queryPaymentResponseBean.getResult_pay(), "SUCCESS")) {
				wdrlRecord.setThirdOrderNo(queryPaymentResponseBean.getOid_paybill());
				try {
					wdrlRecord.setPayTime(DateUtils.parse(queryPaymentResponseBean.getDt_order()));
				} catch (ParseException e) {
					logger.error("提现订单{}打款时间{}解析错误",wdrlRecord.getId(),queryPaymentResponseBean.getDt_order());
				}
				wdrlRecord.setPayAmount(StringUtils.toDecimal(queryPaymentResponseBean.getMoney_order()));
				int code = wdrlRecordService.successConfirm(wdrlRecord);
				if(code==Constant.UPDATE_SUCCESS) {
					addMessage(redirectAttributes, "提现确认成功");
				}else {
					addMessage(redirectAttributes, "提现确认失败");
				}	
			}else {		
				addMessage(redirectAttributes, "连连查询结果显示该订单状态为：" + queryPaymentResponseBean.getResult_pay() + "请重新确认后再操作！");
			}
		}
		return "redirect:"+Global.getAdminPath()+"/wdrlRecord/?repage";
	}
	/**
	 * 汇款失败
	 */
	@RequiresPermissions("wdrl:wdrlRecord:edit")
	@RequestMapping(value = "failedRemitted")
	public String failedRemitted(HttpServletRequest request, RedirectAttributes redirectAttributes) {
		String id = request.getParameter("id");
		NfsWdrlRecord wdrlRecord = wdrlRecordService.get(Long.valueOf(id));
		Status status = wdrlRecord.getStatus();
		if (Status.failure.equals(status)) {
			addMessage(redirectAttributes, "提现记录状态已更新，请勿重复操作！");
			return "redirect:" + Global.getAdminPath() + "/wdrlRecord/?repage";
		}
		User checker = UserUtils.getUser();

		String withdrawType = Global.getConfig("withdrawType");
		if (StringUtils.equals(withdrawType, "fuiou")) {
			Map<String, String> queryResult = fuiouWithdrawService.queryFuiouPayment(wdrlRecord);
			addMessage(redirectAttributes, queryResult.get("message"));
			return "redirect:" + Global.getAdminPath() + "/wdrlRecord/?repage";
		} else {
			QueryPaymentRequestBean queryRequestBean = new QueryPaymentRequestBean();
			queryRequestBean.setNo_order(wdrlRecord.getId() + "");
			String result = lianlianPayService.queryOrder(queryRequestBean);
			QueryPaymentResponseBean queryPaymentResponseBean = JSONObject.parseObject(result,
					QueryPaymentResponseBean.class);
			String result_pay = queryPaymentResponseBean.getResult_pay();
			String oid_paybill = queryPaymentResponseBean.getOid_paybill();
			String money_order = queryPaymentResponseBean.getMoney_order();
			if (StringUtils.equals(result_pay, "SUCCESS")) {
				wdrlRecord.setStatus(Status.madeMoney);
				wdrlRecord.setThirdOrderNo(oid_paybill);
				wdrlRecord.setPayAmount(StringUtils.toDecimal(money_order));
				wdrlRecordService.save(wdrlRecord);
				checkRecordService.saveWdrlCheckLog(wdrlRecord);
				logger.error("提现订单{}连连反馈付款成功", wdrlRecord.getId());
				addMessage(redirectAttributes, "提现订单" + wdrlRecord.getId() + "连连反馈付款成功");
				return "redirect:" + Global.getAdminPath() + "/wdrlRecord/?repage";
			}
			int respCode = wdrlRecordService.failure(wdrlRecord, checker.getEmpNam() + "确认汇款失败");
			if (respCode == Constant.UPDATE_SUCCESS) {
				// 状态变更成功
				addMessage(redirectAttributes, "退款成功！");
			} else {
				addMessage(redirectAttributes, "订单状态已更新，请勿重复操作！");
			}
			return "redirect:" + Global.getAdminPath() + "/wdrlRecord/?repage";
		}
	}
	
	/**
	 *  提现失败退款审核
	 */
//	@RequiresPermissions("wdrl:wdrlRecord:edit")
//	@RequestMapping(value = "checkFailedOrder")
//	public String checkFailedOrder(HttpServletRequest request,RedirectAttributes redirectAttributes) {
//		String id = request.getParameter("id");
//		if(StringUtils.isBlank(id)) {
//			addMessage(redirectAttributes, "参数错误：提现订单id为空");
//			return "redirect:"+Global.getAdminPath()+"/wdrlRecord/?repage";
//		}
//		NfsWdrlRecord wdrlRecord = wdrlRecordService.get(Long.valueOf(id));
//		if(wdrlRecord.getFailedOrderStatus().equals(NfsWdrlRecord.FailedOrderStatus.refundSuccessed)) {
//			addMessage(redirectAttributes, "提现订单"+wdrlRecord.getId()+"已退款成功，请复核！");
//			return "redirect:"+Global.getAdminPath()+"/wdrlRecord/?repage";
//		}
//		QueryPaymentRequestBean queryRequestBean = new QueryPaymentRequestBean();
//		queryRequestBean.setNo_order(wdrlRecord.getId()+ "" );
//		String result = lianlianPayService.queryOrder(queryRequestBean);
//		QueryPaymentResponseBean queryPaymentResponseBean = JSONObject.parseObject(result, QueryPaymentResponseBean.class);
//		String result_pay = queryPaymentResponseBean.getResult_pay();
////		String dt_order = queryPaymentResponseBean.getDt_order();
//		String oid_paybill = queryPaymentResponseBean.getOid_paybill();
//		String money_order = queryPaymentResponseBean.getMoney_order();
//		if(StringUtils.equals(result_pay, "SUCCESS")) {
//			wdrlRecord.setStatus(Status.madeMoney);
//			wdrlRecord.setThirdOrderNo(oid_paybill);
////			Date payTime = null;
////			try {
////				payTime = DateUtils.parse(dt_order);
////			} catch (ParseException e) {
////				logger.error("提现订单{}连连查询接口返回dt_order：{}解析错误",wdrlRecord.getId(),dt_order);
////			}
////			wdrlRecord.setPayTime(payTime);
//			wdrlRecord.setPayAmount(StringUtils.toDecimal(money_order));
//			wdrlRecordService.save(wdrlRecord);
//			checkRecordService.saveWdrlCheckLog(wdrlRecord);
//			logger.error("提现订单连连反馈付款成功",wdrlRecord.getId());
//			addMessage(redirectAttributes, "提现订单"+wdrlRecord.getId()+"连连反馈付款成功");
//			return "redirect:"+Global.getAdminPath()+"/wdrlRecord/?repage";
//		}
////		try {
////			int respCode = wdrlRecordService.withdrawFailRefund(wdrlRecord)
////			if(respCode == Constant.UPDATE_SUCCESS) {
////				logger.info("提现订单{}退款成功",wdrlRecord.getId());
////				wdrlRecord.setFailedOrderStatus(FailedOrderStatus.refundSuccessed);
////				wdrlRecordService.save(wdrlRecord);//更新退款状态
////				addMessage(redirectAttributes, "退款成功！");
////			}else {
////				logger.error("提现订单{}退款失败",wdrlRecord.getId());
////				wdrlRecord.setFailedOrderStatus(FailedOrderStatus.refundFailed);
////				wdrlRecordService.save(wdrlRecord);//更新退款状态
////				addMessage(redirectAttributes, "退款失败，请联系系统管理员！");
////			}	
////		} catch (Exception e) {
////			logger.error("提现订单{}退款异常{}",wdrlRecord.getId(),Exceptions.getStackTraceAsString(e));
////			addMessage(redirectAttributes, "提现订单退款异常请联系系统管理员！");
////		}
//		checkRecordService.saveWdrlCheckLog(wdrlRecord);
//		return "redirect:"+Global.getAdminPath()+"/wdrlRecord/?repage";
//	}
	
	
	
	/**
	 * 查询订单
	 */
	@RequiresPermissions("wdrl:wdrlRecord:view")
	@RequestMapping(value = "queryThirdOrder")
	public String queryThirdOrder(HttpServletRequest request,Model model) {
		String id = request.getParameter("id");
		NfsWdrlRecord wdrlRecord = wdrlRecordService.get(Long.valueOf(id));
		String withdrawType = Global.getConfig("withdrawType");
		if(StringUtils.equals(withdrawType, "fuiou")) {
			//富友提现
			String startdt = DateUtils.getDateStr(wdrlRecord.getSubmitTime(), "yyyyMMdd");
			String enddt = DateUtils.getDateStr(wdrlRecord.getSubmitTime(), "yyyyMMdd");
			FuiouQueryPaymentRequestBean queryPaymentRequestBean = new FuiouQueryPaymentRequestBean();
			queryPaymentRequestBean.setBusicd(FuiouMerchantDataBean.BUSINESS_CODE);
			queryPaymentRequestBean.setOrderno(String.valueOf(wdrlRecord.getId()));
			queryPaymentRequestBean.setVer(FuiouMerchantDataBean.QUERY_REQUEST_VER);
			queryPaymentRequestBean.setStartdt(startdt);
			queryPaymentRequestBean.setEnddt(enddt);
			String response = fuiouWithdrawService.queryOrder(queryPaymentRequestBean);
			FuiouQueryPaymentResponseBean queryPaymentResponseBean = new FuiouQueryPaymentResponseBean();
			
			Map<String, Object> resultMap = XMLParser.xmlStrConvertToMap(response);
			logger.info("富友提现订单查询返回XML解析结果：{}",resultMap.toString());
			String respCode = (String) resultMap.get("ret");
			String memo = (String) resultMap.get("memo");
			queryPaymentResponseBean.setRet(respCode);
			queryPaymentResponseBean.setMemo(memo);
			@SuppressWarnings("unchecked")
			Map<String, Object> childMap = (Map<String, Object>) resultMap.get("trans");
			if(childMap != null && !childMap.isEmpty()) {
				String orderno = (String) childMap.get("orderno"); // 非空
				String state = (String) childMap.get("state");//交易状态
				String amt = (String) childMap.get("amt"); // 单位：分
				String tpst = (String) childMap.get("tpst");// 是否退票 1是，0否
				String transStatusDesc = (String) childMap.get("transStatusDesc");// 交易状态类别
				String reason = (String) childMap.get("reason");// 结果原因
				String merdt = (String)childMap.get("merdt"); 
				String accntno = (String)childMap.get("accntno"); 
				String accntnm = (String)childMap.get("accntnm"); 
				String rspcd = (String)childMap.get("rspcd");//银行响应码
				
				queryPaymentResponseBean.setAccntno(accntno);
				queryPaymentResponseBean.setAccntnm(accntnm);
				queryPaymentResponseBean.setAmt(StringUtils.decimalToStr(new BigDecimal(amt).divide(new BigDecimal(100)), 2));
				queryPaymentResponseBean.setMerdt(merdt);
				queryPaymentResponseBean.setOrderno(orderno);
				queryPaymentResponseBean.setReason(reason);
				queryPaymentResponseBean.setTpst(tpst);
				queryPaymentResponseBean.setTransStatusDesc(transStatusDesc);
				queryPaymentResponseBean.setRspcd(rspcd);
				queryPaymentResponseBean.setState(state);
			}
			model.addAttribute("fuiouQueryPaymentResponseBean", queryPaymentResponseBean);
			return "admin/nfs/wdrl/queryThirdOrderForFuiou";
		}else if(StringUtils.equals(withdrawType, "lianlian")) {
			//连连提现
			QueryPaymentRequestBean queryRequestBean = new QueryPaymentRequestBean();
			queryRequestBean.setNo_order(wdrlRecord.getId()+ "" );
			String result = lianlianPayService.queryOrder(queryRequestBean);
			QueryPaymentResponseBean queryPaymentResponseBean = JSONObject.parseObject(result, QueryPaymentResponseBean.class);
			String settle_date = queryPaymentResponseBean.getSettle_date();
			if(StringUtils.equals(queryPaymentResponseBean.getRet_code(), "8901")){
				model.addAttribute("noRecord", "1");
			}else {
				model.addAttribute("noRecord", "0");
				if(StringUtils.isNotBlank(settle_date)) {
					model.addAttribute("settle_date",DateUtils.parseDate(settle_date));
				}
			}
			model.addAttribute(queryPaymentResponseBean);
		}else {
			
		}
		return "admin/nfs/wdrl/queryThirdOrder";
	}
	
	
	/**
	 * 导出提现数据
	 * @param wdrlRecord
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("wdrl:wdrlRecord:view")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(NfsWdrlRecord wdrlRecord, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            
			int days = DateUtils.getDifferenceOfTwoDate(wdrlRecord.getBeginTime(),wdrlRecord.getEndTime());
			if(days>30){
				addMessage(redirectAttributes, "导出数据的时间跨度不能超过30天");
				return null;
			}
            List<NfsWdrlRecord> wdrlRecordList = wdrlRecordService.findList(wdrlRecord);
            
            String fileName = "提现数据"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            ExportExcel exprotExcel = new ExportExcel("提现数据", NfsWdrlRecord.class);
            exprotExcel.setDataList(wdrlRecordList);
            exprotExcel.write(response, fileName);
            exprotExcel.dispose();
            return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出提现失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/wdrlRecord/?repage";
    }
	
	/**
	 * 下载提现凭证
	 * @param wdrlRecord
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("wdrl:wdrlRecord:view")
    @RequestMapping(value = "downLoadReceipt")
    public String downLoadReceipt(NfsWdrlRecord wdrlRecord, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			wdrlRecord = wdrlRecordService.get(wdrlRecord);
			
			String macSource = "1.0|" + FuiouMerchantDataBean.MERID + "|" 
                    + wdrlRecord.getThirdOrderNo() + "|MD5PY36|MD5|"+FuiouMerchantDataBean.KEY;
	        String sign = MD5Code.MD5Encode(macSource, "UTF-8");
			String fm = "<ORDER>"+
	    			"<VERSION>1.0</VERSION>"+
	    			"<MCHNTCD>" + FuiouMerchantDataBean.MERID + "</MCHNTCD>"+
	    			"<MCHNTORDERID>" + wdrlRecord.getThirdOrderNo() + "</MCHNTORDERID>"+    			
	    			"<BUSICD>PY36</BUSICD>"+
	    			"<SIGNTP>MD5</SIGNTP>"+
	    			"<SIGN>" + sign + "</SIGN>"+
	    			"</ORDER>";
			
	        Map<String, String> queryMap = new HashMap<String, String>();
	        queryMap.put("FM", fm);
	        logger.info("FM={}",fm);
	        String result = new HttpPoster("https://mpay.fuiou.com:16128/checkInfo/createReceipt.pay").postStr(queryMap);
	        logger.info("result={}",result);
	        byte[] data = result.getBytes();  
	        String fileName = wdrlRecord.getId()+".pdf";
	        fileName = URLEncoder.encode(fileName, "UTF-8");  
	        response.reset();  
	        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");  
	        response.addHeader("Content-Length", "" + data.length);  
	        response.setContentType("application/octet-stream;charset=UTF-8");  
	        OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());  
	        outputStream.write(data);  
	        outputStream.flush();  
	        outputStream.close(); 
			return null;

		} catch (Exception e) {
			addMessage(redirectAttributes, "下载提现凭证！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/wdrlRecord/?repage";
    }
}