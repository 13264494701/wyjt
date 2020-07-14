package com.jxf.svc.config;

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.LocaleResolver;

import com.ckfinder.connector.ServletContextFactory;
import com.google.common.collect.Maps;
import com.jxf.svc.init.SpringContextHolder;
import com.jxf.svc.utils.StringUtils;


/**
 * 全局配置类
 * @author jxf
 * @version 2015-07-17
 */
public class Global {

	private static Logger log = LoggerFactory.getLogger(Global.class);
	/**
	 * 当前对象实例
	 */
	private static Global global = new Global();
	
	/**
	 * 保存全局属性值
	 */
	private static Map<String, String> map = Maps.newHashMap();
	
	/**
	 * 属性文件加载对象
	 */
	private static PropertiesLoader loader = new PropertiesLoader("wyjt.properties");

	/**
	 * 显示/隐藏
	 */
	public static final String SHOW = "1";
	public static final String HIDE = "0";

	/**
	 * 是/否
	 */
	public static final String YES = "1";
	public static final String NO = "0";
	
	/**
	 * 对/错
	 */
	public static final String TRUE = "true";
	public static final String FALSE = "false";
	
	/**
	 * 使用CKFinder上传文件路径
	 */
	public static final String CKFINDER_UPLOAD_BASE_URL = "/ckfinder_upload/";
	
	/**
	 * 哈希算法
	 */	
	public static final String HASH_ALGORITHM = "SHA-1";
	/**
	 * 
	 */	
	public static final int HASH_INTERATIONS = 1024;
	/**
	 * 
	 */	
	public static final int SALT_SIZE = 8;
	/**
	 * 
	 */	
	public static final int COOKIE_MAX_AGE = 60*60*24;

	/**
	 * 获取当前对象实例
	 */
	public static Global getInstance() {
		return global;
	}
	
	/**
	 * 获取配置
	 * @see ${fns:getConfig('adminPath')}
	 */
	public static String getConfig(String key) {
		String value = map.get(key);
		if (value == null){
			value = loader.getProperty(key);
			map.put(key, value != null ? value : StringUtils.EMPTY);
		}
		return value;
	}
	
	/**
	 * 获取管理端根路径
	 */
	public static String getAdminPath() {
		return getConfig("adminPath");
	}
	/**
	 * 获取前端根路径
	 */
	public static String getWyjtAppPath() {
		return getConfig("wyjtApp");
	}
	/**
	 * 获取前端根路径
	 */
	public static String getFrontPath() {
		return getConfig("frontPath");
	}
	
	/**
	 * 获取URL后缀
	 */
	public static String getUrlSuffix() {
		return getConfig("urlSuffix");
	}
	/**
	 * 获取token过期时间
	 */
	public static long getTokenTimeout() {
		return Integer.parseInt(getConfig("tokenTimeout"));
	}
	/**
	 * 是否是演示模式，演示模式下不能修改用户、角色、密码、菜单、授权
	 */
	public static Boolean isDemoMode() {
		String dm = getConfig("demoMode");
		return "true".equals(dm) || "1".equals(dm);
	}
	
	/**
	 * 在修改系统用户和角色时是否同步到Activiti
	 */
	public static Boolean isSynActivitiIndetity() {
		String dm = getConfig("activiti.isSynActivitiIndetity");
		return "true".equals(dm) || "1".equals(dm);
	}
    
	/**
	 * 页面获取常量
	 * @see ${fns:getConst('YES')}
	 */
	public static Object getConst(String field) {
		try {
			return Global.class.getField(field).get(null);
		} catch (Exception e) {
			// 异常代表无配置，这里什么也不做
			log.debug("");
		}
		return null;
	}
	
	/**
	 * 获取上传文件的根目录
	 * @return
	 */
//	public static String getUserfilesBaseDir() {
//		String dir = getConfig("userfiles.basedir");
//		if (StringUtils.isBlank(dir)){
//			try {
//				dir = ServletContextFactory.getServletContext().getRealPath("/");
//			} catch (Exception e) {
//				return "";
//			}
//		}
//		if(!dir.endsWith("/")) {
//			dir += "/";
//		}
//		return dir;
//	}
	/**
	 * 获得用户远程地址
	 */
	public static String getRemoteAddr(HttpServletRequest request){
		String remoteAddr = request.getHeader("X-Real-IP");
        if (StringUtils.isNotBlank(remoteAddr)) {
        	remoteAddr = request.getHeader("X-Forwarded-For");
        }else if (StringUtils.isNotBlank(remoteAddr)) {
        	remoteAddr = request.getHeader("Proxy-Client-IP");
        }else if (StringUtils.isNotBlank(remoteAddr)) {
        	remoteAddr = request.getHeader("WL-Proxy-Client-IP");
        }
        return remoteAddr != null ? remoteAddr : request.getRemoteAddr();
	}
	/**
	 * 获得i18n字符串
	 */
	public static String getMessage(String code, Object[] args) {
		LocaleResolver localLocaleResolver = (LocaleResolver) SpringContextHolder.getBean(LocaleResolver.class);
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();  
		Locale localLocale = localLocaleResolver.resolveLocale(request);
		return SpringContextHolder.getApplicationContext().getMessage(code, args, localLocale);
	}
	/**
	 * 获取生成静态文件根目
	 */
	public static String getBaseStaticPath() {
		String dir = getConfig("baseStaticPath");
		if (StringUtils.isBlank(dir)){
			try {
				dir = ServletContextFactory.getServletContext().getRealPath("/");
			} catch (Exception e) {
				return "";
			}
		}
		return dir;
	}
}
