package com.jxf.web.admin.ufang;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.jxf.svc.security.MD5Utils;
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
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.StringUtils;
import com.jxf.svc.validator.BeanValidators;
import com.jxf.ufang.entity.UfangBrn;
import com.jxf.ufang.entity.UfangRole;
import com.jxf.ufang.entity.UfangUser;
import com.jxf.ufang.service.UfangRoleService;
import com.jxf.ufang.service.UfangUserService;
import com.jxf.ufang.util.UfangUserUtils;
import com.jxf.web.admin.sys.BaseController;


/**
 * 用户Controller
 * @author jxf
 * @version 2015-07-28
 */
@Controller("adminUfangUserController")
@RequestMapping(value = "${adminPath}/ufang/user")
public class UfangUserController extends BaseController {

	@Autowired
	private UfangUserService userService;
	@Autowired
	private UfangRoleService roleService;
	
	@ModelAttribute
	public UfangUser get(@RequestParam(required=false) Long id) {
		if (id!=null){
			return userService.getUser(id);
		}else{
			return new UfangUser();
		}
	}

	@RequiresPermissions("ufang:user:view")
	@RequestMapping(value = {"index"})
	public String index(UfangUser user, Model model) {
		return "admin/ufang/user/userIndex";
	}

	@RequiresPermissions("ufang:user:view")
	@RequestMapping(value = {"list", ""})
	public String list(UfangUser user, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<UfangUser> page = userService.findUser(new Page<UfangUser>(request, response), user);
		model.addAttribute("user", user);
        model.addAttribute("page", page);
		return "admin/ufang/user/userList";
	}

	@RequiresPermissions("ufang:user:view")
	@RequestMapping(value = "add")
	public String form(UfangUser user, Model model) {
//		if (user.getBrn()==null || user.getBrn().getId()==null){
//			user.setBrn(UfangUserUtils.getUser().getBrn());
//		}
		model.addAttribute("user", user);
		model.addAttribute("allRoles", roleService.findAllRole());
		return "admin/ufang/user/userAdd";
	}
	
	/**
	 * 用户列表中查看用户信息
	 * @param user
	 * @param model
	 * @return
	 */
	@RequiresPermissions("ufang:user:view")
	@RequestMapping(value = "query")
	public String selectForm(UfangUser user, Model model) {
//		if (user.getBrn()==null || user.getBrn().getId()==null){
//			user.setBrn(UfangUserUtils.getUser().getBrn());
//		}

		model.addAttribute("user", user);
		model.addAttribute("allRoles", roleService.findAllRole());
		return "admin/ufang/user/userQuery";
	}
	/**
	 * 用户信息修改查询
	 * @param user
	 * @param model
	 * @return
	 */
	@RequiresPermissions("ufang:user:view")
	@RequestMapping(value = "update",method = RequestMethod.GET)
	public String alterForm(UfangUser user, Model model) {
//		if (user.getBrn()==null || user.getBrn().getId()==null){
//			user.setBrn(UfangUserUtils.getUser().getBrn());
//		}
		model.addAttribute("user", user);
		model.addAttribute("allRoles",roleService.findAllRole());
		return "admin/ufang/user/userUpdate";
	}
	
