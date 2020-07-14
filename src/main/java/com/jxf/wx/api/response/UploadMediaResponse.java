package com.jxf.wx.api.response;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

/**
 * 
 * @类功能说明： 
 * @类修改者： 
 * @修改日期： 
 * @修改说明： 
 * @公司名称：北京金信服科技有限公司
 * @作者：Administrator 
 * @创建时间：2016年11月11日 下午7:48:08 
 * @版本：V1.0
 */
public class UploadMediaResponse extends BaseResponse {


	private static final long serialVersionUID = 1L;
	
	private String type;
    @JSONField(name = "media_id")
    private String mediaId;
    @JSONField(name = "created_at")
    private Date   createdAt;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
