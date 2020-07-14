package com.jxf.mms.sender.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
import com.jxf.mms.sender.entity.MmsSender;
import com.jxf.mms.sender.dao.MmsSenderDao;
import com.jxf.mms.sender.service.MmsSenderService;
import com.jxf.mms.sender.utils.MmsSenderUtils;
/**
 * 发件人ServiceImpl
 * @author JINXINFU
 * @version 2016-04-08
 */
@Service("mmsSenderService")
@Transactional(readOnly = true)
public class MmsSenderServiceImpl extends CrudServiceImpl<MmsSenderDao, MmsSender> implements MmsSenderService{

	public MmsSender get(Long id) {
		return super.get(id);
	}
	
	public List<MmsSender> findList(MmsSender mmsSender) {
		return super.findList(mmsSender);
	}
	
	public Page<MmsSender> findPage(Page<MmsSender> page, MmsSender mmsSender) {
		return super.findPage(page, mmsSender);
	}
	
	@Transactional(readOnly = false)
	public void save(MmsSender mmsSender) {
		super.save(mmsSender);
		MmsSenderUtils.removeCache(MmsSenderUtils.CACHE_SENDER);
	}
	
	@Transactional(readOnly = false)
	public void delete(MmsSender mmsSender) {
		super.delete(mmsSender);
		MmsSenderUtils.removeCache(MmsSenderUtils.CACHE_SENDER);
	}
	
}