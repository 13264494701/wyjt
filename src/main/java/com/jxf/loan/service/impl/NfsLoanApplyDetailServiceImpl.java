package com.jxf.loan.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.jxf.loan.constant.LoanConstant;
import com.jxf.loan.constant.RecordMessage;
import com.jxf.loan.dao.NfsLoanApplyDetailDao;
import com.jxf.loan.entity.NfsLoanApply;
import com.jxf.loan.entity.NfsLoanApply.LoanRole;
import com.jxf.loan.entity.NfsLoanApply.TrxType;
import com.jxf.loan.entity.NfsLoanApplyDetail;
import com.jxf.loan.entity.NfsLoanApplyDetail.AliveVideoStatus;
import com.jxf.loan.entity.NfsLoanApplyDetail.IntStatus;
import com.jxf.loan.entity.NfsLoanApplyDetail.PayStatus;
import com.jxf.loan.entity.NfsLoanApplyDetail.Status;
import com.jxf.loan.entity.NfsLoanDetailMessage;
import com.jxf.loan.entity.NfsLoanRecord;
import com.jxf.loan.service.NfsLoanApplyDetailService;
import com.jxf.loan.service.NfsLoanApplyService;
import com.jxf.loan.service.NfsLoanDetailMessageService;
import com.jxf.loan.service.NfsLoanRecordService;
import com.jxf.loan.utils.LoanUtils;
import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberAct;
import com.jxf.mem.entity.MemberActTrx;
import com.jxf.mem.entity.MemberMessage;
import com.jxf.mem.service.MemberActService;
import com.jxf.mem.service.MemberActTrxService;
import com.jxf.mem.service.MemberMessageService;
import com.jxf.mem.service.MemberService;
import com.jxf.mms.msg.SendSmsMsgService;
import com.jxf.mms.service.SendMsgService;
import com.jxf.nfs.constant.ActSubConstant;
import com.jxf.nfs.constant.TrxRuleConstant;
import com.jxf.nfs.service.NfsActService;
import com.jxf.payment.entity.Payment;
import com.jxf.payment.service.RefundService;
import com.jxf.svc.cache.RedisUtils;
import com.jxf.svc.config.Constant;
import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
import com.jxf.svc.utils.Collections3;
import com.jxf.svc.utils.StringUtils;
import com.jxf.web.model.ResponseData;

/**
 * 借贷对象ServiceImpl
 * @author wo
 * @version 2018-10-18
 */
@Service("nfsLoanApplyDetailService")
@Transactional(readOnly = true)
public class NfsLoanApplyDetailServiceImpl extends CrudServiceImpl<NfsLoanApplyDetailDao, NfsLoanApplyDetail> implements NfsLoanApplyDetailService{
	
	private static final Logger log = LoggerFactory.getLogger(NfsLoanApplyDetailServiceImpl.class);
	
	@Autowired
	private NfsLoanApplyDetailDao applyDetailDao;
	@Autowired
	private MemberService memberService;
	@Autowired
	private NfsLoanApplyDetailService loanApplyDetailService;
	@Autowired
	private NfsLoanApplyService loanApplyService;
	@Autowired
	private NfsActService actService;
	@Autowired
	private MemberMessageService memberMessageService;
	@Autowired
	private SendMsgService sendMsgService;
	@Autowired
	private NfsLoanDetailMessageService nfsLoanDetailMessageService;
	@Autowired
	private SendSmsMsgService sendSmsMsgService;
	@Autowired
	private MemberActService memberActService;
	@Autowired
	private NfsLoanRecordService loanRecordService;
	@Autowired
	private MemberActTrxService memberActTrxService;
	@Autowired
	private RefundService refundService;
	
	public NfsLoanApplyDetail get(Long id) {
		return super.get(id);
	}
	
	public List<NfsLoanApplyDetail> findList(NfsLoanApplyDetail nfsLoanApplyDetail) {
		return super.findList(nfsLoanApplyDetail);
	}
	
	public Page<NfsLoanApplyDetail> findPage(Page<NfsLoanApplyDetail> page, NfsLoanApplyDetail nfsLoanApplyDetail) {
		return super.findPage(page, nfsLoanApplyDetail);
	}
	
	@Transactional(readOnly = false)
	public void save(NfsLoanApplyDetail nfsLoanApplyDetail) {
		super.save(nfsLoanApplyDetail);
	}
	
	@Transactional(readOnly = false)
	public void delete(NfsLoanApplyDetail nfsLoanApplyDetail) {
		super.delete(nfsLoanApplyDetail);
	}

	@Override
	public Page<NfsLoanApplyDetail> findPage(NfsLoanApplyDetail nfsLoanApplyDetail,
			Integer pageNo, Integer pageSize) {
		Page<NfsLoanApplyDetail> page = new Page<NfsLoanApplyDetail>(pageNo == null?1:pageNo, pageSize == null?10:pageSize);	
		nfsLoanApplyDetail.setPage(page);
		List<NfsLoanApplyDetail> orderList = applyDetailDao.findList(nfsLoanApplyDetail);
		page.setList(orderList);
		return page;
	}

