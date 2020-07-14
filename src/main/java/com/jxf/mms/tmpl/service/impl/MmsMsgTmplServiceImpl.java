package com.jxf.mms.tmpl.service.impl;

import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

import com.jxf.mms.tmpl.entity.MmsMsgTmpl;
import com.jxf.mms.tmpl.dao.MmsMsgTmplDao;
import com.jxf.mms.tmpl.service.MmsMsgTmplService;
/**
 * 消息模板ServiceImpl
 * @author wo
 * @version 2018-10-28
 */
@Service("mmsMsgTmplService")
@Transactional(readOnly = true)
public class MmsMsgTmplServiceImpl extends CrudServiceImpl<MmsMsgTmplDao, MmsMsgTmpl> implements MmsMsgTmplService{

	private final Configuration config = new Configuration(Configuration.VERSION_2_3_22);
	{
		config.setDefaultEncoding("UTF-8");
		config.setTemplateLoader(new StringTemplateLoader());
	}
	
	@Autowired
	private MmsMsgTmplDao msmMsgTmplDao;
	
	public MmsMsgTmpl get(Long id) {
		return super.get(id);
	}
	
	public List<MmsMsgTmpl> findList(MmsMsgTmpl mmsMsgTmpl) {
		return super.findList(mmsMsgTmpl);
	}
	
	public Page<MmsMsgTmpl> findPage(Page<MmsMsgTmpl> page, MmsMsgTmpl mmsMsgTmpl) {
		return super.findPage(page, mmsMsgTmpl);
	}
	
	@Transactional(readOnly = false)
	public void save(MmsMsgTmpl mmsMsgTmpl) {
		super.save(mmsMsgTmpl);
	}
	
	@Transactional(readOnly = false)
	public void delete(MmsMsgTmpl mmsMsgTmpl) {
		super.delete(mmsMsgTmpl);
	}

	@Override
	public MmsMsgTmpl getTmplByCode(String code) {
		return msmMsgTmplDao.getTmplByCode(code);
	}

	@Override
	public String process(String code, Map<String, Object> parameters) {
		
		MmsMsgTmpl msgTmpl = getTmplByCode(code);

		StringWriter result = new StringWriter();
		try {
			new Template(code, msgTmpl.getContent(), config).process(
					parameters, result);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return result.toString();
	}
	
}