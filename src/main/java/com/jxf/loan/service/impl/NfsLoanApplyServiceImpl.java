package com.jxf.loan.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.loan.constant.RecordMessage;
import com.jxf.loan.dao.NfsLoanApplyDao;
import com.jxf.loan.dao.NfsLoanApplyDetailDao;
import com.jxf.loan.entity.NfsLoanApply;
import com.jxf.loan.entity.NfsLoanApply.LoanRole;
import com.jxf.loan.entity.NfsLoanApplyDetail;
import com.jxf.loan.entity.NfsLoanDetailMessage;
import com.jxf.loan.entity.NfsLoanRecord;
import com.jxf.loan.service.NfsLoanApplyService;
import com.jxf.loan.service.NfsLoanDetailMessageService;
import com.jxf.loan.service.NfsLoanRecordService;
import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberMessage;
import com.jxf.mem.service.MemberActService;
import com.jxf.mem.service.MemberFriendRelationService;
import com.jxf.mem.service.MemberMessageService;
import com.jxf.mem.service.MemberService;
import com.jxf.mem.utils.VerifiedUtils;
import com.jxf.mms.msg.SendSmsMsgService;
import com.jxf.nfs.constant.TrxRuleConstant;
import com.jxf.nfs.service.NfsActService;
import com.jxf.svc.config.Constant;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.StringUtils;
import com.jxf.web.model.ResponseData;

/**
 * 借款申请ServiceImpl
 * @author wo
 * @version 2018-09-26
 */
@Service("nfsLoanApplyService")
@Transactional(readOnly = true)
public class NfsLoanApplyServiceImpl extends CrudServiceImpl<NfsLoanApplyDao, NfsLoanApply> implements NfsLoanApplyService{

	@Autowired
	private MemberService memberService;
	@Autowired
	private NfsLoanApplyDao loanApplyDao;
	@Autowired
	private NfsLoanApplyDetailDao applyDetailDao;
	@Autowired
	private NfsLoanRecordService nfsLoanRecordService;
	@Autowired
	private MemberFriendRelationService memberFriendRelationService;
	@Autowired
	private NfsActService actService;
	@Autowired
	private MemberMessageService memberMessageService;
	@Autowired
	private SendSmsMsgService sendSmsMsgService;
	@Autowired
	private NfsLoanDetailMessageService nfsLoanDetailMessageService;
	@Autowired
	private MemberActService memberActService;
	
	
	public NfsLoanApply get(Long id) {
		return super.get(id);
	}
	
	public List<NfsLoanApply> findList(NfsLoanApply nfsLoanApply) {
		return super.findList(nfsLoanApply);
	}
	
	public Page<NfsLoanApply> findPage(Page<NfsLoanApply> page, NfsLoanApply nfsLoanApply) {
		return super.findPage(page, nfsLoanApply);
	}
	@Override
	public Page<NfsLoanApply> findSingleLoanApplyPage(Page<NfsLoanApply> page, NfsLoanApply nfsLoanApply) {
		nfsLoanApply.setPage(page);
		List<NfsLoanApply> applyList = loanApplyDao.findSingleLoanApplyList(nfsLoanApply);
		page.setList(applyList);
		return page;
	}

	@Override
	public Page<NfsLoanApply> findMultipleLoanApplyPage(Page<NfsLoanApply> page, NfsLoanApply nfsLoanApply) {
		nfsLoanApply.setPage(page);
		List<NfsLoanApply> applyList = loanApplyDao.findMultipleLoanApplyList(nfsLoanApply);
		page.setList(applyList);
		return page;
	}
	@Transactional(readOnly = false)
	public void save(NfsLoanApply nfsLoanApply) {
		super.save(nfsLoanApply);
	}
	
	@Transactional(readOnly = false)
	public void delete(NfsLoanApply nfsLoanApply) {
		super.delete(nfsLoanApply);
	}

	@Override
	public Page<NfsLoanApply> findPage(NfsLoanApply loanApply, Integer pageNo, Integer pageSize) {

		Page<NfsLoanApply> page = new Page<NfsLoanApply>(pageNo == null?1:pageNo, pageSize == null?20:pageSize);	
		loanApply.setPage(page);
		List<NfsLoanApply> applyList = loanApplyDao.findList(loanApply);
		page.setList(applyList);
		return page;
	}

	@Override
	public Page<NfsLoanApply> findSingleLoanApplyListForApp(
			NfsLoanApply loanApply, Integer pageNo, Integer pageSize) {
		Page<NfsLoanApply> page = new Page<NfsLoanApply>(pageNo == null?1:pageNo, pageSize == null?20:pageSize);	
		loanApply.setPage(page);
		List<NfsLoanApply> applyList = loanApplyDao.findSingleLoanApplyListForApp(loanApply);
		page.setList(applyList);
		
		return page;
	}


