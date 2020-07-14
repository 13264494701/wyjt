package com.jxf.task.tasks.rc;



import java.sql.PreparedStatement;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.jxf.mem.entity.Member;
import com.jxf.mem.service.MemberService;

import com.jxf.rc.entity.RcCaData;
import com.jxf.svc.utils.CalendarUtil;
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.Exceptions;
import com.jxf.transplantation.dbo.NowDBOperator;
import com.jxf.transplantation.dbo.NowDBOperatorFactory;





/**
 * 迁移RcCaData数据
 *
 * @author xrd
 */
@DisallowConcurrentExecution
public class RcCaDataMongoToMysqlTask implements Job {

	private static Logger logger = LoggerFactory.getLogger(RcCaDataMongoToMysqlTask.class);

	@Autowired
	private MemberService memberService;
	@Autowired
	private MongoTemplate mongoTemplate;
	
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

		Date beginDate = CalendarUtil.StringToDate("2019-08-07");
		Date endDate = CalendarUtil.StringToDate("2019-08-09");
		Date date = beginDate;
		while (date.getTime() <= endDate.getTime()) {
			String dateStr = CalendarUtil.DateToString(date, "yyyy-MM-dd HH");
			logger.warn("开始获取{}的数据",dateStr);
			try {
			remove(dateStr);
			} catch (Exception e) {
				logger.error("出错{}",Exceptions.getStackTraceAsString(e));		
			}
			logger.warn("结束获取{}的数据",dateStr);
			date = CalendarUtil.addHour(date, 1);
		}
    }



	private boolean remove(String dateStr)  {
		
		Calendar beginCalendar = DateUtils.toCalendar(DateUtils.parseDate(dateStr));
//		beginCalendar.set(Calendar.HOUR_OF_DAY, beginCalendar.getActualMinimum(Calendar.HOUR_OF_DAY));
		beginCalendar.set(Calendar.MINUTE, beginCalendar.getActualMinimum(Calendar.MINUTE));
		beginCalendar.set(Calendar.SECOND, beginCalendar.getActualMinimum(Calendar.SECOND));
		Date beginTime = beginCalendar.getTime();

		Calendar endCalendar = DateUtils.toCalendar(DateUtils.parseDate(dateStr));
//		endCalendar.set(Calendar.HOUR_OF_DAY, endCalendar.getActualMaximum(Calendar.HOUR_OF_DAY));
		endCalendar.set(Calendar.MINUTE, endCalendar.getActualMaximum(Calendar.MINUTE));
		endCalendar.set(Calendar.SECOND, endCalendar.getActualMaximum(Calendar.SECOND));
		Date endTime = endCalendar.getTime();
//		logger.warn("开始获取{}的数据",beginTime);
//		logger.warn("结束获取{}的数据",endTime);
	

        
		Query query = new Query();
		query.addCriteria(Criteria.where("createTime").gte(beginTime).lte(endTime));
		List<RcCaData> caDataList = mongoTemplate.find(query, RcCaData.class);
			
		NowDBOperator nowOperator = NowDBOperatorFactory.getNowDBOperator();
		PreparedStatement psmt = null;
		
		String insertSql = " INSERT INTO RC_CA_DATA ( id," + // 1
				"phone_no," + // 2
				"id_no," + // 3
				"name," + // 4
				"type," + // 5
				"provider," + // 6
				"report_no," + // 7
				"content," + // 8
				"create_by,create_time,update_by,update_time,del_flag) VALUES (?,?,?,?,?,?,?,?,999999,?,999999,?,'0')";
		try {
			psmt = nowOperator.getPreparedStatement(insertSql);
			for(RcCaData caData:caDataList) {
				insertCaData(caData,psmt);
			}
		} catch (SQLException e) {
			logger.error("出错{}",Exceptions.getStackTraceAsString(e));		
			return false;
		}finally {
			try {
				psmt.close();
				NowDBOperatorFactory.addNowDBOperator(nowOperator);
			} catch (Exception e) {
				logger.error(Exceptions.getStackTraceAsString(e));	
			}
		}

    	
		return true;
	}
	
	
	private  boolean insertCaData(RcCaData caData, PreparedStatement psmt) throws SQLException {

		try {
	        Member member = memberService.findByUsername(caData.getPhoneNo());
	        if(member==null) {
	        	return false;
	        }
			psmt.setLong(1, caData.getId());
			psmt.setString(2, member.getUsername());
			psmt.setString(3, member.getIdNo());
			psmt.setString(4, member.getName());
			psmt.setInt(5, caData.getType().ordinal());
			psmt.setInt(6, 0);
			psmt.setString(7, "");
			psmt.setString(8, caData.getContent());
			psmt.setString(9, DateUtils.formatDateTime(caData.getCreateTime()));
			psmt.setString(10, DateUtils.formatDateTime(caData.getCreateTime()));
			psmt.executeUpdate();
			return true;
		} catch (Exception e) {
			logger.error("数据{}出错{}",caData.getId(),Exceptions.getStackTraceAsString(e));
			return false;
		}
	}
    

}