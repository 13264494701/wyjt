package com.jxf.svc.sys.image.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.jxf.svc.config.Global;
import com.jxf.svc.config.Setting;
import com.jxf.svc.freemarker.FreeMarkerUtils;
import com.jxf.svc.plugin.PluginService;
import com.jxf.svc.plugin.StoragePlugin;
import com.jxf.svc.sys.image.entity.Image;
import com.jxf.svc.sys.image.service.ImageService;
import com.jxf.svc.utils.ImageUtils;
import com.jxf.svc.utils.SystemUtils;

import freemarker.template.TemplateException;
/**
 * 图片ServiceImpl
 * @author JINXINFU
 * @version 2018-01-14
 */
@Service("imageService")
@Transactional(readOnly = true)
public class ImageServiceImpl  implements ImageService{

	@Autowired
	private PluginService pluginService;
	
	@Resource(name = "taskExecutor")
	private TaskExecutor taskExecutor;
	
	/** 目标扩展名 */
	private static final String DEST_EXTENSION = "jpg";
	/** 目标文件类型 */
	private static final String DEST_CONTENT_TYPE = "image/jpeg";


	public void generate(Image image) {
		if (image == null || image.getFile() == null || image.getFile().isEmpty()) {
			return;
		}
		try {
			Setting setting = SystemUtils.getSetting();
			MultipartFile multipartFile = image.getFile();
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("dir", image.getDir());
			String uploadPath = FreeMarkerUtils.process(setting.getImageUploadPath(), model)+UUID.randomUUID().toString();
			String uuid = UUID.randomUUID().toString();
			String sourceExtension = "-source." + FilenameUtils.getExtension(multipartFile.getOriginalFilename());
			String imagePath = uploadPath + uuid;
			for (StoragePlugin storagePlugin : pluginService.getStoragePlugins(true)) {
				File tempFile = new File(FileUtils.getTempDirectory(), UUID.randomUUID() + ".tmp");
				multipartFile.transferTo(tempFile);
				if(image.getAsync()) {
					asynchronous(storagePlugin, imagePath,sourceExtension, tempFile, multipartFile.getContentType());
				}else {
					synchronous(storagePlugin, imagePath,sourceExtension, tempFile, multipartFile.getContentType());	
				}				
				image.setImagePath(storagePlugin.getUrl(imagePath));
				break;
			}
		} catch (IllegalStateException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (TemplateException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	/**
	 * 同步处理
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
	
	private void synchronous(final StoragePlugin storagePlugin, final String imagePath,final String sourceExtension, final File tempFile, final String contentType) {
		
				Setting setting = SystemUtils.getSetting();
//				String path = Global.getBaseStaticPath()+setting.getWatermarkImage();
//				File watermarkFile = new File(path);				
//				File largeTempFile = new File(FileUtils.getTempDirectory(), UUID.randomUUID() + "." + DEST_EXTENSION);
//				File mediumTempFile = new File(FileUtils.getTempDirectory(), UUID.randomUUID() + "." + DEST_EXTENSION);
				File thumbnailTempFile = new File(FileUtils.getTempDirectory(), UUID.randomUUID() + "." + DEST_EXTENSION);
				try {
//					ImageUtils.zoom(tempFile, largeTempFile, setting.getLargeImageWidth(), setting.getLargeImageHeight());
//					ImageUtils.addWatermark(largeTempFile, largeTempFile, watermarkFile, setting.getWatermarkPosition(), setting.getWatermarkAlpha());
//					ImageUtils.zoom(tempFile, mediumTempFile, setting.getMediumImageWidth(), setting.getMediumImageHeight());
//					ImageUtils.addWatermark(mediumTempFile, mediumTempFile, watermarkFile, setting.getWatermarkPosition(), setting.getWatermarkAlpha());
					ImageUtils.zoom(tempFile, thumbnailTempFile, (float) 0.5);
					//storagePlugin.upload(imagePath+sourceExtension, tempFile, contentType);
					storagePlugin.upload(imagePath+"-source.jpg", tempFile, DEST_CONTENT_TYPE);
//					storagePlugin.upload(imagePath+"-large.jpg", largeTempFile, DEST_CONTENT_TYPE);
//					storagePlugin.upload(imagePath+"-medium.jpg", mediumTempFile, DEST_CONTENT_TYPE);
					storagePlugin.upload(imagePath+"-thumbnail.jpg", thumbnailTempFile, DEST_CONTENT_TYPE);
				} catch (Exception e) {
					throw new RuntimeException(e.getMessage(), e);
				}finally {
					FileUtils.deleteQuietly(tempFile);
//					FileUtils.deleteQuietly(largeTempFile);
//					FileUtils.deleteQuietly(mediumTempFile);
					FileUtils.deleteQuietly(thumbnailTempFile);
				}

	}
	/**
	 * 异步处理
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
	
	private void asynchronous(final StoragePlugin storagePlugin, final String imagePath, final String sourceExtension, final File tempFile, final String contentType) {
		taskExecutor.execute(new Runnable() {
			public void run() {
				Setting setting = SystemUtils.getSetting();
				String path = Global.getBaseStaticPath()+setting.getWatermarkImage();
				File watermarkFile = new File(path);				
				File largeTempFile = new File(FileUtils.getTempDirectory(), UUID.randomUUID() + "." + DEST_EXTENSION);
				File mediumTempFile = new File(FileUtils.getTempDirectory(), UUID.randomUUID() + "." + DEST_EXTENSION);
				File thumbnailTempFile = new File(FileUtils.getTempDirectory(), UUID.randomUUID() + "." + DEST_EXTENSION);
				try {
					ImageUtils.zoom(tempFile, largeTempFile, 2);
					ImageUtils.addWatermark(largeTempFile, largeTempFile, watermarkFile, setting.getWatermarkPosition(), setting.getWatermarkAlpha());
					ImageUtils.zoom(tempFile, mediumTempFile, 1);
					ImageUtils.addWatermark(mediumTempFile, mediumTempFile, watermarkFile, setting.getWatermarkPosition(), setting.getWatermarkAlpha());
					ImageUtils.zoom(tempFile, thumbnailTempFile, (float) 0.5);
					//storagePlugin.upload(imagePath+sourceExtension, tempFile, contentType);
					storagePlugin.upload(imagePath+"-source.jpg", tempFile, DEST_CONTENT_TYPE);
					storagePlugin.upload(imagePath+"-large.jpg", largeTempFile, DEST_CONTENT_TYPE);
					storagePlugin.upload(imagePath+"-medium.jpg", mediumTempFile, DEST_CONTENT_TYPE);
					storagePlugin.upload(imagePath+"-thumbnail.jpg", thumbnailTempFile, DEST_CONTENT_TYPE);
				} catch (Exception e) {
					throw new RuntimeException(e.getMessage(), e);
				}finally {
					FileUtils.deleteQuietly(tempFile);
					FileUtils.deleteQuietly(largeTempFile);
					FileUtils.deleteQuietly(mediumTempFile);
					FileUtils.deleteQuietly(thumbnailTempFile);
				}
			}
		});
	}
	
	public void generate(List<Image> images) {
		if (CollectionUtils.isEmpty(images)) {
			return;
		}
		for (Image image : images) {
			generate(image);
		}
	}
	
	public void filter(List<Image> images) {
		CollectionUtils.filter(images, new Predicate() {
			public boolean evaluate(Object object) {
				Image image = (Image) object;
				return image != null && !image.isEmpty();
			}
		});
	}




}