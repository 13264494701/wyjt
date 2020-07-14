package com.jxf.svc.sys.image.entity;

import javax.validation.constraints.Min;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Entity - 图片
 * 
 * @author JINXINFU
 * @version 2.0
 */
public class Image  {
	
	
	/** 标题 */
	private String imageTitle;

	/** 图片路径 */
	private String imagePath;
	/** 图片扩展名 */
	private String extension;
	
	private int x;
	private int y;
	private int width;
	private int height;
	
	/** 目录 */
	private String dir;

	/** 排序 */
	private Integer sort;
	
	/** 是否异步 */
	private Boolean async;
	
	/** 文件 */
	@JSONField(serialize = false)
	private MultipartFile file;
	
	public Image() {
		super();
	}
	public Image(String imagePath) {
		super();
		this.imagePath = imagePath;
	}

	public String getImageTitle() {
		return imageTitle;
	}

	public void setImageTitle(String imageTitle) {
		this.imageTitle = imageTitle;
	}

	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	
	@JSONField(serialize = false)
	public int getX() {
		return x;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	@JSONField(serialize = false)
	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	@JSONField(serialize = false)
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	@JSONField(serialize = false)
	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	public String getDir() {
		return dir;
	}
	public void setDir(String dir) {
		this.dir = dir;
	}
	public Boolean getAsync() {
		return async;
	}
	public void setAsync(Boolean async) {
		this.async = async;
	}
	/**
	 * 获取排序
	 * 
	 * @return 排序
	 */
	@Min(0)
	public Integer getSort() {
		return sort;
	}
	/**
	 * 设置排序
	 * 
	 * @param order
	 *            排序
	 */
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	/**
	 * 获取文件
	 * 
	 * @return 文件
	 */
	@JSONField(serialize = false)
	public MultipartFile getFile() {
		return file;
	}

	/**
	 * 设置文件
	 * 
	 * @param file
	 *            文件
	 */
	public void setFile(MultipartFile file) {
		this.file = file;
	}

	/**
	 * 判断是否为空
	 * 
	 * @return 是否为空
	 */
	@JSONField(serialize = false)
	public boolean isEmpty() {
		return (getFile() == null || getFile().isEmpty()) && (StringUtils.isEmpty(getImagePath()));
	}

	/**
	 * 实现compareTo方法
	 * 
	 * @param image
	 *            图片
	 * @return 比较结果
	 */
	public int compareTo(Image image) {
		if (image == null) {
			return 1;
		}
		return new CompareToBuilder().append(getSort(), image.getSort()).toComparison();
	}
	public String getExtension() {
		return extension;
	}
	public void setExtension(String extension) {
		this.extension = extension;
	}

}
