package com.jxf.web.app.wyjt.member;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.jxf.loan.entity.NfsLoanRecord;
import com.jxf.loan.service.NfsLoanRecordService;
import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberFriendApply;
import com.jxf.mem.entity.MemberFriendRelation;
import com.jxf.mem.entity.MemberFriendReport;
import com.jxf.mem.entity.MemberPhonebook;
import com.jxf.mem.service.MemberFriendApplyService;
import com.jxf.mem.service.MemberFriendRelationService;
import com.jxf.mem.service.MemberFriendReportService;
import com.jxf.mem.service.MemberPhonebookService;
import com.jxf.mem.service.MemberService;
import com.jxf.mem.utils.MemUtils;
import com.jxf.mem.utils.VerifiedUtils;
import com.jxf.nfs.constant.TrxRuleConstant;
import com.jxf.rc.entity.RcCaDataV2;
import com.jxf.rc.entity.RcConstant;
import com.jxf.rc.entity.RcQuota;
import com.jxf.rc.entity.RcXinyan;
import com.jxf.rc.service.RcCaDataService;
import com.jxf.rc.service.RcCaDataServiceV2;
import com.jxf.rc.service.RcQuotaService;
import com.jxf.rc.service.RcXinyanService;
import com.jxf.svc.annotation.AccessLimit;
import com.jxf.svc.cache.RedisUtils;
import com.jxf.svc.config.Global;
import com.jxf.svc.model.Notice;
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.IdCardUtils;
import com.jxf.svc.utils.StringUtils;
import com.jxf.web.minipro.BaseController;
import com.jxf.web.model.ResponseData;
import com.jxf.web.model.wyjt.app.QrcodeAddFriendRequestParam;
import com.jxf.web.model.wyjt.app.QrcodeAddFriendResponseResult;
import com.jxf.web.model.wyjt.app.friend.AgreeAddFriendRequestParam;
import com.jxf.web.model.wyjt.app.friend.ApplyAddFriendListResponseResult;
import com.jxf.web.model.wyjt.app.friend.ApplyAddFriendListResponseResult.Apply;
import com.jxf.web.model.wyjt.app.friend.ApplyAddFriendRequestParam;
import com.jxf.web.model.wyjt.app.friend.FriendBasicResponseResult;
import com.jxf.web.model.wyjt.app.friend.FriendDetailResponseResult;
import com.jxf.web.model.wyjt.app.friend.FriendIdRequestParam;
import com.jxf.web.model.wyjt.app.friend.FriendListRequestParam;
import com.jxf.web.model.wyjt.app.friend.FriendListResponseResult;
import com.jxf.web.model.wyjt.app.friend.FriendListResponseResult.Friend;
import com.jxf.web.model.wyjt.app.friend.FriendOverdueReportResponseResult;
import com.jxf.web.model.wyjt.app.friend.FriendPhoneNoRequestParam;
import com.jxf.web.model.wyjt.app.friend.PhoneListRequestParam;
import com.jxf.web.model.wyjt.app.friend.PhoneListResponseResult;
import com.jxf.web.model.wyjt.app.friend.PhoneListResponseResult.Phone;
import com.jxf.web.model.wyjt.app.friend.RemoveApplyRequestParam;
import com.jxf.web.model.wyjt.app.friend.RemoveFriendRequestParam;
import com.jxf.web.model.wyjt.app.friend.ReportFriendRequestParam;
import com.jxf.web.model.wyjt.app.friend.SearchFriendRequestParam;
import com.jxf.web.model.wyjt.app.member.CaAuthResponseResult;
import com.jxf.web.model.wyjt.app.member.CaAuthResponseResult.UserBriefLegalize;

/**
 * Controller - 会员好友
 * 
 * @author gaobo
 */
@Controller("wyjtAppMemberFriendController")
@RequestMapping(value = "${wyjtApp}/friend")
public class MemberFriendController extends BaseController {

