package com.jxf.cms.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.cms.dao.TagDao;
import com.jxf.cms.entity.Tag;
import com.jxf.cms.service.TagService;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;

/**
 * 标签ServiceImpl
 * @author JINXINFU
 * @version 2016-04-26
 */
@Service("shopTagService")
@Transactional(readOnly = true)
public class TagServiceImpl extends CrudServiceImpl<TagDao, Tag> implements TagService{
    
	@Autowired
	private TagDao tagDao;
	
	@Override
	public Tag get(Long id) {
		return super.get(id);
	}
	
	@Override
	public List<Tag> findList(Tag shopTag) {
		return super.findList(shopTag);
	}
	
	@Override
	public Page<Tag> findPage(Page<Tag> page, Tag shopTag) {
		return super.findPage(page, shopTag);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void save(Tag shopTag) {
		super.save(shopTag);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void delete(Tag shopTag) {
		super.delete(shopTag);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Tag> findList(String tagType) {
		
		Tag shopTag = new Tag();
		shopTag.setTagType(tagType);
		return findList(shopTag);
	}
	/**
	 * 根据编码获取tag
	 */
	@Override
	public Tag getByTagCode(String tagCode) {
		return tagDao.getByTagCode(tagCode);
	}
}