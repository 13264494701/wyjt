package com.jxf.mms.sender.utils;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import com.jxf.mms.sender.dao.MmsSenderDao;
import com.jxf.mms.sender.entity.MmsSender;
import com.jxf.svc.cache.CacheUtils;
import com.jxf.svc.init.SpringContextHolder;

public class MmsSenderUtils {
	
	private static MmsSenderDao senderDao = SpringContextHolder.getBean(MmsSenderDao.class);
	public static final String CACHE_SENDER = "sender";
	/**
	 * 根据发件人名称取发件人信息
	 * @param 
	 * @return 取不到返回null
	 */
	public static MmsSender getBySenderName(String senderName){
		MmsSender sender = (MmsSender)CacheUtils.get(CACHE_SENDER,senderName);
		if (sender == null){
			sender = senderDao.getBySenderName(senderName);
			if (sender == null){
				return null;
			}
			CacheUtils.put(CACHE_SENDER, senderName, sender);
		}
		return sender;
	}
	
	public static Session getSession(){
		try{
			Subject subject = SecurityUtils.getSubject();
			Session session = subject.getSession(false);
			if (session == null){
				session = subject.getSession();
			}
			if (session != null){
				return session;
			}
//			subject.logout();
		}catch (InvalidSessionException e){
			
		}
		return null;
	}
	public static void removeCache(String key) {
		getSession().removeAttribute(key);
	}
}