	@Override
	public NfsLoanApplyDetail getByApplyIdAndMemberId(Long applyId,Long memberId) {
		
		return applyDetailDao.getByApplyIdAndMemberId(applyId,memberId);
	}

	@Override
	public String getDetailProgress(NfsLoanApplyDetail detail, NfsLoanApply apply , Member member) {
		boolean isInitiator = false;
		boolean isLender = false;
		String progress = "未知状态;757575";
		Status detailStatus = detail.getStatus();//detail的状态
		IntStatus intStatus = detail.getIntStatus();//协商利息的状态
		LoanRole applyLoanRole = apply.getLoanRole();//主动发起人的角色
		AliveVideoStatus videoStatus = detail.getAliveVideoStatus();//录制视频状态
		if(apply.getMember().equals(member)){//我是发起者 看到的是 被拒绝
			isInitiator = true;
		}
		
		//判断是不是放款人
		if(apply.getMember().equals(member) && apply.getLoanRole().equals(NfsLoanApply.LoanRole.loaner) 
				|| detail.getMember().equals(member) && detail.getLoanRole().equals(NfsLoanApply.LoanRole.loaner) ){
			isLender = true;
		}
		
		if (detailStatus.equals(Status.pendingAgree) && (intStatus.equals(IntStatus.primary) ||intStatus.equals(IntStatus.confirmed)) 
				&& applyLoanRole.equals(LoanRole.loanee) && videoStatus.equals(AliveVideoStatus.notUpload)) {
			progress = isLender ? "待放款;FFAE38" : "待确认;FFAE38";
		} else if (detailStatus.equals(Status.pendingAgree) && intStatus.equals(IntStatus.changed)) {
			progress = "协商利息;FFAE38";
		} else if (detailStatus.equals(Status.pendingAgree) && applyLoanRole.equals(LoanRole.loaner) 
				&& videoStatus.equals(AliveVideoStatus.notUpload)) {
			progress = isLender ? "主动放款待确认;FFAE38" : "待收款;FFAE38";
		} else if (detailStatus.equals(Status.pendingAgree) && 
				(videoStatus.equals(AliveVideoStatus.pendingReUpload) || videoStatus.equals(AliveVideoStatus.pendingUpload))) {
			progress = "待录制视频;FFAE38";
		} else if (detailStatus.equals(Status.pendingAgree) && videoStatus.equals(AliveVideoStatus.hasUpload)) {
			progress = "视频待审核;FFAE38";
		} else if (detailStatus.equals(Status.expired)) {
			progress = "已关闭;757575";
		} else if (detailStatus.equals(Status.reject)) {
			if(isInitiator){//我是发起者 我看到的是被拒绝
				progress = "被拒绝;757575";
			}else{
				progress = "已拒绝;757575";
			}
		} else if (detailStatus.equals(Status.canceled)) {
			progress = "已取消;757575";
		}
		return progress;
	}

	@Override
	@Transactional(readOnly = false)
	public int refusePayment(String detailId, Member member) {
			NfsLoanApplyDetail loanApplyDetail = loanApplyDetailService.get(Long.valueOf(detailId));
			NfsLoanApply nfsLoanApply = loanApplyService.get(loanApplyDetail.getApply());
			loanApplyDetail.setStatus(NfsLoanApplyDetail.Status.reject);
			loanApplyDetail.preUpdate();
			int updateLines = applyDetailDao.update(loanApplyDetail);
			if(updateLines == 0) {
				return Constant.UPDATE_FAILED;
			}
			
			//放款人资金解冻
			int code = actService.updateAct(TrxRuleConstant.UNFROZEN_LOANER_FUNDS, loanApplyDetail.getAmount(), nfsLoanApply.getMember(), loanApplyDetail.getId());
			if(code == Constant.UPDATE_FAILED) {
				throw new RuntimeException("借款人拒绝放款人主动放款，detailId:"+detailId);
			}
			//发送会员消息
			MemberMessage sendMessage = memberMessageService.sendMessage(MemberMessage.Type.refuseLendApplication,loanApplyDetail.getId());
			
			//推送
			sendMsgService.beforeSendAppMsg(sendMessage);
			
			//生成对话
			NfsLoanDetailMessage loanDetailMessage = new NfsLoanDetailMessage();
			loanDetailMessage.setDetail(loanApplyDetail);
			loanDetailMessage.setMember(member);
			loanDetailMessage.setMessageId(RecordMessage.CHAT_1106);
			loanDetailMessage.setType(RecordMessage.BORROWER_REFUSE_LOAN);
			nfsLoanDetailMessageService.save(loanDetailMessage);
		    return Constant.UPDATE_SUCCESS;
	}

