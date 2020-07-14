package com.jxf.web.app.wyjt.member;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.ebaoquan.rop.thirdparty.org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jxf.loan.service.NfsLoanRecordService;
import com.jxf.mem.entity.Member;
import com.jxf.mem.service.MemberService;
import com.jxf.mem.utils.MemUtils;
import com.jxf.rc.entity.RcCaDataV2;
import com.jxf.rc.entity.RcCaYysDetails;
import com.jxf.rc.entity.RcXinyan;
import com.jxf.rc.service.RcCaDataServiceV2;
import com.jxf.rc.service.RcCaYysDetailsService;
import com.jxf.rc.service.RcXinyanService;
import com.jxf.svc.config.Global;
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.EncryptUtils;
import com.jxf.svc.utils.IdCardUtils;
import com.jxf.svc.utils.ObjectUtils;
import com.jxf.web.minipro.BaseController;
import com.jxf.web.model.ResponseData;
import com.jxf.web.model.wyjt.app.member.CaAuthResponseResult;
import com.jxf.web.model.wyjt.app.member.CaAuthResponseResult.UserBriefLegalize;


/**
 * Controller - 信用档案报告
 * 
 * @author wo
 */
@Controller("wyjtAppCaReportController")
@RequestMapping(value = "${wyjtApp}/ca")
public class MemberCaReportController extends BaseController {

	@Autowired
	private MemberService memberService;
	@Autowired
	private RcCaDataServiceV2 rcCaDataServiceV2;
	@Autowired
	private RcCaYysDetailsService rcCaYysDetailsService;
	@Autowired
	private NfsLoanRecordService loanRecordService;
	@Autowired
	private RcXinyanService rcXinyanService;
	
