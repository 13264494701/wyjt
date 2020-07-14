package com.jxf.ufang.entity;

import org.hibernate.validator.constraints.Length;

import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 贷超管理Entity
 * @author wo
 * @version 2019-03-07
 */
public class UfangLoanMarket extends CrudEntity<UfangLoanMarket> {
	
	private static final long serialVersionUID = 1L;
	

    /**
     * 跳转方式
     */
    public enum RedirectType {
        /**
         * 内部
         */
        inner,
        /**
         * 外部
         */
        outer
    }
	
	/** 贷超名称 */
	private String name;		
	/** 图标 */
	private String logo;		
	/** 归属机构 */
	private UfangBrn brn;		
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
	private Boolean needsIdentify;		
	/** 贷款要求 */
	private String loanRequirement;		
	/** 申请材料 */
	private String applyMaterials;		
	/** 是否上架 */
	private Boolean isMarketable;	
	/** 跳转类型*/
	private RedirectType redirectType;
	
	/** 展示顺序 */
	private int sort;
	
	public UfangLoanMarket() {
		super();
	}

	public UfangLoanMarket(Long id){
		super(id);
	}

	@Length(min=1, max=100, message="贷超名称长度必须介于 1 和 100 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Length(min=0, max=255, message="图标长度必须介于 0 和 255 之间")
	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}
	
	public UfangBrn getBrn() {
		return brn;
	}

	public void setBrn(UfangBrn brn) {
		this.brn = brn;
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
	
	@Length(min=0, max=11, message="最短借款周期长度必须介于 0 和 11 之间")
	public String getMinLoanTerm() {
		return minLoanTerm;
	}

	public void setMinLoanTerm(String minLoanTerm) {
		this.minLoanTerm = minLoanTerm;
	}
	
	@Length(min=0, max=11, message="最长借款周期长度必须介于 0 和 11 之间")
	public String getMaxLoanTerm() {
		return maxLoanTerm;
	}

	public void setMaxLoanTerm(String maxLoanTerm) {
		this.maxLoanTerm = maxLoanTerm;
	}
	
	@Length(min=0, max=4, message="最小利率长度必须介于 0 和 4 之间")
	public String getMinIntRate() {
		return minIntRate;
	}

	public void setMinIntRate(String minIntRate) {
		this.minIntRate = minIntRate;
	}
	
	@Length(min=0, max=4, message="最大利率长度必须介于 0 和 4 之间")
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
	

	public Boolean getNeedsIdentify() {
		return needsIdentify;
	}

	public void setNeedsIdentify(Boolean needsIdentify) {
		this.needsIdentify = needsIdentify;
	}
	
	@Length(min=0, max=1024, message="贷款要求长度必须介于 0 和 1024 之间")
	public String getLoanRequirement() {
		return loanRequirement;
	}

	public void setLoanRequirement(String loanRequirement) {
		this.loanRequirement = loanRequirement;
	}
	
	@Length(min=0, max=255, message="申请材料长度必须介于 0 和 255 之间")
	public String getApplyMaterials() {
		return applyMaterials;
	}

	public void setApplyMaterials(String applyMaterials) {
		this.applyMaterials = applyMaterials;
	}
	

	public Boolean getIsMarketable() {
		return isMarketable;
	}

	public void setIsMarketable(Boolean isMarketable) {
		this.isMarketable = isMarketable;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}


	public RedirectType getRedirectType() {
		return redirectType;
	}

	public void setRedirectType(RedirectType redirectType) {
		this.redirectType = redirectType;
	}


	
}