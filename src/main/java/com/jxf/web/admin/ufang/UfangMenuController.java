package com.jxf.web.admin.ufang;

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
import com.jxf.svc.sys.util.UserUtils;
import com.jxf.svc.utils.StringUtils;
import com.jxf.ufang.entity.UfangMenu;
import com.jxf.ufang.service.UfangMenuService;
import com.jxf.web.admin.sys.BaseController;


/**
 * 菜单Controller
 * @author wo
 * @version 2018-10-20
 */
@Controller("adminUfangMenuController")
@RequestMapping(value = "${adminPath}/ufang/menu")
public class UfangMenuController extends BaseController {

	@Autowired
	private UfangMenuService menuService;
	
	@ModelAttribute
	public UfangMenu get(@RequestParam(required=false) Long id) {
		if (id!=null){
			return menuService.getMenu(id);
		}else{
			return new UfangMenu();
		}
	}

	@RequiresPermissions("ufang:menu:view")
	@RequestMapping(value = {"list", ""})
	public String list(Model model) {
		List<UfangMenu> list = Lists.newArrayList();
		List<UfangMenu> sourcelist = menuService.findAllMenu();
		UfangMenu.sortList(list, sourcelist, UfangMenu.getRootId(), true);
        model.addAttribute("list", list);
		return "admin/ufang/menu/menuList";
	}

	@RequiresPermissions("ufang:menu:view")
	@RequestMapping(value = "form")
	public String form(UfangMenu menu, Model model) {
		if (menu.getParent()==null||menu.getParent().getId()==null){
			menu.setParent(new UfangMenu(UfangMenu.getRootId()));
		}
		menu.setParent(menuService.getMenu(menu.getParent().getId()));
		// 获取排序号，最末节点排序号+30
		if (menu.getId()==null){
			List<UfangMenu> list = Lists.newArrayList();
			List<UfangMenu> sourcelist = menuService.findAllMenu();
			UfangMenu.sortList(list, sourcelist, menu.getParentId(), false);
			if (list.size() > 0){
				menu.setSort(list.get(list.size()-1).getSort() + 30);
			}
		}
		model.addAttribute("menu", menu);
		return "admin/ufang/menu/menuForm";
	}
	
	@RequiresPermissions("ufang:menu:edit")
	@RequestMapping(value = "save")
	public String save(UfangMenu menu, Model model, RedirectAttributes redirectAttributes) {
		if(!UserUtils.getUser().isGod()){
			addMessage(redirectAttributes, "越权操作，只有超级管理员才能添加或修改数据！");
			return "redirect:" + Global.getAdminPath() + "/ufang/role/?repage";
		}
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + Global.getAdminPath() + "/ufang/menu/";
		}
		if (!beanValidator(model, menu)){
			return form(menu, model);
		}
		menuService.saveMenu(menu);
		addMessage(redirectAttributes, "保存菜单'" + menu.getName() + "'成功");
		return "redirect:" + Global.getAdminPath() + "/ufang/menu/";
	}
	
	@RequiresPermissions("ufang:menu:edit")
	@RequestMapping(value = "delete")
	public String delete(UfangMenu menu, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + Global.getAdminPath() + "/sys/menu/";
		}
//		if (Menu.isRoot(id)){
//			addMessage(redirectAttributes, "删除菜单失败, 不允许删除顶级菜单或编号为空");
//		}else{
			menuService.deleteMenu(menu);
			addMessage(redirectAttributes, "删除菜单成功");
//		}
		return "redirect:" + Global.getAdminPath() + "/ufang/menu/";
	}

	@RequiresPermissions("ufang:menu:view")
	@RequestMapping(value = "tree")
	public String tree() {
		return "admin/ufang/menu/menuTree";
	}

	@RequiresPermissions("ufang:menu:view")
	@RequestMapping(value = "treeselect")
	public String treeselect(String parentId, Model model) {
		model.addAttribute("parentId", parentId);
		return "admin/ufang/menu/menuTreeselect";
	}
	
	/**
	 * 批量修改菜单排序
	 */
	@RequiresPermissions("ufang:menu:edit")
	@RequestMapping(value = "updateSort")
	public String updateSort(Long[] ids, Integer[] sorts, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + Global.getAdminPath() + "/sys/menu/";
		}
    	for (int i = 0; i < ids.length; i++) {
    		UfangMenu menu = new UfangMenu(ids[i]);
    		menu.setSort(sorts[i]);
    		menuService.updateMenuSort(menu);
    	}
    	addMessage(redirectAttributes, "保存菜单排序成功!");
		return "redirect:" + Global.getAdminPath() + "/ufang/menu/";
	}
	
	/**
	 * isShowHide是否显示隐藏菜单
	 * @param extId
	 * @param isShowHidden
	 * @param response
	 * @return
	 */
	@RequiresPermissions("ufang:menu:view")
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String extId,@RequestParam(required=false) String isShowHide, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<UfangMenu> list = menuService.findAllMenu();
		for (int i=0; i<list.size(); i++){
			UfangMenu e = list.get(i);
			if (StringUtils.isBlank(extId) || (extId!=null && !extId.equals(e.getId()+"") && e.getParentIds().indexOf(","+extId+",")==-1)){
				if(isShowHide != null && isShowHide.equals("0") && !e.getIsShow()){
					continue;
				}
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId()+"");
				map.put("pId", e.getParentId()+"");
				map.put("name", e.getName());
				mapList.add(map);
			}
		}
		return mapList;
	}
}
