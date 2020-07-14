package com.jxf.loan.dao;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jxf.loan.entity.NfsLoanArbitration;
import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;

/**
 * 借条仲裁增删改查DAO接口
 * @author LIUHUAIXIN
 * @version 2018-11-07
 */
@MyBatisDao
public interface NfsLoanArbitrationDao extends CrudDao<NfsLoanArbitration> {
	
	
	/**
	 * 查询全部仲裁中的借条
	 * @param loanId
	 * @param arbitration
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	List<NfsLoanArbitration> findInArbitration(@Param("loanerId") Long loanerId,@Param("nfsLoanArbitration") NfsLoanArbitration nfsLoanArbitration,@Param("trxType") int trxType);

	/**
	 * 查询全部仲裁出裁决的借条
	 * @param loanId
	 * @param arbitration
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	List<NfsLoanArbitration> findArbitration(@Param("loanerId") Long loanerId,@Param("nfsLoanArbitration") NfsLoanArbitration nfsLoanArbitration,@Param("trxType") int trxType);

	/**
	 * 查询全部仲裁已完成的借条
	 * @param loanId
	 * @param arbitration
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	List<NfsLoanArbitration> findEndOfArbitration(@Param("loanerId") Long loanerId,@Param("nfsLoanArbitration") NfsLoanArbitration nfsLoanArbitration,@Param("trxType") int trxType);

	/**
	 * 获取仲裁证据列表
	 * @return
	 */
	List<NfsLoanArbitration> findTransferCase();
	
	/**
	 *获取可申请强执列表
	 */
	List<NfsLoanArbitration> findExecution(@Param("id")Long id,@Param("nfsLoanArbitration")NfsLoanArbitration nfsLoanArbitration,@Param("trxType") int trxType);


	/**
	 * 我的仲裁记录
	 * @param nfsLoanArbitration
	 * @return
	 */
	List<NfsLoanArbitration> findMemberArbitrationList(NfsLoanArbitration nfsLoanArbitration);

	/**
	 * 根据仲裁id查询仲裁记录
	 * @param loanId
	 * @return
	 */
	NfsLoanArbitration getByLoanId(Long loanId);
}