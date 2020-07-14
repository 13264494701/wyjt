package com.jxf.svc.config;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import com.jxf.svc.freemarker.FreeMarkerUtils;

import freemarker.template.TemplateException;

/**
 * 模板配置
 * 
 * @author JINXINFU
 * @version 2.0
 */
public class TemplateConfig implements Serializable {

	private static final long serialVersionUID = -517540800437045215L;

	/** 缓存名称 */
	public static final String CACHE_NAME = "templateConfig";

	/**
	 * 类型
	 */
	public enum Type {

		/** 页面模板 */
		page,

		/** 打印模板 */
		print,

		/** 邮件模板 */
		mail,

		/** 短信模板 */
		sms
	}

	/** ID */
	private String id;

	/** 类型 */
	private TemplateConfig.Type type;

	/** 名称 */
	private String name;

	/** 模板文件路径 */
	private String templatePath;

	/** 静态文件路径 */
	private String staticPath;

	/** 描述 */
	private String description;

	/**
	 * 获取ID
	 * 
	 * @return ID
	 */
	public String getId() {
		return id;
	}

	/**
	 * 设置ID
	 * 
	 * @param id
	 *            ID
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 获取类型
	 * 
	 * @return 类型
	 */
	public TemplateConfig.Type getType() {
		return type;
	}

	/**
	 * 设置类型
	 * 
	 * @param type
	 *            类型
	 */
	public void setType(TemplateConfig.Type type) {
		this.type = type;
	}

	/**
	 * 获取名称
	 * 
	 * @return 名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置名称
	 * 
	 * @param name
	 *            名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取模板文件路径
	 * 
	 * @return 模板文件路径
	 */
	public String getTemplatePath() {
		return templatePath;
	}

	/**
	 * 设置模板文件路径
	 * 
	 * @param templatePath
	 *            模板文件路径
	 */
	public void setTemplatePath(String templatePath) {
		this.templatePath = templatePath;
	}

	/**
	 * 获取静态文件路径
	 * 
	 * @return 静态文件路径
	 */
	public String getStaticPath() {
		return staticPath;
	}

	/**
	 * 设置静态文件路径
	 * 
	 * @param staticPath
	 *            静态文件路径
	 */
	public void setStaticPath(String staticPath) {
		this.staticPath = staticPath;
	}

	/**
	 * 获取描述
	 * 
	 * @return 描述
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 设置描述
	 * 
	 * @param description
	 *            描述
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 获取实际模板文件路径
	 * 
	 * @return 实际模板文件路径
	 */
	public String getRealTemplatePath() {
		return getRealTemplatePath(null);
	}

	/**
	 * 获取实际模板文件路径
	 * 
	 * @param model
	 *            数据
	 * @return 实际模板文件路径
	 */
	public String getRealTemplatePath(Map<String, Object> model) {
		try {
			return FreeMarkerUtils.process(getTemplatePath(), model);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (TemplateException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * 获取实际静态文件路径
	 * 
	 * @return 实际静态文件路径
	 */
	public String getRealStaticPath() {
		return getRealStaticPath(null);
	}

	/**
	 * 获取实际静态文件路径
	 * 
	 * @param model
	 *            数据
	 * @return 实际静态文件路径
	 */
	public String getRealStaticPath(Map<String, Object> model) {
		try {
			return FreeMarkerUtils.process(getStaticPath(), model);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (TemplateException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

}