package com.jxf.web.admin.sys;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jxf.svc.excel.ExportExcel;
import com.jxf.svc.excel.ImportExcel;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.security.PasswordUtils;
import com.jxf.svc.sys.user.service.UserService;
import com.jxf.svc.sys.brn.entity.Brn;
import com.jxf.svc.sys.role.entity.Role;
import com.jxf.svc.sys.role.service.RoleService;
import com.jxf.svc.sys.user.entity.User;
import com.jxf.svc.sys.util.UserUtils;
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.StringUtils;
import com.jxf.svc.validator.BeanValidators;


/**
 * 用户Controller
 * @author jxf
 * @version 2015-07-28
 */
@Controller("adminUserController")
@RequestMapping(value = "${adminPath}/sys/user")
public class UserController extends BaseController {

	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;
	
	@ModelAttribute
	public User get(@RequestParam(required=false) Long id) {
		if (id!=null){
			return userService.getUser(id);
		}else{
			return new User();
		}
	}

	@RequiresPermissions("sys:user:view")
	@RequestMapping(value = {"index"})
	public String index(User user, Model model) {
		return "admin/sys/user/userIndex";
	}

	@RequiresPermissions("sys:user:view")
	@RequestMapping(value = {"list", ""})
	public String list(User user, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<User> page = userService.findUser(new Page<User>(request, response), user);
        model.addAttribute("page", page);
		return "admin/sys/user/userList";
	}

	@RequiresPermissions("sys:user:view")
	@RequestMapping(value = "add")
	public String form(User user, Model model) {
		if (user.getBrn()==null || user.getBrn().getId()==null){
			user.setBrn(UserUtils.getUser().getBrn());
		}
		model.addAttribute("user", user);
		model.addAttribute("allRoles", roleService.findAllRole());
		return "admin/sys/user/userAdd";
	}
	
	/**
	 * 用户列表中查看用户信息
	 * @param user
	 * @param model
	 * @return
	 */
	@RequiresPermissions("sys:user:view")
	@RequestMapping(value = "query")
	public String selectForm(User user, Model model) {
		if (user.getBrn()==null || user.getBrn().getId()==null){
			user.setBrn(UserUtils.getUser().getBrn());
		}
		model.addAttribute("user", user);
		model.addAttribute("allRoles", roleService.findAllRole());
		return "admin/sys/user/userQuery";
	}
	/**
	 * 用户信息修改查询
	 * @param user
	 * @param model
	 * @return
	 */
	@RequiresPermissions("sys:user:view")
	@RequestMapping(value = "update",method = RequestMethod.GET)
	public String alterForm(User user, Model model) {
		if (user.getBrn()==null || user.getBrn().getId()==null){
			user.setBrn(UserUtils.getUser().getBrn());
		}
		model.addAttribute("user", user);
		model.addAttribute("allRoles", roleService.findAllRole());
		return "admin/sys/user/userUpdate";
	}
	
