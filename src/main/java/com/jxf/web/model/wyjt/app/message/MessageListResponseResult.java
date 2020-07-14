package com.jxf.web.model.wyjt.app.message;

import java.util.ArrayList;
import java.util.List;

public class MessageListResponseResult {

	/** 会员消息 */
	private List<Message> list = new ArrayList<Message>();

	public List<Message> getList() {
		return list;
	}

	public void setList(List<Message> list) {
		this.list = list;
	}
	
	public static class Message{
		
		/**消息类型*/
		private Integer category;
		
		/**消息是否已读*/
		private Integer isRead;
		
		/**消息id*/
		private String id;
		
		/**
		 * 	消息详情的跳转类型
		 * 	type：0-33 跳转到借款详情 val 存 id|orgType
		 * 	type：34 跳转到多人借款支付页面 val存 id|orgType
		 * 	type：35 跳转到多人借款申请页面 val存 id|orgType
		 * 	type：36 跳转到多人借款关闭页面 
		 * 	type：37  跳转到转账详情	val  存好友id
		 * 	type：38-41 跳转到申请信用档案详情	val 存记录id
		 * 	type：42 跳转到一元报告支付页面  val 存好友id
		 * 	type：43 跳转到一元报告详情页面 val 存好友id
		 * 	type:60 61 跳转到	借款详情 val 存 id|orgType
		 * 	type:62-65 跳转到转让详情 val 存债转id
		 * 	type:66 跳转到债转买入详情 val 存债转id
		 *  
		 */
		private Integer type;
		
		/**消息标题*/
		private String title;
		
		/**消息摘要*/
		private String summary;
		
		/**生成时间*/
		private String createTime;
		
		/**icon 图标下载地址*/
		private String imageUrl;
		
		/**跳转页面所需 val 值*/
		private String val;
		
		/** json串 字段 name:对方姓名 loanAmount:借款金额 time:时间 status:借条状态 
		 * colorStatus 0黑色 1 黄色 2 灰色  fee 服务费
		type 0 借条展示(字段name loanAmount:借款金额 time status colorStatus type)
		1	转账展示(字段loanAmount:交易金额 time type)
		2	信用档案展示(字段name time status colorStatus type)
		3	仲裁展示(字段name loanAmount:仲裁金额 time status colorStatus type)
		4 强执展示(字段name loanAmount:强执金额 time status colorStatus type)
		5退款服务费展示(字段name loanAmount fee time type)
		6老版本展示*/
		private String rmk;

		public Message(Integer category, Integer isRead, String id, Integer type, String title, String summary,
				String createTime, String imageUrl, String val, String rmk) {
			super();
			this.category = category;
			this.isRead = isRead;
			this.id = id;
			this.type = type;
			this.title = title;
			this.summary = summary;
			this.createTime = createTime;
			this.imageUrl = imageUrl;
			this.val = val;
			this.rmk = rmk;
		}

		public Integer getCategory() {
			return category;
		}

		public void setCategory(Integer category) {
			this.category = category;
		}

		public Integer getIsRead() {
			return isRead;
		}

		public void setIsRead(Integer isRead) {
			this.isRead = isRead;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public Integer getType() {
			return type;
		}

		public void setType(Integer type) {
			this.type = type;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getSummary() {
			return summary;
		}

		public void setSummary(String summary) {
			this.summary = summary;
		}

		public String getCreateTime() {
			return createTime;
		}

		public void setCreateTime(String createTime) {
			this.createTime = createTime;
		}

		public String getImageUrl() {
			return imageUrl;
		}

		public void setImageUrl(String imageUrl) {
			this.imageUrl = imageUrl;
		}

		public String getVal() {
			return val;
		}

		public void setVal(String val) {
			this.val = val;
		}

		public String getRmk() {
			return rmk;
		}

		public void setRmk(String rmk) {
			this.rmk = rmk;
		}
		
		

		

		
	}
	
	
	
	
}
