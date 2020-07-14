package com.jxf.svc.init;

import java.sql.DriverManager;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import net.sf.ehcache.CacheManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;

import com.jxf.svc.config.Global;
import com.jxf.svc.utils.Exceptions;
import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;

public class WebContextListener extends org.springframework.web.context.ContextLoaderListener {
	
	private static final Logger log = LoggerFactory.getLogger(WebContextListener.class);
	
	@Override
	public WebApplicationContext initWebApplicationContext(ServletContext servletContext) {
		StringBuilder sb = new StringBuilder();
		sb.append("\r\n======================================================================\r\n");
		sb.append("\r\n    欢迎使用 "+Global.getConfig("productName")+"  - Powered By http://www.wyjt.com\r\n");
		sb.append("\r\n======================================================================\r\n");
		log.info(sb.toString());
		return super.initWebApplicationContext(servletContext);
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		CacheManager cacheManager = ((CacheManager)SpringContextHolder.getBean("cacheManager"));
		cacheManager.shutdown();
		//Tomcat在停止web应用的时候会调用contextDestroyed方法，加入你的项目,即可在tomcat关闭时注销已经注册的JDBC驱动。
		try{
		      while(DriverManager.getDrivers().hasMoreElements()){
		                DriverManager.deregisterDriver(DriverManager.getDrivers().nextElement());
		      }
		     }catch(Exception e){
		      log.error(Exceptions.getStackTraceAsString(e));
		}
		 //com.mysql.jdbc.AbandonedConnectionCleanupThread.uncheckedShutdown();
		try {
            AbandonedConnectionCleanupThread.shutdown();
        } catch (InterruptedException e) {
            log.error("ContextFinalizer:SEVERE problem cleaning up:{}",Exceptions.getStackTraceAsString(e));
        }
		super.contextDestroyed(event);
	}
}
