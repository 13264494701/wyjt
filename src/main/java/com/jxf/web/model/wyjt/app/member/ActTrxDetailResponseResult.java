package com.jxf.web.model.wyjt.app.member;
/**
 * @作者: xiaorongdian
 * @创建时间 :2018年11月6日 下午6:44:52
 * @功能说明:账户详情
 */
public class ActTrxDetailResponseResult {

	/** 标题 */
	private String title;
	
	/** 交易时间 */
	private String trxTime;
	
	/** 方向 0出账 1入账 */
	private Integer drc;
	
	/** 涉及金额 */
	private String amount;
	
	/** 流水编号 */
	private String idNo;
	
	/** 交易备注 */
	private String rmk;
	/** 交易类型(公信堂) */
	private String type;
	
	/** 图标 */
	private String imgUrl;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getDrc() {
		return drc;
	}

	public void setDrc(Integer drc) {
		this.drc = drc;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getRmk() {
		return rmk;
	}

	public void setRmk(String rmk) {
		this.rmk = rmk;
	}

	public String getTrxTime() {
		return trxTime;
	}

	public void setTrxTime(String trxTime) {
		this.trxTime = trxTime;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
