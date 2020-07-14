package com.jxf.web.ufang;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jxf.cms.entity.CmsNotice;
import com.jxf.cms.service.CmsNoticeService;
import com.jxf.mem.entity.Member;
import com.jxf.mem.service.MemberService;
import com.jxf.nfs.constant.ActSubConstant;
import com.jxf.svc.config.Global;
import com.jxf.svc.security.MD5Utils;
import com.jxf.svc.security.PasswordUtils;
import com.jxf.svc.security.rsa.RSAService;
import com.jxf.svc.utils.CalendarUtil;
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.StringUtils;
import com.jxf.ufang.entity.UfangLoaneeDataOrder;
import com.jxf.ufang.entity.UfangUser;
import com.jxf.ufang.service.UfangLoaneeDataOrderService;
import com.jxf.ufang.service.UfangUserActService;
import com.jxf.ufang.service.UfangUserService;
import com.jxf.ufang.util.UfangUserUtils;



/**
 * 用户Controller
 * @author wo
 * @version 2018-10-20
 */
@Controller("ufangMineController")
@RequestMapping(value = "${ufangPath}/mine")
public class UfangMineController extends UfangBaseController {

	@Autowired
	private UfangUserService userService;
	@Autowired
	private UfangUserActService actService;
    @Autowired
    private MemberService memberService;
	@Autowired
	private RSAService rsaService;
	@Autowired
	private CmsNoticeService noticeService;
	
    @Autowired
    private UfangLoaneeDataOrderService ufangLoaneeDataOrderService;


	
	
	@ModelAttribute
	public UfangUser get(@RequestParam(required=false) Long id) {
		if (id!=null){
			return userService.getUser(id);
		}else{
			return new UfangUser();
		}
	}
	/**
	 * 工作面板
	 * @param user
	 * @param model
	 * @return
	 */
	@RequiresPermissions("mine:view")
	@RequestMapping(value = "dashboard")
	public String dashboard(HttpServletResponse response, Model model) {
		UfangUser currentUser = UfangUserUtils.getUser();
		CmsNotice notice = new CmsNotice();
		notice.setPosition(CmsNotice.Position.ufangMarquee);
		notice.setIsPub(true);
		List<CmsNotice> noticeList = noticeService.findList(notice);
		model.addAttribute("noticeList", noticeList);
		
		Map<String, String> today_loanee_data_order_cnt_map = ufangLoaneeDataOrderService.countByEmpNo(currentUser,0);     
		model.addAttribute("today_loanee_data_order_cnt", today_loanee_data_order_cnt_map.get("cnt"));
		model.addAttribute("today_loanee_data_order_amt", today_loanee_data_order_cnt_map.get("amt"));
		
		Map<String, String> yesterday_loanee_data_order_cnt_map = ufangLoaneeDataOrderService.countByEmpNo(currentUser,1);     
		model.addAttribute("yesterday_loanee_data_order_cnt", yesterday_loanee_data_order_cnt_map.get("cnt"));
		model.addAttribute("yesterday_loanee_data_order_amt", yesterday_loanee_data_order_cnt_map.get("amt"));
		
		BigDecimal curBal = actService.getUserAct(currentUser, ActSubConstant.UFANG_USER_AVL_BAL).getCurBal();
		model.addAttribute("curBal", curBal);
		
		
		
		
		model.addAttribute("user", currentUser);
		model.addAttribute("Global", new Global());
		return "ufang/mine/dashboard";
	}
	/**
	 * 个人中心
	 * @param user
	 * @param model
	 * @return
	 */
	@RequiresPermissions("mine:view")
	@RequestMapping(value = "info")
	public String info(UfangUser user, HttpServletResponse response, Model model) {
		UfangUser currentUser = UfangUserUtils.getUser();

		model.addAttribute("user", currentUser);
		model.addAttribute("Global", new Global());
		return "ufang/mine/mineInfo";
	}
	
	/**
	 * 修改个人用户密码
	 * @param password
	 * @param model
	 * @return
	 */
	@RequiresPermissions("mine:view")
	@RequestMapping(value = "updatePwd", method = RequestMethod.GET)
	public String modifyPwd(Model model) {
		
		UfangUser user = UfangUserUtils.getUser();
		model.addAttribute("user", user);
		return "ufang/mine/updatePwd";
	}
	
