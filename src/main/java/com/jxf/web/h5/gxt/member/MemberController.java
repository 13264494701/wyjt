package com.jxf.web.h5.gxt.member;



import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.jxf.loan.entity.NfsLoanArbitration;
import com.jxf.loan.entity.NfsLoanArbitrationDetail;
import com.jxf.loan.entity.NfsLoanCollection;
import com.jxf.loan.entity.NfsLoanCollectionDetail;
import com.jxf.loan.entity.NfsLoanRecord;
import com.jxf.loan.service.NfsLoanArbitrationDetailService;
import com.jxf.loan.service.NfsLoanArbitrationService;
import com.jxf.loan.service.NfsLoanCollectionDetailService;
import com.jxf.loan.service.NfsLoanCollectionService;
import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberFeedback;
import com.jxf.mem.entity.MemberVideoVerify;
import com.jxf.mem.entity.MemberVideoVerify.Status;
import com.jxf.mem.service.MemberFeedbackService;
import com.jxf.mem.service.MemberService;
import com.jxf.mem.service.MemberVideoVerifyService;
import com.jxf.mem.utils.MemUtils;
import com.jxf.mem.utils.VerifiedUtils;
import com.jxf.mms.msg.SendSmsMsgService;
import com.jxf.svc.cache.RedisUtils;
import com.jxf.svc.security.PasswordUtils;
import com.jxf.web.minipro.BaseController;
import com.jxf.web.model.ResponseData;
import com.jxf.web.model.gxt.MemberInfoGxtResponseResult;
import com.jxf.web.model.wyjt.app.member.ChangeNicknameRequestParam;
import com.jxf.web.model.wyjt.app.member.ChangeShowCurBalRequestParam;
import com.jxf.web.model.wyjt.app.member.CheckLoginPwdRequestParam;
import com.jxf.web.model.wyjt.app.member.IdentityInfoResponseResult;
import com.jxf.web.model.wyjt.app.member.RemainderLoanLimitResponseResult;
import com.jxf.web.model.wyjt.app.member.SetAddresRequestParam;
import com.jxf.web.model.wyjt.app.member.SetHeadImageRequestParam;

/**
 * Controller - 会员中心
 * 
 * @author xrd
 */
@Controller("gxtH5MemberController")
@RequestMapping(value="${gxtH5}/member")
public class MemberController extends BaseController {

	@Autowired
	private MemberService memberService;
	@Autowired
	private MemberFeedbackService memberFeedbackService;
	@Autowired
    private NfsLoanCollectionService nfsLoanCollectionService;
	@Autowired
    private NfsLoanArbitrationService nfsLoanArbitrationService;
	@Autowired
	private NfsLoanArbitrationDetailService nfsLoanArbitrationDetailService;
	@Autowired
	private NfsLoanCollectionDetailService nfsLoanCollectionDetailService;
	@Autowired
	private  SendSmsMsgService sendSmsMsgService;//短信
	@Autowired
	private MemberVideoVerifyService memberVideoVerifyService;

	/**
	 * 会员信息
	 */
	@RequestMapping(value = "/info")
	public @ResponseBody
	ResponseData info(HttpServletRequest request){
		
		Member member = memberService.getCurrent();
		MemberInfoGxtResponseResult result = memberService.getMemberInfoGxt(member);
		
		return ResponseData.success("查询成功",result);
	}
	/**
	 * 会员实名认证信息
	 */
	@RequestMapping(value = "/identityInfo")
	public @ResponseBody
	ResponseData identityInfo(HttpServletRequest request){
		
		Member member = memberService.getCurrent();
		IdentityInfoResponseResult result = new IdentityInfoResponseResult();
		result.setName(member.getName());
		member = MemUtils.maskIdNo(member);
		result.setIdNo(member.getIdNo());
		Integer verifiedList = member.getVerifiedList();
		result.setRealIdentityStatus(VerifiedUtils.isVerified(verifiedList, 1) && VerifiedUtils.isVerified(verifiedList, 2)?1:0);
		return ResponseData.success("查询会员实名认证信息成功",result);
	}
	/**
	 * 设置头像
	 * @param request
	 * @return
	 */
	@RequestMapping(value="setHeadImage")
	@ResponseBody
	public ResponseData setHeadImage(HttpServletRequest request) {
		
		Member member = memberService.getCurrent();
		String param = request.getParameter("param");
		SetHeadImageRequestParam reqData = JSONObject.parseObject(param,SetHeadImageRequestParam.class);

		member.setHeadImage(reqData.getHeadImage());
		memberService.save(member);
		RedisUtils.put("memberInfo"+member.getId(),"headImage",reqData.getHeadImage());
				
		return ResponseData.success("保存头像成功");
	}
	/**
	 * 设置邮箱
	 * @param email
	 * @return
	 */
	@RequestMapping(value="setEmail")
	@ResponseBody
	public ResponseData setEmail(String email) {
		if(email.trim().length() <= 5 || !email.contains("@")) {
			return ResponseData.error("无效的邮箱地址！");
		}
		Member member = memberService.getCurrent();
		member.setEmail(email);
		Integer verifiedList = member.getVerifiedList();
		Integer addVerified = VerifiedUtils.addVerified(verifiedList, 23);
		member.setVerifiedList(addVerified);
		memberService.save(member);
		RedisUtils.put("memberInfo"+member.getId(),"email",email);
		RedisUtils.put("memberInfo"+member.getId(),"emailStatus",1);
		return ResponseData.success("邮箱设置成功");
	}
	
