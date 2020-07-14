package com.jxf.loan.signature.youdun.preservation;

import java.io.File;
import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import com.jxf.loan.entity.NfsLoanArbitration;
import com.jxf.loan.entity.NfsLoanContract;
import com.jxf.loan.entity.NfsLoanOperatingRecord;
import com.jxf.loan.entity.NfsLoanRecord;
import com.jxf.loan.entity.NfsLoanApply.RepayType;
import com.jxf.loan.entity.NfsLoanApply.TrxType;
import com.jxf.loan.signature.youdun.DigestUtil;
import com.jxf.loan.signature.youdun.YouDunConstant.NodeType;
import com.jxf.loan.utils.LoanUtils;
import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberCard;
import com.jxf.mem.entity.MemberVideoVerify;
import com.jxf.mem.entity.Member.Gender;
import com.jxf.nfs.entity.NfsWdrlRecord;
import com.jxf.svc.config.Constant;
import com.jxf.svc.config.Global;
import com.jxf.svc.utils.CalendarUtil;
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.Encodes;
import com.jxf.svc.utils.StringUtils;

public class PreservationInfoBuilderFactory {
	
	private static PreservationInfoBuilderFactory instance = new PreservationInfoBuilderFactory();
	
	private PreservationInfoBuilderFactory() {}
	
	public static PreservationInfoBuilderFactory getInstance() {
		return instance;
	}
	
	public  JSONObject buildPreservationInfo(PreservationBuilderData builderData,NodeType nodeType) {
		if(builderData == null) {
			return getPlatformIdentificationInfo();
		}
		switch (nodeType) {
			case identificationInfo:
				return getMemberIdentificationInfo(builderData);
			case contractInfo:
				return getContractInfo(builderData);
			case loanInfo:
				TrxType trxType = builderData.getLoanRecord().getTrxType();
				if(trxType != null) {
					return trxType.equals(TrxType.online)? getLoanInfoForApp(builderData):getLoanInfoForGxt(builderData);
				}
			case repayInfo:
				return getRepayInfo(builderData);
			case renewalInfo:
				return getRenewalInfo(builderData);
			default:
				break;
		}
		return null;
	}
	
	
	/**
	 * 个人认证数据
	 * @param PreservationBuilderData
	 * @return 
	 */
	private  JSONObject getMemberIdentificationInfo(PreservationBuilderData builderData) {
		Member member = builderData.getMember();
		MemberCard memberCard = builderData.getMemberCard();
		MemberVideoVerify memberVideoVerify = builderData.getMemberVideoVerify();
		//处理之前公信堂实名认证丢失部分身份证背面图片的问题
		if(StringUtils.isBlank(memberVideoVerify.getIdcardBackPhoto()) && StringUtils.isNotBlank(memberVideoVerify.getIdcardFrontPhoto())) {
			memberVideoVerify.setIdcardBackPhoto(memberVideoVerify.getIdcardFrontPhoto());
		}
		IdentifyInfo memberInfo = new IdentifyInfo();
		memberInfo.setUserName(member.getName());
		memberInfo.setIdType("0");
		memberInfo.setIdNo(member.getIdNo());
		memberInfo.setGender(member.getGender().equals(Gender.male) ? 1:0);
		memberInfo.setBirthday(member.getIdNo().substring(6,14));
		memberInfo.setAddress(member.getAddr() == null ? "无":member.getAddr());
		memberInfo.setMobile(member.getUsername());
		memberInfo.setEmail(member.getEmail());
		memberInfo.setBankName(memberCard.getBank().getName());
		memberInfo.setBankNo(memberCard.getCardNo());
		memberInfo.setAppid(Constant.APP_NAME);
		String baseStaticPath=Global.getConfig("baseStaticPath");
		String idcardFrontPhoto = (memberVideoVerify.getId()>2981046?baseStaticPath:"/data/wyjt")+memberVideoVerify.getIdcardFrontPhoto();
		String idcardBackPhoto = (memberVideoVerify.getId()>2981046?baseStaticPath:"/data/wyjt")+memberVideoVerify.getIdcardBackPhoto();
		memberInfo.setIdcardFrontPhoto(getBase64Photo(idcardFrontPhoto));
		memberInfo.setIdcardBackPhoto(getBase64Photo(idcardBackPhoto));
		return JSONObject.parseObject(JSONObject.toJSONString(memberInfo));
	}
	
