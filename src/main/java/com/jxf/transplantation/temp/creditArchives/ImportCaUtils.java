package com.jxf.transplantation.temp.creditArchives;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.ebaoquan.rop.thirdparty.org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.Exceptions;
import com.jxf.svc.utils.JSONUtil;
import com.jxf.transplantation.dbo.OldDBOperator;
import com.jxf.transplantation.dbo.OldDBOperatorFactory;
import com.jxf.transplantation.dbo.NowDBOperator;
import com.jxf.transplantation.dbo.NowDBOperatorFactory;

/**
 * @作者: lmy
 * @创建时间 :2019年1月15日
 * @功能说明:信用档案数据迁移
 */
public class ImportCaUtils {

	private static Logger log = LoggerFactory.getLogger(ImportCaUtils.class);
	static HashMap<String, String> map = new HashMap<String, String>();

	public static void main(String[] args) {
		// importSouceCreditArchives(31L,40L);
		// importCreditArchives(65L, 76L);
		importXinYanCreditArchives(60L, 64L);
	}

	// 信用档案 原数据
	public static int importSouceCreditArchives(Long startId, Long endId) {
		OldDBOperator oldOperator = OldDBOperatorFactory.getDBOperator();
		NowDBOperator nowOperator = NowDBOperatorFactory.getNowDBOperator();
		PreparedStatement psmt = null;
		ResultSet rs = null;
		String countsql = "select *  from  t_creditfile_source_data  where id >=? AND id <=? ORDER BY id ";
		Date startDate = new Date();
		log.debug("开始时间:" + DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
		int i = 0;
		try {
			psmt = oldOperator.getPreparedStatement(countsql);
			psmt.setLong(1, startId);
			psmt.setLong(2, endId);
			rs = psmt.executeQuery();
			boolean success = true;
			String insertFriendApplySql = " INSERT INTO RC_CA_SOURCE_DATA ( id," + // 1
					"member_id," + // 2
					"type," + // 3
					"content," + // 4
					"status," + // 5
					"create_by,create_time,update_by,update_time,del_flag) VALUES (?,?,?,?,?,999999,?,999999,?,'0')";
			psmt = nowOperator.getPreparedStatement(insertFriendApplySql);
			while (rs.next()) {
				++i;
				log.debug("开始第" + i + "条数据迁移");
				success = transplantSouceCreditArchives(rs, psmt);
				if (success) {
					log.debug("第" + i + "条迁移成功");
				} else {
					i--;
				}
			}
			psmt.close();
			log.debug("结束,共" + i + "条数据迁移成功");
			log.debug("用时:" + DateUtils.pastMinutes(startDate) + "分钟");
		} catch (SQLException e) {
			log.error(Exceptions.getStackTraceAsString(e));
		}
		return i;

	}

	// 整理后数据
	public static int importCreditArchives(Long startId, Long endId) {
		OldDBOperator oldOperator = OldDBOperatorFactory.getDBOperator();
		NowDBOperator nowOperator = NowDBOperatorFactory.getNowDBOperator();
		PreparedStatement psmt = null;
		ResultSet rs = null;
		PreparedStatement psmtDe = null;
		String countsql = "select *  from  t_creditfile_data  where id >=? AND id <=? ORDER BY id ";
		Date startDate = new Date();
		log.debug("开始时间:" + DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
		int i = 0;
		try {
			psmt = oldOperator.getPreparedStatement(countsql);
			psmt.setLong(1, startId);
			psmt.setLong(2, endId);
			rs = psmt.executeQuery();

			// 其他数据
			String insertFriendApplySql = " INSERT INTO RC_CA_DATA ( id," + // 1
					"member_id," + // 2
					"type," + // 3
					"content," + // 4
					"create_by,create_time,update_by,update_time,del_flag) VALUES (?,?,?,?,999999,?,999999,?,'0')";
			psmt = nowOperator.getPreparedStatement(insertFriendApplySql);
			// 运营商详情
			String insertFriendApplySqldetail = " INSERT INTO RC_CA_YYS_DETAILS ( id," + // 1
					"member_id," + // 2
					"content," + // 3
					"create_by,create_time,update_by,update_time,del_flag) VALUES (?,?,?,999999,?,999999,?,'0')";
			psmtDe = nowOperator.getPreparedStatement(insertFriendApplySqldetail);
			while (rs.next()) {
				++i;
				log.debug("开始第" + i + "条数据迁移");
				boolean success = true;
				if (StringUtils.equals(rs.getString("sourceType"), "3")) {
					success = transplantyySCreditArchives(rs, psmt);
					success = transplantyysDeCreditArchives(rs, psmtDe);
				} else if (!"11".equals(rs.getString("sourceType"))) {
					success = transplantCreditArchives(rs, psmt);
				}

				if (success) {
					log.debug("第" + i + "条迁移成功");
				} else {
					i--;
				}
			}
			psmt.close();
			psmtDe.close();
			log.debug("结束,共" + i + "条数据迁移成功");
			log.debug("用时:" + DateUtils.pastMinutes(startDate) + "分钟");
		} catch (SQLException e) {
			log.error(Exceptions.getStackTraceAsString(e));
		}
		return i;

	}

	// 整理后数据
	public static int importXinYanCreditArchives(Long startId, Long endId) {
		OldDBOperator oldOperator = OldDBOperatorFactory.getDBOperator();
		NowDBOperator nowOperator = NowDBOperatorFactory.getNowDBOperator();
		PreparedStatement psmt = null;
		ResultSet rs = null;
		PreparedStatement psmtNew = null;
		PreparedStatement psmtMem = null;

		String countsql = "select *  from  t_xinyan_creditfile  where id >=? AND id <=? ORDER BY id ";
		Date startDate = new Date();
		log.debug("开始时间:" + DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
		int i = 0;
		try {
			psmt = oldOperator.getPreparedStatement(countsql);
			psmt.setLong(1, startId);
			psmt.setLong(2, endId);
			rs = psmt.executeQuery();
			String insertFriendApplySql = " INSERT INTO RC_XINYAN ( id," + // 1
					"ufang_emp_no," + // 2
					"token," + // 3
					"name," + // 4
					"id_no," + // 5
					"phone_no," + // 6
					"result," + // 7
					"rmk," + // 8
					"is_selfbuy," + // 9
					"create_by,create_time,update_by,update_time,del_flag) VALUES (?,?,?,?,?,?,?,?,?,999999,?,999999,?,'0')";
			psmt = nowOperator.getPreparedStatement(insertFriendApplySql);
			// 查询信用档案
			String countsql1 = "select *  from  t_creditfile_data  where id=?";
			psmtNew = oldOperator.getPreparedStatement(countsql1);
			// 查询用户
			String countsql2 = "select *  from  MEM_MEMBER  where id=?";
			psmtMem = nowOperator.getPreparedStatement(countsql2);

			while (rs.next()) {
				++i;
				log.debug("开始第" + i + "条数据迁移");
				boolean success = true;
				success = transplantxinYanCreditArchives(rs, psmt, psmtNew, psmtMem);
				if (success) {
					log.debug("第" + i + "条迁移成功");
				} else {
					i--;
				}
			}
			psmt.close();
			psmtNew.close();
			psmtMem.close();
			log.debug("结束,共" + i + "条数据迁移成功");
			log.debug("用时:" + DateUtils.pastMinutes(startDate) + "分钟");
		} catch (SQLException e) {
			log.error(Exceptions.getStackTraceAsString(e));
		}

		return i;

	}

	private static boolean transplantSouceCreditArchives(ResultSet rs, PreparedStatement psmt) throws SQLException {
		// id
		String oldId = rs.getString("id");
		// 用户id
		String user_id = rs.getString("userId");

		String note = rs.getString("note");

		String sourceType = rs.getString("sourceType");

		String readStatus = rs.getString("readStatus");

		String createTime = rs.getString("createTime");

		psmt.setLong(1, Long.parseLong(oldId));
		psmt.setLong(2, Long.parseLong(user_id));
		psmt.setInt(3, Integer.parseInt(sourceType) - 1);
		psmt.setString(4, note);
		psmt.setInt(5, Integer.parseInt(readStatus));
		psmt.setString(6, createTime == null ? null : createTime);
		psmt.setString(7, createTime == null ? null : createTime);
//		log.debug("-----------------------------" + psmt);
		psmt.executeUpdate();
		return true;
	}

	private static boolean transplantCreditArchives(ResultSet rs, PreparedStatement psmt) throws SQLException {
		// id
		String oldId = rs.getString("id");
		// 用户id
		String user_id = rs.getString("userId");

		String note = rs.getString("note");

		String sourceType = rs.getString("sourceType");

		String createTime = rs.getString("createTime");

		psmt.setLong(1, Long.parseLong(oldId));
		psmt.setLong(2, Long.parseLong(user_id));
		psmt.setInt(3, Integer.parseInt(sourceType) - 1);
		psmt.setString(4, note);
		psmt.setString(5, createTime == null ? null : createTime);
		psmt.setString(6, createTime == null ? null : createTime);
//		log.debug("-----------------------------" + psmt);
		psmt.executeUpdate();
		return true;
	}

	private static boolean transplantyySCreditArchives(ResultSet rs, PreparedStatement psmt) throws SQLException {
		// id
		String oldId = rs.getString("id");
		// 用户id
		String user_id = rs.getString("userId");

		String note = rs.getString("note");

		String sourceType = rs.getString("sourceType");

		String createTime = rs.getString("createTime");
		Map<String, Object> map2 = JSONUtil.toMap(note);
		Map<String, Object> mapTask1 = new HashMap<String, Object>();

		mapTask1.put("channel_src", map2.get("channel_src"));
		mapTask1.put("channel_attr", map2.get("channel_attr"));
		mapTask1.put("created_time", map2.get("created_time"));
		mapTask1.put("user_mobile", map2.get("user_mobile"));
		mapTask1.put("identity_code", map2.get("identity_code"));
		mapTask1.put("time", map2.get("net_age"));
		mapTask1.put("frrMoney", map2.get("frrMoney"));
		mapTask1.put("Hutong", map2.get("Hutong"));
		mapTask1.put("dayTongNum", map2.get("dayTongNum"));
		mapTask1.put("tongNum", map2.get("tongNum"));
		mapTask1.put("tongCishu", map2.get("tongCishu"));
		mapTask1.put("jngMoNum", map2.get("jngMoNum"));

		mapTask1.put("money", map2.get("money"));
		mapTask1.put("taocanmoney", map2.get("taocanmoney"));
		mapTask1.put("diejiamoney", map2.get("diejiamoney"));
		mapTask1.put("zengzhimoney", map2.get("zengzhimoney"));
		mapTask1.put("othermoney", map2.get("othermoney"));

		mapTask1.put("money1", map2.get("money1"));
		mapTask1.put("taocanmoney1", map2.get("taocanmoney1"));
		mapTask1.put("diejiamoney1", map2.get("diejiamoney1"));
		mapTask1.put("zengzhimoney1", map2.get("zengzhimoney1"));
		mapTask1.put("othermoney1", map2.get("othermoney1"));

		mapTask1.put("money3", map2.get("money3"));
		mapTask1.put("taocanmoney3", map2.get("taocanmoney3"));
		mapTask1.put("diejiamoney3", map2.get("diejiamoney3"));
		mapTask1.put("zengzhimoney3", map2.get("zengzhimoney3"));
		mapTask1.put("othermoney3", map2.get("othermoney3"));
		String json = JSONUtil.toJson(mapTask1);
		psmt.setLong(1, Long.parseLong(oldId));
		psmt.setLong(2, Long.parseLong(user_id));
		psmt.setInt(3, Integer.parseInt(sourceType) - 1);
		psmt.setString(4, json);
		psmt.setString(5, createTime == null ? null : createTime);
		psmt.setString(6, createTime == null ? null : createTime);
//		log.debug("-----------------------------" + psmt);
		psmt.executeUpdate();
		return true;
	}

	private static boolean transplantyysDeCreditArchives(ResultSet rs, PreparedStatement psmt) throws SQLException {
		// id
		String oldId = rs.getString("id");
		// 用户id
		String user_id = rs.getString("userId");

		String note = rs.getString("note");

		String sourceType = rs.getString("sourceType");

		String createTime = rs.getString("createTime");

		psmt.setLong(1, Long.parseLong(oldId));
		psmt.setLong(2, Long.parseLong(user_id));
		psmt.setString(3, note);
		psmt.setString(4, createTime == null ? null : createTime);
		psmt.setString(5, createTime == null ? null : createTime);
//		log.debug("-----------------------------" + psmt);
		psmt.executeUpdate();
		return true;
	}

	private static boolean transplantxinYanCreditArchives(ResultSet rs, PreparedStatement psmt,
			PreparedStatement psmtNew, PreparedStatement psmtMem) throws SQLException {
		// id
		String oldId = rs.getString("id");
		// 用户id
		String payUserId = rs.getString("payUserId");
		String checkUserId = rs.getString("checkUserId");
		String creditfileId = rs.getString("creditfileId");
		String createTime = rs.getString("createTime");
		ResultSet rsnew = null;
		ResultSet rsMem = null;
		psmtNew.setLong(1, Long.parseLong(creditfileId));
		rsnew = psmtNew.executeQuery();
		String note = "";
		Map<String, Object> map2 = new HashMap<String, Object>();
		if (rsnew.next()) {
			note = rsnew.getString("note");
			if(StringUtils.isNotBlank(note)) {
				map2 = JSONUtil.toMap(note);	
			}
		}
		psmtMem.setLong(1, Long.parseLong(checkUserId));
		rsMem = psmtMem.executeQuery();
		if(!rsMem.next()) {
			log.error("用户ID:{}不存在",checkUserId);
			return false;
			
		}

		psmt.setLong(1, Long.parseLong(oldId));
		psmt.setString(2, "0");
		psmt.setString(3, map2.get("trans_id").toString());
		psmt.setString(4, rsMem.getString("name"));
		psmt.setString(5, rsMem.getString("id_no"));
		psmt.setString(6, rsMem.getString("username"));
		psmt.setString(7, note);
		psmt.setString(8, null);
		if (payUserId.equals(checkUserId)) {
			psmt.setString(9, "Y");
		} else {
			psmt.setString(9, "N");
		}

		psmt.setString(10, createTime == null ? null : createTime);
		psmt.setString(11, createTime == null ? null : createTime);
//		log.debug("-----------------------------" + psmt);
		psmt.executeUpdate();
		return true;
	}

}
