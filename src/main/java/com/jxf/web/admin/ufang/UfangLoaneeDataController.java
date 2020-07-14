package com.jxf.web.admin.ufang;

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

import com.jxf.nfs.constant.TrxRuleConstant;
import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.ufang.entity.UfangLoaneeData;
import com.jxf.ufang.service.UfangLoaneeDataService;
import com.jxf.web.admin.sys.BaseController;

/**
 * 流量管理Controller
 * @author wo
 * @version 2018-11-24
 */
@Controller("adminUfangLoaneeDataController")
@RequestMapping(value = "${adminPath}/ufangLoaneeData")
public class UfangLoaneeDataController extends BaseController {

	@Autowired
	private UfangLoaneeDataService ufangLoaneeDataService;
	
	@ModelAttribute
	public UfangLoaneeData get(@RequestParam(required=false) Long id) {
		UfangLoaneeData entity = null;
		if (id!=null){
			entity = ufangLoaneeDataService.get(id);
		}
		if (entity == null){
			entity = new UfangLoaneeData();
		}
		return entity;
	}
	
	@RequiresPermissions("data:ufangLoaneeData:view")
	@RequestMapping(value = {"list", ""})
	public String list(UfangLoaneeData ufangLoaneeData, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<UfangLoaneeData> page = ufangLoaneeDataService.findPage(new Page<UfangLoaneeData>(request, response), ufangLoaneeData); 
		model.addAttribute("page", page);
		if(ufangLoaneeData.getChannel().equals(UfangLoaneeData.Channel.weixin)) {
			model.addAttribute("dataPrice", TrxRuleConstant.UFANG_DATA_LESS_PRICE);
		}else if(ufangLoaneeData.getChannel().equals(UfangLoaneeData.Channel.ufang)) {
			model.addAttribute("dataPrice", TrxRuleConstant.UFANG_DATA_MORE_PRICE);
		}else {
			model.addAttribute("dataPrice", TrxRuleConstant.UFANG_DATA_LESS_PRICE);
		}
		model.addAttribute("channel", ufangLoaneeData.getChannel());
		return "admin/ufang/data/ufangLoaneeDataList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("data:ufangLoaneeData:view")
	@RequestMapping(value = "add")
	public String add(UfangLoaneeData ufangLoaneeData, Model model) {
		model.addAttribute("ufangLoaneeData", ufangLoaneeData);
		return "admin/ufang/data/ufangLoaneeDataAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("data:ufangLoaneeData:view")
	@RequestMapping(value = "query")
	public String query(UfangLoaneeData ufangLoaneeData, Model model) {
		model.addAttribute("ufangLoaneeData", ufangLoaneeData);
		return "admin/ufang/data/ufangLoaneeDataQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("data:ufangLoaneeData:view")
	@RequestMapping(value = "update")
	public String update(UfangLoaneeData ufangLoaneeData, Model model) {
		model.addAttribute("ufangLoaneeData", ufangLoaneeData);
		return "admin/ufang/data/ufangLoaneeDataUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("data:ufangLoaneeData:edit")
	@RequestMapping(value = "save")
	public String save(UfangLoaneeData ufangLoaneeData, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, ufangLoaneeData)){
			return add(ufangLoaneeData, model);
		}
		ufangLoaneeDataService.save(ufangLoaneeData);
		addMessage(redirectAttributes, "保存流量管理成功");
		return "redirect:"+Global.getAdminPath()+"/ufangLoaneeData/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("data:ufangLoaneeData:edit")
	@RequestMapping(value = "delete")
	public String delete(UfangLoaneeData ufangLoaneeData, RedirectAttributes redirectAttributes) {
		ufangLoaneeDataService.delete(ufangLoaneeData);
		addMessage(redirectAttributes, "删除流量管理成功");
		return "redirect:"+Global.getAdminPath()+"/ufangLoaneeData/?repage";
	}

}