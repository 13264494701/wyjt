package com.jxf.wx.api.response;

import com.jxf.wx.api.entity.InterfaceSummary;

import java.util.List;

/***
 * 
 * @类功能说明： 
 * @类修改者： 
 * @修改日期： 
 * @修改说明： 
 * @公司名称：北京金信服科技有限公司
 * @作者：Administrator 
 * @创建时间：2016年11月17日 下午7:47:43 
 * @版本：V1.0
 */
public class GetInterfaceSummaryResponse extends BaseResponse {

	private static final long serialVersionUID = 1L;
	private List<InterfaceSummary> list;

    public List<InterfaceSummary> getList() {
        return list;
    }

    public void setList(List<InterfaceSummary> list) {
        this.list = list;
    }
}
