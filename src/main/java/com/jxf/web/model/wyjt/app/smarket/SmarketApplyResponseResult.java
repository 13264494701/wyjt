package com.jxf.web.model.wyjt.app.smarket;


public class SmarketApplyResponseResult {

	/** 市场ID */
	private String marketId;
	/** 跳转类型*/
	private int redirectType;//0->内部，1->外部
	/** 跳转链接*/
	private String url;		

	public String getMarketId() {
		return marketId;
	}
	public void setMarketId(String marketId) {
		this.marketId = marketId;
	}

	public int getRedirectType() {
		return redirectType;
	}
	public void setRedirectType(int redirectType) {
		this.redirectType = redirectType;
	}

	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}