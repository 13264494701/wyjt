package com.jxf.mms.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxf.svc.init.SpringContextHolder;
import com.jxf.mms.consumer.impl.AppMsgConsumer;
import com.jxf.mms.consumer.impl.SMSConsumer;
import com.jxf.mms.queue.AppMsgQueue;
import com.jxf.mms.queue.SMSQueue;
import com.jxf.mms.record.entity.MmsAppMsgRecord;

import com.jxf.mms.record.entity.MmsSmsRecord;
/**
 * 
 * @类功能说明： 
 * @类修改者： 
 * @修改日期： 
 * @修改说明： 
 * @公司名称：北京金信服科技有限公司
 * @作者：HUOJIABAO 
 * @创建时间：2016年4月9日 下午11:56:29 
 * @版本：V1.0
 */
public class TaskProducer {
	
	private static Logger logger = LoggerFactory.getLogger(TaskProducer.class);
	private static SMSQueue<MmsSmsRecord> smsQueue;
	private static AppMsgQueue<MmsAppMsgRecord> appMsgQueue;

	static {
		logger.info("开始初始化存储消息队列Queue");
		smsQueue = SpringContextHolder.getBean("smsQueue");
		appMsgQueue = SpringContextHolder.getBean("appMsgQueue");
		logger.info("初始化存储消息队列Queue成功");

		logger.info("开始启动Consumer线程池");
		startConsumer();
	}

	/**
	 * 启动所有的Consumer线程池
	 */
	private static void startConsumer() {



		// 短信处理线程池
		logger.info("开始启动短信Consumer线程池");
		SMSConsumer<MmsSmsRecord> smsConsumer = SpringContextHolder.getBean("smsConsumer");
		smsConsumer.startConsume();
		
		
		// APP消息处理线程池
		logger.info("开始启动App消息Consumer线程池");
		AppMsgConsumer<MmsAppMsgRecord> appMsgConsumer = SpringContextHolder.getBean("appMsgConsumer");
		appMsgConsumer.startConsume();
		

		try {
			Thread.sleep(1000);
		} catch (InterruptedException ie) {
			logger.error("等待线程池启动时发生错误", ie);
		}

	}

	/**
	 * 把SMS放入待发送队列中
	 * 
	 * @param sms
	 */
	public static void addSMS(MmsSmsRecord sms) {
		logger.debug("开始加入短信[{}]到队列。",sms.getId());
		smsQueue.put(sms);
	}
	
	
	/**
	 * 把appMsg放入待发送队列中
	 * 
	 * @param appMsg
	 */
	public static void addAppMsg(MmsAppMsgRecord appMsg) {
		logger.debug("开始加入APP消息[{}]到队列。",appMsg.getId());
		appMsgQueue.put(appMsg);
	}

}
