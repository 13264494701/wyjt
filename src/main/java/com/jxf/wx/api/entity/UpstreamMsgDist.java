package com.jxf.wx.api.entity;

import com.alibaba.fastjson.annotation.JSONField;

/***
 * 
 * @类功能说明： 
 * @类修改者： 
 * @修改日期： 
 * @修改说明： 
 * @公司名称：北京金信服科技有限公司
 * @作者：Administrator 
 * @创建时间：2016年11月11日 下午5:29:14 
 * @版本：V1.0
 */
public class UpstreamMsgDist extends BaseDataCube {

	private static final long serialVersionUID = 1L;
	
	@JSONField(name = "count_interval")
    private Integer countInterval;
    @JSONField(name = "msg_user")
    private Integer msgUser;

    public Integer getCountInterval() {
        return countInterval;
    }

    public void setCountInterval(Integer countInterval) {
        this.countInterval = countInterval;
    }

    public Integer getMsgUser() {
        return msgUser;
    }

    public void setMsgUser(Integer msgUser) {
        this.msgUser = msgUser;
    }
}
