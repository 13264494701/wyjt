package com.jxf.cms.entity;

import java.util.Date;

import com.jxf.svc.sys.crud.entity.CrudEntity;
/**
 * 
 * @类功能说明： 静态生成
 * @类修改者： 
 * @修改日期： 
 * @修改说明： 
 * @公司名称：北京金信服科技有限公司
 * @作者：HUOJIABAO 
 * @创建时间：2016年11月13日 下午5:05:29 
 * @版本：V1.0
 */
public class GenerateStatic extends CrudEntity<GenerateStatic> {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 生成类型
	 */
	public enum GenerateType {
		/**
		 * 首页
		 */
		index,

		/**
		 * 文章
		 */
		article,

		/**
		 * 其它
		 */
		other
	}

	private GenerateType genType;
	private Category category;
	private Date beginDate;
	private Date endDate;
	private Integer genCount;
	
	public GenerateType getGenType() {
		return genType;
	}
	public void setGenType(GenerateType genType) {
		this.genType = genType;
	}
	
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}	
	
	public Date getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Integer getGenCount() {
		return genCount;
	}
	public void setGenCount(Integer genCount) {
		this.genCount = genCount;
	}

}
