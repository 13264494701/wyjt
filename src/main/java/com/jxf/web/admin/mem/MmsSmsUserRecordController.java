package com.jxf.web.admin.mem;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jxf.mem.entity.MmsSmsUserRecord;
import com.jxf.mem.service.MmsSmsUserRecordService;
import com.jxf.mms.msg.SendSmsMsgService;
import com.jxf.svc.config.Global;
import com.jxf.svc.model.Message;
import com.jxf.svc.persistence.Page;
import com.jxf.web.admin.sys.BaseController;

/**
 * 发送短信记录表Controller
 * @author gaobo
 * @version 2019-03-19
 */
@Controller
@RequestMapping(value = "${adminPath}/mem/mmsSmsUserRecord")
public class MmsSmsUserRecordController extends BaseController {

	@Autowired
	private MmsSmsUserRecordService mmsSmsUserRecordService;
	@Autowired
	private SendSmsMsgService sendSmsMsgService;
	
	@ModelAttribute
	public MmsSmsUserRecord get(@RequestParam(required=false) Long id) {
		MmsSmsUserRecord entity = null;
		if (id!=null){
			entity = mmsSmsUserRecordService.get(id);
		}
		if (entity == null){
			entity = new MmsSmsUserRecord();
		}
		return entity;
	}
	
	@RequiresPermissions("mem:mmsSmsUserRecord:view")
	@RequestMapping(value = {"forwardSms", ""})
	public String forwardSms(MmsSmsUserRecord mmsSmsUserRecord, HttpServletRequest request, HttpServletResponse response, Model model) {
		return "admin/mms/sms/sendSms";
	}
	
	/**
	 * 发送短信
	 */
	@ResponseBody
	@RequestMapping(value="sendSms")
	public Message sendSMS(HttpServletRequest request){
		
		String usernameStr = request.getParameter("username");
		String type = request.getParameter("type");
		String content = request.getParameter("content");
		String username = "";
		
		if(StringUtils.isBlank(usernameStr)) {
			return Message.error("手机号不能为空");
		}
		if(StringUtils.isBlank(content)) {
			return Message.error("短信内容不能为空");
		}
		
		MmsSmsUserRecord smsRecord = new MmsSmsUserRecord();
		smsRecord.setContent(content);
		smsRecord.setUsernameStr(usernameStr);
		smsRecord.setType(type);
		mmsSmsUserRecordService.save(smsRecord);
		
		String[] split = usernameStr.split(",");
		for (int i = 0; i < split.length; i++) {
			username = split[i];
			if(StringUtils.equals(type, "1")) {
				sendSmsMsgService.sendNormalSmsForAdmin(content, username);
			}else {
				sendSmsMsgService.sendCollectionSmsForAdmin(content, username);
			}
		}
		return Message.success("发送成功");
		
	}
	
	
	
	@RequiresPermissions("mem:mmsSmsUserRecord:view")
	@RequestMapping(value = {"list", ""})
	public String list(MmsSmsUserRecord mmsSmsUserRecord, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MmsSmsUserRecord> page = mmsSmsUserRecordService.findPage(new Page<MmsSmsUserRecord>(request, response), mmsSmsUserRecord); 
		model.addAttribute("page", page);
		return "mem/mem/mmsSmsUserRecordList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("mem:mmsSmsUserRecord:view")
	@RequestMapping(value = "add")
	public String add(MmsSmsUserRecord mmsSmsUserRecord, Model model) {
		model.addAttribute("mmsSmsUserRecord", mmsSmsUserRecord);
		return "mem/mem/mmsSmsUserRecordAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("mem:mmsSmsUserRecord:view")
	@RequestMapping(value = "query")
	public String query(MmsSmsUserRecord mmsSmsUserRecord, Model model) {
		model.addAttribute("mmsSmsUserRecord", mmsSmsUserRecord);
		return "mem/mem/mmsSmsUserRecordQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("mem:mmsSmsUserRecord:view")
	@RequestMapping(value = "update")
	public String update(MmsSmsUserRecord mmsSmsUserRecord, Model model) {
		model.addAttribute("mmsSmsUserRecord", mmsSmsUserRecord);
		return "mem/mem/mmsSmsUserRecordUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("mem:mmsSmsUserRecord:edit")
	@RequestMapping(value = "save")
	public String save(MmsSmsUserRecord mmsSmsUserRecord, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, mmsSmsUserRecord)){
			return add(mmsSmsUserRecord, model);
		}
		mmsSmsUserRecordService.save(mmsSmsUserRecord);
		addMessage(redirectAttributes, "保存发送短信记录表成功");
		return "redirect:"+Global.getAdminPath()+"/mem/mmsSmsUserRecord/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("mem:mmsSmsUserRecord:edit")
	@RequestMapping(value = "delete")
	public String delete(MmsSmsUserRecord mmsSmsUserRecord, RedirectAttributes redirectAttributes) {
		mmsSmsUserRecordService.delete(mmsSmsUserRecord);
		addMessage(redirectAttributes, "删除发送短信记录表成功");
		return "redirect:"+Global.getAdminPath()+"/mem/mmsSmsUserRecord/?repage";
	}

}