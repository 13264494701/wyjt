package com.jxf.rc.utils;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import com.jxf.svc.utils.JSONUtil;
import com.rong360.tianji.sample.OpenapiClient;
import com.rong360.tianji.utils.RequestUtil;


public class TianjiSample {
	private static final String appId = "2010475";

	private static final String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBANrsyRCcoiG0Nn+/\n"
			+ "S6rQWSuzUamzB+LcFAEgktv+UpfZlXDWep3ixjr43jkcznpsVxLMESneyZvnrNc8\n"
			+ "Fj4SzSlW86rN6VBCsr23hK1kfjpP/v741kwfnDLLfZSLY/QsipuM8BEatzESPmGc\n"
			+ "6HCrXqw6yHWp4EWo9gt+UMrQabKdAgMBAAECgYAHRbBUJe/Y0MSyr6cS0IL1yaof\n"
			+ "yfusFIVxmCRovGT/+FFXgzhlgD/3e4bePM+7D1hBHpg/XNAzppl+ONfATh2UfzPv\n"
			+ "UhY9/yvsit69096cLoqYw69K946YIkqFMhYcRocnNzY04IZRcTYsglJysxl9YGh0\n"
			+ "Qkuuou+1HfWVyO6fvQJBAP2qZMfc3k2dJ2+/gXLVSCEslOSAkmu5Ai5yjHleJrfp\n"
			+ "SX0xM/Y016sRDzMYwIflWmwWSjt5P+UTNf4sWPQxsAMCQQDc8Iv9KbdJ4NJG6eVv\n"
			+ "FImf+PLsswSCbrEJ23d6LJDR8te4y2+T2MizikDVafewNeH9DCrfao7sakt8S/xx\n"
			+ "CyDfAkEAlfkXCp9IA4VFmqkTxKvasFpmUU6+wteoNYPeD6edpqBTpaPgf9EwLWCx\n"
			+ "ptkcg3wTYNIw40bFgw8/nd3kXcO+twJATa39ahs6qCz1gZ1G35E2+hnLP1GJqlpE\n"
			+ "wJR7I2d723UoR36vUioMFqPrO52wSjIHrLKUeEy8x35v39nPfedNvQJAbE2sNS5R\n"
			+ "4+cMczCeh3yJ291VnfsL/ZjGodN7Kaly8SMXxouMufbznRBulKC56HH1iWnC1Ws2\n" + "R/kJ2rYP0jKG9w==";

	private static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDa7MkQnKIhtDZ/v0uq0Fkrs1Gp\n"
			+ "swfi3BQBIJLb/lKX2ZVw1nqd4sY6+N45HM56bFcSzBEp3smb56zXPBY+Es0pVvOq\n"
			+ "zelQQrK9t4StZH46T/7++NZMH5wyy32Ui2P0LIqbjPARGrcxEj5hnOhwq16sOsh1\n" + "qeBFqPYLflDK0GmynQIDAQAB";

	/**
	 * 获取天机授权
	 * 
	 * @param userId
	 *            用户id
	 * @param realname
	 *            用户真实姓名
	 * @param idcard
	 *            用户身份证号码
	 * @param mobile
	 *            用户手机号码
	 * @return
	 * @throws Exception
	 */
	public static String getTianJiApply(int userId, String realname, String idcard, String mobile){
		OpenapiClient sample = new OpenapiClient();

		String uuId = UUID.randomUUID().toString().replaceAll("-", "");
		sample.setAppId(TianjiSample.appId); // 设置Appid
		sample.setPrivateKey(privateKey); // 设置机构私钥
		sample.setIsTestEnv(false); // 设置为执行环境， false：正式环境，true：测试环境
		sample.setPrintLog(false); // 是否打印关键日志 true:打印，false：不打印
		sample.setLogid(uuId); // 此次操作唯一标识

		sample.setMethod("tianji.api.baidu.risklist");
		sample.setField("type", "mobile");
		sample.setField("platform", "web");
		sample.setField("userId", userId + "");
		sample.setField("outUniqueId", uuId);
		sample.setField("name", realname);
		sample.setField("phone", mobile);
		sample.setField("idNumber", idcard);
		sample.setField("notifyUrl", "/mobile/tianjiRiskControl.jsp");
		sample.setField("version", "1.0");
		sample.setField("emergencyName1", "");
		sample.setField("emergencyRelation1", "");
		sample.setField("emergencyPhone1", "");
		sample.setField("emergencyName2", "");
		sample.setField("emergencyRelation2", "");
		sample.setField("emergencyPhone2", "");
		sample.setField("account", "");
		sample.setField("homeAddr", "");
		sample.setField("companyAddr", "");
		sample.setField("bankType", "");
		sample.setField("bankCard", "");
		//JSONObject ret =sample.execute();
		//Map<String, Object> res = JSONUtil.toMap(ret.toString());
		Map<String, Object> res = JSONUtil.toMap("");
		Map<String, Object> resss =JSONUtil.toMap(res.get("tianji_api_tianjireport_collectuser_response").toString());
		return resss.get("redirectUrl").toString();
	}

