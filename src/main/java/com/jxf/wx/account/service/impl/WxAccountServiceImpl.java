package com.jxf.wx.account.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
import com.jxf.wx.account.entity.WxAccount;
import com.jxf.wx.account.dao.WxAccountDao;
import com.jxf.wx.account.service.WxAccountService;
/**
 * 微信公众号账号ServiceImpl
 * @author gaobo
 * @version 2018-10-16
 */
@Service("wxAccountService")
@Transactional(readOnly = true)
public class WxAccountServiceImpl extends CrudServiceImpl<WxAccountDao, WxAccount> implements WxAccountService{

	@Autowired
	WxAccountDao wxAccountDao;
	
	public WxAccount get(Long id) {
		return super.get(id);
	}
	
	public List<WxAccount> findList(WxAccount wxAccount) {
		return super.findList(wxAccount);
	}
	
	public Page<WxAccount> findPage(Page<WxAccount> page, WxAccount wxAccount) {
		return super.findPage(page, wxAccount);
	}
	
	@Transactional(readOnly = false)
	public void save(WxAccount wxAccount) {
		super.save(wxAccount);
	}
	
	@Transactional(readOnly = false)
	public void delete(WxAccount wxAccount) {
		super.delete(wxAccount);
	}

	@Override
	public WxAccount findByCode(String string) {
		
		return wxAccountDao.findByCode(string);
	}
	
}