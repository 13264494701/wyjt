package com.jxf.nfs.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.nfs.dao.NfsBankInfoDao;
import com.jxf.nfs.entity.NfsBankInfo;
import com.jxf.nfs.service.NfsBankInfoService;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;

/**
 * 银行编码ServiceImpl
 * @author wo
 * @version 2018-09-29
 */
@Service("nfsBankInfoService")
@Transactional(readOnly = true)
public class NfsBankInfoServiceImpl extends CrudServiceImpl<NfsBankInfoDao, NfsBankInfo> implements NfsBankInfoService{
	@Autowired
	private NfsBankInfoDao nfsBankInfoDao;

	public NfsBankInfo get(Long id) {
		return super.get(id);
	}
	
	public List<NfsBankInfo> findList(NfsBankInfo nfsBankInfo) {
		return super.findList(nfsBankInfo);
	}
	
	public Page<NfsBankInfo> findPage(Page<NfsBankInfo> page, NfsBankInfo nfsBankInfo) {
		return super.findPage(page, nfsBankInfo);
	}
	
	@Transactional(readOnly = false)
	public void save(NfsBankInfo nfsBankInfo) {
		super.save(nfsBankInfo);
	}
	
	@Transactional(readOnly = false)
	public void delete(NfsBankInfo nfsBankInfo) {
		super.delete(nfsBankInfo);
	}


	@Override
	public NfsBankInfo getBankInfoByAbbrName(String abbrName) {
		return nfsBankInfoDao.getByAbbrName(abbrName);
	}

}