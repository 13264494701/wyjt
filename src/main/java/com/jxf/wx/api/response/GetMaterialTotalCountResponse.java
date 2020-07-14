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
 * @创建时间：2016年11月11日 下午9:03:54 
 * @版本：V1.0
 */
public class GetMaterialTotalCountResponse extends BaseResponse {

	private static final long serialVersionUID = 1L;
	
	@JSONField(name="video_count")
    private int video;
    @JSONField(name="voice_count")
    private int voice;
    @JSONField(name="image_count")
    private int image;
    @JSONField(name="news_count")
    private int news;

    public int getVideo() {
        return video;
    }

    public void setVideo(int video) {
        this.video = video;
    }

    public int getVoice() {
        return voice;
    }

    public void setVoice(int voice) {
        this.voice = voice;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getNews() {
        return news;
    }

    public void setNews(int news) {
        this.news = news;
    }

    @Override
    public String toString() {
        return "GetMaterialTotalCountResponse{" +
                "video=" + video +
                ", voice=" + voice +
                ", image=" + image +
                ", news=" + news +
                '}';
    }
}
