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
 * 2019/02/13
 * @author Administrator
 *
 */
public class ImportUpdateMemberIdNoUtils {
	
	private static Logger log = LoggerFactory.getLogger(ImportUpdateMemberIdNoUtils.class);
	
	public static void main(String[] args) {
		importMemberVideoIdNo(1L,5000000L);
    }
	
	public static int importMemberVideoIdNo(Long startId,Long endId) {
		OldDBOperator oldOperator = OldDBOperatorFactory.getDBOperator();
		NowDBOperator nowOperator = NowDBOperatorFactory.getNowDBOperator();
		PreparedStatement psmt = null;
		ResultSet rs = null;
		PreparedStatement psmtNew = null;
		String countsql = "SELECT * FROM t_video_authentication  WHERE  id >=? AND id <=?  and status =200  ORDER BY id";
		
		String insertSql = "UPDATE `MEM_MEMBER` SET id_no=? where id=?";
		Date startDate = new Date();
		log.debug("开始时间:"+DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
		int i = 0;
		try {
			psmt = oldOperator.getPreparedStatement(countsql);
			psmt.setLong(1, startId);
			psmt.setLong(2, endId);
			rs = psmt.executeQuery();
			
			psmtNew = nowOperator.getPreparedStatement(insertSql);
			while (rs.next()) {
				++i;
				log.debug("开始第" + i + "条数据迁移");
				psmtNew.setString(1,rs.getString("idcard_no"));
				psmtNew.setLong(2,Long.parseLong(rs.getString("user_id")));
				psmtNew.executeUpdate();
			    log.debug("第" + i + "条迁移成功");
			}
		    psmt.close();
		    psmtNew.close();
		    log.debug("结束,共"+i+"条数据迁移成功");
		    log.debug("用时:"+ DateUtils.pastMinutes(startDate) + "分钟");
		} catch (SQLException e) {
			log.debug(Exceptions.getStackTraceAsString(e));			
		}
		return i;
		
	}
}
