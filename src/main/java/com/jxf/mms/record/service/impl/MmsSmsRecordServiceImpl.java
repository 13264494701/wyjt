package com.jxf.mms.record.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
import com.jxf.mms.record.dao.MmsSmsRecordDao;
import com.jxf.mms.record.entity.MmsSmsRecord;
import com.jxf.mms.record.service.MmsSmsRecordService;

/**
 * 
 * @类功能说明： 短信记录ServiceImpl
 * @类修改者： 
 * @修改日期： 
 * @修改说明： 
 * @公司名称：北京金信服科技有限公司
 * @作者：HUOJIABAO 
 * @创建时间：2016年4月8日 下午3:29:14 
 * @版本：V1.0
 */
@Service("mmsSmsRecordService")
@Transactional(readOnly = true)
public class MmsSmsRecordServiceImpl extends CrudServiceImpl<MmsSmsRecordDao, MmsSmsRecord> implements MmsSmsRecordService{

	@Autowired
	private MmsSmsRecordDao mmsSmsRecordDao;
	
	public MmsSmsRecord get(Long id) {
		return super.get(id);
	}
	
	public List<MmsSmsRecord> findList(MmsSmsRecord mmsSmsRecord) {
		Page<MmsSmsRecord> page = new Page<MmsSmsRecord>();
		page.setOrderBy("send_time desc");
		mmsSmsRecord.setPage(page);
		return super.findList(mmsSmsRecord);
	}
	
	public Page<MmsSmsRecord> findPage(Page<MmsSmsRecord> page, MmsSmsRecord mmsSmsRecord) {
		return super.findPage(page, mmsSmsRecord);
	}
	
	@Transactional(readOnly = false)
	public void save(MmsSmsRecord mmsSmsRecord) {
		super.save(mmsSmsRecord);
	}
	
	@Transactional(readOnly = false)
	public void delete(MmsSmsRecord mmsSmsRecord) {
		super.delete(mmsSmsRecord);
	}
	/**
	 * 获取已经发送条数
	 * @param phoneNo
	 * @param msgType
	 * @param sendTime
	 * @return
	 */
	public int countSmsHasSend(String tmplCode,String phoneNo,Date beginTime ){
		MmsSmsRecord mmsSmsRecord = new MmsSmsRecord();
		mmsSmsRecord.setTmplCode(tmplCode);
		mmsSmsRecord.setPhoneNo(phoneNo);
		mmsSmsRecord.setBeginTime(beginTime);
		return mmsSmsRecordDao.countSmsHasSend(mmsSmsRecord);
	}
	/**
	 * 获取最近的一条记录
	 * @param phoneNo
	 * @param msgType
	 * @return
	 */
	public MmsSmsRecord getRecentRecord(String tmplCode,String phoneNo){
		MmsSmsRecord smsRecord = new MmsSmsRecord();
		smsRecord.setTmplCode(tmplCode);
		smsRecord.setPhoneNo(phoneNo);
		return mmsSmsRecordDao.getRecentRecord(smsRecord);
	}
	/**
	 * 验证验证码是否存在
	 * @param phoneNo 
	 * @param verifyCode
	 * @param msgType
	 * @return
	 */
	public MmsSmsRecord getSendedVerifyCode(String tmplCode,String phoneNo, String verifyCode) {
		MmsSmsRecord smsRecord = new MmsSmsRecord();
		smsRecord.setTmplCode(tmplCode);
		smsRecord.setPhoneNo(phoneNo);
		smsRecord.setVerifyCode(verifyCode);
		
		List<MmsSmsRecord> records = findList(smsRecord);
		if(records.size()>0){
		   return records.get(0);
		}
		return null;
	}
}