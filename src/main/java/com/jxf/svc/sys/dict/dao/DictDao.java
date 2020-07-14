package com.jxf.svc.sys.dict.dao;

import java.util.List;

import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;
import com.jxf.svc.sys.dict.entity.Dict;

/**
 * 字典DAO接口
 * @author jxf
 * @version 2015-07-28
 */
@MyBatisDao
public interface DictDao extends CrudDao<Dict> {

	public List<String> findTypeList(Dict dict);
	
}
