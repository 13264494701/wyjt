package com.jxf.web.admin.ufang;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.area.service.AreaService;
import com.jxf.svc.utils.StringUtils;
import com.jxf.ufang.entity.UfangBrn;
import com.jxf.ufang.service.UfangBrnService;
import com.jxf.ufang.service.UfangUserService;
import com.jxf.web.admin.sys.BaseController;


/**
 * 优放机构Controller
 * @author jxf
 * @version 2015-07-28
 */
@Controller("adminUfangBrnController")
@RequestMapping(value = "${adminPath}/ufang/brn")
public class UfangBrnController extends BaseController {

	@Autowired
	private UfangBrnService brnService;
	@Autowired
	private UfangUserService userService;
	@Autowired
	private AreaService areaService;
	
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
		return "admin/ufang/brn/brnIndex";
	}
	
	@RequiresPermissions("ufang:brn:view")
	@RequestMapping(value = "list")
	public String list(UfangBrn brn, HttpServletRequest request, HttpServletResponse response, Model model) {
		brn.setGrade(2);
		Page<UfangBrn> page = brnService.findPage(new Page<UfangBrn>(request, response), brn); 
		model.addAttribute("page", page);
		return "admin/ufang/brn/brnList";
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
		brn.setType(UfangBrn.Type.predefine);
		model.addAttribute("brn", brn);
		return "admin/ufang/brn/brnAdd";
	}
	
	
	
	@RequiresPermissions("ufang:brn:view")
	@RequestMapping(value = "update")
	public String update(UfangBrn brn, Model model) {

		if (brn.getParent()==null || brn.getParent().getId()==null){
			brn.setParent(brnService.get(1L));//如果没有上级机构，则上级机构默认为平台
		}else {
			brn.setParent(brnService.get(brn.getParent().getId()));	
		}
		model.addAttribute("brn", brn);
		return "admin/ufang/brn/brnUpdate";
	}
	
	
	
	@RequiresPermissions("ufang:brn:view")
	@RequestMapping(value = "query")
	public String query(UfangBrn brn, Model model) {
		if (brn.getParent()==null || brn.getParent().getId()==null){
			brn.setParent(brnService.get(1L));//如果没有上级机构，则上级机构默认为平台
		}else {
			brn.setParent(brnService.get(brn.getParent().getId()));	
		}
		brn.setArea(areaService.get(brn.getArea()));
		model.addAttribute("brn", brn);
		return "admin/ufang/brn/brnQuery";
	}
	
	
	
	@RequiresPermissions("ufang:brn:edit")
	@RequestMapping(value = "save")
	public String save(UfangBrn brn, Model model, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/ufang/brn/";
		}
		if (!beanValidator(model, brn)){
			return add(brn, model);
		}
		
		brnService.save(brn);
		return "redirect:" + adminPath + "/ufang/brn/list?repage";
	}
	
	@RequiresPermissions("ufang:brn:edit")
	@RequestMapping(value = "delete")
	public String delete(UfangBrn brn, RedirectAttributes redirectAttributes) {

		if (brn.getGrade().equals(1)){
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
		return "redirect:" + adminPath + "/ufang/brn/list?repage";
	}

	/**
	 * 获取机构JSON数据。
	 * @param extId 排除的ID
	 * @param type	类型
	 * @param brnGrade 显示级别
	 * @param response
	 * @return
	 */
	@RequiresPermissions("admin")
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String extId, @RequestParam(required=false) String brnType,
			@RequestParam(required=false) Integer brnGrade, @RequestParam(required=false) Boolean isAll, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<UfangBrn> list = brnService.findList(true);
		for (int i=0; i<list.size(); i++){
			UfangBrn e = list.get(i);
			if ((StringUtils.isBlank(extId) || (extId!=null && !extId.equals(e.getId()+"") && e.getParentIds().indexOf(","+extId+",")==-1))				
					&& (brnGrade == null || (brnGrade != null && e.getGrade() <= brnGrade))){
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
	
	/**
	 * 验证手机号码是否被占用
	 * @param phoneNo
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions("ufang:brn:edit")
	@RequestMapping(value = "checkPhoneNo")
	public String checkPhoneNo(String phoneNo) {
		
        if (StringUtils.isNotBlank(phoneNo)&& userService.getUserByUsername(phoneNo) == null) {
			return "true";
		}
		return "false";
	}
	
}
