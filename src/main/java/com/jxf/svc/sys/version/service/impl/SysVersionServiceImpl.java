package com.jxf.svc.sys.version.service.impl;

import java.util.List;

import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
import com.jxf.svc.sys.version.dao.SysVersionDao;
import com.jxf.svc.sys.version.entity.SysVersion;
import com.jxf.svc.sys.version.entity.SysVersion.Type;
import com.jxf.svc.sys.version.service.SysVersionService;

/**
 * 系统版本ServiceImpl
 * @author wo
 * @version 2019-01-07
 */
@Service("sysVersionService")
@Transactional(readOnly = true)
public class SysVersionServiceImpl extends CrudServiceImpl<SysVersionDao, SysVersion> implements SysVersionService{

	@Autowired
	private SysVersionDao versionDao;
	
	public SysVersion get(Long id) {
		return super.get(id);
	}
	
	public List<SysVersion> findList(SysVersion sysVersion) {
		return super.findList(sysVersion);
	}
	
	public Page<SysVersion> findPage(Page<SysVersion> page, SysVersion sysVersion) {
		return super.findPage(page, sysVersion);
	}
	
	@Transactional(readOnly = false)
	public void save(SysVersion sysVersion) {
		sysVersion.setContent(StringEscapeUtils.unescapeHtml4(sysVersion.getContent()));
		super.save(sysVersion);
	}
	
	@Transactional(readOnly = false)
	public void delete(SysVersion sysVersion) {
		super.delete(sysVersion);
	}

	@Override
	public SysVersion getByType(Type type) {

		SysVersion sysVersion = new SysVersion();
		sysVersion.setType(type);
		return versionDao.getByType(sysVersion);
	}
	
}