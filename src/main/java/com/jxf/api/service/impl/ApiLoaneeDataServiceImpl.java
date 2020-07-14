package com.jxf.api.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.api.dao.ApiLoaneeDataDao;
import com.jxf.api.entity.ApiLoaneeData;
import com.jxf.api.service.ApiLoaneeDataService;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
/**
 * 给第三方查询流量ServiceImpl
 * @author XIAORONGDIAN
 * @version 2019-04-17
 */
@Service("apiLoaneeDataService")
@Transactional(readOnly = true)
public class ApiLoaneeDataServiceImpl extends CrudServiceImpl<ApiLoaneeDataDao, ApiLoaneeData> implements ApiLoaneeDataService{
	
	@Autowired
	private ApiLoaneeDataDao apiLoaneeDataDao;
	
    @Override
	public ApiLoaneeData get(Long id) {
		return super.get(id);
	}
	
	@Override
	public List<ApiLoaneeData> findList(ApiLoaneeData apiLoaneeData) {
		return super.findList(apiLoaneeData);
	}
	
	@Override
	public Page<ApiLoaneeData> findPage(Page<ApiLoaneeData> page, ApiLoaneeData apiLoaneeData) {
		return super.findPage(page, apiLoaneeData);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void save(ApiLoaneeData apiLoaneeData) {
		super.save(apiLoaneeData);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void delete(ApiLoaneeData apiLoaneeData) {
		super.delete(apiLoaneeData);
	}

	@Override
	public ApiLoaneeData getNearest(String merchantNumber) {
		return apiLoaneeDataDao.getNearest(merchantNumber);
	}
	
}