package com.jxf.web.app.wyjt;


import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jxf.loan.entity.NfsLoanApply;
import com.jxf.loan.entity.NfsLoanApply.RepayType;
import com.jxf.loan.entity.NfsLoanRecord;
import com.jxf.loan.service.NfsLoanApplyService;
import com.jxf.loan.utils.LoanUtils;
import com.jxf.mem.entity.Member;
import com.jxf.mem.service.MemberService;
import com.jxf.mms.msg.SendSmsMsgService;
import com.jxf.mms.msg.utils.VerifyCodeUtils;
import com.jxf.nfs.entity.NfsWdrlRecord;
import com.jxf.nfs.service.NfsWdrlRecordService;
import com.jxf.svc.annotation.AccessLimit;
import com.jxf.svc.cache.RedisUtils;
import com.jxf.svc.config.Constant;
import com.jxf.svc.config.Global;
import com.jxf.svc.security.jwt.JwtUtil;
import com.jxf.svc.security.rsa.RSAService;
import com.jxf.svc.servlet.Servlets;
import com.jxf.svc.sys.area.entity.Area;
import com.jxf.svc.sys.area.service.AreaService;
import com.jxf.svc.sys.version.entity.SysVersion;
import com.jxf.svc.sys.version.service.SysVersionService;
import com.jxf.svc.utils.FileUploadUtils;
import com.jxf.svc.utils.IdCardUtils;
import com.jxf.svc.utils.ImageCaptchaUtils;
import com.jxf.svc.utils.RandomValue;
import com.jxf.svc.utils.StringUtils;
import com.jxf.web.model.CheckSmsRequestParam;
import com.jxf.web.model.GetUpSmsVerifyCodeResponseResult;
import com.jxf.web.model.ResponseData;
import com.jxf.web.model.SendSmsRequestParam;
import com.jxf.web.model.gxt.CheckPhoneResponseResult;
import com.jxf.web.model.wyjt.app.ShareCreditChangeRequestParam;
import com.jxf.web.model.wyjt.app.member.CheckMemberRequestParam;


/**
 * @类功能说明： 公用
 * @类修改者：
 * @修改日期：
 * @修改说明：
 * @公司名称：
 * @作者：wo
 * @创建时间：2018年10月25日 上午10:56:53
 * @版本：V1.0
 */
@Controller("wyjtAppCommonController")
@RequestMapping(value = "${wyjtApp}/common")
public class CommonController {
	
	/**
	 * 日志对象
	 */
	protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RSAService rsaService;
    @Autowired
    private AreaService areaService;
    @Autowired
    private SendSmsMsgService sendSmsMsgService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private NfsWdrlRecordService nfsWdrlRecordService;
    @Autowired
    private NfsLoanApplyService loanApplyService;
	@Autowired
	private SysVersionService sysVersionService;


    /**
     * 公钥
     */
    @RequestMapping(value = "/publicKey", method = RequestMethod.GET)
    public @ResponseBody
    Map<String, String> publicKey(HttpServletRequest request) {
        RSAPublicKey publicKey = rsaService.generateKey(request);
        Map<String, String> data = new HashMap<String, String>();
        data.put("modulus", Base64.encodeBase64String(publicKey.getModulus().toByteArray()));
        data.put("exponent", Base64.encodeBase64String(publicKey.getPublicExponent().toByteArray()));
        return data;
    }

