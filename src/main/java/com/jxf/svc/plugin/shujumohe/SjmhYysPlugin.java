package com.jxf.svc.plugin.shujumohe;

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
 * 数据魔盒运营商插件
 *
 * @author Administrator
 */
@Component("SjmhYysPlugin")
public class SjmhYysPlugin extends RiskControlPlugin {

//    private static final String PARTNER_CODE = "yxb_mohe";
//    private static final String PARTNER_KEY = "a184fc5f780a4b0d9ae50fec5856d462";
//    private static final String YYS_URL = "https://api.shujumohe.com/octopus/task.unify.query/v3";
//    private static final String YYS_TOKEN_URL = "https://report.shujumohe.com/report/getToken?partner_code=" + PARTNER_CODE + "&partner_key=" + PARTNER_KEY;

    public static final String PARTNER_CODE_NAME = "code";
    public static final String PARTNER_KEY_NAME = "key";
    public static final String YYS_URL_NAME = "url";
    public static final String YYS_TOKEN_URL_NAME = "tokenurl";
    public static final String H5_URL_NAME = "h5url";
    public static final String REPORT_URL_NAME = "reportUrl";

    @Override
    public String getName() {
        return "数据魔盒运营商";
    }

    @Override
    public String getVersion() {
        return "v3";
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

//    //获取运营商报告
//    public String getYys(String task_id) {
//        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
//        multiValueMap.add("partner_code", PARTNER_CODE);
//        multiValueMap.add("partner_key", PARTNER_KEY);
//        multiValueMap.add("task_id", task_id);
//        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(multiValueMap, httpHeaders);
//        String responseEntityBody = restTemplate.exchange(YYS_URL, HttpMethod.POST, requestEntity, String.class).getBody();
//        return responseEntityBody;
//    }

    @Override
    public List<PluginConfigAttr> getPluginConfigAttrs() {
        pluginConfigAttrs = new ArrayList<>();
        pluginConfigAttrs = super.getPluginConfigAttrs();

        PluginConfigAttr code = new PluginConfigAttr(PARTNER_CODE_NAME, "商户号", true, 100);
        PluginConfigAttr key = new PluginConfigAttr(PARTNER_KEY_NAME, "密钥", true, 200, PluginConfigAttr.ShowType.PASSWORD);
        PluginConfigAttr url = new PluginConfigAttr(YYS_URL_NAME, "查询报告 url", true, 300);
        PluginConfigAttr tokenUrl = new PluginConfigAttr(YYS_TOKEN_URL_NAME, "运营商报告免密token url", true, 400);
        PluginConfigAttr h5url = new PluginConfigAttr(H5_URL_NAME, "H5 url", true, 500);
        PluginConfigAttr reportUrl = new PluginConfigAttr(REPORT_URL_NAME, "查看报告 url", true, 600);

        pluginConfigAttrs.add(code);
        pluginConfigAttrs.add(key);
        pluginConfigAttrs.add(url);
        pluginConfigAttrs.add(tokenUrl);
        pluginConfigAttrs.add(h5url);
        pluginConfigAttrs.add(reportUrl);
        Collections.sort(pluginConfigAttrs);

        PluginConfig pluginConfig = getPluginConfig();

        Map<String, String> map = pluginConfig.getAttributeMap();
        for (PluginConfigAttr pluginConfigAttr : pluginConfigAttrs) {
            pluginConfigAttr.setValue(map.get(pluginConfigAttr.getField()));
        }
        return pluginConfigAttrs;
    }

}