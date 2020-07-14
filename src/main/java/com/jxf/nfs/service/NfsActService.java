package com.jxf.nfs.service;

import java.math.BigDecimal;

import com.jxf.mem.entity.Member;
import com.jxf.ufang.entity.UfangBrn;
import com.jxf.ufang.entity.UfangUser;




/**
 * 账户Service
 * @author jinxinfu
 * @version 2019-12-30
 */
public interface NfsActService{


	/****
	 * @see   会员账户 
	 * @warn 调用此方法必须对返回值为-1时进行事务回滚处理
	 * @param trxCode
	 * @param amount
	 * @param member
	 * @return
	 */
	public int updateAct(String trxCode,BigDecimal amount, Member member,Long orgId);
	
	
	/****
	 * @see   放款人账户和借款人账户
	 * @warn 调用此方法必须对返回值为-1时进行事务回滚处理
	 * @param trxCode
	 * @param amount
	 * @param payer 付款人
	 * @param payee 收款人
	 * @return
	 */
	public int updateAct(String trxCode,BigDecimal amount, Member payer, Member payee,Long orgId);
	
	
	/****
	 * @see   优放公司账户与平台账户
	 * @warn 调用此方法必须对返回值为-1时进行事务回滚处理
	 * @param trxCode
	 * @param amount
	 * @param ufangBrn
	 * @return
	 */
	public int updateAct(String trxCode,BigDecimal amount, UfangBrn ufangBrn,Long orgId);
	
	/****
	 * @see   优放员工账户与平台账户
	 * @warn 调用此方法必须对返回值为-1时进行事务回滚处理
	 * @param trxCode
	 * @param amount
	 * @param ufangBrn
	 * @return
	 */
	public int updateAct(String trxCode,BigDecimal amount, UfangUser ufangUser,Long orgId);
	/****
	 * @see   优放公司账户与优放用户账户
	 * @warn 调用此方法必须对返回值为-1时进行事务回滚处理
	 * @param trxCode
	 * @param amount
	 * @param ufangBrn
	 * @return
	 */
	public int updateAct(String trxCode,BigDecimal amount, UfangBrn ufangBrn,UfangUser ufangUser,Long orgId);
	
}