	/**
	 * 平台认证数据
	 * @return JSONObject 平台数据
	 */
	private  JSONObject getPlatformIdentificationInfo() {
		IdentifyInfo platformInfo = new IdentifyInfo();
		platformInfo.setUserName(Constant.CORPLEGAL_NAME);
		platformInfo.setGender(1);
		platformInfo.setBirthday("19720128");
		platformInfo.setAddress("望京SOHO");
		platformInfo.setMobile(Constant.CORPLEGAL_MOBILE);
		platformInfo.setEmail(Constant.CORPLEGAL_EMAIL);
		platformInfo.setIdType("N");
		platformInfo.setIdNo(Constant.COM_CREDIT_CODE);
		platformInfo.setCorpLegalName(Constant.CORPLEGAL_NAME);
		platformInfo.setCorpLegalTitle( Constant.CORPLEGAL_TITLE);
		platformInfo.setCorpLegalMobile(Constant.CORPLEGAL_MOBILE);
		platformInfo.setCorpLegalEmail(Constant.CORPLEGAL_EMAIL);
		platformInfo.setAppid(Constant.APP_NAME);
		platformInfo.setBankName(Constant.CORP_BANK_NAME);
		platformInfo.setBankNo(Constant.CORP_BANK_CARD_NO);
		platformInfo.setLicensePhoto(getBase64Photo(Global.getBaseStaticPath() + Global.getConfig("licensePhotoPath")));
		return JSONObject.parseObject(JSONObject.toJSONString(platformInfo));
	}
	/**
	 * 借款合同节点数据
	 * @return JSONObject 
	 */
	private  JSONObject getContractInfo(PreservationBuilderData builderData) {
		NfsLoanRecord loanRecord = builderData.getLoanRecord();
		NfsLoanContract loanContract = builderData.getLoanContract();
		Date startTime = loanRecord.getTrxType().equals(TrxType.online) ? loanRecord.getCreateTime():loanRecord.getLoanStart();
		String loanStart =  DateUtils.getDateStr(startTime, "yyyyMMdd");
		String loanStartEndDate = loanStart + "-" + DateUtils.getDateStr(loanRecord.getDueRepayDate(), "yyyyMMdd");
		
		ContractInfo contractInfo = new ContractInfo();
		contractInfo.setContractName(loanRecord.getTrxType().equals(TrxType.online) ? "友借款服务协议":"借款服务协议");
		contractInfo.setContractCode(String.valueOf(loanContract.getId()));
		contractInfo.setSignTime(DateUtils.getDateStr(loanContract.getCreateTime(), "yyyyMMdd HH:mm:ss"));
		contractInfo.setLoanAmount(StringUtils.decimalToStr(loanRecord.getAmount(), 2));
		contractInfo.setLoanStartEndDate(loanStartEndDate);
		contractInfo.setRateYear(StringUtils.decimalToStr(loanRecord.getIntRate(), 2) + "%");
		contractInfo.setGraceDays(0);
		contractInfo.setOverdueDate(DateUtils.getDateStr(CalendarUtil.addDay(loanRecord.getDueRepayDate(), 1), "yyyyMMdd"));
		contractInfo.setOverdueRate("24%");
		contractInfo.setRepayType(loanRecord.getRepayType().equals(RepayType.oneTimePrincipalAndInterest) ? 1:2);
		contractInfo.setLoanPurpose(loanRecord.getLoanPurp().getName());
		contractInfo.setpUserName(loanRecord.getLoaner().getName());
		contractInfo.setpIdNo(loanRecord.getLoaner().getIdNo());
		contractInfo.setmUserName(Constant.COM_NAME);
		contractInfo.setmIdNo(Constant.COM_CREDIT_CODE);
		contractInfo.setUserName(loanRecord.getLoanee().getName());
		contractInfo.setIdNo(loanRecord.getLoanee().getIdNo());
		contractInfo.setmLicensePhoto(getBase64Photo(Global.getBaseStaticPath() + Global.getConfig("licensePhotoPath")));
		contractInfo.setUdContractCode(loanContract.getSignatureNo());
		return JSONObject.parseObject(JSONObject.toJSONString(contractInfo));
	}
	/**
	 * 付款节点数据
	 * @return JSONObject 
	 */
	private  JSONObject getLoanInfoForApp(PreservationBuilderData builderData) {
		NfsLoanRecord loanRecord = builderData.getLoanRecord();
		NfsLoanContract loanContract = builderData.getLoanContract();
		NfsWdrlRecord wdrlRecord = builderData.getWdrlRecord();
		MemberCard loanerCard = builderData.getMemberCard();
		
		LoanInfo loanInfo = new LoanInfo();
		loanInfo.setUserName(loanRecord.getLoaner().getName());
		loanInfo.setIdNo(loanRecord.getLoaner().getIdNo());
		loanInfo.setContractCode(String.valueOf(loanContract.getId()));
		loanInfo.setPaymentName(loanRecord.getLoaner().getName());
		loanInfo.setLoanName(loanRecord.getLoaneeName());
		loanInfo.setDealStatus(1);
		loanInfo.setDealTime(DateUtils.getDateStr(loanRecord.getCreateTime(),"yyyyMMdd HH:mm:ss"));
		loanInfo.setPaymentInstitution(wdrlRecord.getType().equals(NfsWdrlRecord.Type.fuiou) ? "上海富友金融网络技术有限公司" : "连连银通电子支付有限公司");
		loanInfo.setPaymentNo(wdrlRecord.getThirdOrderNo() == null ? "" : wdrlRecord.getThirdOrderNo());

		loanInfo.setPaymentBankCardNo(loanerCard.getCardNo());
		loanInfo.setPaymentBankCardName(loanerCard.getBank().getName());
		loanInfo.setPaymentAmount(StringUtils.decimalToStr(loanRecord.getAmount(), 2));

		loanInfo.setLoanBankCardNo(wdrlRecord.getCardNo());
		loanInfo.setLoanBankCardName(wdrlRecord.getBankName());
		return JSONObject.parseObject(JSONObject.toJSONString(loanInfo));
	}
	
