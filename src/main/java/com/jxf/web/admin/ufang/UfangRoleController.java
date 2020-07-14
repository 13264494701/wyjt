package com.jxf.web.admin.ufang;

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


import com.jxf.svc.utils.Collections3;
import com.jxf.ufang.entity.UfangRole;
import com.jxf.ufang.entity.UfangUser;
import com.jxf.ufang.service.UfangMenuService;
import com.jxf.ufang.service.UfangRoleService;
import com.jxf.ufang.service.UfangUserService;
import com.jxf.web.admin.sys.BaseController;


/**
 * 优放角色Controller
 * @author wo
 * @version 2018-11-26
 */
@Controller("adminUfangRoleController")
@RequestMapping(value = "${adminPath}/ufang/role")
public class UfangRoleController extends BaseController {

	@Autowired
	private UfangRoleService roleService;
	@Autowired
	private UfangMenuService menuService;
	@Autowired
	private UfangUserService userService;
	
	@ModelAttribute
	public UfangRole get(@RequestParam(required=false) Long id) {
		if (id!=null){
			return roleService.getRole(id);
		}else{
			return new UfangRole();
		}
	}
	
	@RequiresPermissions("ufang:role:view")
	@RequestMapping(value = {"list", ""})
	public String list(UfangRole role, Model model) {
		List<UfangRole> list = roleService.findList(role);
		model.addAttribute("list", list);
		return "admin/ufang/role/roleList";
	}

	@RequiresPermissions("ufang:role:view")
	@RequestMapping(value = "add")
	public String form(UfangRole role, Model model) {
		model.addAttribute("role", role);
		model.addAttribute("menuList", menuService.findAllMenu());
		return "admin/ufang/role/roleAdd";
	}
	
	/**
	 * 角色信息修改查询
	 * @param role
	 * @param model
	 * @return
	 */
	@RequiresPermissions("ufang:role:view")
	@RequestMapping(value = "update")
	public String updateform(UfangRole role, Model model) {
		model.addAttribute("role", role);
		model.addAttribute("menuList", menuService.findAllMenu());
		return "admin/ufang/role/roleUpdate";
	}
	
	/**
	 * 角色名称查看功能
	 * @param role
	 * @param model
	 * @return
	 */
	@RequiresPermissions("ufang:role:view")
	@RequestMapping(value = "query")
	public String formQuery(UfangRole role, Model model) {
		model.addAttribute("role", role);
		model.addAttribute("menuList", menuService.findAllMenu());
		return "admin/ufang/role/roleQuery";
	}
	
	
	@RequiresPermissions("ufang:role:edit")
	@RequestMapping(value = "save")
	public String save(UfangRole role, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, role)){
			return form(role, model);
		}
		if (!"true".equals(checkName(role.getOldName(), role.getRoleName()))){
			addMessage(model, "保存角色'" + role.getRoleName() + "'失败, 角色名已存在");
			return form(role, model);
		}
		roleService.saveRole(role);
		addMessage(redirectAttributes, "保存角色'" + role.getRoleName() + "'成功");
		return "redirect:" + adminPath + "/ufang/role/?repage";
	}
	
	@RequiresPermissions("ufang:role:edit")
	@RequestMapping(value = "delete")
	public String delete(UfangRole role, RedirectAttributes redirectAttributes) {

		roleService.delete(role);
		addMessage(redirectAttributes, "删除角色成功");

		return "redirect:" + adminPath + "/ufang/role/?repage";
	}
	
	/**
	 * 角色分配页面
	 * @param role
	 * @param model
	 * @return
	 */
	@RequiresPermissions("ufang:role:edit")
	@RequestMapping(value = "assign")
	public String assign(UfangRole role, Model model) {
		List<UfangUser> userList = userService.findUser(new UfangUser(new UfangRole(role.getId())));
		model.addAttribute("userList", userList);
		return "admin/ufang/role/roleAssign";
	}
	
	/**
	 * 角色分配 -- 打开角色分配对话框
	 * @param role
	 * @param model
	 * @return
	 */
	@RequiresPermissions("ufang:role:view")
	@RequestMapping(value = "usertorole")
	public String selectUserToRole(UfangRole role, Model model) {
		List<UfangUser> userList = userService.findUser(new UfangUser(new UfangRole(role.getId())));
		model.addAttribute("role", role);
		model.addAttribute("userList", userList);
		model.addAttribute("selectIds", Collections3.extractToString(userList, "name", ","));
		return "admin/ufang/role/selectUserToRole";
	}
	
//	/**
//	 * 角色分配 -- 根据部门编号获取用户列表
//	 * @param officeId
//	 * @param response
//	 * @return
//	 */
//	@RequiresPermissions("ufang:role:view")
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
