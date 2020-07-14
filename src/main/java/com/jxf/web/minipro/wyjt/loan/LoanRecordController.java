package com.jxf.web.minipro.wyjt.loan;




import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

import java.util.Map;

import org.apache.commons.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jxf.loan.entity.NfsLoanApply;
import com.jxf.loan.entity.NfsLoanApplyDetail;
import com.jxf.loan.entity.NfsLoanRecord;
import com.jxf.loan.service.NfsLoanApplyDetailService;
import com.jxf.loan.service.NfsLoanRecordService;
import com.jxf.mem.entity.Member;

import com.jxf.mem.service.MemberActService;
import com.jxf.mem.service.MemberService;
import com.jxf.mem.utils.MemUtils;

import com.jxf.svc.persistence.Page;
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
@Controller("wyjtMiniproLoanRecordController")
@RequestMapping(value="${wyjtMinipro}/loan")
public class LoanRecordController extends BaseController {
	
	@Autowired
	private WxUserInfoService wxUserInfoService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private NfsLoanRecordService loanRecordService;
	@Autowired
	private MemberActService memberActService;
	@Autowired
	private NfsLoanApplyDetailService nfsLoanApplyDetailService;

	
	/**
	 * 查询会员借条列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public @ResponseBody
	ResponseData applylist(String role,String status,Integer pageNo, Integer pageSize) {

		WxUserInfo wxUserInfo = wxUserInfoService.getCurrent();
		Member member = memberService.get(wxUserInfo.getMember());
		NfsLoanRecord loanRecord = new NfsLoanRecord();
		if(StringUtils.equals(role, "0")) {
			loanRecord.setLoaner(member);
		}else {
			loanRecord.setLoanee(member);
		}
		if(StringUtils.equals(status, "0")){
			loanRecord.setStatus(NfsLoanRecord.Status.penddingRepay);
			Page<NfsLoanRecord> page = loanRecordService.findPage(loanRecord, pageNo, pageSize);	
			return ResponseData.success("查询借条列表成功", page);
		}else if(StringUtils.equals(status, "1")){
			loanRecord.setStatus(NfsLoanRecord.Status.repayed);
			Page<NfsLoanRecord> page = loanRecordService.findPage(loanRecord, pageNo, pageSize);	
			return ResponseData.success("查询借条列表成功", page);
		}else if(StringUtils.equals(status, "2")){
			loanRecord.setStatus(NfsLoanRecord.Status.overdue);
			Page<NfsLoanRecord> page = loanRecordService.findPage(loanRecord, pageNo, pageSize);	
			return ResponseData.success("查询借条列表成功", page);
		}else{//待确定
			NfsLoanApplyDetail nfsLoanApplyDetail = new NfsLoanApplyDetail();
			nfsLoanApplyDetail.setMember(member);
			nfsLoanApplyDetail.setLoanRole(NfsLoanApply.LoanRole.loanee);
			nfsLoanApplyDetail.setStatus(NfsLoanApplyDetail.Status.pendingAgree);
			Page<NfsLoanApplyDetail> page = nfsLoanApplyDetailService.findPage(nfsLoanApplyDetail, pageNo, pageSize);	
			return ResponseData.success("查询借条列表成功", page);
		}
	}
	/**
	 * 借条详情
	 */
	@RequestMapping(value = "/detail", method = RequestMethod.GET)
	public @ResponseBody
	ResponseData applydetail(String id) {
		
		Map<String, Object> data = new HashMap<String, Object>();
		WxUserInfo wxUserInfo = wxUserInfoService.getCurrent();
		Member member = memberService.get(wxUserInfo.getMember());
		NfsLoanRecord loanRecord = loanRecordService.get(Long.valueOf(id));
		//数据脱敏
		Member loanee = loanRecord.getLoanee();
		loanRecord.setLoanee(MemUtils.mask(loanee));
		loanRecord.setLoaner(MemUtils.mask(loanRecord.getLoaner()));
		loanRecord.setNowDate(new Date());
		if(loanRecord.getLoanee().equals(member)) {
			data.put("role", "loanee");
			data.put("loan", loanRecord);
		}else if(loanRecord.getLoaner().equals(member)) {
			data.put("role", "loaner");
			data.put("loan", loanRecord);
		}else {
			data.put("role", "other");
		}
		return ResponseData.success("查询借条详情成功", data);
	}
	/**
	 * 全部还款
	 */
	@RequestMapping(value = "/loanAllRepay", method = RequestMethod.POST)
	public @ResponseBody
	ResponseData loanAllRepay(String payNum,String loanId) {
		Long id = Long.valueOf(loanId);
		WxUserInfo wxUserInfo = wxUserInfoService.getCurrent();
		Member member = memberService.get(wxUserInfo.getMember());
		NfsLoanRecord nfsLoanRecord = loanRecordService.get(id);
		if(nfsLoanRecord.getStatus().equals(NfsLoanRecord.Status.repayed)){
			return ResponseData.error("该借条已完成还款");
		}
		//校验支付密码
		if(!member.getPayPassword().equals(payNum)){
			return ResponseData.error("支付密码不对");
		}
		//校验可用余额
		BigDecimal avlBal = memberActService.getAvlBal(member);
		BigDecimal dueRepayAmount = nfsLoanRecord.getDueRepayAmount();
		if(avlBal.compareTo(dueRepayAmount)<0){
			return ResponseData.error("可用余额不足");
		}
//		MemberAct memberAct = new MemberAct();
		//更新借款人账户
//		memberAct.setMember(member);
//		List<MemberAct> actList = memberActService.findList(memberAct);
//		for(MemberAct act :actList) {
//			if(StringUtils.equals(act.getSubNo(), "0001")) {//可用账户
//				BigDecimal availableMoney2 = act.getCurBal().subtract(dueRepayAmount);
//				act.setCurBal(availableMoney2);
//				memberActService.save(act);//更新数据库
//				MemberActTrx memberActTrx = new MemberActTrx();//账户交易明细
//				memberActTrx.setDrc("C");
//				memberActTrx.setMemberAct(act);
//				memberActTrx.setAmount(dueRepayAmount);
//				memberActTrx.setBusinessId(id);
//				//memberActTrx.setType(MemberActTrx.Type.CONSUME);
//				memberActTrx.setStatus(MemberActTrx.Status.SUCC);
//				memberActTrxService.save(memberActTrx);
//				RedisUtils.put("memberOftenUse" + member.getId(), Member.AVL_BAL,availableMoney2.toString());//更新缓存
//			}else if(StringUtils.equals(act.getSubNo(), "0005")) {//待还总额
//				BigDecimal pendingRepayment = act.getCurBal().subtract(dueRepayAmount);
//				act.setCurBal(pendingRepayment);
//				memberActService.save(act);//更新数据库
//				MemberActTrx memberActTrx = new MemberActTrx();//账户交易明细
//				memberActTrx.setDrc("C");
//				memberActTrx.setMemberAct(act);
//				memberActTrx.setAmount(dueRepayAmount);
//				memberActTrx.setBusinessId(id);
//				//memberActTrx.setType(MemberActTrx.Type.CONSUME);
//				memberActTrx.setStatus(MemberActTrx.Status.SUCC);
//				memberActTrxService.save(memberActTrx);
//				RedisUtils.put("memberInfo" + member.getId(), Member.PENDING_REPAYMENT,pendingRepayment.toString());//更新缓存
//			}
//		}
//		//更新放款人账户
//		Member loaner = nfsLoanRecord.getLoaner();//放款人
//		memberAct.setMember(loaner);
//		List<MemberAct> actList2 = memberActService.findList(memberAct);
//		for(MemberAct act :actList2) {
//			if(StringUtils.equals(act.getSubNo(), "0001")) {//可用账户
//				act.setCurBal(act.getCurBal().add(dueRepayAmount));
//				memberActService.save(act);//更新数据库
//			}
//		}
		nfsLoanRecord.setStatus(NfsLoanRecord.Status.repayed);
		loanRecordService.save(nfsLoanRecord);
		return ResponseData.success("全部还款成功", nfsLoanRecord);
	}
	

}