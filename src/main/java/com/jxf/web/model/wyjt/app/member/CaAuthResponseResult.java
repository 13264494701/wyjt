package com.jxf.web.model.wyjt.app.member;

import java.util.ArrayList;
import java.util.List;


/**
 * 
 * @author Administrator
   *  我的信用报告授权认证返回类
 */
public class CaAuthResponseResult {
	

	/**
	 *  0：收费一元的信用档案(未申请)   1：收费一元的信用档案(可查看)
	 */
	private Integer pageType;
	/**
	 * 统计数量
	 */
	private String counNum;
	
	private String idNo;
	
	private List<UserBriefLegalize> userBriefLegalize = new ArrayList<UserBriefLegalize>();
	
	private String caReportUrl;//查看全部数据
	
	private String shareReportUrl;// 分享链接
	
	public String getCounNum() {
		return counNum;
	}

	public void setCounNum(String counNum) {
		this.counNum = counNum;
	}

	public Integer getPageType() {
		return pageType;
	}

	public void setPageType(Integer pageType) {
		this.pageType = pageType;
	}

	public List<UserBriefLegalize> getUserBriefLegalize() {
		return userBriefLegalize;
	}

	public void setUserBriefLegalize(List<UserBriefLegalize> userBriefLegalize) {
		this.userBriefLegalize = userBriefLegalize;
	}
	
	
	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}


	public String getCaReportUrl() {
		return caReportUrl;
	}

	public void setCaReportUrl(String caReportUrl) {
		this.caReportUrl = caReportUrl;
	}


	public String getShareReportUrl() {
		return shareReportUrl;
	}

	public void setShareReportUrl(String shareReportUrl) {
		this.shareReportUrl = shareReportUrl;
	}


	public class UserBriefLegalize {
		/**
		 * 是否认证 0:未认证，1：已认证， 2已过期  3 认证中  4 认证失败
		 */
		private int status = 0;
		/***
		 * 	能否点击认证 0可以 1 不可以
		 */
		private int toAuth;
		/**
		 * 	认证吐司
		 */
		private String authMessage;
		/**
		 * 图片地址
		 */
		private String image;
		/**
		 * 认证名称
		 */
		private String name;
		/**
		 * 认证日期
		 */
		private String date = "未评估";
		/**
		 * 查看认证地址
		 */
		private String url;
		/**
		 * xinyong:信用记录 taobao：淘宝 yunying：运营商 zhima：芝麻 shebao 社保 gongji 公积金
		 * xuexin学信网
		 */

		private String jumpStatus;

		/**
		 * 0:无页面跳转 1：h5跳转第三方页面  2：跳转淘宝的SDk（数据魔盒） 3: 客户端本地（目前仅芝麻分）
		 * 4：（点击好友的跳转到相对数据展示界面）
		 */
		private int authenType;

		// 1信用记录 2 淘宝 3运营商 4芝麻分 5学信网 6 社保 7公积金  8网银
		private int pageSatus=0;
		
		/**
		 * 是否显示new的图标 0：不显示  1显示
		 */
		private int newStatus=0;
		
		/** 第三方类型 0数据魔盒1公信宝2天机 */
		private int thirdApplyType;
		
		private String singleCaReportUrl;//单个查询

		public int getNewStatus() {
			return newStatus;
		}

		public void setNewStatus(int newStatus) {
			this.newStatus = newStatus;
		}

		public int getPageSatus() {
			return pageSatus;
		}

		public void setPageSatus(int pageSatus) {
			this.pageSatus = pageSatus;
		}

		public int getAuthenType() {
			return authenType;
		}

		public void setAuthenType(int authenType) {
			this.authenType = authenType;
		}

		public String getJumpStatus() {
			return jumpStatus;
		}

		public void setJumpStatus(String jumpStatus) {
			this.jumpStatus = jumpStatus;
		}

		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
		}

		public String getImage() {
			return image;
		}

		public void setImage(String image) {
			this.image = image;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getDate() {
			return date;
		}

		public void setDate(String date) {
			this.date = date;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public int getThirdApplyType() {
			return thirdApplyType;
		}

		public void setThirdApplyType(int thirdApplyType) {
			this.thirdApplyType = thirdApplyType;
		}

		public int getToAuth() {
			return toAuth;
		}

		public void setToAuth(int toAuth) {
			this.toAuth = toAuth;
		}

		public String getAuthMessage() {
			return authMessage;
		}

		public void setAuthMessage(String authMessage) {
			this.authMessage = authMessage;
		}

		public String getSingleCaReportUrl() {
			return singleCaReportUrl;
		}

		public void setSingleCaReportUrl(String singleCaReportUrl) {
			this.singleCaReportUrl = singleCaReportUrl;
		}

	}


}
