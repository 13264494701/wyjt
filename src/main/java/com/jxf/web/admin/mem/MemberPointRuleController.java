package com.jxf.web.admin.mem;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.text.StringEscapeUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import com.jxf.mem.entity.MemberPointRule;
import com.jxf.mem.service.MemberPointRuleService;
import com.jxf.svc.config.Global;
import com.jxf.svc.model.AjaxRsp;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.utils.StringUtils;
import com.jxf.web.admin.sys.BaseController;


/**
 * 积分规则Controller
 * @author wo
 * @version 2018-08-12
 */
@Controller
@RequestMapping(value = "${adminPath}/memberPointRule")
public class MemberPointRuleController extends BaseController {

	@Autowired
	private MemberPointRuleService memberPointRuleService;
	
	@ModelAttribute
	public MemberPointRule get(@RequestParam(required=false) Long id) {
		MemberPointRule entity = null;
		if (id!=null){
			entity = memberPointRuleService.get(id);
		}
		if (entity == null){
			entity = new MemberPointRule();
		}
		return entity;
	}
	
	@RequiresPermissions("point:memberPointRule:view")
	@RequestMapping(value = {"list", ""})
	public String list(MemberPointRule memberPointRule, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MemberPointRule> page = memberPointRuleService.findPage(new Page<MemberPointRule>(request, response), memberPointRule); 
		model.addAttribute("page", page);
		return "mem/point/rule/memberPointRuleList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("point:memberPointRule:view")
	@RequestMapping(value = "add")
	public String add(MemberPointRule memberPointRule, Model model) {
		model.addAttribute("memberPointRule", memberPointRule);
		return "mem/point/rule/memberPointRuleAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("point:memberPointRule:view")
	@RequestMapping(value = "query")
	public String query(MemberPointRule memberPointRule, Model model) {
		model.addAttribute("memberPointRule", memberPointRule);
		return "mem/point/rule/memberPointRuleQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("point:memberPointRule:view")
	@RequestMapping(value = "update")
	public String update(MemberPointRule memberPointRule, Model model) {
		model.addAttribute("memberPointRule", memberPointRule);
		return "mem/point/rule/memberPointRuleUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("point:memberPointRule:edit")
	@RequestMapping(value = "save")
	public String save(MemberPointRule memberPointRule, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, memberPointRule)){
			return add(memberPointRule, model);
		}
		memberPointRule.setPointExpression(StringEscapeUtils.unescapeHtml4(memberPointRule.getPointExpression()));
		memberPointRuleService.save(memberPointRule);
		addMessage(redirectAttributes, "保存积分规则成功");
		return "redirect:"+Global.getAdminPath()+"/memberPointRule/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("point:memberPointRule:edit")
	@RequestMapping(value = "delete")
	public String delete(MemberPointRule memberPointRule, RedirectAttributes redirectAttributes) {
		memberPointRuleService.delete(memberPointRule);
		addMessage(redirectAttributes, "删除积分规则成功");
		return "redirect:"+Global.getAdminPath()+"/memberPointRule/?repage";
	}
	/**
     * 启用停用规则
     *
     * @param id
     * @param sts
     * @return
     */
    @RequiresPermissions("point:memberPointRule:edit")
    @RequestMapping("enableRule")
    @ResponseBody
    public AjaxRsp enableRule(String id, String sts) {
    	
        AjaxRsp rsp = new AjaxRsp();
        if (StringUtils.equals("0", sts)) {
        	memberPointRuleService.enableRule(id, sts);
            rsp.setStatus(true);
            rsp.setMessage("规则已停用");
        } else {
        	memberPointRuleService.enableRule(id, sts);
            rsp.setStatus(true);
            rsp.setMessage("规则已启用");
        }
        return rsp;
    }
	
	
}