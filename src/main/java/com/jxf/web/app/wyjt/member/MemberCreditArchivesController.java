package com.jxf.web.app.wyjt.member;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.ebaoquan.rop.thirdparty.org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jxf.loan.entity.NfsLoanRecord;
import com.jxf.loan.service.NfsLoanRecordService;
import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberFriendCa;
import com.jxf.mem.entity.MemberFriendRelation;
import com.jxf.mem.entity.MemberMessage;
import com.jxf.mem.service.MemberFriendCaService;
import com.jxf.mem.service.MemberFriendRelationService;
import com.jxf.mem.service.MemberMessageService;
import com.jxf.mem.service.MemberService;
import com.jxf.mem.utils.H5Utils;
import com.jxf.mem.utils.MemUtils;
import com.jxf.mms.service.SendMsgService;
import com.jxf.nfs.constant.TrxRuleConstant;
import com.jxf.nfs.service.NfsActService;
import com.jxf.rc.entity.RcCaData;
import com.jxf.rc.entity.RcCaDataV2;
import com.jxf.rc.entity.RcCaYysDetails;
import com.jxf.rc.entity.RcXinyan;
import com.jxf.rc.service.RcCaDataService;
import com.jxf.rc.service.RcCaDataServiceV2;
import com.jxf.rc.service.RcCaYysDetailsService;
import com.jxf.rc.service.RcXinyanService;
import com.jxf.rc.utils.CreditRadarAction;
import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.Encodes;
import com.jxf.svc.utils.EncryptUtils;
import com.jxf.svc.utils.JSONUtil;
import com.jxf.svc.utils.ObjectUtils;
import com.jxf.web.minipro.BaseController;
import com.jxf.web.model.InviteFriendPayRequestParam;
import com.jxf.web.model.ResponseData;
import com.jxf.web.model.wyjt.app.creditArchives.ApplyAuthRequestParam;
import com.jxf.web.model.wyjt.app.creditArchives.ReplyAuthRequestParam;
import com.jxf.web.model.wyjt.app.creditArchives.SendAuthRequestParam;
import com.jxf.web.model.wyjt.app.member.AuthBackRequestParam;
import com.jxf.web.model.wyjt.app.member.CaAuthResponseResult;
import com.jxf.web.model.wyjt.app.member.CaAuthResponseResult.UserBriefLegalize;
import com.jxf.web.model.wyjt.app.member.CredictBaoGaoRecodeRequestParam;
import com.jxf.web.model.wyjt.app.member.CredictBaoGaoRecodeResponseResult;
import com.jxf.web.model.wyjt.app.member.CredictBaoGaoRecodeResponseResult.CredictBaoGaoRecode;
import com.jxf.web.model.wyjt.app.member.CreditParamsRequestParam;
import com.jxf.web.model.wyjt.app.member.CreditParamsResponseResult;
import com.jxf.web.model.wyjt.app.member.CreditReportRequestParam;
import com.jxf.web.model.wyjt.app.member.CreditReportResponseResult;
import com.jxf.web.model.wyjt.app.member.CreditReportResponseResult.CreditTable;
import com.jxf.web.model.wyjt.app.member.CreditaInformationRequestParam;
import com.jxf.web.model.wyjt.app.member.CreditaInformationResponseResult;
import com.jxf.web.model.wyjt.app.member.SingleCreditReportRequestParam;
import com.jxf.web.model.wyjt.app.member.XinYongLeiDaDataResponseResult;

/**
 * Controller - 信用档案
 * 
 * @author xrd
 */
@Controller("wyjtAppCreditArchivesController")
@RequestMapping(value = "${wyjtApp}/member")
public class MemberCreditArchivesController extends BaseController {

	@Autowired
	private MemberService memberService;

	@Autowired
	private MemberFriendRelationService memberFriendRelationService;

	@Autowired
	private MemberFriendCaService memberFriendCaService;

	@Autowired
	private RcCaDataService rcCaDataService;


	@Autowired
	private RcCaYysDetailsService rcCaYysDetailsService;

	@Autowired
	private RcXinyanService rcXinyanService;

	@Autowired
	private NfsLoanRecordService loanRecordService;

	@Autowired
	private MemberMessageService memberMessageService;
	@Autowired
	private NfsActService actService;
	
	@Autowired
	private SendMsgService sendMsgService;
	@Autowired
	private RcCaDataServiceV2 rcCaDataServiceV2;

