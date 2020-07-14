package com.jxf.web.ufang;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jxf.ufang.entity.UfangUser;
import com.jxf.ufang.util.UfangUserUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.ufang.entity.UfangRchgRecord;
import com.jxf.ufang.service.UfangRchgRecordService;

/**
 * 优放充值记录Controller
 * @author Administrator
 * @version 2018-12-07
 */
@Controller
@RequestMapping(value = "${ufangPath}/recharge")
public class UfangRchgRecordController extends UfangBaseController {

	@Autowired
	private UfangRchgRecordService ufangRchgRecordService;
	
	@ModelAttribute
	public UfangRchgRecord get(@RequestParam(required=false) Long id) {
		UfangRchgRecord entity = null;
		if (id!=null){
			entity = ufangRchgRecordService.get(id);
		}
		if (entity == null){
			entity = new UfangRchgRecord();
		}
		return entity;
	}
	
	@RequiresPermissions("recharge:ufangRchgRecord:view")
	@RequestMapping(value = {"list", ""})
	public String list(UfangRchgRecord ufangRchgRecord, HttpServletRequest request, HttpServletResponse response, Model model) {
		UfangUser ufangUser = UfangUserUtils.getUser();
		UfangUser user = new UfangUser();
		user.setEmpNo(ufangUser.getEmpNo().substring(0,4));
		ufangRchgRecord.setUser(user);
		Page<UfangRchgRecord> page = ufangRchgRecordService.findPage(new Page<UfangRchgRecord>(request, response), ufangRchgRecord); 
		model.addAttribute("page", page);
		return "ufang/recharge/ufangRchgRecordList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("recharge:ufangRchgRecord:view")
	@RequestMapping(value = "add")
	public String add(UfangRchgRecord ufangRchgRecord, Model model) {
		model.addAttribute("ufangRchgRecord", ufangRchgRecord);
		return "ufang/recharge/ufangRchgRecordAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("recharge:ufangRchgRecord:view")
	@RequestMapping(value = "query")
	public String query(UfangRchgRecord ufangRchgRecord, Model model) {
		model.addAttribute("ufangRchgRecord", ufangRchgRecord);
		return "ufang/recharge/ufangRchgRecordQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("recharge:ufangRchgRecord:view")
	@RequestMapping(value = "update")
	public String update(UfangRchgRecord ufangRchgRecord, Model model) {
		model.addAttribute("ufangRchgRecord", ufangRchgRecord);
		return "ufang/recharge/ufangRchgRecordUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("recharge:ufangRchgRecord:edit")
	@RequestMapping(value = "save")
	public String save(UfangRchgRecord ufangRchgRecord, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, ufangRchgRecord)){
			return add(ufangRchgRecord, model);
		}
		ufangRchgRecordService.save(ufangRchgRecord);
		addMessage(redirectAttributes, "保存优放充值记录成功");
		return "redirect:"+Global.getAdminPath()+"/recharge/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("recharge:ufangRchgRecord:edit")
	@RequestMapping(value = "delete")
	public String delete(UfangRchgRecord ufangRchgRecord, RedirectAttributes redirectAttributes) {
		ufangRchgRecordService.delete(ufangRchgRecord);
		addMessage(redirectAttributes, "删除优放充值记录成功");
		return "redirect:"+Global.getAdminPath()+"/recharge/?repage";
	}

}