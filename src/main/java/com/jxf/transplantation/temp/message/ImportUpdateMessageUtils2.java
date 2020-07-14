package com.jxf.transplantation.temp.message;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.ebaoquan.rop.thirdparty.org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.Exceptions;
import com.jxf.transplantation.dbo.OldDBOperator;
import com.jxf.transplantation.dbo.OldDBOperatorFactory;
import com.jxf.transplantation.dbo.NowDBOperator;
import com.jxf.transplantation.dbo.NowDBOperatorFactory;

/**
 * @作者: gaobo
 * @创建时间 :2019年1月14日
 * @功能说明:消息数据迁移
 */
public class ImportUpdateMessageUtils2 {

	private static Logger log = LoggerFactory.getLogger(ImportUpdateMessageUtils2.class);

	public static void main(String[] args) {
		importMessage();
	}

	public static int importMessage() {
		OldDBOperator oldOperator = OldDBOperatorFactory.getDBOperator();
		NowDBOperator nowOperator = NowDBOperatorFactory.getNowDBOperator();
		PreparedStatement psmt = null;
		PreparedStatement psmtLoan = null;
		PreparedStatement psmtMessage = null;
		ResultSet rs = null;
		String countsql = "select * from MEM_MEMBER_MESSAGE where type in('9','15','16') and create_time < '2019-02-13'";
		Date startDate = new Date();
		log.debug("开始时间:" + DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
		int i = 0;
		try {
			psmt = nowOperator.getPreparedStatement(countsql);
			rs = psmt.executeQuery();
			
			String loanSql = "SELECT * FROM t_new_loan where id = ?";
			psmtLoan = oldOperator.getPreparedStatement(loanSql);
			
			
			String updateMessageSql = "update MEM_MEMBER_MESSAGE set org_id = ? where id = ?";
			psmtMessage = nowOperator.getPreparedStatement(updateMessageSql);
			
			
			while (rs.next()) {
				++i;
				log.debug("开始第" + i + "条数据迁移");
				boolean success = true;
				success = transplantMessage(rs, psmtMessage,psmtLoan);
				if (success) {
					log.debug("第" + i + "条迁移成功");
				} else {
					i--;
				}
			}
			psmt.close();
			psmtLoan.close();
			psmtMessage.close();
			log.debug("结束,共" + i + "条数据迁移成功");
			log.debug("用时:" + DateUtils.pastMinutes(startDate) + "分钟");
		} catch (SQLException e) {
			log.error(Exceptions.getStackTraceAsString(e));
		}
		return i;

	}

	private static boolean transplantMessage(ResultSet rs, PreparedStatement psmt, PreparedStatement psmtLoan) throws SQLException {
		
		// id
		String id = rs.getString("id");
		String orgId = rs.getString("org_id");
		
		String nextId = getNextId(orgId,psmtLoan);
		if(StringUtils.isBlank(nextId)) {
			return false;
		}
		psmt.setString(1, nextId);
		psmt.setString(2, id);
		
		log.debug("-----------------------------" + psmt);
		try {
			psmt.executeUpdate();
		} catch (Exception e) {
			log.error("=========出错了==========" + psmt);
			log.error(Exceptions.getStackTraceAsString(e));
		}
		return true;
	}

	public static String getNextId(String val,PreparedStatement psmtLoan) throws SQLException{
		psmtLoan.setString(1, val);
		ResultSet rs = psmtLoan.executeQuery();
		if(rs.next()) {
			int nextId = rs.getInt("nextLoanId");
			if(nextId > 0) {
				return getNextId(nextId+"",psmtLoan);
			}
			
			return rs.getString("id");
		}
		return "";
	}
	

	
}
