package com.jxf.cms.utils;

import java.util.List;

import com.jxf.cms.dao.CmsAdPositionDao;
import com.jxf.cms.entity.CmsAdPosition;
import com.jxf.svc.init.SpringContextHolder;
import com.jxf.svc.utils.StringUtils;

public class AdPositionUtils {
	
	private static CmsAdPositionDao adPositionDao = SpringContextHolder.getBean(CmsAdPositionDao.class);
	/**
	 * 生成新的广告位置编号
	 * @param 
	 * @return
	 */
	public static String  genNewPositionNo(){
		
		String maxPositionNo=adPositionDao.getMaxPositionNo();
		
		if(!StringUtils.isNullOrEmpty(maxPositionNo)){
			return StringUtils.addLeftStr(StringUtils.toLong(maxPositionNo)+1+"","0",4);	
		}else{
			return "0001";
		}	
	}
	/**
	 * 查询广告位置列表
	 * @param 
	 * @return
	 */
	public static List<CmsAdPosition> getAdPositionList(){
		
		CmsAdPosition adPosition = new CmsAdPosition();
		List<CmsAdPosition> adPositionList = adPositionDao.findList(adPosition);
		return adPositionList;
	}
	
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
	public static String getPositionNameByPositionNo(String positionNo)
	{
		return adPositionDao.getNameByNo(positionNo);
	}
}
