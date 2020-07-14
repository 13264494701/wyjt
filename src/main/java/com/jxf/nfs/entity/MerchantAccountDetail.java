package com.jxf.nfs.entity;

import java.math.BigDecimal;

import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 *		商户账户详情
 * @author SuHuimin
 * @version 2019-02-28 14:04:00
 */
public class MerchantAccountDetail extends CrudEntity<MerchantAccountDetail> {
	
	private static final long serialVersionUID = 1L;
	
	/** 富友账户账面余额*/
	private BigDecimal ctamt;
	
	/** 富友账户可用余额*/
	private BigDecimal caamt;
	
	/** 富友账户待结转余额*/
	private BigDecimal cuamt;
	
	/** 富友账户冻结余额*/
	private BigDecimal cfamt;
	
	/** 富友代付账户账面余额*/
	private BigDecimal txctamt;
	
	/** 富友代付账户可用余额*/
	private BigDecimal txcaamt;
	
	/** 富友代付账户待结转余额*/
	private BigDecimal txcuamt;
	
	/** 富友代付账户冻结余额*/
	private BigDecimal txcfamt;
	
	/** 连连账户可用余额*/
	private BigDecimal lianlianBalance;

	public BigDecimal getCtamt() {
		return ctamt;
	}

	public void setCtamt(BigDecimal ctamt) {
		this.ctamt = ctamt;
	}

	public BigDecimal getCaamt() {
		return caamt;
	}

	public void setCaamt(BigDecimal caamt) {
		this.caamt = caamt;
	}

	public BigDecimal getCuamt() {
		return cuamt;
	}

	public void setCuamt(BigDecimal cuamt) {
		this.cuamt = cuamt;
	}

	public BigDecimal getCfamt() {
		return cfamt;
	}

	public void setCfamt(BigDecimal cfamt) {
		this.cfamt = cfamt;
	}

	public BigDecimal getTxctamt() {
		return txctamt;
	}

	public void setTxctamt(BigDecimal txctamt) {
		this.txctamt = txctamt;
	}

	public BigDecimal getTxcaamt() {
		return txcaamt;
	}

	public void setTxcaamt(BigDecimal txcaamt) {
		this.txcaamt = txcaamt;
	}

	public BigDecimal getTxcuamt() {
		return txcuamt;
	}

	public void setTxcuamt(BigDecimal txcuamt) {
		this.txcuamt = txcuamt;
	}

	public BigDecimal getTxcfamt() {
		return txcfamt;
	}

	public void setTxcfamt(BigDecimal txcfamt) {
		this.txcfamt = txcfamt;
	}

	public BigDecimal getLianlianBalance() {
		return lianlianBalance;
	}

	public void setLianlianBalance(BigDecimal lianlianBalance) {
		this.lianlianBalance = lianlianBalance;
	}

}