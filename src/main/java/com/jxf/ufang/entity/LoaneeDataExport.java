package com.jxf.ufang.entity;

import com.jxf.svc.annotation.ExcelField;

public class LoaneeDataExport {

    @ExcelField(title = "购买人")
    private String createUser;
    @ExcelField(title = "借款人姓名")
    private String name;
    @ExcelField(title = "手机号")
    private String phoneNo;

    @ExcelField(title = "年龄")
    private Integer age;
    @ExcelField(title = "芝麻分")
    private String zhimafen;
    @ExcelField(title = "QQ号")
    private String qqNo;
    @ExcelField(title = "微信号")
    private String weixinNo;
    @ExcelField(title = "申请金额")
    private String applyAmount;
    @ExcelField(title = "价格")
    private String price;
    @ExcelField(title = "购 买 时 间")
    private String createTime;

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getZhimafen() {
        return zhimafen;
    }

    public void setZhimafen(String zhimafen) {
        this.zhimafen = zhimafen;
    }

    public String getQqNo() {
        return qqNo;
    }

    public void setQqNo(String qqNo) {
        this.qqNo = qqNo;
    }

    public String getWeixinNo() {
        return weixinNo;
    }

    public void setWeixinNo(String weixinNo) {
        this.weixinNo = weixinNo;
    }

    public String getApplyAmount() {
        return applyAmount;
    }

    public void setApplyAmount(String applyAmount) {
        this.applyAmount = applyAmount;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

}