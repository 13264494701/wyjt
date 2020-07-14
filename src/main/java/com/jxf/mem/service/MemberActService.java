package com.jxf.mem.service;


import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberAct;
import com.jxf.svc.sys.crud.service.CrudService;
import com.jxf.web.model.ResponseData;

import java.math.BigDecimal;
import java.util.List;


/**
 * 会员账户Service
 * @author zhj
 * @version 2016-05-12
 */
public interface MemberActService extends CrudService<MemberAct> {
	
	/**
	 *  设置会员默认账户
	 * @param shopMemberAct
	 */
	void setDefault(MemberAct memberAct);
	
	/**
	 * 取会员默认账户信息
	 * @param member
	 * @return
	 */
	MemberAct getMemberDefaultAct(Member member);
	/***
	 * 取会员指定账户
	 * @param member
	 * @param subNo
	 * @return
	 */
	MemberAct getMemberAct(Member member,String subNo);
	
	/**
	 * 取会员账户列表
	 * @param member
	 * @return
	 */
	List<MemberAct> getMemberActList(Member member);

	/**
	 * 初始化会员账户
	 * @param member
	 * @return
	 */
	Boolean initMemAct(Member member);
	/**
	 * 修改账户状态
	 * @param actNo
	 * @param actSts
	 * @return
	 */
	int updateActSts(MemberAct memberAct);
	
	
	int updateAct(MemberAct memberAct,BigDecimal trxAmt);
	
	/**
	 * 获取用户可用余额 
	 */
	BigDecimal getAvlBal(Member member);
	/**
	 * 变更账户
	 * @param  
	 * @param bean
	 * @return ResponseData
	 */
//	public ResponseData changeAct(ChangeMemberAct bean);

	/**
	 * 检查是否可以转账
	 * @param member 会员(转出者)
	 * @param friend 好友(接收方)
	 * @param amount 金额
	 * 
	 * 转账限制:
	 * 您的账号已被锁定，请联系客服         支付密码输入错误5次
	 * 您今日的肖像比对认证次数已达上限\n如有疑问请联系在线客服   一天累计失败三次
	 * 您还没有设置支付密码,请去设置支付密码
	 * 转账金额必须是整数
	 * 转账金额必须大于0元
	 * 单笔转账金额不得超过5000
	 * 一天不得超过5W
	 * 一年内转账金额不得超过50万
	 * 余额不足 不能转账
	 * @return 
	 */
	ResponseData checkCanTransfer(Member member, Member friend, BigDecimal amount);

	/**
	 * 获取账户金额(可用+借款)
	 * @param memberId
	 * @return
	 */
	BigDecimal getCurBal(Long memberId);


	/**
	 * 更新缓存
	 * @param subNo  科目
	 * @param member 用户
	 * @param trxAmt 变化量
	 * @param memberAct 账户
	 */
//	void updateRedis(String subNo, Member member, BigDecimal trxAmt,MemberAct memberAct);

}