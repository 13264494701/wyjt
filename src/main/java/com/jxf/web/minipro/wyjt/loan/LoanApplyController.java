package com.jxf.web.minipro.wyjt.loan;



import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jxf.fee.service.NfsFeeRuleService;
import com.jxf.loan.entity.NfsLoanApply;
import com.jxf.loan.entity.NfsLoanApplyDetail;
import com.jxf.loan.entity.NfsLoanContract;
import com.jxf.loan.entity.NfsLoanRecord;
import com.jxf.loan.service.NfsLoanApplyDetailService;
import com.jxf.loan.service.NfsLoanApplyService;
import com.jxf.loan.service.NfsLoanContractService;
import com.jxf.loan.service.NfsLoanRecordService;
import com.jxf.loan.utils.LoanUtils;
import com.jxf.mem.entity.Member;

import com.jxf.mem.service.MemberService;
import com.jxf.mem.utils.MemUtils;
import com.jxf.nfs.constant.TrxRuleConstant;

import com.jxf.nfs.service.NfsActService;
import com.jxf.svc.cache.RedisUtils;
import com.jxf.svc.config.Constant;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.utils.CalendarUtil;
import com.jxf.web.minipro.BaseController;
import com.jxf.web.model.ResponseData;
import com.jxf.wx.user.entity.WxUserInfo;
import com.jxf.wx.user.service.WxUserInfoService;


/**
 * Controller - 内容管理
 * 
 * @author JINXINFU
 * @version 2.0
 */
@Controller("wyjtMiniproLoanApplyController")
@RequestMapping(value="${wyjtMinipro}/loan")
public class LoanApplyController extends BaseController {
	
	@Autowired
	private MemberService memberService;
	@Autowired
	private WxUserInfoService wxUserInfoService;

	@Autowired
	private NfsLoanApplyService loanApplyService;
	@Autowired
	private NfsLoanApplyDetailService loanApplyDetailService;
	@Autowired
	private NfsLoanRecordService loanRecordService;
	@Autowired
	private NfsActService actService;
	@Autowired
	private NfsFeeRuleService feeRuleService;
	@Autowired
	private NfsLoanContractService loanContractService;

	/**
	 * 借款申请
	 */
	@RequestMapping(value = "/apply", method = RequestMethod.POST)
	public @ResponseBody
	ResponseData apply(@RequestBody NfsLoanApply loanApply) {
		
		WxUserInfo wxUserInfo = wxUserInfoService.getCurrent();
		Member member = memberService.get(wxUserInfo.getMember());
		loanApply.setMember(MemUtils.mask(member));
		NfsLoanRecord loanRecord = LoanUtils.calInt(loanApply.getAmount(),loanApply.getIntRate(),loanApply.getRepayType(),loanApply.getTerm());
		loanApply.setInterest(loanRecord.getInterest());
		loanApply.setRepayAmt(loanRecord.getDueRepayAmount());
		loanApplyService.save(loanApply);		

		return ResponseData.success("借款申请提交成功", loanApply);
	}
	
	/**
	 * 查询会员借款申请列表
	 */
	@RequestMapping(value = "/applylist", method = RequestMethod.GET)
	public @ResponseBody
	ResponseData applylist(Integer pageNo, Integer pageSize) {

		Member member = memberService.getCurrent();
		NfsLoanApply loanApply = new NfsLoanApply();
		loanApply.setMember(member);
	
		Page<NfsLoanApply> page = loanApplyService.findPage(loanApply, pageNo, pageSize);		
	
		return ResponseData.success("查询借款申请列表成功", page);
	}
	/**
	 * 借款申请详情
	 */
	@RequestMapping(value = "/applydetail", method = RequestMethod.GET)
	public @ResponseBody
	ResponseData applydetail(Long applyId) {
		WxUserInfo wxUserInfo = wxUserInfoService.getCurrent();
		Member member = memberService.get(wxUserInfo.getMember());
		NfsLoanApply loanApply = loanApplyService.get(applyId);
		loanApply.setMember(MemUtils.mask(memberService.get(loanApply.getMember())));
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("loanApply", loanApply);
		result.put("member", member);
		return ResponseData.success("查询借款申请详情成功", result);
	}
	
