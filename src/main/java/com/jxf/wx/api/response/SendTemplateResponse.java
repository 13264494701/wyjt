package com.jxf.wx.api.response;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 
 * @类功能说明： 发送模版消息响应
 * @类修改者： 
 * @修改日期： 
 * @修改说明： 
 * @公司名称：北京金信服科技有限公司
 * @作者：Administrator 
 * @创建时间：2016年11月11日 下午7:50:47 
 * @版本：V1.0
 */
public class SendTemplateResponse extends BaseResponse {


	private static final long serialVersionUID = 1L;
	
	/**
     * 消息id
     */
    @JSONField(name = "msgid")
    private String msgid;

    public String getMsgid() {
        return msgid;
    }

    public void setMsgid(String msgid) {
        this.msgid = msgid;
    }
}
