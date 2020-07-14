package com.jxf.svc.plugin.localStorage;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletContext;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import com.jxf.svc.config.Global;
import com.jxf.svc.plugin.StoragePlugin;
import com.jxf.svc.servlet.Servlets;

/**
 * Plugin - 本地文件存储
 * 
 * @author JINXINFU
 * @version 2.0
 */
@Component("localStoragePlugin")
public class LocalStoragePlugin extends StoragePlugin implements ServletContextAware {

	/** ServletContext */
	private ServletContext servletContext;

	private static final Logger logger = LoggerFactory.getLogger(LocalStoragePlugin.class);
	
	/**
	 * 设置ServletContext
	 * 
	 * @param servletContext
	 *            ServletContext
	 */
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	@Override
	public String getName() {
		return "本地文件存储";
	}

	@Override
	public String getVersion() {
		return "1.0";
	}

	@Override
	public String getAuthor() {
		return "JINXINFU";
	}

	@Override
	public String getSiteUrl() {
		return "http://www.jinxinfu.net";
	}

	@Override
	public String getInstallUrl() {
		return null;
	}

	@Override
	public String getUninstallUrl() {
		return null;
	}

	@Override
	public String getSettingUrl() {
		return "local_storage/setting.jhtml";
	}

	@Override
	public void upload(String path, File file, String contentType) {
		File destFile = new File(Global.getBaseStaticPath()+path);
		try {		
			
			FileUtils.moveFile(file, destFile);		
			
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	@Override
	public String getUrl(String path) {
        //Servlets.getRequest().getContextPath() 需要在网络请求的线程里面才能取到
		String url = Servlets.getRequest().getContextPath()  + path; 
		return url;
	}

}