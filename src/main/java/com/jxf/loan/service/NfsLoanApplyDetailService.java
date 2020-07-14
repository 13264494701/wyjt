package com.jxf.loan.service;

import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.CrudService;
import com.jxf.web.model.ResponseData;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.jxf.loan.entity.NfsLoanApply;
import com.jxf.loan.entity.NfsLoanApplyDetail;
import com.jxf.mem.entity.Member;
import com.jxf.payment.entity.Payment;

/**
 * 借贷对象Service
 * @author wo
 * @version 2018-10-18
 */
public interface NfsLoanApplyDetailService extends CrudService<NfsLoanApplyDetail> {

	Page<NfsLoanApplyDetail> findPage(NfsLoanApplyDetail nfsLoanApplyDetail,
			Integer pageNo, Integer pageSize);

	NfsLoanApplyDetail getByApplyIdAndMemberId(Long applyId,Long memberId);

	/***
	 * 取得申请进度
	 * @param detail
	 * @param apply
	 * @param member 当前用户
	 * @return
	 */
	String getDetailProgress(NfsLoanApplyDetail detail, NfsLoanApply apply, Member member);

	/**
	 * 借款人拒绝主动放款
	 * @param detailId
	 * @param member 
	 */
	int refusePayment(String detailId, Member member);

	/**
	 * 借款人取消借款
	 * @param applyDetail
	 * @param member
	 */
	int cancelBorrow(NfsLoanApplyDetail applyDetail, Member member);

	/**
	 * 申请修改利息
	 * @param applyDetail
	 * @return
	 */
	int applyChangeInterest(Member member,Long detailId,String interest);
	
	/**
	 * 待放款：残忍拒绝
	 * @param applyDetail
	 * @return
	 */
	int rejectApply(Member member,Long detailId);
	
	/**
	 *   同意/拒绝修改利息
	 * @param member
	 * @param detailId
	 * @param isAgree
	 * @return
	 */
	int replyChangeInterest(Member member,NfsLoanApply apply, NfsLoanApplyDetail detail,int isAgree,BigDecimal interest);
	
	/**
	 *   上传审核视频
	 * @param member
	 * @param detailId
	 * @param verifyVideoUrl
	 * @return
	 */
	int uploadVerifyVideo(Member member,Long detailId,String verifyVideoUrl);
	
	/**
	 *   审核视频未通过
	 * @param member
	 * @param detail
	 * @return
	 */
	int verifyVideoNotPass(Member member,NfsLoanApplyDetail detail,Member loanee);

	/**
	 * 申请超时变更
	 * 
     * 1 借款人申请部分还款 有账户变更
     * 2 放款人主动放款的/放款人放款时要求录制视频的 有账户变更
     * 
	 * @param detail
	 */
	void changeStatusToExpired(NfsLoanApplyDetail detail);
	/**
	 * 查找作为放款人的待确认的借条申请
	 * @param paramMap
	 * @return
	 */
	Map<String, Long> getLendingLoanAndAmount(@Param("paramMap")Map<String, Object> paramMap);
	
	/**
	 * 放款人拒绝借款人的补借条申请
	 * @param applyDetail
	 * @return
	 */
	int loanerRefuseLoanApply(NfsLoanApplyDetail applyDetail);
	/**
	 * 借款人取消申请补借条
	 * @param detail
	 * @return
	 */
	int loaneeCancelLoanApply(NfsLoanApplyDetail detail);
	
	/**
	 * 借款人微信支付补借条费用回调处理
	 * @param applyDetail
	 */
	boolean loaneePayForApplyByWx(Payment payment);
	
	/**
	 * 借款人余额支付补借条服务费
	 * @param applyDetail
	 */
	ResponseData loaneePayForApplyByActBal(NfsLoanApplyDetail detail);
	/**
	 * 查询gxt超过72小时的借条
	 * @return
	 */
	List<NfsLoanApplyDetail> findLoanOutTimeList();

	void loanApplyOutTime(NfsLoanApplyDetail detail);
	
	

}