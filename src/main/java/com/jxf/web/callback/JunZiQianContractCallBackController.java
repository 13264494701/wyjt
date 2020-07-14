package com.jxf.web.callback;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;


import com.jxf.loan.dao.NfsLoanRepayRecordDao;
import com.jxf.loan.entity.NfsCrAuction;
import com.jxf.loan.entity.NfsLoanApply;
import com.jxf.loan.entity.NfsLoanContract;
import com.jxf.loan.entity.NfsLoanRecord;
import com.jxf.loan.entity.NfsLoanRepayRecord;
import com.jxf.loan.service.NfsCrAuctionService;

import com.jxf.loan.service.NfsLoanRecordService;
import com.jxf.mem.entity.Member;
import com.jxf.mem.service.MemberService;
import com.jxf.svc.config.Constant;
import com.jxf.svc.config.Global;
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.NumberToChineseUtils;
import com.jxf.svc.utils.StringUtils;

/**
 * @author Administrator
 */
@RestController
@RequestMapping(value = "/callback/contract")
public class JunZiQianContractCallBackController {


	@Autowired
	private NfsLoanRepayRecordDao nfsLoanRepayRecordDao;

	@Autowired
	private MemberService memberService;
	
	@Autowired
	private NfsLoanRecordService nfsLoanRecordService;
	
	@Autowired
	private NfsLoanRecordService loanRecordService;
	@Autowired
	private NfsCrAuctionService crAuctionService;
    /**
            * 电子签章
     * @param loanId
     * @return
     */
	@RequestMapping(value = "genContract")
	public ModelAndView genContract(Long loanId) {
		
		ModelAndView mv = new ModelAndView("admin/loan/contract/junziqianXY");
		NfsLoanRecord loanRecord = nfsLoanRecordService.get(loanId);

		Member loaner = memberService.get(loanRecord.getLoaner());
		Member loanee = memberService.get(loanRecord.getLoanee());
		
		loanRecord.setAmount(loanRecord.getAmount().setScale(2, BigDecimal.ROUND_UP));
		loanRecord.setDueRepayAmount(loanRecord.getDueRepayAmount().setScale(2, BigDecimal.ROUND_UP));
		
		// TODO 分期计算业务待梳理
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String completeDateYear = DateUtils.getYear(loanRecord.getDueRepayDate());
		String completeDateMonth = DateUtils.getMonth(loanRecord.getDueRepayDate());
		String completeDateDay = DateUtils.getDay(loanRecord.getDueRepayDate());
		if(loanRecord.getRepayType().equals(NfsLoanApply.RepayType.principalAndInterestByMonth)) {	
			
			NfsLoanRepayRecord loanRepayRecord = new NfsLoanRepayRecord();
			loanRepayRecord.setLoan(loanRecord);
			List<NfsLoanRepayRecord> repayRecordList = nfsLoanRepayRecordDao.findList(loanRepayRecord);
			for (NfsLoanRepayRecord item : repayRecordList) {
				    completeDateYear = DateUtils.getYear(item.getExpectRepayDate());
				    completeDateMonth = DateUtils.getMonth(item.getExpectRepayDate());
				    completeDateDay = DateUtils.getDay(item.getExpectRepayDate());
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("year", completeDateYear);
					map.put("month", completeDateMonth);
					map.put("day", completeDateDay);
					map.put("periods_seq", item.getPeriodsSeq());
					map.put("amout", item.getExpectRepayAmt().setScale(2, BigDecimal.ROUND_UP));
					list.add(map);
			}
			
		}
		//0->仲裁,1->诉讼
		mv.addObject("disputeResolution", loanRecord.getDisputeResolution().ordinal()+1);
		mv.addObject("loan", loanRecord);
		mv.addObject("createTimeYear", DateUtils.getYear(loanRecord.getCreateTime()));
		mv.addObject("createTimeMonth", DateUtils.getMonth(loanRecord.getCreateTime()));
		mv.addObject("createTimeDay", DateUtils.getDay(loanRecord.getCreateTime()));
		
		mv.addObject("completeDateYear", completeDateYear);
		mv.addObject("completeDateMonth", completeDateMonth);
		mv.addObject("completeDateDay", completeDateDay);
		
		mv.addObject("loaner", loaner);
		mv.addObject("loanee", loanee);
		mv.addObject("loanUse",NfsLoanContract.LoanPurposeType.values()[loanRecord.getLoanPurp().ordinal()].getTypeName());
		mv.addObject("term",(int)(loanRecord.getTerm()/30));
		mv.addObject("list", list);
			
		return mv;
	}
	
