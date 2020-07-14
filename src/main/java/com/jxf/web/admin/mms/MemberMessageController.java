package com.jxf.web.admin.mms;

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

import com.jxf.mem.entity.MemberMessage;
import com.jxf.mem.service.MemberMessageService;
import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.web.admin.sys.BaseController;

/**
 * 会员消息Controller
 * @author gaobo
 * @version 2019-03-18
 */
@Controller
@RequestMapping(value = "${adminPath}/memberMessage")
public class MemberMessageController extends BaseController {

	@Autowired
	private MemberMessageService memberMessageService;
	
	@ModelAttribute
	public MemberMessage get(@RequestParam(required=false) Long id) {
		MemberMessage entity = null;
		if (id!=null){
			entity = memberMessageService.get(id);
		}
		if (entity == null){
			entity = new MemberMessage();
		}
		return entity;
	}
	
	@RequiresPermissions("mem:memberMessage:view")
	@RequestMapping(value = {"list", ""})
	public String list(MemberMessage memberMessage, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MemberMessage> page = memberMessageService.findPage(new Page<MemberMessage>(request, response), memberMessage); 
		model.addAttribute("page", page);
		return "mem/mem/memberMessageList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("mem:memberMessage:view")
	@RequestMapping(value = "add")
	public String add(MemberMessage memberMessage, Model model) {
		model.addAttribute("memberMessage", memberMessage);
		return "mem/mem/memberMessageAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("mem:memberMessage:view")
	@RequestMapping(value = "query")
	public String query(MemberMessage memberMessage, Model model) {
		model.addAttribute("memberMessage", memberMessage);
		return "mem/mem/memberMessageQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("mem:memberMessage:view")
	@RequestMapping(value = "update")
	public String update(MemberMessage memberMessage, Model model) {
		model.addAttribute("memberMessage", memberMessage);
		return "mem/mem/memberMessageUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("mem:memberMessage:edit")
	@RequestMapping(value = "save")
	public String save(MemberMessage memberMessage, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, memberMessage)){
			return add(memberMessage, model);
		}
		memberMessageService.save(memberMessage);
		addMessage(redirectAttributes, "保存会员消息成功");
		return "redirect:"+Global.getAdminPath()+"/mem/memberMessage/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("mem:memberMessage:edit")
	@RequestMapping(value = "delete")
	public String delete(MemberMessage memberMessage, RedirectAttributes redirectAttributes) {
		memberMessageService.delete(memberMessage);
		addMessage(redirectAttributes, "删除会员消息成功");
		return "redirect:"+Global.getAdminPath()+"/mem/memberMessage/?repage";
	}

}