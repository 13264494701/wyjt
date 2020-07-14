package com.jxf.svc.sys.util;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import com.google.common.collect.Lists;
import com.jxf.svc.cache.CacheUtils;
import com.jxf.svc.init.SpringContextHolder;
import com.jxf.svc.security.Principal;
import com.jxf.svc.sys.area.dao.AreaDao;
import com.jxf.svc.sys.area.entity.Area;
import com.jxf.svc.sys.base.entity.BaseEntity;
import com.jxf.svc.sys.brn.dao.BrnDao;
import com.jxf.svc.sys.brn.entity.Brn;
import com.jxf.svc.sys.menu.dao.MenuDao;
import com.jxf.svc.sys.menu.entity.Menu;
import com.jxf.svc.sys.role.dao.RoleDao;
import com.jxf.svc.sys.role.entity.Role;
import com.jxf.svc.sys.user.dao.UserDao;
import com.jxf.svc.sys.user.entity.User;
import com.jxf.svc.utils.StringUtils;


/**
 * 用户工具类
 * @author jxf
 * @version 2015-07-28
 */
public class UserUtils {

	private static UserDao userDao = SpringContextHolder.getBean(UserDao.class);
	private static RoleDao roleDao = SpringContextHolder.getBean(RoleDao.class);
	private static MenuDao menuDao = SpringContextHolder.getBean(MenuDao.class);
	private static AreaDao areaDao = SpringContextHolder.getBean(AreaDao.class);
	private static BrnDao brnDao = SpringContextHolder.getBean(BrnDao.class);

	public static final String USER_CACHE = "userCache";
	public static final String USER_CACHE_ID_ = "id_";
	public static final String USER_CACHE_LOGIN_NAME_ = "ln";
	public static final String USER_CACHE_LIST_BY_BRN_ID_ = "oid_";

	public static final String CACHE_ROLE_LIST = "roleList";
	public static final String CACHE_MENU_LIST = "menuList";
	public static final String CACHE_AREA_LIST = "areaList";
	public static final String CACHE_BRN_LIST = "brnList";
	public static final String CACHE_BRN_ALL_LIST = "brnAllList";
	
	/**
	 * 根据ID获取用户
	 * @param id
	 * @return 取不到返回null
	 */
	public static User get(Long id){
		User user = (User)CacheUtils.get(USER_CACHE, USER_CACHE_ID_ + id);
		if (user ==  null){
			user = userDao.get(id);
			if (user == null){
				return null;
			}
			user.setRoleList(userDao.findUserRoleList(user));
			CacheUtils.put(USER_CACHE, USER_CACHE_ID_ + user.getId(), user);
			CacheUtils.put(USER_CACHE, USER_CACHE_LOGIN_NAME_ + user.getLoginName(), user);
		}
		return user;
	}
	
	/**
	 * 根据登录名获取用户
	 * @param username
	 * @return 取不到返回null
	 */
	public static User getByLoginName(String username){
		User user = (User)CacheUtils.get(USER_CACHE, USER_CACHE_LOGIN_NAME_ + username);
		if (user == null){
			user = userDao.getByLoginName(new User(null, username));
			if (user == null){
				return null;
			}
			user.setRoleList(userDao.findUserRoleList(user));
			CacheUtils.put(USER_CACHE, USER_CACHE_ID_ + user.getId(), user);
			CacheUtils.put(USER_CACHE, USER_CACHE_LOGIN_NAME_ + user.getLoginName(), user);
		}
		return user;
	}
	
	/**
	 * 清除当前用户缓存
	 */
	public static void clearCache(){
		removeCache(CACHE_ROLE_LIST);
		removeCache(CACHE_MENU_LIST);
		removeCache(CACHE_AREA_LIST);
		removeCache(CACHE_BRN_LIST);
		removeCache(CACHE_BRN_ALL_LIST);
		UserUtils.clearCache(getUser());
	}
	
