package com.jxf.svc.sys.base.entity;

import java.io.Serializable;
import java.util.Map;

import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.common.collect.Maps;
import com.jxf.svc.config.Global;
import com.jxf.svc.converter.LongToStringSerializer;
import com.jxf.svc.persistence.Page;



/**
 * Entity支持类
 * @author jxf
 * @version 2015-07-16
 */
public abstract class BaseEntity<T> implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 实体编号（唯一标识）
	 */
	@JSONField(serializeUsing = LongToStringSerializer.class)
	protected Long id;
	
	/**
	 * 当前实体分页对象
	 */
	@JSONField(serialize = false)
	protected Page<T> page;
	
	/**
	 * 自定义SQL（SQL标识，SQL内容）
	 */
	@JSONField(serialize = false)
	protected Map<String, String> sqlMap;
	
	/**
	 * 是否是新记录（默认：false），调用setIsNewRecord()设置新记录，使用自定义ID。
	 * 设置为true后强制执行插入语句，ID不会自动生成，需从手动传入。
	 */
	@JSONField(serialize = false)
	protected boolean isNewRecord = false;
    /**
     * 页面重定向URL
     */
	@JSONField(serialize = false)
	protected String redirectUrl;
	

	public BaseEntity() {
		
	}
	
	public BaseEntity(Long id) {
		this();
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@JSONField(serialize = false)
	@XmlTransient
	public Page<T> getPage() {
		if (page == null){
			page = new Page<T>();
		}
		return page;
	}
	
	public Page<T> setPage(Page<T> page) {
		this.page = page;
		return page;
	}

	@JSONField(serialize = false)
	@XmlTransient
	public Map<String, String> getSqlMap() {
		if (sqlMap == null){
			sqlMap = Maps.newHashMap();
		}
		return sqlMap;
	}

	public void setSqlMap(Map<String, String> sqlMap) {
		this.sqlMap = sqlMap;
	}
	
	/**
	 * 插入之前执行方法，子类实现
	 */
	public abstract void preInsert();
	
	/**
	 * 更新之前执行方法，子类实现
	 */
	public abstract void preUpdate();
	
    /**
	 * 是否是新记录（默认：false），调用setIsNewRecord()设置新记录，使用自定义ID。
	 * 设置为true后强制执行插入语句，ID不会自动生成，需从手动传入。
     * @return
     */
	public boolean getIsNewRecord() {
        return isNewRecord || getId()==null;
    }

	/**
	 * 是否是新记录（默认：false），调用setIsNewRecord()设置新记录，使用自定义ID。
	 * 设置为true后强制执行插入语句，ID不会自动生成，需从手动传入。
	 */
	public void setIsNewRecord(boolean isNewRecord) {
		this.isNewRecord = isNewRecord;
		setId(null);
	}
	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}
	/**
	 * 全局变量对象
	 */
	@JSONField(serialize = false)
	public Global getGlobal() {
		return Global.getInstance();
	}
	
	/**
	 * 获取数据库名称
	 */
	@JSONField(serialize = false)
	public String getDbName(){
		return Global.getConfig("jdbc.type");
	}
	
    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!getClass().equals(obj.getClass())) {
            return false;
        }
        BaseEntity<?> that = (BaseEntity<?>) obj;
        return null == this.getId() ? false : this.getId().equals(that.getId());
    }
	/**
	 * 重写hashCode方法
	 * 
	 * @return HashCode
	 */
	@Override
	public int hashCode() {
		int hashCode = 17;
		hashCode += getId() != null ? getId().hashCode() * 31 : 0;
		return hashCode;
	}
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
    
	/**
	 * 删除标记（0：正常；1：删除；2：审核；）
	 */
	@JSONField(serialize = false)
	public static final String DEL_FLAG_NORMAL = "0";
	@JSONField(serialize = false)
	public static final String DEL_FLAG_DELETE = "1";
	@JSONField(serialize = false)
	public static final String DEL_FLAG_AUDIT = "2";
	
}