	@Override
	@Transactional(readOnly = false)
	public int cancelBorrow(NfsLoanApplyDetail applyDetail, Member member) {
		try {
			AliveVideoStatus videoStatus = applyDetail.getAliveVideoStatus();
			applyDetail.setStatus(NfsLoanApplyDetail.Status.canceled);
			applyDetail.preUpdate();
			int updateLines = applyDetailDao.update(applyDetail);
			if(updateLines == 0) {
				return Constant.UPDATE_FAILED;
			}
			if(videoStatus.equals(AliveVideoStatus.pendingUpload) || videoStatus.equals(AliveVideoStatus.pendingReUpload)) {
				NfsLoanApply apply = loanApplyService.get(applyDetail.getApply());
	
				Member loaner = apply.getLoanRole().equals(LoanRole.loaner)?apply.getMember():applyDetail.getMember();
	
				//放款人放款时要求录视频
				int code = actService.updateAct(TrxRuleConstant.UNFROZEN_LOANER_FUNDS, applyDetail.getAmount(), loaner, applyDetail.getId());
				if(code == Constant.UPDATE_FAILED) {
					throw new RuntimeException("借款人取消借款，放款人资金解冻出现异常。detailId:"+applyDetail.getId());
				}
			}
			MemberMessage sendMessage = memberMessageService.sendMessage(MemberMessage.Type.withdrawloanApplication,applyDetail.getId());
			//推送
			sendMsgService.beforeSendAppMsg(sendMessage);
			
			//生成对话
			NfsLoanDetailMessage nfsLoanDetailMessage = new NfsLoanDetailMessage();
			nfsLoanDetailMessage.setDetail(applyDetail);
			nfsLoanDetailMessage.setMember(member);
			nfsLoanDetailMessage.setMessageId(RecordMessage.CHAT_1103);
			nfsLoanDetailMessage.setType(RecordMessage.CANCEL_LOAN);
			nfsLoanDetailMessageService.save(nfsLoanDetailMessage);
		} catch (Exception e) {
			log.error("server error" + e.toString());
			return Constant.UPDATE_FAILED;
		}
		return Constant.UPDATE_SUCCESS;
	}

	@Transactional(readOnly=false)
	@Override
	public int applyChangeInterest(Member member,Long detailId,String interest) {
		
		NfsLoanApplyDetail nfsLoanApplyDetail = loanApplyDetailService.get(detailId);
		nfsLoanApplyDetail.setIntStatus(NfsLoanApplyDetail.IntStatus.changed);
		nfsLoanApplyDetail.preUpdate();
		int updateLines = applyDetailDao.update(nfsLoanApplyDetail);
		if(updateLines == 0) {
			return Constant.UPDATE_FAILED;
		}
		NfsLoanApply apply = nfsLoanApplyDetail.getApply();
		apply = loanApplyService.get(apply.getId());
		
		RedisUtils.put("memberOftenUse" + member.getId(), "applyChangeInterest" + detailId, interest.toString());	
		
		//生成对话
		Map<String, String> map = new HashMap<String, String>();
		map.put(LoanConstant.OLD_INTEREST, StringUtils.decimalToStr(apply.getInterest(), 2));
		map.put(LoanConstant.NEW_INTEREST, interest.toString());
		String jsonString = JSON.toJSONString(map);
		
		NfsLoanDetailMessage detailMessage = new NfsLoanDetailMessage();
		detailMessage.setDetail(nfsLoanApplyDetail);
		detailMessage.setMember(member);
		detailMessage.setNote(jsonString);
		detailMessage.setMessageId(RecordMessage.CHAT_4);
		detailMessage.setType(RecordMessage.LENDER_MODIFY_INTEREST);
		nfsLoanDetailMessageService.save(detailMessage);
		
		return Constant.UPDATE_SUCCESS;
	}

	@Transactional(readOnly=false)
	@Override
	public int rejectApply(Member member, Long detailId) {
		
		NfsLoanApplyDetail nfsLoanApplyDetail = applyDetailDao.get(detailId);
		NfsLoanApply apply = nfsLoanApplyDetail.getApply();
		apply = loanApplyService.get(apply.getId());
		nfsLoanApplyDetail.setStatus(NfsLoanApplyDetail.Status.reject);
		nfsLoanApplyDetail.preUpdate();
		int updateLines = applyDetailDao.update(nfsLoanApplyDetail);
		if(updateLines == 0) {
			return Constant.UPDATE_FAILED;
		}
		//发送会员消息
		MemberMessage sendMessage = memberMessageService.sendMessage(MemberMessage.Type.refusePayment, detailId);
		
		//推送
		sendMsgService.beforeSendAppMsg(sendMessage);
		
		//生成对话
		NfsLoanDetailMessage nfsLoanDetailMessage = new NfsLoanDetailMessage();
		nfsLoanDetailMessage.setDetail(nfsLoanApplyDetail);
		nfsLoanDetailMessage.setMember(member);
		nfsLoanDetailMessage.setMessageId(RecordMessage.CHAT_1204);
		nfsLoanDetailMessage.setType(RecordMessage.LENDER_REFUSE_LOAN);
		nfsLoanDetailMessageService.save(nfsLoanDetailMessage);
		
		return Constant.UPDATE_SUCCESS;
	}
	
