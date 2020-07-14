package com.jxf.ufang.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.check.service.NfsCheckRecordService;
import com.jxf.nfs.constant.TrxRuleConstant;
import com.jxf.nfs.service.NfsActService;
import com.jxf.svc.config.Constant;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
import com.jxf.ufang.dao.UfangFundAddReduceDao;
import com.jxf.ufang.entity.UfangFundAddReduce;
import com.jxf.ufang.service.UfangFundAddReduceService;
/**
 * 优放机构加减款记录ServiceImpl
 * @author suHuimin
 * @version 2019-01-26
 */
@Service("ufangFundAddReduceService")
@Transactional(readOnly = true)
public class UfangFundAddReduceServiceImpl extends CrudServiceImpl<UfangFundAddReduceDao, UfangFundAddReduce> implements UfangFundAddReduceService{
	
	@Autowired
	private NfsCheckRecordService checkRecordService;
	@Autowired
	private NfsActService actService;
	
	public UfangFundAddReduce get(Long id) {
		return super.get(id);
	}
	
	public List<UfangFundAddReduce> findList(UfangFundAddReduce ufangFundAddReduce) {
		return super.findList(ufangFundAddReduce);
	}
	
	public Page<UfangFundAddReduce> findPage(Page<UfangFundAddReduce> page, UfangFundAddReduce ufangFundAddReduce) {
		return super.findPage(page, ufangFundAddReduce);
	}
	
	@Transactional(readOnly = false)
	public void save(UfangFundAddReduce ufangFundAddReduce) {
		super.save(ufangFundAddReduce);
	}
	
	@Transactional(readOnly = false)
	public void delete(UfangFundAddReduce ufangFundAddReduce) {
		super.delete(ufangFundAddReduce);
	}

	@Override
	public Page<UfangFundAddReduce> getCheckedList(Page<UfangFundAddReduce> page,UfangFundAddReduce ufangFundAddReduce) {
		ufangFundAddReduce.setPage(page);
		page.setList(dao.getCheckedList(ufangFundAddReduce));
		return page;
	}

	@Override
	@Transactional(readOnly = false)
	public int ufangBrnAddReduceApplyCheck(UfangFundAddReduce ufangFundAddReduce) {
		save(ufangFundAddReduce);
		if(ufangFundAddReduce.getType().equals(UfangFundAddReduce.Type.add) && ufangFundAddReduce.getStatus().equals(UfangFundAddReduce.Status.passed)) {
			int code = actService.updateAct(TrxRuleConstant.UFANG_BAL_ADD, ufangFundAddReduce.getAmount(), ufangFundAddReduce.getUfangBrn(),ufangFundAddReduce.getId());
			if(code == Constant.UPDATE_FAILED) {
				return Constant.UPDATE_FAILED;
			}
		}else if(ufangFundAddReduce.getType().equals(UfangFundAddReduce.Type.reduce) && ufangFundAddReduce.getStatus().equals(UfangFundAddReduce.Status.passed)){
			int code = actService.updateAct(TrxRuleConstant.UFANG_BAL_REDUCE, ufangFundAddReduce.getAmount(), ufangFundAddReduce.getUfangBrn(),ufangFundAddReduce.getId());
			if(code == Constant.UPDATE_FAILED) {
				logger.error("优放机构{}账户异常，加减款操作失败！",ufangFundAddReduce.getUfangBrn().getBrnNo());
				throw new RuntimeException("优放机构"+ufangFundAddReduce.getUfangBrn().getBrnNo()+"加减款操作失败");
			}
		}
		checkRecordService.saveUfangFundAddReduceCheckLog(ufangFundAddReduce);
		return Constant.UPDATE_SUCCESS;
	}
	
}