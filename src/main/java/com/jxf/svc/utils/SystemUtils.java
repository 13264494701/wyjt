package com.jxf.svc.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.converters.ArrayConverter;
import org.apache.commons.beanutils.converters.DateConverter;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.core.io.ClassPathResource;


import com.jxf.svc.cache.CacheUtils;
import com.jxf.svc.config.Setting;
import com.jxf.svc.config.TemplateConfig;
import com.jxf.svc.converter.EnumConverter;


/**
 * 
 * @类功能说明： 系统工具类
 * @类修改者： 
 * @修改日期： 
 * @修改说明： 
 * @公司名称：北京金信服科技有限公司
 * @作者：HUOJIABAO 
 * @创建时间：2016年6月16日 上午11:25:34 
 * @版本：V1.0
 */
public final class SystemUtils {

	/** BeanUtilsBean */
	private static final BeanUtilsBean BEAN_UTILS;
	
	/** wyjt.xml文件路径 */
	public static final String wyjt_XML_PATH = "/wyjt.xml";

	static {
		ConvertUtilsBean convertUtilsBean = new ConvertUtilsBean() {
			@Override
			public String convert(Object value) {
				if (value != null) {
					Class<?> type = value.getClass();
					if (type.isEnum() && super.lookup(type) == null) {
						super.register(new EnumConverter(type), type);
					} else if (type.isArray() && type.getComponentType().isEnum()) {
						if (super.lookup(type) == null) {
							ArrayConverter arrayConverter = new ArrayConverter(type, new EnumConverter(type.getComponentType()), 0);
							arrayConverter.setOnlyFirstToString(false);
							super.register(arrayConverter, type);
						}
						Converter converter = super.lookup(type);
						return ((String) converter.convert(String.class, value));
					}
				}
				return super.convert(value);
			}

			@SuppressWarnings("rawtypes")
			@Override
			public Object convert(String value, Class clazz) {
				if (clazz.isEnum() && super.lookup(clazz) == null) {
					super.register(new EnumConverter(clazz), clazz);
				}
				return super.convert(value, clazz);
			}

			@SuppressWarnings("rawtypes")
			@Override
			public Object convert(String[] values, Class clazz) {
				if (clazz.isArray() && clazz.getComponentType().isEnum() && super.lookup(clazz.getComponentType()) == null) {
					super.register(new EnumConverter(clazz.getComponentType()), clazz.getComponentType());
				}
				return super.convert(values, clazz);
			}

			@SuppressWarnings("rawtypes")
			@Override
			public Object convert(Object value, Class targetType) {
				if (super.lookup(targetType) == null) {
					if (targetType.isEnum()) {
						super.register(new EnumConverter(targetType), targetType);
					} else if (targetType.isArray() && targetType.getComponentType().isEnum()) {
						ArrayConverter arrayConverter = new ArrayConverter(targetType, new EnumConverter(targetType.getComponentType()), 0);
						arrayConverter.setOnlyFirstToString(false);
						super.register(arrayConverter, targetType);
					}
				}
				return super.convert(value, targetType);
			}
		};

		DateConverter dateConverter = new DateConverter();
		dateConverter.setPatterns(DateUtils.parsePatterns);
		convertUtilsBean.register(dateConverter, Date.class);
		BEAN_UTILS = new BeanUtilsBean(convertUtilsBean);
	}

	/**
	 * 不可实例化
	 */
	private SystemUtils() {
	}

	/**
	 * 获取系统设置
	 * 
	 * @return 系统设置
	 */
	@SuppressWarnings("unchecked")
	public static Setting getSetting() {
		String cacheKey = "setting";
		Setting setting = (Setting)CacheUtils.get(Setting.CACHE_NAME,cacheKey);
		if (setting == null) {
			setting = new Setting();
			try {
				File xmlFile = new ClassPathResource(wyjt_XML_PATH).getFile();
				Document document = new SAXReader().read(xmlFile);
				List<org.dom4j.Element> elements = document.selectNodes("/wyjt/setting");
				for (org.dom4j.Element element : elements) {
					try {
						String name = element.attributeValue("name");
						String value = element.attributeValue("value");
						BEAN_UTILS.setProperty(setting, name, value);
					} catch (IllegalAccessException e) {
						throw new RuntimeException(e.getMessage(), e);
					} catch (InvocationTargetException e) {
						throw new RuntimeException(e.getMessage(), e);
					}
				}
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage(), e);
			} catch (DocumentException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
			CacheUtils.put(Setting.CACHE_NAME, cacheKey, setting);
		}
		return setting;
	}

