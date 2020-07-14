package com.jxf.wx.api.response;

import com.jxf.wx.api.entity.MenuTemp;

/**
 * 
 * @类功能说明： 
 * @类修改者： 
 * @修改日期： 
 * @修改说明： 
 * @公司名称：北京金信服科技有限公司
 * @作者：Administrator 
 * @创建时间：2016年11月11日 下午9:04:21 
 * @版本：V1.0
 */
public class GetMenuResponse extends BaseResponse {

	private static final long serialVersionUID = 1L;
	
	private MenuTemp menu;

    public MenuTemp getMenu() {
        return menu;
    }

    public void setMenu(MenuTemp menu) {
        this.menu = menu;
    }
}