	/**
	 * 电子签章
	 * 
	 * @param loanId
	 * @return
	 */
	@RequestMapping(value = "genGxtContract")
	public ModelAndView genGxtContract(Long loanId) {

		ModelAndView mv = new ModelAndView("admin/loan/contract/gxtAgreement");
		NfsLoanRecord loanRecord = nfsLoanRecordService.get(loanId);

		Member loaner = memberService.get(loanRecord.getLoaner());
		Member loanee = memberService.get(loanRecord.getLoanee());

		loanRecord.setAmount(loanRecord.getAmount().setScale(2, BigDecimal.ROUND_UP));
		loanRecord.setDueRepayAmount(loanRecord.getDueRepayAmount().setScale(2, BigDecimal.ROUND_UP));

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String completeDateYear = DateUtils.getYear(loanRecord.getDueRepayDate());
		String completeDateMonth = DateUtils.getMonth(loanRecord.getDueRepayDate());
		String completeDateDay = DateUtils.getDay(loanRecord.getDueRepayDate());
		if (loanRecord.getRepayType().equals(NfsLoanApply.RepayType.principalAndInterestByMonth)) {

			NfsLoanRepayRecord loanRepayRecord = new NfsLoanRepayRecord();
			loanRepayRecord.setLoan(loanRecord);
			List<NfsLoanRepayRecord> repayRecordList = nfsLoanRepayRecordDao.findList(loanRepayRecord);
			for (NfsLoanRepayRecord item : repayRecordList) {
				completeDateYear = DateUtils.getYear(item.getExpectRepayDate());
				completeDateMonth = DateUtils.getMonth(item.getExpectRepayDate());
				completeDateDay = DateUtils.getDay(item.getExpectRepayDate());
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("year", completeDateYear);
				map.put("month", completeDateMonth);
				map.put("day", completeDateDay);
				map.put("periods_seq", item.getPeriodsSeq());
				map.put("amout", item.getExpectRepayAmt().setScale(2, BigDecimal.ROUND_UP));
				list.add(map);
			}

		}
		// 0->仲裁,1->诉讼
		mv.addObject("disputeResolution", loanRecord.getDisputeResolution().ordinal() + 1);
		mv.addObject("loan", loanRecord);

		mv.addObject("loaner", loaner);
		mv.addObject("loanee", loanee);
		mv.addObject("loanUse",
				NfsLoanContract.LoanPurposeType.values()[loanRecord.getLoanPurp().ordinal()].getTypeName());
		mv.addObject("term", loanRecord.getTerm());
		mv.addObject("list", list);
		mv.addObject("loanDoneFee", Global.getConfig("gxt.loanDoneFee"));
		mv.addObject("loanDelayFee", Global.getConfig("gxt.loanDelayFee"));
		return mv;
	}
	
	 /**
     *      债转 电子签章
	* @param crId
	* @return
	*/
	@RequestMapping(value = "genCrAuctionContract")
	public ModelAndView genCrAuctionContract(Long crId) {
		
		ModelAndView mv = new ModelAndView("app/cr/claimsTrsAgreementXY");
		NfsCrAuction crAuction = crAuctionService.get(crId);
		
		Member seller = memberService.get(crAuction.getCrSeller());
		Member buyer = memberService.get(crAuction.getCrBuyer());
		NfsLoanRecord loanRecord = loanRecordService.get(crAuction.getLoanRecord());
		String amountCHNum = NumberToChineseUtils.getChineseNumber(StringUtils.decimalToStr(loanRecord.getAmount(), 2));
		String dueRepayCHNum = NumberToChineseUtils.getChineseNumber(StringUtils.decimalToStr(loanRecord.getDueRepayAmount(), 2));
		String sellPriceCHNum = NumberToChineseUtils.getChineseNumber(StringUtils.decimalToStr(crAuction.getCrSellPrice(), 2));
		String creditCode = Constant.COM_CREDIT_CODE;
		mv.addObject("seller", seller);
		mv.addObject("buyer", buyer);
		mv.addObject("loanRecord", loanRecord);
		mv.addObject("amountCHNum", amountCHNum);
		mv.addObject("dueRepayCHNum", dueRepayCHNum);
		mv.addObject("sellPriceCHNum", sellPriceCHNum);
		mv.addObject("createTime", DateUtils.getDateStr(loanRecord.getCreateTime(), "yyyy年MM月dd日"));
		mv.addObject("dueRepayDate", DateUtils.getDateStr(loanRecord.getDueRepayDate(), "yyyy年MM月dd日"));
		mv.addObject("endTime", DateUtils.getDateStr(crAuction.getUpdateTime(), "yyyy年MM月dd日"));
		mv.addObject("sellPrice", crAuction.getCrSellPrice());
		mv.addObject("creditCode", creditCode);
		return mv;
	}
}