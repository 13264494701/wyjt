package com.jxf.loan.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.loan.constant.RecordMessage;
import com.jxf.loan.dao.NfsLoanPartialAndDelayDao;
import com.jxf.loan.entity.NfsLoanApply.LoanRole;
import com.jxf.loan.entity.NfsLoanApplyDetail;
import com.jxf.loan.entity.NfsLoanContract.SignatureType;
import com.jxf.loan.entity.NfsLoanDetailMessage;
import com.jxf.loan.entity.NfsLoanOperatingRecord;
import com.jxf.loan.entity.NfsLoanPartialAndDelay;
import com.jxf.loan.entity.NfsLoanPartialAndDelay.PayStatus;
import com.jxf.loan.entity.NfsLoanRecord;
import com.jxf.loan.entity.NfsLoanRecord.DelayStatus;
import com.jxf.loan.entity.NfsLoanRecord.PartialStatus;
import com.jxf.loan.entity.NfsLoanRecord.Status;
import com.jxf.loan.entity.NfsLoanRepayRecord;
import com.jxf.loan.service.NfsLoanContractService;
import com.jxf.loan.service.NfsLoanDetailMessageService;
import com.jxf.loan.service.NfsLoanOperatingRecordService;
import com.jxf.loan.service.NfsLoanPartialAndDelayService;
import com.jxf.loan.service.NfsLoanRecordService;
import com.jxf.loan.service.NfsLoanRepayRecordService;
import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberAct;
import com.jxf.mem.entity.MemberActTrx;
import com.jxf.mem.entity.MemberMessage;
import com.jxf.mem.service.MemberActService;
import com.jxf.mem.service.MemberActTrxService;
import com.jxf.mem.service.MemberMessageService;
import com.jxf.mem.service.MemberService;
import com.jxf.nfs.constant.ActSubConstant;
import com.jxf.nfs.constant.TrxRuleConstant;
import com.jxf.nfs.service.NfsActService;
import com.jxf.payment.entity.Payment;
import com.jxf.payment.service.RefundService;
import com.jxf.svc.config.Constant;
import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
import com.jxf.svc.utils.CalendarUtil;
import com.jxf.svc.utils.Collections3;
import com.jxf.svc.utils.DateUtils;
import com.jxf.web.model.ResponseData;
/**
 * 部分还款和延期ServiceImpl
 * @author XIAORONGDIAN
 * @version 2018-12-11
 */
@Service("nfsLoanPartialAndDelayService")
@Transactional(readOnly = true)
public class NfsLoanPartialAndDelayServiceImpl extends CrudServiceImpl<NfsLoanPartialAndDelayDao, NfsLoanPartialAndDelay> implements NfsLoanPartialAndDelayService{
	
	
	@Autowired
	private NfsLoanPartialAndDelayDao nfsLoanPartialAndDelayDao;
	@Autowired
	private NfsLoanPartialAndDelayService loanPartialAndDelayService;
	@Autowired
	private MemberMessageService memberMessageService;
	@Autowired
	private NfsActService actService;
	@Autowired
	private NfsLoanRecordService loanRecordService;
	@Autowired
	private NfsLoanDetailMessageService loanDetailMessageService;
	@Autowired
	private NfsLoanOperatingRecordService loanOperatingRecordService;
	@Autowired
	private NfsLoanRepayRecordService repayRecordService;
	@Autowired
	private NfsLoanContractService loanContractService;
	@Autowired
	private MemberActTrxService memberActTrxService;
	@Autowired
	private RefundService refundService;
	@Autowired
	private MemberActService memberActService;
	@Autowired
	private MemberService memberService;
	
	public NfsLoanPartialAndDelay get(Long id) {
		return super.get(id);
	}
	
	public List<NfsLoanPartialAndDelay> findList(NfsLoanPartialAndDelay nfsLoanPartialAndDelay) {
		return super.findList(nfsLoanPartialAndDelay);
	}
	
	public Page<NfsLoanPartialAndDelay> findPage(Page<NfsLoanPartialAndDelay> page, NfsLoanPartialAndDelay nfsLoanPartialAndDelay) {
		return super.findPage(page, nfsLoanPartialAndDelay);
	}
	
	@Transactional(readOnly = false)
	public void save(NfsLoanPartialAndDelay nfsLoanPartialAndDelay) {
		if (nfsLoanPartialAndDelay.getIsNewRecord()){
			nfsLoanPartialAndDelay.preInsert();
			dao.insert(nfsLoanPartialAndDelay);
		}else{
			nfsLoanPartialAndDelay.preUpdate();
			int updateLines = dao.update(nfsLoanPartialAndDelay);
			if(updateLines == 0) {
				throw new RuntimeException("延期部分还款申请[id:" + nfsLoanPartialAndDelay.getId() + "]更新失败");
			}
		}
	
	}
	
	@Transactional(readOnly = false)
	public void delete(NfsLoanPartialAndDelay nfsLoanPartialAndDelay) {
		super.delete(nfsLoanPartialAndDelay);
	}

	@Override
	public int getAgreedApplyCount(Long loanId) {
		return dao.getAgreedApplyCount(loanId);
	}

