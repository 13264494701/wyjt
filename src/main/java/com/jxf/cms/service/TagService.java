package com.jxf.cms.service;

import java.util.List;

import com.jxf.cms.entity.Tag;
import com.jxf.svc.sys.crud.service.CrudService;

/**
 * 标签Service
 * @author JINXINFU
 * @version 2016-04-26
 */
public interface TagService extends CrudService<Tag> {

	/**
	 * 查找标签
	 * 
	 * @param type
	 *            类型
	 * @return 标签
	 */
	List<Tag> findList(String tagType);



	public Tag getByTagCode(String tagCode);
}