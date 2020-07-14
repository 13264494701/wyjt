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
import com.jxf.transplantation.dbo.NowDBOperator;
import com.jxf.transplantation.dbo.NowDBOperatorFactory;

/**
 * @作者: gaobo
 * @创建时间 :2019年5月24日
 * @功能说明:更新新增字段 title_value
 */
public class UpdateMessageUtils {

	private static Logger log = LoggerFactory.getLogger(UpdateMessageUtils.class);

	public static void main(String[] args) {
//		importMessage(1L, 2000L);
	}

	public static int importMessage(String startId, String endId) {
		NowDBOperator nowOperator = NowDBOperatorFactory.getNowDBOperator();
		PreparedStatement psmt = null;
		PreparedStatement psmtMessage = null;
		ResultSet rs = null;
		String countsql = "select * from MEM_MEMBER_MESSAGE a where a.id >= ? and a.id <= ?  and title_value is null";
		Date startDate = new Date();
		log.debug("开始时间:" + DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
		int i = 0;
		try {
			psmt = nowOperator.getPreparedStatement(countsql);
			psmt.setString(1, startId);
		    psmt.setString(2, endId);
			rs = psmt.executeQuery();
			
			String updateMessageSql = "update MEM_MEMBER_MESSAGE set title_value = ? where id = ?";
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
		String title = rs.getString("title");
		
		if(StringUtils.equals("向好友借出",title)) {
			psmt.setString(1, "0");
			psmt.setString(2, id);
			psmt.executeUpdate();
		}else if(StringUtils.equals("借款申请",title)) {
			psmt.setString(1, "1");
			psmt.setString(2, id);
			psmt.executeUpdate();
		}else if(StringUtils.equals("向好友借入",title)) {
			psmt.setString(1, "2");
			psmt.setString(2, id);
			psmt.executeUpdate();
		}else if(StringUtils.equals("转账",title)) {
			psmt.setString(1, "3");
			psmt.setString(2, id);
			psmt.executeUpdate();
		}else if(StringUtils.equals("申请授权",title)) {
			psmt.setString(1, "4");
			psmt.setString(2, id);
			psmt.executeUpdate();
		}else if(StringUtils.equals("信用报告",title)) {
			psmt.setString(1, "5");
			psmt.setString(2, id);
			psmt.executeUpdate();
		}else if(StringUtils.equals("仲裁申请",title)) {
			psmt.setString(1, "6");
			psmt.setString(2, id);
			psmt.executeUpdate();
		}else if(StringUtils.equals("仲裁受理",title)) {
			psmt.setString(1, "7");
			psmt.setString(2, id);
			psmt.executeUpdate();
		}else if(StringUtils.equals("仲裁裁决",title)) {
			psmt.setString(1, "8");
			psmt.setString(2, id);
			psmt.executeUpdate();
		}else if(StringUtils.equals("仲裁缴费",title)) {
			psmt.setString(1, "9");
			psmt.setString(2, id);
			psmt.executeUpdate();
		}else if(StringUtils.equals("强执申请",title)) {
			psmt.setString(1, "10");
			psmt.setString(2, id);
			psmt.executeUpdate();
		}else if(StringUtils.equals("强执缴费",title)) {
			psmt.setString(1, "11");
			psmt.setString(2, id);
			psmt.executeUpdate();
		}else if(StringUtils.equals("强执案件",title)) {
			psmt.setString(1, "12");
			psmt.setString(2, id);
			psmt.executeUpdate();
		}else if(StringUtils.equals("风险提醒",title)) {
			psmt.setString(1, "13");
			psmt.setString(2, id);
			psmt.executeUpdate();
		}else if(StringUtils.equals("借条转让",title)) {
			psmt.setString(1, "14");
			psmt.setString(2, id);
			psmt.executeUpdate();
		}
		return true;
	}

	
	

	
}
