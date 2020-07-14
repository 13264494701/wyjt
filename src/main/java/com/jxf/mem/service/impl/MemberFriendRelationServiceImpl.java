package com.jxf.mem.service.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.ebaoquan.rop.thirdparty.com.alibaba.fastjson.JSON;
import org.ebaoquan.rop.thirdparty.org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.loan.service.NfsLoanRecordService;
import com.jxf.mem.dao.MemberFriendRelationDao;
import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberFriendRelation;
import com.jxf.mem.entity.MemberLoanReport;
import com.jxf.mem.service.MemberFriendRelationService;
import com.jxf.mem.service.MemberService;
import com.jxf.mem.utils.VerifiedUtils;
import com.jxf.rc.entity.RcCaData;
import com.jxf.rc.entity.RcCaData.Type;
import com.jxf.rc.entity.RcCaYysDetails;
import com.jxf.rc.service.RcCaDataService;
import com.jxf.rc.service.RcCaYysDetailsService;
import com.jxf.svc.cache.RedisUtils;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.Exceptions;
import com.jxf.svc.utils.JSONUtil;
import com.jxf.svc.utils.ObjectUtils;
import com.jxf.ufang.entity.UfangExtendMember;

/**
 * 好友关系表ServiceImpl
 * @author XIAORONGDIAN
 * @version 2018-10-11
 */
@Service("memberFriendRelationService")
@Transactional(readOnly = true)
public class MemberFriendRelationServiceImpl extends CrudServiceImpl<MemberFriendRelationDao, MemberFriendRelation> implements MemberFriendRelationService{

	
	public static final Integer DEFAULTPAGESIZE = 2000;
	
	@Resource(name = "taskExecutor")
	private TaskExecutor taskExecutor;
	@Autowired
	private MemberService memberService;
	@Autowired
	private NfsLoanRecordService nfsLoanRecordService;
	@Autowired
	private MemberFriendRelationDao friendRelationDao;
	@Autowired
	private RcCaDataService rcCaDataService;
	@Autowired
	private RcCaYysDetailsService rcCaYysDetailsService;
	
	@Override
	public MemberFriendRelation get(Long id) {
		return super.get(id);
	}
	
	@Override
	public List<MemberFriendRelation> findList(MemberFriendRelation memberFriendRelation) {
		return super.findList(memberFriendRelation);
	}
	