	@Transactional(readOnly=false)
	@Override
	public int replyChangeInterest(Member member, NfsLoanApply apply,NfsLoanApplyDetail detail, int isAgree,BigDecimal interest) {
		
		if(isAgree == 1) {
			detail.setIntStatus(NfsLoanApplyDetail.IntStatus.confirmed);
			detail.preUpdate();
			int updateLines = applyDetailDao.update(detail);
			if(updateLines == 0) {
				return Constant.UPDATE_FAILED;
			}
			BigDecimal intRate = LoanUtils.getIntRate(apply.getAmount(),apply.getTerm(),interest);
			apply.setIntRate(intRate.multiply(new BigDecimal("100")));
			apply.setInterest(interest);
			loanApplyService.save(apply);
			
			//生成对话
			NfsLoanDetailMessage detailMessage = new NfsLoanDetailMessage();
			detailMessage.setDetail(detail);
			detailMessage.setMember(member);
			detailMessage.setMessageId(RecordMessage.CHAT_1109);
			detailMessage.setType(RecordMessage.BORROWER_CONFIRM_INTEREST);
			nfsLoanDetailMessageService.save(detailMessage);
			
		}else {
			detail.setIntStatus(NfsLoanApplyDetail.IntStatus.confirmed);
			detail.preUpdate();
			int updateLines = applyDetailDao.update(detail);
			if(updateLines == 0) {
				return Constant.UPDATE_FAILED;
			}
			//生成对话
			Map<String, String> map = new HashMap<String, String>();
			map.put(LoanConstant.OLD_INTEREST, StringUtils.decimalToStr(apply.getInterest(), 2));
			map.put(LoanConstant.NEW_INTEREST, StringUtils.decimalToStr(interest, 2));
			String jsonString = JSON.toJSONString(map);
			
			NfsLoanDetailMessage detailMessage = new NfsLoanDetailMessage();
			detailMessage.setDetail(detail);
			detailMessage.setMember(member);
			detailMessage.setNote(jsonString);
			detailMessage.setMessageId(RecordMessage.CHAT_1108);
			detailMessage.setType(RecordMessage.BORROWER_REFUSE_INTEREST);
			nfsLoanDetailMessageService.save(detailMessage);
		}
		return Constant.UPDATE_SUCCESS;
	}
	
	@Transactional(readOnly=false)
	@Override
	public int uploadVerifyVideo(Member member, Long detailId, String verifyVideoUrl) {
		
		try {
			NfsLoanApplyDetail nfsLoanApplyDetail = loanApplyDetailService.get(detailId);
			nfsLoanApplyDetail.setVideoUrl(verifyVideoUrl);
			nfsLoanApplyDetail.setAliveVideoStatus(NfsLoanApplyDetail.AliveVideoStatus.hasUpload);
			nfsLoanApplyDetail.preUpdate();
			int updateLines = applyDetailDao.update(nfsLoanApplyDetail);
			if(updateLines == 0) {
				return Constant.UPDATE_FAILED;
			}
			NfsLoanApply nfsLoanApply = loanApplyService.get(nfsLoanApplyDetail.getApply());
			Member loaner;
			if(nfsLoanApply.getLoanRole().equals(NfsLoanApply.LoanRole.loaner)){//放款人主动放款
				loaner = nfsLoanApply.getMember();
			}else{
				loaner = nfsLoanApplyDetail.getMember();
			}
			
			//发对话
			NfsLoanDetailMessage nfsLoanDetailMessage = new NfsLoanDetailMessage();
			nfsLoanDetailMessage.setMember(member);
			nfsLoanDetailMessage.setNote(verifyVideoUrl);
			nfsLoanDetailMessage.setDetail(nfsLoanApplyDetail);
			nfsLoanDetailMessage.setMessageId(RecordMessage.CHAT_1105);
			nfsLoanDetailMessage.setType(RecordMessage.BORROWER_UPLOADED_VIDEO);
			nfsLoanDetailMessageService.save(nfsLoanDetailMessage);

			
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("name", member.getName());
			MemberMessage sendMessage = memberMessageService.sendMessage(MemberMessage.Type.successfulUploadVideo,nfsLoanApplyDetail.getId());

			//推送
			sendMsgService.beforeSendAppMsg(sendMessage);
			
			//短信
			loaner = memberService.get(loaner);
			sendSmsMsgService.sendMessage(MemberMessage.Type.confirmVideo.name(), loaner.getUsername(), map);
		} catch (Exception e) {
			log.error("server error : " + e.getMessage());
			return Constant.UPDATE_FAILED;
		}
		
		return Constant.UPDATE_SUCCESS;
	}

