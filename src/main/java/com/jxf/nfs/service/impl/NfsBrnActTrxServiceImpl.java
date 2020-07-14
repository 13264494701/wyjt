package com.jxf.nfs.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.nfs.dao.NfsBrnActTrxDao;
import com.jxf.nfs.entity.NfsBrnActTrx;
import com.jxf.nfs.service.NfsBrnActTrxService;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;

/**
 * 账户交易ServiceImpl
 * @author jinxinfu
 * @version 2018-07-01
 */
@Service("nfsBrnActTrxService")
@Transactional(readOnly = true)
public class NfsBrnActTrxServiceImpl extends CrudServiceImpl<NfsBrnActTrxDao, NfsBrnActTrx> implements NfsBrnActTrxService{

	public NfsBrnActTrx get(Long id) {
		return super.get(id);
	}
	
	public List<NfsBrnActTrx> findList(NfsBrnActTrx nfsBrnActTrx) {
		return super.findList(nfsBrnActTrx);
	}
	
	public Page<NfsBrnActTrx> findPage(Page<NfsBrnActTrx> page, NfsBrnActTrx nfsBrnActTrx) {
		return super.findPage(page, nfsBrnActTrx);
	}
	
	@Transactional(readOnly = false)
	public void save(NfsBrnActTrx nfsBrnActTrx) {
		super.save(nfsBrnActTrx);
	}
	
	@Transactional(readOnly = false)
	public void delete(NfsBrnActTrx nfsBrnActTrx) {
		super.delete(nfsBrnActTrx);
	}

	@Override
	public List<NfsBrnActTrx> findListByBusinessId(Long businessId) {
		return dao.findListByBusinessId(businessId);
	}

}