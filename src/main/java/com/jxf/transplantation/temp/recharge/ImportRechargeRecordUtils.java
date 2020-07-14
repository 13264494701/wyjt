package com.jxf.transplantation.temp.recharge;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.Exceptions;
import com.jxf.svc.utils.StringUtils;
import com.jxf.transplantation.dbo.OldDBOperator;
import com.jxf.transplantation.dbo.OldDBOperatorFactory;
import com.jxf.transplantation.dbo.NowDBOperator;
import com.jxf.transplantation.dbo.NowDBOperatorFactory;

/**
 * @作者: suHuimin
 * @创建时间 :2019年1月3日
 * @功能说明:充值记录表数据迁移
 */
public class ImportRechargeRecordUtils {
	
	private static Logger log = LoggerFactory.getLogger(ImportRechargeRecordUtils.class);


	public static void main(String[] args) {
		ImportRechargeRecord(1L,2000L);
    }
	
	public static int ImportRechargeRecord(Long startId,Long endId) {
		OldDBOperator oldOperator = OldDBOperatorFactory.getDBOperator();
		NowDBOperator nowOperator = NowDBOperatorFactory.getNowDBOperator();
		PreparedStatement oldpsmt = null;
		PreparedStatement nowpsmt = null;
		ResultSet rs = null;
		String sql = "SELECT a.id, a.memo FROM t_recharge_order a LEFT JOIN t_user c ON c.id = a.user_id WHERE a.id > ? AND a.id <= ?";
		Date startDate = new Date();
	   	log.debug("开始时间:"+DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
	   	int i = 0;
	   	try {
	   		oldpsmt = oldOperator.getPreparedStatement(sql);
	   		oldpsmt.setLong(1, startId);
	   		oldpsmt.setLong(2, endId);
			rs = oldpsmt.executeQuery();
			
			
			StringBuffer updateSql = new StringBuffer("UPDATE NFS_RCHG_RECORD SET card_no = ?,payment_no = ?,third_payment_no = ? WHERE id = ?");
			
			nowpsmt = nowOperator.getPreparedStatement(updateSql.toString());
			
			while (rs.next()) {
				++i;
				boolean success = updateRechargeRecord(rs,nowpsmt);
				if(success){
				}else{
					i--;
				}
			}
		    log.debug("结束,共"+i+"条更新迁移成功");
		    log.debug("用时:"+ DateUtils.pastMinutes(startDate) + "分钟");
		} catch (Exception e) {
			log.error(Exceptions.getStackTraceAsString(e));			
		}finally {
			try {
				oldpsmt.close();
				nowpsmt.close();
				OldDBOperatorFactory.addDBOperator(oldOperator);
				NowDBOperatorFactory.addNowDBOperator(nowOperator);
			} catch (Exception e) {
				log.error(Exceptions.getStackTraceAsString(e));	
			}
		}
	   	return i;

	}
	
    private static boolean updateRechargeRecord(ResultSet rs, PreparedStatement psmt) throws SQLException {
    	//充值  id
		String oldId = rs.getString("id");
		String memo = rs.getString("memo");
		String bankCard = "";
		String thirdOrderNo = "";
		if(StringUtils.contains(memo, "(")) {
			bankCard = memo.substring(memo.lastIndexOf("(")+1, memo.length()-1);
			thirdOrderNo = memo.substring(0,memo.lastIndexOf("("));
		}
		String paymentNo = oldId + "";
		psmt.setString(1, bankCard);
		psmt.setString(2, paymentNo);
		psmt.setString(3, thirdOrderNo);
		psmt.setLong(4, Long.valueOf(oldId));
		psmt.executeUpdate();
		return true;
	}
    
}
