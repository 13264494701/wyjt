package com.jxf.web.admin.sys;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jxf.svc.config.Global;
import com.jxf.svc.security.PasswordUtils;
import com.jxf.svc.security.rsa.RSAService;
import com.jxf.svc.sys.user.entity.User;
import com.jxf.svc.sys.user.service.UserService;
import com.jxf.svc.sys.util.UserUtils;
import com.jxf.svc.utils.StringUtils;



/**
 * 用户Controller
 * @author jxf
 * @version 2015-07-28
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/mine")
public class MineController extends BaseController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private RSAService rsaService;
	
	@ModelAttribute
	public User get(@RequestParam(required=false) Long id) {
		if (id!=null){
			return userService.getUser(id);
		}else{
			return new User();
		}
	}

	/**
	 * 个人中心
	 * @param user
	 * @param model
	 * @return
	 */
	@RequiresPermissions("mine")
	@RequestMapping(value = "info")
	public String info(User user, HttpServletResponse response, Model model) {
		User currentUser = UserUtils.getUser();
		if (StringUtils.isNotBlank(user.getEmpNam())){
			currentUser.setEmail(user.getEmail());
			currentUser.setPhone(user.getPhone());
			currentUser.setMobile(user.getMobile());
			currentUser.setRmk(user.getRmk());
			currentUser.setPhoto(user.getPhoto());
			userService.updateUserInfo(currentUser);
			model.addAttribute("message", "保存用户信息成功");
		}
		model.addAttribute("user", currentUser);
		model.addAttribute("Global", new Global());
		return "admin/sys/mine/mineInfo";
	}
	
	/**
	 * 修改个人用户密码
	 * @param oldPassword
	 * @param newPassword
	 * @param model
	 * @return
	 */
	@RequiresPermissions("mine")
	@RequestMapping(value = "updatePwd")
	public String modifyPwd(HttpServletRequest request, Model model) {
		String oldPassword = rsaService.decryptParameter("oldPassword", (HttpServletRequest)request);
		String newPassword = rsaService.decryptParameter("newPassword", (HttpServletRequest)request);
		rsaService.removePrivateKey((HttpServletRequest)request);
		User user = UserUtils.getUser();
		if (StringUtils.isNotBlank(oldPassword) && StringUtils.isNotBlank(newPassword)){
			if (PasswordUtils.validatePassword(oldPassword, user.getPassword())){
				userService.updatePasswordById(user.getId(), newPassword);
				model.addAttribute("message", "修改密码成功");
			}else{
				model.addAttribute("message", "修改密码失败，旧密码错误");
			}
		}
		model.addAttribute("user", user);
		return "admin/sys/mine/updatePwd";
	}
	
}
