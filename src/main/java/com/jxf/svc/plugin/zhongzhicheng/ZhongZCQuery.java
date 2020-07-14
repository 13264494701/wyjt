package com.jxf.svc.plugin.zhongzhicheng;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


import org.springframework.stereotype.Component;

import com.jxf.svc.plugin.PluginConfig;
import com.jxf.svc.plugin.PluginConfigAttr;
import com.jxf.svc.plugin.RiskControlPlugin;

/** 
 * @类功能说明： 中智诚插件
 * @类修改者： 
 * @修改日期： 
 * @修改说明： 
 * @公司名称：北京金信服科技有限公司
 * @作者：suhuimin 
 * @创建时间：2018年12月11日 下午3:40:28 
 * @版本：V1.0 
 */
@Component("zhongZCQuery")
public class ZhongZCQuery extends RiskControlPlugin {
	public final static String LOAN_TYPE="loanType"; //借款定义 小额贷款2
	public final static String DEFAULT_USER = "userName";
	public final static String DEFAULT_PASS = "passWord";
	public final static String SHENYUE_API_URL = "shenyueurl";
	public final static String HAORI_API_URL = "haoriurl";
	public final static String WEICHENG_API_URL = "weichengurl";
	public final static String LOAN_PURPOSE = "purpose";   //借款用途
	public final static String LOAN_TERM  = "term";     //  借款周期
	

	@Override
	public String getName() {
		return "中智诚";
	}

	@Override
	public String getVersion() {
		return "2.0";
	}

	@Override
	public String getAuthor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSiteUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getInstallUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUninstallUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSettingUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRequestUrl() {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean verifyNotify(NotifyMethod notifyMethod, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getSn(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getNotifyMessage(NotifyMethod notifyMethod, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
    public List<PluginConfigAttr> getPluginConfigAttrs() {
        pluginConfigAttrs = new ArrayList<>();
        pluginConfigAttrs = super.getPluginConfigAttrs();
        PluginConfigAttr loanType = new PluginConfigAttr(LOAN_TYPE, "公司类型", true, 100);
        PluginConfigAttr userName = new PluginConfigAttr(DEFAULT_USER, "用户名", true, 200);
        PluginConfigAttr passWord = new PluginConfigAttr(DEFAULT_USER, "密码", true, 300, PluginConfigAttr.ShowType.PASSWORD);
        PluginConfigAttr shenyueurl = new PluginConfigAttr(SHENYUE_API_URL, "神月API url", true, 400);
        PluginConfigAttr haoriurl = new PluginConfigAttr(HAORI_API_URL, "昊日API url", true, 500);
        PluginConfigAttr weichengurl = new PluginConfigAttr(WEICHENG_API_URL, "卫城API url", true, 600);
        PluginConfigAttr purpose = new PluginConfigAttr(LOAN_PURPOSE, "贷款用途", true, 700);
        PluginConfigAttr term = new PluginConfigAttr(LOAN_TERM, "贷款周期", true, 700);

        pluginConfigAttrs.add(loanType);
        pluginConfigAttrs.add(userName);
        pluginConfigAttrs.add(passWord);
        pluginConfigAttrs.add(shenyueurl);
        pluginConfigAttrs.add(haoriurl);
        pluginConfigAttrs.add(weichengurl);
        pluginConfigAttrs.add(purpose);
        pluginConfigAttrs.add(term);

        Collections.sort(pluginConfigAttrs);
        PluginConfig pluginConfig = getPluginConfig();

        Map<String, String> map = pluginConfig.getAttributeMap();
        for (PluginConfigAttr pluginConfigAttr : pluginConfigAttrs) {
            pluginConfigAttr.setValue(map.get(pluginConfigAttr.getField()));
        }
        return pluginConfigAttrs;
    }
	
}