	@Autowired
	private MemberService memberService;
	@Autowired
	private MemberFriendRelationService friendRelationService;
	@Autowired
	private NfsLoanRecordService loanRecordService;
	@Autowired
	private MemberFriendReportService memberFriendReportService;
	@Autowired
	private MemberFriendApplyService memberFriendApplyService;
	@Autowired
	private MemberPhonebookService memberPhonebookService;
	@Autowired
	private RcCaDataService rcCaDataService;
	@Autowired
	private RcCaDataServiceV2 rcCaDataServiceV2;
	@Autowired
	private RcXinyanService rcXinyanService;
	@Autowired
	private RcQuotaService rcQuotaService;
	@Autowired
	private NfsLoanRecordService nfsLoanRecordService;

	/**
	 * 申请添加好友
	 */
	@RequestMapping(value = "/applyAddFriend")
	public @ResponseBody ResponseData applyAddFriend(HttpServletRequest request) {
		Member member = memberService.getCurrent();
		Integer verifiedList = member.getVerifiedList();
		if (!VerifiedUtils.isVerified(verifiedList, 1) && !VerifiedUtils.isVerified(verifiedList, 2)) {
			return ResponseData.error("您还未实名认证，请先完成实名认证");
		}
		String param = request.getParameter("param");
		ApplyAddFriendRequestParam reqData = JSONObject.parseObject(param, ApplyAddFriendRequestParam.class);
		if(StringUtils.isBlank(reqData.getFriendId())) {
			return ResponseData.error("好友ID不能为空");
		}
		Long friendId = Long.valueOf(reqData.getFriendId());

		if (member.getId().equals(friendId)) {
			return ResponseData.error("不能添加自己为好友");
		}
		Member friend = memberService.get(friendId);
		MemberFriendApply applyRecord = new MemberFriendApply();
		applyRecord.setMember(member);
		applyRecord.setFriend(friend);
		applyRecord.setChannel(MemberFriendApply.Channel.common);
		applyRecord.setNote(reqData.getNote());
		applyRecord.setStatus(MemberFriendApply.Status.pendingAgree);
		List<MemberFriendApply> memberFriendApplyRecordList = memberFriendApplyService.findList(applyRecord);
		if (memberFriendApplyRecordList != null && memberFriendApplyRecordList.size() > 0) {
			return ResponseData.success("申请提交成功");
		}
		memberFriendApplyService.save(applyRecord);

		Notice notice = new Notice();
		notice.setNoticeType(2);
		notice.setNoticeId(applyRecord.getId().toString());
		notice.setNoticeMessage("有新好友申请");
		RedisUtils.leftPush("memberNotice" + friend.getId(), notice);

		return ResponseData.success("申请提交成功");
	}

	/**
	 * 申请列表
	 */
	@RequestMapping(value = "/applyAddFriendList")
	public @ResponseBody ResponseData applyAddFriendList(HttpServletRequest request) {
		Member member = memberService.getCurrent2();
		ApplyAddFriendListResponseResult result = new ApplyAddFriendListResponseResult();

		MemberFriendApply applyRecord = new MemberFriendApply();
		applyRecord.setFriend(member);
		Date nowDate = new Date();
		Date earlistAgreeDate = DateUtils.addCalendarByDate(nowDate, -TrxRuleConstant.SHOW_APPLY_DAYS);
		applyRecord.setAgreeDate(earlistAgreeDate);

		List<MemberFriendApply> findList = memberFriendApplyService.findMemberFriendApply(applyRecord);

		for (MemberFriendApply friendApply : findList) {
			Apply apply = new ApplyAddFriendListResponseResult().new Apply();
			Member friend = friendApply.getMember();// Member是好友
			friend = MemUtils.mask(friend);
			apply.setApplyId(String.valueOf(friendApply.getId()));
			apply.setHeadImage(friend.getHeadImage());
			apply.setName(friend.getName() == null ? "" : friend.getName());
			apply.setPhoneNo(friend.getUsername());
			apply.setNote(friendApply.getNote());
			apply.setStatus(friendApply.getStatus().ordinal());
			result.getApplyList().add(apply);
		}
		return ResponseData.success("查询申请列表成功", result);
	}