	/**
	 * 修改员工信息
	 * @param user
	 * @param request
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("ufang:user:edit")
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(UfangUser user, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
		// 修正引用赋值问题，不知道为何，Company和Brn引用的一个实例地址，修改了一个，另外一个跟着修改。
		Long brnId = Long.parseLong(request.getParameter("brn.id"));
		user.setBrn(new UfangBrn(brnId));
		if (!beanValidator(model, user)){
			return form(user, model);
		}
		// 角色数据有效性验证，过滤不在授权内的角色
		List<UfangRole> roleList = Lists.newArrayList();
		List<Long> roleIdList = user.getRoleIdList();
		for (UfangRole r : roleService.findAllRole()){
			if (roleIdList.contains(r.getId())){
				roleList.add(r);
			}
		}
		user.setRoleList(roleList);
		// 修改用户信息
		userService.updateUser(user);
		addMessage(redirectAttributes, "修改员工'" + user.getUsername() + "'成功");
		return "redirect:" + adminPath + "/ufang/user/list?repage";
	}
	
	@RequiresPermissions("ufang:user:edit")
	@RequestMapping(value = "save")
	public String save(UfangUser user, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
		Long brnId = Long.parseLong(request.getParameter("brn.id"));
		user.setBrn(new UfangBrn(brnId));
		if (!beanValidator(model, user)){
			return form(user, model);
		}
//		if (!"true".equals(checkLoginName(user.getOldLoginName(), user.getLoginName()))){
//			addMessage(model, "保存用户'" + user.getLoginName() + "'失败，登录名已存在");
//			return form(user, model);
//		}
		// 角色数据有效性验证，过滤不在授权内的角色
		List<UfangRole> roleList = Lists.newArrayList();
		List<Long> roleIdList = user.getRoleIdList();
		for (UfangRole r : roleService.findAllRole()){
			if (roleIdList.contains(r.getId())){
				roleList.add(r);
			}
		}
		user.setRoleList(roleList);
		// 保存用户信息
		userService.saveUser(user);
		addMessage(redirectAttributes, "保存用户'" + user.getEmpNam() + "'成功，初始密码111111");
		return "redirect:" + adminPath + "/ufang/user/list?repage";
	}
	

	
	
	/**
	 * 重置员工密码
	 * @param user
	 * @param request
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("ufang:user:edit")
	@RequestMapping(value = "resetPwd")
	public String resetPwd(UfangUser user, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {


		userService.updatePasswordById(user.getId(), MD5Utils.EncoderByMd5("111111").toUpperCase());
		addMessage(redirectAttributes, "员工'" + user.getUsername() + "'密码重置为111111");
		return "redirect:" + adminPath + "/ufang/user/list?repage";
	}
	
	@RequiresPermissions("ufang:user:edit")
	@RequestMapping(value = "delete")
	public String delete(UfangUser user, RedirectAttributes redirectAttributes) {

		userService.deleteUser(user);
		addMessage(redirectAttributes, "删除用户成功");

		return "redirect:" + adminPath + "/ufang/user/list?repage";
	}
	
	/**
	 * 导出用户数据
	 * @param user
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("ufang:user:view")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(UfangUser user, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "用户数据"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<UfangUser> page = userService.findUser(new Page<UfangUser>(request, response, -1), user);
            ExportExcel exprotExcel = new ExportExcel("用户数据", UfangUser.class);
            exprotExcel.setDataList(page.getList());
            exprotExcel.write(response, fileName);
            exprotExcel.dispose();
            return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出用户失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/ufang/user/list?repage";
    }

	/**
	 * 导入用户数据
	 * @param file
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("ufang:user:edit")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<UfangUser> list = ei.getDataList(UfangUser.class);
			for (UfangUser user : list){
				try{
					if ("true".equals(checkUsername(user.getUsername()))){
						user.setPassword(PasswordUtils.entryptPassword(MD5Utils.EncoderByMd5("111111").toUpperCase()));
						BeanValidators.validateWithException(validator, user);
						userService.saveUser(user);
						successNum++;
					}else{
						failureMsg.append("<br/>登录名 "+user.getUsername()+" 已存在; ");
						failureNum++;
					}
				}catch(ConstraintViolationException ex){
					failureMsg.append("<br/>登录名 "+user.getUsername()+" 导入失败：");
					List<String> messageList = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
					for (String message : messageList){
						failureMsg.append(message+"; ");
						failureNum++;
					}
				}catch (Exception ex) {
					failureMsg.append("<br/>登录名 "+user.getUsername()+" 导入失败："+ex.getMessage());
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条用户，导入信息如下：");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条用户"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入用户失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/ufang/user/list?repage";
    }
	
	/**
	 * 下载导入用户数据模板
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("ufang:user:view")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "用户数据导入模板.xlsx";
    		List<UfangUser> list = Lists.newArrayList(); list.add(UfangUserUtils.getUser());
    		new ExportExcel("用户数据", UfangUser.class, 2).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/ufang/user/list?repage";
    }

	/**
	 * 验证登录名是否有效
	 * @param oldLoginName
	 * @param loginName
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions("ufang:user:edit")
	@RequestMapping(value = "checkUsername")
	public String checkUsername(String username) {
        if (username !=null && userService.getUserByUsername(username) == null) {
			return "true";
		}
		return "false";
	}
	
	@RequiresPermissions("admin")
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) Long brnId, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<UfangUser> list = userService.findUserByBrnId(brnId);
		for (int i=0; i<list.size(); i++){
			UfangUser user = list.get(i);
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", "u_"+user.getId());
			map.put("pId", brnId);
			map.put("name", StringUtils.replace(user.getEmpNam(), " ", ""));
			mapList.add(map);
		}
		return mapList;
	}
}
