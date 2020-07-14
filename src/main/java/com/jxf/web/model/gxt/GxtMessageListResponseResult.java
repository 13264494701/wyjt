package com.jxf.web.model.gxt;

import java.util.ArrayList;
import java.util.List;

public class GxtMessageListResponseResult {


	/** 会员消息 */
	private List<GxtMessage> list = new ArrayList<GxtMessage>();

	public List<GxtMessage> getList() {
		return list;
	}

	public void setList(List<GxtMessage> list) {
		this.list = list;
	}
	
	public static class GxtMessage{
		
		/**消息id*/
		private String id;
		
		/**消息是否已读*/
		private Integer isRead;
		
		private Integer type;
		
		/**消息标题*/
		private String title;
		
		/**消息摘要*/
		private String summary;
		
		/**生成时间*/
		private String createTime;
		
		/**图标地址*/
		private String imageUrl;
		
		/** 消息详情*/
		private String jsonStr;
		
		/**跳转页面所需 val 值*/
		private String val;

		public GxtMessage(String id, Integer isRead, Integer type, String title, String summary, String createTime,String imageUrl,
				String jsonStr, String val) {
			super();
			this.id = id;
			this.isRead = isRead;
			this.type = type;
			this.title = title;
			this.summary = summary;
			this.createTime = createTime;
			this.imageUrl = imageUrl;
			this.jsonStr = jsonStr;
			this.val = val;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public Integer getIsRead() {
			return isRead;
		}

		public void setIsRead(Integer isRead) {
			this.isRead = isRead;
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

		public String getJsonStr() {
			return jsonStr;
		}

		public void setJsonStr(String jsonStr) {
			this.jsonStr = jsonStr;
		}

		public String getVal() {
			return val;
		}

		public void setVal(String val) {
			this.val = val;
		}

		public String getImageUrl() {
			return imageUrl;
		}

		public void setImageUrl(String imageUrl) {
			this.imageUrl = imageUrl;
		}
		
		
		

		
	}
	
	
	
	

}
