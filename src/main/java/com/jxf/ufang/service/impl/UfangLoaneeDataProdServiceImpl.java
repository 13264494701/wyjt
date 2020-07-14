package com.jxf.ufang.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
import com.jxf.ufang.dao.UfangLoaneeDataProdDao;
import com.jxf.ufang.entity.UfangLoaneeDataProd;
import com.jxf.ufang.service.UfangLoaneeDataProdService;
/**
 * 借款人数据产品表ServiceImpl
 * @author gaobo
 * @version 2019-07-19
 */
@Service("ufangLoaneeDataProdService")
@Transactional(readOnly = true)
public class UfangLoaneeDataProdServiceImpl extends CrudServiceImpl<UfangLoaneeDataProdDao, UfangLoaneeDataProd> implements UfangLoaneeDataProdService{

    @Override
	public UfangLoaneeDataProd get(Long id) {
		return super.get(id);
	}
	
	@Override
	public List<UfangLoaneeDataProd> findList(UfangLoaneeDataProd ufangLoaneeDataProd) {
		return super.findList(ufangLoaneeDataProd);
	}
	
	@Override
	public Page<UfangLoaneeDataProd> findPage(Page<UfangLoaneeDataProd> page, UfangLoaneeDataProd ufangLoaneeDataProd) {
		return super.findPage(page, ufangLoaneeDataProd);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void save(UfangLoaneeDataProd ufangLoaneeDataProd) {
		super.save(ufangLoaneeDataProd);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void delete(UfangLoaneeDataProd ufangLoaneeDataProd) {
		super.delete(ufangLoaneeDataProd);
	}
	
}