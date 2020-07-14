package com.jxf.task.tasks;


import com.jxf.svc.config.Global;
import com.jxf.svc.utils.AreaUtils;
import com.jxf.svc.utils.CalendarUtil;
import com.jxf.svc.utils.Exceptions;
import com.jxf.ufang.entity.UfangLoanMarket;
import com.jxf.ufang.entity.UfangLoanMarketApplyer;
import com.jxf.ufang.entity.UfangLoanMarketApplyer.OperatorStatus;
import com.jxf.ufang.entity.UfangLoanMarketApplyer.RealNameStatus;
import com.jxf.ufang.entity.UfangLoanMarketApplyer.SesameStatus;
import com.jxf.ufang.service.UfangLoanMarketApplyerService;
import com.jxf.ufang.service.UfangLoaneeDataService;

import java.util.Date;
import java.util.List;

import org.ebaoquan.rop.thirdparty.org.apache.commons.lang3.StringUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @description 贷超申请人转流量
 * @author suHuimin
 */
@DisallowConcurrentExecution
public class LoanMarketApplyerToLoaneeDataTask implements Job {

	private static final Logger log = LoggerFactory.getLogger(LoanMarketApplyerToLoaneeDataTask.class);
	
    @Autowired
    private UfangLoaneeDataService ufangLoaneeDataService;
    @Autowired
    private UfangLoanMarketApplyerService ufangLoanMarketApplyerService;
    
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
    		
    	UfangLoanMarketApplyer ufangLoanMarketApplyer = new UfangLoanMarketApplyer();
    	ufangLoanMarketApplyer.setUfangLoanMarket(new UfangLoanMarket(Long.valueOf(Global.getConfig("ufangDebtId"))));
    	ufangLoanMarketApplyer.setPushStatus(UfangLoanMarketApplyer.PushStatus.pendingPush);
    	Date beginTime = CalendarUtil.addDay(new Date(), -1);
    	ufangLoanMarketApplyer.setBeginTime(beginTime);
		ufangLoanMarketApplyer.setEndTime(new Date());
    	List<UfangLoanMarketApplyer> applyerList = ufangLoanMarketApplyerService.findList(ufangLoanMarketApplyer);
    	for (UfangLoanMarketApplyer applyer : applyerList) {

    		
    		if(StringUtils.isNoneBlank(applyer.getApplyIp())) {
    			String ip = StringUtils.contains(applyer.getApplyIp(), ",")?StringUtils.substringBefore(applyer.getApplyIp(), ","):applyer.getApplyIp();
    			String applyArea = AreaUtils.ipToAreaByGeoLite2(ip);
    			applyer.setApplyArea(applyArea);
    		}
    		if(StringUtils.isNoneBlank(applyer.getPhoneNo())) {
    			String phoneArea = AreaUtils.getAreaByPhoneNo(applyer.getPhoneNo());
    			applyer.setPhoneArea(phoneArea);
    		}  		
    		applyer.setPushStatus(UfangLoanMarketApplyer.PushStatus.hasPush);
    		
    		if(!applyer.getRealNameStatus().equals(RealNameStatus.authed) 
    				|| applyer.getSesameStatus().equals(SesameStatus.unauth) || applyer.getSesameScore()== 0 
    				|| applyer.getOperatorStatus().equals(OperatorStatus.unauth) || StringUtils.isBlank(applyer.getReportTaskId())) {
    			continue;
    		}
    		if(applyer.getSesameScore()<500) {
    			continue;
    		}
    		try {    							
    			ufangLoaneeDataService.pushLoaneeData(applyer);
    			log.info("推送流量：{}",applyer.getPhoneNo());
  	
			} catch (Exception e) {
				log.error("贷超申请人{}转换流量出错！异常：{}",applyer.getId(),Exceptions.getStackTraceAsString(e));
			}
    		
    		ufangLoanMarketApplyerService.save(applyer);
    	}
    }
}