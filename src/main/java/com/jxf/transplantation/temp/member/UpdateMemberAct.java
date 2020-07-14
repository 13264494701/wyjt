package com.jxf.transplantation.temp.member;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.Exceptions;
import com.jxf.transplantation.dbo.NowDBOperator;
import com.jxf.transplantation.dbo.NowDBOperatorFactory;



/**
 * @作者: xrd
 * @创建时间 :2019年1月3日
 * @功能说明:更新所有用户待收待还金额   
 */
public class UpdateMemberAct {
	
	private static Logger log = LoggerFactory.getLogger(UpdateMemberAct.class);
	
	
	static HashMap<String,String> map = new HashMap<String , String>();
	
	public static void main(String[] args)  {
		updateAct(1L,1111111111L);
    }
	
	public static int updateAct(long startId, long endId)  {
		NowDBOperator nowOperator = NowDBOperatorFactory.getNowDBOperator();
		PreparedStatement psmt = null;
		PreparedStatement psmtLoanLoanee = null;
		PreparedStatement psmtLoanLoaner = null;
		PreparedStatement psmtToPaid = null;
		PreparedStatement psmtDueIn = null;
		PreparedStatement psmtSum = null;
		
		ResultSet rs = null;
		
		String quereSql = "SELECT a.id from MEM_MEMBER a where a.id >= ? and a.id <= ?" ;
		//String quereSql = "SELECT a.id from MEM_MEMBER a" ;
		
		//待还集合
		String loanLoaneeSql = "select id,amount,interest,repay_type from NFS_LOAN_RECORD a where a.loanee_id = ? and a.status != 1 " ;
		
		//待收集合
		String loanLoanerSql = "select id,amount,interest,repay_type from NFS_LOAN_RECORD a where a.loaner_id = ? and a.status != 1 " ;
		
		//待还
		String toPaidSql = "update MEM_MEMBER_ACT a set a.cur_bal = ? where a.member_id = ? and a.sub_no = '0005' " ;
		
		//待收
		String dueInSql = "update MEM_MEMBER_ACT a set a.cur_bal = ? where a.member_id = ? and a.sub_no = '0006' " ;
	
		//查该分期借条未还的计划金额之和
		String sumSql = "SELECT sum(a.expect_repay_amt) repayAmt FROM NFS_LOAN_REPAY_RECORD a where a.loan_id = ? AND (a.`status` = 0 OR a.`status` = 3); " ;
		
		
		log.debug("开始时间:{}",DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
	    long startTime=System.currentTimeMillis();
	   	int i = 0;
	   	String memberId = "";
	   	try {
			psmt = nowOperator.getPreparedStatement(quereSql);
			psmt.setLong(1, startId);
		    psmt.setLong(2, endId);
			rs = psmt.executeQuery();
			
			psmtLoanLoanee = nowOperator.getPreparedStatement(loanLoaneeSql);
			
			psmtLoanLoaner = nowOperator.getPreparedStatement(loanLoanerSql);
			
			psmtToPaid = nowOperator.getPreparedStatement(toPaidSql);
			
			psmtDueIn = nowOperator.getPreparedStatement(dueInSql);
			
			psmtSum = nowOperator.getPreparedStatement(sumSql);
			
		while (rs.next()) {
			++i;
			boolean success = true;
			memberId = rs.getString("id");
			log.debug("第{}条数据迁移,用户ID:{}",i,memberId);
			success = transplantLoanRecord(rs,i,memberId,psmtLoanLoanee,psmtLoanLoaner,psmtToPaid,psmtDueIn,psmtSum);
			if(success){
			}else{
				i--;
			}
		}
	    long endTime=System.currentTimeMillis();
	    float excTime=(float)(endTime-startTime)/1000;
	    log.debug("一组结束,共{}条数据迁移成功",i);
	    log.debug("用时:{}毫秒",excTime);
		} catch (SQLException e) {
			log.error("第{}条数据迁移出错了!出错了!出错了!出错了!出错了!,用户ID:{}",i,memberId);
			log.error(Exceptions.getStackTraceAsString(e));
		} catch (Exception e) {
			log.error("第{}条数据迁移出错了!出错了!出错了!出错了!出错了!,用户ID:{}",i,memberId);
			log.error(Exceptions.getStackTraceAsString(e));
		}finally {
		    try {
			    psmt.close();	
			    psmtLoanLoanee.close();	
			    psmtLoanLoaner.close();	
			    psmtToPaid.close();	
			    psmtDueIn.close();	
			    psmtSum.close();	
			   
			    NowDBOperatorFactory.addNowDBOperator(nowOperator);
			} catch (SQLException e) {
				log.error("第{}条数据迁移出错,用户ID:{}",i,memberId);
				log.error(Exceptions.getStackTraceAsString(e));
			} catch (Exception e) {
				log.error("第{}条数据迁移出错,用户ID:{}",i,memberId);
				log.error(Exceptions.getStackTraceAsString(e));
			}
		}
	   	
	   	return i;

	}
	
    private static boolean transplantLoanRecord(ResultSet rs, int i, String memberId, PreparedStatement psmtLoanLoanee
    		, PreparedStatement psmtLoanLoaner, PreparedStatement psmtToPaid, PreparedStatement psmtDueIn, PreparedStatement psmtSum) throws Exception {
    	//待还
    	psmtLoanLoanee.setString(1, memberId);
    	ResultSet toPaidLoan = psmtLoanLoanee.executeQuery();//待还借条集合
    	BigDecimal toPaidBig = BigDecimal.ZERO;//总待还
    	while(toPaidLoan.next()){
    		String loanId = toPaidLoan.getString("id");
    		String amount = toPaidLoan.getString("amount");
    		String interest = toPaidLoan.getString("interest");
    		Integer repayType = toPaidLoan.getInt("repay_type");
    		if(repayType == 0){//全额借条
    			toPaidBig = toPaidBig.add(new BigDecimal(amount).add(new BigDecimal(interest)));
    		}else{//分期借条
    			psmtSum.setString(1, loanId);
    			ResultSet pendingSum = psmtSum.executeQuery();
    			while(pendingSum.next()){
    				String repayAmtSum = pendingSum.getString("repayAmt");
    				toPaidBig = toPaidBig.add(new BigDecimal(repayAmtSum));
    			}
    		}
    	}
    	psmtToPaid.setString(1, toPaidBig.toString());
    	psmtToPaid.setString(2, memberId);
    	psmtToPaid.executeUpdate();
    	
    	//待收
    	psmtLoanLoaner.setString(1, memberId);
    	ResultSet toDueInLoan = psmtLoanLoaner.executeQuery();//待收借条集合
    	BigDecimal toDueInBig = BigDecimal.ZERO;//总待收
    	while(toDueInLoan.next()){
    		Long loanId = toDueInLoan.getLong("id");
    		String amount = toDueInLoan.getString("amount");
    		String interest = toDueInLoan.getString("interest");
    		Integer repayType = toDueInLoan.getInt("repay_type");
    		if(repayType == 0){//全额借条
    			toDueInBig = toDueInBig.add(new BigDecimal(amount).add(new BigDecimal(interest)));
    		}else{//分期借条
    			psmtSum.setLong(1, loanId);
    			ResultSet pendingSum = psmtSum.executeQuery();
    			while(pendingSum.next()){
    				String repayAmtSum = pendingSum.getString("repayAmt");
    				toDueInBig = toDueInBig.add(new BigDecimal(repayAmtSum));
    			}
    		}
    	}
    	psmtDueIn.setString(1, toDueInBig.toString());
    	psmtDueIn.setString(2, memberId);
    	psmtDueIn.executeUpdate();
    	
		return true;
	}
}