	/**
	 * 我的认证信用档案界面
	 */
	@RequestMapping(value = "/newCaAuth")
	public @ResponseBody ResponseData mycreditfile(HttpServletRequest request) {
		Member member = memberService.getCurrent();
		CaAuthResponseResult result = new CaAuthResponseResult();
		List<UserBriefLegalize> userBriefLegalize = new ArrayList<UserBriefLegalize>();
		int renZhengNum = rcCaDataServiceV2.getRenZhengNum(member); // 统计认证的数量
		// 信用报告
		UserBriefLegalize xy = new CaAuthResponseResult().new UserBriefLegalize();
		xy.setStatus(1);
		xy.setName("借贷记录");
		xy.setImage(Global.getConfig("domain")+Global.getConfig("caIcon.xy_1"));
		xy.setDate(DateUtils.formatDate(member.getCreateTime()));
		xy.setUrl("");
		xy.setJumpStatus("xinyong");
		xy.setAuthenType(0);
		userBriefLegalize.add(xy);
		for (RcCaDataV2.Type type : RcCaDataV2.Type.values()) {
			UserBriefLegalize renZhengData = new CaAuthResponseResult().new UserBriefLegalize();
			renZhengData = rcCaDataServiceV2.obtainRenZhengData(member, type);
			userBriefLegalize.add(renZhengData);
		}
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
		result.setIdNo(member.getIdNo());//天机sdk调用需要
		result.setCaReportUrl(Global.getConfig("domain")+"/gxt/report?key="+member.getSafeKeyValue());
		result.setShareReportUrl(Global.getConfig("domain")+"/gxt/report_share?key="+member.getSafeKeyValue());
		return ResponseData.success("操作成功", result);
    	
	}
	/**
	 * 信用报告
	 */
	@RequestMapping(value = "/caReport")
	public @ResponseBody ResponseData getCaReport(HttpServletRequest request) {
		String key = request.getParameter("key");
		Member owner = memberService.getBySafeKey(key);
		Member member = memberService.getCurrent2();// 会员信息

		Map<String, Object> data = new HashMap<String, Object>();
		// 判断是否是本人查看的
//		if (ownerId != member.getId()) {
//			MemberFriendRelation memberFriendRelation = friendRelationService.findByMemberIdAndFriendId(member.getId(), ownerId);
//			if (memberFriendRelation == null) {
//				return ResponseData.error("目前您还不是他的好友请您先加他为好友");
//			} else {
//				// 判断与好友是否存在借条关系
//				NfsLoanRecord findMyandFriendLoan = loanRecordService.findMyandFriendLoan(member, owner);
//				if (findMyandFriendLoan == null) {
//					// 判断是否申请过
//					if (memberFriendRelation.getFreeCaAuthStatus().ordinal() == 2) {
//						// 判断是否过期
//						Calendar cal = Calendar.getInstance();
//						cal.add(Calendar.HOUR, -2);
//						if (memberFriendRelation.getFreeCaAuthTime().getTime() < cal.getTimeInMillis()) {
//							// 变更过期状态
//							memberFriendRelation.setFreeCaAuthStatus(MemberFriendRelation.FreeCaAuthStatus.expired);
//							friendRelationService.update(memberFriendRelation);
//							return ResponseData.error("您查看好友的信用档案申请已过期，请您先去申请");
//						}
//
//					} else {
//						return ResponseData.error("您目前不能查看好友的信用档案，请您先去申请");
//					}
//				}
//			}
//		}
		Map<String, Object> baseInfoMap = new HashMap<String, Object>();
		String idNo = owner.getIdNo();
		int age = IdCardUtils.getAge(owner.getIdNo());
		String phoneNo = owner.getUsername();
		baseInfoMap.put("reportId", "YXB-" + owner.getId());
		baseInfoMap.put("name", owner.getName());
		baseInfoMap.put("phoneNo", MemUtils.mask(owner).getUsername());
		baseInfoMap.put("idNo", MemUtils.mask(owner).getIdNo());
		baseInfoMap.put("age", age);
		baseInfoMap.put("gender", owner.getGender().ordinal());
		baseInfoMap.put("headImage", owner.getHeadImage());
		baseInfoMap.put("addr", owner.getAddr());
		data.put("baseInfo", baseInfoMap);
		

		Map<String, Object> sumInfoMap = new HashMap<String, Object>();
		JSONObject authTime = new JSONObject();
		JSONObject idNoMatch = new JSONObject();
		JSONObject phoneNoMatch = new JSONObject();
	 
		//借条统计
		Map<String, Object> loanInfoMap = new HashMap<String, Object>();
		Map<String, Object> loanInfo7DMap= loanRecordService.getLoanInfo(owner, 7);
		Map<String, Object> loanInfo3MMap= loanRecordService.getLoanInfo(owner, 90);
		Map<String, Object> loanInfo6MMap= loanRecordService.getLoanInfo(owner, 180);
		loanInfoMap.put("loanInfo7DMap", loanInfo7DMap);
		loanInfoMap.put("loanInfo3MMap", loanInfo3MMap);
		loanInfoMap.put("loanInfo6MMap", loanInfo6MMap);
		data.put("loanInfo", loanInfoMap);
		
		//逾期记录
		Map<String, Object> overdueInfoMap= loanRecordService.getOverdueDetail(owner, 0, 0);
		data.put("overdueInfo", overdueInfoMap);
		
		// 芝麻分
		Map<String, Object> zmfMap = new HashMap<String, Object>();
		RcCaDataV2 caDataZmf = rcCaDataServiceV2.getSuccessRecord(phoneNo,RcCaDataV2.Type.zmf);
		if(caDataZmf!=null){
			JSONObject zmfObj = JSONObject.parseObject(caDataZmf.getContent());
			zmfMap.put("zmf", zmfObj.get("zmFen"));
			zmfMap.put("isMatch", zmfObj.get("status"));
			zmfMap.put("provider", caDataZmf.getProvider());
		}
		data.put("zmfInfo", zmfMap);
		

		
		//运营商
		Map<String, Object> yysMap = new HashMap<String, Object>();
		RcCaDataV2 caDataYys = rcCaDataServiceV2.getSuccessRecord(phoneNo,RcCaDataV2.Type.yys);
		if(caDataYys!=null){		
			yysMap=JSONObject.parseObject(caDataYys.getContent());
			JSONObject detail = new JSONObject();
			detail.put("callDetailUrl", Global.getConfig("domain")+"/app/wyjt/ca/yysCallDetail?ownerId="+owner.getId());
			detail.put("billDetailUrl", Global.getConfig("domain")+"/app/wyjt/ca/yysBillDetail?ownerId="+owner.getId());
			yysMap.put("detail", detail);
			yysMap.put("provider", caDataYys.getProvider());
			
			idNoMatch.put("yysMatch", ObjectUtils.isNotBlank(yysMap.get("id_no_match"))?yysMap.get("id_no_match"):"无数据");
			phoneNoMatch.put("yysMatch", ObjectUtils.isNotBlank(yysMap.get("phone_no_match"))?yysMap.get("phone_no_match"):"无数据");
		}
		data.put("yysInfo", yysMap);
		
		// 淘宝
		Map<String, Object> taobaoMap = new HashMap<String, Object>();
		RcCaDataV2 caDataTaobao = rcCaDataServiceV2.getSuccessRecord(phoneNo,RcCaDataV2.Type.taobao);
		if(caDataTaobao!=null){
			taobaoMap = JSONObject.parseObject(caDataTaobao.getContent());
			taobaoMap.put("provider", caDataTaobao.getProvider());
		}
		data.put("taobaoInfo", taobaoMap);

		
		// 网银
		Map<String, Object> wangYinMap = new HashMap<String, Object>();
		RcCaDataV2 caDataWangYin = rcCaDataServiceV2.getSuccessRecord(phoneNo,RcCaDataV2.Type.wangyin);
		if(caDataWangYin!=null){
			JSONObject wangYinObj = JSONObject.parseObject(caDataWangYin.getContent());
			JSONArray listjie_arr = wangYinObj.getJSONArray("listjie");
			for (int i = 0; i < listjie_arr.size(); i++) {
				JSONObject cardObj = listjie_arr.getJSONObject(i);
				cardObj.put("isMatch", StringUtils.equals(owner.getName(), cardObj.getString("name"))?1:0);
				cardObj.remove("xinMin");
				cardObj.remove("xinMax");
			}
			wangYinMap.put("debitCardList", listjie_arr);
			JSONArray listxin_arr = wangYinObj.getJSONArray("listxin");
			for (int j = 0; j < listxin_arr.size(); j++) {
				JSONObject cardObj = listjie_arr.getJSONObject(j);
				cardObj.put("isMatch", StringUtils.equals(owner.getName(), cardObj.getString("name"))?1:0);
			}
			wangYinMap.put("creditCardList", listxin_arr);
			wangYinMap.put("provider", caDataWangYin.getProvider());
		}
		data.put("wangYinInfo", wangYinMap);

	
		// 社保
		Map<String, Object> shebaoMap = new HashMap<String, Object>();
		RcCaDataV2 caDataShebao = rcCaDataServiceV2.getSuccessRecord(phoneNo,RcCaDataV2.Type.shebao);
		if(caDataShebao!=null){
			JSONObject shebaoObj = JSONObject.parseObject(caDataShebao.getContent()).getJSONObject("user_info");
			idNoMatch.put("shebaoMatch", ObjectUtils.isNotBlank(shebaoObj.get("certificate_number"))?StringUtils.equals(shebaoObj.get("certificate_number").toString(), idNo)?"匹配":"不匹配":"无数据");
			
			shebaoMap.put("beginPayDate", shebaoObj.get("begin_date"));
			shebaoMap.put("beginWorkDate", shebaoObj.get("time_to_work"));
			shebaoMap.put("companyName", shebaoObj.get("company_name"));	
			shebaoMap.put("homeAddress", shebaoObj.get("home_address"));
			shebaoMap.put("detailUrl", Global.getConfig("domain")+"/app/wyjt/ca/shebaoDetail?ownerId="+owner.getId());
			shebaoMap.put("provider", caDataShebao.getProvider());
		}
		data.put("shebaoInfo", shebaoMap);

		// 公积金
		Map<String, Object> gjjMap = new HashMap<String, Object>();
		RcCaDataV2 caDataGjj = rcCaDataServiceV2.getSuccessRecord(phoneNo,RcCaDataV2.Type.gjj);
		if(caDataGjj!=null){
			JSONObject gjjObj = JSONObject.parseObject(caDataGjj.getContent()).getJSONObject("base_info");
			idNoMatch.put("gjjMatch", ObjectUtils.isNotBlank(gjjObj.get("cert_no"))?StringUtils.equals(gjjObj.get("cert_no").toString(), idNo)?"匹配":"不匹配":"无数据");
			
			gjjMap.put("beginDate", gjjObj.get("begin_date"));
			gjjMap.put("lastPayDate", gjjObj.get("last_pay_date"));
			gjjMap.put("payStatus", gjjObj.get("pay_status_desc"));	
			gjjMap.put("companyName", gjjObj.get("corp_name"));
			gjjMap.put("homeAddress", gjjObj.get("home_address"));
			gjjMap.put("detailUrl", Global.getConfig("domain")+"/app/wyjt/ca/gjjDetail?ownerId="+owner.getId());
			gjjMap.put("provider", caDataGjj.getProvider());
		}
		data.put("gjjInfo", gjjMap);


		// 学信网
		Map<String, Object> xxwMap = new HashMap<String, Object>();
		RcCaDataV2 caDataXxw = rcCaDataServiceV2.getSuccessRecord(phoneNo,RcCaDataV2.Type.xxw);
		if(caDataXxw!=null){
			JSONArray xxwJSONArray = JSONObject.parseArray(caDataXxw.getContent());
			if(xxwJSONArray.size()>0) {
				JSONObject xxwObj = xxwJSONArray.getJSONObject(0);
				xxwMap.put("name", xxwObj.get("realname"));
				xxwMap.put("idNo", xxwObj.get("card_id"));
				xxwMap.put("school", xxwObj.get("school"));
				xxwMap.put("department", xxwObj.get("department"));
				xxwMap.put("eduLevel", xxwObj.get("edu_level"));	
				xxwMap.put("eduForm", xxwObj.get("edu_form"));
				xxwMap.put("entranceDate", xxwObj.get("entrance_date"));
				xxwMap.put("graduateDate", xxwObj.get("graduate_date"));
				xxwMap.put("provider", caDataXxw.getProvider());
			}
			idNoMatch.put("xxwMatch", ObjectUtils.isNotBlank(xxwMap.get("idNo"))?StringUtils.equals(xxwMap.get("idNo").toString(), idNo)?"匹配":"不匹配":"无数据");
		}
		data.put("xxwInfo", xxwMap);
		
		

		authTime.put("yysAuthTime", caDataYys!=null?caDataYys.getCreateTime():"无数据");
		authTime.put("zmfAuthTime", caDataZmf!=null?caDataZmf.getCreateTime():"无数据");
		authTime.put("taobaoAuthTime", caDataTaobao!=null?caDataTaobao.getCreateTime():"无数据");
		authTime.put("xxwAuthTime", caDataXxw!=null?caDataXxw.getCreateTime():"无数据");
		authTime.put("gjjAuthTime", caDataGjj!=null?caDataGjj.getCreateTime():"无数据");
		authTime.put("shebaoAuthTime", caDataShebao!=null?caDataShebao.getCreateTime():"无数据");
		authTime.put("wangYinAuthTime", caDataWangYin!=null?caDataWangYin.getCreateTime():"无数据");
		sumInfoMap.put("authTime", authTime);
		
		
		
		if(!idNoMatch.isEmpty()) {
			sumInfoMap.put("idNoMatch", idNoMatch);
		}
		if(!phoneNoMatch.isEmpty()) {
			sumInfoMap.put("phoneNoMatch", phoneNoMatch);
		}
		data.put("sumInfo", sumInfoMap);
		
		
		return ResponseData.success("操作成功", data);
	}
	
