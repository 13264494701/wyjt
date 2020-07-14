package com.jxf.svc.freemarker;

import java.io.Writer;

import freemarker.core.Environment;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

/**
 * FreeMarker异常处理
 * 
 * @author JINXINFU
 * @version 2.0
 */
public class FreeMarkerExceptionHandler implements TemplateExceptionHandler {

	/** 默认模板异常处理 */
	private static final TemplateExceptionHandler DEFAULT_TEMPLATE_EXCEPTION_HANDLER = TemplateExceptionHandler.DEBUG_HANDLER;

	/**
	 * 模板异常处理
	 * 
	 * @param te
	 *            模板异常
	 * @param env
	 *            环境变量
	 * @param out
	 *            输出
	 */
	public void handleTemplateException(TemplateException te, Environment env, Writer out) throws TemplateException {

		DEFAULT_TEMPLATE_EXCEPTION_HANDLER.handleTemplateException(te, env, out);
	}

}