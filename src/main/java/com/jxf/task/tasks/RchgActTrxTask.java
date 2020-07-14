package com.jxf.task.tasks;




import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import com.jxf.mem.entity.MemberActTrx;
import com.jxf.mem.service.MemberActTrxService;
import com.jxf.mem.service.MemberService;
import com.jxf.nfs.constant.TrxRuleConstant;
import com.jxf.nfs.dao.NfsRchgActTrxDao;
import com.jxf.nfs.entity.NfsRchgActTrx;
import com.jxf.nfs.entity.NfsRchgRecord;
import com.jxf.nfs.service.NfsRchgRecordService;
import com.jxf.svc.utils.CalendarUtil;
import com.jxf.svc.utils.DateUtils;

/**
 * 充值账户流水
 *
 * @author Administrator
 */
@DisallowConcurrentExecution
public class RchgActTrxTask implements Job {


	@Autowired
	private NfsRchgRecordService rchgRecordService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private MemberActTrxService memberActTrxService;
	@Autowired
	private NfsRchgActTrxDao nfsRchgActTrxDao;


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

		Date startTime = CalendarUtil.addDay(new Date(), -1);
		Date endTime = CalendarUtil.addDay(new Date(), -1);
		
		if (startTime != null) {
			Calendar calendar = DateUtils.toCalendar(startTime);
			calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMinimum(Calendar.HOUR_OF_DAY));
			calendar.set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE));
			calendar.set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND));
			startTime = calendar.getTime();
		}
		if (endTime != null) {
			Calendar calendar = DateUtils.toCalendar(endTime);
			calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMaximum(Calendar.HOUR_OF_DAY));
			calendar.set(Calendar.MINUTE, calendar.getActualMaximum(Calendar.MINUTE));
			calendar.set(Calendar.SECOND, calendar.getActualMaximum(Calendar.SECOND));
			endTime = calendar.getTime();
		}	
		
		
		MemberActTrx memberActTrx = new MemberActTrx();
		memberActTrx.setTrxCode(TrxRuleConstant.MEMBER_RECHARGE);
		memberActTrx.setBeginTime(startTime);
		memberActTrx.setEndTime(endTime);
		List<MemberActTrx> actTrxList = memberActTrxService.findList(memberActTrx);
		for(MemberActTrx trx:actTrxList) {
			NfsRchgRecord rchgRecord = rchgRecordService.get(trx.getOrgId());	
			NfsRchgActTrx nfsRchgActTrx = new NfsRchgActTrx();
			nfsRchgActTrx.setId(trx.getId());
			nfsRchgActTrx.setMember(memberService.get(trx.getMember()));
			nfsRchgActTrx.setTrxAmt(trx.getTrxAmt());
			nfsRchgActTrx.setCurBal(trx.getCurBal());
			nfsRchgActTrx.setType(NfsRchgActTrx.Type.fuiou);
			nfsRchgActTrx.setRchgId(Long.valueOf(rchgRecord.getPaymentNo()));
			nfsRchgActTrx.setThirdPaymentNo(rchgRecord.getThirdOrderNo());
			nfsRchgActTrx.setCardNo(rchgRecord.getCardNo());
			nfsRchgActTrx.setBankName(rchgRecord.getBankName());
			nfsRchgActTrx.setStatus(NfsRchgActTrx.Status.success);
			nfsRchgActTrx.setCreateBy(trx.getCreateBy());
			nfsRchgActTrx.setCreateTime(trx.getCreateTime());
			nfsRchgActTrx.setUpdateBy(trx.getUpdateBy());
			nfsRchgActTrx.setUpdateTime(trx.getUpdateTime());
			nfsRchgActTrxDao.insert(nfsRchgActTrx);
		}
		
		memberActTrx.setTrxCode(TrxRuleConstant.MEMBER_BAL_ADD);
		actTrxList = memberActTrxService.findList(memberActTrx);
		for(MemberActTrx trx:actTrxList) {

			NfsRchgActTrx nfsRchgActTrx = new NfsRchgActTrx();
			nfsRchgActTrx.setId(trx.getId());
			nfsRchgActTrx.setMember(memberService.get(trx.getMember()));
			nfsRchgActTrx.setTrxAmt(trx.getTrxAmt());
			nfsRchgActTrx.setCurBal(trx.getCurBal());
			nfsRchgActTrx.setType(NfsRchgActTrx.Type.other);
			nfsRchgActTrx.setStatus(NfsRchgActTrx.Status.success);
			nfsRchgActTrx.setCreateBy(trx.getCreateBy());
			nfsRchgActTrx.setCreateTime(trx.getCreateTime());
			nfsRchgActTrx.setUpdateBy(trx.getUpdateBy());
			nfsRchgActTrx.setUpdateTime(trx.getUpdateTime());
			nfsRchgActTrxDao.insert(nfsRchgActTrx);
		}

    }

}