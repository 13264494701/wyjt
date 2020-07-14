package com.jxf.nfs.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.jxf.nfs.dao.NfsTrxRuleDao;
import com.jxf.nfs.entity.NfsTrxRule;
import com.jxf.nfs.service.NfsTrxRuleService;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
/**
 * 业务规则ServiceImpl
 * @author XIAORONGDIAN
 * @version 2018-09-10
 */
@Service("nfsTrxRuleService")
@Transactional(readOnly = true)
public class NfsTrxRuleServiceImpl extends CrudServiceImpl<NfsTrxRuleDao, NfsTrxRule> implements NfsTrxRuleService{

	@Autowired
	private NfsTrxRuleDao trxRuleDao;
	
	public NfsTrxRule get(Long id) {
		return super.get(id);
	}
	
	public List<NfsTrxRule> findList(NfsTrxRule nfsTrxRule) {
		return super.findList(nfsTrxRule);
	}
	
	public Page<NfsTrxRule> findPage(Page<NfsTrxRule> page, NfsTrxRule nfsTrxRule) {
		return super.findPage(page, nfsTrxRule);
	}
	
	@Transactional(readOnly = false)
	public void save(NfsTrxRule nfsTrxRule) {
		super.save(nfsTrxRule);
	}
	
	@Transactional(readOnly = false)
	public void delete(NfsTrxRule nfsTrxRule) {
		super.delete(nfsTrxRule);
	}

	@Override
	public NfsTrxRule getByTrxCode(String trxCode) {

		return trxRuleDao.getByTrxCode(trxCode);
	}

}