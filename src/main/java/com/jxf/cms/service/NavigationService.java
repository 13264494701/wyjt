package com.jxf.cms.service;

import java.util.List;

import com.jxf.cms.entity.Navigation;
import com.jxf.svc.sys.crud.service.CrudService;

/**
 * 导航Service
 * @author huojiayuan
 * @version 2016-06-09
 */
public interface NavigationService extends CrudService<Navigation> {

	/**
	 * 查找导航
	 * 
	 * @param position
	 *            导航位置
	 * @param useCache
	 *            是否使用缓存
	 * @return 导航
	 */
	List<Navigation> findListByPosition(String position, boolean useCache);
		
}