package com.jxf.web.model.wyjt.app.loan;

import java.util.ArrayList;
import java.util.List;


public class LoanDetailResponseResult   {
	
	/**
	 * 借据id
	 */
	private int loanId;
	/** 借款用途 */
	private Integer loanPurp;//0->资金周转,1->交房租,2->消费,3->还信用卡,4->报培训班,5->考驾照,6->其它		
	/** 借款金额 */
	private String amount;		
	/** 借款利率 */
	private String intRate;//不含%，如年化15.5%利率，只传15.5
	
	/** 证件号 */
	private String idNo; 
	
	/** 申请时间 */
	private String createDate;
	
	/** 还款时间 */
	private String dueRepayDate;	
	
	/** 剩余可借额度 */
	private Integer remainAmount;
	
	/** 账户可用余额 */
	private Integer availableAmount;
	
	/** 我发起的多人借款申请 的下面的列表 */
	private List<Detail> detailList = new ArrayList<Detail>(); 
	

	/** 借贷角色 
	 *  0->借款人，1->放款人 
	 */
	private Integer loanRole;

	/**
	 * 放款人真实姓名
	 */
	private String loanerName;

	/**
	 * 借款人真实姓名
	 */
	private String loaneeName;

	/** 借款利息 */
	private String interest; 

	/**
	 * 交易码(后台取record的id)
	 */
	private String tradeCode;

	/**
	 * 当前借据状态 1被拒绝; 2待收款; 3待对方确认; 4待录制视频; 5待重录视频; 6还款中; 7即将到期; 8明日还款; 9视频待审核;
	 * 10延期待同意; 11已超时; 12已还款待确认; 13已取消; 14已完成; 15已逾期;
	 * 16待确认; 17今日还款（2016-02-29新增）18 已拒绝 19 待确认收款(2016-04-07) 20 利息协商 21 部分还款待同意
	 */
	private int loanState;

	/**
	 * 如果底部有2个按钮 返回数据格式为 2|5 id中间以|分隔 |号前为left button 如果只有1个按钮， 则只返回 id 例如 8
	 * 
	 * buttonID 对应的图片分别为 100电话联系; 101发起新借款; 102拒绝收款; 103取消借款; 104去录制视频; 105确认收款;
	 * 106申请延期; 107提醒确认(底部只有一个确认按钮); 108提醒确认(底部有2个按钮，提醒确认为其中一个); 109提醒录制视频;
	 * 状态未完待补充
	 */
	private String bottomButtonID;

	/**
	 * 审核视频阅读文字
	 */
	private String videoReadStr;

	/**
	 * 视频审核url地址跳转
	 */
	private String videoReviewUrl;

	/**
	 * 联系电话
	 */
	private String phoneNumber;

	/**
	 * 聊天内容
	 */
	private List<LoanMessage> dialogContent;

	/**
	 * 是否显示更多信息
	 * 0不显示
	 * 1显示
	 */
	private Integer isShowMoreInfo = 0;
	/**
	 * 是否已使用友信宝催收服务
	 * 0未使用 1已使用
	 */
	private Integer isCollectioned = 0;

	/**
	 * 催收流程界面
	 */
	private String urgeRecordUrl = "";

	/**
	 * 左下角快速发送的模板列表
	 */
	private List<TempList> contentTempList;

	/**
	 * 是否显示修改利率
	 * 0不显示 1显示
	 */
	private Integer showRate;
	/**
	 * 是否可以输入文字聊天
	 * 0不可以 1可以
	 */
	private Integer canInput;
	/**
	 * 是否可以部分还款
	 * 0不可以 1可以
	 */
	private Integer canPartRepay;
	/**
	 * 逾期天数(根据当前还款时间计算)
	 * 该字段大于0时表示逾期
	 * 用于客户端部分还款区分
	 */
	private Integer overdueDays;

	/**
	 * 是否已使用仲裁服务
	 * 0未使用 1已使用
	 */
	private Integer isArbitration;

