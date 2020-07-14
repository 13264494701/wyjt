package com.jxf.loan.dao;

import com.jxf.svc.sys.crud.dao.CrudDao;

import javax.ws.rs.PathParam;

import com.jxf.loan.entity.NfsLoanPreservation;
import com.jxf.svc.annotation.MyBatisDao;

/**
 * 业务保全DAO接口
 * @author SuHuimin
 * @version 2019-07-01
 */
@MyBatisDao
public interface NfsLoanPreservationDao extends CrudDao<NfsLoanPreservation> {

	NfsLoanPreservation getPreservationByMemberId(@PathParam("memberId") Long memberId);
	
}