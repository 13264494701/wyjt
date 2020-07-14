package com.jxf.cms.service;

import com.jxf.cms.entity.CmsIconPosition;
import com.jxf.svc.sys.crud.service.CrudService;

/**
 * 图标位置Service
 * @author HUOJIABAO
 * @version 2016-07-23
 */
public interface CmsIconPositionService extends CrudService<CmsIconPosition> {

	/**
	 * 
	 * 函数功能说明 
	 * HUOJIABAO  2016年6月16日 
	 * 修改者名字 修改日期 
	 * 修改内容 
	 * @参数： @param positionNo
	 * @参数： @return     
	 * @return ShopIconPosition    
	 * @throws
	 */
	CmsIconPosition getByPositionNo(String positionNo, boolean useCache);
	
}