package com.jxf.cms.utils;

import java.util.List;




import com.jxf.cms.dao.CmsIconPositionDao;
import com.jxf.cms.entity.CmsIconPosition;
import com.jxf.svc.init.SpringContextHolder;
import com.jxf.svc.utils.StringUtils;

public class IconPositionUtils {
	
	private static CmsIconPositionDao iconPositionDao = SpringContextHolder.getBean(CmsIconPositionDao.class);
	/**
	 * 生成新的图标位置编号
	 * @param 
	 * @return
	 */
	public static String  genNewPositionNo(){
		
		String maxPositionNo=iconPositionDao.getMaxPositionNo();
		
		if(!StringUtils.isNullOrEmpty(maxPositionNo)){
			return StringUtils.addLeftStr(StringUtils.toLong(maxPositionNo)+1+"","0",4);	
		}else{
			return "0001";
		}	
	}
	/**
	 * 查询图标位置列表
	 * @param 
	 * @return
	 */
	public static List<CmsIconPosition> getIconPositionList(){
		
		CmsIconPosition iconPosition = new CmsIconPosition();
		List<CmsIconPosition> iconPositionList = iconPositionDao.findList(iconPosition);
		return iconPositionList;
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
	public static String getNameByIconPositionNo(String positionNo)
	{
		return iconPositionDao.getNameByNo(positionNo);
	}
}