	@Override
	@Transactional(readOnly = false)
	public void cancelPartialPayOrDelay(NfsLoanPartialAndDelay nfsLoanPartialRepayApply,Member member, NfsLoanRecord record) {
		boolean isLoaner = false;
		if(member.getId().equals(record.getLoaner().getId())){
			isLoaner = true;
		}
		nfsLoanPartialRepayApply.setStatus(NfsLoanPartialAndDelay.Status.canceled);
		loanPartialAndDelayService.save(nfsLoanPartialRepayApply);
		
		BigDecimal partialAmount = nfsLoanPartialRepayApply.getPartialAmount();
		
		//发对话
		NfsLoanDetailMessage nfsLoanDetailMessage = new NfsLoanDetailMessage();
		nfsLoanDetailMessage.setType(RecordMessage.LENDER_PAID_REPAYMENT);
		NfsLoanApplyDetail loanDetail = record.getLoanApplyDetail();
		nfsLoanDetailMessage.setDetail(loanDetail);
		nfsLoanDetailMessage.setMember(member);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("name", member.getName());
		
		if(isLoaner){
			if(partialAmount.compareTo(BigDecimal.ZERO) == 0){//仅延期
				nfsLoanDetailMessage.setMessageId(RecordMessage.CHAT_2226);
				record.setDelayStatus(NfsLoanRecord.DelayStatus.initial);
				//发送会员消息
				memberMessageService.sendMessage(MemberMessage.Type.withdrawExtensionApplication,nfsLoanPartialRepayApply.getId());
			}else{
				record.setPartialStatus(NfsLoanRecord.PartialStatus.initial);
				if(nfsLoanPartialRepayApply.getDelayDays() > 0){
					record.setDelayStatus(NfsLoanRecord.DelayStatus.initial);
				}
				nfsLoanDetailMessage.setMessageId(RecordMessage.CHAT_2225);
				
				//发送会员消息
				memberMessageService.sendMessage(MemberMessage.Type.withdrawPartialRepaymentApplication,nfsLoanPartialRepayApply.getId());
			}
		}else{
			if(partialAmount.compareTo(BigDecimal.ZERO) == 0){//仅延期
				nfsLoanDetailMessage.setMessageId(RecordMessage.CHAT_2208);
				record.setDelayStatus(NfsLoanRecord.DelayStatus.initial);
				memberMessageService.sendMessage(MemberMessage.Type.withdrawExtensionApplication,nfsLoanPartialRepayApply.getId());
			}else{
				record.setPartialStatus(NfsLoanRecord.PartialStatus.initial);
				if(nfsLoanPartialRepayApply.getDelayDays() > 0){
					record.setDelayStatus(NfsLoanRecord.DelayStatus.initial);
				}
				nfsLoanDetailMessage.setMessageId(RecordMessage.CHAT_2217);
				//账户变动
				int code = actService.updateAct(TrxRuleConstant.CANCEL_PARTIAL_REPAY, partialAmount, member, record.getId());
				if(code == Constant.UPDATE_FAILED) {
					throw new RuntimeException("借款人["+ record.getLoanee().getId() +"]取消部分还款更新账户异常！");
				}
				//发送会员消息
				memberMessageService.sendMessage(MemberMessage.Type.withdrawPartialRepaymentApplication,nfsLoanPartialRepayApply.getId());
			}
		}
		loanRecordService.save(record);
		loanDetailMessageService.save(nfsLoanDetailMessage);
	}

