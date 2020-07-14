package com.jxf.nfs.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.nfs.dao.NfsActSubDao;
import com.jxf.nfs.entity.NfsActSub;
import com.jxf.nfs.entity.NfsActSub.TrxRole;
import com.jxf.nfs.service.NfsActSubService;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;

/**
 * 账户科目ServiceImpl
 * @author wo
 * @version 2018-09-18
 */
@Service("nfsActSubService")
@Transactional(readOnly = true)
public class NfsActSubServiceImpl extends CrudServiceImpl<NfsActSubDao, NfsActSub> implements NfsActSubService{

	public NfsActSub get(Long id) {
		return super.get(id);
	}
	
	public List<NfsActSub> findList(NfsActSub nfsActSub) {
		return super.findList(nfsActSub);
	}
	
	public Page<NfsActSub> findPage(Page<NfsActSub> page, NfsActSub nfsActSub) {
		return super.findPage(page, nfsActSub);
	}
	
	@Transactional(readOnly = false)
	public void save(NfsActSub nfsActSub) {
		super.save(nfsActSub);
	}
	
	@Transactional(readOnly = false)
	public void delete(NfsActSub nfsActSub) {
		super.delete(nfsActSub);
	}

	@Override
	public List<NfsActSub> findSubsByTrxRole(TrxRole trxRole) {
		NfsActSub nfsActSub = new NfsActSub();
		nfsActSub.setTrxRole(trxRole);
		return findList(nfsActSub);
	}
	
}