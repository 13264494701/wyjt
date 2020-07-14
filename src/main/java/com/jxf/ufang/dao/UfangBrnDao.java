package com.jxf.ufang.dao;

import java.util.List;

import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.tree.dao.TreeDao;
import com.jxf.ufang.entity.UfangBrn;
import org.apache.ibatis.annotations.Param;


/**
 * 机构DAO接口
 * @author jxf
 * @version 2015-07-28
 */
@MyBatisDao
public interface UfangBrnDao extends TreeDao<UfangBrn> {
	public List<UfangBrn> getChildrenByParentId(Long parentId);
	public String getChildMaxNo(Long parentId);
	public String getBrnNamByNo(String brnNo);
	int updateFreeData(@Param("freeData")int freeData, @Param("brnNo")String brnNo);
}