	/**
	 * 同意添加好友
	 */
	@RequestMapping(value = "/agreeAddFriend")
	public @ResponseBody ResponseData agreeAddFriend(HttpServletRequest request) {
		Member member = memberService.getCurrent();// 我是被申请
		Integer verifiedList = member.getVerifiedList();
		if (!VerifiedUtils.isVerified(verifiedList, 1) && !VerifiedUtils.isVerified(verifiedList, 2)) {
			return ResponseData.error("您还未实名认证，请先完成实名认证");
		}
		String param = request.getParameter("param");
		AgreeAddFriendRequestParam reqData = JSONObject.parseObject(param, AgreeAddFriendRequestParam.class);
		Long applyId = Long.valueOf(reqData.getApplyId());
		MemberFriendApply friendApply = memberFriendApplyService.get(applyId);
		Member friend = memberService.get(friendApply.getMember());

		// 双向好友,数据库中存两条
		MemberFriendRelation memberTofriendRelation = friendRelationService.findByMemberIdAndFriendId(member.getId(),
				friend.getId());
		if (memberTofriendRelation == null) {
			memberTofriendRelation = new MemberFriendRelation();
			memberTofriendRelation.setMember(member);
			memberTofriendRelation.setFriend(friend);
			memberTofriendRelation.setUnread(0);
			memberTofriendRelation.setFreeCaAuthStatus(MemberFriendRelation.FreeCaAuthStatus.unauthorized);
			memberTofriendRelation.setFreeCaAuthTime(null);
			memberTofriendRelation.setChargeCaStatus(MemberFriendRelation.ChargeCaStatus.unpay);
			memberTofriendRelation.setChargeCaTime(null);
			friendRelationService.save(memberTofriendRelation);
		}

		MemberFriendRelation friendToMemberRelation = friendRelationService.findByMemberIdAndFriendId(friend.getId(),
				member.getId());
		if (friendToMemberRelation == null) {
			friendToMemberRelation = new MemberFriendRelation();
			friendToMemberRelation.setMember(friend);
			friendToMemberRelation.setFriend(member);
			friendToMemberRelation.setUnread(0);
			friendToMemberRelation.setFreeCaAuthStatus(MemberFriendRelation.FreeCaAuthStatus.unauthorized);
			friendToMemberRelation.setFreeCaAuthTime(null);
			friendToMemberRelation.setChargeCaStatus(MemberFriendRelation.ChargeCaStatus.unpay);
			friendToMemberRelation.setChargeCaTime(null);
			friendRelationService.save(friendToMemberRelation);
		}

		friendApply.setStatus(MemberFriendApply.Status.agreed);
		friendApply.setAgreeDate(new Date());
		memberFriendApplyService.save(friendApply);

		friendRelationService.changeTooManyFriendsStatus(member);
		return ResponseData.success("同意添加好友成功");
	}

	/**
	 * 好友列表
	 */
	@RequestMapping(value = "/friendList")
	public @ResponseBody ResponseData friendList(HttpServletRequest request) {
		Member member = memberService.getCurrent2();
		String param = request.getParameter("param");
		FriendListRequestParam reqData = JSONObject.parseObject(param, FriendListRequestParam.class);
		String timestamp = reqData.getTimestamp();
		FriendListResponseResult result = new FriendListResponseResult();

		List<MemberFriendRelation> findList = friendRelationService.findMemberFriendRelation(member, timestamp);

		for (MemberFriendRelation friendRelation : findList) {
			Friend friend = new FriendListResponseResult().new Friend();
			Member friendInfo = friendRelation.getFriend();
			friendInfo = MemUtils.mask(friendInfo);
			friend.setId(String.valueOf(friendInfo.getId()));
			friend.setHeadImage(friendInfo.getHeadImage() == null ? "" : friendInfo.getHeadImage());
			String nameAndPhone = "";
			if (StringUtils.isNotEmpty(friendInfo.getName())) {
				nameAndPhone = friendInfo.getName() + "(" + friendInfo.getUsername() + ")";
			} else {
				nameAndPhone = friendInfo.getUsername();
				friendInfo.setName(friendInfo.getUsername());
			}
			friend.setNameAndPhone(nameAndPhone);
			friend.setPhoneNo(friendInfo.getUsername());
			friend.setName(friendInfo.getName());
			friend.setNameSpell(friendInfo.getNickname());
			result.getFriendList().add(friend);
		}
		result.setTimestamp(DateUtils.getDateTime());
		return ResponseData.success("查询好友列表成功", result);
	}

