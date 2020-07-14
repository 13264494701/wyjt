package com.jxf.transplantation.temp.loan;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.Exceptions;
import com.jxf.transplantation.dbo.NowDBOperator;
import com.jxf.transplantation.dbo.NowDBOperatorFactory;

/**
 * @作者: xiaorongdian
 * @创建时间 :2019年1月23日 下午3:53:56
 * @功能说明: 分期状态有问题2 全都还了 还在待还中
 */
public class UpdateRepayLoanStatusUtils {
	private static Logger log = LoggerFactory.getLogger(ImportLoanMessageUtils.class);
	
	public static void main(String[] args) {
		updateLoanRepay(1L, 287678846990422016L);
	}
    
	public static int updateLoanRepay(Long startId, Long endId) {
		NowDBOperator nowOperator = NowDBOperatorFactory.getNowDBOperator();
		PreparedStatement psmt = null;
		PreparedStatement psmtrepayRecord = null;
		PreparedStatement psmtUpdateDueRepayRecord = null;
		ResultSet rs = null;
		String countsql = " select id from NFS_LOAN_RECORD where  id >? and id <? and repay_type = 1 and status != 1 AND id<'29320555210277273' ";
		//String countsql = " select id from NFS_LOAN_RECORD where  id =? and repay_type = 1 and status = 0 AND id<'29320555210277273' ";
		
		String repayRecordSql = " select * FROM NFS_LOAN_REPAY_RECORD where loan_id = ? order by periods_seq desc limit 1 ";
		
		String updateRepayRecordSql = " UPDATE NFS_LOAN_RECORD  SET `status` = 1 WHERE `id` = ? ";
		
		Date startDate = new Date();
		log.debug("开始时间:" + DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
		int i = 0;
		String oldId = "";
		try {
			psmt = nowOperator.getPreparedStatement(countsql);
			psmt.setLong(1, startId);
			psmt.setLong(2, endId);
			rs = psmt.executeQuery();
			
			psmtUpdateDueRepayRecord = nowOperator.getPreparedStatement(updateRepayRecordSql);
			psmtrepayRecord = nowOperator.getPreparedStatement(repayRecordSql);
			
			while (rs.next()) {
				++i;
				boolean success = true;
				oldId = rs.getString("id");
				log.debug("第{}条数据迁移,还款计划id:{}",i,oldId);
				success = updateLoanStatus(rs,i,oldId,psmtUpdateDueRepayRecord,psmtrepayRecord);
				if (success) {
				} else {
					i--;
				}
			}
			psmt.close();
			psmtUpdateDueRepayRecord.close();
			psmtrepayRecord.close();
			NowDBOperatorFactory.addNowDBOperator(nowOperator);
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

	private static boolean updateLoanStatus(ResultSet rs, int i, String recordId, PreparedStatement psmtUpdateDueRepayRecord
			, PreparedStatement psmtRepayRecord) throws SQLException {
		psmtRepayRecord.setString(1, recordId);
		ResultSet repayRecordRs = psmtRepayRecord.executeQuery();
		boolean needUpdate = false;
		while(repayRecordRs.next()){
			String status = repayRecordRs.getString("status");
			if(StringUtils.equals(status, "1")){
				needUpdate = true;
			} 
		}
		
		if(needUpdate){
			psmtUpdateDueRepayRecord.setString(1, recordId);
			psmtUpdateDueRepayRecord.executeUpdate();
			return true;
		}else{
			return false;
		}
		
	}
}
