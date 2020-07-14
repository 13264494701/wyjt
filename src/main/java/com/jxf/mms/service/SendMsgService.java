package com.jxf.mms.service;


import com.jxf.mem.entity.MemberMessage;
import com.jxf.mms.record.entity.MmsAppMsgRecord;
import com.jxf.mms.record.entity.MmsSmsRecord;
/**
 * 
 * @类功能说明： 发信息服务接口类
 * @类修改者： 
 * @修改日期： 
 * @修改说明： 
 * @公司名称：北京金信服科技有限公司
 * @作者：HUOJIABAO 
 * @创建时间：2016年4月8日 上午11:20:48 
 * @版本：V1.0
 */
public interface SendMsgService {


	public void sendSMS(MmsSmsRecord sms);
	
	public void sendAppMsg(MmsAppMsgRecord appMsg);
	
	public void beforeSendAppMsg(MemberMessage message);

}
