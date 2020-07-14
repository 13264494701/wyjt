package com.jxf.cms.utils;


import java.util.List;

import com.jxf.cms.dao.CmsAdPositionDao;
import com.jxf.cms.dao.CmsIconDao;
import com.jxf.cms.entity.CmsAdPosition;
import com.jxf.cms.entity.CmsChannel;
import com.jxf.cms.entity.CmsIcon;
import com.jxf.cms.service.CmsChannelService;
import com.jxf.svc.cache.CacheUtils;
import com.jxf.svc.init.SpringContextHolder;
import com.jxf.svc.utils.StringUtils;


/**
 * 内容管理工具类
 * @author jxf
 * @version 1.0 2015-07-10
 */
public class CmsUtils {
	
	private static CmsChannelService channelService = SpringContextHolder.getBean(CmsChannelService.class);
    private static CmsAdPositionDao adPositionDao = SpringContextHolder.getBean(CmsAdPositionDao.class);
    private static CmsIconDao iconDao = SpringContextHolder.getBean(CmsIconDao.class);
    
	
	public static final String CACHE_ICON_LIST = "iconList";
	
	/**
	 * 获得站点列表
	 */
	public static List<CmsChannel> getChannelList(){
	
	
		List<CmsChannel> channelList = channelService.findList(new CmsChannel());
		return channelList;
	}
	

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
	

	/**
	 * 获取图标列表
	 * @return
	 */
	public static List<CmsIcon>  getIconList(String pageNo){

		@SuppressWarnings("unchecked")
		List<CmsIcon> iconList = (List<CmsIcon>)CacheUtils.get(CACHE_ICON_LIST,pageNo);
		if (iconList == null){
			iconList = iconDao.findListByPageNo(pageNo);
			CacheUtils.put(CACHE_ICON_LIST, pageNo, iconList);
		}
		return iconList;
	}
}