package com.jxf.mms.sender.dao;

import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;
import com.jxf.mms.sender.entity.MmsSender;

/**
 * 发件人列表DAO接口
 * @author JINXINFU
 * @version 2016-04-08
 */
@MyBatisDao
public interface MmsSenderDao extends CrudDao<MmsSender> {
	MmsSender getBySenderName(String senderName);
}