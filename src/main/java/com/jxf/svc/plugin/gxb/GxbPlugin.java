package com.jxf.svc.plugin.gxb;

import com.jxf.svc.plugin.PluginConfig;
import com.jxf.svc.plugin.PluginConfigAttr;
import com.jxf.svc.plugin.RiskControlPlugin;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 公信宝插件
 *
 * @author Administrator
 */
@Component("GxbPlugin")
public class GxbPlugin extends RiskControlPlugin {

//    private static final String APPID_NAME = "gxba73a492b99519fd0";
//    private static final String APPSECRET_NAME = "dbe4436ca83c474985eeb081a9efb28e";
//    private static final String TOKEN_URL_NAME = "https://prod.gxb.io/crawler/auth/v2/get_auth_token";
//    private static final String SESAME_AUTH_ITEM_NAME = "sesame_multiple";
//    private static final String WECHAT_AUTH_ITEM_NAME = "wechat_phone";

    public static final String APPID_NAME = "appId";
    public static final String APPSECRET_NAME = "appSecret";
    public static final String TOKEN_URL_NAME = "tokenUrl";
    public static final String SESAME_H5_URL_NAME = "sesameUrl";
    public static final String WECHAT_URL_NAME = "wechatUrl";

    @Override
    public String getName() {
        return "公信宝";
    }

    @Override
    public String getVersion() {
        return "v2";
    }

    @Override
    public String getAuthor() {
        return "Administrator";
    }

    @Override
    public String getSiteUrl() {
        return null;
    }

    @Override
    public String getInstallUrl() {
        return null;
    }

    @Override
    public String getUninstallUrl() {
        return null;
    }

    @Override
    public String getSettingUrl() {
        return null;
    }

    @Override
    public String getRequestUrl() {
        return null;
    }

    @Override
    public RequestMethod getRequestMethod() {
        return RequestMethod.post;
    }

    @Override
    public String getRequestCharset() {
        return "utf-8";
    }

    @Override
    public Map<String, Object> getParameterMap(String sn, String description, HttpServletRequest request) {
        return null;
    }

    @Override
    public boolean verifyNotify(RiskControlPlugin.NotifyMethod notifyMethod, HttpServletRequest request) {
        return false;
    }

    @Override
    public String getSn(HttpServletRequest request) {
        return null;
    }

    @Override
    public String getNotifyMessage(RiskControlPlugin.NotifyMethod notifyMethod, HttpServletRequest request) {
        return null;
    }

    @Override
    public List<PluginConfigAttr> getPluginConfigAttrs() {
        pluginConfigAttrs = new ArrayList<>();
        pluginConfigAttrs = super.getPluginConfigAttrs();

        PluginConfigAttr appId = new PluginConfigAttr(APPID_NAME, "商户应用ID", true, 100);
        PluginConfigAttr appSecret = new PluginConfigAttr(APPSECRET_NAME, "密钥", true, 200, PluginConfigAttr.ShowType.PASSWORD);
        PluginConfigAttr tokenUrl = new PluginConfigAttr(TOKEN_URL_NAME, "token url", true, 300);
        PluginConfigAttr sesameUrl = new PluginConfigAttr(SESAME_H5_URL_NAME, "芝麻分H5 url", true, 400);
        PluginConfigAttr wechatUrl = new PluginConfigAttr(WECHAT_URL_NAME, "微信 url", true, 500);

        pluginConfigAttrs.add(appId);
        pluginConfigAttrs.add(appSecret);
        pluginConfigAttrs.add(tokenUrl);
        pluginConfigAttrs.add(sesameUrl);
        pluginConfigAttrs.add(wechatUrl);

        Collections.sort(pluginConfigAttrs);
        PluginConfig pluginConfig = getPluginConfig();

        Map<String, String> map = pluginConfig.getAttributeMap();
        for (PluginConfigAttr pluginConfigAttr : pluginConfigAttrs) {
            pluginConfigAttr.setValue(map.get(pluginConfigAttr.getField()));
        }
        return pluginConfigAttrs;
    }

}