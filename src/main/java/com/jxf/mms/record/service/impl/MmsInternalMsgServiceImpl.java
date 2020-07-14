package com.jxf.mms.record.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
import com.jxf.mms.record.dao.MmsInternalMsgDao;
import com.jxf.mms.record.entity.MmsInternalMsg;
import com.jxf.mms.record.service.MmsInternalMsgService;

/**
 * 站内消息Service
 * @author zhj
 * @version 2016-02-16
 */
@Service("mmsInternalMsgService")
@Transactional(readOnly = true)
public class MmsInternalMsgServiceImpl extends CrudServiceImpl<MmsInternalMsgDao, MmsInternalMsg> implements MmsInternalMsgService{

	@Autowired
	private MmsInternalMsgDao mmsInternalMsgDao;
	
	public MmsInternalMsg get(Long id) {
		return super.get(id);
	}
	
	public List<MmsInternalMsg> findList(MmsInternalMsg nfsMmsInternalMsg) {
		return super.findList(nfsMmsInternalMsg);
	}
	
	public Page<MmsInternalMsg> findPage(Page<MmsInternalMsg> page, MmsInternalMsg nfsMmsInternalMsg) {
		return super.findPage(page, nfsMmsInternalMsg);
	}
	
	@Transactional(readOnly = false)
	public void save(MmsInternalMsg nfsMmsInternalMsg) {
		super.save(nfsMmsInternalMsg);
	}
	
	@Transactional(readOnly = false)
	public void delete(MmsInternalMsg nfsMmsInternalMsg) {
		super.delete(nfsMmsInternalMsg);
	}
	@Transactional(readOnly = false)
	public void sendInternalMsg(String sender,String receiver,String tmplCode,String subject,String content){
		MmsInternalMsg internalMsg = new MmsInternalMsg();
		internalMsg.preInsert();
		internalMsg.setSender(sender);
		internalMsg.setReceiver(receiver);
		internalMsg.setTmplCode(tmplCode);
		internalMsg.setSubject(subject);
		internalMsg.setContent(content);
		internalMsg.setIsread("0");
		internalMsg.setIsflag("0");
		internalMsg.setSendTime(new Date());
		mmsInternalMsgDao.insert(internalMsg);
	}
	
	
}