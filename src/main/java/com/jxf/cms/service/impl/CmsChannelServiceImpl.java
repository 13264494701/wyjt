package com.jxf.cms.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.cms.dao.CmsChannelDao;
import com.jxf.cms.entity.CmsChannel;
import com.jxf.cms.service.CmsChannelService;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;

/**
 * 频道信息ServiceImpl
 * @author JINXINFU
 * @version 2016-11-20
 */
@Service("cmsChannelService")
@Transactional(readOnly = true)
public class CmsChannelServiceImpl extends CrudServiceImpl<CmsChannelDao, CmsChannel> implements CmsChannelService{

	@Autowired
	private CmsChannelDao channelDao;
	
	public CmsChannel get(Long id) {
		return super.get(id);
	}
	
	public List<CmsChannel> findList(CmsChannel cmsChannel) {
		return super.findList(cmsChannel);
	}
	
	public Page<CmsChannel> findPage(Page<CmsChannel> page, CmsChannel cmsChannel) {
		return super.findPage(page, cmsChannel);
	}
	
	@Transactional(readOnly = false)
	public void save(CmsChannel cmsChannel) {
		super.save(cmsChannel);
	}
	
	@Transactional(readOnly = false)
	public void delete(CmsChannel cmsChannel) {
		super.delete(cmsChannel);
	}

	@Override
	public CmsChannel getByAlias(String alias) {
		
		return channelDao.getByAlias(alias);
	}
	
}