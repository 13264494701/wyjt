package com.jxf.mem.service.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.jxf.mem.dao.MemberActDao;
import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberAct;

import com.jxf.mem.service.MemberActService;


import com.jxf.mem.utils.VerifiedUtils;
import com.jxf.nfs.constant.ActSubConstant;
import com.jxf.nfs.constant.TrxRuleConstant;
import com.jxf.nfs.entity.NfsActSub;
import com.jxf.nfs.entity.NfsTransferRecord;

import com.jxf.nfs.service.NfsTransferRecordService;
import com.jxf.nfs.utils.NfsUtils;
import com.jxf.svc.config.Constant;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.ServiceException;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
import com.jxf.svc.utils.Collections3;
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.StringUtils;
import com.jxf.web.model.ResponseData;



/**
 * 会员账户ServiceImpl
 * @author zhj
 * @version 2016-05-12
 */
@Service("memberActService")
@Transactional(readOnly = true)
public class MemberActServiceImpl extends CrudServiceImpl<MemberActDao, MemberAct> implements MemberActService{
	
	
	private static Logger log = LoggerFactory.getLogger(MemberActServiceImpl.class);

	@Autowired
	private MemberActDao memberActDao;
	@Autowired
	private MemberActService memberActService;

	@Autowired
	private NfsTransferRecordService transferRecordService;
	
	private BigDecimal zero = BigDecimal.ZERO;
	
	@Override
	public MemberAct get(Long id) {
		return super.get(id);
	}
	
	@Override
	public List<MemberAct> findList(MemberAct memberAct) {
		return super.findList(memberAct);
	}
	
