package com.jxf.rc.entity;

import org.hibernate.validator.constraints.Length;

import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 新颜雷达报告Entity
 * @author lmy
 * @version 2018-12-18
 */
public class RcXinyan extends CrudEntity<RcXinyan> {
	
	private static final long serialVersionUID = 1L;
			/** 优放用户 */
	private String ufangEmpNo;		
			/** 授权唯一标识 */
	private String token;		
			/** 姓名 */
	private String name;		
			/** 身份证号码 */
	private String idNo;		
			/** 手机号码 */
	private String phoneNo;		
			/** 查询结果 */
	private String result;		
			/** 是否是自己支付的 */
	private String isSelfbuy;		
	
	public RcXinyan() {
		super();
	}

	public RcXinyan(Long id){
		super(id);
	}

	@Length(min=1, max=8, message="优放用户长度必须介于 1 和 8 之间")
	public String getUfangEmpNo() {
		return ufangEmpNo;
	}

	public void setUfangEmpNo(String ufangEmpNo) {
		this.ufangEmpNo = ufangEmpNo;
	}
	
	@Length(min=0, max=50, message="授权唯一标识长度必须介于 0 和 50 之间")
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	@Length(min=0, max=64, message="姓名长度必须介于 0 和 64 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Length(min=0, max=18, message="身份证号码长度必须介于 0 和 18 之间")
	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}
	
	@Length(min=0, max=11, message="手机号码长度必须介于 0 和 11 之间")
	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	@Length(min=0, max=1, message="是否是自己支付的长度必须介于 0 和 1 之间")
	public String getIsSelfbuy() {
		return isSelfbuy;
	}

	public void setIsSelfbuy(String isSelfbuy) {
		this.isSelfbuy = isSelfbuy;
	}
	
}