package com.jxf.web.admin.mem;

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

import com.jxf.mem.entity.MemberPoint;
import com.jxf.mem.entity.MemberPointRule;
import com.jxf.mem.service.MemberPointService;
import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.web.admin.sys.BaseController;

/**
 * 会员积分Controller
 * @author JINXINFU
 * @version 2016-04-25
 */
@Controller("adminMemberPointController")
@RequestMapping(value = "${adminPath}/memberPoint")
public class MemberPointController extends BaseController {

	@Autowired
	private MemberPointService memberPointService;
	
	@ModelAttribute
	public MemberPoint get(@RequestParam(required=false) Long id) {
		MemberPoint entity = null;
		if (id!=null){
			entity = memberPointService.get(id);
		}
		if (entity == null){
			entity = new MemberPoint();
		}
		return entity;
	}
	
	@RequiresPermissions("mem:member:view")
	@RequestMapping(value = {"list", "","point"})
	public String list(MemberPoint memberPoint, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MemberPoint> page = memberPointService.findPage(new Page<MemberPoint>(request, response), memberPoint); 
		model.addAttribute("page", page);
		return "mem/point/memberPointList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("mem:member:view")
	@RequestMapping(value = "add")
	public String add(MemberPoint memberPoint, Model model) {
		model.addAttribute("memberPoint", memberPoint);
		return "mem/point/memberPointAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("mem:member:view")
	@RequestMapping(value = "query")
	public String query(MemberPoint memberPoint, Model model) {
		model.addAttribute("memberPoint", memberPoint);
		return "mem/point/memberPointQuery";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("mem:member:edit")
	@RequestMapping(value = "save")
	public String save(MemberPoint memberPoint, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, memberPoint)){
			return add(memberPoint, model);
		}
		memberPointService.save(memberPoint);
		addMessage(redirectAttributes, "保存会员积分成功");
		return "redirect:"+Global.getAdminPath()+"/member/memberPoint/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("mem:member:edit")
	@RequestMapping(value = "delete")
	public String delete(MemberPoint memberPoint, RedirectAttributes redirectAttributes) {
		memberPointService.delete(memberPoint);
		addMessage(redirectAttributes, "删除会员积分成功");
		return "redirect:"+Global.getAdminPath()+"/member/memberPoint/?repage";
	}
	@RequiresPermissions("mem:member:edit")
	@RequestMapping(value = "update",method=RequestMethod.GET)
	public String update(MemberPoint memberPoint,Model model){
		memberPoint = memberPointService.getByMember(memberPoint.getMember());
		model.addAttribute("memberPoint", memberPoint);
		return "mem/point/memberPointUpdate";
	}
	
//	@RequiresPermissions("mem:member:edit")
//	@RequestMapping(value = "update",method=RequestMethod.POST)
//	public String update(MemberPoint memberPoint,String points,RedirectAttributes redirectAttributes){
//		memberPointService.updateMemberPoint(memberPoint.getMember(),Long.valueOf(points),MemberPointRule.Type.adjustment);
//		addMessage(redirectAttributes, "调整成功");
//		return "redirect:"+Global.getAdminPath()+"/memberPointDetail?repage&member.id="+memberPoint.getMember().getId();
//	}
}