	/**
	 * 清除指定用户缓存
	 * @param user
	 */
	public static void clearCache(User user){
		CacheUtils.remove(USER_CACHE, USER_CACHE_ID_ + user.getId());
		CacheUtils.remove(USER_CACHE, USER_CACHE_LOGIN_NAME_ + user.getLoginName());
		CacheUtils.remove(USER_CACHE, USER_CACHE_LOGIN_NAME_ + user.getOldLoginName());
//		if (user.getBrn() != null && user.getBrn().getId() != null){
//			CacheUtils.remove(USER_CACHE, USER_CACHE_LIST_BY_BRN_ID_ + user.getBrn().getId());
//		}
	}
	
	/**
	 * 获取当前用户
	 * @return 取不到返回 new User()
	 */
	public static User getUser(){
		Principal principal = getPrincipal();
		User admin = getByLoginName("999999");
		if (principal!=null){
			User user = get(principal.getId());
			if (user != null){
				return user;
			}
			return admin;
		}
		// 如果没有登录，则返回实例化空的User对象。
		return admin;
	}

	/**
	 * 获取当前用户角色列表
	 * @return
	 */
	public static List<Role> getRoleList(){
		@SuppressWarnings("unchecked")
		List<Role> roleList = (List<Role>)getCache(CACHE_ROLE_LIST);
		if (roleList == null){
			User user = getUser();
			if (user.isGod()){
				roleList = roleDao.findAllList(new Role());
			}else{
				Role role = new Role();
				role.getSqlMap().put("dsf", dataScopeFilter("o", "u"));
				roleList = roleDao.findList(role);
			}
			putCache(CACHE_ROLE_LIST, roleList);
		}
		return roleList;
	}
	
	/**
	 * 获取当前用户授权菜单
	 * @return
	 */
	public static List<Menu> getMenuList(){
		@SuppressWarnings("unchecked")
		List<Menu> menuList = (List<Menu>)getCache(CACHE_MENU_LIST);
		if (menuList == null){
			User user = getUser();
			if (user.isGod()){
				menuList = menuDao.findAllList(new Menu());
			}else{		
				menuList = menuDao.findByUserId(user.getId());
			}
			putCache(CACHE_MENU_LIST, menuList);
		}
		return menuList;
	}
	
	/**
	 * 获取当前用户授权的区域
	 * @return
	 */
	public static List<Area> getAreaList(){
		@SuppressWarnings("unchecked")
		List<Area> areaList = (List<Area>)getCache(CACHE_AREA_LIST);
		if (areaList == null){
			areaList = areaDao.findAllList(new Area());
			putCache(CACHE_AREA_LIST, areaList);
		}
		return areaList;
	}
	
	/**
	 * 获取当前用户有权限访问的部门
	 * @return
	 */
	public static List<Brn> getBrnList(){
		@SuppressWarnings("unchecked")
		List<Brn> brnList = (List<Brn>)getCache(CACHE_BRN_LIST);
		if (brnList == null){
			User user = getUser();
			if (user.isGod()){
				brnList = brnDao.findAllList(new Brn());
			}else{
				Brn brn = new Brn();
				brn.getSqlMap().put("dsf", dataScopeFilter("a", ""));
				brnList = brnDao.findList(brn);
			}
			putCache(CACHE_BRN_LIST, brnList);
		}
		return brnList;
	}

	/**
	 * 获取当前用户有权限访问的部门
	 * @return
	 */
	public static List<Brn> getBrnAllList(){
		@SuppressWarnings("unchecked")
		List<Brn> brnList = (List<Brn>)getCache(CACHE_BRN_ALL_LIST);
		if (brnList == null){
			brnList = brnDao.findAllList(new Brn());
		}
		return brnList;
	}
	
	/**
	 * 获取授权主要对象
	 */
	public static Subject getSubject(){
		return SecurityUtils.getSubject();
	}
	
	/**
	 * 获取当前登录者对象
	 */
	public static Principal getPrincipal(){
		try{
			Subject subject = SecurityUtils.getSubject();
			Principal principal = (Principal)subject.getPrincipal();
			if (principal != null){
				return principal;
			}
//			subject.logout();
		}catch (UnavailableSecurityManagerException e) {
			
		}catch (InvalidSessionException e){
			
		}
		return null;
	}
	