	@Override
	@Transactional(readOnly = false)
	public ArrayList<Object> answerPartialPay(NfsLoanRecord loanRecord, Member member , NfsLoanPartialAndDelay loanPartialRepayApply , String isAgree) {

		if(!loanPartialRepayApply.getStatus().equals(NfsLoanPartialAndDelay.Status.confirm)){
			ArrayList<Object> arrayList = new ArrayList<Object>();
			arrayList.add(0, "借条状态异常!");
			arrayList.add(1, null);
			return arrayList;
		}
		//借条记录实体
		NfsLoanOperatingRecord operatingRecord = new NfsLoanOperatingRecord();
		NfsLoanRecord oldRecord = new NfsLoanRecord();
		oldRecord.setId(loanRecord.getId());
		Long loanerId = loanRecord.getLoaner().getId();
		Long loaneeId = loanRecord.getLoanee().getId();
		Member operatingLoaner = new Member();
		Member operatingLoanee = new Member();
		operatingLoaner.setId(loanerId);
		operatingLoanee.setId(loaneeId);
		oldRecord.setLoaner(operatingLoaner);
		oldRecord.setLoanee(operatingLoanee);
		oldRecord.setAmount(loanRecord.getAmount());
		oldRecord.setInterest(loanRecord.getInterest());
		oldRecord.setOverdueInterest(loanRecord.getOverdueInterest());
		operatingRecord.setOldRecord(oldRecord);
		
	    boolean isLoaner = member.equals(loanRecord.getLoaner());
	
		Integer delayDays = loanPartialRepayApply.getDelayDays();//延期天数
		BigDecimal partialAmount = loanPartialRepayApply.getPartialAmount();//部分还款金额
		BigDecimal delayInterest = loanPartialRepayApply.getDelayInterest();//延期利息
		BigDecimal delayRate = loanPartialRepayApply.getDelayRate();//延期利率
		Date repayDateAfterDelay = loanPartialRepayApply.getNowRepayDate();//延期后还款日
		
		String message = "同意";
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("name", member.getName());
		
		NfsLoanDetailMessage loanDetailMessage = new NfsLoanDetailMessage();
		loanDetailMessage.setType(RecordMessage.LENDER_PAID_REPAYMENT);
		NfsLoanApplyDetail loanDetail = loanRecord.getLoanApplyDetail();
		loanDetailMessage.setDetail(loanDetail);
		loanDetailMessage.setMember(member);
		MemberMessage sendMessage = null;
		/**
		 * 仅延期
		 */
		if(partialAmount.compareTo(BigDecimal.ZERO) == 0){
			DelayStatus delayStatus = loanRecord.getDelayStatus();
			if(delayStatus.equals(DelayStatus.initial)){
				ArrayList<Object> arrayList = new ArrayList<Object>();
				arrayList.add(0, "借条状态异常!");
				arrayList.add(1, sendMessage);
				return arrayList;
			}
			if(Integer.parseInt(isAgree) == 0){
				loanPartialRepayApply.setStatus(NfsLoanPartialAndDelay.Status.reject);
				message = "拒绝";
				loanRecord.setDelayStatus(NfsLoanRecord.DelayStatus.initial);
				//发消息 发对话
				if(isLoaner){
					loanDetailMessage.setMessageId(RecordMessage.CHAT_2202);
					loanDetailMessageService.save(loanDetailMessage);
				}else{
					loanDetailMessage.setMessageId(RecordMessage.CHAT_2228);
					loanDetailMessageService.save(loanDetailMessage);
				}
				//发送会员消息
				sendMessage = memberMessageService.sendMessage(MemberMessage.Type.refuseExtensionApplication,loanPartialRepayApply.getId());
			}else{
				loanPartialRepayApply.setStatus(NfsLoanPartialAndDelay.Status.agreed);
				loanRecord.setDelayStatus(NfsLoanRecord.DelayStatus.initial);
				//变更record
				loanRecord.setInterest(delayInterest.add(loanRecord.getInterest()));//延期利息加原借条利息
				loanRecord.setIntRate(delayRate);
				loanRecord.setDueRepayDate(repayDateAfterDelay);
				int distanceOfTwoDate = CalendarUtil.getIntervalDays2(repayDateAfterDelay, new Date());//如果确认时借条状态有变更
				
				if(distanceOfTwoDate >= 0){
					loanRecord.setStatus(Status.penddingRepay);
				}else{
					loanRecord.setStatus(Status.overdue);
				}
				
				loanRecord.setDueRepayAmount(loanRecord.getDueRepayAmount().add(delayInterest));
				//发消息
				loanDetailMessage.setMessageId(RecordMessage.CHAT_2203);
				loanDetailMessage.setType(RecordMessage.LENDER_AGREE_EXTENSION);
				if(isLoaner){
					loanDetailMessageService.save(loanDetailMessage);
					if(delayInterest.compareTo(BigDecimal.ZERO) > 0){
						int code = actService.updateAct(TrxRuleConstant.LOANER_APPROVE_DELAY_PENDINGREPAY, delayInterest
								, loanRecord.getLoanee(),loanRecord.getLoaner(), loanRecord.getId());
						if(code == Constant.UPDATE_FAILED) {
							throw new RuntimeException("放款人[ID："+ loanerId +"]同意延期申请修改待收待还金额失败！借条ID：["+ loanRecord.getId()+"]");
						}
					}
				}else{
					loanDetailMessageService.save(loanDetailMessage);
					if(delayInterest.compareTo(BigDecimal.ZERO) > 0){
						int code = actService.updateAct(TrxRuleConstant.LOANEE_APPROVE_DELAY_PENDING_REPAY, delayInterest
								, loanRecord.getLoanee(),loanRecord.getLoaner(), loanRecord.getId());
						if(code == Constant.UPDATE_FAILED) {
							throw new RuntimeException("借款人[ID："+ loaneeId +"]同意延期申请修改待收待还金额失败！借条ID：["+ loanRecord.getId()+"]");
						}
					}
				}
				//发送会员消息
				sendMessage = memberMessageService.sendMessage(MemberMessage.Type.confirmExtensionApplication,loanPartialRepayApply.getId());
				//生成借条记录----延期还款
				operatingRecord.setNowRecord(loanRecord);
				operatingRecord.setInitiator(loanPartialRepayApply.getMemberRole().getName());
				operatingRecord.setType(NfsLoanOperatingRecord.Type.delay);
				operatingRecord.setDelayDays(delayDays);
				operatingRecord.setDelayInterest(delayInterest);
				loanOperatingRecordService.save(operatingRecord);
				
				//合同实体
				loanContractService.createContract(loanRecord,SignatureType.youdun); 
				int term = DateUtils.getDistanceOfTwoDate(repayDateAfterDelay,loanRecord.getCreateTime()); 
				loanRecord.setTerm(term + 1);
				
				//更新还款记录
				NfsLoanRepayRecord nfsLoanRepayRecord = new NfsLoanRepayRecord();
				nfsLoanRepayRecord.setLoan(loanRecord);
				List<NfsLoanRepayRecord> repayRecords = repayRecordService.findList(nfsLoanRepayRecord);
				NfsLoanRepayRecord repayRecord = repayRecords.get(0);
				repayRecord.setExpectRepayDate(repayDateAfterDelay);//应还日期
				
				if(delayInterest.compareTo(BigDecimal.ZERO)>0){
					repayRecord.setExpectRepayAmt(repayRecord.getExpectRepayAmt().add(delayInterest));
					repayRecord.setExpectRepayInt(repayRecord.getExpectRepayInt().add(delayInterest));
				} 
				
				if(distanceOfTwoDate >= 0){
					repayRecord.setStatus(NfsLoanRepayRecord.Status.pending);
				}else{
					repayRecord.setStatus(NfsLoanRepayRecord.Status.overdue);
				}
				repayRecordService.save(repayRecord);
			}
			
			loanRecordService.save(loanRecord);
			save(loanPartialRepayApply);
			ArrayList<Object> arrayList = new ArrayList<Object>();
			arrayList.add(0, "延期申请已" + message);
			arrayList.add(1, sendMessage);
			return arrayList;
		}else{
			PartialStatus partialStatus = loanRecord.getPartialStatus();
			if(partialStatus.equals(PartialStatus.initial)){
				ArrayList<Object> arrayList = new ArrayList<Object>();
				arrayList.add(0, "借条状态异常!");
				arrayList.add(1, sendMessage);
				return arrayList;
			}
			/**
			 * 部分还款or部分还款+延期
			 */
			loanRecord.setPartialStatus(NfsLoanRecord.PartialStatus.initial);
			if(Integer.parseInt(isAgree) == 0){
				if(delayDays > 0) {
					loanRecord.setDelayStatus(NfsLoanRecord.DelayStatus.initial);
				}
				loanPartialRepayApply.setStatus(NfsLoanPartialAndDelay.Status.reject);
				message = "拒绝";
				//发消息 发对话 
				if(isLoaner){
					loanDetailMessage.setMessageId(RecordMessage.CHAT_2214);
					loanDetailMessageService.save(loanDetailMessage);
					//发送会员消息
					sendMessage = memberMessageService.sendMessage(MemberMessage.Type.refusePartialRepaymentApplication,loanPartialRepayApply.getId());
					//账户变更
					int code = actService.updateAct(TrxRuleConstant.REFUSE_PARTIAL_PAYMENT, partialAmount, loanRecord.getLoanee(), loanRecord.getId());
					if(code == Constant.UPDATE_FAILED) {
						throw new RuntimeException("借款Id: " + loanRecord.getId() + " 放款人拒绝部分还款失败！");
					}
				}else{
					loanDetailMessage.setMessageId(RecordMessage.CHAT_2227);
					loanDetailMessageService.save(loanDetailMessage);
					//发送会员消息
					sendMessage = memberMessageService.sendMessage(MemberMessage.Type.refusePartialRepaymentApplication,loanPartialRepayApply.getId());
				}
			}else{
				loanPartialRepayApply.setStatus(NfsLoanPartialAndDelay.Status.agreed);
				BigDecimal interest = loanRecord.getInterest();
				if(partialAmount.compareTo(interest) <= 0){//部分还款金额<原利息
					loanRecord.setInterest(interest.subtract(partialAmount));
				}else{
					loanRecord.setInterest(BigDecimal.ZERO);
					BigDecimal amount = loanRecord.getAmount();
					loanRecord.setAmount(amount.subtract(partialAmount.subtract(interest)));
				}
				BigDecimal dueRepayAmount = loanRecord.getDueRepayAmount();
				loanRecord.setDueRepayAmount(dueRepayAmount.subtract(partialAmount));
				
				operatingRecord.setType(NfsLoanOperatingRecord.Type.partial);
				if(delayDays > 0){//有延期
					operatingRecord.setType(NfsLoanOperatingRecord.Type.partialAndDelay);
					operatingRecord.setDelayDays(delayDays);
					operatingRecord.setDelayInterest(delayInterest);
					
					loanRecord.setDueRepayDate(repayDateAfterDelay);
					loanRecord.setInterest(loanRecord.getInterest().add(delayInterest));
					loanRecord.setDelayStatus(NfsLoanRecord.DelayStatus.initial);
					loanRecord.setDueRepayAmount(loanRecord.getDueRepayAmount().add(delayInterest));
				}
				int distanceOfTwoDate = CalendarUtil.getIntervalDays2(repayDateAfterDelay, new Date());//如果确认时借条状态有变更
				if(distanceOfTwoDate >= 0){
					loanRecord.setStatus(Status.penddingRepay);
				}else{
					loanRecord.setStatus(Status.overdue);
				}
				
				//生成借条记录----延期还款or部分+延期
				operatingRecord.setNowRecord(loanRecord);
				operatingRecord.setRepaymentAmount(partialAmount);
				operatingRecord.setInitiator(loanPartialRepayApply.getMemberRole().getName());
				loanOperatingRecordService.save(operatingRecord);
				
				loanDetailMessage.setMessageId(RecordMessage.CHAT_2215);
				loanDetailMessage.setType(RecordMessage.LENDER_AGREE_PART);
				//注:借款人同意部分还款走H5页面所以这里代码只能是放款人能走到
				
				//发对话
				loanDetailMessageService.save(loanDetailMessage);
				//发送会员消息
				sendMessage = memberMessageService.sendMessage(MemberMessage.Type.confirmPartialRepaymentApplication,loanPartialRepayApply.getId());
				//账户变更
				int code = actService.updateAct(TrxRuleConstant.LOANER_APPROVE_PARTIAL_REPAYMENT, partialAmount, loanRecord.getLoanee(),loanRecord.getLoaner(), loanRecord.getId());
				if(code == Constant.UPDATE_FAILED) {
					throw new RuntimeException("借款Id: " + loanRecord.getId() + " 放款人同意部分还款失败！");
				}
				if(delayInterest.compareTo(BigDecimal.ZERO)>0){
					//变更待收待还
					code = actService.updateAct(TrxRuleConstant.LOANER_APPROVE_DELAY_PENDINGREPAY, delayInterest, loanRecord.getLoanee(),loanRecord.getLoaner(), loanRecord.getId());
					if(code == Constant.UPDATE_FAILED) {
						throw new RuntimeException("借款Id: " + loanRecord.getId() + " 放款人同意部分还款变更待收待还失败！");
					}
				}
				//更新还款记录
				NfsLoanRepayRecord nfsLoanRepayRecord = new NfsLoanRepayRecord();
				nfsLoanRepayRecord.setLoan(loanRecord);
				List<NfsLoanRepayRecord> repayRecords = repayRecordService.findList(nfsLoanRepayRecord);
				NfsLoanRepayRecord repayRecord = repayRecords.get(0);
				BigDecimal actualRepayAmt = new BigDecimal(repayRecord.getActualRepayAmt()==null ? "0" : repayRecord.getActualRepayAmt());
				repayRecord.setActualRepayAmt(actualRepayAmt.add(partialAmount).toString());
				repayRecord.setActualRepayDate(new Date());
				repayRecord.setStatus(NfsLoanRepayRecord.Status.partialDone);
				if(delayInterest.compareTo(BigDecimal.ZERO)>0){
					repayRecord.setExpectRepayAmt(repayRecord.getExpectRepayAmt().add(delayInterest));
					repayRecord.setExpectRepayInt(repayRecord.getExpectRepayInt().add(delayInterest));
				} 
				
				repayRecord.setExpectRepayDate(repayDateAfterDelay);
				if(distanceOfTwoDate >= 0){
					repayRecord.setStatus(NfsLoanRepayRecord.Status.pending);
				}else{
					repayRecord.setStatus(NfsLoanRepayRecord.Status.overdue);
				}
				repayRecordService.save(repayRecord);
				
				//合同实体
				loanContractService.createContract(loanRecord,SignatureType.youdun); 
				
				int term = 0;
				if(repayDateAfterDelay == null){
					term = DateUtils.getDistanceOfTwoDate(loanRecord.getDueRepayDate(),loanRecord.getCreateTime()); 
				}else{
					term = DateUtils.getDistanceOfTwoDate(repayDateAfterDelay,loanRecord.getCreateTime()); 
				}
				loanRecord.setTerm(term+1);
			}
			
			
			loanRecordService.save(loanRecord);
			loanPartialAndDelayService.save(loanPartialRepayApply);
			
			ArrayList<Object> arrayList = new ArrayList<Object>();
			arrayList.add(0, "部分还款已" + message);
			arrayList.add(1, sendMessage);
			return arrayList;
		}
	}