	/**
	 * 借款支付
	 */
	@RequestMapping(value = "/pay", method = RequestMethod.POST)
	public @ResponseBody
	ResponseData pay(Long applyId,String payPw) {
		
		Map<String, Object> data = new HashMap<String, Object>();
		
		WxUserInfo wxUserInfo = wxUserInfoService.getCurrent();
		Member loaner = memberService.get(wxUserInfo.getMember());//放款人
		/**
		 *  验证支付密码
		 * **/
		if(!StringUtils.equals(loaner.getPayPassword(), payPw)){
			return ResponseData.error("支付密码不对");
		}
		NfsLoanApply loanApply = loanApplyService.get(applyId);
		if(!loanApply.getLoanRole().equals(NfsLoanApply.LoanRole.loanee)) {
			return ResponseData.error("借款支付对象必须是借款人");
		}
		NfsLoanApplyDetail detail = new NfsLoanApplyDetail();
		detail.setApply(loanApply);
		detail.setStatus(NfsLoanApplyDetail.Status.success);
		detail.setIntStatus(NfsLoanApplyDetail.IntStatus.primary);
		detail.setAliveVideoStatus(NfsLoanApplyDetail.AliveVideoStatus.notUpload);
		detail.setProgress("已完成;7FC153");
		detail.setMember(loaner);
		detail.setLoanRole(NfsLoanApply.LoanRole.loaner);
		detail.setAmount(loanApply.getAmount());
		loanApplyDetailService.save(detail);
		BigDecimal amount = loanApply.getAmount();//本金
		Member loanee = loanApply.getMember();
		NfsLoanRecord loanRecord = new NfsLoanRecord();
		String process = loanApply.getTerm()>30?"30;ED2E24|日以上还款;757575":loanApply.getTerm()+";ED2E24|日内还款;757575";
		loanRecord.setLoaner(loaner);
		loanRecord.setLoanee(loanee);
		loanRecord.setLoanType(loanApply.getLoanType());
		loanRecord.setLoanPurp(loanApply.getLoanPurp());
		loanRecord.setRepayType(loanApply.getRepayType());
		loanRecord.setAmount(loanApply.getAmount());
		loanRecord.setIntRate(loanApply.getIntRate());
		loanRecord.setInterest(loanApply.getInterest());
		loanRecord.setTerm(loanApply.getTerm());
		loanRecord.setDueRepayAmount(loanApply.getAmount().add(loanApply.getInterest()));
		loanRecord.setDueRepayDate(CalendarUtil.addDay(new Date(), loanApply.getTerm()));
		loanRecord.setStatus(NfsLoanRecord.Status.penddingRepay);
		loanRecord.setChannel(loanApply.getChannel());
		loanRecord.setLoanApplyDetail(detail);
		loanRecord.setProgress(process);
//		loanRecord.setSignatureStatus(NfsLoanRecord.SignatureStatus.unauth);
		loanRecordService.save(loanRecord);
		//账户变动	
		BigDecimal fee = feeRuleService.getFee(TrxRuleConstant.LOAN_DONE_FEE, amount);
		int code = actService.updateAct(TrxRuleConstant.LOAN_DONE_AVLAMT, amount, loaner, loanee, loanRecord.getId());
		if(code == Constant.UPDATE_FAILED) {
			return ResponseData.error("账户处理失败");
		}
		code = actService.updateAct(TrxRuleConstant.LOAN_DONE_REPAY, loanApply.getRepayAmt(), loaner, loanee, detail.getId());
		if(code == Constant.UPDATE_FAILED) {
			return ResponseData.error("账户处理失败");
		}
		code = actService.updateAct(TrxRuleConstant.LOAN_DONE_FEE,fee,loanee, detail.getId());
		if(code == Constant.UPDATE_FAILED) {
			return ResponseData.error("账户处理失败");
		}
		
		//生成电子合同
		NfsLoanContract loanContract = new NfsLoanContract();
		loanContract.setLoanId(loanRecord.getId());
		loanContract.setSignatureType(NfsLoanContract.SignatureType.youdun);
		loanContract.setStatus(NfsLoanContract.Status.notCreate);
		loanContractService.save(loanContract);
		
		data.put("loanRecord", loanRecord);
		BigDecimal curBal = (BigDecimal)RedisUtils.getHashKey("memberInfo" + loaner.getId(), "curBal");
		data.put("curBal", curBal.setScale(2,BigDecimal.ROUND_HALF_UP));
		return ResponseData.success("借款支付成功", data);
	}
}