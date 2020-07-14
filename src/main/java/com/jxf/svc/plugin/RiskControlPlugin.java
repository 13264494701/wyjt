package com.jxf.svc.plugin;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 风控插件
 *
 * @author Administrator
 */
public abstract class RiskControlPlugin extends Plugin {

    /**
     * 风控数据服务商属性名称
     */
    public static final String VENDOR_ATTRIBUTE_NAME = "vendor";
    /**
     * 风控项目属性名称
     */
    public static final String PROJECT_ATTRIBUTE_NAME = "project";
    /**
     * 说明属性名称
     */
    public static final String DESCRIPTION_ATTRIBUTE_NAME = "description";

    /**
     * 请求方法
     */
    public enum RequestMethod {

        /**
         * POST
         */
        post,

        /**
         * GET
         */
        get
    }

    /**
     * 通知方法
     */
    public enum NotifyMethod {

        /**
         * 通用
         */
        general,

        /**
         * 同步
         */
        sync,

        /**
         * 异步
         */
        async
    }

    @Autowired
    private PluginConfigService pluginConfigService;

    /**
     * 获取ID
     *
     * @return ID
     */
    @Override
    public String getId() {
        return getClass().getAnnotation(Component.class).value();
    }

    /**
     * 获取名称
     *
     * @return 名称
     */
    @Override
    public abstract String getName();

    /**
     * 获取版本
     *
     * @return 版本
     */
    @Override
    public abstract String getVersion();

    /**
     * 获取作者
     *
     * @return 作者
     */
    public abstract String getAuthor();

    /**
     * 获取网址
     *
     * @return 网址
     */
    public abstract String getSiteUrl();

    /**
     * 获取安装URL
     *
     * @return 安装URL
     */
    public abstract String getInstallUrl();

    /**
     * 获取卸载URL
     *
     * @return 卸载URL
     */
    public abstract String getUninstallUrl();

    /**
     * 获取设置URL
     *
     * @return 设置URL
     */
    public abstract String getSettingUrl();


    /**
     * 获取插件配置
     *
     * @return 插件配置
     */
    public PluginConfig getShopPluginConfig() {
        return pluginConfigService.findByPluginId(getId());
    }

    /**
     * 获取是否已启用
     *
     * @return 是否已启用
     */
    @Override
    public boolean getIsEnabled() {
        PluginConfig pluginConfig = getShopPluginConfig();
        return pluginConfig != null ? pluginConfig.getIsEnabled() : false;
    }

    /**
     * 获取属性值
     *
     * @param name 属性名称
     * @return 属性值
     */
    @Override
    public String getAttribute(String name) {
        PluginConfig pluginConfig = getShopPluginConfig();
        return pluginConfig != null ? pluginConfig.getAttribute(name) : null;
    }

    /**
     * 获取排序
     *
     * @return 排序
     */
    @Override
    public Integer getOrder() {
        PluginConfig pluginConfig = getShopPluginConfig();
        return pluginConfig != null ? pluginConfig.getDisplayOrder() : null;
    }

    /**
     * 获取风控数据服务商
     *
     * @return 风控数据服务商
     */
    public String getPaymentName() {
        PluginConfig pluginConfig = getShopPluginConfig();
        return pluginConfig != null ? pluginConfig.getAttribute(VENDOR_ATTRIBUTE_NAME) : null;
    }

    /**
     * 获取风控项目
     *
     * @return 风控项目
     */
    public String getFeeType() {
        PluginConfig pluginConfig = getShopPluginConfig();
        return pluginConfig != null ? pluginConfig.getAttribute(PROJECT_ATTRIBUTE_NAME) : null;
    }

    /**
     * 获取说明
     *
     * @return 说明
     */
    public String getDescription() {
        PluginConfig pluginConfig = getShopPluginConfig();
        return pluginConfig != null ? pluginConfig.getAttribute(DESCRIPTION_ATTRIBUTE_NAME) : null;
    }

    /**
     * 获取请求URL
     *
     * @return 请求URL
     */
    public abstract String getRequestUrl();

    /**
     * 获取请求方法
     *
     * @return 请求方法
     */
    public abstract RequestMethod getRequestMethod();

    /**
     * 获取请求字符编码
     *
     * @return 请求字符编码
     */
    public abstract String getRequestCharset();

    /**
     * 获取请求参数
     *
     * @param sn          编号
     * @param description 描述
     * @param request     HttpServletRequest
     * @return 请求参数
     */
    public abstract Map<String, Object> getParameterMap(String sn, String description, HttpServletRequest request);

