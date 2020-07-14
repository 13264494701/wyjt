package com.jxf.mms.record.service;

import com.jxf.svc.sys.crud.service.CrudService;
import com.jxf.mms.record.entity.MmsEmailRecord;

/**
 * 邮件记录Service
 * @author JINXINFU
 * @version 2016-04-08
 */
public interface MmsEmailRecordService extends CrudService<MmsEmailRecord> {

	/**
	 * 验证验证码是否存在
	 * @param msgType
	 * @param phoneNo 
	 * @param verifyCode
	 * @return
	 */
	public MmsEmailRecord getSendedVerifyCode(String tmplCode,String email, String verifyCode);
	
}