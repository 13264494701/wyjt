package com.jxf.svc.sys.area.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.area.entity.Area;
import com.jxf.svc.sys.tree.dao.TreeDao;



/**
 * 区域DAO接口
 * @author jxf
 * @version 2015-07-28
 */
@MyBatisDao
public interface AreaDao extends TreeDao<Area> {
	
	List<Area> getRoot();
	
	List<Area> getChildren(@Param("parentId")Long parentId);
	
	List<Area> getParents(Long[] parentIds);

	Area getAreaByCode(@Param("code")String code);
	List<Area> getAreaByType(@Param("type")String type);
	List<Area> findAreaList(Area area);
	
}
