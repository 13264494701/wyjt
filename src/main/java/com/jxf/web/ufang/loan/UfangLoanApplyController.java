package com.jxf.web.ufang.loan;

import java.math.BigDecimal;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jxf.loan.constant.RecordMessage;
import com.jxf.loan.entity.NfsLoanApply;
import com.jxf.loan.entity.NfsLoanApply.LoanRole;
import com.jxf.loan.entity.NfsLoanApply.RepayType;
import com.jxf.loan.entity.NfsLoanApplyDetail;
import com.jxf.loan.entity.NfsLoanDetailMessage;
import com.jxf.loan.entity.NfsLoanRecord;
import com.jxf.loan.service.NfsLoanApplyDetailService;
import com.jxf.loan.service.NfsLoanApplyService;
import com.jxf.loan.service.NfsLoanDetailMessageService;
import com.jxf.loan.utils.LoanUtils;
import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberMessage;
import com.jxf.mem.service.MemberActService;
import com.jxf.mem.service.MemberMessageService;
import com.jxf.mem.service.MemberService;
import com.jxf.mem.utils.MemUtils;
import com.jxf.mem.utils.VerifiedUtils;
import com.jxf.nfs.constant.TrxRuleConstant;
import com.jxf.nfs.service.NfsActService;
import com.jxf.svc.cache.RedisUtils;
import com.jxf.svc.model.Message;
import com.jxf.svc.model.Notice;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.security.MD5Utils;
import com.jxf.svc.security.PasswordUtils;
import com.jxf.svc.security.rsa.RSAService;
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.StringUtils;
import com.jxf.ufang.entity.UfangUser;
import com.jxf.ufang.util.UfangUserUtils;
import com.jxf.web.model.ResponseData;
import com.jxf.web.ufang.UfangBaseController;


/**
 * 借款申请Controller
 * @author wo
 * @version 2018-09-26
 */
@Controller("ufangLoanApplyController")
@RequestMapping(value = "${ufangPath}/loanApply")
public class UfangLoanApplyController extends UfangBaseController {

	@Autowired
	private NfsLoanApplyService nfsLoanApplyService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private MemberActService memberActService;
	@Autowired
	private RSAService rsaService;
	@Autowired
	private NfsLoanApplyDetailService applyDetailService;
	@Autowired
	private NfsActService actService;
	@Autowired
	private MemberMessageService memberMessageService;

	@Autowired
	private NfsLoanDetailMessageService nfsLoanDetailMessageService;
	@ModelAttribute
	public NfsLoanApply get(@RequestParam(required=false) Long id) {
		NfsLoanApply entity = null;
		if (id!=null){
			entity = nfsLoanApplyService.get(id);
		}
		if (entity == null){
			entity = new NfsLoanApply();
		}
		return entity;
	}
	
	@RequiresPermissions("ufang:loanApply:view")
	@RequestMapping(value = "singleLoanApplyList")
	public String singleLoanApplyList(NfsLoanApply nfsLoanApply, HttpServletRequest request, HttpServletResponse response, Model model) {
		UfangUser ufangUser = UfangUserUtils.getUser();
		if (ufangUser.getBindStatus().equals(UfangUser.BindStatus.binded)&&ufangUser.getMember()!=null&&ufangUser.getMember().getId()!=null) {
			nfsLoanApply.setMember(ufangUser.getMember());
			Page<NfsLoanApply> page = nfsLoanApplyService.findSingleLoanApplyPage(new Page<NfsLoanApply>(request, response), nfsLoanApply);
			model.addAttribute("page", page);
		}
		return "ufang/loan/apply/singleLoanApplyList";
	}
	
