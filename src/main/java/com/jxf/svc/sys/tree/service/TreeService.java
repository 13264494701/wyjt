package com.jxf.svc.sys.tree.service;

import org.springframework.transaction.annotation.Transactional;
import com.jxf.svc.sys.crud.service.CrudService;
import com.jxf.svc.sys.tree.dao.TreeDao;
import com.jxf.svc.sys.tree.entity.TreeEntity;


/**
 * Service基类
 * @author jxf
 * @version 1.0 2015-07-10
 */
@Transactional(readOnly = true)
public interface  TreeService<D extends TreeDao<T>, T extends TreeEntity<T>> extends CrudService<T> {
	


}
