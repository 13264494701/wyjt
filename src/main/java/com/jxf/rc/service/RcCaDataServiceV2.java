package com.jxf.rc.service;




import com.jxf.mem.entity.Member;
import com.jxf.rc.entity.RcCaDataV2;
import com.jxf.rc.entity.RcCaDataV2.Type;
import com.jxf.svc.sys.crud.service.CrudService;
import com.jxf.web.model.wyjt.app.member.CaAuthResponseResult.UserBriefLegalize;



/**
 * 信用报告数据表Service
 * @author wo
 * @version 2019-08-12
 */
public interface RcCaDataServiceV2 extends CrudService<RcCaDataV2>{

	
	
	/**
	   * 根据手机号和认证类型获取信用档案
	 * @param 
	 * @return
	 */
	RcCaDataV2  getByPhoneNoAndType(String phoneNo,RcCaDataV2.Type type);
	/**
	   * 根据手机号和认证类型获取信用档案 (已认证的)
	 * @param 
	 * @return
	 */
	RcCaDataV2 getSuccessRecord(String phoneNo, Type type);
	
	
	/**
	 * 获取根据的认证项信息(我的认证界面使用)新版本
	 * @param member
	 * @param type
	 * @return
	 */
	UserBriefLegalize obtainRenZhengData(Member member, Type type);
	
	String getGxbToken(Member member, int type);
	
	int getRenZhengNum(Member merber);
}
