package com.jxf.rc.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.rc.dao.RcShandieDao;
import com.jxf.rc.entity.RcShandie;
import com.jxf.rc.service.RcShandieService;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
/**
 * 闪谍报告（中智诚）ServiceImpl
 * @author XIAORONGDIAN
 * @version 2019-03-21
 */
@Service("rcShandieService")
@Transactional(readOnly = true)
public class RcShandieServiceImpl extends CrudServiceImpl<RcShandieDao, RcShandie> implements RcShandieService{

    @Override
	public RcShandie get(Long id) {
		return super.get(id);
	}
	
	@Override
	public List<RcShandie> findList(RcShandie rcShandie) {
		return super.findList(rcShandie);
	}
	
	@Override
	public Page<RcShandie> findPage(Page<RcShandie> page, RcShandie rcShandie) {
		return super.findPage(page, rcShandie);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void save(RcShandie rcShandie) {
		super.save(rcShandie);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void delete(RcShandie rcShandie) {
		super.delete(rcShandie);
	}
	
}