package com.jxf.rc.entity;



import com.jxf.rc.entity.RcCaData.Type;
import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 信用报告原始数据表Entity
 * @author lmy
 * @version 2018-12-17
 */
public class RcCaSourceData extends CrudEntity<RcCaSourceData> {
	
	
	public enum Status {

		/**
		 * 未进行过数据整理
		 */
		notfinish_arrange,
		/**
		 * 已进行过数据整理
		 */
		finish_arrange

	}
	
	private static final long serialVersionUID = 1L;
	/** 会员编号 */
	private Long memberId;	
	/** 源数据编号 */
	private String sourceDataId;
	/** 数据类型 */
	private Type type;		
	/** 内容 */
	private String content;	
	/** 内容路径 */
	private String path;
	/** 数据状态 */
	private Status status;	
	
	/** 是否平台会员 */
	private Boolean isMember;
	
	public RcCaSourceData() {
		super();
	}

	public RcCaSourceData(Long id){
		super(id);
	}

	


	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public String getSourceDataId() {
		return sourceDataId;
	}

	public void setSourceDataId(String sourceDataId) {
		this.sourceDataId = sourceDataId;
	}


	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Boolean getIsMember() {
		return isMember;
	}

	public void setIsMember(Boolean isMember) {
		this.isMember = isMember;
	}

	
	
	
}