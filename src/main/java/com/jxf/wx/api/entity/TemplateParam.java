package com.jxf.wx.api.entity;

import com.jxf.svc.sys.crud.entity.CrudEntity;

/***
 * 
 * @类功能说明：模版参数 
 * @类修改者： 
 * @修改日期： 
 * @修改说明： 
 * @公司名称：北京金信服科技有限公司
 * @作者：Administrator 
 * @创建时间：2016年11月11日 下午5:30:56 
 * @版本：V1.0
 */
public class TemplateParam extends CrudEntity<TemplateParam> {
 
	private static final long serialVersionUID = 1L;
	/**
     * 值
     */
    private String value;
    /**
     * 颜色
     */
    private String color;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
