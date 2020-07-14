package com.jxf.web.admin.mem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

import com.jxf.loan.entity.NfsLoanRecord;
import com.jxf.loan.service.NfsLoanRecordService;
import com.jxf.mem.entity.MemberPhonebook;
import com.jxf.mem.service.MemberPhonebookService;
import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.user.entity.User;
import com.jxf.svc.sys.util.UserUtils;
import com.jxf.svc.utils.Encodes;
import com.jxf.svc.utils.Exceptions;
import com.jxf.web.admin.sys.BaseController;


/**
 * 手机通讯录Controller
 * @author wo
 * @version 2019-03-07
 */
@Controller
@RequestMapping(value = "${adminPath}/memberPhonebook")
public class MemberPhonebookController extends BaseController {

	@Autowired
	private MemberPhonebookService memberPhonebookService;
	@Autowired
	private NfsLoanRecordService loanRecordService;
	
	@ModelAttribute
	public MemberPhonebook get(@RequestParam(required=false) Long id) {
		MemberPhonebook entity = null;
		if (id!=null){
			entity = memberPhonebookService.get(id);
		}
		if (entity == null){
			entity = new MemberPhonebook();
		}
		return entity;
	}
	
	@RequiresPermissions("phonebook:memberPhonebook:view")
	@RequestMapping(value = {"list", ""})
	public String list(MemberPhonebook memberPhonebook, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MemberPhonebook> page = memberPhonebookService.findPage(new Page<MemberPhonebook>(request, response), memberPhonebook); 
		model.addAttribute("page", page);
		return "admin/mem/phonebook/memberPhonebookList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("phonebook:memberPhonebook:view")
	@RequestMapping(value = "add")
	public String add(MemberPhonebook memberPhonebook, Model model) {
		model.addAttribute("memberPhonebook", memberPhonebook);
		return "admin/mem/phonebook/memberPhonebookAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("phonebook:memberPhonebook:view")
	@RequestMapping(value = "query")
	public String query(MemberPhonebook memberPhonebook, Model model) {
		model.addAttribute("memberPhonebook", memberPhonebook);
		return "admin/mem/phonebook/memberPhonebookQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("phonebook:memberPhonebook:view")
	@RequestMapping(value = "update")
	public String update(MemberPhonebook memberPhonebook, Model model) {
		model.addAttribute("memberPhonebook", memberPhonebook);
		return "admin/mem/phonebook/memberPhonebookUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("phonebook:memberPhonebook:edit")
	@RequestMapping(value = "save")
	public String save(MemberPhonebook memberPhonebook, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, memberPhonebook)){
			return add(memberPhonebook, model);
		}
		memberPhonebookService.save(memberPhonebook);
		addMessage(redirectAttributes, "保存手机通讯录成功");
		return "redirect:"+Global.getAdminPath()+"/memberPhonebook/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("phonebook:memberPhonebook:edit")
	@RequestMapping(value = "delete")
	public String delete(MemberPhonebook memberPhonebook, RedirectAttributes redirectAttributes) {
		memberPhonebookService.delete(memberPhonebook);
		addMessage(redirectAttributes, "删除手机通讯录成功");
		return "redirect:"+Global.getAdminPath()+"/memberPhonebook/?repage";
	}

	@RequiresPermissions("phonebook:memberPhonebook:view")
	@RequestMapping(value = "booklist")
	public String booklist(MemberPhonebook memberPhonebook, HttpServletRequest request, HttpServletResponse response, Model model) {
				
		String loginIp = Global.getRemoteAddr(request);
		User user = UserUtils.getUser();
		if(!StringUtils.contains(user.getAllowIps(), loginIp)) {
			logger.error("当前IP{}不在操作运行范围内",loginIp);
			model.addAttribute("note", "当前IP不在操作运行范围内");
			return "admin/collection/memberPhonebook";
		}		
		
		if(memberPhonebook.getMember()==null||StringUtils.isBlank(memberPhonebook.getMember().getUsername())) {
			model.addAttribute("note", "请输入手机号查询");
			return "admin/collection/memberPhonebook";
        }	
		
		NfsLoanRecord loanRecord = new NfsLoanRecord();
		loanRecord.setLoanee(memberPhonebook.getMember());
		loanRecord.setCollectionStatus(NfsLoanRecord.CollectionStatus.doing);
		List<NfsLoanRecord> loanRecordList = loanRecordService.findList(loanRecord);
		if(loanRecordList==null||loanRecordList.size()==0) {
			model.addAttribute("note", "该用户没有被催收的借条。");
			return "admin/collection/memberPhonebook";
		}
		
		memberPhonebook = memberPhonebookService.getByMember(memberPhonebook.getMember());
		if(memberPhonebook==null) {
			model.addAttribute("note", "暂无数据");
			return "admin/collection/memberPhonebook";
		}

		String phoneBook = memberPhonebook.getPhoneBook();
		if(StringUtils.isBlank(phoneBook)) {
			model.addAttribute("note", "暂无数据");
			return "admin/collection/memberPhonebook";
		}
		
		ArrayList<String> resList = new ArrayList<String>();
		String[] phoneNoAndNames = phoneBook.split(",");
		for(String pN:phoneNoAndNames){
		        String num = "";
		        String name = "";
		        if(StringUtils.isBlank(pN)) {
		        	continue;
		        }
		        String[] s = pN.split("\\|");
		        num = s[0];
		        try{
		          byte[] bytes = Encodes.decodeBase64(s[1]);
		          name = new String(bytes,"UTF-8");
		        }catch (IOException e){
		          logger.error(Exceptions.getStackTraceAsString(e));
		        }
		        resList.add("电话:"+num+"-姓名:"+name);
		}
		model.addAttribute("resList", resList);
		return "admin/collection/memberPhonebook";
	}
}