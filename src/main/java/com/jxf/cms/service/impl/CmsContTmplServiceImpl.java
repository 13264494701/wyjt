package com.jxf.cms.service.impl;

import java.util.List;

import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.cms.dao.CmsContTmplDao;
import com.jxf.cms.entity.CmsContTmpl;
import com.jxf.cms.service.CmsContTmplService;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;

/**
 * 合同模板ServiceImpl
 * @author huojiayuan
 * @version 2016-12-01
 */
@Service("contTmplService")
@Transactional(readOnly = true)
public class CmsContTmplServiceImpl extends CrudServiceImpl<CmsContTmplDao, CmsContTmpl> implements CmsContTmplService{

	@Autowired 
	private CmsContTmplDao contTmplDao;
	
	@Override
	public CmsContTmpl get(Long id) {
		return super.get(id);
	}
	
	@Override
	public List<CmsContTmpl> findList(CmsContTmpl contTmpl) {
		return super.findList(contTmpl);
	}
	
	@Override
	public Page<CmsContTmpl> findPage(Page<CmsContTmpl> page, CmsContTmpl contTmpl) {
		return super.findPage(page, contTmpl);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void save(CmsContTmpl contTmpl) {
		contTmpl.setContent(StringEscapeUtils.unescapeHtml4(contTmpl.getContent()));
		if (contTmpl.getIsNewRecord()){
			contTmpl.setIsStatic(false);
			contTmpl.preInsert();
			contTmplDao.insert(contTmpl);
		}else{
			contTmpl.setIsStatic(false);
			contTmpl.preUpdate();
			contTmplDao.update(contTmpl);
		}
	}
	
	@Override
	@Transactional(readOnly = false)
	public void delete(CmsContTmpl contTmpl) {
		super.delete(contTmpl);
	}
	/**
	 * 获取已生效的注册协议
	 */
	@Override
	public CmsContTmpl getRegContract() {
		CmsContTmpl contTmpl = new CmsContTmpl();
		contTmpl.setType(CmsContTmpl.Type.registration);
		contTmpl.setStatus(CmsContTmpl.Status.valid);
		return contTmplDao.getContTmpl(contTmpl);
	}

	@Override
	@Transactional(readOnly = false)
	public void setStatic(CmsContTmpl contTmpl) {
		contTmpl.setIsStatic(true);
		contTmpl.preUpdate();
		contTmplDao.update(contTmpl);	
	}
	
}