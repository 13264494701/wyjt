package com.jxf.loan.entity;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 合同表Entity
 * @author lmy
 * @version 2019-01-08
 */
public class NfsLoanContract extends CrudEntity<NfsLoanContract> {
	
	
	public enum SignatureType{
		//0:未认证
		none,
		//,1:ca认证
		caAuth,
		//,2:画个押认证
		
		huaya,
		//,3:君子签认证
		junziqian,
		
		//,4:安存认证
		anquanAuth,
		//5 有盾
		youdun
		
	}
	
	
	public enum Status{
		
		/** 未生成 */
		notCreate,
		
		/** 已生成 */
		created,
		
		/** 已签章 */
		signatured
		
	}
	
	private static final long serialVersionUID = 1L;
	/** 借条编号 */
	private Long loanId;		
	/** 合同地址 */
	private String contractUrl;		
	/** 签章类型 */
	private SignatureType signatureType;
	
	/** 签章编号 */
	private String signatureNo;		
	/** 签章地址 */
	private String signatureUrl;		
	/** 合同状态 */
	private Status status;	
	
	private Integer count;
	
	public enum LoanPurposeType {

		/*
		 * 临时周转 交房租 消费 还信用卡 报培训班 考驾照 其他
		 */
		OTHERS("其他"), // 0
		SHORTTIMEUSE("临时周转"), // 1
		REPAY_CHUMMAGE("交房租"), // 2
		CONSUMPTION("消费"), // 3
		REPAY_CREDITCARD("还信用卡"), // 4
		REPAY_TRAINING("报培训班"), // 5
		REPAY_DRIVINGLICENSE("考驾照"), // 6
		OTHERSNEW("其他");
		/** 类型名称 */
		private final String typeName;

		private LoanPurposeType(String typeName) {

			this.typeName = typeName;

		}

		public String getTypeName() {

			return typeName;

		}
	}
	
	public NfsLoanContract() {
		super();
	}

	public NfsLoanContract(Long id){
		super(id);
	}

	@NotNull(message="借条编号不能为空")
	public Long getLoanId() {
		return loanId;
	}

	public void setLoanId(Long loanId) {
		this.loanId = loanId;
	}
	
	@Length(min=0, max=255, message="合同地址长度必须介于 0 和 255 之间")
	public String getContractUrl() {
		return contractUrl;
	}

	public void setContractUrl(String contractUrl) {
		this.contractUrl = contractUrl;
	}
	
	public SignatureType getSignatureType() {
		return signatureType;
	}

	public void setSignatureType(SignatureType signatureType) {
		this.signatureType = signatureType;
	}

	@Length(min=0, max=32, message="签章编号长度必须介于 0 和 32 之间")
	public String getSignatureNo() {
		return signatureNo;
	}

	public void setSignatureNo(String signatureNo) {
		this.signatureNo = signatureNo;
	}
	
	@Length(min=0, max=255, message="签章地址长度必须介于 0 和 255 之间")
	public String getSignatureUrl() {
		return signatureUrl;
	}

	public void setSignatureUrl(String signatureUrl) {
		this.signatureUrl = signatureUrl;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
	

	
}