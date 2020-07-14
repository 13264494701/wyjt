package com.jxf.svc.sys.brn.entity;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.jxf.svc.sys.area.entity.Area;
import com.jxf.svc.sys.tree.entity.TreeEntity;

/**
 * 机构Entity
 * @author lx
 * @version 2015-07-28
 */
public class Brn extends TreeEntity<Brn> {

	private static final long serialVersionUID = 1L;
	private Area area;		// 归属区域
	private String brnNo;		// 机构编号
	private String brnName;		// 机构名称

	private String brnType; 	// 机构类型（1：公司；2：部门；3：小组）
	private String brnGrade; 	// 机构等级（1：一级；2：二级；3：三级；4：四级）
	private String address; // 联系地址
	private String zipCode; // 邮政编码
	private String phone; 	// 电话
	private String faxNo; 	// 传真
	private String email; 	// 邮箱
	private String primaryPerson;		// 主负责人
	private String deputyPerson;		// 副负责人
	private List<String> childDeptList;//快速添加子部门
	
	public Brn(){
		super();
//		this.sort = 30;
		this.brnType = "2";
	}

	public Brn(Long id){
		super(id);
	}
	
	public List<String> getChildDeptList() {
		return childDeptList;
	}

	public void setChildDeptList(List<String> childDeptList) {
		this.childDeptList = childDeptList;
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
	
	@Length(min=0, max=64, message="副负责人长度必须介于 0 和 64 之间")
	public String getDeputyPerson() {
		return deputyPerson;
	}

	public void setDeputyPerson(String deputyPerson) {
		this.deputyPerson = deputyPerson;
	}

	public Brn getParent() {
		return parent;
	}

	public void setParent(Brn parent) {
		this.parent = parent;
	}

	@NotNull
	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}


	public String getBrnType() {
		return brnType;
	}

	public void setBrnType(String brnType) {
		this.brnType = brnType;
	}

	public String getBrnGrade() {
		return brnGrade;
	}

	public void setBrnGrade(String brnGrade) {
		this.brnGrade = brnGrade;
	}

	@Length(min=0, max=255)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Length(min=0, max=6, message="邮政编码长度必须介于 0 和 6 之间")
	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	@Length(min=0, max=20, message="电话号码长度必须介于 0 和 20 之间")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Length(min=0, max=200, message="传真号码长度必须介于 0 和 200 之间")
	public String getFaxNo() {
		return faxNo;
	}

	public void setFaxNo(String faxNo) {
		this.faxNo = faxNo;
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
}