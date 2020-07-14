package com.jxf.transplantation.temp.member;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Date;
import java.util.HashMap;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.Exceptions;

import com.jxf.transplantation.dbo.OldDBOperator;
import com.jxf.transplantation.dbo.OldDBOperatorFactory;
import com.jxf.transplantation.dbo.NowDBOperator;
import com.jxf.transplantation.dbo.NowDBOperatorFactory;

/**
 * @作者: gaobo
 * @创建时间 :2018年11月19日 上午9:38:09
 * @功能说明:手机号数据迁移
 */
public class ImportPhoneBook {
	
	private static Logger log = LoggerFactory.getLogger(ImportPhoneBook.class);

	static HashMap<String,String> map = new HashMap<String , String>();
	
	public static void main(String[] args) {
    	importNumbers(1L,100L);
    }
	

	public static int importNumbers(Long startId,Long endId) {
		OldDBOperator oldOperator = OldDBOperatorFactory.getDBOperator();
		NowDBOperator nowOperator = NowDBOperatorFactory.getNowDBOperator();
		PreparedStatement psmt = null;
		PreparedStatement insertNumsPsmt = null;
		ResultSet rs = null;
	
		String querysql = "select *  from  t_user_phonenums where id >=? AND id <=?";	
		
	   	Date startDate = new Date();
	   	log.debug("开始时间:"+DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
	   	int i = 0;
	   	try {
	   		
			psmt = oldOperator.getPreparedStatement(querysql);
			psmt.setLong(1, startId);
			psmt.setLong(2, endId);
			rs = psmt.executeQuery();
			
            //通讯录                                                      
			String insertNums =" INSERT INTO MEM_MEMBER_PHONEBOOK (id, member_id, username, phone_list, phone_book, create_by, create_time, update_by, update_time, del_flag) VALUES (?,?,?,?,?,'999999',?,'999999',?,0)";
			insertNumsPsmt = nowOperator.getPreparedStatement(insertNums);
			
			while (rs.next()) {
				++i;
				log.debug("开始第"+i+"条数据迁移");
				boolean success = transplantMember(rs,insertNumsPsmt);
				if(success){
					log.debug("第"+i+"条迁移成功");
				}else{
					i--;
				}
			}
		    psmt.close();	
		    insertNumsPsmt.close();	
		    oldOperator.close();
		    nowOperator.close();
			OldDBOperatorFactory.addDBOperator(oldOperator);
			NowDBOperatorFactory.addNowDBOperator(nowOperator);
			log.debug("结束,共"+i+"条数据迁移成功");
			log.debug("用时:"+ DateUtils.pastMinutes(startDate) + "分钟");
			return i;
		} catch (SQLException e) {
			log.error(Exceptions.getStackTraceAsString(e));	
			return i;
		}

	}
	
    private static boolean transplantMember(ResultSet rs, PreparedStatement insertNumsPsmt) throws SQLException {
    	
    	String id = rs.getString("id");
    	String memberId = rs.getString("userId");
    	String userMobile = rs.getString("userMobile");
    	String phoneNums = rs.getString("phoneNums");
    	String phoneNameAndNums = rs.getString("phoneNameAndNums");
    	String createTime = rs.getString("createTime");
    	
		
    	insertNumsPsmt.setString(1, id);
    	insertNumsPsmt.setString(2, memberId);
    	insertNumsPsmt.setString(3, userMobile);
    	insertNumsPsmt.setString(4, phoneNums);
    	insertNumsPsmt.setString(5, phoneNameAndNums);
    	insertNumsPsmt.setString(6, createTime);
    	insertNumsPsmt.setString(7, createTime);
    	insertNumsPsmt.executeUpdate();
    	return true;
		
	}
    
	
	

}
