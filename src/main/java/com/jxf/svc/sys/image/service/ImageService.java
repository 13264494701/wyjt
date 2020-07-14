package com.jxf.svc.sys.image.service;

import java.util.List;

import com.jxf.svc.sys.image.entity.Image;


/**
 * 图片Service
 * @author JINXINFU
 * @version 2018-01-14
 */
public interface ImageService {
	
	public void generate(Image image);
	
	public void filter(List<Image> images);
	
	public void generate(List<Image> images);
	
	
}