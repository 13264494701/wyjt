package com.jxf.report.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.report.dao.ReportLoanDailyDao;
import com.jxf.report.entity.ReportLoanDaily;
import com.jxf.report.service.ReportLoanDailyService;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;

/**
 * 借条统计ServiceImpl
 * @author wo
 * @version 2019-02-28
 */
@Service("reportLoanDailyService")
@Transactional(readOnly = true)
public class ReportLoanDailyServiceImpl extends CrudServiceImpl<ReportLoanDailyDao, ReportLoanDaily> implements ReportLoanDailyService{

	@Autowired
	private ReportLoanDailyDao loanDailyDao;
	
	public ReportLoanDaily get(Long id) {
		return super.get(id);
	}
	
	public List<ReportLoanDaily> findList(ReportLoanDaily reportLoanDaily) {
		return super.findList(reportLoanDaily);
	}
	
	public Page<ReportLoanDaily> findPage(Page<ReportLoanDaily> page, ReportLoanDaily reportLoanDaily) {
		return super.findPage(page, reportLoanDaily);
	}
	
	@Transactional(readOnly = false)
	public void save(ReportLoanDaily reportLoanDaily) {
		super.save(reportLoanDaily);
	}
	
	@Transactional(readOnly = false)
	public void delete(ReportLoanDaily reportLoanDaily) {
		super.delete(reportLoanDaily);
	}

	@Override
	public List<ReportLoanDaily> loanCount(int daysAgo) {

		return loanDailyDao.loanCount(daysAgo);
	}

	@Override
	@Transactional(readOnly = false)
	public int deleteByDate(String date) {
		 return loanDailyDao.deleteByDate(date);
	}
	
}