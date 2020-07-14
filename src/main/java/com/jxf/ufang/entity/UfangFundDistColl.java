package com.jxf.ufang.entity;

import java.math.BigDecimal;

import org.hibernate.validator.constraints.Length;

import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 资金分发归集Entity
 * @author wo
 * @version 2018-11-22
 */
public class UfangFundDistColl extends CrudEntity<UfangFundDistColl> {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 交易状态
	 */
	public enum Status {

		/**登记*/
		REGIST,
		
		/**成功*/
		SUCC,

		/**失败*/
		FAIL,
		
		/**其它*/
		OTHERS
	}
	/**
	 * 交易类型
	 */
	public enum Type {
		
		/** 派发 */
		dist,

		/** 归集*/
		coll
		
	}
	/** 机构账户 */
	private UfangBrnAct brnAct;
	
	private UfangUserAct userAct;
	/** 用户 */
	private UfangUser user;		
	/** 交易类型 */
	private Type type;		
	/** 交易金额 */
	private BigDecimal amount;		
	/** 币种 */
	private String currCode;		
	/** 交易状态 */
	private Status status;

	public UfangUserAct getUserAct() {
		return userAct;
	}

	public void setUserAct(UfangUserAct userAct) {
		this.userAct = userAct;
	}

	public UfangFundDistColl() {
		super();
	}

	public UfangFundDistColl(Long id){
		super(id);
	}

	public UfangBrnAct getBrnAct() {
		return brnAct;
	}

	public void setBrnAct(UfangBrnAct brnAct) {
		this.brnAct = brnAct;
	}

	public UfangUser getUser() {
		return user;
	}

	public void setUser(UfangUser user) {
		this.user = user;
	}
	

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
	
	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	@Length(min=1, max=3, message="币种长度必须介于 1 和 3 之间")
	public String getCurrCode() {
		return currCode;
	}

	public void setCurrCode(String currCode) {
		this.currCode = currCode;
	}
	

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}




	
}