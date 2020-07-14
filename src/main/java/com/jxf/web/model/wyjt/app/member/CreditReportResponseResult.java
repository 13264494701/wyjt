package com.jxf.web.model.wyjt.app.member;

import java.util.ArrayList;

/**
 * 信用报告返回类
 * 
 * @author Administrator
 *
 */
public class CreditReportResponseResult {
	/**
	 * 报告编号
	 */
	protected String reportID;

	public String getCertified() {
		return certified;
	}

	public void setCertified(String certified) {
		this.certified = certified;
	}

	/**
	 * 更新时间
	 */
	protected String updateTime;

	/**
	 * 姓名
	 */
	protected String name;

	/**
	 * 手机号
	 */
	protected String cellPhone;

	/**
	 * 身份证
	 */
	protected String card;

	/**
	 * 居住地
	 */
	protected String residence;

	/**
	 * 头像地址
	 */
	protected String headImgUrl;

	/**
	 * 表格数据
	 */
	protected ArrayList<CreditTable> tableItem;

	/**
	 * 已经认证过的 如果没有串空字符串， 多个数据之间以^分割 e.g. 基础^淘宝^运营商
	 */

	protected String certified;

	public String getReportID() {
		return reportID;
	}

	public void setReportID(String reportID) {
		this.reportID = reportID;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCellPhone() {
		return cellPhone;
	}

	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}

	public String getCard() {
		return card;
	}

	public void setCard(String card) {
		this.card = card;
	}

	public String getResidence() {
		return residence;
	}

	public void setResidence(String residence) {
		this.residence = residence;
	}

	public String getHeadImgUrl() {
		return headImgUrl;
	}

	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}

	public ArrayList<CreditTable> getTableItem() {
		return tableItem;
	}

	public void setTableItem(ArrayList<CreditTable> tableItem) {
		this.tableItem = tableItem;
	}

	public class CreditTable {

		/**
		 * 表格式样 101 数据源分析 102 风险排查、手机号分析、用户画像、消费能力、淘宝数据分析（不要）、淘宝消费情况、淘宝绑定支付宝信息 103
		 * 借贷数据分析 、 逾期记录 、 多头借贷 104 芝麻分认证、地址与收货人地址、社保分析、公积金分析、学信网分析 105 紧急联系人分析 106
		 * （新）紧急联系人 101 224:268:258 102 534:216 103 200:380:170 104 250:500 105
		 * 300:226:224 106 210:180:180:180
		 */
		protected int tableType;

		/**
		 * 标题
		 */
		protected String title;

		/**
		 * 副标题 (没有副标题的话，传空字符串)
		 */
		protected String subtitle;

		/**
		 * 是否存在说明 0：没有 1：有
		 */
		protected int explainType = 0;

		/**
		 * 是否存在说明内容
		 */
		protected String explainNote;

		/**
		 * 简单类型的表格数据，如果数组size大于1,, 说明是需要筛选的表格
		 */
		protected ArrayList<CreditTableBasicItem> tableItem;

		public int getExplainType() {
			return explainType;
		}

		public void setExplainType(int explainType) {
			this.explainType = explainType;
		}

		public String getExplainNote() {
			return explainNote;
		}

		public void setExplainNote(String explainNote) {
			this.explainNote = explainNote;
		}

		public int getTableType() {
			return tableType;
		}

		public void setTableType(int tableType) {
			this.tableType = tableType;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getSubtitle() {
			return subtitle;
		}

		public void setSubtitle(String subtitle) {
			this.subtitle = subtitle;
		}

		public ArrayList<CreditTableBasicItem> getTableItem() {
			return tableItem;
		}

		public void setTableItem(ArrayList<CreditTableBasicItem> tableItem) {
			this.tableItem = tableItem;
		}

	}

	public class CreditTableBasicItem {

		/**
		 * 表格筛选标题
		 */
		protected String tableFiltrateTitle;

		/**
		 * 表格头信息，如果没有串空字符串， 多个数据之间以^分割 e.g. 数据源^使用时长^权限收集时间
		 */
		protected String tableHeadData;

		/**
		 * 表格体数据 多个数据之间以^分割 e.g. 移动运营商^15^2018-0502
		 */
		protected ArrayList<String> tableBodyData;

		/**
		 * 特殊控制，如果没有特殊控制, 传空数组
		 */
		protected ArrayList<CreditTableSpecialHandle> specialHandle;

		public String getTableFiltrateTitle() {
			return tableFiltrateTitle;
		}

		public void setTableFiltrateTitle(String tableFiltrateTitle) {
			this.tableFiltrateTitle = tableFiltrateTitle;
		}

		public String getTableHeadData() {
			return tableHeadData;
		}

		public void setTableHeadData(String tableHeadData) {
			this.tableHeadData = tableHeadData;
		}

		public ArrayList<String> getTableBodyData() {
			return tableBodyData;
		}

		public void setTableBodyData(ArrayList<String> tableBodyData) {
			this.tableBodyData = tableBodyData;
		}

		public ArrayList<CreditTableSpecialHandle> getSpecialHandle() {
			return specialHandle;
		}

		public void setSpecialHandle(ArrayList<CreditTableSpecialHandle> specialHandle) {
			this.specialHandle = specialHandle;
		}

	}

	public class CreditTableSpecialHandle {

		/**
		 * 特殊控制类型 200 颜色控制 201 特殊跳转控制
		 */
		private int handleType;

		/**
		 * 多少行
		 */
		protected int rowNum;

		/**
		 * 多少列
		 */
		protected int columnNum;

		/**
		 * 控制参数 200 颜色控制 对应的为rgb色值 e.g. #ffff00 201 特殊跳转控制 对应跳转的详情页面 tag
		 */
		protected String handleParameter;

		public int getHandleType() {
			return handleType;
		}

		public void setHandleType(int handleType) {
			this.handleType = handleType;
		}

		public int getRowNum() {
			return rowNum;
		}

		public void setRowNum(int rowNum) {
			this.rowNum = rowNum;
		}

		public int getColumnNum() {
			return columnNum;
		}

		public void setColumnNum(int columnNum) {
			this.columnNum = columnNum;
		}

		public String getHandleParameter() {
			return handleParameter;
		}

		public void setHandleParameter(String handleParameter) {
			this.handleParameter = handleParameter;
		}
	}

}
