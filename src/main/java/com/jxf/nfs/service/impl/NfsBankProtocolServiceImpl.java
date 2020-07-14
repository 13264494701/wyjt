package com.jxf.nfs.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.nfs.dao.NfsBankProtocolDao;
import com.jxf.nfs.entity.NfsBankProtocol;
import com.jxf.nfs.service.NfsBankProtocolService;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
/**
 * 签约协议ServiceImpl
 * @author suhuimin
 * @version 2018-09-30
 */
@Service("nfsBankProtocolService")
@Transactional(readOnly = true)
public class NfsBankProtocolServiceImpl extends CrudServiceImpl<NfsBankProtocolDao, NfsBankProtocol> implements NfsBankProtocolService{

	@Autowired
	private NfsBankProtocolDao nfsBankProtocolDao;
	
	public NfsBankProtocol get(Long id) {
		return super.get(id);
	}
	
	public List<NfsBankProtocol> findList(NfsBankProtocol nfsBankProtocol) {
		return super.findList(nfsBankProtocol);
	}
	
	public Page<NfsBankProtocol> findPage(Page<NfsBankProtocol> page, NfsBankProtocol nfsBankProtocol) {
		return super.findPage(page, nfsBankProtocol);
	}
	
	@Transactional(readOnly = false)
	public void save(NfsBankProtocol nfsBankProtocol) {
		super.save(nfsBankProtocol);
	}
	
	@Transactional(readOnly = false)
	public void delete(NfsBankProtocol nfsBankProtocol) {
		super.delete(nfsBankProtocol);
	}

	@Override
	public NfsBankProtocol getByMember(Long memberId) {
		
		return nfsBankProtocolDao.getByMember(memberId);
	}

	
	
}