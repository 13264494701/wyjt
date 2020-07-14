package com.jxf.mem.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.mem.dao.MessageUserRecordDao;
import com.jxf.mem.entity.MessageUserRecord;
import com.jxf.mem.service.MessageUserRecordService;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
/**
 * 发送站内信记录表ServiceImpl
 * @author gaobo
 * @version 2019-03-19
 */
@Service("messageUserRecordService")
@Transactional(readOnly = true)
public class MessageUserRecordServiceImpl extends CrudServiceImpl<MessageUserRecordDao, MessageUserRecord> implements MessageUserRecordService{

    @Override
	public MessageUserRecord get(Long id) {
		return super.get(id);
	}
	
	@Override
	public List<MessageUserRecord> findList(MessageUserRecord messageUserRecord) {
		return super.findList(messageUserRecord);
	}
	
	@Override
	public Page<MessageUserRecord> findPage(Page<MessageUserRecord> page, MessageUserRecord messageUserRecord) {
		return super.findPage(page, messageUserRecord);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void save(MessageUserRecord messageUserRecord) {
		super.save(messageUserRecord);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void delete(MessageUserRecord messageUserRecord) {
		super.delete(messageUserRecord);
	}
	
}