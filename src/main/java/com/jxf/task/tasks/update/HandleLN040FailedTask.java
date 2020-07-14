package com.jxf.task.tasks.update;

import com.jxf.loan.entity.NfsLoanApply;
import com.jxf.loan.entity.NfsLoanApplyDetail;
import com.jxf.loan.service.NfsLoanApplyDetailService;
import com.jxf.loan.service.NfsLoanApplyService;
import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberAct;
import com.jxf.mem.entity.MemberActTrx;
import com.jxf.mem.service.MemberActService;
import com.jxf.mem.service.MemberActTrxService;
import com.jxf.mem.service.MemberMessageService;
import com.jxf.mem.service.MemberService;
import com.jxf.nfs.constant.ActSubConstant;
import com.jxf.nfs.constant.TrxRuleConstant;
import com.jxf.svc.utils.DateUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *   处理LN040交易失败任务
 *
 * @author suHuimin
 */
@DisallowConcurrentExecution
public class HandleLN040FailedTask implements Job {
	private static final Logger logger = LoggerFactory.getLogger(HandleLN040FailedTask.class);
    @Autowired
    private MemberActTrxService memberActTrxService;
    @Autowired
    private MemberActService memberActService;
    @Autowired
    private NfsLoanApplyService applyService;
    @Autowired
    private NfsLoanApplyDetailService detailService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberMessageService memberMessageService;
    
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.error("开始处理没生成借条Detail错误");
    	handleLN040Failed();
    	logger.error("处理没生成借条Detail错误完毕");
	}
    
   private void handleLN040Failed() {
	   //查询可用余额账户对应的流水
	   MemberActTrx param1 = new MemberActTrx();
	   param1.setTrxCode(TrxRuleConstant.FROZEN_LOANER_FUNDS);
	   param1.setSubNo(ActSubConstant.MEMBER_AVL_BAL);
	   Date startDate  = null;
	   try {
		   startDate  = DateUtils.parse("2019-02-15 11:30:43");
		} catch (ParseException e) {
			e.printStackTrace();
		}
	   param1.setBeginTime(startDate);
	   List<MemberActTrx> avlActTrxs = memberActTrxService.findLN040FailedList(param1);
	   //更新可用余额账户余额
	   for (MemberActTrx memberActTrx : avlActTrxs) {
		   //扣除的金额
		   BigDecimal amount = memberActTrx.getTrxAmt();
		   //用户
		   Member member = memberActTrx.getMember();
		   member = memberService.get(member);
		   MemberAct avlAct = memberActService.getMemberAct(member, ActSubConstant.MEMBER_AVL_BAL);
		   MemberAct freezenAct = memberActService.getMemberAct(member, ActSubConstant.MEMBER_FREEZEN_BAL);
		   logger.error("处理没生成detail借条：给会员" + member.getId() + "可用余额账户加款amount = " + amount);
		   memberActService.updateAct(avlAct, amount);
		   memberActService.updateAct(freezenAct, amount.negate());
		   logger.error("处理没生成detail借条：给会员" + member.getId() + "冻结账户扣款amount = " + amount.negate());
		   //删除对应的流水
		   memberActTrxService.delete(memberActTrx);
		   logger.error("处理没生成detail借条：删除会员可用余额流水id： " + memberActTrx.getId());
	   }
	   //查询冻结账户对应的流水
	   MemberActTrx param2 = new MemberActTrx();
	   param2.setTrxCode(TrxRuleConstant.FROZEN_LOANER_FUNDS);
	   param2.setSubNo(ActSubConstant.MEMBER_FREEZEN_BAL);
	   param2.setBeginTime(startDate);
	   List<MemberActTrx> freezenActTrxs = memberActTrxService.findLN040FailedList(param2);
	   //删除冻结账户的流水
	   for (MemberActTrx memberActTrx : freezenActTrxs) {
		   memberActTrxService.delete(memberActTrx);
		   logger.error("处理没生成detail借条：删除会员冻结账户流水id： " + memberActTrx.getId());
	   }
	   //查询对应的放款申请
	   NfsLoanApply param3 = new NfsLoanApply();
	   Date beginDate = null;
	   Date endDate = null;
		try {
			beginDate = DateUtils.parse("2019-02-15 11:34:00");
			endDate = DateUtils.parse("2019-02-15 13:26:59");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   param3.setBeginDate(beginDate);
	   param3.setEndDate(endDate);
	   List<NfsLoanApply> applies = applyService.findNoDetailApply(param3);
	   //删除对应的放款申请
	   for (NfsLoanApply nfsLoanApply : applies) {
		   NfsLoanApplyDetail detail = new NfsLoanApplyDetail();
		   detail.setApply(nfsLoanApply);
		   List<NfsLoanApplyDetail>  applyDetails = detailService.findList(detail);
		   if(applyDetails.size() > 0) {
			   continue;
		   }
		   logger.error("处理没生成detail借条：删除借款申请apply的id： " + nfsLoanApply.getId());
		   applyService.delete(nfsLoanApply);
	   }
	   
	   //删除消息
	   int updateLines = memberMessageService.UpdateNoOrgId();
	   logger.error("删除LN040错误消息 " + updateLines + " 条 " );
   }
}