	@Transactional(readOnly=false)
	@Override
	public int verifyVideoNotPass(Member member, NfsLoanApplyDetail detail,Member loanee) {
		detail.setAliveVideoStatus(NfsLoanApplyDetail.AliveVideoStatus.pendingReUpload);
		detail.preUpdate();
		int udpateLinse = applyDetailDao.update(detail);
		if(udpateLinse == 0) {
			return Constant.UPDATE_FAILED;
		}
		MemberMessage sendMessage = memberMessageService.sendMessage(MemberMessage.Type.failVideo,detail.getId());

		//推送
		sendMsgService.beforeSendAppMsg(sendMessage);
		
		//发对话
		NfsLoanDetailMessage nfsLoanDetailMessage = new NfsLoanDetailMessage();
		nfsLoanDetailMessage.setMember(member);
		nfsLoanDetailMessage.setDetail(detail);
		nfsLoanDetailMessage.setMessageId(RecordMessage.CHAT_1208);
		nfsLoanDetailMessage.setType(RecordMessage.LENDER_REVIEWVIDEO_NOTPASS);
		nfsLoanDetailMessageService.save(nfsLoanDetailMessage);
		
		return Constant.UPDATE_SUCCESS;
	}
	
	@Transactional(readOnly=false)
	@Override
	public void changeStatusToExpired(NfsLoanApplyDetail applyDetail) {

		AliveVideoStatus aliveVideoStatus = applyDetail.getAliveVideoStatus();//录视频状态
		if(aliveVideoStatus.equals(AliveVideoStatus.hasUpload)){//已上传未审核的申请 不关闭
			return;
		}
		LoanRole loanRole = applyDetail.getLoanRole();
		NfsLoanApply apply = loanApplyService.get(applyDetail.getApply());
		Member loaner = null;
		if(loanRole.equals(LoanRole.loanee)){
			loaner = apply.getMember();
		}else{
			loaner = applyDetail.getMember();
		}
		if((loanRole.equals(LoanRole.loanee)|| aliveVideoStatus.equals(AliveVideoStatus.pendingReUpload)
				|| aliveVideoStatus.equals(AliveVideoStatus.pendingUpload)) 
				&& applyDetail.getTrxType().equals(NfsLoanApply.TrxType.online)){//主动放款/要求录视频  放款人冻结资金解冻
			actService.updateAct(TrxRuleConstant.UNFROZEN_LOANER_FUNDS, applyDetail.getAmount(), loaner, applyDetail.getId());
		}
		applyDetail.setStatus(NfsLoanApplyDetail.Status.expired);
		applyDetail.setProgress("已关闭;757575");
		loanApplyDetailService.save(applyDetail);
		TrxType trxType = applyDetail.getTrxType();
		if(trxType.equals(TrxType.offline)){//公信堂超时发消息
			//发送会员消息
			if(loanRole.equals(LoanRole.loanee)){
				memberMessageService.sendMessage(MemberMessage.Type.applicationTimeoutLoaner,applyDetail.getId());
			}else{
				memberMessageService.sendMessage(MemberMessage.Type.applicationTimeoutLoanee,applyDetail.getId());
			}
		}
		log.info("借条申请编号：" + applyDetail.getId() + " 当前已过期");
	}
	
	@Override
	public Map<String, Long> getLendingLoanAndAmount(Map<String, Object> paramMap) {
		
		return applyDetailDao.getLendingLoanAndAmount(paramMap);
	}

	@Override
	@Transactional(readOnly=false)
	public int loanerRefuseLoanApply(NfsLoanApplyDetail applyDetail) {
		NfsLoanApply apply = loanApplyService.get(applyDetail.getApply());
		Member loanee = memberService.get(apply.getMember());
		Long orgId = applyDetail.getId();
		Long loaneeId = loanee.getId();
		//更改detail状态
		applyDetail.setStatus(NfsLoanApplyDetail.Status.reject);
		applyDetail.preUpdate();
		int updateLines = applyDetailDao.update(applyDetail);
		if(updateLines == 0) {
			return Constant.UPDATE_FAILED;
		}
		//退款
		List<MemberActTrx> list = memberActTrxService.findListByOrgId(orgId);
		for (MemberActTrx memberActTrx : list) {
			BigDecimal amount = memberActTrx.getTrxAmt();
			if(memberActTrx.getTrxCode().equals(TrxRuleConstant.GXT_LOAN_DONE_WXPAY)) {
				//调用微信退款接口 添加微信退款流水
				boolean result = refundService.wxRefund(loanee, Payment.Type.loanDone, orgId, amount);
				if(result) {
					//生成退款流水
					int code = actService.updateAct(TrxRuleConstant.GXT_REFUSE_LOAN_REFUND_WXPAY, amount, loanee, orgId);
					if(code == Constant.UPDATE_FAILED) {
						logger.error("放款人拒绝借款人补借条申请detail:{}，借款人:{}的微信账户更新失败！",orgId,loaneeId);
						throw new RuntimeException("放款人拒绝借款人补借条申请detail:"+orgId+"，借款人"+loaneeId+"的微信账户更新失败！");
					}
				}else {
					logger.error("放款人拒绝借款人补借条申请detail:{}，微信请求退款失败！",orgId,loaneeId);
					throw new RuntimeException("放款人拒绝借款人补借条申请detail:"+orgId+"，微信请求退款失败！");
				}
			}else if(memberActTrx.getTrxCode().equals(TrxRuleConstant.GXT_LOAN_DONE_LOANACT)){
				//退款到借款账户
				int code = actService.updateAct(TrxRuleConstant.GXT_REFUSE_LOAN_REFUND_LOANACT, amount, loanee, orgId);
				if(code == Constant.UPDATE_FAILED) {
					logger.error("放款人拒绝借款人补借条申请detail:{}操作，借款人{}的借款账户退款失败！",orgId,loaneeId);
					throw new RuntimeException("放款人拒绝借款人补借条申请detail:"+orgId+"，借款人"+loaneeId+"的借款账户退款失败！");
				}
			}else if(memberActTrx.getTrxCode().equals(TrxRuleConstant.GXT_LOAN_DONE_AVLACT)) {
				//退款到可用余额账户
				int code = actService.updateAct(TrxRuleConstant.GXT_REFUSE_LOAN_REFUND_AVLACT, amount, loanee, orgId);
				if(code == Constant.UPDATE_FAILED) {
					logger.error("放款人拒绝借款人补借条申请detail:{}操作，借款人{}的可用余额账户退款失败！",orgId,loaneeId);
					throw new RuntimeException("放款人拒绝借款人补借条申请detail:"+orgId+"，借款人"+loaneeId+"的可用余额账户退款失败！");
				}
			}else {
				logger.warn("detail{}放款人拒绝，退款时流水账户为{}",orgId,memberActTrx.getSubNo());
				continue;
			}
		}
		return Constant.UPDATE_SUCCESS;
	}

