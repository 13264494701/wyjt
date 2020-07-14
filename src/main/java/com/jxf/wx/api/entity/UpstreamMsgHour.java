package com.jxf.wx.api.entity;

import com.alibaba.fastjson.annotation.JSONField;

/***
 * 
 * @类功能说明： 消息分送分时数据
 * @类修改者： 
 * @修改日期： 
 * @修改说明： 
 * @公司名称：北京金信服科技有限公司
 * @作者：Administrator 
 * @创建时间：2016年11月11日 下午7:43:41 
 * @版本：V1.0
 */
public class UpstreamMsgHour extends BaseDataCube {


	private static final long serialVersionUID = 1L;
	
	@JSONField(name = "ref_hour")
    private Integer refHour;
    @JSONField(name = "msg_type")
    private Integer msgType;
    @JSONField(name = "msg_user")
    private Integer msgUser;
    @JSONField(name = "msg_count")
    private Integer msgCount;

    public Integer getRefHour() {
        return refHour;
    }

    public void setRefHour(Integer refHour) {
        this.refHour = refHour;
    }

    public Integer getMsgType() {
        return msgType;
    }

    public void setMsgType(Integer msgType) {
        this.msgType = msgType;
    }

    public Integer getMsgUser() {
        return msgUser;
    }

    public void setMsgUser(Integer msgUser) {
        this.msgUser = msgUser;
    }

    public Integer getMsgCount() {
        return msgCount;
    }

    public void setMsgCount(Integer msgCount) {
        this.msgCount = msgCount;
    }
}
