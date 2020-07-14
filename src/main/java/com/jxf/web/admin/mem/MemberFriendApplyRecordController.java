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

import com.jxf.mem.entity.MemberFriendApply;
import com.jxf.mem.service.MemberFriendApplyService;
import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.web.admin.sys.BaseController;

/**
 * 好友申请记录Controller
 * @author XIAORONGDIAN
 * @version 2018-10-30
 */
@Controller
@RequestMapping(value = "${adminPath}/mem/memberFriendApplyRecord")
public class MemberFriendApplyRecordController extends BaseController {

	@Autowired
	private MemberFriendApplyService memberFriendApplyRecordService;
	
	@ModelAttribute
	public MemberFriendApply get(@RequestParam(required=false) Long id) {
		MemberFriendApply entity = null;
		if (id!=null){
			entity = memberFriendApplyRecordService.get(id);
		}
		if (entity == null){
			entity = new MemberFriendApply();
		}
		return entity;
	}
	
	@RequiresPermissions("mem:memberFriendApplyRecord:view")
	@RequestMapping(value = {"list", ""})
	public String list(MemberFriendApply memberFriendApplyRecord, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MemberFriendApply> page = memberFriendApplyRecordService.findPage(new Page<MemberFriendApply>(request, response), memberFriendApplyRecord); 
		model.addAttribute("page", page);
		return "mem/mem/memberFriendApplyRecordList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("mem:memberFriendApplyRecord:view")
	@RequestMapping(value = "add")
	public String add(MemberFriendApply memberFriendApplyRecord, Model model) {
		model.addAttribute("memberFriendApplyRecord", memberFriendApplyRecord);
		return "mem/mem/memberFriendApplyRecordAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("mem:memberFriendApplyRecord:view")
	@RequestMapping(value = "query")
	public String query(MemberFriendApply memberFriendApplyRecord, Model model) {
		model.addAttribute("memberFriendApplyRecord", memberFriendApplyRecord);
		return "mem/mem/memberFriendApplyRecordQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("mem:memberFriendApplyRecord:view")
	@RequestMapping(value = "update")
	public String update(MemberFriendApply memberFriendApplyRecord, Model model) {
		model.addAttribute("memberFriendApplyRecord", memberFriendApplyRecord);
		return "mem/mem/memberFriendApplyRecordUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("mem:memberFriendApplyRecord:edit")
	@RequestMapping(value = "save")
	public String save(MemberFriendApply memberFriendApplyRecord, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, memberFriendApplyRecord)){
			return add(memberFriendApplyRecord, model);
		}
		memberFriendApplyRecordService.save(memberFriendApplyRecord);
		addMessage(redirectAttributes, "保存好友申请成功");
		return "redirect:"+Global.getAdminPath()+"/mem/memberFriendApplyRecord/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("mem:memberFriendApplyRecord:edit")
	@RequestMapping(value = "delete")
	public String delete(MemberFriendApply memberFriendApplyRecord, RedirectAttributes redirectAttributes) {
		memberFriendApplyRecordService.delete(memberFriendApplyRecord);
		addMessage(redirectAttributes, "删除好友申请成功");
		return "redirect:"+Global.getAdminPath()+"/mem/memberFriendApplyRecord/?repage";
	}

}