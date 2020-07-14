package com.jxf.nfs.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.mem.entity.Member;
import com.jxf.nfs.constant.TrxRuleConstant;
import com.jxf.nfs.dao.NfsRchgRecordDao;
import com.jxf.nfs.entity.NfsRchgRecord;
import com.jxf.nfs.service.NfsActService;
import com.jxf.nfs.service.NfsRchgRecordService;
import com.jxf.svc.config.Constant;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
/**
 * 充值记录ServiceImpl
 * @author suhuimin
 * @version 2018-10-10
 */
@Service("nfsRchgRecordService")
@Transactional(readOnly = true)
public class NfsRchgRecordServiceImpl extends CrudServiceImpl<NfsRchgRecordDao, NfsRchgRecord> implements NfsRchgRecordService{
	
	@Autowired
	private NfsRchgRecordDao nfsRchgRecordDao;
	@Autowired
	private NfsActService actService;
	
	public NfsRchgRecord get(Long id) {
		return super.get(id);
	}
	
	public List<NfsRchgRecord> findList(NfsRchgRecord nfsRchgRecord) {
		return super.findList(nfsRchgRecord);
	}
	
	public Page<NfsRchgRecord> findPage(Page<NfsRchgRecord> page, NfsRchgRecord nfsRchgRecord) {
		return super.findPage(page, nfsRchgRecord);
	}
	
	@Transactional(readOnly = false)
	public void save(NfsRchgRecord nfsRchgRecord) {
		super.save(nfsRchgRecord);
	}
	
	@Transactional(readOnly = false)
	public void delete(NfsRchgRecord nfsRchgRecord) {
		super.delete(nfsRchgRecord);
	}
	
	public List<NfsRchgRecord> getRchgRecordByMemberId(Long memberId,Date loanTime){
		return nfsRchgRecordDao.getRchgRecordByMemberId(memberId,loanTime);
	}

	@Override
	@Transactional(readOnly = false)
	public int confirmSuccess(NfsRchgRecord rchgRecord,Member member) {
		save(rchgRecord);
		if(rchgRecord.getStatus().equals(NfsRchgRecord.Status.success)) {
			BigDecimal amount = rchgRecord.getAmount();
			int code = actService.updateAct(TrxRuleConstant.MEMBER_RECHARGE, amount, member, rchgRecord.getId());
			if(code == Constant.UPDATE_FAILED) {
				throw new RuntimeException("会员"+member.getId()+"充值后确认充值结果时账户更新失败！充值金额："+amount); 
			}
		}
		return Constant.UPDATE_SUCCESS;
	}
	
}