	@Override
	@Transactional(readOnly=false)
	public int loaneeCancelLoanApply(NfsLoanApplyDetail detail) {
		NfsLoanApply apply = loanApplyService.get(detail.getApply());
		Member loanee = memberService.get(apply.getMember());
		Long orgId = detail.getId();
		Long loaneeId = loanee.getId();
		//更改detail状态
		detail.setStatus(NfsLoanApplyDetail.Status.canceled);
		detail.preUpdate();
		int updateLines = applyDetailDao.update(detail);
		if(updateLines == 0) {
			return Constant.UPDATE_FAILED;
		}
		//退款
		List<MemberActTrx> list = memberActTrxService.findListByOrgId(orgId);
		for (MemberActTrx memberActTrx : list) {
			BigDecimal amount = memberActTrx.getTrxAmt();
			if (memberActTrx.getTrxCode().equals(TrxRuleConstant.GXT_LOAN_DONE_WXPAY)) {
				//调用微信退款接口 添加微信退款流水
				boolean result = refundService.wxRefund(loanee, Payment.Type.loanDone, orgId, amount);
				if(result) {
					//生成退款流水
					int code = actService.updateAct(TrxRuleConstant.GXT_CANCEL_LOAN_REFUND_WXPAY, amount, loanee, orgId);
					if(code == Constant.UPDATE_FAILED) {
						logger.error("借款人：{}取消补借条申请detail:{}，微信账户更新失败！",loaneeId,orgId);
						throw new RuntimeException("借款人" + loanee.getId() + "取消补借条申请" + orgId + "，微信账户更新失败！");
					}
				}else {
					logger.error("借款人：{}取消补借条申请detail:{}，微信请求退款失败！",loaneeId,orgId);
					throw new RuntimeException("借款人" + loanee.getId() + "取消补借条申请" + orgId + "，微信请求退款失败！");
				}
			} else if (memberActTrx.getTrxCode().equals(TrxRuleConstant.GXT_LOAN_DONE_LOANACT)) {
				// 退款到借款账户
				int code = actService.updateAct(TrxRuleConstant.GXT_CANCEL_LOAN_REFUND_LOANACT, amount, loanee, orgId);
				if (code == Constant.UPDATE_FAILED) {
					logger.error("借款人:{}取消补借条申请detail:{}，借款人{}的借款账户退款失败！",  loanee.getId(),orgId);
					throw new RuntimeException("借款人" + loanee.getId() + "取消补借条申请" + orgId + "，借款账户退款失败！");
				}
			} else if (memberActTrx.getTrxCode().equals(TrxRuleConstant.GXT_LOAN_DONE_AVLACT)) {
				// 退款到可用余额账户
				int code = actService.updateAct(TrxRuleConstant.GXT_CANCEL_LOAN_REFUND_AVLACT, amount, loanee, orgId);
				if (code == Constant.UPDATE_FAILED) {
					logger.error("借款人:{}取消补借条申请detail:{}，可用余额账户退款失败！", loanee.getId(),orgId);
					throw new RuntimeException("借款人" + loanee.getId() + "取消补借条申请" + orgId + "，可用余额账户退款失败！");
				}
			} else {
				logger.warn("detail{}借款人取消，退款时流水账户为{}", orgId, memberActTrx.getSubNo());
				continue;
			}
		}
		return Constant.UPDATE_SUCCESS;
	}

