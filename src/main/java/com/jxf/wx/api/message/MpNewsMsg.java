package com.jxf.wx.api.message;
/***
 * 
 * @类功能说明： 提交至微信的图文消息素材
 * @类修改者： 
 * @修改日期： 
 * @修改说明： 
 * @公司名称：北京金信服科技有限公司
 * @作者：Administrator 
 * @创建时间：2016年11月11日 下午4:19:40 
 * @版本：V1.0
 */
public class MpNewsMsg extends BaseMsg {

    /** 
	 * @Fields serialVersionUID : TODO 
	 */ 
	private static final long serialVersionUID = 1L;
	private String mediaId;

    public MpNewsMsg() {
    }

    public MpNewsMsg(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }
}
