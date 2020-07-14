package com.jxf.svc.utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import com.jxf.svc.config.Setting;
import com.jxf.svc.freemarker.FreeMarkerUtils;
import com.jxf.svc.model.HandleRsp;
import com.jxf.svc.plugin.PluginService;
import com.jxf.svc.plugin.StoragePlugin;


import freemarker.template.TemplateException;

public class FileUploadUtils {
	
	private static PluginService pluginService = SpringUtils.getBean("pluginServiceImpl", PluginService.class);
	
	
	/** 目标文件类型 */
	private static final String DEST_CONTENT_TYPE = "image/jpeg";
	
	public static String upload(File tempFile,String dir,String type,String extension) {

		String uploadPath = getUploadPath(dir,type,extension);
		if(!StringUtils.isBlank(uploadPath)){
			return upload(tempFile,uploadPath);   					
		}	
		return "";
	}
	private static String upload(File tempFile,String uploadPath){	
		for (StoragePlugin storagePlugin : pluginService.getStoragePlugins(true)) {			
			
			storagePlugin.upload(uploadPath, tempFile, DEST_CONTENT_TYPE);					
			FileUtils.deleteQuietly(tempFile);
			return uploadPath;
		}
		return "";
	}

	private static String getUploadPath(String dir,String type,String extension) {
		HandleRsp rsp = new HandleRsp();
		rsp.setStatus(true);			 
		try {			
			Setting setting = SystemUtils.getSetting();
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("dir", dir);		
			switch(type)
			{
			    case "image":
				return FreeMarkerUtils.process(setting.getImageUploadPath(), model) +IdGen.uuid()+"."+ extension;	
			    case "video":
				return FreeMarkerUtils.process(setting.getVideoUploadPath(), model) +IdGen.uuid()+"."+ extension;
			    case "file":
				return FreeMarkerUtils.process(setting.getFileUploadPath(), model) +IdGen.uuid()+"."+ extension;				
			    default:break;
			}
		
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {		
			e.printStackTrace();
		}
		return "";
	}
}
