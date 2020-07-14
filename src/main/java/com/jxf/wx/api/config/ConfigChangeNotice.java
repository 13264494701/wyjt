package com.jxf.wx.api.config;

import java.util.Date;

import com.jxf.svc.sys.crud.entity.CrudEntity;
/***
 * 
 * @类功能说明： 配置变化通知
 * @类修改者： 
 * @修改日期： 
 * @修改说明： 
 * @公司名称：北京金信服科技有限公司
 * @作者：Administrator 
 * @创建时间：2016年11月11日 下午4:35:56 
 * @版本：V1.0
 */
public final class ConfigChangeNotice extends CrudEntity<ConfigChangeNotice> {

	private static final long serialVersionUID = 1L;

	private Date noticeTime;

    private String appid;

    private ChangeType type;

    private String value;

    public ConfigChangeNotice() {
        this.noticeTime = new Date();
    }

    public ConfigChangeNotice(String appid, ChangeType type, String value) {
        this();
        this.appid = appid;
        this.type = type;
        this.value = value;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public ChangeType getType() {
        return type;
    }

    public void setType(ChangeType type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Date getNoticeTime() {
        return noticeTime;
    }

    public void setNoticeTime(Date noticeTime) {
        this.noticeTime = noticeTime;
    }

    @Override
    public String toString() {
        return "ConfigChangeNotice{" +
                "noticeTime=" + noticeTime +
                ", appid='" + appid + '\'' +
                ", type=" + type +
                ", value='" + value + '\'' +
                '}';
    }
}