	/**
	 * 仲裁流程界面(点击仲裁进度)
	 */
	private String arbitrationRecordUrl;
	/**
	 * 0:未录制视频；1：待审核；2：审核未通过；3：审核通过；4：不需要录制视频或者借款单已关闭
	 */
	private int videoStatus;
	/**
	 * 进度     (还款中;FFF   5;FFF|日;还款)返回参数与列表相同
	 */
	private String progress;
	/**
	 * 部分还款状态，0：可以申请部分还款，n：提示  已申请部分还款n次，不能申请部分还款了
	 */
	private int partStatus = 0;
	/**
	 * 延期还款状态，0：可以申请延期还款，n：提示 已申请延期n次，不能申请延期
	 */
	private int delayStatus = 0;
	/**
	 * 电子借条显示按钮：0：不显示，1：显示 
	 */
	private int loanCenterStatus = 0;
	/**
	 * 消借条按钮，0：不显示，1：显示
	 */
	private int closeLoan = 0;
	
	
	public class LoanMessage{//俩人对话
		/**
		 * 对话框类型 1文字语言对话框; 2图片; 3申请延期 ; 4利息协商 5 输入文字 ;6 部分还款; 7 收取服务费 ; 8 放款人申请部分还款 ； 9：放款人申请延期
		 */
		private String dialogueMode;
	
		/**
		 * 时间戳
		 */
		private String time;
	
		/**
		 * 头像地址
		 */
		private String imgUrl;
	
		/**
		 * 姓名
		 */
		private String name;
	
		/**
		 * 位置显示 1左侧; 2右侧
		 */
		private String displayMode;
	
		/**
		 * 如果dialogueMode为1  对话内容
		 * 如果dialogueMode为2（图片对话框）, 那么代表服务器返回的对应图片编码
		 * 如果dialogueMode为3（申请延期）, 那么代表待还金额
		 * 如果dialogueMode为4 (利息协商),那么代表原利息
		 * 如果dialogueMode为5 (输入文字),那么代表输入文字内容
		 * 如果dialogueMode为6 (部分还款),应还本金
		 * 如果dialogueMode为7 (收取服务费),手续费金额
		 * 如果dialogueMode为8 (放款人申请部分还款),应还本金
		 * 如果dialogueMode为9 (放款人申请延期),应还本金
		 */
		private String var1;
		/**
		 * 如果dialogueMode为3（申请延期）, 那么代表延期补偿金额
		 * 如果dialogueMode为4 (利息协商),那么代表修改后利息
		 * 如果dialogueMode为6 (部分还款),总利息
		 * 如果dialogueMode为8 (放款人申请部分还款),总利息
		 * 如果dialogueMode为9 (放款人申请延期),总利息
		 */
		private String var2;
		/**
		 * 如果dialogueMode为3（申请延期）, 那么代表延至时间
		 *  如果dialogueMode为6 (部分还款),部分还款金额
		 *  如果dialogueMode为8 (放款人申请部分还款),部分还款金额
		 *  如果dialogueMode为9 (放款人申请延期),延期利息
		 */
		private String var3;
		/**
		 * 如果dialogueMode为3（申请延期）, 那么代表应还总额
		 *  如果dialogueMode为6 (部分还款),延期利息
		 *  如果dialogueMode为8 (放款人申请部分还款),延期利息
		 *  如果dialogueMode为9 (放款人申请延期),当前还款时间
		 */
		private String var4;
		/**
		 * 如果dialogueMode为3（申请延期）, 那么代表应还本金
		 *  如果dialogueMode为6 (部分还款),还款时间
		 *  如果dialogueMode为8 (放款人申请部分还款),总金额
		 *  如果dialogueMode为9 (放款人申请延期),延长后申请时间
		 */
		private String var5;
		/**
		 * 如果dialogueMode为3（申请延期）, 那么代表总利息
		 * 如果dialogueMode为8（放款人申请部分还款）, 还款时间
		 */
		private String var6;
		
