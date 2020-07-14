package com.jxf.loan.dao;

import com.jxf.loan.entity.NfsLoanDetailMessage;
import com.jxf.svc.sys.crud.dao.CrudDao;
import com.jxf.svc.annotation.MyBatisDao;

/**
 * 借条详情对话记录DAO接口
 * @author XIAORONGDIAN
 * @version 2018-12-03
 */
@MyBatisDao
public interface NfsLoanDetailMessageDao extends CrudDao<NfsLoanDetailMessage> {
	
}