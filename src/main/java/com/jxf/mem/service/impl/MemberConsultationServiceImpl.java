package com.jxf.mem.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.mem.dao.MemberConsultationDao;
import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberConsultation;
import com.jxf.mem.service.MemberConsultationService;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
/**
 * 会员咨询ServiceImpl
 * @author huojiayuan
 * @version 2016-06-09
 */
@Service("memberConsultationService")
@Transactional(readOnly = true)
public class MemberConsultationServiceImpl extends CrudServiceImpl<MemberConsultationDao, MemberConsultation> implements MemberConsultationService{
    
	@Autowired
	private MemberConsultationDao consultationDao;
	
	public MemberConsultation get(Long id) {
		return super.get(id);
	}
	
	public List<MemberConsultation> findList(MemberConsultation shopGoodsConsultation) {
		return super.findList(shopGoodsConsultation);
	}
	
	public Page<MemberConsultation> findPage(Page<MemberConsultation> page, MemberConsultation consultation) {
		return super.findPage(page, consultation);
	}
	
	@Transactional(readOnly = false)
	public void save(MemberConsultation consultation) {
		super.save(consultation);
	}
	
	@Transactional(readOnly = false)
	public void delete(MemberConsultation consultation) {
		super.delete(consultation);
	}

	@Override
	public Page<MemberConsultation> findConsultationPage(Member member,Boolean isShow, Integer pageNo, Integer pageSize) {
		
		Page<MemberConsultation> page = new Page<MemberConsultation>(pageNo == null?1:pageNo, pageSize == null?10:pageSize);	
		MemberConsultation consultation = new MemberConsultation();	
		consultation.setPage(page);
		consultation.setMember(member);
		consultation.setIsShow(isShow);
		List<MemberConsultation> consultationList = consultationDao.findList(consultation);
		page.setList(consultationList);
		return page;
	}

	@Override
	public Long count(Member member,Boolean isShow) {
		
		MemberConsultation consultation = new MemberConsultation();	
		consultation.setMember(member);
		consultation.setIsShow(isShow);
		return consultationDao.count(consultation);
	}

	@Override
	public List<MemberConsultation> findReplyConsultationList(MemberConsultation consultation) {
		
		return consultationDao.findReplyConsultationList(consultation);
	}
}