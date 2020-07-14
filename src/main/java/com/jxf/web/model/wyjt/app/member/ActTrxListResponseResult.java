package com.jxf.web.model.wyjt.app.member;

import java.util.ArrayList;
import java.util.List;

/**
 * @作者: xiaorongdian
 * @创建时间 :2018年11月6日 下午5:54:29
 * @功能说明:账户流水列表
 */
public class ActTrxListResponseResult {

	/** 流水列表 */
	private List<Trx> trxList = new ArrayList<Trx>();
	
	/** 展示时间 */
	private String time;
	/** 开始时间 */
	private String startTime;
	/** 结束时间 */
	private String endTime;
	
	/** 总收入 */
	private String totalIncome = "0";
	
	/** 总支出 */
	private String totalExpenditure = "0";
	
	public List<Trx> getTrxList() {
		return trxList;
	}

	public void setTrxList(List<Trx> trxList) {
		this.trxList = trxList;
	}
	
	public class Trx {
		/** 流水编号 */
		private String trxId;

		/** 标题 */
		private String title;

		/** 交易时间 */
		private String trxTime;

		/** 方向 0出账 1入账 */
		private Integer drc;

		/** 交易金额 */
		private String amount;
		
		/** 图标 */
		private String imgUrl;

		public String getTrxId() {
			return trxId;
		}

		public void setTrxId(String trxId) {
			this.trxId = trxId;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public Integer getDrc() {
			return drc;
		}

		public void setDrc(Integer drc) {
			this.drc = drc;
		}

		public String getAmount() {
			return amount;
		}

		public void setAmount(String amount) {
			this.amount = amount;
		}

		public String getTrxTime() {
			return trxTime;
		}

		public void setTrxTime(String trxTime) {
			this.trxTime = trxTime;
		}

		public String getImgUrl() {
			return imgUrl;
		}

		public void setImgUrl(String imgUrl) {
			this.imgUrl = imgUrl;
		}
	}

	public String getTotalIncome() {
		return totalIncome;
	}

	public void setTotalIncome(String totalIncome) {
		this.totalIncome = totalIncome;
	}

	public String getTotalExpenditure() {
		return totalExpenditure;
	}

	public void setTotalExpenditure(String totalExpenditure) {
		this.totalExpenditure = totalExpenditure;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

}
