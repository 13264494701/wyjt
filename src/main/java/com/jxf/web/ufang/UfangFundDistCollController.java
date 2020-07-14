package com.jxf.web.ufang;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jxf.nfs.constant.ActSubConstant;
import com.jxf.ufang.entity.*;
import com.jxf.ufang.service.*;
import com.jxf.ufang.util.UfangUserUtils;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jxf.nfs.constant.TrxRuleConstant;
import com.jxf.nfs.service.NfsActService;

import com.jxf.svc.model.Message;
import com.jxf.svc.persistence.Page;

import java.math.BigDecimal;


/**
 * 资金分发归集Controller
 * @author wo
 * @version 2018-11-22
 */
@Controller("ufangFundDistCollController")
@RequestMapping(value = "${ufangPath}/ufangFundDistColl")
public class UfangFundDistCollController extends UfangBaseController {

	@Autowired
	private UfangFundDistCollService ufangFundDistCollService;
	@Autowired
	private UfangBrnActService brnActService;
	@Autowired
	private UfangUserActService userActService;
	@Autowired
	private NfsActService actService;
	@Autowired
	private UfangBrnService brnService;
	@Autowired
	private UfangUserService userService;

	
	@ModelAttribute
	public UfangFundDistColl get(@RequestParam(required=false) Long id) {
		UfangFundDistColl entity = null;
		if (id!=null){
			entity = ufangFundDistCollService.get(id);
		}
		if (entity == null){
			entity = new UfangFundDistColl();
		}
		return entity;
	}
	
