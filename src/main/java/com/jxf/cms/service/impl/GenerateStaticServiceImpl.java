package com.jxf.cms.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.jxf.cms.entity.CmsContTmpl;
import com.jxf.cms.service.GenerateStaticService;
import com.jxf.svc.config.Global;
import com.jxf.svc.config.TemplateConfig;
import com.jxf.svc.utils.StringUtils;
import com.jxf.svc.utils.SystemUtils;

import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * Service - 静态化
 * 
 * @author JINXINFU
 * @version 2.0
 */
@Service("staticServiceImpl")
public class GenerateStaticServiceImpl implements GenerateStaticService, ServletContextAware {


	/** ServletContext */
	private ServletContext servletContext;

	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;


	/**
	 * 设置ServletContext
	 * 
	 * @param servletContext
	 *            ServletContext
	 */
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	@Transactional(readOnly = true)
	public int generate(String templatePath, String staticPath, Map<String, Object> model) {

		Writer writer = null;
		try {
			Template template = freeMarkerConfigurer.getConfiguration().getTemplate(templatePath);
			File staticFile = new File(Global.getBaseStaticPath()+staticPath);
			File staticDir = staticFile.getParentFile();
			if (staticDir != null) {
				staticDir.mkdirs();
			}
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(staticFile), "UTF-8"));
			template.process(model, writer);
			writer.flush();
			return 1;
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (TemplateException e) {
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Transactional(readOnly = true)
	public int generate(CmsContTmpl contTmpl) {
		if (contTmpl == null) {
			return 0;
		}
		delete(contTmpl);

//		TemplateConfig pcContractContent = SystemUtils.getTemplateConfig("pcContractContent");
		TemplateConfig mbContractContent = SystemUtils.getTemplateConfig("mbContractContent");
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("contTmpl", contTmpl);
		contTmpl.setGenerateMethod(CmsContTmpl.GenerateMethod.none);
		int generateCount = 0;

//		generateCount += generate(pcContractContent.getRealTemplatePath(), contTmpl.getPcPath(), model);
		generateCount += generate(mbContractContent.getRealTemplatePath(), contTmpl.getMbPath(), model);

		return generateCount;
	}

	@Transactional(readOnly = true)
	public int generateIndex() {
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("index");
		return generate(templateConfig.getRealTemplatePath(), templateConfig.getRealStaticPath(), null);
	}


	@Transactional(readOnly = true)
	public int generateOther() {
		int generateCount = 0;
		TemplateConfig hLoadJsTemplateConfig = SystemUtils.getTemplateConfig("HLoadJs");
		generateCount += generate(hLoadJsTemplateConfig.getRealTemplatePath(), hLoadJsTemplateConfig.getRealStaticPath(), null);
		TemplateConfig hEventJsTemplateConfig = SystemUtils.getTemplateConfig("HEventJs");
		generateCount += generate(hEventJsTemplateConfig.getRealTemplatePath(), hEventJsTemplateConfig.getRealStaticPath(), null);
		TemplateConfig hRenderJsTemplateConfig = SystemUtils.getTemplateConfig("HRenderJs");
		generateCount += generate(hRenderJsTemplateConfig.getRealTemplatePath(), hRenderJsTemplateConfig.getRealStaticPath(), null);
		
		TemplateConfig iLoadJsTemplateConfig = SystemUtils.getTemplateConfig("ILoadJs");
		generateCount += generate(iLoadJsTemplateConfig.getRealTemplatePath(), iLoadJsTemplateConfig.getRealStaticPath(), null);
		TemplateConfig iEventJsTemplateConfig = SystemUtils.getTemplateConfig("IEventJs");
		generateCount += generate(iEventJsTemplateConfig.getRealTemplatePath(), iEventJsTemplateConfig.getRealStaticPath(), null);
		TemplateConfig iRenderJsTemplateConfig = SystemUtils.getTemplateConfig("IRenderJs");
		generateCount += generate(iRenderJsTemplateConfig.getRealTemplatePath(), iRenderJsTemplateConfig.getRealStaticPath(), null);
		
		TemplateConfig loginJsTemplateConfig = SystemUtils.getTemplateConfig("LoginJs");
		generateCount += generate(loginJsTemplateConfig.getRealTemplatePath(), loginJsTemplateConfig.getRealStaticPath(), null);
		
		TemplateConfig wxShareJsTemplateConfig = SystemUtils.getTemplateConfig("WxShareJs");
		generateCount += generate(wxShareJsTemplateConfig.getRealTemplatePath(), wxShareJsTemplateConfig.getRealStaticPath(), null);
		
		return generateCount;
	}

	@Transactional(readOnly = true)
	public int delete(String staticPath) {
		if (StringUtils.isEmpty(staticPath)) {
			return 0;
		}
		File staticFile = new File(servletContext.getRealPath(staticPath));
		return FileUtils.deleteQuietly(staticFile) ? 1 : 0;
	}

	@Transactional
	public int delete(CmsContTmpl contTmpl) {
		if (contTmpl == null || StringUtils.isEmpty(contTmpl.getMbPath())) {
			return 0;
		}
		int deleteCount = 0;
//		deleteCount += delete(contTmpl.getPcPath());
		deleteCount += delete(contTmpl.getMbPath());
		return deleteCount;
	}

	@Transactional(readOnly = true)
	public int deleteIndex() {
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("index");
		return delete(templateConfig.getRealStaticPath());
	}

	@Transactional(readOnly = true)
	public int deleteOther() {
		int deleteCount = 0;
		
		return deleteCount;
	}

	/**
	 * SitemapUrl
	 * 
	 * @author JINXINFU
	 * @version 2.0
	 */
	public static class SitemapUrl {

		/**
		 * 更新频率
		 */
		public enum Changefreq {

			/** 经常 */
			always,

			/** 每小时 */
			hourly,

			/** 每天 */
			daily,

			/** 每周 */
			weekly,

			/** 每月 */
			monthly,

			/** 每年 */
			yearly,

			/** 从不 */
			never
		}

		/** 链接地址 */
		private String loc;

		/** 最后修改日期 */
		private Date lastmod;

		/** 更新频率 */
		private Changefreq changefreq;

		/** 权重 */
		private float priority;

		/**
		 * 获取链接地址
		 * 
		 * @return 链接地址
		 */
		public String getLoc() {
			return loc;
		}

		/**
		 * 设置链接地址
		 * 
		 * @param loc
		 *            链接地址
		 */
		public void setLoc(String loc) {
			this.loc = loc;
		}

		/**
		 * 获取最后修改日期
		 * 
		 * @return 最后修改日期
		 */
		public Date getLastmod() {
			return lastmod;
		}

		/**
		 * 设置最后修改日期
		 * 
		 * @param lastmod
		 *            最后修改日期
		 */
		public void setLastmod(Date lastmod) {
			this.lastmod = lastmod;
		}

		/**
		 * 获取更新频率
		 * 
		 * @return 更新频率
		 */
		public Changefreq getChangefreq() {
			return changefreq;
		}

		/**
		 * 设置更新频率
		 * 
		 * @param changefreq
		 *            更新频率
		 */
		public void setChangefreq(Changefreq changefreq) {
			this.changefreq = changefreq;
		}

		/**
		 * 获取权重
		 * 
		 * @return 权重
		 */
		public float getPriority() {
			return priority;
		}

		/**
		 * 设置权重
		 * 
		 * @param priority
		 *            权重
		 */
		public void setPriority(float priority) {
			this.priority = priority;
		}

	}

}