package com.jxf.mem.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ebaoquan.rop.thirdparty.com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.loan.entity.NfsCrAuction;
import com.jxf.loan.entity.NfsLoanApply;
import com.jxf.loan.entity.NfsLoanApply.LoanRole;
import com.jxf.loan.entity.NfsLoanApply.TrxType;
import com.jxf.loan.entity.NfsLoanApplyDetail;
import com.jxf.loan.entity.NfsLoanArbitration;
import com.jxf.loan.entity.NfsLoanArbitrationExecution;
import com.jxf.loan.entity.NfsLoanPartialAndDelay;
import com.jxf.loan.entity.NfsLoanRecord;
import com.jxf.loan.service.NfsCrAuctionService;
import com.jxf.loan.service.NfsLoanApplyDetailService;
import com.jxf.loan.service.NfsLoanApplyService;
import com.jxf.loan.service.NfsLoanArbitrationExecutionService;
import com.jxf.loan.service.NfsLoanArbitrationService;
import com.jxf.loan.service.NfsLoanPartialAndDelayService;
import com.jxf.loan.service.NfsLoanRecordService;
import com.jxf.mem.dao.MemberMessageDao;
import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberActTrx;
import com.jxf.mem.entity.MemberFriendCa;
import com.jxf.mem.entity.MemberMessage;
import com.jxf.mem.entity.MemberMessage.Type;
import com.jxf.mem.service.MemberActTrxService;
import com.jxf.mem.service.MemberFriendCaService;
import com.jxf.mem.service.MemberMessageService;
import com.jxf.mem.service.MemberService;
import com.jxf.mms.tmpl.entity.MmsMsgTmpl;
import com.jxf.mms.tmpl.service.MmsMsgTmplService;
import com.jxf.nfs.entity.NfsRchgRecord;
import com.jxf.nfs.entity.NfsTransferRecord;
import com.jxf.nfs.entity.NfsWdrlRecord;
import com.jxf.nfs.service.NfsRchgRecordService;
import com.jxf.nfs.service.NfsTransferRecordService;
import com.jxf.nfs.service.NfsWdrlRecordService;
import com.jxf.svc.cache.RedisUtils;
import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
import com.jxf.svc.utils.CalendarUtil;
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.Exceptions;
import com.jxf.svc.utils.JSONUtil;
import com.jxf.svc.utils.StringUtils;
/**
 * 会员消息ServiceImpl
 * @author gaobo
 * @version 2018-10-19
 */
@Service("memMemberMessageService")
@Transactional(readOnly = true)
public class MemberMessageServiceImpl extends CrudServiceImpl<MemberMessageDao, MemberMessage> implements MemberMessageService{

	@Autowired
	private MemberMessageDao memberMessageDao;
	@Autowired
	private MmsMsgTmplService msgTmplService;
	@Autowired
	private NfsLoanApplyDetailService nfsLoanApplyDetail;
	@Autowired
	private NfsLoanRecordService nfsLoanRecord;
	@Autowired
	private NfsLoanApplyService loanApplyService;
	@Autowired
	private NfsCrAuctionService nfsCrAuctionService;
	@Autowired
	private NfsLoanArbitrationService nfsLoanArbitrationService;
	@Autowired
	private NfsLoanArbitrationExecutionService nfsLoanArbitrationExecutionService;
	@Autowired
	private NfsTransferRecordService nfsTransferRecordService;
	@Autowired
	private MemberFriendCaService memberFriendCaService;
	@Autowired 
	private MemberService memberService;
	@Autowired
	private MemberActTrxService memberActTrxService;
	@Autowired
	private NfsRchgRecordService nfsRchgRecordService;
	@Autowired
	private NfsWdrlRecordService wdrlRecordService;
	@Autowired
	private NfsLoanArbitrationService loanArbitrationService;
	@Autowired
	private NfsLoanPartialAndDelayService nfsLoanPartialAndDelayService;
	
	public MemberMessage get(Long id) {
		return super.get(id);
	}
	
	public List<MemberMessage> findList(MemberMessage memMemberMessage) {
		return super.findList(memMemberMessage);
	}
	
	public Page<MemberMessage> findPage(Page<MemberMessage> page, MemberMessage memMemberMessage) {
		return super.findPage(page, memMemberMessage);
	}
	
	@Transactional(readOnly = false)
	public void save(MemberMessage memMemberMessage) {
		super.save(memMemberMessage);
	}
	
	@Transactional(readOnly = false)
	public void delete(MemberMessage memMemberMessage) {
		super.delete(memMemberMessage);
	}

