package com.jxf.svc.sys.dict.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.svc.cache.CacheUtils;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
import com.jxf.svc.sys.dict.dao.DictDao;
import com.jxf.svc.sys.dict.entity.Dict;
import com.jxf.svc.sys.util.DictUtils;


/**
 * 字典Service
 * @author jxf
 * @version 2015-07-28
 */
@Service
@Transactional(readOnly = true)
public class DictService extends CrudServiceImpl<DictDao, Dict> {
	
	/**
	 * 查询字段类型列表
	 * @return
	 */
	public List<String> findTypeList(){
		return dao.findTypeList(new Dict());
	}

	@Transactional(readOnly = false)
	public void save(Dict dict) {
		super.save(dict);
		CacheUtils.remove(DictUtils.CACHE_DICT_MAP);
	}

	@Transactional(readOnly = false)
	public void delete(Dict dict) {
		super.delete(dict);
		CacheUtils.remove(DictUtils.CACHE_DICT_MAP);
	}

}
