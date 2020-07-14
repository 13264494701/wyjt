package com.jxf.rc.entity;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.jxf.rc.service.impl.RcCaDataServiceImpl;

/**
 * 天机常量
 * @author suhuimin
 * @version 2019-07-31
 */
public class TianjiConstant {
	public static final String tj_app_id = "2010475";//天机appId
	public static final String tj_gateway_url = "https://openapi.rong360.com/gateway?logid=";//天机网关地址
	public static final String tj_method_crawl = "tianji.api.tianjireport.collectuser";//天机抓取数据方法接口名
	public static final String tj_method_getData_shebao = "wd.api.insure.getDataV2";//社保获取原始数据接口名
	public static final String tj_method_getData_gjj = "wd.api.fund.getDataV2";//公积金获取原始数据接口名
	public static final String tj_method_getData_xuexin = "wd.api.chsi.getData";//学信网获取原始数据接口名
	public static final String tj_method_getData_yys = "wd.api.mobilephone.getdatav2";//运营商获取原始数据接口名
	public static final String tj_method_getData_jingdong = "wd.api.jd.getDataV2";//京东获取原始数据接口名
	public static final String tj_method_getData_wangyin = "wd.api.ibank.getdata";//网银获取原始数据接口名
	public static final String tj_method_getData_alipay = "wd.api.alipay.getDataBySessionV3";//支付宝获取原始数据接口名
	public static final String tj_method_getData_taobao = "wd.api.taobao.getDataV4";//淘宝获取原始数据接口名
	public static final String tj_method_getData_zhima = "wd.api.taobao.getZhimaData";//芝麻分获取原始数据接口名
	public static final String tj_method_detail = "tianji.api.tianjireport.detail";//获取数据报告详情接口名
	public static final String tj_private_key = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALrZigeh+hT2zKuh" + 
			"QABU3mYs1HmTFjkGKQwg6fuGqOVRIGG9mrm7racigk6g7eWPljRaxk6wBDSjF2kv" + 
			"x9F5IAHBFMwprw41vJvqq4WKsKbJZ3iayQu/XnTyN9w6nrl+LelwWn7KkEs7/NlG" + 
			"qWU0KuL/J6g5ds5AlXNkZ1UCLYVJAgMBAAECgYBPUbvrw5IzRhXaoihWO7DxtQQ5" + 
			"2/vqFzhjorFRine2aXsSovfE8uwj8F1a3aw/8HV2Ijbe/o+6fKWajKMJxJ6WBcOJ" + 
			"Z6TOb9IBOqNSE05jt6/GpGd3hLLBsekYBQJD3uEMm4BJbVc378JDf0VhdYQkMYpW" + 
			"apQ8yfwIcI+Q3NVkdQJBAO0JNWJTzfDRo8tWnCDWDY7jEdu3Gmq+4fQ1FOi+rLf/" + 
			"ytYI11lLcO8397kGBf2hJ+aYA1C7rQ1tM0aDBYpfabsCQQDJzHRmS4+6qu8sJQKs" + 
			"8GgEbNeOrWyanjVuvhtpveKZ/DbfLRZV2eiB6ogw2Xr1Ad7ktsUnDqGuThMddZUq" + 
			"xCrLAkEA1mQZ0NGT11kGGQhBNsHkiXR/1oaKcjosDMFmJA0yE0pcn7JndPBzOSsZ" + 
			"+FOmX3y/piWf+gmFwDsgwYoIHNunuQJBAJwkE6iXLMX9vwQEOxa/CWzrHrrqG09j" + 
			"vOpMNH/UCgKlsXtrso2JByuG4gwSKt5qrSGA/flji9yH0qiHs3ycvpMCQFq+6C2S" + 
			"y/ilv9aAfzcDX0od5NpebAf+9VI8B8bOB6n3/jcUpdIzoZmcrxKIM/DoPIpqt3ws" + 
			"HIm8sYAXw6IWPK0=";
	/**
     * 从文件中获取私钥
     * @return String 返回文件中的私钥字符串
     */
    public static String getPrivateKey() {
		try {
			String path = RcCaDataServiceImpl.class.getClassLoader().getResource("private_key.pem").getPath();
			FileReader fileReader = new FileReader(path);
			BufferedReader br = new BufferedReader(fileReader);
			String tempStr = "";
			String privateKey = "";
			int i=0;
			while((tempStr = br.readLine()) != null) {
				if(i==0 || i==15) {
					if(tempStr.contains("BEGIN PRIVATE KEY") || tempStr.contains("END PRIVATE KEY")) {
						i++;
						continue;
					}else {
						privateKey = privateKey + tempStr + "\n";
					}
				}else{
					privateKey = privateKey + tempStr + "\n";
					i++;
				}
			}
			br.close();
			fileReader.close();
			return privateKey;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}