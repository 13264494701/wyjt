package com.jxf.web.wap;


import java.math.BigDecimal;
import java.util.HashMap;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.RequestMapping;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;


import com.jxf.pay.utils.HttpPoster;
import com.jxf.pwithdraw.entity.QueryPaymentRequestBean;
import com.jxf.pwithdraw.service.FuiouWithdrawService;
import com.jxf.pwithdraw.utils.SignUtil;

import com.jxf.svc.plugin.PluginConfig;
import com.jxf.svc.plugin.fuyouPayment.FyPaymentPlugin;
import com.jxf.svc.plugin.lianlianPayment.LianlianPaymentPlugin;


import com.jxf.svc.utils.StringUtils;
import com.jxf.svc.utils.XMLParser;

import com.jxf.svc.utils.Exceptions;
import com.jxf.svc.utils.HttpUtils;
import com.jxf.svc.utils.MD5Code;


/**
 * 财务管理Controller
 * @author wo
 * @version 2018-12-20
 */
@Controller
@RequestMapping(value = "${wapPath}/account")
public class WapAccountController extends BaseController {

	@Autowired
	private LianlianPaymentPlugin lianlianPaymentPlugin;
	@Autowired
	private FyPaymentPlugin fyPaymentPlugin;
	@Autowired
	private FuiouWithdrawService fuiouWithdrawService;

