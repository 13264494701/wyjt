package com.jxf.svc.sys.brn.dao;

import java.util.List;

import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.brn.entity.Brn;
import com.jxf.svc.sys.tree.dao.TreeDao;


/**
 * 机构DAO接口
 * @author jxf
 * @version 2015-07-28
 */
@MyBatisDao
public interface BrnDao extends TreeDao<Brn> {
	public List<Brn> getChildrenByParentId(Long parentId);
	public String getChildMaxNo(Long parentId);
	public String getBrnNamByNo(String brnNo);
}
