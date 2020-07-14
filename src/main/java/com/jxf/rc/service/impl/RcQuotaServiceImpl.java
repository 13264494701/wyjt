package com.jxf.rc.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.rc.dao.RcQuotaDao;
import com.jxf.rc.entity.RcQuota;
import com.jxf.rc.service.RcQuotaService;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
/**
 * 会员额度评估ServiceImpl
 * @author SuHuimin
 * @version 2019-08-23
 */
@Service("rcQuotaService")
@Transactional(readOnly = true)
public class RcQuotaServiceImpl extends CrudServiceImpl<RcQuotaDao, RcQuota> implements RcQuotaService{

	@Autowired
	private RcQuotaDao rcQuotaDao;
	
    @Override
	public RcQuota get(Long id) {
		return super.get(id);
	}
	
	@Override
	public List<RcQuota> findList(RcQuota rcQuota) {
		return super.findList(rcQuota);
	}
	
	@Override
	public Page<RcQuota> findPage(Page<RcQuota> page, RcQuota rcQuota) {
		return super.findPage(page, rcQuota);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void save(RcQuota rcQuota) {
		super.save(rcQuota);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void delete(RcQuota rcQuota) {
		super.delete(rcQuota);
	}

	@Override
	public RcQuota getByMemberId(Long memberId) {
		return rcQuotaDao.getByMemberId(memberId);
	}

	@Override
	public RcQuota getByPhoneNo(String phoneNo) {
		return rcQuotaDao.getByPhoneNo(phoneNo);
	}
	
}