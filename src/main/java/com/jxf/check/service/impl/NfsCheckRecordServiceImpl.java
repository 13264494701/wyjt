package com.jxf.check.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.check.dao.NfsCheckRecordDao;
import com.jxf.check.entity.NfsCheckRecord;
import com.jxf.check.entity.NfsCheckRecord.CheckStatus;
import com.jxf.check.entity.NfsCheckRecord.OrgType;
import com.jxf.check.service.NfsCheckRecordService;
import com.jxf.mem.entity.MemberCancellation;
import com.jxf.nfs.entity.NfsFundAddReduce;
import com.jxf.nfs.entity.NfsWdrlRecord;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
import com.jxf.svc.sys.user.entity.User;
import com.jxf.svc.sys.util.UserUtils;
import com.jxf.ufang.entity.UfangFundAddReduce;
/**
 * 审核记录ServiceImpl
 * @author suHuimin
 * @version 2019-01-26
 */
@Service("nfsCheckRecordService")
@Transactional(readOnly = true)
public class NfsCheckRecordServiceImpl extends CrudServiceImpl<NfsCheckRecordDao, NfsCheckRecord> implements NfsCheckRecordService{
	
	@Autowired
	private NfsCheckRecordService checkRecordService;

	public NfsCheckRecord get(Long id) {
		return super.get(id);
	}
	
	public List<NfsCheckRecord> findList(NfsCheckRecord nfsCheckRecord) {
		return super.findList(nfsCheckRecord);
	}
	
	public Page<NfsCheckRecord> findPage(Page<NfsCheckRecord> page, NfsCheckRecord nfsCheckRecord) {
		return super.findPage(page, nfsCheckRecord);
	}
	
	@Transactional(readOnly = false)
	public void save(NfsCheckRecord nfsCheckRecord) {
		super.save(nfsCheckRecord);
	}
	
	@Transactional(readOnly = false)
	public void delete(NfsCheckRecord nfsCheckRecord) {
		super.delete(nfsCheckRecord);
	}
	
	@Override
	@Transactional(readOnly=false)
	public NfsCheckRecord saveWdrlCheckLog(NfsWdrlRecord wdrlRecord) {

		User checker = UserUtils.getUser();
		//审核记录
		NfsCheckRecord checkRecord = new NfsCheckRecord();
		checkRecord.setCheckerName(checker.getEmpNam());
		checkRecord.setCheckerNo(checker.getEmpNo());
		switch (wdrlRecord.getStatus()) {
		case refuse:
			checkRecord.setCheckStatus(CheckStatus.reject);
			break;
		case cancel:
			checkRecord.setCheckStatus(CheckStatus.cancel);
			break;
		case pendingPay:
			checkRecord.setCheckStatus(CheckStatus.pass_manual);
			break;
		case failure:
			checkRecord.setCheckStatus(CheckStatus.cancel);
			break;
		default:
			break;
		}
		checkRecord.setCheckTime(new Date());
		checkRecord.setOrgType(OrgType.withdraw);
		checkRecord.setOrgId(wdrlRecord.getId());
		checkRecordService.save(checkRecord);
		return checkRecord;
	}
	@Override
	@Transactional(readOnly=false)
	public NfsCheckRecord saveMemberFundAddReduceCheckLog(NfsFundAddReduce fundAddReduce) {
		User checker = UserUtils.getUser();
		//审核记录
		NfsCheckRecord checkRecord = new NfsCheckRecord();
		checkRecord.setCheckerName(checker.getEmpNam());
		checkRecord.setCheckerNo(checker.getEmpNo());
		switch (fundAddReduce.getStatus()) {
		case passed:
			checkRecord.setCheckStatus(CheckStatus.pass_manual);
			break;
		case reject:
			checkRecord.setCheckStatus(CheckStatus.reject);
			break;
		default:
			break;
		}
		checkRecord.setCheckTime(new Date());
		checkRecord.setOrgType(fundAddReduce.getType().equals(NfsFundAddReduce.Type.add) ? OrgType.memberAddBal:OrgType.memberSubBal);
		checkRecord.setOrgId(fundAddReduce.getId());
		checkRecordService.save(checkRecord);
		return checkRecord;
	}
	
	@Override
	@Transactional(readOnly=false)
	public NfsCheckRecord saveUfangFundAddReduceCheckLog(UfangFundAddReduce ufangFundAddReduce) {
		User checker = UserUtils.getUser();
		NfsCheckRecord checkRecord = new NfsCheckRecord();
		checkRecord.setCheckerName(checker.getEmpNam());
		checkRecord.setCheckerNo(checker.getEmpNo());
		checkRecord.setCheckStatus(ufangFundAddReduce.getStatus().equals(UfangFundAddReduce.Status.passed) ? CheckStatus.pass_manual:CheckStatus.reject);
		checkRecord.setCheckTime(new Date());
		checkRecord.setOrgId(ufangFundAddReduce.getId());
		if(ufangFundAddReduce.getType().equals(UfangFundAddReduce.Type.add) ) {
			checkRecord.setOrgType(NfsCheckRecord.OrgType.ufangBrnAddBal);
		}else if(ufangFundAddReduce.getType().equals(UfangFundAddReduce.Type.reduce) ) {
			checkRecord.setOrgType(NfsCheckRecord.OrgType.ufangBrnSubBal);
		}
		checkRecordService.save(checkRecord);
		return checkRecord;
	}

	@Override
	@Transactional(readOnly=false)
	public NfsCheckRecord saveCancellationCheckLog(MemberCancellation memberCancellation) {
		User checker = UserUtils.getUser();
		NfsCheckRecord checkRecord = new NfsCheckRecord();
		checkRecord.setCheckerName(checker.getEmpNam());
		checkRecord.setCheckerNo(checker.getEmpNo());
		checkRecord.setCheckStatus(memberCancellation.getStatus().equals(MemberCancellation.Status.success) ? CheckStatus.pass_manual:CheckStatus.reject);
		checkRecord.setCheckTime(new Date());
		checkRecord.setOrgId(memberCancellation.getId());
		checkRecord.setOrgType(NfsCheckRecord.OrgType.memberCancellation);
		checkRecordService.save(checkRecord);
		return checkRecord;
	}
}