package com.jxf.transplantation.temp.loan;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.Exceptions;
import com.jxf.svc.utils.StringUtils;
import com.jxf.transplantation.dbo.OldDBOperator;
import com.jxf.transplantation.dbo.OldDBOperatorFactory;
import com.jxf.transplantation.dbo.NowDBOperator;
import com.jxf.transplantation.dbo.NowDBOperatorFactory;


/**
 * @作者: xiaorongdian
 * @创建时间 :2019年1月17日 下午2:04:26
 * @功能说明:迁移借条对话气泡
 */
public class ImportLoanMessageUtils {
	
	private static Logger log = LoggerFactory.getLogger(ImportLoanMessageUtils.class);
	
	public static void main(String[] args) {
		importLoanMessage(250671L, 300000L);
	}
    
	public static int importLoanMessage(Long startId, Long endId) {
		OldDBOperator oldOperator = OldDBOperatorFactory.getDBOperator();
		NowDBOperator nowOperator = NowDBOperatorFactory.getNowDBOperator();
		PreparedStatement psmt = null;
		PreparedStatement psmtInsertMessage = null;
		PreparedStatement psmtCountSql = null;
		PreparedStatement psmtOldLoanSql = null;
		ResultSet rs = null;
		String countsql = " select * from t_new_loan_log a where a.id >= ? AND id <=? ORDER BY id";
//		String countsql = " select * from t_new_loan_log where id = 19864325  ";
		Date startDate = new Date();
		log.debug("开始时间:" + DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
		int i = 0;
		String id = "";
		try {
			psmt = oldOperator.getPreparedStatement(countsql);
			psmt.setLong(1, startId);
			psmt.setLong(2, endId);
			rs = psmt.executeQuery();
			
			//去重
			String countSql = "select count(1) as count from NFS_LOAN_DETAIL_MESSAGE a where a.id = ? ";
			psmtCountSql = nowOperator.getPreparedStatement(countSql);
			
			//去重
			String oldLoanSql = "SELECT a.repayLatestDate,a.totalMoney,a.totalInterest,a.compensationSum,a.partRepaySum FROM t_new_loan a WHERE a.id = ? ";
			psmtOldLoanSql = oldOperator.getPreparedStatement(oldLoanSql);
			
			String insertTransferSql = "INSERT INTO `NFS_LOAN_DETAIL_MESSAGE`("
					+"`id`,"//1
					+"`detail_id`,"//2
					+"`member_id`,"//3
					+"`message_id`,"//4
					+"`type`,"//5
					+"`note`,"//6
					+"`rmk`,"//7
					+"`create_by`,"
					+"`create_time`,"//8
					+"`update_by`,"
					+"`update_time`,"//9
					+"`del_flag`"
					+")VALUES (?,?,?,?,?,?,?,'999999',?,'999999',?,'0')";
			psmtInsertMessage = nowOperator.getPreparedStatement(insertTransferSql);
			
			while (rs.next()) {
				id = rs.getString("id");
				log.debug("第{}条数据迁移 id:{}",i,id);
				++i;
				boolean success = true;
					success = transplantLoanMessage(rs, psmtInsertMessage, psmtCountSql,psmtOldLoanSql,i);
				if (success) {
				} else {
					i--;
				}
			}
			
			psmt.close();
			
			psmtInsertMessage.executeBatch();
			psmtInsertMessage.clearBatch();
			psmtInsertMessage.close();
			
			psmtCountSql.close();
			psmtOldLoanSql.close();

			log.debug("结束,共" + i + "条数据迁移成功");
			log.debug("用时:" + DateUtils.pastMinutes(startDate) + "分钟");
		} catch (SQLException e) {
			log.error("第{}条数据迁移出错了!出错了!出错了!出错了!出错了! id:{}",i,id);
			log.error(Exceptions.getStackTraceAsString(e));
		}catch (Exception e){
			log.error("第{}条数据迁移出错了!出错了!出错了!出错了!出错了! id:{}",i,id);
			log.error(Exceptions.getStackTraceAsString(e));
		}
		return i;

	}

	private static boolean transplantLoanMessage(ResultSet rs, PreparedStatement psmtInsertMessage, PreparedStatement psmtCountSql
			, PreparedStatement psmtOldLoanSql, int i) throws SQLException {
		String oldId = rs.getString("id");
		//log.debug("开始第{}条数据迁移 id:{}",i,oldId);
		//去重
		boolean repeat = getRepeat(psmtCountSql,oldId);
		if(repeat){
			return false;
		}
		//借款单id
		String loanId = rs.getString("loanId");
		//用户id
		String userId = rs.getString("userId");
		//状态 NewLoanStatus
		String status = rs.getString("status");
		//对话ID NewLoanMessage
		Integer type = rs.getInt("type");
		//创建日期
		String createDate = rs.getString("createDate");
		//备注
		String note = rs.getString("note");


		psmtInsertMessage.setLong(1, Long.parseLong(oldId));
		psmtInsertMessage.setString(2, loanId);//原借条ID是现在的 apply/detail/record 三个表的id都是一样的
		psmtInsertMessage.setString(3, userId);
		psmtInsertMessage.setInt(4, type);
		psmtInsertMessage.setString(5, status);
		if(type >= 3 && type <=9 && type != 7 && type != 4){
			String messageNote = getNote(loanId,status,type,psmtOldLoanSql,note);
			psmtInsertMessage.setString(6, messageNote);
		}else{
			psmtInsertMessage.setString(6, note);
		}
		
		psmtInsertMessage.setString(7, "");
		psmtInsertMessage.setString(8, createDate);
		Date nowDate = new Date();
		psmtInsertMessage.setString(9, DateUtils.formatDate(nowDate, "yyyy-MM-dd HH:mm:ss"));
		
		psmtInsertMessage.addBatch();
		return true;
	}
	
	private static String getNote(String loanId, String status, Integer type,
			PreparedStatement psmtOldLoanSql, String note) throws SQLException {
		HashMap<String,String> nowNote = new HashMap<String,String>();
		psmtOldLoanSql.setString(1, loanId);
		ResultSet rs = psmtOldLoanSql.executeQuery();
		String repayLatestDate = "";
		String totalMoney = "";
		String totalInterest = "";
		String compensationSum = "";
		String partRepaySum = "";
		while(rs.next()){
			repayLatestDate = rs.getString("repayLatestDate");//协议还款时间
			totalMoney = rs.getString("totalMoney");//本金
			totalInterest = rs.getString("totalInterest");//利息
			compensationSum = rs.getString("compensationSum");//累计延期补偿金
			partRepaySum = rs.getString("partRepaySum");//累计部分还款补偿金
		}
		Map<String,String> noteHash = (Map) JSON.parse(note);
		if(type == 3){//借款人申请延期  原来{"lastRepayDate":"2016-09-19 23:59:59","bcMoney":"0"} 
			//现在 {"currentRepayDate":"2019-01-14","oldAmount":"500.00","lastRepayDate":"2019-01-30","delayInterest":"4","oldInterest":"2.33"}
			String lastRepayDate = noteHash.get("lastRepayDate");
			String bcMoney = noteHash.get("bcMoney");
			nowNote.put("currentRepayDate", repayLatestDate);
			nowNote.put("oldAmount", totalMoney);
			nowNote.put("lastRepayDate", lastRepayDate);
			nowNote.put("delayInterest", bcMoney);
			nowNote.put("oldInterest", totalInterest);
		}else if(type == 6){//借款人申请部分还款   {"lastRepayDate":"2017-01-18 23:59:59","bcMoney":"0","money":"1000"}
			//现在 {"remainPayment":"79.93","currentRepayDate":"2019-01-07","oldAmount":"99.93","lastRepayDate":"2019-01-07","delayInterest":"0.00","oldInterest":"0.00","partialPayment":"20.00"}
			String lastRepayDate = noteHash.get("lastRepayDate");
			String bcMoney = noteHash.get("bcMoney");
			String money = noteHash.get("money");
			nowNote.put("currentRepayDate", repayLatestDate);
			nowNote.put("oldAmount", totalMoney);
			nowNote.put("lastRepayDate", lastRepayDate);
			nowNote.put("delayInterest", bcMoney);
			nowNote.put("oldInterest", totalInterest);
			nowNote.put("partialPayment", money);
		}else if(type == 8){//放款人申请部分还款 原{"lastRepayDate":"2018-07-17 23:59:59 23:59:59","bcMoney":"11","money":"100"}
			//现在 {"remainPayment":"704.67","currentRepayDate":"2019-01-21","oldAmount":"804.67","lastRepayDate":"2019-01-22","oldInterest":"0.00","delayInterest":"0","partialPayment":"100"}
			String lastRepayDate = noteHash.get("lastRepayDate");
			String bcMoney = noteHash.get("bcMoney");
			String money = noteHash.get("money");
			/**
			 * loan.getTotalMoney() + loan.getTotalInterest() + bcMoney + loan.getCompensationSum()
							+ loan.getPartRepaySum() - money)
			 */
			BigDecimal amount = new BigDecimal(StringUtils.isNotEmpty(totalMoney) ? totalMoney:"0");
			BigDecimal interest = new BigDecimal(StringUtils.isNotEmpty(totalInterest) ? totalInterest:"0");
			BigDecimal bcMoneyBig = new BigDecimal(StringUtils.isNotEmpty(bcMoney) ? bcMoney:"0");
			BigDecimal compensationSumBig = new BigDecimal( StringUtils.isNotEmpty(compensationSum) ? compensationSum:"0");
			BigDecimal partRepaySumBig = new BigDecimal(StringUtils.isNotEmpty(partRepaySum) ? partRepaySum:"0");
			BigDecimal moneyBig = new BigDecimal(money);
			BigDecimal remainPayment = amount.add(interest).add(bcMoneyBig).add(compensationSumBig).add(partRepaySumBig).subtract(moneyBig);
			nowNote.put("remainPayment", StringUtils.decimalToStr(remainPayment, 2));
			nowNote.put("currentRepayDate", repayLatestDate);
			nowNote.put("oldAmount", totalMoney);
			nowNote.put("lastRepayDate", lastRepayDate);
			nowNote.put("oldInterest", totalInterest);
			nowNote.put("delayInterest", bcMoney);
			nowNote.put("partialPayment", money);
		}else if(type == 9){//放款人申请延期还款 原{"lastRepayDate":"2018-05-14 23:59:59","bcMoney":"50"}
			//现在 {"currentRepayDate":"2019-01-21","oldAmount":"600.00","lastRepayDate":"2019-01-22","delayInterest":"0","oldInterest":"2.00"}
			String lastRepayDate = noteHash.get("lastRepayDate");
			String bcMoney = noteHash.get("bcMoney");
			nowNote.put("currentRepayDate", repayLatestDate);
			nowNote.put("oldAmount", totalMoney);
			nowNote.put("lastRepayDate", lastRepayDate);
			nowNote.put("delayInterest", bcMoney);
			nowNote.put("oldInterest", totalInterest);
		}
		return JSON.toJSONString(nowNote);
	}

	private static boolean getRepeat(PreparedStatement psmt,
			String oldId) throws SQLException {
		psmt.setString(1, oldId);
		ResultSet rs = psmt.executeQuery();
		Integer count = 0;
		while(rs.next()){
			count = rs.getInt("count");
		}
		if(count > 0){
			return true;
		}else{
			return false;
		}
	}
}
