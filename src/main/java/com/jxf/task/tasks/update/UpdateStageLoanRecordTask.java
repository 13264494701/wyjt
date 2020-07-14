package com.jxf.task.tasks.update;

import com.jxf.loan.entity.NfsLoanApply.RepayType;
import com.jxf.loan.entity.NfsLoanRecord;
import com.jxf.loan.entity.NfsLoanRepayRecord;
import com.jxf.loan.service.NfsLoanRecordService;
import com.jxf.loan.service.NfsLoanRepayRecordService;
import com.jxf.svc.utils.CalendarUtil;
import com.jxf.svc.utils.Collections3;
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.Exceptions;
import com.jxf.transplantation.dbo.NowDBOperator;
import com.jxf.transplantation.dbo.NowDBOperatorFactory;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
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
 *   获取视频认证通过会员所在的城市
 * @author suHuimin
 */
@DisallowConcurrentExecution
public class UpdateStageLoanRecordTask implements Job {
	private static final Logger logger = LoggerFactory.getLogger(UpdateStageLoanRecordTask.class);
    @Autowired
    private NfsLoanRepayRecordService nfsLoanRepayRecordService;
    @Autowired
    private NfsLoanRecordService nfsLoanRecordService;
    
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
    	logger.error("开始处理分期借条");
		NowDBOperator nowOperator = NowDBOperatorFactory.getNowDBOperator();
		PreparedStatement updateLoanPsmt = null;
		PreparedStatement updateRepayPsmt = null;
		NfsLoanRecord nfsLoanRecord = new NfsLoanRecord();
		nfsLoanRecord.setRepayType(RepayType.principalAndInterestByMonth);
		nfsLoanRecord.setStatus(NfsLoanRecord.Status.penddingRepay);
		List<NfsLoanRecord> pendingRepayList = nfsLoanRecordService.findList(nfsLoanRecord);
		NfsLoanRecord nfsLoanRecord1 = new NfsLoanRecord();
		nfsLoanRecord1.setRepayType(RepayType.principalAndInterestByMonth);
		nfsLoanRecord1.setStatus(NfsLoanRecord.Status.overdue);
		List<NfsLoanRecord> overdueList = nfsLoanRecordService.findList(nfsLoanRecord1);
		
		List<NfsLoanRecord> repayList = new ArrayList<>();
		if(!Collections3.isEmpty(overdueList)) {
			repayList.addAll(overdueList);
		}
		repayList.addAll(pendingRepayList);
		String updateLoanSql = "UPDATE NFS_LOAN_RECORD SET due_repay_date = ? , `status` = ? WHERE id = ?";
		String updateRepaySql = "UPDATE NFS_LOAN_REPAY_RECORD SET `status` = ? WHERE id = ?";
		
		for (NfsLoanRecord nfsLoanRecord2 : repayList) {
			NfsLoanRepayRecord nfsLoanRepayRecord = new NfsLoanRepayRecord();
			nfsLoanRepayRecord.setLoan(nfsLoanRecord2);
			nfsLoanRepayRecord.setStatus(NfsLoanRepayRecord.Status.done);
			List<NfsLoanRepayRecord> pendingRepayRecordList = nfsLoanRepayRecordService.findPendingRepayList(nfsLoanRepayRecord);
			
			Date dueRepayDate = nfsLoanRecord2.getDueRepayDate();//当前借条还款日期
			Date expectRepayDate = pendingRepayRecordList.get(0).getExpectRepayDate();//当前还款计划还款日期
			String expectDateStr = CalendarUtil.getDate(expectRepayDate);
			String today = CalendarUtil.getDate(new Date());
			// 借条里的应还日期比还款计划应还日期靠后 且逾期
			logger.error("分期借条{}当前还款日期{}错误，应该为{}", nfsLoanRecord2.getId(), dueRepayDate, expectRepayDate);
			nfsLoanRecord2.setDueRepayDate(expectRepayDate);
			try {
				updateLoanPsmt = nowOperator.getPreparedStatement(updateLoanSql);
				updateRepayPsmt = nowOperator.getPreparedStatement(updateRepaySql);

				updateLoanPsmt.setString(1, DateUtils.formatDate(expectRepayDate, "yyyy-MM-dd"));
				if (expectDateStr.compareTo(today) < 0) {
					updateLoanPsmt.setInt(2, 2);
					updateRepayPsmt.setInt(1, 3);
				}else{
					updateLoanPsmt.setInt(2, 0);
					updateRepayPsmt.setInt(1, 0);
				}
				updateLoanPsmt.setLong(3, nfsLoanRecord2.getId());
				updateRepayPsmt.setLong(2, pendingRepayRecordList.get(0).getId());

				updateLoanPsmt.executeUpdate();
				updateRepayPsmt.executeUpdate();

			} catch (SQLException e) {
				logger.error("借条{}分期还款日期更新错误{}", nfsLoanRecord2.getId(), Exceptions.getStackTraceAsString(e));
			} finally {
				if (updateLoanPsmt != null) {
					try {
						updateLoanPsmt.close();
					} catch (SQLException e) {
						logger.error("关闭updateLoanPsmt连接异常{}", Exceptions.getStackTraceAsString(e));
					}
				}
				if (updateRepayPsmt != null) {
					try {
						updateRepayPsmt.close();
					} catch (SQLException e) {
						logger.error("关闭updateRepayPsmt连接异常{}", Exceptions.getStackTraceAsString(e));
					}
				}
			}
    }
    	logger.error("处理分期借条完毕");
	}
    
    
    public static void main(String[] args) {
    	String exDate = "2019-04-24";
    	String today = CalendarUtil.getDate(new Date());
		System.out.println(exDate.compareTo(today));
		
	}
    
}
