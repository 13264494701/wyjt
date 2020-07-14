package com.jxf.mem.entity;


import org.hibernate.validator.constraints.Length;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxf.svc.sys.crud.entity.CrudEntity;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

/**
 * 积分规则Entity
 * @author wo
 * @version 2018-08-12
 */
public class MemberPointRule extends CrudEntity<MemberPointRule> {
	private static final Logger log = LoggerFactory.getLogger(MemberPointRule.class);
	
	private static final long serialVersionUID = 1L;
	/**
	 * 类型
	 */
	public enum Type {

		/** 注册 */
		 register,

		/** 积分调整 */
		adjustment,

		/**同意放款,放款人加分,(100+金额分) */
		agreeLoan,
		/**借款人按时还款、逾期15天内还款,借款人加分,(100+金额分) */
		repay15,
		/**逾期15-30天间还款,借款人加分,(100+50%金额分) */
		repay30,
		/**逾期30-45天间还款,借款人加分,(100+20%金额分) */
		repay45,
		/**逾期超过5天,借款人扣分,(-200-金额分) */
		overDue5,
		/**借款人分期还款,借款人加当期分,(加当期金额分) */
		staging,
		/**借款人分期还款完结,借款人加分,(加100加当期金额分) */
		stagingPayOff,

		/**逾期超过45天且金额大于200,拉黑*/
		overDue45

		}
	/**
	 * 获取和使用
	 */
	public enum Drc {
		
		/** 增加 */
		reward,
		
		/** 减少 */
		spend
				
	}
	/** 业务类型 */
	private Type type;
	
	/** 增减方向 */
	private Drc drc;		
	/** 规则名称 */
	private String name;		
	/** 积分运算表达式 */
	private String pointExpression;		
	/** 显示排序 */
	private Integer sort;
	/** 是否启用*/
	private  Boolean isOn;
	
	public Boolean getIsOn() {
		return isOn;
	}

	public void setIsOn(Boolean isOn) {
		this.isOn = isOn;
	}

	public MemberPointRule() {
		super();
	}

	public MemberPointRule(Long id){
		super(id);
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
	
	public Drc getDrc() {
		return drc;
	}

	public void setDrc(Drc drc) {
		this.drc = drc;
	}
	
	@Length(min=1, max=4, message="规则名称长度必须介于 1 和 4 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getPointExpression() {
		return pointExpression;
	}

	public void setPointExpression(String pointExpression) {
		this.pointExpression = pointExpression;
	}
	
	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
	/**
	 * 计算积分
	 * 
	 * @param type
	 *            业务类型
	 * @param binding
	 *            输入变量          
	 * @return 积分
	 */
	public Long calculatePoint(Binding binding) {

		Long result = 0L;
		try {	
			GroovyShell groovyShell = new GroovyShell(binding);
			String pointExpression2 = getPointExpression();
			Object evaluate = groovyShell.evaluate(pointExpression2);
			String str = evaluate.toString();
			Long valueOf = Long.valueOf(str);
			result = valueOf;
		} catch (Exception e) {
			log.error("MemberPointRule.java 155 : " + e.toString());
			return 0L;
		}

		return result > 0L ? result : 0L;
	}
}