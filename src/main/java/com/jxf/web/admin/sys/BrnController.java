package com.jxf.web.admin.sys;

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
import com.jxf.svc.sys.brn.entity.Brn;
import com.jxf.svc.sys.brn.service.BrnService;
import com.jxf.svc.sys.user.service.UserService;
import com.jxf.svc.utils.StringUtils;

/**
 * 机构Controller
 * @author jxf
 * @version 2015-07-28
 */
@Controller("adminBrnController")
@RequestMapping(value = "${adminPath}/sys/brn")
public class BrnController extends BaseController {

	@Autowired
	private BrnService brnService;
	@Autowired
	private UserService userService;
	
	@ModelAttribute("brn")
	public Brn get(@RequestParam(required=false) Long id) {
		if (id!=null){
			return brnService.get(id);
		}else{
			return new Brn();
		}
	}

	@RequiresPermissions("sys:brn:view")
	@RequestMapping(value = {""})
	public String index(Brn brn, Model model) {
        model.addAttribute("list", brnService.findAll());
		return "admin/sys/brn/brnIndex";
	}

	@RequiresPermissions("sys:brn:view")
	@RequestMapping(value = {"list"})
	public String list(Brn brn, Model model) {
        model.addAttribute("list", brnService.findList(true));
		return "admin/sys/brn/brnList";
	}
	
	@RequiresPermissions("sys:brn:view")
	@RequestMapping(value = "add")
	public String add(Brn brn, Model model) {

		if (brn.getParent()==null || brn.getParent().getId()==null){
			brn.setParent(brnService.get(1L));//如果没有上级机构，则上级机构默认为平台
			brn.setArea(brn.getParent().getArea());
		}else {
			brn.setParent(brnService.get(brn.getParent()));
			brn.setArea(brn.getParent().getArea());
		}

		model.addAttribute("brn", brn);
		return "admin/sys/brn/brnAdd";
	}
	
	
	
	@RequiresPermissions("sys:brn:view")
	@RequestMapping(value = "update")
	public String update(Brn brn, Model model) {

		brn.setParent(brnService.get(brn.getParent().getId()));
		model.addAttribute("brn", brn);
		return "admin/sys/brn/brnUpdate";
	}
	
	
	
	@RequiresPermissions("sys:brn:view")
	@RequestMapping(value = "query")
	public String query(Brn brn, Model model) {

		brn.setParent(brnService.get(brn.getParent().getId()));

		model.addAttribute("brn", brn);
		return "admin/sys/brn/brnQuery";
	}
	
	
	
	@RequiresPermissions("sys:brn:edit")
	@RequestMapping(value = "save")
	public String save(Brn brn, Model model, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/sys/brn/";
		}
		if (!beanValidator(model, brn)){
			return add(brn, model);
		}
		
		brnService.save(brn);
		return "redirect:" + adminPath + "/sys/brn/list?repage";
	}
	
	@RequiresPermissions("sys:brn:edit")
	@RequestMapping(value = "delete")
	public String delete(Brn brn, RedirectAttributes redirectAttributes) {

		if (brn.getBrnGrade().equals("1")){
			addMessage(redirectAttributes, "删除机构失败, 不允许删除总机构");
		}else{
			int sumcount=userService.getCountBrnUser("0", brn.getId());
			if(sumcount!=0){
				addMessage(redirectAttributes, "删除机构失败, 该机构下存在用户！");
			}else{
				brnService.delete(brn);
				addMessage(redirectAttributes, "删除机构成功");	
			}

		}
		return "redirect:" + adminPath + "/sys/brn/list?repage";
	}

	/**
	 * 获取机构JSON数据。
	 * @param extId 排除的ID
	 * @param type	类型（1：公司；2：部门/小组/其它：3：用户）
	 * @param brnGrade 显示级别
	 * @param response
	 * @return
	 */
	@RequiresPermissions("admin")
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String extId, @RequestParam(required=false) String brnType,
			@RequestParam(required=false) Long brnGrade, @RequestParam(required=false) Boolean isAll, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<Brn> list = brnService.findList(isAll);
		for (int i=0; i<list.size(); i++){
			Brn e = list.get(i);
			if ((StringUtils.isBlank(extId) || (extId!=null && !extId.equals(e.getId()+"") && e.getParentIds().indexOf(","+extId+",")==-1))
					&& (brnType == null || (brnType != null && (brnType.equals("1") ? brnType.equals(e.getBrnGrade()) : true)))
					&& (brnGrade == null || (brnGrade != null && Integer.parseInt(e.getBrnGrade()) <= brnGrade.intValue()))){
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
