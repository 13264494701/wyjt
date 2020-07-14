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
 * @创建时间：2016年11月11日 下午5:28:26 
 * @版本：V1.0
 */
public class InterfaceSummaryHour extends InterfaceSummary {


	private static final long serialVersionUID = 1L;
	
	@JSONField(name = "ref_hour")
    private Integer refHour;

    public Integer getRefHour() {
        return refHour;
    }

    public void setRefHour(Integer refHour) {
        this.refHour = refHour;
    }
}
