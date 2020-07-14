package com.jxf.wx.api.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.jxf.svc.sys.crud.entity.CrudEntity;
import com.jxf.wx.api.MenuAPI.MenuType;
import com.jxf.wx.api.exception.WeixinException;

import java.util.List;

/***
 * 
 * @类功能说明： 菜单按钮对象
 * @类修改者： 
 * @修改日期： 
 * @修改说明： 
 * @公司名称：北京金信服科技有限公司
 * @作者：Administrator 
 * @创建时间：2016年11月11日 下午5:25:16 
 * @版本：V1.0
 */
public class MenuButton extends CrudEntity<MenuButton> {

	private static final long serialVersionUID = 1L;
	
	/**
     * 菜单类别
     */
    private MenuType type;

    /**
     * 菜单上显示的文字
     */
    private String name;

    /**
     * 菜单key，当MenuType值为CLICK时用于区别菜单
     */
    private String key;

    /**
     * 菜单跳转的URL，当MenuType值为VIEW时用
     */
    private String url;

    /**
     * 菜单显示的永久素材的MaterialID,当MenuType值为media_id和view_limited时必需
     */
    @JSONField(name = "media_id")
    private String mediaId;

    /**
     * 二级菜单列表，每个一级菜单下最多5个
     */
    @JSONField(name = "sub_button")
    private List<MenuButton> subButton;

    public MenuType getType() {
        return type;
    }

    public void setType(MenuType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public List<MenuButton> getSubButton() {
        return subButton;
    }

    public void setSubButton(List<MenuButton> subButton) {
        if (null == subButton || subButton.size() > 5) {
            throw new WeixinException("子菜单最多只有5个");
        }
        this.subButton = subButton;
    }
}
