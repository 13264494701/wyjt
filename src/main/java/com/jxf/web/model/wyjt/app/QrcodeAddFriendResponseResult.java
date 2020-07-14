package com.jxf.web.model.wyjt.app;

public class QrcodeAddFriendResponseResult {

    /**
     * 好友ID
     */
    private String friendId;
    /**
     * 姓名
     */
    private String name;
    /**
     * 会员昵称
     */
    private String nickname;
    /**
     * 电话号码
     */
    private String phoneNo;
    /**
     * 头像
     */
    private String headImage;
    /**
     * 好友关系状态 0->不是好友;1->是好友;
     */
    private Integer status;
    /**
     * 用户等级
     */
    private String rankName;

    public String getRankName() {
        return rankName;
    }

    public void setRankName(String rankName) {
        this.rankName = rankName;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}