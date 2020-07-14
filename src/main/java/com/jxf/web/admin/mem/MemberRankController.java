package com.jxf.web.admin.mem;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jxf.mem.entity.MemberRank;
import com.jxf.mem.service.MemberRankService;
import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.web.admin.sys.BaseController;

/**
 * 
 * @类功能说明： 会员等级
 * @类修改者： 
 * @修改日期： 
 * @修改说明： 
 * @公司名称：北京金信服科技有限公司
 * @作者：HUOJIABAO 
 * @创建时间：2016年4月25日 下午4:37:13 
 * @版本：V1.0
 */
@Controller
@RequestMapping(value = "${adminPath}/member/memberRank")
public class MemberRankController extends BaseController {

	@Autowired
	private MemberRankService memberRankService;
	
	@ModelAttribute
	public MemberRank get(@RequestParam(required=false) Long id) {
		MemberRank entity = null;
		if (id!=null){
			entity = memberRankService.get(id);
		}
		if (entity == null){
			entity = new MemberRank();
		}
		return entity;
	}
	
	@RequiresPermissions("mem:memberRank:view")
	@RequestMapping(value = {"list", ""})
	public String list(MemberRank memberRank, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MemberRank> page = memberRankService.findPage(new Page<MemberRank>(request, response), memberRank); 
		model.addAttribute("page", page);
		return "admin/mem/rank/memberRankList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("mem:memberRank:view")
	@RequestMapping(value = "add")
	public String add(MemberRank memberRank, Model model) {
		model.addAttribute("memberRank", memberRank);
		return "admin/mem/rank/memberRankAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("mem:memberRank:view")
	@RequestMapping(value = "query")
	public String query(MemberRank memberRank, Model model) {
		model.addAttribute("memberRank", memberRank);
		return "admin/mem/rank/memberRankQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("mem:memberRank:view")
	@RequestMapping(value = "update")
	public String update(MemberRank memberRank, Model model) {
		model.addAttribute("memberRank", memberRank);
		return "admin/mem/rank/memberRankUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("mem:memberRank:edit")
	@RequestMapping(value = "save")
	public String save(MemberRank memberRank, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, memberRank)){
			return add(memberRank, model);
		}
		memberRankService.save(memberRank);
		addMessage(redirectAttributes, "保存会员等级成功");
		return "redirect:"+Global.getAdminPath()+"/member/memberRank/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("mem:memberRank:edit")
	@RequestMapping(value = "delete")
	public String delete(MemberRank memberRank, RedirectAttributes redirectAttributes) {
		memberRankService.delete(memberRank);
		addMessage(redirectAttributes, "删除会员等级成功");
		return "redirect:"+Global.getAdminPath()+"/member/memberRank/?repage";
	}
	/**
	 * 设置默认
	 */
	@RequiresPermissions("mem:memberRank:edit")
	@RequestMapping(value = "setdefault")
	public String setDefault(MemberRank memberRank, RedirectAttributes redirectAttributes) {
		memberRankService.setDefault(memberRank);		
		return "redirect:"+Global.getAdminPath()+"/member/memberRank/?repage";
	}
}