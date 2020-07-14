package com.jxf.ufang.service;

import java.util.List;


import com.jxf.svc.sys.tree.service.TreeService;

import com.jxf.ufang.dao.UfangBrnDao;
import com.jxf.ufang.entity.UfangBrn;




/**
 * 机构Service
 * @author jxf
 * @version 2015-07-28
 */

public interface UfangBrnService extends TreeService<UfangBrnDao, UfangBrn> {
	
	public List<UfangBrn> findAll();

	public List<UfangBrn> findList(Boolean isAll);
	

	public List<UfangBrn> findList(UfangBrn brn);

	int updateFreeData(int freeData, String brnNo);
}
