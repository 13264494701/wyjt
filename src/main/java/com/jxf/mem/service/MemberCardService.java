package com.jxf.mem.service;

import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberCard;
import com.jxf.nfs.entity.NfsBankProtocol;
import com.jxf.svc.model.HandleRsp;
import com.jxf.svc.sys.crud.service.CrudService;


/**
 * 银行卡Service
 * @author wo
 * @version 2018-09-29
 */
public interface MemberCardService extends CrudService<MemberCard> {

	
	MemberCard getCardByMember(Member member);
	
	void changeCard(String cardNo,Member member,NfsBankProtocol protocol);
	
	int getChangeCardCount(Member member);
	
	int getChangeCardCountLast2Week(Member member);
	
	String hideCardNo(String cardNo);

	MemberCard getCardByMemberId(Long memberId);
	
	void unBindBankCard(Member member);
	
	
	HandleRsp checkCard4Factors(String cardNo, String name, String idNo, String phoneNo);
}