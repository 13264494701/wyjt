package com.jxf.ufang.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
import com.jxf.ufang.dao.ZZCUploadDao;
import com.jxf.ufang.entity.ZZCUpload;
import com.jxf.ufang.service.ZZCUploadService;
/**
 * 中智诚上报ServiceImpl
 * @author XIAORONGDIAN
 * @version 2019-04-22
 */
@Service("zZCUploadService")
@Transactional(readOnly = true)
public class ZZCUploadServiceImpl extends CrudServiceImpl<ZZCUploadDao, ZZCUpload> implements ZZCUploadService{

    @Override
	public ZZCUpload get(Long id) {
		return super.get(id);
	}
	
	@Override
	public List<ZZCUpload> findList(ZZCUpload zZCUpload) {
		return super.findList(zZCUpload);
	}
	
	@Override
	public Page<ZZCUpload> findPage(Page<ZZCUpload> page, ZZCUpload zZCUpload) {
		return super.findPage(page, zZCUpload);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void save(ZZCUpload zZCUpload) {
		super.save(zZCUpload);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void delete(ZZCUpload zZCUpload) {
		super.delete(zZCUpload);
	}
	
}