package com.jxf.web.model;

import java.util.ArrayList;
import java.util.List;

import com.jxf.svc.config.Global;


/**
 * @作者: xiaorongdian
 * @创建时间 :2019年3月6日 上午10:22:58
 * @功能说明:审核后页面 有修改父类子类都修改  AuditPage AuditBeforePage
 */
public class AuditAfterPage extends AuditPage{

	/** 首页右上角 逾期解决/催收仲裁*/
	protected String index1001 = "催收仲裁";
	
	/** 首页大按钮 逾期解决/催收仲裁*/
	protected Integer index1002 = 1;
	
	/** 首页案例标题1 申请时间/申请人*/
	protected String index1003 = "申请人";
	
	/** 首页案例标题2 逾期笔数/逾期天数*/
	protected String index1004 = "逾期天数";
	
	/** 首页下方案例 */
	protected List<ArbitrationCase> index1005 = getIndex1005();
	
	/** 首页案例点击能否跳转 0否 1可以 */
	protected Integer index1006 = 1;
	
	/** 逾期解决/催收仲裁 */
	protected String collection1101 = "催收仲裁";
	
	/** 电话提醒/合法催收 */
	protected String collection1102 = "合法催收";
	
	/** 一键裁决/法律仲裁 */
	protected String collection1103 = "法律仲裁";
	
	/** 更快捷,更专业/举证出庭 */
	protected String collection1104 = "举证出庭、寻找法律裁决的问题交给我|#898989";
	
	/** 更快捷,更专业 */
	protected String collection1105 = "更快捷、更专业、与诉讼同等效力|#DF3030";
	
	/** 待提醒/待催收 */
	protected String collection1301 = "待催收";
	
	/** 提醒中/催收中 */
	protected String collection1302 = "催收中";
	
	/** 提醒结束/催收结束 */
	protected String collection1303 = "催收结束";
	
	/** 电话提醒/专业催收 */
	protected String collection1304 = "专业催收";
	
	/** 待裁决/待仲裁 */
	protected String collection1401 = "待仲裁";
	
	/** 裁决中/仲裁中 */
	protected String collection1402 = "仲裁中";
	
	/** 已出裁决/仲裁出裁决 */
	protected String collection1403 = "仲裁出裁决";
	
	/** 裁决已完结/仲裁已完结 */
	protected String collection1404 = "仲裁已完结";
	
	/** 一键裁决/法律仲裁 */
	protected String collection1405 = "法律仲裁";
	
	
	/** 点击好友是否可以跳转 0否 1可以*/
	protected Integer friend2001 = 1;
	
	/** 是否显示信用报告/贷款超市 0否 1可以*/
	protected Integer mine3001 = 1;
	/** 是否显示债转中心 0否 1可以*/
	protected Integer mine3002 = 1;
	
	/** 是否显示借条中心 0否 1可以*/
	protected Integer mine3003 = 1;
	
	/** 是否显示信用检测 0否 1可以*/
	protected Integer mine3004 = 1;
	
	/** 借条中心*/
	protected String mineName3001 = "借条中心";
	
	/** 信用报告*/
	protected String mineName3002 = "信用报告";
	
	/** 债转中心*/
	protected String mineName3003 = "债转中心";
	
	/** 贷款超市*/
	protected String mineName3004 = "贷款超市";
	
	/** 信用检测*/
	protected String mineName3005 = "信用检测";
	
	/** 信用检测h5地址*/
	protected String creditH5 = "https://m.tianxiaxinyong.cn/cooperation/crp-v2/per-detail.html?channel=Z3J2QUp2M3ltVlVuRjR5QjRrR0RWdz09";
	
	/** 预留1位置 0否 1可以*/
	protected Integer mine3005;
	
	/** 预留1位置 */
	protected String mineName3006;
	
	/** 预留1位置  h5地址*/
	protected String otherH5;
	
	/** 借条中心  图标地址*/
	protected String centerImage = Global.getConfig("domain")+Global.getConfig("memberInfo.center");
	
	/** 信用档案  图标地址*/
	protected String creditImage = Global.getConfig("domain")+Global.getConfig("memberInfo.credit");
	
