package com.jxf.wx.api.response;

import com.jxf.wx.api.entity.UserSummary;

import java.util.List;

/***
 * 
 * @类功能说明： 
 * @类修改者： 
 * @修改日期： 
 * @修改说明： 
 * @公司名称：北京金信服科技有限公司
 * @作者：Administrator 
 * @创建时间：2016年10月9日 下午9:07:16 
 * @版本：V1.0
 */
public class GetUserSummaryResponse extends BaseResponse {

	private static final long serialVersionUID = 1L;
	
	private List<UserSummary> list;

    public List<UserSummary> getList() {
        return list;
    }

    public void setList(List<UserSummary> list) {
        this.list = list;
    }
}
