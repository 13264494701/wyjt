package com.jxf.mem.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.mem.dao.MemberPhonebookDao;
import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberPhonebook;
import com.jxf.mem.service.MemberPhonebookService;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;

/**
 * 手机通讯录ServiceImpl
 * @author wo
 * @version 2019-03-07
 */
@Service("memberPhonebookService")
@Transactional(readOnly = true)
public class MemberPhonebookServiceImpl extends CrudServiceImpl<MemberPhonebookDao, MemberPhonebook> implements MemberPhonebookService{

	public MemberPhonebook get(Long id) {
		return super.get(id);
	}
	
	public List<MemberPhonebook> findList(MemberPhonebook memberPhonebook) {
		return super.findList(memberPhonebook);
	}
	
	public Page<MemberPhonebook> findPage(Page<MemberPhonebook> page, MemberPhonebook memberPhonebook) {
		return super.findPage(page, memberPhonebook);
	}
	
	@Transactional(readOnly = false)
	public void save(MemberPhonebook memberPhonebook) {
		super.save(memberPhonebook);
	}
	
	@Transactional(readOnly = false)
	public void delete(MemberPhonebook memberPhonebook) {
		super.delete(memberPhonebook);
	}

	@Override
	public MemberPhonebook getByMember(Member member) {
	
		MemberPhonebook memberPhonebook = new MemberPhonebook();
		memberPhonebook.setMember(member);
		List<MemberPhonebook> list = findList(memberPhonebook);
		if(list!=null&&list.size()>0) {
			return list.get(0);
		}
		return null;
	}
	
}