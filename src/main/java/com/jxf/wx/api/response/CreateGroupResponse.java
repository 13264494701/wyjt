package com.jxf.wx.api.response;

import com.jxf.wx.api.entity.Group;

/***
 * 
 * @类功能说明： 
 * @类修改者： 
 * @修改日期： 
 * @修改说明： 
 * @公司名称：北京金信服科技有限公司
 * @作者：Administrator 
 * @创建时间：2016年11月11日 下午5:34:01 
 * @版本：V1.0
 */
public class CreateGroupResponse extends BaseResponse {

	private static final long serialVersionUID = 1L;
	
	private Group group;

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}
	
	
}