	@Override
	public NfsLoanPartialAndDelay findNearestPartialAndDelayByRecordId(Long loanId) {
		
		return nfsLoanPartialAndDelayDao.findNearestByRecordId(loanId);
	}

	@Override
	public void filter(List<NfsLoanPartialAndDelay> loanPartialRepayApplyList) {

		CollectionUtils.filter(loanPartialRepayApplyList, new Predicate() {
			public boolean evaluate(Object object) {
				NfsLoanPartialAndDelay loanPartialAndDelay = (NfsLoanPartialAndDelay) object;
				NfsLoanRecord loanRecord = loanPartialAndDelay.getLoan();
				loanRecord.setStatus(NfsLoanRecord.Status.overdue);
				int overdueCount = loanRecordService.countLoaneeLoan(loanRecord);
				return overdueCount==0;
			}
		});
	}

	@Override
	@Transactional(readOnly = false)
	public ResponseData answerDelay(NfsLoanRecord loanRecord, Member member, NfsLoanPartialAndDelay loanPartialRepayApply, int isAgree) {
		if(!loanPartialRepayApply.getStatus().equals(NfsLoanPartialAndDelay.Status.confirm)){
			return ResponseData.error("借条状态异常!");
		}
		Long loanerId = loanRecord.getLoaner().getId();
		Long loaneeId = loanRecord.getLoanee().getId();
	    boolean isLoaner = member.equals(loanRecord.getLoaner());
	
		if(loanRecord.getDelayStatus().equals(DelayStatus.initial)){
			return ResponseData.error("借条状态异常!");
		}
		String message = "同意";
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("name", member.getName());
		
		if(isAgree == 0){
			loanPartialRepayApply.setStatus(NfsLoanPartialAndDelay.Status.reject);
			save(loanPartialRepayApply);
			message = "拒绝";
			loanRecord.setDelayStatus(NfsLoanRecord.DelayStatus.initial);
			loanRecordService.save(loanRecord);
			//发消息 发对话
			if(isLoaner){
				//TODO 退还服务费
				refundDelayFeeForLoanerRefuse(loanPartialRepayApply);
			} 
			//发消息
			memberMessageService.sendMessage(MemberMessage.Type.refuseDelayApplication,loanPartialRepayApply.getId());
		}else{
			Member loanee = memberService.get(loaneeId);
			Member loaner = memberService.get(loanerId);
			loanRecord.setLoanee(loanee);
			loanRecord.setLoaner(loaner);
			updateAfterDelay(loanRecord,loanPartialRepayApply);
			
			//发消息
			memberMessageService.sendMessage(MemberMessage.Type.agreeDelayApplication,loanPartialRepayApply.getId());
		}
		return ResponseData.success("延期申请已" + message);
	}
	
