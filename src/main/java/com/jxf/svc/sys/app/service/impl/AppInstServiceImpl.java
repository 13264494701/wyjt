package com.jxf.svc.sys.app.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.app.dao.AppInstDao;
import com.jxf.svc.sys.app.entity.AppInst;
import com.jxf.svc.sys.app.service.AppInstService;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;

/**
 * 应用激活ServiceImpl
 * @author wo
 * @version 2019-07-09
 */
@Service("appInstService")
@Transactional(readOnly = true)
public class AppInstServiceImpl extends CrudServiceImpl<AppInstDao, AppInst> implements AppInstService{

	@Autowired
	private AppInstDao appInstDao;
	
    @Override
	public AppInst get(Long id) {
		return super.get(id);
	}
	
	@Override
	public List<AppInst> findList(AppInst appInst) {
		return super.findList(appInst);
	}
	
	@Override
	public Page<AppInst> findPage(Page<AppInst> page, AppInst appInst) {
		return super.findPage(page, appInst);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void save(AppInst appInst) {
		super.save(appInst);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void delete(AppInst appInst) {
		super.delete(appInst);
	}

	@Override
	public Boolean idfaExist(String idfa) {
		return appInstDao.idfaExist(idfa);
	}
	
}