	@Override
	public Page<MemberAct> findPage(Page<MemberAct> page, MemberAct memberAct) {		
		return super.findPage(page, memberAct);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void save(MemberAct memberAct) {
		if (memberAct.getIsNewRecord()){
			memberAct.preInsert();		
			memberAct.setStatus(MemberAct.Status.enabled);
			memberAct.setCurBal(new BigDecimal("0.00"));
			memberActDao.insert(memberAct);
		}else{
			memberAct.preUpdate();
			memberActDao.update(memberAct);
		}
	}
	
	@Override
	@Transactional(readOnly = false)
	public void delete(MemberAct memberAct) {
		super.delete(memberAct);
	}
	
	
	/**
	 * 初始化会员账户
	 * @param member
	 * @return
	 */
	@Override
	@Transactional(readOnly=false)
	public Boolean initMemAct(Member member){
		Boolean flag = true;
		List<NfsActSub> subs = NfsUtils.findSubsByTrxRole("member");	
		MemberAct memAct = new MemberAct();
		try {
			for (NfsActSub sub : subs) {
				memAct.setIsNewRecord(true);
				memAct.setMember(member);
				memAct.setSubNo(sub.getSubNo());
				memAct.setCurrCode("CNY");								
				memAct.setName(sub.getName() + "账户");
				memAct.setIsDefault(StringUtils.equals(sub.getSubNo(), "0001"));
				save(memAct);
			}
		} catch (ServiceException e) {
			flag = false;
		}
		return flag;
	}

	/**
	 * 修改状态
	 * @param actNo
	 * @param actSts
	 */
	@Override
	@Transactional(readOnly= false)
	public int updateActSts(MemberAct memberAct){
		return memberActDao.updateActSts(memberAct);
	}
	
	@Override
	@Transactional(readOnly= false)
	public int updateAct(MemberAct memberAct, BigDecimal trxAmt) {
		Long memberId = memberAct.getMember().getId();
		String subNo = memberAct.getSubNo();
									
		if (trxAmt.compareTo(BigDecimal.ZERO) == 0) {
			log.error("会员[{}]账户科目[{} ]  交易金额为 0！",memberId,subNo);
			return Constant.UPDATE_FAILED;
		}
		BigDecimal minCurBal = new BigDecimal(0.1).negate();
		BigDecimal newCurBal = memberAct.getCurBal().add(trxAmt);
	    if(memberAct.getMember().getId().longValue() == 296463856056803328L && (StringUtils.equals(subNo, ActSubConstant.MEMBER_AVL_BAL)||StringUtils.equals(subNo, ActSubConstant.MEMBER_LOAN_BAL))) {
			if(trxAmt.compareTo(BigDecimal.ZERO)<0&&newCurBal.compareTo(new BigDecimal(495)) <= 0) {
				log.error("会员{}账户科目：{} 扣款后金额为 ： {} ,扣款失败！", memberId,subNo,newCurBal.toString());
				return Constant.UPDATE_FAILED;
			}
		}else if(memberAct.getMember().getId().longValue() == 293073456373829632L && (StringUtils.equals(subNo, ActSubConstant.MEMBER_AVL_BAL)||StringUtils.equals(subNo, ActSubConstant.MEMBER_LOAN_BAL))) {
			if(trxAmt.compareTo(BigDecimal.ZERO)<0&&newCurBal.compareTo(new BigDecimal(3000)) <= 0) {
				log.error("会员{}账户科目：{} 扣款后金额为 ： {} ,扣款失败！", memberId,subNo,newCurBal.toString());
				return Constant.UPDATE_FAILED;
			}
		}else if(memberAct.getMember().getId().longValue() == 337297175077851136L && (StringUtils.equals(subNo, ActSubConstant.MEMBER_AVL_BAL)||StringUtils.equals(subNo, ActSubConstant.MEMBER_LOAN_BAL))) {
			if(trxAmt.compareTo(BigDecimal.ZERO)<0&&newCurBal.compareTo(new BigDecimal(1039)) <= 0) {
				log.error("会员{}账户科目：{} 扣款后金额为 ： {} ,扣款失败！", memberId,subNo,newCurBal.toString());
				return Constant.UPDATE_FAILED;
			}
		}else {
			//待收待还扣减阈值下限放到-0.1元
			if(StringUtils.equals(subNo, ActSubConstant.MEMBER_PENDING_RECEIVE) || StringUtils.equals(subNo, ActSubConstant.MEMBER_PENDING_REPAYMENT)) {
				if(newCurBal.compareTo(minCurBal) < 0) {
					log.error("会员{}账户科目：{} 扣款后金额为 ： {} ,扣款失败！",memberId,subNo,newCurBal.toString());
					return Constant.UPDATE_FAILED;
				}
			    if(newCurBal.compareTo(BigDecimal.ZERO) < 0) {
			    	newCurBal = BigDecimal.ZERO; 
			    }
			}else {
				if(newCurBal.compareTo(BigDecimal.ZERO) < 0) {
					log.error("会员{}账户科目：{} 扣款后金额为 ： {} ,扣款失败！",memberId,subNo,newCurBal.toString());
					return Constant.UPDATE_FAILED;
				}
			}
		}

		memberAct.setCurBal(newCurBal);
		memberAct.preUpdate();
		int numAttempts = 1;
		int updateLines = 0;
		do {
			updateLines = memberActDao.update(memberAct);// 乐观锁重试
			if (updateLines > 0 || numAttempts > Constant.MAX_ATTEMPTS) {
				break;
			} else {
				log.warn("会员[{}]账户科目[{}]更新失败，发起第{}次重试",memberId,subNo, numAttempts);
				memberAct = memberActDao.get(memberAct);
				newCurBal = memberAct.getCurBal().add(trxAmt);
				//待收待还扣减阈值下限放到-0.1元
				if(StringUtils.equals(subNo, ActSubConstant.MEMBER_PENDING_RECEIVE) || StringUtils.equals(subNo, ActSubConstant.MEMBER_PENDING_REPAYMENT)) {
					if(newCurBal.compareTo(minCurBal) < 0) {
						log.error("会员{}账户科目：{} 扣款后金额为 ： {} ,扣款失败！",memberId,subNo,newCurBal.toString());
						return Constant.UPDATE_FAILED;
					}
					if(newCurBal.compareTo(BigDecimal.ZERO) < 0) {
				    	newCurBal = BigDecimal.ZERO; 
				    }
				}else {
					if(newCurBal.compareTo(BigDecimal.ZERO) < 0) {
						log.error("会员{}账户科目：{} 扣款后金额为 ： {} ,扣款失败！",memberId,subNo,newCurBal.toString());
						return Constant.UPDATE_FAILED;
					}
				}
				
				memberAct.setCurBal(newCurBal);
				numAttempts++;
			}
		} while (updateLines == 0);
		return Constant.UPDATE_SUCCESS;
	}

	@Override
	@Transactional(readOnly= false)
	public void setDefault(MemberAct memberAct) {		
		memberActDao.setDefault(memberAct);
	}
	@Override
	public MemberAct getMemberDefaultAct(Member member) {
		
		MemberAct memberAct = new MemberAct();
		memberAct.setMember(member);
		memberAct.setIsDefault(true);
		return memberActDao.getMemberDefaultAct(memberAct);
	}
	@Override
	public MemberAct getMemberAct(Member member,String subNo) {
		
		MemberAct act = new MemberAct();
		act.setMember(member);
		act.setSubNo(subNo);			
		List<MemberAct> actList = memberActService.findList(act);
		if(Collections3.isEmpty(actList)) {
			return null;
		}
		return actList.get(0);
	}
	@Override
	public List<MemberAct> getMemberActList(Member member) {
		MemberAct act = new MemberAct();
		act.setMember(member);
		return memberActDao.findList(act);
	}
    /****
     * 获取会员可用余额
     */
	@Override
	public BigDecimal getAvlBal(Member member) {
//		String avlBalStr = (String)RedisUtils.getHashKey("memberInfo" + member.getId(), "avlBal");
		BigDecimal avlBal = BigDecimal.ZERO;
//		if(StringUtils.isNotBlank(avlBalStr)){
//			avlBal = new BigDecimal(avlBalStr);
//		}else{
//			MemberAct act = getMemberAct(member,ActSubConstant.MEMBER_AVL_BAL);						
//			avlBal = act.getCurBal().setScale(2, BigDecimal.ROUND_HALF_UP);
//			RedisUtils.put("memberInfo" + member.getId(), "avlBal",StringUtils.decimalToStr(avlBal, 2));
//		}
		MemberAct act = getMemberAct(member,ActSubConstant.MEMBER_AVL_BAL);						
		avlBal = act.getCurBal().setScale(2, BigDecimal.ROUND_HALF_UP);
		return avlBal;
	}
	@Override
	public ResponseData checkCanTransfer(Member member, Member friend, BigDecimal increment){
		Date nowDate = new Date();
		//BigDecimal temp = zero; //从可用余额中转账金额
		if(member.getIsLocked()){
			return ResponseData.error("您的账号已被锁定，请联系客服!");
		}
		Integer verifiedList = member.getVerifiedList();
		if(!VerifiedUtils.isVerified(verifiedList, 22)){
			return ResponseData.error("您还没有设置支付密码,请去设置支付密码!");
		}
		if(new BigDecimal(increment.intValue()).compareTo(increment) != 0){
			return ResponseData.error("转账金额必须是整数!");
		}  
		if(increment.compareTo(zero) < 0){
			return ResponseData.error("转账金额必须大于0元!");
		} 
		
		//判断金额够不够
		BigDecimal curBal = memberActService.getCurBal(member.getId());
		if(curBal.compareTo(increment) < 0){
			return ResponseData.error("账户余额不足!");
		}
		
		if(member.getId().equals(315583504190672896L)) {
			return ResponseData.success("余额充足"); 
		}
		
		if(increment.intValue() > TrxRuleConstant.SINGLE_LIMIT){
			return ResponseData.error("单笔转账金额不得超过5000");
		}
		//一年50W 当年1月1号
		String year = DateUtils.getYear(new Date());
		Date beginTime = null;
		try {
			beginTime = DateUtils.parseDate(year + "-01-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		NfsTransferRecord transfer = new NfsTransferRecord();
		transfer.setBeginTime(beginTime);
		transfer.setMember(member);
		BigDecimal totalTransfer = transferRecordService.getTotalTransferOneYear(transfer);
		if(totalTransfer == null){
			totalTransfer = BigDecimal.ZERO;
		}
		BigDecimal add = totalTransfer.add(increment);
		
		if(add.compareTo(TrxRuleConstant.YEAR_LIMIT)>0){
			return ResponseData.error("一年内转账金额不得超过50万,您还可以转账金额:"+TrxRuleConstant.YEAR_LIMIT.subtract(totalTransfer).intValue());
		}
		
		//一天5W
		Calendar calendar = Calendar.getInstance();
        calendar.setTime(nowDate);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        beginTime  = calendar.getTime();//当天 0 点
		transfer = new NfsTransferRecord();
		transfer.setBeginTime(beginTime);
		transfer.setMember(member);
		transfer.setStatus(NfsTransferRecord.Status.alreadyReceived);
		List<NfsTransferRecord> recordListInOneDay = transferRecordService.findList(transfer);
		if(recordListInOneDay != null && recordListInOneDay.size()>0){
			BigDecimal total = zero;
			for (NfsTransferRecord nfsTransferRecord : recordListInOneDay) {
				total = total.add(nfsTransferRecord.getAmount());
			}
			BigDecimal temp = total.add(increment);
			if(temp.compareTo(TrxRuleConstant.DAY_LIMIT)>0){
				return ResponseData.error("一天内转账金额不得超过5万,您还可以转账金额:"+TrxRuleConstant.DAY_LIMIT.subtract(total).intValue());
			}
		}
		
		//一天失败3次
		Integer times = transferRecordService.todayHasTransferFailedTimes(member,beginTime);
		if(times >= TrxRuleConstant.TIMES_TODAY){ 
			return ResponseData.error("您今日的肖像比对认证次数已达上限\n如有疑问请联系在线客服!");
		}

		/*变需求了
		 * //先判断从借款账户 转帐 金额
		MemberAct memberAct = new MemberAct();
		memberAct.setMember(member);
		NfsLoanRecord nfsLoanRecord = new NfsLoanRecord();
		nfsLoanRecord.setLoanee(member);
		nfsLoanRecord.setLoaner(friend);//好友是放款人
		nfsLoanRecord.setStatus(NfsLoanRecord.Status.repayed);
		List<NfsLoanRecord> findList = nfsLoanRecordService.findNotRepayedList(nfsLoanRecord);//未完成状态的借款单
		if(findList != null && findList.size() > 0){//如果俩人之间有待还的借条且friend是放款人
			memberAct.setSubNo(ActSubConstant.MEMBER_LOAN_BAL);
			List<MemberAct> actList = memberActService.findList(memberAct);
			memberAct = actList.get(0);
			BigDecimal curBal = memberAct.getCurBal();
			if(curBal.compareTo(zero) != 0){//借款账户不为0
				BigDecimal maxCanTransfer = zero;//累计最多可以转账的金额
				Date earliestTime = null;//俩人之间最早 未完成 借条的时间
				for (NfsLoanRecord record : findList) {
					BigDecimal amount = record.getAmount();//本金
					BigDecimal fee = record.getFee();//手续费
					maxCanTransfer = maxCanTransfer.add(amount.subtract(fee));
					Date createTime = record.getCreateTime();
					if(earliestTime == null){
						earliestTime = createTime;
					}else if(earliestTime != null && earliestTime.getTime()>createTime.getTime()){
						earliestTime = createTime;
					}
				}
				//查俩人有 未完成 借条之后 的成功转账记录
				List<NfsTransferRecord> transferRecordList = transferRecordService.findByMemberIdAndFriendIdAndCreateDate(member,friend,earliestTime);
				BigDecimal haveTransfer = zero;//过去已经转账金额
				if(transferRecordList != null && transferRecordList.size()>0){
					for (NfsTransferRecord nfsTransferRecord : transferRecordList) {
						haveTransfer = haveTransfer.add(nfsTransferRecord.getAmount());
					}
				}
				BigDecimal transferable = maxCanTransfer.subtract(haveTransfer);//现在还可以从借款账户转出金额
				if(transferable.compareTo(zero)>0){//如果还可以从借款账户转的金额大于0
					if(transferable.compareTo(increment)<0){//现在可转金额<本次转账金额 那么最多从借款账户转 transferable 这么多
						if(transferable.compareTo(curBal)>0){//账户余额不足
							temp = increment.subtract(curBal);
						}else{
							temp = increment.subtract(transferable);//先从借款账户转transferable 剩下从可用余额转
						}
					}else{//现在可转金额>本次转账量 那么最多从借款账户转 increment 这么多
						if(increment.abs().compareTo(curBal)>0){//借款账户余额不足
							temp = increment.subtract(curBal);
						} 
					}
				}else{ //如果还可以从借款账户转的金额为0
					temp = increment;
				}
			}else{ //借款账户为0
				temp = increment;
			}
		}else {//俩人之间没借条
			temp = increment;
		}
		//再判断 可用余额是否不足
		MemberAct actTemp = new MemberAct();
		actTemp.setSubNo(ActSubConstant.MEMBER_AVL_BAL);
		actTemp.setMember(member);
		List<MemberAct> actList = memberActService.findList(actTemp);
		actTemp = actList.get(0);
		BigDecimal curBal = actTemp.getCurBal();
		if(temp.compareTo(curBal)>0){
			return ResponseData.error("可用余额不足!");
		}*/
		
		
		
		return ResponseData.success("余额充足"); 
	}
//	@Override
//	public void updateRedis(String subNo , Member member , BigDecimal trxAmt,MemberAct memberAct) {
//		BigDecimal curBal = memberAct.getCurBal(); //更新后的账户余额
//		//TODO 这里的curBal取得是更新后的，但是没有提交到数据库的数据
//		//有可能会引发并发问题：比如借款人确认收款的同时，其他借条申请放款人同意修改了数据库金额，那么就会造成这里的curBal覆盖放款人操作后的结果
//		BigDecimal account = zero;
//		//更新前的账户总额
//		MemberAct param = new MemberAct();
//		param.setMember(member);
//		List<MemberAct> list = memberActService.findList(param);
//		for (MemberAct memberAct2 : list) {
//			if(StringUtils.equals(memberAct2.getSubNo(), ActSubConstant.MEMBER_AVL_BAL)) {
//				account = account.add(memberAct2.getCurBal());
//			}else if(StringUtils.equals(memberAct2.getSubNo(), ActSubConstant.MEMBER_LOAN_BAL)) {
//				account = account.add(memberAct2.getCurBal());
//			}
//		}
//		switch(subNo){//更新缓存
//		case ActSubConstant.MEMBER_AVL_BAL:
//			RedisUtils.put("memberInfo" + member.getId(),Member.AVL_BAL,StringUtils.decimalToStr(curBal, 2));
//			account = account.add(trxAmt);
//			//账户金额有变化更新缓存
//			RedisUtils.put("memberInfo" + member.getId(),Member.CUR_BAL,StringUtils.decimalToStr(account, 2));
//			break; 
//		case ActSubConstant.MEMBER_LOAN_BAL://借款账户目前没放缓存
//			RedisUtils.put("memberInfo" + member.getId(),Member.LOAN_BAL,StringUtils.decimalToStr(curBal, 2));
//			//账户金额有变化更新缓存
//			account = account.add(trxAmt);
//			RedisUtils.put("memberInfo" + member.getId(),Member.CUR_BAL,StringUtils.decimalToStr(account, 2));
//			break; 
//		case ActSubConstant.MEMBER_FREEZEN_BAL:
//			RedisUtils.put("memberInfo" + member.getId(),Member.FREEZEN_BAL,StringUtils.decimalToStr(curBal, 2));
//			break; 
//		case ActSubConstant.MEMBER_REDBAG_BAL:
//			RedisUtils.put("memberInfo" + member.getId(),Member.REDBAG_BAL,StringUtils.decimalToStr(curBal, 2));
//			break; 
//		case ActSubConstant.MEMBER_PENDING_REPAYMENT:
//			RedisUtils.put("memberInfo" + member.getId(),Member.PENDING_REPAYMENT,StringUtils.decimalToStr(curBal, 2));
//			break; 
//		case ActSubConstant.MEMBER_PENDING_RECEIVE:
//			RedisUtils.put("memberInfo" + member.getId(),Member.PENDING_RECEIVE,StringUtils.decimalToStr(curBal, 2));
//			break; 
//		}
//	}

	@Override
	public BigDecimal getCurBal(Long memberId) {
		return memberActDao.getCurBal(memberId);
	}

}