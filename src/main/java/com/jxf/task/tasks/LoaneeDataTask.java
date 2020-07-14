package com.jxf.task.tasks;



import com.jxf.svc.utils.CalendarUtil;
import com.jxf.ufang.entity.UfangLoaneeData;

import com.jxf.ufang.service.UfangLoaneeDataService;
import com.jxf.ufang.util.UfangLoaneeDataUtils;

import java.math.BigDecimal;
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
 * @description 流量处理
 * @author wo
 */
@DisallowConcurrentExecution
public class LoaneeDataTask implements Job {

	private static final Logger log = LoggerFactory.getLogger(LoaneeDataTask.class);
	
    @Autowired
    private UfangLoaneeDataService ufangLoaneeDataService;
    
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
    	
    	discount5();
    	discount2();

    }
    
    
    private void discount5() {
    	log.info("对申请时间超过7天小于等于15的数据做5折处理");
    	UfangLoaneeData ufangLoaneeData = new UfangLoaneeData();
    	Date beginTime = CalendarUtil.addDay(new Date(), -15);
    	Date endTime = CalendarUtil.addDay(new Date(), -8);
    	ufangLoaneeData.setBeginApplyTime(beginTime);
    	ufangLoaneeData.setEndApplyTime(endTime);
    	ufangLoaneeData.setProdCode("001");
        List<UfangLoaneeData> dataList = ufangLoaneeDataService.findList(ufangLoaneeData);
        for(UfangLoaneeData data:dataList) {
        	data.setPrice(new BigDecimal("3"));
        	data.setProdCode("003");
        	ufangLoaneeDataService.save(data);
        }
    }
    
    private void discount2() {
    	log.info("对申请时间超过15天小于等于30的数据做2折处理");
    	UfangLoaneeData ufangLoaneeData = new UfangLoaneeData();
    	Date beginTime = CalendarUtil.addDay(new Date(), -30);
    	Date endTime = CalendarUtil.addDay(new Date(), -16);
    	ufangLoaneeData.setBeginApplyTime(beginTime);
    	ufangLoaneeData.setEndApplyTime(endTime);
    	ufangLoaneeData.setProdCode("003");//超过7天已经打折，超过15天重新打折
        List<UfangLoaneeData> dataList = ufangLoaneeDataService.findList(ufangLoaneeData);
        for(UfangLoaneeData data:dataList) {
        	data.setPrice(new BigDecimal("2.5"));
        	ufangLoaneeDataService.save(data);
        }
    }
}