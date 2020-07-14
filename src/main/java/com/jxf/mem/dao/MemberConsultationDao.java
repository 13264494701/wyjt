package com.jxf.mem.dao;

import java.util.List;

import com.jxf.mem.entity.MemberConsultation;
import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;

/**
 * 会员咨询DAO接口
 * @author huojiayuan
 * @version 2016-06-09
 */
@MyBatisDao
public interface MemberConsultationDao extends CrudDao<MemberConsultation> {
	
	Long count(MemberConsultation consultation);
	
	List<MemberConsultation> findReplyConsultationList(MemberConsultation consultation);
}