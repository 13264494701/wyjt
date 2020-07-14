package com.jxf.web.wap;

import com.jxf.mem.entity.Member;
import com.jxf.mem.service.MemberCardService;
import com.jxf.mem.service.MemberService;
import com.jxf.mem.utils.MySSLProtocolSocketFactory;
import com.jxf.rc.utils.ThirdPartyUtils;
import com.jxf.svc.config.Global;
import com.jxf.svc.model.HandleRsp;
import com.jxf.svc.security.MD5Utils;
import com.jxf.svc.utils.Encodes;
import com.jxf.svc.utils.EncryptUtils;
import com.jxf.svc.utils.Exceptions;
import com.jxf.svc.utils.JSONUtil;
import com.jxf.ufang.entity.UfangLoanMarket;
import com.jxf.ufang.entity.UfangLoanMarketApplyer;
import com.jxf.ufang.entity.UfangLoanMarketApplyer.Channel;
import com.jxf.ufang.entity.UfangLoanMarketApplyer.OperatorStatus;
import com.jxf.ufang.entity.UfangLoanMarketApplyer.RealNameStatus;
import com.jxf.ufang.entity.UfangLoanMarketApplyer.SesameStatus;
import com.jxf.ufang.service.UfangLoanMarketApplyerService;
import com.jxf.web.model.ResponseData;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "${wapPath}/ufangDebt")
public class UfangDebtController extends BaseController {

    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberCardService memberCardService;
    @Autowired
    private UfangLoanMarketApplyerService ufangLoanMarketApplyerService;
    
