package com.jxf.report.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.report.dao.ReportLoanApplyDailyDao;
import com.jxf.report.entity.ReportLoanApplyDaily;
import com.jxf.report.service.ReportLoanApplyDailyService;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
/**
 * 申请统计ServiceImpl
 * @author XIAORONGDIAN
 * @version 2019-04-04
 */
@Service("reportLoanApplyDailyService")
@Transactional(readOnly = true)
public class ReportLoanApplyDailyServiceImpl extends CrudServiceImpl<ReportLoanApplyDailyDao, ReportLoanApplyDaily> implements ReportLoanApplyDailyService{

	@Autowired
    private ReportLoanApplyDailyDao reportLoanApplyDailyDao;
	
    @Override
	public ReportLoanApplyDaily get(Long id) {
		return super.get(id);
	}
	
	@Override
	public List<ReportLoanApplyDaily> findList(ReportLoanApplyDaily reportLoanApplyDaily) {
		return super.findList(reportLoanApplyDaily);
	}
	
	@Override
	public Page<ReportLoanApplyDaily> findPage(Page<ReportLoanApplyDaily> page, ReportLoanApplyDaily reportLoanApplyDaily) {
		return super.findPage(page, reportLoanApplyDaily);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void save(ReportLoanApplyDaily reportLoanApplyDaily) {
		super.save(reportLoanApplyDaily);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void delete(ReportLoanApplyDaily reportLoanApplyDaily) {
		super.delete(reportLoanApplyDaily);
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteByDate(String date) {
		reportLoanApplyDailyDao.deleteByDate(date);
		
	}

	@Override
	public ReportLoanApplyDaily applyCount(int daysAgo) {
		return reportLoanApplyDailyDao.applyCount(daysAgo);
	}
	
}