	/**
	 * 付款节点数据
	 * @return JSONObject 
	 */
	private  JSONObject getLoanInfoForGxt(PreservationBuilderData builderData) {
		NfsLoanRecord loanRecord = builderData.getLoanRecord();
		NfsLoanContract loanContract = builderData.getLoanContract();
		NfsLoanArbitration loanArbitration = builderData.getLoanArbitration();
		MemberCard loanerCard = builderData.getMemberCard();
		
		LoanInfo loanInfo = new LoanInfo();
		loanInfo.setUserName(loanRecord.getLoaner().getName());
		loanInfo.setIdNo(loanRecord.getLoaner().getIdNo());
		loanInfo.setContractCode(String.valueOf(loanContract.getId()));
		loanInfo.setPaymentName(loanRecord.getLoaner().getName());
		loanInfo.setLoanName(loanRecord.getLoaneeName());
		loanInfo.setDealStatus(1);
		loanInfo.setDealTime(DateUtils.getDateStr(loanRecord.getLoanStart(),"yyyyMMdd HH:mm:ss"));
		loanInfo.setPaymentInstitution(loanArbitration.getLoanerAccountName());
		loanInfo.setPaymentNo(loanArbitration.getPayOrderNo());

		loanInfo.setPaymentBankCardNo(loanerCard.getCardNo());
		loanInfo.setPaymentBankCardName(loanerCard.getBank().getName());
		loanInfo.setPaymentAmount(StringUtils.decimalToStr(loanRecord.getAmount(), 2));

		loanInfo.setLoanBankCardNo(loanArbitration.getLoaneeAccount());
		loanInfo.setLoanBankCardName(loanArbitration.getLoaneeAccountName());
		return JSONObject.parseObject(JSONObject.toJSONString(loanInfo));
	}
	
