package com.jxf.svc.sys.role.entity;

import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.google.common.collect.Lists;
import com.jxf.svc.sys.crud.entity.CrudEntity;
import com.jxf.svc.sys.menu.entity.Menu;
import com.jxf.svc.utils.StringUtils;

/**
 * 角色Entity
 * @author jxf
 * @version 2015-07-28
 */
public class Role extends CrudEntity<Role> {
	
	private static final long serialVersionUID = 1L;
	
	
	private String roleName;		// 角色名称
	private String dataScope;		// 数据范围
	private String oldName; 	// 原角色名称

	private List<Menu> menuList = Lists.newArrayList(); // 拥有菜单列表
	
	/**1：所有数据*/
	public static final String DATA_SCOPE_ALL = "1";
	/**2：所在公司及以下数据*/
	public static final String DATA_SCOPE_COMPANY_AND_CHILD = "2";
	/**3：所在公司数据*/
	public static final String DATA_SCOPE_COMPANY = "3";
	/**4：所在部门及以下数据*/
	public static final String DATA_SCOPE_OFFICE_AND_CHILD = "4";
	/**5：所在部门数据*/
	public static final String DATA_SCOPE_OFFICE = "5";
	/**8：仅本人数据*/
	public static final String DATA_SCOPE_SELF = "8";
	/**9：按明细设置*/
	public static final String DATA_SCOPE_CUSTOM = "9";
	
	public Role() {
		super();
		this.dataScope = DATA_SCOPE_SELF;
	}
	
	public Role(Long id){
		super(id);
	}
	
	@Length(min=1, max=20, message="角色名称长度必须介于 1 和 20 之间")
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	

	@Length(min=0, max=1, message="数据范围长度必须介于 0 和 1 之间")
	public String getDataScope() {
		return dataScope;
	}

	public void setDataScope(String dataScope) {
		this.dataScope = dataScope;
	}
	public String getOldName() {
		return oldName;
	}

	public void setOldName(String oldName) {
		this.oldName = oldName;
	}
	public List<Menu> getMenuList() {
		return menuList;
	}

	public void setMenuList(List<Menu> menuList) {
		this.menuList = menuList;
	}

	public List<Long> getMenuIdList() {
		List<Long> menuIdList = Lists.newArrayList();
		for (Menu menu : menuList) {
			menuIdList.add(menu.getId());
		}
		return menuIdList;
	}

	public void setMenuIdList(List<Long> menuIdList) {
		menuList = Lists.newArrayList();
		for (Long menuId : menuIdList) {
			Menu menu = new Menu();
			menu.setId(menuId);
			menuList.add(menu);
		}
	}

	public String getMenuIds() {
		return StringUtils.join(getMenuIdList(), ",");
	}
	
	public void setMenuIds(String menuIds) {
		menuList = Lists.newArrayList();
		if (menuIds != null){
			Long[] ids = StringUtils.splitToLong(menuIds);
			setMenuIdList(Lists.newArrayList(ids));
		}
	}	
	/**
	 * 获取权限字符串列表
	 */
	public List<String> getPermissions() {
		List<String> permissions = Lists.newArrayList();
		for (Menu menu : menuList) {
			if (menu.getPermission()!=null && !"".equals(menu.getPermission())){
				permissions.add(menu.getPermission());
			}
		}
		return permissions;
	}

	public boolean getIsGod(){
		return isGod(this.id);
	}
	public boolean isGod(){
		return isGod(this.id);
	}
	
	public static boolean isGod(Long id){
		return id != null && id==1L;
	}
	

}
