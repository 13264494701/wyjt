package com.jxf.wx.api.entity;

import com.alibaba.fastjson.annotation.JSONField;

/***
 * 
 * @类功能说明： 累计用户数据
 * @类修改者： 
 * @修改日期： 
 * @修改说明： 
 * @公司名称：北京金信服科技有限公司
 * @作者：Administrator 
 * @创建时间：2016年11月11日 下午7:44:12 
 * @版本：V1.0
 */
public class UserCumulate extends BaseDataCube {

	private static final long serialVersionUID = 1L;
	
	@JSONField(name = "cumulate_user")
    private Integer cumulateUser;

    public Integer getCumulateUser() {
        return cumulateUser;
    }

    public void setCumulateUser(Integer cumulateUser) {
        this.cumulateUser = cumulateUser;
    }
}
