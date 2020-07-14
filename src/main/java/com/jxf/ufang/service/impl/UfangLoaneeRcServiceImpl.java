package com.jxf.ufang.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
import com.jxf.ufang.dao.UfangLoaneeRcDao;
import com.jxf.ufang.entity.UfangLoaneeRc;
import com.jxf.ufang.service.UfangLoaneeRcService;

/**
 * 借款人风控数据ServiceImpl
 * @author wo
 * @version 2019-04-28
 */
@Service("ufangLoaneeRcService")
@Transactional(readOnly = true)
public class UfangLoaneeRcServiceImpl extends CrudServiceImpl<UfangLoaneeRcDao, UfangLoaneeRc> implements UfangLoaneeRcService{

	@Autowired
	private UfangLoaneeRcDao loanRecordDataDao;
	
    @Override
	public UfangLoaneeRc get(Long id) {
		return super.get(id);
	}
	
	@Override
	public List<UfangLoaneeRc> findList(UfangLoaneeRc ufangLoaneeRc) {
		return super.findList(ufangLoaneeRc);
	}
	
	@Override
	public Page<UfangLoaneeRc> findPage(Page<UfangLoaneeRc> page, UfangLoaneeRc ufangLoaneeRc) {
		return super.findPage(page, ufangLoaneeRc);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void save(UfangLoaneeRc ufangLoaneeRc) {
		super.save(ufangLoaneeRc);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void delete(UfangLoaneeRc ufangLoaneeRc) {
		super.delete(ufangLoaneeRc);
	}

	@Override
	public UfangLoaneeRc getByPhoneNo(String phoneNo) {

		return loanRecordDataDao.getByPhoneNo(phoneNo);
	}
	
}