	/**
	 * 更改昵称
	 * @param request
	 * @return
	 */
	@RequestMapping(value="changeNickname")
	@ResponseBody
	public ResponseData changeNickname(HttpServletRequest request) {
		String param = request.getParameter("param");
		ChangeNicknameRequestParam reqData = JSONObject.parseObject(param,ChangeNicknameRequestParam.class);
		String newNickname = reqData.getNickname();
		if(StringUtils.isBlank(newNickname)) {
			return ResponseData.error("无效的昵称！请重新填写");
		}
		Member member = memberService.getCurrent();

		member.setNickname(newNickname);
		memberService.save(member);
		RedisUtils.put("memberInfo"+member.getId(),"nickname",newNickname);
		return ResponseData.success("昵称修改成功");
	}
	
	/**
	 * 	更换手机号前实名认证
	 * @param 
	 * @return
	 */
	@RequestMapping(value="beforeChangePhoneNo")
	@ResponseBody
	public ResponseData beforeChangePhoneNo() {
		Member member = memberService.getCurrent();
		
		int n = memberVideoVerifyService.getChangePhoneNoCounts(member);
		if(n > 2) {
			return ResponseData.error("每年修改手机号 次数超限");
		}
		
		MemberVideoVerify videoVerify = new MemberVideoVerify();
		videoVerify.setMember(member);
		videoVerify.setStatus(MemberVideoVerify.Status.pendingReview);
		videoVerify.setType(MemberVideoVerify.Type.changePhone);
		videoVerify.setChannel(MemberVideoVerify.Channel.gxt);
		MemberVideoVerify findPendingReviewVerify = memberVideoVerifyService.get(videoVerify);
		if(findPendingReviewVerify != null){
			return ResponseData.error("身份信息已提交，请勿重复提交!");
		}
		//检查认证失败次数
		Integer failureTimes = memberVideoVerifyService.countFailure(member.getId());
		if(failureTimes > 3){
			return ResponseData.error("您尝试认证失败，不能再继续尝试。如需重新认证请联系客服.");
		}
		
		HashMap<String, Object> result = new HashMap<String,Object>();
		
		String h5Url = "";
		// 人脸demo数据
		memberVideoVerifyService.save(videoVerify);
		h5Url = memberVideoVerifyService.getGxtResult(2,videoVerify.getId().toString(),member);
		
		result.put("h5Url", h5Url);
		result.put("orderId", videoVerify.getId() == null ? "" : videoVerify.getId().toString());
		
		return ResponseData.success("成功",result);
	}
	
	/**
	 * 	更换手机号
	 * @param phoneNo,smsCode,type
	 * @return
	 */
	@RequestMapping(value="changePhoneNo")
	@ResponseBody
	public ResponseData changePhoneNo(String phoneNo, String smsCode, String type) {
		Member member = memberService.getCurrent();
		String username = member.getUsername();
		if (StringUtils.isBlank(phoneNo) || StringUtils.isBlank(smsCode)) {
            return ResponseData.error("手机号或者验证码为空");
        }
		String serverSmsCode = RedisUtils.get("smsCode" + phoneNo);
        if (StringUtils.isBlank(serverSmsCode) || !StringUtils.equals(serverSmsCode, smsCode)) {
        	return ResponseData.error("短信验证码错误 请重试");
        }
		
        Member isMember = memberService.findByUsername(phoneNo);
		if(isMember != null) {
			return ResponseData.error("当前的手机号已存在");
		}
		ResponseData responseData = memberService.dealPhoneNo(member,type,phoneNo);
		// 短信
		sendSmsMsgService.sendMessage("changPhoneNo", username, null);
		return responseData;
	}

