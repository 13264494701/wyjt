package com.jxf.loan.service;

import java.util.List;
import java.util.Map;

import com.jxf.loan.entity.NfsCrAuction;
import com.jxf.loan.entity.NfsLoanRecord;
import com.jxf.mem.entity.Member;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.CrudService;
import com.jxf.web.model.ResponseData;
import com.jxf.web.model.wyjt.app.auction.LoanCrAuctionListRequestParam;
import com.jxf.web.model.wyjt.app.auction.LoanCrAuctionListResponseResult;
import com.jxf.web.model.wyjt.app.auction.MinePurchaseListRequestParam;
import com.jxf.web.model.wyjt.app.auction.MineTransferListRequestParam;
import com.jxf.web.model.wyjt.app.auction.PayAuctionRequestParam;
import com.jxf.web.model.wyjt.app.auction.PurchaseDetailResponseResult;
import com.jxf.web.model.wyjt.app.auction.ReleaseAuctionResponseResult;


/**
 * 债权买卖Service
 * @author wo
 * @version 2018-12-25
 */
public interface NfsCrAuctionService extends CrudService<NfsCrAuction> {

	Page<NfsCrAuction> findPage(NfsCrAuction crAuction, Integer pageNo, Integer pageSize);

	/**
	 * 债权转让支付
	 * @param crAuction  债权拍卖记录
	 * @param loanRecord 借条
	 * @return
	 */
	int payCrAuction(NfsCrAuction crAuction,NfsLoanRecord loanRecord);

	Integer checkStatus(NfsLoanRecord nfsLoanRecord);

	ResponseData applyCrAuction(NfsLoanRecord nfsLoanRecord, String price, ReleaseAuctionResponseResult result);
	/**
	 *     债权购买操作合法检查
	 * @param loanRecord
	 * @param crAuction
	 * @return
	 */
	Map<String, String> buyCrOperationLegalCheck(NfsLoanRecord loanRecord,NfsCrAuction crAuction);
	
	ResponseData findCrAuctionPage(Member member, LoanCrAuctionListRequestParam reqData, LoanCrAuctionListResponseResult result);

	ResponseData findPurchaseList(Member member, MinePurchaseListRequestParam reqData,
			LoanCrAuctionListResponseResult result);

	ResponseData findAuctionList(Member member, MineTransferListRequestParam reqData,
			LoanCrAuctionListResponseResult result);

	ResponseData releaseAuction(Member member, PayAuctionRequestParam reqData, ReleaseAuctionResponseResult result);

	ReleaseAuctionResponseResult getCrAuctionDetail(String auctionId);
	/**
	 * 购买成功后发送消息短信推送
	 * @param crAuction 债权记录
	 * @param loanRecord 借条
	 */
	void sendMessageAfterPaySucceed(NfsCrAuction crAuction,NfsLoanRecord loanRecord);
	
	PurchaseDetailResponseResult getCrPurchaseDetail(String auctionId);

	Page<NfsCrAuction> findAuditPage(Page<NfsCrAuction> page, NfsCrAuction nfsCrAuction);

	Page<NfsCrAuction> findAuditedPage(Page<NfsCrAuction> page, NfsCrAuction nfsCrAuction);

	List<NfsCrAuction> findOvertimeAuctionList();

	List<NfsCrAuction> findOverOneDayList();
	
	
	Member getCrBuyer(NfsLoanRecord loanRecord);

	NfsCrAuction getSuccessedAuctionByLoan(NfsLoanRecord record);
	
}