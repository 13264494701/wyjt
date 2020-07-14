package com.jxf.mem.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.mem.dao.MemberResetPayPwdDao;
import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberResetPayPwd;
import com.jxf.mem.service.MemberResetPayPwdService;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
import com.jxf.svc.utils.DateUtils;
/**
 * 修改支付密码ServiceImpl
 * @author XIAORONGDIAN
 * @version 2018-11-08
 */
@Service("memberResetPayPwdService")
@Transactional(readOnly = true)
public class MemberResetPayPwdServiceImpl extends CrudServiceImpl<MemberResetPayPwdDao, MemberResetPayPwd> implements MemberResetPayPwdService{

	@Autowired
	private MemberResetPayPwdDao memberResetPayPwdDao;
	
	public MemberResetPayPwd get(Long id) {
		return super.get(id);
	}
	
	public List<MemberResetPayPwd> findList(MemberResetPayPwd memberResetPayPwd) {
		return super.findList(memberResetPayPwd);
	}
	
	public Page<MemberResetPayPwd> findPage(Page<MemberResetPayPwd> page, MemberResetPayPwd memberResetPayPwd) {
		return super.findPage(page, memberResetPayPwd);
	}
	
	@Transactional(readOnly = false)
	public void save(MemberResetPayPwd memberResetPayPwd) {
		super.save(memberResetPayPwd);
	}
	
	@Transactional(readOnly = false)
	public void delete(MemberResetPayPwd memberResetPayPwd) {
		super.delete(memberResetPayPwd);
	}

	@Override
	public int getResetPayPwdCountEveryday(Member member) {
		
		String beginDate = DateUtils.getDate()+" 00:00:00";
		String endDate = DateUtils.getDate()+" 23:59:59";
		
		return memberResetPayPwdDao.getResetPayPwdCountEveryday(member,beginDate,endDate);
	}

	@Override
	public int getResetPayPwdCountEveryMonth(Member member) {
		
		String beginDate = DateUtils.getMonthStart()+" 00:00:00";
		String endDate = DateUtils.getMonthEnd()+" 23:59:59";
		
		return memberResetPayPwdDao.getResetPayPwdCountEveryMonth(member,beginDate,endDate);
	}
	
	
	
	
	
}