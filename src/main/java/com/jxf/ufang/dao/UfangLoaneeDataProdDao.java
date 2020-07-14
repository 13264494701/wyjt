package com.jxf.ufang.dao;

import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;
import com.jxf.ufang.entity.UfangLoaneeDataProd;

/**
 * 借款人数据产品表DAO接口
 * @author gaobo
 * @version 2019-07-19
 */
@MyBatisDao
public interface UfangLoaneeDataProdDao extends CrudDao<UfangLoaneeDataProd> {
	
}