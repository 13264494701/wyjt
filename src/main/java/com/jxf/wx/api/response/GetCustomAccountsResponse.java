package com.jxf.wx.api.response;

import com.alibaba.fastjson.annotation.JSONField;
import com.jxf.wx.api.entity.CustomAccount;

import java.util.List;
/***
 * 
 * @类功能说明： 
 * @类修改者： 
 * @修改日期： 
 * @修改说明： 
 * @公司名称：北京金信服科技有限公司
 * @作者：Administrator 
 * @创建时间：2016年7月12日 下午6:04:19 
 * @版本：V1.0
 */
public class GetCustomAccountsResponse extends BaseResponse {


	private static final long serialVersionUID = 1L;
	
	
	@JSONField(name = "kf_list")
    private List<CustomAccount> customAccountList;

    public List<CustomAccount> getCustomAccountList() {
        return customAccountList;
    }

    public void setCustomAccountList(List<CustomAccount> customAccountList) {
        this.customAccountList = customAccountList;
    }
}
