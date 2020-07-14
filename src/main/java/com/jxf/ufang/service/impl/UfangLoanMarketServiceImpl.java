package com.jxf.ufang.service.impl;


import java.util.List;

import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
import com.jxf.ufang.dao.UfangLoanMarketDao;

import com.jxf.ufang.entity.UfangLoanMarket;
import com.jxf.ufang.service.UfangLoanMarketService;


/**
 * 贷超管理ServiceImpl
 * @author wo
 * @version 2019-03-07
 */
@Service("ufangLoanMarketService")
@Transactional(readOnly = true)
public class UfangLoanMarketServiceImpl extends CrudServiceImpl<UfangLoanMarketDao, UfangLoanMarket> implements UfangLoanMarketService{

	@Autowired
	private UfangLoanMarketDao loanMarketDao;
	
	public UfangLoanMarket get(Long id) {
		return super.get(id);
	}
	
	public List<UfangLoanMarket> findList(UfangLoanMarket ufangLoanMarket) {
		return super.findList(ufangLoanMarket);
	}
	
	public Page<UfangLoanMarket> findPage(Page<UfangLoanMarket> page, UfangLoanMarket ufangLoanMarket) {
		return super.findPage(page, ufangLoanMarket);
	}
	
	@Transactional(readOnly = false)
	public void save(UfangLoanMarket ufangLoanMarket) {
		ufangLoanMarket.setLoanRequirement(StringEscapeUtils.unescapeHtml4(ufangLoanMarket.getLoanRequirement()));
		ufangLoanMarket.setRedirectUrl(StringEscapeUtils.unescapeHtml4(ufangLoanMarket.getRedirectUrl()));
		if(ufangLoanMarket.getIsNewRecord()) {
			ufangLoanMarket.setIsMarketable(false);
			ufangLoanMarket.preInsert();
			loanMarketDao.insert(ufangLoanMarket);
		}else {
			ufangLoanMarket.preUpdate();
			loanMarketDao.update(ufangLoanMarket);
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(UfangLoanMarket ufangLoanMarket) {
		super.delete(ufangLoanMarket);
	}
	
}