	@Override
	@Transactional(readOnly = false)
	public ResponseData loaneePayForDelayByActBal(NfsLoanPartialAndDelay loanPartialAndDelay) {
		Map<String, String> data = new HashMap<String, String>();
		BigDecimal fee = new BigDecimal(Global.getConfig("gxt.loanDelayFee"));
		Long orgId = loanPartialAndDelay.getId();
		Member loanee = loanPartialAndDelay.getLoan().getLoanee();
		Member loaner = loanPartialAndDelay.getLoan().getLoaner();
		// 判断延期申请的角色是借款人还是放款人
		NfsLoanRecord loanRecord = loanRecordService.get(loanPartialAndDelay.getLoan());
		// 变更账户余额
		BigDecimal loanActBal = memberActService.getMemberAct(loanee, ActSubConstant.MEMBER_LOAN_BAL).getCurBal();
		// 优先使用借款账户余额支付
		if (loanActBal.compareTo(fee) >= 0) {
			// 借款账户余额足够
			int code = actService.updateAct(TrxRuleConstant.GXT_LOAN_DELAY_LOANACT, fee, loanee, orgId);
			if (code == Constant.UPDATE_FAILED) {
				logger.error("公信堂会员:{}申请延期:{}借款账户扣款失败！", loanee.getId(), orgId);
				throw new RuntimeException("公信堂会员" + loanee.getId() + "申请延期" + orgId + "借款账户扣款失败！");
			}
		} else {
			// 借款账户没钱 全部使用可用余额账户的余额支付
			if (loanActBal.compareTo(BigDecimal.ZERO) == 0) {
				int code = actService.updateAct(TrxRuleConstant.GXT_LOAN_DELAY_AVLACT, fee, loanee, orgId);
				if (code == Constant.UPDATE_FAILED) {
					logger.error("公信堂会员:{}申请延期:{}可用余额账户扣款失败！", loanee.getId(), orgId);
					throw new RuntimeException("公信堂会员" + loanee.getId() + "申请延期" + orgId + "可用余额账户扣款失败！");
				}
			} else {
				// 先扣除借款账户余额 再扣除可用余额账户
				int code = actService.updateAct(TrxRuleConstant.GXT_LOAN_DELAY_LOANACT, loanActBal, loanee, orgId);
				if (code == Constant.UPDATE_FAILED) {
					logger.error("公信堂会员:{}申请延期:{}借款账户扣款失败！", loanee.getId(), orgId);
					throw new RuntimeException("公信堂会员" + loanee.getId() + "申请延期" + orgId + "借款账户扣款失败！");
				}
				BigDecimal amountFromAvlActBal = fee.subtract(loanActBal);
				int code2 = actService.updateAct(TrxRuleConstant.GXT_LOAN_DELAY_AVLACT, amountFromAvlActBal, loanee,
						orgId);
				if (code2 == Constant.UPDATE_FAILED) {
					logger.error("公信堂会员:{}申请延期:{}可用余额账户扣款失败！", loanee.getId(), orgId);
					throw new RuntimeException("公信堂会员" + loanee.getId() + "申请延期" + orgId + "可用余额账户扣款失败！");
				}
			}
		}
		loanPartialAndDelay.setPayStatus(PayStatus.success);
		if (loanPartialAndDelay.getMemberRole().equals(LoanRole.loanee)) {
			loanRecord.setDelayStatus(DelayStatus.loaneeApplyDelay);
			loanRecordService.save(loanRecord);
			// 借款人主动申请延期
			loanPartialAndDelay.setStatus(NfsLoanPartialAndDelay.Status.confirm);
			save(loanPartialAndDelay);
		} else {
			// 借款人同意放款人的延期申请 变更申请status paystatus 借条 record 的delaystatus 判断借条延期后是否逾期 添加操作记录
			loanRecord.setLoanee(loanee);
			loanRecord.setLoaner(loaner);
			updateAfterDelay(loanRecord, loanPartialAndDelay);
		}
		data.put("loan_type", loanRecord.getId() + "_2");
		return ResponseData.success("支付成功", data);
	}
	
