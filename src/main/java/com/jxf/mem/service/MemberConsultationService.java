package com.jxf.mem.service;


import java.util.List;

import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberConsultation;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.CrudService;

/**
 * 商品咨询Service
 * @author huojiayuan
 * @version 2016-06-09
 */
public interface MemberConsultationService extends CrudService<MemberConsultation> {
    /***
     * 
     * @param consultation
     * @return
     */
	List<MemberConsultation> findReplyConsultationList(MemberConsultation consultation);
	
	/**
	 * 查询会员的咨询消息
	 * @param member
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	Page<MemberConsultation> findConsultationPage(Member member,Boolean isShow, Integer pageNo, Integer pageSize);
	
	/**
	 * 查找咨询数量
	 * 
	 * @param member
	 *            会员
	 * @param isShow
	 *            是否显示
	 * @return 咨询数量，不包含咨询回复
	 */
	Long count(Member member, Boolean isShow);
	
}