package com.jxf.report.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.report.dao.ReportRchgDailyDao;
import com.jxf.report.entity.ReportRchgDaily;
import com.jxf.report.service.ReportRchgDailyService;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;

/**
 * 充值统计ServiceImpl
 * @author wo
 * @version 2019-03-25
 */
@Service("reportRchgDailyService")
@Transactional(readOnly = true)
public class ReportRchgDailyServiceImpl extends CrudServiceImpl<ReportRchgDailyDao, ReportRchgDaily> implements ReportRchgDailyService{

	@Autowired
	private ReportRchgDailyDao rchgDailyDao;
	
    @Override
	public ReportRchgDaily get(Long id) {
		return super.get(id);
	}
	
	@Override
	public List<ReportRchgDaily> findList(ReportRchgDaily reportRchgDaily) {
		return super.findList(reportRchgDaily);
	}
	
	@Override
	public Page<ReportRchgDaily> findPage(Page<ReportRchgDaily> page, ReportRchgDaily reportRchgDaily) {
		return super.findPage(page, reportRchgDaily);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void save(ReportRchgDaily reportRchgDaily) {
		super.save(reportRchgDaily);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void delete(ReportRchgDaily reportRchgDaily) {
		super.delete(reportRchgDaily);
	}

	@Override
	public ReportRchgDaily rchgCount(int daysAgo) {

		return rchgDailyDao.rchgCount(daysAgo);
	}

	@Override
	@Transactional(readOnly = false)
	public int deleteByDate(String date) {

		return rchgDailyDao.deleteByDate(date);
	}
	
}