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

import com.jxf.nfs.entity.NfsBankInfo;
import com.jxf.nfs.service.NfsBankInfoService;
import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.utils.StringUtils;
import com.jxf.web.admin.sys.BaseController;

/**
 * 银行编码Controller
 * @author wo
 * @version 2018-09-29
 */
@Controller
@RequestMapping(value = "${adminPath}/nfsBankInfo")
public class NfsBankInfoController extends BaseController {

	@Autowired
	private NfsBankInfoService nfsBankInfoService;
	
	@ModelAttribute
	public NfsBankInfo get(@RequestParam(required=false) Long id) {
		NfsBankInfo entity = null;
		if (id!=null){
			entity = nfsBankInfoService.get(id);
		}
		if (entity == null){
			entity = new NfsBankInfo();
		}
		return entity;
	}
	
	@RequiresPermissions("bank:nfsBankInfo:view")
	@RequestMapping(value = {"list", ""})
	public String list(NfsBankInfo nfsBankInfo, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<NfsBankInfo> page = nfsBankInfoService.findPage(new Page<NfsBankInfo>(request, response), nfsBankInfo); 
		model.addAttribute("page", page);
		return "admin/nfs/bank/nfsBankInfoList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("bank:nfsBankInfo:view")
	@RequestMapping(value = "add")
	public String add(NfsBankInfo nfsBankInfo, Model model) {
		model.addAttribute("nfsBankInfo", nfsBankInfo);
		return "admin/nfs/bank/nfsBankInfoAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("bank:nfsBankInfo:view")
	@RequestMapping(value = "query")
	public String query(NfsBankInfo nfsBankInfo, Model model) {
		model.addAttribute("nfsBankInfo", nfsBankInfo);
		return "admin/nfs/bank/nfsBankInfoQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("bank:nfsBankInfo:view")
	@RequestMapping(value = "update")
	public String update(NfsBankInfo nfsBankInfo, Model model) {
		model.addAttribute("nfsBankInfo", nfsBankInfo);
		return "admin/nfs/bank/nfsBankInfoUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("bank:nfsBankInfo:edit")
	@RequestMapping(value = "save")
	public String save(NfsBankInfo nfsBankInfo,String newImage, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, nfsBankInfo)){
			return add(nfsBankInfo, model);
		}
        //单张图片处理
        if (StringUtils.isNotBlank(newImage)) {
        	nfsBankInfo.setLogo(newImage);
        } 
		nfsBankInfoService.save(nfsBankInfo);
		addMessage(redirectAttributes, "保存银行编码成功");
		return "redirect:"+Global.getAdminPath()+"/nfsBankInfo/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("bank:nfsBankInfo:edit")
	@RequestMapping(value = "delete")
	public String delete(NfsBankInfo nfsBankInfo, RedirectAttributes redirectAttributes) {
		nfsBankInfoService.delete(nfsBankInfo);
		addMessage(redirectAttributes, "删除银行编码成功");
		return "redirect:"+Global.getAdminPath()+"/nfsBankInfo/?repage";
	}

}