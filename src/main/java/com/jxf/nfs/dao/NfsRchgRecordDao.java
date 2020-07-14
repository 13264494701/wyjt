package com.jxf.nfs.dao;

import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jxf.nfs.entity.NfsRchgRecord;

/**
 * 充值记录DAO接口
 * @author suhuimin
 * @version 2018-10-10
 */
@MyBatisDao
public interface NfsRchgRecordDao extends CrudDao<NfsRchgRecord> {
	
	/**
	 * 根据memberId查询放款人的充值记录
	 * @param memberId
	 * @param loanTime
	 * @return
	 */
	List<NfsRchgRecord> getRchgRecordByMemberId(@Param("memberId")Long memberId,@Param("loanTime")Date loanTime);
	
	/**
	 * 官网根据memberId查询放款人的充值记录
	 * @param memberId
	 * @return
	 */
	List<NfsRchgRecord> findListByMemberId(NfsRchgRecord nfsRchgRecord);
	
}