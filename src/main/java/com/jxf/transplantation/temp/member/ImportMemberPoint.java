package com.jxf.transplantation.temp.member;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.Exceptions;
import com.jxf.svc.utils.SnowFlake;
import com.jxf.transplantation.dbo.OldDBOperator;
import com.jxf.transplantation.dbo.OldDBOperatorFactory;
import com.jxf.transplantation.dbo.NowDBOperator;
import com.jxf.transplantation.dbo.NowDBOperatorFactory;

/**
 * @作者: gaobo
 * @创建时间 :2019年 1 月22日 下午2:24:43
 * @功能说明:会员积分 迁移
 */
public class ImportMemberPoint {
	
	private static Logger log = LoggerFactory.getLogger(ImportMemberPoint.class);

	public static void main(String[] args) {
		importPoint(1L, 5000L);
	}

	public static int importPoint(Long startId, Long endId) {
		OldDBOperator oldOperator = OldDBOperatorFactory.getDBOperator();
		NowDBOperator nowOperator = NowDBOperatorFactory.getNowDBOperator();
		PreparedStatement psmt = null;
		PreparedStatement psmtForRankName = null;
		PreparedStatement psmtForPoint = null;
		ResultSet rs = null;

		String querysql = "select *  from  t_user where id >=? AND id <=? GROUP BY username ORDER BY id  ";
		
		Date startDate = new Date();
		log.debug("开始时间:" + DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
		int i = 0;
		try {
			psmt = oldOperator.getPreparedStatement(querysql);
			psmt.setLong(1, startId);
			psmt.setLong(2, endId);
			rs = psmt.executeQuery();
			
			//获取信用等级
			String queryForRankNameSql = "select creditDegree_score as balancePoints,creditDegree_level as rankName from t_credit_center_data where user_id = ?";
			psmtForRankName = oldOperator.getPreparedStatement(queryForRankNameSql);
			
			//积分
			String pointSql = "INSERT INTO `MEM_MEMBER_POINT`(`id`, `member_id`, `balance_points`, `total_points`, `reduce_points`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES (?, ?, ?, ?, 0, 999999, now(), 999999, now(), '0')";
			psmtForPoint = nowOperator.getPreparedStatement(pointSql);
			
			while (rs.next()) {
				++i;
				log.debug("开始第" + i + "条数据迁移");
				boolean success = transplantPoint(rs,psmtForPoint,psmtForRankName);
				if (success) {
					log.debug("第" + i + "条迁移成功");
				} else {
					i--;
				}
			}
			psmt.close();
			psmtForPoint.close();
			psmtForRankName.close();
			log.debug("结束,共" + i + "条数据迁移成功");
			log.debug("用时:" + DateUtils.pastMinutes(startDate) + "分钟");
		} catch (Exception e) {
			log.error(Exceptions.getStackTraceAsString(e));
		}
		return i;

	}

	private static boolean transplantPoint(ResultSet rs, PreparedStatement psmtForPoint,
			PreparedStatement psmtForRankName) throws SQLException {
		String usernameStr = rs.getString("username");
		if(StringUtils.isBlank(usernameStr)){
			return false;
		} 
		String yxbToken = rs.getString("yxb_token");//用户唯一标识
		if(StringUtils.isBlank(yxbToken)){
			return false;
		} 
		
		String spareMobileStrStr = rs.getString("spare_mobile");//备用手机号
		if(StringUtils.isNotBlank(spareMobileStrStr) && spareMobileStrStr.length() == 11){
			usernameStr = spareMobileStrStr;
		}
		
    	Long oldId = rs.getLong("id");
    	Map<String,String> m = getCreditDegreeLevel(oldId,psmtForRankName);
    	String balancePoints = m.get("balancePoints");
		psmtForPoint.setLong(1, SnowFlake.nextId(1, 1));
		psmtForPoint.setLong(2, oldId);
		psmtForPoint.setString(3, balancePoints);
		psmtForPoint.setString(4, balancePoints);
		psmtForPoint.execute();
    	
		return true;
	}

	//获取信用等级
	private static Map<String, String> getCreditDegreeLevel(Long oldId, PreparedStatement psmtForRankName) throws SQLException {
		ResultSet rs = null;
		psmtForRankName.setLong(1, oldId);
		rs = psmtForRankName.executeQuery();
		String rankName = "";
		String balancePoints = "";
		Map<String, String> m = new HashMap<>();
		while (rs.next()) {
			rankName = rs.getString("rankName");
			balancePoints = rs.getString("balancePoints");
		}
		m.put("rankName",StringUtils.isNotBlank(rankName)?rankName:"A");
		m.put("balancePoints",StringUtils.isNotBlank(balancePoints)?balancePoints:"0");
//			log.debug("获取信用等级成功："+rankName+"积分："+balancePoints);
		return m;
	}
		

}
