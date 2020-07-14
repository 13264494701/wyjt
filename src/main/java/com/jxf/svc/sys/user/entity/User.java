package com.jxf.svc.sys.user.entity;

import java.util.Date;
import java.util.List;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.common.collect.Lists;
import com.jxf.svc.annotation.ExcelField;
import com.jxf.svc.sys.brn.entity.Brn;
import com.jxf.svc.sys.crud.entity.CrudEntity;
import com.jxf.svc.sys.role.entity.Role;
import com.jxf.svc.utils.Collections3;


/**
 * 用户Entity
 * @author jxf
 * @version 2015-07-28
 */
public class User extends CrudEntity<User> {

	private static final long serialVersionUID = 1L;
	private Brn brn;	// 归属机构
	private String loginName;// 登录名称
	private String password;// 登录密码
	private String empNo;	// 员工编号
	private String empNam;	// 员工姓名
	private String email;	// 邮箱地址
	private String phone;	// 电话码
	private String mobile;	// 手机码
	private String photo;	// 头像头像地址
	private String oldLoginName;// 原登录名
	private String newPassword;	// 新密码
	private String oldLoginIp;	// 上次登陆IP
	private Date oldLoginDate;	// 上次登陆日期
	private String loginIp;	// 最后登陆IP
	private Date loginDate;	// 最后登陆日期
	private Boolean isEnabled;		// 是否可用
	private Boolean isLocked;		// 是否锁定
	private String allowIps; //IP白名单
	private Role role;	// 根据角色查询用户条件
	private List<Role> roleList = Lists.newArrayList(); // 拥有角色列表
	private Integer mask;

	public User() {
		super();
	}
	
	public User(Long id){
		super(id);
	}

	public User(Long id, String loginName){
		super(id);
		this.loginName = loginName;
	}

	public User(Role role){
		super();
		this.role = role;
	}
	
	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}



	public Long getId() {
		return id;
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
	
	@JSONField(serialize = false)
	@ExcelField(title="归属机构", align=2, sort=25)
	public Brn getBrn() {
		return brn;
	}

	public void setBrn(Brn brn) {
		this.brn = brn;
	}

	@Length(min=1, max=100, message="登录名长度必须介于 1 和 100 之间")
	@ExcelField(title="登录名", align=2, sort=30)
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	@JSONField(serialize = false)
	@Length(min=1, max=100, message="密码长度必须介于 1 和 100 之间")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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
	
	@Length(min=0, max=200, message="电话长度必须介于 1 和 200 之间")
	@ExcelField(title="电话", align=2, sort=60)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Length(min=0, max=200, message="手机长度必须介于 1 和 200 之间")
	@ExcelField(title="手机", align=2, sort=70)
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getRmk() {
		return rmk;
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

	public String getOldLoginName() {
		return oldLoginName;
	}

	public void setOldLoginName(String oldLoginName) {
		this.oldLoginName = oldLoginName;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
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

	public String getAllowIps() {
		return allowIps;
	}

	public void setAllowIps(String allowIps) {
		this.allowIps = allowIps;
	}
	
	
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@JSONField(serialize = false)
	public List<Role> getRoleList() {
		return roleList;
	}
	
	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}

	@JSONField(serialize = false)
	public List<Long> getRoleIdList() {
		List<Long> roleIdList = Lists.newArrayList();
		for (Role role : roleList) {
			roleIdList.add(role.getId());
		}
		return roleIdList;
	}

	public void setRoleIdList(List<Long> roleIdList) {
		roleList = Lists.newArrayList();
		for (Long roleId : roleIdList) {
			Role role = new Role();
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
	
	public boolean isGod(){
		return isGod(this.id);
	}
	
	public static boolean isGod(Long id){
		return id != null && id==1L;
	}

	public Integer getMask() {
		return mask;
	}

	public void setMask(Integer mask) {
		this.mask = mask;
	}



}