	@Override
	public ResponseData checkFriend(Member member, String friendsId) {
		String[] friendsIdList = friendsId.split("\\|");
		for (String friendIdStr : friendsIdList) {
			//检查好友是否有15天逾期的借条
			NfsLoanRecord loanRecord = new NfsLoanRecord();
			Member friend = memberService.get(Long.valueOf(friendIdStr));
			loanRecord.setLoanee(friend);
			loanRecord.setStatus(NfsLoanRecord.Status.overdue);
			Date endDueRepayDate = DateUtils.addCalendarByDate(new Date(), -16);
			loanRecord.setEndDueRepayDate(endDueRepayDate);
			List<NfsLoanRecord> findOverdueLoanList = nfsLoanRecordService.findList(loanRecord);
			if(findOverdueLoanList != null && findOverdueLoanList.size()>0){
				return ResponseData.error("["+friend.getName()+"]有逾期15天以上的借条无法完成借款");
			}
			//检查你是不是他的好友
			Boolean flag = memberFriendRelationService.checkFriendRelation(member.getId(),friend.getId());
			if(flag == null){
				return ResponseData.error("好友["+friend.getName()+"]已将您移除好友列表,请重新添加后再操作");
			}
			//好友是否实名认证
			Integer verifiedList = friend.getVerifiedList();

			if(!(VerifiedUtils.isVerified(verifiedList, 1) && VerifiedUtils.isVerified(verifiedList, 2))){
				return ResponseData.error("您的好友还未实名认证，暂时无法选此好友");
			}		
			//邮箱
			if(!VerifiedUtils.isVerified(verifiedList, 23)){
				return ResponseData.error("您的好友还未填写邮箱，暂时无法选此好友");
			}
		}
			return ResponseData.success("");
	}

	@Override
	public List<NfsLoanApply> findSingleLoanApplyListForApp(NfsLoanApply apply) {
		return loanApplyDao.findSingleLoanApplyListForApp(apply);
	}

	@Override
	public Page<NfsLoanApply> findMemberSingleLoanApplyListForApp(
			NfsLoanApply loanApply, Integer pageNo, Integer pageSize,String orderBy) {
		Page<NfsLoanApply> page = new Page<NfsLoanApply>(pageNo == null  ? 1 : pageNo , pageSize == null ? 20 : pageSize);
		if(StringUtils.isNotBlank(orderBy)) {
			page.setOrderBy(orderBy);
		}
		loanApply.setPage(page);
		List<NfsLoanApply> applyList = loanApplyDao.findMemberApplyListForApp(loanApply);
		page.setList(applyList);
		
		return page;
	}
	
	@Transactional(readOnly=false)
	@Override
	public int loanerRequireVideo(NfsLoanApplyDetail detail) {
		int updateLines = applyDetailDao.update(detail);
		if (updateLines == 0) {
			return Constant.UPDATE_FAILED;
		}
		Member loaner = memberService.get(detail.getMember());
		NfsLoanApply apply = get(detail.getApply());
		Member loanee = memberService.get(apply.getMember());

		// 冻结放款人资金
		int code = actService.updateAct(TrxRuleConstant.FROZEN_LOANER_FUNDS, detail.getAmount(), loaner, detail.getId());
		if(code == Constant.UPDATE_FAILED) {
			throw new RuntimeException("放款人[ " + loaner.getId() + "]要求录制视频资金冻结，扣款不成功！detailId["+ detail.getId() +"]");
		}
		//发送会员消息
		memberMessageService.sendMessage(MemberMessage.Type.successfulPaymentThree,detail.getId());
		
		memberMessageService.sendMessage(MemberMessage.Type.successfulPaymentSecond,detail.getId());

		// 发短信
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", loaner.getName());
		sendSmsMsgService.sendMessage("recordVideo", loanee.getUsername(), map);
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("name", loanee.getName());
		sendSmsMsgService.sendMessage("successfulPaymentSecond", loaner.getUsername(), map2);

		// 生成对话
		NfsLoanDetailMessage nfsLoanDetailMessage = new NfsLoanDetailMessage();
		nfsLoanDetailMessage.setDetail(detail);
		nfsLoanDetailMessage.setMember(loaner);
		nfsLoanDetailMessage.setMessageId(RecordMessage.CHAT_1205);
		nfsLoanDetailMessage.setType(RecordMessage.SEND_REMIND);
		nfsLoanDetailMessageService.save(nfsLoanDetailMessage);
		return Constant.UPDATE_SUCCESS;
	}

	@Override
	@Transactional(readOnly=false)
	public int payForLoanToFriend(NfsLoanApply apply, NfsLoanApplyDetail detail,Member loaner) {
		save(apply);
		detail.setApply(apply);
		detail.preInsert();
		applyDetailDao.insert(detail);
		int code = actService.updateAct(TrxRuleConstant.FROZEN_LOANER_FUNDS, detail.getAmount(),loaner, detail.getId());
		if(code == Constant.UPDATE_FAILED) {
			throw new RuntimeException("主动放款申请扣款失败，放款人id: " + loaner.getId());
		}
		return Constant.UPDATE_SUCCESS;
	}

