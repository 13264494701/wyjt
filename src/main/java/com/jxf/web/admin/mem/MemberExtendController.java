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

import com.jxf.mem.entity.MemberExtend;
import com.jxf.mem.service.MemberExtendService;
import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.web.admin.sys.BaseController;

/**
 * 会员扩展信息Controller
 * @author XIAORONGDIAN
 * @version 2018-10-13
 */
@Controller
@RequestMapping(value = "${adminPath}/mem/memberExtend")
public class MemberExtendController extends BaseController {

	@Autowired
	private MemberExtendService memberExtendService;
	
	@ModelAttribute
	public MemberExtend get(@RequestParam(required=false) Long id) {
		MemberExtend entity = null;
		if (id!=null){
			entity = memberExtendService.get(id);
		}
		if (entity == null){
			entity = new MemberExtend();
		}
		return entity;
	}
	
	@RequiresPermissions("mem:memberExtend:view")
	@RequestMapping(value = {"list", ""})
	public String list(MemberExtend memberExtend, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MemberExtend> page = memberExtendService.findPage(new Page<MemberExtend>(request, response), memberExtend); 
		model.addAttribute("page", page);
		return "mem/mem/memberExtendList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("mem:memberExtend:view")
	@RequestMapping(value = "add")
	public String add(MemberExtend memberExtend, Model model) {
		model.addAttribute("memberExtend", memberExtend);
		return "mem/mem/memberExtendAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("mem:memberExtend:view")
	@RequestMapping(value = "query")
	public String query(MemberExtend memberExtend, Model model) {
		model.addAttribute("memberExtend", memberExtend);
		return "mem/mem/memberExtendQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("mem:memberExtend:view")
	@RequestMapping(value = "update")
	public String update(MemberExtend memberExtend, Model model) {
		model.addAttribute("memberExtend", memberExtend);
		return "mem/mem/memberExtendUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("mem:memberExtend:edit")
	@RequestMapping(value = "save")
	public String save(MemberExtend memberExtend, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, memberExtend)){
			return add(memberExtend, model);
		}
		memberExtendService.save(memberExtend);
		addMessage(redirectAttributes, "保存会员扩展信息成功");
		return "redirect:"+Global.getAdminPath()+"/mem/memberExtend/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("mem:memberExtend:edit")
	@RequestMapping(value = "delete")
	public String delete(MemberExtend memberExtend, RedirectAttributes redirectAttributes) {
		memberExtendService.delete(memberExtend);
		addMessage(redirectAttributes, "删除会员扩展信息成功");
		return "redirect:"+Global.getAdminPath()+"/mem/memberExtend/?repage";
	}

}