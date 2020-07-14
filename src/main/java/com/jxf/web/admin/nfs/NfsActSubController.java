package com.jxf.web.admin.nfs;

import java.util.List;

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

import com.jxf.nfs.entity.NfsActSub;
import com.jxf.nfs.service.NfsActSubService;
import com.jxf.nfs.utils.NfsUtils;
import com.jxf.svc.config.Global;
import com.jxf.svc.model.AjaxRsp;
import com.jxf.svc.persistence.Page;
import com.jxf.web.admin.sys.BaseController;


/**
 * 账户科目Controller
 * @author wo
 * @version 2018-09-18
 */
@Controller
@RequestMapping(value = "${adminPath}/nfsActSub")
public class NfsActSubController extends BaseController {

	@Autowired
	private NfsActSubService nfsActSubService;
	
	@ModelAttribute
	public NfsActSub get(@RequestParam(required=false) Long id) {
		NfsActSub entity = null;
		if (id!=null){
			entity = nfsActSubService.get(id);
		}
		if (entity == null){
			entity = new NfsActSub();
		}
		return entity;
	}
	
	@RequiresPermissions("sub:nfsActSub:view")
	@RequestMapping(value = {"list", ""})
	public String list(NfsActSub nfsActSub, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<NfsActSub> page = nfsActSubService.findPage(new Page<NfsActSub>(request, response), nfsActSub); 
		model.addAttribute("page", page);
		return "admin/nfs/sub/nfsActSubList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("sub:nfsActSub:view")
	@RequestMapping(value = "add")
	public String add(NfsActSub nfsActSub, Model model) {
		model.addAttribute("nfsActSub", nfsActSub);
		return "admin/nfs/sub/nfsActSubAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("sub:nfsActSub:view")
	@RequestMapping(value = "query")
	public String query(NfsActSub nfsActSub, Model model) {
		model.addAttribute("nfsActSub", nfsActSub);
		return "admin/nfs/sub/nfsActSubQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("sub:nfsActSub:view")
	@RequestMapping(value = "update")
	public String update(NfsActSub nfsActSub, Model model) {
		model.addAttribute("nfsActSub", nfsActSub);
		return "admin/nfs/sub/nfsActSubUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("sub:nfsActSub:edit")
	@RequestMapping(value = "save")
	public String save(NfsActSub nfsActSub, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, nfsActSub)){
			return add(nfsActSub, model);
		}
		nfsActSubService.save(nfsActSub);
		addMessage(redirectAttributes, "保存账户科目成功");
		return "redirect:"+Global.getAdminPath()+"/nfsActSub/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("sub:nfsActSub:edit")
	@RequestMapping(value = "delete")
	public String delete(NfsActSub nfsActSub, RedirectAttributes redirectAttributes) {
		nfsActSubService.delete(nfsActSub);
		addMessage(redirectAttributes, "删除账户科目成功");
		return "redirect:"+Global.getAdminPath()+"/nfsActSub/?repage";
	}
	
	@RequestMapping("getSubList")
	@ResponseBody
	public AjaxRsp getSubList(String trxRole){

		AjaxRsp rsp  = new AjaxRsp();
		List<NfsActSub> subList = NfsUtils.findSubsByTrxRole(trxRole);
		rsp.setResult(subList);
		return rsp;
	}

}