package com.jxf.mem.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberVideoVerify;
import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;

/**
 * 视频认证DAO接口
 * @author XIAORONGDIAN
 * @version 2018-10-10
 */
@MyBatisDao
public interface MemberVideoVerifyDao extends CrudDao<MemberVideoVerify> {

	/**
	 * 根据身份证查memberId
	 * @param id_number
	 * @return
	 */
	Long getMemberIdByIdcard(@Param("idNo") String idNo);
	/**
	 * 根据身份证查是否已经认证
	 * @param id_number
	 * @return >0是 0否
	 */
	Integer checkIdcardNo(@Param("idNo")String idNo);

	/**
	 * 根据memberId 查询认证失败次数
	 * @param memberId
	 * @return	MemberVideoVerify
	 */
	Integer countFailure(@Param("memberId")Long memberId);
	
	/**
	 * 根据memberId 查询身份证照片
	 * @param memberId
	 * @return
	 */
	MemberVideoVerify getMemberVideoVerifyByMemberId(Long memberId);
	/**
	 * 根据trxid 获取认证记录
	 * @param id
	 * @return
	 */
	MemberVideoVerify getByTrxId(Long id);
	
	/**
	 * 查询每年 修改手机号次数
	 * @param member
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	int getChangePhoneNoCounts(@Param("member")Member member, @Param("beginDate")String beginDate,@Param("endDate") String endDate);
	
	/**
	 * 查询最近5小时内实名认证通过的认证项
	 * @return
	 */
	List<MemberVideoVerify> getLast5hRealIdentityRecords(@Param("createTime")String createTime);
	
}