package com.jxf.loan.signature.junziqian;

import com.junziqian.api.JunziqianClient;

import org.ebaoquan.rop.thirdparty.com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by wlinguo on 5/11/2016.
 */
public class JunziqianClientInit {
	// 请填入服务地址（根据环境的不同选择不同的服务地址），沙箱环境，正式环境
	
//	public static final String SERVICE_URL = "http://sandbox.api.junziqian.com/services";;
	public static final String SERVICE_URL = "http://api.junziqian.com/services";

	// 请填入你的APPKey
//	public static final String APP_KEY = "a21b8a408805ec0a";
	public static final String APP_KEY = "7ebf486f57a31cf9";

	// 请填入你的APPSecret
//	public static final String APP_SECRET = "0af80e7aa21b8a408805ec0ac54f4aca";
	public static final String APP_SECRET = "e39db8b87ebf486f57a31cf94d1fe8bb";

	protected static Map<String, String> props = Maps.newHashMap();

	private static JunziqianClient client;

	static {
		client = new JunziqianClient(SERVICE_URL, APP_KEY, APP_SECRET);
	}

	public static JunziqianClient getClient() {
		return client;
	}
}
