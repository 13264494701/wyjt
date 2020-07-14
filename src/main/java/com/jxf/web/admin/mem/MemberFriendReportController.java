package com.jxf.web.admin.mem;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.ebaoquan.rop.thirdparty.org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jxf.mem.entity.MemberFriendReport;
import com.jxf.mem.entity.MessageUserRecord;
import com.jxf.mem.service.MemberFriendReportService;
import com.jxf.mem.service.MemberMessageService;
import com.jxf.mem.service.MessageUserRecordService;
import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.utils.Exceptions;
import com.jxf.web.admin.sys.BaseController;


/**
 * 好友投诉Controller
 * @author wo
 * @version 2019-02-11
 */
@Controller
@RequestMapping(value = "${adminPath}/memberFriendReport")
public class MemberFriendReportController extends BaseController {

	@Autowired
	private MemberFriendReportService memberFriendReportService;
	@Autowired
	private MessageUserRecordService messageUserRecordService;
	@Autowired
	private MemberMessageService memberMessageService;//站内信
	
	@ModelAttribute
	public MemberFriendReport get(@RequestParam(required=false) Long id) {
		MemberFriendReport entity = null;
		if (id!=null){
			entity = memberFriendReportService.get(id);
		}
		if (entity == null){
			entity = new MemberFriendReport();
		}
		return entity;
	}
	
	@RequiresPermissions("report:memberFriendReport:view")
	@RequestMapping(value = {"list", ""})
	public String list(MemberFriendReport memberFriendReport, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MemberFriendReport> page = memberFriendReportService.findPage(new Page<MemberFriendReport>(request, response), memberFriendReport); 
		model.addAttribute("page", page);
		return "admin/mem/friend/report/memberFriendReportList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("report:memberFriendReport:view")
	@RequestMapping(value = "add")
	public String add(MemberFriendReport memberFriendReport, Model model) {
		model.addAttribute("memberFriendReport", memberFriendReport);
		return "admin/mem/friend/report/memberFriendReportAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("report:memberFriendReport:view")
	@RequestMapping(value = "query")
	public String query(MemberFriendReport memberFriendReport, Model model) {
		if(StringUtils.isNotBlank(memberFriendReport.getImages())) {
			String [] images = StringUtils.split(memberFriendReport.getImages(), "|");
			for(String img :images) {
				memberFriendReport.getImagesList().add(img);
			}			
		}	
		model.addAttribute("memberFriendReport", memberFriendReport);
		return "admin/mem/friend/report/memberFriendReportQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("report:memberFriendReport:view")
	@RequestMapping(value = "update")
	public String update(MemberFriendReport memberFriendReport, Model model) {
		if(StringUtils.isNotBlank(memberFriendReport.getImages())) {
			String [] images = StringUtils.split(memberFriendReport.getImages(), "|");
			for(String img :images) {
				memberFriendReport.getImagesList().add(img);
			}			
		}	
		model.addAttribute("memberFriendReport", memberFriendReport);
		return "admin/mem/friend/report/memberFriendReportUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("report:memberFriendReport:edit")
	@RequestMapping(value = "save")
	public String save(MemberFriendReport memberFriendReport, Model model, RedirectAttributes redirectAttributes) {

		String memberIms = memberFriendReport.getMemberIms();
		String friendIms = memberFriendReport.getFriendIms();
		try {
			if(StringUtils.isNoneBlank(friendIms)) {
				MessageUserRecord messageUserRecord = new MessageUserRecord();
				messageUserRecord.setContent(friendIms);
				messageUserRecord.setTitle("投诉提醒");
				messageUserRecord.setUsernameStr(memberFriendReport.getFriend().getUsername());
				messageUserRecordService.save(messageUserRecord);
				
				memberMessageService.sendMessageForAdmin(friendIms, "投诉提醒", memberFriendReport.getFriend());
			}
			if(StringUtils.isNoneBlank(memberIms)) {
				MessageUserRecord messageUserRecord = new MessageUserRecord();
				messageUserRecord.setContent(memberIms);
				messageUserRecord.setTitle("投诉提醒");
				messageUserRecord.setUsernameStr(memberFriendReport.getMember().getUsername());
				messageUserRecordService.save(messageUserRecord);
				
				memberMessageService.sendMessageForAdmin(memberIms, "投诉提醒", memberFriendReport.getMember());
			}
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(e));
		}
		
		memberFriendReportService.save(memberFriendReport);
		addMessage(redirectAttributes, "保存好友投诉成功");
		return "redirect:"+Global.getAdminPath()+"/memberFriendReport/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("report:memberFriendReport:edit")
	@RequestMapping(value = "delete")
	public String delete(MemberFriendReport memberFriendReport, RedirectAttributes redirectAttributes) {
		memberFriendReportService.delete(memberFriendReport);
		addMessage(redirectAttributes, "删除好友投诉成功");
		return "redirect:"+Global.getAdminPath()+"/memberFriendReport/?repage";
	}

}