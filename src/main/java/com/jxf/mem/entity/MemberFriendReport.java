package com.jxf.mem.entity;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 好友投诉Entity
 * @author gaobo
 * @version 2018-11-16
 */
public class MemberFriendReport extends CrudEntity<MemberFriendReport> {
	
	private static final long serialVersionUID = 1L;
	
	public enum Type {
		
		/** 密码泄露 */
		paswdLeak,
		
		/** 电话骚扰 */
		phoneHarassment,
		
		/** 欺诈 */
		cheat,

		/** 暴力催收 */
		violence,
		
		/** 其他 */
		other,
		
	}
	
	/**
	 * 状态
	 */
	public enum Status {

		/** 待处理 */
		pendingProcess,

		/** 已处理 */
		hasProcessed
	}
	
	/** 投诉人 */
	private Member member;		
	/** 好友 */
	private Member friend;		
	/** 投诉类型 */
	private Type type;	
	/** 投诉类型 */
	private Status status;	
	/** 投诉标题 */
	private String title;		
	/** 投诉内容 */
	private String content;		
	/** 图片证据 */
	private String images;	
	/** 投诉人站内信内容 */
	private String memberIms;
	/** 被投诉人站内信内容 */
	private String friendIms;
	
	private List<String> imagesList = new ArrayList<String>();
	
	public MemberFriendReport() {
		super();
	}

	public MemberFriendReport(Long id){
		super(id);
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public Member getFriend() {
		return friend;
	}

	public void setFriend(Member friend) {
		this.friend = friend;
	}
	
	@Length(min=0, max=64, message="投诉标题长度必须介于 0 和 64 之间")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@Length(min=0, max=255, message="投诉内容长度必须介于 0 和 255 之间")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@Length(min=0, max=255, message="图片证据长度必须介于 0 和 255 之间")
	public String getImages() {
		return images;
	}

	public void setImages(String images) {
		this.images = images;
	}

	public MemberFriendReport.Type getType() {
		return type;
	}

	public void setType(MemberFriendReport.Type type) {
		this.type = type;
	}
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	public List<String> getImagesList() {
		return imagesList;
	}

	public void setImagesList(List<String> imagesList) {
		this.imagesList = imagesList;
	}

	public String getMemberIms() {
		return memberIms;
	}

	public void setMemberIms(String memberIms) {
		this.memberIms = memberIms;
	}

	public String getFriendIms() {
		return friendIms;
	}

	public void setFriendIms(String friendIms) {
		this.friendIms = friendIms;
	}




	
}