    /**
     * 地区
     */
    @RequestMapping(value = "/area", method = RequestMethod.GET)
    public @ResponseBody
    List<Map<String, Object>> area(Long parentId) {
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        Area parent = areaService.get(parentId);
        Collection<Area> areas = parent != null ? areaService.getChildren(parent) : areaService.getRoot();
        for (Area area : areas) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("name", area.getName());
            item.put("value", area.getId());
            data.add(item);
        }
        return data;
    }

    /**
     * 获取图形验证码
     *
     * @throws IOException
     */
    @RequestMapping(value = "getImageCaptcha")
    public void getImageCaptcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ImageCaptchaUtils.createImageCaptcha(request, response);
    }

    /**
     * 验证图形验证码并发送短信验证码
     */
    @RequestMapping(value = "/checkImageCaptchaAndSendSmsCode", method = RequestMethod.POST)
    public @ResponseBody
    ResponseData checkImageCaptchaAndSendSmsCode(HttpServletRequest request) {
        String param = request.getParameter("param");
        SendSmsRequestParam reqData = JSONObject.parseObject(param, SendSmsRequestParam.class);

        String serverImageCaptcha = RedisUtils.get("imageCode" + reqData.getCaptchaId());
        if (StringUtils.isNotBlank(serverImageCaptcha) && StringUtils.equalsIgnoreCase(serverImageCaptcha, reqData.getImageCaptcha())) {
            //生成短信验证码
            String smsCode = VerifyCodeUtils.genSmsValidCode();
            //将短信验证码存入缓存中
			RedisUtils.setForTimeMIN("smsCode" + reqData.getPhoneNo(), smsCode, 10);
            sendSmsMsgService.sendSms(reqData.getTmplCode(), reqData.getPhoneNo(), smsCode);
			RedisUtils.delete("imageCode" + reqData.getCaptchaId());
        } else {
            return ResponseData.error("图形验证码错误 请重试");
        }
        return ResponseData.success("发送短信验证码成功");
    }

    /**
     * 发送短信验证码
     */
    @RequestMapping(value = "/sendSmsVerifyCode", method = RequestMethod.POST)
    public @ResponseBody
    ResponseData sendSmsVerifyCode(HttpServletRequest request) {
        String param = request.getParameter("param");
        SendSmsRequestParam reqData = JSONObject.parseObject(param, SendSmsRequestParam.class);

        //生成短信验证码
        String smsCode = VerifyCodeUtils.genSmsValidCode();
        //将短信验证码存入缓存中
		RedisUtils.setForTimeMIN("smsCode" + reqData.getPhoneNo(), smsCode, 10);
        sendSmsMsgService.sendSms(reqData.getTmplCode(), reqData.getPhoneNo(), smsCode);

        return ResponseData.success("发送短信验证码成功");
    }

    /**
     * 验证短信验证码
     */
    @RequestMapping(value = "/checkSmsCode", method = RequestMethod.POST)
    public @ResponseBody
    ResponseData checkSmsCode(HttpServletRequest request) {
        String param = request.getParameter("param");
        CheckSmsRequestParam reqData = JSONObject.parseObject(param, CheckSmsRequestParam.class);
        String serverSmsCode = RedisUtils.get("smsCode" + reqData.getPhoneNo());
        if (StringUtils.isNotBlank(serverSmsCode) && StringUtils.equals(serverSmsCode, reqData.getSmsCode())) {
        	return ResponseData.success("验证短信验证码成功");
        } else {
            return ResponseData.error("短信验证码错误 请重试");
        }
    }
    /**
     * 发送短信验证码H5页面用
     */
    @AccessLimit(maxCount=5,seconds=43200)
    @RequestMapping(value = "/sendSmsVerifyCodeForWeb", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData sendSmsVerifyCodeForWeb(HttpServletRequest request) {
    	String phoneNo = request.getParameter("phoneNo");
    	String tmplCode = request.getParameter("tmplCode");
    	
        //生成短信验证码
        String smsCode = VerifyCodeUtils.genSmsValidCode();
        //将短信验证码存入缓存中
		RedisUtils.setForTimeMIN("smsCode" + phoneNo, smsCode, 10);
        sendSmsMsgService.sendSms(tmplCode, phoneNo, smsCode);

        return ResponseData.success("短信验证码发送成功");
    }
    /**
     * 验证短信验证码H5页面用
     */
    @RequestMapping(value = "/checkSmsCodeForWeb", method = RequestMethod.POST)
    @ResponseBody 
    public ResponseData checkSmsCodeForWeb(HttpServletRequest request) {
        String phoneNo = request.getParameter("phoneNo");
        String smsCode = request.getParameter("smsCode");
        String serverSmsCode = RedisUtils.get("smsCode" + phoneNo);
        if (StringUtils.isNotBlank(serverSmsCode) && StringUtils.equals(serverSmsCode, smsCode)) {
        	RedisUtils.delete("smsCode"+phoneNo);
        	return ResponseData.success("验证短信验证码成功");
        } else {
            return ResponseData.error("短信验证码错误 请重试");
        }
    }
    
    /**
	 * 	发送短信验证码 并校验注册手机号(注册时用)
	 */
	@RequestMapping(value = "/checkPhone")
	public @ResponseBody
	ResponseData checkPhone(HttpServletRequest request){
		CheckPhoneResponseResult result = new CheckPhoneResponseResult();
		String param = request.getParameter("param");
        SendSmsRequestParam reqData = JSONObject.parseObject(param, SendSmsRequestParam.class);

        String phoneNo = reqData.getPhoneNo();
        //生成短信验证码
        String smsCode = VerifyCodeUtils.genSmsValidCode();
        //将短信验证码存入缓存中
		RedisUtils.setForTimeMIN("smsCode" + phoneNo, smsCode, 10);
        sendSmsMsgService.sendSms(reqData.getTmplCode(), phoneNo, smsCode);
		
		Member member = memberService.findByUsername(phoneNo);
		if(member == null) {
			result.setIsMember("0");
			return ResponseData.success("发送验证码成功", result);
		}else {
			result.setIsMember("1");
			return ResponseData.success("发送验证码成功", result);
		}
	}
	
	/**
	 * 	校验手机号
	 */
	@AccessLimit(maxCount = 2, seconds = 1)
	@RequestMapping(value = "/checkMember")
	public @ResponseBody
	ResponseData checkMember(HttpServletRequest request){
		CheckPhoneResponseResult result = new CheckPhoneResponseResult();
		String param = request.getParameter("param");
		CheckMemberRequestParam reqData = JSONObject.parseObject(param, CheckMemberRequestParam.class);
        String phoneNo = reqData.getPhoneNo();
        
		Member member = memberService.findByUsername(phoneNo);
		if(member == null) {
			result.setIsMember("0");
			return ResponseData.success("不是平台用户", result);
		}else {
			result.setIsMember("1");
			return ResponseData.success("是平台用户", result);
		}
		
	}

    /**
     * 获取上行短信参数
     */
    @RequestMapping(value = "/getUpSmsVerifyCode")
    @ResponseBody
    public ResponseData getUpSmsVerifyCode() {
        GetUpSmsVerifyCodeResponseResult result = new GetUpSmsVerifyCodeResponseResult();
        result.setUp("10690529135060");
        int randomInt = new Random().nextInt(899999) + 100000;
        result.setYzm("" + randomInt);
        return ResponseData.success("获取上行短信参数成功", result);
    }


    /**
     * 扫描二维码
     */
    @RequestMapping(value = "/qrcodeAddFriend")
    public String qrcode(HttpServletRequest request, Model model) {
        String agent = request.getHeader("user-agent").toUpperCase();
		//如果是微信
//		if (agent.contains("MICROMESSENGER")) {
//			//判断是安卓还是ios返回提示浏览器打开图片
//			if (agent.contains("IPHONE")) {
//				model.addAttribute("imgpath", Global.getConfig("domain")+"/mb/assets/images/wechat-tips-ios.jpg");
//			} else {
//				model.addAttribute("imgpath", Global.getConfig("domain")+"/mb/assets/images/wechat-tips-android.jpg");
//			}
//			return "app/download/wechatpage";
//		}
        //返回下载页
        return "app/download/download";
    }

	@RequestMapping(value = "/callApp")
	public String callApp(HttpServletRequest request, Model model) {
		String agent = request.getHeader("user-agent").toUpperCase();
		//如果是微信
//		if (agent.contains("MICROMESSENGER")) {
//			//判断是安卓还是ios返回提示浏览器打开图片
//			if (agent.contains("IPHONE")) {
//				model.addAttribute("imgpath", Global.getConfig("domain")+"/mb/assets/images/wechat-tips-ios.jpg");
//			} else {
//				model.addAttribute("imgpath", Global.getConfig("domain")+"/mb/assets/images/wechat-tips-android.jpg");
//			}
//			return "app/download/wechatpage";
//		}
		//返回下载页
		return "app/download/callApp";
	}

	@RequestMapping(value = "/download")
	public String download(HttpServletRequest request) {
		String agent = request.getHeader("user-agent").toUpperCase();
		//如果是微信
		if (agent.contains("MICROMESSENGER")) {
			return "redirect:https://android.myapp.com/myapp/detail.htm?apkName=com.yxbao.faith&ADTAG=mobile";
		}
		if (agent.contains("IPHONE")) {
			return "https://itunes.apple.com/cn/app/%E6%97%A0%E5%BF%A7%E5%80%9F%E6%9D%A1-%E7%94%B5%E5%AD%90%E5%80%9F%E6%9D%A1%E5%80%9F%E6%8D%AE/id1321795027?l=zh&ls=1&mt=8";
		}
		SysVersion	androidVersion = sysVersionService.getByType(SysVersion.Type.android);	
		return "redirect:" + androidVersion.getUrl();
	}
	
	/**
	 * 用户上传视频
	 * @return
	 */
	@RequestMapping(value="uploadVideo")
	@ResponseBody
	public ResponseData uploadVideo(MultipartHttpServletRequest multiRequest,HttpServletResponse response) {
		 String imageUploadPath = "";
			// 获取多个文件
		    try {
		     for (Iterator<String>  it = multiRequest.getFileNames(); it.hasNext(); ) {
		         // 文件名
		         String key = (String) it.next();
		         // 根据key得到文件
		         MultipartFile multipartFile = multiRequest.getFile(key);
		         if (multipartFile.getSize() <= 0) {
		             continue;
		         }
			    String extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
				File tempFile = new File(FileUtils.getTempDirectory(), UUID.randomUUID() + ".tmp");				
				multipartFile.transferTo(tempFile);	
				//Servlets.getRequest().getContextPath() 需要在网络请求的线程里面才能取到
	            imageUploadPath = Global.getConfig("domain")+Servlets.getRequest().getContextPath()+FileUploadUtils.upload(tempFile, "video", "video", extension);           
		     }
			} catch (IllegalStateException | IOException e) {			
				e.printStackTrace();
				return ResponseData.error("视频上传错误");
			}
		    return ResponseData.success("上传成功", imageUploadPath);	
	}
	
	/**
	 * 用户上传图片
	 * @return
	 */
	@RequestMapping(value="uploadImage")
	@ResponseBody
	public ResponseData uploadImage(MultipartHttpServletRequest multiRequest,HttpServletResponse response) {
	    
		 String imageUploadPath = "";
		// 获取多个文件
	    try {
	     for (Iterator<String>  it = multiRequest.getFileNames(); it.hasNext(); ) {
	         // 文件名
	         String key = (String) it.next();
	         // 根据key得到文件
	         MultipartFile multipartFile = multiRequest.getFile(key);
	         if (multipartFile.getSize() <= 0) {
	             continue;
	         }
		    String extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
			File tempFile = new File(FileUtils.getTempDirectory(), UUID.randomUUID() + ".tmp");				
			multipartFile.transferTo(tempFile);	
			//Servlets.getRequest().getContextPath() 需要在网络请求的线程里面才能取到
            imageUploadPath = Global.getConfig("domain")+Servlets.getRequest().getContextPath()+FileUploadUtils.upload(tempFile, "image", "image", extension);           
	     }
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
			return ResponseData.error("图片上传错误");
		}
	    return ResponseData.success("上传成功", imageUploadPath);		
	}
	
	
    /**
	    * 充值失败解答
	    * @param title
	    * @param data
	    * @return
	    */
	   @RequestMapping(value = "rechargeHelp")
	   public ModelAndView rechargeHelp(String title,String data) {
		   String titleName = "";
		   if (null == title) {
			   title = "czsm";
		   }
		   if ("czsm".equals(title)) {
			   titleName = "充值说明";
		   } else if ("cjwt".equals(title)) {
			   titleName = "常见问题";
		   } else if ("aqbz".equals(title)) {
			   titleName = "安全保障";
		   } else if ("sfrz".equals(title)) {
			   titleName = "身份认证";
		   } else if ("zhzc".equals(title)) {
			   titleName = "账号注册";
		   } else if ("hyjk".equals(title)) {
			   titleName = "好友借款";
		   } else if ("cztx".equals(title)) {
			   titleName = "充值提现";
		   } else if ("csfw".equals(title)) {
			   titleName = "催收服务";
		   }
		   
		   ModelAndView mv = new ModelAndView("app/rechargeHelp");
		   mv.addObject("title", title);
		   mv.addObject("titleName", titleName);
		   mv.addObject("data", data);
		   return mv;
		} 
	   
	   /**
		 * 提现记录详情
		 * @param id
		 * @return
		 */
		@RequestMapping(value="withdrawDetail")
		public ModelAndView withdrawDetail (HttpServletRequest request,String id) {
			ModelAndView mv2 = new ModelAndView("app/withdraw/withdraw_details");
			NfsWdrlRecord nfsWdrlRecord = nfsWdrlRecordService.get(Long.valueOf(id));
			Member member = memberService.get(nfsWdrlRecord.getMember().getId());
			Member member2 = new Member();
			member2.setName(member.getName());
			NfsWdrlRecord wdrlRecord = new NfsWdrlRecord();
			wdrlRecord.setAmount(nfsWdrlRecord.getAmount());
			wdrlRecord.setStatus(nfsWdrlRecord.getStatus());
			wdrlRecord.setFee(nfsWdrlRecord.getFee());
			wdrlRecord.setId(nfsWdrlRecord.getId());
			wdrlRecord.setCreateTime(nfsWdrlRecord.getCreateTime());
			mv2.addObject("member", member2);
			mv2.addObject("detail", wdrlRecord);
			mv2.addObject("actualMoney", StringUtils.decimalToStr(nfsWdrlRecord.getAmount().subtract(new BigDecimal(nfsWdrlRecord.getFee())), 2));
			String appPlatform = (String) RedisUtils.getHashKey("loginInfo"+member.getId(), "osType");
			mv2.addObject("appPlatform", appPlatform);
			boolean isWeiXin = false;
		    if (StringUtils.startsWith(appPlatform, "weixin_")) {
		        isWeiXin = true;
		        appPlatform = appPlatform.replace("weixin_", "");
		    }	  	    
		    mv2.addObject("isWeiXin", isWeiXin);
			return mv2;
		}
		/**
		 * 友借款服务协议
		 * @return
		 */
		@RequestMapping(value="agreement")
		public  ModelAndView agreement(String appPlatform,String isWeiXin) {
			ModelAndView mv = new ModelAndView("app/agreement");
			mv.addObject("platForm", appPlatform);
			mv.addObject("isWeiXin", Boolean.valueOf(isWeiXin));
			return mv;
		}
		
		/**
		 * 查看详细说明
		 * @return
		 */
		@RequestMapping(value="lawArbiDetailedExplain")
		public  ModelAndView lawArbiDetailedExplain(String appPlatform,String isWeiXin,String zcType) {
			ModelAndView mv = new ModelAndView("app/lawArbiDetailedExplain");
			mv.addObject("platForm", appPlatform);
			mv.addObject("isWeiXin", Boolean.valueOf(isWeiXin));
			mv.addObject("zcType", zcType);
			return mv;
		}
		/**
		 * 分期计划
		 * @return
		 */
		@RequestMapping(value="loanPeriodDetails")
		public ModelAndView loanPeriodDetails(HttpServletRequest request) {
			String applyId = request.getParameter("applyId");
			String intRateStr = request.getParameter("intRate");
			String amountStr = request.getParameter("amount");
			String loanTermStr = request.getParameter("loanTerm");
			String appPlatform = request.getParameter("appPlatform");
			String isWeiXin = request.getParameter("isWeiXin");
			BigDecimal amount = null;
			BigDecimal intRate = null;
			int loanTerm = 0;
			NfsLoanRecord record = null;
			if(StringUtils.isNotBlank(applyId) ) {
				NfsLoanApply apply = loanApplyService.get(Long.valueOf(applyId));
		    	amount = apply.getAmount();
		    	intRate = apply.getIntRate();
		    	loanTerm = apply.getTerm();
		    	record = LoanUtils.calInt(amount, intRate, RepayType.principalAndInterestByMonth, loanTerm);
			}else {
				amount = new BigDecimal(amountStr);
		    	intRate = new BigDecimal(intRateStr);
		    	loanTerm = Integer.valueOf(loanTermStr);
		    	record = LoanUtils.calInt(amount, intRate, RepayType.principalAndInterestByMonth, loanTerm);
			}
	    	ModelAndView mv = new ModelAndView("app/repaymentPlan");
			BigDecimal interest = record.getInterest();
			mv.addObject("platForm", appPlatform);
			mv.addObject("isWeiXin", Boolean.valueOf(isWeiXin));
			mv.addObject("repayRecordList", record.getRepayRecordList());
			mv.addObject("interest", StringUtils.decimalToStr(interest, 2));
			mv.addObject("amountAndInt", StringUtils.decimalToStr(amount.add(interest), 2));
			mv.addObject("amount", StringUtils.decimalToStr(amount, 2));
			return mv;
		}
	/**
	 * 分期规则页面跳转
	 * @return
	 */
	@RequestMapping("/stagesRule")
	public ModelAndView stagesRule(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("app/stagesRule");
		String memberToken = request.getHeader("x-memberToken");
		ResponseData responseData= JwtUtil.verifyToken(memberToken);
		String appPlatform = "";
		boolean isWeiXin = false;
		if(responseData.getCode()==Constant.JWT_SUCCESS){
			JSONObject payload = (JSONObject) JSON.toJSON(responseData.getResult());
			Long id = payload.getLong("id");
			appPlatform = (String)RedisUtils.getHashKey("loginInfo"+id, "osType");
		}
		if(StringUtils.startsWith(appPlatform, "weixin_")) {
			isWeiXin = true;
			appPlatform = StringUtils.replace(appPlatform, "weixin_", "");
		}
		mv.addObject("appPlatform", appPlatform);
		mv.addObject("isWeiXin", isWeiXin);
		return mv;
	}

	@RequestMapping("/question")
	public String question(Model model) {
		Member member = memberService.getCurrent2();
		String appPlatform = (String) RedisUtils.getHashKey("loginInfo" + member.getId(), "osType");
		boolean isWeiXin = false;
		if (StringUtils.startsWith(appPlatform, "weixin_")) {
			isWeiXin = true;
			appPlatform = appPlatform.replace("weixin_", "");
		}
		model.addAttribute("isWeiXin", isWeiXin);
		model.addAttribute("appPlatform", appPlatform);
		return "app/page/question";
	}

	@RequestMapping("/answer")
	public String answer(Model model, String appPlatform) {
		model.addAttribute("appPlatform", appPlatform);
		return "app/page/answer";
	}

	@RequestMapping("/upgradeInstructions")
	public String upgradeInstructions(Model model, String appPlatform) {
		if (appPlatform == null) {
			Member member = memberService.getCurrent2();
			appPlatform = (String) RedisUtils.getHashKey("loginInfo" + member.getId(), "osType");
		}
		model.addAttribute("appPlatform", appPlatform);
		return "app/page/upgradeInstructions";
	}


	/**
	 * 信誉变化分享
	 * @param
	 * @return
	 */
	@RequestMapping("/shareCreditChange")
	public String shareCreditChange(HttpServletRequest request, Model model) {

		String param = request.getParameter("param");
		ShareCreditChangeRequestParam reqData = JSONObject.parseObject(param, ShareCreditChangeRequestParam.class);

		String rankNo = reqData.getRankName();
		String percent = reqData.getPercent();
		String type = reqData.getType();

		model.addAttribute("type", type);
		model.addAttribute("nickname", reqData.getNickname());
		model.addAttribute("rankNo", rankNo);

		if ("1".equals(type)) {
			switch (rankNo) {
				case "A":
					model.addAttribute("percent", "您已成功立足" + percent + "%人之上，<br/>跻身信用达人行列！");
					break;
				case "AA":
					model.addAttribute("percent", "信用达人，<br/>已成功立足" + percent + "%人之上，<br/>可以用来装Bility啦！");
					break;
				case "AAA":
					model.addAttribute("percent", "超级信用，<br/>已有" + percent + "%的用户被您的信用值秒杀！");
					break;
				case "AAAA":
					model.addAttribute("percent", "超级信用，<br/>已有" + percent + "%的用户被您的信用值秒杀！");
					break;
				default:
					break;
			}
		} else {
			switch (rankNo) {
				case "AAA":
					model.addAttribute("percent", "降级容易升级难，<br/>请珍惜您的信用！");
					break;
				case "AA":
					model.addAttribute("percent", "降级容易升级难，<br/>请珍惜您的信用！");
					break;
				case "A":
					model.addAttribute("percent", "降级容易升级难，<br/>请珍惜您的信用！");
					break;
				case "B":
					model.addAttribute("percent", "无忧借条温馨提示：<br/>信用需要及时维护！");
					break;
				case "C":
					model.addAttribute("percent", "您已进入失信地图，<br/>且行且珍惜！");
					break;
				default:
					break;
			}
		}

		return "app/page/shareCreditChange";
	}

	/**
	 * 信誉分享
	 * @param model
	 * @return
	 */
	@RequestMapping("/shareCreditRating")
	@AccessLimit(maxCount = 1, seconds = 1)
	public String shareCreditRating(Model model, Long id, String appPlatform) {

		boolean isWeiXin = false;
		if (StringUtils.startsWith(appPlatform, "weixin_")) {
			isWeiXin = true;
			appPlatform = appPlatform.replace("weixin_", "");
		}
		if(id==null) {
			return "app/page/grade-meter-share";
		}
		Member member = memberService.get(id);
		if(member==null) {
			member = new Member();
			member.setUsername(RandomValue.getTel());
			member.setName(RandomValue.getChineseName());
			member.setEmail(RandomValue.getEmail(10, 32));
			member.setAddr(RandomValue.getRoad());
			member.setIdNo(IdCardUtils.getRandomID());
			model.addAttribute("isWeiXin", isWeiXin);
			model.addAttribute("appPlatform", appPlatform);
			model.addAttribute("member", member);
			if(id%10==0||id%10==6) {
				model.addAttribute("className","xinyongdj");
				model.addAttribute("title","太棒啦!");
				model.addAttribute("tips","您已达到信用巅峰");
				model.addAttribute("tips1","信用超级好");
				model.addAttribute("tips2","我击败了全国99.99%的用户");
			}else if(id%10==1||id%10==5||id%10==8){
				model.addAttribute("className","grayBg");
				model.addAttribute("title","很遗憾");
				model.addAttribute("tips","您的信用降级了");
				model.addAttribute("tips1","信用较差");
				model.addAttribute("tips2","您有逾期账单，尽早还款挽回信誉！");
				
			}else{
				model.addAttribute("className","blockBg");
				model.addAttribute("title","很遗憾");
				model.addAttribute("tips","您被列入无忧借条黑名单");
				model.addAttribute("tips1","信用较差");
				model.addAttribute("tips2","请及时呵护您的信用");
			}
					
		}else {
			//不能全部返还member的信息
			Member member2 = new Member();
			member2.setId(member.getId());
			member2.setMemberRank(member.getMemberRank());
			member2.setName(member.getName());
			member2.setHeadImage(member.getHeadImage());
			model.addAttribute("isWeiXin", isWeiXin);
			model.addAttribute("appPlatform", appPlatform);
			model.addAttribute("member", member2);
			if ("C".equals(member.getMemberRank().getRankNo())) {
				model.addAttribute("className","blockBg");
				model.addAttribute("title","很遗憾");
				model.addAttribute("tips","您被列入无忧借条黑名单");
				model.addAttribute("tips1","信用较差");
				model.addAttribute("tips2","请及时呵护您的信用");
			} else {
				if ("B".equals(member.getMemberRank().getRankNo())) {
					model.addAttribute("className","grayBg");
					model.addAttribute("title","很遗憾");
					model.addAttribute("tips","您的信用降级了");
					model.addAttribute("tips1","信用较差");
					model.addAttribute("tips2","您有逾期账单，尽早还款挽回信誉！");
				} else {
					model.addAttribute("className","xinyongdj");
					if("AAAAA".equals(member.getMemberRank().getRankNo())){
						model.addAttribute("title","太棒啦!");
						model.addAttribute("tips","您已达到信用巅峰");
						model.addAttribute("tips1","信用超级好");
						model.addAttribute("tips2","我击败了全国99.99%的用户");
					} else {
						model.addAttribute("tips","讲信用 才有真朋友");
					}
				}
			}
		}
		

		return "app/page/grade-meter-share";
	}
	/**
	 * 催收服务协议
	 */
	@RequestMapping(value="collectionAgreement")
	public ModelAndView collectionAgreement(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("app/page/agreement-collection");
		String appPlatform = request.getParameter("appPlatform");
		String isWeiXin = request.getParameter("isWeiXin");
		mv.addObject("isWeiXin", Boolean.valueOf(isWeiXin));
		mv.addObject("appPlatform", appPlatform);
		return mv;
	}


	/**
	 * 客服
	 * @param model
	 * @return
	 */
	@RequestMapping("/contact")
	public String contact(Model model, HttpServletRequest request) {
		String memberToken = request.getHeader("x-memberToken");
		Member member = new Member();
		if (!org.apache.commons.lang3.StringUtils.isBlank(memberToken)) {
			ResponseData responseData = JwtUtil.verifyToken(memberToken);
			if (responseData.getCode() == Constant.JWT_SUCCESS) {
				JSONObject payload = (JSONObject) JSON.toJSON(responseData.getResult());
				Long id = payload.getLong("id");
				member = memberService.get(id);
			}
		}
		model.addAttribute("member", member);
		return "app/page/service";
	}


	/**
	 * 在页面上掉线
	 * @param model
	 * @return
	 */
	@RequestMapping("/dropped")
	public String dropped(Model model, String loginDevice, String loginIp, String loginTime, String appPlatform) {
		model.addAttribute("loginDevice", loginDevice);
		model.addAttribute("loginIp", loginIp);
		model.addAttribute("loginTime", loginTime);
		model.addAttribute("appPlatform", appPlatform);
		return "app/page/securitySignTip";
	}
	

	/**
	 * 未登录提示
	 * @param model
	 * @return
	 */
	@RequestMapping("/loginTip")
	public String loginTip(Model model, String appPlatform) {
		model.addAttribute("appPlatform", appPlatform);
		return "app/page/loginTip";
	}
	/**
	 * 更新提示
	 * @param model
	 * @return
	 */
	@RequestMapping("/needToUpdate")
	public String needToUpdate(Model model, String appPlatform) {
		model.addAttribute("appPlatform", appPlatform);
		return "app/page/needToUpdate";
	}
}