	/**
	 * 	保存地址
	 * @param request
	 * @return
	 */
	@RequestMapping(value="setAddress")
	@ResponseBody
	public ResponseData setAddress(HttpServletRequest request) {
		String param = request.getParameter("param");
		SetAddresRequestParam reqData = JSONObject.parseObject(param,SetAddresRequestParam.class);
		String newAddres = reqData.getAddres();
		if(StringUtils.isBlank(newAddres)) {
			return ResponseData.error("请填写正确的地址！");
		}
		Member member = memberService.getCurrent();
	
		member.setAddr(newAddres);
		memberService.save(member);
		RedisUtils.put("memberInfo"+member.getId(),"addr",newAddres);
		return ResponseData.success("地址保存成功");
	}
	
	/**
	 * 提交意见
	 * @param request
	 * @return
	 */
	@RequestMapping(value="feedback")
	@ResponseBody
	public ResponseData feedback(HttpServletRequest request) {
		String note = request.getParameter("note");
		if(StringUtils.isBlank(note)) {
			return ResponseData.error("请填写您的意见");
		}
		Member member = memberService.getCurrent();
		MemberFeedback feedback = new MemberFeedback();
		feedback.setMember(member);
		feedback.setNote(note);
		memberFeedbackService.save(feedback);
		return ResponseData.success("意见提交成功");
	}
	
	
	
	/**
	 * 查询剩余可借额度
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "getRemainderLoanLimit")
	@ResponseBody
	public ResponseData getRemainderLoanLimit(HttpServletRequest request) {
		//Member member = memberService.getCurrent();
		//计算剩余可借额度
		RemainderLoanLimitResponseResult result = new RemainderLoanLimitResponseResult();
		result.setRemainderLoanLimit("10000");
		return ResponseData.success("查询剩余可借额度成功",result);
	}
	
	/**
	 * 更新显示余额标志
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "changeShowCurBal")
	@ResponseBody
	public ResponseData changeShowCurBal(HttpServletRequest request) { 
		
		Member member = memberService.getCurrent2();
		String param = request.getParameter("param");
		ChangeShowCurBalRequestParam reqData = JSONObject.parseObject(param,ChangeShowCurBalRequestParam.class);
		
		RedisUtils.put("memberInfo"+member.getId(),"showCurBal",reqData.getShowCulBal());
		
		return ResponseData.success("成功更新显示余额标志");
	}

//	/**
//	 * 信誉
//	 * @param model
//	 * @return
//	 */
//	@RequestMapping("/creditRating")
//	public String reputation(Model model) {
//		Member member = memberService.getCurrent();
//		String appPlatform = (String) RedisUtils.getHashKey("loginInfo" + member.getId(), "osType");
//		boolean isWeiXin = false;
//		if (com.jxf.svc.utils.StringUtils.startsWith(appPlatform, "weixin_")) {
//			isWeiXin = true;
//			appPlatform = appPlatform.replace("weixin_", "");
//		}
//		model.addAttribute("isWeiXin", isWeiXin);
//		model.addAttribute("appPlatform", appPlatform);
//		model.addAttribute("member", member);
//		if ("C".equals(member.getMemberRank().getRankNo())) {
//			return "app/page/grade-meterC";
//		} else {
//			if ("B".equals(member.getMemberRank().getRankNo())) {
//				model.addAttribute("className","grayBg");
//				model.addAttribute("title","很遗憾");
//				model.addAttribute("tips","您的信用降级了");
//				model.addAttribute("tips1","信用较差");
//				model.addAttribute("tips2","您有逾期账单，尽早还款挽回信誉！");
//			} else {
//				model.addAttribute("className","xinyongdj");
//				if("AAAAA".equals(member.getMemberRank().getRankNo())){
//					model.addAttribute("title","太棒啦!");
//					model.addAttribute("tips","您已达到信用巅峰");
//					model.addAttribute("tips1","信用超级好");
//					model.addAttribute("tips2","我击败了全国99.99%的用户");
//				} else {
//					model.addAttribute("tips","讲信用 才有真朋友");
//				}
//			}
//			return "app/page/grade-meter";
//		}
//	}

	@RequestMapping("/collectionRecordDetail")
	public String collectionDetail(Model model, Long id, String appPlatform) {
		NfsLoanCollectionDetail nfsLoanCollectionDetail = new NfsLoanCollectionDetail();
		NfsLoanCollection collection = nfsLoanCollectionService.get(id);
		nfsLoanCollectionDetail.setCollectionId(collection.getId());
		model.addAttribute("detail",nfsLoanCollectionDetailService.findList(nfsLoanCollectionDetail));
		model.addAttribute("appPlatform", appPlatform);
		return "app/collection/collectionSchedule";
	}

