package com.jxf.mms.service.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jxf.mem.entity.MemberMessage;
import com.jxf.mms.gateway.HttpSMSGateWay;
import com.jxf.mms.producer.TaskProducer;
import com.jxf.mms.record.entity.MmsAppMsgRecord;

import com.jxf.mms.record.entity.MmsSmsRecord;

import com.jxf.mms.record.service.MmsSmsRecordService;
import com.jxf.mms.service.SendMsgService;


/**
 * 
 * @类功能说明： 发信息服务实现类
 * @类修改者： 
 * @修改日期： 
 * @修改说明： 
 * @公司名称：北京金信服科技有限公司
 * @作者：HUOJIABAO
 * @创建时间：2016年4月10日 上午12:25:52 
 * @版本：V1.0
 */
@Service("sendMsgService")
public class SendMsgServiceImpl implements SendMsgService {
	
	private static Logger logger = LoggerFactory.getLogger(SendMsgServiceImpl.class);
	
	@Autowired
	private MmsSmsRecordService mmsSmsRecordService;
	@Autowired
	private HttpSMSGateWay smsGateWay;
	


	public void sendSMS(MmsSmsRecord sms) {
		//添加短信记录到数据库
		mmsSmsRecordService.save(sms);
		//添加短信对象到发送队列
		TaskProducer.addSMS(sms);
//		int result = smsGateWay.send(sms.getPhoneNo(),sms.getContent());
//		if (result == 0) {
//			logger.info("发送短信成功,短信编号[{}]手机号码[{}]",sms.getId(),sms.getPhoneNo());
//		} else {
//			logger.error("发送短信时发生异常, 异常短信编号[{}]手机号码[{}]",sms.getId(),sms.getPhoneNo());
//		}
	}



	@Override
	public void sendAppMsg(MmsAppMsgRecord appMsg) {

		TaskProducer.addAppMsg(appMsg);
	}



	@Override
	public void beforeSendAppMsg(MemberMessage message) {
		
		MmsAppMsgRecord appMsg = new MmsAppMsgRecord();
		appMsg.setMemberId(message.getMember().getId().toString());
		appMsg.setTitle(message.getContent());
		appMsg.setMode(String.valueOf(message.getType().ordinal()));
		appMsg.setMsgId(String.valueOf(message.getId()));
		String orgId = String.valueOf(message.getOrgId());
		int orgType = Integer.parseInt(message.getOrgType());
		if(orgType == 3) {
			appMsg.setPara(orgId);
		}else {
			appMsg.setPara(orgId+"|"+orgType);
		}
		
		sendAppMsg(appMsg);
	}

}
