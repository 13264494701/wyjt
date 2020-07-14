package com.jxf.svc.utils;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.ResourceAccessException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jxf.svc.config.Global;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Subdivision;

/**
 * IP工具类
 * 
 * @author jxf
 * @version 1.0 2015-07-10
 */
public class AreaUtils{
	
	private static final Logger log = LoggerFactory.getLogger(AreaUtils.class);
	
	/***
	 * 通过联网IP
	 * @param ip
	 * @return
	 * @throws IOException 
	 * @throws GeoIp2Exception 
	 */
	public static String ipToArea(String ip) throws IOException, GeoIp2Exception {
	    
		String area = ipToAreaByGeoLite2(ip);
		if(StringUtils.isNotBlank(area)) {
			return area;
		}
        try {
        	Map<String, Object> parameterMap = new HashMap<String, Object>();
        	parameterMap.put("ip", ip);
            String result = WebUtils.get("http://ip.taobao.com/service/getIpInfo.php",parameterMap);
            JSONObject jsonObject = JSON.parseObject(result);
            if(StringUtils.equals(jsonObject.getString("code"), "0")) {
            	JSONObject dataObject = jsonObject.getJSONObject("data");
            	area = dataObject.getString("region")+"-"+dataObject.getString("city");
            	return area;
            }
        
        } catch (ResourceAccessException e) {
        	log.warn(Exceptions.getStackTraceAsString(e));
        }
		return null;

	}
	
	public static String ipToAreaByGeoLite2(String ip) {
		
	     // 创建 GeoLite2 数据库 
        File database = new File(Global.getBaseStaticPath()+"/geoip/GeoLite2-City.mmdb");   
		try {
			// 读取数据库内容 
			DatabaseReader reader = new DatabaseReader.Builder(database).build();
	        InetAddress ipAddress = InetAddress.getByName(ip); 
	        // 获取查询结果 
	           CityResponse response = reader.city(ipAddress); 
	        // 获取国家信息
//	           Country country = response.getCountry();
//	           String countryName = country.getNames().get("zh-CN");
//	           if(StringUtils.isBlank(countryName)) {
//	        	   return null;
//	           }
	           
	        // 获取省份
	           Subdivision subdivision = response.getMostSpecificSubdivision();
	           String subdivisionName = subdivision.getNames().get("zh-CN");
	           if(StringUtils.isBlank(subdivisionName)) {
	        	   return null;
	           }
	        // 获取城市
	           City city = response.getCity();
	           String cityName = city.getNames().get("zh-CN");
	           if(StringUtils.isBlank(cityName)) {
	        	   return null;
	           }
	           
	           return subdivisionName+"-"+cityName;
	           
		} catch (IOException | GeoIp2Exception e) {
			log.error(Exceptions.getStackTraceAsString(e));
		} 
        return null;
	}
	
	public static String getAreaByPhoneNo(String phoneNo ) {
			try {
				CloseableHttpClient httpClient = HttpClients.createDefault();
				HttpGet get = new HttpGet("http://mobsec-dianhua.baidu.com/dianhua_api/open/location?tel="+phoneNo);
				RequestConfig config = RequestConfig.custom()
				        .setConnectTimeout(10000).setConnectionRequestTimeout(2000)
				        .setSocketTimeout(10000).build();
				get.setConfig(config);
				CloseableHttpResponse rsp = httpClient.execute(get);
				HttpEntity entity = rsp.getEntity();
				String result = EntityUtils.toString(entity);
				JSONObject jsonObject = JSONObject.parseObject(result);
				JSONObject response = jsonObject.getJSONObject("response");
				JSONObject phoneNoObject = response.getJSONObject(phoneNo);
				if(phoneNoObject == null) {
					return null;
				}
				JSONObject detailObject = phoneNoObject.getJSONObject("detail");
				String province = detailObject.getString("province");
				JSONArray areaArray = detailObject.getJSONArray("area");
				JSONObject cityObject = areaArray.getJSONObject(0);
				String city = cityObject.getString("city");
				return province+"-"+city;
			} catch (IOException e) {
				log.error(Exceptions.getStackTraceAsString(e));
			}
			return null;
	   }
	   
    public static void main(String[] args) {
        
    	String area;
		area = getAreaByPhoneNo("15106194580");
		System.out.println(area);

    
    }
}
