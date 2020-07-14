package com.jxf.wx.api.message;

import com.jxf.wx.api.message.util.MessageBuilder;

public class TransferMsg extends BaseMsg{
	
	private static final long serialVersionUID = 1L;
	private String accountName;
	
	
	
	public TransferMsg() {
		super();
	}
	
	public TransferMsg(String accountName) {
		super();
		this.accountName = accountName;
	}

	public String getAccountName() {
		return accountName;
	}


	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}


	@Override
	public String toXml() {
		MessageBuilder mb = new MessageBuilder(super.toXml());
        mb.addData("MsgType", RespType.TRANSFER);
        if(accountName != null && !"".equals(accountName)){
            mb.append("<TransInfo>\n");
            mb.addData("KfAccount",accountName );
            mb.append("</TransInfo>\n");
            mb.surroundWith("xml");
        }
		return mb.toString();
	}
	
}
