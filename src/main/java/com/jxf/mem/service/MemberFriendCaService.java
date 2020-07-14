package com.jxf.mem.service;

import java.util.Date;

import com.jxf.loan.entity.NfsLoanArbitration;
import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberFriendCa;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.CrudService;
import com.jxf.web.model.wyjt.app.member.CreditaInformationResponseResult;


/**
 * 信用报告申请Service
 * @author wo
 * @version 2018-12-17
 */
public interface MemberFriendCaService extends CrudService<MemberFriendCa> {
	
  /**
   * 查询信用档案记录
   * @param memberFriendCa
   * @param pageNo
   * @param pageSize
   * @return
   */
	Page<MemberFriendCa> findCredictBaoGaoRecode(MemberFriendCa memberFriendCa ,Integer pageNo,Integer pageSize);

	/**
	 * 
	 * 收到拒绝与同意接口
	 * @param member
	 * @param valId
	 * @return
	 */
	CreditaInformationResponseResult getConsentOrRefusalCredita(Member member,String valId);
	
	/**
	 * 收到请求档案消息
	 * @param member
	 * @param valId
	 * @return
	 */
	CreditaInformationResponseResult getCreditarchives(Member member,String valId);
	
	
}