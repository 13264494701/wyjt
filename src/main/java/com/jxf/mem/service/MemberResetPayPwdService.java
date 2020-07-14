package com.jxf.mem.service;

import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberResetPayPwd;
import com.jxf.svc.sys.crud.service.CrudService;

/**
 * 修改支付密码Service
 * @author XIAORONGDIAN
 * @version 2018-11-08
 */
public interface MemberResetPayPwdService extends CrudService<MemberResetPayPwd> {

	/**
	 * 获取每天重置支付密码次数
	 * @param member
	 * @return
	 */
	int getResetPayPwdCountEveryday(Member member);

	/**
	 * 获取每月重置支付密码次数
	 * @param member
	 * @return
	 */
	int getResetPayPwdCountEveryMonth(Member member);

	
	
}