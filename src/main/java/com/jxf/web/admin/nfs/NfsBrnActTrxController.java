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

import com.jxf.nfs.entity.NfsBrnActTrx;
import com.jxf.nfs.service.NfsBrnActTrxService;
import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.web.admin.sys.BaseController;


/**
 * 账户交易Controller
 * @author jinxinfu
 * @version 2018-07-01
 */
@Controller
@RequestMapping(value = "${adminPath}/nfsBrnActTrx")
public class NfsBrnActTrxController extends BaseController {

	@Autowired
	private NfsBrnActTrxService nfsBrnActTrxService;
	
	@ModelAttribute
	public NfsBrnActTrx get(@RequestParam(required=false) Long id) {
		NfsBrnActTrx entity = null;
		if (id!=null){
			entity = nfsBrnActTrxService.get(id);
		}
		if (entity == null){
			entity = new NfsBrnActTrx();
		}
		return entity;
	}
	
	@RequiresPermissions("trx:nfsBrnActTrx:view")
	@RequestMapping(value = {"list", ""})
	public String list(NfsBrnActTrx nfsBrnActTrx, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<NfsBrnActTrx> page = nfsBrnActTrxService.findPage(new Page<NfsBrnActTrx>(request, response), nfsBrnActTrx); 
		model.addAttribute("page", page);
		return "nfs/brnAct/trx/nfsBrnActTrxList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("trx:nfsBrnActTrx:view")
	@RequestMapping(value = "add")
	public String add(NfsBrnActTrx nfsBrnActTrx, Model model) {
		model.addAttribute("nfsBrnActTrx", nfsBrnActTrx);
		return "nfs/brnAct/trx/nfsBrnActTrxAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("trx:nfsBrnActTrx:view")
	@RequestMapping(value = "query")
	public String query(NfsBrnActTrx nfsBrnActTrx, Model model) {
		model.addAttribute("nfsBrnActTrx", nfsBrnActTrx);
		return "nfs/brnAct/trx/nfsBrnActTrxQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("trx:nfsBrnActTrx:view")
	@RequestMapping(value = "update")
	public String update(NfsBrnActTrx nfsBrnActTrx, Model model) {
		model.addAttribute("nfsBrnActTrx", nfsBrnActTrx);
		return "nfs/brnAct/trx/nfsBrnActTrxUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("trx:nfsBrnActTrx:edit")
	@RequestMapping(value = "save")
	public String save(NfsBrnActTrx nfsBrnActTrx, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, nfsBrnActTrx)){
			return add(nfsBrnActTrx, model);
		}
		nfsBrnActTrxService.save(nfsBrnActTrx);
		addMessage(redirectAttributes, "保存账户交易成功");
		return "redirect:"+Global.getAdminPath()+"/nfsBrnActTrx/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("trx:nfsBrnActTrx:edit")
	@RequestMapping(value = "delete")
	public String delete(NfsBrnActTrx nfsBrnActTrx, RedirectAttributes redirectAttributes) {
		nfsBrnActTrxService.delete(nfsBrnActTrx);
		addMessage(redirectAttributes, "删除账户交易成功");
		return "redirect:"+Global.getAdminPath()+"/nfsBrnActTrx/?repage";
	}
}