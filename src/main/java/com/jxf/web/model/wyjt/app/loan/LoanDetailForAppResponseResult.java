package com.jxf.web.model.wyjt.app.loan;

import java.util.ArrayList;
import java.util.List;


public class LoanDetailForAppResponseResult   {
	
	/**
	 * 借据id
	 */
	private String loanId;
	
	/** 借条类型 0apply 1detail 2record*/
	private Integer type;
	
	//前2个参数也用来给前端刷新页面用
	
	/**
	 * 借贷人id
	 */
	private String friendId;
	/** 借款用途 */
	private String loanPurp;	
	/** 借款金额 */
	private String amount;		
	/** 借款利率 */
	private String intRate;//不含%，如年化15.5%利率，只传15.5
	
	/** 证件号 */
	private String idNo; 
	
	/** 申请时间 yyyy-MM-dd */
	private String createDate;
	
	/** 还款时间 yyyy-MM-dd */
	private String dueRepayDate;	
	/** 结清时间 yyyy-MM-dd 债转后对方已还款后成功页面使用 */
	private String completeDate;	
	/** 当前用户是放款人0不是 1是 */
	private Integer isLoaner;
	/** 多人申请剩余可借额度 */
	private String remainAmount;
	/** 多人申请已借金额 */
	private String hadBorrowAmount;
	
	/** 账户可用余额 */
	private String availableAmount;
	
	/** 我发起的多人借款申请 的下面的列表 */
	private List<Detail> detailList = new ArrayList<Detail>(); 
	

	/**
	 * 借贷人真实姓名
	 */
	private String realName;
	/**
	 * 借款人头像  多人我收到的时使用
	 */
	private String loaneeHeadImage;

	/** 借款利息 */
	private String interest; 

	/**
	 * 交易码(后台取record的id,没有record取detailId)
	 */
	private String tradeCode;

	/**
	 * 当前借据状态    1被拒绝;2待收款;3待对方确认;6还款中;14已完成; 15已逾期;16待确认;18 已拒绝
	 */
	private Integer loanStatus;

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
	 * 1显示 更多信息 按钮
	 * 2显示 信用查询 按钮 
	 */
	private Integer showButton = 0;
	/**
	 * 是否显示债转标记
	 * 0不显示
	 * 1显示  
	 */
	private Integer showAuction = 0;
	/**
	 * 是否显示债权人
	 * 0不显示
	 * 1显示  
	 */
	private Integer hasAuctioned = 0;
	/**
	 * 新债权人
	 */
	private String  nowCreditor = "";
	
	/**
	 * 是否显示分期计划
	 * 0不显示
	 * 1显示
	 */
	private Integer showPlanButton = 0;
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
	private Integer showModifyRate;
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
	private Integer overdueDays = 0;

	/**
	 * 是否已使用仲裁服务
	 * 0未使用 1已使用
	 */
	private Integer isArbitration = 0;

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
	 * 部分还款状态，0 未超过5次,1 已申请部分还款5次，不能申请部分还款了
	 */
	private int partStatus = 0;
	/**
	 * 延期还款状态，0 未超过5次,1 提示 已申请延期5次，不能申请延期
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
		 * 对话框类型 1文字语言对话框; 2图片; 3申请延期 ; 4利息协商 ;6 部分还款; 7 收取服务费 ; 8 放款人申请部分还款 ； 9：放款人申请延期 10：放款人转让成功
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
		 * 发对话的会员Id
		 */
		private String memberId;
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
		 * 如果dialogueMode为3（申请延期）, 那么代应还本金
		 * 如果dialogueMode为4 (利息协商),那么代表原利息
		 * 如果dialogueMode为1 (输入文字),那么代表输入文字内容
		 * 如果dialogueMode为6 (部分还款),应还本金
		 * 如果dialogueMode为7 (收取服务费),手续费金额
		 * 如果dialogueMode为8 (放款人申请部分还款),应还本金
		 * 如果dialogueMode为9 (放款人申请延期),应还本金
		 * 如果dialogueMode为10 (债转成功),新债权人姓名
		 */
		private String var1;
		/**
		 * 如果dialogueMode为3（申请延期）, 那么代表延期利息
		 * 如果dialogueMode为4 (利息协商),那么代表修改后利息
		 * 如果dialogueMode为6 (部分还款),原利息
		 * 如果dialogueMode为8 (放款人申请部分还款),原利息
		 * 如果dialogueMode为9 (放款人申请延期),当前利息
		 * * 如果dialogueMode为10 (债转成功),新债权人电话
		 */
		private String var2;
		/**
		 * 如果dialogueMode为3（申请延期）, 那么代表延至时间
		 *  如果dialogueMode为6 (部分还款),部分还款金额
		 *  如果dialogueMode为8 (放款人申请部分还款),部分还款金额
		 *  如果dialogueMode为9 (放款人申请延期),延期利息
		 *  * 如果dialogueMode为10 (债转成功),新债权人身份证
		 */
		private String var3;
		/**
		 * 如果dialogueMode为3（申请延期）, 那么代表应还总额
		 * 如果dialogueMode为6（部分还款）, 那么代表延期利息
		 * 如果dialogueMode为8 (放款人申请部分还款),延期利息
		 *  如果dialogueMode为9 (放款人申请延期),当前还款时间
		 * 如果dialogueMode为10 (债转成功),转让成功时间
		 */
		private String var4;
		/**
		 * 如果dialogueMode为3（申请延期）, 那么代表应还本金
		 *  如果dialogueMode为6 (部分还款),还款时间
		 *  如果dialogueMode为8 (放款人申请部分还款),确认后应还总额
		 *  如果dialogueMode为9 (放款人申请延期),延长后申请时间
		 */
		private String var5;
		/**
		 * 如果dialogueMode为3（申请延期）, 那么代表原利息
		 * 如果dialogueMode为8（放款人申请部分还款）, 还款时间
		 */
		private String var6;
		
