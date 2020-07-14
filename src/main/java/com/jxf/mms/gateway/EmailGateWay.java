package com.jxf.mms.gateway;

/**
 * 
 * @version 1.0
 */
public interface EmailGateWay {

	/**
	 * 
	 * @param newMessage
	 */
	public int send() throws Exception;

}
