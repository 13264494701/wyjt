package com.jxf.loan.utils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Date;

import com.jxf.loan.entity.NfsLoanApply;
import com.jxf.loan.entity.NfsLoanRecord;
import com.jxf.loan.entity.NfsLoanRepayRecord;
import com.jxf.svc.utils.CalendarUtil;


public class LoanUtils {

	private static BigDecimal oneHundred = new BigDecimal(100);
	private static BigDecimal yearDays = new BigDecimal(360);
	private static BigDecimal maxIntRate = new BigDecimal(24);
/**
 * 还款计划试算	
 * @param amount 借款金额
 * @param intRate 借款年利率(不含%)
 * @param repayType 还款方式
 * @param loanTerm 借款时长(天数)
 * @return
 */
public static  NfsLoanRecord  calInt(BigDecimal amount,BigDecimal intRate,NfsLoanApply.RepayType repayType,Integer loanTerm){
		
	    NfsLoanRecord loanRecord = new NfsLoanRecord();
	    
		BigDecimal yearRate = intRate.divide(new BigDecimal(100),6,BigDecimal.ROUND_HALF_UP);// 年利率;
		BigDecimal monthRate = yearRate.divide(new BigDecimal(12),10,BigDecimal.ROUND_HALF_UP);//月利率
		BigDecimal dayRate = yearRate.divide(new BigDecimal(360),10,BigDecimal.ROUND_HALF_UP);//日利率
				
		//
		BigDecimal expectRepayAmt = BigDecimal.ZERO; // 每期应还金额
		BigDecimal expectRepayPrn = BigDecimal.ZERO; // 每期应还本金
		BigDecimal expectRepayInt = BigDecimal.ZERO; // 每期应还利息
		BigDecimal repayPrn = amount;//本金
		BigDecimal repayInt = BigDecimal.ZERO; // 应还利息
		BigDecimal repayAmt = BigDecimal.ZERO; // 应还金额

		//还款方式:一次性还本付息
		if((loanTerm%30!=0)&&repayType.equals(NfsLoanApply.RepayType.oneTimePrincipalAndInterest)){
			expectRepayPrn = repayPrn.setScale(2, BigDecimal.ROUND_HALF_UP);// 应还本金
			expectRepayInt = repayPrn.multiply(dayRate).multiply(new BigDecimal(loanTerm)).setScale(2, BigDecimal.ROUND_HALF_UP);//应还利息=本金*日利率*天数
			expectRepayAmt = repayPrn.add(expectRepayInt).setScale(2,  BigDecimal.ROUND_HALF_UP);// 本息合计			
			Date expectRepayDate = CalendarUtil.addDay(new Date(), loanTerm-1);//结束日期
			
			NfsLoanRepayRecord repayRecord=new NfsLoanRepayRecord();
			repayRecord.setExpectRepayAmt(expectRepayAmt);// 每期还款金额
			repayRecord.setExpectRepayPrn(expectRepayPrn);// 每期还款本金 
			repayRecord.setExpectRepayInt(expectRepayInt);// 每期还款利息 			
			repayRecord.setPeriodsSeq(1);// 还款期数
			repayRecord.setStatus(NfsLoanRepayRecord.Status.pending);
			repayRecord.setExpectRepayDate(expectRepayDate);// 结束日期	
			
			loanRecord.getRepayRecordList().add(repayRecord);
			loanRecord.setRepayedTerm(0);
			loanRecord.setDueRepayTerm(1);
			loanRecord.setInterest(expectRepayInt);//借条应还利息
			loanRecord.setDueRepayAmount(expectRepayAmt);//借条应还金额
			loanRecord.setDueRepayDate(expectRepayDate);//借条到期时间

		//还款方式:一次性还本付息
		}else if((loanTerm%30==0)&&repayType.equals(NfsLoanApply.RepayType.oneTimePrincipalAndInterest)){

			expectRepayPrn = repayPrn.setScale(2, BigDecimal.ROUND_HALF_UP);// 应还本金
			expectRepayInt = repayPrn.multiply(dayRate).multiply(new BigDecimal(loanTerm)).setScale(2, BigDecimal.ROUND_HALF_UP);//应还利息=本金*月利率*月数
			expectRepayAmt=repayPrn.add(expectRepayInt);	// 本息合计	
			Date expectRepayDate = CalendarUtil.addDay(new Date(), loanTerm-1);//每期到期日期	
			
			NfsLoanRepayRecord repayRecord=new NfsLoanRepayRecord();
			repayRecord.setExpectRepayAmt(expectRepayAmt);// 每期还款金额
			repayRecord.setExpectRepayPrn(expectRepayPrn);// 每期还款本金 
			repayRecord.setExpectRepayInt(expectRepayInt);// 每期还款利息 			
			repayRecord.setPeriodsSeq(1);// 还款期数
			repayRecord.setStatus(NfsLoanRepayRecord.Status.pending);
			repayRecord.setExpectRepayDate(expectRepayDate);// 结束日期	
			
			loanRecord.getRepayRecordList().add(repayRecord);
			loanRecord.setRepayedTerm(0);
			loanRecord.setDueRepayTerm(1);
			loanRecord.setInterest(expectRepayInt);//借条应还利息
			loanRecord.setDueRepayAmount(expectRepayAmt);//借条应还金额
			loanRecord.setDueRepayDate(expectRepayDate);//借条到期时间
		
		//还款方式:按月付息到期还本
		}else if((loanTerm%30==0)&&repayType.equals(NfsLoanApply.RepayType.interestFirstByMonth)){
			for (int i = 1; i <=loanTerm/30; i++) { 				
				if(i==loanTerm/30){
					expectRepayPrn = repayPrn.setScale(2, BigDecimal.ROUND_HALF_UP);// 每期应还本金
					expectRepayInt = repayPrn.multiply(monthRate).setScale(2, BigDecimal.ROUND_HALF_UP);//每期应还利息=本金*月利率
					expectRepayAmt=expectRepayPrn.add(expectRepayInt);	// 每期本息合计	
					
				}else{
					expectRepayPrn = BigDecimal.ZERO;// 每期应还本金
					expectRepayInt = repayPrn.multiply(monthRate).setScale(2, BigDecimal.ROUND_HALF_UP);//每期应还利息=本金*月利率
					expectRepayAmt=expectRepayPrn.add(expectRepayInt);	// 每期本息合计					
				}
				NfsLoanRepayRecord repayRecord=new NfsLoanRepayRecord();
				repayRecord.setExpectRepayAmt(expectRepayAmt);// 还款金额
				repayRecord.setExpectRepayPrn(expectRepayPrn);// 还款本金 
				repayRecord.setExpectRepayInt(expectRepayInt);// 还款利息 			
				repayRecord.setPeriodsSeq(i);// 还款期数
				repayRecord.setStatus(NfsLoanRepayRecord.Status.pending);
				repayRecord.setExpectRepayDate(CalendarUtil.addDay(new Date(), i*30-1));// 结束日期	
				loanRecord.getRepayRecordList().add(repayRecord);		
				
				repayAmt = repayAmt.add(expectRepayAmt);
				repayInt = repayInt.add(expectRepayInt);
				
			}
			loanRecord.setRepayedTerm(0);
			loanRecord.setDueRepayTerm(loanTerm/30);
			loanRecord.setInterest(repayInt);	//借条应还利息		
			loanRecord.setDueRepayAmount(repayAmt);//借条应还金额
			loanRecord.setDueRepayDate(CalendarUtil.addDay(new Date(), loanTerm-1));//借条到期时间

		//还款方式:按月等额本息
		}else if((loanTerm%30==0)&&repayType.equals(NfsLoanApply.RepayType.principalAndInterestByMonth)){

            //计算因子=（1+月利率）^还款月数
			MathContext mc = new MathContext(10); //精度为10位
			BigDecimal calFactor = monthRate.add(new BigDecimal(1)).pow(loanTerm/30,mc);
					
			//每月还款金额计算公式：每月还款额=贷款本金×[月利率×（1+月利率）^还款月数]÷[（1+月利率）^还款月数—1]
			if(monthRate.compareTo(BigDecimal.ZERO)==0) {
			   expectRepayAmt = repayPrn.divide(new BigDecimal(loanTerm/30),2,BigDecimal.ROUND_HALF_UP);
			}else {
			   expectRepayAmt = (repayPrn.multiply(monthRate,mc).multiply(calFactor, mc).divide(calFactor.subtract(new BigDecimal(1)), mc));
			}
			BigDecimal remainder = repayPrn;
			for (int i = 1; i <=loanTerm/30; i++) { 
				
				expectRepayInt = remainder.multiply(monthRate,mc);//每期应还利息=剩余本金*月利率
				expectRepayPrn = expectRepayAmt.subtract(expectRepayInt);// 每期应还本金				
				remainder = remainder.subtract(expectRepayPrn);
				
				NfsLoanRepayRecord repayRecord=new NfsLoanRepayRecord();
				repayRecord.setExpectRepayAmt(expectRepayAmt.setScale(2, BigDecimal.ROUND_HALF_UP));// 还款金额
				repayRecord.setExpectRepayPrn(expectRepayPrn.setScale(2, BigDecimal.ROUND_HALF_UP));// 还款本金 
				repayRecord.setExpectRepayInt(expectRepayInt.setScale(2, BigDecimal.ROUND_HALF_UP));// 还款利息 			
				repayRecord.setPeriodsSeq(i);// 还款期数
				repayRecord.setStatus(NfsLoanRepayRecord.Status.pending);
				repayRecord.setExpectRepayDate(CalendarUtil.addDay(new Date(), i*30-1));// 应还日期
				if(i == 1) {
					loanRecord.setDueRepayDate(CalendarUtil.addDay(new Date(), i*30-1));//借条到期时间
				}
				loanRecord.getRepayRecordList().add(repayRecord);		
				
				repayInt = repayInt.add(expectRepayInt);
				repayAmt = repayAmt.add(expectRepayAmt);
		    }
			loanRecord.setRepayedTerm(0);
			loanRecord.setDueRepayTerm(loanTerm/30);
			loanRecord.setInterest(repayInt);			
			loanRecord.setDueRepayAmount(repayAmt);//借条应还金额
		}

		return loanRecord;
	}

/**
 * 还款计划试算	
 * @param amount 借款金额
 * @param intRate 借款年利率(不含%)
 * @param repayType 还款方式
 * @param loanTerm 借款时长(天数)
 * @return
 */
public static  NfsLoanRecord  calIntForOffline(BigDecimal amount,BigDecimal intRate,NfsLoanApply.RepayType repayType,Integer loanTerm,Date loanStart){
		
	    NfsLoanRecord loanRecord = new NfsLoanRecord();
	    
		BigDecimal yearRate = intRate.divide(new BigDecimal(100),6,BigDecimal.ROUND_HALF_UP);// 年利率;
		BigDecimal monthRate = yearRate.divide(new BigDecimal(12),10,BigDecimal.ROUND_HALF_UP);//月利率
		BigDecimal dayRate = yearRate.divide(new BigDecimal(360),10,BigDecimal.ROUND_HALF_UP);//日利率
				
		//
		BigDecimal expectRepayAmt = BigDecimal.ZERO; // 每期应还金额
		BigDecimal expectRepayPrn = BigDecimal.ZERO; // 每期应还本金
		BigDecimal expectRepayInt = BigDecimal.ZERO; // 每期应还利息
		BigDecimal repayPrn = amount;//本金
		BigDecimal repayInt = BigDecimal.ZERO; // 应还利息
		BigDecimal repayAmt = BigDecimal.ZERO; // 应还金额
		//还款方式:一次性还本付息
		if((loanTerm%30!=0)&&repayType.equals(NfsLoanApply.RepayType.oneTimePrincipalAndInterest)){
			expectRepayPrn = repayPrn.setScale(2, BigDecimal.ROUND_HALF_UP);// 应还本金
			expectRepayInt = repayPrn.multiply(dayRate).multiply(new BigDecimal(loanTerm)).setScale(2, BigDecimal.ROUND_HALF_UP);//应还利息=本金*日利率*天数
			expectRepayAmt = repayPrn.add(expectRepayInt).setScale(2,  BigDecimal.ROUND_HALF_UP);// 本息合计			
			Date expectRepayDate = CalendarUtil.addDay(loanStart, loanTerm-1);//结束日期
			
			NfsLoanRepayRecord repayRecord=new NfsLoanRepayRecord();
			repayRecord.setExpectRepayAmt(expectRepayAmt);// 每期还款金额
			repayRecord.setExpectRepayPrn(expectRepayPrn);// 每期还款本金 
			repayRecord.setExpectRepayInt(expectRepayInt);// 每期还款利息 			
			repayRecord.setPeriodsSeq(1);// 还款期数
			repayRecord.setStatus(NfsLoanRepayRecord.Status.pending);
			repayRecord.setExpectRepayDate(expectRepayDate);// 结束日期	
			
			loanRecord.getRepayRecordList().add(repayRecord);
			loanRecord.setRepayedTerm(0);
			loanRecord.setDueRepayTerm(1);
			loanRecord.setInterest(expectRepayInt);//借条应还利息
			loanRecord.setDueRepayAmount(expectRepayAmt);//借条应还金额
			loanRecord.setDueRepayDate(expectRepayDate);//借条到期时间

		//还款方式:一次性还本付息
		}else if((loanTerm%30==0)&&repayType.equals(NfsLoanApply.RepayType.oneTimePrincipalAndInterest)){

			expectRepayPrn = repayPrn.setScale(2, BigDecimal.ROUND_HALF_UP);// 应还本金
			expectRepayInt = repayPrn.multiply(dayRate).multiply(new BigDecimal(loanTerm)).setScale(2, BigDecimal.ROUND_HALF_UP);//应还利息=本金*月利率*月数
			expectRepayAmt=repayPrn.add(expectRepayInt);	// 本息合计	
			Date expectRepayDate = CalendarUtil.addDay(loanStart, loanTerm-1);//每期到期日期	
			
			NfsLoanRepayRecord repayRecord=new NfsLoanRepayRecord();
			repayRecord.setExpectRepayAmt(expectRepayAmt);// 每期还款金额
			repayRecord.setExpectRepayPrn(expectRepayPrn);// 每期还款本金 
			repayRecord.setExpectRepayInt(expectRepayInt);// 每期还款利息 			
			repayRecord.setPeriodsSeq(1);// 还款期数
			repayRecord.setStatus(NfsLoanRepayRecord.Status.pending);
			repayRecord.setExpectRepayDate(expectRepayDate);// 结束日期	
			
			loanRecord.getRepayRecordList().add(repayRecord);
			loanRecord.setRepayedTerm(0);
			loanRecord.setDueRepayTerm(1);
			loanRecord.setInterest(expectRepayInt);//借条应还利息
			loanRecord.setDueRepayAmount(expectRepayAmt);//借条应还金额
			loanRecord.setDueRepayDate(expectRepayDate);//借条到期时间
		
		//还款方式:按月付息到期还本
		}else if((loanTerm%30==0)&&repayType.equals(NfsLoanApply.RepayType.interestFirstByMonth)){
			for (int i = 1; i <=loanTerm/30; i++) { 				
				if(i==loanTerm/30){
					expectRepayPrn = repayPrn.setScale(2, BigDecimal.ROUND_HALF_UP);// 每期应还本金
					expectRepayInt = repayPrn.multiply(monthRate).setScale(2, BigDecimal.ROUND_HALF_UP);//每期应还利息=本金*月利率
					expectRepayAmt=expectRepayPrn.add(expectRepayInt);	// 每期本息合计	
					
				}else{
					expectRepayPrn = BigDecimal.ZERO;// 每期应还本金
					expectRepayInt = repayPrn.multiply(monthRate).setScale(2, BigDecimal.ROUND_HALF_UP);//每期应还利息=本金*月利率
					expectRepayAmt=expectRepayPrn.add(expectRepayInt);	// 每期本息合计					
				}
				NfsLoanRepayRecord repayRecord=new NfsLoanRepayRecord();
				repayRecord.setExpectRepayAmt(expectRepayAmt);// 还款金额
				repayRecord.setExpectRepayPrn(expectRepayPrn);// 还款本金 
				repayRecord.setExpectRepayInt(expectRepayInt);// 还款利息 			
				repayRecord.setPeriodsSeq(i);// 还款期数
				repayRecord.setStatus(NfsLoanRepayRecord.Status.pending);
				repayRecord.setExpectRepayDate(CalendarUtil.addDay(loanStart, i*30-1));// 结束日期	
				loanRecord.getRepayRecordList().add(repayRecord);		
				
				repayAmt = repayAmt.add(expectRepayAmt);
				repayInt = repayInt.add(expectRepayInt);
				
			}
			loanRecord.setRepayedTerm(0);
			loanRecord.setDueRepayTerm(loanTerm/30);
			loanRecord.setInterest(repayInt);	//借条应还利息		
			loanRecord.setDueRepayAmount(repayAmt);//借条应还金额
			loanRecord.setDueRepayDate(CalendarUtil.addDay(new Date(), loanTerm-1));//借条到期时间

		//还款方式:按月等额本息
		}else if((loanTerm%30==0)&&repayType.equals(NfsLoanApply.RepayType.principalAndInterestByMonth)){

            //计算因子=（1+月利率）^还款月数
			MathContext mc = new MathContext(10); //精度为10位
			BigDecimal calFactor = monthRate.add(new BigDecimal(1)).pow(loanTerm/30,mc);
					
			//每月还款金额计算公式：每月还款额=贷款本金×[月利率×（1+月利率）^还款月数]÷[（1+月利率）^还款月数—1]
			if(monthRate.compareTo(BigDecimal.ZERO)==0) {
			   expectRepayAmt = repayPrn.divide(new BigDecimal(loanTerm/30),2,BigDecimal.ROUND_HALF_UP);
			}else {
			   expectRepayAmt = (repayPrn.multiply(monthRate,mc).multiply(calFactor, mc).divide(calFactor.subtract(new BigDecimal(1)), mc));
			}
			BigDecimal remainder = repayPrn;
			for (int i = 1; i <=loanTerm/30; i++) { 
				
				expectRepayInt = remainder.multiply(monthRate,mc);//每期应还利息=剩余本金*月利率
				expectRepayPrn = expectRepayAmt.subtract(expectRepayInt);// 每期应还本金				
				remainder = remainder.subtract(expectRepayPrn);
				
				NfsLoanRepayRecord repayRecord=new NfsLoanRepayRecord();
				repayRecord.setExpectRepayAmt(expectRepayAmt.setScale(2, BigDecimal.ROUND_HALF_UP));// 还款金额
				repayRecord.setExpectRepayPrn(expectRepayPrn.setScale(2, BigDecimal.ROUND_HALF_UP));// 还款本金 
				repayRecord.setExpectRepayInt(expectRepayInt.setScale(2, BigDecimal.ROUND_HALF_UP));// 还款利息 			
				repayRecord.setPeriodsSeq(i);// 还款期数
				repayRecord.setStatus(NfsLoanRepayRecord.Status.pending);
				repayRecord.setExpectRepayDate(CalendarUtil.addDay(loanStart, i*30-1));// 应还日期
				if(i == 1) {
					loanRecord.setDueRepayDate(CalendarUtil.addDay(loanStart, i*30-1));//借条到期时间
				}
				loanRecord.getRepayRecordList().add(repayRecord);		
				
				repayInt = repayInt.add(expectRepayInt);
				repayAmt = repayAmt.add(expectRepayAmt);
		    }
			loanRecord.setRepayedTerm(0);
			loanRecord.setDueRepayTerm(loanTerm/30);
			loanRecord.setInterest(repayInt);			
			loanRecord.setDueRepayAmount(repayAmt);//借条应还金额
		}

		return loanRecord;
	}