	/**
	 * 发送会员消息 
	 * member为收到短信的会员  
	 * map 内指定模板参数值 如果没有参数设为 null 
	 * title 消息标题
	 * orgId 原业务ID (多人我发起的是applyId 单人/多人我收到 的申请 时候是detailId 单人借条是recordId)
	 * orgType 原业务ID类型  0：apply; 1:detail; 2:record; 3 其他
	 */
	@Transactional(readOnly = false)
	@Override
	public MemberMessage sendMessage(MemberMessage.Type type,MemberMessage.Group group, Member member, Map<String, Object> map, 
			Long orgId,String orgType, String rmk) {
		//获取模板信息
		String content = msgTmplService.process(type.toString(), map);
		MmsMsgTmpl tmpl = msgTmplService.getTmplByCode(type.toString());
		
		MemberMessage memberMessage = new MemberMessage();
		memberMessage.setMember(member);
		memberMessage.setTitle(tmpl.getName());
		memberMessage.setTitleValue(tmpl.getNameValue());
		memberMessage.setContent(content);
		memberMessage.setGroups(group);
		memberMessage.setRead(false);
		memberMessage.setType(type);
		memberMessage.setOrgId(orgId);
		memberMessage.setOrgType(orgType);
		memberMessage.setRmk(rmk);
		save(memberMessage);
		
		RedisUtils.increment("memberInfo"+member.getId(),"newMsgCount",1);
				
		return memberMessage;
	}
	/***
	 * 	发送会员消息 
	 *  type 消息类型 orgId 原业务id n：type范围
 	 * 	n >= 0&&n <= 7||n >= 10&&n <= 14||n >= 76&&n <= 79||n == 25||n == 34 -> orgId = detailId
	 * 	n >= 8&&n <= 9||n >= 15&&n <= 18||n >= 21&&n <= 22||n >= 26&&n <= 27||n == 30||n == 31||n >= 44&&n <= 61
		||n >= 80&&n <= 81||n >= 87&&n <= 98||n >= 101&&n <= 105||n == 111 -> orgId = recordId
	 * 	37转账id	38-43ca_id 62-66 cr_id	68-73 110 orgTrxId	 19/20/23/24/28/29/32/33 82-86部分还款id -> orgId = otherId
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = false)
	@Override
	public MemberMessage sendMessage(MemberMessage.Type type,Long orgId) {
		logger.debug("============发送消息===========");
		MemberMessage memberMessage = new MemberMessage();
		try {
			//1
			String group = getGroupsByType(type);
			//2
			String orgType = getOrgTypeByType(type);
			//3
			Map<String,Object> map =  getOtherParams(type, orgId);
			//4
			Long newOrgId = getNewOrgId(type, orgId);
			Member member = (Member) map.get("member");
			//充值 提现消息公用一个模板 需要重新赋值group
			if(type.equals(MemberMessage.Type.successfulRecharge)||type.equals(MemberMessage.Type.cashWithdrawalAccount)) {
				group = (String) map.get("groups");
			}
//		logger.debug("=====================map===================="+map.get("map"));
//		logger.debug("=====================rmk===================="+(String) map.get("rmk"));
//		logger.debug("=====================memberId===================="+member.getId());
//		logger.debug("=====================orgId===================="+orgId);
//		logger.debug("=====================newOrgId===================="+newOrgId);
//		logger.debug("=====================group===================="+newOrgId);
			//获取模板信息
			String content = msgTmplService.process(type.toString(), map.get("map") == null?null:(Map<String, Object>) map.get("map"));
			MmsMsgTmpl tmpl = msgTmplService.getTmplByCode(type.toString());
			
			memberMessage.setMember(member);
			memberMessage.setTitle(tmpl.getName());
			memberMessage.setTitleValue(tmpl.getNameValue());
			memberMessage.setContent(content);
			memberMessage.setGroups(MemberMessage.Group.valueOf(group));
			memberMessage.setRead(false);
			memberMessage.setType(type);
			memberMessage.setOrgId(newOrgId);
			memberMessage.setOrgType(orgType);
			memberMessage.setRmk((String) map.get("rmk"));
			save(memberMessage);
			RedisUtils.increment("memberInfo"+member.getId(),"newMsgCount",1);
			
		} catch (Exception e) {
			Exceptions.getStackTraceAsString(e);
			logger.error("======发送消息失败type:{} orgId:{}======", type, orgId);
		}
		return memberMessage;
	}

	private Map<String, Object> getOtherParams(Type type, Long orgId) {
		Map<String, Object> map = new HashMap<String, Object>();
		int n = type.ordinal();
		if(n >= 0&&n <= 7||n >= 10&&n <= 14||n >= 76&&n <= 79||n == 25||n == 34) {
			//detailId
			map = getParamsByDetailId(n, orgId);
		}else if(n >= 8&&n <= 9||n >= 15&&n <= 18||n >= 21&&n <= 22||n >= 26&&n <= 27||n == 30||n == 31||n >= 44&&n <= 61
				||n >= 80&&n <= 81||n >= 87&&n <= 98||n >= 101&&n <= 105||n == 111) {
			//recordId	
			map = getParamsByRecordId(n, orgId);
		}else {
			//otherId 37转账id	38-43 caid(42/43 org_id 好友id) 62-66 cr_id	68-73 110 orgTrxId1	 19/20/23/24/28/29/32/33 82-86部分还款id
			map = getParamsByOtherId(n, orgId);
		}
		return map;
	}
	private Long getNewOrgId(Type type, Long orgId) {
		Long newOrgId = 0L;
		MemberFriendCa ca = null;
		NfsLoanPartialAndDelay partialAndDelay = null;
		int n = type.ordinal();
		switch(n) {
		case 37:
			NfsTransferRecord transferRecord = nfsTransferRecordService.get(orgId);
			newOrgId = transferRecord.getMember().getId();
			break;
		case 42:
			ca = memberFriendCaService.get(orgId);
			newOrgId = ca.getFriend().getId();
			break;
		case 43:
			ca = memberFriendCaService.get(orgId);
			newOrgId = ca.getFriend().getId();
			break;
		case 19:
			partialAndDelay = nfsLoanPartialAndDelayService.get(orgId);
			newOrgId = partialAndDelay.getLoan().getId();
			break;
		case 20:
			partialAndDelay = nfsLoanPartialAndDelayService.get(orgId);
			newOrgId = partialAndDelay.getLoan().getId();
			break;
		case 23:
			partialAndDelay = nfsLoanPartialAndDelayService.get(orgId);
			newOrgId = partialAndDelay.getLoan().getId();
			break;
		case 24:
			partialAndDelay = nfsLoanPartialAndDelayService.get(orgId);
			newOrgId = partialAndDelay.getLoan().getId();
			break;
		case 28:
			partialAndDelay = nfsLoanPartialAndDelayService.get(orgId);
			newOrgId = partialAndDelay.getLoan().getId();
			break;
		case 29:
			partialAndDelay = nfsLoanPartialAndDelayService.get(orgId);
			newOrgId = partialAndDelay.getLoan().getId();
			break;
		case 32:
			partialAndDelay = nfsLoanPartialAndDelayService.get(orgId);
			newOrgId = partialAndDelay.getLoan().getId();
			break;
		case 33:
			partialAndDelay = nfsLoanPartialAndDelayService.get(orgId);
			newOrgId = partialAndDelay.getLoan().getId();
			break;
		case 82:
			partialAndDelay = nfsLoanPartialAndDelayService.get(orgId);
			newOrgId = partialAndDelay.getLoan().getId();
			break;
		case 83:
			partialAndDelay = nfsLoanPartialAndDelayService.get(orgId);
			newOrgId = partialAndDelay.getLoan().getId();
			break;
		case 84:
			partialAndDelay = nfsLoanPartialAndDelayService.get(orgId);
			newOrgId = partialAndDelay.getLoan().getId();
			break;
		case 85:
			partialAndDelay = nfsLoanPartialAndDelayService.get(orgId);
			newOrgId = partialAndDelay.getLoan().getId();
			break;
		case 86:
			partialAndDelay = nfsLoanPartialAndDelayService.get(orgId);
			newOrgId = partialAndDelay.getLoan().getId();
			break;
		default:
			newOrgId = orgId;
			break;
		}
		return newOrgId;
	}
	private Map<String, Object> getParamsByOtherId(int n, Long orgId) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> inMap = new HashMap<String, Object>();
		Map<String, Object> tradDetail = new HashMap<String,Object>();
		String jsonString = "";
		MemberFriendCa ca = null;
		Member member = null;
		MemberActTrx memberActTrx = null;
		NfsLoanApplyDetail applyDetail = null;
		NfsLoanApply apply = null;
		NfsLoanArbitration arbitration = null;
		NfsLoanRecord record = null;
		NfsLoanPartialAndDelay partialAndDelay = null;
		NfsCrAuction crAuction = null;
		switch(n) {
			case 37:
				NfsTransferRecord transferRecord = nfsTransferRecordService.get(orgId);
				inMap.put("name", transferRecord.getMember().getName());
				inMap.put("money", StringUtils.decimalToStr(transferRecord.getAmount(), 2));
				
				tradDetail.put("fee", StringUtils.decimalToStr(transferRecord.getAmount(), 2));
				tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
				tradDetail.put("type", "1");
				jsonString = JSON.toJSONString(tradDetail);
				
				map.put("member", transferRecord.getFriend());
				map.put("map", inMap);
				map.put("rmk", jsonString);
				break;
			case 38:
				ca = memberFriendCaService.get(orgId);
				member = memberService.get(ca.getMember().getId());
				inMap.put("name", member.getName());
				
				tradDetail.put("name", member.getName());
				tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
				tradDetail.put("status", "申请待确认");
				tradDetail.put("colorStatus", "1");
				tradDetail.put("type", "2");
				jsonString = JSON.toJSONString(tradDetail);
				
				map.put("member", ca.getFriend());
				map.put("map", inMap);
				map.put("rmk", jsonString);
				break;
			case 39:
				ca = memberFriendCaService.get(orgId);
				member = memberService.get(ca.getFriend().getId());
				inMap.put("name", member.getName());
				
				tradDetail.put("name", member.getName());
				tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
				tradDetail.put("status", "申请已同意");
				tradDetail.put("colorStatus", "0");
				tradDetail.put("type", "2");
				jsonString = JSON.toJSONString(tradDetail);
				
				map.put("member", ca.getMember());
				map.put("map", inMap);
				map.put("rmk", jsonString);
				break;
			case 40:
				ca = memberFriendCaService.get(orgId);
				member = memberService.get(ca.getFriend().getId());
				inMap.put("name", member.getName());
				
				tradDetail.put("name", member.getName());
				tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
				tradDetail.put("status", "申请被拒绝");
				tradDetail.put("colorStatus", "2");
				tradDetail.put("type", "2");
				jsonString = JSON.toJSONString(tradDetail);
				
				map.put("member", ca.getMember());
				map.put("map", inMap);
				map.put("rmk", jsonString);
				break;
			case 41:
				ca = memberFriendCaService.get(orgId);
				member = memberService.get(ca.getMember().getId());
				inMap.put("name", member.getName());
				
				tradDetail.put("name", member.getName());
				tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
				tradDetail.put("status", "待查看");
				tradDetail.put("colorStatus", "1");
				tradDetail.put("type", "2");
				jsonString = JSON.toJSONString(tradDetail);
				
				map.put("member", ca.getFriend());
				map.put("map", inMap);
				map.put("rmk", jsonString);
				break;
			case 42:
				ca = memberFriendCaService.get(orgId);
				member = memberService.get(ca.getMember().getId());
				inMap.put("name", member.getName());
				
				tradDetail.put("name", member.getName());
				tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
				tradDetail.put("status", "待支付");
				tradDetail.put("colorStatus", "1");
				tradDetail.put("type", "2");
				jsonString = JSON.toJSONString(tradDetail);
				
				map.put("member", ca.getFriend());
				map.put("map", inMap);
				map.put("rmk", jsonString);
				break;
			case 43:
				ca = memberFriendCaService.get(orgId);
				member = memberService.get(ca.getFriend().getId());
				inMap.put("name", member.getName());
				
				tradDetail.put("name", member.getName());
				tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
				tradDetail.put("status", "待查看");
				tradDetail.put("colorStatus", "1");
				tradDetail.put("type", "2");
				jsonString = JSON.toJSONString(tradDetail);
				
				map.put("member", ca.getMember());
				map.put("map", inMap);
				map.put("rmk", jsonString);
				break;
			case 62:
				crAuction = nfsCrAuctionService.get(orgId);
				record = nfsLoanRecord.get(crAuction.getLoanRecord());
				inMap.put("money", StringUtils.decimalToStr(crAuction.getCrSellPrice(),2));
				
				tradDetail.put("name", record.getLoanee().getName());
				tradDetail.put("loanAmount", StringUtils.decimalToStr(record.getAmount(), 2));
				tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
				tradDetail.put("status", "转让成功");
				tradDetail.put("colorStatus", "0");
				tradDetail.put("type", "0");
				jsonString = JSON.toJSONString(tradDetail);
				
				map.put("member", record.getLoaner());
				map.put("map", inMap);
				map.put("rmk", jsonString);
				break;
			case 63:
				crAuction = nfsCrAuctionService.get(orgId);
				record = nfsLoanRecord.get(crAuction.getLoanRecord());
				inMap.put("money", StringUtils.decimalToStr(record.getAmount(),2));
				
				tradDetail.put("name", record.getLoanee().getName());
				tradDetail.put("loanAmount", StringUtils.decimalToStr(record.getAmount(), 2));
				tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
				tradDetail.put("status", "转让失败");
				tradDetail.put("colorStatus", "2");
				tradDetail.put("type", "0");
				jsonString = JSON.toJSONString(tradDetail);
				
				map.put("member", record.getLoaner());
				map.put("map", inMap);
				map.put("rmk", jsonString);
				break;
			case 64:
				crAuction = nfsCrAuctionService.get(orgId);
				record = nfsLoanRecord.get(crAuction.getLoanRecord());
				inMap.put("money", StringUtils.decimalToStr(record.getAmount(),2));
				
				tradDetail.put("name", record.getLoanee().getName());
				tradDetail.put("loanAmount", StringUtils.decimalToStr(record.getAmount(), 2));
				tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
				tradDetail.put("status", "转让失败");
				tradDetail.put("colorStatus", "2");
				tradDetail.put("type", "0");
				jsonString = JSON.toJSONString(tradDetail);
				
				map.put("member", record.getLoaner());
				map.put("map", inMap);
				map.put("rmk", jsonString);
				break;
			case 65:
				crAuction = nfsCrAuctionService.get(orgId);
				record = nfsLoanRecord.get(crAuction.getLoanRecord());
				inMap.put("money", StringUtils.decimalToStr(record.getAmount(),2));
				
				tradDetail.put("name", record.getLoanee().getName());
				tradDetail.put("loanAmount", StringUtils.decimalToStr(record.getAmount(), 2));
				tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
				tradDetail.put("status", "审核失败");
				tradDetail.put("colorStatus", "2");
				tradDetail.put("type", "0");
				jsonString = JSON.toJSONString(tradDetail);
				
				map.put("member", record.getLoaner());
				map.put("map", inMap);
				map.put("rmk", jsonString);
				break;
			case 66:
				crAuction = nfsCrAuctionService.get(orgId);
				record = nfsLoanRecord.get(crAuction.getLoanRecord());
				tradDetail.put("name", record.getLoanee().getName());
				tradDetail.put("loanAmount", StringUtils.decimalToStr(record.getAmount(), 2));
				tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
				tradDetail.put("status", "借条已关闭");
				tradDetail.put("colorStatus", "2");
				tradDetail.put("type", "0");
				jsonString = JSON.toJSONString(tradDetail);
				
				map.put("member", crAuction.getCrBuyer());
				map.put("map", null);
				map.put("rmk", jsonString);
				break;
			case 68:
				memberActTrx = memberActTrxService.get(orgId);
				applyDetail = nfsLoanApplyDetail.get(memberActTrx.getOrgId());
				apply = loanApplyService.get(applyDetail.getApply());
				if(apply.getLoanRole().equals(LoanRole.loanee)) {
					tradDetail.put("name",applyDetail.getMember().getName());
					tradDetail.put("fee",StringUtils.decimalToStr(new BigDecimal(Global.getConfig("gxt.loanDoneFee")), 2));
					tradDetail.put("loanAmount",StringUtils.decimalToStr(applyDetail.getAmount(), 2));
					tradDetail.put("time", CalendarUtil.toCNStringWithoutYS(DateUtils.formatDate(new Date(), "yyyyMMddHHmmss")));
					tradDetail.put("status", "1");
					jsonString = JSON.toJSONString(tradDetail);
					
					map.put("member", apply.getMember());
					map.put("map", null);
					map.put("rmk", jsonString);
				}else {
					tradDetail.put("name",apply.getMember().getName());
					tradDetail.put("fee",StringUtils.decimalToStr(new BigDecimal(Global.getConfig("gxt.loanDoneFee")), 2));
					tradDetail.put("loanAmount",StringUtils.decimalToStr(applyDetail.getAmount(), 2));
					tradDetail.put("time", CalendarUtil.toCNStringWithoutYS(DateUtils.formatDate(new Date(), "yyyyMMddHHmmss")));
					tradDetail.put("status", "1");
					jsonString = JSON.toJSONString(tradDetail);
					
					map.put("member", applyDetail.getMember());
					map.put("map", null);
					map.put("rmk", jsonString);
				}
				break;
			case 69:
				memberActTrx = memberActTrxService.get(orgId);
				NfsRchgRecord rchgRecord = nfsRchgRecordService.get(memberActTrx.getOrgId());
				if(rchgRecord.getChannel().equals(NfsRchgRecord.Channel.gxt)) {
					tradDetail.put("time", CalendarUtil.toCNStringWithoutYS(DateUtils.formatDate(new Date(), "yyyyMMddHHmmss")));
					tradDetail.put("fee", StringUtils.decimalToStr(rchgRecord.getAmount(), 2));
					jsonString = JSON.toJSONString(tradDetail);
					map.put("groups", "gxtTransactionMessage");
				}else {
					tradDetail.put("time", CalendarUtil.toCNStringWithoutYS(DateUtils.formatDate(new Date(), "yyyyMMddHHmmss")));
					tradDetail.put("fee", StringUtils.decimalToStr(rchgRecord.getAmount(), 2));
					tradDetail.put("type", "1");
					jsonString = JSON.toJSONString(tradDetail);
					map.put("groups", "appTransactionMessage");
				}
				
				map.put("member", rchgRecord.getMember());
				map.put("map", null);
				map.put("rmk", jsonString);
				break;
			case 70:
				memberActTrx = memberActTrxService.get(orgId);
				NfsWdrlRecord wdrlRecord = wdrlRecordService.get(memberActTrx.getOrgId());
				inMap.put("bankName", wdrlRecord.getBankName());
				inMap.put("cardNo", StringUtils.substring(wdrlRecord.getCardNo(), wdrlRecord.getCardNo().length() - 4));
				
				if(wdrlRecord.getSource().equals(NfsWdrlRecord.Source.GXT)) {
					tradDetail.put("time", CalendarUtil.toCNStringWithoutYS(DateUtils.formatDate(new Date(), "yyyyMMddHHmmss")));
					tradDetail.put("fee", StringUtils.decimalToStr(wdrlRecord.getAmount(), 2));
					jsonString = JSON.toJSONString(tradDetail);
					map.put("groups", "gxtTransactionMessage");
				}else {
					tradDetail.put("time", CalendarUtil.toCNStringWithoutYS(DateUtils.formatDate(new Date(), "yyyyMMddHHmmss")));
					tradDetail.put("fee", StringUtils.decimalToStr(wdrlRecord.getAmount(), 2));
					tradDetail.put("type", "1");
					jsonString = JSON.toJSONString(tradDetail);
					map.put("groups", "appTransactionMessage");
				}
				
				map.put("member", wdrlRecord.getMember());
				map.put("map", inMap);
				map.put("rmk", jsonString);
				break;
			case 71:
				memberActTrx = memberActTrxService.get(orgId);
				applyDetail = nfsLoanApplyDetail.get(memberActTrx.getOrgId());
				apply = loanApplyService.get(applyDetail.getApply());
				if(apply.getLoanRole().equals(LoanRole.loanee)) {
					tradDetail.put("name",applyDetail.getMember().getName());
					tradDetail.put("fee",StringUtils.decimalToStr(new BigDecimal(Global.getConfig("gxt.loanDoneFee")), 2));
					tradDetail.put("loanAmount",StringUtils.decimalToStr(applyDetail.getAmount(), 2));
					tradDetail.put("time", CalendarUtil.toCNStringWithoutYS(DateUtils.formatDate(new Date(), "yyyyMMddHHmmss")));
					jsonString = JSON.toJSONString(tradDetail);
					
					map.put("member", apply.getMember());
					map.put("map", null);
					map.put("rmk", jsonString);
				}else {
					tradDetail.put("name",apply.getMember().getName());
					tradDetail.put("fee",StringUtils.decimalToStr(new BigDecimal(Global.getConfig("gxt.loanDoneFee")), 2));
					tradDetail.put("loanAmount",StringUtils.decimalToStr(applyDetail.getAmount(), 2));
					tradDetail.put("time", CalendarUtil.toCNStringWithoutYS(DateUtils.formatDate(new Date(), "yyyyMMddHHmmss")));
					jsonString = JSON.toJSONString(tradDetail);
					
					map.put("member", applyDetail.getMember());
					map.put("map", null);
					map.put("rmk", jsonString);
				}
				break;
			case 72:
				memberActTrx = memberActTrxService.get(orgId);
				arbitration = loanArbitrationService.get(memberActTrx.getOrgId());
				tradDetail.put("name",arbitration.getLoan().getLoaneeName());
				tradDetail.put("loanAmount",StringUtils.decimalToStr(arbitration.getLoan().getAmount(), 2));
				tradDetail.put("time", CalendarUtil.toCNStringWithoutYS(DateUtils.formatDate(new Date(), "yyyyMMddHHmmss")));
				tradDetail.put("status", "17");
				tradDetail.put("fee",StringUtils.decimalToStr(new BigDecimal(arbitration.getFee()), 2));
				jsonString = JSON.toJSONString(tradDetail);
				
				map.put("member", arbitration.getLoan().getLoaner());
				map.put("map", null);
				map.put("rmk", jsonString);
				break;
			case 73:
				memberActTrx = memberActTrxService.get(orgId);
				arbitration = loanArbitrationService.get(memberActTrx.getOrgId());
				if(arbitration.getLoan().getTrxType().equals(TrxType.offline)) {
					tradDetail.put("name", arbitration.getLoan().getLoaneeName());
					tradDetail.put("fee",StringUtils.decimalToStr(new BigDecimal(arbitration.getRefundFee()), 2));
					tradDetail.put("loanAmount",StringUtils.decimalToStr(arbitration.getApplyAmount(), 2));
					tradDetail.put("time",CalendarUtil.tonewCNString(DateUtils.formatDate(new Date(), "yyyyMMddHHmmss")));
					jsonString = JSON.toJSONString(tradDetail);
					
					map.put("member", arbitration.getLoan().getLoaner());
					map.put("map", null);
					map.put("rmk", jsonString);
				}else {
					tradDetail.put("name", arbitration.getLoan().getLoaneeName());
					tradDetail.put("fee",StringUtils.decimalToStr(new BigDecimal(arbitration.getRefundFee()), 2));
					tradDetail.put("loanAmount",StringUtils.decimalToStr(arbitration.getApplyAmount(), 2));
					tradDetail.put("time",CalendarUtil.tonewCNString(DateUtils.formatDate(new Date(), "yyyyMMddHHmmss")));
					tradDetail.put("type", "5");
					jsonString = JSON.toJSONString(tradDetail);
					
					map.put("member", arbitration.getLoan().getLoaner());
					map.put("map", null);
					map.put("rmk", jsonString);
				}
				break;
			case 110:
				memberActTrx = memberActTrxService.get(orgId);
				NfsLoanArbitrationExecution execution = nfsLoanArbitrationExecutionService.get(memberActTrx.getOrgId());
				record = nfsLoanRecord.get(execution.getLoan());
				tradDetail.put("name", record.getLoaneeName());
				tradDetail.put("fee",StringUtils.decimalToStr(execution.getFee(), 2));
				tradDetail.put("loanAmount",StringUtils.decimalToStr(record.getAmount(), 2));
				tradDetail.put("time",CalendarUtil.tonewCNString(DateUtils.formatDate(new Date(), "yyyyMMddHHmmss")));
				tradDetail.put("type", "5");
				
				map.put("member", record.getLoaner());
				map.put("map", null);
				map.put("rmk", jsonString);
				break;
			case 19:
				partialAndDelay = nfsLoanPartialAndDelayService.get(orgId);
				record = nfsLoanRecord.get(partialAndDelay.getLoan());
				if(partialAndDelay.getMemberRole().equals(LoanRole.loanee)) {
					inMap.put("name", record.getLoanee().getName());
					
					tradDetail.put("name", record.getLoanee().getName());
					tradDetail.put("loanAmount", StringUtils.decimalToStr(record.getAmount(), 2));
					tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
					tradDetail.put("status", "部分还款申请");
					tradDetail.put("colorStatus", "0");
					tradDetail.put("type", "0");
					jsonString = JSON.toJSONString(tradDetail);
					
					map.put("member", record.getLoaner());
					map.put("map", inMap);
					map.put("rmk", jsonString);
				}else {
					inMap.put("name", record.getLoaner().getName());
					
					tradDetail.put("name", record.getLoaner().getName());
					tradDetail.put("loanAmount", StringUtils.decimalToStr(record.getAmount(), 2));
					tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
					tradDetail.put("status", "部分还款申请");
					tradDetail.put("colorStatus", "0");
					tradDetail.put("type", "0");
					jsonString = JSON.toJSONString(tradDetail);
					
					map.put("member", record.getLoanee());
					map.put("map", inMap);
					map.put("rmk", jsonString);
				}
				break;
			case 20:
				partialAndDelay = nfsLoanPartialAndDelayService.get(orgId);
				record = nfsLoanRecord.get(partialAndDelay.getLoan());
				if(partialAndDelay.getMemberRole().equals(LoanRole.loanee)) {
					inMap.put("name", record.getLoanee().getName());
					
					tradDetail.put("name", record.getLoanee().getName());
					tradDetail.put("loanAmount", StringUtils.decimalToStr(record.getAmount(), 2));
					tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
					tradDetail.put("status", "取消部分还款申请");
					tradDetail.put("colorStatus", "2");
					tradDetail.put("type", "0");
					jsonString = JSON.toJSONString(tradDetail);
					
					map.put("member", record.getLoaner());
					map.put("map", inMap);
					map.put("rmk", jsonString);
				}else {
					inMap.put("name", record.getLoaner().getName());
					
					tradDetail.put("name", record.getLoaner().getName());
					tradDetail.put("loanAmount", StringUtils.decimalToStr(record.getAmount(), 2));
					tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
					tradDetail.put("status", "取消部分还款申请");
					tradDetail.put("colorStatus", "2");
					tradDetail.put("type", "0");
					jsonString = JSON.toJSONString(tradDetail);
					
					map.put("member", record.getLoanee());
					map.put("map", inMap);
					map.put("rmk", jsonString);
				}
				break;
			case 23:
				partialAndDelay = nfsLoanPartialAndDelayService.get(orgId);
				record = nfsLoanRecord.get(partialAndDelay.getLoan());
				if(partialAndDelay.getMemberRole().equals(LoanRole.loanee)) {
					inMap.put("name", record.getLoaner().getName());
					
					tradDetail.put("name", record.getLoaner().getName());
					tradDetail.put("loanAmount", StringUtils.decimalToStr(record.getAmount(), 2));
					tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
					tradDetail.put("status", "确认部分还款申请");
					tradDetail.put("colorStatus", "0");
					tradDetail.put("type", "0");
					jsonString = JSON.toJSONString(tradDetail);
					
					map.put("member", record.getLoanee());
					map.put("map", inMap);
					map.put("rmk", jsonString);
				}else {
					inMap.put("name", record.getLoanee().getName());
					
					tradDetail.put("name", record.getLoanee().getName());
					tradDetail.put("loanAmount", StringUtils.decimalToStr(record.getAmount(), 2));
					tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
					tradDetail.put("status", "确认部分还款申请");
					tradDetail.put("colorStatus", "0");
					tradDetail.put("type", "0");
					jsonString = JSON.toJSONString(tradDetail);
					
					map.put("member", record.getLoaner());
					map.put("map", inMap);
					map.put("rmk", jsonString);
				}
				break;
			case 24:
				partialAndDelay = nfsLoanPartialAndDelayService.get(orgId);
				record = nfsLoanRecord.get(partialAndDelay.getLoan());
				if(partialAndDelay.getMemberRole().equals(LoanRole.loanee)) {
					inMap.put("name", record.getLoaner().getName());
					
					tradDetail.put("name", record.getLoaner().getName());
					tradDetail.put("loanAmount", StringUtils.decimalToStr(record.getAmount(), 2));
					tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
					tradDetail.put("status", "拒绝部分还款申请");
					tradDetail.put("colorStatus", "2");
					tradDetail.put("type", "0");
					jsonString = JSON.toJSONString(tradDetail);
					
					map.put("member", record.getLoanee());
					map.put("map", inMap);
					map.put("rmk", jsonString);
				}else {
					inMap.put("name", record.getLoanee().getName());
					
					tradDetail.put("name", record.getLoanee().getName());
					tradDetail.put("loanAmount", StringUtils.decimalToStr(record.getAmount(), 2));
					tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
					tradDetail.put("status", "拒绝部分还款申请");
					tradDetail.put("colorStatus", "2");
					tradDetail.put("type", "0");
					jsonString = JSON.toJSONString(tradDetail);
					
					map.put("member", record.getLoaner());
					map.put("map", inMap);
					map.put("rmk", jsonString);
				}
				break;
			case 28:
				partialAndDelay = nfsLoanPartialAndDelayService.get(orgId);
				record = nfsLoanRecord.get(partialAndDelay.getLoan());
				if(partialAndDelay.getMemberRole().equals(LoanRole.loanee)) {
					inMap.put("name", record.getLoanee().getName());
					
					tradDetail.put("name", record.getLoanee().getName());
					tradDetail.put("loanAmount", StringUtils.decimalToStr(record.getAmount(), 2));
					tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
					tradDetail.put("status", "延期待确认");
					tradDetail.put("colorStatus", "1");
					tradDetail.put("type", "0");
					jsonString = JSON.toJSONString(tradDetail);
					
					map.put("member", record.getLoaner());
					map.put("map", inMap);
					map.put("rmk", jsonString);
				}else {
					inMap.put("name", record.getLoaner().getName());
					
					tradDetail.put("name", record.getLoaner().getName());
					tradDetail.put("loanAmount", StringUtils.decimalToStr(record.getAmount(), 2));
					tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
					tradDetail.put("status", "延期待确认");
					tradDetail.put("colorStatus", "1");
					tradDetail.put("type", "0");
					jsonString = JSON.toJSONString(tradDetail);
					
					map.put("member", record.getLoanee());
					map.put("map", inMap);
					map.put("rmk", jsonString);
				}
				break;
			case 29:
				partialAndDelay = nfsLoanPartialAndDelayService.get(orgId);
				record = nfsLoanRecord.get(partialAndDelay.getLoan());
				if(partialAndDelay.getMemberRole().equals(LoanRole.loanee)) {
					inMap.put("name", record.getLoanee().getName());
					
					tradDetail.put("name", record.getLoanee().getName());
					tradDetail.put("loanAmount", StringUtils.decimalToStr(record.getAmount(), 2));
					tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
					tradDetail.put("status", "延期失败");
					tradDetail.put("colorStatus", "2");
					tradDetail.put("type", "0");
					jsonString = JSON.toJSONString(tradDetail);
					
					map.put("member", record.getLoaner());
					map.put("map", inMap);
					map.put("rmk", jsonString);
				}else {
					inMap.put("name", record.getLoaner().getName());
					
					tradDetail.put("name", record.getLoaner().getName());
					tradDetail.put("loanAmount", StringUtils.decimalToStr(record.getAmount(), 2));
					tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
					tradDetail.put("status", "延期失败");
					tradDetail.put("colorStatus", "2");
					tradDetail.put("type", "0");
					jsonString = JSON.toJSONString(tradDetail);
					
					map.put("member", record.getLoanee());
					map.put("map", inMap);
					map.put("rmk", jsonString);
				}
				break;
			case 32:
				partialAndDelay = nfsLoanPartialAndDelayService.get(orgId);
				record = nfsLoanRecord.get(partialAndDelay.getLoan());
				if(partialAndDelay.getMemberRole().equals(LoanRole.loanee)) {
					inMap.put("name", record.getLoaner().getName());
					
					tradDetail.put("name", record.getLoaner().getName());
					tradDetail.put("loanAmount", StringUtils.decimalToStr(record.getAmount(), 2));
					tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
					tradDetail.put("status", "延期成功");
					tradDetail.put("colorStatus", "0");
					tradDetail.put("type", "0");
					jsonString = JSON.toJSONString(tradDetail);
					
					map.put("member", record.getLoanee());
					map.put("map", inMap);
					map.put("rmk", jsonString);
				}else {
					inMap.put("name", record.getLoanee().getName());
					
					tradDetail.put("name", record.getLoanee().getName());
					tradDetail.put("loanAmount", StringUtils.decimalToStr(record.getAmount(), 2));
					tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
					tradDetail.put("status", "延期成功");
					tradDetail.put("colorStatus", "0");
					tradDetail.put("type", "0");
					jsonString = JSON.toJSONString(tradDetail);
					
					map.put("member", record.getLoaner());
					map.put("map", inMap);
					map.put("rmk", jsonString);
				}
				break;
			case 33:
				partialAndDelay = nfsLoanPartialAndDelayService.get(orgId);
				record = nfsLoanRecord.get(partialAndDelay.getLoan());
				if(partialAndDelay.getMemberRole().equals(LoanRole.loanee)) {
					inMap.put("name", record.getLoaner().getName());
					
					tradDetail.put("name", record.getLoaner().getName());
					tradDetail.put("loanAmount", StringUtils.decimalToStr(record.getAmount(), 2));
					tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
					tradDetail.put("status", "延期失败");
					tradDetail.put("colorStatus", "2");
					tradDetail.put("type", "0");
					jsonString = JSON.toJSONString(tradDetail);
					
					map.put("member", record.getLoanee());
					map.put("map", inMap);
					map.put("rmk", jsonString);
				}else {
					inMap.put("name", record.getLoanee().getName());
					
					tradDetail.put("name", record.getLoanee().getName());
					tradDetail.put("loanAmount", StringUtils.decimalToStr(record.getAmount(), 2));
					tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
					tradDetail.put("status", "延期失败");
					tradDetail.put("colorStatus", "2");
					tradDetail.put("type", "0");
					jsonString = JSON.toJSONString(tradDetail);
					
					map.put("member", record.getLoaner());
					map.put("map", inMap);
					map.put("rmk", jsonString);
				}
				break;
			case 82:
				partialAndDelay = nfsLoanPartialAndDelayService.get(orgId);
				record = nfsLoanRecord.get(partialAndDelay.getLoan());
				if(partialAndDelay.getMemberRole().equals(LoanRole.loanee)) {
					tradDetail.put("name", record.getLoaner().getName());
					tradDetail.put("loanAmount", StringUtils.decimalToStr(record.getAmount(), 2));
					tradDetail.put("status","4");
					tradDetail.put("time",DateUtils.formatDateForMessage(new Date()));
					jsonString = JSON.toJSONString(tradDetail);
					
					map.put("member", record.getLoanee());
					map.put("map", null);
					map.put("rmk", jsonString);
				}else {
					tradDetail.put("name", record.getLoanee().getName());
					tradDetail.put("loanAmount", StringUtils.decimalToStr(record.getAmount(), 2));
					tradDetail.put("status","4");
					tradDetail.put("time",DateUtils.formatDateForMessage(new Date()));
					jsonString = JSON.toJSONString(tradDetail);
					
					map.put("member", record.getLoaner());
					map.put("map", null);
					map.put("rmk", jsonString);
				}
				break;
			case 83:
				partialAndDelay = nfsLoanPartialAndDelayService.get(orgId);
				record = nfsLoanRecord.get(partialAndDelay.getLoan());
				if(partialAndDelay.getMemberRole().equals(LoanRole.loanee)) {
					tradDetail.put("name", record.getLoanee().getName());
					tradDetail.put("loanAmount", StringUtils.decimalToStr(record.getAmount(), 2));
					tradDetail.put("status","4");
					tradDetail.put("time",DateUtils.formatDateForMessage(new Date()));
					jsonString = JSON.toJSONString(tradDetail);
					
					map.put("member", record.getLoaner());
					map.put("map", null);
					map.put("rmk", jsonString);
				}else {
					tradDetail.put("name", record.getLoaner().getName());
					tradDetail.put("loanAmount", StringUtils.decimalToStr(record.getAmount(), 2));
					tradDetail.put("status","4");
					tradDetail.put("time",DateUtils.formatDateForMessage(new Date()));
					jsonString = JSON.toJSONString(tradDetail);
					
					map.put("member", record.getLoanee());
					map.put("map", null);
					map.put("rmk", jsonString);
				}
				break;
			case 84:
				partialAndDelay = nfsLoanPartialAndDelayService.get(orgId);
				record = nfsLoanRecord.get(partialAndDelay.getLoan());
				if(partialAndDelay.getMemberRole().equals(LoanRole.loanee)) {
					tradDetail.put("name", record.getLoanee().getName());
					tradDetail.put("loanAmount", StringUtils.decimalToStr(record.getAmount(), 2));
					tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
					tradDetail.put("status", 5);
					jsonString = JSON.toJSONString(tradDetail);
					
					map.put("member", record.getLoaner());
					map.put("map", null);
					map.put("rmk", jsonString);
				}else {
					tradDetail.put("name", record.getLoaner().getName());
					tradDetail.put("loanAmount", StringUtils.decimalToStr(record.getAmount(), 2));
					tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
					tradDetail.put("status", 5);
					jsonString = JSON.toJSONString(tradDetail);
					
					map.put("member", record.getLoanee());
					map.put("map", null);
					map.put("rmk", jsonString);
				}
				break;
			case 85:
				partialAndDelay = nfsLoanPartialAndDelayService.get(orgId);
				record = nfsLoanRecord.get(partialAndDelay.getLoan());
				if(partialAndDelay.getMemberRole().equals(LoanRole.loanee)) {
					tradDetail.put("name", record.getLoaner().getName());
					tradDetail.put("loanAmount", StringUtils.decimalToStr(record.getAmount(), 2));
					tradDetail.put("status", "5");
					tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
					jsonString = JSON.toJSONString(tradDetail);
					
					map.put("member", record.getLoanee());
					map.put("map", null);
					map.put("rmk", jsonString);
				}else {
					tradDetail.put("name", record.getLoanee().getName());
					tradDetail.put("loanAmount", StringUtils.decimalToStr(record.getAmount(), 2));
					tradDetail.put("status", "5");
					tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
					jsonString = JSON.toJSONString(tradDetail);
					
					map.put("member", record.getLoaner());
					map.put("map", null);
					map.put("rmk", jsonString);
				}
				break;
			case 86:
				partialAndDelay = nfsLoanPartialAndDelayService.get(orgId);
				record = nfsLoanRecord.get(partialAndDelay.getLoan());
				if(partialAndDelay.getMemberRole().equals(LoanRole.loanee)) {
					tradDetail.put("name", record.getLoaner().getName());
					tradDetail.put("loanAmount", StringUtils.decimalToStr(record.getAmount(), 2));
					tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
					tradDetail.put("status", "6");
					jsonString = JSON.toJSONString(tradDetail);
					
					map.put("member", record.getLoanee());
					map.put("map", null);
					map.put("rmk", jsonString);
				}else {
					tradDetail.put("name", record.getLoanee().getName());
					tradDetail.put("loanAmount", StringUtils.decimalToStr(record.getAmount(), 2));
					tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
					tradDetail.put("status", "6");
					jsonString = JSON.toJSONString(tradDetail);
					
					map.put("member", record.getLoaner());
					map.put("map", null);
					map.put("rmk", jsonString);
				}
				break;
			default:
				break;
		}
		return map;
	}

	private Map<String, Object> getParamsByRecordId(int n, Long orgId) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> inMap = new HashMap<String, Object>();
		Map<String, Object> tradDetail = new HashMap<String,Object>();
		String jsonString = "";
		NfsLoanRecord record = nfsLoanRecord.get(orgId);
		Member loaner = record.getLoaner();
		Member loanee = record.getLoanee();
		String loanerName = loaner.getName();
		String loaneeName = loanee.getName();
		NfsLoanArbitration arbitration = null;
		NfsLoanArbitrationExecution execution = null;
		switch(n) {
			case 8:
				inMap.put("name", loanerName);
				
				tradDetail.put("name", loanerName);
				tradDetail.put("loanAmount", StringUtils.decimalToStr(record.getAmount(), 2));
				tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
				tradDetail.put("status", "视频通过");
				tradDetail.put("colorStatus", "0");
				tradDetail.put("type", "0");
				jsonString = JSON.toJSONString(tradDetail);
				
				map.put("member", loanee);
				map.put("map", inMap);
				map.put("rmk", jsonString);
				break;
			case 9:
				inMap.put("name", loanerName);
				
				tradDetail.put("name", loaner.getName());
				tradDetail.put("loanAmount", StringUtils.decimalToStr(record.getAmount(), 2));
				tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
				tradDetail.put("status", "借条已达成");
				tradDetail.put("colorStatus", "2");
				tradDetail.put("type", "0");
				jsonString = JSON.toJSONString(tradDetail);
				
				map.put("member", loanee);
				map.put("map", inMap);
				map.put("rmk", jsonString);
				break;
			case 15:
				inMap.put("name", loaneeName);
				
				tradDetail.put("name", loaneeName);
				tradDetail.put("loanAmount", StringUtils.decimalToStr(record.getAmount(), 2));
				tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
				tradDetail.put("status", "借条已达成");
				tradDetail.put("colorStatus", "2");
				tradDetail.put("type", "0");
				jsonString = JSON.toJSONString(tradDetail);
				
				map.put("member", loaner);
				map.put("map", inMap);
				map.put("rmk", jsonString);
				break;
			case 16:
				inMap.put("name", loaneeName);
				
				tradDetail.put("name", loaneeName);
				tradDetail.put("loanAmount", StringUtils.decimalToStr(record.getAmount(), 2));
				tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
				tradDetail.put("status", "借条已关闭");
				tradDetail.put("colorStatus", "2");
				tradDetail.put("type", "0");
				jsonString = JSON.toJSONString(tradDetail);
				
				map.put("member", loaner);
				map.put("map", inMap);
				map.put("rmk", jsonString);
				break;
			case 17:
				inMap.put("name", loaneeName);
				
				tradDetail.put("name", loaneeName);
				tradDetail.put("loanAmount", StringUtils.decimalToStr(record.getAmount(), 2));
				tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
				tradDetail.put("status", "收款待确认");
				tradDetail.put("colorStatus", "1");
				tradDetail.put("type", "0");
				jsonString = JSON.toJSONString(tradDetail);
				
				map.put("member", loaner);
				map.put("map", inMap);
				map.put("rmk", jsonString);
				break;
			case 18:
				inMap.put("name", loanerName);
				
				tradDetail.put("name", loanerName);
				tradDetail.put("loanAmount", StringUtils.decimalToStr(record.getAmount(), 2));
				tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
				tradDetail.put("status", "还款失败");
				tradDetail.put("colorStatus", "2");
				tradDetail.put("type", "0");
				jsonString = JSON.toJSONString(tradDetail);
				
				map.put("member", loanee);
				map.put("map", inMap);
				map.put("rmk", jsonString);
				break;
			case 21:
				inMap.put("name", loanerName);
				
				tradDetail.put("name", loanerName);
				tradDetail.put("loanAmount", StringUtils.decimalToStr(record.getAmount(), 2));
				tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
				tradDetail.put("status", "部分还款提醒");
				tradDetail.put("colorStatus", "0");
				tradDetail.put("type", "0");
				jsonString = JSON.toJSONString(tradDetail);
				
				map.put("member", loanee);
				map.put("map", inMap);
				map.put("rmk", jsonString);
				break;
			case 22:
				inMap.put("name", loaneeName);
				
				tradDetail.put("name", loaneeName);
				tradDetail.put("loanAmount", StringUtils.decimalToStr(record.getAmount(), 2));
				tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
				tradDetail.put("status", "部分还款提醒");
				tradDetail.put("colorStatus", "0");
				tradDetail.put("type", "0");
				jsonString = JSON.toJSONString(tradDetail);
				
				map.put("member", loaner);
				map.put("map", inMap);
				map.put("rmk", jsonString);
				break;
			case 26:
				inMap.put("name", loanerName);
				
				tradDetail.put("name", loanerName);
				tradDetail.put("loanAmount", StringUtils.decimalToStr(record.getAmount(), 2));
				tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
				tradDetail.put("status", "借条已完成");
				tradDetail.put("colorStatus", "2");
				tradDetail.put("type", "0");
				jsonString = JSON.toJSONString(tradDetail);
				
				map.put("member", loanee);
				map.put("map", inMap);
				map.put("rmk", jsonString);
				break;
			case 27://无此调用
				break;
			case 30:
				inMap.put("name", loanerName);
				
				tradDetail.put("name", loanerName);
				tradDetail.put("loanAmount", StringUtils.decimalToStr(record.getAmount(), 2));
				tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
				tradDetail.put("status", "延期提醒");
				tradDetail.put("colorStatus", "0");
				tradDetail.put("type", "0");
				jsonString = JSON.toJSONString(tradDetail);
				
				map.put("member", loanee);
				map.put("map", inMap);
				map.put("rmk", jsonString);
				break;
			case 31:
				inMap.put("name", loaneeName);
				
				tradDetail.put("name",loaneeName);
				tradDetail.put("loanAmount", StringUtils.decimalToStr(record.getAmount(), 2));
				tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
				tradDetail.put("status", "延期提醒");
				tradDetail.put("colorStatus", "0");
				tradDetail.put("type", "0");
				jsonString = JSON.toJSONString(tradDetail);
				
				map.put("member", loaner);
				map.put("map", inMap);
				map.put("rmk", jsonString);
				break;
			case 44:
				arbitration = nfsLoanArbitrationService.getByLoanId(orgId);
				tradDetail.put("name",loanerName);
				tradDetail.put("loanAmount",StringUtils.decimalToStr(arbitration.getApplyAmount(), 2));
				tradDetail.put("time", CalendarUtil.toCNStringWithoutYS(DateUtils.formatDate(new Date(), "yyyyMMddHHmmss")));
				tradDetail.put("status", "仲裁待受理");
				tradDetail.put("colorStatus", "1");
				tradDetail.put("type", "3");
				jsonString = JSON.toJSONString(tradDetail);
				
				map.put("member", loanee);
				map.put("map", null);
				map.put("rmk", jsonString);
				break;
			case 45:
				arbitration = nfsLoanArbitrationService.getByLoanId(orgId);
				tradDetail.put("name",loaneeName);
				tradDetail.put("loanAmount",StringUtils.decimalToStr(arbitration.getApplyAmount(), 2));
				tradDetail.put("time", CalendarUtil.toCNStringWithoutYS(DateUtils.formatDate(new Date(), "yyyyMMddHHmmss")));
				tradDetail.put("status", "仲裁中");
				tradDetail.put("colorStatus", "0");
				tradDetail.put("type", "3");
				jsonString = JSON.toJSONString(tradDetail);
				
				map.put("member", loaner);
				map.put("map", null);
				map.put("rmk", jsonString);
				break;
			case 46:
				arbitration = nfsLoanArbitrationService.getByLoanId(orgId);
				tradDetail.put("name",loanerName);
				tradDetail.put("loanAmount",StringUtils.decimalToStr(arbitration.getApplyAmount(), 2));
				tradDetail.put("time", CalendarUtil.toCNStringWithoutYS(DateUtils.formatDate(new Date(), "yyyyMMddHHmmss")));
				tradDetail.put("status", "仲裁中");
				tradDetail.put("colorStatus", "0");
				tradDetail.put("type", "3");
				jsonString = JSON.toJSONString(tradDetail);
				
				map.put("member", loanee);
				map.put("map", null);
				map.put("rmk", jsonString);
				break;
			case 47:
				arbitration = nfsLoanArbitrationService.getByLoanId(orgId);
				tradDetail.put("name",loaneeName);
				tradDetail.put("loanAmount",StringUtils.decimalToStr(arbitration.getApplyAmount(), 2));
				tradDetail.put("time", CalendarUtil.toCNStringWithoutYS(DateUtils.formatDate(new Date(), "yyyyMMddHHmmss")));
				tradDetail.put("status", "仲裁出裁决");
				tradDetail.put("colorStatus", "0");
				tradDetail.put("type", "3");
				jsonString = JSON.toJSONString(tradDetail);
				
				map.put("member", loaner);
				map.put("map", null);
				map.put("rmk", jsonString);
				break;
			case 48:
				arbitration = nfsLoanArbitrationService.getByLoanId(orgId);
				tradDetail.put("name",loaneeName);
				tradDetail.put("loanAmount",StringUtils.decimalToStr(arbitration.getApplyAmount(), 2));
				tradDetail.put("time", CalendarUtil.toCNStringWithoutYS(DateUtils.formatDate(new Date(), "yyyyMMddHHmmss")));
				tradDetail.put("status", "等待组庭");
				tradDetail.put("colorStatus", "1");
				tradDetail.put("type", "3");
				jsonString = JSON.toJSONString(tradDetail);
				
				map.put("member", loaner);
				map.put("map", null);
				map.put("rmk", jsonString);
				break;
			case 49:
				arbitration = nfsLoanArbitrationService.getByLoanId(orgId);
				tradDetail.put("name",loaneeName);
				tradDetail.put("loanAmount",StringUtils.decimalToStr(arbitration.getApplyAmount(), 2));
				tradDetail.put("time", CalendarUtil.toCNStringWithoutYS(DateUtils.formatDate(new Date(), "yyyyMMddHHmmss")));
				tradDetail.put("status", "强执已受理");
				tradDetail.put("colorStatus", "1");
				tradDetail.put("type", "4");
				jsonString = JSON.toJSONString(tradDetail);
				
				map.put("member", loaner);
				map.put("map", null);
				map.put("rmk", jsonString);
				break;
			case 50:
				arbitration = nfsLoanArbitrationService.getByLoanId(orgId);
				execution = nfsLoanArbitrationExecutionService.findByArbitrationId(arbitration.getId());
				tradDetail.put("name",loaneeName);
				tradDetail.put("loanAmount",StringUtils.decimalToStr(execution.getDueRepayAmount(), 2));
				tradDetail.put("time", CalendarUtil.toCNStringWithoutYS(DateUtils.formatDate(new Date(), "yyyyMMddHHmmss")));
				tradDetail.put("status", "强执缴费中");
				tradDetail.put("colorStatus", "0");
				tradDetail.put("type", "4");
				jsonString = JSON.toJSONString(tradDetail);
				
				map.put("member", loaner);
				map.put("map", null);
				map.put("rmk", jsonString);
				break;
			case 51:
				arbitration = nfsLoanArbitrationService.getByLoanId(orgId);
				execution = nfsLoanArbitrationExecutionService.findByArbitrationId(arbitration.getId());
				tradDetail.put("name",loaneeName);
				tradDetail.put("loanAmount",StringUtils.decimalToStr(execution.getDueRepayAmount(), 2));
				tradDetail.put("time", CalendarUtil.toCNStringWithoutYS(DateUtils.formatDate(new Date(), "yyyyMMddHHmmss")));
				tradDetail.put("status", "强执缴费中");
				tradDetail.put("colorStatus", "0");
				tradDetail.put("type", "4");
				jsonString = JSON.toJSONString(tradDetail);
				
				map.put("member", loaner);
				map.put("map", null);
				map.put("rmk", jsonString);
				break;
			case 52:
				arbitration = nfsLoanArbitrationService.getByLoanId(orgId);
				execution = nfsLoanArbitrationExecutionService.findByArbitrationId(arbitration.getId());
				tradDetail.put("name",loaneeName);
				tradDetail.put("loanAmount",StringUtils.decimalToStr(execution.getDueRepayAmount(), 2));
				tradDetail.put("time", CalendarUtil.toCNStringWithoutYS(DateUtils.formatDate(new Date(), "yyyyMMddHHmmss")));
				tradDetail.put("status", "强执已结束");
				tradDetail.put("colorStatus", "2");
				tradDetail.put("type", "4");
				jsonString = JSON.toJSONString(tradDetail);
				
				map.put("member", loaner);
				map.put("map", null);
				map.put("rmk", jsonString);
				break;
			case 53:
				arbitration = nfsLoanArbitrationService.getByLoanId(orgId);
				execution = nfsLoanArbitrationExecutionService.findByArbitrationId(arbitration.getId());
				tradDetail.put("name",loaneeName);
				tradDetail.put("loanAmount",StringUtils.decimalToStr(execution.getDueRepayAmount(), 2));
				tradDetail.put("time", CalendarUtil.toCNStringWithoutYS(DateUtils.formatDate(new Date(), "yyyyMMddHHmmss")));
				tradDetail.put("status", "强执已失效");
				tradDetail.put("colorStatus", "2");
				tradDetail.put("type", "4");
				jsonString = JSON.toJSONString(tradDetail);
				
				map.put("member", loaner);
				map.put("map", null);
				map.put("rmk", jsonString);
				break;
			case 54:
				tradDetail.put("name", loanerName);
				tradDetail.put("loanAmount", StringUtils.decimalToStr(record.getAmount(), 2));
				tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
				tradDetail.put("status", "还有3天到期");
				tradDetail.put("colorStatus", "0");
				tradDetail.put("type", "0");
				jsonString = JSON.toJSONString(tradDetail);
				
				map.put("member", loanee);
				map.put("map", null);
				map.put("rmk", jsonString);
				break;
			case 55:
				tradDetail.put("name", loanerName);
				tradDetail.put("loanAmount", StringUtils.decimalToStr(record.getAmount(), 2));
				tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
				tradDetail.put("status", "今日还款");
				tradDetail.put("colorStatus", "0");
				tradDetail.put("type", "0");
				jsonString = JSON.toJSONString(tradDetail);
				
				map.put("member", loanee);
				map.put("map", null);
				map.put("rmk", jsonString);
				break;
			case 56:
				tradDetail.put("name", loanerName);
				tradDetail.put("loanAmount", StringUtils.decimalToStr(record.getAmount(), 2));
				tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
				tradDetail.put("status", "逾期1天");
				tradDetail.put("colorStatus", "0");
				tradDetail.put("type", "0");
				jsonString = JSON.toJSONString(tradDetail);
				
				map.put("member", loanee);
				map.put("map", null);
				map.put("rmk", jsonString);
				break;
			case 57:
				tradDetail.put("name", loaneeName);
				tradDetail.put("loanAmount", StringUtils.decimalToStr(record.getAmount(), 2));
				tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
				tradDetail.put("status", "逾期3天");
				tradDetail.put("colorStatus", "0");
				tradDetail.put("type", "0");
				jsonString = JSON.toJSONString(tradDetail);
				
				map.put("member", loaner);
				map.put("map", null);
				map.put("rmk", jsonString);
				break;
			case 58:
				tradDetail.put("name", loanerName);
				tradDetail.put("loanAmount", StringUtils.decimalToStr(record.getAmount(), 2));
				tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
				tradDetail.put("status", "逾期7天");
				tradDetail.put("colorStatus", "0");
				tradDetail.put("type", "0");
				jsonString = JSON.toJSONString(tradDetail);
				
				map.put("member", loanee);
				map.put("map", null);
				map.put("rmk", jsonString);
				break;
			case 59:
				inMap.put("name", loaneeName);
				
				tradDetail.put("name", loaneeName);
				tradDetail.put("loanAmount", StringUtils.decimalToStr(record.getAmount(), 2));
				tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
				tradDetail.put("status", "逾期15天");
				tradDetail.put("colorStatus", "0");
				tradDetail.put("type", "0");
				jsonString = JSON.toJSONString(tradDetail);
				
				map.put("member", loaner);
				map.put("map", inMap);
				map.put("rmk", jsonString);
				break;
			case 60:
				inMap.put("name", loanerName);
				inMap.put("money", StringUtils.decimalToStr(record.getAmount(),2));
				
				tradDetail.put("name", loanerName);
				tradDetail.put("loanAmount", StringUtils.decimalToStr(record.getAmount(), 2));
				tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
				tradDetail.put("status", "审核中");
				tradDetail.put("colorStatus", "0");
				tradDetail.put("type", "0");
				jsonString = JSON.toJSONString(tradDetail);
				
				map.put("member", loanee);
				map.put("map", inMap);
				map.put("rmk", jsonString);
				break;
			case 61:
				Member buyer = nfsCrAuctionService.getCrBuyer(record);
				inMap.put("money", StringUtils.decimalToStr(record.getAmount(),2));
				inMap.put("name",loanerName);
				inMap.put("newLoaner",buyer.getName());
				
				tradDetail.put("name", loanerName);
				tradDetail.put("loanAmount", StringUtils.decimalToStr(record.getAmount(), 2));
				tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
				tradDetail.put("status", "转让成功");
				tradDetail.put("colorStatus", "0");
				tradDetail.put("type", "0");
				jsonString = JSON.toJSONString(tradDetail);
				
				map.put("member", loanee);
				map.put("map", inMap);
				map.put("rmk", jsonString);
				break;
			case 80:
				tradDetail.put("name", loaneeName);
				tradDetail.put("loanAmount", StringUtils.decimalToStr(record.getAmount(), 2));
				tradDetail.put("status", "3");
				tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
				jsonString = JSON.toJSONString(tradDetail);
				
				map.put("member", loaner);
				map.put("map", null);
				map.put("rmk", jsonString);
				break;
			case 81:
				tradDetail.put("name", loanerName);
				tradDetail.put("loanAmount", StringUtils.decimalToStr(record.getAmount(), 2));
				tradDetail.put("status", "3");
				tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
				jsonString = JSON.toJSONString(tradDetail);
				
				map.put("member", loanee);
				map.put("map", null);
				map.put("rmk", jsonString);
				break;
			case 87:
				tradDetail.put("name", loaneeName);
				tradDetail.put("loanAmount", StringUtils.decimalToStr(record.getAmount(), 2));
				tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
				tradDetail.put("status", 7);
				jsonString = JSON.toJSONString(tradDetail);
				
				map.put("member", loaner);
				map.put("map", null);
				map.put("rmk", jsonString);
				break;
			case 88:
				tradDetail.put("name", loanerName);
				tradDetail.put("loanAmount", StringUtils.decimalToStr(record.getAmount(), 2));
				tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
				tradDetail.put("status", 8);
				jsonString = JSON.toJSONString(tradDetail);
				
				map.put("member", loanee);
				map.put("map", null);
				map.put("rmk", jsonString);
				break;
			case 89:
				tradDetail.put("name", loaneeName);
				tradDetail.put("loanAmount", StringUtils.decimalToStr(record.getAmount(), 2));
				tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
				tradDetail.put("status", 9);
				jsonString = JSON.toJSONString(tradDetail);
				
				map.put("member", loaner);
				map.put("map", null);
				map.put("rmk", jsonString);
				break;
			case 90:
				tradDetail.put("name", loanerName);
				tradDetail.put("loanAmount", StringUtils.decimalToStr(record.getAmount(), 2));
				tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
				tradDetail.put("status", "10");
				jsonString = JSON.toJSONString(tradDetail);
				
				map.put("member", loanee);
				map.put("map", null);
				map.put("rmk", jsonString);
				break;
			case 91:
				tradDetail.put("name", loanerName);
				tradDetail.put("loanAmount", StringUtils.decimalToStr(record.getAmount(), 2));
				tradDetail.put("status", "11");
				tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
				jsonString = JSON.toJSONString(tradDetail);
				
				map.put("member", loanee);
				map.put("map", null);
				map.put("rmk", jsonString);
				break;
			case 92:
				tradDetail.put("name", loanerName);
				tradDetail.put("loanAmount", StringUtils.decimalToStr(record.getAmount(), 2));
				tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
				tradDetail.put("status", 11);
				jsonString = JSON.toJSONString(tradDetail);
				
				map.put("member", loanee);
				map.put("map", null);
				map.put("rmk", jsonString);
				break;
			case 93:
				tradDetail.put("name", loanerName);
				tradDetail.put("loanAmount", StringUtils.decimalToStr(record.getAmount(), 2));
				tradDetail.put("status", 13);
				tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
				jsonString = JSON.toJSONString(tradDetail);
				
				map.put("member", loanee);
				map.put("map", null);
				map.put("rmk", jsonString);
				break;
			case 94:
				tradDetail.put("name", loaneeName);
				tradDetail.put("loanAmount", StringUtils.decimalToStr(record.getAmount(), 2));
				tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
				tradDetail.put("status", 13);
				jsonString = JSON.toJSONString(tradDetail);
				
				map.put("member", loaner);
				map.put("map", null);
				map.put("rmk", jsonString);
				break;
			case 95:
				tradDetail.put("name", loanerName);
				tradDetail.put("loanAmount", StringUtils.decimalToStr(record.getAmount(), 2));
				tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
				tradDetail.put("status", 14);
				jsonString = JSON.toJSONString(tradDetail);
				
				map.put("member", loanee);
				map.put("map", null);
				map.put("rmk", jsonString);
				break;
			case 96:
				tradDetail.put("name", loaneeName);
				tradDetail.put("loanAmount", StringUtils.decimalToStr(record.getAmount(), 2));
				tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
				tradDetail.put("status", 15);
				jsonString = JSON.toJSONString(tradDetail);
				
				map.put("member", loaner);
				map.put("map", null);
				map.put("rmk", jsonString);
				break;
			case 97:
				tradDetail.put("name", loanerName);
				tradDetail.put("loanAmount", StringUtils.decimalToStr(record.getAmount(), 2));
				tradDetail.put("status", 16);
				tradDetail.put("time",  DateUtils.formatDateForMessage(new Date()));
				jsonString = JSON.toJSONString(tradDetail);
				
				map.put("member", loanee);
				map.put("map", null);
				map.put("rmk", jsonString);
				break;
			case 98:
				tradDetail.put("name", loaneeName);
				tradDetail.put("loanAmount", StringUtils.decimalToStr(record.getAmount(), 2));
				tradDetail.put("status", 16);
				tradDetail.put("time",  DateUtils.formatDateForMessage(new Date()));
				jsonString = JSON.toJSONString(tradDetail);
				
				map.put("member", loaner);
				map.put("map", null);
				map.put("rmk", jsonString);
				break;
			case 101:
				arbitration = nfsLoanArbitrationService.getByLoanId(orgId);
				tradDetail.put("name",loanerName);
				tradDetail.put("loanAmount",StringUtils.decimalToStr(arbitration.getApplyAmount(), 2));
				tradDetail.put("status", "18");
				tradDetail.put("time", CalendarUtil.toCNStringWithoutYS(DateUtils.formatDate(new Date(), "yyyyMMddHHmmss")));
				jsonString = JSON.toJSONString(tradDetail);
				
				map.put("member", loanee);
				map.put("map", null);
				map.put("rmk", jsonString);
				break;
			case 102:
				arbitration = nfsLoanArbitrationService.getByLoanId(orgId);
				tradDetail.put("name",loanerName);
				tradDetail.put("loanAmount",StringUtils.decimalToStr(arbitration.getApplyAmount(), 2));
				tradDetail.put("status", "19");
				tradDetail.put("time", CalendarUtil.toCNStringWithoutYS(DateUtils.formatDate(new Date(), "yyyyMMddHHmmss")));
				jsonString = JSON.toJSONString(tradDetail);
				
				map.put("member", loanee);
				map.put("map", null);
				map.put("rmk", jsonString);
				break;
			case 103:
				arbitration = nfsLoanArbitrationService.getByLoanId(orgId);
				tradDetail.put("name",loaneeName);
				tradDetail.put("loanAmount",StringUtils.decimalToStr(arbitration.getApplyAmount(), 2));
				tradDetail.put("status", "18");
				tradDetail.put("time", CalendarUtil.toCNStringWithoutYS(DateUtils.formatDate(new Date(), "yyyyMMddHHmmss")));
				jsonString = JSON.toJSONString(tradDetail);
				
				map.put("member", loaner);
				map.put("map", null);
				map.put("rmk", jsonString);
				break;
			case 104:
				arbitration = nfsLoanArbitrationService.getByLoanId(orgId);
				tradDetail.put("name",loaneeName);
				tradDetail.put("loanAmount",StringUtils.decimalToStr(arbitration.getApplyAmount(), 2));
				tradDetail.put("status", "20");
				tradDetail.put("time", CalendarUtil.toCNStringWithoutYS(DateUtils.formatDate(new Date(), "yyyyMMddHHmmss")));
				jsonString = JSON.toJSONString(tradDetail);
				
				map.put("member", loaner);
				map.put("map", null);
				map.put("rmk", jsonString);
				break;
			case 105:
				arbitration = nfsLoanArbitrationService.getByLoanId(orgId);
				tradDetail.put("name",loaneeName);
				tradDetail.put("loanAmount",StringUtils.decimalToStr(arbitration.getApplyAmount(), 2));
				tradDetail.put("status", "19");
				tradDetail.put("time", CalendarUtil.toCNStringWithoutYS(DateUtils.formatDate(new Date(), "yyyyMMddHHmmss")));
				jsonString = JSON.toJSONString(tradDetail);
				
				map.put("member", loaner);
				map.put("map", null);
				map.put("rmk", jsonString);
				break;
			case 111:
				inMap.put("name", loaneeName);
				
				tradDetail.put("name", loanee.getName());
				tradDetail.put("loanAmount", StringUtils.decimalToStr(record.getAmount(), 2));
				tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
				tradDetail.put("status", "收款待确认");
				tradDetail.put("colorStatus", "1");
				tradDetail.put("type", "0");
				jsonString = JSON.toJSONString(tradDetail);
				
				map.put("member", loaner);
				map.put("map", inMap);
				map.put("rmk", jsonString);
				break;
			default:
				break;
		}
		return map;
	}

	private Map<String, Object> getParamsByDetailId(int n, Long orgId) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> inMap = new HashMap<String, Object>();
		Map<String, Object> tradDetail = new HashMap<String,Object>();
		String jsonString = "";
		NfsLoanApplyDetail applyDetail = nfsLoanApplyDetail.get(orgId);
		NfsLoanApply apply = loanApplyService.get(applyDetail.getApply());
		LoanRole loanRole = apply.getLoanRole();
		String applyName = apply.getMember().getName();
		String detailName = applyDetail.getMember().getName();
		switch(n) {
			case 0:
				inMap.put("name", applyName);
				
				tradDetail.put("name", applyName);
				tradDetail.put("loanAmount", StringUtils.decimalToStr(applyDetail.getAmount(), 2));
				tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
				tradDetail.put("status", "申请待确认");
				tradDetail.put("colorStatus", "1");
				tradDetail.put("type", "0");
				jsonString = JSON.toJSONString(tradDetail);
				
				map.put("member", applyDetail.getMember());
				map.put("map", inMap);
				map.put("rmk", jsonString);
				break;
			case 1:	
				inMap.put("name", applyName);
				
				tradDetail.put("name", applyName);
				tradDetail.put("loanAmount", StringUtils.decimalToStr(applyDetail.getAmount(), 2));
				tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
				tradDetail.put("status", "取消借款申请");
				tradDetail.put("colorStatus", "2");
				tradDetail.put("type", "0");
				jsonString = JSON.toJSONString(tradDetail);
				
				map.put("member", applyDetail.getMember());
				map.put("map", inMap);
				map.put("rmk", jsonString);
				break;
			case 2:	
				tradDetail.put("name", applyName);
				tradDetail.put("loanAmount", StringUtils.decimalToStr(applyDetail.getAmount(), 2));
				tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
				tradDetail.put("status", "借款提醒");
				tradDetail.put("colorStatus", "0");
				tradDetail.put("type", "0");
				jsonString = JSON.toJSONString(tradDetail);
				
				map.put("member", applyDetail.getMember());
				map.put("map", null);
				map.put("rmk", jsonString);
				break;
			case 3:
				if(loanRole.equals(LoanRole.loanee)) {
					inMap.put("name", detailName);
					
					tradDetail.put("name", detailName);
					tradDetail.put("loanAmount", StringUtils.decimalToStr(applyDetail.getAmount(), 2));
					tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
					tradDetail.put("status", "提醒录制视频");
					tradDetail.put("colorStatus", "0");
					tradDetail.put("type", "0");
					jsonString = JSON.toJSONString(tradDetail);
					
					map.put("member", apply.getMember());
					map.put("map", inMap);
					map.put("rmk", jsonString);
				}else {
					inMap.put("name", applyName);
					
					tradDetail.put("name", applyName);
					tradDetail.put("loanAmount", StringUtils.decimalToStr(applyDetail.getAmount(), 2));
					tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
					tradDetail.put("status", "提醒录制视频");
					tradDetail.put("colorStatus", "0");
					tradDetail.put("type", "0");
					jsonString = JSON.toJSONString(tradDetail);
					
					map.put("member", applyDetail.getMember());
					map.put("map", inMap);
					map.put("rmk", jsonString);
				}
				break;
			case 4:	
				if(loanRole.equals(LoanRole.loanee)) {
					inMap.put("name", applyName);
					
					tradDetail.put("name", applyName);
					tradDetail.put("loanAmount", StringUtils.decimalToStr(applyDetail.getAmount(), 2));
					tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
					tradDetail.put("status", "审核视频");
					tradDetail.put("colorStatus", "0");
					tradDetail.put("type", "0");
					jsonString = JSON.toJSONString(tradDetail);
					
					map.put("member", applyDetail.getMember());
					map.put("map", inMap);
					map.put("rmk", jsonString);
				}else {
					inMap.put("name", detailName);
					
					tradDetail.put("name", detailName);
					tradDetail.put("loanAmount", StringUtils.decimalToStr(applyDetail.getAmount(), 2));
					tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
					tradDetail.put("status", "审核视频");
					tradDetail.put("colorStatus", "0");
					tradDetail.put("type", "0");
					jsonString = JSON.toJSONString(tradDetail);
					
					map.put("member", apply.getMember());
					map.put("map", inMap);
					map.put("rmk", jsonString);
				}
				break;
			case 5:	
				if(loanRole.equals(LoanRole.loanee)) {
					inMap.put("name", applyName);
					
					tradDetail.put("name", applyName);
					tradDetail.put("loanAmount", StringUtils.decimalToStr(applyDetail.getAmount(), 2));
					tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
					tradDetail.put("status", "提醒审核视频");
					tradDetail.put("colorStatus", "0");
					tradDetail.put("type", "0");
					jsonString = JSON.toJSONString(tradDetail);
					
					map.put("member", applyDetail.getMember());
					map.put("map", inMap);
					map.put("rmk", jsonString);
				}else {
					inMap.put("name", detailName);
					
					tradDetail.put("name", detailName);
					tradDetail.put("loanAmount", StringUtils.decimalToStr(applyDetail.getAmount(), 2));
					tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
					tradDetail.put("status", "提醒审核视频");
					tradDetail.put("colorStatus", "0");
					tradDetail.put("type", "0");
					jsonString = JSON.toJSONString(tradDetail);
					
					map.put("member", apply.getMember());
					map.put("map", inMap);
					map.put("rmk", jsonString);
				}
				break;
			case 6:	
				if(loanRole.equals(LoanRole.loanee)) {
					inMap.put("name", detailName);
					
					tradDetail.put("name", detailName);
					tradDetail.put("loanAmount", StringUtils.decimalToStr(applyDetail.getAmount(), 2));
					tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
					tradDetail.put("status", "视频审核失败");
					tradDetail.put("colorStatus", "2");
					tradDetail.put("type", "0");
					jsonString = JSON.toJSONString(tradDetail);
					
					map.put("member", apply.getMember());
					map.put("map", inMap);
					map.put("rmk", jsonString);
				}else {
					inMap.put("name", applyName);
					
					tradDetail.put("name", applyName);
					tradDetail.put("loanAmount", StringUtils.decimalToStr(applyDetail.getAmount(), 2));
					tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
					tradDetail.put("status", "视频审核失败");
					tradDetail.put("colorStatus", "2");
					tradDetail.put("type", "0");
					jsonString = JSON.toJSONString(tradDetail);
					
					map.put("member", applyDetail.getMember());
					map.put("map", inMap);
					map.put("rmk", jsonString);
				}
				break;
			case 7:	
				if(loanRole.equals(LoanRole.loanee)) {
					inMap.put("name", detailName);
					
					tradDetail.put("name", detailName);
					tradDetail.put("loanAmount", StringUtils.decimalToStr(applyDetail.getAmount(), 2));
					tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
					tradDetail.put("status", "提醒录制视频");
					tradDetail.put("colorStatus", "0");
					tradDetail.put("type", "0");
					jsonString = JSON.toJSONString(tradDetail);
					
					map.put("member", apply.getMember());
					map.put("map", inMap);
					map.put("rmk", jsonString);
				}else {
					inMap.put("name", applyName);
					
					tradDetail.put("name", applyName);
					tradDetail.put("loanAmount", StringUtils.decimalToStr(applyDetail.getAmount(), 2));
					tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
					tradDetail.put("status", "提醒录制视频");
					tradDetail.put("colorStatus", "0");
					tradDetail.put("type", "0");
					jsonString = JSON.toJSONString(tradDetail);
					
					map.put("member", applyDetail.getMember());
					map.put("map", inMap);
					map.put("rmk", jsonString);
				}
				break;
			case 10:	
				inMap.put("name", applyName);
				
				tradDetail.put("name", applyName);
				tradDetail.put("loanAmount", StringUtils.decimalToStr(applyDetail.getAmount(), 2));
				tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
				tradDetail.put("status", "待录制视频");
				tradDetail.put("colorStatus", "1");
				tradDetail.put("type", "0");
				jsonString = JSON.toJSONString(tradDetail);
				
				map.put("member", applyDetail.getMember());
				map.put("map", inMap);
				map.put("rmk", jsonString);
				break;
			case 11:	
				inMap.put("name", detailName);
				
				tradDetail.put("name", detailName);
				tradDetail.put("loanAmount", StringUtils.decimalToStr(applyDetail.getAmount(), 2));
				tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
				tradDetail.put("status", "待录制视频");
				tradDetail.put("colorStatus", "1");
				tradDetail.put("type", "0");
				jsonString = JSON.toJSONString(tradDetail);
				
				map.put("member", apply.getMember());
				map.put("map", inMap);
				map.put("rmk", jsonString);
				break;
			case 12:	
				inMap.put("name", detailName);
				
				tradDetail.put("name", detailName);
				tradDetail.put("loanAmount", StringUtils.decimalToStr(applyDetail.getAmount(), 2));
				tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
				tradDetail.put("status", "拒绝借款申请");
				tradDetail.put("colorStatus", "2");
				tradDetail.put("type", "0");
				jsonString = JSON.toJSONString(tradDetail);
				
				map.put("member", apply.getMember());
				map.put("map", inMap);
				map.put("rmk", jsonString);
				break;
			case 13:	
				inMap.put("name", applyName);
				
				tradDetail.put("name", applyName);
				tradDetail.put("loanAmount", StringUtils.decimalToStr(applyDetail.getAmount(), 2));
				tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
				tradDetail.put("status", "申请待确认");
				tradDetail.put("colorStatus", "1");
				tradDetail.put("type", "0");
				jsonString = JSON.toJSONString(tradDetail);
				
				map.put("member", applyDetail.getMember());
				map.put("map", inMap);
				map.put("rmk", jsonString);
				break;
			case 14:	
				inMap.put("name", detailName);
				
				tradDetail.put("name", detailName);
				tradDetail.put("loanAmount", StringUtils.decimalToStr(applyDetail.getAmount(), 2));
				tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
				tradDetail.put("status", "申请被拒绝");
				tradDetail.put("colorStatus", "2");
				tradDetail.put("type", "0");
				jsonString = JSON.toJSONString(tradDetail);
				
				map.put("member", apply.getMember());
				map.put("map", inMap);
				map.put("rmk", jsonString);
				break;
			case 25:
				tradDetail.put("name", applyName);
				tradDetail.put("loanAmount", StringUtils.decimalToStr(applyDetail.getAmount(), 2));
				tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
				tradDetail.put("status", "收款待确认");
				tradDetail.put("colorStatus", "1");
				tradDetail.put("type", "0");
				jsonString = JSON.toJSONString(tradDetail);
				
				map.put("member", applyDetail.getMember());
				map.put("map", null);
				map.put("rmk", jsonString);
				break;
			case 34://多人借款 
				break;
			case 76:	
				tradDetail.put("name", detailName);
				tradDetail.put("loanAmount", StringUtils.decimalToStr(applyDetail.getAmount(), 2));
				tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
				tradDetail.put("status", "2");
				jsonString = JSON.toJSONString(tradDetail);
				
				map.put("member", apply.getMember());
				map.put("map", null);
				map.put("rmk", jsonString);
				break;
			case 77:	
				tradDetail.put("name", detailName);
				tradDetail.put("loanAmount", StringUtils.decimalToStr(applyDetail.getAmount(), 2));
				tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
				tradDetail.put("status", "2");
				jsonString = JSON.toJSONString(tradDetail);
				
				map.put("member", apply.getMember());
				map.put("map", null);
				map.put("rmk", jsonString);
				break;
			case 78:
				tradDetail.put("name", detailName);
				tradDetail.put("loanAmount", StringUtils.decimalToStr(applyDetail.getAmount(), 2));
				tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
				tradDetail.put("status", 2);
				jsonString = JSON.toJSONString(tradDetail);
				
				map.put("member", apply.getMember());
				map.put("map", null);
				map.put("rmk", jsonString);
				break;
			case 79:	
				tradDetail.put("name", detailName);
				tradDetail.put("loanAmount", StringUtils.decimalToStr(applyDetail.getAmount(), 2));
				tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
				tradDetail.put("status", 2);
				jsonString = JSON.toJSONString(tradDetail);
				
				map.put("member", apply.getMember());
				map.put("map", null);
				map.put("rmk", jsonString);
			default:
				break;
		}
		return map;
	}

	private String getOrgTypeByType(Type type) {
		int n = type.ordinal();
		if(n >= 0&&n <= 7||n >= 10&&n <= 14||n >= 76&&n <= 79||n == 25||n == 34) {
			return "1";
		}else if(n >= 8&&n <= 9||n >= 15&&n <= 24||n >= 26&&n <= 33||n >= 54&&n <= 61||n >= 80&&n <= 98||n >= 101&&n <= 102||n == 111) {
			return "2";
		}else {
			return "3";
		}
	}

	private String getGroupsByType(Type type) {
		int n = type.ordinal();
		if(n >= 0&&n <= 36||n >= 54&&n <= 66||n == 111) {
			return "appLoanMessage";
		}else if (n == 37||n == 110) {
			return "appTransactionMessage";
		}else if (n >= 38&&n <= 53) {
			return "appServiceMessage";
		}else if(n >= 68&&n <= 73) {
			return "gxtTransactionMessage";
		}else if(n >= 74&&n <= 98) {
			return "gxtLoanMessage";
		}else if(n >= 99&&n <= 109) {
			return "gxtArbitrationMessage";
		}else {
			return "";
		}
	}

	/**
	 * 后台管理 单独发站内信
	 */
	@Transactional(readOnly = false)
	@Override
	public MemberMessage sendMessageForAdmin(String content, String title, Member member) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("type", "6");
		String json = JSONUtil.toJson(map);
		
