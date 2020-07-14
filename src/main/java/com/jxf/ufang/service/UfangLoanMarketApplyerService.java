package com.jxf.ufang.service;

import com.jxf.mem.entity.Member;
import com.jxf.svc.sys.crud.service.CrudService;
import com.jxf.ufang.entity.UfangLoanMarketApplyer;

/**
 * 优放贷申请人Service
 * @author suHuimin
 * @version 2019-03-27
 */
public interface UfangLoanMarketApplyerService extends CrudService<UfangLoanMarketApplyer> {
	/**
	 * 根据注册会员的信息保存优放贷申请人信息
	 * @param member
	 * @return
	 */
	UfangLoanMarketApplyer saveApplyerByMemberInfo(UfangLoanMarketApplyer applyer,Member member);
	/**
	 * @description 获取指定条件的申请人数量
	 * @param applyer
	 * @return
	 */
	int	getCountByCondition(UfangLoanMarketApplyer applyer);
	
	/**
	 * 获取注册会员的认证状态
	 * @param applyer
	 * @param member
	 * @return
	 */
	UfangLoanMarketApplyer getApplyerAuthStatus(UfangLoanMarketApplyer applyer,Member member);
	
	
	int selectWeekUpdateCount(Long marketId,String phoneNo);
	  
	  /**
		 * 根据手机号和贷超获取申请人
		 * @param applyer
		 * @return
		 */
	UfangLoanMarketApplyer getByPhoneNoAndMarketId(UfangLoanMarketApplyer applyer);
	  
}