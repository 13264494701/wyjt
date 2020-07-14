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

import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MessageUserRecord;
import com.jxf.mem.service.MemberMessageService;
import com.jxf.mem.service.MemberService;
import com.jxf.mem.service.MessageUserRecordService;
import com.jxf.svc.config.Global;
import com.jxf.svc.model.Message;
import com.jxf.svc.persistence.Page;
import com.jxf.web.admin.sys.BaseController;

/**
 * 发送站内信记录表Controller
 * @author gaobo
 * @version 2019-03-19
 */
@Controller
@RequestMapping(value = "${adminPath}/mem/messageUserRecord")
public class MessageUserRecordController extends BaseController {

	@Autowired
	private MessageUserRecordService messageUserRecordService;
	@Autowired
	private MemberMessageService memberMessageService;//站内信
	@Autowired
	private MemberService memberService;
	
	@ModelAttribute
	public MessageUserRecord get(@RequestParam(required=false) Long id) {
		MessageUserRecord entity = null;
		if (id!=null){
			entity = messageUserRecordService.get(id);
		}
		if (entity == null){
			entity = new MessageUserRecord();
		}
		return entity;
	}
	
	@RequiresPermissions("mem:messageUserRecord:view")
	@RequestMapping(value = {"list", ""})
	public String list(MessageUserRecord messageUserRecord, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MessageUserRecord> page = messageUserRecordService.findPage(new Page<MessageUserRecord>(request, response), messageUserRecord); 
		model.addAttribute("page", page);
		return "mem/mem/messageUserRecordList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("mem:messageUserRecord:view")
	@RequestMapping(value = "add")
	public String add(MessageUserRecord messageUserRecord, Model model) {
		model.addAttribute("messageUserRecord", messageUserRecord);
		return "mem/mem/messageUserRecordAdd";
	}
	
	@RequiresPermissions("mem:messageUserRecord:view")
	@RequestMapping(value = "forwardIms")
	public String forwardIms(MessageUserRecord messageUserRecord, HttpServletRequest request, HttpServletResponse response, Model model) {
		return "admin/mms/sms/sendIms";
	}
	
	/**
	 * 发送站内信
	 */
	@ResponseBody
	@RequestMapping(value="sendIms")
	public Message sendIms(HttpServletRequest request){
		Member member = null;
		String usernameStr = request.getParameter("username");
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		String username = "";
		
		if(StringUtils.isBlank(usernameStr)) {
			return Message.error("手机号不能为空");
		}
		if(StringUtils.isBlank(content)) {
			return Message.error("站内信内容不能为空");
		}
		
		String[] split = usernameStr.split(",");
		for (int i = 0; i < split.length; i++) {
			username = split[i];
			member = memberService.findByUsername(username);
			if(member != null) {
				memberMessageService.sendMessageForAdmin(content, title, member);
				
				MessageUserRecord messageUserRecord = new MessageUserRecord();
				messageUserRecord.setContent(content);
				messageUserRecord.setTitle(title);
				messageUserRecord.setUsernameStr(username);
				messageUserRecordService.save(messageUserRecord);
			}
		}
		return Message.success("发送成功");
		
	}
	
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("mem:messageUserRecord:view")
	@RequestMapping(value = "query")
	public String query(MessageUserRecord messageUserRecord, Model model) {
		model.addAttribute("messageUserRecord", messageUserRecord);
		return "mem/mem/messageUserRecordQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("mem:messageUserRecord:view")
	@RequestMapping(value = "update")
	public String update(MessageUserRecord messageUserRecord, Model model) {
		model.addAttribute("messageUserRecord", messageUserRecord);
		return "mem/mem/messageUserRecordUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("mem:messageUserRecord:edit")
	@RequestMapping(value = "save")
	public String save(MessageUserRecord messageUserRecord, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, messageUserRecord)){
			return add(messageUserRecord, model);
		}
		messageUserRecordService.save(messageUserRecord);
		addMessage(redirectAttributes, "保存发送站内信记录表成功");
		return "redirect:"+Global.getAdminPath()+"/mem/messageUserRecord/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("mem:messageUserRecord:edit")
	@RequestMapping(value = "delete")
	public String delete(MessageUserRecord messageUserRecord, RedirectAttributes redirectAttributes) {
		messageUserRecordService.delete(messageUserRecord);
		addMessage(redirectAttributes, "删除发送站内信记录表成功");
		return "redirect:"+Global.getAdminPath()+"/mem/messageUserRecord/?repage";
	}

}