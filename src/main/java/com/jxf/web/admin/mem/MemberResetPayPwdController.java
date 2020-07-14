package com.jxf.web.admin.mem;

import java.util.List;

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


import com.jxf.mem.entity.MemberResetPayPwd;
import com.jxf.mem.entity.MemberVideoVerify;
import com.jxf.mem.service.MemberResetPayPwdService;
import com.jxf.mem.service.MemberVideoVerifyService;
import com.jxf.mem.utils.MemUtils;
import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.web.admin.sys.BaseController;

/**
 * 修改支付密码Controller
 * @author XIAORONGDIAN
 * @version 2018-11-08
 */
@Controller
@RequestMapping(value = "${adminPath}/memberResetPayPwd")
public class MemberResetPayPwdController extends BaseController {

	@Autowired
	private MemberResetPayPwdService memberResetPayPwdService;
	@Autowired
	private MemberVideoVerifyService memberVideoVerifyService;
	
	@ModelAttribute
	public MemberResetPayPwd get(@RequestParam(required=false) Long id) {
		MemberResetPayPwd entity = null;
		if (id!=null){
			entity = memberResetPayPwdService.get(id);
		}
		if (entity == null){
			entity = new MemberResetPayPwd();
		}
		return entity;
	}
	
	@RequiresPermissions("mem:resetPayPwd:view")
	@RequestMapping(value = {"list", ""})
	public String list(MemberResetPayPwd memberResetPayPwd, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MemberResetPayPwd> page = memberResetPayPwdService.findPage(new Page<MemberResetPayPwd>(request, response), memberResetPayPwd); 
		List<MemberResetPayPwd> list = page.getList();
		for (int i = 0; i < list.size(); i++) {
			MemUtils.mask(list.get(i).getMember());
		}
		page.setList(list);	
		model.addAttribute("page", page);
		return "admin/mem/resetPayPwd/memberResetPayPwdList";
	}
	
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("mem:resetPayPwd:view")
	@RequestMapping(value = "getLivingPhoto")
	@ResponseBody
	public String getLivingPhoto(MemberResetPayPwd memberResetPayPwd, Model model) {
		MemberVideoVerify memberVideoVerify = memberVideoVerifyService.getByTrxId(memberResetPayPwd.getId());
		model.addAttribute(memberVideoVerify.getLivingPhoto());
		return memberVideoVerify.getLivingPhoto();
	}
	
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("mem:resetPayPwd:view")
	@RequestMapping(value = "add")
	public String add(MemberResetPayPwd memberResetPayPwd, Model model) {
		model.addAttribute("memberResetPayPwd", memberResetPayPwd);
		return "admin/mem/resetPayPwd/memberResetPayPwdAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("mem:resetPayPwd:view")
	@RequestMapping(value = "query")
	public String query(MemberResetPayPwd memberResetPayPwd, Model model) {
		model.addAttribute("memberResetPayPwd", memberResetPayPwd);
		return "admin/mem/resetPayPwd/memberResetPayPwdQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("mem:resetPayPwd:view")
	@RequestMapping(value = "update")
	public String update(MemberResetPayPwd memberResetPayPwd, Model model) {
		model.addAttribute("memberResetPayPwd", memberResetPayPwd);
		return "admin/mem/resetPayPwd/memberResetPayPwdUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("mem:resetPayPwd:edit")
	@RequestMapping(value = "save")
	public String save(MemberResetPayPwd memberResetPayPwd, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, memberResetPayPwd)){
			return add(memberResetPayPwd, model);
		}
		memberResetPayPwdService.save(memberResetPayPwd);
		addMessage(redirectAttributes, "保存修改支付密码成功");
		return "redirect:"+Global.getAdminPath()+"/memberResetPayPwd/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("mem:resetPayPwd:edit")
	@RequestMapping(value = "delete")
	public String delete(MemberResetPayPwd memberResetPayPwd, RedirectAttributes redirectAttributes) {
		memberResetPayPwdService.delete(memberResetPayPwd);
		addMessage(redirectAttributes, "删除修改支付密码成功");
		return "redirect:"+Global.getAdminPath()+"/memberResetPayPwd/?repage";
	}

}