	@Override
	@Transactional(readOnly=false)
	public void loanApplyOutTime(NfsLoanApplyDetail detail) {
		//改状态
		detail.setStatus(NfsLoanApplyDetail.Status.expired);
		detail.preUpdate();
		applyDetailDao.update(detail);
		
		NfsLoanApply apply = loanApplyService.get(detail.getApply());
		if(apply.getLoanRole().equals(NfsLoanApply.LoanRole.loanee)) {
			Member loanee = memberService.get(apply.getMember());
			Long orgId = detail.getId();
			Long loaneeId = loanee.getId();
			
			//退款
			List<MemberActTrx> list = memberActTrxService.findListByOrgId(orgId);
			for (MemberActTrx memberActTrx : list) {
				BigDecimal amount = memberActTrx.getTrxAmt();
				if (memberActTrx.getTrxCode().equals(TrxRuleConstant.GXT_LOAN_DONE_WXPAY)) {
					//调用微信退款接口 添加微信退款流水
					boolean result = refundService.wxRefund(loanee, Payment.Type.loanDone, orgId, amount);
					if(result) {
						//生成退款流水
						int code = actService.updateAct(TrxRuleConstant.GXT_TIMEOUT_LOAN_REFUND_WXPAY, amount, loanee, orgId);
						if(code == Constant.UPDATE_FAILED) {
							logger.error("借款人：{}打借条申请超时detail:{}，微信账户更新失败！",loaneeId,orgId);
							throw new RuntimeException("借款人" + loanee.getId() + "打借条申请超时" + orgId + "，微信账户更新失败！");
						}
					}else {
						logger.error("借款人：{}打借条申请超时detail:{}，微信请求退款失败！",loaneeId,orgId);
						throw new RuntimeException("借款人" + loanee.getId() + "打借条申请超时" + orgId + "，微信请求退款失败！");
					}
				} else if (memberActTrx.getTrxCode().equals(TrxRuleConstant.GXT_LOAN_DONE_LOANACT)) {
					// 退款到借款账户
					int code = actService.updateAct(TrxRuleConstant.GXT_TIMEOUT_LOAN_REFUND_LOANACT, amount, loanee, orgId);
					if (code == Constant.UPDATE_FAILED) {
						logger.error("借款人:{}打借条申请超时detail:{}，借款人{}的借款账户退款失败！",  loanee.getId(),orgId);
						throw new RuntimeException("借款人" + loanee.getId() + "打借条申请超时" + orgId + "，借款账户退款失败！");
					}
				} else if (memberActTrx.getTrxCode().equals(TrxRuleConstant.GXT_LOAN_DONE_AVLACT)) {
					// 退款到可用余额账户
					int code = actService.updateAct(TrxRuleConstant.GXT_TIMEOUT_LOAN_REFUND_AVLACT, amount, loanee, orgId);
					if (code == Constant.UPDATE_FAILED) {
						logger.error("借款人:{}打借条申请超时detail:{}，可用余额账户退款失败！", loanee.getId(),orgId);
						throw new RuntimeException("借款人" + loanee.getId() + "打借条申请超时" + orgId + "，可用余额账户退款失败！");
					}
				} else {
					logger.warn("detail{}打借条申请超时，退款时流水账户为{}", orgId, memberActTrx.getSubNo());
					continue;
				}
			}
			
		}
	}
	
	@Override
	@Transactional(readOnly=false)
	public boolean loaneePayForApplyByWx(Payment payment) {
		Long detailId = payment.getOrgId();
		NfsLoanApplyDetail applyDetail = get(detailId);
		if(!applyDetail.getPayStatus().equals(NfsLoanApplyDetail.PayStatus.waitingPay)) {
			log.error("借条申请detail:{}支付状态已改变，现在的支付状态：{}",detailId,applyDetail.getPayStatus());
			return false;
		}
		if(!applyDetail.getStatus().equals(NfsLoanApplyDetail.Status.pendingAgree)) {
			log.error("借条申请detail:{}状态已改变，现在的状态：{}",detailId,applyDetail.getStatus());
			return false;
		}
		Member loanee = memberService.get(payment.getPrincipalId());
		LoanRole memberRole = applyDetail.getLoanRole();
		if(payment.getStatus().equals(Payment.Status.failure)) {
			//如果是支付失败
			if(memberRole.equals(LoanRole.loanee)) {
				//借款人申请补借条 删除该支付失败的借条
				applyDetail.setPayStatus(PayStatus.fail);
				loanApplyDetailService.delete(applyDetail);
				loanApplyService.delete(applyDetail.getApply());
			}
			return false;
		}
		//获取微信账户
		MemberAct memberAct = memberActService.getMemberAct(loanee, ActSubConstant.MEMBER_WEIXIN_PAYMENT);
		if (memberAct == null) {
			//生成微信账户
			MemberAct memberAct2 = new MemberAct();
			memberAct2.setMember(loanee);
			memberAct2.setCurBal(BigDecimal.ZERO);
			memberAct2.setCurrCode("CNY");
			memberAct2.setName("微信账户");
			memberAct2.setSubNo("0007");
			memberAct2.setStatus(MemberAct.Status.enabled);
			memberAct2.setIsDefault(false);
			memberActService.save(memberAct2);
		}
		
		//微信支付服务费
		BigDecimal fee = new BigDecimal(Global.getConfig("gxt.loanDoneFee"));
		int code = actService.updateAct(TrxRuleConstant.GXT_LOAN_DONE_WXPAY, fee, loanee, detailId);
		if(code == Constant.UPDATE_FAILED) {
			logger.error("公信堂会员{}申请补借条微信账户更新失败！",loanee.getId());
			throw new RuntimeException("公信堂会员"+loanee.getId()+"申请补借条微信账户更新失败！");
		}
		//变更detail状态，如果是放款人申请补借条就生成借条
		applyDetail.setPayStatus(PayStatus.success);
		if(applyDetail.getLoanRole() == LoanRole.loanee) {
			applyDetail.setMember(loanee);
			//生成借条
			loanRecordService.createLoanRecordForOffLine(applyDetail);
		}else {
			//只变更detail状态
			save(applyDetail);
		}
		return true;
	}

