package com.jxf.report.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.report.dao.ReportLoanAmtDailyDao;
import com.jxf.report.entity.ReportLoanAmtDaily;
import com.jxf.report.service.ReportLoanAmtDailyService;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;

/**
 * 借条金额统计ServiceImpl
 * @author wo
 * @version 2019-07-10
 */
@Service("reportLoanAmtDailyService")
@Transactional(readOnly = true)
public class ReportLoanAmtDailyServiceImpl extends CrudServiceImpl<ReportLoanAmtDailyDao, ReportLoanAmtDaily> implements ReportLoanAmtDailyService{

	@Autowired
	private ReportLoanAmtDailyDao loanAmtDailyDao;
	
	public ReportLoanAmtDaily get(Long id) {
		return super.get(id);
	}
	
	public List<ReportLoanAmtDaily> findList(ReportLoanAmtDaily reportLoanAmtDaily) {
		return super.findList(reportLoanAmtDaily);
	}
	
	public Page<ReportLoanAmtDaily> findPage(Page<ReportLoanAmtDaily> page, ReportLoanAmtDaily reportLoanAmtDaily) {
		return super.findPage(page, reportLoanAmtDaily);
	}
	
	@Transactional(readOnly = false)
	public void save(ReportLoanAmtDaily reportLoanAmtDaily) {
		super.save(reportLoanAmtDaily);
	}
	
	@Transactional(readOnly = false)
	public void delete(ReportLoanAmtDaily reportLoanAmtDaily) {
		super.delete(reportLoanAmtDaily);
	}

	@Override
	public ReportLoanAmtDaily loanAmtCount(int daysAgo) {

		return loanAmtDailyDao.loanAmtCount(daysAgo);
	}

	@Override
	@Transactional(readOnly = false)
	public int deleteByDate(String date) {
		 return loanAmtDailyDao.deleteByDate(date);
	}
	
}