	/**
	 * 通过手机号和姓名模糊查询 好友列表
	 */
	@RequestMapping(value = "/searchFriend")
	public @ResponseBody ResponseData searchFriend(HttpServletRequest request) {
		Member member = memberService.getCurrent2();
		String param = request.getParameter("param");
		SearchFriendRequestParam reqData = JSONObject.parseObject(param, SearchFriendRequestParam.class);
		FriendListResponseResult result = new FriendListResponseResult();
		Member friend = new Member();

		String value = reqData.getValue();

		// 验证搜索内容是否为空
		if (StringUtils.isBlank(value)) {
			return ResponseData.error("搜索内容不能为空");
		}
		if (value.matches("[0-9]*") && value.length() < 5) {
			return ResponseData.error("无此好友");
		}
		if (value.matches("[0-9]*")) {
			friend.setUsername(value);
		} else {
			friend.setName(value);
		}
		MemberFriendRelation memberFriendRelation = new MemberFriendRelation();
		memberFriendRelation.setMember(member);
		memberFriendRelation.setFriend(friend);
		List<MemberFriendRelation> list = friendRelationService.vagueSearchfriendList(memberFriendRelation);
		if (list == null || list.size() == 0) {
			return ResponseData.error("还不是您好友");
		}

		for (MemberFriendRelation friendRelation : list) {
			Friend friendTmp = new FriendListResponseResult().new Friend();
			Member friendInfo = friendRelation.getFriend();
			friendInfo = MemUtils.mask(friendInfo);
			friendTmp.setId(String.valueOf(friendInfo.getId()));
			friendTmp.setHeadImage(friendInfo.getHeadImage() == null ? "" : friendInfo.getHeadImage());
			String nameAndPhone = "";
			if (StringUtils.isNotEmpty(friendInfo.getName())) {
				nameAndPhone = friendInfo.getName() + "(" + friendInfo.getUsername() + ")";
			} else {
				nameAndPhone = friendInfo.getUsername();
				friendInfo.setName(friendInfo.getUsername());
			}
			friendTmp.setNameAndPhone(nameAndPhone);
			friendTmp.setPhoneNo(friendInfo.getUsername());
			friendTmp.setName(friendInfo.getName());
			friendTmp.setNameSpell(friendInfo.getNickname());
			result.getFriendList().add(friendTmp);
		}
		return ResponseData.success("查询好友列表成功", result);
	}

	/**
	 * 删除好友
	 */
	@RequestMapping(value = "/removeFriend")
	public @ResponseBody ResponseData removeMyFriend(HttpServletRequest request) {
		Member member = memberService.getCurrent();
		String param = request.getParameter("param");
		RemoveFriendRequestParam reqData = JSONObject.parseObject(param, RemoveFriendRequestParam.class);

		friendRelationService.deleteByFriendId(member.getId(), Long.valueOf(reqData.getFriendId()));

		friendRelationService.changeTooManyFriendsStatus(member);
		return ResponseData.success("好友删除成功");
	}

	/**
	 * 删除申请记录
	 */
	@RequestMapping(value = "/removeApply")
	public @ResponseBody ResponseData removeApply(HttpServletRequest request) {
		String param = request.getParameter("param");
		RemoveApplyRequestParam reqData = JSONObject.parseObject(param, RemoveApplyRequestParam.class);
		MemberFriendApply memberFriendApplyRecord = new MemberFriendApply();

		Long applyId = Long.valueOf(reqData.getApplyId());
		memberFriendApplyRecord.setId(applyId);
		memberFriendApplyService.delete(memberFriendApplyRecord);
		return ResponseData.success("好友申请记录删除成功");
	}

