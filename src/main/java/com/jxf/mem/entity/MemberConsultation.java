package com.jxf.mem.entity;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 会员咨询Entity
 * @author huojiayuan
 * @version 2016-06-09
 */
public class MemberConsultation extends CrudEntity<MemberConsultation> {
	
	private static final long serialVersionUID = 1L;
	private Member member;		// 会员
	private String content;		// 咨询内容
	private String ipAddr;		// IP地址
	private Boolean isShow;		// 是否显示
	private MemberConsultation forConsultation;		// 原咨询编号

	private Integer count;
	
	/** 回复 */
	private List<MemberConsultation> replyConsultations = new ArrayList<MemberConsultation>();

	public MemberConsultation() {
		super();
	}

	public MemberConsultation(Long id){
		super(id);
	}	
	
	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	@Length(min=1, max=255, message="咨询内容长度必须介于 1 和 255 之间")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@Length(min=1, max=15, message="IP地址长度必须介于 1 和 15 之间")
	public String getIpAddr() {
		return ipAddr;
	}

	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}
	
	public Boolean getIsShow() {
		return isShow;
	}

	public void setIsShow(Boolean isShow) {
		this.isShow = isShow;
	}
	
	public MemberConsultation getForConsultation() {
		return forConsultation;
	}

	public void setForConsultation(MemberConsultation forConsultation) {
		this.forConsultation = forConsultation;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
	/**
	 * 获取回复
	 * 
	 * @return 回复
	 */
	public List<MemberConsultation> getReplyConsultations() {
		return replyConsultations;
	}

	/**
	 * 设置回复
	 * 
	 * @param replyConsultations
	 *            回复
	 */
	public void setReplyConsultations(List<MemberConsultation> replyConsultations) {
		this.replyConsultations = replyConsultations;
	}

}