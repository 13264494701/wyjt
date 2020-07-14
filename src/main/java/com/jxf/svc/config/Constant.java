package com.jxf.svc.config;

public class Constant {


	/**成功*/
	public static final int RESCODE_SUCCESS = 1000;	
	/**成功(有返回数据)*/
	public static final int RESCODE_SUCCESS_DATA = 1001;
	/**查询结果为空*/
	public static final int RESCODE_NOEXIST = 1004;			

	/**请求抛出异常*/
	public static final int RESCODE_EXCEPTION = 2001;
	/**异常带数据*/
	public static final int RESCODE_EXCEPTION_DATA = 2002;	
	
	/**未登陆状态*/
	public static final int RESCODE_NOLOGIN = 3001;				
	/**无操作权限*/
	public static final int RESCODE_NOAUTH = 3002;		
	/**登录过期*/
	public static final int RESCODE_LOGINEXPIRE = 3003;	
	
	
	/**Token验证通过*/
	public static final int JWT_SUCCESS = 0;	
	/**Token过期*/
	public static final int JWT_ERRCODE_EXPIRE = 401;	
	/**验证不通过*/
	public static final int JWT_ERRCODE_FAIL = 402;	
	
	
	/** 数据记录更新最大重试次数*/
	public static final Integer MAX_ATTEMPTS = 20;
	
	/** 数据记录更新失败 -1*/
	public static final Integer UPDATE_FAILED = -1;

	/** 数据记录更新成功 0*/
	public static final Integer UPDATE_SUCCESS = 0;
	
	
	/** 苹果是否显示 0*/
	public static final Integer IOS_SHOW = 0;
	
	/** 合同类型-借条  0*/
	public static final int CONTRACT_LOAN = 0;
	/** 合同类型-债转 1*/
	public static final int CONTRACT_CR = 1;
	
	/** 公司名称*/
	public static final String COM_NAME = "北京友信宝网络科技有限公司";
	/** 社会统一信用代码*/
	public static final String COM_CREDIT_CODE = "91110105335565804C";
	/** 公司法人代表*/
	public static final String CORPLEGAL_NAME = "王继军";
	/** 公司法人职务*/
	public static final String CORPLEGAL_TITLE = "董事长";
	/** 法人手机号*/
	public static final String CORPLEGAL_MOBILE = "13366661088";
	/** 法人邮箱*/
	public static final String CORPLEGAL_EMAIL = "7777298@qq.com";
	
	/** 公司账户开户行*/
	public static final String CORP_BANK_NAME ="华夏银行北京分行上地支行";
	
	/** 公司账户开户行*/
	public static final String CORP_BANK_CARD_NO ="10285000001460020";
	
	/** 公司账户开户行*/
	public static final String APP_NAME ="无忧借条";
}