	/**
	 * 查看手机通讯录
	 */
	@RequestMapping(value = "/comparePhoneListAndFriendList")
	public @ResponseBody ResponseData comparePhoneListAndFriendList(HttpServletRequest request) {
		Member member = memberService.getCurrent();
		String param = request.getParameter("param");
		PhoneListRequestParam reqData = JSONObject.parseObject(param, PhoneListRequestParam.class);
		String phoneNoAndNameList = reqData.getPhonelist();
		PhoneListResponseResult result = new PhoneListResponseResult();
		MemberPhonebook memberPhonebook = memberPhonebookService.getByMember(member);
		String phoneList = "";
		String phoneBook = "";
		if(memberPhonebook!=null) {
			phoneList = memberPhonebook.getPhoneList();
			phoneBook = memberPhonebook.getPhoneBook();
		}else {
			memberPhonebook = new MemberPhonebook();
			memberPhonebook.setMember(member);
		}
		
		if (StringUtils.isNotBlank(phoneNoAndNameList)) {
			String[] phoneNoAndNames = phoneNoAndNameList.split(",");
			for (int i = 0; i < phoneNoAndNames.length; i++) {
				String phoneNo = StringUtils.substringBefore(phoneNoAndNames[i], "|");// 前台传的是 电话号|名字base64加密 格式 ,姓名这里预留
				Member friend = memberService.findByUsername(phoneNo);
				if (friend == null) {
					result.getPhoneList().add(new Phone(phoneNo, "", 0));// 不是注册用户
				} else {
					MemberFriendRelation memberFriendRelation = friendRelationService.findByMemberIdAndFriendId(member.getId(), friend.getId());
					String friendId = friend.getId().toString();
					if (memberFriendRelation == null) {
						result.getPhoneList().add(new Phone(phoneNo, friendId, 1));// 是注册用户但不是好友
					} else {
						result.getPhoneList().add(new Phone(phoneNo, friendId, 2));// 是好友
					}
				}
				if(!StringUtils.contains(phoneList, phoneNo)) {
					phoneList = phoneList+","+phoneNo;
				}
				if(!StringUtils.contains(phoneBook, phoneNoAndNames[i])) {
					phoneBook = phoneBook+","+phoneNoAndNames[i];
				}
			}
			memberPhonebook.setPhoneList(phoneList);
			memberPhonebook.setPhoneBook(phoneBook);
			memberPhonebookService.save(memberPhonebook);
			return ResponseData.success("查看手机通讯录成功", result);
		} else {
			return ResponseData.error("您的通讯录为空");
		}
	}

	/**
	 * 举报好友
	 */
	@RequestMapping(value = "/reportFriend")
	public @ResponseBody ResponseData reportFriend(HttpServletRequest request) {
		Member member = memberService.getCurrent2();
		String param = request.getParameter("param");
		ReportFriendRequestParam reqData = JSONObject.parseObject(param, ReportFriendRequestParam.class);

		Integer type = reqData.getType();

		MemberFriendReport friendReportRecord = new MemberFriendReport();
		friendReportRecord.setMember(member);
		friendReportRecord.setFriend(new Member(Long.parseLong(reqData.getFriendId())));
		friendReportRecord.setType(MemberFriendReport.Type.values()[type]);
		friendReportRecord.setTitle(reqData.getTitle());
		friendReportRecord.setContent(reqData.getContent());
		friendReportRecord.setImages(reqData.getImages());
		friendReportRecord.setStatus(MemberFriendReport.Status.pendingProcess);
		memberFriendReportService.save(friendReportRecord);
		return ResponseData.success("举报好友成功");
	}

	/**
	 * 通过好友ID查询好友基本信息
	 */
	@RequestMapping(value = "/getFriendBasicById")
	public @ResponseBody ResponseData getFriendBasicById(HttpServletRequest request) {
		Member member = memberService.getCurrent2();
		String param = request.getParameter("param");
		FriendIdRequestParam reqData = JSONObject.parseObject(param, FriendIdRequestParam.class);
		FriendBasicResponseResult result = new FriendBasicResponseResult();

		Member friend = memberService.get(Long.valueOf(reqData.getFriendId()));
		if (friend == null) {
			return ResponseData.error("操作成功:查询结果为空");
		}
		friend = MemUtils.mask(friend);
		if (StringUtils.isBlank(friend.getName())) {
			friend.setName(friend.getUsername());
		}
		result.setFriendId(String.valueOf(friend.getId()));
		result.setName(friend.getName());
		result.setPhoneNo(friend.getUsername());
		result.setHeadImage(friend.getHeadImage());
		result.setRankName(friend.getMemberRank().getRankNo());

		MemberFriendRelation friendRelation = friendRelationService.findByMemberIdAndFriendId(member.getId(),
				friend.getId());
		result.setStatus(friendRelation == null ? 0 : 1);

		return ResponseData.success("查询成功", result);
	}

