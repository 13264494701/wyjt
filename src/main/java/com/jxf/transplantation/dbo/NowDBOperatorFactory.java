package com.jxf.transplantation.dbo;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class NowDBOperatorFactory {
	 
	private static Queue<NowDBOperator> pool;
	
	private static  int size =0;

	private static Logger log = LoggerFactory.getLogger(NowDBOperatorFactory.class);

	private NowDBOperatorFactory() {
		pool = new ConcurrentLinkedQueue<NowDBOperator>();
		size = 5;
		for(int i=0;i<size;i++)
		{
			pool.add(new NowDBOperator());
		}
		log.info("create TaskQueue-- >> " + pool);
	}
	public synchronized static void addNowDBOperator(NowDBOperator operator)
	{
		if(pool==null)
		{
			log.info("数据库队列已经关闭");
			return;
		}
		pool.add(operator);
	}
	public synchronized static NowDBOperator getNowDBOperator(){
        if( pool == null ) { 
            new NowDBOperatorFactory(); 
        } 
        NowDBOperator operator = pool.poll();
		if(operator==null)
		{
			operator = new NowDBOperator();
			log.info("队列中没有可用的数据库连接，新申请一个连接 " + operator.toString());
		}
        return operator;

	}
	
	public static void destroyNowDBOperatorFactory()
	{
		if(pool==null)
		{
			return;
		}
		while(pool.size()!=0)
		{
			NowDBOperator operator =pool.poll();
			operator.close();		
			log.info("关闭第"+(pool.size()+1)+"个数据库连接 ");
		}
		pool =null;
	}
}