	/** 信用检测  图标地址*/
	protected String detectionImage = Global.getConfig("domain")+Global.getConfig("memberInfo.detection");
	
	/** 债转中心  图标地址*/
	protected String loanTransferImage = Global.getConfig("domain")+Global.getConfig("memberInfo.loanTransfer");
	
	/** 贷款超市  图标地址*/
	protected String marketImage = Global.getConfig("domain")+Global.getConfig("memberInfo.market");
	
	/** 预留1位置 图标地址*/
	protected String otherImage = "";
	
	/** 一键裁决/法律仲裁*/
	protected String trx4001 = "法律仲裁";
	
	/** 电话提醒/专业催收*/
	protected String trx4002 = "专业催收";
	
	public List<ArbitrationCase> getIndex1005() {
		List<ArbitrationCase> list = new ArrayList<ArbitrationCase>();
		ArbitrationCase arbitrationCase = new AuditPage().new ArbitrationCase();
		arbitrationCase.setName("姚**");
		arbitrationCase.setAmount("5450元");
		arbitrationCase.setDaysOverdue("47");
		arbitrationCase.setStatus("仲裁出裁决");
		list.add(arbitrationCase);
		
		arbitrationCase = new AuditPage().new ArbitrationCase();
		arbitrationCase.setName("李**");
		arbitrationCase.setAmount("2100元");
		arbitrationCase.setDaysOverdue("4");
		arbitrationCase.setStatus("仲裁出裁决");
		list.add(arbitrationCase);
		
		arbitrationCase = new AuditPage().new ArbitrationCase();
		arbitrationCase.setName("郝**");
		arbitrationCase.setAmount("1600元");
		arbitrationCase.setDaysOverdue("12");
		arbitrationCase.setStatus("仲裁出裁决");
		list.add(arbitrationCase);
		
		arbitrationCase = new AuditPage().new ArbitrationCase();
		arbitrationCase.setName("刘**");
		arbitrationCase.setAmount("1507元");
		arbitrationCase.setDaysOverdue("7");
		arbitrationCase.setStatus("仲裁出裁决");
		list.add(arbitrationCase);
		
		return list;
	}

	public String getIndex1001() {
		return index1001;
	}

	public void setIndex1001(String index1001) {
		this.index1001 = index1001;
	}

	public String getIndex1003() {
		return index1003;
	}

	public void setIndex1003(String index1003) {
		this.index1003 = index1003;
	}

	public String getIndex1004() {
		return index1004;
	}

	public void setIndex1004(String index1004) {
		this.index1004 = index1004;
	}

	public Integer getIndex1006() {
		return index1006;
	}

	public void setIndex1006(Integer index1006) {
		this.index1006 = index1006;
	}

	public String getCollection1101() {
		return collection1101;
	}

	public void setCollection1101(String collection1101) {
		this.collection1101 = collection1101;
	}

	public String getCollection1102() {
		return collection1102;
	}

	public void setCollection1102(String collection1102) {
		this.collection1102 = collection1102;
	}

	public String getCollection1103() {
		return collection1103;
	}

	public void setCollection1103(String collection1103) {
		this.collection1103 = collection1103;
	}

	public String getCollection1104() {
		return collection1104;
	}

	public void setCollection1104(String collection1104) {
		this.collection1104 = collection1104;
	}

	public String getCollection1105() {
		return collection1105;
	}

	public void setCollection1105(String collection1105) {
		this.collection1105 = collection1105;
	}

	public String getCollection1301() {
		return collection1301;
	}

	public void setCollection1301(String collection1301) {
		this.collection1301 = collection1301;
	}

	public String getCollection1302() {
		return collection1302;
	}

	public void setCollection1302(String collection1302) {
		this.collection1302 = collection1302;
	}

	public String getCollection1303() {
		return collection1303;
	}

	public void setCollection1303(String collection1303) {
		this.collection1303 = collection1303;
	}

	public String getCollection1304() {
		return collection1304;
	}

	public void setCollection1304(String collection1304) {
		this.collection1304 = collection1304;
	}

	public String getCollection1401() {
		return collection1401;
	}

	public void setCollection1401(String collection1401) {
		this.collection1401 = collection1401;
	}

	public String getCollection1402() {
		return collection1402;
	}

