package com.jxf.mem.dao;

import org.apache.ibatis.annotations.Param;

import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberResetPayPwd;
import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;

/**
 * 修改支付密码DAO接口
 * @author XIAORONGDIAN
 * @version 2018-11-08
 */
@MyBatisDao
public interface MemberResetPayPwdDao extends CrudDao<MemberResetPayPwd> {

	int getResetPayPwdCountEveryday(@Param("member")Member member, @Param("beginDate")String beginDate,@Param("endDate") String endDate);

	int getResetPayPwdCountEveryMonth(@Param("member")Member member,@Param("beginDate")String beginDate,@Param("endDate") String endDate);
	
}