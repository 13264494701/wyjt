package com.jxf.mms.msg;

import java.util.Map;

/**
 * @类功能说明： 发送短信交易接口
 * @类修改者： 
 * @修改日期： 
 * @修改说明： 
 * @公司名称：北京金信服科技有限公司
 * @作者：zhuhuijie 
 * @创建时间：2016年3月18日 下午9:23:47 
 * @版本：V1.0
 */
public interface SendAppMsgService {

	void sendSms(String tmplCode,String phoneNo,String smsCode);

	void sendMessage(String type, String phoneNo, Map<String, Object> map);


}
