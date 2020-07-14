package com.jxf.rc.utils;

import com.jxf.svc.config.Global;

public class ThirdPartyUtils {
	
	// 数据魔盒
	public static final String xxurl =      "https://api.shujumohe.com/octopus/task.unify.create/v3?";// 创建学信抓取地址
	public static final String xuexinwangQueryUrl = "https://api.shujumohe.com/octopus/task.unify.query/v3?";// 查询学信抓取地址
	public static final String xxdengluUrl = "https://api.shujumohe.com/octopus/chsi.unify.acquire/v3?";// 登录学信抓取地址
	public static final String shebaoQueryUrl = "https://api.shujumohe.com/octopus/task.unify.query/v3?";// 查询社保抓取地址
	public static final String yunyingshangQueryUrl = "https://api.shujumohe.com/octopus/task.unify.query/v4?";// 查询运营商抓取地址(原始的)
	public static final String taobaoQueryUrl = "https://api.shujumohe.com/octopus/task.unify.query/v3?";// 查询淘宝抓取地址
	public static final String wangyinQueryUrl = "https://api.shujumohe.com/octopus/task.unify.query/v3?";// 查询网银
	public static final String yunyingshangReportQueryUrl = "https://api.shujumohe.com/octopus/report.task.query/v2?";// 查询运营商抓取地址(报告)
	
	public static final String jingdongQueryUrl = "https://api.shujumohe.com/octopus/task.unify.query/v3?";// 查询京东抓取地址
	public static final String yychannel_type = "YYS"; // 渠道类型。
	public static final String yychannel_code = "100000"; // 渠道类型。
	public static final String sbchannel_type = "SHE_BAO"; // 渠道类型。
	public static final String gjchannel_type = "GJJ"; // 渠道类型。
	public static final String login_type = "1"; // 登陆类型
	public static final String partner_code = "wyjt_mohe"; // 账户号     yxb_mohe 
	public static final String channel_type = "CHSI"; // 渠道类型。
	public static final String channel_code = "000000";
	public static final String TBchannel_code = "005003";
	public static final String partner_key = "6b042168ff774605bd5d3d154b40faaa"; // 秘钥  a184fc5f780a4b0d9ae50fec5856d462 

	
	
	
	// 获取芝麻分token（公信宝）
	public static final String authItem = "sesame_multiple"; // 授权项
	public static final String appId = "gxb33424a60a76ef301";  //gxb53a6464199af1398
	public static final String appSecret = "fa0b76b2543d4d0fba3b0a4f2132efa3";  //544af531b45e4ea59a3e1c5d96ca6fda
	public static final String url = "https://prod.gxb.io/crawler/auth/v2/get_auth_token";
	
	// 新颜雷达报告
	public static final long serialVersionUID = 4865269178052033560L;
	public static final String versions = "1.3.0";
	public static final String member_id = "8150715517"; // 商户号
	public static final String terminal_id = "8150715517"; //
	public static final String xinyanurl = "https://api.xinyan.com/product/radar/v3/report";
	public static final String pfxpwd = "youxinbao365";
	public static final String pfxpath = Global.getConfig("xinyan.cert"); /// data/friendcredit/
	
	// 获取优放贷芝麻分认证token（公信宝）
	public static final String appId4UfangDebt = "gxb9d234868b12da891"; 
	public static final String appSecret4UfangDebt = "13fee235effd4577a1ab8c2c4b974231";
	
	//公信宝
	public static final String yunyingshangReportHtmlUrl = "https://prod.gxb.io/datalist.html?";// 获取运营商报告html地址
	public static final String yunyingshangReportCrawlUrl = "https://prod.gxb.io/crawler/data/report";// 拉取运营商报告数据地址
	
	/** h5*/
	public static final Integer AUTHETYPEH5 = 1;
	/** sdk*/
	public static final Integer AUTHETYPESDK = 2;

	
	
}
