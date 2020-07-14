package com.jxf.mms.queue;

import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxf.svc.sys.base.entity.BaseEntity;

/**
 * 
 * @类功能说明： 所有任务队列的父类
 * @类修改者： 
 * @修改日期： 
 * @修改说明： 
 * @公司名称：北京金信服科技有限公司
 * @作者：JINXINFU 
 * @创建时间：2016年4月11日 下午11:40:51 
 * @版本：V1.0
 */
public abstract class AbstractTaskQueue<T> extends LinkedBlockingQueue<BaseEntity<T>>
{

    private static final long serialVersionUID = -3645243155204152472L;
    
    private static Logger logger = LoggerFactory.getLogger(AbstractTaskQueue.class);
    
    @Override
    public void put(BaseEntity<T> e) 
    {
        try
        {
            super.put(e);
        }
        catch(InterruptedException ie)
        {
            logger.error("往队列中添加一个任务对象出错", ie);
        }
    }

    /**
     * 取队列头部的一个Task对象
     */
    @Override
    public BaseEntity<T> take()
    {
        try
        {
            return super.take();
        }
        catch(InterruptedException ie)
        {
            logger.error("从队列中取出一个任务对象出错", ie);
            return null;
        }
    }

    
}
