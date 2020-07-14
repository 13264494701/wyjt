package com.jxf.web.model.wyjt.app;

public class ShareCreditChangeRequestParam {


    /**
     * 类型 1 升级 2降级
     */
    private String type;

    /**
     *昵称
     */
    private String nickname;

    /**
     *等级
     */
    private String rankName;

    /**
     *百分百
     */
    private String percent;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRankName() {
        return rankName;
    }

    public void setRankName(String rankName) {
        this.rankName = rankName;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }
}
