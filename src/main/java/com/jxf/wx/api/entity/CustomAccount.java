package com.jxf.wx.api.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 客服帐号对象
 *
 * @author peiyu
 */
public class CustomAccount extends CrudEntity<CustomAccount> {

	private static final long serialVersionUID = 1L;

	@JSONField(name = "kf_account")
    private String accountName;

    @JSONField(name = "kf_nick")
    private String nickName;

    private String password;

    @JSONField(name = "kf_id")
    private Long id;

    @JSONField(name = "kf_headimg")
    private String headImg;

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }
}
