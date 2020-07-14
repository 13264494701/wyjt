package com.jxf.cms.service;

import java.util.Map;

import com.jxf.cms.entity.CmsContTmpl;

/**
 * Service - 静态化
 * 
 * @author JINXINFU
 * @version 2.0
 */
public interface GenerateStaticService {

	/**
	 * 生成静态
	 * 
	 * @param templatePath
	 *            模板文件路径
	 * @param staticPath
	 *            静态文件路径
	 * @param model
	 *            数据
	 * @return 生成数量
	 */
	int generate(String templatePath, String staticPath, Map<String, Object> model);

	/**
	 * 生成静态
	 * 
	 * @param contTmpl
	 *            协议
	 * @return 
	 */
	int generate(CmsContTmpl contTmpl);


	/**
	 * 生成首页静态
	 * 
	 * @return 生成数量
	 */
	int generateIndex();



	/**
	 * 生成其它静态
	 * 
	 * @return 生成数量
	 */
	int generateOther();

	/**
	 * 删除静态
	 * 
	 * @param staticPath
	 *            静态文件路径
	 * @return 删除数量
	 */
	int delete(String staticPath);

	/**
	 * 删除静态
	 * 
	 * @param contTmpl
	 *           
	 * @return 删除数量
	 */
	int delete(CmsContTmpl contTmpl);

	/**
	 * 删除首页静态
	 * 
	 * @return 删除数量
	 */
	int deleteIndex();

	/**
	 * 删除其它静态
	 * 
	 * @return 删除数量
	 */
	int deleteOther();

}