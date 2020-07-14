package com.jxf.web.model.wyjt.app.friend;

import java.util.ArrayList;
import java.util.List;



/**
 * @作者: xiaorongdian
 * @创建时间 :2018年11月5日 下午3:59:36
 * @功能说明:
 */
public class PhoneListResponseResult {

	/** 通讯录人员列表 */
	private List<Phone>  phoneList = new ArrayList<Phone>();	

	public List<Phone> getPhoneList() {
		return phoneList;
	}
	public void setPhoneList(List<Phone> phoneList) {
		this.phoneList = phoneList;
	}


	public static class Phone {
		
		/** 电话 */
		private String phoneNo;
		/**	好友id */
		private String friendId;//不是注册用户 为  " "
		/** 状态 */
		private Integer status;//0->不是注册用户;1->是注册用户但不是好友;2->是好友;
		

		public Phone(String phoneNo,String friendId, Integer status) {
			super();
			this.phoneNo = phoneNo;
			this.friendId = friendId;
			this.status = status;
		}
		
		public String getPhoneNo() {
			return phoneNo;
		}
		public void setPhoneNo(String phoneNo) {
			this.phoneNo = phoneNo;
		}
		public Integer getStatus() {
			return status;
		}
		public void setStatus(Integer status) {
			this.status = status;
		}

		public String getFriendId() {
			return friendId;
		}

		public void setFriendId(String friendId) {
			this.friendId = friendId;
		}
		
	}





	
}
