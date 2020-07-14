package com.jxf.mms.consumer.impl;
import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxf.mms.consumer.AbstractTaskConsumer;
import com.jxf.mms.queue.AbstractTaskQueue;

/**
 * 
 * @类功能说明： 线程池，其中的线程从短信队列中取待发送的SMS对象
 * @类修改者： 
 * @修改日期： 
 * @修改说明： 
 * @公司名称：北京金信服科技有限公司
 * @作者：HUOJIABAO 
 * @创建时间：2016年4月12日 上午12:23:10 
 * @版本：V1.0
 */
public class AppMsgConsumer<T> extends AbstractTaskConsumer implements Closeable {

	// 
	private ScheduledExecutorService executorService;
	
	private AbstractTaskQueue<T> queue;

	private static Logger logger = LoggerFactory.getLogger(AppMsgConsumer.class);


	/**
	 * 启动短信Consumer的线程池
	 * 
	 * @param message
	 */
	public void startConsume() {
		logger.info("开始启动APP消息发送线程池");
		if (executorService == null || executorService.isShutdown()|| executorService.isTerminated()) {
			executorService = Executors.newScheduledThreadPool(consumer_thread_number);
			for (int i = 1; i < (consumer_thread_number + 1); i++) {
				// 此处initialDelay是i，单位秒
				executorService.scheduleWithFixedDelay(new AppMsgConsumerThread<T>(i,queue), 0, interval, TimeUnit.SECONDS);
			}
		}
		logger.info("APP消息发送线程池初始化成功, 共含{}个线程, 每{}秒钟轮询队列一次",consumer_thread_number,interval);
	}

	/**
	 * @return the executorService
	 */
	public ScheduledExecutorService getExecutorService() {
		return executorService;
	}
	
	/**
	 * Spring注入queue
	 * 
	 * @param queue
	 *            the queue to set
	 */
	public void setQueue(AbstractTaskQueue<T> queue) {
		this.queue = queue;
	}
	
	@Override
	public void close() throws IOException {
		logger.info("AppMsgConsumer 准备关闭");
		if (executorService != null) {
			executorService.shutdownNow();
		}

		logger.info("AppMsgConsumer 关闭完成");
	}

}
