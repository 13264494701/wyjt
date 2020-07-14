package com.jxf.transplantation.temp.member;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.Exceptions;
import com.jxf.transplantation.dbo.NowDBOperator;
import com.jxf.transplantation.dbo.NowDBOperatorFactory;



/**
 * @作者: xrd
 * @创建时间 :2019年1月3日
 * @功能说明:流水金额为0 问题   
 */
public class UpdateMemberTrxUtils {
	
	private static Logger log = LoggerFactory.getLogger(UpdateMemberTrxUtils.class);
	
	
	static HashMap<String,String> map = new HashMap<String , String>();
	
	public static void main(String[] args)  {
		updateTrx(1L,1111111111L);
    }
	
	public static int updateTrx(long startId, long endId)  {
		NowDBOperator nowOperator = NowDBOperatorFactory.getNowDBOperator();
		PreparedStatement psmtQueryTrx = null;	
		PreparedStatement psmtUpdateAct = null;	
		ResultSet rs = null;
		
		String trxQuery = "select * from  MEM_MEMBER_ACT_TRX  where org_id = 1 AND id >= ? AND id <=? ORDER BY id" ;
		
		String updateActSql = "UPDATE MEM_MEMBER_ACT_TRX  SET org_id = ? where id = ?" ;
		
		log.debug("开始时间:{}",DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
	    long startTime=System.currentTimeMillis();
	   	int i = 0;
	   	String trx_id = "";
	   	String rmk = "";
	   	try {
	   		psmtQueryTrx = nowOperator.getPreparedStatement(trxQuery);
	   		psmtQueryTrx.setLong(1, startId);
			psmtQueryTrx.setLong(2, endId);
			rs = psmtQueryTrx.executeQuery();
				
			psmtUpdateAct = nowOperator.getPreparedStatement(updateActSql);
			
		    while (rs.next()) {
			++i;
			boolean success = true;
			trx_id = rs.getString("id");
			rmk = rs.getString("rmk");
//			log.warn("第{}条数据,ID:{}",i,trx_id);
			success = update(trx_id,rmk,psmtUpdateAct);
			if(success){
			}else{
				i--;
			}
		}
	    long endTime=System.currentTimeMillis();
	    float excTime=(float)(endTime-startTime)/1000;
	    log.warn("一组结束,共{}条数据迁移成功",i);
	    log.warn("用时:{}毫秒",excTime);
		} catch (SQLException e) {
			log.error("第{}条数据迁移出错了!出错了!出错了!出错了!出错了!,流水ID:{}",i,rmk);
			log.error(Exceptions.getStackTraceAsString(e));
		} catch (Exception e) {
			log.error("第{}条数据迁移出错了!出错了!出错了!出错了!出错了!,流水ID:{}",i,rmk);
			log.error(Exceptions.getStackTraceAsString(e));
		}finally {
		    try {
		    	psmtQueryTrx.close();	
			    psmtUpdateAct.close();		   
			    NowDBOperatorFactory.addNowDBOperator(nowOperator);
			} catch (SQLException e) {
				log.error("第{}条数据迁移出错,流水备注:{}",trx_id,rmk);
				log.error(Exceptions.getStackTraceAsString(e));
			} catch (Exception e) {
				log.error("第{}条数据迁移出错,流水备注:{}",trx_id,rmk);
				log.error(Exceptions.getStackTraceAsString(e));
			}
		}
	   	
	   	return i;

	}
	
    private static boolean update(String trx_id,String rmk ,PreparedStatement psmtUpdateAct) throws Exception {

    	String[] orgIdStrs = StringUtils.substringsBetween(rmk, "[", "]");
    	if(orgIdStrs==null) {
    		return false;
    	}
    	String org_id = "";
    	for(String str:orgIdStrs) {
    		if(isNumeric(str)) {
    			org_id = str;
    		}	
    	}
    	if(StringUtils.isNoneBlank(org_id)) {
    		try {
        	  psmtUpdateAct.setString(1, org_id);
        	  psmtUpdateAct.setString(2, trx_id);
        	  psmtUpdateAct.executeUpdate();
    		}catch (SQLException e) {
				log.error("第{}条数据迁移出错,流水备注:{}",trx_id,rmk);
				log.error(Exceptions.getStackTraceAsString(e)); 
        	}finally {
        		
        	}
    	}
		return true;
	}
    
    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();   
    }
}
