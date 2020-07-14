package com.jxf.wx.api.exception;

/***
 * 
 * @类功能说明： 微信API处理异常
 * @类修改者： 
 * @修改日期： 
 * @修改说明： 
 * @公司名称：北京金信服科技有限公司
 * @作者：Administrator 
 * @创建时间：2016年11月11日 下午7:44:49 
 * @版本：V1.0
 */
public class WeixinException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public WeixinException() {
        super();
    }

    public WeixinException(String message) {
        super(message);
    }

    public WeixinException(String message, Throwable cause) {
        super(message, cause);
    }

    public WeixinException(Throwable cause) {
        super(cause);
    }
}
