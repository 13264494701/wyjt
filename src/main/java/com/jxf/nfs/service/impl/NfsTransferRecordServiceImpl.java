package com.jxf.nfs.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberMessage;
import com.jxf.mem.service.MemberActService;
import com.jxf.mem.service.MemberMessageService;
import com.jxf.mem.service.MemberService;
import com.jxf.nfs.constant.ActSubConstant;
import com.jxf.nfs.constant.TrxRuleConstant;
import com.jxf.nfs.dao.NfsTransferRecordDao;
import com.jxf.nfs.entity.NfsTransferRecord;
import com.jxf.nfs.service.NfsActService;
import com.jxf.nfs.service.NfsTransferRecordService;
import com.jxf.svc.config.Constant;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
/**
 * 转账ServiceImpl
 * @author XIAORONGDIAN
 * @version 2018-11-09
 */
@Service("nfsTransferRecordService")
@Transactional(readOnly = true)
public class NfsTransferRecordServiceImpl extends CrudServiceImpl<NfsTransferRecordDao, NfsTransferRecord> implements NfsTransferRecordService{

	@Autowired
	private NfsTransferRecordDao nfsTransferRecordDao;
	@Autowired
	private MemberService memberService;
	@Autowired
	private NfsActService actService;
	@Autowired
	private MemberMessageService memberMessageService;
	@Autowired
	private MemberActService memberActService;
	
	public NfsTransferRecord get(Long id) {
		return super.get(id);
	}
	
	public List<NfsTransferRecord> findList(NfsTransferRecord nfsTransferRecord) {
		return super.findList(nfsTransferRecord);
	}
	
	public Page<NfsTransferRecord> findPage(Page<NfsTransferRecord> page, NfsTransferRecord nfsTransferRecord) {
		return super.findPage(page, nfsTransferRecord);
	}
	
	@Transactional(readOnly = false)
	public void save(NfsTransferRecord nfsTransferRecord) {
		super.save(nfsTransferRecord);
	}
	
	@Transactional(readOnly = false)
	public void delete(NfsTransferRecord nfsTransferRecord) {
		super.delete(nfsTransferRecord);
	}

	@Override
	public List<NfsTransferRecord> findByMemberIdAndFriendIdAndCreateDate(
			Member member, Member friend, Date earliestTime) {
		return nfsTransferRecordDao.findByMemberIdAndFriendIdAndCreateDate(member,friend,earliestTime);
	}

	@Override
	public Integer todayHasTransferFailedTimes(Member member, Date startTime) {
		return nfsTransferRecordDao.todayHasTransferFailedTimes(member,startTime);
	}

	@Override
	public List<NfsTransferRecord> findByMemberIdAndFriendId(Long memberId,
			Long friendId) {
		return nfsTransferRecordDao.findByMemberIdAndFriendId(memberId,friendId);
	}

	@Override
	public Page<NfsTransferRecord> findPageList(
			NfsTransferRecord nfsTransferRecord, Integer pageNo, Integer pageSize) {
		Page<NfsTransferRecord> page = new Page<NfsTransferRecord>(pageNo == null?1:pageNo, pageSize == null?20:pageSize);	
		nfsTransferRecord.setPage(page);
		List<NfsTransferRecord> applyList = nfsTransferRecordDao.findList(nfsTransferRecord);
		page.setList(applyList);
		return page;
	}

	@Override
	public Page<NfsTransferRecord> findAllTransferPageList(Member me, Integer pageNo,
			Integer pageSize) {
		Page<NfsTransferRecord> page = new Page<NfsTransferRecord>(pageNo == null?1:pageNo, pageSize == null?20:pageSize);
		NfsTransferRecord nfsTransferRecord = new NfsTransferRecord();
		nfsTransferRecord.setPage(page);
		nfsTransferRecord.setMember(me);
		List<NfsTransferRecord> applyList = nfsTransferRecordDao.findAllTransferPageList(nfsTransferRecord);
		page.setList(applyList);
		return page;
	}

	@Override
	@Transactional(readOnly=false)
	public void updateActForTransfer(NfsTransferRecord transferRecord) {
		//可以转给任何人，优先转借款账户钱 amount 转账金额
		Long orgId = transferRecord.getId();
		Member payer = transferRecord.getMember();//付款人
		payer = memberService.get(payer);
		Member payee = transferRecord.getFriend();//收款人
		payee = memberService.get(payee);
		BigDecimal amount = transferRecord.getAmount();//转账金额
		int code = Constant.UPDATE_FAILED;
		BigDecimal LoanActBal = memberActService.getMemberAct(payer, ActSubConstant.MEMBER_LOAN_BAL).getCurBal();
		if(LoanActBal.compareTo(amount) >= 0) {
			//借款账户的余额够本次转账
			code = actService.updateAct(TrxRuleConstant.MEMBER_TRANSFER_LOANACT, amount, payer, payee, orgId);
			if(code == Constant.UPDATE_FAILED) {
				logger.error("转账业务ID：" + transferRecord.getId() + " 借款账户扣款失败！");
				throw new RuntimeException("转账业务ID：" + transferRecord.getId() + " 借款账户扣款失败！");
			}
		}else {
			//借款账户钱不够
			if(LoanActBal.compareTo(BigDecimal.ZERO) <= 0) {
				//借款账户没钱
				code = actService.updateAct(TrxRuleConstant.MEMBER_TRANSFER_AVLACT, amount, payer, payee, orgId);
				if(code == Constant.UPDATE_FAILED) {
					logger.error("转账业务ID：" + transferRecord.getId() + " 当前借款账户余额为0且可用余额账户扣款失败！");
					throw new RuntimeException("转账业务ID：" + transferRecord.getId() + " 当前借款账户余额为0且可用余额账户扣款失败！");
				}
			}else {
				//先扣借款账户再扣可用余额账户
				code = actService.updateAct(TrxRuleConstant.MEMBER_TRANSFER_LOANACT, LoanActBal, payer, payee, orgId);
				if(code == Constant.UPDATE_FAILED) {
					logger.error("转账业务ID：" + transferRecord.getId() + " 借款账户全额扣款失败！");
					throw new RuntimeException("转账业务ID：" + transferRecord.getId() + " 借款账户全额扣款失败！");
				}
				code = actService.updateAct(TrxRuleConstant.MEMBER_TRANSFER_AVLACT, amount.subtract(LoanActBal), payer, payee, orgId);
				if(code == Constant.UPDATE_FAILED) {
					logger.error("转账业务ID：" + transferRecord.getId() + " 可用余额账户扣款失败！");
					throw new RuntimeException("转账业务ID：" + transferRecord.getId() + " 可用余额账户扣款失败！");
				}
			}
		}
		transferRecord.setStatus(NfsTransferRecord.Status.alreadyReceived);		
		save(transferRecord);
		//发送会员消息
		memberMessageService.sendMessage(MemberMessage.Type.transferAccounts,transferRecord.getId());
	}

	@Override
	public BigDecimal getTotalTransferOneYear(NfsTransferRecord transfer) {
		return nfsTransferRecordDao.getTotalTransferOneYear(transfer);
	}

}