	@Override
	public List<NfsLoanApply> findNoDetailApply(NfsLoanApply loanApply) {
		return dao.findNoDetailApply(loanApply);
	}
	
	@Override
	public Map<String, String> preCheckOfCreateLoan(NfsLoanApply loanApply, NfsLoanApplyDetail detail) {
		Map<String, String> checkResultMap = new HashMap<String,String>();
		Member loanee = null;
		Member loaner = null;
		if(loanApply == null) {
			checkResultMap.put("code", "-1");
			checkResultMap.put("message", "借款申请异常，请联系客服人员处理");
			return checkResultMap;
		}
		if(LoanRole.loanee.equals(loanApply.getLoanRole())) {
			loanee = memberService.get(loanApply.getMember());
			if(detail != null) {
				loaner = memberService.get(detail.getMember());
			}
		}else {
			loaner = memberService.get(loanApply.getMember());
			if(detail != null) {
				loanee = memberService.get(detail.getMember());
			}
		}
		if(detail != null) {
			if(!detail.getStatus().equals(NfsLoanApplyDetail.Status.pendingAgree)){
				checkResultMap.put("code", "-1");
				checkResultMap.put("message", "借条申请状态已更新，请勿重复操作！");
				return checkResultMap;
			}
		}
		BigDecimal avlBal = memberActService.getAvlBal(loaner);
		if(loanApply.getLoanType().equals(NfsLoanApply.LoanType.single)) {
			//单人借款apply里的金额和detail里的金额是一致的
			if(avlBal.compareTo(loanApply.getAmount()) <0) {
				checkResultMap.put("code", "-1");
				checkResultMap.put("message", "账户可用余额不足，请先充值再操作！");
				return checkResultMap;
			}
		}else {
			if(detail != null) {
				BigDecimal applyRemainAmount = loanApply.getRemainAmount();
				if(applyRemainAmount.compareTo(detail.getAmount()) < 0) {
					checkResultMap.put("code", "-1");
					checkResultMap.put("message", "该借款申请的剩余可借额度发生了改变，请重新操作！");
					return checkResultMap;
				}
				if(applyRemainAmount.compareTo(new BigDecimal(100)) < 0) {
					checkResultMap.put("code", "-1");
					checkResultMap.put("message", "该借款申请的剩余可借额度已不足100元，不能再出借了！");
					return checkResultMap;
				}
				if(avlBal.compareTo(detail.getAmount()) < 0) {
					checkResultMap.put("code", "-1");
					checkResultMap.put("message", "账户可用余额不足，请先充值再操作！");
					return checkResultMap;
				}
			}
		}
		if(loanee != null) {
			Integer loaneeVerifiedList = loanee.getVerifiedList();
			if(!(VerifiedUtils.isVerified(loaneeVerifiedList, 1) && VerifiedUtils.isVerified(loaneeVerifiedList, 2))) {
				checkResultMap.put("code", "-1");
				checkResultMap.put("message", "借款人需完成实名认证之后再进行操作");
				return checkResultMap;
			};
			if(!VerifiedUtils.isVerified(loaneeVerifiedList, 23)) {
				checkResultMap.put("code", "-1");
				checkResultMap.put("message", "借款人需先设置联系邮箱才能借款！");
				return checkResultMap;
			};
		}
		if(loaner != null) {
			Integer loanerVerifiedList = loanee.getVerifiedList();
			if(!VerifiedUtils.isVerified(loanerVerifiedList, 23)) {
				checkResultMap.put("code", "-1");
				checkResultMap.put("message", "放款人需先设置联系邮箱才能放款！");
				return checkResultMap;
			};
			if(!(VerifiedUtils.isVerified(loanerVerifiedList, 1) && VerifiedUtils.isVerified(loanerVerifiedList, 2))) {
				checkResultMap.put("code", "-1");
				checkResultMap.put("message", "放款人需完成实名认证之后再进行操作");
				return checkResultMap;
			};
		}
		int term = loanApply.getTerm();
		if(term > 360*3 || term < 2) {
			checkResultMap.put("code", "-1");
			checkResultMap.put("message", "借款时长最低2天，最多不能超过3年");
			return checkResultMap;
		}
		BigDecimal intRate = loanApply.getIntRate();
		if(intRate.compareTo(new BigDecimal(24)) ==1 ||intRate.compareTo(BigDecimal.ZERO) == -1) {
			checkResultMap.put("code", "-1");
			checkResultMap.put("message", "年化利率错误，应在0~24%之间（包含0和24%）");
			return checkResultMap;
		}
		return null;
	}

	@Override
	public Page<NfsLoanApply> findLoanApplyListForGxt(NfsLoanApply loanApply) {
		List<NfsLoanApply> applyList = loanApplyDao.findApplyListForGxt(loanApply);
		Page<NfsLoanApply> page = loanApply.getPage();
		page.setList(applyList);
		return page;
	}

}