	public void setCollection1402(String collection1402) {
		this.collection1402 = collection1402;
	}

	public String getCollection1403() {
		return collection1403;
	}

	public void setCollection1403(String collection1403) {
		this.collection1403 = collection1403;
	}

	public String getCollection1404() {
		return collection1404;
	}

	public void setCollection1404(String collection1404) {
		this.collection1404 = collection1404;
	}

	public String getCollection1405() {
		return collection1405;
	}

	public void setCollection1405(String collection1405) {
		this.collection1405 = collection1405;
	}

	public Integer getFriend2001() {
		return friend2001;
	}

	public void setFriend2001(Integer friend2001) {
		this.friend2001 = friend2001;
	}

	public Integer getMine3001() {
		return mine3001;
	}

	public void setMine3001(Integer mine3001) {
		this.mine3001 = mine3001;
	}

	public String getTrx4001() {
		return trx4001;
	}

	public void setTrx4001(String trx4001) {
		this.trx4001 = trx4001;
	}

	public String getTrx4002() {
		return trx4002;
	}

	public void setTrx4002(String trx4002) {
		this.trx4002 = trx4002;
	}

	public void setIndex1005(List<ArbitrationCase> index1005) {
		this.index1005 = index1005;
	}

	public Integer getIndex1002() {
		return index1002;
	}

	public void setIndex1002(Integer index1002) {
		this.index1002 = index1002;
	}

	public Integer getMine3002() {
		return mine3002;
	}

	public void setMine3002(Integer mine3002) {
		this.mine3002 = mine3002;
	}

	public Integer getMine3003() {
		return mine3003;
	}

	public void setMine3003(Integer mine3003) {
		this.mine3003 = mine3003;
	}

	public Integer getMine3004() {
		return mine3004;
	}

	public void setMine3004(Integer mine3004) {
		this.mine3004 = mine3004;
	}

	public String getMineName3001() {
		return mineName3001;
	}

	public void setMineName3001(String mineName3001) {
		this.mineName3001 = mineName3001;
	}

	public String getMineName3002() {
		return mineName3002;
	}

	public void setMineName3002(String mineName3002) {
		this.mineName3002 = mineName3002;
	}

	public String getMineName3003() {
		return mineName3003;
	}

	public void setMineName3003(String mineName3003) {
		this.mineName3003 = mineName3003;
	}

	public String getMineName3004() {
		return mineName3004;
	}

	public void setMineName3004(String mineName3004) {
		this.mineName3004 = mineName3004;
	}

	public String getMineName3005() {
		return mineName3005;
	}

	public void setMineName3005(String mineName3005) {
		this.mineName3005 = mineName3005;
	}

	public String getCreditH5() {
		return creditH5;
	}

	public void setCreditH5(String creditH5) {
		this.creditH5 = creditH5;
	}

	public Integer getMine3005() {
		return mine3005;
	}

	public void setMine3005(Integer mine3005) {
		this.mine3005 = mine3005;
	}

	public String getMineName3006() {
		return mineName3006;
	}

	public void setMineName3006(String mineName3006) {
		this.mineName3006 = mineName3006;
	}

	public String getOtherH5() {
		return otherH5;
	}

	public void setOtherH5(String otherH5) {
		this.otherH5 = otherH5;
	}

	public String getCenterImage() {
		return centerImage;
	}

	public void setCenterImage(String centerImage) {
		this.centerImage = centerImage;
	}

	public String getCreditImage() {
		return creditImage;
	}

	public void setCreditImage(String creditImage) {
		this.creditImage = creditImage;
	}

	public String getLoanTransferImage() {
		return loanTransferImage;
	}

	public void setLoanTransferImage(String loanTransferImage) {
		this.loanTransferImage = loanTransferImage;
	}

	public String getMarketImage() {
		return marketImage;
	}

	public void setMarketImage(String marketImage) {
		this.marketImage = marketImage;
	}

	public String getDetectionImage() {
		return detectionImage;
	}

	public void setDetectionImage(String detectionImage) {
		this.detectionImage = detectionImage;
	}

	public String getOtherImage() {
		return otherImage;
	}

	public void setOtherImage(String otherImage) {
		this.otherImage = otherImage;
	}
	
	
}
