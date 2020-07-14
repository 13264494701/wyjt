package com.jxf.loan.entity;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 债转合同Entity
 * @author suHuimin
 * @version 2019-03-12
 */
public class NfsCrContract extends CrudEntity<NfsCrContract> {
	
	private static final long serialVersionUID = 1L;
	
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
	
	
	/** 债权转让记录id */
	private Long crId;		
	/** 债权合同地址 */
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
	
	public NfsCrContract() {
		super();
	}

	public NfsCrContract(Long id){
		super(id);
	}

	@NotNull(message="债权转让记录id不能为空")
	public Long getCrId() {
		return crId;
	}

	public void setCrId(Long crId) {
		this.crId = crId;
	}
	
	@Length(min=0, max=255, message="债权合同地址长度必须介于 0 和 255 之间")
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

	@Length(min=0, max=255, message="签章编号长度必须介于 0 和 255 之间")
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