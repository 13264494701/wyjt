package com.jxf.web.admin.mem;


import java.math.BigDecimal;

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

import com.jxf.mem.entity.MemberAct;
import com.jxf.mem.service.MemberActService;
import com.jxf.mem.service.MemberService;
import com.jxf.nfs.entity.NfsFundAddReduce;
import com.jxf.nfs.service.NfsFundAddReduceService;
import com.jxf.svc.config.Global;
import com.jxf.svc.model.Message;
import com.jxf.svc.persistence.Page;
import com.jxf.web.admin.sys.BaseController;

/**
 * 会员账户Controller
 * @author wo
 * @version 2018-08-27
 */
@Controller
@RequestMapping(value = "${adminPath}/memberAct")
public class MemberActController extends BaseController {

	@Autowired
	private MemberActService memberActService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private NfsFundAddReduceService fundAddReduceService;
	
	@ModelAttribute
	public MemberAct get(@RequestParam(required=false) Long id) {
		MemberAct entity = null;
		if (id!=null){
			entity = memberActService.get(id);
		}
		if (entity == null){
			entity = new MemberAct();
		}
		return entity;
	}
	
	@RequiresPermissions("act:memberAct:view")
	@RequestMapping(value = {"list", ""})
	public String list(MemberAct memberAct, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MemberAct> page = memberActService.findPage(new Page<MemberAct>(request, response), memberAct); 
		model.addAttribute("page", page);
		model.addAttribute("member", memberService.get(memberAct.getMember()));
		return "admin/mem/act/memberActList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("act:memberAct:view")
	@RequestMapping(value = "add")
	public String add(MemberAct memberAct, Model model) {
		model.addAttribute("memberAct", memberAct);
		return "admin/mem/act/memberActAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("act:memberAct:view")
	@RequestMapping(value = "query")
	public String query(MemberAct memberAct, Model model) {
		model.addAttribute("memberAct", memberAct);
		return "admin/mem/act/memberActQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("act:memberAct:view")
	@RequestMapping(value = "update")
	public String update(MemberAct memberAct, Model model) {
		model.addAttribute("memberAct", memberAct);
		return "admin/mem/act/memberActUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("act:memberAct:edit")
	@RequestMapping(value = "save")
	public String save(MemberAct memberAct, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, memberAct)){
			return add(memberAct, model);
		}
		memberActService.save(memberAct);
		addMessage(redirectAttributes, "保存会员账户成功");
		return "redirect:"+Global.getAdminPath()+"/memberAct/?repage&member.id="+memberAct.getMember().getId();
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("act:memberAct:edit")
	@RequestMapping(value = "delete")
	public String delete(MemberAct memberAct, RedirectAttributes redirectAttributes) {
		memberActService.delete(memberAct);
		addMessage(redirectAttributes, "删除会员账户成功");
		return "redirect:"+Global.getAdminPath()+"/memberAct/?repage&member.id="+memberAct.getMember().getId();
	}
	
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("act:memberAct:view")
	@RequestMapping(value = "addBal")
	public String addBal(MemberAct memberAct, Model model) {
		model.addAttribute("memberAct", memberAct);
		return "admin/mem/act/addActBal";
	}
	
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("act:memberAct:view")
	@RequestMapping(value = "reduceBal")
	public String reduceBal(MemberAct memberAct, Model model) {
		model.addAttribute("memberAct", memberAct);
		return "admin/mem/act/reduceActBal";
	}
	
	 /**
	 * 后台给会员加款 
	 */
	@RequiresPermissions("act:memberAct:edit")
	@RequestMapping(value = "addActBal")
	@ResponseBody
	public Message addActBal(MemberAct memberAct, BigDecimal trxAmt, Model model, RedirectAttributes redirectAttributes) {
		NfsFundAddReduce addRecord = new NfsFundAddReduce();
		addRecord.setAmount(trxAmt);
		addRecord.setCurBal(memberAct.getCurBal().add(trxAmt));
		addRecord.setStatus(NfsFundAddReduce.Status.auditing);
		addRecord.setType(NfsFundAddReduce.Type.add);
		addRecord.setCurrCode("CNY");
		addRecord.setMember(memberAct.getMember());
		addRecord.setRmk(memberAct.getRmk());
		fundAddReduceService.save(addRecord);
		return Message.success("加款申请成功");
	}
	
	 /**
	 * 后台给会员减款 
	 */
	@RequiresPermissions("act:memberAct:edit")
	@RequestMapping(value = "reduceActBal")
	@ResponseBody
	public Message reduceActBal(MemberAct memberAct, BigDecimal trxAmt, Model model, RedirectAttributes redirectAttributes) {
		NfsFundAddReduce reduceRecord = new NfsFundAddReduce();
		reduceRecord.setAmount(trxAmt);
		BigDecimal curBal = memberAct.getCurBal().subtract(trxAmt);
		if(curBal.compareTo(BigDecimal.ZERO) < 0) {
			return Message.success("账户余额不足，减款申请失败");
		}
		reduceRecord.setCurBal(curBal);
		reduceRecord.setStatus(NfsFundAddReduce.Status.auditing);
		reduceRecord.setType(NfsFundAddReduce.Type.reduce);
		reduceRecord.setCurrCode("CNY");
		reduceRecord.setMember(memberAct.getMember());
		reduceRecord.setRmk(memberAct.getRmk());
		fundAddReduceService.save(reduceRecord);
		return Message.success("减款申请成功");
	}
	
	

}