	/**
	 * 查询第三方渠道商户账户余额详情
	 * @return
	 */
    @RequestMapping(value = "queryThirdAct")
	public String queryMerchantAccountDetail(HttpServletRequest request,Model model) {

		//富友充值账户详情
		PluginConfig config = fyPaymentPlugin.getPluginConfig();
		Map<String,String> configAttr = config.getAttributeMap();
		String MCHNTCD=	configAttr.get(FyPaymentPlugin.PAYMENT_MCHNTCD_ATTRIBUTE_NAME);
		String mchntkey = configAttr.get(FyPaymentPlugin.PAYMENT_MCTKEY_REQUEST_URL_ATTRIBUTE_NAME);
		String md5Source ="1.0|"+ MCHNTCD+"|"+ mchntkey;
		String dataStr = "";
		try {
		   dataStr = MD5Code.MD5Encode(md5Source, "UTF-8");
			 String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><qryacnt><ver>1.0</ver><merid>" + MCHNTCD + "</merid><mac>" + dataStr + "</mac></qryacnt>";
			 String url = configAttr.get(FyPaymentPlugin.PAYMENT_QUERYTRADERACCOUNT_REQUEST_URL_ATTRIBUTE_NAME);
			 Map<String, String> map = new HashMap<String,String>();
			 map.put("xml", xml);
			 String outStr = new HttpPoster(url).postStr(map);
			 logger.info("查询富友账户返回信息[{}]",outStr);
			 if(StringUtils.isEmpty(outStr)){  
		          logger.error("xml为空：");  
		     } 
			Map<String, String> result = new HashMap<String, String>();
		 
			result = XMLParser.readStringXmlOut(outStr);
		    String ret = result.get("ret");
		    String memo = result.get("memo");
		    String ctamtStr = result.get("ctamt");
		    String caamtStr = result.get("caamt");
		    String cuamtStr = result.get("cuamt");
		    String cfamtStr = result.get("cfamt");
		    String mac = result.get("mac");
			
		    String md5SourceRe = MCHNTCD+"|"+ret+"|"+memo+"|"+ctamtStr+"|"+caamtStr+"|"+cuamtStr+"|"+cfamtStr+"|"+mchntkey;
		    String encode = MD5Code.MD5Encode(md5SourceRe, "UTF-8");
		    if(!StringUtils.equals(encode, mac)){
		      logger.error("查询富友账户详情，校验签名失败");
		      model.addAttribute("fyfailed", true);
		      return "wap/account/queryThirdAct";
		    }
		    BigDecimal caamt = new BigDecimal(caamtStr).divide(new BigDecimal(100));
		    BigDecimal ctamt = new BigDecimal(ctamtStr).divide(new BigDecimal(100));
		    BigDecimal cuamt = new BigDecimal(cuamtStr).divide(new BigDecimal(100));
		    BigDecimal cfamt = new BigDecimal(cfamtStr).divide(new BigDecimal(100));

		    model.addAttribute("caamt", caamt);
		    model.addAttribute("ctamt", ctamt);
		    model.addAttribute("cuamt", cuamt);
		    model.addAttribute("cfamt", cfamt);
		    
		    //富友代付账户详情
		    Map<String, String> queryResultMap = fuiouWithdrawService.queryAccount();
		    if(queryResultMap == null) {
		    	model.addAttribute("fytxfailed", true);
		    }else {
		    	BigDecimal txctamt = new BigDecimal(queryResultMap.get("ctamt")).divide(new BigDecimal(100));
		    	BigDecimal txcaamt = new BigDecimal(queryResultMap.get("caamt")).divide(new BigDecimal(100));
			    BigDecimal txcuamt = new BigDecimal(queryResultMap.get("cuamt")).divide(new BigDecimal(100));
			    BigDecimal txcfamt = new BigDecimal(queryResultMap.get("cfamt")).divide(new BigDecimal(100));
			    model.addAttribute("txctamt", txctamt);
			    model.addAttribute("txcaamt", txcaamt);
			    model.addAttribute("txcuamt", txcuamt);
			    model.addAttribute("txcfamt", txcfamt);
		    }
		    
		    //连连账户详情
		    QueryPaymentRequestBean queryRequestBean = new QueryPaymentRequestBean();
			PluginConfig lianlianConfig = lianlianPaymentPlugin.getPluginConfig();
			Map<String,String> lianlianConfigAttr = lianlianConfig.getAttributeMap();
			String oid_partner = lianlianConfigAttr.get(LianlianPaymentPlugin.PAYMENT_OID_PARTNER_ATTRIBUTE_NAME);
			String version = lianlianConfigAttr.get(LianlianPaymentPlugin.PAYMENT_API_VERSION_ATTRIBUTE_NAME);
			String signType = lianlianConfigAttr.get(LianlianPaymentPlugin.PAYMENT_SIGN_TYPE_ATTRIBUTE_NAME);
			String key = lianlianConfigAttr.get(LianlianPaymentPlugin.PAYMENT_BUSINESS_PRIVATE_KEY_ATTRIBUTE_NAME);
			String queryUrl = lianlianConfigAttr.get(LianlianPaymentPlugin.PAYMENT_QUERYTRADERACCOUNT_ATTRIBUTE_NAME);
			
			queryRequestBean.setOid_partner(oid_partner);
			queryRequestBean.setApi_version(version);
			queryRequestBean.setSign_type(signType);
			queryRequestBean.setSign(SignUtil.genRSASign(JSONObject.parseObject(JSON.toJSONString(queryRequestBean)),key));
			JSONObject json = JSON.parseObject(JSON.toJSONString(queryRequestBean));
			String queryResult = HttpUtils.doPost(queryUrl, json.toJSONString());
			logger.info("查询连连账户返回信息[{}]",queryResult);
			@SuppressWarnings("unchecked")
			Map<String,String> map_1  = (Map<String, String>) JSON.parse(queryResult);
			String lianlianBalance = map_1.get("amt_balance");
			if(StringUtils.isBlank(lianlianBalance) || StringUtils.equals(lianlianBalance, "null")) {
				 model.addAttribute("llfailed", true);
				 return "wap/account/queryThirdAct";
			}
			model.addAttribute("lianlianBal", lianlianBalance);
		  } catch (Exception e) {
			model.addAttribute("failed", true);
		    logger.error(Exceptions.getStackTraceAsString(e));
		  }
			
		return "wap/account/queryThirdAct";
	}
}