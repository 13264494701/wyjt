package com.jxf.report.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.report.dao.ReportTransferDailyDao;
import com.jxf.report.entity.ReportTransferDaily;
import com.jxf.report.service.ReportTransferDailyService;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;

/**
 * 转账统计ServiceImpl
 * @author wo
 * @version 2019-03-25
 */
@Service("reportTransferDailyService")
@Transactional(readOnly = true)
public class ReportTransferDailyServiceImpl extends CrudServiceImpl<ReportTransferDailyDao, ReportTransferDaily> implements ReportTransferDailyService{

	@Autowired
	private ReportTransferDailyDao transferDailyDao;
	
    @Override
	public ReportTransferDaily get(Long id) {
		return super.get(id);
	}
	
	@Override
	public List<ReportTransferDaily> findList(ReportTransferDaily reportTransferDaily) {
		return super.findList(reportTransferDaily);
	}
	
	@Override
	public Page<ReportTransferDaily> findPage(Page<ReportTransferDaily> page, ReportTransferDaily reportTransferDaily) {
		return super.findPage(page, reportTransferDaily);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void save(ReportTransferDaily reportTransferDaily) {
		super.save(reportTransferDaily);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void delete(ReportTransferDaily reportTransferDaily) {
		super.delete(reportTransferDaily);
	}

	@Override
	public ReportTransferDaily transferCount(int daysAgo) {

		return transferDailyDao.transferCount(daysAgo);
	}

	@Override
	@Transactional(readOnly = false)
	public int deleteByDate(String date) {

		return transferDailyDao.deleteByDate(date);
	}
	
}