package com.jxf.loan.entity;

import org.hibernate.validator.constraints.Length;

import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 业务保全Entity
 * @author SuHuimin
 * @version 2019-07-01
 */
public class NfsLoanPreservation extends CrudEntity<NfsLoanPreservation> {
	
	private static final long serialVersionUID = 1L;
	
	
	/**
	 * 节点类型
	 */
	public enum NodeType {

		/** 认证*/
		IDENTIFY,
		
		/** 合同*/
		CONTRACT,
		
		/** 付款*/
		PAY,
		
		/** 还款*/
		REPAY,
		
		/** 续签合同*/
		RENEWAL,
		
	}
	
	
			/** 证据链id */
	private Long proofChainId;
			/** 关联业务id */
	private Long businessId;
			/** 订单id */
	private Long partnerOrderId;	
			/** 父节点订单id */
	private Long parentOrderId;		
			/** 节点*/
	private NodeType nodeType;		
			/** 保全号 */
	private String precode;		
	
	
	public NfsLoanPreservation() {
		super();
	}

	public NfsLoanPreservation(Long id){
		super(id);
	}
	
	public Long getProofChainId() {
		return proofChainId;
	}

	public void setProofChainId(Long proofChainId) {
		this.proofChainId = proofChainId;
	}

	public Long getBusinessId() {
		return businessId;
	}

	public void setBusinessId(Long businessId) {
		this.businessId = businessId;
	}

	public Long getPartnerOrderId() {
		return partnerOrderId;
	}

	public void setPartnerOrderId(Long partnerOrderId) {
		this.partnerOrderId = partnerOrderId;
	}

	public Long getParentOrderId() {
		return parentOrderId;
	}

	public void setParentOrderId(Long parentOrderId) {
		this.parentOrderId = parentOrderId;
	}
	
	public NodeType getNodeType() {
		return nodeType;
	}

	public void setNodeType(NodeType nodeType) {
		this.nodeType = nodeType;
	}

	@Length(min=0, max=64, message="保全号长度必须介于 0 和 64 之间")
	public String getPrecode() {
		return precode;
	}

	public void setPrecode(String precode) {
		this.precode = precode;
	}

	
}