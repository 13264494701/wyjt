package com.jxf.web.ufang;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jxf.ufang.util.UfangUserUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jxf.svc.persistence.Page;
import com.jxf.ufang.entity.UfangUserAct;
import com.jxf.ufang.service.UfangUserActService;


/**
 * 优放用户账户Controller
 * @author jinxinfu
 * @version 2018-06-29
 */
@Controller("ufangUserActController")
@RequestMapping(value = "${ufangPath}/userAct")
public class UfangUserActController extends UfangBaseController {


	@Autowired
	private UfangUserActService userActService;
	
	@ModelAttribute
	public UfangUserAct get(@RequestParam(required=false) Long id) {
		UfangUserAct entity = null;
		if (id!=null){
			entity = userActService.get(id);
		}
		if (entity == null){
			entity = new UfangUserAct();
		}
		return entity;
	}
	
	@RequiresPermissions("ufang:userAct:view")
	@RequestMapping(value = {"list", ""})
	public String list(UfangUserAct ufangUserAct, HttpServletRequest request, HttpServletResponse response, Model model) {
		ufangUserAct.getSqlMap().put("dsf", UfangUserUtils.dataScopeFilter("e", ""));
		Page<UfangUserAct> page = userActService.findPage(new Page<UfangUserAct>(request, response), ufangUserAct); 
		model.addAttribute("page", page);
		return "ufang/user/act/userActList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("ufang:userAct:view")
	@RequestMapping(value = "add")
	public String add(UfangUserAct ufangUserAct, Model model) {
		model.addAttribute("ufangUserAct", ufangUserAct);
		return "ufang/user/act/userActAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("ufang:userAct:view")
	@RequestMapping(value = "query")
	public String query(UfangUserAct ufangUserAct, Model model) {
		model.addAttribute("ufangUserAct", ufangUserAct);
		return "ufang/user/act/userActQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("ufang:userAct:view")
	@RequestMapping(value = "update")
	public String update(UfangUserAct ufangUserAct, Model model) {
		model.addAttribute("ufangUserAct", ufangUserAct);
		return "ufang/user/act/userActUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("ufang:userAct:edit")
	@RequestMapping(value = "save")
	public String save(UfangUserAct ufangUserAct, Model model, RedirectAttributes redirectAttributes) {

		if (!beanValidator(model, ufangUserAct)){
			return add(ufangUserAct, model);
		}		
//		ufangUserAct.setUser(userService.get(ufangUserAct.getUser()));
		userActService.save(ufangUserAct);
		addMessage(redirectAttributes, "保存优放用户账户成功");
		return "redirect:"+ufangPath+"/userAct/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("ufang:userAct:edit")
	@RequestMapping(value = "delete")
	public String delete(UfangUserAct ufangUserAct, RedirectAttributes redirectAttributes) {
		userActService.delete(ufangUserAct);
		addMessage(redirectAttributes, "删除优放用户账户成功");
		return "redirect:"+ufangPath+"/userAct/?repage";
	}
		
}