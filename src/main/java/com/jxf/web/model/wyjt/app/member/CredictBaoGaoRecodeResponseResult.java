package com.jxf.web.model.wyjt.app.member;

import java.util.ArrayList;



/**
 * 信用报告申请记录
 * @author Administrator
 *
 */
public class CredictBaoGaoRecodeResponseResult {
	/**
	 * 表格数据
	 */
	protected ArrayList<CredictBaoGaoRecode> tableItem;
	
	public ArrayList<CredictBaoGaoRecode> getTableItem() {
		return tableItem;
	}

	public void setTableItem(ArrayList<CredictBaoGaoRecode> tableItem) {
		this.tableItem = tableItem;
	}

	public class CredictBaoGaoRecode {
		/**
		 * 姓名
		 */
		private String name;
		
		/**
		 * 内容
		 */
		private String note;
		
		/**
		 * 时间
		 */
		private String createTime;
		
		/**
		 * 头像地址
		 */
		private String imgUrl;
		
		/**
		 * 0：可查看  1已过期
		 */
		private int examineStatus;
		
		
		private String yxbId;
		
		/**
		 * 跳转类别 0:免费 1 一元的
		 */
		
		private int type;

		public int getType() {
			return type;
		}

		public void setType(int type) {
			this.type = type;
		}

		public String getYxbId() {
			return yxbId;
		}

		public void setYxbId(String yxbId) {
			this.yxbId = yxbId;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getNote() {
			return note;
		}

		public void setNote(String note) {
			this.note = note;
		}

		public String getCreateTime() {
			return createTime;
		}

		public void setCreateTime(String createTime) {
			this.createTime = createTime;
		}

		public String getImgUrl() {
			return imgUrl;
		}

		public void setImgUrl(String imgUrl) {
			this.imgUrl = imgUrl;
		}

		public int getExamineStatus() {
			return examineStatus;
		}

		public void setExamineStatus(int examineStatus) {
			this.examineStatus = examineStatus;
		}

	
	}

}
