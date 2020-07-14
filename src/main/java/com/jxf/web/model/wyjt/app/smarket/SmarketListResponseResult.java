package com.jxf.web.model.wyjt.app.smarket;

import java.util.ArrayList;
import java.util.List;



public class SmarketListResponseResult {

	/** 市场列表 */
	private List<Market> marketList = new ArrayList<Market>();

	public List<Market> getMarketList() {
		return marketList;
	}
	public void setMarketList(List<Market> marketList) {
		this.marketList = marketList;
	}



	
	public class Market {
	
		/** 市场ID */
		private String marketId;	
		/** 贷超名称 */
		private String name;		
		/** 图标 */
		private String logo;	
		/** 最小借款金额 */
		private String minLoanAmt;		
		/** 最大借款金额 */
		private String maxLoanAmt;	
		
		/** 展示已放款笔数 */
		private String displayLoanQuantity;

		public String getMarketId() {
			return marketId;
		}

		public void setMarketId(String marketId) {
			this.marketId = marketId;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getLogo() {
			return logo;
		}

		public void setLogo(String logo) {
			this.logo = logo;
		}

		public String getMinLoanAmt() {
			return minLoanAmt;
		}

		public void setMinLoanAmt(String minLoanAmt) {
			this.minLoanAmt = minLoanAmt;
		}

		public String getMaxLoanAmt() {
			return maxLoanAmt;
		}

		public void setMaxLoanAmt(String maxLoanAmt) {
			this.maxLoanAmt = maxLoanAmt;
		}

		public String getDisplayLoanQuantity() {
			return displayLoanQuantity;
		}

		public void setDisplayLoanQuantity(String displayLoanQuantity) {
			this.displayLoanQuantity = displayLoanQuantity;
		}	
		
		
	}






}