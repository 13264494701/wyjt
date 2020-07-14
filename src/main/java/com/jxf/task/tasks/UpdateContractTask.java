package com.jxf.task.tasks;

import java.io.File;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.jxf.loan.entity.NfsLoanContract;
import com.jxf.loan.service.NfsLoanContractService;
import com.jxf.svc.config.Global;
import com.jxf.svc.utils.CalendarUtil;
import com.jxf.svc.utils.Exceptions;;

/**
 * 异常合同处理
 * 
 * @author wo
 *
 */
@DisallowConcurrentExecution
public class UpdateContractTask implements Job {

	private static final Logger log = LoggerFactory.getLogger(UpdateContractTask.class);

	@Autowired
	private NfsLoanContractService loanContractService;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		
		Date today = CalendarUtil.getCurrentDate();
		Date yestoday = CalendarUtil.addDay(today, -1);


		String todayStr = CalendarUtil.DateToString(today, "yyyy/MM/dd");
		proTodayData(todayStr);
		
		String yestodayStr = CalendarUtil.DateToString(yestoday, "yyyy/MM/dd");
		proYestodayData(yestodayStr);
	
	
	}

	private void proTodayData(String dateStr) {
		log.warn("处理日期{}开始", dateStr);
		int i = 0, j = 0;
		try {
			File fileday = new File(Global.getBaseStaticPath() + Global.getConfig("junziqianContractPath") + dateStr);
			if (fileday.exists()) {
				File[] files = fileday.listFiles();
				if (files.length == 0) {
					log.error("文件夹是空的!+13");
					return;
				}
				for (File file2 : files) {
					//错乱合同
					if (file2.length() < 200000 && file2.getAbsolutePath().contains("junziqian_signed_")) {
						String absolutePath = file2.getAbsolutePath();
						String conId = absolutePath.substring(StringUtils.lastIndexOf(absolutePath, "_") + 1,
								absolutePath.indexOf("."));

						NfsLoanContract nfsLoanContract = loanContractService.get(Long.valueOf(conId));
						nfsLoanContract.setStatus(NfsLoanContract.Status.notCreate);
						loanContractService.save(nfsLoanContract);
						j++;
					}
				}

			}
			log.warn("处理日期{}结束,一共处理{}个缺章文件和{}个错乱文件", dateStr, i, j);
		} catch (Exception e) {
			log.error(Exceptions.getStackTraceAsString(e));
		}
	}
	
	private void proYestodayData(String dateStr) {
		log.warn("处理日期{}开始", dateStr);
		int i = 0, j = 0;
		try {
			File fileday = new File(Global.getBaseStaticPath() + Global.getConfig("junziqianContractPath") + dateStr);
			if (fileday.exists()) {
				File[] files = fileday.listFiles();
				if (files.length == 0) {
					log.error("文件夹是空的!+13");
					return;
				}
				for (File file2 : files) {
					//缺章合同
					if ((file2.length() >= 200000 && file2.length() < 220000)
							&& file2.getAbsolutePath().contains("junziqian_signed_")) {
						String absolutePath = file2.getAbsolutePath();
						String conId = absolutePath.substring(StringUtils.lastIndexOf(absolutePath, "_") + 1,
								absolutePath.indexOf("."));

						NfsLoanContract nfsLoanContract = loanContractService.get(Long.valueOf(conId));
						nfsLoanContract.setStatus(NfsLoanContract.Status.created);
						loanContractService.save(nfsLoanContract);
						i++;
					}
				}

			}
			log.warn("处理日期{}结束,一共处理{}个缺章文件和{}个错乱文件", dateStr, i, j);
		} catch (Exception e) {
			log.error(Exceptions.getStackTraceAsString(e));
		}
	}

}