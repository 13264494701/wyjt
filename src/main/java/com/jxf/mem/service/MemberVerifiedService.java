package com.jxf.mem.service;

import java.util.Map;

import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberVerified;
import com.jxf.svc.sys.crud.service.CrudService;
import com.jxf.web.model.ResponseData;

/**
 * 实名认证Service
 * @author wo
 * @version 2018-09-28
 */
public interface MemberVerifiedService extends CrudService<MemberVerified> {
	
	/**
	 * 小程序验证四要素
	 * @param memberVerified
	 * @param memberService 
	 * @return boolean
	 */
	public boolean verifyInputInformation(MemberVerified memberVerified, Map<String, Object> data);

	/**
	 * 根据会员ID查已通过认证
	 * @param memberId
	 * @return
	 */
	MemberVerified getVerifiedByMemberId(Long memberId);
	
	/**
	 * app 验证四要素
	 * @param cardNo
	 * @param phoneNo
	 * @param name
	 * @param idNo
	 * @param memberVerified 
	 * @return 
	 */
	public ResponseData verifyCardInformation(String cardNo, String phoneNo, String name, String idNo, MemberVerified memberVerified);

	public ResponseData verifyChangeCardInformation(String cardNo, String phoneNo, String name, String idNo, Member member);

	
	
}