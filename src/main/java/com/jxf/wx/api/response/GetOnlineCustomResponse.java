package com.jxf.wx.api.response;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.jxf.wx.api.entity.OnlineCustom;

/***
 * 
 * @类功能说明：获取在线客服应答接口 
 * @类修改者： 
 * @修改日期： 
 * @修改说明： 
 * @公司名称：北京金信服科技有限公司
 * @作者：Administrator 
 * @创建时间：2016年11月11日 下午9:04:40 
 * @版本：V1.0
 */
public class GetOnlineCustomResponse extends BaseResponse{


	private static final long serialVersionUID = 1L;
	
	@JSONField(name = "kf_online_list")
	private List<OnlineCustom> onlineCustomList;

	public List<OnlineCustom> getOnlineCustomList() {
		return onlineCustomList;
	}

	public void setOnlineCustomList(List<OnlineCustom> onlineCustomList) {
		this.onlineCustomList = onlineCustomList;
	}
	
}