	/**
	 * 	计算利息
	 * @param days
	 * @param rate
	 * @param amount
	 * @return
	 */
	public static int getInterestByRate(int days, int rate, BigDecimal amount) {
		// 计算利息公式 利息 = 利率 * 天数 / 100 / 360 * 借款金额
		// 计算利率公式 利率 = 利息 / 天数 * 100 * 360 / 借款金额
		// 若利率大于24就向下取整，如果利率小于24就向上取整
		int insert = 0;
		try {
	
			BigDecimal b_days = new BigDecimal(days);
			BigDecimal b_rate = new BigDecimal(rate);
			// 计算出原利息
			BigDecimal multiply = b_rate.multiply(b_days).divide(new BigDecimal(100), 5, BigDecimal.ROUND_HALF_UP);
			BigDecimal multiply2 = multiply.divide(new BigDecimal(360), 5, BigDecimal.ROUND_HALF_UP).multiply(amount);
			BigDecimal setScale = multiply2.setScale(0, BigDecimal.ROUND_CEILING);
			BigDecimal setScale2 = setScale.divide(b_days, 5, BigDecimal.ROUND_HALF_UP);
			BigDecimal setScale3 = setScale2.multiply(new BigDecimal(100)).multiply(new BigDecimal(360)).divide(amount,
					5, BigDecimal.ROUND_HALF_UP);
			if (setScale3.intValue() >= 24) {
				BigDecimal setScale1 = multiply2.setScale(0, BigDecimal.ROUND_FLOOR);
				insert = setScale1.intValue();
			} else {
				insert = setScale.intValue();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		return insert;
	}
	/**
	 * 计算逾期利息
	 * @param amount 本金 分期时本金为每一期的应还金额
	 * @param oldIntRate  原利率
	 * @param overdueDays  逾期天数
 	 * @return
	 */
	public static BigDecimal calOverdueInterest(BigDecimal amount,BigDecimal overdueDays) {
		MathContext mc = new MathContext(10);
		BigDecimal overdueInterest = amount.multiply(overdueDays,mc).multiply(maxIntRate,mc).divide(oneHundred,mc).divide(yearDays,mc);
		return overdueInterest;
	}
	
	/**
	 * 获取利率
	 * @param amount 本金 
	 * @param days  天数
	 * @param interest  利息
 	 * @return 利率 20% 返回的就是0.2
	 */
	public static BigDecimal getIntRate(BigDecimal amount, Integer days, BigDecimal interest) {
		//利息*360/本金*天数  *100%
		BigDecimal a = interest.multiply(yearDays);
		BigDecimal b = amount.multiply(new BigDecimal(days));
		return a.divide(b, 6, BigDecimal.ROUND_HALF_UP) ;
	}
	/**
	 * 计算利息
	 * @return
	 */
	public static BigDecimal getBigDecimalInterest(BigDecimal intrate, BigDecimal amount, int term) {
		MathContext mc = new MathContext(10);
		BigDecimal interest = intrate.divide(oneHundred,mc).divide(yearDays,mc).multiply(amount,mc).multiply(new BigDecimal(term),mc);
		return interest;
	}
	
    public static void main(String[] args) {
        

    	BigDecimal intRate = getIntRate(new BigDecimal(750.00), 7, new BigDecimal(3));
    	System.out.println(intRate);

    
    }
    
}