		/**
		 * 部分/延期的当前利息（总利息）
		 */
		private  String var7;
		
	
		public String getVar7() {
			return var7;
		}
		public void setVar7(String var7) {
			this.var7 = var7;
		}
		public String getDialogueMode() {
			return dialogueMode;
		}
		public void setDialogueMode(String dialogueMode) {
			this.dialogueMode = dialogueMode;
		}
		public String getTime() {
			return time;
		}
		public void setTime(String time) {
			this.time = time;
		}
		public String getImgUrl() {
			return imgUrl;
		}
		public void setImgUrl(String imgUrl) {
			this.imgUrl = imgUrl;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getDisplayMode() {
			return displayMode;
		}
		public void setDisplayMode(String displayMode) {
			this.displayMode = displayMode;
		}
		public String getVar1() {
			return var1;
		}
		public void setVar1(String var1) {
			this.var1 = var1;
		}
		public String getVar2() {
			return var2;
		}
		public void setVar2(String var2) {
			this.var2 = var2;
		}
		public String getVar3() {
			return var3;
		}
		public void setVar3(String var3) {
			this.var3 = var3;
		}
	
		public String getVar4() {
			return var4;
		}
	
		public void setVar4(String var4) {
			this.var4 = var4;
		}
	
		public String getVar5() {
			return var5;
		}
	
		public void setVar5(String var5) {
			this.var5 = var5;
		}
	
		public String getVar6() {
			return var6;
		}
	
		public void setVar6(String var6) {
			this.var6 = var6;
		}
	}
	
	//左下角的快速发送消息模板
	public class TempList {

		/**
		 * 文字显示
		 */
		private String text;

		/**
		 * 文字编号
		 */
		private Integer textNum;

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

		public TempList(String text, Integer textNum) {
			super();
			this.text = text;
			this.textNum = textNum;
		}

		public TempList() {
		}
		public Integer getTextNum() {
			return textNum;
		}

