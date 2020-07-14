package com.jxf.mem.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.jxf.mem.service.MemberHeadImageService;
import com.jxf.svc.config.Setting;
import com.jxf.svc.freemarker.FreeMarkerUtils;
import com.jxf.svc.plugin.PluginService;
import com.jxf.svc.plugin.StoragePlugin;
import com.jxf.svc.sys.image.entity.Image;
import com.jxf.svc.utils.ImageUtils;
import com.jxf.svc.utils.SystemUtils;

import freemarker.template.TemplateException;
/**
 * 商品图片ServiceImpl
 * @author JINXINFU
 * @version 2016-09-28
 */
@Service("memberHeadImageService")
@Transactional(readOnly = true)
public class MemberHeadImageServiceImpl  implements MemberHeadImageService{

	@Autowired
	private PluginService pluginService;
	@Resource(name = "taskExecutor")
	private TaskExecutor taskExecutor;
	
	/** 目标扩展名 */
	private static final String DEST_EXTENSION = "jpg";
	/** 目标文件类型 */
	private static final String DEST_CONTENT_TYPE = "image/jpeg";


	@Override
	public void generate(Image headImage) {
		if (headImage == null || headImage.getFile() == null || headImage.getFile().isEmpty()) {
			return;
		}
		try {
			Setting setting = SystemUtils.getSetting();
			MultipartFile multipartFile = headImage.getFile();
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("dir", "headimage");
			String uploadPath = FreeMarkerUtils.process(setting.getImageUploadPath(), model)+UUID.randomUUID().toString();
			String sourceExtension = "-source." + FilenameUtils.getExtension(multipartFile.getOriginalFilename());
			for (StoragePlugin storagePlugin : pluginService.getStoragePlugins(true)) {
				File tempFile1 = new File(FileUtils.getTempDirectory(), UUID.randomUUID() + ".tmp");
				File tempFile2 = new File(FileUtils.getTempDirectory(), UUID.randomUUID() + ".tmp");
				multipartFile.transferTo(tempFile1);	
				ImageUtils.cutImage(tempFile1.getAbsolutePath(), tempFile2.getAbsolutePath(), headImage.getX(), headImage.getY(), headImage.getWidth(), headImage.getHeight());
				FileUtils.deleteQuietly(tempFile1);
				addTask(storagePlugin, uploadPath, sourceExtension, tempFile2, multipartFile.getContentType());
				headImage.setImagePath(storagePlugin.getUrl(uploadPath));
				break;
			}
		} catch (IllegalStateException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (TemplateException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			
		}
	}
	/**
	 * 添加图片处理任务
	 * 
	 * @param storagePlugin
	 *            存储插件
	 * @param sourcePath
	 *            原图片上传路径
	 * @param largePath
	 *            图片文件(大)上传路径
	 * @param mediumPath
	 *            图片文件(小)上传路径
	 * @param thumbnailPath
	 *            图片文件(缩略)上传路径
	 * @param tempFile
	 *            原临时文件
	 * @param contentType
	 *            原文件类型
	 */
	
	private void addTask(final StoragePlugin storagePlugin, final String imagePath, final String sourceExtension, final File tempFile, final String contentType) {
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				Setting setting = SystemUtils.getSetting();
				String path = System.getProperty("webapp.root")+setting.getWatermarkImage();
				File watermarkFile = new File(path);
				
				File largeTempFile = new File(FileUtils.getTempDirectory(), UUID.randomUUID() + "." + DEST_EXTENSION);
				File mediumTempFile = new File(FileUtils.getTempDirectory(), UUID.randomUUID() + "." + DEST_EXTENSION);
				File thumbnailTempFile = new File(FileUtils.getTempDirectory(), UUID.randomUUID() + "." + DEST_EXTENSION);
				try {					
					ImageUtils.zoom(tempFile, largeTempFile, 180, 180);
					ImageUtils.addWatermark(largeTempFile, largeTempFile, watermarkFile, setting.getWatermarkPosition(), setting.getWatermarkAlpha());
					ImageUtils.zoom(tempFile, mediumTempFile, 50, 50);
					ImageUtils.addWatermark(mediumTempFile, mediumTempFile, watermarkFile, setting.getWatermarkPosition(), setting.getWatermarkAlpha());
					ImageUtils.zoom(tempFile, thumbnailTempFile, 30, 30);
					storagePlugin.upload(imagePath+sourceExtension, tempFile, contentType);
					storagePlugin.upload(imagePath+"-large.jpg", largeTempFile, DEST_CONTENT_TYPE);
					storagePlugin.upload(imagePath+"-medium.jpg", mediumTempFile, DEST_CONTENT_TYPE);
					storagePlugin.upload(imagePath+"-thumbnail.jpg", thumbnailTempFile, DEST_CONTENT_TYPE);
				} finally {
					FileUtils.deleteQuietly(tempFile);
					FileUtils.deleteQuietly(largeTempFile);
					FileUtils.deleteQuietly(mediumTempFile);
					FileUtils.deleteQuietly(thumbnailTempFile);
				}
			}
		});
	}
	
}