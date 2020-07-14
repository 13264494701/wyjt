package com.jxf.wx.api.message.req;

/***
 * 
 * @类功能说明： 
 * @类修改者： 
 * @修改日期： 
 * @修改说明： 
 * @公司名称：北京金信服科技有限公司
 * @作者：Administrator 
 * @创建时间：2016年11月11日 下午4:14:30 
 * @版本：V1.0
 */
public final class VoiceReqMsg extends BaseReqMsg {

    private String mediaId;
    private String format;
    private String recognition;

    public VoiceReqMsg(String mediaId, String format, String recognition) {
        super();
        this.mediaId = mediaId;
        this.format = format;
        this.recognition = recognition;
        setMsgType(ReqType.VOICE);
    }

    public String getMediaId() {
        return mediaId;
    }

    public String getFormat() {
        return format;
    }

    public String getRecognition() {
        return recognition;
    }

    @Override
    public String toString() {
        return "VoiceReqMsg [mediaId=" + mediaId + ", format=" + format
                + ", recognition=" + recognition + ", msgId=" + msgId
                + ", toUserName=" + toUserName + ", fromUserName="
                + fromUserName + ", createTime=" + createTime + ", msgType="
                + msgType + "]";
    }

}