	@RequiresPermissions("ufang:loanApply:view")
	@RequestMapping(value = "multipleLoanApplyList")
	public String multipleLoanApplyList(NfsLoanApply nfsLoanApply, HttpServletRequest request, HttpServletResponse response, Model model) {
		UfangUser ufangUser = UfangUserUtils.getUser();
		if (ufangUser.getBindStatus().equals(UfangUser.BindStatus.binded)&&ufangUser.getMember()!=null&&ufangUser.getMember().getId()!=null) {
			nfsLoanApply.setMember(ufangUser.getMember());
			Page<NfsLoanApply> page = nfsLoanApplyService.findMultipleLoanApplyPage(new Page<NfsLoanApply>(request, response), nfsLoanApply);
			model.addAttribute("page", page);
		}
		return "ufang/loan/apply/multipleLoanApplyList";
	}
	
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("ufang:loanApply:view")
	@RequestMapping(value = "add")
	public String add(NfsLoanApply nfsLoanApply, Model model) {
		model.addAttribute("nfsLoanApply", nfsLoanApply);
		return "ufang/loan/apply/loan/nfsLoanApplyAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("ufang:loanApply:view")
	@RequestMapping(value = "query")
	public String query(NfsLoanApply nfsLoanApply, Model model) {
		model.addAttribute("nfsLoanApply", nfsLoanApply);
		return "ufang/loan/apply/nfsLoanApplyQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("ufang:loanApply:view")
	@RequestMapping(value = "update")
	public String update(NfsLoanApply nfsLoanApply, Model model) {
		model.addAttribute("nfsLoanApply", nfsLoanApply);
		return "ufang/loan/apply/nfsLoanApplyUpdate";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("ufang:loanApply:edit")
	@RequestMapping(value = "delete")
	public String delete(NfsLoanApply nfsLoanApply, RedirectAttributes redirectAttributes) {
		nfsLoanApplyService.delete(nfsLoanApply);
		addMessage(redirectAttributes, "删除借款申请成功");
		return "redirect:"+ufangPath+"/loanApply/?repage";
	}
	
	@RequiresPermissions("ufang:loanApply:view")
	@RequestMapping(value="loanToFriend",method=RequestMethod.GET)
	public  String loanToFriend(Long friendId,Model model) {
		Member friend = memberService.get(friendId);
		MemUtils.mask(friend);
		model.addAttribute("friend", friend);
		return "ufang/loan/loanToFriend";
	}
	/**
	 * 借款给好友
	 * @param apply
	 * @return
	 */
	@RequiresPermissions("ufang:loanApply:edit")
	@RequestMapping(value="loanToFriend",method=RequestMethod.POST)
	@ResponseBody
	public  Message loanToFriend(NfsLoanApply apply,HttpServletRequest request) {
		
        //检查放款人
		UfangUser user = UfangUserUtils.getUser();
		if(user.getBindStatus().equals(UfangUser.BindStatus.unbind)) {
			return Message.error("您还未绑定无忧借条APP账号，请先绑定账号再放款！");
		}
		Member loaner = memberService.get(user.getMember());
		if(!VerifiedUtils.isVerified(loaner.getVerifiedList(), 1) || !VerifiedUtils.isVerified(loaner.getVerifiedList(), 2)) {
			return Message.error("您绑定的无忧借条APP账号还没有完成实名认证不能打借条");
		}		
		if(!VerifiedUtils.isVerified(loaner.getVerifiedList(), 22)) {
			return Message.error("您绑定的无忧借条APP账号还没有设置支付密码，请设置支付密码后再进行放款操作！");
		}

		String payPassWord = rsaService.decryptParameter("payPassWord", (HttpServletRequest)request);
		rsaService.removePrivateKey((HttpServletRequest)request);
		String enPassword = MD5Utils.EncoderByMd5("cpbao.com_友信宝" + MD5Utils.EncoderByMd5(payPassWord).toLowerCase()).toLowerCase();
		if(!PasswordUtils.validatePassword(enPassword, loaner.getPayPassword())) {
			return Message.warn("支付密码错误");
		}
		
		//余额校验
		BigDecimal availableAmount = memberActService.getAvlBal(loaner);
		if(availableAmount.compareTo(apply.getAmount()) < 0) {
			return Message.warn("您绑定的无忧借条APP账号可用余额不足，请重新输入借款金额！");
		}

		// 检查借款人
		Member loanee = apply.getDetail().getMember();
		ResponseData result = nfsLoanApplyService.checkFriend(loaner, loanee.getId()+"");
		if(result.getCode() != 0) {
			return Message.error(result.getMessage());
		}
		
		apply.setMember(loaner);
		apply.setChannel(NfsLoanApply.Channel.ufang);
		apply.setIsNewRecord(true);
		apply.setLoanType(NfsLoanApply.LoanType.single);
		apply.setLoanPurp(NfsLoanApply.LoanPurp.turnover);
		apply.setLoanRole(NfsLoanApply.LoanRole.loaner);
		apply.setTrxType(NfsLoanApply.TrxType.online);

		apply.setBeginDate(new Date());
		NfsLoanRecord loanRecord = LoanUtils.calInt(apply.getAmount(), apply.getIntRate(), apply.getRepayType(), apply.getTerm());
		apply.setRepayAmt(loanRecord.getDueRepayAmount());
		apply.setInterest(loanRecord.getInterest());
		Date parseDate = DateUtils.parseDate("1900-01-01 00:00:01");
		apply.setLoanStart(parseDate);
		apply.setTrxType(NfsLoanApply.TrxType.online);
		
		
		NfsLoanApplyDetail detail = apply.getDetail();
		detail.setLoanRole(LoanRole.loanee);
		detail.setAmount(apply.getAmount());
		detail.setAliveVideoStatus(NfsLoanApplyDetail.AliveVideoStatus.notUpload);		
		detail.setIntStatus(NfsLoanApplyDetail.IntStatus.primary);				
		detail.setStatus(NfsLoanApplyDetail.Status.pendingAgree);
		detail.setDisputeResolution(NfsLoanApplyDetail.DisputeResolution.arbitration);
		detail.setTrxType(NfsLoanApply.TrxType.online);
		
		nfsLoanApplyService.save(apply);
		detail.setApply(apply);
		applyDetailService.save(detail);
		int res = actService.updateAct(TrxRuleConstant.FROZEN_LOANER_FUNDS, detail.getAmount(), loaner, detail.getId());
		if(res == 0) {
			//发送会员消息
			memberMessageService.sendMessage(MemberMessage.Type.lendApplication, detail.getId());
			
			Notice notice = new Notice();
			notice.setNoticeType(1);
			notice.setNoticeId(apply.getId().toString());
			notice.setNoticeMessage("有新借条申请");		    			
			RedisUtils.leftPush("memberNotice"+loanee.getId(), notice);
			
			// 生成对话气泡
			NfsLoanDetailMessage nfsLoanDetailMessage = new NfsLoanDetailMessage();
			nfsLoanDetailMessage.setDetail(detail);
			nfsLoanDetailMessage.setMember(loaner);
			nfsLoanDetailMessage.setMessageId(RecordMessage.CHAT_1202);
			nfsLoanDetailMessage.setType(RecordMessage.SEND_REMIND);
			nfsLoanDetailMessageService.save(nfsLoanDetailMessage);
			return Message.success("借条发送成功，请耐心等待借款人确认借条。");
		}else {
			return Message.error("借条发送失败");
		}
	}
    @RequestMapping(value = "calInterest")
    @RequiresPermissions("ufang:loanApply:view")
    @ResponseBody
    public String calInterest(BigDecimal amount,BigDecimal intRate ,RepayType repayType,Integer term) {

		NfsLoanRecord record = LoanUtils.calInt(amount, intRate, repayType, term);
		return StringUtils.decimalToStr(record.getInterest(), 2);
	}
}