package com.jxf.cms.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.jxf.mem.entity.Member;
import com.jxf.svc.sys.crud.entity.CrudEntity;
import com.jxf.svc.sys.user.entity.User;

/**
 * 评论Entity
 * @author jxf
 * @version 1.0 2015-07-10
 */
public class CmsArticleComment extends CrudEntity<CmsArticleComment> {

	private static final long serialVersionUID = 1L;
	/**
	 * 评论类型
	 */
	public enum Type {

		/** 用户评论 */
		member,

		/** 管理员评论 */
		admin,
		
		/** 系统评论 */
		system

	}
	
	/** 评论文章 */
	private CmsArticle article;	
	
	/** 评论会员 */
	private Member member; 	
	
	/** 评论类型 */
	private Type type;
	
	private String content; // 评论内容	
	private Integer likes;  // 点赞人数
	private String ip; 		// 评论IP
	private User auditUser; // 审核人
	private Date auditDate;	// 审核时间


	/** 评论回复 */
	private List<CmsArticleCommentReply> commentReplys = new ArrayList<CmsArticleCommentReply>();

	private Integer count;
	
	public CmsArticleComment() {
		super();
		this.delFlag = DEL_FLAG_NORMAL;
	}
	
	public CmsArticleComment(Long id){
		this();
		this.id = id;
	}

	public CmsArticle getArticle() {
		return article;
	}

	public void setArticle(CmsArticle article) {
		this.article = article;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
	
	@Length(min=1, max=255)
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	public Integer getLikes() {
		return likes;
	}

	public void setLikes(Integer likes) {
		this.likes = likes;
	}
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	public User getAuditUser() {
		return auditUser;
	}

	public void setAuditUser(User auditUser) {
		this.auditUser = auditUser;
	}

	public Date getAuditDate() {
		return auditDate;
	}

	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public List<CmsArticleCommentReply> getCommentReplys() {
		return commentReplys;
	}

	public void setCommentReplys(List<CmsArticleCommentReply> commentReplys) {
		this.commentReplys = commentReplys;
	}



}