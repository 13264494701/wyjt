package com.jxf.web.model.wyjt.app.act;
/**
 * @作者: xiaorongdian
 * @创建时间 :2018年12月20日 上午10:12:16
 * @功能说明:我的转账记录
 */
public class TransferListRequestParam {
	
	/** 页码 */
	private Integer pageNo; 
	
	/** -1全部 0转入 1转出 */
	private Integer type;
	
	public Integer getPageNo() {
		return pageNo;
	}
	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
}
