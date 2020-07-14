package com.jxf.ufang.util;

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
import com.jxf.svc.utils.StringUtils;
import com.jxf.ufang.dao.UfangBrnDao;
import com.jxf.ufang.dao.UfangMenuDao;
import com.jxf.ufang.dao.UfangUserActDao;
import com.jxf.ufang.dao.UfangUserDao;
import com.jxf.ufang.entity.UfangBrn;
import com.jxf.ufang.entity.UfangMenu;
import com.jxf.ufang.entity.UfangRole;
import com.jxf.ufang.entity.UfangUser;
import com.jxf.ufang.entity.UfangUserAct;


/**
 * 用户工具类
 * @author jxf
 * @version 2015-07-28
 */
public class UfangUserUtils {

	private static UfangUserDao userDao = SpringContextHolder.getBean(UfangUserDao.class);
	private static UfangUserActDao userActDao = SpringContextHolder.getBean(UfangUserActDao.class);
	private static UfangBrnDao brnDao = SpringContextHolder.getBean(UfangBrnDao.class);
	
	private static UfangMenuDao menuDao = SpringContextHolder.getBean(UfangMenuDao.class);
	private static AreaDao areaDao = SpringContextHolder.getBean(AreaDao.class);


	public static final String USER_CACHE = "ufangUserCache";
	public static final String USER_CACHE_ID_ = "id_";
	public static final String USER_CACHE_LOGIN_NAME_ = "ln";
	public static final String USER_CACHE_LIST_BY_BRN_ID_ = "oid_";

	public static final String CACHE_ROLE_LIST = "ufangRoleList";
	public static final String CACHE_MENU_LIST = "ufangMenuList";
	public static final String CACHE_AREA_LIST = "areaList";
	public static final String CACHE_BRN_LIST = "ufangBrnList";
	public static final String CACHE_BRN_ALL_LIST = "ufangBrnAllList";
	
	/**
	 * 根据ID获取用户
	 * @param id
	 * @return 取不到返回null
	 */
	public static UfangUser get(Long id){
		UfangUser user = (UfangUser)CacheUtils.get(USER_CACHE, USER_CACHE_ID_ + id);
		if (user ==  null){
			user = userDao.get(id);
			if (user == null){
				return null;
			}
			user.setRoleList(userDao.findUserRoleList(user));
			CacheUtils.put(USER_CACHE, USER_CACHE_ID_ + user.getId(), user);
			CacheUtils.put(USER_CACHE, USER_CACHE_LOGIN_NAME_ + user.getUsername(), user);
		}
		return user;
	}
	