	/**
	 * 修改个人用户密码
	 * @param oldPassword
	 * @param newPassword
	 * @param model
	 * @return
	 */
	@RequiresPermissions("mine:edit")
	@RequestMapping(value = "updatePwd", method = RequestMethod.POST)
	public String modifyPwd(HttpServletRequest request, Model model) {
		
		String oldPassword = rsaService.decryptParameter("oldPassword", (HttpServletRequest)request);
		String newPassword = rsaService.decryptParameter("newPassword", (HttpServletRequest)request);
		rsaService.removePrivateKey((HttpServletRequest)request);
		
		UfangUser user = UfangUserUtils.getUser();
		if (StringUtils.isNotBlank(oldPassword) && StringUtils.isNotBlank(newPassword)){
			if (PasswordUtils.validatePassword(MD5Utils.EncoderByMd5(oldPassword).toUpperCase(), user.getPassword())){
				userService.updatePasswordById(user.getId(), MD5Utils.EncoderByMd5(newPassword).toUpperCase());
				model.addAttribute("message", "修改密码成功");
			}else{
				model.addAttribute("message", "修改密码失败，旧密码错误");
			}
		}
		model.addAttribute("user", user);
		return "ufang/mine/updatePwd";
	}
	
	/**
	 * 绑定无忧借条账号
	 * @param password
	 * @param model
	 * @return
	 */
	@RequiresPermissions("mine:view")
	@RequestMapping(value = "bindMember", method = RequestMethod.GET)
	public String bindMember(Model model) {
		
		UfangUser user = UfangUserUtils.getUser();
		if(user.getBindStatus().equals(UfangUser.BindStatus.binded)){
			Member member = memberService.get(user.getMember());
			model.addAttribute("member", member);
		}
		model.addAttribute("user", user);
		return "ufang/mine/bindMember";
	}
	/**
	 * 绑定无忧借条账号
	 * @param password
	 * @param model
	 * @return
	 */
	@RequiresPermissions("mine:edit")
	@RequestMapping(value = "bindMember",  method = RequestMethod.POST)
	public String bindMember(HttpServletRequest request, Model model,RedirectAttributes redirectAttributes) {
		String username = request.getParameter("username");
		String password = rsaService.decryptParameter("password", (HttpServletRequest)request);
		rsaService.removePrivateKey((HttpServletRequest)request);
		
		UfangUser user = UfangUserUtils.getUser();
		Member member = memberService.findByUsername(username);
		if(member==null) {
			addMessage(redirectAttributes, "绑定无忧借条账号失败[003]，请联系客服。");
			model.addAttribute("user", user);
			return "redirect:" + ufangPath + "/mine/bindMember?repage";
		}
		String enPassword = MD5Utils.EncoderByMd5("cpbao.com_友信宝" + MD5Utils.EncoderByMd5(password).toLowerCase()).toLowerCase();
		if (!PasswordUtils.validatePassword(enPassword, member.getPassword())){			
			addMessage(redirectAttributes, "绑定无忧借条账号失败，无忧借条账号密码错误");
			model.addAttribute("user", user);
			return "redirect:" + ufangPath + "/mine/bindMember?repage";
		}
		user.setMember(member);
		user.setBindStatus(UfangUser.BindStatus.binded);
		userService.saveUser(user);
		addMessage(redirectAttributes, "绑定无忧借条账号成功");
		model.addAttribute("user", user);
		return "redirect:" + ufangPath + "/mine/info?repage";
	}
	/**
	 * 解除绑定借条账号
	 * @param password
	 * @param model
	 * @return
	 */
	@RequiresPermissions("mine:edit")
	@RequestMapping(value = "relieve")
	public String relieve(HttpServletRequest request, Model model,RedirectAttributes redirectAttributes) {
		UfangUser user = UfangUserUtils.getUser();
		user.setBindStatus(UfangUser.BindStatus.unbind);
		userService.saveUser(user);
		addMessage(redirectAttributes, "解除绑定成功");
		model.addAttribute("user", user);
		return "redirect:"+ ufangPath +"/mine/bindMember";
	}
	
}
