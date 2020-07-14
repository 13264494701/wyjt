package com.jxf.wx.api;

import com.alibaba.fastjson.JSON;
import com.jxf.wx.api.response.BaseResponse;
import com.jxf.wx.api.response.QrcodeResponse;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/***
 * 
 * @类功能说明： 二维码相关API
 * @类修改者： 
 * @修改日期： 
 * @修改说明： 
 * @公司名称：北京金信服科技有限公司
 * @作者：Administrator 
 * @创建时间：2016年11月11日 下午5:07:20 
 * @版本：V1.0
 */
public class QrcodeAPI extends BaseAPI {
	
	public enum QrcodeType {

	    /**
	     * 临时二维码
	     */
	    QR_SCENE,

	    /**
	     * 永久二维码
	     */
	    QR_LIMIT_SCENE
	}
	

	public QrcodeAPI(String accessToken) {
		super(accessToken);
	}

	private static final Logger LOG = LoggerFactory.getLogger(QrcodeAPI.class);

//    public QrcodeAPI(ApiConfig config) {
//        super(config);
//    }

    /**
     * 创建二维码
     *
     * @param actionName    二维码类型，QR_SCENE为临时,QR_LIMIT_SCENE为永久
     * @param sceneId       场景值ID，临时二维码时为32位非0整型，永久二维码时最大值为100000（目前参数只支持1--100000）
     * @param expireSeconds 该二维码有效时间，以秒为单位。 最大不超过2592000
     * @return 二维码对象
     */
    public QrcodeResponse createQrcode(QrcodeType actionName, String sceneId, Integer expireSeconds) {

        Assert.notNull(actionName,"");
        Assert.notNull(sceneId,"");
        LOG.debug("创建二维码信息.....");

        QrcodeResponse response = null;
        String url = BASE_API_URL + "cgi-bin/qrcode/create?access_token=#";

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("action_name", actionName);
        Map<String, Object> actionInfo = new HashMap<String, Object>();
        Map<String, Object> scene = new HashMap<String, Object>();
        scene.put("scene_id", sceneId);
        actionInfo.put("scene", scene);
        param.put("action_info", actionInfo);
        if (expireSeconds!=null && expireSeconds != 0 ) {
            param.put("expire_seconds", expireSeconds);
        }
        BaseResponse r = executePost(url, JSON.toJSONString(param));
        String resultJson = isSuccess(r.getErrcode()) ? r.getErrmsg() : JSON.toJSONString(r);
        response = JSON.parseObject(resultJson, QrcodeResponse.class);
        return response;
    }
    
    /**
     * 获取二维码图片地址
     * @param ticket
     * @return
     */
    public static String getQrCodeUrl(String ticket){
    	try {
			ticket = URLEncoder.encode(ticket,"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
    	String url = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=#";
    	return  url.replace("#", ticket);
    }
}