	public static Session getSession(){
		try{
			Subject subject = SecurityUtils.getSubject();
			Session session = subject.getSession(false);
			if (session == null){
				session = subject.getSession();
			}
			if (session != null){
				return session;
			}
//			subject.logout();
		}catch (InvalidSessionException e){
			
		}
		return null;
	}
	
	// ============== User Cache ==============
	
	public static Object getCache(String key) {
		return getCache(key, null);
	}
	
	public static Object getCache(String key, Object defaultValue) {
//		Object obj = getCacheMap().get(key);
		Object obj = getSession().getAttribute(key);
		return obj==null?defaultValue:obj;
	}

	public static void putCache(String key, Object value) {
//		getCacheMap().put(key, value);
		getSession().setAttribute(key, value);
	}

	public static void removeCache(String key) {
//		getCacheMap().remove(key);
		getSession().removeAttribute(key);
	}
	
//	public static Map<String, Object> getCacheMap(){
//		Principal principal = getPrincipal();
//		if(principal!=null){
//			return principal.getCacheMap();
//		}
//		return new HashMap<String, Object>();
//	}
	/**
	 * 生成新员工号
	 * @param 
	 * @return
	 */
	public static String  genNewEmpNo(Brn brn){
		String new_emp_no="";
		String max_emp_no=userDao.getMaxEmpNo(brn.getId());
		String brnNo = brnDao.get(brn).getBrnNo();
		if(!StringUtils.isNullOrEmpty(max_emp_no)){
			new_emp_no=brnNo+String.format("%02x", Long.parseLong(max_emp_no.substring(4, 6),16)+1).toUpperCase();
		}else{
			new_emp_no = brnNo+"01";
		}
		return new_emp_no;
		
	}
	public static String getEmpNamByEmpNo(String empNo){
		
	    String empName = userDao.getEmpNamByEmpNo(empNo);
	    
	    return empName;
	}
	
	
	
	/**
	 * 数据范围过滤
	 * @param user 当前用户对象，通过“entity.getCurrentUser()”获取
	 * @param brnAlias 机构表别名，多个用“,”逗号隔开。
	 * @param userAlias 用户表别名，多个用“,”逗号隔开，传递空，忽略此参数
	 * @return 标准连接条件对象
	 */
	public static String dataScopeFilter(String brnAlias, String userAlias) {

		StringBuilder sqlString = new StringBuilder();
		
		// 进行权限过滤，多个角色权限范围之间为或者关系。
		List<String> dataScope = Lists.newArrayList();
		
		// 超级管理员，跳过权限过滤
		User user = getUser();
		if (!user.isGod()){
			boolean isDataScopeAll = false;
			for (Role r : user.getRoleList()){
				for (String brn : StringUtils.split(brnAlias, ",")){
					if (!dataScope.contains(r.getDataScope()) && StringUtils.isNotBlank(brn)){
						if (Role.DATA_SCOPE_ALL.equals(r.getDataScope())){
							isDataScopeAll = true;
						}
//						else if (Role.DATA_SCOPE_COMPANY_AND_CHILD.equals(r.getDataScope())){
//							sqlString.append(" OR " + brn + ".id = '" + user.getBrn().getParentId() + "'");
//							sqlString.append(" OR " + brn + ".parent_ids LIKE '" + user.getBrn().getParentIds() + "%'");
//						}
						else if (Role.DATA_SCOPE_COMPANY.equals(r.getDataScope())){
							sqlString.append(" OR " + brn + ".id = '" + user.getBrn().getParentId() + "'");
							sqlString.append(" OR " + brn + ".parent_id = '" + user.getBrn().getParentId() + "'");
						}
//						else if (Role.DATA_SCOPE_OFFICE_AND_CHILD.equals(r.getDataScope())){
//							sqlString.append(" OR " + brn + ".id = '" + user.getBrn().getId() + "'");
//							sqlString.append(" OR " + brn + ".parent_ids LIKE '" + user.getBrn().getParentIds() + "%'");
//						}
						else if (Role.DATA_SCOPE_OFFICE.equals(r.getDataScope())){
							sqlString.append(" OR " + brn + ".id = '" + user.getBrn().getId() + "'");
						}
						else if (Role.DATA_SCOPE_CUSTOM.equals(r.getDataScope())){
//							String officeIds =  StringUtils.join(r.getBrnIdList(), "','");
//							if (StringUtils.isNotEmpty(officeIds)){
//								sqlString.append(" OR " + oa + ".id IN ('" + officeIds + "')");
//							}
							sqlString.append(" OR EXISTS (SELECT 1 FROM sys_role_office WHERE role_id = '" + r.getId() + "'");
							sqlString.append(" AND office_id = " + brn +".id)");
						}
						//else if (Role.DATA_SCOPE_SELF.equals(r.getDataScope())){
						dataScope.add(r.getDataScope());
					}
				}
			}
			// 如果没有全部数据权限，并设置了用户别名，则当前权限为本人；如果未设置别名，当前无权限为已植入权限
			if (!isDataScopeAll){
				if (StringUtils.isNotBlank(userAlias)){
					for (String ua : StringUtils.split(userAlias, ",")){
						sqlString.append(" OR " + ua + ".create_by = '" + user.getEmpNo() + "'");
					}
				}else {
					for (String oa : StringUtils.split(brnAlias, ",")){
						//sqlString.append(" OR " + oa + ".id  = " + user.getBrn().getId());
						sqlString.append(" OR " + oa + ".id IS NULL");
					}
				}
			}else{
				// 如果包含全部权限，则去掉之前添加的所有条件，并跳出循环。
				sqlString = new StringBuilder();
			}
		}
		if (StringUtils.isNotBlank(sqlString.toString())){
			return " AND (" + sqlString.substring(4) + ")";
		}
		return "";
	}


