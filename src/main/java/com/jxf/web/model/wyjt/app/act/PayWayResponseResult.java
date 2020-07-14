package com.jxf.web.model.wyjt.app.act;

import java.util.List;

/**
 * 获取充值支付方式返回参数实体类
 * @author suhuimin
 *
 */
public class PayWayResponseResult  {
	
	/** 支付方式集合 */
	private List<PayWay> payWays;
	/** 提示语 */
	private String tips;
	
	public class PayWay{
		
		/** 支付方式 */
		private String method;
		/** 名称 */
		private String name;
		/** logo */
		private String logo;
		/** 说明 */
		private String explain;
		
		public String getMethod() {
			return method;
		}
		public void setMethod(String method) {
			this.method = method;
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
		public String getExplain() {
			return explain;
		}
		public void setExplain(String explain) {
			this.explain = explain;
		}
	}

	public List<PayWay> getPayWays() {
		return payWays;
	}

	public void setPayWays(List<PayWay> payWays) {
		this.payWays = payWays;
	}

	public String getTips() {
		return tips;
	}

	public void setTips(String tips) {
		this.tips = tips;
	}
	
}
