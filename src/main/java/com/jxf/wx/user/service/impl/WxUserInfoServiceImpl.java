package com.jxf.wx.user.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.jxf.mem.dao.MemberDao;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.security.Principal;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
import com.jxf.wx.user.entity.WxUserInfo;
import com.jxf.wx.user.dao.WxUserInfoDao;
import com.jxf.wx.user.service.WxUserInfoService;
/**
 * 微信用户信息ServiceImpl
 * @author gaobo
 * @version 2018-10-16
 */
@Service("wxUserInfoService")
@Transactional(readOnly = true)
public class WxUserInfoServiceImpl extends CrudServiceImpl<WxUserInfoDao, WxUserInfo> implements WxUserInfoService{

	@Autowired
	WxUserInfoDao wxUserInfoDao;
	@Autowired
	private MemberDao memberDao;
	
	public WxUserInfo get(Long id) {
		return super.get(id);
	}
	
	public List<WxUserInfo> findList(WxUserInfo wxUserInfo) {
		return super.findList(wxUserInfo);
	}
	
	public Page<WxUserInfo> findPage(Page<WxUserInfo> page, WxUserInfo wxUserInfo) {
		return super.findPage(page, wxUserInfo);
	}
	
	@Transactional(readOnly = false)
	public void save(WxUserInfo wxUserInfo) {
		super.save(wxUserInfo);
	}
	
	@Transactional(readOnly = false)
	public void delete(WxUserInfo wxUserInfo) {
		super.delete(wxUserInfo);
	}

	@Override
	public WxUserInfo findByOpenId(String openid) {
		
		return wxUserInfoDao.findByOpenId(openid);
	}

	@Override
	public WxUserInfo findByMember(Long memberId,String code) {
		
		return wxUserInfoDao.findByMemberAndCode(memberId,code);
	}
	@Override
	public WxUserInfo findByUnionId(String unionId) {
		return wxUserInfoDao.findByUnionId(unionId);
	}
	@Override
	public WxUserInfo getCurrent() {
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		Principal principal = requestAttributes != null ? (Principal) requestAttributes.getAttribute(WxUserInfo.PRINCIPAL_ATTRIBUTE_NAME, RequestAttributes.SCOPE_SESSION) : null;
		Long id = principal != null ? principal.getId() : null;
        if(id!=null) {
        	WxUserInfo userInfo = wxUserInfoDao.get(id);
        	userInfo.setMember(memberDao.get(userInfo.getMember()));
        	return userInfo;
        }
		return null;

	}


}