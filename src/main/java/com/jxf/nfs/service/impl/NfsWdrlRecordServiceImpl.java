package com.jxf.nfs.service.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.check.service.NfsCheckRecordService;
import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberActTrx;
import com.jxf.mem.entity.MemberCard;
import com.jxf.mem.service.MemberActService;
import com.jxf.mem.service.MemberActTrxService;
import com.jxf.mem.service.MemberService;
import com.jxf.mms.msg.SendSmsMsgService;
import com.jxf.nfs.constant.ActSubConstant;
import com.jxf.nfs.constant.TrxRuleConstant;
import com.jxf.nfs.dao.NfsWdrlRecordDao;
import com.jxf.nfs.entity.NfsWdrlRecord;
import com.jxf.nfs.entity.NfsWdrlRecord.Status;
import com.jxf.nfs.entity.NfsWdrlRecord.Type;
import com.jxf.nfs.service.NfsActService;
import com.jxf.nfs.service.NfsWdrlRecordService;
import com.jxf.svc.config.Constant;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.Exceptions;
import com.jxf.svc.utils.StringUtils;
/**
 * 提现记录ServiceImpl
 * @author gaobo
 * @version 2018-10-23
 */
@Service("nfsWdrlRecordService")
@Transactional(readOnly = false)
public class NfsWdrlRecordServiceImpl extends CrudServiceImpl<NfsWdrlRecordDao, NfsWdrlRecord> implements NfsWdrlRecordService{
	
	@Autowired
	private NfsWdrlRecordDao wdrlRecordDao;
	@Autowired
	private MemberActService memberActService;
	@Autowired
	private NfsActService actService;
	@Autowired
	private SendSmsMsgService sendSmsMsgService;
	@Autowired
	private MemberActTrxService actTrxService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private NfsCheckRecordService checkRecordService;
	
	public NfsWdrlRecord get(Long id) {
		return super.get(id);
	}
	
	public List<NfsWdrlRecord> findList(NfsWdrlRecord nfsWdrlRecord) {
		return super.findList(nfsWdrlRecord);
	}
	
	public Page<NfsWdrlRecord> findPage(Page<NfsWdrlRecord> page, NfsWdrlRecord nfsWdrlRecord) {
		return super.findPage(page, nfsWdrlRecord);
	}
	
	@Transactional(readOnly = false)
	public void save(NfsWdrlRecord wdrlRecord) {
		if (wdrlRecord.getIsNewRecord()) {
			wdrlRecord.preInsert();
			wdrlRecordDao.insert(wdrlRecord);
		} else {
			wdrlRecord.preUpdate();
			int updateLines = wdrlRecordDao.update(wdrlRecord);
			if(updateLines == 0) {
				logger.error("提现记录{}更新失败",wdrlRecord.getId());
			}
		}

	}
	
	@Transactional(readOnly = false)
	public void delete(NfsWdrlRecord nfsWdrlRecord) {
		super.delete(nfsWdrlRecord);
	}

	@Override
	public boolean checkPayPassword(Member member, String payNum) {
		
		return StringUtils.equals(member.getPayPassword(), payNum);
	}

	@Override
	public BigDecimal getWdrlAmount(Member member, Date startTime, Date endTime) {
		BigDecimal sumAmount = wdrlRecordDao.getWdrlAmount(member,startTime,endTime);
		return sumAmount==null?BigDecimal.ZERO:sumAmount;
	}

