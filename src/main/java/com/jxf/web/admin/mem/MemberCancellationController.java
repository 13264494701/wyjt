package com.jxf.web.admin.mem;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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

import com.jxf.check.service.NfsCheckRecordService;
import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberAct;
import com.jxf.mem.entity.MemberCancellation;
import com.jxf.mem.entity.MemberResetPayPwd;
import com.jxf.mem.entity.MemberCancellation.Status;
import com.jxf.mem.service.MemberActService;
import com.jxf.mem.service.MemberCancellationService;
import com.jxf.mem.service.MemberCardService;
import com.jxf.mem.service.MemberResetPayPwdService;
import com.jxf.mem.service.MemberService;
import com.jxf.nfs.constant.ActSubConstant;
import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.utils.CalendarUtil;
import com.jxf.svc.utils.Collections3;
import com.jxf.web.admin.sys.BaseController;
import com.jxf.web.model.ResponseData;

/**
 * 会员注销申请Controller
 * @author SuHuimin
 * @version 2019-06-19
 */
@Controller
@RequestMapping(value = "${adminPath}/memberCancellation")
public class MemberCancellationController extends BaseController {

	@Autowired
	private MemberCancellationService memberCancellationService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private NfsCheckRecordService checkRecordService;
	@Autowired
	private MemberResetPayPwdService memberResetPayPwdService;
	@Autowired
	private MemberCardService memberCardService;
	@Autowired
	private MemberActService memberActService;
	
	@ModelAttribute
	public MemberCancellation get(@RequestParam(required=false) Long id) {
		MemberCancellation entity = null;
		if (id!=null){
			entity = memberCancellationService.get(id);
		}
		if (entity == null){
			entity = new MemberCancellation();
		}
		return entity;
	}
	
	@RequiresPermissions("mem:memberCancellation:view")
	@RequestMapping(value = {"list", ""})
	public String list(MemberCancellation memberCancellation, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MemberCancellation> page = memberCancellationService.findPage(new Page<MemberCancellation>(request, response), memberCancellation); 
		model.addAttribute("page", page);
		return "admin/mem/cancellation/memberCancellationList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("mem:memberCancellation:view")
	@RequestMapping(value = "add")
	public String add(MemberCancellation memberCancellation, Model model) {
		model.addAttribute("memberCancellation", memberCancellation);
		return "admin/mem/cancellation/memberCancellationAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("mem:memberCancellation:view")
	@RequestMapping(value = "query")
	public String query(MemberCancellation memberCancellation, Model model) {
		model.addAttribute("memberCancellation", memberCancellation);
		return "admin/mem/cancellation/MemberCancellationQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("mem:MemberCancellation:view")
	@RequestMapping(value = "update")
	public String update(MemberCancellation memberCancellation, Model model) {
		model.addAttribute("memberCancellation", memberCancellation);
		return "admin/mem/cancellation/memberCancellationUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("mem:memberCancellation:edit")
	@RequestMapping(value = "save")
	public String save(MemberCancellation memberCancellation, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, memberCancellation)){
			return add(memberCancellation, model);
		}
		memberCancellationService.save(memberCancellation);
		addMessage(redirectAttributes, "保存会员注销申请成功");
		return "redirect:"+Global.getAdminPath()+"/memberCancellation/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("mem:memberCancellation:edit")
	@RequestMapping(value = "delete")
	public String delete(MemberCancellation memberCancellation, RedirectAttributes redirectAttributes) {
		memberCancellationService.delete(memberCancellation);
		addMessage(redirectAttributes, "删除会员注销申请成功");
		return "redirect:"+Global.getAdminPath()+"/memberCancellation/?repage";
	}
	
	 /**
		 * 新增与修改的提交
		 */
		@RequiresPermissions("mem:memberCancellation:edit")
		@RequestMapping(value = "checkCancellation")
		@ResponseBody
	public ResponseData checkCancellation(MemberCancellation memberCancellation, Model model,
			RedirectAttributes redirectAttributes) {
		MemberCancellation memberCancellation2 = get(memberCancellation.getId());
		if (!memberCancellation2.getStatus().equals(Status.review)) {
			return ResponseData.error("该记录已审核，请勿重复操作！");
		}

		if (memberCancellation.getStatus().equals(MemberCancellation.Status.success)) {
			Member member = memberService.get(memberCancellation.getMember());
			MemberResetPayPwd memberResetPayPwd = new MemberResetPayPwd();
			memberResetPayPwd.setBeginTime(CalendarUtil.addDay(new Date(), -14));
			memberResetPayPwd.setEndTime(new Date());
			memberResetPayPwd.setMember(member);
			memberResetPayPwd.setStatus(MemberResetPayPwd.Status.verified);
			List<MemberResetPayPwd> resetPayPwdList = memberResetPayPwdService.findList(memberResetPayPwd);
			if (!Collections3.isEmpty(resetPayPwdList)) {
				return ResponseData.error("该账户最近2周内修改过交易密码，不能申请注销");
			}
			// 修改绑定银行卡
			int changeCardCountLastWeek = memberCardService.getChangeCardCountLast2Week(member);
			if (changeCardCountLastWeek > 0) {
				return ResponseData.error("该账户最近2周内修改过绑定银行卡，不能申请注销");
			}

			MemberAct memberActParam = new MemberAct();
			memberActParam.setMember(member);
			List<MemberAct> actList = memberActService.findList(memberActParam);
			for (MemberAct memberAct : actList) {
				if ((StringUtils.equals(memberAct.getSubNo(), ActSubConstant.MEMBER_PENDING_REPAYMENT)
						&& memberAct.getCurBal().compareTo(BigDecimal.ZERO) != 0)
						|| (StringUtils.equals(memberAct.getSubNo(), ActSubConstant.MEMBER_PENDING_RECEIVE)
								&& memberAct.getCurBal().compareTo(BigDecimal.ZERO) != 0)) {
					return ResponseData.error("您的账户还有未结清的借条，不能申请注销");
				} else {
					if (memberAct.getCurBal().compareTo(BigDecimal.ZERO) != 0
							&& !StringUtils.equals(memberAct.getSubNo(), ActSubConstant.MEMBER_WEIXIN_PAYMENT)) {
						return ResponseData.error("您的账户还有余额未提现，不能申请注销");
					}
				}
			}
			memberService.delete(member);
			member.setIsLocked(true);
			member.setIsEnabled(false);
			member.setLockedDate(new Date());
			member.setRmk("客户申请注销，经客服审核后通过注销申请。");
			memberService.insertHis(member);
		}
		memberCancellationService.save(memberCancellation);
		checkRecordService.saveCancellationCheckLog(memberCancellation);
		addMessage(redirectAttributes, "保存会员注销申请成功");
		return ResponseData.success("保存成功");
	}
}