	/**
	 * 修改员工信息
	 * @param user
	 * @param request
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("sys:user:edit")
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(User user, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
		// 修正引用赋值问题，不知道为何，Company和Brn引用的一个实例地址，修改了一个，另外一个跟着修改。
		Long brnId = Long.parseLong(request.getParameter("brn.id"));
		user.setBrn(new Brn(brnId));
		if (!beanValidator(model, user)){
			return form(user, model);
		}
		// 角色数据有效性验证，过滤不在授权内的角色
		List<Role> roleList = Lists.newArrayList();
		List<Long> roleIdList = user.getRoleIdList();
		for (Role r : roleService.findAllRole()){
			if (roleIdList.contains(r.getId())){
				roleList.add(r);
			}
		}
		user.setRoleList(roleList);
		// 修改用户信息
		userService.updateUser(user);
		// 清除当前用户缓存
		if (user.getLoginName().equals(UserUtils.getUser().getLoginName())){
			UserUtils.clearCache();
		}
		addMessage(redirectAttributes, "修改员工'" + user.getLoginName() + "'成功");
		return "redirect:" + adminPath + "/sys/user/list?repage";
	}
	
	@RequiresPermissions("sys:user:edit")
	@RequestMapping(value = "save")
	public String save(User user, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
		Long brnId = Long.parseLong(request.getParameter("brn.id"));
		user.setBrn(new Brn(brnId));
		// 默认设置密码为6个1
		user.setPassword(PasswordUtils.entryptPassword("111111"));
		if (!beanValidator(model, user)){
			return form(user, model);
		}
//		if (!"true".equals(checkLoginName(user.getOldLoginName(), user.getLoginName()))){
//			addMessage(model, "保存用户'" + user.getLoginName() + "'失败，登录名已存在");
//			return form(user, model);
//		}
		// 角色数据有效性验证，过滤不在授权内的角色
		List<Role> roleList = Lists.newArrayList();
		List<Long> roleIdList = user.getRoleIdList();
		for (Role r : roleService.findAllRole()){
			if (roleIdList.contains(r.getId())){
				roleList.add(r);
			}
		}
		user.setRoleList(roleList);
		// 保存用户信息
		userService.saveUser(user);
		// 清除当前用户缓存
		if (user.getLoginName().equals(UserUtils.getUser().getLoginName())){
			UserUtils.clearCache();
		}
		addMessage(redirectAttributes, "保存用户'" + user.getLoginName() + "'成功");
		return "redirect:" + adminPath + "/sys/user/list?repage";
	}
	

	
	
	/**
	 * 重置员工密码
	 * @param user
	 * @param request
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("sys:user:edit")
	@RequestMapping(value = "resetPwd")
	public String resetPwd(User user, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {

		// 修改用户信息
		userService.updatePasswordById(user.getId(), "111111");

		addMessage(redirectAttributes, "员工'" + user.getLoginName() + "'密码重置为111111");
		return "redirect:" + adminPath + "/sys/user/list?repage";
	}
	
	@RequiresPermissions("sys:user:edit")
	@RequestMapping(value = "delete")
	public String delete(User user, RedirectAttributes redirectAttributes) {
		if (UserUtils.getUser().getId().equals(user.getId())){
			addMessage(redirectAttributes, "删除用户失败, 不允许删除当前用户");
		}else if (User.isGod(user.getId())){
			addMessage(redirectAttributes, "删除用户失败, 不允许删除内置用户");
		}else{
			userService.deleteUser(user);
			addMessage(redirectAttributes, "删除用户成功");
		}
		return "redirect:" + adminPath + "/sys/user/list?repage";
	}
	
	/**
	 * 导出用户数据
	 * @param user
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("sys:user:view")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(User user, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "用户数据"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<User> page = userService.findUser(new Page<User>(request, response, -1), user);
            ExportExcel exprotExcel = new ExportExcel("用户数据", User.class);
            exprotExcel.setDataList(page.getList());
            exprotExcel.write(response, fileName);
            exprotExcel.dispose();
            return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出用户失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/sys/user/list?repage";
    }

	/**
	 * 导入用户数据
	 * @param file
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("sys:user:edit")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<User> list = ei.getDataList(User.class);
			for (User user : list){
				try{
					if ("true".equals(checkLoginName("", user.getLoginName()))){
						user.setPassword(PasswordUtils.entryptPassword("111111"));
						BeanValidators.validateWithException(validator, user);
						userService.saveUser(user);
						successNum++;
					}else{
						failureMsg.append("<br/>登录名 "+user.getLoginName()+" 已存在; ");
						failureNum++;
					}
				}catch(ConstraintViolationException ex){
					failureMsg.append("<br/>登录名 "+user.getLoginName()+" 导入失败：");
					List<String> messageList = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
					for (String message : messageList){
						failureMsg.append(message+"; ");
						failureNum++;
					}
				}catch (Exception ex) {
					failureMsg.append("<br/>登录名 "+user.getLoginName()+" 导入失败："+ex.getMessage());
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条用户，导入信息如下：");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条用户"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入用户失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/sys/user/list?repage";
    }
	
	/**
	 * 下载导入用户数据模板
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("sys:user:view")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "用户数据导入模板.xlsx";
    		List<User> list = Lists.newArrayList(); list.add(UserUtils.getUser());
    		new ExportExcel("用户数据", User.class, 2).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/sys/user/list?repage";
    }

	/**
	 * 验证登录名是否有效
	 * @param oldLoginName
	 * @param loginName
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions("sys:user:edit")
	@RequestMapping(value = "checkLoginName")
	public String checkLoginName(String oldLoginName, String loginName) {
		if (loginName !=null && loginName.equals(oldLoginName)) {
			return "true";
		} else if (loginName !=null && userService.getUserByLoginName(loginName) == null) {
			return "true";
		}
		return "false";
	}


	
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) Long brnId, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<User> list = userService.findUserByBrnId(brnId);
		for (int i=0; i<list.size(); i++){
			User user = list.get(i);
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", "u_"+user.getId());
			map.put("pId", brnId);
			map.put("name", StringUtils.replace(user.getEmpNam(), " ", ""));
			mapList.add(map);
		}
		return mapList;
	}
}
