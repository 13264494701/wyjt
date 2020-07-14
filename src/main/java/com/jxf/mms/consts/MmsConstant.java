package com.jxf.mms.consts;

import com.jxf.svc.config.Global;


public class MmsConstant {
	
	public static final String WEBISITE_TITLE = Global.getConfig("website.title");

	public static final String WEBSITE_NAME_CN = Global.getConfig("website.name.cn");
	
	public static final  String WEBSITE_DOMAIN_NAME = Global.getConfig("website.domain");
	
	public static final String WEBSITE_ICP = Global.getConfig("website.icp");
	
	public static String EMAIL_SERVICE = Global.getConfig("email.service");
	
	public static final int SMS_OUTTIME = 10*60;
	
	/**
	 * 消息类型
	 */
	public enum MsgType {

		/** 无忧借条APP注册*/
		wyjtAppRegister,

		/** 找回密码 */
		findpasswd, 
		
		/** 修改密码 */
		changepasswd,
		
		/** 修改手机 */
		changephone  ,
		
		register
	}
}
