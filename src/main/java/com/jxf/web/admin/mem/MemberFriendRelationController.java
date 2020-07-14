package com.jxf.web.admin.mem;

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

import com.jxf.mem.entity.MemberFriendRelation;
import com.jxf.mem.service.MemberFriendRelationService;
import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.web.admin.sys.BaseController;

/**
 * 好友关系表Controller
 * @author XIAORONGDIAN
 * @version 2018-10-11
 */
@Controller
@RequestMapping(value = "${adminPath}/memberFriendRelation")
public class MemberFriendRelationController extends BaseController {

	@Autowired
	private MemberFriendRelationService memberFriendRelationService;
	
	@ModelAttribute
	public MemberFriendRelation get(@RequestParam(required=false) Long id) {
		MemberFriendRelation entity = null;
		if (id!=null){
			entity = memberFriendRelationService.get(id);
		}
		if (entity == null){
			entity = new MemberFriendRelation();
		}
		return entity;
	}
	
	@RequiresPermissions("mem:member:view")
	@RequestMapping(value = {"list", ""})
	public String list(MemberFriendRelation memberFriendRelation, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MemberFriendRelation> page = memberFriendRelationService.findPage(new Page<MemberFriendRelation>(request, response), memberFriendRelation); 
		model.addAttribute("page", page);
		model.addAttribute("member", memberFriendRelation.getMember());
		return "admin/mem/friend/memberFriendRelationList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("mem:member:view")
	@RequestMapping(value = "add")
	public String add(MemberFriendRelation memberFriendRelation, Model model) {
		model.addAttribute("memberFriendRelation", memberFriendRelation);
		return "admin/mem/friend/memberFriendRelationAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("mem:member:view")
	@RequestMapping(value = "query")
	public String query(MemberFriendRelation memberFriendRelation, Model model) {
		model.addAttribute("memberFriendRelation", memberFriendRelation);
		return "admin/mem/friend/memberFriendRelationQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("mem:member:view")
	@RequestMapping(value = "update")
	public String update(MemberFriendRelation memberFriendRelation, Model model) {
		model.addAttribute("memberFriendRelation", memberFriendRelation);
		return "admin/mem/friend/memberFriendRelationUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("mem:member:edit")
	@RequestMapping(value = "save")
	public String save(MemberFriendRelation memberFriendRelation, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, memberFriendRelation)){
			return add(memberFriendRelation, model);
		}
		memberFriendRelationService.save(memberFriendRelation);
		addMessage(redirectAttributes, "保存好友关系成功");
		return "redirect:"+Global.getAdminPath()+"/memberFriendRelation/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("mem:member:edit")
	@RequestMapping(value = "delete")
	public String delete(MemberFriendRelation memberFriendRelation, RedirectAttributes redirectAttributes) {
		memberFriendRelationService.delete(memberFriendRelation);
		addMessage(redirectAttributes, "删除好友关系成功");
		return "redirect:"+Global.getAdminPath()+"/memberFriendRelation/?repage";
	}

}