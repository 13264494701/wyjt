package com.jxf.web.model.wyjt.app.auction;

/**
 * @作者: gaobo
 * @创建时间 :2019年3月6日 
 * @功能说明: 我转让的列表查询
 */
public class MineTransferListRequestParam {

	/** 每页记录数 */
	private Integer pageSize = 20;
	/** 页码(必传) */
	private Integer pageNo;
	
	private Integer auctionTab;//转让中 排序专用 默认0 不排序  1 正序 2 倒叙
	
	private Integer auctionedTab;//转让完成 排序专用 默认0 不排序  1 正序 2 倒叙
	
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public Integer getPageNo() {
		return pageNo;
	}
	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}
	public Integer getAuctionTab() {
		return auctionTab;
	}
	public void setAuctionTab(Integer auctionTab) {
		this.auctionTab = auctionTab;
	}
	public Integer getAuctionedTab() {
		return auctionedTab;
	}
	public void setAuctionedTab(Integer auctionedTab) {
		this.auctionedTab = auctionedTab;
	}
	
}
