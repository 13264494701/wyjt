package com.jxf.ufang.entity;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

import com.jxf.svc.sys.crud.entity.CrudEntity;


/**
 * 流量管理Entity
 * @author wo
 * @version 2018-11-24
 */
public class UfangLoaneeData extends CrudEntity<UfangLoaneeData> {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 运营商状态
	 */
	public enum YunyingshangStatus {

	    /** 未认证*/
		unverified,
		
	    /** 已认证*/
		verified,
		
		/** 已过期 */
		expired
	}
	
	/**
	 * 流量渠道
	 */
	public enum Channel {

		/** 无忧借条APP*/
		  wyjt,
		  
		/** 无忧借条官网公众号 */
		 weixin,
		  
		/** 优放贷*/
		  ufang,
		  
		/** 第三方渠道 */
          thirdpart
	}
	
	/**
	 * 数据状态
	 */
	public enum Status {
		
	    /** 最新的*/
		fresh,
		
		/** 已过期 */
		expired
	}
	private String prodCode;
	/** 借款人姓名 */
	private String name;		
	/** 放款金额 */
	private String phoneNo;		
	/** 身份证号 */
	private String idNo;		
	/** 年龄 */
	private Integer age;		
	/** 芝麻分 */
	private Integer zhimafen;		
	/** QQ号码 */
	private String qqNo;		
	/** 微信账号 */
	private String weixinNo;		
	/** 运营商状态 */
	private YunyingshangStatus yunyingshangStatus;
	/** 运营商报告TaskId */
	private String reportTaskId;
	/** 借款人ID */
	private Long loaneeId;		
	/** 申请金额 */
	private String applyAmount;	
	/** 申请IP*/
	private String applyIp;
	/** 申请地区*/
	private String applyArea;
	/** 手机地区*/
	private String phoneArea;
	/** 销售量 */
	private String sales;		
	/** 申请渠道 */
	private Channel channel;
	/** 状态 */
	private Status status;	
	
	private BigDecimal price;
	
	private Date applyTime;
	
	private String minAge;		// 最小 年龄
	private String maxAge;		    // 最大 年龄
	private String minZmf;   // 最小 芝麻分
	private String maxZmf;		// 最大 芝麻分
	
	private BigDecimal minPrice;        // 最小 价格
	private BigDecimal maxPrice;		// 最大 价格
	
	/** 起始申请时间 */
	private Date beginApplyTime;		
	/** 结束申请时间 */
	private Date endApplyTime;
	
	/** 起始更新时间 */
	private Date beginUpdateTime;		
	/** 结束更新时间 */
	private Date endUpdateTime;

	private Integer bought;
	
	private String empNo;

	public Integer getBought() {
		return bought;
	}

	public void setBought(Integer bought) {
		this.bought = bought;
	}

	public String getEmpNo() {
		return empNo;
	}

	public void setEmpNo(String empNo) {
		this.empNo = empNo;
	}

	public UfangLoaneeData() {
		super();
	}

	public UfangLoaneeData(Long id){
		super(id);
	}

	@Length(min=0, max=64, message="借款人姓名长度必须介于 0 和 64 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Length(min=0, max=11, message="放款金额长度必须介于 0 和 11 之间")
	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	
	@Length(min=0, max=32, message="身份证号长度必须介于 0 和 32 之间")
	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}
	

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}
	

	public Integer getZhimafen() {
		return zhimafen;
	}

	public void setZhimafen(Integer zhimafen) {
		this.zhimafen = zhimafen;
	}
	
	@Length(min=0, max=20, message="QQ号码长度必须介于 0 和 20 之间")
	public String getQqNo() {
		return qqNo;
	}

	public void setQqNo(String qqNo) {
		this.qqNo = qqNo;
	}
	
	@Length(min=0, max=32, message="微信账号长度必须介于 0 和 32 之间")
	public String getWeixinNo() {
		return weixinNo;
	}

	public void setWeixinNo(String weixinNo) {
		this.weixinNo = weixinNo;
	}
	

	public YunyingshangStatus getYunyingshangStatus() {
		return yunyingshangStatus;
	}

	public void setYunyingshangStatus(YunyingshangStatus yunyingshangStatus) {
		this.yunyingshangStatus = yunyingshangStatus;
	}
	
	public String getReportTaskId() {
		return reportTaskId;
	}

	public void setReportTaskId(String reportTaskId) {
		this.reportTaskId = reportTaskId;
	}

	public Long getLoaneeId() {
		return loaneeId;
	}

	public void setLoaneeId(Long loaneeId) {
		this.loaneeId = loaneeId;
	}
	
	public String getApplyAmount() {
		return applyAmount;
	}

	public void setApplyAmount(String applyAmount) {
		this.applyAmount = applyAmount;
	}

	@Length(min=0, max=11, message="销售量长度必须介于 0 和 11 之间")
	public String getSales() {
		return sales;
	}

	public void setSales(String sales) {
		this.sales = sales;
	}
	

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}
	

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
	
	
	public boolean isValidPhoneNo() {
		String workerPhone = "18201660917,15910691574,18603061657,18010195820,17310238801,15652583246,18210214696,13366661088,15110269799,13699224794,13264494701,13121812870,18801204212,15901353486";
		if(StringUtils.contains(workerPhone, getPhoneNo())) {
			return false;
		}
		return true;
	}

	public String getApplyIp() {
		return applyIp;
	}

	public void setApplyIp(String applyIp) {
		this.applyIp = applyIp;
	}

	public String getApplyArea() {
		return applyArea;
	}

	public void setApplyArea(String applyArea) {
		this.applyArea = applyArea;
	}

	public String getPhoneArea() {
		return phoneArea;
	}

	public void setPhoneArea(String phoneArea) {
		this.phoneArea = phoneArea;
	}

	public String getProdCode() {
		return prodCode;
	}

	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}

	public BigDecimal getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(BigDecimal minPrice) {
		this.minPrice = minPrice;
	}

	public BigDecimal getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(BigDecimal maxPrice) {
		this.maxPrice = maxPrice;
	}

	public String getMinAge() {
		return minAge;
	}

	public void setMinAge(String minAge) {
		this.minAge = minAge;
	}

	public String getMaxAge() {
		return maxAge;
	}

	public void setMaxAge(String maxAge) {
		this.maxAge = maxAge;
	}

	public String getMinZmf() {
		return minZmf;
	}

	public void setMinZmf(String minZmf) {
		this.minZmf = minZmf;
	}

	public String getMaxZmf() {
		return maxZmf;
	}

	public void setMaxZmf(String maxZmf) {
		this.maxZmf = maxZmf;
	}

	public Date getBeginApplyTime() {
		return beginApplyTime;
	}

	public void setBeginApplyTime(Date beginApplyTime) {
		this.beginApplyTime = beginApplyTime;
	}

	public Date getEndApplyTime() {
		return endApplyTime;
	}

	public void setEndApplyTime(Date endApplyTime) {
		this.endApplyTime = endApplyTime;
	}

	public Date getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}

	public Date getBeginUpdateTime() {
		return beginUpdateTime;
	}

	public void setBeginUpdateTime(Date beginUpdateTime) {
		this.beginUpdateTime = beginUpdateTime;
	}

	public Date getEndUpdateTime() {
		return endUpdateTime;
	}

	public void setEndUpdateTime(Date endUpdateTime) {
		this.endUpdateTime = endUpdateTime;
	}


		
}