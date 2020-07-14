package com.jxf.mms.msg.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.mms.consts.SendPrior;
import com.jxf.mms.msg.SendAppMsgService;

import com.jxf.mms.record.entity.MmsSmsRecord;
import com.jxf.mms.service.SendMsgService;
import com.jxf.mms.tmpl.service.MmsMsgTmplService;

/**
 * 
 * @类功能说明： 
 * @类修改者： 
 * @修改日期： 
 * @修改说明： 
 * @公司名称：北京金信服科技有限公司
 * @作者：HUOJIABAO 
 * @创建时间：2016年4月26日 下午4:31:26 
 * @版本：V1.0
 */
@Service("sendAppMsgService")
@Transactional(readOnly = true)
public class SendAppMsgServiceImpl implements  SendAppMsgService{
	
	@Autowired
	private SendMsgService sendMsgService;
	
	@Autowired
	private MmsMsgTmplService msgTmplService;
	
	@Override
	@Transactional(readOnly = false)
	public void sendSms(String tmplCode,String phoneNo,String smsCode){

		// 获取短信内容
		String smsContent = "";
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("verifyCode", smsCode);
		
		smsContent = msgTmplService.process(tmplCode, paramMap);
		
		MmsSmsRecord sms = new MmsSmsRecord();
		sms.setTmplCode(tmplCode);
		sms.setMsgPriority(SendPrior.High.getCode());
		sms.setPhoneNo(phoneNo);
		sms.setContent(smsContent);
		sms.setVerifyCode(smsCode);
		sms.setSendTime(new Date());
		sendMsgService.sendSMS(sms);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void sendMessage(String type,String phoneNo,Map<String, Object> map){

		// 获取短信内容
		String smsContent = msgTmplService.process(type, map);
		
		MmsSmsRecord sms = new MmsSmsRecord();
		sms.setTmplCode(type);
		sms.setMsgPriority(SendPrior.High.getCode());
		sms.setPhoneNo(phoneNo);
		sms.setContent(smsContent);
		sms.setSendTime(new Date());
		sendMsgService.sendSMS(sms);
	}

}
