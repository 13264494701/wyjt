package com.jxf.cms.service.impl;

import java.util.List;

import org.apache.commons.text.StringEscapeUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.cms.dao.CmsSiteInfoDao;
import com.jxf.cms.entity.CmsSiteInfo;
import com.jxf.cms.service.CmsSiteInfoService;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
/**
 * 站点信息ServiceImpl
 * @author JINXINFU
 * @version 2016-11-20
 */
@Service("cmsSiteInfoService")
@Transactional(readOnly = true)
public class CmsSiteInfoServiceImpl extends CrudServiceImpl<CmsSiteInfoDao, CmsSiteInfo> implements CmsSiteInfoService{

	public CmsSiteInfo get(Long id) {
		return super.get(id);
	}
	
	public List<CmsSiteInfo> findList(CmsSiteInfo cmsSiteInfo) {
		return super.findList(cmsSiteInfo);
	}
	
	public Page<CmsSiteInfo> findPage(Page<CmsSiteInfo> page, CmsSiteInfo cmsSiteInfo) {
		return super.findPage(page, cmsSiteInfo);
	}
	
	@Transactional(readOnly = false)
	public void save(CmsSiteInfo cmsSiteInfo) {
		cmsSiteInfo.setValue(StringEscapeUtils.unescapeHtml4(cmsSiteInfo.getValue()));
		super.save(cmsSiteInfo);
	}
	
	@Transactional(readOnly = false)
	public void delete(CmsSiteInfo cmsSiteInfo) {
		super.delete(cmsSiteInfo);
	}
	
}