	@Override
	public boolean checkMoney(NfsWdrlRecord nfsWdrlRecord,Member member,BigDecimal money,MemberCard memberCard,Map<String, Object> data) {
		
		
		long nowTime = DateUtils.getTimestamp();
		String date = DateUtils.getDate();
		Date yes = DateUtils.addCalendarByDate(new Date(), -1);
		String yesDate = DateUtils.formatDate(yes, "yyyy-MM-dd");
		try {
			long workStart = DateUtils.parse(date+" 09:30:00").getTime();
			long workEnd = DateUtils.parse(date+" 17:30:00").getTime();
			if(nowTime>=workStart&&nowTime<=workEnd) {//9:30-17:30
				if(money.doubleValue()>=30000) {
					//生成提现记录
		    		nfsWdrlRecord = createWdrlRecord(member,memberCard,money,NfsWdrlRecord.Status.auditing);
					data.put("resultCode", 20);
					data.put("resultMsg", "您的提现待审核,请耐心等待");
					return false;
				}else {
					//生成提现记录
		    		nfsWdrlRecord = createWdrlRecord(member,memberCard,money,NfsWdrlRecord.Status.pendingPay);
				}
			}else {
				if(money.doubleValue()>=30000) {
					data.put("resultCode", 10);
					data.put("resultMsg", "您单笔提现金额超出限制,请重新输入金额！");
					return false;
				}
				Integer hh = Integer.valueOf(DateUtils.getHour());
				List<Date> param = new ArrayList<Date>();
		            if(hh>=0&&hh<=9){//早上 查询昨日至今日累计金额
		                param.add(DateUtils.parse(yesDate+" 17:30:00"));
		                param.add(DateUtils.parse(date+" 09:30:00"));
		            }
		            if(hh>=17&&hh<=23){//下午 查询今日0点前累计金额
		                param.add(DateUtils.parse(date+" 17:30:00"));
		                param.add(DateUtils.parse(date+" 23:59:59"));
		            }
		        if(param!=null&&param.size()!=0) {
		        	Date startTime = param.get(0);
		        	Date endTime =  param.get(1);
		        	BigDecimal amount = getWdrlAmount(member,startTime,endTime);
		        	if(amount.add(money).doubleValue()> 30000) {
		        		data.put("resultCode", 10);
		        		data.put("resultMsg", "您当前的提现金额超出限制,请重新输入金额！!");
		        		return false;
		        	}
		        	//生成提现记录
		    		nfsWdrlRecord = createWdrlRecord(member,memberCard,money,NfsWdrlRecord.Status.pendingPay);
		        }
			}
		} catch (ParseException e) {
			logger.error(Exceptions.getStackTraceAsString(e));
			return false;
		}
		
		return true;
	}
	
	@Override
	public NfsWdrlRecord createWdrlRecord(Member member, MemberCard memberCard, BigDecimal money,NfsWdrlRecord.Status status) {
		
		NfsWdrlRecord nfsWdrlRecord = new NfsWdrlRecord();
		nfsWdrlRecord.setMember(member);
		nfsWdrlRecord.setAmount(money);
		nfsWdrlRecord.setFee("1");
		nfsWdrlRecord.setType(NfsWdrlRecord.Type._default);
		nfsWdrlRecord.setBankId(memberCard.getBank().getId());
		nfsWdrlRecord.setBankName(memberCard.getBank().getName());
		nfsWdrlRecord.setCardNo(memberCard.getCardNo());
		nfsWdrlRecord.setStatus(status);
		save(nfsWdrlRecord);
		
		return nfsWdrlRecord;
	}


	@Override
	public Map<String, Object> checkMoney(Member member, BigDecimal money) {
		Map<String, Object> data = new HashMap<String,Object>();
		long nowTime = DateUtils.getTimestamp();
		String date = DateUtils.getDate();
		Date yes = DateUtils.addCalendarByDate(new Date(), -1);
		String yesDate = DateUtils.formatDate(yes, "yyyy-MM-dd");
		try {
			long workStart = DateUtils.parse(date+" 09:30:00").getTime();
			long workEnd = DateUtils.parse(date+" 17:30:00").getTime();
			if(nowTime >= workStart && nowTime <= workEnd) {
				data.put("success", true);
				return data;
			}else {
				if(money.doubleValue() >= 30000) {
					data.put("success", false);
					data.put("msg", "您单笔提现金额超出限制,请重新输入金额！");
					return data;
				}
				Integer hh = Integer.valueOf(DateUtils.getHour());
				List<Date> param = new ArrayList<Date>();
		            if(hh >= 0 && hh <= 9){//早上 查询昨日至今日累计金额
		                param.add(DateUtils.parse(yesDate+" 17:30:00"));
		                param.add(DateUtils.parse(date+" 09:30:00"));
		            }
		            if(hh >= 17 && hh <= 23){//下午 查询今日0点前累计金额
		                param.add(DateUtils.parse(date+" 17:30:00"));
		                param.add(DateUtils.parse(date+" 23:59:59"));
		            }
		        if(param != null && param.size() !=0) {
		        	Date startTime = param.get(0);
		        	Date endTime =  param.get(1);
		        	BigDecimal amount = getWdrlAmount(member,startTime,endTime);
		        	if(amount == null) {
		        		amount = new BigDecimal(0);
		        	}
		        	if(amount.add(money).doubleValue()> 30000) {
		        		BigDecimal restMoney = new BigDecimal(30000).subtract(amount);
			        	data.put("success", false);
			        	data.put("msg", "您当前剩余时段提现金额为: " + restMoney + " ，请重新输入提现金额！");
			        	return data;
		        	}
		        	data.put("success", true);
		        	return data;
		        }
			}
		} catch (ParseException e) {
			logger.error(Exceptions.getStackTraceAsString(e));
			data.put("success", false);
        	data.put("msg", "系统错误");
			return data;
		}
		data.put("success", true);
		return data;
	}

