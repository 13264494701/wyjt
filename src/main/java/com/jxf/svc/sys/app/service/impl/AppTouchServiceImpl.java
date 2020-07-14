package com.jxf.svc.sys.app.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.app.dao.AppTouchDao;
import com.jxf.svc.sys.app.entity.AppTouch;
import com.jxf.svc.sys.app.service.AppTouchService;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;

/**
 * 应用曝光ServiceImpl
 * @author wo
 * @version 2019-07-09
 */
@Service("appTouchService")
@Transactional(readOnly = true)
public class AppTouchServiceImpl extends CrudServiceImpl<AppTouchDao, AppTouch> implements AppTouchService{

    @Override
	public AppTouch get(Long id) {
		return super.get(id);
	}
	
	@Override
	public List<AppTouch> findList(AppTouch appTouch) {
		return super.findList(appTouch);
	}
	
	@Override
	public Page<AppTouch> findPage(Page<AppTouch> page, AppTouch appTouch) {
		return super.findPage(page, appTouch);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void save(AppTouch appTouch) {
		super.save(appTouch);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void delete(AppTouch appTouch) {
		super.delete(appTouch);
	}

	@Override
	public AppTouch getByIdfa(String idfa) {
		// TODO Auto-generated method stub
		return null;
	}
	
}