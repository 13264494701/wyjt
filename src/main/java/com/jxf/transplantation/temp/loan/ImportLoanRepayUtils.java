package com.jxf.transplantation.temp.loan;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxf.svc.utils.CalendarUtil;
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.Exceptions;
import com.jxf.svc.utils.StringUtils;
import com.jxf.transplantation.dbo.OldDBOperator;
import com.jxf.transplantation.dbo.OldDBOperatorFactory;
import com.jxf.transplantation.dbo.NowDBOperator;
import com.jxf.transplantation.dbo.NowDBOperatorFactory;

/**
 * @作者: xiaorongdian
 * @创建时间 :2019年1月23日 下午3:53:56
 * @功能说明: 迁移还款计划
 */
public class ImportLoanRepayUtils {
	private static Logger log = LoggerFactory.getLogger(ImportLoanMessageUtils.class);
	
	public static void main(String[] args) {
		importLoanRepay(1L, 8295105L);
	}
    
	public static int importLoanRepay(Long startId, Long endId) {
		OldDBOperator oldOperator = OldDBOperatorFactory.getDBOperator();
		NowDBOperator nowOperator = NowDBOperatorFactory.getNowDBOperator();
		PreparedStatement psmt = null;
		PreparedStatement psmtInsterLoanRepay = null;
		ResultSet rs = null;
		String countsql = " select *  from t_new_loan_staging_detail where id >= ? AND id <=? ORDER BY id";
//		String countsql = " select * from t_new_loan_log where id = 19864325  ";
		Date startDate = new Date();
		log.debug("开始时间:" + DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
		int i = 0;
		String oldId = "";
		try {
			psmt = oldOperator.getPreparedStatement(countsql);
			psmt.setLong(1, startId);
			psmt.setLong(2, endId);
			rs = psmt.executeQuery();
			
			//插入现还款计划
			String insertRepayRecordSql = "INSERT INTO `NFS_LOAN_REPAY_RECORD` ("
					+"`id`," //1
					+"`loan_id`,"//2
					+"`periods_seq`,"//3
					+"`expect_repay_amt`,"//4
					+"`expect_repay_prn`,"//5
					+"`expect_repay_int`,"//6
					+"`expect_repay_date`,"//7
					+"`actual_repay_amt`,"//8
					+"`actual_repay_date`,"//9
					+"`status`,"//10
					+"`create_by`,"
					+"`create_time`,"//11
					+"`update_by`,"
					+"`update_time`,"//12
					+"`del_flag`"
				    +") VALUES (?,?,?,?,?,?,?,?,?,?, '999999',?,'999999',?,'0');";
			psmtInsterLoanRepay = nowOperator.getPreparedStatement(insertRepayRecordSql);
			
			while (rs.next()) {
				++i;
				boolean success = true;
				oldId = rs.getString("id");
				log.debug("第{}条数据迁移,还款计划id:{}",i,oldId);
				success = transplantLoanMessage(rs, psmtInsterLoanRepay,i,oldId);
				if (success) {
					//log.debug("第" + i + "条迁移成功");
				} else {
					i--;
				}
			}
			psmt.close();

			psmtInsterLoanRepay.executeBatch();
			psmtInsterLoanRepay.clearBatch();
			psmtInsterLoanRepay.close();
			
			log.debug("结束,共" + i + "条数据迁移成功");
			log.debug("用时:" + DateUtils.pastMinutes(startDate) + "分钟");
		} catch (SQLException e) {
			log.error("第{}条数据迁移出错了!出错了!出错了!出错了!出错了!出错了!,还款计划id:{}",i,oldId);
			log.error(Exceptions.getStackTraceAsString(e));
		}catch (Exception e) {
			log.error("第{}条数据迁移出错了!出错了!出错了!出错了!出错了!出错了!,还款计划id:{}",i,oldId);
			log.error(Exceptions.getStackTraceAsString(e));
		}
		return i;

	}

	private static boolean transplantLoanMessage(ResultSet rs, PreparedStatement psmtInsterLoanRepay, int i, String oldId) throws SQLException {
		
		//还款计划id
		String repayRecordId = rs.getString("loanId");
		//分期编号
		String no = rs.getString("no");
		//分期状态(0:未还，120 申请延期关闭,140 部分还款的关闭 150 逾期 已还，200： 已还)
		String status = rs.getString("status");
		//借款单总期数
		String repayPeriod = rs.getString("repayPeriod");
		//放款人id
		String lenderId = rs.getString("lenderId");
		//借款人id
		String borrowerId = rs.getString("borrowerId");
		//当前分期应还金额
		String money = rs.getString("money");
		//当前分期应还利息
		String interest = rs.getString("interest");
		//原定还款时间
		String expectRepayDate = rs.getString("expectRepayDate");
		//实际还款时间
		String realRepayDate = rs.getString("realRepayDate");
		//逾期状态
		String delay = rs.getString("delay");
		//版本号
		String version = rs.getString("version");
		
		psmtInsterLoanRepay.setString(1, oldId);
		
		psmtInsterLoanRepay.setString(2, repayRecordId);//借条Id
		
		psmtInsterLoanRepay.setString(3, no);//第几期
		
		BigDecimal amt = new BigDecimal(money == null ? "0" : money).add(new BigDecimal(interest == null ? "0" : interest));
		psmtInsterLoanRepay.setString(4, StringUtils.decimalToStr(amt, 2));//本期还金额
		
		psmtInsterLoanRepay.setString(5, money);//本期还本金
		
		psmtInsterLoanRepay.setString(6, interest);//本期还利息
		
		psmtInsterLoanRepay.setString(7, expectRepayDate);//应还日期
		
		psmtInsterLoanRepay.setString(8, "0");//实际还款 先给个0 TODO
		
		psmtInsterLoanRepay.setString(9, realRepayDate);//实际还款日
		
		Integer statusInt = Integer.parseInt(status) ;
		
		if(statusInt == 150 ||statusInt ==  200){//已还
			psmtInsterLoanRepay.setInt(10, 1);//状态
		}else if(statusInt == 0){
			Date repayDate = DateUtils.parseDate(expectRepayDate); 
			Integer passDays = CalendarUtil.getIntervalDays2(new Date(), repayDate);
			if(passDays < 0){//逾期
				psmtInsterLoanRepay.setInt(10, 3);
			}else{
				psmtInsterLoanRepay.setInt(10, 0);
			}
		}else{
			return false;
		}
		Date nowDate = new Date();
		String nowDateStr = DateUtils.formatDate(nowDate, "yyyy-MM-dd HH:mm:ss");
		psmtInsterLoanRepay.setString(11, nowDateStr);
		psmtInsterLoanRepay.setString(12, nowDateStr);
		
		psmtInsterLoanRepay.addBatch();
		
		return true;
	}
}
