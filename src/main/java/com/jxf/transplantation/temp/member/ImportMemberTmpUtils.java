package com.jxf.transplantation.temp.member;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
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
 * @功能说明:舍弃手机号数据迁移
 */
public class ImportMemberTmpUtils {
	
	private static Logger log = LoggerFactory.getLogger(ImportMemberTmpUtils.class);

	static HashMap<String,String> map = new HashMap<String , String>();
	
	public static void main(String[] args) {
    	importMember(1L,5000L);
    }
	

	public static int importMember(Long startId,Long endId) {
		OldDBOperator oldOperator = OldDBOperatorFactory.getDBOperator();
		NowDBOperator nowOperator = NowDBOperatorFactory.getNowDBOperator();
		PreparedStatement psmt = null;
		ResultSet rs = null;
	
		String querysql = "select *  from  t_user where id >=? AND id <=? GROUP BY username ORDER BY id  ";	
		
	   	Date startDate = new Date();
	   	log.debug("开始时间:"+DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
	   	int i = 0;
	   	try {
	   		
			psmt = oldOperator.getPreparedStatement(querysql);
			psmt.setLong(1, startId);
			psmt.setLong(2, endId);
			rs = psmt.executeQuery();
			
            //会员                                              1    2         3         4             5             6      7        8      9          10     11    12    13              14       15         16         17         18       19            20            21                    22            23               24                 25
			String insertMemberSql =" INSERT INTO MEM_MEMBER_TMP ( id, username,spareMobile,create_by,create_time,update_by,update_time,del_flag) VALUES (?,?,?,'999999',?,'999999',?,0)";
			psmt = nowOperator.getPreparedStatement(insertMemberSql);
			
			while (rs.next()) {
				++i;
				log.debug("开始第"+i+"条数据迁移");
				boolean success = transplantMember(rs,psmt);
				if(success){
					log.debug("第"+i+"条迁移成功");
				}else{
					i--;
				}
			}
		    psmt.close();	
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
	
    private static boolean transplantMember(ResultSet rs, PreparedStatement psmt) throws SQLException {
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
    		String nowDate = DateUtils.getDate("yyyy-MM-dd HH:mm:ss");
    		psmt.setLong(1, rs.getLong("id"));
    		psmt.setString(2, usernameStr);
    		psmt.setString(3, spareMobileStrStr);
    		psmt.setString(4, nowDate);
    		psmt.setString(5, nowDate);
    		psmt.executeUpdate();
    		return true;
    	}else {
    		return false;
    	}
		
	}
    
	
	

}