	/**
	 * 数据范围过滤（符合业务表字段不同的时候使用，采用exists方法）
	 * @param entity 当前过滤的实体类
	 * @param sqlMapKey sqlMap的键值，例如设置“dsf”时，调用方法：${sqlMap.sdf}
	 * @param brnWheres brn表条件，组成：部门表字段=业务表的部门字段
	 * @param userWheres user表条件，组成：用户表字段=业务表的用户字段
	 * @example
	 * 		dataScopeFilter(user, "dsf", "id=a.office_id", "id=a.create_by");
	 * 		dataScopeFilter(entity, "dsf", "code=a.jgdm", "no=a.cjr"); // 适应于业务表关联不同字段时使用，如果关联的不是机构id是code。
	 */
	public static void dataScopeFilter(BaseEntity<?> entity, String sqlMapKey, String brnWheres, String userWheres) {

		User user = getUser();
		
		// 如果是超级管理员，则不过滤数据
		if (user.isGod()) {
			return;
		}

		// 数据范围（1：所有数据；2：所在公司及以下数据；3：所在公司数据；4：所在部门及以下数据；5：所在部门数据；8：仅本人数据；9：按明细设置）
		StringBuilder sqlString = new StringBuilder();
		
		// 获取到最大的数据权限范围
		//Long roleId = 0L;
		int dataScopeInteger = 8;
		for (Role r : user.getRoleList()){
			int ds = Integer.valueOf(r.getDataScope());
			if (ds == 9){
				//roleId = r.getId();
				dataScopeInteger = ds;
				break;
			}else if (ds < dataScopeInteger){
				//roleId = r.getId();
				dataScopeInteger = ds;
			}
		}
		String dataScopeString = String.valueOf(dataScopeInteger);
		
		// 生成部门权限SQL语句
		for (String where : StringUtils.split(brnWheres, ",")){
			if (Role.DATA_SCOPE_COMPANY_AND_CHILD.equals(dataScopeString)){
				// 包括本公司下的部门 （type=1:公司；type=2：部门）
				sqlString.append(" AND EXISTS (SELECT 1 FROM SYS_BRN");
				sqlString.append(" WHERE type='2'");
				sqlString.append(" AND (id = '" + user.getBrn().getParentId() + "'");
				sqlString.append(" OR parent_ids LIKE '" + user.getBrn().getParent().getParentIds() + "%')");
				sqlString.append(" AND " + where +")");
			}
			else if (Role.DATA_SCOPE_COMPANY.equals(dataScopeString)){
				sqlString.append(" AND EXISTS (SELECT 1 FROM SYS_BRN");
				sqlString.append(" WHERE type='2'");
				sqlString.append(" AND id = '" + user.getBrn().getParentId() + "'");
				sqlString.append(" AND " + where +")");
			}
			else if (Role.DATA_SCOPE_OFFICE_AND_CHILD.equals(dataScopeString)){
				sqlString.append(" AND EXISTS (SELECT 1 FROM SYS_BRN");
				sqlString.append(" WHERE (id = '" + user.getBrn().getId() + "'");
				sqlString.append(" OR parent_ids LIKE '" + user.getBrn().getParentIds() + "%')");
				sqlString.append(" AND " + where +")");
			}
			else if (Role.DATA_SCOPE_OFFICE.equals(dataScopeString)){
				sqlString.append(" AND EXISTS (SELECT 1 FROM SYS_BRN");
				sqlString.append(" WHERE id = '" + user.getBrn().getId() + "'");
				sqlString.append(" AND " + where +")");
			}
			else if (Role.DATA_SCOPE_CUSTOM.equals(dataScopeString)){
//				sqlString.append(" AND EXISTS (SELECT 1 FROM sys_role_office ro123456, SYS_BRN o123456");
//				sqlString.append(" WHERE ro123456.office_id = o123456.id");
//				sqlString.append(" AND ro123456.role_id = '" + roleId + "'");
//				sqlString.append(" AND o123456." + where +")");
			}
		}
		// 生成个人权限SQL语句
		for (String where : StringUtils.split(userWheres, ",")){
			if (Role.DATA_SCOPE_SELF.equals(dataScopeString)){
				sqlString.append(" AND EXISTS (SELECT 1 FROM sys_user");
				sqlString.append(" WHERE id='" + user.getId() + "'");
				sqlString.append(" AND " + where + ")");
			}
		}

		// 设置到自定义SQL对象
		entity.getSqlMap().put(sqlMapKey, sqlString.toString());
		
	}

