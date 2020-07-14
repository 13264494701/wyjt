package com.jxf.transplantation.temp.member;


import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.jxf.loan.entity.NfsLoanApply.RepayType;
import com.jxf.loan.entity.NfsLoanRecord;
import com.jxf.loan.service.NfsLoanRecordService;
import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberAct;
import com.jxf.mem.service.MemberActService;

import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.Exceptions;
import com.jxf.transplantation.dbo.NowDBOperator;
import com.jxf.transplantation.dbo.NowDBOperatorFactory;



/**
 * @作者: xrd
 * @创建时间 :2019年1月3日
 * @功能说明:更新所有用户待收待还金额   
 */
public class UpdateMemberAct2 {
	
	private static Logger log = LoggerFactory.getLogger(UpdateMemberAct2.class);
	
	@Autowired
	private NfsLoanRecordService loanRecordService;
	@Autowired
	private MemberActService memberActService;

	
	
	static HashMap<String,String> map = new HashMap<String , String>();
	

	
	public  int updateAct(long startId, long endId)  {
		NowDBOperator nowOperator = NowDBOperatorFactory.getNowDBOperator();
		PreparedStatement psmt = null;		
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
		String sumSql = "SELECT sum(a.expect_repay_amt) repayAmt FROM NFS_LOAN_REPAY_RECORD a where a.loan_id = ? WHERE a.`status` = 0 OR a.`status` = 3; " ;
		
		
		log.debug("开始时间:{}",DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
	    long startTime=System.currentTimeMillis();
	   	int i = 0;
	   	Long memberId = null;
	   	try {
			psmt = nowOperator.getPreparedStatement(quereSql);
			psmt.setLong(1, startId);
		    psmt.setLong(2, endId);
			rs = psmt.executeQuery();
			

			
		while (rs.next()) {
			++i;

			memberId = rs.getLong("id");
			BigDecimal toPayment = BigDecimal.ZERO;
			BigDecimal dueIn = BigDecimal.ZERO;
			log.warn("第{}条数据迁移,用户ID:{}",i,memberId);
			//借款人待还
			NfsLoanRecord nfsLoanRecord = new NfsLoanRecord();
			nfsLoanRecord.setLoanee(new Member(memberId));
			nfsLoanRecord.setRepayType(RepayType.oneTimePrincipalAndInterest);
			nfsLoanRecord.setStatus(NfsLoanRecord.Status.penddingRepay);
			List<NfsLoanRecord> list1 = loanRecordService.findList(nfsLoanRecord);
			for(NfsLoanRecord loan:list1){
				toPayment = toPayment.add(loan.getDueRepayAmount());
			}
			
			nfsLoanRecord.setStatus(NfsLoanRecord.Status.overdue);
			List<NfsLoanRecord> list2 = loanRecordService.findList(nfsLoanRecord);
			for(NfsLoanRecord loan:list2){
				toPayment = toPayment.add(loan.getDueRepayAmount());
			}
			MemberAct memberAct5 = memberActService.getMemberAct(new Member(memberId), "0005");
			memberAct5.setCurBal(toPayment);
			memberActService.save(memberAct5);
			
			//放款人待收
			nfsLoanRecord.setLoaner(new Member(memberId));
			nfsLoanRecord.setLoanee(null);
			nfsLoanRecord.setRepayType(RepayType.oneTimePrincipalAndInterest);
			nfsLoanRecord.setStatus(NfsLoanRecord.Status.penddingRepay);
			List<NfsLoanRecord> list3 = loanRecordService.findList(nfsLoanRecord);
			for(NfsLoanRecord loan:list3){
				dueIn = dueIn.add(loan.getDueRepayAmount());
			}
			nfsLoanRecord.setStatus(NfsLoanRecord.Status.overdue);
			List<NfsLoanRecord> list4 = loanRecordService.findList(nfsLoanRecord);
			for(NfsLoanRecord loan:list4){
				dueIn = dueIn.add(loan.getDueRepayAmount());
			}
			
			MemberAct memberAct6 = memberActService.getMemberAct(new Member(memberId), "0006");
			memberAct6.setCurBal(dueIn);
			memberActService.save(memberAct6);

		}
	    long endTime=System.currentTimeMillis();
	    float excTime=(float)(endTime-startTime)/1000;
	    log.debug("一组结束,共{}条数据迁移成功",i);
	    log.debug("用时:{}毫秒",excTime);
		}  catch (Exception e) {
			log.error("第{}条数据迁移出错了!出错了!出错了!出错了!出错了!,用户ID:{}",i,memberId);
			log.error(Exceptions.getStackTraceAsString(e));
		}finally {
		    try {
			    psmt.close();				   
			    NowDBOperatorFactory.addNowDBOperator(nowOperator);
			}  catch (Exception e) {
				log.error("第{}条数据迁移出错,用户ID:{}",i,memberId);
				log.error(Exceptions.getStackTraceAsString(e));
			}
		}
	   	
	   	return i;

	}
	
   
}
