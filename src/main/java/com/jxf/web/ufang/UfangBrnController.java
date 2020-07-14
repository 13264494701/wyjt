package com.jxf.web.ufang;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jxf.svc.config.Global;
import com.jxf.svc.utils.StringUtils;
import com.jxf.ufang.entity.UfangBrn;
import com.jxf.ufang.service.UfangBrnService;
import com.jxf.ufang.service.UfangUserService;
import com.jxf.ufang.util.UfangUserUtils;


/**
 * 机构Controller
 * @author jxf
 * @version 2015-07-28
 */
@Controller("ufangBrnController")
@RequestMapping(value = "${ufangPath}/brn")
public class UfangBrnController extends UfangBaseController {

	@Autowired
	private UfangBrnService brnService;
	@Autowired
	private UfangUserService userService;
	
	@ModelAttribute
	public UfangBrn get(@RequestParam(required=false) Long id) {
		if (id!=null){
			return brnService.get(id);
		}else{
			return new UfangBrn();
		}
	}

	@RequiresPermissions("ufang:brn:view")
	@RequestMapping(value = {""})
	public String index(UfangBrn brn, Model model) {
        model.addAttribute("list", brnService.findAll());
		return "ufang/brn/brnIndex";
	}

	@RequiresPermissions("ufang:brn:view")
	@RequestMapping(value = {"list"})
	public String list(UfangBrn brn, Model model) {
		brn.getSqlMap().put("dsf", UfangUserUtils.dataScopeFilter("a", ""));
		List<UfangBrn> list = brnService.findList(brn);
        model.addAttribute("list", list);
		return "ufang/brn/brnList";
	}
	
	@RequiresPermissions("ufang:brn:view")
	@RequestMapping(value = "add")
	public String add(UfangBrn brn, Model model) {

		if (brn.getParent()==null || brn.getParent().getId()==null){
			brn.setParent(brnService.get(1L));//如果没有上级机构，则上级机构默认为平台
			brn.setArea(brn.getParent().getArea());
		}else {
			brn.setParent(brnService.get(brn.getParent()));
			brn.setArea(brn.getParent().getArea());
		}

		model.addAttribute("brn", brn);
		return "ufang/brn/brnAdd";
	}
	
	
	
	@RequiresPermissions("ufang:brn:view")
	@RequestMapping(value = "update")
	public String update(UfangBrn brn, Model model) {

		brn.setParent(brnService.get(brn.getParent().getId()));
		model.addAttribute("brn", brn);
		return "ufang/brn/brnUpdate";
	}
	
	
	
	@RequiresPermissions("ufang:brn:view")
	@RequestMapping(value = "query")
	public String query(UfangBrn brn, Model model) {

		brn.setParent(brnService.get(brn.getParent().getId()));

		model.addAttribute("brn", brn);
		return "ufang/brn/brnQuery";
	}
	
	
	
	@RequiresPermissions("ufang:brn:edit")
	@RequestMapping(value = "save")
	public String save(UfangBrn brn, Model model, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + ufangPath + "/ufang/brn/";
		}
		if (!beanValidator(model, brn)){
			return add(brn, model);
		}

		brnService.save(brn);
		return "redirect:" + ufangPath + "/brn/list?repage";
	}
	
	@RequiresPermissions("ufang:brn:edit")
	@RequestMapping(value = "delete")
	public String delete(UfangBrn brn, RedirectAttributes redirectAttributes) {

		if (brn.getGrade()==1){
			addMessage(redirectAttributes, "删除机构失败, 不允许删除总机构");
		}else{
			int sumcount=userService.getCountBrnUser(brn.getId());
			if(sumcount!=0){
				addMessage(redirectAttributes, "删除机构失败, 该机构下存在用户！");
			}else{
				brnService.delete(brn);
				addMessage(redirectAttributes, "删除机构成功");	
			}

		}
		return "redirect:" + ufangPath + "/brn/list?repage";
	}

	/**
	 * 获取机构JSON数据。
	 * @param extId 排除的ID
	 * @param type	类型（1：公司；2：部门/小组/其它：3：用户）
	 * @param brnGrade 显示级别
	 * @param response
	 * @return
	 */
	@RequiresPermissions("ufang")
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String extId, @RequestParam(required=false) String brnType,
			@RequestParam(required=false) Integer brnGrade, @RequestParam(required=false) Boolean isAll, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<UfangBrn> list = brnService.findList(isAll);
		for (int i=0; i<list.size(); i++){
			UfangBrn e = list.get(i);
			if ((StringUtils.isBlank(extId) || (extId!=null && !extId.equals(e.getId()+"") && e.getParentIds().indexOf(","+extId+",")==-1))					
					&& (brnGrade == null || (brnGrade != null &&e.getGrade() <= brnGrade))){
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId()+"");
				map.put("pId", e.getParentId()+"");
				map.put("pIds", e.getParentIds());
				map.put("name", e.getBrnName());
				if (brnType != null && "3".equals(brnType)){
					map.put("isParent", true);
				}
				mapList.add(map);
			}
		}
		return mapList;
	}
}