		MemberMessage memberMessage = new MemberMessage();
		memberMessage.setMember(member);
		memberMessage.setTitle(title);
		memberMessage.setTitleValue("27");
		memberMessage.setContent(content);
		memberMessage.setGroups(MemberMessage.Group.appLoanMessage);
		memberMessage.setRead(false);
		memberMessage.setType(MemberMessage.Type.messageForAdmin);
		memberMessage.setOrgId(0L);
		memberMessage.setOrgType("0");
		memberMessage.setRmk(json);
		save(memberMessage);
		
		RedisUtils.increment("memberInfo"+member.getId(),"newMsgCount",1);
				
		return memberMessage;
	}
	
	@Override
	@Transactional(readOnly = false)
	public void deleteMessages(List<String> list) {
		
		memberMessageDao.deleteMessages(list);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void setRead(Member member) {
		
		memberMessageDao.setRead(member);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void updateReadById(long id) {
		
		memberMessageDao.updateReadById(id);
	}

	@Override
	public int getCountsUnRead(Member member) {
	
		return memberMessageDao.getCountsUnRead(member);
	}
	
	@Override
	@Transactional(readOnly=false)
	public int UpdateNoOrgId() {
		return dao.UpdateNoOrgId();
	}

	@Override
	public String getImageUrl(String titleValue) {
		String imageUrl =  Global.getConfig("domain");
		if(StringUtils.contains("0,1", titleValue)) {
			imageUrl =  imageUrl+Global.getConfig("messageIcon.loanNotice");
		}else if(StringUtils.equals(titleValue, "2")) {
			imageUrl = imageUrl+Global.getConfig("messageIcon.lendNotice");
		}else if(StringUtils.equals(titleValue, "3")) {
			imageUrl = imageUrl+Global.getConfig("messageIcon.transferAccountsNotice");			
		}else if(StringUtils.equals("4",titleValue)) {
			imageUrl = imageUrl+Global.getConfig("messageIcon.creditArchives");
		}else if(StringUtils.equals("5",titleValue)) {
			imageUrl = imageUrl+Global.getConfig("messageIcon.loanNoticeGray");
		}else if(StringUtils.equals("12",titleValue)) {
			imageUrl = imageUrl+Global.getConfig("messageIcon.strongGraspGray");
		}else if(StringUtils.equals(titleValue, "14")) {
			imageUrl = imageUrl+Global.getConfig("messageIcon.auctionNotice");
		}else if(StringUtils.contains("15,18,19,20",titleValue)) {
			imageUrl = imageUrl+Global.getConfig("messageIcon.fee");
		}else if(StringUtils.equals(titleValue, "16")) {
			imageUrl = imageUrl+Global.getConfig("messageIcon.recharge");
		}else if(StringUtils.equals(titleValue, "17")) {
			imageUrl = imageUrl+Global.getConfig("messageIcon.withdrawal");
		}else if(StringUtils.contains("21,26",titleValue)) {
			imageUrl = imageUrl+Global.getConfig("messageIcon.loanNotice");
		}else if(StringUtils.contains("22,25",titleValue)) {
			imageUrl = imageUrl+Global.getConfig("messageIcon.lendNotice");
		}else if(StringUtils.contains("23,24",titleValue)) {
			imageUrl = imageUrl+Global.getConfig("messageIcon.delay");
		}else if(StringUtils.contains("27,28",titleValue)) {
			imageUrl = imageUrl+Global.getConfig("messageIcon.archivesNotice");
		}else if(StringUtils.contains("6,29,30",titleValue)) {
			imageUrl = imageUrl+Global.getConfig("messageIcon.arbitration");
		}else if(StringUtils.contains("10,31,32",titleValue)) {
			imageUrl = imageUrl+Global.getConfig("messageIcon.strongGrasp");
		}
		
		return imageUrl;
	}

	@Override
	public Page<MemberMessage> findTransactionPage(Integer pageNo, Integer pageSize, Member member) {
		Page<MemberMessage> page = new Page<MemberMessage>(pageNo == null?1:pageNo, pageSize == null?10:pageSize);
		MemberMessage memberMessage = new MemberMessage();
		memberMessage.setPage(page);
		memberMessage.setGroups(MemberMessage.Group.gxtTransactionMessage);
		memberMessage.setMember(member);
		List<MemberMessage> list = memberMessageDao.findList(memberMessage);
		page.setList(list);
		
		return page;
	}

	@Override
	public Page<MemberMessage> findLoanPage(Integer pageNo, Integer pageSize, Member member) {
		Page<MemberMessage> page = new Page<MemberMessage>(pageNo == null?1:pageNo, pageSize == null?10:pageSize);
		MemberMessage memberMessage = new MemberMessage();
		memberMessage.setPage(page);
		memberMessage.setGroups(MemberMessage.Group.gxtLoanMessage);
		memberMessage.setMember(member);
		List<MemberMessage> list = memberMessageDao.findList(memberMessage);
		page.setList(list);
		
		return page;
	}

	@Override
	public Page<MemberMessage> findArbitrationPage(Integer pageNo, Integer pageSize, Member member) {
		Page<MemberMessage> page = new Page<MemberMessage>(pageNo == null?1:pageNo, pageSize == null?10:pageSize);
		MemberMessage memberMessage = new MemberMessage();
		memberMessage.setPage(page);
		memberMessage.setGroups(MemberMessage.Group.gxtArbitrationMessage);
		memberMessage.setMember(member);
		List<MemberMessage> list = memberMessageDao.findList(memberMessage);
		page.setList(list);
		
		return page;
	}

	@Override
	public Page<MemberMessage> findGxtPages(Integer pageNo, Integer pageSize, Member member) {
		Page<MemberMessage> page = new Page<MemberMessage>(pageNo == null?1:pageNo, pageSize == null?10:pageSize);
		MemberMessage memberMessage = new MemberMessage();
		memberMessage.setPage(page);
		memberMessage.setMember(member);
		List<MemberMessage> list = memberMessageDao.findGxtLists(memberMessage);
		page.setList(list);
		
		return page;
	}

	@Override
	@Transactional(readOnly = false)
	public void setReadByGroup(Member member, Integer type) {
		
		memberMessageDao.setReadByGroup(member, type);
		
	}

	@Override
	public Page<MemberMessage> findAppPages(Integer pageNo, Integer pageSize, Member member) {
		Page<MemberMessage> page = new Page<MemberMessage>(pageNo == null?1:pageNo, pageSize == null?10:pageSize);
		MemberMessage memberMessage = new MemberMessage();
		memberMessage.setPage(page);
		memberMessage.setMember(member);
		List<MemberMessage> list = memberMessageDao.findAppLists(memberMessage);
		page.setList(list);
		
		return page;
	}

	@Override
	public Page<MemberMessage> findAppLoanPage(Integer pageNo, Integer pageSize, Member member) {
		Page<MemberMessage> page = new Page<MemberMessage>(pageNo == null?1:pageNo, pageSize == null?10:pageSize);
		MemberMessage memberMessage = new MemberMessage();
		memberMessage.setPage(page);
		memberMessage.setGroups(MemberMessage.Group.appLoanMessage);
		memberMessage.setMember(member);
		List<MemberMessage> list = memberMessageDao.findList(memberMessage);
		page.setList(list);
		
		return page;
	}

	@Override
	public Page<MemberMessage> findAppServicePage(Integer pageNo, Integer pageSize, Member member) {
		Page<MemberMessage> page = new Page<MemberMessage>(pageNo == null?1:pageNo, pageSize == null?10:pageSize);
		MemberMessage memberMessage = new MemberMessage();
		memberMessage.setPage(page);
		memberMessage.setGroups(MemberMessage.Group.appServiceMessage);
		memberMessage.setMember(member);
		List<MemberMessage> list = memberMessageDao.findList(memberMessage);
		page.setList(list);
		
		return page;
	}

	@Override
	public Page<MemberMessage> findAppTransactionPage(Integer pageNo, Integer pageSize, Member member) {
		Page<MemberMessage> page = new Page<MemberMessage>(pageNo == null?1:pageNo, pageSize == null?10:pageSize);
		MemberMessage memberMessage = new MemberMessage();
		memberMessage.setPage(page);
		memberMessage.setGroups(MemberMessage.Group.appTransactionMessage);
		memberMessage.setMember(member);
		List<MemberMessage> list = memberMessageDao.findList(memberMessage);
		page.setList(list);
		
		return page;
	}

	@Override
	public Page<MemberMessage> findPage(Integer pageNo, Integer pageSize,Member member) {
		
		Page<MemberMessage> page = new Page<MemberMessage>(pageNo == null?1:pageNo, pageSize == null?10:pageSize);
		MemberMessage memberMessage = new MemberMessage();
		memberMessage.setPage(page);
		memberMessage.setMember(member);
		List<MemberMessage> list = memberMessageDao.findAppLists(memberMessage);
		page.setList(list);
		
		return page;
	}

	@Override
	public Page<MemberMessage> findUnReadPage(Integer pageNo, Integer pageSize,Member member) {
		
		Page<MemberMessage> page = new Page<MemberMessage>(pageNo == null?1:pageNo, pageSize == null?10:pageSize);
		MemberMessage memberMessage = new MemberMessage();
		memberMessage.setPage(page);
		memberMessage.setRead(false);
		memberMessage.setMember(member);
		List<MemberMessage> list = memberMessageDao.findAppLists(memberMessage);
		page.setList(list);
		
		return page;
	}
	
}