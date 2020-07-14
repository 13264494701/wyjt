package com.jxf.transplantation.dbo;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class OldDBOperatorFactory {
	 
	private static Queue<OldDBOperator> pool;
	
	private static  int size =0;

	private static Logger log = LoggerFactory.getLogger(OldDBOperatorFactory.class);

	private OldDBOperatorFactory() {
		pool = new ConcurrentLinkedQueue<OldDBOperator>();
		size = 5;
		for(int i=0;i<size;i++)
		{
			pool.add(new OldDBOperator());
		}
		log.info("create TaskQueue-- >> " + pool);
	}
	public synchronized static void addDBOperator(OldDBOperator operator)
	{
		if(pool==null)
		{
			log.info("数据库队列已经关闭");
			return;
		}
		pool.add(operator);
	}
	public synchronized static OldDBOperator getDBOperator(){
        if( pool == null ) { 
            new OldDBOperatorFactory(); 
        } 
        OldDBOperator operator = pool.poll();
		if(operator==null)
		{
			operator = new OldDBOperator();
			log.info("队列中没有可用的数据库连接，新申请一个连接 " + operator.toString());
		}
        return operator;

	}
	
	public static void destroyDBOperatorFactory()
	{
		if(pool==null)
		{
			return;
		}
		while(pool.size()!=0)
		{
			OldDBOperator operator =pool.poll();
			operator.close();		
			log.info("关闭第"+(pool.size()+1)+"个数据库连接 ");
		}
		pool =null;
	}
}
