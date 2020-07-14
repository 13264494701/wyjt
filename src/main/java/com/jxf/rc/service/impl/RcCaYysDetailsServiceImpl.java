package com.jxf.rc.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
import com.jxf.rc.entity.RcCaYysDetails;
import com.jxf.rc.dao.RcCaYysDetailsDao;
import com.jxf.rc.service.RcCaYysDetailsService;
/**
 * 信用档案运营商通话与花费详情ServiceImpl
 * @author lmy
 * @version 2018-12-17
 */
@Service("rcCaYysDetailsService")
@Transactional(readOnly = true)
public class RcCaYysDetailsServiceImpl extends CrudServiceImpl<RcCaYysDetailsDao, RcCaYysDetails> implements RcCaYysDetailsService{

	public RcCaYysDetails get(Long id) {
		return super.get(id);
	}
	
	public List<RcCaYysDetails> findList(RcCaYysDetails rcCaYysDetails) {
		return super.findList(rcCaYysDetails);
	}
	
	public Page<RcCaYysDetails> findPage(Page<RcCaYysDetails> page, RcCaYysDetails rcCaYysDetails) {
		return super.findPage(page, rcCaYysDetails);
	}
	
	@Transactional(readOnly = false)
	public void save(RcCaYysDetails rcCaYysDetails) {
		super.save(rcCaYysDetails);
	}
	
	@Transactional(readOnly = false)
	public void delete(RcCaYysDetails rcCaYysDetails) {
		super.delete(rcCaYysDetails);
	}
	
}