    /**
     * 验证通知是否合法
     *
     * @param notifyMethod 通知方法
     * @param request      HttpServletRequest
     * @return 通知是否合法
     */
    public abstract boolean verifyNotify(RiskControlPlugin.NotifyMethod notifyMethod, HttpServletRequest request);

    /**
     * 获取编号
     *
     * @param request HttpServletRequest
     * @return 编号
     */
    public abstract String getSn(HttpServletRequest request);

    /**
     * 获取通知返回消息
     *
     * @param notifyMethod 通知方法
     * @param request      HttpServletRequest
     * @return 通知返回消息
     */
    public abstract String getNotifyMessage(RiskControlPlugin.NotifyMethod notifyMethod, HttpServletRequest request);


    /**
     * 连接Map键值对
     *
     * @param map              Map
     * @param prefix           前缀
     * @param suffix           后缀
     * @param separator        连接符
     * @param ignoreEmptyValue 忽略空值
     * @param ignoreKeys       忽略Key
     * @return 字符串
     */
    protected String joinKeyValue(Map<String, Object> map, String prefix, String suffix, String separator, boolean ignoreEmptyValue, String... ignoreKeys) {
        List<String> list = new ArrayList<String>();
        if (map != null) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                String value = ConvertUtils.convert(entry.getValue());
                if (StringUtils.isNotEmpty(key) && !ArrayUtils.contains(ignoreKeys, key) && (!ignoreEmptyValue || StringUtils.isNotEmpty(value))) {
                    list.add(key + "=" + (value != null ? value : ""));
                }
            }
        }
        return (prefix != null ? prefix : "") + StringUtils.join(list, separator) + (suffix != null ? suffix : "");
    }

    /**
     * 连接Map值
     *
     * @param map              Map
     * @param prefix           前缀
     * @param suffix           后缀
     * @param separator        连接符
     * @param ignoreEmptyValue 忽略空值
     * @param ignoreKeys       忽略Key
     * @return 字符串
     */
    protected String joinValue(Map<String, Object> map, String prefix, String suffix, String separator, boolean ignoreEmptyValue, String... ignoreKeys) {
        List<String> list = new ArrayList<String>();
        if (map != null) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                String value = ConvertUtils.convert(entry.getValue());
                if (StringUtils.isNotEmpty(key) && !ArrayUtils.contains(ignoreKeys, key) && (!ignoreEmptyValue || StringUtils.isNotEmpty(value))) {
                    list.add(value != null ? value : "");
                }
            }
        }
        return (prefix != null ? prefix : "") + StringUtils.join(list, separator) + (suffix != null ? suffix : "");
    }

    /**
     * 重写equals方法
     *
     * @param obj 对象
     * @return 是否相等
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        PaymentPlugin other = (PaymentPlugin) obj;
        return getId() == other.getId();
    }

    /**
     * 重写hashCode方法
     *
     * @return HashCode
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getId()).toHashCode();
    }

    /**
     * 实现compareTo方法
     *
     * @param paymentPlugin 支付插件
     * @return 比较结果
     */
    public int compareTo(RiskControlPlugin riskControlPlugin) {
        if (riskControlPlugin == null) {
            return 1;
        }
        return new CompareToBuilder().append(getOrder(), riskControlPlugin.getOrder()).append(getId(), riskControlPlugin.getId()).toComparison();
    }

    @Override
    public Plugin.Type getPluginType() {
        return Type.riskControl;
    }

    @Override
    public List<PluginConfigAttr> getPluginConfigAttrs() {
        pluginConfigAttrs = new ArrayList<PluginConfigAttr>();
        PluginConfigAttr vendor = new PluginConfigAttr(VENDOR_ATTRIBUTE_NAME, "风控数据服务商", true, 10);
        pluginConfigAttrs.add(vendor);
        PluginConfigAttr project = new PluginConfigAttr(PROJECT_ATTRIBUTE_NAME, "风控项目", true, 20);
        pluginConfigAttrs.add(project);
        PluginConfigAttr description = new PluginConfigAttr(DESCRIPTION_ATTRIBUTE_NAME, "描述", false, 30, PluginConfigAttr.ShowType.TEXTAREA);
        pluginConfigAttrs.add(description);
        PluginConfig pluginConfig = pluginConfigService.findByPluginId(getId());
        Map<String, String> attributesMap = pluginConfig.getAttributeMap();
        for (PluginConfigAttr pluginConfigAttr : pluginConfigAttrs) {
            pluginConfigAttr.setValue(attributesMap.get(pluginConfigAttr.getField()));
        }
        return pluginConfigAttrs;
    }

}