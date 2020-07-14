package com.jxf.report.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
import com.jxf.report.entity.ReportMemberDaily;
import com.jxf.report.dao.ReportMemberDailyDao;
import com.jxf.report.service.ReportMemberDailyService;

/**
 * 用户统计ServiceImpl
 *
 * @author Administrator
 * @version 2018-09-06
 */
@Service("reportMemberDailyService")
@Transactional(readOnly = true)
public class ReportMemberDailyServiceImpl extends CrudServiceImpl<ReportMemberDailyDao, ReportMemberDaily> implements ReportMemberDailyService {

    @Autowired
    private ReportMemberDailyDao reportMemberDailyDao;

    public ReportMemberDaily get(Long id) {
        return super.get(id);
    }

    public List<ReportMemberDaily> findList(ReportMemberDaily reportMemberDaily) {
        return super.findList(reportMemberDaily);
    }

    public Page<ReportMemberDaily> findPage(Page<ReportMemberDaily> page, ReportMemberDaily reportMemberDaily) {
        return super.findPage(page, reportMemberDaily);
    }

    @Transactional(readOnly = false)
    public void save(ReportMemberDaily reportMemberDaily) {
        super.save(reportMemberDaily);
    }

    @Transactional(readOnly = false)
    public void delete(ReportMemberDaily reportMemberDaily) {
        super.delete(reportMemberDaily);
    }

    @Override
    public ReportMemberDaily memberCount(int daysAgo) {
        return reportMemberDailyDao.memberCount(daysAgo);
    }

    @Override
    @Transactional(readOnly = false)
    public int deleteByDate(String date) {
        return reportMemberDailyDao.deleteByDate(date);
    }
}