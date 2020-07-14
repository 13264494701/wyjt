package com.jxf.mms.record.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
import com.jxf.mms.record.dao.MmsEmailRecordDao;
import com.jxf.mms.record.entity.MmsEmailRecord;
import com.jxf.mms.record.service.MmsEmailRecordService;
/**
 * 
 * @类功能说明： 
 * @类修改者： 
 * @修改日期： 
 * @修改说明： 
 * @公司名称：北京金信服科技有限公司
 * @作者：HUOJIABAO 
 * @创建时间：2016年4月7日 下午3:26:30 
 * @版本：V1.0
 */
@Service("mmsEmailRecordService")
@Transactional(readOnly = true)
public class MmsEmailRecordServiceImpl extends CrudServiceImpl<MmsEmailRecordDao, MmsEmailRecord> implements MmsEmailRecordService{

	public MmsEmailRecord get(Long id) {
		return super.get(id);
	}
	
	public List<MmsEmailRecord> findList(MmsEmailRecord mmsEmailRecord) {
		Page<MmsEmailRecord> page = new Page<MmsEmailRecord>();
		page.setOrderBy("send_time desc");
		mmsEmailRecord.setPage(page);
		return super.findList(mmsEmailRecord);
	}
	
	public Page<MmsEmailRecord> findPage(Page<MmsEmailRecord> page, MmsEmailRecord mmsEmailRecord) {
		return super.findPage(page, mmsEmailRecord);
	}
	
	@Transactional(readOnly = false)
	public void save(MmsEmailRecord mmsEmailRecord) {
		super.save(mmsEmailRecord);
	}
	
	@Transactional(readOnly = false)
	public void delete(MmsEmailRecord mmsEmailRecord) {
		super.delete(mmsEmailRecord);
	}
	/**
	 * 验证验证码是否存在
	 * @param msgType
	 * @param phoneNo 
	 * @param verifyCode
	 * @return
	 */
	public MmsEmailRecord getSendedVerifyCode(String tmplCode,String email, String verifyCode) {
		MmsEmailRecord emailRecord = new MmsEmailRecord();
		emailRecord.setTmplCode(tmplCode);
		emailRecord.setReceiverAddr(email);
		emailRecord.setVerifyCode(verifyCode);
		
		List<MmsEmailRecord> records = findList(emailRecord);
		if(records.size()==1){
		   return records.get(0);
		}
		return null;
	}
}