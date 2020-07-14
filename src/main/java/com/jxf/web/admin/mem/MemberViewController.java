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

import com.jxf.mem.entity.MemberView;
import com.jxf.mem.service.MemberViewService;
import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.web.admin.sys.BaseController;


/**
 * 会员视图Controller
 * @author wo
 * @version 2019-01-20
 */
@Controller
@RequestMapping(value = "${adminPath}/view/memberView")
public class MemberViewController extends BaseController {

	@Autowired
	private MemberViewService memberViewService;
	
	@ModelAttribute
	public MemberView get(@RequestParam(required=false) Long id) {
		MemberView entity = null;
		if (id!=null){
			entity = memberViewService.get(id);
		}
		if (entity == null){
			entity = new MemberView();
		}
		return entity;
	}
	
	@RequiresPermissions("view:memberView:view")
	@RequestMapping(value = {"list", ""})
	public String list(MemberView memberView, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MemberView> page = memberViewService.findPage(new Page<MemberView>(request, response), memberView); 
		model.addAttribute("page", page);
		return "admin/mem/view/memberViewList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("view:memberView:view")
	@RequestMapping(value = "add")
	public String add(MemberView memberView, Model model) {
		model.addAttribute("memberView", memberView);
		return "admin/mem/view/memberViewAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("view:memberView:view")
	@RequestMapping(value = "query")
	public String query(MemberView memberView, Model model) {
		model.addAttribute("memberView", memberView);
		return "admin/mem/view/memberViewQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("view:memberView:view")
	@RequestMapping(value = "update")
	public String update(MemberView memberView, Model model) {
		model.addAttribute("memberView", memberView);
		return "admin/mem/view/memberViewUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("view:memberView:edit")
	@RequestMapping(value = "save")
	public String save(MemberView memberView, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, memberView)){
			return add(memberView, model);
		}
		memberViewService.save(memberView);
		addMessage(redirectAttributes, "保存会员视图成功");
		return "redirect:"+Global.getAdminPath()+"/view/memberView/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("view:memberView:edit")
	@RequestMapping(value = "delete")
	public String delete(MemberView memberView, RedirectAttributes redirectAttributes) {
		memberViewService.delete(memberView);
		addMessage(redirectAttributes, "删除会员视图成功");
		return "redirect:"+Global.getAdminPath()+"/view/memberView/?repage";
	}

	/**
	 * 刷新会员视图
	 */
	@RequiresPermissions("view:memberView:edit")
	@RequestMapping(value = "refresh")
	public String refresh(RedirectAttributes redirectAttributes) {
		memberViewService.refresh();
		addMessage(redirectAttributes, "刷新会员视图成功");
		return "redirect:"+Global.getAdminPath()+"/view/memberView/?repage";
	}
	
	/**
	 * 会员视图
	 */
	@RequiresPermissions("view:memberView:edit")
	@RequestMapping(value = "take")
	public String take(MemberView memberView,RedirectAttributes redirectAttributes) {
		
	    if(memberView.getStatus().equals(MemberView.Status.take)) {
	    	addMessage(redirectAttributes, "会员视图已接管");
	    }else {
	    	memberViewService.take(memberView);	
	    	addMessage(redirectAttributes, "接管会员视图成功");
	    }	
		return "redirect:"+Global.getAdminPath()+"/view/memberView/?repage";
	}
	
	/**
	 * 会员视图
	 */
	@RequiresPermissions("view:memberView:edit")
	@RequestMapping(value = "reset")
	public String reset(MemberView memberView,RedirectAttributes redirectAttributes) {
	    if(memberView.getStatus().equals(MemberView.Status.take)) {	    	
	    	memberViewService.reset(memberView);	
	    }
	    addMessage(redirectAttributes, "复位会员视图成功");
		return "redirect:"+Global.getAdminPath()+"/view/memberView/?repage";
	}
	
}