package com.jxf.transplantation.temp.member;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxf.svc.security.PasswordUtils;
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.Exceptions;
import com.jxf.transplantation.dbo.OldDBOperator;
import com.jxf.transplantation.dbo.OldDBOperatorFactory;
import com.jxf.transplantation.dbo.NowDBOperator;
import com.jxf.transplantation.dbo.NowDBOperatorFactory;

/**
 * @作者: gaobo
 * @创建时间 :2019年 1 月22日 下午2:24:43
 * @功能说明:会员重置支付密码表迁移
 */
public class ImportMemberResetPaypwdUtils {
	
	private static Logger log = LoggerFactory.getLogger(ImportMemberResetPaypwdUtils.class);

	public static void main(String[] args) {
		importResetPaypwd(22000L, 23000L);
	}

	public static int importResetPaypwd(Long startId, Long endId) {
		OldDBOperator oldOperator = OldDBOperatorFactory.getDBOperator();
		NowDBOperator nowOperator = NowDBOperatorFactory.getNowDBOperator();
		PreparedStatement psmt = null;
		ResultSet rs = null;

		String countSql = " SELECT * from t_user_reset_paypwd where id >= ? AND id <=? ORDER BY id";
		Date startDate = new Date();
		log.debug("开始时间:" + DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
		int i = 0;
		try {
			psmt = oldOperator.getPreparedStatement(countSql);
			psmt.setLong(1, startId);
			psmt.setLong(2, endId);
			rs = psmt.executeQuery();
			
			String insertResetPaypwdSql = "INSERT INTO `MEM_MEMBER_RESET_PAY_PWD` ("
					+"`id`,"//1
					+"`member_id`,"//2
					+"`pay_pwd`,"//3
					+"`status`,"//4
					+"`fail_reason`,"//5
					+"`create_by`,"
					+"`create_time`,"//6
					+"`update_by`,"
					+"`update_time`,"//7
					+"`del_flag` ) VALUES (?,?,?,?,?, '999999',?,'999999',?,'0')";
			psmt = nowOperator.getPreparedStatement(insertResetPaypwdSql);
			while (rs.next()) {
				++i;
				log.debug("开始第" + i + "条数据迁移");
				boolean	success = transplantResetPaypwd(rs, psmt);
				if (success) {
					log.debug("第" + i + "条迁移成功");
				} else {
					i--;
				}
			}
			psmt.close();
			OldDBOperatorFactory.addDBOperator(oldOperator);
			NowDBOperatorFactory.addNowDBOperator(nowOperator);
			log.debug("结束,共" + i + "条数据迁移成功");
			log.debug("用时:" + DateUtils.pastMinutes(startDate) + "分钟");
		} catch (SQLException e) {
			log.error(Exceptions.getStackTraceAsString(e));
		}
		return i;

	}

	private static boolean transplantResetPaypwd(ResultSet rs, PreparedStatement psmt) throws SQLException {
		
		// id
		String oldId = rs.getString("id");
		
		// userId
		String userId = rs.getString("userId");
		
		// payPwd
		String payPwd = rs.getString("payPwd");
		
		// payPwd
		int authStatus = rs.getInt("authStatus");
		
		// payPwd
		String failReason = rs.getString("failReason");

		// payPwd
		String createTime = rs.getString("time");

		// 最后操作时间
		String updateTime = DateUtils.formatDateTime(new Date());
		
		psmt.setLong(1, Long.parseLong(oldId));
		psmt.setLong(2, Long.parseLong(userId));
		
		if(StringUtils.isNotBlank(payPwd)&&payPwd.length()==32){
			psmt.setString(3, PasswordUtils.entryptPassword(payPwd));
		}else {
			psmt.setString(3,payPwd);
		}
		if(authStatus == 0) {
			psmt.setInt(4,authStatus);
		}else if(authStatus == 100) {
			psmt.setInt(4,1);
		}else if(authStatus == -1){
			psmt.setInt(4,2);
		}else {
			return false;
		}
		psmt.setString(5,failReason);
		psmt.setString(6,createTime);
		psmt.setString(7,updateTime);
		
		log.debug("-----------------------------" + psmt);
		psmt.executeUpdate();
		return true;
	}

	
	
}