	@Override
	@Transactional(readOnly = false)
	public boolean loaneePayForDelayByWx(Payment payment) {
		Long orgId = payment.getOrgId();
		NfsLoanPartialAndDelay partialAndDelay = get(orgId);
		if(payment.getStatus().equals(Payment.Status.failure)) {
			if(partialAndDelay.getMemberRole().equals(LoanRole.loanee)) {
				//借款人主动发起的，删除申请
				partialAndDelay.setPayStatus(PayStatus.fail);
				update(partialAndDelay);
				delete(partialAndDelay);
			}
			return false;
		}
		NfsLoanRecord loanRecord = loanRecordService.get(partialAndDelay.getLoan());
		LoanRole memberRole = partialAndDelay.getMemberRole();
		Member loanee = memberService.get(partialAndDelay.getMember());
		if(memberRole.equals(LoanRole.loaner)) {
			loanee = memberService.get(loanRecord.getLoanee());
		}
		
		// 获取微信账户
		MemberAct memberAct = memberActService.getMemberAct(loanee, ActSubConstant.MEMBER_WEIXIN_PAYMENT);
		if (memberAct == null) {
			// 生成微信账户
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
		BigDecimal fee = new BigDecimal(Global.getConfig("gxt.loanDelayFee"));
		int code = actService.updateAct(TrxRuleConstant.GXT_LOAN_DELAY_WXPAY, fee, loanee, orgId);
		if (code == Constant.UPDATE_FAILED) {
			logger.error("公信堂会员{}申请补借条微信账户更新失败！", loanee.getId());
			throw new RuntimeException("公信堂会员" + loanee.getId() + "申请补借条微信账户更新失败！");
		}
		partialAndDelay.setPayStatus(PayStatus.success);
		if(memberRole.equals(LoanRole.loanee)) {
			//借款人主动申请，申请时付费,只变更状态即可
			partialAndDelay.setStatus(NfsLoanPartialAndDelay.Status.confirm);
			save(partialAndDelay);
			loanRecord.setDelayStatus(DelayStatus.loaneeApplyDelay);
			loanRecordService.save(loanRecord);
		}else {
			//放款人主动申请，借款人同意时付费 
			loanRecord.setLoanee(loanee);
			Member loaner = memberService.get(loanRecord.getLoaner());
			loanRecord.setLoaner(loaner);
			updateAfterDelay(loanRecord,partialAndDelay);
		}
		return true;
	}
	
	@Transactional(readOnly = false)
	public int loaneeCancelDelay(NfsLoanPartialAndDelay loanPartialRepayApply) {
		//更改detail状态
		loanPartialRepayApply.setStatus(NfsLoanPartialAndDelay.Status.canceled);
		loanPartialRepayApply.preUpdate();
		int updateLines = nfsLoanPartialAndDelayDao.update(loanPartialRepayApply);
		if(updateLines == 0) {
			return Constant.UPDATE_FAILED;
		}
		//退款
		refundDelayFeeForLoaneeCancel(loanPartialRepayApply);
		return Constant.UPDATE_SUCCESS;
	}
	
	/**
	 * 延期达成的后续处理
	 * @param loanRecord
	 * @param loanPartialAndDelay
	 */
	private void updateAfterDelay(NfsLoanRecord loanRecord,NfsLoanPartialAndDelay loanPartialAndDelay) {	
		Member loaner = loanRecord.getLoaner();
		Member loanee = loanRecord.getLoanee();
		BigDecimal delayInterest = loanPartialAndDelay.getDelayInterest();
		if (delayInterest.compareTo(BigDecimal.ZERO) > 0) {
			int code = actService.updateAct(TrxRuleConstant.GXT_LOAN_DELAY_REPAY_RECEIVE, delayInterest, loaner,
					loanee, loanRecord.getId());
			if (code == Constant.UPDATE_FAILED) {
				throw new RuntimeException(
						"借款人[ID：" + loanee.getId() + "]同意延期申请修改待收待还金额失败！借条ID：[" + loanRecord.getId() + "]");
			}
		}

		loanPartialAndDelay.setStatus(NfsLoanPartialAndDelay.Status.agreed);
		save(loanPartialAndDelay);

		// 添加操作记录
		NfsLoanOperatingRecord operatingRecord = new NfsLoanOperatingRecord();
		NfsLoanRecord oldRecord = new NfsLoanRecord();
		oldRecord.setId(loanRecord.getId());
		Member operatingLoaner = new Member();
		Member operatingLoanee = new Member();
		operatingLoaner.setId(loaner.getId());
		operatingLoanee.setId(loanee.getId());
		oldRecord.setLoaner(operatingLoaner);
		oldRecord.setLoanee(operatingLoanee);
		oldRecord.setAmount(loanRecord.getAmount());
		oldRecord.setInterest(loanRecord.getInterest());
		oldRecord.setOverdueInterest(loanRecord.getOverdueInterest());
		operatingRecord.setOldRecord(oldRecord);
		operatingRecord.setNowRecord(loanRecord);
		operatingRecord.setInitiator(loanPartialAndDelay.getMemberRole().getName());
		operatingRecord.setType(NfsLoanOperatingRecord.Type.delay);
		operatingRecord.setDelayDays(loanPartialAndDelay.getDelayDays());
		operatingRecord.setDelayInterest(delayInterest);
		loanOperatingRecordService.save(operatingRecord);

		// 更新借条信息
		loanRecord.setDelayStatus(DelayStatus.initial);
		loanRecord.setIntRate(loanPartialAndDelay.getDelayRate());
		loanRecord.setInterest(delayInterest.add(loanRecord.getInterest()));
		loanRecord.setDueRepayAmount(loanRecord.getDueRepayAmount().add(delayInterest));
		loanRecord.setDueRepayDate(loanPartialAndDelay.getNowRepayDate());
		String nowRepayDate = CalendarUtil.getDate(loanPartialAndDelay.getNowRepayDate());
		String curDate = CalendarUtil.getDate(new Date());
		if (nowRepayDate.compareTo(curDate) > 0) {
			loanRecord.setStatus(NfsLoanRecord.Status.penddingRepay);
		} else {
			loanRecord.setStatus(NfsLoanRecord.Status.overdue);
		}
		loanRecord.setTerm(loanRecord.getTerm() + loanPartialAndDelay.getDelayDays());
		loanRecordService.save(loanRecord);

		// 合同实体
		loanContractService.createContract(loanRecord,SignatureType.youdun);

		// 更新还款记录
		NfsLoanRepayRecord nfsLoanRepayRecord = new NfsLoanRepayRecord();
		nfsLoanRepayRecord.setLoan(loanRecord);
		List<NfsLoanRepayRecord> repayRecords = repayRecordService.findList(nfsLoanRepayRecord);
		NfsLoanRepayRecord repayRecord = repayRecords.get(0);
		repayRecord.setExpectRepayDate(loanPartialAndDelay.getNowRepayDate());// 应还日期

		if (delayInterest.compareTo(BigDecimal.ZERO) > 0) {
			repayRecord.setExpectRepayAmt(repayRecord.getExpectRepayAmt().add(delayInterest));
			repayRecord.setExpectRepayInt(repayRecord.getExpectRepayInt().add(delayInterest));
		}

		if (nowRepayDate.compareTo(curDate) > 0) {
			repayRecord.setStatus(NfsLoanRepayRecord.Status.pending);
		} else {
			repayRecord.setStatus(NfsLoanRepayRecord.Status.overdue);
		}
		repayRecordService.save(repayRecord);
	}
	
	/**
	 * 退还借款人延期服务费
	 * @param partialAndDelay
	 */
	private void refundDelayFeeForLoaneeCancel(NfsLoanPartialAndDelay partialAndDelay) {
		Long orgId = partialAndDelay.getId();
		Member loanee = partialAndDelay.getMember();
		if(partialAndDelay.getMemberRole().equals(LoanRole.loanee)) {
			NfsLoanRecord loanRecord = loanRecordService.get(partialAndDelay.getLoan());
			loanee = memberService.get(loanRecord.getLoanee());
		}
		Long loaneeId = loanee.getId();
		List<MemberActTrx> list = memberActTrxService.findListByOrgId(orgId);
		for (MemberActTrx memberActTrx : list) {
			BigDecimal amount = memberActTrx.getTrxAmt();
			if(memberActTrx.getTrxCode().equals(TrxRuleConstant.GXT_LOAN_DELAY_WXPAY)) {
				//调用微信退款接口 添加微信退款流水
				boolean result = refundService.wxRefund(loanee, Payment.Type.loanDelay, orgId, amount);
				if(result) {
					//生成退款流水
					int code = actService.updateAct(TrxRuleConstant.GXT_CANCEL_LOAN_REFUND_WXPAY, amount, loanee, orgId);
					if(code == Constant.UPDATE_FAILED) {
						logger.error("延期申请：{}退款操作，借款人:{}的微信账户更新失败！",orgId,loaneeId);
						throw new RuntimeException("延期申请:"+orgId+"，借款人"+loaneeId+"的微信账户更新失败！");
					}
				}else {
					logger.error("延期申请：{}退款操作，借款人:{}的微信退款请求失败！",orgId,loaneeId);
					throw new RuntimeException("延期申请:"+orgId+"，借款人"+loaneeId+"的微信退款请求失败！");
				}
			}else if(memberActTrx.getTrxCode().equals(TrxRuleConstant.GXT_LOAN_DELAY_LOANACT)){
				//退款到借款账户
				int code = actService.updateAct(TrxRuleConstant.GXT_CANCEL_LOAN_REFUND_LOANACT, amount, loanee, orgId);
				if(code == Constant.UPDATE_FAILED) {
					logger.error("延期申请：{}退款操作，借款人:{}的借款账户更新失败！",orgId,loaneeId);
					throw new RuntimeException("延期申请:"+orgId+"，借款人"+loaneeId+"的借款余额账户更新失败！");
				}
			}else if(memberActTrx.getTrxCode().equals(TrxRuleConstant.GXT_LOAN_DELAY_AVLACT)) {
				//退款到可用余额账户
				int code = actService.updateAct(TrxRuleConstant.GXT_CANCEL_LOAN_REFUND_AVLACT, amount, loanee, orgId);
				if(code == Constant.UPDATE_FAILED) {
					logger.error("延期申请：{}退款操作，借款人:{}的可用余额账户更新失败！",orgId,loaneeId);
					throw new RuntimeException("延期申请:"+orgId+"，借款人"+loaneeId+"的可用账户更新失败！");
				}
			}else {
				logger.warn("延期申请{}，退款时流水账户为{}",orgId,memberActTrx.getSubNo());
				continue;
			}
		}
	}
	
	/**
	 * 退还借款人延期服务费
	 * @param partialAndDelay
	 */
	private void refundDelayFeeForLoanerRefuse(NfsLoanPartialAndDelay partialAndDelay) {
		Long orgId = partialAndDelay.getId();
		Member loanee = partialAndDelay.getMember();
		if(partialAndDelay.getMemberRole().equals(LoanRole.loanee)) {
			NfsLoanRecord loanRecord = loanRecordService.get(partialAndDelay.getLoan());
			loanee = memberService.get(loanRecord.getLoanee());
		}
		Long loaneeId = loanee.getId();
		List<MemberActTrx> list = memberActTrxService.findListByOrgId(orgId);
		for (MemberActTrx memberActTrx : list) {
			BigDecimal amount = memberActTrx.getTrxAmt();
			if(memberActTrx.getTrxCode().equals(TrxRuleConstant.GXT_LOAN_DELAY_WXPAY)) {
				//调用微信退款接口 添加微信退款流水
				boolean result = refundService.wxRefund(loanee, Payment.Type.loanDelay, orgId, amount);
				if(result) {
					//生成退款流水
					int code = actService.updateAct(TrxRuleConstant.GXT_REFUSE_LOAN_REFUND_WXPAY, amount, loanee, orgId);
					if(code == Constant.UPDATE_FAILED) {
						logger.error("延期申请：{}退款操作，借款人:{}的微信账户更新失败！",orgId,loaneeId);
						throw new RuntimeException("延期申请:"+orgId+"，借款人"+loaneeId+"的微信账户更新失败！");
					}
				}else {
					logger.error("延期申请：{}退款操作，借款人:{}的微信退款请求失败！",orgId,loaneeId);
					throw new RuntimeException("延期申请:"+orgId+"，借款人"+loaneeId+"的微信退款请求失败！");
				}
			}else if(memberActTrx.getTrxCode().equals(TrxRuleConstant.GXT_LOAN_DELAY_LOANACT)){
				//退款到借款账户
				int code = actService.updateAct(TrxRuleConstant.GXT_REFUSE_LOAN_REFUND_LOANACT, amount, loanee, orgId);
				if(code == Constant.UPDATE_FAILED) {
					logger.error("延期申请：{}退款操作，借款人:{}的借款账户更新失败！",orgId,loaneeId);
					throw new RuntimeException("延期申请:"+orgId+"，借款人"+loaneeId+"的借款余额账户更新失败！");
				}
			}else if(memberActTrx.getTrxCode().equals(TrxRuleConstant.GXT_LOAN_DELAY_AVLACT)) {
				//退款到可用余额账户
				int code = actService.updateAct(TrxRuleConstant.GXT_REFUSE_LOAN_REFUND_AVLACT, amount, loanee, orgId);
				if(code == Constant.UPDATE_FAILED) {
					logger.error("延期申请：{}退款操作，借款人:{}的可用余额账户更新失败！",orgId,loaneeId);
					throw new RuntimeException("延期申请:"+orgId+"，借款人"+loaneeId+"的可用账户更新失败！");
				}
			}else {
				logger.warn("延期申请{}，退款时流水账户为{}",orgId,memberActTrx.getSubNo());
				continue;
			}
		}
	}

	@Override
	public NfsLoanPartialAndDelay getLoanerPartialApplyForApp(NfsLoanRecord loanRecord) {
		NfsLoanPartialAndDelay partialAndDelay = new NfsLoanPartialAndDelay();
		partialAndDelay.setMember(loanRecord.getLoaner());
		partialAndDelay.setMemberRole(LoanRole.loaner);
		partialAndDelay.setLoan(loanRecord);
		partialAndDelay.setStatus(NfsLoanPartialAndDelay.Status.confirm);
		List<NfsLoanPartialAndDelay> loanerPartialAndDelays = findList(partialAndDelay);
		if(!Collections3.isEmpty(loanerPartialAndDelays)) {
			return loanerPartialAndDelays.get(0);
		}
		return null;
	}
}