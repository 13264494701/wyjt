package com.jxf.mem.service;

import java.math.BigDecimal;
import java.util.List;

import com.jxf.loan.entity.NfsLoanRecord;
import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberActTrx;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.CrudService;

/**
 * 账户交易记录Service
 * @author zhj
 * @version 2016-05-13
 */
public interface MemberActTrxService extends CrudService<MemberActTrx> {
		
	BigDecimal sumAmount(Member member,String trxCode);
	
	Page<MemberActTrx> findMemberActTrxPage(Member member, Integer pageNo, Integer pageSize);

	Page<MemberActTrx> findActTrxListPage(MemberActTrx memberActTrx,
			Integer pageNo, Integer pageSize);

	/**
	 * 根据group 获取总支出和总收入
	 * @param memberActTrx
	 * @return
	 */
	MemberActTrx countTotalAccountByGroup(MemberActTrx memberActTrx);
	
	/**
	 * 根据用户和借条查询仲裁证据流水
	 * @param member
	 * @param loanRecord
	 * @return
	 */
	List<MemberActTrx> getTrxByLoanArbitrationProof(Member member,NfsLoanRecord loanRecord);
	
	/**
	 * 根据业务Id查询资金明细
	 * @param orgId
	 * @return
	 */
	List<MemberActTrx> findListByOrgId(Long orgId);

	/***
	 * 获取账户流水图标
	 * @param actTrx
	 * @return
	 */
	String getImage(MemberActTrx actTrx);
	/***
	 * APP获取全部账户流水
	 * @param actTrx
	 * @return
	 */
	Page<MemberActTrx> findAllActTrxListPage(MemberActTrx memberActTrx,
			Integer pageNo, Integer pageSize);
	
	/**
	 * 处理主动放款失败
	 * @param memberActTrx
	 * @return
	 */
	List<MemberActTrx> findLN040FailedList(MemberActTrx memberActTrx);
	

	/***
	 * APP获取 可用余额 借款账户 冻结账户
	 * @param actTrx
	 * @return
	 */
	Page<MemberActTrx> findActTrxPage(Page<MemberActTrx> page, MemberActTrx memberActTrx);
	/***
	 * APP获取3个月内放款记录
	 * @param actTrx
	 * @return
	 */
	Page<MemberActTrx> findLoanTrxListPage(MemberActTrx memberActTrx, Integer pageNo, Integer pageSize);
	/***
	 * APP获取3个月内收款记录
	 * @param actTrx
	 * @return
	 */
	Page<MemberActTrx> findCollectionTrxListPage(MemberActTrx memberActTrx, Integer pageNo, Integer pageSize);
	/**
	 * 根据trxCodes查记录
	 * @param memberActTrx
	 * @return
	 */
	List<MemberActTrx> findByTrxCodesList(MemberActTrx memberActTrx);
	/**
	 * 根据trxCodes查总数
	 * @param memberActTrx
	 * @return
	 */
	MemberActTrx countTotalByTrxCodes(MemberActTrx memberActTrx);

	/**
	 * 除了待收待还的流水
	 * @param memberActTrx
	 * @return
	 */
	List<MemberActTrx> findListExceptToPaidAndDueIn(MemberActTrx memberActTrx);


}