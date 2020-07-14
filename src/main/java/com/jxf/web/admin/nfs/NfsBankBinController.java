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

import com.jxf.nfs.entity.NfsBankBin;
import com.jxf.nfs.service.NfsBankBinService;
import com.jxf.nfs.service.NfsBankInfoService;
import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.web.admin.sys.BaseController;

/**
 * 卡BINController
 * @author wo
 * @version 2018-09-29
 */
@Controller
@RequestMapping(value = "${adminPath}/nfsBankBin")
public class NfsBankBinController extends BaseController {

	@Autowired
	private NfsBankBinService nfsBankBinService;
	@Autowired
	private NfsBankInfoService nfsBankService;
	
	@ModelAttribute
	public NfsBankBin get(@RequestParam(required=false) Long id) {
		NfsBankBin entity = null;
		if (id!=null){
			entity = nfsBankBinService.get(id);
		}
		if (entity == null){
			entity = new NfsBankBin();
		}
		return entity;
	}
	
	@RequiresPermissions("bank:nfsBankBin:view")
	@RequestMapping(value = {"list", ""})
	public String list(NfsBankBin nfsBankBin, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<NfsBankBin> page = new Page<NfsBankBin>(request, response);
		page.setPageSize(10);
		page = nfsBankBinService.findPage(page, nfsBankBin); 
		model.addAttribute("page", page);
		model.addAttribute("bank", nfsBankService.get(nfsBankBin.getBank()));
		return "admin/nfs/bank/cardBin/nfsBankBinList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("bank:nfsBankBin:view")
	@RequestMapping(value = "add")
	public String add(NfsBankBin nfsBankBin, Model model) {
		nfsBankBin.setBank(nfsBankService.get(nfsBankBin.getBank()));
		model.addAttribute("nfsBankBin", nfsBankBin);
		return "admin/nfs/bank/cardBin/nfsBankBinAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("bank:nfsBankBin:view")
	@RequestMapping(value = "query")
	public String query(NfsBankBin nfsBankBin, Model model) {
		nfsBankBin.setBank(nfsBankService.get(nfsBankBin.getBank()));
		model.addAttribute("nfsBankBin", nfsBankBin);
		return "admin/nfs/bank/cardBin/nfsBankBinQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("bank:nfsBankBin:view")
	@RequestMapping(value = "update")
	public String update(NfsBankBin nfsBankBin, Model model) {
		nfsBankBin.setBank(nfsBankService.get(nfsBankBin.getBank()));
		model.addAttribute("nfsBankBin", nfsBankBin);
		return "admin/nfs/bank/cardBin/nfsBankBinUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("bank:nfsBankBin:edit")
	@RequestMapping(value = "save")
	public String save(NfsBankBin nfsBankBin, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, nfsBankBin)){
			return add(nfsBankBin, model);
		}
		nfsBankBinService.save(nfsBankBin);
		addMessage(redirectAttributes, "保存卡BIN成功");
		return "redirect:"+Global.getAdminPath()+"/nfsBankBin/?repage&bank.id="+nfsBankBin.getBank().getId();
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("bank:nfsBankBin:edit")
	@RequestMapping(value = "delete")
	public String delete(NfsBankBin nfsBankBin, RedirectAttributes redirectAttributes) {
		nfsBankBinService.delete(nfsBankBin);
		addMessage(redirectAttributes, "删除卡BIN成功");
		return "redirect:"+Global.getAdminPath()+"/nfsBankBin/?repage&bank.id="+nfsBankBin.getBank().getId();
	}

}