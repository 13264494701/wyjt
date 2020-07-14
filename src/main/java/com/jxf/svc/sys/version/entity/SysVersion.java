package com.jxf.svc.sys.version.entity;

import org.hibernate.validator.constraints.Length;

import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 系统版本Entity
 * @author wo
 * @version 2019-01-07
 */
public class SysVersion extends CrudEntity<SysVersion> {
	
	private static final long serialVersionUID = 1L;
	/**
	 * 前端类型
	 */
	public enum Type {

		/** 安卓 */
		android,

		/** 苹果 */
		ios
	}
	
	/** 前端类型 */
	private Type type;		
	/** 最新版本号 */
	private String lastVersion;		
	/** 是否需要升级 */
	private Boolean needsUpdate;		
	/** 是否强制升级 */
	private Boolean isForce;		
	/** 升级内容 */
	private String content;		
	/** 升级路径 */
	private String url;		
	
	public SysVersion() {
		super();
	}

	public SysVersion(Long id){
		super(id);
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
	
	@Length(min=1, max=32, message="最新版本号长度必须介于 1 和 32 之间")
	public String getLastVersion() {
		return lastVersion;
	}

	public void setLastVersion(String lastVersion) {
		this.lastVersion = lastVersion;
	}
	

	public Boolean getNeedsUpdate() {
		return needsUpdate;
	}

	public void setNeedsUpdate(Boolean needsUpdate) {
		this.needsUpdate = needsUpdate;
	}
	


	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@Length(min=0, max=255, message="升级路径长度必须介于 0 和 255 之间")
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Boolean getIsForce() {
		return isForce;
	}

	public void setIsForce(Boolean isForce) {
		this.isForce = isForce;
	}
	
}