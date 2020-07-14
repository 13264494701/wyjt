package com.jxf.ufang.entity;

import java.util.Date;
import java.util.List;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.common.collect.Lists;
import com.jxf.mem.entity.Member;
import com.jxf.svc.annotation.ExcelField;
import com.jxf.svc.sys.crud.entity.CrudEntity;
import com.jxf.svc.utils.Collections3;




/**
 * 用户Entity
 * @author wo
 * @version 2018-11-18
 */
public class UfangUser extends CrudEntity<UfangUser> {

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
	
	/**
	 * 绑定状态
	 */
	public enum BindStatus {

		/** 未绑定 */
		unbind,

		/** 已绑定 */
		binded

	}
	private Type type; 	// 用户类型
	private UfangBrn brn;	// 归属机构
	private String username;// 手机号码(登录名)
	private String password;// 登录密码
	private String empNo;	// 员工编码(4位公司号+2位部门号+2位员工序号)
	private String empNam;	// 员工姓名
	private String email;	// 邮箱地址
	private String headImage;	// 头像头像地址
    
	/**绑定无忧借条账号*/
	private  Member member;
	/**绑定无忧借条账号状态*/
	private BindStatus bindStatus;
	/**是否可用*/
	private Boolean isEnabled;	
	/**是否锁定*/
	private Boolean isLocked;	
	
	private String oldLoginIp;	// 上次登陆IP
	private Date oldLoginDate;	// 上次登陆日期
	private String loginIp;	// 最后登陆IP
	private Date loginDate;	// 最后登陆日期
	
	private UfangRole role;	// 根据角色查询用户条件
	private List<UfangRole> roleList = Lists.newArrayList(); // 拥有角色列表

	public UfangUser() {
		super();
		this.setType(Type.custom);
	}
	
	public UfangUser(Long id){
		super(id);
	}

	public UfangUser(Long id, String username){
		super(id);
		this.setUsername(username);
	}

	public UfangUser(UfangRole role){
		super();
		this.role = role;
	}
	
	@JSONField(serialize = false)
	@ExcelField(title="归属机构", align=2, sort=25)
	public UfangBrn getBrn() {
		return brn;
	}

	public void setBrn(UfangBrn brn) {
		this.brn = brn;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	@JSONField(serialize = false)
	@Length(min=1, max=100, message="密码长度必须介于 1 和 100 之间")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getEmpNo() {
		return empNo;
	}

	public void setEmpNo(String empNo) {
		this.empNo = empNo;
	}
	
	@ExcelField(title="姓名", align=2, sort=40)
	public String getEmpNam() {
		return empNam;
	}

	public void setEmpNam(String empNam) {
		this.empNam = empNam;
	}
	
	@Email(message="邮箱格式不正确")
	@Length(min=0, max=200, message="邮箱长度必须介于 1 和 200 之间")
	@ExcelField(title="邮箱", align=1, sort=50)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	public String getHeadImage() {
		return headImage;
	}

	public void setHeadImage(String headImage) {
		this.headImage = headImage;
	}

	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	public Date getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}


	public String getOldLoginIp() {
		if (oldLoginIp == null){
			return loginIp;
		}
		return oldLoginIp;
	}

	public void setOldLoginIp(String oldLoginIp) {
		this.oldLoginIp = oldLoginIp;
	}

	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	public Date getOldLoginDate() {
		if (oldLoginDate == null){
			return loginDate;
		}
		return oldLoginDate;
	}

	public void setOldLoginDate(Date oldLoginDate) {
		this.oldLoginDate = oldLoginDate;
	}

	public UfangRole getRole() {
		return role;
	}

	public void setRole(UfangRole role) {
		this.role = role;
	}

	@JSONField(serialize = false)
	public List<UfangRole> getRoleList() {
		return roleList;
	}
	
	public void setRoleList(List<UfangRole> roleList) {
		this.roleList = roleList;
	}

	@JSONField(serialize = false)
	public List<Long> getRoleIdList() {
		List<Long> roleIdList = Lists.newArrayList();
		for (UfangRole role : roleList) {
			roleIdList.add(role.getId());
		}
		return roleIdList;
	}

	public void setRoleIdList(List<Long> roleIdList) {
		roleList = Lists.newArrayList();
		for (Long roleId : roleIdList) {
			UfangRole role = new UfangRole();
			role.setId(roleId);
			roleList.add(role);
		}
	}
	
	/**
	 * 用户拥有的角色名称字符串, 多个角色名称用','分隔.
	 */
	public String getRoleNames() {
		return Collections3.extractToString(roleList, "roleName", ",");
	}


	public BindStatus getBindStatus() {
		return bindStatus;
	}

	public void setBindStatus(BindStatus bindStatus) {
		this.bindStatus = bindStatus;
	}

	public Boolean getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public Boolean getIsLocked() {
		return isLocked;
	}

	public void setIsLocked(Boolean isLocked) {
		this.isLocked = isLocked;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}








	

}