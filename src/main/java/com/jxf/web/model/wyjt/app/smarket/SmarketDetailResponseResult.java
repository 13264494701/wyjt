package com.jxf.web.model.wyjt.app.smarket;

import java.util.ArrayList;
import java.util.List;

public class SmarketDetailResponseResult {

	/** 市场ID */
	private String marketId;	
	/** 贷超名称 */
	private String name;		
	/** 图标 */
	private String logo;			
	/** 最小借款金额 */
	private String minLoanAmt;		
	/** 最大借款金额 */
	private String maxLoanAmt;		
	/** 最短借款周期 */
	private String minLoanTerm;		
	/** 最长借款周期 */
	private String maxLoanTerm;		
	/** 最小利率 */
	private String minIntRate;		
	/** 最大利率 */
	private String maxIntRate;		
	/** 审批时长 */
	private String checkTerm;		
	/** 展示已放款笔数 */
	private String displayLoanQuantity;		
	/** 是否需要认证 */
	private Integer needsIdentify;//0->不需要;1->需要		
	/** 认证列表 */
	private List<Identify> identifyList = new ArrayList<Identify>();
	
	/** 贷款要求 */
	private String loanRequirement;		
	/** 申请材料 */
	private String applyMaterials;
		
	
	public String getMarketId() {
		return marketId;
	}
	public void setMarketId(String marketId) {
		this.marketId = marketId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getMinLoanAmt() {
		return minLoanAmt;
	}
	public void setMinLoanAmt(String minLoanAmt) {
		this.minLoanAmt = minLoanAmt;
	}
	public String getMaxLoanAmt() {
		return maxLoanAmt;
	}
	public void setMaxLoanAmt(String maxLoanAmt) {
		this.maxLoanAmt = maxLoanAmt;
	}
	public String getMinLoanTerm() {
		return minLoanTerm;
	}
	public void setMinLoanTerm(String minLoanTerm) {
		this.minLoanTerm = minLoanTerm;
	}
	public String getMaxLoanTerm() {
		return maxLoanTerm;
	}
	public void setMaxLoanTerm(String maxLoanTerm) {
		this.maxLoanTerm = maxLoanTerm;
	}
	public String getMinIntRate() {
		return minIntRate;
	}
	public void setMinIntRate(String minIntRate) {
		this.minIntRate = minIntRate;
	}
	public String getMaxIntRate() {
		return maxIntRate;
	}
	public void setMaxIntRate(String maxIntRate) {
		this.maxIntRate = maxIntRate;
	}
	public String getCheckTerm() {
		return checkTerm;
	}
	public void setCheckTerm(String checkTerm) {
		this.checkTerm = checkTerm;
	}
	public String getDisplayLoanQuantity() {
		return displayLoanQuantity;
	}
	public void setDisplayLoanQuantity(String displayLoanQuantity) {
		this.displayLoanQuantity = displayLoanQuantity;
	}
	public Integer getNeedsIdentify() {
		return needsIdentify;
	}
	public void setNeedsIdentify(Integer needsIdentify) {
		this.needsIdentify = needsIdentify;
	}
	public String getLoanRequirement() {
		return loanRequirement;
	}
	public void setLoanRequirement(String loanRequirement) {
		this.loanRequirement = loanRequirement;
	}
	public String getApplyMaterials() {
		return applyMaterials;
	}
	public void setApplyMaterials(String applyMaterials) {
		this.applyMaterials = applyMaterials;
	}		
	
	public List<Identify> getIdentifyList() {
		return identifyList;
	}
	public void setIdentifyList(List<Identify> identifyList) {
		this.identifyList = identifyList;
	}

	public class Identify {
	

		/** 认证类型 */
		private Integer type;//0->实名认证;1->运营商认证;2->芝麻分认证
		/** 跳转链接 */
		private String url;	
		
		public Identify(Integer type, String url) {
			super();
			this.type = type;
			this.url = url;
		}
		
		public Integer getType() {
			return type;
		}
		public void setType(Integer type) {
			this.type = type;
		}

		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
	}

}