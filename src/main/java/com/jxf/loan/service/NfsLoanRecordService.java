package com.jxf.loan.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.jxf.loan.entity.NfsLoanApplyDetail;
import com.jxf.loan.entity.NfsLoanPartialAndDelay;
import com.jxf.loan.entity.NfsLoanRecord;
import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberLoanReport;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.CrudService;
import com.jxf.web.model.ResponseData;
import com.jxf.web.model.gxt.CreditInfoResponseResult;
import com.jxf.web.model.gxt.LoanDetailForGxtResponseResult;
import com.jxf.web.model.wyjt.app.auction.CrTransferListRequestParam;
import com.jxf.web.model.wyjt.app.auction.CrTransferListResponseResult;
import com.jxf.web.model.wyjt.app.loan.LoanDetailForAppResponseResult;


/**
 * 借条记录Service
 * @author wo
 * @version 2018-10-10
 */
public interface NfsLoanRecordService extends CrudService<NfsLoanRecord> {

	
	/***
	 * 
	 * 函数功能说明              分页获取借款列表
	 * wo  2018年09月26日 
	 * 修改者名字 修改日期 
	 * 修改内容 
	 * @参数： @param loanRecord
	 * @参数： @param pageNo
	 * @参数： @param pageSize
	 * @参数： @return     
	 * @return Page<NfsLoanApply>    
	 * @throws
	 */
	Page<NfsLoanRecord> findPage(NfsLoanRecord loanRecord, Integer pageNo, Integer pageSize);
	
	
    Page<NfsLoanRecord> findPageForUfang(Page<NfsLoanRecord> page, NfsLoanRecord loanRecord);

	
	/**
	 * 获取案件证据
	 */
	NfsLoanRecord listCase(Long id);

	/**
	 * 好友借贷分析
	 * @return
	 */
	void loanReport(MemberLoanReport memberLoanReport);

	/**
	 * 获取详情
	 * @param loanId 借条ID (单人/多人我收到 的申请 时候是detailId 单人借条是recordId 多人我发起的是applyId)
	 * @param type 查询的对象类型   0是apply 1是detail 2是record
	 * @return
	 */
	LoanDetailForAppResponseResult getDetail(String loanId, Integer type,Member member);

		/**
	 * 查询全部可申请仲裁借条
	 * @param loanRecord
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	Page<NfsLoanRecord> findApplyForArbitration(NfsLoanRecord loanRecord,Integer pageNo,Integer pageSize);
	
	
	/**
	  * 判断与好友之间是否存在借条关系
	 * @param merber
	 * @param friend
	 * @return
	 */
	NfsLoanRecord findMyandFriendLoan(Member merber,Member friend);

	/**
	  * 根据detailId
	 * @param detailId
	 * @return 借条
	 */
	NfsLoanRecord findByApplyDetailId(Long detailId);
	
	/**
	 * 查询全部可申请催收借条
	 * @param loanRecord
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	Page<NfsLoanRecord> findCollection(NfsLoanRecord loanRecord,Integer pageNo,Integer pageSize);

	/**
	 * 获取record的进度
	 * @param record
	 * @param member 
	 * @return
	 */
	String getRecordProgress(NfsLoanRecord record, Member member);
	

	/**
	 * 生成借条
	 * @return
	 */
	NfsLoanRecord createLoanRecord(NfsLoanApplyDetail applyDetail,Integer trxType);
	/**
	 * 查询逾期15天以上的 我是借款人的借条的个数
	 * @return 个数
	 */
	Integer countOverdueMoreThan15daysRecord(Member member);

	/**
	 * 分页获取待还/待收/逾期 借款列表
	 * @参数： @param loanRecord
	 * @参数： @param pageNo
	 * @参数： @param pageSize
	 * @参数： @return     
	 * @return Page<NfsLoanApply>    
	 * @throws
	 */
	Page<NfsLoanRecord> findToPaidPage(NfsLoanRecord loanRecord, Integer pageNo, Integer pageSize);

	/**
	 * 分页获取已还借款列表
	 * @参数： @param loanRecord
	 * @参数： @param pageNo
	 * @参数： @param pageSize
	 * @参数： @return     
	 * @return Page<NfsLoanApply>    
	 * @throws
	 */
	Page<NfsLoanRecord> findClosedPage(NfsLoanRecord loanRecord, Integer pageNo, Integer pageSize);

	/**
	 * 放款人主动销借条
	 * @param loanRecord
	 * @param member 
	 */
	void closeLoanLineDown(NfsLoanRecord loanRecord);

	/**
	 * 放款人同意或者拒绝借款人的线下已还款申请
	 * @param loanRecord
	 * @return 
	 */
	String replyLineDown(NfsLoanRecord loanRecord,String isAgree);
	
	/**
	 * 官网借条展示
	 * @return 
	 */

	Page<NfsLoanRecord> findContractByMemberId(NfsLoanRecord loanRecord,Page<NfsLoanRecord> page);
	
	/**
	 * 查询借款人的借条数量
	 * @param loanee
	 * @return
	 */
	int countLoaneeLoan(NfsLoanRecord loanRecord);
	/**
	 * 统计借款人的借条总额
	 * @param loanee
	 * @return
	 */
	BigDecimal sumLoaneeLoan(NfsLoanRecord loanRecord);

	/**
	 * 查找未还的分期借条
	 * @return
	 */
	List<NfsLoanRecord> findPeriodRepayLoanList();


	/**
	 * 部分还款状态变回初始 解冻借款人冻结资金
	 * @param record
	 * @param partialAmount 
	 * @param partialAndDelay 
	 * @return 
	 */
	int returnStatusToInitial(NfsLoanRecord record, BigDecimal partialAmount, NfsLoanPartialAndDelay partialAndDelay);
	
	
	
	void filter(List<NfsLoanRecord> loans);

	/**
	 * 	可转让的列表查询
	 * @param member
	 * @param reqData
	 * @param result
	 * @return	
	 */
	ResponseData findCrTransferPage(Member member, CrTransferListRequestParam reqData,CrTransferListResponseResult result);

	/**
	 * 谁欠我钱借条集合
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
	 * 放款人统计放款金额
	 * @param loanRecord
	 * @return
	 */
	BigDecimal loanerSumLoan(NfsLoanRecord loanRecord);
	
	/**
	 * 查询ufang借条id
	 * @return
	 */
	List<Long> findUfangUserLoanCount(Map<String, Object> paramMap);
	
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

	
	
	NfsLoanRecord closeArbitrationAndCollection(NfsLoanRecord loanRecord);

	/**
	 * 生成借条-不走账户资金的
	 * @param applyDetail
	 * @return
	 */
	NfsLoanRecord createLoanRecordForOffLine(NfsLoanApplyDetail applyDetail);

	/**
	 * 获取公信堂借条详情
	 * @param loanId
	 * @param type
	 * @param member
	 * @return
	 */
	LoanDetailForGxtResponseResult getGxtLoanDetail(String loanId,Integer type, Member member);


	/**
	 * 获取借条进度
	 * @param loanRecord
	 * @return
	 */
	Integer getGxtLoanRecordProgress(NfsLoanRecord loanRecord);

	/**
	 * 	获取借贷详情
	 * @param member
	 * @return
	 */
	CreditInfoResponseResult getLoanInfo(Member member);
	
	/**
	 * 	获取逾期信息
	 * @param member
	 * @return
	 */
	Map<String, Object> getOverdueDetail(Member member, int days, int timeType);
	/**
	 * 	获取借贷统计分析
	 * @param member
	 * @return
	 */
    Map<String, Object> getLoanInfo(Member member, int days);


}