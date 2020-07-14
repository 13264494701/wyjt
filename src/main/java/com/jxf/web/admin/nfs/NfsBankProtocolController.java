package com.jxf.web.admin.nfs;

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

import com.jxf.nfs.entity.NfsBankProtocol;
import com.jxf.nfs.service.NfsBankProtocolService;
import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.web.admin.sys.BaseController;

/**
 * 签约协议Controller
 * @author suhuimin
 * @version 2018-09-30
 */
@Controller
@RequestMapping(value = "${adminPath}/bank/nfsBankProtocol")
public class NfsBankProtocolController extends BaseController {

	@Autowired
	private NfsBankProtocolService nfsBankProtocolService;
	
	@ModelAttribute
	public NfsBankProtocol get(@RequestParam(required=false) Long id) {
		NfsBankProtocol entity = null;
		entity = nfsBankProtocolService.get(id);
		if (entity == null){
			entity = new NfsBankProtocol();
		}
		return entity;
	}
	
	@RequiresPermissions("bank:nfsBankProtocol:view")
	@RequestMapping(value = {"list", ""})
	public String list(NfsBankProtocol nfsBankProtocol, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<NfsBankProtocol> page = nfsBankProtocolService.findPage(new Page<NfsBankProtocol>(request, response), nfsBankProtocol); 
		model.addAttribute("page", page);
		return "nfs/bank/nfsBankProtocolList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("bank:nfsBankProtocol:view")
	@RequestMapping(value = "add")
	public String add(NfsBankProtocol nfsBankProtocol, Model model) {
		model.addAttribute("nfsBankProtocol", nfsBankProtocol);
		return "nfs/bank/nfsBankProtocolAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("bank:nfsBankProtocol:view")
	@RequestMapping(value = "query")
	public String query(NfsBankProtocol nfsBankProtocol, Model model) {
		model.addAttribute("nfsBankProtocol", nfsBankProtocol);
		return "nfs/bank/nfsBankProtocolQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("bank:nfsBankProtocol:view")
	@RequestMapping(value = "update")
	public String update(NfsBankProtocol nfsBankProtocol, Model model) {
		model.addAttribute("nfsBankProtocol", nfsBankProtocol);
		return "nfs/bank/nfsBankProtocolUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("bank:nfsBankProtocol:edit")
	@RequestMapping(value = "save")
	public String save(NfsBankProtocol nfsBankProtocol, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, nfsBankProtocol)){
			return add(nfsBankProtocol, model);
		}
		nfsBankProtocolService.save(nfsBankProtocol);
		addMessage(redirectAttributes, "保存签约协议成功");
		return "redirect:"+Global.getAdminPath()+"/bank/nfsBankProtocol/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("bank:nfsBankProtocol:edit")
	@RequestMapping(value = "delete")
	public String delete(NfsBankProtocol nfsBankProtocol, RedirectAttributes redirectAttributes) {
		nfsBankProtocolService.delete(nfsBankProtocol);
		addMessage(redirectAttributes, "删除签约协议成功");
		return "redirect:"+Global.getAdminPath()+"/bank/nfsBankProtocol/?repage";
	}
}