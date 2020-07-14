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

import com.jxf.nfs.entity.NfsTransferRecord;
import com.jxf.nfs.service.NfsTransferRecordService;
import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.web.admin.sys.BaseController;
/**
 * 转账Controller
 * @author XIAORONGDIAN
 * @version 2018-11-09
 */
@Controller
@RequestMapping(value = "${adminPath}/transferRecord")
public class NfsTransferRecordController extends BaseController {

	@Autowired
	private NfsTransferRecordService nfsTransferRecordService;
	
	@ModelAttribute
	public NfsTransferRecord get(@RequestParam(required=false) Long id) {
		NfsTransferRecord entity = null;
		if (id!=null){
			entity = nfsTransferRecordService.get(id);
		}
		if (entity == null){
			entity = new NfsTransferRecord();
		}
		return entity;
	}
	
	@RequiresPermissions("nfs:transferRecord:view")
	@RequestMapping(value = {"list", ""})
	public String list(NfsTransferRecord nfsTransferRecord, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<NfsTransferRecord> page = nfsTransferRecordService.findPage(new Page<NfsTransferRecord>(request, response), nfsTransferRecord); 
		model.addAttribute("page", page);
		return "admin/nfs/transfer/nfsTransferRecordList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("nfs:transferRecord:view")
	@RequestMapping(value = "add")
	public String add(NfsTransferRecord nfsTransferRecord, Model model) {
		model.addAttribute("nfsTransferRecord", nfsTransferRecord);
		return "admin/nfs/transfer/nfsTransferRecordAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("nfs:transferRecord:view")
	@RequestMapping(value = "query")
	public String query(NfsTransferRecord nfsTransferRecord, Model model) {
		model.addAttribute("nfsTransferRecord", nfsTransferRecord);
		return "admin/nfs/transfer/nfsTransferRecordQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("nfs:transferRecord:view")
	@RequestMapping(value = "update")
	public String update(NfsTransferRecord nfsTransferRecord, Model model) {
		model.addAttribute("nfsTransferRecord", nfsTransferRecord);
		return "admin/nfs/transfer/nfsTransferRecordUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("nfs:transferRecord:edit")
	@RequestMapping(value = "save")
	public String save(NfsTransferRecord nfsTransferRecord, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, nfsTransferRecord)){
			return add(nfsTransferRecord, model);
		}
		nfsTransferRecordService.save(nfsTransferRecord);
		addMessage(redirectAttributes, "保存转账成功");
		return "redirect:"+Global.getAdminPath()+"/transferRecord/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("nfs:transferRecord:edit")
	@RequestMapping(value = "delete")
	public String delete(NfsTransferRecord nfsTransferRecord, RedirectAttributes redirectAttributes) {
		nfsTransferRecordService.delete(nfsTransferRecord);
		addMessage(redirectAttributes, "删除转账成功");
		return "redirect:"+Global.getAdminPath()+"/transferRecord/?repage";
	}

}