	@RequestMapping("/arbitrationRecordDetail")
	public String arbitrationDetail(Model model, Long id, String appPlatform) {
		NfsLoanArbitrationDetail nfsLoanArbitrationDetail = new NfsLoanArbitrationDetail();
		nfsLoanArbitrationDetail.setArbitrationId(id);
		model.addAttribute("detail",nfsLoanArbitrationDetailService.findList(nfsLoanArbitrationDetail));
		model.addAttribute("appPlatform", appPlatform);
		return "app/arbitration/lawsuitSchedule";
	}

	/**
	 * 查看我的催收和仲裁记录
	 * @param model
	 * @return
	 */
	@RequestMapping("/collectionAndArbitrationRecord")
	public String collectionAndArbitration(HttpServletRequest request, Model model,String type,String version,String pt) {
		Member member = memberService.getCurrent2();
		if (member != null) {
			String appPlatform = (String) RedisUtils.getHashKey("loginInfo" + member.getId(), "osType");
			boolean isWeiXin = false;
			if (com.jxf.svc.utils.StringUtils.startsWith(appPlatform, "weixin_")) {
				isWeiXin = true;
				appPlatform = appPlatform.replace("weixin_", "");
			}
			model.addAttribute("isWeiXin", isWeiXin);
			model.addAttribute("appPlatform", appPlatform);
		}
		if (StringUtils.equals(type, "1")) {
			NfsLoanArbitration nfsLoanArbitration = new NfsLoanArbitration();
			nfsLoanArbitration.setMember(member);
			model.addAttribute("arbitrationList", nfsLoanArbitrationService.findMemberArbitrationList(nfsLoanArbitration));
		} else {
			type = "0";
			NfsLoanCollection nfsLoanCollection = new NfsLoanCollection();
			NfsLoanRecord loanRecord = new NfsLoanRecord();
			loanRecord.setLoaner(member);
			nfsLoanCollection.setLoan(loanRecord);
			model.addAttribute("collectionList", nfsLoanCollectionService.findList(nfsLoanCollection));
		}
		model.addAttribute("type",type);
		String memberToken = request.getHeader("x-memberToken");
		model.addAttribute("memberToken",memberToken);
		
		String appVersion = request.getHeader("x-appVersion");
		String osType = request.getHeader("x-osType");
		model.addAttribute("version",version == null ? appVersion  : version);
		model.addAttribute("pt",pt == null ? osType  : pt);
		
		return "app/page/my-list-prepay";
	}

	/**
	 * 校验登陆密码
	 */
	@RequestMapping(value = "/checkLoginPwd")
	public @ResponseBody
	ResponseData checkLoginPwd(HttpServletRequest request){
		Member member = memberService.getCurrent();
		String param = request.getParameter("param");
		CheckLoginPwdRequestParam reqData = JSONObject.parseObject(param,CheckLoginPwdRequestParam.class);
		String pwd = reqData.getPwd();
		boolean validatePassword = PasswordUtils.validatePassword(pwd, member.getPassword());
		if(validatePassword){
			return ResponseData.success("登录密码正确");
		}else{
			return ResponseData.error("登录密码错误");
		}
	}
	
	/**
	 * 设置邮箱
	 * @param email
	 * @return
	 */
	@RequestMapping(value="initEmail")
	@ResponseBody
	public ResponseData initEmail(String email ,String orderId) {
		if(StringUtils.isBlank(orderId)){
			return ResponseData.error("后端程序异常！");
		}
		
		if(email.trim().length() <= 5 || !email.contains("@")) {
			return ResponseData.error("无效的邮箱地址！");
		}
		MemberVideoVerify memberVideoVerify = memberVideoVerifyService.get(Long.parseLong(orderId));
		Status status = memberVideoVerify.getStatus();
		if(!status.equals(Status.verified)){
			return ResponseData.error("实名认证未通过！");
		}
		
		Member member = memberService.getCurrent();
		member.setEmail(email);
		Integer verifiedList = member.getVerifiedList();
		Integer addVerified = VerifiedUtils.addVerified(verifiedList, 23);
		member.setVerifiedList(addVerified);
		memberService.save(member);
		RedisUtils.put("memberInfo"+member.getId(),"email",email);
		RedisUtils.put("memberInfo"+member.getId(),"emailStatus",1);
		return ResponseData.success("初始化邮箱成功");
	}
	

}