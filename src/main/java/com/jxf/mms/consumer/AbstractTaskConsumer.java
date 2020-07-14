package com.jxf.mms.consumer;


/**
 * 
 * @类功能说明： 
 * @类修改者： 
 * @修改日期： 
 * @修改说明： 
 * @公司名称：北京金信服科技有限公司
 * @作者：HUOJIABAO 
 * @创建时间：2016年4月12日 上午12:24:38 
 * @版本：V1.0
 */
public abstract class AbstractTaskConsumer
{

    protected int consumer_thread_number;

    // 每个线程轮询队列的时间间隔
    protected int interval;

    /**
     * 启动
     * 
     * @param message
     */
    public abstract void startConsume();



    /**
     * Spring注入线程数
     * 
     * @param consumer_thread_number
     *            the consumer_thread_number to set
     */
    public void setConsumer_thread_number(int consumer_thread_number)
    {
        this.consumer_thread_number = consumer_thread_number;
    }

    /**
     * Spring注入轮询queue间隔
     * 
     * @param interval
     *            the interval to set
     */
    public void setInterval(int interval)
    {
        this.interval = interval;
    }
}
