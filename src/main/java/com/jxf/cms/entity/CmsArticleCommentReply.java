package com.jxf.cms.entity;

import org.hibernate.validator.constraints.Length;

import com.jxf.mem.entity.Member;
import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 评论回复Entity
 * @author JINXINFU
 * @version 2017-01-05
 */
public class CmsArticleCommentReply extends CrudEntity<CmsArticleCommentReply> {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 回复类型
	 */
	public enum Type {

		/** 用户回复 */
		member,

		/** 管理员回复 */
		admin,
		
		/** 系统回复 */
		system

	}
	/**
	 * 回复目标
	 */
	public enum Target {

		/** 回复评论 */
		tocomment,

		/** 回复会员*/
		tomember

	}
	private CmsArticleComment comment;		// 评论编号
	private Member fromMember;		// 发送会员
	private Member toMember;		// 接收会员
	private Type type;		// 回复类型
	private Target target;		// 回复目标
	private String content;		// 回复内容
	private String ip;		// 评论IP

	
	public CmsArticleCommentReply() {
		super();
	}

	public CmsArticleCommentReply(Long id){
		super(id);
	}

	public CmsArticleComment getComment() {
		return comment;
	}

	public void setComment(CmsArticleComment comment) {
		this.comment = comment;
	}
	

	public Member getFromMember() {
		return fromMember;
	}

	public void setFromMember(Member fromMember) {
		this.fromMember = fromMember;
	}
	

	public Member getToMember() {
		return toMember;
	}

	public void setToMember(Member toMember) {
		this.toMember = toMember;
	}
	
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
	public Target getTarget() {
		return target;
	}

	public void setTarget(Target target) {
		this.target = target;
	}
	@Length(min=1, max=255, message="回复内容长度必须介于 1 和 255 之间")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@Length(min=0, max=100, message="评论IP长度必须介于 0 和 100 之间")
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	
}