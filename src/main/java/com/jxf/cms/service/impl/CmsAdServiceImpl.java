package com.jxf.cms.service.impl;

import java.util.List;

import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.cms.dao.CmsAdDao;
import com.jxf.cms.entity.CmsAd;
import com.jxf.cms.service.CmsAdService;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;

/**
 * 
 * @类功能说明： 广告
 * @类修改者： 
 * @修改日期： 
 * @修改说明： 
 * @公司名称：北京金信服科技有限公司
 * @作者：HUOJIABAO 
 * @创建时间：2016年4月21日 下午2:10:13 
 * @版本：V1.0
 */
@Service("adService")
@Transactional(readOnly = true)
public class CmsAdServiceImpl extends CrudServiceImpl<CmsAdDao, CmsAd> implements CmsAdService{

	@Autowired
	private CmsAdDao adDao;
	
	@Override
	public CmsAd get(Long id) {
		return super.get(id);
	}
	
	@Override
	public List<CmsAd> findList(CmsAd ad) {
		return super.findList(ad);
	}
	
	@Override
	public Page<CmsAd> findPage(Page<CmsAd> page, CmsAd ad) {
		return super.findPage(page, ad);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void save(CmsAd ad) {
		ad.setTextContent(StringEscapeUtils.unescapeHtml4(ad.getTextContent()));
		ad.setRedirectUrl(StringEscapeUtils.unescapeHtml4(ad.getRedirectUrl()));
		if(ad.getIsNewRecord()){	
			ad.setIsEnabled(false);
			ad.preInsert();
			adDao.insert(ad);
		}else{
			ad.preUpdate();
			adDao.update(ad);
		}
	}
	
	@Override
	@Transactional(readOnly = false)
	public void delete(CmsAd ad) {
		super.delete(ad);
	}

	@Override
	public List<CmsAd> findListByPosition(String positionNo) {
	
		return adDao.findAdsByPositionNo(positionNo);
	}
	
}