	@Override
	public Page<MemberFriendRelation> findPage(Page<MemberFriendRelation> page, MemberFriendRelation memberFriendRelation) {
		page.setPageSize(10);
		return super.findPage(page, memberFriendRelation);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void save(MemberFriendRelation memberFriendRelation) {
		
		
		super.save(memberFriendRelation);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void delete(MemberFriendRelation memberFriendRelation) {
		super.delete(memberFriendRelation);
	}


	/**
	 * 根据用户ID查好友关系列表
	 * @param memberId
	 * @return
	 */
	public List<MemberFriendRelation> findByFriendIdAndShowFlag(Long memberId) {
		MemberFriendRelation friendRelation = new MemberFriendRelation();
		Member friend = new Member();
		friend.setId(memberId);
		friendRelation.setFriend(friend);
		return friendRelationDao.findList(friendRelation);
	}


	@Override
	@Transactional(readOnly = false)
	public void deleteByFriendId(Long memberId, Long friendId) {
		friendRelationDao.deleteByFriendId(memberId, friendId);
	}

	@Override
	public MemberFriendRelation findByMemberIdAndFriendId(Long memberId,Long friendId) {
		
		return friendRelationDao.getByMemberIdAndFriendId(memberId,friendId);
		
	}

	@Override
	public List<MemberFriendRelation> vagueSearchfriendList(MemberFriendRelation memberFriendRelation) {
		
		return friendRelationDao.findListByVagueSearch(memberFriendRelation);
	}

	@Override
	@Transactional(readOnly = false)
	public void changeTooManyFriendsStatus(Member member) {
		
		Integer verifiedList = member.getVerifiedList();
		int count = friendRelationDao.getFriendCount(member.getId());
		if(count > DEFAULTPAGESIZE) {
			member.setVerifiedList(VerifiedUtils.addVerified(verifiedList, 25));
			RedisUtils.put("memberInfo"+member.getId(), "tooManyFriends", 1);
		}else {
			member.setVerifiedList(VerifiedUtils.removeVerified(verifiedList, 25));
			RedisUtils.put("memberInfo"+member.getId(), "tooManyFriends", 0);
		}
		memberService.save(member);
	}

	@Override
	public List<MemberFriendRelation> findMemberFriendRelation(Member member,
			String timestamp) {
		Date updateTime = null;
		List<MemberFriendRelation> friendRelationList = null ;
		try {
			if(StringUtils.isNotEmpty(timestamp) && !StringUtils.equals(timestamp, "0")) {
				updateTime = DateUtils.parse(timestamp);
			}
			MemberFriendRelation friendRelation = new MemberFriendRelation();	
			friendRelation.setMember(member);
			friendRelation.setUpdateTime(updateTime);
			friendRelationList = friendRelationDao.findList(friendRelation);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return friendRelationList;
	}

	@Override
	public Boolean checkFriendRelation(Long memberId, Long friendId) {
		
		return friendRelationDao.checkFriendRelation(memberId, friendId);
	}

	@Override
	public void getFriendCreditRecord(MemberLoanReport memberLoanReport) {
		try {
			Member friend = memberService.get(memberLoanReport.getId());
			//认证信息
			verifyInfo(memberLoanReport,friend);
			
			//信用记录
			nfsLoanRecordService.loanReport(memberLoanReport);
			
			//淘宝信息
			taobaoInfo(memberLoanReport,friend);
			
			//运营商信息
			yunyingshangInfo(memberLoanReport,friend);
			
			//学信网信息
			xuexinwangInfo(memberLoanReport,friend);
			
			//社保信息
			shebaoInfo(memberLoanReport,friend);
			
			//公积金信息
			gongjijinInfo(memberLoanReport,friend);
			
			//紧急联系人
			jinjilianxirenInfo(memberLoanReport,friend);
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(Exceptions.getStackTraceAsString(e));
		}
		
	}
	
	/**
	 * 紧急联系人
	 * @param memberLoanReport
	 * @param friend
	 */
	private void jinjilianxirenInfo(MemberLoanReport memberLoanReport, Member friend) {

		RcCaData caDate = rcCaDataService.getByPhoneNoAndType(friend.getUsername(),RcCaData.Type.emergency_people);
		String nameF = "";
		String nameM = "";
		String nameL = "";
		String moblieF = "";
		String moblieM = "";
		String moblieL = "";
		int intF = 0;
		int intM = 0;
		int intL = 0;
		int tonghuaF = 0;
		int tonghuaM = 0;
		int tonghuaL = 0;
		
		if(caDate != null) {
			Map<String, Object> map = JSONUtil.toMap(caDate.getContent());
			nameF = map.get("nameF").toString();
			nameM = map.get("nameM").toString();
			nameL = map.get("nameL").toString();
			moblieF = map.get("moblieF").toString();
			moblieM = map.get("moblieM").toString();
			moblieL = map.get("moblieL").toString();
			
			RcCaYysDetails details = new RcCaYysDetails();
			details.setMemberId(friend.getId());
			List<RcCaYysDetails> findList = rcCaYysDetailsService.findList(details);
			if(findList.size() > 0) {
				for (int i = 0; i < findList.size(); i++) {
					Map<String, Object> map2 = JSONUtil.toMap(findList.get(i).getContent());
					List<Map> listMapFromJson = JSON.parseArray(map2.get("call_info").toString(), Map.class);
					
					//取得半年
					List<List<Map>> listNew = new ArrayList<List<Map>>();
					for (int j = 0; j < listMapFromJson.size(); j++) {
						Map<String, Object> recordMap = listMapFromJson.get(j);
						if (recordMap != null) {
							List<Map> list2 = new ArrayList<>();
							list2 = JSON.parseArray(recordMap.get("call_record").toString(), Map.class);
							listNew.add(list2);
						}
					}
					if (listNew.size() > 0) {
						for (List<Map> obj : listNew) {
							for (Map<String, Object> list : obj) {
								String strin = list.get("call_other_number").toString();
								if (StringUtils.equals(strin,moblieF)) {
									intF = intF + 1;
									String strin1 = list.get("call_time").toString();
									tonghuaF = tonghuaF + Integer.parseInt(strin1);
								}
								if (StringUtils.equals(strin,moblieM)) {
									intM = intM + 1;
									String strin1 = list.get("call_time").toString();
									tonghuaM = tonghuaM + Integer.parseInt(strin1);
								}
								if (StringUtils.equals(strin,moblieL)) {
									intL = intL + 1;
									String strin1 = list.get("call_time").toString();
									tonghuaL = tonghuaL + Integer.parseInt(strin1);
								}
							}
						}
					}
				}
			}
		}
		
		List<UfangExtendMember> extendMemberList = memberLoanReport.getExtendMemberList();
		UfangExtendMember extendF = new UfangExtendMember();
		extendF.setName(nameF);
		extendF.setUsername(moblieF);
		extendF.setCallCount(intF);
		extendF.setCallTime(tonghuaF);
		extendMemberList.add(extendF);
		UfangExtendMember extendM = new UfangExtendMember();
		extendM.setName(nameM);
		extendM.setUsername(moblieM);
		extendM.setCallCount(intM);
		extendM.setCallTime(tonghuaM);
		extendMemberList.add(extendM);
		UfangExtendMember extendL = new UfangExtendMember();
		extendL.setName(nameL);
		extendL.setUsername(moblieL);
		extendL.setCallCount(intL);
		extendL.setCallTime(tonghuaL);
		extendMemberList.add(extendL);
	}

	/**
	 * 运营商 
	 * @param memberLoanReport
	 * @param friend
	 * @throws ParseException
	 */
	private void yunyingshangInfo(MemberLoanReport memberLoanReport, Member friend) throws ParseException {

		RcCaData caDate = rcCaDataService.getByPhoneNoAndType(friend.getUsername(),RcCaData.Type.yunyingshang);
		
		if(caDate != null) {
			Map<String, Object> map = JSONUtil.toMap(caDate.getContent());
			memberLoanReport.setYunyingshangUsername(StringUtils.isBlank(map.get("user_mobile").toString())? "":map.get("user_mobile").toString());
			memberLoanReport.setYunyingshang(StringUtils.isBlank(map.get("channel_src").toString())? "":map.get("channel_src").toString());
			memberLoanReport.setTaocanMoney(StringUtils.isBlank(map.get("taocanmoney").toString())? "0":map.get("taocanmoney").toString());
			String createTime = map.get("created_time").toString();
			Date CreateDate = DateUtils.parse(createTime);
			memberLoanReport.setUserTime(DateUtils.pastDays(CreateDate));
		}
		
		RcCaData caDateReport = rcCaDataService.getByPhoneNoAndType(friend.getUsername(),RcCaData.Type.yunyingshang_report);
		if(caDateReport != null) {
			Map<String, Object> map = JSONUtil.toMap(caDateReport.getContent());
			if(StringUtils.isNotBlank(map.get("all_contact_stats").toString())) {
				Map<String, Object> callMap = JSONUtil.toMap(map.get("all_contact_stats").toString());
				memberLoanReport.setCallCount(StringUtils.isBlank(callMap.get("call_count_6month").toString())? "0":callMap.get("call_count_6month").toString());//半年内通话次数
				memberLoanReport.setNightCallCount(StringUtils.isBlank(callMap.get("call_count_late_night_6month").toString())? "0":
					callMap.get("call_count_late_night_6month").toString());//夜间通话次数
				String callTime = callMap.get("call_time_6month").toString();
				if(StringUtils.isNotBlank(callTime)) {
					int a = Integer.parseInt(callTime);
					int c = 0;
					if(a%180 != 0) {
						c = a/180 + 1;
					}else {
						c = a/180;
					}
					memberLoanReport.setOneDayCallTime(c);//日均通话时长
				}
			}
			
			if(StringUtils.isNotBlank(map.get("active_silence_stats").toString())) {
				Map<String, Object> silenceMap = JSONUtil.toMap(map.get("active_silence_stats").toString());
				String silenceCount = silenceMap.get("continue_silence_day_over3_0call_6month").toString();
				memberLoanReport.setSilenceCount(StringUtils.isBlank(silenceCount)? "0":silenceCount);//静默次数
			}
			
			if(StringUtils.isNotBlank(map.get("carrier_consumption_stats").toString())) {
				Map<String, Object> consumptionMap = JSONUtil.toMap(map.get("carrier_consumption_stats").toString());
				if(StringUtils.isBlank(consumptionMap.get("consume_amount_6month").toString())) {
					memberLoanReport.setOneMonthUseMoney("0");
				}else {
					BigDecimal consumeAmount = new BigDecimal(consumptionMap.get("consume_amount_6month").toString());
					String oneMonthUseMoney = consumeAmount.divide(new BigDecimal("6"), BigDecimal.ROUND_HALF_UP).toString();
					memberLoanReport.setOneMonthUseMoney(oneMonthUseMoney);//月均消费金额
				}
			}
			
		}
		
	}
	
	/**
	 * 公积金
	 * @param memberLoanReport
	 * @param friend
	 */
	private void gongjijinInfo(MemberLoanReport memberLoanReport, Member friend) {
		
		RcCaData caDate = rcCaDataService.getByPhoneNoAndType(friend.getUsername(),RcCaData.Type.gongjijin);
		
		if(caDate != null) {
			Map<String, Object> map = JSONUtil.toMap(caDate.getContent());
			map = JSONUtil.toMap(map.get("base_info").toString());
			memberLoanReport.setGongjijinCompanyName(ObjectUtils.isNotBlank(map.get("corp_name"))==false? "":map.get("corp_name").toString());
			memberLoanReport.setGongjijinOpenAccountTime(ObjectUtils.isNotBlank(map.get("begin_date"))==false? "":map.get("begin_date").toString());
			memberLoanReport.setGongjijinPaymentStatus(ObjectUtils.isNotBlank(map.get("pay_status_desc"))==false? "":map.get("pay_status_desc").toString());
			memberLoanReport.setGongjijinLastPaymentTime(ObjectUtils.isNotBlank(map.get("last_pay_date"))==false? "":map.get("last_pay_date").toString());
			memberLoanReport.setGongjijinAddr(ObjectUtils.isNotBlank(map.get("home_address"))==false?"":map.get("home_address").toString());
		}
		
	}

	/**
	 * 社保
	 * @param memberLoanReport
	 * @param friend
	 */
	private void shebaoInfo(MemberLoanReport memberLoanReport, Member friend) {

		RcCaData caDate = rcCaDataService.getByPhoneNoAndType(friend.getUsername(),RcCaData.Type.shebao);
		
		if(caDate != null) {
			Map<String, Object> map = JSONUtil.toMap(caDate.getContent());
			map = JSONUtil.toMap(map.get("user_info").toString());
			memberLoanReport.setShebaoCompanyName(ObjectUtils.isNotBlank(map.get("company_name"))==false? "":map.get("company_name").toString());
			memberLoanReport.setShebaoWorkTime(ObjectUtils.isNotBlank(map.get("time_to_work"))==false? "":map.get("time_to_work").toString());
			memberLoanReport.setShebaoFamilyAddr(ObjectUtils.isNotBlank(map.get("home_address"))==false? "":map.get("home_address").toString());
			memberLoanReport.setShebaoPaymentTime(ObjectUtils.isNotBlank(map.get("begin_date"))==false? "":map.get("begin_date").toString());
		}
		
	}
	/**
	 * 学信网
	 * @param memberLoanReport
	 * @param friend
	 */
	private void xuexinwangInfo(MemberLoanReport memberLoanReport, Member friend) {

		RcCaData caDate = rcCaDataService.getByPhoneNoAndType(friend.getUsername(),RcCaData.Type.xuexinwang);
		
		if(caDate != null) {
			List<Map> listMapFromJson = JSON.parseArray(caDate.getContent().toString(), Map.class);
			for (Map map2 : listMapFromJson) {
				memberLoanReport.setColleges(ObjectUtils.isNotBlank(map2.get("school"))==false? "":map2.get("school").toString());
				memberLoanReport.setMajor(ObjectUtils.isNotBlank(map2.get("major"))==false? "":map2.get("major").toString());
				memberLoanReport.setEntranceTime(ObjectUtils.isNotBlank(map2.get("entrance_date"))==false? "":map2.get("entrance_date").toString());
				memberLoanReport.setEducation(ObjectUtils.isNotBlank(map2.get("edu_level"))==false? "":map2.get("edu_level").toString());
				memberLoanReport.setGraduationTime(ObjectUtils.isNotBlank(map2.get("graduate_date"))==false? "":map2.get("graduate_date").toString());
			}
		}
		
	}
	/**
	 * 淘宝
	 * @param memberLoanReport
	 * @param friend
	 */
	private void taobaoInfo(MemberLoanReport memberLoanReport, Member friend) {

		RcCaData caDate = rcCaDataService.getByPhoneNoAndType(friend.getUsername(),RcCaData.Type.taobao);
		
		if(caDate != null) {
			Map<String, Object> map = JSONUtil.toMap(caDate.getContent());
			memberLoanReport.setYueAmount(ObjectUtils.isNotBlank(map.get("tbYEbao"))==false? "0":map.get("tbYEbao").toString());
			memberLoanReport.setHuabeiAmount(ObjectUtils.isNotBlank(map.get("tbHuabei"))==false? "0":map.get("tbHuabei").toString());
			memberLoanReport.setTaoboAddr(ObjectUtils.isNotBlank(map.get("taoadree"))==false? "":map.get("taoadree").toString());
			memberLoanReport.setOrderCount(ObjectUtils.isNotBlank(map.get("yueNum"))==false? 0:(int) map.get("yueNum"));
			memberLoanReport.setOrderAmount(ObjectUtils.isNotBlank(map.get("money"))==false? "0":map.get("money").toString());
			memberLoanReport.setOneMonthMoney(ObjectUtils.isNotBlank(map.get("yuemoney"))==false? "0":map.get("yuemoney").toString());
			memberLoanReport.setMaxMoney(ObjectUtils.isNotBlank(map.get("maxmoney"))==false? "0":map.get("maxmoney").toString());
			memberLoanReport.setMinMoney(ObjectUtils.isNotBlank(map.get("minmoney"))==false? "0":map.get("minmoney").toString());
		}
	}
	/**
	 * 信用记录
	 * @param memberLoanReport
	 * @param friend
	 */
	private void verifyInfo(MemberLoanReport memberLoanReport, Member friend) {
		memberLoanReport.setName(friend.getName());
		memberLoanReport.setUsername(friend.getUsername());
		memberLoanReport.setCrStatus("已认证");
		
		memberLoanReport.setTaobaoStatus(getVerifyStatus(friend,RcCaData.Type.taobao));
		memberLoanReport.setYunyingshangStatus(getVerifyStatus(friend,RcCaData.Type.yunyingshang));
		memberLoanReport.setZhimafenStatus(getVerifyStatus(friend,RcCaData.Type.zhimafen));
		memberLoanReport.setXuexingwangStatus(getVerifyStatus(friend,RcCaData.Type.xuexinwang));
		memberLoanReport.setGongjijingStatus(getVerifyStatus(friend,RcCaData.Type.gongjijin));
		memberLoanReport.setShebaoStatus(getVerifyStatus(friend,RcCaData.Type.shebao));
	}
	/**
	 * 认证状态
	 * @param memberLoanReport
	 * @param friend
	 */
	private String getVerifyStatus(Member friend, Type type) {

		RcCaData caDate = rcCaDataService.getByPhoneNoAndType(friend.getUsername(),type);
		
		if(caDate == null) {
			return "未认证";
		}
		if(DateUtils.pastDays(caDate.getCreateTime()) > 30) {
			return "已过期";
		}else {
			return "已认证";
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}