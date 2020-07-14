package com.jxf.report.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.mem.dao.MemberActDao;
import com.jxf.mem.entity.MemberAct;
import com.jxf.nfs.constant.ActSubConstant;
import com.jxf.report.dao.ReportMemberActDailyDao;
import com.jxf.report.entity.ReportMemberActDaily;
import com.jxf.report.service.ReportMemberActDailyService;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
import com.jxf.svc.utils.StringUtils;

/**
 * 账户统计ServiceImpl
 * @author wo
 * @version 2019-02-22
 */
@Service("reportMemberActDailyService")
@Transactional(readOnly = true)
public class ReportMemberActDailyServiceImpl extends CrudServiceImpl<ReportMemberActDailyDao, ReportMemberActDaily> implements ReportMemberActDailyService{

	@Autowired
	private ReportMemberActDailyDao memberActDailyDao;
	@Autowired
	private MemberActDao memberActDao;
	
	public ReportMemberActDaily get(Long id) {
		return super.get(id);
	}
	
	public List<ReportMemberActDaily> findList(ReportMemberActDaily reportMemberActDaily) {
		return super.findList(reportMemberActDaily);
	}
	
	public Page<ReportMemberActDaily> findPage(Page<ReportMemberActDaily> page, ReportMemberActDaily reportMemberActDaily) {
		return super.findPage(page, reportMemberActDaily);
	}
	
	@Transactional(readOnly = false)
	public void save(ReportMemberActDaily reportMemberActDaily) {
		super.save(reportMemberActDaily);
	}
	
	@Transactional(readOnly = false)
	public void delete(ReportMemberActDaily reportMemberActDaily) {
		super.delete(reportMemberActDaily);
	}
	
    @Override
    public ReportMemberActDaily sumMemberAct(ReportMemberActDaily memberActDaily) {
    	
    	
    	List<MemberAct> actList = memberActDao.sumMemberAct();
		for (MemberAct act : actList) {
            if(StringUtils.equals(act.getSubNo(), ActSubConstant.MEMBER_AVL_BAL)) {
            	memberActDaily.setAvlBal(act.getCurBal());

			}else if(StringUtils.equals(act.getSubNo(), ActSubConstant.MEMBER_LOAN_BAL)) {
				memberActDaily.setLoanBal(act.getCurBal());
			}else if(StringUtils.equals(act.getSubNo(), ActSubConstant.MEMBER_FREEZEN_BAL)) {
				memberActDaily.setFreezenBal(act.getCurBal());
			}else if(StringUtils.equals(act.getSubNo(), ActSubConstant.MEMBER_REDBAG_BAL)) {
				memberActDaily.setRedbagBal(act.getCurBal());

			}else if(StringUtils.equals(act.getSubNo(), ActSubConstant.MEMBER_PENDING_REPAYMENT)){//总待还
				memberActDaily.setPendingRepayment(act.getCurBal());
			}else if(StringUtils.equals(act.getSubNo(), ActSubConstant.MEMBER_PENDING_RECEIVE)){//总待收
				memberActDaily.setPendingReceiver(act.getCurBal());
			}
		}
        return memberActDaily;
    }

    @Override
    @Transactional(readOnly = false)
    public int deleteByDate(String date) {
        return memberActDailyDao.deleteByDate(date);
    }
}