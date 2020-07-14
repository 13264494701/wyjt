package com.jxf.wx.api.entity;

import com.jxf.svc.sys.crud.entity.CrudEntity;
import com.jxf.wx.api.exception.WeixinException;

import java.util.List;

/**
 * 菜单对象，包含所有菜单按钮
 *
 * @author peiyu
 */
public class MenuTemp extends CrudEntity<MenuTemp> {

	private static final long serialVersionUID = 1L;
	/**
     * 一级菜单列表，最多3个
     */
    private List<MenuButton> button;

    public List<MenuButton> getButton() {
        return button;
    }

    public void setButton(List<MenuButton> button) {
        if (null == button || button.size() > 3) {
            throw new WeixinException("主菜单最多3个");
        }
        this.button = button;
    }
}
