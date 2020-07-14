package com.jxf.transplantation.temp;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.Exceptions;
import com.jxf.transplantation.dbo.NowDBOperator;
import com.jxf.transplantation.dbo.NowDBOperatorFactory;



/**
 * @作者: wo
 * @创建时间 :2019年7月23日
 * @功能说明:  
 */
public class UpdateUfangLoaneeDataUtils {
	
	private static Logger log = LoggerFactory.getLogger(UpdateUfangLoaneeDataUtils.class);
	
	
	static HashMap<String,String> map = new HashMap<String , String>();
	
	public static void main(String[] args)  {
		updateLoaneeData();
    }
	
	public static int updateLoaneeData()  {
		NowDBOperator nowOperator = NowDBOperatorFactory.getNowDBOperator();
		PreparedStatement psmtQueryLoaneeData = null;	
		PreparedStatement psmtQueryByPhoneNo = null;	
		PreparedStatement psmtQueryDataOrderByDataId = null;
		PreparedStatement psmtUpdateLoaneeData = null;	
		PreparedStatement psmtUpdateLoaneeOrderData = null;	
		ResultSet rs = null;
		
		String chongfuQuery = "select phone_no,count(*) as count from UFANG_LOANEE_DATA group by phone_no having count>1" ;
		
		String queryByPhoneNo = "SELECT id FROM UFANG_LOANEE_DATA WHERE phone_no = ?" ;
		
		String queryDataOrderByDataId = "SELECT COUNT(*) AS count FROM UFANG_LOANEE_DATA_ORDER WHERE data_id = ?" ;
		
		String updateLoaneeDataSql = "UPDATE UFANG_LOANEE_DATA SET del_flag = '1' WHERE id = ?";
		
		String updateLoaneeDataOrderSql = "UPDATE UFANG_LOANEE_DATA_ORDER set data_id = ? WHERE data_id = ?";
		
		log.debug("开始时间:{}",DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
	    long startTime=System.currentTimeMillis();
	   	int i = 0;
	   	String phone_no = "";
	   	try {
	   		psmtQueryLoaneeData = nowOperator.getPreparedStatement(chongfuQuery);
			rs = psmtQueryLoaneeData.executeQuery();
				
			psmtQueryByPhoneNo = nowOperator.getPreparedStatement(queryByPhoneNo);
			psmtQueryDataOrderByDataId = nowOperator.getPreparedStatement(queryDataOrderByDataId);
			
			psmtUpdateLoaneeData = nowOperator.getPreparedStatement(updateLoaneeDataSql);
			
			psmtUpdateLoaneeOrderData = nowOperator.getPreparedStatement(updateLoaneeDataOrderSql);
			
			
		    while (rs.next()) {
			++i;
			boolean success = true;
			phone_no = rs.getString("phone_no");
			success = update(phone_no,psmtQueryByPhoneNo,psmtQueryDataOrderByDataId,psmtUpdateLoaneeData,psmtUpdateLoaneeOrderData);
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
			log.error("{}出错了!出错了!出错了!出错了!出错了!",phone_no);
			log.error(Exceptions.getStackTraceAsString(e));
		} catch (Exception e) {
			log.error("{}出错了!出错了!出错了!出错了!出错了!",phone_no);
			log.error(Exceptions.getStackTraceAsString(e));
		}finally {
		    try {
		    	psmtQueryLoaneeData.close();	
		    	psmtQueryByPhoneNo.close();		   
			    NowDBOperatorFactory.addNowDBOperator(nowOperator);
			} catch (SQLException e) {
				log.error("{}出错",phone_no);
				log.error(Exceptions.getStackTraceAsString(e));
			} catch (Exception e) {
				log.error("{}出错",phone_no);
				log.error(Exceptions.getStackTraceAsString(e));
			}
		}
	   	
	   	return i;

	}
	
    private static boolean update(String phone_no,PreparedStatement psmtQueryByPhoneNo,PreparedStatement psmtQueryDataOrderByDataId ,PreparedStatement psmtUpdateLoaneeData,PreparedStatement psmtUpdateLoaneeOrderData) throws Exception {


    	psmtQueryByPhoneNo.setString(1, phone_no);
    	ResultSet rs = psmtQueryByPhoneNo.executeQuery();
    	int j = 0;
    	String first_data_id = "";
		while (rs.next()) {
			String data_id = rs.getString("id");
			j++;
			if(j==1) {
				first_data_id = data_id;
			}
			if(j>1){
				updateOrder(first_data_id, data_id, psmtUpdateLoaneeOrderData);
				delete(data_id, psmtQueryDataOrderByDataId,psmtUpdateLoaneeData);
			}	
		}

		return true;
	}
    
    public static boolean delete(String data_id,PreparedStatement psmtQueryDataOrderByDataId,PreparedStatement psmtUpdateLoaneeData) throws SQLException{

    	psmtQueryDataOrderByDataId.setString(1, data_id);
    	ResultSet rs = psmtQueryDataOrderByDataId.executeQuery();
    	Integer count = 0;
    	while (rs.next()) {
    		count = rs.getInt("count");	
    	}
    	if(count>0) {
    		return false;
    	}
    	psmtUpdateLoaneeData.setString(1, data_id);
    	psmtUpdateLoaneeData.executeUpdate();
        return true;   
    }
    
    public static boolean updateOrder(String first_data_id,String data_id,PreparedStatement psmtUpdateLoaneeOrderData) throws SQLException{

    	psmtUpdateLoaneeOrderData.setString(1, first_data_id);
    	psmtUpdateLoaneeOrderData.setString(2, data_id);
    	psmtUpdateLoaneeOrderData.executeUpdate();
        return true;   
    }
}
