package com.jxf.wx.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.jxf.wx.api.entity.MenuTemp;
import com.jxf.wx.api.response.BaseResponse;
import com.jxf.wx.api.response.BaseResponse.ResultType;
import com.jxf.wx.api.response.GetMenuResponse;
import com.jxf.wx.api.utils.CollectionUtil;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.List;

/**
 * 菜单相关API
 *
 * @author peiyu
 * @since 1.2
 */
public class MenuAPI extends BaseAPI {
	
	public enum MenuType {
	    /**
	     * 点击推事件
	     */
	    CLICK("click"),

	    /**
	     * 跳转URL
	     */
	    VIEW("view"),

	    /*-------------------------以下仅支持微信iPhone5.4.1以上版本，和Android5.4以上版本的微信用户------------------------*/

	    /**
	     * 扫码推事件
	     */
	    SCANCODE_PUSH("scancode_push"),

	    /**
	     * 扫码推事件且弹出“消息接收中”提示框
	     */
	    SCANCODE_WAITMSG("scancode_waitmsg"),

	    /**
	     * 弹出系统拍照发图
	     */
	    PIC_SYSPHOTO("pic_sysphoto"),

	    /**
	     * 弹出拍照或者相册发图
	     */
	    PIC_PHOTO_OR_ALBUM("pic_photo_or_album"),

	    /**
	     * 弹出微信相册发图器
	     */
	    PIC_WEIXIN("pic_weixin"),

	    /**
	     * 弹出地理位置选择器
	     */
	    LOCATION_SELECT("location_select"),


	    /*-----------------------------以下专门给第三方平台旗下未微信认证（具体而言，是资质认证未通过）的订阅号准备的事件类型，它们是没有事件推送的，能力相对受限，其他类型的公众号不必使用--------------------------*/

	    /**
	     * 下发消息（除文本消息）
	     */
	    MEDIA_ID("media_id"),

	    /**
	     * 跳转图文消息URL
	     */
	    VIEW_LIMITED("view_limited");

	    String value;

	    MenuType(String value) {
	        this.value = value;
	    }

	    @Override
	    public String toString() {
	        return this.value;
	    }

	}

	public MenuAPI(String accessToken) {
		super(accessToken);
	}

	private static final Logger LOG = LoggerFactory.getLogger(MenuAPI.class);

//    public MenuAPI(ApiConfig config) {
//        super(config);
//    }

    /**
     * 创建菜单
     *
     * @param menu 菜单对象
     * @return 调用结果
     */
    public ResultType createMenu(MenuTemp menu) {
        Assert.notNull(menu,"");
        LOG.debug("创建菜单.....");
        String url = BASE_API_URL + "cgi-bin/menu/create?access_token=#";
        BaseResponse response = executePost(url, JSON.toJSONString(menu));
        return ResultType.get(response.getErrcode());
    }

    /**
     * 获取所有菜单
     *
     * @return 菜单列表对象
     */
    @SuppressWarnings("rawtypes")
	public GetMenuResponse getMenu() {
        GetMenuResponse response = null;
        LOG.debug("获取菜单信息.....");
        String url = BASE_API_URL + "cgi-bin/menu/get?access_token=#";

        BaseResponse r = executeGet(url);
        if (isSuccess(r.getErrcode())) {
            JSONObject jsonObject = JSON.parseObject(r.getErrmsg());
            //通过jsonpath不断修改type的值，才能正常解析- -
           
			List buttonList = (List) JSONPath.eval(jsonObject, "$.menu.button");
            if (CollectionUtil.isNotEmpty(buttonList)) {
                for (Object button : buttonList) {
                    List subList = (List) JSONPath.eval(button, "$.sub_button");
                    if (CollectionUtil.isNotEmpty(subList)) {
                        for (Object sub : subList) {
                            Object type = JSONPath.eval(sub, "$.type");
                            JSONPath.set(sub, "$.type", type.toString().toUpperCase());
                        }
                    }else{
                        Object type = JSONPath.eval(button, "$.type");
                        JSONPath.set(button, "$.type", type.toString().toUpperCase());
                    }
                }
            }
            response = JSON.parseObject(jsonObject.toJSONString(), GetMenuResponse.class);
        } else {
            response = JSON.parseObject(JSON.toJSONString(r), GetMenuResponse.class);
        }
        return response;
    }

    /**
     * 删除所有菜单
     *
     * @return 调用结果
     */
    public ResultType deleteMenu() {
        LOG.debug("删除菜单.....");
        String url = BASE_API_URL + "cgi-bin/menu/delete?access_token=#";
        BaseResponse response = executeGet(url);
        return ResultType.get(response.getErrcode());
    }
}
