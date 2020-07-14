package com.jxf.ufang.entity;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.jxf.svc.sys.area.entity.Area;
import com.jxf.svc.sys.tree.entity.TreeEntity;

/**
 * 机构Entity
 * @author lx
 * @version 2015-07-28
 */
public class UfangBrn extends TreeEntity<UfangBrn> {

	private static final long serialVersionUID = 1L;
	/**
	 * 类型
	 */
	public enum Type {

		/** 系统预定义 */
		predefine,
		
		/** 用户自定义 */
		custom
	}
	private Area area;		// 归属区域
	private String brnNo;		// 机构编号
	private String brnName;		// 机构名称

	private Type type; 	// 机构类型
	private Integer grade; 	// 机构等级（1：一级；2：二级；3：三级）
	
	private String phoneNo; 	//手机号码
	private String primaryPerson;		// 主负责人
	private String address; // 联系地址
	private String email; 	// 邮箱
	/**剩余免费流量条数*/
	private Integer freeData;

	/**是否锁定*/
	private Boolean isLocked;

	private UfangBrnAct act;//优放余额
	
	public UfangBrn(){
		super();
		this.setType(Type.custom);
	}

	public UfangBrn(Long id){
		super(id);
	}


	public Boolean getIsLocked() {
		return isLocked;
	}

	public void setIsLocked(Boolean isLocked) {
		this.isLocked = isLocked;
	}

	@Length(min=1, max=100, message="机构编号长度必须介于 1 和 100 之间")
	public String getBrnNo() {
		return brnNo;
	}
	public void setBrnNo(String brnNo) {
		this.brnNo = brnNo;
	}
	@Length(min=1, max=100, message="机构名称长度必须介于 1 和 100 之间")
	public String getBrnName() {
		return brnName;
	}

	public void setBrnName(String brnName) {
		this.brnName = brnName;
	}
	
	@Length(min=0, max=64, message="主负责人长度必须介于 0 和 64 之间")
	public String getPrimaryPerson() {
		return primaryPerson;
	}

	public void setPrimaryPerson(String primaryPerson) {
		this.primaryPerson = primaryPerson;
	}

	@Override
	public UfangBrn getParent() {
		return parent;
	}

	@Override
	public void setParent(UfangBrn parent) {
		this.parent = parent;
	}

	@NotNull
	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Integer getGrade() {
		return grade;
	}

	public void setGrade(Integer grade) {
		this.grade = grade;
	}

	@Length(min=0, max=255)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Length(min=0, max=11, message="电话号码长度必须介于 0 和 11 之间")
	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}


	@Length(min=0, max=200, message="邮箱地址长度必须介于 0 和 200 之间")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return name=brnName;
	}

	public UfangBrnAct getAct() {
		return act;
	}

	public void setAct(UfangBrnAct act) {
		this.act = act;
	}

	public Integer getFreeData() {
		return freeData;
	}

	public void setFreeData(Integer freeData) {
		this.freeData = freeData;
	}




}