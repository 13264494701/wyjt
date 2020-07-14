package com.jxf.transplantation.temp.withdraw;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.Exceptions;
import com.jxf.svc.utils.StringUtils;
import com.jxf.transplantation.dbo.NowDBOperator;
import com.jxf.transplantation.dbo.NowDBOperatorFactory;
import com.jxf.transplantation.dbo.OldDBOperator;
import com.jxf.transplantation.dbo.OldDBOperatorFactory;

/**
 * @作者: suHuimin
 * @创建时间 :2019年1月22日
 * @功能说明:提现记录表数据迁移
 */
public class ImportWithdrawRecordUtils {
	
	private static Logger log = LoggerFactory.getLogger(ImportWithdrawRecordUtils.class);

	public static void main(String[] args) {
		ImportWithdrawRecord(1360664L,1361000L);
    }
	
	@SuppressWarnings("resource")
	public static int ImportWithdrawRecord(Long startId,Long endId) {
		OldDBOperator oldOperator = OldDBOperatorFactory.getDBOperator();
		NowDBOperator nowOperator = NowDBOperatorFactory.getNowDBOperator();
		PreparedStatement psmt = null;
		ResultSet rs = null;

		String sql = "SELECT * FROM t_withdraw_order WHERE id >=? AND id <=?;";
		Date startDate = new Date();
	   	log.debug("开始时间:"+DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
	   	int i = 0;
	   	try {
			psmt = oldOperator.getPreparedStatement(sql);
			psmt.setLong(1, startId);
			psmt.setLong(2, endId);
			rs = psmt.executeQuery();
			
			String insertMemberSql =" INSERT INTO NFS_WDRL_RECORD ( id," //1
					+ "member_id,"//2
					+ "member_name,"//3
					+ "member_username,"//4
					+ "type,"//5
					+ "third_order_no,"//6
					+ "amount,"//7
					+ "pay_amount,"//8
					+ "fee,"//9
					+ "bank_id,"//10
					+ "bank_name,"//11
					+ "card_no,"//12
					+ "check_time,"//13
					+ "pay_time,"//14
					+ "status,"//15
					+ "rmk,"//16
					+ "create_by,"//17
					+ "create_time,"//18
					+ "update_by,"//29
					+ "update_time,"//20 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 
					+ "del_flag) VALUES (?,?,?,?,?,?,?,?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,0)";
			psmt = nowOperator.getPreparedStatement(insertMemberSql);
			
			while (rs.next()) {
				++i;
				boolean success = transplantWithdrawRecord(rs,psmt);
				if(success){
				}else{
					i--;
				}
			}
		    log.debug("结束,共"+i+"条数据迁移成功");
		    log.debug("用时:"+ DateUtils.pastMinutes(startDate) + "分钟");
		} catch (Exception e) {
			log.error(Exceptions.getStackTraceAsString(e));			
		}finally {
			try {
				psmt.close();
			} catch (Exception e) {
				log.error(Exceptions.getStackTraceAsString(e));	
			}	
		}
	   	return i;

	}
	
    private static boolean transplantWithdrawRecord(ResultSet rs, PreparedStatement psmt) throws Exception {
		String oldId = rs.getString("id");
		String userId = rs.getString("user_id");
		String realname = rs.getString("realname");
		String money = rs.getString("money");
		String fee = rs.getString("commission");
		String state = rs.getString("state");
		String mobile = rs.getString("mobile");
		String bankCardNo = rs.getString("bank_card_no");
		String bank_name = rs.getString("bank_name");

		String serialNo = rs.getString("serial_no"); 
		String remark = rs.getString("remark");

		String create_time = rs.getString("create_time");
		String update_time = rs.getString("last_modify_time");
		psmt.setLong(1,Long.valueOf(oldId));
		psmt.setLong(2, Long.valueOf(userId));
		psmt.setString(3, realname == null ? "" : realname);
		psmt.setString(4, mobile == null ? "" : mobile);
		int type = 0;//系统默认
		psmt.setInt(5, type);
		psmt.setString(6, serialNo);
		psmt.setBigDecimal(7, StringUtils.toDecimal(money).add(BigDecimal.ONE));
		psmt.setBigDecimal(8, StringUtils.toDecimal(money));
		psmt.setBigDecimal(9, StringUtils.toDecimal(fee));
		psmt.setLong(10, 0);//bankId先设为0后面再统一update
		psmt.setString(11, bank_name);//bankName先不设值再统一update
		psmt.setString(12, bankCardNo);
		psmt.setString(13, create_time);
		psmt.setString(14, update_time);
		
//		 100申请中待审核 150 客服推迟审核 200 待提交  300 已提交 310 疑似重复订单 320 重复订单发送(人工查询) 330 未响应待查询后发送(人工查询)
//	     350 富友已发送 400 已成功 500客服审核失败(已退换用户金额) 600反馈失败(未退还用户金额)  700已取消
//       待审核 0，待复审 1 ，待打款 2， 已提交 3 ，疑似重复订单4，重复订单发送(人工查询) 5，已打款6，打款失败7，已拒绝8 ,已取消 9
		if(state.equals("100")) {
			psmt.setInt(15, 0);
		}else if(state.equals("150")) {
			psmt.setInt(15, 1);
		}else if(state.equals("200")) {
			psmt.setInt(15, 2);
		}else if(state.equals("300")) {
			psmt.setInt(15, 3);
		}else if(state.equals("310")) {
			psmt.setInt(15, 4);
		}else if(state.equals("320")) {
			psmt.setInt(15, 5);
		}else if(state.equals("330")) {
			psmt.setInt(15, 2);
		}else if(state.equals("350")) {
			psmt.setInt(15, 3);
		}else if(state.equals("400")) {
			psmt.setInt(15, 6);
		}else if(state.equals("500")) {
			psmt.setInt(15, 8);
		}else if(state.equals("600")) {
			psmt.setInt(15, 7);
		}else if(state.equals("700")) {
			psmt.setInt(15, 9);
		}else {
			psmt.setInt(15, 7);
		}
		psmt.setString(16, remark);
		psmt.setString(17, "999999");
		psmt.setString(18, create_time);
		psmt.setString(19, "999999");
		psmt.setString(20, update_time);
		psmt.executeUpdate();
		return true;
	}
}
