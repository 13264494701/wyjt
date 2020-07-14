package com.jxf.report.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.report.dao.ReportWdrlDailyDao;
import com.jxf.report.entity.ReportWdrlDaily;
import com.jxf.report.service.ReportWdrlDailyService;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;

/**
 * 充值统计ServiceImpl
 * @author wo
 * @version 2019-03-25
 */
@Service("reportWdrlDailyService")
@Transactional(readOnly = true)
public class ReportWdrlDailyServiceImpl extends CrudServiceImpl<ReportWdrlDailyDao, ReportWdrlDaily> implements ReportWdrlDailyService{

	@Autowired
	private ReportWdrlDailyDao wdrlDailyDao;
	
    @Override
	public ReportWdrlDaily get(Long id) {
		return super.get(id);
	}
	
	@Override
	public List<ReportWdrlDaily> findList(ReportWdrlDaily reportWdrlDaily) {
		return super.findList(reportWdrlDaily);
	}
	
	@Override
	public Page<ReportWdrlDaily> findPage(Page<ReportWdrlDaily> page, ReportWdrlDaily reportWdrlDaily) {
		return super.findPage(page, reportWdrlDaily);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void save(ReportWdrlDaily reportWdrlDaily) {
		super.save(reportWdrlDaily);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void delete(ReportWdrlDaily reportWdrlDaily) {
		super.delete(reportWdrlDaily);
	}

	@Override
	public ReportWdrlDaily wdrlCount(int daysAgo) {

		return wdrlDailyDao.wdrlCount(daysAgo);
	}

	@Override
	@Transactional(readOnly = false)
	public int deleteByDate(String date) {

		return wdrlDailyDao.deleteByDate(date);
	}
	
}