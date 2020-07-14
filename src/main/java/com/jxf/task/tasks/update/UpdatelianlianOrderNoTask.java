package com.jxf.task.tasks.update;

import com.alibaba.fastjson.JSONObject;
import com.jxf.nfs.entity.NfsWdrlRecord;
import com.jxf.nfs.service.NfsWdrlRecordService;
import com.jxf.pwithdraw.entity.QueryPaymentRequestBean;
import com.jxf.pwithdraw.entity.QueryPaymentResponseBean;
import com.jxf.pwithdraw.service.LianlianPayService;
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.StringUtils;


import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *   更新已发送连连的提现订单的第三方订单号
 *
 * @author suHuimin
 */
@DisallowConcurrentExecution
public class UpdatelianlianOrderNoTask implements Job {
	private static final Logger logger = LoggerFactory.getLogger(UpdatelianlianOrderNoTask.class);
    @Autowired
    private NfsWdrlRecordService nfsWdrlRecordService;
    @Autowired
    private LianlianPayService lianlianPayService;
   
    
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.error("开始处理连连提现第三方单号");
		updateThirdOrderNo();
    	logger.error("处理连连提现第三方单号完毕");
	}
    
   private void updateThirdOrderNo() {
	   //获取连连提现已发送订单
	   NfsWdrlRecord wdrlRecord = new NfsWdrlRecord();
	   Date beginTime = null;
	   Date endTime = null;
	   try {
			beginTime = DateUtils.parse("2019-02-13 00:00:00");
			endTime = DateUtils.parse("2019-02-15 19:00:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}
	   wdrlRecord.setBeginTime(beginTime);
	   wdrlRecord.setEndTime(endTime);
	   List<NfsWdrlRecord> nfsWdrlRecords = nfsWdrlRecordService.findSubmitedNoThirdOrderNoRecord(wdrlRecord);
	   for (NfsWdrlRecord nfsWdrlRecord : nfsWdrlRecords) {
		   QueryPaymentRequestBean queryRequestBean = new QueryPaymentRequestBean();
		   queryRequestBean.setNo_order(nfsWdrlRecord.getId()+ "");
		   String result = lianlianPayService.queryOrder(queryRequestBean);
		   QueryPaymentResponseBean responseBean = JSONObject.parseObject(result, QueryPaymentResponseBean.class);
		   logger.error("连连查询接口返回： " + result);
		   String thirdOrderNo = responseBean.getOid_paybill();
		   String oldThirdNo = nfsWdrlRecord.getThirdOrderNo();
		   if(StringUtils.isBlank(thirdOrderNo)) {
			   nfsWdrlRecord.setThirdOrderNo(thirdOrderNo);
			   nfsWdrlRecordService.save(nfsWdrlRecord);
			   logger.error("提现订单：  " + nfsWdrlRecord.getId() + "第三方单号为空： " + thirdOrderNo);
		   }
		   if(StringUtils.equals(oldThirdNo, "201706221001845524")) {
			   nfsWdrlRecord.setThirdOrderNo(thirdOrderNo);
			   nfsWdrlRecordService.save(nfsWdrlRecord);
		   }
	   }
	   
   }
}