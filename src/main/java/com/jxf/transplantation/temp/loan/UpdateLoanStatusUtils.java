package com.jxf.transplantation.temp.loan;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.Exceptions;
import com.jxf.transplantation.dbo.NowDBOperator;
import com.jxf.transplantation.dbo.NowDBOperatorFactory;

/**
 * @作者: xiaorongdian
 * @创建时间 :2019年1月23日 下午3:53:56
 * @功能说明: 分期状态问题 不应该逾期--->待还
 */
public class UpdateLoanStatusUtils {
	private static Logger log = LoggerFactory.getLogger(ImportLoanMessageUtils.class);
	
	public static void main(String[] args) {
		updateLoanRepay(1L, 287678846990422016L);
	}
    
	public static int updateLoanRepay(Long startId, Long endId) {
		NowDBOperator nowOperator = NowDBOperatorFactory.getNowDBOperator();
		PreparedStatement psmt = null;
		PreparedStatement psmtRepayRecord = null;
		PreparedStatement psmtUpdateRecord = null;
		PreparedStatement psmtUpdateRepayRecord = null;
		PreparedStatement psmtUpdateDueRepayRecord = null;
		ResultSet rs = null;
		String countsql = " select id from NFS_LOAN_RECORD where repay_type = 1 and status = 2 and id > ? and id < ? ";
		//String countsql = " select id from NFS_LOAN_RECORD where repay_type = 1 and status = 2 and id = '280340085340246016' ";
		
		String repayRecordSql = " select * FROM NFS_LOAN_REPAY_RECORD where loan_id = ? order by periods_seq ";
		String updateRecordSql = " UPDATE NFS_LOAN_RECORD SET `status` = 0 , `due_repay_date` = ? WHERE id = ? ";
		
		String updateDueRepayDateSql = " UPDATE NFS_LOAN_RECORD SET  `due_repay_date` = ? WHERE id = ? ";
		
		String updateRepayRecordSql = " UPDATE NFS_LOAN_REPAY_RECORD  SET `status` = 0 WHERE `expect_repay_date` > ? and  `status` = 3 ";
		
		Date startDate = new Date();
		log.debug("开始时间:" + DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
		int i = 0;
		String oldId = "";
		try {
			psmt = nowOperator.getPreparedStatement(countsql);
			psmt.setLong(1, startId);
			psmt.setLong(2, endId);
			rs = psmt.executeQuery();
			
			psmtRepayRecord = nowOperator.getPreparedStatement(repayRecordSql);
			psmtUpdateRecord = nowOperator.getPreparedStatement(updateRecordSql);
			psmtUpdateRepayRecord = nowOperator.getPreparedStatement(updateRepayRecordSql);
			psmtUpdateDueRepayRecord = nowOperator.getPreparedStatement(updateDueRepayDateSql);
			
			Date nowDate = new Date();
			String formatDate = DateUtils.formatDate(nowDate, "yyyy-MM-dd");
			psmtUpdateRepayRecord.setString(1, formatDate);
			psmtUpdateRepayRecord.executeUpdate();
			
			while (rs.next()) {
				++i;
				boolean success = true;
				oldId = rs.getString("id");
				log.debug("第{}条数据迁移,还款计划id:{}",i,oldId);
				success = updateLoanStatus(rs,i,oldId,psmtRepayRecord,psmtUpdateRecord,psmtUpdateDueRepayRecord);
				if (success) {
				} else {
					i--;
				}
			}
			psmt.close();
			psmtRepayRecord.close();
			psmtUpdateRecord.close();
			psmtUpdateRepayRecord.close();
			psmtUpdateDueRepayRecord.close();

			log.debug("结束,共" + i + "条数据迁移成功");
			log.debug("用时:" + DateUtils.pastMinutes(startDate) + "分钟");
		} catch (SQLException e) {
			log.error("第{}条数据迁移出错了!出错了!出错了!出错了!出错了!出错了!,id:{}",i,oldId);
			log.error(Exceptions.getStackTraceAsString(e));
		}catch (Exception e) {
			log.error("第{}条数据迁移出错了!出错了!出错了!出错了!出错了!出错了!,id:{}",i,oldId);
			log.error(Exceptions.getStackTraceAsString(e));
		}
		return i;

	}

	private static boolean updateLoanStatus(ResultSet rs, int i, String oldId, PreparedStatement psmtRepayRecord
			, PreparedStatement psmtUpdateRecord, PreparedStatement psmtUpdateDueRepayRecord) throws SQLException {
		
		//已逾期分期借条的id
		String id = rs.getString("id");
		
		psmtRepayRecord.setString(1, id);
		ResultSet repayRecrodRs = psmtRepayRecord.executeQuery();
		boolean needUpdate = false;
		Date newDate = new Date();
		Date dueRepayDate = null;//更改后应还日期
		int term = 0;
		while(repayRecrodRs.next()){
			String expectRepayDate = repayRecrodRs.getString("expect_repay_date");//应还日期
			String status = repayRecrodRs.getString("status");//状态
			Date parseDateExpectRepayDate = DateUtils.parseDate(expectRepayDate);
			if(dueRepayDate == null && !status.equals("1")){//不是已还的第一条数据
				dueRepayDate = parseDateExpectRepayDate;
			}
		}
		if(dueRepayDate != null){
			term = DateUtils.getDifferenceOfTwoDate(dueRepayDate , newDate);
		}
		if(term > 0){//需要更改状态和还款时间
			needUpdate = true;
		}
		
		if(needUpdate){
			psmtUpdateRecord.setString(1, DateUtils.formatDate(dueRepayDate, "yyyy-MM-dd"));
			psmtUpdateRecord.setString(2, id);
			psmtUpdateRecord.executeUpdate();
			return true;
		}else{//只改还款时间
			psmtUpdateDueRepayRecord.setString(1, DateUtils.formatDate(dueRepayDate, "yyyy-MM-dd"));
			psmtUpdateDueRepayRecord.setString(2, id);
			psmtUpdateDueRepayRecord.executeUpdate();
			return true;
		}
	}
}
