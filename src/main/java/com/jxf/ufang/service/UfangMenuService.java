package com.jxf.ufang.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
import com.jxf.ufang.dao.UfangMenuDao;
import com.jxf.ufang.entity.UfangMenu;




/**
 * 字典Service
 * @author jxf
 * @version 2015-07-28
 */
@Service
@Transactional(readOnly = true)
public class UfangMenuService extends CrudServiceImpl<UfangMenuDao, UfangMenu> {
	
	@Autowired
	private UfangMenuDao menuDao;
	
	public UfangMenu getMenu(Long id) {
		return menuDao.get(id);
	}

	public List<UfangMenu> findAllMenu(){
		return menuDao.findList(new UfangMenu());
	}
	
	@Transactional(readOnly = false)
	public void saveMenu(UfangMenu menu) {
		
		// 获取父节点实体
		menu.setParent(this.getMenu(menu.getParent().getId()));
		
		// 获取修改前的parentIds，用于更新子节点的parentIds
		String oldParentIds = menu.getParentIds(); 
		
		// 设置新的父节点串
		menu.setParentIds(menu.getParent().getParentIds()+menu.getParent().getId()+",");

		// 保存或更新实体
		if (menu.getIsNewRecord()){
			menu.preInsert();
			menuDao.insert(menu);
		}else{
			menu.preUpdate();
			menuDao.update(menu);
		}
		
		// 更新子节点 parentIds
		UfangMenu m = new UfangMenu();
		m.setParentIds("%,"+menu.getId()+",%");
		List<UfangMenu> list = menuDao.findByParentIdsLike(m);
		for (UfangMenu e : list){
			e.setParentIds(e.getParentIds().replace(oldParentIds, menu.getParentIds()));
			menuDao.updateParentIds(e);
		}

	}

	@Transactional(readOnly = false)
	public void updateMenuSort(UfangMenu menu) {
		menuDao.updateSort(menu);
	}

	@Transactional(readOnly = false)
	public void deleteMenu(UfangMenu menu) {
		menuDao.delete(menu);
	}

}
