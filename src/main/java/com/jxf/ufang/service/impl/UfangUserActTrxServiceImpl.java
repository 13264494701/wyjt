package com.jxf.ufang.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
import com.jxf.ufang.dao.UfangUserActTrxDao;
import com.jxf.ufang.entity.UfangUserActTrx;
import com.jxf.ufang.service.UfangUserActTrxService;

/**
 * 账户交易ServiceImpl
 * @author jinxinfu
 * @version 2018-07-01
 */
@Service("ufangUserActTrxService")
@Transactional(readOnly = true)
public class UfangUserActTrxServiceImpl extends CrudServiceImpl<UfangUserActTrxDao, UfangUserActTrx> implements UfangUserActTrxService{

	public UfangUserActTrx get(Long id) {
		return super.get(id);
	}
	
	public List<UfangUserActTrx> findList(UfangUserActTrx ufangUserActTrx) {
		return super.findList(ufangUserActTrx);
	}
	
	public Page<UfangUserActTrx> findPage(Page<UfangUserActTrx> page, UfangUserActTrx ufangUserActTrx) {
		return super.findPage(page, ufangUserActTrx);
	}
	
	@Transactional(readOnly = false)
	public void save(UfangUserActTrx ufangUserActTrx) {
		super.save(ufangUserActTrx);
	}
	
	@Transactional(readOnly = false)
	public void delete(UfangUserActTrx ufangUserActTrx) {
		super.delete(ufangUserActTrx);
	}
	
}