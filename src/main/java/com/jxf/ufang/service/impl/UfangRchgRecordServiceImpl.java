package com.jxf.ufang.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
import com.jxf.ufang.entity.UfangRchgRecord;
import com.jxf.ufang.dao.UfangRchgRecordDao;
import com.jxf.ufang.service.UfangRchgRecordService;
/**
 * 优放充值记录ServiceImpl
 * @author Administrator
 * @version 2018-12-07
 */
@Service("ufangRchgRecordService")
@Transactional(readOnly = true)
public class UfangRchgRecordServiceImpl extends CrudServiceImpl<UfangRchgRecordDao, UfangRchgRecord> implements UfangRchgRecordService{

	public UfangRchgRecord get(Long id) {
		return super.get(id);
	}
	
	public List<UfangRchgRecord> findList(UfangRchgRecord ufangRchgRecord) {
		return super.findList(ufangRchgRecord);
	}
	
	public Page<UfangRchgRecord> findPage(Page<UfangRchgRecord> page, UfangRchgRecord ufangRchgRecord) {
		return super.findPage(page, ufangRchgRecord);
	}
	
	@Transactional(readOnly = false)
	public void save(UfangRchgRecord ufangRchgRecord) {
		super.save(ufangRchgRecord);
	}
	
	@Transactional(readOnly = false)
	public void delete(UfangRchgRecord ufangRchgRecord) {
		super.delete(ufangRchgRecord);
	}
	
}