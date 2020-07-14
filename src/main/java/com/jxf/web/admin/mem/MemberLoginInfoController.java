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

import com.jxf.mem.entity.MemberLoginInfo;
import com.jxf.mem.service.MemberLoginInfoService;
import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.web.admin.sys.BaseController;

/**
 * 会员登陆信息Controller
 * @author gaobo
 * @version 2019-05-31
 */
@Controller
@RequestMapping(value = "${adminPath}/mem/memMemberLoginInfo")
public class MemberLoginInfoController extends BaseController {

	@Autowired
	private MemberLoginInfoService memMemberLoginInfoService;
	
	@ModelAttribute
	public MemberLoginInfo get(@RequestParam(required=false) Long id) {
		MemberLoginInfo entity = null;
		if (id!=null){
			entity = memMemberLoginInfoService.get(id);
		}
		if (entity == null){
			entity = new MemberLoginInfo();
		}
		return entity;
	}
	
	@RequiresPermissions("mem:memMemberLoginInfo:view")
	@RequestMapping(value = {"list", ""})
	public String list(MemberLoginInfo memMemberLoginInfo, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MemberLoginInfo> page = memMemberLoginInfoService.findPage(new Page<MemberLoginInfo>(request, response), memMemberLoginInfo); 
		model.addAttribute("page", page);
		return "mem/mem/memMemberLoginInfoList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("mem:memMemberLoginInfo:view")
	@RequestMapping(value = "add")
	public String add(MemberLoginInfo memMemberLoginInfo, Model model) {
		model.addAttribute("memMemberLoginInfo", memMemberLoginInfo);
		return "mem/mem/memMemberLoginInfoAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("mem:memMemberLoginInfo:view")
	@RequestMapping(value = "query")
	public String query(MemberLoginInfo memMemberLoginInfo, Model model) {
		model.addAttribute("memMemberLoginInfo", memMemberLoginInfo);
		return "mem/mem/memMemberLoginInfoQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("mem:memMemberLoginInfo:view")
	@RequestMapping(value = "update")
	public String update(MemberLoginInfo memMemberLoginInfo, Model model) {
		model.addAttribute("memMemberLoginInfo", memMemberLoginInfo);
		return "mem/mem/memMemberLoginInfoUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("mem:memMemberLoginInfo:edit")
	@RequestMapping(value = "save")
	public String save(MemberLoginInfo memMemberLoginInfo, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, memMemberLoginInfo)){
			return add(memMemberLoginInfo, model);
		}
		memMemberLoginInfoService.save(memMemberLoginInfo);
		addMessage(redirectAttributes, "保存会员登陆信息成功");
		return "redirect:"+Global.getAdminPath()+"/mem/memMemberLoginInfo/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("mem:memMemberLoginInfo:edit")
	@RequestMapping(value = "delete")
	public String delete(MemberLoginInfo memMemberLoginInfo, RedirectAttributes redirectAttributes) {
		memMemberLoginInfoService.delete(memMemberLoginInfo);
		addMessage(redirectAttributes, "删除会员登陆信息成功");
		return "redirect:"+Global.getAdminPath()+"/mem/memMemberLoginInfo/?repage";
	}

}