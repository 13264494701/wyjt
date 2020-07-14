package com.jxf.wx.api.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.jxf.svc.sys.crud.entity.CrudEntity;

import java.util.Date;

/***
 * 
 * @类功能说明： 数据分析基类
 * @类修改者： 
 * @修改日期： 
 * @修改说明： 
 * @公司名称：北京金信服科技有限公司
 * @作者：Administrator 
 * @创建时间：2016年9月11日 下午5:26:16 
 * @版本：V1.0
 */
public class BaseDataCube extends CrudEntity<BaseDataCube> {

	private static final long serialVersionUID = 1L;
	
	@JSONField(name = "ref_date", format = "yyyy-MM-dd")
    private Date refDate;

    public Date getRefDate() {
        return refDate;
    }

    public void setRefDate(Date refDate) {
        this.refDate = refDate;
    }
}