	/**
	 * 还款节点数据
	 * @return JSONObject 
	 */
	private  JSONObject getRepayInfo(PreservationBuilderData builderData) {
		NfsLoanOperatingRecord operatingRecord = builderData.getOperatingRecord();
		MemberCard loaneeCard = builderData.getMemberCard();
		Member loanee = builderData.getMember();
		NfsLoanContract loanContract = builderData.getLoanContract();
		NfsLoanRecord oldLoanRecord = operatingRecord.getOldRecord();
		
		
		RepayInfo repayInfo = new RepayInfo();
		repayInfo.setUserName(loanee.getName());
		repayInfo.setIdNo(loanee.getIdNo());
		repayInfo.setRepayTime(DateUtils.getDateStr(operatingRecord.getCreateTime(), "yyyyMMdd"));
		
		BigDecimal operationAmount = operatingRecord.getRepaymentAmount();//本次还款的金额
		BigDecimal oldInterest = oldLoanRecord.getInterest();
		BigDecimal repayedInterest = BigDecimal.ZERO;//还款利息
		BigDecimal repayedAmount = BigDecimal.ZERO;//还款本金
		if(operationAmount.compareTo(oldInterest) < 0) {
			//还款额小于原利息 还款利息：还款额，还款本金：0
			repayedInterest = operatingRecord.getRepaymentAmount();
			repayedAmount = BigDecimal.ZERO;
		}else {
			//还款额大于等于原利息 还款利息：原利息， 还款本金：还款金额-原利息
			repayedInterest = oldInterest;
			repayedAmount = operationAmount.subtract(oldInterest);
		}
		repayInfo.setRepayAmount(StringUtils.decimalToStr(repayedAmount, 2));
		repayInfo.setInterestAmount(StringUtils.decimalToStr(repayedInterest, 2));
		
		repayInfo.setLoanName(loanee.getName());
		repayInfo.setLoanBankCardNo(loaneeCard.getCardNo());
		repayInfo.setLoanBankCardName(loaneeCard.getBank().getName());;
		repayInfo.setContractCode(loanContract.getId().toString());
		return JSONObject.parseObject(JSONObject.toJSONString(repayInfo));
	}
	
	/**
	 * 延期借款合同节点数据
	 * @return JSONObject 
	 */
	private  JSONObject getRenewalInfo(PreservationBuilderData builderData) {
		NfsLoanRecord loanRecord = builderData.getLoanRecord();
		NfsLoanContract loanContract = builderData.getLoanContract();
		NfsLoanOperatingRecord operatingRecord = builderData.getOperatingRecord();
		
		RenewalInfo renewalInfo = new RenewalInfo();
		renewalInfo.setContractName(loanRecord.getTrxType().equals(TrxType.online) ? "友借款服务协议":"借款服务协议");
		renewalInfo.setContractCode(loanContract.getId().toString());
		renewalInfo.setLastContractCode(builderData.getLastContractCode());
		renewalInfo.setSignTime(DateUtils.getDateStr(loanContract.getCreateTime(), "yyyyMMdd HH:mm:ss"));
		renewalInfo.setLoanAmount(StringUtils.decimalToStr(operatingRecord.getNowRecord().getAmount(), 2));
		Date startTime = loanRecord.getTrxType().equals(TrxType.online) ? loanRecord.getCreateTime():loanRecord.getLoanStart();
		String loanStart =  DateUtils.getDateStr(startTime, "yyyyMMdd");
		String loanStartEndDate = loanStart + "-" + DateUtils.getDateStr(loanRecord.getDueRepayDate(), "yyyyMMdd");
		renewalInfo.setLoanStartEndDate(loanStartEndDate);
		BigDecimal nowDueRepayAmount = operatingRecord.getNowRecord().getAmount().add(operatingRecord.getNowRecord().getInterest());
		if(operatingRecord.getDelayInterest().compareTo(BigDecimal.ZERO) == 0) {
			renewalInfo.setRateYear("0%");
		}else {
			BigDecimal delayRate = LoanUtils.getIntRate(nowDueRepayAmount, operatingRecord.getDelayDays(), operatingRecord.getDelayInterest());
			renewalInfo.setRateYear(StringUtils.decimalToStr(delayRate, 2) + "%");
		}
		//宽限日
		renewalInfo.setGraceDays(0);
		renewalInfo.setOverdueDate(DateUtils.getDateStr(CalendarUtil.addDay(loanRecord.getDueRepayDate(), 1), "yyyyMMdd"));
		renewalInfo.setOverdueRate("24%");
		renewalInfo.setLoanPurpose(loanRecord.getLoanPurp().getName());
		//出借人
		renewalInfo.setpUserName(loanRecord.getLoaner().getName());
		renewalInfo.setIdNo(loanRecord.getLoaner().getIdNo());
		//居间方
		renewalInfo.setmUserName(Constant.COM_NAME);
		renewalInfo.setmIdNo(Constant.COM_CREDIT_CODE);
		//借款人
		renewalInfo.setUserName(loanRecord.getLoanee().getName());
		renewalInfo.setIdNo(loanRecord.getLoanee().getIdNo());
		renewalInfo.setUdContractCode(loanContract.getSignatureNo());
		return JSONObject.parseObject(JSONObject.toJSONString(renewalInfo));
	}
	
	
	
	 private  String getBase64Photo(String filePath) {
	    	byte[] bytes = DigestUtil.fileConvertToByteArray(new File(filePath));
	    	return Encodes.encodeBase64(bytes);
	    }
}
