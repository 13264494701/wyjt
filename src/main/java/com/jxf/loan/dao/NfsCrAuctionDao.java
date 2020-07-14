package com.jxf.loan.dao;

import com.jxf.svc.sys.crud.dao.CrudDao;

import java.util.List;

import com.jxf.loan.entity.NfsCrAuction;
import com.jxf.svc.annotation.MyBatisDao;


/**
 * 债权买卖DAO接口
 * @author wo
 * @version 2018-12-25
 */
@MyBatisDao
public interface NfsCrAuctionDao extends CrudDao<NfsCrAuction> {
	/***
	 * 获取可购入借条列表
	 * @param nfsCrAuction
	 * @return
	 */
	List<NfsCrAuction> findCrAuctionList(NfsCrAuction nfsCrAuction);
	/***
	 * 获取待审核列表
	 * @param nfsCrAuction
	 * @return
	 */
	List<NfsCrAuction> findAuditList(NfsCrAuction nfsCrAuction);
	/***
	 * 获取已审核列表
	 * @param nfsCrAuction
	 * @return
	 */
	List<NfsCrAuction> findAuditedList(NfsCrAuction nfsCrAuction);
	/***
	 * 获取72小时逾期列表
	 * @return
	 */
	List<NfsCrAuction> findOvertimeAuctionList();
	/***
	 * 超过24小时自动通过审核的借条
	 * @return
	 */
	List<NfsCrAuction> findOverOneDayList();
	
	NfsCrAuction getCrBuyerByLoan(NfsCrAuction nfsCrAuction);

	int getCrBuyTimes(NfsCrAuction crAuction);
	
}