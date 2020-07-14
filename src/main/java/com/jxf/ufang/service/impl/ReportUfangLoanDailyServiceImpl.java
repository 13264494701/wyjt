package com.jxf.ufang.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.loan.entity.NfsLoanOperatingRecord;
import com.jxf.loan.entity.NfsLoanRecord;
import com.jxf.loan.service.NfsLoanApplyDetailService;
import com.jxf.loan.service.NfsLoanOperatingRecordService;
import com.jxf.loan.service.NfsLoanRecordService;
import com.jxf.loan.service.NfsLoanRepayRecordService;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
import com.jxf.svc.utils.StringUtils;
import com.jxf.ufang.dao.ReportUfangLoanDailyDao;
import com.jxf.ufang.entity.ReportUfangLoanDaily;
import com.jxf.ufang.entity.UfangBrn;
import com.jxf.ufang.entity.UfangUser;
import com.jxf.ufang.service.ReportUfangLoanDailyService;
import com.jxf.ufang.service.UfangBrnService;
import com.jxf.ufang.service.UfangUserService;
/**
 * 借条统计ServiceImpl
 * @author suHuimin
 * @version 2019-03-25
 */
@Service("reportUfangLoanDailyService")
@Transactional(readOnly = true)
public class ReportUfangLoanDailyServiceImpl extends CrudServiceImpl<ReportUfangLoanDailyDao, ReportUfangLoanDaily> implements ReportUfangLoanDailyService{
	private static Logger  log = LoggerFactory.getLogger(ReportUfangLoanDailyServiceImpl.class);
	
	@Autowired
	private UfangUserService ufangUserService;
	@Autowired
	private NfsLoanRecordService loanRecordService;
	@Autowired
	private NfsLoanRepayRecordService loanRepayRecordService;
	@Autowired
	private NfsLoanApplyDetailService loanApplyDetailService;
	@Autowired
	private UfangBrnService ufangBrnService;
	@Autowired
	private NfsLoanOperatingRecordService loanOperatingRecordService;
	
    @Override
	public ReportUfangLoanDaily get(Long id) {
		return super.get(id);
	}
	
	@Override
	public List<ReportUfangLoanDaily> findList(ReportUfangLoanDaily reportUfangLoanDaily) {
		return super.findList(reportUfangLoanDaily);
	}
	
