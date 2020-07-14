package com.jxf.mms.consumer.impl;

import org.ebaoquan.rop.thirdparty.com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxf.mms.msg.utils.XiaomiPushUtils;
import com.jxf.mms.queue.AbstractTaskQueue;
import com.jxf.mms.record.entity.MmsAppMsgRecord;


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
public class AppMsgConsumerThread<T> extends Thread {
	private static Logger logger = LoggerFactory.getLogger(AppMsgConsumerThread.class);
	private int thread_id;
	private AbstractTaskQueue<T> queue;
	/**
	 * 构造函数
	 * 
	 * @param thread_id
	 */
	public AppMsgConsumerThread(int thread_id,AbstractTaskQueue<T> queue) {
		this.thread_id = thread_id;
		this.queue = queue;
		logger.info("APP消息处理线程[{}]创建成功。",thread_id);
	}

	@Override
	public void run() {
		
		logger.debug("APP消息线程[{}]轮询一次",thread_id);
		MmsAppMsgRecord appMsg = (MmsAppMsgRecord)queue.take();
		if (appMsg == null) {
			return;
		}
		logger.debug("APP消息线程[{}]从队列取出[{}]",thread_id,JSON.toJSONString(appMsg));
		
		
		int result = XiaomiPushUtils.sendMessage(appMsg);
		if(result==0) {
			logger.debug("APP消息推送成功{}",result);
		}
		

	}
}