		public void setTextNum(Integer textNum) {
			this.textNum = textNum;
		}
		

	}
	
	
	public Integer getLoanPurp() {
		return loanPurp;
	}
	public void setLoanPurp(Integer loanPurp) {
		this.loanPurp = loanPurp;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getIntRate() {
		return intRate;
	}
	public void setIntRate(String intRate) {
		this.intRate = intRate;
	}
	
	//多人借款时 下面的借条列表
   public class Detail {
    	
    	/**详情ID*/
    	private String detailId;
    	/**好友姓名*/
    	private String memberName;
    	/**好友头像*/
    	private String headImage;
    	/** 借款金额 */
    	private String amount;	
    	/** 借款状态 */
    	private Integer status;//0->待确认;1->已成功;2->已拒绝;3->已取消;4->已过期
    	
    	
		public String getMemberName() {
			return memberName;
		}
		public void setMemberName(String memberName) {
			this.memberName = memberName;
		}
		public String getHeadImage() {
			return headImage;
		}
		public void setHeadImage(String headImage) {
			this.headImage = headImage;
		}
		public String getAmount() {
			return amount;
		}
		public void setAmount(String amount) {
			this.amount = amount;
		}
		public Integer getStatus() {
			return status;
		}
		public void setStatus(Integer status) {
			this.status = status;
		}
		public String getDetailId() {
			return detailId;
		}
		public void setDetailId(String detailId) {
			this.detailId = detailId;
		}
		
    	
    }

	public String getInterest() {
		return interest;
	}
	public void setInterest(String interest) {
		this.interest = interest;
	}
	public String getIdNo() {
		return idNo;
	}
	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getDueRepayDate() {
		return dueRepayDate;
	}
	public void setDueRepayDate(String dueRepayDate) {
		this.dueRepayDate = dueRepayDate;
	}
	public String getProgress() {
		return progress;
	}
	public void setProgress(String progress) {
		this.progress = progress;
	}
	public Integer getAvailableAmount() {
		return availableAmount;
	}
	public void setAvailableAmount(Integer availableAmount) {
		this.availableAmount = availableAmount;
	}
	public Integer getRemainAmount() {
		return remainAmount;
	}
	public void setRemainAmount(Integer remainAmount) {
		this.remainAmount = remainAmount;
	}
	public List<Detail> getDetailList() {
		return detailList;
	}
	public void setDetailList(List<Detail> detailList) {
		this.detailList = detailList;
	}
	public int getLoanId() {
		return loanId;
	}
	public void setLoanId(int loanId) {
		this.loanId = loanId;
	}
	public Integer getLoanRole() {
		return loanRole;
	}
	public void setLoanRole(Integer loanRole) {
		this.loanRole = loanRole;
	}
	public String getLoanerName() {
		return loanerName;
	}
	public void setLoanerName(String loanerName) {
		this.loanerName = loanerName;
	}
	public String getLoaneeName() {
		return loaneeName;
	}
	public void setLoaneeName(String loaneeName) {
		this.loaneeName = loaneeName;
	}
	public String getTradeCode() {
		return tradeCode;
	}
	public void setTradeCode(String tradeCode) {
		this.tradeCode = tradeCode;
	}
	public int getLoanState() {
		return loanState;
	}
	public void setLoanState(int loanState) {
		this.loanState = loanState;
	}
	public String getBottomButtonID() {
		return bottomButtonID;
	}
	public void setBottomButtonID(String bottomButtonID) {
		this.bottomButtonID = bottomButtonID;
	}
	public String getVideoReadStr() {
		return videoReadStr;
	}
	public void setVideoReadStr(String videoReadStr) {
		this.videoReadStr = videoReadStr;
	}
	public String getVideoReviewUrl() {
		return videoReviewUrl;
	}
	public void setVideoReviewUrl(String videoReviewUrl) {
		this.videoReviewUrl = videoReviewUrl;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public List<LoanMessage> getDialogContent() {
		return dialogContent;
	}
	public void setDialogContent(List<LoanMessage> dialogContent) {
		this.dialogContent = dialogContent;
	}
	public Integer getIsShowMoreInfo() {
		return isShowMoreInfo;
	}
	public void setIsShowMoreInfo(Integer isShowMoreInfo) {
		this.isShowMoreInfo = isShowMoreInfo;
	}
	public Integer getIsCollectioned() {
		return isCollectioned;
	}
	public void setIsCollectioned(Integer isCollectioned) {
		this.isCollectioned = isCollectioned;
	}
	public String getUrgeRecordUrl() {
		return urgeRecordUrl;
	}
	public void setUrgeRecordUrl(String urgeRecordUrl) {
		this.urgeRecordUrl = urgeRecordUrl;
	}
	public List<TempList> getContentTempList() {
		return contentTempList;
	}
	public void setContentTempList(List<TempList> contentTempList) {
		this.contentTempList = contentTempList;
	}
	public Integer getShowRate() {
		return showRate;
	}
	public void setShowRate(Integer showRate) {
		this.showRate = showRate;
	}
	public Integer getCanInput() {
		return canInput;
	}
	public void setCanInput(Integer canInput) {
		this.canInput = canInput;
	}
	public Integer getCanPartRepay() {
		return canPartRepay;
	}
	public void setCanPartRepay(Integer canPartRepay) {
		this.canPartRepay = canPartRepay;
	}
	public Integer getOverdueDays() {
		return overdueDays;
	}
	public void setOverdueDays(Integer overdueDays) {
		this.overdueDays = overdueDays;
	}
	public Integer getIsArbitration() {
		return isArbitration;
	}
	public void setIsArbitration(Integer isArbitration) {
		this.isArbitration = isArbitration;
	}
	public String getArbitrationRecordUrl() {
		return arbitrationRecordUrl;
	}
	public void setArbitrationRecordUrl(String arbitrationRecordUrl) {
		this.arbitrationRecordUrl = arbitrationRecordUrl;
	}
	public int getVideoStatus() {
		return videoStatus;
	}
	public void setVideoStatus(int videoStatus) {
		this.videoStatus = videoStatus;
	}
	public int getPartStatus() {
		return partStatus;
	}
	public void setPartStatus(int partStatus) {
		this.partStatus = partStatus;
	}
	public int getDelayStatus() {
		return delayStatus;
	}
	public void setDelayStatus(int delayStatus) {
		this.delayStatus = delayStatus;
	}
	public int getLoanCenterStatus() {
		return loanCenterStatus;
	}
	public void setLoanCenterStatus(int loanCenterStatus) {
		this.loanCenterStatus = loanCenterStatus;
	}
	public int getCloseLoan() {
		return closeLoan;
	}
	public void setCloseLoan(int closeLoan) {
		this.closeLoan = closeLoan;
	}
	
}
