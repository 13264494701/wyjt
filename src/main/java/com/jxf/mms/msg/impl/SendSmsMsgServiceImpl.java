package com.jxf.mms.msg.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.mms.consts.SendPrior;
import com.jxf.mms.gateway.HttpSMSGateWay;
import com.jxf.mms.msg.SendSmsMsgService;
import com.jxf.mms.record.entity.MmsSmsRecord;
import com.jxf.mms.record.service.MmsSmsRecordService;
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
@Service("SendSmsMsgService")
@Transactional(readOnly = true)
public class SendSmsMsgServiceImpl implements  SendSmsMsgService{
	
	@Autowired
	private SendMsgService sendMsgService;
	
	@Autowired
	private MmsMsgTmplService msgTmplService;
	
	@Autowired
	private MmsSmsRecordService mmsSmsRecordService;
	
	@Autowired
	private HttpSMSGateWay smsGateWay;
	
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

	/**
	 * 	催收短信专用(行业账号)
	 */
	@Override
	@Transactional(readOnly = false)
	public void sendNormalSms(String type, String phoneNo, Map<String, Object> map) {
		// 获取短信内容
		String smsContent = msgTmplService.process(type, map);
		
		MmsSmsRecord sms = new MmsSmsRecord();
		sms.setTmplCode(type);
		sms.setMsgPriority(SendPrior.High.getCode());
		sms.setPhoneNo(phoneNo);
		sms.setContent(smsContent);
		sms.setSendTime(new Date());
		mmsSmsRecordService.save(sms);
		
		smsGateWay.send(phoneNo, smsContent);
		
	}
	
	/**
	 * 	催收短信专用(催收账号)
	 */
	@Override
	@Transactional(readOnly = false)
	public void sendCollectionSms(String type, String phoneNo, Map<String, Object> map) {
		// 获取短信内容
		String smsContent = msgTmplService.process(type, map);
		
		MmsSmsRecord sms = new MmsSmsRecord();
		sms.setTmplCode(type);
		sms.setMsgPriority(SendPrior.High.getCode());
		sms.setPhoneNo(phoneNo);
		sms.setContent(smsContent);
		sms.setSendTime(new Date());
		mmsSmsRecordService.save(sms);
		
		smsGateWay.sendCollectionMessage(phoneNo, smsContent);
		
	}
	
	@Override
	@Transactional(readOnly = false)
	public void sendNetLoanSms(String type, String phoneNo, Map<String, Object> map) {
		// 获取短信内容
		String smsContent = msgTmplService.process(type, map);
		
		MmsSmsRecord sms = new MmsSmsRecord();
		sms.setTmplCode(type);
		sms.setMsgPriority(SendPrior.High.getCode());
		sms.setPhoneNo(phoneNo);
		sms.setContent(smsContent);
		sms.setSendTime(new Date());
		mmsSmsRecordService.save(sms);
		
		smsGateWay.sendNetLoanMessage(phoneNo, smsContent);
		
	}
	
	/**
	 * 	后台发送短信(行业账号)
	 */
	@Override
	@Transactional(readOnly = false)
	public void sendNormalSmsForAdmin(String context, String phoneNo) {
		
		MmsSmsRecord sms = new MmsSmsRecord();
		sms.setTmplCode("1");
		sms.setMsgPriority(SendPrior.High.getCode());
		sms.setPhoneNo(phoneNo);
		sms.setContent(context);
		sms.setSendTime(new Date());
		mmsSmsRecordService.save(sms);
		
		smsGateWay.send(phoneNo, context);
		
	}

	/**
	 * 	后台发送短信
	 */
	@Override
	@Transactional(readOnly = false)
	public void sendCollectionSmsForAdmin(String context, String phoneNo) {
		
		MmsSmsRecord sms = new MmsSmsRecord();
		sms.setTmplCode("1");
		sms.setMsgPriority(SendPrior.High.getCode());
		sms.setPhoneNo(phoneNo);
		sms.setContent(context);
		sms.setSendTime(new Date());
		mmsSmsRecordService.save(sms);
		
		smsGateWay.sendCollectionMessage(phoneNo, context);
		
	}

	

}