	/**
	 * 申请查看信用档案
	 */
	@RequestMapping(value = "/applyAuth")
	public @ResponseBody ResponseData applyAuth(HttpServletRequest request) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR, -2);
		Member member = memberService.getCurrent();
		String param = request.getParameter("param");
		ApplyAuthRequestParam reqData = JSONObject.parseObject(param, ApplyAuthRequestParam.class);
		String friendId1 = reqData.getFriendId();
		long friendId = Long.parseLong(friendId1);
		// 好友信息
		Member friend = memberService.get(friendId);
		MemberFriendRelation memberFriendRelation = memberFriendRelationService
				.findByMemberIdAndFriendId(member.getId(), friendId);
		if (memberFriendRelation == null) {
			return ResponseData.error("目前您还不是他的好友请您先加他为好友");
		}
		if (memberFriendRelation.getFreeCaAuthStatus().ordinal() == 1) {

			if (memberFriendRelation.getFreeCaAuthTime().getTime() >= cal.getTimeInMillis()) {
				// 申请是否过期
				return ResponseData.error("您查看好友的信用档案申请未过期，请您不要重复申请");
			}
		} else if (memberFriendRelation.getFreeCaAuthStatus().ordinal() == 2) {
			if (memberFriendRelation.getFreeCaAuthTime().getTime() >= cal.getTimeInMillis()) {
				// 申请是否过期
				return ResponseData.error("您查看好友的信用档案未过期，请您不要重复申请");
			}
		}
		// 创建申请记录表
		MemberFriendCa ca = new MemberFriendCa();
		ca.setDrc(MemberFriendCa.Drc.apply);
		ca.setFriend(friend);
		ca.setMember(member);
		ca.setStatus(MemberFriendCa.Status.apply);
		ca.setType(MemberFriendCa.Type.free);
		memberFriendCaService.save(ca);
		memberFriendRelation.setFreeCaAuthStatus(MemberFriendRelation.FreeCaAuthStatus.applied);
		memberFriendRelation.setFreeCaAuthTime(new Date());
		memberFriendRelationService.update(memberFriendRelation);
		//发送会员消息
		MemberMessage sendMessage = memberMessageService.sendMessage(MemberMessage.Type.viewCreditFiles,ca.getId());
		// 消息推送
		sendMsgService.beforeSendAppMsg(sendMessage);
		return ResponseData.success("申请提交成功");
	}

	/**
	 * 同意/拒绝 查看信用档案
	 */
	@RequestMapping(value = "/replyAuth")
	public @ResponseBody ResponseData replyAuth(HttpServletRequest request) {
		Member member = memberService.getCurrent();
		String param = request.getParameter("param");
		ReplyAuthRequestParam reqData = JSONObject.parseObject(param, ReplyAuthRequestParam.class);
		String friendId1 = reqData.getFriendId();
		long friendId = Long.parseLong(friendId1);
		// 好友信息
		Member friend = memberService.get(friendId);
		MemberFriendRelation memberFriendRelation = memberFriendRelationService
				.findByMemberIdAndFriendId(friendId,member.getId());
		Integer isAgree = reqData.getIsAgree();
		if (memberFriendRelation == null) {
			return ResponseData.error("目前您还不是他的好友请您先加他为好友");
		}
		MemberFriendCa ca = new MemberFriendCa();

		ca.setDrc(MemberFriendCa.Drc.apply);
		ca.setFriend(member);
		ca.setMember(friend);
		ca.setStatus(MemberFriendCa.Status.apply);
		ca.setType(MemberFriendCa.Type.free);
		List<MemberFriendCa> findList = memberFriendCaService.findList(ca);
		if (findList.size() <= 0) {
			return ResponseData.error("该申请已失效，请您重新进行申请");
		}
		ca = findList.get(0);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", member.getName());
		if (isAgree == 0) {
			memberFriendRelation.setFreeCaAuthStatus(MemberFriendRelation.FreeCaAuthStatus.reject);
			ca.setStatus(MemberFriendCa.Status.refuse);
			//发送会员消息
			MemberMessage sendMessage = memberMessageService.sendMessage(MemberMessage.Type.refuseViewCreditFiles, ca.getId());
			// 消息推送
			sendMsgService.beforeSendAppMsg(sendMessage);

		} else if (isAgree == 1) {
			memberFriendRelation.setFreeCaAuthStatus(MemberFriendRelation.FreeCaAuthStatus.authorized);
			memberFriendRelation.setFreeCaAuthTime(new Date());
			ca.setStatus(MemberFriendCa.Status.agree);
			ca.setUpdateTime(new Date());
			//发送会员消息
			MemberMessage sendMessage = memberMessageService.sendMessage(MemberMessage.Type.agreeViewCreditFiles,ca.getId());
			// 消息推送
			sendMsgService.beforeSendAppMsg(sendMessage);
		}
		memberFriendRelation.setFreeCaAuthTime(new Date());
		memberFriendRelationService.update(memberFriendRelation);
		memberFriendCaService.save(ca);
		return ResponseData.success("处理申请成功");
	}

	/**
	 * 主动发送信用档案
	 */
	@RequestMapping(value = "/sendAuth")
	public @ResponseBody ResponseData sendAuth(HttpServletRequest request) {
		String param = request.getParameter("param");
		SendAuthRequestParam reqData = JSONObject.parseObject(param, SendAuthRequestParam.class);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR, -2);
		Member member = memberService.getCurrent();// 我是该会员
		String friendId1 = reqData.getFriendId();
		long friendId = Long.parseLong(friendId1);
		// 好友信息
		Member friend = memberService.get(friendId);
		MemberFriendRelation memberFriendRelation1 = memberFriendRelationService.findByMemberIdAndFriendId(member.getId(), friendId);
		if (memberFriendRelation1 == null) {
			return ResponseData.error("目前您还不是他的好友，请您先加他为好友");
		}
		// 查找出 好友关系的
		MemberFriendRelation memberFriendRelation = memberFriendRelationService
				.findByMemberIdAndFriendId(friend.getId(), member.getId());
		if (memberFriendRelation == null) {
			return ResponseData.error("目前他还不是您的好友，请您先加他为好友");
		}
		MemberFriendCa ca = new MemberFriendCa();
		ca.setDrc(MemberFriendCa.Drc.send);
		ca.setFriend(friend);
		ca.setMember(member);
		ca.setStatus(MemberFriendCa.Status.agree);
		ca.setType(MemberFriendCa.Type.free);
		List<MemberFriendCa> findList = memberFriendCaService.findList(ca);
		if (findList.size() > 0) {
			if (memberFriendRelation.getFreeCaAuthTime().getTime() >= cal.getTimeInMillis()) {
				// 申请是否过期
				return ResponseData.error("您已授权给该好友，对方可在2小时内直接查看");
			}

		}
		memberFriendCaService.save(ca);
		memberFriendRelation.setFreeCaAuthStatus(MemberFriendRelation.FreeCaAuthStatus.authorized);
		memberFriendRelation.setFreeCaAuthTime(new Date());
		memberFriendRelationService.update(memberFriendRelation);
		//发送会员消息
		MemberMessage sendMessage = memberMessageService.sendMessage(MemberMessage.Type.sendCreditFiles,ca.getId());
		// 消息推送
		sendMsgService.beforeSendAppMsg(sendMessage);
		return ResponseData.success("授权成功");
	}

	/**
	 * 我的认证信用档案界面
	 */
	@RequestMapping(value = "/caAuth")
	public @ResponseBody ResponseData mycreditfile(HttpServletRequest request) {
		Member member = memberService.getCurrent();
		CaAuthResponseResult result = new CaAuthResponseResult();
		List<UserBriefLegalize> userBriefLegalize = new ArrayList<UserBriefLegalize>();
		int renZhengNum = rcCaDataService.getRenZhengNum(member); // 统计认证的数量
		// 信用报告
		UserBriefLegalize xy = new CaAuthResponseResult().new UserBriefLegalize();
		xy.setStatus(1);
		xy.setName("信用记录");
		xy.setImage(Global.getConfig("domain")+Global.getConfig("caIcon.xy_1"));
		xy.setDate(DateUtils.formatDate(member.getCreateTime()));
		xy.setUrl("");
		xy.setJumpStatus("xinyong");
		xy.setAuthenType(0);
		userBriefLegalize.add(xy);
		// 创建实体类RcCaData
		RcCaData entity = new RcCaData();
		entity.setPhoneNo(member.getUsername());
		// 淘宝
		entity.setType(RcCaData.Type.taobao);
		UserBriefLegalize renZhengDataTB = rcCaDataService.getRenZhengData(entity, 2);
		userBriefLegalize.add(renZhengDataTB);
		// 运营商
		entity.setType(RcCaData.Type.yunyingshang_report);
		UserBriefLegalize renZhengDataYYS = rcCaDataService.getRenZhengData(entity, 1);
		userBriefLegalize.add(renZhengDataYYS);
		// 芝麻分
		entity.setType(RcCaData.Type.zhimafen);
		UserBriefLegalize renZhengDataZM = rcCaDataService.getRenZhengData(entity, 1);
		userBriefLegalize.add(renZhengDataZM);
		// 学信网
		entity.setType(RcCaData.Type.xuexinwang);
		UserBriefLegalize renZhengDataxx = rcCaDataService.getRenZhengData(entity, 1);
		userBriefLegalize.add(renZhengDataxx);
		// 社保
		entity.setType(RcCaData.Type.shebao);
		UserBriefLegalize renZhengDataSB = rcCaDataService.getRenZhengData(entity, 1);
		userBriefLegalize.add(renZhengDataSB);
		// 公积金
		entity.setType(RcCaData.Type.gongjijin);
		UserBriefLegalize renZhengDataGJ = rcCaDataService.getRenZhengData(entity, 1);
		userBriefLegalize.add(renZhengDataGJ);
		// 网银
		entity.setType(RcCaData.Type.wangyin);
		UserBriefLegalize renZhengDataWY = rcCaDataService.getRenZhengData(entity, 1);
		userBriefLegalize.add(renZhengDataWY);
		result.setUserBriefLegalize(userBriefLegalize);
		result.setCounNum(renZhengNum + "");
		// 判断是否支付过一元
		// 获取用户的身份证号
		RcXinyan xinyan = new RcXinyan();
		xinyan.setIdNo(member.getIdNo());
		RcXinyan getxinyanData = rcXinyanService.getxinyanData(xinyan);
		if (getxinyanData == null) {
			result.setPageType(0);
		} else {
			result.setPageType(1);
		}
		return ResponseData.success("操作成功", result);
	}

	/**
	 *  获取天机、公信宝 h5 地址 或者公信宝token
	 */
	@RequestMapping(value = "/getCreditParams")
	public @ResponseBody ResponseData getCreditParams(HttpServletRequest request) {
		Member member = memberService.getCurrent();
		CreditParamsResponseResult result = new CreditParamsResponseResult();
		String param = request.getParameter("param");
		CreditParamsRequestParam reqData = JSONObject.parseObject(param, CreditParamsRequestParam.class);
		int authenType = reqData.getAuthenType();
		int thirdApplyType = reqData.getThirdApplyType();
		int type = reqData.getType();
		//调用第三方接口前的验证
		RcCaDataV2 caData = obtainCaDataByType(member, type);
		if(caData != null) {
			if(caData.getStatus().equals(RcCaDataV2.Status.processing)) {
				return ResponseData.error("报告认证中,请勿重复认证"); 
			}
//			else if (caData.getStatus().equals(RcCaDataV2.Status.success)) {
//				Calendar cal = Calendar.getInstance();
//				cal.add(Calendar.MONTH, -1);
//				if (cal.getTimeInMillis() <= caData.getCreateTime().getTime()) {
//					return ResponseData.error("报告未过期,请勿重复认证"); 
//				}
//			}
		}
		
		if(thirdApplyType == 1) {//天机
			if(authenType == 1) {//h5
				String response = rcCaDataService.getTJAddr(member, type);
				Map<String, Object> map = JSONUtil.toMap(response);
				if((int) map.get("error") == 200) {
					JSONObject jsonObject = (JSONObject) map.get("tianji_api_tianjireport_collectuser_response");
					result.setStr((String) jsonObject.get("redirectUrl"));
					return ResponseData.success("查询成功", result);
				}else {
					return ResponseData.error((String) map.get("msg"));
				}
			}
		}else if (thirdApplyType == 2) {//公信宝
			//先获取token
			String token = rcCaDataService.getGxbToken(member, type);
			if(StringUtils.isBlank(token)) {
				return ResponseData.error("认证失败,请联系客服进行处理");
			}
			if(authenType == 2) {//sdk
				result.setStr(token);
				return ResponseData.success("查询成功", result);
			}else if(authenType == 1) {//h5
				String str = "https://prod.gxb.io/v2/auth?returnUrl=";
				String urlStr = Global.getConfig("domain")+"/app/wyjt/member/returnUrl?phoneNo="+member.getUsername()+"&type=";
				//拼接url 返回  2 淘宝 3运营商 4芝麻分 5学信网 6 社保 7公积金  8网银
				if(type == 2) {
					urlStr = Encodes.urlEncode(urlStr+2);
					str = str + urlStr + "&token="+token + "&subItem=taobao";
				}else if(type == 3){
					urlStr = Encodes.urlEncode(urlStr+1);
					str = str + urlStr + "&token="+token;
				}else if(type == 4){
					urlStr = Encodes.urlEncode(urlStr+0);
					str = str + urlStr + "&token="+token;
				}else if(type == 5){
					urlStr = Encodes.urlEncode(urlStr+3);
					str = str + urlStr + "&token="+token;
				}else if(type == 6){
					urlStr = Encodes.urlEncode(urlStr+4);
					str = str + urlStr + "&token="+token;
				}else if(type == 7){
					urlStr = Encodes.urlEncode(urlStr+5);
					str = str + urlStr + "&token="+token;
				}else if(type == 8){
					urlStr = Encodes.urlEncode(urlStr+6);
					str = str + urlStr + "&token="+token;
				}
				result.setStr(str);
			}
		}
		return ResponseData.success("查询成功", result);
	}
	
	private RcCaDataV2 obtainCaDataByType(Member member, int type) {
		RcCaDataV2 rcCaData = new RcCaDataV2();
		String username = member.getUsername();
		switch(type) {
			case 2:
				rcCaData = rcCaDataServiceV2.getByPhoneNoAndType(username, RcCaDataV2.Type.taobao);
				break;
			case 3:
				rcCaData = rcCaDataServiceV2.getByPhoneNoAndType(username, RcCaDataV2.Type.yys);
				break;
			case 4:
				rcCaData = rcCaDataServiceV2.getByPhoneNoAndType(username, RcCaDataV2.Type.zmf);
				break;
			case 5:
				rcCaData = rcCaDataServiceV2.getByPhoneNoAndType(username, RcCaDataV2.Type.xxw);
				break;
			case 6:
				rcCaData = rcCaDataServiceV2.getByPhoneNoAndType(username, RcCaDataV2.Type.shebao);
				break;
			case 7:
				rcCaData = rcCaDataServiceV2.getByPhoneNoAndType(username, RcCaDataV2.Type.gjj);
				break;
			case 8:
				rcCaData = rcCaDataServiceV2.getByPhoneNoAndType(username, RcCaDataV2.Type.wangyin);
				break;
			default:
				break;
		}
		return rcCaData;
	}

	@RequestMapping(value = "/returnUrl")
	public ModelAndView returnUrl(HttpServletRequest request, Model model) {
		String phoneNo = request.getParameter("phoneNo");
		String param = request.getParameter("type");
		logger.debug("===phoneNo:{}=====type:{}=====",phoneNo,param);
		if(StringUtils.isNotBlank(phoneNo)&&StringUtils.isNotBlank(param)) {
			int type = Integer.parseInt(param);
			RcCaDataV2 caData = new RcCaDataV2();
			caData.setPhoneNo(phoneNo);
			caData.setStatus(RcCaDataV2.Status.processing);
			caData.setType(RcCaDataV2.Type.values()[type]);
			rcCaDataServiceV2.save(caData);
		}
		
		ModelAndView mv2 = new ModelAndView("app/creditArchives/getMofangShuJu");
		model.addAttribute("code",0 );
		model.addAttribute("message","提交成功");
		return mv2;
	}
	
	/**
	 *  sdk调用成功 给前端调用
	 */
	@RequestMapping(value = "/addAuthStatus")
	public @ResponseBody ResponseData addAuthStatus(HttpServletRequest request) {
		String param = request.getParameter("param");
		AuthBackRequestParam reqData = JSONObject.parseObject(param, AuthBackRequestParam.class);
		String phoneNo = reqData.getPhoneNo();
		Integer type = reqData.getType();
		RcCaDataV2 caData = new RcCaDataV2();
		caData.setPhoneNo(phoneNo);
		caData.setStatus(RcCaDataV2.Status.processing);
		caData.setType(RcCaDataV2.Type.values()[type]);
		rcCaDataServiceV2.save(caData);
		return ResponseData.success("处理成功");
	}
	/**
	 * 信用报告（免费）
	 */
	@RequestMapping(value = "/creditReport")
	public @ResponseBody ResponseData getMyCreditarchives(HttpServletRequest request) {
		String param = request.getParameter("param");
		CreditReportRequestParam reqData = JSONObject.parseObject(param, CreditReportRequestParam.class);
		String friendId1 = reqData.getUserId();// 好友id
		long friendId = Long.parseLong(friendId1);
		Member friend = memberService.get(friendId);
		Member member = memberService.getCurrent2();// 会员信息
		String frienPhone=friend.getUsername();
		CreditReportResponseResult result = new CreditReportResponseResult();
		// 判断是否是本人查看的
		if (friendId != member.getId()) {
			MemberFriendRelation memberFriendRelation = memberFriendRelationService
					.findByMemberIdAndFriendId(member.getId(), friendId);
			if (memberFriendRelation == null) {
				return ResponseData.error("目前您还不是他的好友请您先加他为好友");
			} else {
				// 判断与好友是否存在借条关系
				NfsLoanRecord findMyandFriendLoan = loanRecordService.findMyandFriendLoan(member, friend);
				if (findMyandFriendLoan == null) {
					// 判断是否申请过
					if (memberFriendRelation.getFreeCaAuthStatus().ordinal() == 2) {
						// 判断是否过期
						Calendar cal = Calendar.getInstance();
						cal.add(Calendar.HOUR, -2);
						if (memberFriendRelation.getFreeCaAuthTime().getTime() < cal.getTimeInMillis()) {
							// 变更过期状态
							memberFriendRelation
									.setFreeCaAuthStatus(MemberFriendRelation.FreeCaAuthStatus.expired);
							memberFriendRelationService.update(memberFriendRelation);
							return ResponseData.error("您查看好友的信用档案申请已过期，请您先去申请");
						}

					} else {
						return ResponseData.error("您目前不能查看好友的信用档案，请您先去申请");
					}
				}
			}
		}
		// 返回头像
		String phoneNo = friend.getUsername();
		result.setCard(MemUtils.mask(friend).getIdNo());
		result.setCellPhone(friend.getUsername());
		result.setHeadImgUrl(friend.getHeadImage());
		result.setName(friend.getName());
		result.setReportID("YXB-" + friendId);
		result.setResidence(friend.getAddr()); // 地址

		// 数据表格信息
		ArrayList<CreditTable> creditTablesList = new ArrayList<CreditTable>();

		// 数据源分析 表格标题样式实体类一(运营商)
		RcCaData caDataYY = rcCaDataService.getByPhoneNoAndType(phoneNo,RcCaData.Type.yunyingshang);
		Map<String, Object> caDataYY_map = new HashMap<String, Object>();
		if (caDataYY != null) {
			caDataYY_map = JSONUtil.toMap(caDataYY.getContent());
			CreditTable creditTable = rcCaDataService.getCreditarchivesShuJuYuan(caDataYY_map);
			creditTablesList.add(creditTable);
		}
		// 学信网
		String xx = "";
		RcCaData creditarchivesxx = rcCaDataService.getByPhoneNoAndType(phoneNo,RcCaData.Type.xuexinwang);
		if (creditarchivesxx != null) {
			xx = creditarchivesxx.getContent();
		}

		// 淘宝
		RcCaData creditarchivesTB = rcCaDataService.getByPhoneNoAndType(phoneNo,RcCaData.Type.taobao);
		Map<String, Object> map5 = new HashMap<String, Object>();
		if (creditarchivesTB != null) {
			map5 = JSONUtil.toMap(creditarchivesTB.getContent());
		}

		// 风险排查 表格标题样式实体类二
		CreditTable creditTable1 = rcCaDataService.getCreditarchivesFengXian(caDataYY_map, map5, xx, friend);
		creditTablesList.add(creditTable1);

		// 借贷数据分析 表格标题样式实体类三
		CreditTable creditTable2 = rcCaDataService.getCreditarchivesJieDai(friend);
		creditTablesList.add(creditTable2);
		// 逾期记录 表格标题样式实体类四
		CreditTable creditTable3 = rcCaDataService.getCreditarchivesYuQi(friend);
		creditTablesList.add(creditTable3);

		// 芝麻分

		RcCaData creditarchiveszm = rcCaDataService.getByPhoneNoAndType(phoneNo,RcCaData.Type.zhimafen);
		Map<String, Object> map6 = new HashMap<String, Object>();

		// 芝麻分认证 表格标题样式实体类六
		if (creditarchiveszm != null) {
			map6 = JSONUtil.toMap(creditarchiveszm.getContent());
			CreditTable creditTable6 = rcCaDataService.getCreditarchivesZhiMa(map6);
			creditTablesList.add(creditTable6);
		}
		// 紧急联系人分析
		RcCaData creditarchivesjj = rcCaDataService.getByPhoneNoAndType(phoneNo,RcCaData.Type.emergency_people);
		Map<String, Object> emergencyPeopleMap = new HashMap<String, Object>();
		if (creditarchivesjj != null) {
			emergencyPeopleMap = JSONUtil.toMap(creditarchivesjj.getContent());
		}
		// 紧急联系人分析 表格标题样式实体类七
		if (caDataYY != null && creditarchivesjj != null) {
			Map<String, Object> mapyys = new HashMap<String, Object>();
	        RcCaYysDetails rcCaYysDetails =new RcCaYysDetails();
	        rcCaYysDetails.setMemberId(friend.getId());
			List<RcCaYysDetails> rcCaYysDetailsList = rcCaYysDetailsService.findList(rcCaYysDetails);
			if(rcCaYysDetailsList.size()>0) {
				RcCaYysDetails rcCaYysDetailsNew = rcCaYysDetailsList.get(0);
				mapyys = JSONUtil.toMap(rcCaYysDetailsNew.getContent());
			}
			// 对手机号码进行重置
			friend.setUsername(frienPhone);
			CreditTable creditTable7 = rcCaDataService.getCreditarchivesJinJiV2(mapyys, emergencyPeopleMap, member, friend);
			creditTablesList.add(creditTable7);
		}

		// 手机号分析 表格标题样式实体类八
		if (caDataYY != null) {
			CreditTable creditTable8 = rcCaDataService.getCreditarchivesMobileNum(caDataYY_map, friend);
			creditTablesList.add(creditTable8);
		}
		// 运营商报告
		RcCaData creditarchivesyr = rcCaDataService.getByPhoneNoAndType(phoneNo,RcCaData.Type.yunyingshang_report);
		Map<String, Object> userli = new HashMap<String, Object>();
		if (creditarchivesyr != null) {
			userli = JSONUtil.toMap(creditarchivesyr.getContent());
			CreditTable creditTable9 = rcCaDataService.getCreditarchivesYYBgZHF(userli);
			creditTablesList.add(creditTable9);
			// 前十联系人风险分析
			CreditTable creditTable10 = rcCaDataService.getCreditarchivesYYBgLxFx(userli);
			creditTablesList.add(creditTable10);

			// 联系人催收
			CreditTable creditTable11 = rcCaDataService.getCreditarchivesYYBgCsFx(userli);
			creditTablesList.add(creditTable11);
			// 风险
			CreditTable creditTable12 = rcCaDataService.getCreditarchivesYYBgFXlr(userli);
			creditTablesList.add(creditTable12);

			// 金融
			CreditTable creditTable13 = rcCaDataService.getCreditarchivesYYBgJrLr(userli);
			creditTablesList.add(creditTable13);

			// 全部统计
			CreditTable creditTable14 = rcCaDataService.getCreditarchivesYYBgThTj(userli);
			creditTablesList.add(creditTable14);
			// 消费
			CreditTable creditTable15 = rcCaDataService.getCreditarchivesYYBgHFXF(userli, caDataYY_map);
			creditTablesList.add(creditTable15);

			// 静默
			CreditTable creditTable16 = rcCaDataService.getCreditarchivesYYBgJMTJ(userli);
			creditTablesList.add(creditTable16);

			// 时间段
			CreditTable creditTable17 = rcCaDataService.getCreditarchivesYYBgSJTJ(userli);
			creditTablesList.add(creditTable17);
			if (caDataYY_map != null) {
				CreditTable creditTable20 = rcCaDataService.getCreditarchivesYYBgTHXQ(caDataYY_map, friend);
				creditTablesList.add(creditTable20);

			}
		}
		// 淘宝消费情况 表格标题样式实体类十一// 淘宝绑定支付宝信息 表格标题样式实体类十二// 地址与收货地址 表格标题样式实体类十三
		if (creditarchivesTB != null) {
			CreditTable creditTable11 = rcCaDataService.getCreditarchivesTBover(map5);
			creditTablesList.add(creditTable11);
			CreditTable creditTable12 = rcCaDataService.getCreditarchivesTBPayover(map5);
			creditTablesList.add(creditTable12);
			CreditTable creditTable13 = rcCaDataService.getCreditarchivesaddress(map5, friend);
			creditTablesList.add(creditTable13);
		}

		// 网银
		RcCaData creditarchiveswy = rcCaDataService.getByPhoneNoAndType(phoneNo,RcCaData.Type.wangyin);
		Map<String, Object> mapFromJsonString = new HashMap<String, Object>();
		// 银行卡
		if (creditarchiveswy != null) {
			mapFromJsonString = JSONUtil.toMap(creditarchiveswy.getContent());
			List<Map> sublist = JSON.parseArray(mapFromJsonString.get("listjie").toString(), Map.class);
			if (sublist.size() > 0) {
				for (int i = 0; i < sublist.size(); i++) {
					Map<String, Object> jiemap = sublist.get(i);
					CreditTable creditTable13 = rcCaDataService.getCreditarchivesWYJieJi(jiemap, friend);
					creditTablesList.add(creditTable13);
				}
			}
			//
			List<Map> sublist1 = JSON.parseArray(mapFromJsonString.get("listxin").toString(), Map.class);
			if (sublist1.size() > 0) {
				for (int i = 0; i < sublist1.size(); i++) {
					Map<String, Object> jiemap = sublist1.get(i);
					CreditTable creditTable13 = rcCaDataService.getCreditarchivesWYXinYong(jiemap, friend);
					creditTablesList.add(creditTable13);

				}
			}
		}
		// 社保
		RcCaData creditarchivessb = rcCaDataService.getByPhoneNoAndType(phoneNo,RcCaData.Type.shebao);
		Map<String, Object> map8 = new HashMap<String, Object>();
		// 社保分析 表格标题样式实体类十四
		if (creditarchivessb != null) {
			map8 = JSONUtil.toMap(creditarchivessb.getContent());
			CreditTable creditTable14 = rcCaDataService.getCreditarchivesSheBao(map8, friend, 0);
			creditTablesList.add(creditTable14);
		}

		// 公积金
		RcCaData creditarchivesgj = rcCaDataService.getByPhoneNoAndType(phoneNo,RcCaData.Type.gongjijin);
		Map<String, Object> map9 = new HashMap<String, Object>();
		// 公积金分析 表格标题样式实体类十五
		if (creditarchivesgj != null) {
			map9 = JSONUtil.toMap(creditarchivesgj.getContent());
			CreditTable creditTable15 = rcCaDataService.getCreditarchivesGongJing(map9, friend, 0);
			creditTablesList.add(creditTable15);
		}
		// 学信网分析 表格标题样式实体类十六
		if (StringUtils.isNotBlank(xx)) {
			CreditTable creditTable16 = rcCaDataService.getCreditarchivesXX(xx);
			creditTablesList.add(creditTable16);
		}

		result.setTableItem(creditTablesList);
		return ResponseData.success("操作成功", result);
	}

	/**
	 * 获取单个报告
	 * 
	 * @param userToken
	 * @param yxbId
	 * @return type 1信用记录 2 淘宝 3运营商 4芝麻分 5学信网 6 社保 7公积金 8网银
	 */
	@RequestMapping(value = "/singleCreditReport")
	public @ResponseBody ResponseData getsingleCreditReport(HttpServletRequest request) {
		String param = request.getParameter("param");
		SingleCreditReportRequestParam reqData = JSONObject.parseObject(param, SingleCreditReportRequestParam.class);

		long friendId = Long.parseLong(reqData.getUserId());
		Member friend = memberService.get(friendId);
		Member member = memberService.getCurrent();// 会员信息
		CreditReportResponseResult result = new CreditReportResponseResult();
		int type = reqData.getType();
		if (friendId != member.getId()) {
			MemberFriendRelation memberFriendRelation = memberFriendRelationService.findByMemberIdAndFriendId(member.getId(), friendId);
			if (memberFriendRelation == null) {
				return ResponseData.error("目前您还不是他的好友请您先加他为好友");
			}
		} else {
			return ResponseData.error("用户的id有误");
		}
		// 查询结果是否有数据
		ArrayList<CreditTable> creditTablesList = new ArrayList<CreditTable>();
		if (type == 1) {
			// 借贷数据分析 表格标题样式实体类三
			CreditTable creditTable2 = rcCaDataService.getCreditarchivesJieDai(friend);
			creditTablesList.add(creditTable2);
			// 逾期记录 表格标题样式实体类四
			CreditTable creditTable3 = rcCaDataService.getCreditarchivesYuQi(friend);
			creditTablesList.add(creditTable3);
		} else if (type == 2) {
			// 淘宝
			RcCaData creditarchivesTB = rcCaDataService.getByPhoneNoAndType(friend.getUsername(),RcCaData.Type.taobao);
			Map<String, Object> map5 = new HashMap<String, Object>();
			if (creditarchivesTB != null) {
				map5 = JSONUtil.toMap(creditarchivesTB.getContent());
				CreditTable creditTable11 = rcCaDataService.getCreditarchivesTBover(map5);
				creditTablesList.add(creditTable11);
				// 淘宝绑定支付宝信息 表格标题样式实体类十二
				CreditTable creditTable12 = rcCaDataService.getCreditarchivesTBPayover(map5);
				creditTablesList.add(creditTable12);
				// 地址与收货地址 表格标题样式实体类十三
				CreditTable creditTable13 = rcCaDataService.getCreditarchivesaddress(map5, friend);
				creditTablesList.add(creditTable13);
			}

		} else if (type == 3) {
	
			RcCaData caDataYY = rcCaDataService.getByPhoneNoAndType(friend.getUsername(),RcCaData.Type.yunyingshang);
			Map<String, Object> caDataYY_map = new HashMap<String, Object>();
			if (caDataYY != null) {
				caDataYY_map = JSONUtil.toMap(caDataYY.getContent());
				CreditTable creditTable = rcCaDataService.getCreditarchivesShuJuYuan(caDataYY_map);
				creditTablesList.add(creditTable);

				// 运营商报告
				RcCaData creditarchivesyr = rcCaDataService.getByPhoneNoAndType(friend.getUsername(),RcCaData.Type.yunyingshang_report);
				Map<String, Object> userli = new HashMap<String, Object>();
				// 紧急联系人分析
				RcCaData creditarchivesjj = rcCaDataService.getByPhoneNoAndType(friend.getUsername(),RcCaData.Type.emergency_people);
				Map<String, Object> map7 = new HashMap<String, Object>();
				// 紧急联系人分析 表格标题样式实体类七
				if (caDataYY != null && creditarchivesjj != null) {
					map7 = JSONUtil.toMap(creditarchivesjj.getContent());
					CreditTable creditTable7 = rcCaDataService.getCreditarchivesJinJiV2(caDataYY_map, map7, member, friend);
					creditTablesList.add(creditTable7);
				}

				// 手机号分析 表格标题样式实体类八
				if (caDataYY != null) {
					CreditTable creditTable8 = rcCaDataService.getCreditarchivesMobileNum(caDataYY_map, friend);
					creditTablesList.add(creditTable8);
				}
				if (creditarchivesyr != null) {
					userli = JSONUtil.toMap(creditarchivesyr.getContent());
					CreditTable creditTable9 = rcCaDataService.getCreditarchivesYYBgZHF(userli);
					creditTablesList.add(creditTable9);
					// 前十联系人风险分析
					CreditTable creditTable10 = rcCaDataService.getCreditarchivesYYBgLxFx(userli);
					creditTablesList.add(creditTable10);

					// 联系人催收
					CreditTable creditTable11 = rcCaDataService.getCreditarchivesYYBgCsFx(userli);
					creditTablesList.add(creditTable11);
					// 风险
					CreditTable creditTable12 = rcCaDataService.getCreditarchivesYYBgFXlr(userli);
					creditTablesList.add(creditTable12);

					// 金融
					CreditTable creditTable13 = rcCaDataService.getCreditarchivesYYBgJrLr(userli);
					creditTablesList.add(creditTable13);

					// 全部统计
					CreditTable creditTable14 = rcCaDataService.getCreditarchivesYYBgThTj(userli);
					creditTablesList.add(creditTable14);
					// 消费
					CreditTable creditTable15 = rcCaDataService.getCreditarchivesYYBgHFXF(userli, caDataYY_map);
					creditTablesList.add(creditTable15);

					// 静默
					CreditTable creditTable16 = rcCaDataService.getCreditarchivesYYBgJMTJ(userli);
					creditTablesList.add(creditTable16);

					// 时间段
					CreditTable creditTable17 = rcCaDataService.getCreditarchivesYYBgSJTJ(userli);
					creditTablesList.add(creditTable17);
					if (caDataYY_map != null) {
						CreditTable creditTable20 = rcCaDataService.getCreditarchivesYYBgTHXQ(caDataYY_map, friend);
						creditTablesList.add(creditTable20);

					}
				}
			}
		} else if (type == 4) {
			// 芝麻分
			RcCaData creditarchiveszm = rcCaDataService.getByPhoneNoAndType(friend.getUsername(),RcCaData.Type.zhimafen);
			Map<String, Object> map6 = new HashMap<String, Object>();

			// 芝麻分认证 表格标题样式实体类六
			if (creditarchiveszm != null) {
				map6 = JSONUtil.toMap(creditarchiveszm.getContent());
				CreditTable creditTable6 = rcCaDataService.getCreditarchivesZhiMa(map6);
				creditTablesList.add(creditTable6);
			}
		} else if (type == 5) {
			// 学信网分析 表格标题样式实体类十六
			RcCaData caData = rcCaDataService.getByPhoneNoAndType(friend.getUsername(),RcCaData.Type.xuexinwang);
			if (caData != null&&StringUtils.isNotBlank(caData.getContent())) {
				CreditTable creditTable16 = rcCaDataService.getCreditarchivesXX(caData.getContent());
				creditTablesList.add(creditTable16);
			}

		} else if (type == 6) {
			// 社保分析 表格标题样式实体类十四
			RcCaData creditarchivessb = rcCaDataService.getByPhoneNoAndType(friend.getUsername(),RcCaData.Type.shebao);
			Map<String, Object> map8 = new HashMap<String, Object>();
			// 社保分析 表格标题样式实体类十四
			if (creditarchivessb != null) {
				map8 = JSONUtil.toMap(creditarchivessb.getContent());
				CreditTable creditTable14 = rcCaDataService.getCreditarchivesSheBao(map8, friend, 0);
				creditTablesList.add(creditTable14);
			}
		} else if (type == 7) {
			// 公积金
			RcCaData creditarchivesgj = rcCaDataService.getByPhoneNoAndType(friend.getUsername(),RcCaData.Type.gongjijin);
			Map<String, Object> map9 = new HashMap<String, Object>();
			// 公积金分析 表格标题样式实体类十五
			if (creditarchivesgj != null) {
				map9 = JSONUtil.toMap(creditarchivesgj.getContent());
				CreditTable creditTable15 = rcCaDataService.getCreditarchivesGongJing(map9, friend, 0);
				creditTablesList.add(creditTable15);
			}
		} else if (type == 8) {
			// 网银
			RcCaData creditarchiveswy = rcCaDataService.getByPhoneNoAndType(friend.getUsername(),RcCaData.Type.wangyin);
			Map<String, Object> mapFromJsonString = new HashMap<String, Object>();
			// 银行卡
			if (creditarchiveswy != null) {
				mapFromJsonString = JSONUtil.toMap(creditarchiveswy.getContent());
				;
				List<Map> sublist = JSON.parseArray(mapFromJsonString.get("listjie").toString(), Map.class);
				if (sublist.size() > 0) {
					for (int i = 0; i < sublist.size(); i++) {
						Map<String, Object> jiemap = sublist.get(i);
						CreditTable creditTable13 = rcCaDataService.getCreditarchivesWYJieJi(jiemap, friend);
						creditTablesList.add(creditTable13);
					}
				}
				//
				List<Map> sublist1 = JSON.parseArray(mapFromJsonString.get("listxin").toString(), Map.class);
				if (sublist1.size() > 0) {
					for (int i = 0; i < sublist1.size(); i++) {
						Map<String, Object> jiemap = sublist1.get(i);
						CreditTable creditTable13 = rcCaDataService.getCreditarchivesWYXinYong(jiemap, friend);
						creditTablesList.add(creditTable13);

					}
				}
			}
		} else {
			return ResponseData.error("你的输入的type类型不存在");
		}
		result.setTableItem(creditTablesList);

		return ResponseData.success("操作成功", result);

	}

	/**
	 * 获得新颜报告信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/xinyanReport")
	public @ResponseBody ResponseData getXinyanCreditArchivesReport(HttpServletRequest request) {
		String param = request.getParameter("param");
		CreditReportRequestParam reqData = JSONObject.parseObject(param, CreditReportRequestParam.class);
		String friendId1 = reqData.getUserId();// 好友id
		long friendId = Long.parseLong(friendId1);
		Member friend = memberService.get(friendId);
		Member member = memberService.getCurrent();// 会员信息
		// 获取用户的身份证号
		XinYongLeiDaDataResponseResult result = new XinYongLeiDaDataResponseResult();
		RcXinyan xinyan = null;
		if (friendId != member.getId()) {
			MemberFriendRelation memberFriendRelation = memberFriendRelationService
					.findByMemberIdAndFriendId(member.getId(), friendId);
			if (memberFriendRelation == null) {
				return ResponseData.error("目前您还不是他的好友请您先加他为好友");
			} else {
				xinyan = new RcXinyan();
				xinyan.setIdNo(friend.getIdNo());
				xinyan = rcXinyanService.getxinyanData(xinyan);
				if (xinyan == null) {
					return ResponseData.error("请先支付你的信用报告");
				}
				if (xinyan.getIsSelfbuy().equals("N")) {
					if (memberFriendRelation.getChargeCaStatus().ordinal() == 1) {
						// 判断是否过期
						Calendar cal = Calendar.getInstance();
						cal.add(Calendar.MONTH, -1);
						if (memberFriendRelation.getChargeCaTime().getTime() < cal.getTimeInMillis()) {
							// 变更过期状态
							memberFriendRelation.setChargeCaStatus(memberFriendRelation.getChargeCaStatus().expired);
							memberFriendRelationService.update(memberFriendRelation);
							return ResponseData.error("请您先支付好友的信用雷达报告档案信息");
						}

					} else {
						return ResponseData.error("请您先支付好友的信用雷达报告档案信息");
					}

				}

			}
		} else {
			// 判断是否自己支付过一元
			xinyan = new RcXinyan();
			xinyan.setIdNo(friend.getIdNo());
			xinyan = rcXinyanService.getxinyanData(xinyan);
			if (xinyan == null) {
				return ResponseData.error("请先支付你的信用报告");
			}
		}
		// 雷达报告
		Map<String, Object> mapFromJsonString = JSONUtil.toMap(xinyan.getResult());
		result.setName(MemUtils.mask(friend).getName());
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		result.setCreateTime(simpleDateFormat.format(xinyan.getCreateTime()));
		result.setIdcard(MemUtils.mask(friend).getIdNo());
		String applyLeiDa = "0|0|0|0|0|0|暂无|0|0|0"; // 10个 准入分/置信度 申请雷达
		String behaviorDa = "0|0|0|0|0|0|0|0|0|0|0|暂无|0|0|0|0|0"; // 17 行为雷达
		String creditLeiDa = "0|0|0|0|0|0|0|0|0|0|0|0"; // 12 //信誉雷达
		if (mapFromJsonString != null) {
			result.setBianNum(mapFromJsonString.get("trade_no").toString() == null ? "暂无"
					: mapFromJsonString.get("trade_no").toString());
			// 命中为0
			if (mapFromJsonString.get("code").toString().equals("0")) {
				String rea = mapFromJsonString.get("result_detail").toString();
				if (StringUtils.isNotBlank(rea)) {
					Map<String, Object> resde = JSONUtil.toMap(rea);
					if (resde.get("apply_report_detail") != null
							&& !resde.get("apply_report_detail").toString().equals("null")) {
						String rea2 = resde.get("apply_report_detail").toString();
						Map<String, Object> res1 = JSONUtil.toMap(rea2);
						applyLeiDa = res1.get("apply_score").toString() + "|" + res1.get("apply_credibility").toString()
								+ "|" + res1.get("query_org_count").toString() + "|"
								+ res1.get("query_finance_count").toString() + "|"
								+ res1.get("query_cash_count").toString() + "|" + res1.get("query_sum_count").toString()
								+ "|" + res1.get("latest_query_time").toString() + "|"
								+ res1.get("latest_one_month").toString() + "|"
								+ res1.get("latest_three_month").toString() + "|"
								+ res1.get("latest_six_month").toString();

					}
					if (resde.get("behavior_report_detail") != null
							&& !resde.get("behavior_report_detail").toString().equals("null")) {
						String rea2 = resde.get("behavior_report_detail").toString();
						Map<String, Object> res1 = JSONUtil.toMap(rea2);
						behaviorDa = res1.get("loans_score").toString() + "|" + res1.get("loans_credibility").toString()
								+ "|" + res1.get("loans_count").toString() + "|"
								+ res1.get("loans_settle_count").toString() + "|"
								+ res1.get("loans_overdue_count").toString() + "|"
								+ res1.get("loans_org_count").toString() + "|"
								+ res1.get("consfin_org_count").toString() + "|"
								+ res1.get("loans_cash_count").toString() + "|"
								+ res1.get("latest_one_month").toString() + "|"
								+ res1.get("latest_three_month").toString() + "|"
								+ res1.get("latest_six_month").toString() + "|"
								+ res1.get("loans_latest_time").toString() + "|"
								+ res1.get("loans_long_time").toString() + "|" + res1.get("history_suc_fee").toString()
								+ "|" + res1.get("history_fail_fee").toString() + "|"
								+ res1.get("latest_one_month_suc").toString() + "|"
								+ res1.get("latest_one_month_fail").toString();

					}

					if (resde.get("current_report_detail") != null
							&& !resde.get("current_report_detail").toString().equals("null")) {
						String rea2 = resde.get("current_report_detail").toString();
						Map<String, Object> res1 = JSONUtil.toMap(rea2);
						creditLeiDa = res1.get("loans_credit_limit").toString() + "|"
								+ res1.get("loans_credibility").toString() + "|"
								+ res1.get("loans_org_count").toString() + "|"
								+ res1.get("loans_product_count").toString() + "|"
								+ res1.get("loans_max_limit").toString() + "|" + res1.get("loans_avg_limit").toString()
								+ "|" + res1.get("consfin_credit_limit").toString() + "|"
								+ res1.get("consfin_credibility").toString() + "|"
								+ res1.get("consfin_org_count").toString() + "|"
								+ res1.get("consfin_product_count").toString() + "|"
								+ res1.get("consfin_max_limit").toString() + "|"
								+ res1.get("consfin_avg_limit").toString();

					}
				}

			}
		}
		result.setApplyLeiDa(applyLeiDa);
		result.setBehaviorDa(behaviorDa);
		result.setCreditLeiDa(creditLeiDa);
		result.setNote("1.置信度：代表分数的可靠程度，取决于数据的新鲜度和饱和度，分数越大，数据量越大，越是近期的数据，分数越可靠。\n" + "2.申请准入分：分数越高，客户质量越好。\n"
				+ "3.贷款行为分：行为分代表这个人的贷款历史行为，分数越高代表历史行为越好。");
		return ResponseData.success("操作成功", result);
	}

	/**
	 * 查看记录 0：全部 1：免费 2一元
	 */
	@RequestMapping(value = "/credictRecode")
	public @ResponseBody ResponseData credictBaoGaoRecode(HttpServletRequest request) {
		String param = request.getParameter("param");
		CredictBaoGaoRecodeRequestParam reqData = JSONObject.parseObject(param, CredictBaoGaoRecodeRequestParam.class);
		Member member = memberService.getCurrent();// 会员信息
		Integer pageNo = reqData.getPageNo();
		Integer pageSize = reqData.getPageSize();
		int type = reqData.getType();
		CredictBaoGaoRecodeResponseResult result = new CredictBaoGaoRecodeResponseResult();
		ArrayList<CredictBaoGaoRecode> list = new ArrayList<CredictBaoGaoRecode>();
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.MONTH, -1);
		// 2个小时免费报告
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(new Date());
		cal1.add(Calendar.HOUR, -2);
		// 0：全部 1：可查看 2已过期
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		MemberFriendCa memberFriendCa = new MemberFriendCa();
		Page<MemberFriendCa> object = new Page<MemberFriendCa>();
		memberFriendCa.setMember(member);
		if (type == 0) {
			object = memberFriendCaService.findCredictBaoGaoRecode(memberFriendCa, pageNo, pageSize);
			if(object !=null) {
			for (MemberFriendCa object2 : object.getList()) {
				// 内部类好友信息
				CredictBaoGaoRecode entity = new CredictBaoGaoRecodeResponseResult().new CredictBaoGaoRecode();
				// 获得好友信息
				Member loadById = memberService.get(object2.getFriend());
				if (StringUtils.isEmpty(loadById.getName())) {
					return ResponseData.error("好友信息有误");
				}
				// 头像地址
				entity.setImgUrl(loadById.getHeadImage());
				// 同意时间
				entity.setCreateTime(formatter.format(object2.getUpdateTime()));
				// 申请人或者支付者
				entity.setName(loadById.getName());
				// free 免费申请 oneCoat 一元信用档案
				if (StringUtils.isBlank(object2.getType().toString())) {
					entity.setNote("信用报告（免费）");
					entity.setType(0);
					// 判断是否过期
					// 0：可查看 1已过期
					if (object2.getUpdateTime().getTime() >= cal1.getTimeInMillis()) {
						entity.setExamineStatus(0);
					} else {
						entity.setExamineStatus(1);
					}
				} else if (object2.getType().ordinal() == 1) {
					entity.setNote("信用报告（一元查看）");
					entity.setType(1);
					if (object2.getUpdateTime().getTime() >= cal.getTimeInMillis()) {
						entity.setExamineStatus(0);
					} else {
						entity.setExamineStatus(1);
					}
				} else if (object2.getType().ordinal() == 0) {
					entity.setNote("信用报告（免费）");
					entity.setType(0);
					if (object2.getUpdateTime().getTime() >= cal1.getTimeInMillis()) {
						entity.setExamineStatus(0);
					} else {
						entity.setExamineStatus(1);
					}
				} else {
					entity.setNote("未知");
					if (object2.getUpdateTime().getTime() >= cal1.getTimeInMillis()) {
						entity.setExamineStatus(0);
					} else {
						entity.setExamineStatus(1);
					}
				}
				entity.setYxbId(object2.getFriend().getId() + "");
				list.add(entity);

			}
			}
		} else if (type == 1) {
			memberFriendCa.setType(MemberFriendCa.Type.free);
			object = memberFriendCaService.findCredictBaoGaoRecode(memberFriendCa, pageNo, pageSize);
			if(object !=null) {
			for (MemberFriendCa object2 : object.getList()) {
				// 内部类好友信息
				CredictBaoGaoRecode entity = new CredictBaoGaoRecodeResponseResult().new CredictBaoGaoRecode();
				// 获得好友信息
				Member loadById = memberService.get(object2.getFriend());
				if (StringUtils.isEmpty(loadById.getName())) {
					return ResponseData.error("好友信息有误");
				}
				// 头像地址
				entity.setImgUrl(loadById.getHeadImage());
				// 同意时间
				entity.setCreateTime(formatter.format(object2.getUpdateTime()));
				// 申请人或者支付者
				entity.setName(loadById.getName());
				if (StringUtils.isBlank(object2.getType().toString())) {
					entity.setNote("信用报告（免费）");
					entity.setType(0);
					// 判断是否过期
					// 0：可查看 1已过期
					if (object2.getUpdateTime().getTime() >= cal1.getTimeInMillis()) {
						entity.setExamineStatus(0);
					} else {
						entity.setExamineStatus(1);
					}
				} else if (object2.getType().ordinal() == 0) {
					entity.setNote("信用报告（免费）");
					entity.setType(0);
					if (object2.getUpdateTime().getTime() >= cal1.getTimeInMillis()) {
						entity.setExamineStatus(0);
					} else {
						entity.setExamineStatus(1);
					}
				} else {
					entity.setNote("未知");
					if (object2.getUpdateTime().getTime() >= cal1.getTimeInMillis()) {
						entity.setExamineStatus(0);
					} else {
						entity.setExamineStatus(1);
					}
				}
				entity.setYxbId(object2.getFriend().getId() + "");
				list.add(entity);
			}
			}
		} else if (type == 2) {
			memberFriendCa.setType(MemberFriendCa.Type.oenMoney);
			object = memberFriendCaService.findCredictBaoGaoRecode(memberFriendCa, pageNo, pageSize);
			if(object !=null) {
			for (MemberFriendCa object2 : object.getList()) {
				// 内部类好友信息
				CredictBaoGaoRecode entity = new CredictBaoGaoRecodeResponseResult().new CredictBaoGaoRecode();
				// 获得好友信息
				Member loadById = memberService.get(object2.getFriend());
				if (StringUtils.isEmpty(loadById.getName())) {
					return ResponseData.error("好友信息有误");
				}
				// 头像地址
				entity.setImgUrl(loadById.getHeadImage());
				// 同意时间
				entity.setCreateTime(formatter.format(object2.getUpdateTime()));
				// 申请人或者支付者
				entity.setName(loadById.getName());
				if (object2.getType().ordinal() == 1) {
					entity.setNote("信用报告（一元查看）");
					entity.setType(1);
					if (object2.getUpdateTime().getTime() >= cal.getTimeInMillis()) {
						entity.setExamineStatus(0);
					} else {
						entity.setExamineStatus(1);
					}
				} else {
					entity.setNote("未知");
					if (object2.getUpdateTime().getTime() >= cal1.getTimeInMillis()) {
						entity.setExamineStatus(0);
					} else {
						entity.setExamineStatus(1);
					}
				}
				entity.setYxbId(object2.getFriend().getId() + "");
				list.add(entity);
			}
			}
		} else {

			return ResponseData.error("该类型不存在");

		}
		result.setTableItem(list);

		return ResponseData.success("操作成功", result);
	}

	/**
	 * 邀请好友支付一元雷达报告
	 */
	@RequestMapping(value = "/inviteFriendPay")
	public @ResponseBody ResponseData inviteFriendPay(HttpServletRequest request) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		Member member = memberService.getCurrent();
		String param = request.getParameter("param");
		InviteFriendPayRequestParam reqData = JSONObject.parseObject(param, InviteFriendPayRequestParam.class);
		String friendId1 = reqData.getFriendId();
		long friendId = Long.parseLong(friendId1);
		// 好友信息
		Member friend = memberService.get(friendId);
		MemberFriendRelation memberFriendRelation = memberFriendRelationService.findByMemberIdAndFriendId(member.getId(), friendId);
		if (memberFriendRelation == null) {
			return ResponseData.error("目前您还不是他的好友请您先加他为好友");
		}
		if (memberFriendRelation.getChargeCaStatus().ordinal() != 0) {
			return ResponseData.error("报告已经支付过了,您不能邀请好友进行支付");

		}
		// 创建申请记录表
		MemberFriendCa ca = new MemberFriendCa();
		ca.setDrc(MemberFriendCa.Drc.apply);
		ca.setFriend(friend);
		ca.setMember(member);
		ca.setStatus(MemberFriendCa.Status.apply);
		ca.setType(MemberFriendCa.Type.oenMoney);
		memberFriendCaService.save(ca);
		memberFriendRelation.setChargeCaStatus(MemberFriendRelation.ChargeCaStatus.unpay);
		memberFriendRelation.setChargeCaTime(new Date());
		memberFriendRelationService.update(memberFriendRelation);
		//发送会员消息
		MemberMessage sendMessage = memberMessageService.sendMessage(MemberMessage.Type.payReport,ca.getId());
		// 消息推送
		sendMsgService.beforeSendAppMsg(sendMessage);
		return ResponseData.success("申请提交成功");
	}

	/**
	 * 消息中心收到请求档案请求
	 * 
	 * @param userToken
	 * @return tfId 申请记录的val
	 */
	@RequestMapping(value = "/sendCaPage") // CreditaInformation
	public @ResponseBody ResponseData sendCaPage(HttpServletRequest request) {
		String param = request.getParameter("param");
		CreditaInformationRequestParam reqData = JSONObject.parseObject(param, CreditaInformationRequestParam.class);
		String valId = reqData.getValId();// 申请记录id
		CreditaInformationResponseResult result = new CreditaInformationResponseResult();
		MemberFriendCa memberFriendCa = memberFriendCaService.get(Long.parseLong(valId));
		Member member = memberService.getCurrent();// 会员信息
		// 好友的
		Member friend = memberService.get(memberFriendCa.getFriend().getId());
		// 申请者
		Member applyer = memberService.get(memberFriendCa.getMember().getId());
		if (member.equals(friend)|| (member.equals(applyer)&& memberFriendCa.getDrc().ordinal() == 1)) {
			result = memberFriendCaService.getCreditarchives(member, valId);
		} else if (memberFriendCa.getDrc().ordinal() == 0
				&& member.getId().toString().equals(applyer.getId().toString())) {
			// 收到拒绝与同意接口
			result = memberFriendCaService.getConsentOrRefusalCredita(member, valId);
		} else {
			return ResponseData.error("该类型不存在");

		}
		return ResponseData.success("操作成功", result);
	}

	/**
	 * 申请一元支付
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "payXinyanBaogaoApply")
	public ModelAndView payXinyanBaogaoApply(HttpServletRequest request, Model model) {

		// Member member = memberService.get(247332211534204928L);
		Member member = memberService.getCurrent();
		String param = request.getParameter("param");
		CreditReportRequestParam reqData = JSONObject.parseObject(param, CreditReportRequestParam.class);
		ModelAndView mv2 = new ModelAndView("app/creditArchives/payXinyanBaogao");
		mv2 = H5Utils.addPlatform(member, mv2);
		String userId = reqData.getUserId();
		model.addAttribute("friendId", userId);
		model.addAttribute("amount", new BigDecimal("1"));
		String memberToken = request.getHeader("x-memberToken");
		model.addAttribute("memberToken", memberToken);
		return mv2;
	}

	/**
	 * 支付一元报告的处理逻辑
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "payXinyanBaogaologic")
	public ModelAndView payXinyanBaogaologic(HttpServletRequest request) {
		String friendId = request.getParameter("friendId");
		String amount ="1";
		ModelAndView mv = new ModelAndView("app/creditArchives/payXinyanBaogaoResult");

		Member member = memberService.getCurrent();
		// 获取好友的
		Member friend = memberService.get(Long.parseLong(friendId));
		// 判断是否是好友，是否支付过
		MemberFriendRelation memberFriendRelation = new MemberFriendRelation();
		RcXinyan xinyanOld = new RcXinyan();
		xinyanOld.setIdNo(friend.getIdNo());
		xinyanOld.setIsSelfbuy("Y");
		xinyanOld = rcXinyanService.getxinyanData(xinyanOld);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		if (xinyanOld != null) {
			if (xinyanOld.getCreateTime().getTime() >= cal.getTimeInMillis()) {
				mv.addObject("code", -1);
				mv.addObject("message", "雷达报告还未过期，请您不要重复支付");
				return mv;
			}

		}
		if (!member.getId().toString().equals(friend.getId().toString())) {
			memberFriendRelation = memberFriendRelationService.findByMemberIdAndFriendId(member.getId(),
					friend.getId());
			if (memberFriendRelation == null) {
				mv.addObject("code", -1);
				mv.addObject("message", "您们还不是好友请您先加他们为好友");
				return mv;
			}
			if (memberFriendRelation.getChargeCaStatus().ordinal() != 0) {
				if (memberFriendRelation.getChargeCaStatus().ordinal() == 2) {
					// 判断是否过期
					if (memberFriendRelation.getChargeCaTime().getTime() >= cal.getTimeInMillis()) {
						mv.addObject("code", -1);
						mv.addObject("message", "雷达报告还未过期，请您不要重复支付");
						return mv;
					}

				}
			}
		}
		mv = H5Utils.addPlatform(member, mv);
		// 获取用户可用余额

		BigDecimal avlBal = memberService.getAvlBal(member);
		if (avlBal.compareTo(new BigDecimal(amount)) < 0) {
			mv.addObject("code", -1);
			mv.addObject("message", "账户余额不足，请充值后再操作!");
			return mv;
		}
		// 获取新颜数据
		// 获取用户的信息
		String xinyan = "";
		try {
			xinyan = CreditRadarAction.getXinyan(friend.getIdNo(), friend.getName(), friend.getUsername(), null);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (StringUtils.isEmpty(xinyan)) {
			mv.addObject("code", -1);
			mv.addObject("message", "支付失败,认证信息有误");
			return mv;
		}

		Map<String, Object> resMap = JSONUtil.toMap(xinyan);
		if (resMap.get("success").toString().equals("false")) {
			mv.addObject("code", -1);
			mv.addObject("message", "支付失败,认证信息有误");
			return mv;

		}
		String data = resMap.get("data").toString();
		Map<String, Object> resMapnew = JSONUtil.toMap(data);
		if (resMapnew.get("code").toString().equals("9")) {
			mv.addObject("code", -1);
			mv.addObject("message", "网络异常");
			return mv;
		}
		// 变更数据账户

		int code = actService.updateAct(TrxRuleConstant.ONE_YUAN_RECORD, new BigDecimal(amount), member, 1L);
		if (code == 0) {
			// 创建新颜数据
			RcXinyan rcXinyan = new RcXinyan();
			rcXinyan.setIdNo(friend.getIdNo());
			rcXinyan.setName(friend.getName());
			rcXinyan.setPhoneNo(friend.getUsername());
			// 判断是否自己支付支付的
			if (member.getId().toString().equals(friend.getId().toString())) {
				rcXinyan.setIsSelfbuy("Y");
			} else {
				rcXinyan.setIsSelfbuy("N");
			}
			rcXinyan.setResult(data);
			rcXinyan.setUfangEmpNo("0");
			rcXinyan.setToken(resMapnew.get("trans_id").toString());
			rcXinyanService.save(rcXinyan);
			// 判断是否是自己支付
			if (!member.getId().toString().equals(friend.getId().toString())) {
				memberFriendRelation.setChargeCaStatus(MemberFriendRelation.ChargeCaStatus.payed);
				memberFriendRelation.setChargeCaTime(new Date());
				memberFriendRelationService.update(memberFriendRelation);
				// 变更申请表的数据
				MemberFriendCa entity = new MemberFriendCa();
				entity.setFriend(friend);
				entity.setMember(member);
				entity.setStatus(MemberFriendCa.Status.apply);
				entity.setType(MemberFriendCa.Type.oenMoney);
				List<MemberFriendCa> findList = memberFriendCaService.findList(entity);
				if (findList.size() > 0) {
					for (MemberFriendCa ca : findList) {
						ca.setStatus(MemberFriendCa.Status.agree);
						ca.setCreateTime(new Date());
						memberFriendCaService.update(ca);
					}
				}
				// 变更消息中心
				MemberMessage memberMessage = new MemberMessage();
				memberMessage.setMember(friend);
				memberMessage.setOrgType("3");
				memberMessage.setType(MemberMessage.Type.payReport);
				memberMessage.setOrgId(friend.getId());
				List<MemberMessage> findListMe = memberMessageService.findList(memberMessage);
				if (findListMe.size() > 0) {
					for (MemberMessage memberMessage2 : findListMe) {
						memberMessage2.setType(MemberMessage.Type.checkReport);
						memberMessageService.update(memberMessage2);
					}

				}

			} else {
				// 自己支付
				MemberFriendCa entity = new MemberFriendCa();
				entity.setFriend(friend);
				entity.setStatus(MemberFriendCa.Status.apply);
				entity.setType(MemberFriendCa.Type.oenMoney);
				List<MemberFriendCa> findList = memberFriendCaService.findList(entity);
				if (findList.size() > 0) {
					for (MemberFriendCa ca : findList) {
						ca.setStatus(MemberFriendCa.Status.agree);
						ca.setCreateTime(new Date());
						memberFriendCaService.update(ca);
						//发送会员消息
						memberMessageService.sendMessage(MemberMessage.Type.checkReport,ca.getId());
					}

				}
				// 变更消息中心
				MemberMessage memberMessage = new MemberMessage();
				memberMessage.setMember(friend);
				memberMessage.setOrgType("3");
				memberMessage.setType(MemberMessage.Type.payReport);
				List<MemberMessage> findListMe = memberMessageService.findList(memberMessage);
				if (findListMe.size() > 0) {
					for (MemberMessage memberMessage2 : findListMe) {
						memberMessage2.setType(MemberMessage.Type.checkReport);
						memberMessageService.update(memberMessage2);
					}

				}
			}

		} else {
			mv.addObject("code", -1);
			mv.addObject("message", "");
			return mv;
		}
		mv.addObject("showMoney", "1.00");
		mv.addObject("yxbId", friendId);
		mv.addObject("code", 0);
		mv.addObject("message", "一元支付缴费成功！");
		return mv;
	}

	/**
	 * 公积金详情
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "accumulationDetails")
	public ModelAndView accumulationDetails(HttpServletRequest request, Model model) {

		//Member member = memberService.get(247332211534204928L);
		String param = request.getParameter("param");
		Member member = memberService.get(Long.parseLong(param));
		ModelAndView mv2 = new ModelAndView("app/creditArchives/accumulationFundDetails");
		mv2 = H5Utils.addPlatform(member, mv2);
		List<Map> listMapFromJson2 = new ArrayList<Map>();
		List<Map> listMapFromJson = new ArrayList<Map>();
		RcCaData entity = new RcCaData();
		entity.setPhoneNo(member.getUsername());
		entity.setType(RcCaData.Type.gongjijin);
		List<RcCaData> findList = rcCaDataService.findList(entity);
		RcCaData rcCaData = findList.get(0);
		Map<String, Object> userMap = new HashMap<String, Object>();
		userMap = JSONUtil.toMap(rcCaData.getContent());
		listMapFromJson2 = JSON.parseArray(userMap.get("bill_record").toString(), Map.class);
		for (Map<String, Object> obj : listMapFromJson2) {
			Map<String, Object> map = new HashMap<String, Object>();
			String time = "未知";
			if (ObjectUtils.isNotBlank(obj.get("deal_time"))) {
				String text = obj.get("deal_time").toString();
				String[] split = text.split("-");
				time = split[0] + split[1];
				// 类型
			}
			String desc = "未知";
			if (ObjectUtils.isNotBlank(obj.get("desc"))) {
				if (obj.get("desc").toString().contains("2")) {
					String[] split2 = obj.get("desc").toString().split("2", 6);
					desc = split2[0];
				} else {
					desc = obj.get("desc").toString();
				}
			}

			int outcom = 0;
			int yue = 0;
			int incom = 0;
			if (ObjectUtils.isNotBlank(obj.get("outcome"))) {
				outcom = (int) (Integer.parseInt(obj.get("outcome").toString()) / 100);
			}
			if (ObjectUtils.isNotBlank(obj.get("balance"))) {
				yue = (int) (Integer.parseInt(obj.get("balance").toString()) / 100);
			}
			if (ObjectUtils.isNotBlank(obj.get("income"))) {
				incom = (int) (Integer.parseInt(obj.get("income").toString()) / 100);
			}
			if (desc.length() > 4) {
				desc = desc.substring(0, 4) + "..";

			}
			map.put("outcom", outcom);
			map.put("incom", incom);
			map.put("yue", yue);
			map.put("desc", desc);
			map.put("time", time);
			listMapFromJson.add(map);
		}
		model.addAttribute("listMapFromJson2", listMapFromJson);
		String memberToken = request.getHeader("x-memberToken");
		model.addAttribute("memberToken", memberToken);
		return mv2;
	}

	/**
	 * 社保详情
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "socialSecurityDetails")
	public ModelAndView socialSecurityDetails(HttpServletRequest request, Model model) {

		String param = request.getParameter("param");
		Member member = memberService.get(Long.parseLong(param));
		ModelAndView mv2 = new ModelAndView("app/creditArchives/socialSecurityDetails");
		mv2 = H5Utils.addPlatform(member, mv2);
		List<Map> listMapFromJsonYaL = new ArrayList<Map>();
		List<Map> listMapFromJsonSiY = new ArrayList<Map>();
		List<Map> listMapFromJsonGoS = new ArrayList<Map>();
		List<Map> listMapFromJsonSeY = new ArrayList<Map>();
		List<Map> listMapFromJsonYaL1 = new ArrayList<Map>();
		List<Map> listMapFromJsonYiL1 = new ArrayList<Map>();
		List<Map> listMapFromJsonSiY1 = new ArrayList<Map>();
		List<Map> listMapFromJsonGoS1 = new ArrayList<Map>();
		List<Map> listMapFromJsonSeY1 = new ArrayList<Map>();
		RcCaData entity = new RcCaData();
		entity.setPhoneNo(member.getUsername());
		entity.setType(RcCaData.Type.shebao);
		List<RcCaData> findList = rcCaDataService.findList(entity);
		RcCaData rcCaData = findList.get(0);
		Map<String, Object> userMap = new HashMap<String, Object>();
		userMap = JSONUtil.toMap(rcCaData.getContent());
		String string3 = userMap.get("endowment_insurance").toString();
		listMapFromJsonYaL = JSON.parseArray(string3, Map.class);
		String string4 = userMap.get("medical_insurance").toString();
		if (userMap.get("unemployment_insurance") != null) {
			String string5 = userMap.get("unemployment_insurance").toString();
			listMapFromJsonSiY = JSON.parseArray(string5, Map.class);
		}

		String string6 = userMap.get("accident_insurance").toString();
		listMapFromJsonGoS = JSON.parseArray(string6, Map.class);

		String string7 = userMap.get("maternity_insurance").toString();
		listMapFromJsonSeY = JSON.parseArray(string7, Map.class);
		for (Map<String, Object> obj : listMapFromJsonYaL) {
			Map<String, Object> map = new HashMap<String, Object>();
			int jishu = 0;
			int com = 0;
			int ge = 0;
			if (ObjectUtils.isNotBlank(obj.get("base_number"))) {
				jishu = (int) (Integer.parseInt(obj.get("base_number").toString()) / 100);
			}
			if (ObjectUtils.isNotBlank(obj.get("monthly_company_income"))) {
				com = (int) (Integer.parseInt(obj.get("monthly_company_income").toString()) / 100);
			}
			if (ObjectUtils.isNotBlank(obj.get("monthly_personal_income"))) {
				ge = (int) (Integer.parseInt(obj.get("monthly_personal_income").toString()) / 100);
			}
			map.put("jishu", jishu);
			map.put("ge1", ge);
			map.put("com", com);
			map.put("month", obj.get("month"));
			map.put("type", obj.get("type"));
			listMapFromJsonYaL1.add(map);
		}
		for (Map<String, Object> obj : listMapFromJsonYaL) {
			Map<String, Object> map = new HashMap<String, Object>();
			int jishu = 0;
			int com = 0;
			int ge = 0;
			if (ObjectUtils.isNotBlank(obj.get("base_number"))) {
				jishu = (int) (Integer.parseInt(obj.get("base_number").toString()) / 100);
			}
			if (ObjectUtils.isNotBlank(obj.get("monthly_company_income"))) {
				com = (int) (Integer.parseInt(obj.get("monthly_company_income").toString()) / 100);
			}
			if (ObjectUtils.isNotBlank(obj.get("monthly_personal_income"))) {
				ge = (int) (Integer.parseInt(obj.get("monthly_personal_income").toString()) / 100);
			}
			map.put("jishu", jishu);
			map.put("ge1", ge);
			map.put("com", com);
			map.put("month", obj.get("month"));
			map.put("type", obj.get("type"));
			listMapFromJsonYaL1.add(map);
		}
		for (Map<String, Object> obj : listMapFromJsonSiY) {
			Map<String, Object> map = new HashMap<String, Object>();
			int jishu = 0;
			int com = 0;
			int ge = 0;
			if (ObjectUtils.isNotBlank(obj.get("base_number"))) {
				jishu = (int) (Integer.parseInt(obj.get("base_number").toString()) / 100);
			}
			if (ObjectUtils.isNotBlank(obj.get("monthly_company_income"))) {
				com = (int) (Integer.parseInt(obj.get("monthly_company_income").toString()) / 100);
			}
			if (ObjectUtils.isNotBlank(obj.get("monthly_personal_income"))) {
				ge = (int) (Integer.parseInt(obj.get("monthly_personal_income").toString()) / 100);
			}
			map.put("jishu", jishu);
			map.put("ge1", ge);
			map.put("com", com);
			map.put("month", obj.get("month"));
			map.put("type", obj.get("type"));
			listMapFromJsonSiY1.add(map);
		}
		for (Map<String, Object> obj : listMapFromJsonGoS) {
			Map<String, Object> map = new HashMap<String, Object>();
			int jishu = 0;
			int com = 0;
			int ge = 0;
			if (ObjectUtils.isNotBlank(obj.get("base_number"))) {
				jishu = (int) (Integer.parseInt(obj.get("base_number").toString()) / 100);
			}
			if (ObjectUtils.isNotBlank(obj.get("monthly_company_income"))) {
				com = (int) (Integer.parseInt(obj.get("monthly_company_income").toString()) / 100);
			}
			if (ObjectUtils.isNotBlank(obj.get("monthly_personal_income"))) {
				ge = (int) (Integer.parseInt(obj.get("monthly_personal_income").toString()) / 100);
			}
			map.put("jishu", jishu);
			map.put("ge1", ge);
			map.put("com", com);
			map.put("month", obj.get("month"));
			map.put("type", obj.get("type"));
			listMapFromJsonGoS1.add(map);
		}
		for (Map<String, Object> obj : listMapFromJsonSeY) {
			Map<String, Object> map = new HashMap<String, Object>();
			int jishu = 0;
			int com = 0;
			int ge = 0;
			if (ObjectUtils.isNotBlank(obj.get("base_number"))) {
				jishu = (int) (Integer.parseInt(obj.get("base_number").toString()) / 100);
			}
			if (ObjectUtils.isNotBlank(obj.get("monthly_company_income"))) {
				com = (int) (Integer.parseInt(obj.get("monthly_company_income").toString()) / 100);
			}
			if (ObjectUtils.isNotBlank(obj.get("monthly_personal_income"))) {
				ge = (int) (Integer.parseInt(obj.get("monthly_personal_income").toString()) / 100);
			}
			map.put("jishu", jishu);
			map.put("ge1", ge);
			map.put("com", com);
			map.put("month", obj.get("month"));
			map.put("type", obj.get("type"));
			listMapFromJsonSeY1.add(map);
		}
		model.addAttribute("listMapFromJsonYaL", listMapFromJsonYaL1);
		model.addAttribute("listMapFromJsonYiL", listMapFromJsonYiL1);
		model.addAttribute("listMapFromJsonSiY", listMapFromJsonSiY1);
		model.addAttribute("listMapFromJsonGoS", listMapFromJsonGoS1);
		model.addAttribute("listMapFromJsonSeY", listMapFromJsonSeY1);
		String memberToken = request.getHeader("x-memberToken");
		model.addAttribute("memberToken", memberToken);
		return mv2;
	}

	/**
	 * 话费记录详情
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "telephoneChargesDetails")
	public ModelAndView telephoneChargesDetails(HttpServletRequest request, Model model) {

		String param = request.getParameter("param");
		Member member = memberService.get(Long.parseLong(param));
		ModelAndView mv2 = new ModelAndView("app/creditArchives/telephoneChargesDetails");
		mv2 = H5Utils.addPlatform(member, mv2);
		List<Map> listMapFromJson2 = new ArrayList<Map>();
		List<Map> listMapFromJson = new ArrayList<Map>();
		RcCaYysDetails entity = new RcCaYysDetails();
		entity.setMemberId(member.getId());
		List<RcCaYysDetails> findList = rcCaYysDetailsService.findList(entity);
		RcCaYysDetails rcCaData = findList.get(0);
		Map<String, Object> userMap = new HashMap<String, Object>();
		userMap = JSONUtil.toMap(rcCaData.getContent());
		String string51 = userMap.get("bill_info").toString();
		listMapFromJson2 = JSON.parseArray(string51, Map.class);
		for (Map<String, Object> obj : listMapFromJson2) {
			Map<String, Object> map = new HashMap<String, Object>();
			String stri = obj.get("bill_cycle").toString();
			String money = obj.get("bill_fee").toString();
			int moneyNew = (int) (Integer.parseInt(money) / 100);
			map.put("stri", stri);
			map.put("moneyNew", moneyNew);
			listMapFromJson.add(map);
		}
		model.addAttribute("listMapFromJson2", listMapFromJson);
		String memberToken = request.getHeader("x-memberToken");
		model.addAttribute("memberToken", memberToken);
		return mv2;
	}
	/**
	 * 通话记录详情
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "callRecordDetails")
	public ModelAndView callRecordDetails(HttpServletRequest request, Model model) {

		String param = request.getParameter("param");
		Member owner = memberService.get(Long.parseLong(param));
		ModelAndView mv2 = new ModelAndView("app/creditArchives/callRecordDetails");
		mv2 = H5Utils.addPlatform(owner, mv2);
		
		Member member = memberService.getCurrent();
		if (owner.getId() != member.getId()) {
			MemberFriendRelation memberFriendRelation = memberFriendRelationService.findByMemberIdAndFriendId(member.getId(), owner.getId());
			if (memberFriendRelation == null) {
				return mv2;
			}
		}
        List<Map<String, String>> callList = new ArrayList<Map<String, String>>();
		RcCaYysDetails yysDetails = new RcCaYysDetails();
		yysDetails.setMemberId(owner.getId());
		List<RcCaYysDetails> yysDetailsList = rcCaYysDetailsService.findList(yysDetails);
		if(yysDetailsList==null||yysDetailsList.size()==0) {
			return mv2;
		}
		RcCaYysDetails rcCaData = yysDetailsList.get(0);	
		JSONObject rcCaDataObj = JSON.parseObject(rcCaData.getContent());
		if(rcCaDataObj.containsKey("call_info")) {
			JSONArray callInfoList = rcCaDataObj.getJSONArray("call_info");  
			if(callInfoList!=null&&callInfoList.size()>0){
				 for(int i = 0 ;i<callInfoList.size();i++){  
					 JSONObject callInfo = callInfoList.getJSONObject(i);
					 if(callInfo.containsKey("call_record")){
						 JSONArray callRecordList = callInfo.getJSONArray("call_record"); 
						 if(callRecordList!=null&&callRecordList.size()>0) {
							 for(int j = 0 ;j<callRecordList.size();j++){  
								    JSONObject callRecord = callRecordList.getJSONObject(j);
									Map<String, String> callMap = new HashMap<String, String>();
									double count1 =(double)(Integer.parseInt(callRecord.get("call_time").toString()))/60; 
									int count=(int)Math.ceil(count1);
									String moblie=EncryptUtils.encryptString(callRecord.get("call_other_number").toString(),EncryptUtils.Type.PHONE);
									callMap.put("moblie", moblie);
									callMap.put("time", callRecord.getString("call_start_time"));
									callMap.put("count", String.valueOf(count));
									callMap.put("type", callRecord.getString("call_type_name"));
									callList.add(callMap);
								 
							 }
						 }
					 } 
				 }		
			}	
		}			
		model.addAttribute("list", callList);
		String memberToken = request.getHeader("x-memberToken");
		model.addAttribute("memberToken", memberToken);
		return mv2;
	}
	
}