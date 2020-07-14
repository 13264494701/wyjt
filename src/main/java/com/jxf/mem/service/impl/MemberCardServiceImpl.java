package com.jxf.mem.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.mem.dao.MemberCardDao;
import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberCard;
import com.jxf.mem.entity.MemberVerified;
import com.jxf.mem.service.MemberCardService;
import com.jxf.mem.service.MemberService;
import com.jxf.mem.utils.VerifiedUtils;
import com.jxf.nfs.dao.NfsBankProtocolDao;
import com.jxf.nfs.entity.NfsBankBin;
import com.jxf.nfs.entity.NfsBankProtocol;
import com.jxf.nfs.service.NfsBankBinService;
import com.jxf.partner.udcredit.UdcreditUtils;
import com.jxf.svc.model.HandleRsp;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
import com.jxf.svc.utils.CalendarUtil;
import com.jxf.svc.utils.DateUtils;

/**
 * 银行卡ServiceImpl
 * @author wo
 * @version 2018-09-29
 */
@Service("memberCardService")
@Transactional(readOnly = true)
public class MemberCardServiceImpl extends CrudServiceImpl<MemberCardDao, MemberCard> implements MemberCardService{

	@Autowired
	private MemberCardDao memberCardDao;
	@Autowired
	private NfsBankBinService bankBinService;
	@Autowired
	private MemberVerifiedServiceImpl memberVerifiedServiceImpl;
	@Autowired
	private NfsBankProtocolDao nfsBankProtocolDao;
	@Autowired
	private MemberService memberservice;
	
	public MemberCard get(Long id) {
		return super.get(id);
	}
	
	public MemberCard getCardByMemberId(Long memberId) {
		List<MemberCard> cardByMember = memberCardDao.getCardByMemberId(memberId);
		if(cardByMember.size()>0) {
			return cardByMember.get(0);
		}
		return null;
	}
	
	public List<MemberCard> findList(MemberCard memberCard) {
		return super.findList(memberCard);
	}
	
	public Page<MemberCard> findPage(Page<MemberCard> page, MemberCard memberCard) {
		return super.findPage(page, memberCard);
	}
	
	@Transactional(readOnly = false)
	public void save(MemberCard memberCard) {
		
		if(memberCard.getIsNewRecord()) {
			NfsBankBin byCardBin = bankBinService.getByCardNo(memberCard.getCardNo());
			memberCard.setBank(byCardBin.getBank());
			memberCard.setCardType(byCardBin.getCardType());
			memberCard.preInsert();
			memberCardDao.insert(memberCard);
		}else {
			memberCard.preUpdate();
			memberCardDao.update(memberCard);
		}

	}
	
	@Transactional(readOnly = false)
	public void delete(MemberCard memberCard) {
		super.delete(memberCard);
	}

	@Override
	public MemberCard getCardByMember(Member member) {

		MemberCard card = new MemberCard();
		card.setMember(member);
		card.setStatus(MemberCard.Status.binded);
		List<MemberCard> cardList = findList(card);
		return cardList!=null?(cardList.size()>0?cardList.get(0):null):null;
	}
	

	/**
	 * 	更换银行卡 变更银行卡表 认证表 如果是app 没有协议支付 无需解除协议 入参 protocol 传null 
	 */
	@Override
	@Transactional(readOnly = false)
	public void changeCard(String cardNo,Member member,NfsBankProtocol protocol) {

		//更新card表
		NfsBankBin bankBin = bankBinService.getByCardNo(cardNo);

		MemberCard card = getCardByMember(member);
		card.setStatus(MemberCard.Status.unbind);
		delete(card);
		
		MemberCard newCard = new MemberCard();
		newCard.setMember(member);
		newCard.setCardNo(cardNo);
		newCard.setBank(bankBin.getBank());
		newCard.setCardType(bankBin.getCardType());
		newCard.setStatus(MemberCard.Status.binded);
		save(newCard);
		
		//记录认证表
		MemberVerified memberVerified = new MemberVerified();
		memberVerified.setMember(member);
		memberVerified.setIdNo(member.getIdNo());
		memberVerified.setRealName(member.getName());
		memberVerified.setPhoneNo(member.getUsername());
		memberVerified.setCardNo(cardNo);
		memberVerified.setEmail(member.getEmail());
		memberVerified.setStatus(MemberVerified.Status.verified);
		memberVerifiedServiceImpl.save(memberVerified);
		if(protocol != null) {
			//更新协议表
			nfsBankProtocolDao.delete(protocol);
		}
	}

	@Override
	public int getChangeCardCount(Member member) {
		
		String beginDate = DateUtils.getYear()+"-01-01 00:00:00";
		String endDate = DateUtils.getYear()+"-12-31 23:59:59";
		
		return memberCardDao.getChangeCardCount(member,beginDate,endDate);
	}
	
	/**
	 * 隐藏银行卡号
	 * @param cardNo
	 * @return
	 */
	public String hideCardNo(String cardNo) {
		
		if (cardNo.length() < 10) {
			cardNo = "**** ****" + cardNo;
		} else {
			cardNo = cardNo.substring(0, 4) + "**** ****" + cardNo.substring(cardNo.length() - 4);
		}
		return cardNo;
	}
	/**
	 * 解绑银行卡
	 */
	@Override
	@Transactional(readOnly = false)
	public void unBindBankCard(Member member) {
		
		MemberCard card = getCardByMember(member);
		card.setStatus(MemberCard.Status.unbind);
		save(card);
		
		member.setVerifiedList(VerifiedUtils.removeVerified(member.getVerifiedList(), 3));
		memberservice.save(member);
	}

	@Override
	public int getChangeCardCountLast2Week(Member member) {
		String endDate = DateUtils.getDateStr(new Date(), "yyyy-MM-dd HH:mm:ss");
		String beginDate = CalendarUtil.addDay(endDate, -14);
		return memberCardDao.getChangeCardCount(member,beginDate,endDate);
	}

	@Override
	public HandleRsp checkCard4Factors(String cardNo, String name, String idNo, String phoneNo) {
	
		return UdcreditUtils.checkCard4Factors(cardNo, name, idNo, phoneNo);
	}
	
}