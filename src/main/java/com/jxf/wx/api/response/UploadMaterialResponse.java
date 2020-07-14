package com.jxf.wx.api.response;

import com.alibaba.fastjson.annotation.JSONField;

/***
 * 
 * @类功能说明： 
 * @类修改者： 
 * @修改日期： 
 * @修改说明： 
 * @公司名称：北京金信服科技有限公司
 * @作者：Administrator 
 * @创建时间：2016年11月11日 下午7:50:09 
 * @版本：V1.0
 */
public class UploadMaterialResponse extends BaseResponse  {

	private static final long serialVersionUID = 1L;

	@JSONField(name = "media_id")
    private String mediaId;

    @JSONField(name="url")
    private String url;
    
    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
    
}
