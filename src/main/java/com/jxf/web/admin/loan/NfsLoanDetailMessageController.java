package com.jxf.web.admin.loan;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jxf.loan.entity.NfsLoanDetailMessage;
import com.jxf.loan.service.NfsLoanDetailMessageService;
import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.web.admin.sys.BaseController;

/**
 * 借条详情对话记录Controller
 * @author XIAORONGDIAN
 * @version 2018-12-03
 */
@Controller
@RequestMapping(value = "${adminPath}/loan/nfsLoanDetailMessage")
public class NfsLoanDetailMessageController extends BaseController {

	@Autowired
	private NfsLoanDetailMessageService nfsLoanDetailMessageService;
	
	@ModelAttribute
	public NfsLoanDetailMessage get(@RequestParam(required=false) Long id) {
		NfsLoanDetailMessage entity = null;
		if (id!=null){
			entity = nfsLoanDetailMessageService.get(id);
		}
		if (entity == null){
			entity = new NfsLoanDetailMessage();
		}
		return entity;
	}
	
	@RequiresPermissions("loan:nfsLoanDetailMessage:view")
	@RequestMapping(value = {"list", ""})
	public String list(NfsLoanDetailMessage nfsLoanDetailMessage, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<NfsLoanDetailMessage> page = nfsLoanDetailMessageService.findPage(new Page<NfsLoanDetailMessage>(request, response), nfsLoanDetailMessage); 
		model.addAttribute("page", page);
		return "loan/loan/nfsLoanDetailMessageList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("loan:nfsLoanDetailMessage:view")
	@RequestMapping(value = "add")
	public String add(NfsLoanDetailMessage nfsLoanDetailMessage, Model model) {
		model.addAttribute("nfsLoanDetailMessage", nfsLoanDetailMessage);
		return "loan/loan/nfsLoanDetailMessageAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("loan:nfsLoanDetailMessage:view")
	@RequestMapping(value = "query")
	public String query(NfsLoanDetailMessage nfsLoanDetailMessage, Model model) {
		model.addAttribute("nfsLoanDetailMessage", nfsLoanDetailMessage);
		return "loan/loan/nfsLoanDetailMessageQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("loan:nfsLoanDetailMessage:view")
	@RequestMapping(value = "update")
	public String update(NfsLoanDetailMessage nfsLoanDetailMessage, Model model) {
		model.addAttribute("nfsLoanDetailMessage", nfsLoanDetailMessage);
		return "loan/loan/nfsLoanDetailMessageUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("loan:nfsLoanDetailMessage:edit")
	@RequestMapping(value = "save")
	public String save(NfsLoanDetailMessage nfsLoanDetailMessage, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, nfsLoanDetailMessage)){
			return add(nfsLoanDetailMessage, model);
		}
		nfsLoanDetailMessageService.save(nfsLoanDetailMessage);
		addMessage(redirectAttributes, "保存借条详情对话记录成功");
		return "redirect:"+Global.getAdminPath()+"/loan/nfsLoanDetailMessage/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("loan:nfsLoanDetailMessage:edit")
	@RequestMapping(value = "delete")
	public String delete(NfsLoanDetailMessage nfsLoanDetailMessage, RedirectAttributes redirectAttributes) {
		nfsLoanDetailMessageService.delete(nfsLoanDetailMessage);
		addMessage(redirectAttributes, "删除借条详情对话记录成功");
		return "redirect:"+Global.getAdminPath()+"/loan/nfsLoanDetailMessage/?repage";
	}

}