package com.jxf.web.ufang;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import com.jxf.svc.utils.StringUtils;
import com.jxf.ufang.entity.UfangMenu;
import com.jxf.ufang.service.UfangMenuService;
import com.jxf.web.admin.sys.BaseController;


/**
 * 菜单Controller
 * @author wo
 * @version 2018-10-20
 */
@Controller("ufangMenuController")
@RequestMapping(value = "${ufangPath}/menu")
public class UfangMenuController extends BaseController {

	@Autowired
	private UfangMenuService menuService;
	
	@RequiresPermissions("ufang")
	@RequestMapping(value = "tree")
	public String tree() {
		return "ufang/menu/menuTree";
	}

	@RequiresPermissions("ufang")
	@RequestMapping(value = "treeselect")
	public String treeselect(String parentId, Model model) {
		model.addAttribute("parentId", parentId);
		return "ufang/menu/menuTreeselect";
	}
	/**
	 * isShowHide是否显示隐藏菜单
	 * @param extId
	 * @param isShowHidden
	 * @param response
	 * @return
	 */
	@RequiresPermissions("ufang")
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
