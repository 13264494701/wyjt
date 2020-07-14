package com.jxf.mms.consumer.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxf.mms.gateway.HttpSMSGateWay;
import com.jxf.mms.queue.AbstractTaskQueue;
import com.jxf.mms.record.entity.MmsSmsRecord;

/**
 * 
 * @类功能说明： 短信的consumer线程，从队列中取待发短信，然后调用HttpSMSGateWay发送
 * @类修改者： 
 * @修改日期： 
 * @修改说明： 
 * @公司名称：北京金信服科技有限公司
 * @作者：HUOJIABAO 
 * @创建时间：2016年4月12日 上午12:22:35 
 * @版本：V1.0
 */
public class SMSConsumerThread<T> extends Thread {
	private static Logger logger = LoggerFactory.getLogger(SMSConsumerThread.class);
	private int thread_id;
	private HttpSMSGateWay smsGateWay;
	private AbstractTaskQueue<T> queue;
	/**
	 * 构造函数
	 * 
	 * @param thread_id
	 */
	public SMSConsumerThread(int thread_id,AbstractTaskQueue<T> queue,HttpSMSGateWay smsGateWay) {
		this.thread_id = thread_id;
		this.queue = queue;
		this.smsGateWay = smsGateWay;
		logger.info("短信处理线程[{}]创建成功。",thread_id);
	}

	@Override
	public void run() {
		
		logger.debug("短信线程[{}]轮询一次",thread_id);
		MmsSmsRecord sms = (MmsSmsRecord)queue.take();
		if (sms == null) {
			return;
		}
		logger.debug("短信线程[{}]从队列取出短信[{}]",thread_id,sms.getId());
		int result = smsGateWay.send(sms.getPhoneNo(),sms.getContent());
		if (result == 0) {
			logger.debug("发送短信成功,短信编号[{}]手机号码[{}]",sms.getId(),sms.getPhoneNo());
		} else {
			logger.error("发送短信时发生异常, 异常短信编号[{}]手机号码[{}]",sms.getId(),sms.getPhoneNo());
		}

	}
}
