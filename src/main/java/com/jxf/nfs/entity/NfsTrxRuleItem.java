package com.jxf.nfs.entity;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

import com.jxf.mem.entity.MemberActTrx;
import com.jxf.nfs.entity.NfsActSub.TrxRole;
import com.jxf.svc.sys.crud.entity.CrudEntity;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

/**
 * 交易规则Entity
 * @author wo
 * @version 2018-09-20
 */
public class NfsTrxRuleItem extends CrudEntity<NfsTrxRuleItem> implements Comparable<NfsTrxRuleItem> {
	
	private static final long serialVersionUID = 1L;
		
	/** 交易代码 */
	private String trxCode;		
	/** 交易名称 */
	private String name;			
	/** 业务角色 */
	private TrxRole trxRole;		
	/** 科目编号 */
	private String subNo;		
	/** 科目名称 */
	private String subName;		
	/** 交易方向  C出账 D入账*/
	private String drc;	
	/** 金额计算表达式 */
	private String expression;	

	/** 流水组(给前台展示) */
	private MemberActTrx.Group trxGroup;
	/** 标题 */
	private String title;
	
	public NfsTrxRuleItem() {
		super();
	}

	public NfsTrxRuleItem(Long id){
		super(id);
	}

	@Length(min=1, max=5, message="交易代码长度必须介于 1 和 5 之间")
	public String getTrxCode() {
		return trxCode;
	}

	public void setTrxCode(String trxCode) {
		this.trxCode = trxCode;
	}
	
	@Length(min=1, max=127, message="交易名称长度必须介于 1 和 127 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
		
	public TrxRole getTrxRole() {
		return trxRole;
	}

	public void setTrxRole(TrxRole trxRole) {
		this.trxRole = trxRole;
	}
	
	@Length(min=1, max=4, message="科目编号长度必须介于 1 和 4 之间")
	public String getSubNo() {
		return subNo;
	}

	public void setSubNo(String subNo) {
		this.subNo = subNo;
	}
	
	@Length(min=1, max=64, message="科目名称长度必须介于 1 和 64 之间")
	public String getSubName() {
		return subName;
	}

	public void setSubName(String subName) {
		this.subName = subName;
	}
	
	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public String getDrc() {
		return drc;
	}

	public void setDrc(String drc) {
		this.drc = drc;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public MemberActTrx.Group getTrxGroup() {
		return trxGroup;
	}

	public void setTrxGroup(MemberActTrx.Group trxGroup) {
		this.trxGroup = trxGroup;
	}

	/**
	 * 如果记账方向为出账，则取反
	 * 
	 * @param trxAmt
	 *            
	 * @return 
	 */
	public BigDecimal getTrxAmt(BigDecimal trxAmt) {

		trxAmt = StringUtils.equals(getDrc(), "C")?trxAmt.negate():trxAmt;
		
		return trxAmt;
	}
	
	
	/**
	 * 计算金额
	 * 
	 * @param binding
	 *            
	 * @return 
	 */
	public BigDecimal calculate(Binding binding) {
		if (StringUtils.isEmpty(getExpression())) {
			return BigDecimal.ZERO;
		}
		BigDecimal result = BigDecimal.ZERO;
		try {
			GroovyShell groovyShell = new GroovyShell(binding);
			result = new BigDecimal(groovyShell.evaluate(getExpression()).toString());
		} catch (Exception e) {
			return BigDecimal.ZERO;
		}
		if(StringUtils.equals(getDrc(), "C") ){//出账
			result = result.negate();//取反
		}
		return result;
	}

	/**
	 * 实现compareTo方法
	 * 
	 * @param item
	 *            
	 * @return 比较结果
	 */
	public int compareTo(NfsTrxRuleItem item) {
		if (item == null) {
			return 1;
		}
		return this.drc.compareTo(item.getDrc());
	}
}