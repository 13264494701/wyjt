package com.jxf.mem.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.mem.dao.MemberFeedbackDao;
import com.jxf.mem.entity.MemberFeedback;
import com.jxf.mem.service.MemberFeedbackService;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;


/**
 * 保存用户反馈意见ServiceImpl
 * @author suhuimin
 * @version 2018-11-01
 */
@Service("memberFeedbackService")
@Transactional(readOnly = true)
public class MemberFeedbackServiceImpl extends CrudServiceImpl<MemberFeedbackDao, MemberFeedback> implements MemberFeedbackService{

	@Override
	public MemberFeedback get(Long id) {
		return super.get(id);
	}
	
	@Override
	public List<MemberFeedback> findList(MemberFeedback memberFeedback) {
		return super.findList(memberFeedback);
	}
	
	@Override
	public Page<MemberFeedback> findPage(Page<MemberFeedback> page, MemberFeedback memberFeedback) {
		return super.findPage(page, memberFeedback);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void save(MemberFeedback memberFeedback) {
		super.save(memberFeedback);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void delete(MemberFeedback memberFeedback) {
		super.delete(memberFeedback);
	}
	
}