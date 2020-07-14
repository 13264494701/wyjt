package com.jxf.wx.api.response;

import com.alibaba.fastjson.annotation.JSONField;

/***
 * 
 * @类功能说明： 获取群发消息结果
 * @类修改者： 
 * @修改日期： 
 * @修改说明： 
 * @公司名称：北京金信服科技有限公司
 * @作者：Administrator 
 * @创建时间：2016年11月11日 下午9:04:57 
 * @版本：V1.0
 */
public class GetSendMessageResponse extends BaseResponse {

    /** 
	 * @Fields serialVersionUID : TODO 
	 */ 
	private static final long serialVersionUID = 1L;
	@JSONField(name="msg_id")
    private String msgId;

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }
}