	@Override
	public List<NfsWdrlRecord> findAuditingRecord(NfsWdrlRecord nfsWdrlRecord) {
		return wdrlRecordDao.findAuditingRecord(nfsWdrlRecord);
	}

	@Override
	public List<NfsWdrlRecord> findListByStatus(NfsWdrlRecord nfsWdrlRecord) {
		return wdrlRecordDao.findListByStatus(nfsWdrlRecord);
	}

	@Override
	public List<NfsWdrlRecord> getWdrlRecordByMemberId(Long memberId, Date loanTime) {
		return wdrlRecordDao.getWdrlRecordByMemberId(memberId, loanTime);
	}

	@Transactional(readOnly=false)
	@Override
	public NfsWdrlRecord withdrawSubmit(NfsWdrlRecord wdrlRecord, int flag) throws Exception{
		save(wdrlRecord);
		BigDecimal amount = wdrlRecord.getAmount();
		Member member = wdrlRecord.getMember();
		Long orgId = wdrlRecord.getId();
		BigDecimal loanActCurBal = memberActService.getMemberAct(member, ActSubConstant.MEMBER_LOAN_BAL).getCurBal();
		int updateCode = Constant.UPDATE_FAILED;
		if(loanActCurBal.compareTo(amount) >= 0) {
			//借款账户余额足够提现
			updateCode = actService.updateAct(TrxRuleConstant.MEMBER_WITHDRAWALS, amount, member, orgId);
			if(updateCode == Constant.UPDATE_FAILED) {
				logger.warn("提现Id:" + wdrlRecord.getId() + " 账户扣款失败!");
				throw new RuntimeException("提现Id:" + wdrlRecord.getId() + " 账户扣款失败!");
			}
		}else {
			//借款账户余额不够提现
			BigDecimal overAmt = amount.subtract(loanActCurBal);//超出部分
			if(loanActCurBal.compareTo(BigDecimal.ZERO) > 0) {
				//借款账户余额不为零  先扣除借款账户余额再扣除可用账户余额
				updateCode = actService.updateAct(TrxRuleConstant.MEMBER_WITHDRAWALS, loanActCurBal, member, orgId);
				if(updateCode == Constant.UPDATE_FAILED) {
					logger.warn("提现Id:" + wdrlRecord.getId() + " 账户扣款失败!");
					throw new RuntimeException("提现Id:" + wdrlRecord.getId() + " 账户扣款失败!");
				}
				updateCode = actService.updateAct(TrxRuleConstant.MEMBER_WITHDRAWALS_AVL, overAmt, member, orgId);
				if(updateCode == Constant.UPDATE_FAILED) {
					logger.warn("提现Id:" + wdrlRecord.getId() + " 账户扣款失败!");
					throw new RuntimeException("提现Id:" + wdrlRecord.getId() + " 账户扣款失败!");
				}
			}else {
				//借款账户余额为零 直接扣除可用余额账户余额
				updateCode = actService.updateAct(TrxRuleConstant.MEMBER_WITHDRAWALS_AVL, amount, member, orgId);
				if(updateCode == Constant.UPDATE_FAILED) {
					logger.warn("提现Id:" + wdrlRecord.getId() + " 账户扣款失败!");
					throw new RuntimeException("提现Id:" + wdrlRecord.getId() + " 账户扣款失败!");
				}
			}
		}
		actService.updateAct(TrxRuleConstant.MEMBER_WITHDRAWALS_FEE,new BigDecimal(wdrlRecord.getFee()), member, orgId);
		
		if(flag == 0 && wdrlRecord.getStatus().equals(NfsWdrlRecord.Status.auditing)) {
			//不需要审核 账户扣款成功更新提现记录状态改为待打款
			wdrlRecord.setStatus(Status.pendingPay);
			save(wdrlRecord);
		}else if(flag == 2 && wdrlRecord.getStatus().equals(NfsWdrlRecord.Status.auditing)){
			//夜晚累计提现大于三万的为可疑订单，需人工审核
			wdrlRecord.setStatus(Status.questionOrder);
			save(wdrlRecord);
		}else {
			//flag = 1 都需要审
		}
		return wdrlRecord;
	}
	
