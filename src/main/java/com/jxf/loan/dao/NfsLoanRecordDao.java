package com.jxf.loan.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.jxf.loan.entity.NfsLoanRecord;
import com.jxf.loan.entity.NfsLoanReport;
import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;


/**
 * 借条记录DAO接口
 * @author wo
 * @version 2018-10-10
 */
@MyBatisDao
public  interface NfsLoanRecordDao extends CrudDao<NfsLoanRecord> {
	
	/**
	 * 获取案件证据
	 */
	NfsLoanRecord listCase(Long id);

	/**
	 * 查询逾期15天以上的 我是借款人的借条的个数
	 * @return 个数
	 */
	Integer countOverdueMoreThan15daysRecord(@Param("loanRecord")NfsLoanRecord nfsLoanRecord);
	

	NfsLoanReport daihuan(@Param("status") int status , @Param("id") Long id, @Param("daysAgo") int daysAgo);

	NfsLoanReport daishou(@Param("status") int status , @Param("id") Long id, @Param("daysAgo") int daysAgo);

	NfsLoanReport yuqiyihuan(@Param("status") int status , @Param("id") Long id, @Param("daysAgo") int daysAgo);

	NfsLoanReport jieru(@Param("id") Long id, @Param("daysAgo") int daysAgo);

	NfsLoanReport jiechu(@Param("id") Long id, @Param("daysAgo") int daysAgo);

	NfsLoanReport yihuan(@Param("status") int status , @Param("id") Long id, @Param("daysAgo") int daysAgo);

	NfsLoanReport yihuanyuqi(@Param("status") int status , @Param("id") Long id, @Param("daysAgo") int daysAgo);

	/**
	 * 查询全部可申请催收借条
	 * @param loanRecord
	 * @return
	 */
	List<NfsLoanRecord> findCollection(@Param("loanRecord") NfsLoanRecord loanRecord);
	
	/**
	 * 查询全部可申请仲裁借条
	 * @param nowDate
	 * @param loanRecord
	 * @return
	 */
	List<NfsLoanRecord> findApplyForArbitration(NfsLoanRecord nfsLoanRecord);
	
	/**
	 * 用户待还统计 用户待收统计
	 * @param nfsLoanRecord
	 * @return
	 */
	List<NfsLoanRecord> findBorrowandLendList(@Param("nfsLoanRecord") NfsLoanRecord nfsLoanRecord,@Param("nowDate") Date nowDate,@Param("timeType") int timeType);
	
	/**
	 * 用户逾期已还统计
	 * @param nfsLoanRecord
	 * @return
	 */
	List<NfsLoanRecord> findBorrowandLendOverList(@Param("nfsLoanRecord") NfsLoanRecord nfsLoanRecord,@Param("nowDate") Date nowDate,@Param("timeType") int timeType);
	
	/**
	 * 用户逾期未还统计
	 * @param nfsLoanRecord
	 * @return
	 */
	List<NfsLoanRecord> findBorrowOverNotReturnList(@Param("nfsLoanRecord") NfsLoanRecord nfsLoanRecord,@Param("nowDate") Date nowDate,@Param("timeType") int timeType);

	
	/**
	 * 用户按时未还统计
	 * @param nfsLoanRecord
	 * @return
	 */
	List<NfsLoanRecord> findBorrowOnTimeReturnList(@Param("nfsLoanRecord") NfsLoanRecord nfsLoanRecord,@Param("nowDate") Date nowDate,@Param("timeType") int timeType);
	
	/**
	 * 用户借入/借出
	 * @param nfsLoanRecord
	 * @return
	 */
	List<NfsLoanRecord> findLoanList(@Param("nfsLoanRecord") NfsLoanRecord nfsLoanRecord,@Param("nowDate") Date nowDate,@Param("timeType") int timeType);
	
	
	
	List<NfsLoanRecord> findListForUfang(NfsLoanRecord nfsLoanRecord);
	/**
	 * 通过detailId查借条
	 * @param nfsLoanRecord
	 * @return
	 */
	NfsLoanRecord findByApplyDetailId(@Param("detailId")Long detailId);
	
	/**
	 * 查询待还/待收/逾期 借条
	 * @return 
	 */
	List<NfsLoanRecord> findToPaidList( NfsLoanRecord loanRecord);
	/**
	 * 查询已还 借条
	 * @return 
	 */
	List<NfsLoanRecord> findClosedList(NfsLoanRecord loanRecord);
	
	/**
	 * 
	 * @return 
	 */
	List<NfsLoanRecord> findContractByMemberId(NfsLoanRecord loanRecord);
	
	/**
	 * 查找未还的分期借条
	 * @return
	 */
	List<NfsLoanRecord> findPeriodRepayLoanList();

	/**
	 * 查询谁欠我钱集合
	 * @param loanRecord
	 * @return
	 */
	List<NfsLoanRecord> findWhoOweMeList(NfsLoanRecord record);
	
	
	/**
	 * 放款人统计放款笔数
	 * @param loanRecord
	 * @return
	 */
	int loanerCountLoan(NfsLoanRecord loanRecord);
	/**
	 * 借款人统计借款笔数
	 * @param loaneeId
	 * @return
	 */
	int loaneeCountLoan(NfsLoanRecord loanRecord);
	
	/**
	 * 放款人统计放款金额
	 * @param loanRecord
	 * @return
	 */
	BigDecimal loanerSumLoan(NfsLoanRecord loanRecord);
	
	/**
	 *  借款人统计借款总额
	 * @param loaneeId
	 * @return
	 */
	BigDecimal loaneeSumLoan(NfsLoanRecord loanRecord);
	
	
	/**
	 * 可转让列表
	 * @param record
	 * @return
	 */
	List<NfsLoanRecord> findAuctionList(NfsLoanRecord record);
	/**
	 * 查询ufang会员借条id
	 * @return
	 */
	List<Long> findUfangUserLoanCount(@Param("paramMap")Map<String, Object> paramMap);
	/**
	 * 查询优放逾期借条金额
	 * @param loanIds
	 * @return
	 */
	BigDecimal sumOverdueLoanAmount(List<Long> loanIds);
	
	/**
	 *  查询优放机构员工的借条
	 * @param paramMap
	 * @return
	 */
	List<NfsLoanRecord> getUfangUserLoanList(@Param("paramMap")Map<String, Object> paramMap);



	/**
	 * 未逾期待还
	 * @param nfsLoanRecord
	 * @param nowDate
	 * @param timeType
	 * @return
	 */
	List<NfsLoanRecord> penddingRepayList(@Param("nfsLoanRecord") NfsLoanRecord nfsLoanRecord,@Param("nowDate") Date nowDate,@Param("timeType") int timeType);
	
}