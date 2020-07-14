package com.jxf.web.model.wyjt.app;



public class VersionUpdateResponseResult {

	/**是否需要升级*/
	private Integer needsUpdate;//0->不需要   1->需要
	
	/**是否强制升级*/
	private Integer force;//0->不需要强制升级 1->强制升级
	
	/**升级内容*/
	private String content;
	/**升级地址*/
	private String url;
	
	
	public Integer getNeedsUpdate() {
		return needsUpdate;
	}
	public void setNeedsUpdate(Integer needsUpdate) {
		this.needsUpdate = needsUpdate;
	}
	public Integer getForce() {
		return force;
	}
	public void setForce(Integer force) {
		this.force = force;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

}