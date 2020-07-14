package com.jxf.mem.dao;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jxf.mem.entity.MemberActTrx;
import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;

/**
 * 账户流水DAO接口
 * @author JINXINFU
 * @version 2016-10-08
 */
@MyBatisDao
public interface MemberActTrxDao extends CrudDao<MemberActTrx> {
	/***
	 * 
	 * 函数功能说明 
	 * Administrator  2018年1月14日 
	 * 修改者名字 修改日期 
	 * 修改内容 
	 * @参数： @param memberNo
	 * @参数： @param trxType
	 * @参数： @return     
	 * @return BigDecimal    
	 * @throws
	 */
	BigDecimal sumAmount(MemberActTrx memberActTrx);

	/**
	 * 根据group 获取总支出和总收入
	 * @param memberActTrx
	 * @return
	 */
	MemberActTrx countTotalAccountByGroup(MemberActTrx memberActTrx);
	
	/**
	 * 根据用户id查询用户的资金明细
	 * @param memberId
	 * @return
	 */
	List<MemberActTrx> getTrxByMemberAndSubNo(MemberActTrx memberActTrx);
	
	List<MemberActTrx> getTrxByMemberAndLoan(MemberActTrx memberActTrx);
	
	List<MemberActTrx> getExtraTrx(MemberActTrx memberActTrx);
	/**
	 * 根据业务Id查询资金明细
	 * @param businessId
	 * @return
	 */
	List<MemberActTrx> findListByOrgId(@Param("orgId")Long orgId);

	/**
	 * 获取全部账户流水给APP
	 * @param memberActTrx
	 * @return
	 */
	List<MemberActTrx> findAllForAppList(MemberActTrx memberActTrx);
	
	/**
	 * 处理主动放款失败
	 * @param memberActTrx
	 * @return
	 */
	List<MemberActTrx> findLN040FailedList(MemberActTrx memberActTrx);
	
	/**
	 * 	APP获取 可用余额 借款账户 冻结账户
	 * @param memberActTrx
	 * @return
	 */
	List<MemberActTrx> findActTrxList(MemberActTrx memberActTrx);
	/**
	 * 	APP获取 3个月内放款
	 * @param memberActTrx
	 * @return
	 */
	List<MemberActTrx> findLoanTrxListPage(MemberActTrx memberActTrx);
	/**
	 * 	APP获取 3个月内收款
	 * @param memberActTrx
	 * @return
	 */
	List<MemberActTrx> findCollectionTrxListPage(MemberActTrx memberActTrx);
	/**
	 * 	根据trxCodes查记录
	 * @param memberActTrx 
	 * @return
	 */
	List<MemberActTrx> findByTrxCodesList(MemberActTrx memberActTrx);
	/**
	 * 	根据trxCodes查总数
	 * @param memberActTrx 
	 * @return
	 */
	MemberActTrx countTotalByTrxCodes(MemberActTrx memberActTrx);

	/**
	 * 除了待收待还的流水
	 */
	List<MemberActTrx> findListExceptToPaidAndDueIn(MemberActTrx memberActTrx);

}