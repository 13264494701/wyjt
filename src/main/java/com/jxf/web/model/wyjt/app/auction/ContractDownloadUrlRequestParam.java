package com.jxf.web.model.wyjt.app.auction;
/**
 * @作者: suHuim
 * @创建时间 :2019年3月6日 
 * @功能说明: 获取合同下载地址
 */
public class ContractDownloadUrlRequestParam {

	/** 债转Id */
	private String auctionId;
	
	/** 合同类型 1：债转合同，2：借条合同 */
	private String type;

	public String getAuctionId() {
		return auctionId;
	}

	public void setAuctionId(String auctionId) {
		this.auctionId = auctionId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	
}
