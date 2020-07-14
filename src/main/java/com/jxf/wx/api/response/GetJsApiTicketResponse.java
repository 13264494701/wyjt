package com.jxf.wx.api.response;

import com.alibaba.fastjson.annotation.JSONField;

/***
 * 
 * @类功能说明： 
 * @类修改者： 
 * @修改日期： 
 * @修改说明： 
 * @公司名称：北京金信服科技有限公司
 * @作者：Administrator 
 * @创建时间：2016年11月11日 下午4:04:58 
 * @版本：V1.0
 */
public class GetJsApiTicketResponse extends BaseResponse {

    /** 
	 * @Fields serialVersionUID : TODO 
	 */ 
	private static final long serialVersionUID = 1L;

	private String ticket;

    @JSONField(name = "expires_in")
    private Integer expiresIn;

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public Integer getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
    }
}
