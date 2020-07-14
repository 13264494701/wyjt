package com.jxf.mem.utils;

import com.alibaba.fastjson.JSONObject;
import com.jxf.svc.utils.StringUtils;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 *
 * 免密登录：token查询手机号
 **/
public class DirectLoginUtils {
    // 移动telcome枚举值
    public static final String CMCC = "CMCC";
    // 联通telcome枚举值
    public static final String CUCC = "CUCC";
    // 电信telcome枚举值
    public static final String CTCC = "CTCC";

    // 移动免密登录后台url
    public static final String cmcc_login_url = "https://api.253.com/open/flashsdk/mobile-query-m";
    // 联通免密登录后台url
    public static final String cucc_login_url = "https://api.253.com/open/flashsdk/mobile-query-u";
    // 电信免密登录后台url
    public static final String ctcc_login_url = "https://api.253.com/open/flashsdk/mobile-query-t";


    public static void main(String[] args) {
        String appId = "XXXXXX";    //	当前APP对应的appid,SDK传入
        String accessToken = "STsid0000001539309139956vipc10agFWvK9iw0s79s276SlKIxS8FG";    //运营商token,SDK传入
        String telecom = "CMCC";    //运营商，SDK传入
        String timestamp = "1539309140000"; //UNIX时间戳，毫秒级，SDK传入
        String randoms = "7b8c50b139824189aca36df8cf8f5ef1";    //随机数，SDK传入
        String version = "2.0.6";   //SDK版本号，SDK传入
        String device = "iphone7";  //设备型号，SDK传入
        String sign = "ywLmsbPEi5vuM22njekhrFI5gMg=";   //签名，SDK传入

        try {
            JSONObject jsonObject = tokenExchangeMobileRequest(appId, accessToken, telecom, timestamp, randoms, version , device ,sign);
            if (null != jsonObject) {
                String code = jsonObject.getString("code");     //返回码 200000为成功
                String message = jsonObject.getString("message");//返回消息
                String chargeStatus = jsonObject.getString("chargeStatus"); //是否收费
                if ("200000".equals(code)) {
                    String dataStr = jsonObject.getString("data");
                    JSONObject dataObj = JSONObject.parseObject(dataStr);
                    String mobile = dataObj.getString("mobileName");
                    String tradeNo = dataObj.getString("tradeNo");
                    System.out.println("mobile:" + DESForLogin.decryptDES(mobile, "XXXXXX")); //手机号DES解密，解密秘钥为appId对应的appKey
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * tonken置换手机号后台接口
     * @param appId
     * @param accessToken
     * @param telecom
     * @param timestamp
     * @param randoms
     * @param version
     * @param sign
     * @return
     */
    public static JSONObject tokenExchangeMobileRequest(String appId, String accessToken, String telecom,String timestamp,String randoms ,String version, String device,String sign) {
        try {
            String url = null;
            if(StringUtils.isBlank(telecom)) {
                return null;
            }
            if(CMCC.equals(telecom)){
                url = cmcc_login_url;
            }else if(CUCC.equals(telecom)){
                url = cucc_login_url;
            }else if(CTCC.equals(telecom)){
                url = ctcc_login_url;
            }else{
                return null;
            }
            RequestBody body = new FormBody.Builder()
                    .add("appId", appId)
                    .add("accessToken", accessToken)
                    .add("telecom", telecom)
                    .add("timestamp", timestamp)
                    .add("randoms", randoms)
                    .add("version", version)
                    .add("device", device)
                    .add("sign", sign)
                    .build();
            Request request = new Request.Builder()
                    .post(body)
                    .url(url)
                    .build();
            Response response = SingletonForLogin.getInstance().newCall(request).execute();
            if(response.isSuccessful()) {
                String content = response.body().string();
//                System.out.println("response:"+content);
                if (StringUtils.isNotBlank(content)) {
                    JSONObject jsonObject = JSONObject.parseObject(content);
                    return jsonObject;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }







}
