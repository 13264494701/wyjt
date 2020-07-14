package com.jxf.svc.plugin;


/**
 * 
 * @类功能说明： 插件配置属性参数
 * @类修改者： 
 * @修改日期： 
 * @修改说明： 
 * @公司名称：北京金信服科技有限公司
 * @作者：zhuhuijie 
 * @创建时间：2016年6月17日 下午12:00:43 
 * @版本：V1.0
 */
public class PluginConfigAttr implements Comparable<PluginConfigAttr>{
	
	private String field; // 属性
	private String name;  //名称
	private String value; //值
	private Boolean required; //是否必须
	private Integer sort;
	private ShowType showType = ShowType.INPUT;
	
	public enum ShowType{
		INPUT,
		PASSWORD,
		TEXTAREA,
		CKEDITOR,
		SELECT,
		RADIOBOX,
		CHECKBOX,
		DATESELECT,
		IMAGESELECT,
		FILESELECT;
	}
	
	public PluginConfigAttr() {
		
	}
	public PluginConfigAttr(String field, String name, Boolean required, Integer sort) {
		super();
		this.field = field;
		this.name = name;
		this.required = required;
		this.sort = sort;
	}
	
	public PluginConfigAttr(String field, String name, Boolean required, Integer sort, ShowType showType) {
		super();
		this.field = field;
		this.name = name;
		this.required = required;
		this.sort = sort;
		this.showType = showType;
	}
	public PluginConfigAttr(String field, String name, String value, Boolean required, Integer sort) {
		super();
		this.field = field;
		this.name = name;
		this.value = value;
		this.required = required;
		this.sort = sort;
	}
	public PluginConfigAttr(String field, String name, String value, Boolean required, Integer sort,ShowType showType) {
		super();
		this.field = field;
		this.name = name;
		this.value = value;
		this.required = required;
		this.sort = sort;
		this.showType = showType;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Boolean getRequired() {
		return required;
	}
	public void setRequired(Boolean required) {
		this.required = required;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	public ShowType getShowType() {
		return showType;
	}
	public void setShowType(ShowType showType) {
		this.showType = showType;
	}
	@Override
	public String toString() {
		return "PluginConfigAttr [field=" + field + ", name=" + name + ", value=" + value + ", required=" + required
				+ ", sort=" + sort + ", showType=" + showType + "]";
	}
	@Override
	public int compareTo(PluginConfigAttr o) {
		if(this.getSort() > o.getSort()){  
            return 1;  
        }else {  
            return -1;  
        }  
	}
	
}
