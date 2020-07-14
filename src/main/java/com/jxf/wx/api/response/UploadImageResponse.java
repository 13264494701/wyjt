package com.jxf.wx.api.response;

import com.alibaba.fastjson.annotation.JSONField;

/***
 * 
 * @类功能说明： 上传图文消息中的图片响应
 * @类修改者： 
 * @修改日期： 
 * @修改说明： 
 * @公司名称：北京金信服科技有限公司
 * @作者：Administrator 
 * @创建时间：2016年11月11日 下午7:50:25 
 * @版本：V1.0
 */
public class UploadImageResponse extends BaseResponse{
	
	/** 
	 * @Fields serialVersionUID : TODO 
	 */ 
	private static final long serialVersionUID = 1L;
	@JSONField(name="url")
	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	
	
}
