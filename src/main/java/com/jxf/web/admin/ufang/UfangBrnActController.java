package com.jxf.web.admin.ufang;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jxf.svc.config.Global;
import com.jxf.svc.model.Message;
import com.jxf.svc.persistence.Page;
import com.jxf.ufang.entity.UfangBrnAct;
import com.jxf.ufang.entity.UfangFundAddReduce;
import com.jxf.ufang.entity.UfangFundAddReduce.Status;
import com.jxf.ufang.service.UfangBrnActService;
import com.jxf.ufang.service.UfangBrnService;
import com.jxf.ufang.service.UfangFundAddReduceService;
import com.jxf.web.admin.sys.BaseController;

/**
 * 机构账户Controller
 * @author jinxinfu
 * @version 2018-06-29
 */
@Controller("adminUfangBrnActController")
@RequestMapping(value = "${adminPath}/ufangBrnAct")
public class UfangBrnActController extends BaseController {

	@Autowired
	private UfangBrnService brnService;
	@Autowired
	private UfangBrnActService brnActService;
	@Autowired
	private UfangFundAddReduceService ufangFundAddReduceService;
	
	@ModelAttribute
	public UfangBrnAct get(@RequestParam(required=false) Long id) {
		UfangBrnAct entity = null;
		if (id!=null){
			entity = brnActService.get(id);
		}
		if (entity == null){
			entity = new UfangBrnAct();
		}
		return entity;
	}
	
	@RequiresPermissions("brn:ufangBrnAct:view")
	@RequestMapping(value = {"list", ""})
	public String list(UfangBrnAct ufangBrnAct, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<UfangBrnAct> page = brnActService.findPage(new Page<UfangBrnAct>(request, response), ufangBrnAct); 
		model.addAttribute("page", page);
		return "admin/ufang/brn/act/ufangBrnActList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("brn:ufangBrnAct:view")
	@RequestMapping(value = "add")
	public String add(UfangBrnAct ufangBrnAct, Model model) {
		model.addAttribute("ufangBrnAct", ufangBrnAct);
		return "admin/ufang/brn/act/ufangBrnActAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("brn:ufangBrnAct:view")
	@RequestMapping(value = "query")
	public String query(UfangBrnAct ufangBrnAct, Model model) {
		model.addAttribute("ufangBrnAct", ufangBrnAct);
		return "admin/ufang/brn/act/ufangBrnActQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("brn:ufangBrnAct:view")
	@RequestMapping(value = "update")
	public String update(UfangBrnAct ufangBrnAct, Model model) {
		model.addAttribute("ufangBrnAct", ufangBrnAct);
		return "admin/ufang/brn/act/ufangBrnActUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("brn:ufangBrnAct:edit")
	@RequestMapping(value = "save")
	public String save(UfangBrnAct ufangBrnAct, Model model, RedirectAttributes redirectAttributes) {

		if (!beanValidator(model, ufangBrnAct)){
			return add(ufangBrnAct, model);
		}		
		ufangBrnAct.setCompany(brnService.get(ufangBrnAct.getCompany()));
		brnActService.save(ufangBrnAct);
		addMessage(redirectAttributes, "保存机构账户成功");
		return "redirect:"+Global.getAdminPath()+"/ufangBrnAct/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("brn:ufangBrnAct:edit")
	@RequestMapping(value = "delete")
	public String delete(UfangBrnAct ufangBrnAct, RedirectAttributes redirectAttributes) {
		brnActService.delete(ufangBrnAct);
		addMessage(redirectAttributes, "删除机构账户成功");
		return "redirect:"+Global.getAdminPath()+"/ufangBrnAct/?repage";
	}
	
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("brn:ufangBrnAct:view")
	@RequestMapping(value = "addBal")
	public String addBal(UfangBrnAct ufangBrnAct, Model model) {
		model.addAttribute("ufangBrnAct", ufangBrnAct);
		return "admin/ufang/brn/act/addActBal";
	}
	
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("brn:ufangBrnAct:view")
	@RequestMapping(value = "reduceBal")
	public String reduceBal(UfangBrnAct ufangBrnAct, Model model) {
		model.addAttribute("ufangBrnAct", ufangBrnAct);
		return "admin/ufang/brn/act/reduceActBal";
	}
	
	 /**
	 * 手动给优放公司加款 
	 */
	@RequiresPermissions("brn:ufangBrnAct:edit")
	@RequestMapping(value = "addActBal")
	@ResponseBody
	public Message addActBal(UfangBrnAct ufangBrnAct, BigDecimal trxAmt, Model model, RedirectAttributes redirectAttributes) {
		UfangFundAddReduce addRecord = new UfangFundAddReduce();
		addRecord.setAmount(trxAmt);
		addRecord.setCurBal(ufangBrnAct.getCurBal());
		addRecord.setStatus(UfangFundAddReduce.Status.auditing);
		addRecord.setType(UfangFundAddReduce.Type.add);
		addRecord.setUfangBrn(ufangBrnAct.getCompany());
		addRecord.setCurrCode("CNY");
		ufangFundAddReduceService.save(addRecord);
		return Message.success("加款申请成功");
	}
	
	
	
	 /**
	 * 手动给优放公司加款  
	 */
	@RequiresPermissions("brn:ufangBrnAct:edit")
	@RequestMapping(value = "reduceActBal")
	@ResponseBody
	public Message reduceActBal(UfangBrnAct ufangBrnAct, BigDecimal trxAmt, Model model, RedirectAttributes redirectAttributes) {
		UfangFundAddReduce reduceRecord = new UfangFundAddReduce();
		reduceRecord.setAmount(trxAmt);
		reduceRecord.setCurBal(ufangBrnAct.getCurBal());
		reduceRecord.setStatus(UfangFundAddReduce.Status.auditing);
		reduceRecord.setType(UfangFundAddReduce.Type.reduce);
		reduceRecord.setUfangBrn(ufangBrnAct.getCompany());
		reduceRecord.setCurrCode("CNY");
		ufangFundAddReduceService.save(reduceRecord);
		return Message.success("减款申请成功");
	}
	
	/**
	 * 优放机构加减款审核
	 */
	@RequiresPermissions("brn:ufangBrnAct:edit")
	@RequestMapping(value = "addReduceFundCheck")
	@ResponseBody
	public Message addReduceFundCheck( Long addReduceId,String stataus,Model model, RedirectAttributes redirectAttributes) {
		UfangFundAddReduce ufangFundAddReduce = ufangFundAddReduceService.get(addReduceId);
		if(stataus.equals("pass")) {
			ufangFundAddReduce.setStatus(Status.passed);
			ufangFundAddReduceService.ufangBrnAddReduceApplyCheck(ufangFundAddReduce);
		}else if(stataus.equals("reject")) {
			ufangFundAddReduce.setStatus(Status.reject);
			ufangFundAddReduceService.ufangBrnAddReduceApplyCheck(ufangFundAddReduce);
		}
		return Message.success("操作成功");
	}

}