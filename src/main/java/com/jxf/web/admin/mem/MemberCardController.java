package com.jxf.web.admin.mem;

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

import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberCard;
import com.jxf.mem.entity.MemberVerified;
import com.jxf.mem.service.MemberCardService;
import com.jxf.mem.service.MemberService;
import com.jxf.mem.service.impl.MemberVerifiedServiceImpl;

import com.jxf.mem.utils.MemUtils;
import com.jxf.nfs.entity.NfsBankBin;
import com.jxf.nfs.service.NfsBankBinService;
import com.jxf.svc.config.Global;
import com.jxf.svc.model.HandleRsp;
import com.jxf.svc.persistence.Page;
import com.jxf.web.admin.sys.BaseController;



/**
 * 银行卡Controller
 * @author wo
 * @version 2018-09-29
 */
@Controller
@RequestMapping(value = "${adminPath}/memberCard")
public class MemberCardController extends BaseController {

	@Autowired
	private MemberCardService memberCardService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private MemberVerifiedServiceImpl memberVerifiedServiceImpl;
	@Autowired
	private NfsBankBinService bankBinService;
	
	@ModelAttribute
	public MemberCard get(@RequestParam(required=false) Long id) {
		MemberCard entity = null;
		if (id!=null){
			entity = memberCardService.get(id);
		}
		if (entity == null){
			entity = new MemberCard();
		}
		return entity;
	}
	
	@RequiresPermissions("card:memberCard:view")
	@RequestMapping(value = {"list", ""})
	public String list(MemberCard memberCard, HttpServletRequest request, HttpServletResponse response, Model model) {
		if(memberCard.getMember()!=null&&StringUtils.isNotBlank(memberCard.getMember().getUsername())) {
			Member member = memberService.findByUsername(memberCard.getMember().getUsername());
			memberCard.setMember(member);
		}
		if(memberCard.getMember()!=null&&memberCard.getMember().getId() != null) {
			Member member = memberService.get(memberCard.getMember().getId());
			memberCard.setMember(member);
		}
		Page<MemberCard> page = memberCardService.findPage(new Page<MemberCard>(request, response), memberCard); 
		List<MemberCard> list = page.getList();
		for(MemberCard card :page.getList()) {
			card.setMember(memberService.get(card.getMember()));
			MemUtils.mask(card.getMember());
		}
		page.setList(list);	
		model.addAttribute("page", page);
		return "admin/mem/card/memberCardList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("card:memberCard:view")
	@RequestMapping(value = "add")
	public String add(MemberCard memberCard, Model model) {
		model.addAttribute("memberCard", memberCard);
		return "admin/mem/card/memberCardAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("card:memberCard:view")
	@RequestMapping(value = "query")
	public String query(MemberCard memberCard, Model model) {
		model.addAttribute("memberCard", memberCard);
		return "admin/mem/card/memberCardQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("card:memberCard:view")
	@RequestMapping(value = "update")
	public String update(MemberCard memberCard, Model model) {
		model.addAttribute("memberCard", memberCard);
		return "admin/mem/card/memberCardUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("card:memberCard:edit")
	@RequestMapping(value = "save")
	public String save(MemberCard memberCard, Model model, RedirectAttributes redirectAttributes) {
		String cardNo = memberCard.getCardNo();
		String phoneNo = memberCard.getPhoneNo();
		Member member = memberService.get(memberCard.getMember());
		String name = member.getName();
		String idNo = member.getIdNo();
		
		/** 验证银行卡四要素*/
		HandleRsp rsp = memberCardService.checkCard4Factors(cardNo, name, idNo, phoneNo);
		if(!rsp.getStatus()) {
			addMessage(redirectAttributes, rsp.getMessage());
			return "redirect:"+Global.getAdminPath()+"/memberCard/?repage";
		}
		
		NfsBankBin bankBin = bankBinService.getByCardNo(cardNo);
		memberCard.setBank(bankBin.getBank());
		memberCard.setCardNo(cardNo);
		memberCard.setCardType(bankBin.getCardType());
		memberCardService.save(memberCard);
		
		//记录认证表
		MemberVerified memberVerified = new MemberVerified();
		memberVerified.setMember(member);
		memberVerified.setIdNo(member.getIdNo());
		memberVerified.setRealName(member.getName());
		memberVerified.setPhoneNo(phoneNo);
		memberVerified.setCardNo(cardNo);
		memberVerified.setEmail(member.getEmail());
		memberVerified.setStatus(MemberVerified.Status.verified);
		memberVerifiedServiceImpl.save(memberVerified);
		
		addMessage(redirectAttributes, "保存银行卡成功");
		return "redirect:"+Global.getAdminPath()+"/memberCard/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("card:memberCard:edit")
	@RequestMapping(value = "delete")
	public String delete(MemberCard memberCard, RedirectAttributes redirectAttributes) {
		memberCardService.delete(memberCard);
		addMessage(redirectAttributes, "删除银行卡成功");
		return "redirect:"+Global.getAdminPath()+"/memberCard/?repage";
	}

}