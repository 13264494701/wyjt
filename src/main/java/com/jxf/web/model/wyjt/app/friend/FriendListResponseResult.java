package com.jxf.web.model.wyjt.app.friend;

import java.util.ArrayList;
import java.util.List;

/**
 * @作者: xiaorongdian
 * @创建时间 :2018年10月30日 下午5:06:22
 * @功能说明:
 */
public class FriendListResponseResult {

	/** 好友列表 */
	private List<Friend> friendList = new ArrayList<Friend>();
	/**时间戳*/
	private String timestamp;
	
	public List<Friend> getFriendList() {
		return friendList;
	}

	public void setFriendList(List<Friend> friendList) {
		this.friendList = friendList;
	}
	
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
	public class Friend {
		/** ID */
		private String id;
		/** 姓名(电话) */
		private String nameAndPhone;
		/** 电话 */
		private String phoneNo;
		/** 姓名 */
		private String name;
	    /** 姓名拼音*/
	    private String nameSpell;
		/** 头像 */
		private String headImage;
		
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getNameSpell() {
			return nameSpell;
		}
		public void setNameSpell(String nameSpell) {
			this.nameSpell = nameSpell;
		}
		public String getHeadImage() {
			return headImage;
		}
		public void setHeadImage(String headImage) {
			this.headImage = headImage;
		}
		public String getNameAndPhone() {
			return nameAndPhone;
		}
		public void setNameAndPhone(String nameAndPhone) {
			this.nameAndPhone = nameAndPhone;
		}
		public String getPhoneNo() {
			return phoneNo;
		}
		public void setPhoneNo(String phoneNo) {
			this.phoneNo = phoneNo;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
	}

}
