package com.jxf.report.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.report.dao.ReportLoaneeDataDailyDao;
import com.jxf.report.entity.ReportLoaneeDataDaily;
import com.jxf.report.service.ReportLoaneeDataDailyService;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;

/**
 * 流量统计ServiceImpl
 * @author wo
 * @version 2019-02-28
 */
@Service("reportLoaneeDataDailyService")
@Transactional(readOnly = true)
public class ReportLoaneeDataDailyServiceImpl extends CrudServiceImpl<ReportLoaneeDataDailyDao, ReportLoaneeDataDaily> implements ReportLoaneeDataDailyService{

	@Autowired
	private ReportLoaneeDataDailyDao loaneeDataDailyDao;
	
	public ReportLoaneeDataDaily get(Long id) {
		return super.get(id);
	}
	
	public List<ReportLoaneeDataDaily> findList(ReportLoaneeDataDaily reportLoaneeDataDaily) {
		return super.findList(reportLoaneeDataDaily);
	}
	
	public Page<ReportLoaneeDataDaily> findPage(Page<ReportLoaneeDataDaily> page, ReportLoaneeDataDaily reportLoaneeDataDaily) {
		return super.findPage(page, reportLoaneeDataDaily);
	}
	
	@Transactional(readOnly = false)
	public void save(ReportLoaneeDataDaily reportLoaneeDataDaily) {
		super.save(reportLoaneeDataDaily);
	}
	
	@Transactional(readOnly = false)
	public void delete(ReportLoaneeDataDaily reportLoaneeDataDaily) {
		super.delete(reportLoaneeDataDaily);
	}

	@Override
	public ReportLoaneeDataDaily dataCount(int daysAgo) {

		return loaneeDataDailyDao.dataCount(daysAgo);
	}

	@Override
	@Transactional(readOnly = false)
	public int deleteByDate(String date) {
	
		return loaneeDataDailyDao.deleteByDate(date);
	}
	
}