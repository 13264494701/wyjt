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

import com.jxf.mem.entity.MemberPoint;
import com.jxf.mem.entity.MemberPointDetail;
import com.jxf.mem.service.MemberPointDetailService;
import com.jxf.mem.service.MemberPointService;
import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.web.admin.sys.BaseController;

/**
 * 积分明细Controller
 * @author zhj
 * @version 2016-05-13
 */
@Controller
@RequestMapping(value = "${adminPath}/memberPointDetail")
public class MemberPointDetailController extends BaseController {

	@Autowired
	private MemberPointService memberPointService;
	@Autowired
	private MemberPointDetailService memberPointDetailService;
	
	@ModelAttribute
	public MemberPointDetail get(@RequestParam(required=false) Long id) {
		MemberPointDetail entity = null;
		if (id!=null){
			entity = memberPointDetailService.get(id);
		}
		if (entity == null){
			entity = new MemberPointDetail();
		}
		return entity;
	}
	
	@RequiresPermissions("mem:member:view")
	@RequestMapping(value = {"list", ""})
	public String list(MemberPointDetail memberPointDetail, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MemberPointDetail> page = memberPointDetailService.findPage(new Page<MemberPointDetail>(request, response), memberPointDetail); 
		MemberPoint memberPoint = memberPointService.getByMember(memberPointDetail.getMember());
		model.addAttribute("memberPoint", memberPoint);
		model.addAttribute("page", page);
		return "mem/point/memberPointDetailList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("mem:member:view")
	@RequestMapping(value = "add")
	public String add(MemberPointDetail memberPointDetail, Model model) {
		model.addAttribute("memberPointDetail", memberPointDetail);
		return "mem/memberPointDetailAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("mem:member:view")
	@RequestMapping(value = "query")
	public String query(MemberPointDetail memberPointDetail, Model model) {
		model.addAttribute("memberPointDetail", memberPointDetail);
		return "mem/memberPointDetailQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("mem:member:view")
	@RequestMapping(value = "update")
	public String update(MemberPointDetail memberPointDetail, Model model) {
		model.addAttribute("memberPointDetail", memberPointDetail);
		return "mem/memberPointDetailUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("mem:member:edit")
	@RequestMapping(value = "save")
	public String save(MemberPointDetail memberPointDetail, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, memberPointDetail)){
			return add(memberPointDetail, model);
		}
		memberPointDetailService.save(memberPointDetail);
		addMessage(redirectAttributes, "保存积分明细成功");
		return "redirect:"+Global.getAdminPath()+"/memberPointDetail/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("mem:member:edit")
	@RequestMapping(value = "delete")
	public String delete(MemberPointDetail memberPointDetail, RedirectAttributes redirectAttributes) {
		memberPointDetailService.delete(memberPointDetail);
		addMessage(redirectAttributes, "删除积分明细成功");
		return "redirect:"+Global.getAdminPath()+"/memberPointDetail/?repage";
	}

}