    /**
     *  @description 优放贷首页跳转    
     *  @author suHuimin
     */
    @RequestMapping(value="index")
    public String index() {
		return "ufangDebt/index";
	}
    /**
     * @description 跳转到申请页面
     * @author suHuimin
     * @return
     */
    @RequestMapping(value="applyPre")
	public String applyPre(HttpServletRequest request,Model model) {
    	
    	String phoneNo = request.getParameter("userTel");
    	if(StringUtils.isBlank(phoneNo)) {
    		logger.error("申请的手机号码为{}",phoneNo);
    	}
		model.addAttribute("phoneNo", phoneNo);
			
		UfangLoanMarketApplyer ufangLoanMarketApplyer = new UfangLoanMarketApplyer();
		ufangLoanMarketApplyer.setUfangLoanMarket(new UfangLoanMarket(310184741351591936L));//优放贷
		ufangLoanMarketApplyer.setPhoneNo(phoneNo);
		UfangLoanMarketApplyer applyer = ufangLoanMarketApplyerService.getByPhoneNoAndMarketId(ufangLoanMarketApplyer);
			
		if (applyer == null) {
			Member member = memberService.findByUsername(phoneNo);
			// 优放贷新用户 且没有注册APP
			applyer = new UfangLoanMarketApplyer();
			applyer.setPhoneNo(phoneNo);
			applyer.setChannel(Channel.weixinPublic);
			applyer.setUfangLoanMarket(new UfangLoanMarket(310184741351591936L));//优放贷

			String applyIp = Global.getRemoteAddr(request);
			applyer.setApplyIp(applyIp);
			applyer.setApplyArea("");
			applyer.setPushStatus(UfangLoanMarketApplyer.PushStatus.pendingPush);
			
			if(member==null) {
				applyer.setAppRegister("0");
				applyer.setRealNameStatus(RealNameStatus.unauth);
				applyer.setSesameStatus(SesameStatus.unauth);
				applyer.setOperatorStatus(OperatorStatus.unauth);
				ufangLoanMarketApplyerService.save(applyer);
				model.addAttribute("smAuth", false);
				model.addAttribute("zmfAuth", false);
				model.addAttribute("yysAuth", false);
				model.addAttribute("applyer",applyer);
				return "ufangDebt/perfectInfoPre";
			}else {
				applyer.setAppRegister("1");
				applyer.setMember(member);
				// 优放贷新用户 且注册了APP
				applyer = ufangLoanMarketApplyerService.saveApplyerByMemberInfo(applyer, member);
				if (applyer.getRealNameStatus().equals(RealNameStatus.unauth)) {
					// 没有实名认证
					model.addAttribute("applyer", applyer);
					return "ufangDebt/perfectInfoPre";
				}
				if (applyer.getSesameStatus().equals(SesameStatus.unauth) || applyer.getOperatorStatus().equals(OperatorStatus.unauth)) {
					String idNo = applyer.getIdNo();
					String newIdNo = EncryptUtils.encryptString(idNo, EncryptUtils.Type.ID);
					applyer.setIdNo(newIdNo);
					String cardNo = applyer.getCardNo();
					String newCardNo = EncryptUtils.encryptString(cardNo, EncryptUtils.Type.CARD);
					applyer.setCardNo(newCardNo);
					model.addAttribute("applyer", applyer);
					return "ufangDebt/perfectInfo";
				}
				// 芝麻分和运营商均已认证 待转为流量
				return "ufangDebt/applyResult";
			}
		}else {
			applyer.setChannel(Channel.weixinPublic);
	        //每周一个用户只能提交一次
	        if (ufangLoanMarketApplyerService.selectWeekUpdateCount(310184741351591936L,phoneNo) == 0) {
	        	applyer.setPushStatus(UfangLoanMarketApplyer.PushStatus.pendingPush);
	        }	
			String applyIp = Global.getRemoteAddr(request);
			applyer.setApplyIp(applyIp);
			applyer.setApplyArea("");
			applyer.setApplyTimes(applyer.getApplyTimes() == null ? 0 : applyer.getApplyTimes() + 1);
			ufangLoanMarketApplyerService.save(applyer);

			if (applyer.getRealNameStatus().equals(RealNameStatus.unauth)) {
				// 没有实名认证的先让用户重新认证
				model.addAttribute("applyer", applyer);
				return "ufangDebt/perfectInfoPre";
			}
			if (applyer.getSesameStatus().equals(SesameStatus.unauth) || applyer.getOperatorStatus().equals(OperatorStatus.unauth)) {
				// 跳转认证页面
				String idNo = applyer.getIdNo();
				String newIdNo = EncryptUtils.encryptString(idNo, EncryptUtils.Type.ID);
				applyer.setIdNo(newIdNo);
				String cardNo = applyer.getCardNo();
				String newCardNo = EncryptUtils.encryptString(cardNo, EncryptUtils.Type.CARD);
				applyer.setCardNo(newCardNo);
				model.addAttribute("applyer", applyer);
				return "ufangDebt/perfectInfo";
			}
		}
		// 已认证用户，待转为流量
		return "ufangDebt/applyResult";
	}
    /**
     * @description 申请人银行卡四要素认证
     * @param request
     * @return
     */
    @RequestMapping(value="siYaoSuRenzheng")
    @ResponseBody
    public ResponseData checkCard4Factors(HttpServletRequest request) {
		String phoneNo = request.getParameter("phoneNo");
		String idNo = request.getParameter("idNo");
		String realName = request.getParameter("realName");
		String cardNo = request.getParameter("cardNo");
		HandleRsp rsp = memberCardService.checkCard4Factors(cardNo, realName, idNo, phoneNo);
		if(rsp.getStatus()) {
			return ResponseData.success(rsp.getMessage());
		}else {
			return ResponseData.error(rsp.getMessage());
		}
	}
    /**
     * @description 跳转到完善信息页面
     * @param request
     * @return
     */
    @RequestMapping(value="perfectInfo")
    public String perfectInfo(HttpServletRequest request,Model model) {
		String idNo = request.getParameter("userId");
		String realName = request.getParameter("userName");
		String cardNo = request.getParameter("userBankCode");
		String applyerId = request.getParameter("applyerId");
		String weixinNo = request.getParameter("weixinNo");
		UfangLoanMarketApplyer applyer = ufangLoanMarketApplyerService.get(Long.valueOf(applyerId));
		applyer.setIdNo(idNo);
		applyer.setRealNameStatus(RealNameStatus.authed);
		applyer.setName(realName);
		applyer.setCardNo(cardNo);
		applyer.setWeixinNo(weixinNo);
		applyer.setSesameStatus(SesameStatus.unauth);
		applyer.setOperatorStatus(OperatorStatus.unauth);
		ufangLoanMarketApplyerService.save(applyer);
		String newIdNo = EncryptUtils.encryptString(idNo, EncryptUtils.Type.ID);
		applyer.setIdNo(newIdNo);
		String newCardNo = EncryptUtils.encryptString(cardNo, EncryptUtils.Type.CARD);
		applyer.setCardNo(newCardNo);
		model.addAttribute("applyer", applyer);
		return "ufangDebt/perfectInfo";
	}
    
    /**
     * @description 优放贷芝麻分认证
     * @param phoneNo
     * @param idNo
     * @param realName
     * @param idCard
     */
    @RequestMapping(value="sesameAuth")
    public String sesameAuth(HttpServletRequest request) {
    	String sequenceNo = request.getParameter("applyerId");
    	UfangLoanMarketApplyer applyer = ufangLoanMarketApplyerService.get(Long.valueOf(sequenceNo));
    	String name = applyer.getName();
    	String phoneNo = applyer.getPhoneNo();
    	String idNo = applyer.getIdNo();
    	//获取token
    	String zmToken = getZmToken(name, phoneNo, idNo, sequenceNo);
    	logger.info("芝麻token{}",zmToken);
    	//获取公信宝配置信息
    	String returnUrl = Encodes.urlEncode(Global.getConfig("domain")+"/callback/gxb/returnUfangDebtPage?uid="+sequenceNo);
    	String sesameUrl = "https://prod.gxb.io/v2/auth/sesame_multiple?token=" + zmToken + "&returnUrl="+returnUrl;
    	//跳转到公信宝芝麻分认证页面
    	return "redirect:" + sesameUrl;
    }
    
