package com.jxf.web.admin.ufang;

import java.util.Date;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jxf.mem.entity.Member;
import com.jxf.mem.service.MemberService;
import com.jxf.mem.utils.VerifiedUtils;
import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;

import com.jxf.svc.sys.util.UserUtils;

import com.jxf.svc.utils.JSONUtil;
import com.jxf.svc.utils.SnowFlake;

import com.jxf.ufang.entity.UfangAnalysisBean;
import com.jxf.ufang.entity.UfangLoanMarketApplyer;
import com.jxf.ufang.service.UfangLoanMarketApplyerService;
import com.jxf.ufang.util.LoanAnalysisUtils;

import com.jxf.web.admin.sys.BaseController;

/**
 * 优放贷申请人Controller
 * @author suHuimin
 * @version 2019-03-27
 */
@Controller
@RequestMapping(value = "${adminPath}/loanMarketApplyer")
public class UfangLoanMarketApplyerController extends BaseController {

	@Autowired
	private UfangLoanMarketApplyerService ufangLoanMarketApplyerService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@ModelAttribute
	public UfangLoanMarketApplyer get(@RequestParam(required=false) Long id) {
		UfangLoanMarketApplyer entity = null;
		if (id!=null){
			entity = ufangLoanMarketApplyerService.get(id);
		}
		if (entity == null){
			entity = new UfangLoanMarketApplyer();
		}
		return entity;
	}
	
	@RequiresPermissions("ufangdebt:ufangLoanMarketApplyer:view")
	@RequestMapping(value = {"list", ""})
	public String list(UfangLoanMarketApplyer ufangLoanMarketApplyer, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<UfangLoanMarketApplyer> page = ufangLoanMarketApplyerService.findPage(new Page<UfangLoanMarketApplyer>(request, response), ufangLoanMarketApplyer); 
		
//		User user = UserUtils.getUser();
//		if(user.getMask()==1) {
//			List<UfangLoanMarketApplyer> applyerList = page.getList();
//			for (UfangLoanMarketApplyer applyer : applyerList) {
//				String phoneNo = EncryptUtils.encryptString(applyer.getPhoneNo(), Type.PHONE);
//				applyer.setPhoneNo(phoneNo);
//			}
//		}
		model.addAttribute("page", page);
		return "admin/ufang/loanMarketApplyer/ufangLoanMarketApplyerList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("ufangdebt:ufangLoanMarketApplyer:view")
	@RequestMapping(value = "add")
	public String add(UfangLoanMarketApplyer ufangLoanMarketApplyer, Model model) {
		model.addAttribute("UfangLoanMarketApplyer", ufangLoanMarketApplyer);
		return "admin/ufang/loanMarketApplyer/ufangLoanMarketApplyerAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("ufangdebt:ufangLoanMarketApplyer:view")
	@RequestMapping(value = "query")
	public String query(UfangLoanMarketApplyer ufangLoanMarketApplyer, Model model) {
		model.addAttribute("UfangLoanMarketApplyer", ufangLoanMarketApplyer);
		return "admin/ufang/loanMarketApplyer/ufangLoanMarketApplyerQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("ufangdebt:ufangLoanMarketApplyer:view")
	@RequestMapping(value = "update")
	public String update(UfangLoanMarketApplyer ufangLoanMarketApplyer, Model model) {
		model.addAttribute("UfangLoanMarketApplyer", ufangLoanMarketApplyer);
		return "admin/ufang/loanMarketApplyer/ufangLoanMarketApplyerUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("ufangdebt:ufangLoanMarketApplyer:edit")
	@RequestMapping(value = "save")
	public String save(UfangLoanMarketApplyer ufangLoanMarketApplyer, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, ufangLoanMarketApplyer)){
			return add(ufangLoanMarketApplyer, model);
		}
		ufangLoanMarketApplyerService.save(ufangLoanMarketApplyer);
		addMessage(redirectAttributes, "保存优放贷申请人成功");
		return "redirect:"+Global.getAdminPath()+"/loanMarketApplyer/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("ufangdebt:ufangLoanMarketApplyer:edit")
	@RequestMapping(value = "delete")
	public String delete(UfangLoanMarketApplyer ufangLoanMarketApplyer, RedirectAttributes redirectAttributes) {
		ufangLoanMarketApplyerService.delete(ufangLoanMarketApplyer);
		addMessage(redirectAttributes, "删除优放贷申请人成功");
		return "redirect:"+Global.getAdminPath()+"/loanMarketApplyer/?repage";
	}
	
	/**
	 * 	风险画像
	 * @throws Exception 
	 */
	@RequiresPermissions("ufangdebt:ufangLoanMarketApplyer:view")
	@RequestMapping(value = "applyerAnalysis")
	public String applyerAnalysis(String phoneNo, RedirectAttributes redirectAttributes,Model model) throws Exception {
		UfangAnalysisBean analysisBean = new UfangAnalysisBean();

		Member member = memberService.findByUsername(phoneNo);
		if(member==null) {
			addMessage(redirectAttributes, "用户还未注册无忧借条APP");
			return "redirect:"+Global.getAdminPath()+"/loanMarketApplyer/?repage";
		}
		Integer verifiedList = member.getVerifiedList();
		if(VerifiedUtils.isVerified(verifiedList,1)&&VerifiedUtils.isVerified(verifiedList,2)) {
			String idNo = member.getIdNo();
			LoanAnalysisUtils analysis = new LoanAnalysisUtils();
			JSONObject result = analysis.getResult(idNo);
			String header = result.getString("header");
			Map<String, Object> map = JSONUtil.toMap(header);
			String ret_code = (String) map.get("ret_code");
			if(StringUtils.equals("000000", ret_code)) {
				String body = result.getString("body");
				Object content = JSON.parse(body);
				analysisBean.setId(SnowFlake.getId());
				analysisBean.setMemberId(member.getId());
				analysisBean.setUserId(UserUtils.getUser().getId());
				analysisBean.setContent(content);
				analysisBean.setCreateTime(new Date());
					
				mongoTemplate.save(analysisBean);
				model.addAttribute("content",content);
			}else {
				addMessage(redirectAttributes, (String)map.get("ret_msg"));
				return "redirect:"+Global.getAdminPath()+"/loanMarketApplyer/?repage";
			}
			logger.debug("===========借贷分析返回RESULT："+result);
		}else {
			addMessage(redirectAttributes, "未进行实名认证,不能查看");
			return "redirect:"+Global.getAdminPath()+"/loanMarketApplyer/?repage";
		}
		return "admin/ufang/loanMarketApplyer/applyerAnalysis";		
	}

}