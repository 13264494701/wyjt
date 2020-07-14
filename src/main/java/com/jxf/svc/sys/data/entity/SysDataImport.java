package com.jxf.svc.sys.data.entity;

import org.hibernate.validator.constraints.Length;
import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 数据导入Entity
 * @author wo
 * @version 2019-01-13
 */
public class SysDataImport extends CrudEntity<SysDataImport> {
	
	private static final long serialVersionUID = 1L;
	/** 名称 */
	private String name;		
	/** 最小ID */
	private Long minId;		
	/** 最大ID */
	private Long maxId;		
	/** 总数量 */
	private Integer totalQuantity;		
	/** 已完成数量 */
	private Integer importQuantity;		
	/** 处理器 */
	private String handler;		
	/** 每次处理数量 */
	private Integer pQuantity;	
	
	
	public SysDataImport() {
		super();
	}

	public SysDataImport(Long id){
		super(id);
	}

	@Length(min=1, max=64, message="名称长度必须介于 1 和 64 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	

	public Long getMinId() {
		return minId;
	}

	public void setMinId(Long minId) {
		this.minId = minId;
	}
	

	public Long getMaxId() {
		return maxId;
	}

	public void setMaxId(Long maxId) {
		this.maxId = maxId;
	}
	

	public Integer getTotalQuantity() {
		return totalQuantity;
	}

	public void setTotalQuantity(Integer totalQuantity) {
		this.totalQuantity = totalQuantity;
	}
	

	public Integer getImportQuantity() {
		return importQuantity;
	}

	public void setImportQuantity(Integer importQuantity) {
		this.importQuantity = importQuantity;
	}
	
	@Length(min=1, max=128, message="处理器长度必须介于 1 和 128 之间")
	public String getHandler() {
		return handler;
	}

	public void setHandler(String handler) {
		this.handler = handler;
	}

	public Integer getpQuantity() {
		return pQuantity;
	}

	public void setpQuantity(Integer pQuantity) {
		this.pQuantity = pQuantity;
	}
	

	
}