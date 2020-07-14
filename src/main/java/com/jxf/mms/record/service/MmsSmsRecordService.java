package com.jxf.mms.record.service;

import java.util.Date;

import com.jxf.svc.sys.crud.service.CrudService;
import com.jxf.mms.record.entity.MmsSmsRecord;

/**
 * 短信记录Service
 * @author JINXINFU
 * @version 2016-04-08
 */
public interface MmsSmsRecordService extends CrudService<MmsSmsRecord> {

	/**
	 * 获取某时间之后的发送条数
	 * @param type
	 * @param phoneNo
	 * @param beginTime
	 * @return
	 */
	public int countSmsHasSend(String tmplCode,String phoneNo,Date beginTime);
	/**
	 * 获取最近的一条记录
	 * @param type
	 * @param phoneNo
	 * @return
	 */
	public MmsSmsRecord getRecentRecord(String tmplCode,String phoneNo);
	/**
	 * 
	 * 获取最近发送的验证码
	 * zhuhuijie  2016年3月18日 
	 * 修改者名字 修改日期 
	 * 修改内容 
	 * @参数： @param msgType
	 * @参数： @param phoneNo
	 * @参数： @param verifyCode
	 * @参数： @return     
	 * @return MmsSmsRecord    
	 * @throws
	 */
	public MmsSmsRecord getSendedVerifyCode(String tmplCode,String phoneNo, String verifyCode);
	
}