package com.jxf.wx.api.response;

import com.jxf.wx.api.entity.Group;

import java.util.List;

/***
 * 
 * @类功能说明： 新建实体类Group，将id，name，count属性移动到Group实体中。本实体采用List封装Groups信息
 * @类修改者： 
 * @修改日期： 
 * @修改说明： 
 * @公司名称：北京金信服科技有限公司
 * @作者：Administrator 
 * @创建时间：2016年11月11日 下午7:46:51 
 * @版本：V1.0
 */
public class GetGroupsResponse extends BaseResponse {


	private static final long serialVersionUID = 1L;
	
	private List<Group> groups;

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }
}
