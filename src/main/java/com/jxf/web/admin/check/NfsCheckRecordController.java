package com.jxf.web.admin.check;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jxf.check.entity.NfsCheckRecord;
import com.jxf.check.entity.NfsCheckRecord.OrgType;
import com.jxf.check.service.NfsCheckRecordService;
import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.web.admin.sys.BaseController;

/**
 * 审核记录Controller
 * @author suHuimin
 * @version 2019-01-26
 */
@Controller
@RequestMapping(value = "${adminPath}/check/nfsCheckRecord")
public class NfsCheckRecordController extends BaseController {

	@Autowired
	private NfsCheckRecordService nfsCheckRecordService;
	
	@ModelAttribute
	public NfsCheckRecord get(@RequestParam(required=false) Long id) {
		NfsCheckRecord entity = null;
		if (id!=null){
			entity = nfsCheckRecordService.get(id);
		}
		if (entity == null){
			entity = new NfsCheckRecord();
		}
		return entity;
	}
	
	@RequiresPermissions("check:nfsCheckRecord:view")
	@RequestMapping(value = {"list", ""})
	public String list(NfsCheckRecord nfsCheckRecord, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<NfsCheckRecord> page = nfsCheckRecordService.findPage(new Page<NfsCheckRecord>(request, response), nfsCheckRecord); 
		model.addAttribute("page", page);
		return "admin/check/nfsCheckRecordList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("check:nfsCheckRecord:view")
	@RequestMapping(value = "add")
	public String add(NfsCheckRecord nfsCheckRecord, Model model) {
		model.addAttribute("nfsCheckRecord", nfsCheckRecord);
		return "admin/check/nfsCheckRecordAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("check:nfsCheckRecord:view")
	@RequestMapping(value = "query")
	public String query(NfsCheckRecord nfsCheckRecord, Model model) {
		model.addAttribute("nfsCheckRecord", nfsCheckRecord);
		return "admin/check/nfsCheckRecordQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("check:nfsCheckRecord:view")
	@RequestMapping(value = "update")
	public String update(NfsCheckRecord nfsCheckRecord, Model model) {
		model.addAttribute("nfsCheckRecord", nfsCheckRecord);
		return "admin/check/nfsCheckRecordUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("check:nfsCheckRecord:edit")
	@RequestMapping(value = "save")
	public String save(NfsCheckRecord nfsCheckRecord, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, nfsCheckRecord)){
			return add(nfsCheckRecord, model);
		}
		nfsCheckRecordService.save(nfsCheckRecord);
		addMessage(redirectAttributes, "保存审核记录成功");
		return "redirect:"+Global.getAdminPath()+"/check/nfsCheckRecord/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("check:nfsCheckRecord:edit")
	@RequestMapping(value = "delete")
	public String delete(NfsCheckRecord nfsCheckRecord, RedirectAttributes redirectAttributes) {
		nfsCheckRecordService.delete(nfsCheckRecord);
		addMessage(redirectAttributes, "删除审核记录成功");
		return "redirect:"+Global.getAdminPath()+"/check/nfsCheckRecord/?repage";
	}

	
	/**
	 * 查看原业务页面跳转
	 */
	@RequiresPermissions("check:nfsCheckRecord:view")
	@RequestMapping(value = "queryOrgOrder")
	public String queryOrgOrder(Long orgId, Model model) {
		NfsCheckRecord param = new NfsCheckRecord();
		param.setOrgId(orgId);
		List<NfsCheckRecord> checkRecords = nfsCheckRecordService.findList(param);
		NfsCheckRecord checkRecord = checkRecords.get(0);
		if (checkRecord.getOrgType().equals(OrgType.withdraw)) {
			return "redirect:"+Global.getAdminPath()+"/wdrl/nfsWdrlRecord/query?id="+orgId;
		}else if(checkRecord.getOrgType().equals(OrgType.memberAddBal)) {
			return "redirect:"+Global.getAdminPath()+"/fundAddReduce/query?id="+orgId;
		}if(checkRecord.getOrgType().equals(OrgType.memberSubBal)) {
			return "redirect:"+Global.getAdminPath()+"/fundAddReduce/query?id="+orgId;
		}else if (checkRecord.getOrgType().equals(OrgType.ufangBrnAddBal)) {
			return "redirect:"+Global.getAdminPath()+"/ufangFundAddReduce/query?id="+orgId;
		}else if (checkRecord.getOrgType().equals(OrgType.ufangBrnSubBal)) {
			return "redirect:"+Global.getAdminPath()+"/ufangFundAddReduce/query?id="+orgId;
		} 
		return "admin/check/nfsCheckRecordQuery";
	}
	
	
}