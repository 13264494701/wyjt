package com.jxf.cms.service.impl;

import java.util.List;

import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.cms.dao.CmsNoticeDao;
import com.jxf.cms.entity.CmsNotice;
import com.jxf.cms.service.CmsNoticeService;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;

/**
 * 通知ServiceImpl
 * @author wo
 * @version 2018-10-04
 */
@Service("cmsNoticeService")
@Transactional(readOnly = true)
public class CmsNoticeServiceImpl extends CrudServiceImpl<CmsNoticeDao, CmsNotice> implements CmsNoticeService{

	
	@Autowired
	private CmsNoticeDao noticeDao;
	
	@Override
	public CmsNotice get(Long id) {
		return super.get(id);
	}
	
	@Override
	public List<CmsNotice> findList(CmsNotice cmsNotice) {
		return super.findList(cmsNotice);
	}
	
	@Override
	public Page<CmsNotice> findPage(Page<CmsNotice> page, CmsNotice cmsNotice) {
		return super.findPage(page, cmsNotice);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void save(CmsNotice cmsNotice) {
		cmsNotice.setTitle(StringEscapeUtils.unescapeHtml4(cmsNotice.getTitle()));
		cmsNotice.setContent(StringEscapeUtils.unescapeHtml4(cmsNotice.getContent()));
		if(cmsNotice.getIsNewRecord()) {
			cmsNotice.setIsPub(false);
			cmsNotice.preInsert();
			noticeDao.insert(cmsNotice);
		}else {
			cmsNotice.preUpdate();
			noticeDao.update(cmsNotice);
		}

	}
	
	@Override
	@Transactional(readOnly = false)
	public void delete(CmsNotice cmsNotice) {
		super.delete(cmsNotice);
	}

	@Override
	@Transactional(readOnly = false)
	public void pub(String id) {

		noticeDao.pub(id);
	}

	@Override
	@Transactional(readOnly = false)
	public void unpub(String id) {

		noticeDao.unpub(id);
	}

	@Override
	public Page<CmsNotice> findNoticePage(CmsNotice notice, Integer pageNo, Integer pageSize) {

		Page<CmsNotice> page = new Page<CmsNotice>(pageNo == null?1:pageNo, pageSize == null?10:pageSize);	
		notice.setPage(page);
	
		page = findPage(page, notice);
		return page;
	}
	
}