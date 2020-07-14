package com.jxf.transplantation.temp.member;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.Exceptions;

import com.jxf.transplantation.dbo.OldDBOperator;
import com.jxf.transplantation.dbo.OldDBOperatorFactory;
import com.jxf.transplantation.dbo.NowDBOperator;
import com.jxf.transplantation.dbo.NowDBOperatorFactory;

/**
 * @作者: xiaorongdian
 * @创建时间 :2018年11月19日 下午5:49:02
 * @功能说明:视频认证迁移
 */
public class ImportMemberVideoVerifyUtils {
	
	private static Logger log = LoggerFactory.getLogger(ImportMemberVideoVerifyUtils.class);
	
	public static void main(String[] args) {
		importMemberVideoVerify(1L,55572100L);
    }
	
	public static int importMemberVideoVerify(Long startId,Long endId) {
		OldDBOperator oldOperator = OldDBOperatorFactory.getDBOperator();
		NowDBOperator nowOperator = NowDBOperatorFactory.getNowDBOperator();
		PreparedStatement psmt = null;
		ResultSet rs = null;
		String countsql = "SELECT * FROM t_video_authentication  WHERE  id >=? AND id <=?  and status = 100  ORDER BY id";
		
		String insertSql = "INSERT INTO `MEM_MEMBER_VIDEO_VERIFY` (`id`, `trx_id`, `member_id`, `type`, `status`, `fail_reason`, `real_name`, `id_no`, `idcard_front_photo`, `idcard_portrait_photo`, `idcard_back_photo`, `living_photo`, `video_url`, `nation`, `address`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES (?, null, ?, '0', '1' , null , ?, ?, ?, ?, ?, ?, ?, ?, ?, '999999', ?, '999999', ?, '0');";

		
		Date startDate = new Date();
		log.debug("开始时间:"+DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
		int i = 0;
		try {
			psmt = oldOperator.getPreparedStatement(countsql);
			psmt.setLong(1, startId);
			psmt.setLong(2, endId);
			rs = psmt.executeQuery();
			
			psmt = nowOperator.getPreparedStatement(insertSql);
			while (rs.next()) {
				
				++i;
				log.debug("开始第" + i + "条数据迁移");
				 boolean success = transplantVideoVerify(rs, psmt);
				if (success) {
					log.debug("第" + i + "条迁移成功");
				} else {
					i--;
				}
			}
		    psmt.close();	
		    log.debug("结束,共"+i+"条数据迁移成功");
		    log.debug("用时:"+ DateUtils.pastMinutes(startDate) + "分钟");
		} catch (SQLException e) {
			log.debug(Exceptions.getStackTraceAsString(e));			
		}
		return i;
		
	}

	private static boolean transplantVideoVerify(ResultSet rs, PreparedStatement psmt) throws SQLException {
		
		psmt.setLong(1, rs.getLong("id"));
		psmt.setLong(2, rs.getLong("user_id"));
		psmt.setString(3, rs.getString("name"));
		psmt.setString(4, rs.getString("idcard_no"));
		psmt.setString(5, rs.getString("idcard_front_photo"));
		psmt.setString(6, rs.getString("idcard_portrait_photo"));
		psmt.setString(7, rs.getString("idcard_back_photo"));
		psmt.setString(8, rs.getString("living_photo"));
		psmt.setString(9, rs.getString("video_url"));
		psmt.setString(10, rs.getString("nation"));
		psmt.setString(11, rs.getString("address"));
		psmt.setString(12, rs.getString("create_time"));
		psmt.setString(13, rs.getString("modify_time"));
		psmt.executeUpdate();
		
		return true;
	}
}