	@Transactional(readOnly=false)
	private int withdrawFailRefund(NfsWdrlRecord wdrlRecord) {
		Long orgId = wdrlRecord.getId();
		Member member = memberService.get(wdrlRecord.getMember());
		//获取流水记录
		List<MemberActTrx> actTrxList = actTrxService.findListByOrgId(orgId);
		BigDecimal loanActAmount = BigDecimal.ZERO;
		BigDecimal avlActAmount = BigDecimal.ZERO;
		for (MemberActTrx actTrx : actTrxList) {
			if(actTrx.getTrxCode().equals(TrxRuleConstant.MEMBER_WITHDRAWALS) && actTrx.getSubNo().equals(ActSubConstant.MEMBER_LOAN_BAL)) {
				loanActAmount = actTrx.getTrxAmt();
			}
			if(actTrx.getTrxCode().equals(TrxRuleConstant.MEMBER_WITHDRAWALS_AVL) && actTrx.getSubNo().equals(ActSubConstant.MEMBER_AVL_BAL)) {
				avlActAmount = actTrx.getTrxAmt();
			}
		}
		int code = Constant.UPDATE_FAILED;
		if(loanActAmount.compareTo(BigDecimal.ZERO) > 0) {
			code = actService.updateAct(TrxRuleConstant.MEMBER_WITHDRAWALS_REFUND_LOAN, loanActAmount, member, orgId);
			if(code == Constant.UPDATE_FAILED) {
				logger.error("会员{}借款账户异常，提现退款失败！",member.getId());
				return code;
			}
		}
		if(avlActAmount.compareTo(BigDecimal.ZERO) > 0) {
			code = actService.updateAct(TrxRuleConstant.MEMBER_WITHDRAWALS_REFUND_AVL, avlActAmount, member, orgId);
			if(code == Constant.UPDATE_FAILED) {
				logger.error("会员{}可用余额账户异常，提现退款失败！",member.getId());
				return code;
			}
		}
//		code = actService.updateAct(TrxRuleConstant.MEMBER_WITHDRAWALS_FEE, new BigDecimal(-1), member, orgId);
		//发短信
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("money", wdrlRecord.getAmount());
		sendSmsMsgService.sendMessage("UnauditedWithdrawal", member.getUsername(), map);
		return Constant.UPDATE_SUCCESS;
	}

	@Override
	public List<NfsWdrlRecord> findFailedRecord(NfsWdrlRecord nfsWdrlRecord) {
		return wdrlRecordDao.findFailedRecord(nfsWdrlRecord);
	}

	@Override
	public Page<NfsWdrlRecord> findAuditedRecord(Page<NfsWdrlRecord> page,NfsWdrlRecord nfsWdrlRecord) {
		nfsWdrlRecord.setPage(page);
		page.setList(wdrlRecordDao.findAuditedRecord(nfsWdrlRecord));
		return page;
	}

