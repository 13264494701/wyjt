package com.jxf.wx.api.message;

import com.jxf.wx.api.message.util.MessageBuilder;

public class ArticleMsg {

    private String title;
    private String description;
    private String picUrl;
    private String url;

    public ArticleMsg() {

    }

    public ArticleMsg(String title) {
        this.title = title;
    }

    public ArticleMsg(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public ArticleMsg(String title, String description, String picUrl, String url) {
        this.title = title;
        this.description = description;
        this.picUrl = picUrl;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String toXml() {
        MessageBuilder mb = new MessageBuilder();
        mb.addData("Title", title);
        mb.addData("Description", description);
        mb.addData("PicUrl", picUrl);
        mb.addData("Url", url);
        mb.surroundWith("item");
        return mb.toString();
    }

}