	/**
	 * 通过手机号查好友
	 */
	@AccessLimit(maxCount = 1, seconds = 1)
	@RequestMapping(value = "/getFriendBasicByPhoneNo")
	public @ResponseBody ResponseData getFriendBasicByPhoneNo(HttpServletRequest request) {

		Member member = memberService.getCurrent();
		logger.warn("当前用户是：{}",member.getId());
		if(!VerifiedUtils.isVerified(member.getVerifiedList(), 1)) 
		{
			return ResponseData.error("尚未实名认证,请先实名认证");
		}
		FriendBasicResponseResult result = new FriendBasicResponseResult();
		String param = request.getParameter("param");
		FriendPhoneNoRequestParam reqData = JSONObject.parseObject(param, FriendPhoneNoRequestParam.class);
		String phoneNo = reqData.getPhoneNo();

		if (!phoneNo.matches("[0-9]*") || phoneNo.length() < 5 || phoneNo.length() > 11) {
			return ResponseData.error("无此好友");
		}

		Member friend = memberService.findByUsername(phoneNo);
		if (friend == null) {
			return ResponseData.error("无此好友");
		}
		friend = MemUtils.mask(friend);
		// 后期加次数校验
		result.setFriendId(String.valueOf(friend.getId()));
		if (StringUtils.isBlank(friend.getName())) {
			friend.setName(friend.getUsername());
		}
		result.setName(friend.getName());
		result.setPhoneNo(friend.getUsername());
		result.setHeadImage(friend.getHeadImage());
		result.setRankName(friend.getMemberRank().getRankNo());

		Boolean flag = friendRelationService.checkFriendRelation(member.getId(), friend.getId());
		result.setStatus(flag == null ? 0 : (flag == true ? 1 : 0));

		return ResponseData.success("通过手机号查好友成功", result);
	}

