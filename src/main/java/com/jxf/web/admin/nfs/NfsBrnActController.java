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

import com.jxf.nfs.entity.NfsBrnAct;
import com.jxf.nfs.service.NfsBrnActService;
import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.brn.service.BrnService;
import com.jxf.web.admin.sys.BaseController;

/**
 * 机构账户Controller
 * @author jinxinfu
 * @version 2018-06-29
 */
@Controller
@RequestMapping(value = "${adminPath}/nfsBrnAct")
public class NfsBrnActController extends BaseController {

	@Autowired
	private BrnService brnService;
	@Autowired
	private NfsBrnActService brnActService;
	
	@ModelAttribute
	public NfsBrnAct get(@RequestParam(required=false) Long id) {
		NfsBrnAct entity = null;
		if (id!=null){
			entity = brnActService.get(id);
		}
		if (entity == null){
			entity = new NfsBrnAct();
		}
		return entity;
	}
	
	@RequiresPermissions("brn:nfsBrnAct:view")
	@RequestMapping(value = {"list", ""})
	public String list(NfsBrnAct nfsBrnAct, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<NfsBrnAct> page = brnActService.findPage(new Page<NfsBrnAct>(request, response), nfsBrnAct); 
		model.addAttribute("page", page);
		return "admin/nfs/brnAct/nfsBrnActList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("brn:nfsBrnAct:view")
	@RequestMapping(value = "add")
	public String add(NfsBrnAct nfsBrnAct, Model model) {
		model.addAttribute("nfsBrnAct", nfsBrnAct);
		return "nfs/brnAct/nfsBrnActAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("brn:nfsBrnAct:view")
	@RequestMapping(value = "query")
	public String query(NfsBrnAct nfsBrnAct, Model model) {
		model.addAttribute("nfsBrnAct", nfsBrnAct);
		return "brn/nfsBrnActQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("brn:nfsBrnAct:view")
	@RequestMapping(value = "update")
	public String update(NfsBrnAct nfsBrnAct, Model model) {
		model.addAttribute("nfsBrnAct", nfsBrnAct);
		return "nfs/brnAct/nfsBrnActUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("brn:nfsBrnAct:edit")
	@RequestMapping(value = "save")
	public String save(NfsBrnAct nfsBrnAct, Model model, RedirectAttributes redirectAttributes) {

		if (!beanValidator(model, nfsBrnAct)){
			return add(nfsBrnAct, model);
		}		
		nfsBrnAct.setCompany(brnService.get(nfsBrnAct.getCompany()));
		brnActService.save(nfsBrnAct);
		addMessage(redirectAttributes, "保存机构账户成功");
		return "redirect:"+Global.getAdminPath()+"/nfsBrnAct/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("brn:nfsBrnAct:edit")
	@RequestMapping(value = "delete")
	public String delete(NfsBrnAct nfsBrnAct, RedirectAttributes redirectAttributes) {
		brnActService.delete(nfsBrnAct);
		addMessage(redirectAttributes, "删除机构账户成功");
		return "redirect:"+Global.getAdminPath()+"/nfsBrnAct/?repage";
	}

}