	public static void dataScopeFilter(BaseEntity<?> entity, String sqlMapKey, String brnFactor, String prjFactor,String userFactor) {

		User user = getUser();
		
		// 如果是超级管理员，则不过滤数据
		if (user.isGod()) {
			return;
		}

		// 数据范围（1：所有数据；2：所在公司及以下数据；3：所在公司数据；4：所在部门及以下数据；5：所在部门数据；6：所在项目数据；8：仅本人数据；9：按明细设置）
		StringBuilder sqlString = new StringBuilder();
		
		// 获取到最大的数据权限范围
		int dataScopeInteger = 8;//默认只能看本人的数据
		for (Role r : user.getRoleList()){
			int ds = Integer.valueOf(r.getDataScope());
			if (ds == 9){
				dataScopeInteger = ds;
				break;
			}else if (ds < dataScopeInteger){
				dataScopeInteger = ds;
			}
		}
	
		// 生成部门权限SQL语句
		if(brnFactor!=null){
			if (dataScopeInteger==1){
				// 不做过滤
			}
			else if (dataScopeInteger==2){
				// 不做过滤
			}
			else if (dataScopeInteger==3){
				// 不做过滤
			}
			else if (dataScopeInteger==4){
				sqlString.append(" AND " + brnFactor+"IN ("+user.getBrn().getBrnNo()+")");
			}
			else if (dataScopeInteger==5){
				sqlString.append(" AND " + brnFactor+"="+user.getBrn().getBrnNo());
			}
		}
		// 生成项目权限SQL语句
		if(prjFactor!=null){
			if (dataScopeInteger==6){
				sqlString.append(" AND " + prjFactor+"="+user.getEmpNo());
			}
		}
		// 生成个人权限SQL语句
		if(userFactor!=null){
			if (dataScopeInteger==8){
				sqlString.append(" AND " + userFactor+"="+user.getEmpNo());
			}
		}


		// 设置到自定义SQL对象
		entity.getSqlMap().put(sqlMapKey, sqlString.toString());
		
	}
}
