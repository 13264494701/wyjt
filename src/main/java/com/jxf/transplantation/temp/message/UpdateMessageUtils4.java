package com.jxf.transplantation.temp.message;

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
 * @作者: gaobo
 * @创建时间 :2019年6月26日
 * @功能说明:更新字段 group
 */
public class UpdateMessageUtils4 {

	private static Logger log = LoggerFactory.getLogger(UpdateMessageUtils4.class);

	public static void main(String[] args) {
//		importMessage(1L, 2000L);
	}

	public static int importMessage(String startId, String endId) {
		NowDBOperator nowOperator = NowDBOperatorFactory.getNowDBOperator();
		PreparedStatement psmt = null;
		PreparedStatement psmtMessage = null;
		ResultSet rs = null;
		String countsql = "select * from MEM_MEMBER_MESSAGE a where a.id >= ? and a.id <= ? and a.groups = '0'";
		Date startDate = new Date();
		log.debug("开始时间:" + DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
		int i = 0;
		try {
			psmt = nowOperator.getPreparedStatement(countsql);
			psmt.setString(1, startId);
		    psmt.setString(2, endId);
			rs = psmt.executeQuery();
			
			String updateMessageSql = "update MEM_MEMBER_MESSAGE set groups = ? where id = ?";
			psmtMessage = nowOperator.getPreparedStatement(updateMessageSql);
			
			
			while (rs.next()) {
				++i;
				log.debug("开始第" + i + "条数据迁移");
				boolean success = true;
				success = transplantMessage(rs, psmtMessage);
				if (success) {
					log.debug("第" + i + "条迁移成功");
				} else {
					i--;
				}
			}
			psmt.close();
			psmtMessage.close();
			log.debug("结束,共" + i + "条数据迁移成功");
			log.debug("用时:" + DateUtils.pastMinutes(startDate) + "分钟");
		} catch (SQLException e) {
			log.error(Exceptions.getStackTraceAsString(e));
		}
		return i;

	}

	private static boolean transplantMessage(ResultSet rs, PreparedStatement psmt) throws SQLException {
		// id
		String id = rs.getString("id");
		int type = rs.getInt("type");
		
		if(0<= type&&type <=36) {
			psmt.setString(1, "0");
			psmt.setString(2, id);
			psmt.executeUpdate();
		}else if(54<= type&&type <=67) {
			psmt.setString(1, "0");
			psmt.setString(2, id);
			psmt.executeUpdate();
		}else if(type == 37) {
			psmt.setString(1, "5");
			psmt.setString(2, id);
			psmt.executeUpdate();
		}else if(38<= type&&type <=53) {
			psmt.setString(1, "4");
			psmt.setString(2, id);
			psmt.executeUpdate();
		}
		return true;
	}

	
	

	
}
