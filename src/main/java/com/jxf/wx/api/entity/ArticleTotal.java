package com.jxf.wx.api.entity;

import java.util.List;

/***
 * 
 * @类功能说明： 图文群发总数据
 * @类修改者： 
 * @修改日期： 
 * @修改说明： 
 * @公司名称：北京金信服科技有限公司
 * @作者：Administrator 
 * @创建时间：2016年6月11日 下午5:26:58 
 * @版本：V1.0
 */
public class ArticleTotal extends BaseDataCube {

	private static final long serialVersionUID = 1L;
	
	private String                   msgid;
    private String                   title;
    private List<ArticleTotalDetail> details;

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

    public List<ArticleTotalDetail> getDetails() {
        return details;
    }

    public void setDetails(List<ArticleTotalDetail> details) {
        this.details = details;
    }
}
