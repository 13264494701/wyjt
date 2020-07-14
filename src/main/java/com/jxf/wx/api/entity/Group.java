package com.jxf.wx.api.entity;

import com.jxf.svc.sys.crud.entity.CrudEntity;

/***
 * 
 * @类功能说明： 分组信息
 * @类修改者： 
 * @修改日期： 
 * @修改说明： 
 * @公司名称：北京金信服科技有限公司
 * @作者：Administrator 
 * @创建时间：2016年11月11日 下午5:22:12 
 * @版本：V1.0
 */
public class Group extends CrudEntity<Group>  {

	private static final long serialVersionUID = 1L;
    private String  name;
    private Integer count;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
