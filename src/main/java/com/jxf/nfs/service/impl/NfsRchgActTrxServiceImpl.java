package com.jxf.nfs.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.nfs.dao.NfsRchgActTrxDao;
import com.jxf.nfs.entity.NfsRchgActTrx;
import com.jxf.nfs.service.NfsRchgActTrxService;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;

/**
 * 充值流水ServiceImpl
 * @author wo
 * @version 2019-03-24
 */
@Service("nfsRchgActTrxService")
@Transactional(readOnly = true)
public class NfsRchgActTrxServiceImpl extends CrudServiceImpl<NfsRchgActTrxDao, NfsRchgActTrx> implements NfsRchgActTrxService{

    @Override
	public NfsRchgActTrx get(Long id) {
		return super.get(id);
	}
	
	@Override
	public List<NfsRchgActTrx> findList(NfsRchgActTrx nfsRchgActTrx) {
		return super.findList(nfsRchgActTrx);
	}
	
	@Override
	public Page<NfsRchgActTrx> findPage(Page<NfsRchgActTrx> page, NfsRchgActTrx nfsRchgActTrx) {
		return super.findPage(page, nfsRchgActTrx);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void save(NfsRchgActTrx nfsRchgActTrx) {
		super.save(nfsRchgActTrx);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void delete(NfsRchgActTrx nfsRchgActTrx) {
		super.delete(nfsRchgActTrx);
	}
	
}