	@RequiresPermissions("ufang:fundDistColl:view")
	@RequestMapping(value = {"list", ""})
	public String list(UfangFundDistColl ufangFundDistColl, HttpServletRequest request, HttpServletResponse response, Model model) {
		UfangUser ufangUser = UfangUserUtils.getUser();
		UfangBrn ufangBrn = brnService.get(ufangUser.getBrn()).getParent();
		UfangBrnAct brnAct = brnActService.getBrnAct(ufangBrn, ActSubConstant.UFANG_BRN_AVL_BAL);
		ufangFundDistColl.setBrnAct(brnAct);
		Page<UfangFundDistColl> page = ufangFundDistCollService.findPage(new Page<UfangFundDistColl>(request, response), ufangFundDistColl); 
		model.addAttribute("page", page);
		return "ufang/dc/ufangFundDistCollList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("ufang:fundDistColl:view")
	@RequestMapping(value = "add")
	public String add(UfangFundDistColl ufangFundDistColl, Model model) {
		model.addAttribute("ufangFundDistColl", ufangFundDistColl);
		return "ufang/dc/ufangFundDistCollAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("ufang:fundDistColl:view")
	@RequestMapping(value = "query")
	public String query(UfangFundDistColl ufangFundDistColl, Model model) {
		model.addAttribute("ufangFundDistColl", ufangFundDistColl);
		return "ufang/dc/ufangFundDistCollQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("ufang:fundDistColl:view")
	@RequestMapping(value = "update")
	public String update(UfangFundDistColl ufangFundDistColl, Model model) {
		model.addAttribute("ufangFundDistColl", ufangFundDistColl);
		return "ufang/dc/ufangFundDistCollUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("ufang:fundDistColl:edit")
	@RequestMapping(value = "save")
	public String save(UfangFundDistColl ufangFundDistColl, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, ufangFundDistColl)){
			return add(ufangFundDistColl, model);
		}
		ufangFundDistCollService.save(ufangFundDistColl);
		addMessage(redirectAttributes, "保存资金分发归集成功");
		return "redirect:"+ufangPath+"/ufangFundDistColl/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("ufang:fundDistColl:edit")
	@RequestMapping(value = "delete")
	public String delete(UfangFundDistColl ufangFundDistColl, RedirectAttributes redirectAttributes) {
		ufangFundDistCollService.delete(ufangFundDistColl);
		addMessage(redirectAttributes, "删除资金分发归集成功");
		return "redirect:"+ufangPath+"/ufangFundDistColl/?repage";
	}

	/**
	 * 回收交易金
	 */
	@RequiresPermissions("ufang:fundDistColl:view")
	@RequestMapping(value = "collectFund",method = RequestMethod.GET)
	public String collectFund(UfangFundDistColl ufangFundDistColl, Model model) {
		ufangFundDistColl.setType(UfangFundDistColl.Type.coll);
		UfangUserAct userAct = userActService.get(ufangFundDistColl.getUserAct());
		ufangFundDistColl.setUserAct(userAct);
		model.addAttribute("ufangFundDistColl", ufangFundDistColl);
		return "ufang/dc/ufangFundCollAdd";
	}


	/**
	 * 回收交易金
	 */
	@RequiresPermissions("ufang:fundDistColl:edit")
	@RequestMapping(value = "collectFund",method = RequestMethod.POST)
	@ResponseBody
	public Message collectFund(UfangFundDistColl ufangFundDistColl) {
		UfangUser ufangUser = ufangFundDistColl.getUserAct().getUser();
		BigDecimal amount = ufangFundDistColl.getAmount();
		BigDecimal act = userActService.getUserAct(ufangUser, ActSubConstant.UFANG_USER_AVL_BAL).getCurBal();

		if (amount.compareTo(act) > 0) {
			return Message.error("超过用户账户总额");
		}
		UfangBrn ufangBrn = brnService.get(userService.get(ufangUser).getBrn().getParent());;
		UfangBrnAct ufangBrnAct = brnActService.getBrnAct(ufangBrn,ActSubConstant.UFANG_BRN_AVL_BAL);

		ufangFundDistColl.setBrnAct(ufangBrnAct);
		ufangFundDistColl.setUser(ufangUser);

		ufangFundDistColl.setCurrCode("CNY");
		ufangFundDistColl.setType(UfangFundDistColl.Type.coll);
		ufangFundDistColl.setStatus(UfangFundDistColl.Status.SUCC);
		ufangFundDistCollService.save(ufangFundDistColl);

		actService.updateAct(TrxRuleConstant.UFANG_FUND_COLL, ufangFundDistColl.getAmount(), ufangBrn, ufangUser,ufangFundDistColl.getId());

		return Message.success("回收交易金成功");
	}


	/**
	 * 派发交易金
	 */
	@RequiresPermissions("ufang:fundDistColl:view")
	@RequestMapping(value = "distributeFund",method = RequestMethod.GET)
	public String distributeFund(UfangFundDistColl ufangFundDistColl, Model model) {
		ufangFundDistColl.setType(UfangFundDistColl.Type.dist);
		UfangBrnAct brnAct = brnActService.get(ufangFundDistColl.getBrnAct());
		ufangFundDistColl.setBrnAct(brnAct);
		model.addAttribute("ufangFundDistColl", ufangFundDistColl);	
		return "ufang/dc/ufangFundDistCollAdd";
	}
	
	
	/**
	 * 派发交易金
	 */
	@RequiresPermissions("ufang:fundDistColl:edit")
	@RequestMapping(value = "distributeFund",method = RequestMethod.POST)
	@ResponseBody
	public Message distributeFund(UfangFundDistColl ufangFundDistColl) {

		BigDecimal amount = ufangFundDistColl.getAmount();
		BigDecimal act = brnActService.getBrnAct(ufangFundDistColl.getBrnAct().getCompany(), ActSubConstant.UFANG_BRN_AVL_BAL).getCurBal();

		if (amount.compareTo(act) > 0) {
			return Message.error("超过公司账户总额");
		}

		ufangFundDistColl.setCurrCode("CNY");
		ufangFundDistColl.setType(UfangFundDistColl.Type.dist);
		ufangFundDistColl.setStatus(UfangFundDistColl.Status.SUCC);
		ufangFundDistCollService.save(ufangFundDistColl);

		//公司账户流水
//		brnActTrx.setRmk("派发交易金["+amount.toString()+"元]到员工["+ ufangUser.getEmpNam()+"]账户");
		//员工账户流水
//		userActTrx.setRmk("入账交易金[" + amount.toString() + "元]");
		
		actService.updateAct(TrxRuleConstant.UFANG_FUND_DIST, ufangFundDistColl.getAmount(),ufangFundDistColl.getBrnAct().getCompany(), ufangFundDistColl.getUser(),ufangFundDistColl.getId());
	
		
		return Message.success("派发交易金成功");
	}

}