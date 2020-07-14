package com.jxf.web.admin.sys;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jxf.svc.sys.menu.service.MenuService;
import com.jxf.svc.sys.role.entity.Role;
import com.jxf.svc.sys.role.service.RoleService;
import com.jxf.svc.sys.user.entity.User;
import com.jxf.svc.sys.user.service.UserService;
import com.jxf.svc.sys.util.UserUtils;
import com.jxf.svc.utils.Collections3;


/**
 * 角色Controller
 * @author jxf
 * @version 2015-07-28
 */
@Controller("adminRoleController")
@RequestMapping(value = "${adminPath}/sys/role")
public class RoleController extends BaseController {

	@Autowired
	private RoleService roleService;
	@Autowired
	private MenuService menuService;
	@Autowired
	private UserService userService;
	
	@ModelAttribute("role")
	public Role get(@RequestParam(required=false) Long id) {
		if (id!=null){
			return roleService.getRole(id);
		}else{
			return new Role();
		}
	}
	
	@RequiresPermissions("sys:role:view")
	@RequestMapping(value = {"list", ""})
	public String list(Role role, Model model) {
		List<Role> list = roleService.findAllRole();
		model.addAttribute("list", list);
		return "admin/sys/role/roleList";
	}

	@RequiresPermissions("sys:role:view")
	@RequestMapping(value = "add")
	public String form(Role role, Model model) {
		model.addAttribute("role", role);
		model.addAttribute("menuList", menuService.findAllMenu());
		return "admin/sys/role/roleAdd";
	}
	
	/**
	 * 角色信息修改查询
	 * @param role
	 * @param model
	 * @return
	 */
	@RequiresPermissions("sys:role:view")
	@RequestMapping(value = "update")
	public String updateform(Role role, Model model) {
		model.addAttribute("role", role);
		model.addAttribute("menuList", menuService.findAllMenu());
		return "admin/sys/role/roleUpdate";
	}
	
	/**
	 * 角色名称查看功能
	 * @param role
	 * @param model
	 * @return
	 */
	@RequiresPermissions("sys:role:view")
	@RequestMapping(value = "query")
	public String formQuery(Role role, Model model) {
		model.addAttribute("role", role);
		model.addAttribute("menuList", menuService.findAllMenu());
		return "admin/sys/role/roleQuery";
	}
	
	
	@RequiresPermissions("sys:role:edit")
	@RequestMapping(value = "save")
	public String save(Role role, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, role)){
			return form(role, model);
		}
		if (!"true".equals(checkName(role.getOldName(), role.getRoleName()))){
			addMessage(model, "保存角色'" + role.getRoleName() + "'失败, 角色名已存在");
			return form(role, model);
		}
		roleService.saveRole(role);
		addMessage(redirectAttributes, "保存角色'" + role.getRoleName() + "'成功");
		return "redirect:" + adminPath + "/sys/role/?repage";
	}
	
	@RequiresPermissions("sys:role:edit")
	@RequestMapping(value = "delete")
	public String delete(Role role, RedirectAttributes redirectAttributes) {

		if (Role.isGod(role.getId())){
			addMessage(redirectAttributes, "删除角色失败, 不允许删除内置角色");
		}else if (UserUtils.getUser().getRoleIdList().contains(role.getId())){
			addMessage(redirectAttributes, "删除角色失败, 不能删除当前用户所在角色");
		}else{
			roleService.deleteRole(role);
			addMessage(redirectAttributes, "删除角色成功");
		}
		return "redirect:" + adminPath + "/sys/role/?repage";
	}
	
	/**
	 * 角色分配页面
	 * @param role
	 * @param model
	 * @return
	 */
	@RequiresPermissions("sys:role:edit")
	@RequestMapping(value = "assign")
	public String assign(Role role, Model model) {
		List<User> userList = userService.findUser(new User(new Role(role.getId())));
		model.addAttribute("userList", userList);
		return "admin/sys/role/roleAssign";
	}
	
	/**
	 * 角色分配 -- 打开角色分配对话框
	 * @param role
	 * @param model
	 * @return
	 */
	@RequiresPermissions("sys:role:view")
	@RequestMapping(value = "usertorole")
	public String selectUserToRole(Role role, Model model) {
		List<User> userList = userService.findUser(new User(new Role(role.getId())));
		model.addAttribute("role", role);
		model.addAttribute("userList", userList);
		model.addAttribute("selectIds", Collections3.extractToString(userList, "name", ","));
		return "admin/sys/role/selectUserToRole";
	}
	
//	/**
//	 * 角色分配 -- 根据部门编号获取用户列表
//	 * @param officeId
//	 * @param response
//	 * @return
//	 */
//	@RequiresPermissions("sys:role:view")
//	@ResponseBody
//	@RequestMapping(value = "users")
//	public List<Map<String, Object>> users(String officeId, HttpServletResponse response) {
//		List<Map<String, Object>> mapList = Lists.newArrayList();
//		User user = new User();
//		Page<User> page = userService.findUser(new Page<User>(1, -1), user);
//		for (User e : page.getList()) {
//			Map<String, Object> map = Maps.newHashMap();
//			map.put("id", e.getId());
//			map.put("pId", 0);
//			map.put("name", e.getName());
//			mapList.add(map);			
//		}
//		return mapList;
//	}
	

	/**
	 * 验证角色名是否有效
	 * @param oldName
	 * @param name
	 * @return
	 */
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "checkName")
	public String checkName(String oldName, String name) {
		if (name!=null && name.equals(oldName)) {
			return "true";
		} else if (name!=null && roleService.getRoleByName(name) == null) {
			return "true";
		}
		return "false";
	}
	
	

}
