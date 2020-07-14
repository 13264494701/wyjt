package com.jxf.mem.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.mem.dao.MemberVerifiedDao;
import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberCard;
import com.jxf.mem.entity.MemberVerified;
import com.jxf.mem.service.MemberCardService;
import com.jxf.mem.service.MemberService;
import com.jxf.mem.service.MemberVerifiedService;
import com.jxf.mem.utils.VerifiedUtils;
import com.jxf.nfs.entity.NfsBankProtocol;
import com.jxf.nfs.service.NfsBankProtocolService;
import com.jxf.pay.service.FuyouPayService;
import com.jxf.svc.cache.RedisUtils;
import com.jxf.svc.model.HandleRsp;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
import com.jxf.svc.utils.Exceptions;
import com.jxf.web.model.ResponseData;
import com.jxf.web.model.wyjt.app.card.BindBankCardResponseResult;


/**
 * 实名认证ServiceImpl
 * @author wo
 * @version 2018-09-28
 */
@Service("memberVerifiedService")
@Transactional(readOnly = false)
public class MemberVerifiedServiceImpl extends CrudServiceImpl<MemberVerifiedDao, MemberVerified> implements MemberVerifiedService{

	private static final Logger logger = LoggerFactory.getLogger(MemberVerifiedServiceImpl.class);
	
	@Autowired
	private MemberService memberService;
	@Autowired
	private MemberCardService memberCardService;
	@Autowired
	private MemberVerifiedDao memberVerifiedDao;
	@Autowired
	private FuyouPayService fuyouPayService;
	@Autowired
	private NfsBankProtocolService nfsBankProtocolService;

	
	public MemberVerified get(Long id) {
		return super.get(id);
	}
	
	public List<MemberVerified> findList(MemberVerified memberVerified) {
		return super.findList(memberVerified);
	}
	
	public Page<MemberVerified> findPage(Page<MemberVerified> page, MemberVerified memberVerified) {
		return super.findPage(page, memberVerified);
	}
	
	@Transactional(readOnly = false)
	public void save(MemberVerified memberVerified) {
		super.save(memberVerified);
	}
	
	@Transactional(readOnly = false)
	public void delete(MemberVerified memberVerified) {
		super.delete(memberVerified);
	}
	
	@Override
	public boolean verifyInputInformation(MemberVerified memberVerified,Map<String, Object> data) {
		
		String cardNo = memberVerified.getCardNo();
		String realName = memberVerified.getRealName(); 
		String idNo = memberVerified.getIdNo();
		String phoneNo = memberVerified.getPhoneNo();
		try {

			HandleRsp rsp = memberCardService.checkCard4Factors(cardNo, realName,  idNo, phoneNo);
			if(rsp.getStatus()) {
				data.put("resultCode", "00");
				data.put("resultMsg", "实名认证成功");
				return true; 
			}else {
				data.put("resultCode", "30");
				data.put("resultMsg", rsp.getMessage());
				return false;
			}
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(e));
			data.put("resultCode", "30");
			data.put("resultMsg", "实名认证失败");
			return false;
		}
	}
	
	@Override
	public MemberVerified getVerifiedByMemberId(Long memberId) {
		return memberVerifiedDao.getVerifiedByMemberId(memberId);
	}
	/**
	 * app 绑卡验证四要素 并更新认证信息 绑定银行卡
	 */
	@Override
	public ResponseData verifyCardInformation(String cardNo, String phoneNo, String name, String idNo, MemberVerified memberVerified) {
		
			BindBankCardResponseResult result = new BindBankCardResponseResult();

			HandleRsp rsp = memberCardService.checkCard4Factors(cardNo, name, idNo, phoneNo);		
			if(rsp.getStatus()) {
				/**更新认证信息*/
				memberVerified.setStatus(MemberVerified.Status.verified);
				save(memberVerified);
				
				/**更新会员认证状态*/
				Member member = memberVerified.getMember();
				member.setVerifiedList(VerifiedUtils.addVerified(member.getVerifiedList(), 3));
				memberService.save(member);
				RedisUtils.put("memberInfo"+member.getId(), "bankCardStatus", 1);
				
				/**绑定银行卡信息*/
				MemberCard card = new MemberCard();
				card.setMember(member);
				card.setCardNo(cardNo);
				card.setStatus(MemberCard.Status.binded);
				memberCardService.save(card);
				result.setResultCode(0);
				result.setResultMessage("绑卡成功");
				return ResponseData.success("绑卡成功",result);
			}else {
				return ResponseData.error(rsp.getMessage());
			}
			
	}

	/**
	 * 	更换银行卡前 验证四要素 并解除协议 更新银行卡信息
	 */
	@Override
	public ResponseData verifyChangeCardInformation(String cardNo, String phoneNo, String name, String idNo, Member member) {
		
		BindBankCardResponseResult result = new BindBankCardResponseResult();
		
		HandleRsp rsp = memberCardService.checkCard4Factors(cardNo, name,  idNo, phoneNo);
		if(!rsp.getStatus()) {
			return ResponseData.error(rsp.getMessage());
		}
		
		/**解绑协议*/
		NfsBankProtocol nfsBankProtocol = nfsBankProtocolService.getByMember(member.getId());
		if(null != nfsBankProtocol) {
			Map<String, Object> map = fuyouPayService.unBind(nfsBankProtocol);
			if(!StringUtils.equals((String)map.get("sucess"), "0000")) {
				logger.error((String)map.get("reson"));
				return ResponseData.error((String)map.get("reson"));
			}
		}
		
		member.setUsername(phoneNo);
		memberCardService.changeCard(cardNo, member, nfsBankProtocol);
		result.setResultCode(0);
		result.setResultMessage("更换银行卡成功");
		return ResponseData.success("更换银行卡成功",result);
	}
	
}