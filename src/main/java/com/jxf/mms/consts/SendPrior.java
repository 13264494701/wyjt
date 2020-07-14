package com.jxf.mms.consts;

/**
 * 
 * @类功能说明： 
 * @类修改者： 
 * @修改日期： 
 * @修改说明： 
 * @公司名称：北京金信服科技有限公司
 * @作者：Administrator 
 * @创建时间：2016年3月11日 下午12:12:22 
 * @版本：V1.0
 */
public enum SendPrior {

	High("001","高"),
	Normal("002","中"),
	Low("003","低");
	
	private String code;
	private String desc;
	
	private SendPrior(String code,String desc){
		this.code = code;
		this.desc = desc;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	public static SendPrior getSendPrior(String code) {
		for (SendPrior sendPrior : SendPrior.values()) {
			if(code.equals(sendPrior.getCode())){
				return sendPrior;
			}
		}
		return null;
	}
}
