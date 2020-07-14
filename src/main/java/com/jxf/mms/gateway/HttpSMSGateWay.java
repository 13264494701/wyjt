package com.jxf.mms.gateway;

public interface HttpSMSGateWay {

	public int send(String phoneNo, String msg);

	public int sendCollectionMessage(String phoneNo, String content);
	
	public int sendNetLoanMessage(String phoneNo, String content);

}