package com.jxf.web.admin.ufang;

import java.math.BigDecimal;

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

import com.jxf.mem.entity.MemberActTrx;
import com.jxf.svc.config.Global;
import com.jxf.svc.model.Message;
import com.jxf.svc.persistence.Page;
import com.jxf.ufang.entity.UfangUserAct;
import com.jxf.ufang.service.UfangUserActService;
import com.jxf.web.admin.sys.BaseController;

/**
 * 优放用户账户Controller
 * @author jinxinfu
 * @version 2018-06-29
 */
@Controller("adminUfangUserActController")
@RequestMapping(value = "${adminPath}/ufangUserAct")
public class UfangUserActController extends BaseController {


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
	
	@RequiresPermissions("user:ufangUserAct:view")
	@RequestMapping(value = {"list", ""})
	public String list(UfangUserAct ufangUserAct, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<UfangUserAct> page = userActService.findPage(new Page<UfangUserAct>(request, response), ufangUserAct); 
		model.addAttribute("page", page);
		return "admin/ufang/user/act/ufangUserActList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("user:ufangUserAct:view")
	@RequestMapping(value = "add")
	public String add(UfangUserAct ufangUserAct, Model model) {
		model.addAttribute("ufangUserAct", ufangUserAct);
		return "admin/ufang/user/act/ufangUserActAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("user:ufangUserAct:view")
	@RequestMapping(value = "query")
	public String query(UfangUserAct ufangUserAct, Model model) {
		model.addAttribute("ufangUserAct", ufangUserAct);
		return "admin/ufang/user/act/ufangUserActQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("user:ufangUserAct:view")
	@RequestMapping(value = "update")
	public String update(UfangUserAct ufangUserAct, Model model) {
		model.addAttribute("ufangUserAct", ufangUserAct);
		return "admin/ufang/user/act/ufangUserActUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("user:ufangUserAct:edit")
	@RequestMapping(value = "save")
	public String save(UfangUserAct ufangUserAct, Model model, RedirectAttributes redirectAttributes) {

		if (!beanValidator(model, ufangUserAct)){
			return add(ufangUserAct, model);
		}		
//		ufangUserAct.setUser(userService.get(ufangUserAct.getUser()));
		userActService.save(ufangUserAct);
		addMessage(redirectAttributes, "保存优放用户账户成功");
		return "redirect:"+Global.getAdminPath()+"/ufangUserAct/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("user:ufangUserAct:edit")
	@RequestMapping(value = "delete")
	public String delete(UfangUserAct ufangUserAct, RedirectAttributes redirectAttributes) {
		userActService.delete(ufangUserAct);
		addMessage(redirectAttributes, "删除优放用户账户成功");
		return "redirect:"+Global.getAdminPath()+"/ufangUserAct/?repage";
	}
	 /**
	 * 修改账户余额
	 */
	@RequiresPermissions("user:ufangUserAct:edit")
	@RequestMapping(value = "updateActBal")
	@ResponseBody
	public Message updateActBal(UfangUserAct ufangUserAct, BigDecimal trxAmt, Model model, RedirectAttributes redirectAttributes) {
				
//		userActService.updateActBal(ufangUserAct,MemberActTrx.Type.manual,trxAmt);
		
		return Message.success("加/减款成功");
	}
		

}