	@Override
	public List<NfsWdrlRecord> findFailedSendOrder(Date startTime, Date endTime, int type) {
		return wdrlRecordDao.findFailedSendOrder(startTime, endTime, type);
	}

	@Override
	public List<NfsWdrlRecord> findSubmitedNoRespCodeOrder(Date startTime, Date endTime, int type) {
		return wdrlRecordDao.findSubmitedNoRespCodeOrder(startTime, endTime, type);
	}

	@Override
	public Page<NfsWdrlRecord> findPendingAuditRecord(Page<NfsWdrlRecord> page,NfsWdrlRecord nfsWdrlRecord) {
		nfsWdrlRecord.setPage(page);
		page.setList( wdrlRecordDao.findPendingAuditRecord(nfsWdrlRecord));
		return page;
	}

	@Override
	public List<NfsWdrlRecord> findSubmitedNoThirdOrderNoRecord(NfsWdrlRecord nfsWdrlRecord) {
		
		return wdrlRecordDao.findSubmitedNoThirdOrderNoRecord(nfsWdrlRecord);
	}

	@Override
	@Transactional(readOnly=false)
	public int refuse(NfsWdrlRecord wdrlRecord) {
		wdrlRecord = get(wdrlRecord);
		if(wdrlRecord.getStatus().equals(NfsWdrlRecord.Status.refuse)||wdrlRecord.getStatus().equals(NfsWdrlRecord.Status.cancel)||wdrlRecord.getStatus().equals(NfsWdrlRecord.Status.failure)||wdrlRecord.getStatus().equals(NfsWdrlRecord.Status.madeMoney)){
			return Constant.UPDATE_FAILED;
		}
		wdrlRecord.setStatus(NfsWdrlRecord.Status.refuse);
		int updateLines = wdrlRecordDao.update(wdrlRecord);
		if(updateLines > 0){
			return withdrawFailRefund(wdrlRecord);
		}else {
			return Constant.UPDATE_FAILED;
		}
		
	}

	@Override
	@Transactional(readOnly=false)
	public int failure(NfsWdrlRecord wdrlRecord,String rmk) {

		wdrlRecord = get(wdrlRecord);
		if(wdrlRecord.getStatus().equals(NfsWdrlRecord.Status.refuse)||wdrlRecord.getStatus().equals(NfsWdrlRecord.Status.cancel)||wdrlRecord.getStatus().equals(NfsWdrlRecord.Status.failure)||wdrlRecord.getStatus().equals(NfsWdrlRecord.Status.madeMoney)){
			return Constant.UPDATE_SUCCESS;
		}
		wdrlRecord.setType(Type.lianlian);
		wdrlRecord.setPayAmount(wdrlRecord.getAmount().subtract(new BigDecimal(wdrlRecord.getFee())));
		wdrlRecord.setStatus(NfsWdrlRecord.Status.failure);
		if(StringUtils.isNotBlank(rmk)) {
			if(StringUtils.isBlank(wdrlRecord.getRmk())) {
				wdrlRecord.setRmk(rmk);
			}else {
				wdrlRecord.setRmk(wdrlRecord.getRmk() + "#" + rmk);
			}
		}
		int updateLines = wdrlRecordDao.update(wdrlRecord);
		if(updateLines > 0){
			//打款失败处理退款
			return withdrawFailRefund(wdrlRecord);
		}else {
			logger.error("提现订单{}状态更新失败",wdrlRecord.getId());
			return Constant.UPDATE_FAILED;
		}
	}

	@Override
	@Transactional(readOnly=false)
	public int cancel(NfsWdrlRecord wdrlRecord) {

		wdrlRecord = get(wdrlRecord);
		if(wdrlRecord.getStatus().equals(NfsWdrlRecord.Status.refuse)||wdrlRecord.getStatus().equals(NfsWdrlRecord.Status.cancel)||wdrlRecord.getStatus().equals(NfsWdrlRecord.Status.failure)||wdrlRecord.getStatus().equals(NfsWdrlRecord.Status.madeMoney)){
			return Constant.UPDATE_FAILED;
		}
		wdrlRecord.setStatus(NfsWdrlRecord.Status.cancel);
		int updateLines = wdrlRecordDao.update(wdrlRecord);
		if(updateLines > 0){
			return withdrawFailRefund(wdrlRecord);
		}else {
			return Constant.UPDATE_FAILED;
		}
	}