    /**
     * @description 优放贷运营商认证
     * @param phoneNo
     * @param idNo
     * @param realName
     */
    @RequestMapping(value="operatorAuth")
    public String operatorAuth(HttpServletRequest request) {
    	String applyerId = request.getParameter("applyerId");
    	UfangLoanMarketApplyer applyer = ufangLoanMarketApplyerService.get(Long.valueOf(applyerId));
    	String name = applyer.getName();
    	String idNo = applyer.getIdNo();
    	String token = Global.getConfig("sjmhtoken");
    	try {
			name = URLEncoder.encode(name, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error(Exceptions.getStackTraceAsString(e));
		}
    	String url = "https://amazing.shujumohe.com/box/yys?box_token="+ token +"&arr_pass_hide=real_name,identity_code,passback_params&real_name="
				+ name + "&identity_code=" + idNo + "&passback_params=ufdebt_"+ applyerId;
    	String returnUrl = "&cb=" + Encodes.urlEncode(Global.getConfig("domain") + "/wap/wyjt/ufangDebt/operatorAuthComplete?uid="+applyerId);
    	url = url + returnUrl;
    	return "redirect:"+url;
    }
    
    /**
     * @description 完成申请
     * @param request
     * @return
     */
    @RequestMapping(value="apply")
    public String apply(HttpServletRequest request,Model model) {
//		String applyerId = request.getParameter("applyerId");
//		UfangLoanMarketApplyer applyer = ufangLoanMarketApplyerService.get(Long.valueOf(applyerId));
		return "ufangDebt/applyResult";
	}
    /**
     * @description 获取优放贷应用下的token
     * @param name
     * @param phoneNo
     * @param idNo
     * @param sequenceNo
     * @return
     */
    private  String getZmToken(String name, String phoneNo, String idNo, String sequenceNo) {
		StringBuffer ss = new StringBuffer();
		ss.append("{");
		ss.append("\"name\":");
		ss.append("\"" + name + "\",");

		ss.append("\"phone\":");
		ss.append("\"" + phoneNo + "\",");

		ss.append("\"authItem\":");
		ss.append("\"" + ThirdPartyUtils.authItem + "\",");

		ss.append("\"appId\":");
		ss.append("\"" + ThirdPartyUtils.appId4UfangDebt + "\",");

		ss.append("\"idcard\":");
		ss.append("\"" + idNo + "\",");

		ss.append("\"sequenceNo\":");
		ss.append("\"" + sequenceNo + "\",");

		Date date = new Date();
		ss.append("\"timestamp\":");
		ss.append("\"" + date.getTime() + "\",");

		String sign = MD5Utils.EncoderByMd5(ThirdPartyUtils.appId4UfangDebt + ThirdPartyUtils.appSecret4UfangDebt + ThirdPartyUtils.authItem
				+ date.getTime() + sequenceNo);
		ss.append("\"sign\":");
		ss.append("\"" + sign + "\"");
		ss.append("}");
		String ess = ss.toString();
		HttpClient httpClient = new HttpClient();
		// 设置连接超时时间(单位毫秒)
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(60 * 1000);
		// 设置读取超时时间(单位毫秒)
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(60 * 1000);
		Protocol myhttps = new Protocol("https", new MySSLProtocolSocketFactory(), 443);
		Protocol.registerProtocol("https", myhttps);
		PostMethod method = new PostMethod(ThirdPartyUtils.url);
		String token = "0126200676000WKN35TiMa7vFCb4Q3Ds";
		try {
			RequestEntity entity = new StringRequestEntity(ess, "application/json", "UTF-8");
			method.setRequestEntity(entity);
			httpClient.executeMethod(method);
			int code = method.getStatusCode();
			// 判断请求是否成功
			if (code == HttpStatus.SC_OK) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream()));
				StringBuffer stringBuffer = new StringBuffer();
				String str = "";
				while ((str = reader.readLine()) != null) {
					stringBuffer.append(str);
				}
				// 获取第三方的结果
				String rsp = stringBuffer.toString();
				if (StringUtils.isBlank(rsp)) {
					// 如果没有拿到token，给个默认假的
					return token;
				} else {
					Map<String, Object> mapFromJson = JSONUtil.toMap(rsp);
					logger.info("获取公信宝芝麻分token时的返回数据：{}",mapFromJson);
					if (mapFromJson.get("retCode").toString().equals("1")) {
						Map<String, Object> mapFromJson1 = JSONUtil.toMap(mapFromJson.get("data").toString());
						token = mapFromJson1.get("token").toString();
					} else {
						return token;
					}
				}
			}
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(e));
		}
		return token;
	}
    /**
     * @description 运营商认证完之后回跳页面
     * @return
     */
    @RequestMapping(value="operatorAuthComplete")
    public ModelAndView operatorAuthComplete(HttpServletRequest request) {
    	String uid = request.getParameter("uid");
    	UfangLoanMarketApplyer applyer = ufangLoanMarketApplyerService.get(Long.valueOf(uid));
    	applyer.setOperatorStatus(OperatorStatus.authed);
    	ufangLoanMarketApplyerService.save(applyer);
    	ModelAndView mv = new ModelAndView("ufangDebt/perfectInfo");
    	mv.addObject("applyer", applyer);
    	return mv;
	}
}