package com.jxf.mem.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.mem.dao.MmsSmsUserRecordDao;
import com.jxf.mem.entity.MmsSmsUserRecord;
import com.jxf.mem.service.MmsSmsUserRecordService;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
/**
 * 发送短信记录表ServiceImpl
 * @author gaobo
 * @version 2019-03-19
 */
@Service("mmsSmsUserRecordService")
@Transactional(readOnly = true)
public class MmsSmsUserRecordServiceImpl extends CrudServiceImpl<MmsSmsUserRecordDao, MmsSmsUserRecord> implements MmsSmsUserRecordService{

    @Override
	public MmsSmsUserRecord get(Long id) {
		return super.get(id);
	}
	
	@Override
	public List<MmsSmsUserRecord> findList(MmsSmsUserRecord mmsSmsUserRecord) {
		return super.findList(mmsSmsUserRecord);
	}
	
	@Override
	public Page<MmsSmsUserRecord> findPage(Page<MmsSmsUserRecord> page, MmsSmsUserRecord mmsSmsUserRecord) {
		return super.findPage(page, mmsSmsUserRecord);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void save(MmsSmsUserRecord mmsSmsUserRecord) {
		super.save(mmsSmsUserRecord);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void delete(MmsSmsUserRecord mmsSmsUserRecord) {
		super.delete(mmsSmsUserRecord);
	}
	
}