	@Override
	public Page<ReportUfangLoanDaily> findPage(Page<ReportUfangLoanDaily> page, ReportUfangLoanDaily reportUfangLoanDaily) {
		return super.findPage(page, reportUfangLoanDaily);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void save(ReportUfangLoanDaily reportUfangLoanDaily) {
		super.save(reportUfangLoanDaily);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void delete(ReportUfangLoanDaily reportUfangLoanDaily) {
		super.delete(reportUfangLoanDaily);
	}

	@Override
	@Transactional(readOnly = false)
	public boolean loanStatistics(Long brnId, Date beginTime, Date endTime) {
    	ReportUfangLoanDaily ufangLoanDaily = new ReportUfangLoanDaily();
    	UfangBrn ufangBrn = ufangBrnService.get(brnId);
    	ufangLoanDaily.setUfangBrn(ufangBrn);
    	ufangLoanDaily.setDate(beginTime);
    	//先查出所有的绑定账号的ufang会员的无忧借条id
    	UfangUser ufangUser = new UfangUser();
    	ufangUser.setBrn(ufangBrn);
    	ufangUser.setBindStatus(UfangUser.BindStatus.binded);
    	List<UfangUser> ufangUsers = ufangUserService.findUser(ufangUser);
    	List<Long> ids = new ArrayList<Long>(ufangUsers.size());
    	if(ids.size() == 0) {
    		ufangLoanDaily.setCompletedLoanQuantity(0L);
    		ufangLoanDaily.setRepayedLoanAmount(BigDecimal.ZERO);
    		ufangLoanDaily.setCreatedLoanQuantity(0L);
    		ufangLoanDaily.setCreatedLoanAmount(BigDecimal.ZERO);
    		ufangLoanDaily.setLendingLoanAmount(BigDecimal.ZERO);
    		ufangLoanDaily.setLendingLoanQuantity(0L);
    		ufangLoanDaily.setOverdueLoanQuantity(0L);
    		ufangLoanDaily.setOverdueLoanAmount(BigDecimal.ZERO);
    		save(ufangLoanDaily);
    		return true;
    	}
    	for (UfangUser ufangUser2 : ufangUsers) {
    		ids.add(ufangUser2.getMember().getId());
		}
		// 结清个数
		String timeType = "2";
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ids", ids);
		paramMap.put("beginTime", beginTime);
		paramMap.put("endTime", endTime);
		paramMap.put("timType", timeType);
		paramMap.put("status", NfsLoanRecord.Status.repayed.ordinal());
		List<Long> completedLoanIds = loanRecordService.findUfangUserLoanCount(paramMap);
		ufangLoanDaily.setCompletedLoanQuantity(completedLoanIds == null ? 0L : completedLoanIds.size());
		//还款金额
		BigDecimal repayedAmount = BigDecimal.ZERO;
		//拿到机构员工名下结清时间在时间段或者没结清的借条
		Map<String, Object> mapForRepayedAmount = new HashMap<String,Object>();
		mapForRepayedAmount.put("ids", ids);
		mapForRepayedAmount.put("beginTime", beginTime);
		mapForRepayedAmount.put("endTime", endTime);
		List<NfsLoanRecord> ufangUserLoanList = new ArrayList<NfsLoanRecord>();
		ufangUserLoanList = loanRecordService.getUfangUserLoanList(mapForRepayedAmount);
		List<NfsLoanOperatingRecord> operatingRecordList = new ArrayList<NfsLoanOperatingRecord>();
		BigDecimal operateAmount = BigDecimal.ZERO;//本次操作还款金额
		BigDecimal preAmount = BigDecimal.ZERO;//操作前本金
		BigDecimal postAmount = BigDecimal.ZERO;//操作后本金
		BigDecimal preInterest = BigDecimal.ZERO;//操作前利息
		BigDecimal postInterest = BigDecimal.ZERO;//操作后利息
		BigDecimal preOverdueInterest = BigDecimal.ZERO;//操作前逾期利息
		for (NfsLoanRecord nfsLoanRecord : ufangUserLoanList) {
			//遍历借条 取出当前查询时间段内的的操作记录
			NfsLoanOperatingRecord nfsLoanOperatingRecord = new NfsLoanOperatingRecord();
			nfsLoanOperatingRecord.setOldRecord(nfsLoanRecord);
			nfsLoanOperatingRecord.setBeginTime(beginTime);
			nfsLoanOperatingRecord.setEndTime(endTime);
			operatingRecordList = loanOperatingRecordService.findList(nfsLoanOperatingRecord);
			//遍历操作记录如果是还款动作就记录还款金额 
			for (NfsLoanOperatingRecord operatingRecord : operatingRecordList) {
				NfsLoanOperatingRecord.Type operateType = operatingRecord.getType();
				if(operateType.equals(NfsLoanOperatingRecord.Type.delay) 
						|| operateType.equals(NfsLoanOperatingRecord.Type.create)) {
					continue;
				}
				preAmount = operatingRecord.getOldRecord().getAmount();//操作前本金
				postAmount = operatingRecord.getNowRecord().getAmount();//操作后本金
				preInterest = operatingRecord.getOldRecord().getInterest();//操作前利息
				postInterest = operatingRecord.getNowRecord().getInterest();//操作后利息
				preOverdueInterest = operatingRecord.getOldRecord().getOverdueInterest();//操作前逾期利息
				if(operateType.equals(NfsLoanOperatingRecord.Type.partial) || operateType.equals(NfsLoanOperatingRecord.Type.partialAndDelay) 
						|| operateType.equals(NfsLoanOperatingRecord.Type.principalAndInterestByMonth)) {
					operateAmount = operatingRecord.getRepaymentAmount();
					if(operateAmount == null) {
						//没有记录数据
						log.error("借条操作{}没有记录还款金额",operatingRecord.getId());
					}else {
						//操作还款金额 = 操作前本金-操作后本金+操作前利息-操作后利息+操作前逾期利息-操作后逾期利息
						operateAmount = preAmount.subtract(postAmount).add(preInterest).subtract(postInterest).add(preOverdueInterest);
					}
					repayedAmount = repayedAmount.add(operateAmount);
				}else {
					//线下还款，全额还款
					repayedAmount = preAmount.subtract(postAmount).add(preInterest).subtract(postInterest).add(preOverdueInterest);
				}
			}
		}
		ufangLoanDaily.setRepayedLoanAmount(repayedAmount);
		// 借出个数
		timeType = "3";
		paramMap.put("timType", timeType);
		paramMap.put("status", null);
		List<Long> createLoanIds = loanRecordService.findUfangUserLoanCount(paramMap);
		ufangLoanDaily.setCreatedLoanQuantity(createLoanIds == null ? 0L : createLoanIds.size());
		if(createLoanIds.size() != 0) {
			// 借出金额 查询还款计划
			String createloanIdStr = StringUtils.generateStringFromList(createLoanIds);
			BigDecimal loanAmount = loanRepayRecordService.sumLoanAmount(createloanIdStr);
			ufangLoanDaily.setCreatedLoanAmount(loanAmount);
		}else {
			ufangLoanDaily.setCreatedLoanAmount(BigDecimal.ZERO);
		}

		// 逾期未还总数
		timeType = "1";
		paramMap.put("timType", timeType);
		paramMap.put("status", NfsLoanRecord.Status.overdue.ordinal());
		List<Long> overdueLoanIds = loanRecordService.findUfangUserLoanCount(paramMap);
		ufangLoanDaily.setOverdueLoanQuantity(overdueLoanIds == null ? 0L : overdueLoanIds.size());
		// 逾期未还金额
		BigDecimal overdueAmount = BigDecimal.ZERO;
		if (overdueLoanIds.size() != 0) {
			overdueAmount = loanRecordService.sumOverdueLoanAmount(overdueLoanIds);
		}
		ufangLoanDaily.setOverdueLoanAmount(overdueAmount);
		// 借款人待收总数 主动放款和找好友借款
		Map<String, Long> resultMap = loanApplyDetailService.getLendingLoanAndAmount(paramMap);
		Long lendingLoanCount = resultMap.get("count");
		Long lendingLoanAmount = resultMap.get("totalAmount");
		ufangLoanDaily.setLendingLoanQuantity(lendingLoanCount);
		ufangLoanDaily.setLendingLoanAmount(new BigDecimal(lendingLoanAmount == null ? 0 : lendingLoanAmount));
		save(ufangLoanDaily);
		return true;
	}
	
}