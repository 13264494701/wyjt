package com.jxf.pwithdraw.entity.fuiou;


/**
 * @description 富友提现交易状态
 * @author suHuimin
 */
public enum FuiouWithdrawStateEnum {

	SUCCESS("success","成功"), 
	ACCEPTSUCCESS("acceptSuccess","受理成功"), 
	INTERNALFAIL("internalFail","富友失败"), 
	CHANACCEPTSUCCESS("chanAcceptSuccess","银行受理成功"), 
	CHANNELFAIL("channelFail","通道失败"),
	CARDINFOERROR("cardInfoError ","卡信息错误"),
	UNKNOWREASONS("unknowReasons","交易结果未知"),
	UNSEND("0","交易未发送"),
	SENDED_CHANACCEPT("1","交易已发送且银行受理成功"),
	SENDING("3","交易发送中"),
	FUIOUACCEPT("000000","富友受理成功"),
	FUIOUTIMEOUT("999999","富友系统超时"),
	TRADESUCCESS("0000","交易成功");
	
	
	public final String code;

	public final String desc;

	FuiouWithdrawStateEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static FuiouWithdrawStateEnum getRetCodeEnumByValue(String value) {
		for (FuiouWithdrawStateEnum retCodeEnum : FuiouWithdrawStateEnum.values()) {
			if (retCodeEnum.getCode().equals(value)) {
				return retCodeEnum;
			}
		}
		return null;
	}

	
	public String getCode() {
		return code;
	}
}
