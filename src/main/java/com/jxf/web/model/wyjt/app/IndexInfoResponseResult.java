package com.jxf.web.model.wyjt.app;

import java.util.ArrayList;
import java.util.List;

import com.jxf.web.model.AuditPage;

/**
 * @作者: wo
 * @创建时间 :2018年12月18日 上午9:12:09
 * @功能说明:
 */
public class IndexInfoResponseResult{

	/** 图标列表 */
	private List<Icon> iconList = new ArrayList<Icon>();
	/** 跑马灯列表 */
	private List<Marquee> marqueeList = new ArrayList<Marquee>();
	/** banner列表 */
	private List<Ad> bannerList = new ArrayList<Ad>();

	private Ad adPositionThree;//急速申请(左)
	
	private Ad adPositionFour;//债转中心(右)
	
	/** 案例  为了线上不报错 将来可以删掉 TODO*/
	private List<ArbitrationCase> arbitrationCaseList = new ArrayList<ArbitrationCase>();
	
	private String showThirdLogin;//0->不显示；1->显示
	
	private String showFlag;//0->不显示；1->显示 showFlag  是控制首页成功案例的图片的 显示申请时间还是申请人的 重复了 将来去掉 TODO
	
	private String directLoginFlag;//0->不显示；1->显示 控制一键登录按钮显示 

	/** 审核用的AB页 */
	private AuditPage auditPage;
	
	public String getShowThirdLogin() {
		return showThirdLogin;
	}

	public void setShowThirdLogin(String showThirdLogin) {
		this.showThirdLogin = showThirdLogin;
	}
	
	//为了线上不出错 将来可以删掉 start TODO
	public List<ArbitrationCase> getArbitrationCaseList() {
		return arbitrationCaseList;
	}

	public void setArbitrationCaseList(List<ArbitrationCase> arbitrationCaseList) {
		this.arbitrationCaseList = arbitrationCaseList;
	}

	public class ArbitrationCase{
		
		/** 申请人 */
		private String name;
		/** 借条金额 */
		private String amount;
		/** 逾期天数 */
		private Integer daysOverdue;
		/** 借条状态 */
		private String status;
		
		public String getAmount() {
			return amount;
		}
		public void setAmount(String amount) {
			this.amount = amount;
		}
		public Integer getDaysOverdue() {
			return daysOverdue;
		}
		public void setDaysOverdue(Integer daysOverdue) {
			this.daysOverdue = daysOverdue;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		
	}
	// end
	
	public String getShowFlag() {
		return showFlag;
	}

	public void setShowFlag(String showFlag) {
		this.showFlag = showFlag;
	}
	public List<Icon> getIconList() {
		return iconList;
	}

	public void setIconList(List<Icon> iconList) {
		this.iconList = iconList;
	}
	
	public List<Marquee> getMarqueeList() {
		return marqueeList;
	}

	public void setMarqueeList(List<Marquee> marqueeList) {
		this.marqueeList = marqueeList;
	}
	
	public List<Ad> getBannerList() {
		return bannerList;
	}

	public void setBannerList(List<Ad> bannerList) {
		this.bannerList = bannerList;
	}
	

	public class Icon{
		
		/** 图标位编号 */
		private String positionNo;//四位编码，2位页面编码+2位序列号,例如定义0101 为首页的第一个图标,页面内从左到右，从上到下排序
		/** 图标名称 */
		private String name;
		/** 图标图片 */
		private String image;
		/** 跳转类型 */
		private Integer redirecType;//0->内部,1->外部
		/** 跳转链接 */
		private String redirectUrl;//如不需要跳转传#
		
		public String getPositionNo() {
			return positionNo;
		}
		public void setPositionNo(String positionNo) {
			this.positionNo = positionNo;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getImage() {
			return image;
		}
		public void setImage(String image) {
			this.image = image;
		}
		public Integer getRedirecType() {
			return redirecType;
		}
		public void setRedirecType(Integer redirecType) {
			this.redirecType = redirecType;
		}
		public String getRedirectUrl() {
			return redirectUrl;
		}
		public void setRedirectUrl(String redirectUrl) {
			this.redirectUrl = redirectUrl;
		}
		
	}
	
	public class Marquee{
		
		/** 图片 */
		private String title;
		/** 跳转链接 */
		private String redirectUrl;//如不需要跳转传#
		
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getRedirectUrl() {
			return redirectUrl;
		}
		public void setRedirectUrl(String redirectUrl) {
			this.redirectUrl = redirectUrl;
		}
		
	}
	
	public class Ad{
		
		/** 图片 */
		private String image;
		/** 跳转类型 */
		private Integer redirecType;//0->内部浏览器,1->外部浏览器 2->原生页面跳转 ,3->第三方链接 
		/** 跳转链接 */
		private String redirectUrl;//如不需要跳转传#
		
		public String getImage() {
			return image;
		}
		public void setImage(String image) {
			this.image = image;
		}
		public Integer getRedirecType() {
			return redirecType;
		}
		public void setRedirecType(Integer redirecType) {
			this.redirecType = redirecType;
		}
		public String getRedirectUrl() {
			return redirectUrl;
		}
		public void setRedirectUrl(String redirectUrl) {
			this.redirectUrl = redirectUrl;
		}

		
	}
	
	public AuditPage getAuditPage() {
		return auditPage;
	}

	public void setAuditPage(AuditPage auditPage) {
		this.auditPage = auditPage;
	}

	public String getDirectLoginFlag() {
		return directLoginFlag;
	}

	public void setDirectLoginFlag(String directLoginFlag) {
		this.directLoginFlag = directLoginFlag;
	}

	public Ad getAdPositionThree() {
		return adPositionThree;
	}

	public void setAdPositionThree(Ad adPositionThree) {
		this.adPositionThree = adPositionThree;
	}

	public Ad getAdPositionFour() {
		return adPositionFour;
	}

	public void setAdPositionFour(Ad adPositionFour) {
		this.adPositionFour = adPositionFour;
	}
	
}
