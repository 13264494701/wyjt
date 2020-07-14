package com.jxf.web.model.wyjt.app.auction;




/***
 * 可购入的列表查询
 * @author wo
 *
 */
public class LoanCrAuctionListRequestParam {

	/** 所属地市编号 */
	private String cityId;
	/** 每页记录数 */
	private Integer pageSize = 20;
	/** 页码(必传) */
	private Integer pageNo;
	
	private Integer overdueTab;//逾期天数 排序专用 默认0 不排序  1 正序 2 倒叙
	
	private Integer priceTab;//转让金额 排序专用 默认0 不排序  1 正序 2 倒叙
	
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
	public String getCityId() {
		return cityId;
	}
	public void setCityId(String cityId) {
		this.cityId = cityId;
	}
	public Integer getOverdueTab() {
		return overdueTab;
	}
	public void setOverdueTab(Integer overdueTab) {
		this.overdueTab = overdueTab;
	}
	public Integer getPriceTab() {
		return priceTab;
	}
	public void setPriceTab(Integer priceTab) {
		this.priceTab = priceTab;
	}


}