		/**
		 * 如果dialogueMode为3（申请延期）, 那么代表延期利息
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
		public String getMemberId() {
			return memberId;
		}
		public void setMemberId(String memberId) {
			this.memberId = memberId;
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
    	private String loanId;
    	/**类型 1detail 2record 跳转详情时传给服务器*/
    	private Integer type;
    	/**好友姓名*/
    	private String memberName;
    	/**好友头像*/
    	private String headImage;
    	/** 借到金额 */
    	private String amount;	
    	/** 借款进度 */
    	private String progress;
    	
    	
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
		public String getLoanId() {
			return loanId;
		}
		public void setLoanId(String loanId) {
			this.loanId = loanId;
		}
		public Integer getType() {
			return type;
		}
		public void setType(Integer type) {
			this.type = type;
		}
		public String getProgress() {
			return progress;
		}
		public void setProgress(String progress) {
			this.progress = progress;
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
	public List<Detail> getDetailList() {
		return detailList;
	}
	public void setDetailList(List<Detail> detailList) {
		this.detailList = detailList;
	}
	public String getTradeCode() {
		return tradeCode;
	}
	public void setTradeCode(String tradeCode) {
		this.tradeCode = tradeCode;
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
	public void setContentTempList(List<TempList> tempList) {
		this.contentTempList = tempList;
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
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getLoanId() {
		return loanId;
	}
	public void setLoanId(String loanId) {
		this.loanId = loanId;
	}
	public Integer getLoanStatus() {
		return loanStatus;
	}
	public void setLoanStatus(Integer loanStatus) {
		this.loanStatus = loanStatus;
	}
	public Integer getShowModifyRate() {
		return showModifyRate;
	}
	public void setShowModifyRate(Integer showModifyRate) {
		this.showModifyRate = showModifyRate;
	}
	public Integer getIsLoaner() {
		return isLoaner;
	}
	public void setIsLoaner(Integer isLoaner) {
		this.isLoaner = isLoaner;
	}
	public String getFriendId() {
		return friendId;
	}
	public void setFriendId(String friendId) {
		this.friendId = friendId;
	}
	public String getLoanPurp() {
		return loanPurp;
	}
	public void setLoanPurp(String loanPurp) {
		this.loanPurp = loanPurp;
	}
	public Integer getShowButton() {
		return showButton;
	}
	public void setShowButton(Integer showButton) {
		this.showButton = showButton;
	}
	public Integer getShowPlanButton() {
		return showPlanButton;
	}
	public void setShowPlanButton(Integer showPlanButton) {
		this.showPlanButton = showPlanButton;
	}
	public String getAvailableAmount() {
		return availableAmount;
	}
	public void setAvailableAmount(String availableAmount) {
		this.availableAmount = availableAmount;
	}
	public String getRemainAmount() {
		return remainAmount;
	}
	public void setRemainAmount(String remainAmount) {
		this.remainAmount = remainAmount;
	}
	public String getHadBorrowAmount() {
		return hadBorrowAmount;
	}
	public void setHadBorrowAmount(String hadBorrowAmount) {
		this.hadBorrowAmount = hadBorrowAmount;
	}
	public String getLoaneeHeadImage() {
		return loaneeHeadImage;
	}
	public void setLoaneeHeadImage(String loaneeHeadImage) {
		this.loaneeHeadImage = loaneeHeadImage;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getShowAuction() {
		return showAuction;
	}
	public void setShowAuction(Integer showAuction) {
		this.showAuction = showAuction;
	}
	public String getNowCreditor() {
		return nowCreditor;
	}
	public void setNowCreditor(String nowCreditor) {
		this.nowCreditor = nowCreditor;
	}
	public Integer getHasAuctioned() {
		return hasAuctioned;
	}
	public void setHasAuctioned(Integer hasAuctioned) {
		this.hasAuctioned = hasAuctioned;
	}
	public String getCompleteDate() {
		return completeDate;
	}
	public void setCompleteDate(String completeDate) {
		this.completeDate = completeDate;
	}
}