	/**
	 * 好友详情(通过好友ID)
	 */
	@RequestMapping(value = "/getFriendDetailById")
	public @ResponseBody ResponseData getFriendDetailById(HttpServletRequest request) {
		Member member = memberService.getCurrent2();
		FriendDetailResponseResult result = new FriendDetailResponseResult();
		String param = request.getParameter("param");
		FriendIdRequestParam reqData = JSONObject.parseObject(param, FriendIdRequestParam.class);

		String friendId = reqData.getFriendId();
		Member friend = memberService.get(Long.valueOf(friendId));

		if (StringUtils.isBlank(friend.getName())) {
			friend.setName(friend.getUsername());
		}
		result.setFriendId(friendId);
		result.setHeadImage(friend.getHeadImage());
		result.setName(friend.getName());
		
		String appVersion = request.getHeader("x-appVersion");
		RcQuota memberQuota = rcQuotaService.getByMemberId(friend.getId());
		if(StringUtils.equals(appVersion, "4.10")){
			result.setQuotaAssessment("");
		}else {
			if(memberQuota != null && memberQuota.getQuota() != null) {
				String quotaAssesment = String.format(RcConstant.REMINDER_OF_QUOTA_IN_MEMBERINFO_PAGE, StringUtils.numberToStringWithComma(memberQuota.getQuota()));
				result.setQuotaAssessment(quotaAssesment);
			}else {
				result.setQuotaAssessment(RcConstant.REMINDER_OF_NO_QUOTA_IN_FRIENDDETAIL_PAGE);
			}
		}
		Boolean verified = VerifiedUtils.isVerified(friend.getVerifiedList(), 1);
		Boolean verified2 = VerifiedUtils.isVerified(friend.getVerifiedList(), 2);
		result.setRealIdentityStatus(verified == true && verified2 == true ? 1 : 0);
		result.setRankName(friend.getMemberRank().getRankNo());
		MemberFriendRelation friendRelation = friendRelationService.findByMemberIdAndFriendId(member.getId(),
				friend.getId());
		result.setIsFriend(friendRelation == null ? 0 : 1);
		// 判断有权查看免费信用报告
		NfsLoanRecord findMyandFriendLoan = nfsLoanRecordService.findMyandFriendLoan(member, friend);
		if (findMyandFriendLoan == null) {
			if (friendRelation != null && friendRelation.getFreeCaAuthStatus().ordinal() == 2
					&& DateUtils.pastHour(friendRelation.getFreeCaAuthTime()) < 2) {
				result.setIsFreeCa(1);
			} else {
				result.setIsFreeCa(0);
			}

		}else {
			result.setIsFreeCa(1);	
		}

		CaAuthResponseResult caAuthResponseResult = new CaAuthResponseResult();
		List<UserBriefLegalize> ublList = new ArrayList<UserBriefLegalize>();
		int renZhengNum = rcCaDataService.getRenZhengNum(friend); // 统计认证的数量
		// 信用报告
		UserBriefLegalize ubl = new CaAuthResponseResult().new UserBriefLegalize();
		ubl.setStatus(1);
		ubl.setName("借贷记录");
		ubl.setImage(Global.getConfig("domain")+Global.getConfig("caIcon.xy_1"));
		ubl.setDate(DateUtils.formatDate(friend.getCreateTime()));
		ubl.setUrl("");
		ubl.setJumpStatus("xinyong");
		ubl.setAuthenType(0);
		ubl.setPageSatus(1);
		ubl.setSingleCaReportUrl(Global.getConfig("domain")+"/gxt/report_xyjl?key="+friend.getSafeKeyValue()+"&type=xinyong");
		ublList.add(ubl);
		for (RcCaDataV2.Type type : RcCaDataV2.Type.values()) {
			UserBriefLegalize renZhengData = new CaAuthResponseResult().new UserBriefLegalize();
			renZhengData = rcCaDataServiceV2.obtainRenZhengData(friend, type);
			ublList.add(renZhengData);
		}
//		// 创建实体类RcCaData
//		RcCaData caData = new RcCaData();
//		caData.setPhoneNo(friend.getUsername());
//		// 淘宝
//		caData.setType(RcCaData.Type.taobao);
//		UserBriefLegalize renZhengDataTB = rcCaDataService.getRenZhengData(caData, 4);
//		ublList.add(renZhengDataTB);
//		// 运营商
//		caData.setType(RcCaData.Type.yunyingshang_report);
//		UserBriefLegalize renZhengDataYYS = rcCaDataService.getRenZhengData(caData, 4);
//		ublList.add(renZhengDataYYS);
//		// 芝麻分
//		caData.setType(RcCaData.Type.zhimafen);
//		UserBriefLegalize renZhengDataZM = rcCaDataService.getRenZhengData(caData, 4);
//		ublList.add(renZhengDataZM);
//		// 学信网
//		caData.setType(RcCaData.Type.xuexinwang);
//		UserBriefLegalize renZhengDataxx = rcCaDataService.getRenZhengData(caData, 4);
//		ublList.add(renZhengDataxx);
//		// 社保
//		caData.setType(RcCaData.Type.shebao);
//		UserBriefLegalize renZhengDataSB = rcCaDataService.getRenZhengData(caData, 4);
//		ublList.add(renZhengDataSB);
//		// 公积金
//		caData.setType(RcCaData.Type.gongjijin);
//		UserBriefLegalize renZhengDataGJ = rcCaDataService.getRenZhengData(caData, 4);
//		ublList.add(renZhengDataGJ); 
//		// 网银
//		caData.setType(RcCaData.Type.wangyin);
//		UserBriefLegalize renZhengDataWY = rcCaDataService.getRenZhengData(caData, 4);
//		ublList.add(renZhengDataWY);
		caAuthResponseResult.setUserBriefLegalize(ublList);
		caAuthResponseResult.setCounNum(renZhengNum + "");
		// 判断是否支付过一元
		// 获取用户的身份证号
//		MemberVerified verifiedByMemberId = memberVerifiedService.getVerifiedByMemberId(friend.getId());
		RcXinyan xinyan = new RcXinyan();
		xinyan.setIdNo(friend.getIdNo());
		RcXinyan getxinyanData = rcXinyanService.getxinyanData(xinyan);
		caAuthResponseResult.setPageType(1);
		if (getxinyanData == null) {
			caAuthResponseResult.setPageType(0);
		} else {
			if ("N".equals(getxinyanData.getIsSelfbuy())) {
				if (friendRelation.getChargeCaStatus().ordinal() == 1) {
					if (DateUtils.pastDays(friendRelation.getChargeCaTime()) > 30) {
						caAuthResponseResult.setPageType(0);
					}
				} else {
					caAuthResponseResult.setPageType(0);
				}
			}
		}
		caAuthResponseResult.setCaReportUrl(Global.getConfig("domain")+"/gxt/report?key="+friend.getSafeKeyValue());
		result.setCaAuthResponseResult(caAuthResponseResult);
		return ResponseData.success("查询好友详情成功", result);
	}

