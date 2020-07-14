package com.jxf.wx.api.entity;

import com.alibaba.fastjson.annotation.JSONField;

/***
 * 
 * @类功能说明：图文群发每日数据 
 * @类修改者： 
 * @修改日期： 
 * @修改说明： 
 * @公司名称：北京金信服科技有限公司
 * @作者：Administrator 
 * @创建时间：2016年11月11日 下午5:27:58 
 * @版本：V1.0
 */
public class ArticleSummary extends BaseDataCube {

	private static final long serialVersionUID = 1L;
	
	private String  msgid;
    private String  title;
    @JSONField(name = "int_page_read_user")
    private Integer intPageReadUser;
    @JSONField(name = "int_page_read_count")
    private Integer intPageReadCount;
    @JSONField(name = "ori_page_read_user")
    private Integer oriPageReadUser;
    @JSONField(name = "ori_page_read_count")
    private Integer oriPageReadCount;
    @JSONField(name = "share_user")
    private Integer shareUser;
    @JSONField(name = "shareCount")
    private Integer share_count;
    @JSONField(name = "add_to_fav_user")
    private Integer addToFavUser;
    @JSONField(name = "add_to_fav_count")
    private Integer addToFavCount;

    public String getMsgid() {
        return msgid;
    }

    public void setMsgid(String msgid) {
        this.msgid = msgid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getIntPageReadUser() {
        return intPageReadUser;
    }

    public void setIntPageReadUser(Integer intPageReadUser) {
        this.intPageReadUser = intPageReadUser;
    }

    public Integer getIntPageReadCount() {
        return intPageReadCount;
    }

    public void setIntPageReadCount(Integer intPageReadCount) {
        this.intPageReadCount = intPageReadCount;
    }

    public Integer getOriPageReadUser() {
        return oriPageReadUser;
    }

    public void setOriPageReadUser(Integer oriPageReadUser) {
        this.oriPageReadUser = oriPageReadUser;
    }

    public Integer getOriPageReadCount() {
        return oriPageReadCount;
    }

    public void setOriPageReadCount(Integer oriPageReadCount) {
        this.oriPageReadCount = oriPageReadCount;
    }

    public Integer getShareUser() {
        return shareUser;
    }

    public void setShareUser(Integer shareUser) {
        this.shareUser = shareUser;
    }

    public Integer getShare_count() {
        return share_count;
    }

    public void setShare_count(Integer share_count) {
        this.share_count = share_count;
    }

    public Integer getAddToFavUser() {
        return addToFavUser;
    }

    public void setAddToFavUser(Integer addToFavUser) {
        this.addToFavUser = addToFavUser;
    }

    public Integer getAddToFavCount() {
        return addToFavCount;
    }

    public void setAddToFavCount(Integer addToFavCount) {
        this.addToFavCount = addToFavCount;
    }
}