	/**
	 * 2.0授权
	 * 
	 * @param userId
	 * @param realname
	 * @param idcard
	 * @param mobile
	 * @return
	 * @throws Exception
	 */
	public static String getTianJiApplyV2(String thirdId, String realname, String idcard, String mobile)
			throws Exception {
		OpenapiClient sample = new OpenapiClient();

		String uuId = UUID.randomUUID().toString().replaceAll("-", "");
		sample.setAppId(TianjiSample.appId); // 设置Appid
		sample.setPrivateKey(privateKey); // 设置机构私钥
		sample.setIsTestEnv(false); // 设置为执行环境， false：正式环境，true：测试环境
		sample.setPrintLog(false); // 是否打印关键日志 true:打印，false：不打印
		sample.setLogid(uuId); // 此次操作唯一标识

		sample.setMethod("tianji.api.tianjireport.collectuser");
		sample.setField("type", "mobile");
		sample.setField("platform", "api");
		sample.setField("userId", thirdId);
		sample.setField("outUniqueId", uuId);
		sample.setField("name", realname);
		sample.setField("phone", mobile);
		sample.setField("idNumber", idcard);
		sample.setField("notifyUrl", "/mobile/tianjiRiskControlV2.jsp");
		sample.setField("version", "2.0");
		sample.setField("emergencyName1", "");
		sample.setField("emergencyRelation1", "");
		sample.setField("emergencyPhone1", "");
		sample.setField("emergencyName2", "");
		sample.setField("emergencyRelation2", "");
		sample.setField("emergencyPhone2", "");
		sample.setField("account", "");
		sample.setField("homeAddr", "");
		sample.setField("companyAddr", "");
		sample.setField("bankType", "");
		sample.setField("bankCard", "");
		//JSONObject ret = sample.execute();
	    //return ret.toString();
		return "ceshi";
	}

	// 风险名单
	public static String getInformation(String phone, String idNumber, String name) throws Exception {
		OpenapiClient sample = new OpenapiClient();
		sample.setAppId(appId); // TODO 设置Appid
		sample.setPrivateKey(privateKey); // TODO
											// 设置机构私钥，需要使用方替换private_key.pem文件
		sample.setIsTestEnv(false); // TODO
									// 设置为请求测试环境，默认为线上环境（false），需要使用方替换，也可不替换
		sample.setPrintLog(false);// 是否打印关键日志，默认为false，需要使用方替换，也可不替换
		// generateLogid()代表利用UUID随机生成一个id，传入String类型可用自己的id
		sample.setLogid(RequestUtil.generateLogid());
		sample.setMethod("tianji.api.baidu.risklist");
		sample.setField("phone", phone);
		sample.setField("idNumber", idNumber);
		sample.setField("name", name);
		//JSONObject ret =sample.execute();
		//Map<String, Object> res = JSONUtil.toMap(ret.toString());
		String uString = "";
		Map<String, Object> ret =new HashMap<String, Object>();
		Map<String, Object> res2 = new HashMap<String, Object>();
		if (res2.size() <= 0) {
			uString = "";
		}else {
			if (!ret.get("tianji_api_baidu_risklist_response").equals("")
					&& !ret.get("tianji_api_baidu_risklist_response").equals("null")
					&& ret.get("tianji_api_baidu_risklist_response") != null) {
				uString = ret.get("tianji_api_baidu_risklist_response").toString();
				res2 = JSONUtil.toMap(uString);
			}
		}

		return uString;
	}

}