	/**
	 * 扫描二维码
	 */
	@RequestMapping(value = "/qrcodeAddFriend")
	@ResponseBody
	public ResponseData qrcode(HttpServletRequest request) {
		String param = request.getParameter("param");
		QrcodeAddFriendRequestParam qrcodeAddFriendRequestParam = JSONObject.parseObject(param,
				QrcodeAddFriendRequestParam.class);
		Long friendId = Long.valueOf(qrcodeAddFriendRequestParam.getMemberId());
		Member member = memberService.getCurrent2();
		Member friend = memberService.get(friendId);
		if (friend == null) {
			return ResponseData.error("操作成功:查询结果为空");
		}
		friend = MemUtils.mask(friend);
		if (StringUtils.isBlank(friend.getName())) {
			friend.setName(friend.getUsername());
		}
		QrcodeAddFriendResponseResult result = new QrcodeAddFriendResponseResult();

		result.setFriendId(String.valueOf(friend.getId()));
		result.setName(friend.getName());
		result.setPhoneNo(friend.getUsername());
		result.setHeadImage(friend.getHeadImage());
		result.setNickname(friend.getNickname());
		result.setRankName(friend.getMemberRank().getRankNo());
		MemberFriendRelation friendRelation = friendRelationService.findByMemberIdAndFriendId(member.getId(),friend.getId());
		result.setStatus(friendRelation == null ? 0 : 1);

		return ResponseData.success("通过id查好友成功", result);
	}

	/**
	 * 通过好友ID查询好友逾期报告
	 */
	@RequestMapping(value = "/getOverdueReportById")
	public @ResponseBody ResponseData getOverdueReportById(HttpServletRequest request) {

		String param = request.getParameter("param");
		FriendIdRequestParam reqData = JSONObject.parseObject(param, FriendIdRequestParam.class);
		FriendOverdueReportResponseResult result = new FriendOverdueReportResponseResult();

		Member member = memberService.getCurrent2();
		/** 检查是否为好友关系 */
		Boolean flag = friendRelationService.checkFriendRelation(member.getId(), Long.valueOf(reqData.getFriendId()));
		if (!flag) {
			return ResponseData.error("查询失败,好友已经把你删除");
		}

		
		Member friend = memberService.get(Long.valueOf(reqData.getFriendId()));
		if (friend == null) {
			return ResponseData.warn("该好友已注销");
		}
		String idNo = friend.getIdNo();
		if(StringUtils.isBlank(idNo)) {
			return ResponseData.warn("您的好友还未做实名认证，请提醒该好友做实名认证。");
		}
		Integer age = IdCardUtils.getAge(idNo);
		friend = MemUtils.mask(friend);
		if (StringUtils.isBlank(friend.getName())) {
			friend.setName(friend.getUsername());
		}
		result.setFriendId(String.valueOf(friend.getId()));
		result.setName(friend.getName());
		result.setPhoneNo(friend.getUsername());
		result.setIdNo(friend.getIdNo());
		result.setAge(age);

		/** 查询好友逾期借条 */
		NfsLoanRecord nfsLoanRecord = new NfsLoanRecord();
		nfsLoanRecord.setLoanee(friend);
		nfsLoanRecord.setStatus(NfsLoanRecord.Status.overdue);
		Date endDueRepayDate = DateUtils.addCalendarByDate(new Date(), -15);
		nfsLoanRecord.setEndDueRepayDate(endDueRepayDate);
		List<NfsLoanRecord> loanList = loanRecordService.findList(nfsLoanRecord);
		BigDecimal overDueAmount = BigDecimal.ZERO;
		for (NfsLoanRecord loanRecord : loanList) {
			overDueAmount = overDueAmount.add(loanRecord.getAmount());
		}

		if (overDueAmount.compareTo(BigDecimal.ZERO) > 0) {
			result.setQuantity(loanList.size());
			result.setAmount(overDueAmount.toString());
			return ResponseData.success("查询成功", result);
		} else {
			return ResponseData.warn("您的好友在无忧平台信誉良好,没有15天未还的借条");
		}

	}

}