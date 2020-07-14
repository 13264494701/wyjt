package com.jxf.svc.plugin.ossStorage;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.jxf.svc.plugin.PluginConfig;
import com.jxf.svc.plugin.StoragePlugin;


/**
 * Plugin - 阿里云存储
 * 
 * @author JINXINFU
 * @version 2.0
 */
@Component("ossStoragePlugin")
public class OssStoragePlugin extends StoragePlugin {

	@Override
	public String getName() {
		return "阿里云存储";
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
		return "oss_storage/install.jhtml";
	}

	@Override
	public String getUninstallUrl() {
		return "oss_storage/uninstall.jhtml";
	}

	@Override
	public String getSettingUrl() {
		return "oss_storage/setting.jhtml";
	}

	@Override
	public void upload(String path, File file, String contentType) {
		PluginConfig pluginConfig = getPluginConfig();
		if (pluginConfig != null) {
			String endpoint = pluginConfig.getAttribute("endpoint");
			String accessId = pluginConfig.getAttribute("accessId");
			String accessKey = pluginConfig.getAttribute("accessKey");
			String bucketName = pluginConfig.getAttribute("bucketName");
			InputStream inputStream = null;
			try {
				inputStream = new BufferedInputStream(new FileInputStream(file));
				OSSClient ossClient = new OSSClient(endpoint, accessId, accessKey);
				ObjectMetadata objectMetadata = new ObjectMetadata();
				objectMetadata.setContentType(contentType);
				objectMetadata.setContentLength(file.length());
				ossClient.putObject(bucketName, StringUtils.removeStart(path, "/"), inputStream, objectMetadata);
			} catch (FileNotFoundException e) {
				throw new RuntimeException(e.getMessage(), e);
			} finally {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
	}

	@Override
	public String getUrl(String path) {
		PluginConfig pluginConfig = getPluginConfig();
		if (pluginConfig != null) {
			String urlPrefix = pluginConfig.getAttribute("urlPrefix");
			return urlPrefix + path;
		}
		return null;
	}

}