	/**
	 * 根据登录名获取用户
	 * @param username
	 * @return 取不到返回null
	 */
	public static UfangUser getByUsername(String username){
		UfangUser user = (UfangUser)CacheUtils.get(USER_CACHE, USER_CACHE_LOGIN_NAME_ + username);
		if (user == null){
			user = userDao.getByUsername(username);
			if (user == null){
				return null;
			}
			user.setRoleList(userDao.findUserRoleList(user));
			CacheUtils.put(USER_CACHE, USER_CACHE_ID_ + user.getId(), user);
			CacheUtils.put(USER_CACHE, USER_CACHE_LOGIN_NAME_ + user.getUsername(), user);
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
		UfangUserUtils.clearCache(getUser());
	}
	
	/**
	 * 清除指定用户缓存
	 * @param user
	 */
	public static void clearCache(UfangUser user){
		CacheUtils.remove(USER_CACHE, USER_CACHE_ID_ + user.getId());
		CacheUtils.remove(USER_CACHE, USER_CACHE_LOGIN_NAME_ + user.getUsername());
//		if (user.getBrn() != null && user.getBrn().getId() != null){
//			CacheUtils.remove(USER_CACHE, USER_CACHE_LIST_BY_BRN_ID_ + user.getBrn().getId());
//		}
	}
	
	/**
	 * 获取当前用户
	 * @return 取不到返回 new User()
	 */
	public static UfangUser getUser(){
		Principal principal = getPrincipal();
		UfangUser ufang = getByUsername("666666");
		if (principal!=null){
			UfangUser user = get(principal.getId());
			if (user != null){
				return user;
			}
			return ufang;
		}
		// 如果没有登录，则返回实例化空的User对象。
		return ufang;
	}

	/**
	 * 获取当前用户角色列表
	 * @return
	 */
	public static List<UfangRole> getRoleList(){
		@SuppressWarnings("unchecked")
		List<UfangRole> roleList = (List<UfangRole>)getCache(CACHE_ROLE_LIST);
		if (roleList == null){
			UfangUser user = getUser();	
			roleList = userDao.findUserRoleList(user);	
			putCache(CACHE_ROLE_LIST, roleList);
		}
		return roleList;
	}
	
	/**
	 * 获取当前用户授权菜单
	 * @return
	 */
	public static List<UfangMenu> getMenuList(){
		@SuppressWarnings("unchecked")
		List<UfangMenu> menuList = (List<UfangMenu>)getCache(CACHE_MENU_LIST);
		if (menuList == null){
			UfangUser user = getUser();
			menuList = menuDao.findByUfangUserId(user.getId());
	
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
	public static List<UfangBrn> getBrnList(){
		@SuppressWarnings("unchecked")
		List<UfangBrn> brnList = (List<UfangBrn>)getCache(CACHE_BRN_LIST);
		if (brnList == null){
			UfangBrn brn = new UfangBrn();
			brn.getSqlMap().put("dsf", dataScopeFilter("a", ""));
			brnList = brnDao.findList(brn);

			putCache(CACHE_BRN_LIST, brnList);
		}
		return brnList;
	}

	/**
	 * 获取当前用户有权限访问的部门
	 * @return
	 */
	public static List<UfangBrn> getBrnAllList(){
		@SuppressWarnings("unchecked")
		List<UfangBrn> brnList = (List<UfangBrn>)getCache(CACHE_BRN_ALL_LIST);
		if (brnList == null){
			brnList = brnDao.findAllList(new UfangBrn());
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
	public static String  genNewEmpNo(UfangBrn brn){
		String new_emp_no="";
		String max_emp_no=userDao.getMaxEmpNo(brn.getId());
		String brnNo = brnDao.get(brn).getBrnNo();
		if(!StringUtils.isNullOrEmpty(max_emp_no)){
			new_emp_no=brnNo+String.format("%02x", Long.parseLong(max_emp_no.substring(6, 8),16)+1).toUpperCase();
		}else{
			new_emp_no = brnNo+"01";
		}
		return new_emp_no;
		
	}
	public static String getEmpNamByEmpNo(String empNo){
		
	    String empName = userDao.getEmpNamByEmpNo(empNo);
	    
	    return empName;
	}
	
	public static List<UfangUser> getUserList(Long brnId){
		
		UfangUser user = new UfangUser();
		user.setBrn(new UfangBrn(brnId));
		List<UfangUser> userList = userDao.findList(user);
				
		return userList;
	}
	
	public static String getByUserAndSubNo(Long userId, String subNo) {
		
		UfangUserAct ufangUserAct = new UfangUserAct();
		ufangUserAct.setUser(userDao.get(userId));
		ufangUserAct.setSubNo(subNo);
		return StringUtils.decimalToStr(userActDao.getByUserAndSubNo(ufangUserAct).getCurBal(), 2);
	}
	
	/**
	 * 数据范围过滤
	 * @param brnAlias 机构表别名，多个用“,”逗号隔开。
	 * @param userAlias 用户表别名，多个用“,”逗号隔开，传递空，忽略此参数
	 * @return 标准连接条件对象
	 */
	public static String dataScopeFilter(String brnAlias, String userAlias) {

		StringBuilder sqlString = new StringBuilder();
		
		// 进行权限过滤，多个角色权限范围之间为或者关系。
		List<String> dataScope = Lists.newArrayList();
		
		UfangUser user = getUser();

		for (UfangRole r : user.getRoleList()){
			for (String brn : StringUtils.split(brnAlias, ",")){
				if (!dataScope.contains(r.getDataScope()) && StringUtils.isNotBlank(brn)){

					if (UfangRole.DATA_SCOPE_COMPANY.equals(r.getDataScope())){
						sqlString.append(" OR " + brn + ".id = '" + user.getBrn().getParentId() + "'");
						sqlString.append(" OR " + brn + ".parent_id = '" + user.getBrn().getParentId() + "'");
					}
					else if (UfangRole.DATA_SCOPE_OFFICE.equals(r.getDataScope())){
						sqlString.append(" OR " + brn + ".id = '" + user.getBrn().getId() + "'");
					}
				
					dataScope.add(r.getDataScope());
				}
			}
		}
		// 如果设置了用户别名，则当前权限为本人；
		if (StringUtils.isNotBlank(userAlias)){
			for (String ua : StringUtils.split(userAlias, ",")){
				sqlString.append(" OR " + ua + ".create_by = '" + user.getEmpNo() + "'");
			}
		}
		
		if (StringUtils.isNotBlank(sqlString.toString())){
			return " AND (" + sqlString.substring(4) + ")";
		}
		return "";
	}
	
	/**
	 * 数据范围过滤
	 * @param brnAlias 机构表别名，多个用“,”逗号隔开。
	 * @param userAlias 用户表别名，多个用“,”逗号隔开，传递空，忽略此参数
	 * @return 标准连接条件对象
	 */
	public static String dataScopeFilter1(String brnAlias, String userAlias) {

		StringBuilder sqlString = new StringBuilder();
		
		// 进行权限过滤，多个角色权限范围之间为或者关系。
		List<String> dataScope = Lists.newArrayList();
		
		UfangUser user = getUser();

		for (UfangRole r : user.getRoleList()){
			for (String brn : StringUtils.split(brnAlias, ",")){
				if (!dataScope.contains(r.getDataScope()) && StringUtils.isNotBlank(brn)){

					if (UfangRole.DATA_SCOPE_COMPANY.equals(r.getDataScope())){
						sqlString.append(" OR " + brn + ".id = '" + user.getBrn().getParentId() + "'");
						sqlString.append(" OR " + brn + ".parent_id = '" + user.getBrn().getParentId() + "'");
					}
					else if (UfangRole.DATA_SCOPE_OFFICE.equals(r.getDataScope())){
						sqlString.append(" OR " + brn + ".id = '" + user.getBrn().getId() + "'");
					}
				
					dataScope.add(r.getDataScope());
				}
			}
		}
		// 如果设置了用户别名，则当前权限为本人；
		if (StringUtils.isNotBlank(userAlias)){
			for (String ua : StringUtils.split(userAlias, ",")){
				sqlString.append(" OR " + ua + ".create_by = '" + user.getEmpNo() + "'");
			}
		}
		
		if (StringUtils.isNotBlank(sqlString.toString())){
			return " AND (" + sqlString.substring(4) + ")";
		}
		return "";
	}
}
