package com.jxf.report.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.report.dao.ReportPayRefundDailyDao;
import com.jxf.report.entity.ReportPayRefundDaily;
import com.jxf.report.service.ReportPayRefundDailyService;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;

/**
 * 缴费退费统计ServiceImpl
 * @author wo
 * @version 2019-02-28
 */
@Service("reportPayRefundDailyService")
@Transactional(readOnly = true)
public class ReportPayRefundDailyServiceImpl extends CrudServiceImpl<ReportPayRefundDailyDao, ReportPayRefundDaily> implements ReportPayRefundDailyService{

	@Autowired
	private ReportPayRefundDailyDao payRefundDailyDao;
	
	public ReportPayRefundDaily get(Long id) {
		return super.get(id);
	}
	
	public List<ReportPayRefundDaily> findList(ReportPayRefundDaily reportPayRefundDaily) {
		return super.findList(reportPayRefundDaily);
	}
	
	public Page<ReportPayRefundDaily> findPage(Page<ReportPayRefundDaily> page, ReportPayRefundDaily reportPayRefundDaily) {
		return super.findPage(page, reportPayRefundDaily);
	}
	
	@Transactional(readOnly = false)
	public void save(ReportPayRefundDaily reportPayRefundDaily) {
		super.save(reportPayRefundDaily);
	}
	
	@Transactional(readOnly = false)
	public void delete(ReportPayRefundDaily reportPayRefundDaily) {
		super.delete(reportPayRefundDaily);
	}

	@Override
	public ReportPayRefundDaily payCount(int daysAgo) {

		return payRefundDailyDao.payCount(daysAgo);
	}
	
	@Override
	public ReportPayRefundDaily refundCount(int daysAgo) {

		return payRefundDailyDao.refundCount(daysAgo);
	}

	@Override
	@Transactional(readOnly = false)
	public int deleteByDate(String date) {
		 return payRefundDailyDao.deleteByDate(date);
	}
	
}