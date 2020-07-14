package com.jxf.loan.service;


import java.util.List;
import java.util.Map;

import com.jxf.loan.entity.NfsLoanApply;
import com.jxf.loan.entity.NfsLoanApplyDetail;
import com.jxf.mem.entity.Member;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.CrudService;
import com.jxf.web.model.ResponseData;


/**
 * 借款申请Service
 * @author wo
 * @version 2018-09-26
 */
public interface NfsLoanApplyService extends CrudService<NfsLoanApply> {

	/***
	 * 函数功能说明              分页获取借款申请列表(单人)
	 * @param page
	 * @param nfsLoanApply
	 * @return
	 */
	Page<NfsLoanApply> findSingleLoanApplyPage(Page<NfsLoanApply> page, NfsLoanApply nfsLoanApply);
	/***
	 * 函数功能说明              分页获取借款申请列表(多人)
	 * @param page
	 * @param nfsLoanApply
	 * @return
	 */
	Page<NfsLoanApply> findMultipleLoanApplyPage(Page<NfsLoanApply> page, NfsLoanApply nfsLoanApply);
	/***
	 * 
	 * 函数功能说明              分页获取借款申请列表
	 * wo  2018年09月26日 
	 * 修改者名字 修改日期 
	 * 修改内容 
	 * @参数： @param loanApply
	 * @参数： @param pageNo
	 * @参数： @param pageSize
	 * @参数： @return     
	 * @return Page<NfsLoanApply>    
	 * @throws
	 */
	Page<NfsLoanApply> findPage(NfsLoanApply loanApply, Integer pageNo, Integer pageSize);
	/***
	 * 函数功能说明              分页获取借款申请列表(单人)给APP展示
	 * @param page
	 * @param nfsLoanApply
	 * @return
	 */
	Page<NfsLoanApply> findSingleLoanApplyListForApp(NfsLoanApply loanApply,
			Integer pageNo, Integer pageSize);
	/***
	 * 函数功能说明              获取借款申请列表(单人)给APP展示
	 * @param page
	 * @param nfsLoanApply
	 * @return
	 */
	List<NfsLoanApply> findSingleLoanApplyListForApp(NfsLoanApply apply);
	
	/***
	 * 检查好友是否可以达成借条
	 * 检查项目:1.好友 是否实名认证 2.好友是否有逾期15天以上借条 3.你是否是他的好友
	 * @param member当前用户
	 * @param friendsId选择的好友 如果是多个用"|"隔开
	 * @return
	 */
	ResponseData checkFriend(Member member, String friendsId);
	/***
	 * 函数功能说明              获取借款申请列表(单人)给APP展示 包括member发起的和member收到的
	 * @param page
	 * @param nfsLoanApply
	 * @return
	 */
	Page<NfsLoanApply> findMemberSingleLoanApplyListForApp (NfsLoanApply loanApply, Integer pageNo, Integer pageSize,String orderBy);
	
	/**
	 * 放款人同意借款申请要求录视频 - 适用于借款人发起的借款申请
	 * @param detail
	 * @return
	 */
	int loanerRequireVideo(NfsLoanApplyDetail detail);
	
	/**
	 * 放款人主动放款
	 * @param detail
	 * @return
	 */
	int payForLoanToFriend(NfsLoanApply apply ,NfsLoanApplyDetail detail,Member loaner);
	
	/**
	 * 仅用来处理没生成detail错误
	 * @param loanApply
	 */
	List<NfsLoanApply>  findNoDetailApply(NfsLoanApply loanApply);
	/**
	 * @description 达成借条前的合法性检查
	 * @param loanApply 借款申请 不能为空
	 * @param detail 借款申请详情  可以为空
	 * @return
	 */
	Map<String, String> preCheckOfCreateLoan(NfsLoanApply loanApply,NfsLoanApplyDetail detail);
	/***
	 *  获取借款申请列表给公信堂展示 包括member发起的和member收到的
	 * @param page
	 * @param nfsLoanApply
	 * @return
	 */
	Page<NfsLoanApply> findLoanApplyListForGxt(NfsLoanApply loanApply);
	
}