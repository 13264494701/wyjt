package com.jxf.ufang.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
import com.jxf.ufang.dao.UfangFundDistCollDao;
import com.jxf.ufang.entity.UfangFundDistColl;
import com.jxf.ufang.service.UfangFundDistCollService;


/**
 * 资金分发归集ServiceImpl
 * @author wo
 * @version 2018-11-22
 */
@Service("ufangFundDistCollService")
@Transactional(readOnly = true)
public class UfangFundDistCollServiceImpl extends CrudServiceImpl<UfangFundDistCollDao, UfangFundDistColl> implements UfangFundDistCollService{

	public UfangFundDistColl get(Long id) {
		return super.get(id);
	}
	
	public List<UfangFundDistColl> findList(UfangFundDistColl ufangFundDistColl) {
		return super.findList(ufangFundDistColl);
	}
	
	public Page<UfangFundDistColl> findPage(Page<UfangFundDistColl> page, UfangFundDistColl ufangFundDistColl) {
		return super.findPage(page, ufangFundDistColl);
	}
	
	@Transactional(readOnly = false)
	public void save(UfangFundDistColl ufangFundDistColl) {
		super.save(ufangFundDistColl);
	}
	
	@Transactional(readOnly = false)
	public void delete(UfangFundDistColl ufangFundDistColl) {
		super.delete(ufangFundDistColl);
	}
	
}