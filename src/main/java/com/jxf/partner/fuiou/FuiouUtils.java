package com.jxf.partner.fuiou;

import java.net.URLEncoder;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxf.svc.model.HandleRsp;
import com.jxf.svc.security.MD5Utils;
import com.jxf.svc.utils.Exceptions;
import com.jxf.svc.utils.HtmlUtils;
import com.jxf.svc.utils.XMLParser;


public class FuiouUtils {

	private static final Logger logger = LoggerFactory.getLogger(FuiouUtils.class);

	public static final String merchant_number = "0001000F0395736"; // 商户号
	public static final String id_cardNo = "0"; // 证件号
	public static final String sign = "mracxqc4wlak6ife764zjtdug2u8uo8t"; // 秘钥
	public static final String version = "1.30"; // 版本号

	/***
	 * 银行卡四要素验证
	 * @param cardNo
	 * @param userName
	 * @param idNo
	 * @param phoneNo
	 * @return
	 */
	public static HandleRsp checkCard(String cardNo, String userName, String idNo, String phoneNo) {

		try {
			String url = "https://mpay.fuiou.com:16128/checkCard/checkCard01.pay?FM=";
			Date date = new Date();
			StringBuffer bf = new StringBuffer();
			bf.append("<FM>");
			// 商户代码
			bf.append("<MchntCd>");
			bf.append(merchant_number);
			bf.append("</MchntCd>");
			// 银行卡号
			bf.append("<Ono>");
			bf.append(cardNo);
			bf.append("</Ono>");
			// 用户名
			bf.append("<Onm>");
			bf.append(userName);
			bf.append("</Onm>");
			// 证件类型
			bf.append("<OCerTp>");
			bf.append(id_cardNo);
			bf.append("</OCerTp>");
			// 身份证号
			bf.append("<OCerNo>");
			bf.append(idNo);
			bf.append("</OCerNo>");
			// 预留电话
			bf.append("<Mno>");
			bf.append(phoneNo);
			bf.append("</Mno>");
			// 流水账号
			bf.append("<OSsn>");
			bf.append(date.getTime() + "");
			bf.append("</OSsn>");

			// 秘钥
			String signM = merchant_number + "|" + version + "|" + date.getTime() + "" + "|" + cardNo + "|" + id_cardNo
					+ "|" + idNo + "|" + sign;

			signM = MD5Utils.EncoderByMd5(signM);
			bf.append("<Sign>");
			bf.append(signM);
			bf.append("</Sign>");
			// 版本号
			bf.append("<Ver>");
			bf.append(version);
			bf.append("</Ver>");
			bf.append("</FM>");

			String rsp = HtmlUtils.getContent(url + URLEncoder.encode(bf.toString(), "UTF-8"));
			Map<String, String> map = XMLParser.readStringXmlOut(rsp);
			String rsp_code = map.get("Rcd");
			if(StringUtils.equals("0000",rsp_code)) {
				return HandleRsp.success("验证通过");
			}else {
				String rsp_desc = map.get("RDesc");
				if(rsp_desc.contains("|")) {
					String first_desc = rsp_desc.substring(0,rsp_desc.indexOf("|"));
					logger.error("====="+first_desc+"=====");
					return HandleRsp.fail(first_desc);
				}else {
					logger.error("====="+rsp_desc+"=====");
					return HandleRsp.fail(rsp_desc);
				}
			}

		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(e));
			return HandleRsp.fail("验证失败,请求富友接口失败");
		}
		
	}
}
