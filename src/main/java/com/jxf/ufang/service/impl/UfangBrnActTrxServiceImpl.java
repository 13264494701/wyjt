package com.jxf.ufang.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
import com.jxf.ufang.dao.UfangBrnActTrxDao;
import com.jxf.ufang.entity.UfangBrnActTrx;
import com.jxf.ufang.service.UfangBrnActTrxService;

/**
 * 账户交易ServiceImpl
 * @author jinxinfu
 * @version 2018-07-01
 */
@Service("ufangBrnActTrxService")
@Transactional(readOnly = true)
public class UfangBrnActTrxServiceImpl extends CrudServiceImpl<UfangBrnActTrxDao, UfangBrnActTrx> implements UfangBrnActTrxService{

	public UfangBrnActTrx get(Long id) {
		return super.get(id);
	}
	
	public List<UfangBrnActTrx> findList(UfangBrnActTrx nfsBrnActTrx) {
		return super.findList(nfsBrnActTrx);
	}
	
	public Page<UfangBrnActTrx> findPage(Page<UfangBrnActTrx> page, UfangBrnActTrx nfsBrnActTrx) {
		return super.findPage(page, nfsBrnActTrx);
	}
	
	@Transactional(readOnly = false)
	public void save(UfangBrnActTrx nfsBrnActTrx) {
		super.save(nfsBrnActTrx);
	}
	
	@Transactional(readOnly = false)
	public void delete(UfangBrnActTrx nfsBrnActTrx) {
		super.delete(nfsBrnActTrx);
	}
	
}