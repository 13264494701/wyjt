package com.jxf.wx.api.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.jxf.svc.sys.crud.entity.CrudEntity;

/***
 * 
 * @类功能说明： 在线客服
 * @类修改者： 
 * @修改日期： 
 * @修改说明： 
 * @公司名称：北京金信服科技有限公司
 * @作者：Administrator 
 * @创建时间：2016年11月11日 下午5:29:46 
 * @版本：V1.0
 */
public class OnlineCustom extends CrudEntity<OnlineCustom>{
   

	private static final long serialVersionUID = 1L;

	@JSONField(name = "kf_account")
    private String accountName;
    
	@JSONField(name="status")
	private String status;	

    @JSONField(name="kf_id")
    private Long id;
    
    @JSONField(name="auto_accept")
    private int autoAccept;
    
    @JSONField(name="accepted_case")
    private int acceptedCase;

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getAutoAccept() {
		return autoAccept;
	}

	public void setAutoAccept(int autoAccept) {
		this.autoAccept = autoAccept;
	}

	public int getAcceptedCase() {
		return acceptedCase;
	}

	public void setAcceptedCase(int acceptedCase) {
		this.acceptedCase = acceptedCase;
	}
    
}
