package com.jxf.nfs.service.impl;

import java.util.List;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.nfs.dao.NfsTrxRuleItemDao;
import com.jxf.nfs.entity.NfsTrxRuleItem;
import com.jxf.nfs.service.NfsTrxRuleItemService;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
/**
 * 业务规则ServiceImpl
 * @author wo
 * @version 2018-09-21
 */
@Service("nfsTrxRuleItemService")
@Transactional(readOnly = true)
public class NfsTrxRuleItemServiceImpl extends CrudServiceImpl<NfsTrxRuleItemDao, NfsTrxRuleItem> implements NfsTrxRuleItemService{


	
	public NfsTrxRuleItem get(Long id) {
		return super.get(id);
	}
	
	public List<NfsTrxRuleItem> findList(NfsTrxRuleItem nfsTrxRuleItem) {
		return super.findList(nfsTrxRuleItem);
	}
	
	public Page<NfsTrxRuleItem> findPage(Page<NfsTrxRuleItem> page, NfsTrxRuleItem nfsTrxRuleItem) {
		return super.findPage(page, nfsTrxRuleItem);
	}
	
	@Transactional(readOnly = false)
	public void save(NfsTrxRuleItem nfsTrxRuleItem) {
		super.save(nfsTrxRuleItem);
	}
	
	@Transactional(readOnly = false)
	public void delete(NfsTrxRuleItem nfsTrxRuleItem) {
		super.delete(nfsTrxRuleItem);
	}
}