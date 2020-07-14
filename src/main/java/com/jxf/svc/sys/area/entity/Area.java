package com.jxf.svc.sys.area.entity;

import org.hibernate.validator.constraints.Length;

import com.jxf.svc.sys.tree.entity.TreeEntity;

/**
 * 区域Entity
 * @author jxf
 * @version 2015-07-28
 */
public class Area extends TreeEntity<Area> {

	private static final long serialVersionUID = 1L;
	private String code; 	// 区域编码
	private String type; 	// 区域类型（1：国家；2：省份、直辖市；3：地市；4：区县）
	
	public Area(){
		super();
		this.sort = 30;
	}

	public Area(Long id){
		super(id);
	}
	
//	@JsonBackReference
//	@NotNull
	public Area getParent() {
		return parent;
	}

	public void setParent(Area parent) {
		this.parent = parent;
	}

	@Length(min=1, max=1)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Length(min=0, max=100)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	@Override
	public String toString() {
		return name;
	}
	/**
	 * 获取树路径
	 * 
	 * @return 树路径
	 */
	public String getTreePath() {
		return super.getParentIds();
	}
}