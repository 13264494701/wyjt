package com.jxf.transplantation.temp.member;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxf.mem.utils.VerifiedUtils;
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.Exceptions;
import com.jxf.svc.utils.JSONUtil;
import com.jxf.transplantation.dbo.OldDBOperator;
import com.jxf.transplantation.dbo.OldDBOperatorFactory;
import com.jxf.transplantation.dbo.NowDBOperator;
import com.jxf.transplantation.dbo.NowDBOperatorFactory;

/**
 * @作者: gaobo
 * @创建时间 :2019年 1 月22日 下午2:24:43
 * @功能说明:会员扩展表迁移
 */
public class ImportMemberExtendUtils {
	
	private static Logger log = LoggerFactory.getLogger(ImportMemberExtendUtils.class);

	public static void main(String[] args) {
		importExtend(590000L, 600000L);
	}

	public static int importExtend(Long startId, Long endId) {
		OldDBOperator oldOperator = OldDBOperatorFactory.getDBOperator();
		NowDBOperator nowOperator = NowDBOperatorFactory.getNowDBOperator();
		PreparedStatement psmt = null;
		PreparedStatement userInfoPsmt = null;
		PreparedStatement creditfilePsmt = null;
		PreparedStatement selectPsmt = null;
		PreparedStatement updatePsmt = null;
		ResultSet rs = null;

		String t_userSql = " SELECT * from t_user where id >= ? AND id <=? ORDER BY id";
		String t_userInfoSql = "SELECT * from t_user_info where userId = ? ";
		String t_creditfileSql = "SELECT * from t_creditfile_data where userId = ? and sourceType = '1' ORDER BY createTime DESC limit 1 ";
		String countSql = "select * from  MEM_MEMBER where id = ? " ;
		String updateSql = "UPDATE MEM_MEMBER set verified_list = ? where id = ? " ;
		
		Date startDate = new Date();
		log.debug("开始时间:" + DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
		int i = 0;
		try {
			userInfoPsmt = oldOperator.getPreparedStatement(t_userInfoSql);
			creditfilePsmt = oldOperator.getPreparedStatement(t_creditfileSql);
			selectPsmt = nowOperator.getPreparedStatement(countSql);
			updatePsmt = nowOperator.getPreparedStatement(updateSql);
			psmt = oldOperator.getPreparedStatement(t_userSql);
			psmt.setLong(1, startId);
			psmt.setLong(2, endId);
			rs = psmt.executeQuery();
			
			String insertMemberExtendSql = "INSERT INTO `MEM_MEMBER_EXTEND` ("
					+"`id`,"//1
					+"`member_id`,"//2
					+"`email`,"//3
					+"`sex`,"//4
					+"`resi_address`,"//5
					+"`ecp1`,"//6
					+"`ecp_phone_no1`,"//7
					+"`ecp2`,"//8
					+"`ecp_phone_no2`,"//9
					+"`ecp3`,"//10
					+"`ecp_phone_no3`,"//11
					+"`create_time`,"//12
					+"`update_time`,"//13
					+"`create_by`,"
					+"`update_by`,"
					+"`del_flag` ) VALUES (?,?,?,?,?,?,?,?,?,?, ?,?,?, '999999','999999','0')";
				psmt = nowOperator.getPreparedStatement(insertMemberExtendSql);
			while (rs.next()) {
				++i;
				log.debug("开始第" + i + "条数据迁移");
				 boolean success = transplantExtend(rs, psmt, userInfoPsmt,creditfilePsmt,selectPsmt,updatePsmt);
				if (success) {
					log.debug("第" + i + "条迁移成功");
				} else {
					i--;
				}
			}
			psmt.close();
			userInfoPsmt.close();
			creditfilePsmt.close();
			selectPsmt.close();
			updatePsmt.close();
			log.debug("结束,共" + i + "条数据迁移成功");
			log.debug("用时:" + DateUtils.pastMinutes(startDate) + "分钟");
		} catch (Exception e) {
			log.error(Exceptions.getStackTraceAsString(e));
		}
		return i;

	}

	private static boolean transplantExtend(ResultSet rs, PreparedStatement psmt, PreparedStatement userInfoPsmt,
			PreparedStatement creditfilePsmt, PreparedStatement selectPsmt, PreparedStatement updatePsmt) throws SQLException {
		
		// id
		String userId = rs.getString("id");
		int gender = rs.getInt("gender");
		// 最后操作时间
		String updateTime = DateUtils.formatDateTime(new Date());
		psmt.setLong(2, Long.parseLong(userId));
		if(gender == 0) {
			psmt.setInt(4, 1);
		}else {
			psmt.setInt(4, 0);
		}
		psmt.setString(13,updateTime);
		
		boolean flag = true;
		userInfoPsmt.setString(1,userId);
		ResultSet userInfoRs = userInfoPsmt.executeQuery();
		while(userInfoRs.next()) {
			String userInfoId = userInfoRs.getString("id");
			String email = userInfoRs.getString("email");
			String addr = userInfoRs.getString("addr");
			String createTime = userInfoRs.getString("createTime");
			psmt.setLong(1, Long.parseLong(userInfoId));
			psmt.setString(3, email);
			psmt.setString(5, addr);
			psmt.setString(12, createTime);
			flag = false;
		}
		if(flag) {
			return false;
		}
		
		boolean str = true;
		
		creditfilePsmt.setString(1, userId);
		ResultSet creditfileRs = creditfilePsmt.executeQuery();
		while(creditfileRs.next()) {
			String note = creditfileRs.getString("note");
			Map<String, Object> map = JSONUtil.toMap(note);
			String nameF = map.get("nameF").toString();
			String nameL = map.get("nameL").toString();
			String nameM = map.get("nameM").toString();
			if (nameF.getBytes().length > 64) {
				return false;
			}
			if (nameL.getBytes().length > 64) {
				return false;
			}
			if (nameM.getBytes().length > 64) {
				return false;
			}
			psmt.setString(6,nameF);
			psmt.setString(7,map.get("moblieF").toString());
			psmt.setString(8,nameL);
			psmt.setString(9,map.get("moblieL").toString());
			psmt.setString(10,nameM);
			psmt.setString(11,map.get("moblieM").toString());
			str = false;
		}
		if(str) {
			return false;
		}
//		log.debug("-----------------------------" + psmt);
		psmt.executeUpdate();
		updateMember(userId,selectPsmt,updatePsmt);
		
		return true;
	}
	
	private static void updateMember(String userId, PreparedStatement selectPsmt, PreparedStatement updatePsmt) throws SQLException {
		
		ResultSet rs = null;
		
		selectPsmt.setString(1, userId);
		rs = selectPsmt.executeQuery();
		while(rs.next()) {
			int verifiedList = rs.getInt("verified_list");
			verifiedList = VerifiedUtils.addVerified(verifiedList, 24);
			updatePsmt.setInt(1, verifiedList);
			updatePsmt.setString(2, userId);
			updatePsmt.executeUpdate();
		}
	}

}
