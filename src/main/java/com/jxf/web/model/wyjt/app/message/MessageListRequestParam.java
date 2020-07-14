package com.jxf.web.model.wyjt.app.message;



public class MessageListRequestParam   {

	/** 消息标签 */
	private Integer tabIndex;//0->全部,1->借条消息,2->服务消息,3->交易消息,4->系统消息
	
	/** 页码 */
	private Integer pageNo;
	/** 每页大小 */
	private Integer pageSize = 20;
	
	public Integer getTabIndex() {
		return tabIndex;
	}
	public void setTabIndex(Integer tabIndex) {
		this.tabIndex = tabIndex;
	}
	
	public Integer getPageNo() {
		return pageNo;
	}
	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}
	
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	
}
