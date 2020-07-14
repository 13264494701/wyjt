package com.jxf.cms.service;

import java.util.List;

import com.jxf.cms.entity.CmsIcon;
import com.jxf.svc.sys.crud.service.CrudService;

/**
 * 图标Service
 * @author HUOJIABAO
 * @version 2016-07-23
 */
public interface CmsIconService extends CrudService<CmsIcon> {

	/**
	 * 
	 * 函数功能说明 
	 * HUOJIABAO  2016年6月16日 
	 * 修改者名字 修改日期 
	 * 修改内容 
	 * @参数： @param positionNo
	 * @参数： @return     
	 * @return ShopIcon  
	 * @throws
	 */
	CmsIcon getByPositionNo(String positionNo, boolean useCache);
	/**
	 * 
	 * 函数功能说明 
	 * HUOJIABAO  2016年6月16日 
	 * 修改者名字 修改日期 
	 * 修改内容 
	 * @参数： @param pageNo
	 * @参数： @return     
	 * @return List<CmsIcon>   
	 * @throws
	 */
	List<CmsIcon> findListByPageNo(String pageNo);
	
}