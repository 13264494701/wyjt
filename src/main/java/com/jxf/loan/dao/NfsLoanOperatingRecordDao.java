package com.jxf.loan.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jxf.loan.entity.NfsLoanOperatingRecord;
import com.jxf.svc.sys.crud.dao.CrudDao;
import com.jxf.svc.annotation.MyBatisDao;

/**
 * 借条操作记录DAO接口
 * @author XIAORONGDIAN
 * @version 2018-12-18
 */
@MyBatisDao
public interface NfsLoanOperatingRecordDao extends CrudDao<NfsLoanOperatingRecord> {
	
	/**
	 * 用户借入借出
	 * @param nfsLoanRecord
	 * @return
	 */
	List<NfsLoanOperatingRecord> findBorrowandLendZongList(@Param("nfsLoanOperatingRecord") NfsLoanOperatingRecord nfsLoanOperatingRecord,@Param("nowDate") Date nowDate,@Param("timeType")  int timeType);
	
	/**
	 * 用户延期
	 * @param nfsLoanRecord
	 * @return
	 */
	List<NfsLoanOperatingRecord> findBorrowDelyList(@Param("nfsLoanOperatingRecord") NfsLoanOperatingRecord nfsLoanOperatingRecord,@Param("nowDate") Date nowDate,@Param("timeType")  int timeType);
		
	
	
}