	/**
	 * 设置系统设置
	 * 
	 * @param setting
	 *            系统设置
	 */
	@SuppressWarnings("unchecked")
	public static void setSetting(Setting setting) {

		try {
			File jxfshopXmlFile = new ClassPathResource(wyjt_XML_PATH).getFile();
			Document document = new SAXReader().read(jxfshopXmlFile);
			List<org.dom4j.Element> elements = document.selectNodes("/wyjt/setting");
			for (org.dom4j.Element element : elements) {
				try {
					String name = element.attributeValue("name");
					String value = BEAN_UTILS.getProperty(setting, name);
					Attribute attribute = element.attribute("value");
					attribute.setValue(value);
				} catch (IllegalAccessException e) {
					throw new RuntimeException(e.getMessage(), e);
				} catch (InvocationTargetException e) {
					throw new RuntimeException(e.getMessage(), e);
				} catch (NoSuchMethodException e) {
					throw new RuntimeException(e.getMessage(), e);
				}
			}

			XMLWriter xmlWriter = null;
			try {
				OutputFormat outputFormat = OutputFormat.createPrettyPrint();
				outputFormat.setEncoding("UTF-8");
				outputFormat.setIndent(true);
				outputFormat.setIndent("	");
				outputFormat.setNewlines(true);
				xmlWriter = new XMLWriter(new FileOutputStream(jxfshopXmlFile), outputFormat);
				xmlWriter.write(document);
				xmlWriter.flush();
			} catch (FileNotFoundException e) {
				throw new RuntimeException(e.getMessage(), e);
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e.getMessage(), e);
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage(), e);
			} finally {
				try {
					if (xmlWriter != null) {
						xmlWriter.close();
					}
				} catch (IOException e) {
				}
			}
			String cacheKey = "setting";
			CacheUtils.put(Setting.CACHE_NAME, cacheKey, setting);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (DocumentException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * 获取模板配置
	 * 
	 * @param id
	 *            ID
	 * @return 模板配置
	 */
	public static TemplateConfig getTemplateConfig(String id) {

		String cacheKey = "templateConfig_" + id;
		TemplateConfig templateConfig= (TemplateConfig) CacheUtils.get(TemplateConfig.CACHE_NAME,cacheKey);
		
		if (templateConfig == null) {

			try {
				File jxfshopXmlFile = new ClassPathResource(wyjt_XML_PATH).getFile();
				Document document = new SAXReader().read(jxfshopXmlFile);
				org.dom4j.Element element = (org.dom4j.Element) document.selectSingleNode("/wyjt/templateConfig[@id='" + id + "']");
				if (element != null) {
					templateConfig = getTemplateConfig(element);
				}
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage(), e);
			} catch (DocumentException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
			CacheUtils.put(TemplateConfig.CACHE_NAME, cacheKey, templateConfig);
		}
		return templateConfig;
	}

	/**
	 * 获取模板配置
	 * 
	 * @param type
	 *            类型
	 * @return 模板配置
	 */
	@SuppressWarnings("unchecked")
	public static List<TemplateConfig> getTemplateConfigs(TemplateConfig.Type type) {
		
		String cacheKey = "templateConfigs_" + type;
		List<TemplateConfig> templateConfigs = (List<TemplateConfig>) CacheUtils.get(TemplateConfig.CACHE_NAME,cacheKey);
			
		if (templateConfigs == null) {
			templateConfigs = new ArrayList<TemplateConfig>();
			try {
				File jxfshopXmlFile = new ClassPathResource(wyjt_XML_PATH).getFile();
				Document document = new SAXReader().read(jxfshopXmlFile);
				List<org.dom4j.Element> elements = document.selectNodes(type != null ? "/wyjt/templateConfig[@type='" + type + "']" : "/jxfshop/templateConfig");
				for (org.dom4j.Element element : elements) {
					templateConfigs.add(getTemplateConfig(element));
				}
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage(), e);
			} catch (DocumentException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
			CacheUtils.put(TemplateConfig.CACHE_NAME, cacheKey, templateConfigs);
		}
		return templateConfigs;
	}

	/**
	 * 获取所有模板配置
	 * 
	 * @return 所有模板配置
	 */
	public static List<TemplateConfig> getTemplateConfigs() {
		return getTemplateConfigs(null);
	}

	/**
	 * 获取模板配置
	 * 
	 * @param element
	 *            元素
	 * @return 模板配置
	 */
	private static TemplateConfig getTemplateConfig(org.dom4j.Element element) {

		String id = element.attributeValue("id");
		String type = element.attributeValue("type");
		String name = element.attributeValue("name");
		String templatePath = element.attributeValue("templatePath");
		String staticPath = element.attributeValue("staticPath");
		String description = element.attributeValue("description");

		TemplateConfig templateConfig = new TemplateConfig();
		templateConfig.setId(id);
		templateConfig.setType(TemplateConfig.Type.valueOf(type));
		templateConfig.setName(name);
		templateConfig.setTemplatePath(templatePath);
		templateConfig.setStaticPath(staticPath);
		templateConfig.setDescription(description);
		return templateConfig;
	}

}