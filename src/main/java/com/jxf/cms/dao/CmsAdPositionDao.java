package com.jxf.cms.dao;

import com.jxf.cms.entity.CmsAdPosition;
import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;

/**
 * 广告位置DAO接口
 * @author JINXINFU
 * @version 2016-04-25
 */
@MyBatisDao
public interface CmsAdPositionDao extends CrudDao<CmsAdPosition> {
	
	/**
	 * 
	 * 函数功能说明 
	 * HUOJIABAO  2016年6月16日 
	 * 修改者名字 修改日期 
	 * 修改内容 
	 * @参数： @param positionNo
	 * @参数： @return     
	 * @return ShopAdPosition    
	 * @throws
	 */
	CmsAdPosition getByPositionNo(String positionNo);
	
	/**
	 * 查询最大的编号
	 * @param 
	 * @return
	 */
	String getMaxPositionNo();
	/**
	 * 
	 * 函数功能说明   根据编号查询名称
	 * HUOJIABAO  2016年6月21日 
	 * 修改者名字 修改日期 
	 * 修改内容 
	 * @参数： @param positionNo
	 * @参数： @return     
	 * @return String    
	 * @throws
	 */
	String getNameByNo(String positionNo);

}