	@Override
	@Transactional(readOnly=false)
	public ResponseData loaneePayForApplyByActBal(NfsLoanApplyDetail applyDetail) {
		Map<String, String> data = new HashMap<String,String>();
		BigDecimal fee = new BigDecimal(Global.getConfig("gxt.loanDoneFee"));
		Long orgId = applyDetail.getId();
		Member loanee = applyDetail.getMember();
		LoanRole loanRole = applyDetail.getLoanRole();
		if(loanRole.equals(LoanRole.loaner)) {
			NfsLoanApply apply = loanApplyService.get(applyDetail.getApply());
			loanee = memberService.get(apply.getMember());
		}
		//变更账户余额
		BigDecimal loanActBal = memberActService.getMemberAct(loanee, ActSubConstant.MEMBER_LOAN_BAL).getCurBal();
		//优先使用借款账户余额支付
		if(loanActBal.compareTo(fee) >= 0) {
			//借款账户余额足够
			int code = actService.updateAct(TrxRuleConstant.GXT_LOAN_DONE_LOANACT, fee, loanee, orgId);
			if(code == Constant.UPDATE_FAILED) {
				logger.error("公信堂会员:{}补借条申请detail:{}借款账户扣款失败！",loanee.getId(),orgId);
				throw new RuntimeException("公信堂会员"+loanee.getId()+"同意补借条申请"+orgId+"借款账户扣款失败！");
			}
		}else {
			//借款账户没钱  全部使用可用余额账户的余额支付
				if(loanActBal.compareTo(BigDecimal.ZERO) == 0) {
					int code = actService.updateAct(TrxRuleConstant.GXT_LOAN_DONE_AVLACT, fee, loanee, orgId);
				if(code == Constant.UPDATE_FAILED) {
					logger.error("公信堂会员:{}补借条申请detail:{}可用余额账户扣款失败！",loanee.getId(),orgId);
					throw new RuntimeException("公信堂会员"+loanee.getId()+"同意补借条申请"+orgId+"可用余额账户扣款失败！");
				}
			}else {
				//先扣除借款账户余额 再扣除可用余额账户
				int code = actService.updateAct(TrxRuleConstant.GXT_LOAN_DONE_LOANACT, loanActBal, loanee, orgId);
				if(code == Constant.UPDATE_FAILED) {
					logger.error("公信堂会员:{}补借条申请detail:{}借款账户扣款失败！",loanee.getId(),orgId);
					throw new RuntimeException("公信堂会员"+loanee.getId()+"同意补借条申请"+orgId+"借款账户扣款失败！");
				}
				BigDecimal amountFromAvlActBal = fee.subtract(loanActBal);
				int code2 = actService.updateAct(TrxRuleConstant.GXT_LOAN_DONE_AVLACT, amountFromAvlActBal, loanee, orgId);
				if(code2 == Constant.UPDATE_FAILED) {
					logger.error("公信堂会员:{}同意补借条申请detail:{}可用余额账户扣款失败！",loanee.getId(),orgId);
					throw new RuntimeException("公信堂会员"+loanee.getId()+"同意补借条申请"+orgId+"可用余额账户扣款失败！");
				}
			}
		}
		applyDetail.setPayStatus(PayStatus.success);
		if(applyDetail.getLoanRole().equals(LoanRole.loanee)) {
			//借款人同意放款人的补借条申请
			//变更detail 状态 status payStatus
			NfsLoanRecord loanRecord = loanRecordService.createLoanRecordForOffLine(applyDetail);
			data.put("loan_type", loanRecord.getId()+"_2");
		}else{
			//借款人主动申请补借条
			save(applyDetail);
			data.put("loan_type", applyDetail.getId()+"_1");
		}
		return ResponseData.success("支付成功",data);
	}

	@Override
	public List<NfsLoanApplyDetail> findLoanOutTimeList() {
		
		return applyDetailDao.findLoanOutTimeList();
	}

	
}