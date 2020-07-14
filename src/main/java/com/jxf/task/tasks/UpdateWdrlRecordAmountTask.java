package com.jxf.task.tasks;

import com.alibaba.fastjson.JSONObject;
import com.jxf.nfs.entity.NfsWdrlRecord;
import com.jxf.nfs.entity.NfsWdrlRecord.Status;
import com.jxf.nfs.service.NfsWdrlRecordService;
import com.jxf.pwithdraw.entity.PaymentStatusEnum;
import com.jxf.pwithdraw.entity.QueryPaymentRequestBean;
import com.jxf.pwithdraw.entity.QueryPaymentResponseBean;
import com.jxf.pwithdraw.service.LianlianPayService;
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.Exceptions;
import com.jxf.svc.utils.StringUtils;

import java.math.BigDecimal;
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
 *   更新已发送连连的提现订单的金额
 *
 * @author suHuimin
 */
@DisallowConcurrentExecution
public class UpdateWdrlRecordAmountTask implements Job {
	private static final Logger logger = LoggerFactory.getLogger(UpdateWdrlRecordAmountTask.class);
    @Autowired
    private NfsWdrlRecordService nfsWdrlRecordService;
    @Autowired
    private LianlianPayService lianlianPayService;
   
    
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.error("开始处理连连提现金额");
		updateWdrlRecordAmount();
    	logger.error("处理连连提现金额完毕");
	}
    
   private void updateWdrlRecordAmount() {
	   //获取连连提现已发送订单
	   NfsWdrlRecord wdrlRecord = new NfsWdrlRecord();
	   Date beginTime = null;
	   try {
			beginTime = DateUtils.parse("2019-02-11 00:01:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}
	   wdrlRecord.setBeginTime(beginTime);
	   wdrlRecord.setType(NfsWdrlRecord.Type.lianlian);
	   wdrlRecord.setStatus(Status.madeMoney);
	   List<NfsWdrlRecord> wdrlRecordList = nfsWdrlRecordService.findList(wdrlRecord);//打款成功订单
	   logger.error("连连提现打款成功订单待处理数为：{}",wdrlRecordList.size());
	   int count = 0;
	   for (NfsWdrlRecord nfsWdrlRecord : wdrlRecordList) {
		   try {
			QueryPaymentRequestBean queryRequestBean = new QueryPaymentRequestBean();
			   queryRequestBean.setNo_order(nfsWdrlRecord.getId()+ "");
			   String result = lianlianPayService.queryOrder(queryRequestBean);
			   QueryPaymentResponseBean responseBean = JSONObject.parseObject(result, QueryPaymentResponseBean.class);
			   if(StringUtils.equals(responseBean.getResult_pay(), PaymentStatusEnum.PAYMENT_SUCCESS.getValue()) ) {
				   if(StringUtils.isNotBlank(responseBean.getMoney_order())) {
					   BigDecimal payAmount = StringUtils.toDecimal(responseBean.getMoney_order());//连连付款金额
					   BigDecimal amount = payAmount.add(BigDecimal.ONE);
					   nfsWdrlRecord.setAmount(amount);
					   nfsWdrlRecord.setPayAmount(payAmount);
					   nfsWdrlRecordService.save(nfsWdrlRecord);
					   count ++;
				   }
			   }else {
				   logger.error("提现订单{}连连查询接口返回： {}" ,nfsWdrlRecord.getId(),result);
			   }
		} catch (Exception e) {
			logger.error("提现订单{}更新异常{}",nfsWdrlRecord.getId(),Exceptions.getStackTraceAsString(e));
		}
	   }
	   logger.error("共处理打款成功订单{}笔",count);
	  /* NfsWdrlRecord wdrlRecord2 = new NfsWdrlRecord();
	   wdrlRecord2.setBeginTime(beginTime);
	   wdrlRecord2.setType(NfsWdrlRecord.Type.lianlian);
	   wdrlRecord2.setStatus(Status.failure);
	   List<NfsWdrlRecord> wdrlRecordList2 = nfsWdrlRecordService.findList(wdrlRecord2);//打款失败订单
	   logger.error("连连提现打款失败订单待处理数为：{}",wdrlRecordList2.size());
	   count = 0;
	   for (NfsWdrlRecord nfsWdrlRecord : wdrlRecordList2) {
		   try {
			QueryPaymentRequestBean queryRequestBean = new QueryPaymentRequestBean();
			   queryRequestBean.setNo_order(nfsWdrlRecord.getId()+ "");
			   String result = lianlianPayService.queryOrder(queryRequestBean);
			   QueryPaymentResponseBean responseBean = JSONObject.parseObject(result, QueryPaymentResponseBean.class);
			   if(StringUtils.equals(responseBean.getResult_pay(), PaymentStatusEnum.PAYMENT_CLOSED.getValue()) || 
					   StringUtils.equals(responseBean.getResult_pay(), PaymentStatusEnum.PAYMENT_FAILURE.getValue()) || 
					   StringUtils.equals(responseBean.getResult_pay(), PaymentStatusEnum.PAYMENT_RETURN.getValue())) {
				   if(StringUtils.isNotBlank(responseBean.getMoney_order())) {
					   BigDecimal payAmount = StringUtils.toDecimal(responseBean.getMoney_order());//连连付款金额
					   BigDecimal amount = payAmount.add(BigDecimal.ONE);
					   nfsWdrlRecord.setAmount(amount);
					   nfsWdrlRecord.setPayAmount(payAmount);
					   nfsWdrlRecordService.save(nfsWdrlRecord);
					   count ++;
				   }
			   }else {
				   logger.error("提现订单{}连连查询接口返回： {}" ,nfsWdrlRecord.getId(),result);
			   }
			} catch (Exception e) {
				logger.error("提现订单{}更新异常{}",nfsWdrlRecord.getId(),Exceptions.getStackTraceAsString(e));
			}
	   }
	   logger.error("共处理打款失败订单{}笔",count);*/
   }
}