package com.jxf.svc.sys.crud.entity;

import java.util.Date;


import org.hibernate.validator.constraints.Length;

import com.alibaba.fastjson.annotation.JSONField;
import com.jxf.svc.sys.base.entity.BaseEntity;
import com.jxf.svc.sys.user.entity.User;
import com.jxf.svc.sys.util.UserUtils;
import com.jxf.svc.utils.SnowFlake;

/**
 * 数据Entity类
 * @author jxf
 * @version 2015-07-16
 */
public abstract class CrudEntity<T> extends BaseEntity<T> {

	@JSONField(serialize = false)
	private static final long serialVersionUID = 1L;
	
	@JSONField(serialize = false)
	protected String rmk;	// 备注
	
	/**创建者*/
	@JSONField(serialize = false)
	protected User createBy;
	
    /**创建日期*/
	protected Date createTime;	
	
	/**更新者*/
	@JSONField(serialize = false)
	protected User updateBy;	
	
	/**更新日期*/
	protected Date updateTime; 
	
	/**数据版本*/
	@JSONField(serialize = false)
	protected Integer version; 	
	

	/**删除标记*/
	@JSONField(serialize = false)
	protected String delFlag; 	//（0：正常；1：删除；2：审核）
	
	public CrudEntity() {
		super();
		this.delFlag = DEL_FLAG_NORMAL;
	}
	
	public CrudEntity(Long id) {
		super(id);
	}
	
	public String getRmk() {
		return rmk;
	}

	public void setRmk(String rmk) {
		this.rmk = rmk;
	}
	@JSONField(serialize = false)
	public User getCreateBy() {
		return createBy;
	}

	public void setCreateBy(User createBy) {
		this.createBy = createBy;
	}

	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@JSONField(serialize = false)
	public User getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(User updateBy) {
		this.updateBy = updateBy;
	}

	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@JSONField(serialize = false)
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
	
	@JSONField(serialize = false)
	@Length(min=1, max=1)
	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	/**
	 * 插入之前执行方法，需要手动调用
	 */
	@Override
	public void preInsert(){
		//不限制ID为UUID，调用setIsNewRecord()使用自定义ID
		if (this.getIsNewRecord()){
			setId(SnowFlake.getId());
		}
		User user = UserUtils.getUser();
		if (null!=user&&user.getId()!=null){
			this.updateBy = user;
			this.createBy = user;
		}
		this.updateTime = new Date();
		this.createTime = this.updateTime;
		this.version = 0;
	}
	
	/**
	 * 更新之前执行方法，需要手动调用
	 */
	@Override
	public void preUpdate(){
		User user = UserUtils.getUser();
		if (user.getId()!=null){
			this.updateBy = user;
		}
		this.updateTime = new Date();
	}
}
