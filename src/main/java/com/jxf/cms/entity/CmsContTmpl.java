package com.jxf.cms.entity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.annotation.JSONField;
import com.jxf.svc.config.TemplateConfig;
import com.jxf.svc.sys.crud.entity.CrudEntity;
import com.jxf.svc.utils.SystemUtils;

/**
 * 合同模板Entity
 * @author huojiayuan
 * @version 2016-12-01
 */
public class CmsContTmpl extends CrudEntity<CmsContTmpl> {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 静态生成方式
	 */
	public enum GenerateMethod {

		/** 无 */
		none,

		/** 即时 */
		eager,

		/** 延时 */
		lazy
	}
	
	/**
	 * 类型
	 */
	public enum Type {

		/** 借款协议 */
		loan,
		
		/** 用户注册协议 */
		registration,
		
		/** 催收服务协议*/
		collection,
		
		/** 仲裁服务说明*/
		arbitration,
		
		/** 支付服务协议 */
		payservice

	}
	/**
	 * 状态
	 */
	public enum Status {

		/**未生效*/
		invalid,

		/**已生效*/
		valid
	}
	/** 标题 */
	private String title;
	/** 合同类型 */
	private CmsContTmpl.Type type;	
	/** 状态 */
	private CmsContTmpl.Status status;	
	/** 合同内容 */
	private String content;	
	/** 生效时间 */
	private Date validTime;	
	/** 失效时间 */
	private Date invalidTime;
	/** 是否静态 */
	private Boolean isStatic;   
	
	/** 静态生成方式 */
	private GenerateMethod generateMethod;
	
	public CmsContTmpl() {
		super();
	}

	public CmsContTmpl(Long id){
		super(id);
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public CmsContTmpl.Type getType() {
		return type;
	}

	public void setType(CmsContTmpl.Type type) {
		this.type = type;
	}

	public CmsContTmpl.Status getStatus() {
		return status;
	}

	public void setStatus(CmsContTmpl.Status status) {
		this.status = status;
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	public Date getValidTime() {
		return validTime;
	}

	public void setValidTime(Date validTime) {
		this.validTime = validTime;
	}
	
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	public Date getInvalidTime() {
		return invalidTime;
	}

	public void setInvalidTime(Date invalidTime) {
		this.invalidTime = invalidTime;
	}

	public Boolean getIsStatic() {
		return isStatic;
	}

	public void setIsStatic(Boolean isStatic) {
		this.isStatic = isStatic;
	}

	/**
	 * 获取路径
	 * 
	 * @return 路径
	 */

	public String getPcPath() {
		TemplateConfig pcContractContent = SystemUtils.getTemplateConfig("pcContractContent");
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("contTmpl", this);
		return pcContractContent.getRealStaticPath(model);
	}
	/**
	 * 获取路径
	 * 
	 * @return 路径
	 */

	public String getMbPath() {

		TemplateConfig mbContractContent = SystemUtils.getTemplateConfig("mbContractContent");
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("contTmpl", this);
		return mbContractContent.getRealStaticPath(model);
	}

	/**
	 * 获取PC URL
	 * 
	 * @return URL
	 */
	public String getPcUrl() {
		return getPcPath();
	}
	/**
	 * 获取移动 URL
	 * 
	 * @return URL
	 */
	public String getMbUrl() {
		return getMbPath();
	}

	public GenerateMethod getGenerateMethod() {
		return generateMethod;
	}

	public void setGenerateMethod(GenerateMethod generateMethod) {
		this.generateMethod = generateMethod;
	}


}