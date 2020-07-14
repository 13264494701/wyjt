package com.jxf.cms.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.cms.dao.NavigationDao;
import com.jxf.cms.entity.Navigation;
import com.jxf.cms.service.NavigationService;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;

/**
 * 导航ServiceImpl
 * @author huojiayuan
 * @version 2016-06-09
 */
@Service("shopNavigationService")
@Transactional(readOnly = true)
public class NavigationServiceImpl extends CrudServiceImpl<NavigationDao, Navigation> implements NavigationService{
    
	@Autowired
	private NavigationDao navigationDao;
	
	public Navigation get(Long id) {
		return super.get(id);
	}
	
	public List<Navigation> findList(Navigation shopNavigation) {
		return super.findList(shopNavigation);
	}
	
	public Page<Navigation> findPage(Page<Navigation> page, Navigation shopNavigation) {
		return super.findPage(page, shopNavigation);
	}
	
	@Transactional(readOnly = false)
	public void save(Navigation shopNavigation) {
		super.save(shopNavigation);
	}
	
	@Transactional(readOnly = false)
	public void delete(Navigation shopNavigation) {
		super.delete(shopNavigation);
	}
	
	@Transactional(readOnly = true)
	@Cacheable(value = "navigation", condition = "#useCache")
	public List<Navigation> findListByPosition(String position, boolean useCache) {
		return navigationDao.findListByPosition(position);
	}
}