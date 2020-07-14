package com.jxf.wx.api.response;

import com.jxf.wx.api.entity.InterfaceSummaryHour;

import java.util.List;

/***
 * 
 * @类功能说明： 
 * @类修改者： 
 * @修改日期： 
 * @修改说明： 
 * @公司名称：北京金信服科技有限公司
 * @作者：Administrator 
 * @创建时间：2016年10月18日 下午7:47:18 
 * @版本：V1.0
 */
public class GetInterfaceSummaryHourResponse extends BaseResponse {


	private static final long serialVersionUID = 1L;
	
	private List<InterfaceSummaryHour> list;

    public List<InterfaceSummaryHour> getList() {
        return list;
    }

    public void setList(List<InterfaceSummaryHour> list) {
        this.list = list;
    }
}