	/**
	 * 获取单个报告
	 * 
	 * @return type 
	 */
	@RequestMapping(value = "/singleCaReport")
	public @ResponseBody ResponseData getSingleCaReport(HttpServletRequest request) {
		String key = request.getParameter("key");
		String type = request.getParameter("type");
		Member owner = memberService.getBySafeKey(key);
		Member member = memberService.getCurrent2();
	
//		if (ownerId != member.getId()) {
//			MemberFriendRelation memberFriendRelation = friendRelationService.findByMemberIdAndFriendId(member.getId(), ownerId);
//			if (memberFriendRelation == null) {
//				return ResponseData.error("目前您还不是他的好友请您先加他为好友");
//			}
//		} 
		String phoneNo = owner.getUsername();
		Map<String, Object> data = new HashMap<String, Object>();
		
		switch(type){
		   case "xinyong":
			   	//借条统计
				Map<String, Object> loanInfoMap = new HashMap<String, Object>();
				Map<String, Object> loanInfo7DMap= loanRecordService.getLoanInfo(owner, 7);
				Map<String, Object> loanInfo3MMap= loanRecordService.getLoanInfo(owner, 90);
				Map<String, Object> loanInfo6MMap= loanRecordService.getLoanInfo(owner, 180);
				loanInfoMap.put("loanInfo7DMap", loanInfo7DMap);
				loanInfoMap.put("loanInfo3MMap", loanInfo3MMap);
				loanInfoMap.put("loanInfo6MMap", loanInfo6MMap);
				data.put("loanInfo", loanInfoMap);
				
				//逾期记录
				Map<String, Object> overdueInfoMap= loanRecordService.getOverdueDetail(owner, 0, 0);
				data.put("overdueInfo", overdueInfoMap);
			break;
		   case "zmf":
				Map<String, Object> zmfMap = new HashMap<String, Object>();
				RcCaDataV2 caDataZmf = rcCaDataServiceV2.getSuccessRecord(phoneNo,RcCaDataV2.Type.zmf);
				if(caDataZmf!=null){
					JSONObject zmfObj = JSONObject.parseObject(caDataZmf.getContent());
					zmfMap.put("zmf", zmfObj.get("zmFen"));
					zmfMap.put("isMatch", zmfObj.get("status"));
					zmfMap.put("provider", caDataZmf.getProvider());
				}
				data.put("zmfInfo", zmfMap);
			    break;
		   case "yys":
				Map<String, Object> yysMap = new HashMap<String, Object>();
				RcCaDataV2 caDataYys = rcCaDataServiceV2.getSuccessRecord(phoneNo,RcCaDataV2.Type.yys);
				if(caDataYys!=null){		
					yysMap=JSONObject.parseObject(caDataYys.getContent());
					JSONObject detail = new JSONObject();
					detail.put("callDetailUrl", Global.getConfig("domain")+"/app/wyjt/ca/yysCallDetail?ownerId="+owner.getId());
					detail.put("billDetailUrl", Global.getConfig("domain")+"/app/wyjt/ca/yysBillDetail?ownerId="+owner.getId());
					yysMap.put("detail", detail);
					yysMap.put("provider", caDataYys.getProvider());
				}
				data.put("yysInfo", yysMap);
			    break;
		   case "taobao":
				Map<String, Object> taobaoMap = new HashMap<String, Object>();
				RcCaDataV2 caDataTaobao = rcCaDataServiceV2.getSuccessRecord(phoneNo,RcCaDataV2.Type.taobao);
				if(caDataTaobao!=null){
					taobaoMap = JSONObject.parseObject(caDataTaobao.getContent());
					taobaoMap.put("provider", caDataTaobao.getProvider());
				}
				data.put("taobaoInfo", taobaoMap);
			    break;
		   case "xxw":
				Map<String, Object> xxwMap = new HashMap<String, Object>();
				RcCaDataV2 caDataXxw = rcCaDataServiceV2.getSuccessRecord(phoneNo,RcCaDataV2.Type.xxw);
				if(caDataXxw!=null){
					JSONArray xxwJSONArray = JSONObject.parseArray(caDataXxw.getContent());
					if(xxwJSONArray.size()>0) {
						JSONObject xxwObj = xxwJSONArray.getJSONObject(0);
						xxwMap.put("name", xxwObj.get("realname"));
						xxwMap.put("idNo", xxwObj.get("card_id"));
						xxwMap.put("school", xxwObj.get("school"));
						xxwMap.put("department", xxwObj.get("department"));
						xxwMap.put("eduLevel", xxwObj.get("edu_level"));	
						xxwMap.put("eduForm", xxwObj.get("edu_form"));
						xxwMap.put("entranceDate", xxwObj.get("entrance_date"));
						xxwMap.put("graduateDate", xxwObj.get("graduate_date"));
						xxwMap.put("provider", caDataXxw.getProvider());
					}
				}
				data.put("xxwInfo", xxwMap);
			    break;
		   case "shebao":
				Map<String, Object> shebaoMap = new HashMap<String, Object>();
				RcCaDataV2 caDataShebao = rcCaDataServiceV2.getSuccessRecord(phoneNo,RcCaDataV2.Type.shebao);
				if(caDataShebao!=null){
					JSONObject gjjObj = JSONObject.parseObject(caDataShebao.getContent()).getJSONObject("user_info");
					shebaoMap.put("beginPayDate", gjjObj.get("begin_date"));
					shebaoMap.put("beginWorkDate", gjjObj.get("time_to_work"));
					shebaoMap.put("companyName", gjjObj.get("company_name"));	
					shebaoMap.put("homeAddress", gjjObj.get("home_address"));
					shebaoMap.put("detailUrl", Global.getConfig("domain")+"/app/wyjt/ca/shebaoDetail?ownerId="+owner.getId());
					shebaoMap.put("provider", caDataShebao.getProvider());
				}
				data.put("shebaoInfo", shebaoMap);
			    break;
		   case "gjj":
				Map<String, Object> gjjMap = new HashMap<String, Object>();
				RcCaDataV2 caDataGjj = rcCaDataServiceV2.getSuccessRecord(phoneNo,RcCaDataV2.Type.gjj);
				if(caDataGjj!=null){
					JSONObject gjjObj = JSONObject.parseObject(caDataGjj.getContent()).getJSONObject("base_info");
					gjjMap.put("beginDate", gjjObj.get("begin_date"));
					gjjMap.put("lastPayDate", gjjObj.get("last_pay_date"));
					gjjMap.put("payStatus", gjjObj.get("pay_status_desc"));	
					gjjMap.put("companyName", gjjObj.get("corp_name"));
					gjjMap.put("homeAddress", gjjObj.get("home_address"));
					gjjMap.put("detailUrl", Global.getConfig("domain")+"/app/wyjt/ca/gjjDetail?ownerId="+owner.getId());
					gjjMap.put("provider", caDataGjj.getProvider());
				}
				data.put("gjjInfo", gjjMap);
			    break;
		   case "wangyin":
				Map<String, Object> wangYinMap = new HashMap<String, Object>();
				RcCaDataV2 caDataWangYin = rcCaDataServiceV2.getSuccessRecord(phoneNo,RcCaDataV2.Type.wangyin);
				if(caDataWangYin!=null){
					JSONObject wangYinObj = JSONObject.parseObject(caDataWangYin.getContent());
					JSONArray listjie_arr = wangYinObj.getJSONArray("listjie");
					for (int i = 0; i < listjie_arr.size(); i++) {
						JSONObject cardObj = listjie_arr.getJSONObject(i);
						cardObj.put("isMatch", StringUtils.equals(owner.getName(), cardObj.getString("name"))?1:0);
						cardObj.remove("xinMin");
						cardObj.remove("xinMax");
					}
					wangYinMap.put("debitCardList", listjie_arr);
					JSONArray listxin_arr = wangYinObj.getJSONArray("listxin");
					for (int j = 0; j < listxin_arr.size(); j++) {
						JSONObject cardObj = listjie_arr.getJSONObject(j);
						cardObj.put("isMatch", StringUtils.equals(owner.getName(), cardObj.getString("name"))?1:0);
					}
					wangYinMap.put("creditCardList", listxin_arr);
					wangYinMap.put("provider", caDataWangYin.getProvider());
				}
				data.put("wangYinInfo", wangYinMap);
			    break;
	       default : 
		        return ResponseData.error("type类型不存在");
		}
		
		return ResponseData.success("操作成功", data);

	}


	
	/**
	 * 公积金详情
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "gjjDetail")
	public @ResponseBody ResponseData gjjDetail(HttpServletRequest request, Model model) {
		int pageNo = Integer.parseInt(request.getParameter("pageNo"));
		Long ownerId = Long.valueOf(request.getParameter("ownerId"));
		Member owner = memberService.get(ownerId);
		 
		RcCaDataV2 caDataGjj = rcCaDataServiceV2.getSuccessRecord(owner.getUsername(),RcCaDataV2.Type.gjj);
		Map<String, Object> data = new HashMap<String, Object>();
		if(caDataGjj!=null){
			JSONObject gjjObj = JSONObject.parseObject(caDataGjj.getContent());
			data.put("bill_record", obtainPageList2(gjjObj.getJSONArray("bill_record"),pageNo));
		}	
		return ResponseData.success("操作成功", data);
	}

	/**
	 * 社保详情
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "shebaoDetail")
	public @ResponseBody ResponseData socialSecurityDetails(HttpServletRequest request, Model model) {
		int pageNo = Integer.parseInt(request.getParameter("pageNo"));
		Long ownerId = Long.valueOf(request.getParameter("ownerId"));
		Member owner = memberService.get(ownerId);

		RcCaDataV2 caDataShebao = rcCaDataServiceV2.getSuccessRecord(owner.getUsername(),RcCaDataV2.Type.shebao);
		Map<String, Object> data = new HashMap<String, Object>();
		if(caDataShebao != null) {
			JSONObject shebaoObj = JSONObject.parseObject(caDataShebao.getContent());
			data.put("endowment_insurance", obtainPageList2(shebaoObj.getJSONArray("endowment_insurance"),pageNo));//养老
			data.put("medical_insurance", obtainPageList2(shebaoObj.getJSONArray("medical_insurance"),pageNo));  //医疗
			data.put("unemployment_insurance", obtainPageList2(shebaoObj.getJSONArray("unemployment_insurance"),pageNo));//失业
			data.put("accident_insurance", obtainPageList2(shebaoObj.getJSONArray("accident_insurance"),pageNo));//工伤
			data.put("maternity_insurance", obtainPageList2(shebaoObj.getJSONArray("maternity_insurance"),pageNo));//生育
		}
		return ResponseData.success("操作成功", data);
	}

	/**
	 * 话费记录详情
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "yysBillDetail")
	public @ResponseBody ResponseData yysBillDetail(HttpServletRequest request, Model model) {

		Long ownerId = Long.valueOf(request.getParameter("ownerId"));
		int pageNo = Integer.parseInt(request.getParameter("pageNo"));
		Member owner = memberService.get(ownerId);
		List<Map<String, String>> billList = new ArrayList<Map<String, String>>();
		RcCaYysDetails yysDetails = new RcCaYysDetails();
		yysDetails.setMemberId(owner.getId());
		List<RcCaYysDetails> yysDetailsList = rcCaYysDetailsService.findList(yysDetails);
		RcCaYysDetails rcCaData = yysDetailsList.get(0);	
		JSONObject rcCaDataObj = JSON.parseObject(rcCaData.getContent());
		if(rcCaDataObj.containsKey("bill_info")) {
			JSONArray billInfoList = rcCaDataObj.getJSONArray("bill_info");  
			if(billInfoList!=null&&billInfoList.size()>0){
				 for(int i = 0 ;i<billInfoList.size();i++){ 
					 JSONObject billInfoObj = billInfoList.getJSONObject(i); 
					 Map<String, String> billMap = new HashMap<String, String>();
					 billMap.put("bill_cycle", billInfoObj.getString("bill_cycle"));
					 billMap.put("bill_total", billInfoObj.getString("bill_total"));
					 billList.add(billMap);
				 }
			}
		}
		List<Map<String, String>> dataList = obtainPageList(billList,pageNo);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("billList", dataList);
		return ResponseData.success("操作成功", data);
	}
	/**
	 * 通话记录详情
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "yysCallDetail")
	public @ResponseBody ResponseData yysCallDetail(HttpServletRequest request, Model model) {

		Long ownerId = Long.valueOf(request.getParameter("ownerId"));
		int pageNo = Integer.parseInt(request.getParameter("pageNo"));
		Member owner = memberService.get(ownerId);

        List<Map<String, String>> callList = new ArrayList<Map<String, String>>();
		RcCaYysDetails yysDetails = new RcCaYysDetails();
		yysDetails.setMemberId(owner.getId());
		List<RcCaYysDetails> yysDetailsList = rcCaYysDetailsService.findList(yysDetails);

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
		List<Map<String, String>> dataList = obtainPageList(callList,pageNo);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("callList", dataList);
		return ResponseData.success("操作成功", data);
	}
	private List<Map<String, String>> obtainPageList(List<Map<String, String>> list, int pageNo) {
		List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
		for(int j = 40*(pageNo-1) ;j< (40*pageNo > list.size()?list.size():40*pageNo);j++){  
			dataList.add(list.get(j));
		}
		return dataList;
	}
	
	private Object obtainPageList2(JSONArray jsonArray, int pageNo) {
		List<Object> list = new ArrayList<>(jsonArray);
		List<Object> dataList = new ArrayList<Object>();
		for(int j = 40*(pageNo-1) ;j< (40*pageNo > list.size()?list.size():40*pageNo);j++){  
			dataList.add(list.get(j));
		}
		return dataList;
	}

	/**
	 * 分享信用报告
	 */
	@RequestMapping(value = "/shareCaReport")
	public @ResponseBody ResponseData shareCaReport(HttpServletRequest request) {
		String key = request.getParameter("key");
		Member owner = memberService.getBySafeKey(key);

		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> baseInfoMap = new HashMap<String, Object>();
		String idNo = owner.getIdNo();
		int age = IdCardUtils.getAge(owner.getIdNo());
		String phoneNo = owner.getUsername();
		baseInfoMap.put("reportId", "YXB-" + owner.getId());
		baseInfoMap.put("name", owner.getName());
		baseInfoMap.put("phoneNo", MemUtils.mask(owner).getUsername());
		baseInfoMap.put("idNo", MemUtils.mask(owner).getIdNo());
		baseInfoMap.put("age", age);
		baseInfoMap.put("gender", owner.getGender().ordinal());
		baseInfoMap.put("headImage", owner.getHeadImage());
		baseInfoMap.put("addr", owner.getAddr());
		data.put("baseInfo", baseInfoMap);
		

	 
		//借条统计
		Map<String, Object> loanInfoMap = new HashMap<String, Object>();
		Map<String, Object> loanInfo7DMap= loanRecordService.getLoanInfo(owner, 7);
		Map<String, Object> loanInfo3MMap= loanRecordService.getLoanInfo(owner, 90);
		Map<String, Object> loanInfo6MMap= loanRecordService.getLoanInfo(owner, 180);
		loanInfoMap.put("loanInfo7DMap", loanInfo7DMap);
		loanInfoMap.put("loanInfo3MMap", loanInfo3MMap);
		loanInfoMap.put("loanInfo6MMap", loanInfo6MMap);
		data.put("loanInfo", loanInfoMap);
		
		//逾期记录
		Map<String, Object> overdueInfoMap= loanRecordService.getOverdueDetail(owner, 0, 0);
		data.put("overdueInfo", overdueInfoMap);
		
		// 芝麻分
		Map<String, Object> zmfMap = new HashMap<String, Object>();
		RcCaDataV2 caDataZmf = rcCaDataServiceV2.getSuccessRecord(phoneNo,RcCaDataV2.Type.zmf);
		if(caDataZmf!=null){
			JSONObject zmfObj = JSONObject.parseObject(caDataZmf.getContent());
			zmfMap.put("zmf", zmfObj.get("zmFen"));
			zmfMap.put("isMatch", zmfObj.get("status"));
			zmfMap.put("provider", caDataZmf.getProvider());
		}
		data.put("zmfInfo", zmfMap);
		

		
		//运营商
		Map<String, Object> yysMap = new HashMap<String, Object>();
		RcCaDataV2 caDataYys = rcCaDataServiceV2.getSuccessRecord(phoneNo,RcCaDataV2.Type.yys);
		if(caDataYys!=null){		
			yysMap=JSONObject.parseObject(caDataYys.getContent());
			JSONObject detail = new JSONObject();
			detail.put("callDetailUrl", Global.getConfig("domain")+"/app/wyjt/ca/yysShareCallDetail?key="+key);
			detail.put("billDetailUrl", Global.getConfig("domain")+"/app/wyjt/ca/yysShareBillDetail?key="+key);
			yysMap.put("detail", detail);
			yysMap.put("provider", caDataYys.getProvider());
		}
		data.put("yysInfo", yysMap);
		
		// 淘宝
		Map<String, Object> taobaoMap = new HashMap<String, Object>();
		RcCaDataV2 caDataTaobao = rcCaDataServiceV2.getSuccessRecord(phoneNo,RcCaDataV2.Type.taobao);
		if(caDataTaobao!=null){
			taobaoMap = JSONObject.parseObject(caDataTaobao.getContent());
			taobaoMap.put("provider", caDataTaobao.getProvider());
		}
		data.put("taobaoInfo", taobaoMap);

		
		// 网银
		Map<String, Object> wangYinMap = new HashMap<String, Object>();
		RcCaDataV2 caDataWangYin = rcCaDataServiceV2.getSuccessRecord(phoneNo,RcCaDataV2.Type.wangyin);
		if(caDataWangYin!=null){
			JSONObject wangYinObj = JSONObject.parseObject(caDataWangYin.getContent());
			JSONArray listjie_arr = wangYinObj.getJSONArray("listjie");
			for (int i = 0; i < listjie_arr.size(); i++) {
				JSONObject cardObj = listjie_arr.getJSONObject(i);
				cardObj.put("isMatch", StringUtils.equals(owner.getName(), cardObj.getString("name"))?1:0);
				cardObj.remove("xinMin");
				cardObj.remove("xinMax");
			}
			wangYinMap.put("debitCardList", listjie_arr);
			JSONArray listxin_arr = wangYinObj.getJSONArray("listxin");
			for (int j = 0; j < listxin_arr.size(); j++) {
				JSONObject cardObj = listjie_arr.getJSONObject(j);
				cardObj.put("isMatch", StringUtils.equals(owner.getName(), cardObj.getString("name"))?1:0);
			}
			wangYinMap.put("creditCardList", listxin_arr);
			wangYinMap.put("provider", caDataWangYin.getProvider());
		}
		data.put("wangYinInfo", wangYinMap);

	
		// 社保
		Map<String, Object> shebaoMap = new HashMap<String, Object>();
		RcCaDataV2 caDataShebao = rcCaDataServiceV2.getSuccessRecord(phoneNo,RcCaDataV2.Type.shebao);
		if(caDataShebao!=null){
			JSONObject gjjObj = JSONObject.parseObject(caDataShebao.getContent()).getJSONObject("user_info");
			shebaoMap.put("beginPayDate", gjjObj.get("begin_date"));
			shebaoMap.put("beginWorkDate", gjjObj.get("time_to_work"));
			shebaoMap.put("companyName", gjjObj.get("company_name"));	
			shebaoMap.put("homeAddress", gjjObj.get("home_address"));
			shebaoMap.put("detailUrl", Global.getConfig("domain")+"/app/wyjt/ca/shebaoShareDetail?key="+key);
			shebaoMap.put("provider", caDataShebao.getProvider());
		}
		data.put("shebaoInfo", shebaoMap);

		// 公积金
		Map<String, Object> gjjMap = new HashMap<String, Object>();
		RcCaDataV2 caDataGjj = rcCaDataServiceV2.getSuccessRecord(phoneNo,RcCaDataV2.Type.gjj);
		if(caDataGjj!=null){
			JSONObject gjjObj = JSONObject.parseObject(caDataGjj.getContent()).getJSONObject("base_info");
			gjjMap.put("beginDate", gjjObj.get("begin_date"));
			gjjMap.put("lastPayDate", gjjObj.get("last_pay_date"));
			gjjMap.put("payStatus", gjjObj.get("pay_status_desc"));	
			gjjMap.put("companyName", gjjObj.get("corp_name"));
			gjjMap.put("homeAddress", gjjObj.get("home_address"));
			gjjMap.put("detailUrl", Global.getConfig("domain")+"/app/wyjt/ca/gjjShareDetail?key="+key);
			gjjMap.put("provider", caDataGjj.getProvider());
		}
		data.put("gjjInfo", gjjMap);


		// 学信网
		Map<String, Object> xxwMap = new HashMap<String, Object>();
		RcCaDataV2 caDataXxw = rcCaDataServiceV2.getSuccessRecord(phoneNo,RcCaDataV2.Type.xxw);
		if(caDataXxw!=null){
			JSONArray xxwJSONArray = JSONObject.parseArray(caDataXxw.getContent());
			if(xxwJSONArray.size()>0) {
				JSONObject xxwObj = xxwJSONArray.getJSONObject(0);
				xxwMap.put("name", xxwObj.get("realname"));
				xxwMap.put("idNo", xxwObj.get("card_id"));
				xxwMap.put("school", xxwObj.get("school"));
				xxwMap.put("department", xxwObj.get("department"));
				xxwMap.put("eduLevel", xxwObj.get("edu_level"));	
				xxwMap.put("eduForm", xxwObj.get("edu_form"));
				xxwMap.put("entranceDate", xxwObj.get("entrance_date"));
				xxwMap.put("graduateDate", xxwObj.get("graduate_date"));
				xxwMap.put("provider", caDataXxw.getProvider());
			}
		}
		data.put("xxwInfo", xxwMap);
		
		
		Map<String, Object> sumInfoMap = new HashMap<String, Object>();
		JSONObject authTime = new JSONObject();
		authTime.put("yysAuthTime", caDataYys!=null?caDataYys.getCreateTime():"无数据");
		authTime.put("zmfAuthTime", caDataZmf!=null?caDataZmf.getCreateTime():"无数据");
		authTime.put("taobaoAuthTime", caDataTaobao!=null?caDataTaobao.getCreateTime():"无数据");
		authTime.put("xxwAuthTime", caDataXxw!=null?caDataXxw.getCreateTime():"无数据");
		authTime.put("gjjAuthTime", caDataGjj!=null?caDataGjj.getCreateTime():"无数据");
		authTime.put("shebaoAuthTime", caDataShebao!=null?caDataShebao.getCreateTime():"无数据");
		authTime.put("wangYinAuthTime", caDataWangYin!=null?caDataWangYin.getCreateTime():"无数据");
		sumInfoMap.put("authTime", authTime);
		
		JSONObject idNoMatch = new JSONObject();
		idNoMatch.put("yysMatch", ObjectUtils.isNotBlank(yysMap.get("id_no_match"))?yysMap.get("id_no_match"):"无数据");
		idNoMatch.put("xxwMatch", ObjectUtils.isNotBlank(xxwMap.get("idNo"))?StringUtils.equals(xxwMap.get("idNo").toString(), idNo)?"匹配":"不匹配":"无数据");
		sumInfoMap.put("idNoMatch", idNoMatch);
		
		JSONObject phoneNoMatch = new JSONObject();
		phoneNoMatch.put("yysMatch", ObjectUtils.isNotBlank(yysMap.get("phone_no_match"))?yysMap.get("phone_no_match"):"无数据");
		sumInfoMap.put("phoneNoMatch", phoneNoMatch);
		
		data.put("sumInfo", sumInfoMap);
		
		
		return ResponseData.success("操作成功", data);
	}
	/**
	 * 分享 公积金详情
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "gjjShareDetail")
	public @ResponseBody ResponseData gjjShareDetail(HttpServletRequest request, Model model) {
		int pageNo = Integer.parseInt(request.getParameter("pageNo"));
		String key = request.getParameter("key");
		Member owner = memberService.getBySafeKey(key);
		 
		RcCaDataV2 caDataGjj = rcCaDataServiceV2.getSuccessRecord(owner.getUsername(),RcCaDataV2.Type.gjj);
		Map<String, Object> data = new HashMap<String, Object>();
		if(caDataGjj!=null){
			JSONObject gjjObj = JSONObject.parseObject(caDataGjj.getContent());
			data.put("bill_record", obtainPageList2(gjjObj.getJSONArray("bill_record"),pageNo));
		}	
		return ResponseData.success("操作成功", data);
	}

	/**
	 * 分享 社保详情
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "shebaoShareDetail")
	public @ResponseBody ResponseData shebaoShareDetail(HttpServletRequest request, Model model) {
		int pageNo = Integer.parseInt(request.getParameter("pageNo"));
		String key = request.getParameter("key");
		Member owner = memberService.getBySafeKey(key);

		RcCaDataV2 caDataShebao = rcCaDataServiceV2.getSuccessRecord(owner.getUsername(),RcCaDataV2.Type.shebao);
		Map<String, Object> data = new HashMap<String, Object>();
		if(caDataShebao != null) {
			JSONObject shebaoObj = JSONObject.parseObject(caDataShebao.getContent());
			data.put("endowment_insurance", obtainPageList2(shebaoObj.getJSONArray("endowment_insurance"),pageNo));//养老
			data.put("medical_insurance", obtainPageList2(shebaoObj.getJSONArray("medical_insurance"),pageNo));  //医疗
			data.put("unemployment_insurance", obtainPageList2(shebaoObj.getJSONArray("unemployment_insurance"),pageNo));//失业
			data.put("accident_insurance", obtainPageList2(shebaoObj.getJSONArray("accident_insurance"),pageNo));//工伤
			data.put("maternity_insurance", obtainPageList2(shebaoObj.getJSONArray("maternity_insurance"),pageNo));//生育
		}
		return ResponseData.success("操作成功", data);
	}
	/**
	 * 分享 话费记录详情
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "yysShareBillDetail")
	public @ResponseBody ResponseData yysShareBillDetail(HttpServletRequest request, Model model) {
		int pageNo = Integer.parseInt(request.getParameter("pageNo"));
		String key = request.getParameter("key");
		Member owner = memberService.getBySafeKey(key);
		
		List<Map<String, String>> billList = new ArrayList<Map<String, String>>();
		RcCaYysDetails yysDetails = new RcCaYysDetails();
		yysDetails.setMemberId(owner.getId());
		List<RcCaYysDetails> yysDetailsList = rcCaYysDetailsService.findList(yysDetails);
		RcCaYysDetails rcCaData = yysDetailsList.get(0);	
		JSONObject rcCaDataObj = JSON.parseObject(rcCaData.getContent());
		if(rcCaDataObj.containsKey("bill_info")) {
			JSONArray billInfoList = rcCaDataObj.getJSONArray("bill_info");  
			if(billInfoList!=null&&billInfoList.size()>0){
				 for(int i = 0 ;i<billInfoList.size();i++){ 
					 JSONObject billInfoObj = billInfoList.getJSONObject(i); 
					 Map<String, String> billMap = new HashMap<String, String>();
					 billMap.put("bill_cycle", billInfoObj.getString("bill_cycle"));
					 billMap.put("bill_total", billInfoObj.getString("bill_total"));
					 billList.add(billMap);
				 }
			}
		}
		List<Map<String, String>> dataList = obtainPageList(billList,pageNo);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("billList", dataList);
		return ResponseData.success("操作成功", data);
	}
	/**
	 * 分享 通话记录详情
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "yysShareCallDetail")
	public @ResponseBody ResponseData yysShareCallDetail(HttpServletRequest request, Model model) {
		int pageNo = Integer.parseInt(request.getParameter("pageNo"));
		String key = request.getParameter("key");
		Member owner = memberService.getBySafeKey(key);

        List<Map<String, String>> callList = new ArrayList<Map<String, String>>();
		RcCaYysDetails yysDetails = new RcCaYysDetails();
		yysDetails.setMemberId(owner.getId());
		List<RcCaYysDetails> yysDetailsList = rcCaYysDetailsService.findList(yysDetails);

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
		List<Map<String, String>> dataList = obtainPageList(callList,pageNo);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("callList", dataList);
		return ResponseData.success("操作成功", data);
	}
}