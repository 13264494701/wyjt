package com.jxf.loan.signature.youdun.preservation;

import com.jxf.loan.signature.youdun.YouDunConstant;

/**
 * 有盾业务保全数据基类
 * @author Administrator
 *
 */
public class UdPreservationInfo {
	/**  证据链Id*/
	private String proofChainId;
	/**  订单Id*/
	private String partnerOrderId;
	/**  父订单Id*/
	private String parentOrderId;
	/**  节点类型*/
	private YouDunConstant.NodeType nodeType;
	/** 构造上传数据*/
	private PreservationBuilderData preservationBuilderData;
	
	
	public String getProofChainId() {
		return proofChainId;
	}
	public void setProofChainId(String proofChainId) {
		this.proofChainId = proofChainId;
	}
	public String getPartnerOrderId() {
		return partnerOrderId;
	}
	public void setPartnerOrderId(String partnerOrderId) {
		this.partnerOrderId = partnerOrderId;
	}
	public String getParentOrderId() {
		return parentOrderId;
	}
	public void setParentOrderId(String parentOrderId) {
		this.parentOrderId = parentOrderId;
	}
	public YouDunConstant.NodeType getNodeType() {
		return nodeType;
	}
	public void setNodeType(YouDunConstant.NodeType nodeType) {
		this.nodeType = nodeType;
	}
	public PreservationBuilderData getPreservationBuilderData() {
		return preservationBuilderData;
	}
	public void setPreservationBuilderData(PreservationBuilderData preservationBuilderData) {
		this.preservationBuilderData = preservationBuilderData;
	}
	
}
