package com.jxf.nfs.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.check.service.NfsCheckRecordService;
import com.jxf.mem.entity.Member;
import com.jxf.nfs.constant.TrxRuleConstant;
import com.jxf.nfs.dao.NfsFundAddReduceDao;
import com.jxf.nfs.entity.NfsFundAddReduce;
import com.jxf.nfs.service.NfsActService;
import com.jxf.nfs.service.NfsFundAddReduceService;
import com.jxf.svc.config.Constant;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
/**
 * 会员加减款记录ServiceImpl
 * @author suHuimin
 * @version 2019-01-26
 */
@Service("nfsFundAddReduceService")
@Transactional(readOnly = true)
public class NfsFundAddReduceServiceImpl extends CrudServiceImpl<NfsFundAddReduceDao, NfsFundAddReduce> implements NfsFundAddReduceService{

	@Autowired
	private NfsActService actService;
	@Autowired
	private NfsCheckRecordService checkRecordService;
	
	public NfsFundAddReduce get(Long id) {
		return super.get(id);
	}
	
	public List<NfsFundAddReduce> findList(NfsFundAddReduce nfsFundAddReduce) {
		return super.findList(nfsFundAddReduce);
	}
	
	public Page<NfsFundAddReduce> findPage(Page<NfsFundAddReduce> page, NfsFundAddReduce nfsFundAddReduce) {
		return super.findPage(page, nfsFundAddReduce);
	}
	
	@Transactional(readOnly = false)
	public void save(NfsFundAddReduce nfsFundAddReduce) {
		super.save(nfsFundAddReduce);
	}
	
	@Transactional(readOnly = false)
	public void delete(NfsFundAddReduce nfsFundAddReduce) {
		super.delete(nfsFundAddReduce);
	}

	@Override
	public Page<NfsFundAddReduce> getCheckedList(Page<NfsFundAddReduce> page, NfsFundAddReduce fundAddReduce) {
		fundAddReduce.setPage(page);
		page.setList(dao.getCheckedList(fundAddReduce));
		return page;
	}

	@Override
	@Transactional(readOnly = false)
	public int applyCheck(NfsFundAddReduce fundAddReduce) {
		save(fundAddReduce);
		Member member = fundAddReduce.getMember();
		if(fundAddReduce.getType().equals(NfsFundAddReduce.Type.add) && fundAddReduce.getStatus().equals(NfsFundAddReduce.Status.passed)) {
			int code = actService.updateAct(TrxRuleConstant.MEMBER_BAL_ADD, fundAddReduce.getAmount(), member, fundAddReduce.getId());
			if(code==Constant.UPDATE_FAILED) {
				logger.error("会员账户加款操作失败，会员id{},加款申请id{}",member.getId(),fundAddReduce.getId());
				throw new RuntimeException("会员账户加款操作失败!");
			}
		}else if(fundAddReduce.getType().equals(NfsFundAddReduce.Type.reduce) && fundAddReduce.getStatus().equals(NfsFundAddReduce.Status.passed)) {
			int code = actService.updateAct(TrxRuleConstant.MEMBER_BAL_REDUCE, fundAddReduce.getAmount(), member,fundAddReduce.getId());
			if(code==Constant.UPDATE_FAILED) {
				logger.error("会员账户减款操作失败，会员id{},减款申请id{}",member.getId(),fundAddReduce.getId());
				throw new RuntimeException("会员账户减款操作失败!");
			}
		}
		checkRecordService.saveMemberFundAddReduceCheckLog(fundAddReduce);
		return Constant.UPDATE_SUCCESS;
	}

}