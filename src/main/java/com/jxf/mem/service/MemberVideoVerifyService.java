package com.jxf.mem.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberVideoVerify;
import com.jxf.svc.sys.crud.service.CrudService;
import com.jxf.web.model.wyjt.app.member.VideoVerifyResponseResult;


/**
 * 视频认证Service
 * @author XIAORONGDIAN
 * @version 2018-10-10
 */
public interface MemberVideoVerifyService extends CrudService<MemberVideoVerify> {

	/**
	 *	生产MD5签名
	 */
	String getMD5Sign(String pub_key, String partner_order_id, String sign_time, String security_key);

	/**
	 *	根据身份证查memberId
	 */
	Long getMemberIdByIdcard(String id_number);
	/**
	 *	根据身份证查是否已经认证 0否 1是
	 */
	Integer checkIdcardNo(String id_number);
	
	/**
	 * 	认证回调
	 * @param request
	 */
	Map<String, String> notifyProcess(HttpServletRequest request);

	/**
	 * 查询认证失败次数
	 * @param memberId
	 * @return
	 */
	Integer countFailure(Long memberId);

	/***
	 * 获取视频参数
	 * @param type 1修改支付密码 2实名认证 3转账
	 * @param orderId 业务ID
	 * @param member 身份证号
	 * @return
	 */
	VideoVerifyResponseResult getResult(int type, String orderId, Member member);
	
	
	void clear(MemberVideoVerify videoVerify);

	/**
	 * 根据memberId 查询身份证照片
	 * @param memberId
	 * @return
	 */
	MemberVideoVerify getMemberVideoVerifyByMemberId(Long memberId);

	MemberVideoVerify getByTrxId(Long id);

	void dealPayPassword(Member member, String payPassword, String orderId);

	/***
	 * 获取实名认证路径参数
	 * @param type 0实名认证 1修改支付密码 2更换手机
	 * @param orderId 业务ID
	 * @param member 用户
	 * @return
	 */
	String getGxtResult(int type, String orderId, Member member);
	/**
	 * 	认证回调公信堂
	 * @param request
	 */
	Map<String, String> notifyProcessForGxt(HttpServletRequest request);

	int getChangePhoneNoCounts(Member member);

	List<MemberVideoVerify> getLast5hRealIdentityRecords(String createTime);
	
	
	JSONObject orderQuery(String partner_order_id) throws Exception;
	
}