	@Override
	@Transactional(readOnly=false)
	public int checkPass(NfsWdrlRecord wdrlRecord) {
		wdrlRecord.setCheckTime(new Date());
		checkRecordService.saveWdrlCheckLog(wdrlRecord);
		if(wdrlRecord.getStatus().equals(NfsWdrlRecord.Status.mayRepeatOrder)) {
			wdrlRecord.setType(NfsWdrlRecord.Type.lianlian);
			wdrlRecord.setStatus(NfsWdrlRecord.Status.sendRepeatOrder);
		}else {
			wdrlRecord.setStatus(NfsWdrlRecord.Status.pendingPay);
		}
		int updateLines = wdrlRecordDao.update(wdrlRecord);
		if(updateLines > 0) {
			return Constant.UPDATE_SUCCESS;
		}else {
			return Constant.UPDATE_FAILED;
		}
	}

	@Override
	@Transactional(readOnly=false)
	public int successConfirm(NfsWdrlRecord wdrlRecord) {

		wdrlRecord.setStatus(NfsWdrlRecord.Status.madeMoney);
		checkRecordService.saveWdrlCheckLog(wdrlRecord);
		int updateLines = wdrlRecordDao.update(wdrlRecord);
		if(updateLines > 0) {
			return Constant.UPDATE_SUCCESS;
		}else {
			return Constant.UPDATE_FAILED;
		}
	}

	@Override
	public int isNeedCheck(BigDecimal amount, Member member) {

        //单笔大于30000都需要审核
	     if (amount.compareTo(new BigDecimal(30000))>=0){
	    	 return 1;//需要审核
	     }
        Date date = new Date();
        try {
			 Date yes = DateUtils.addCalendarByDate(date, -1);
			 String nowYMD = DateUtils.getDateStr(date, "yyyy-MM-dd");
			 String yesYMD = DateUtils.getDateStr(yes, "yyyy-MM-dd");
			 long nowTime = DateUtils.getTimestamp();
			 String dateNoTime = DateUtils.getDate();
			
			long workStart = DateUtils.parse(dateNoTime+" 09:30:00").getTime();
			long workEnd = DateUtils.parse(dateNoTime+" 17:30:00").getTime();
			if(!(nowTime >= workStart && nowTime <= workEnd)) {
		         //其余时间
			     SimpleDateFormat sdf = new SimpleDateFormat("HH");
			     int HH = Integer.valueOf(sdf.format(date));
			     BigDecimal sumAmount = BigDecimal.ZERO;
			     if(HH>=0&&HH<=9){//早上 查询昨日至今日累计金额
			         sumAmount = getWdrlAmount(member, DateUtils.parse(yesYMD+" 17:30:00"), DateUtils.parse(nowYMD+" 09:30:00"));
			     }
			     if(HH>=17&&HH<=23){//下午 查询今日0点前累计金额
			         sumAmount = getWdrlAmount(member, DateUtils.parse(nowYMD+" 17:30:00"), DateUtils.parse(nowYMD+" 23:59:59"));
			     }
			     
			     if(sumAmount.doubleValue()+amount.doubleValue()> 30000d){
			         return 2;//累计提现3W 禁止提现
			     }
			}
			 return 0;
		} catch (NumberFormatException e) {
			logger.error(Exceptions.getStackTraceAsString(e));
			 return 2;
		} catch (ParseException e) {
			logger.error(Exceptions.getStackTraceAsString(e));
			return 2;
		}
	}

	@Override
	@Transactional(readOnly=false)
	public int failureForFuiou(NfsWdrlRecord wdrlRecord) {
		int updateLines = wdrlRecordDao.update(wdrlRecord);
		if(updateLines > 0){
			return withdrawFailRefund(wdrlRecord);
		}else {
			logger.error("提现订单{}状态更新失败",wdrlRecord.getId());
			return Constant.UPDATE_FAILED;
		}
	}
}