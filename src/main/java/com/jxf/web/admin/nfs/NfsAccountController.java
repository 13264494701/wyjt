package com.jxf.web.admin.nfs;


import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberCard;
import com.jxf.nfs.entity.MerchantAccountDetail;
import com.jxf.nfs.entity.NfsFundAddReduce;
import com.jxf.nfs.entity.NfsRchgRecord;
import com.jxf.nfs.entity.NfsWdrlRecord;
import com.jxf.nfs.service.NfsFundAddReduceService;
import com.jxf.nfs.service.NfsRchgRecordService;
import com.jxf.nfs.service.NfsWdrlRecordService;
import com.jxf.pay.utils.HttpPoster;
import com.jxf.pwithdraw.entity.QueryPaymentRequestBean;
import com.jxf.pwithdraw.entity.QueryPaymentResponseBean;
import com.jxf.pwithdraw.service.FuiouWithdrawService;
import com.jxf.pwithdraw.service.LianlianPayService;
import com.jxf.pwithdraw.utils.SignUtil;
import com.jxf.svc.config.Global;
import com.jxf.svc.excel.ExportExcel;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.plugin.PluginConfig;
import com.jxf.svc.plugin.fuyouPayment.FyPaymentPlugin;
import com.jxf.svc.plugin.lianlianPayment.LianlianPaymentPlugin;
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.EncryptUtils;

import com.jxf.svc.utils.StringUtils;
import com.jxf.svc.utils.XMLParser;
import com.jxf.ufang.entity.UfangFundAddReduce;
import com.jxf.ufang.entity.UfangRchgRecord;
import com.jxf.ufang.service.UfangFundAddReduceService;
import com.jxf.ufang.service.UfangRchgRecordService;
import com.jxf.svc.utils.EncryptUtils.Type;
import com.jxf.svc.utils.Exceptions;
import com.jxf.svc.utils.HttpUtils;
import com.jxf.svc.utils.MD5Code;
import com.jxf.web.admin.sys.BaseController;

/**
 * 财务管理Controller
 * @author wo
 * @version 2018-12-20
 */
@Controller
@RequestMapping(value = "${adminPath}/account")
public class NfsAccountController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(NfsAccountController.class);
	
	@Autowired
	private NfsRchgRecordService rchgRecordService;
	@Autowired
	private NfsWdrlRecordService wdrlRecordService;
	@Autowired
	private NfsFundAddReduceService fundAddReduceService;
	@Autowired
	private UfangFundAddReduceService ufangFundAddReduceService;
    @Autowired
    private UfangRchgRecordService ufangRchgRecordService;
	@Autowired
	private LianlianPayService lianlianPayService;
	@Autowired
	private LianlianPaymentPlugin lianlianPaymentPlugin;
	@Autowired
	private FyPaymentPlugin fyPaymentPlugin;
	@Autowired
	private FuiouWithdrawService fuiouWithdrawService;
	
	@ModelAttribute
	public NfsWdrlRecord get(@RequestParam(required=false) Long id) {
		NfsWdrlRecord entity = null;
		if (id!=null){
			entity = wdrlRecordService.get(id);
		}
		if (entity == null){
			entity = new NfsWdrlRecord();
		}
		return entity;
	}
	
	/***
	 * 充值列表
	 * @param rchgRecord
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("account:view")
	@RequestMapping(value = "rchgList")
	public String rchgList(NfsRchgRecord rchgRecord, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<NfsRchgRecord> page = rchgRecordService.findPage(new Page<NfsRchgRecord>(request, response), rchgRecord); 
		for (NfsRchgRecord rchg : page.getList()) {
			MemberCard card = rchg.getCard();
			String cardNo = card.getCardNo();
			card.setCardNo(EncryptUtils.encryptString(cardNo, Type.CARD));
			rchg.setCard(card);
		}
		model.addAttribute("page", page);
		model.addAttribute("rchgRecord", rchgRecord);
		return "admin/nfs/account/rchgRecordList";
	}
	

	/***
	 *  提现列表
	 * @param wdrlRecord
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("account:view")
	@RequestMapping(value = "wdrlList")
	public String wdrlList(NfsWdrlRecord wdrlRecord, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<NfsWdrlRecord> page = wdrlRecordService.findPage(new Page<NfsWdrlRecord>(request, response), wdrlRecord); 
		List<NfsWdrlRecord> wdrlRecords = page.getList();
		for (NfsWdrlRecord wdrlRecord2 : wdrlRecords) {
			Member member = wdrlRecord2.getMember();
			String username = EncryptUtils.encryptString(member.getUsername(), EncryptUtils.Type.PHONE);
			member.setUsername(username);
			wdrlRecord2.setMember(member);
			wdrlRecord2.setCardNo(StringUtils.replacePattern(wdrlRecord2.getCardNo(), "(?<=[\\d]{4})\\d(?=[\\d]{4})", "*"));
		}
		model.addAttribute("page", page);
		model.addAttribute("wdrlRecord", wdrlRecord);
		return "admin/nfs/account/wdrlRecordList";
	}

	/***
	 *  优放充值
	 *  
	 * @param rchgRecord
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("account:view")
	@RequestMapping(value = "ufangRchgList")
	public String ufangRchgList(UfangRchgRecord rchgRecord, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<UfangRchgRecord> page = ufangRchgRecordService.findPage(new Page<UfangRchgRecord>(request, response), rchgRecord); 

		model.addAttribute("page", page);
		model.addAttribute("rchgRecord", rchgRecord);
		return "admin/nfs/account/ufangRchgRecordList";
	}

	@RequiresPermissions("account:view")
	@RequestMapping(value = "fundAddReduceList")
	public String list(NfsFundAddReduce nfsFundAddReduce, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<NfsFundAddReduce> page = fundAddReduceService.findPage(new Page<NfsFundAddReduce>(request, response), nfsFundAddReduce); 
		model.addAttribute("page", page);
		model.addAttribute("nfsFundAddReduce", nfsFundAddReduce);
		return "admin/nfs/account/fundAddReduceList";
	}
	
	@RequiresPermissions("account:view")
	@RequestMapping(value = "ufangFundAddReduce")
	public String ufangFundAddReduce(UfangFundAddReduce ufangFundAddReduce, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<UfangFundAddReduce> page = ufangFundAddReduceService.findPage(new Page<UfangFundAddReduce>(request, response), ufangFundAddReduce); 
		model.addAttribute("page", page);
		model.addAttribute("ufangFundAddReduce", ufangFundAddReduce);
		return "admin/nfs/account/ufangFundAddReduceList";
	}
	
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("account:view")
	@RequestMapping(value = "query")
	public String query(NfsWdrlRecord wdrlRecord, Model model) {
		model.addAttribute("wdrlRecord", wdrlRecord);
		return "admin/nfs/account/wdrlRecordQuery";
	}
	
	
	/**
	 * 查询订单
	 */
	@RequiresPermissions("account:view")
	@RequestMapping(value = "queryThirdOrder")
	public String queryThirdOrder(HttpServletRequest request,Model model) {
		String id = request.getParameter("id");
		NfsWdrlRecord wdrlRecord = wdrlRecordService.get(Long.valueOf(id));
		QueryPaymentRequestBean queryRequestBean = new QueryPaymentRequestBean();
		queryRequestBean.setNo_order(wdrlRecord.getId()+ "" );
		String result = lianlianPayService.queryOrder(queryRequestBean);
		QueryPaymentResponseBean queryPaymentResponseBean = JSONObject.parseObject(result, QueryPaymentResponseBean.class);
		if(StringUtils.equals(queryPaymentResponseBean.getRet_code(), "8901")){
			model.addAttribute("noRecord", "1");
		}else {
			model.addAttribute("noRecord", "0");
		}
		model.addAttribute(queryPaymentResponseBean);
		return "admin/nfs/wdrl/queryThirdOrder";
	}
	
	
	/**
	 * 导出充值数据
	 * @param rchgRecord
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("account:view")
    @RequestMapping(value = "exportRchgData", method=RequestMethod.POST)
    public String exportRchgFile(NfsRchgRecord rchgRecord, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            if(rchgRecord.getBeginTime()==null||rchgRecord.getEndTime()==null) {
				addMessage(redirectAttributes, "起始时间和结束时间不能为空");
				return "redirect:"+Global.getAdminPath()+"/account/rchgList?repage";
            }
			int days = DateUtils.getDifferenceOfTwoDate(rchgRecord.getBeginTime(),rchgRecord.getEndTime());
			if(Math.abs(days)>30){
				addMessage(redirectAttributes, "导出数据的时间跨度不能超过30天");
				return "redirect:"+Global.getAdminPath()+"/account/rchgList?repage";
			}
            List<NfsRchgRecord> rchgRecordList = rchgRecordService.findList(rchgRecord);
            
            String fileName = "充值数据"+DateUtils.getDate("yyyy-MM-dd")+".xlsx";
            ExportExcel exprotExcel = new ExportExcel("充值数据", NfsRchgRecord.class);
            exprotExcel.setDataList(rchgRecordList);
            exprotExcel.write(response, fileName);
            exprotExcel.dispose();
            return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出充值数据失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/account/rchgList?repage";
    }
	
	/**
	 * 导出提现数据
	 * @param wdrlRecord
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("account:view")
    @RequestMapping(value = "exportWdrlData", method=RequestMethod.POST)
    public String exportWdrlFile(NfsWdrlRecord wdrlRecord, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            if(wdrlRecord.getBeginTime()==null||wdrlRecord.getEndTime()==null) {
				addMessage(redirectAttributes, "起始时间和结束时间不能为空");
				return "redirect:"+Global.getAdminPath()+"/account/wdrlList?repage";	
            }
			int days = DateUtils.getDifferenceOfTwoDate(wdrlRecord.getBeginTime(),wdrlRecord.getEndTime());
			if(Math.abs(days)>30){
				addMessage(redirectAttributes, "导出数据的时间跨度不能超过30天");
				return "redirect:"+Global.getAdminPath()+"/account/wdrlList?repage";
			}
            List<NfsWdrlRecord> wdrlRecordList = wdrlRecordService.findList(wdrlRecord);
            
            String fileName = "提现数据"+DateUtils.getDate("yyyy-MM-dd")+".xlsx";
            ExportExcel exprotExcel = new ExportExcel("提现数据", NfsWdrlRecord.class);
            exprotExcel.setDataList(wdrlRecordList);
            exprotExcel.write(response, fileName);
            exprotExcel.dispose();
            return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出提现失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/account/wdrlList?repage";
    }
	
	
	/**
	 * 导出优放充值数据
	 * @param rchgRecord
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("account:view")
    @RequestMapping(value = "exportUfangRchgData", method=RequestMethod.POST)
    public String exportUfangRchgFile(UfangRchgRecord rchgRecord, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            if(rchgRecord.getBeginTime()==null||rchgRecord.getEndTime()==null) {
				addMessage(redirectAttributes, "起始时间和结束时间不能为空");
				return "redirect:"+Global.getAdminPath()+"/account/ufangRchgList?repage";
            }
			int days = DateUtils.getDifferenceOfTwoDate(rchgRecord.getBeginTime(),rchgRecord.getEndTime());
			if(Math.abs(days)>30){
				addMessage(redirectAttributes, "导出数据的时间跨度不能超过30天");
				return "redirect:"+Global.getAdminPath()+"/account/ufangRchgList?repage";
			}
            List<UfangRchgRecord> rchgRecordList = ufangRchgRecordService.findList(rchgRecord);
            
            String fileName = "优放充值数据"+DateUtils.getDate("yyyy-MM-dd")+".xlsx";
            ExportExcel exprotExcel = new ExportExcel("优放充值数据", UfangRchgRecord.class);
            exprotExcel.setDataList(rchgRecordList);
            exprotExcel.write(response, fileName);
            exprotExcel.dispose();
            return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出充值数据失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/account/ufangRchgList?repage";
    }
	/**
	 * 查询第三方渠道商户账户余额详情
	 * @return
	 */
	@RequiresPermissions("account:view")
    @RequestMapping(value = "queryThirdAct")
	public String queryMerchantAccountDetail(HttpServletRequest request,Model model) {
		MerchantAccountDetail merchantAccountDetail = new MerchantAccountDetail();
		//富友账户详情
		PluginConfig config = fyPaymentPlugin.getPluginConfig();
		Map<String,String> configAttr = config.getAttributeMap();
		String MCHNTCD=	configAttr.get(FyPaymentPlugin.PAYMENT_MCHNTCD_ATTRIBUTE_NAME);
		String mchntkey = configAttr.get(FyPaymentPlugin.PAYMENT_MCTKEY_REQUEST_URL_ATTRIBUTE_NAME);
		String md5Source ="1.0|"+ MCHNTCD+"|"+ mchntkey;
		String dataStr = "";
		try {
		   dataStr = MD5Code.MD5Encode(md5Source, "UTF-8");
			 String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><qryacnt><ver>1.0</ver><merid>" + MCHNTCD + "</merid><mac>" + dataStr + "</mac></qryacnt>";
			 String url = configAttr.get(FyPaymentPlugin.PAYMENT_QUERYTRADERACCOUNT_REQUEST_URL_ATTRIBUTE_NAME);
			 Map<String, String> map = new HashMap<String,String>();
			 map.put("xml", xml);
			 String outStr = new HttpPoster(url).postStr(map);
			 logger.info("查询富友账户返回信息[{}]",outStr);
			 if(StringUtils.isEmpty(outStr)){  
		          logger.error("xml为空：");  
		     } 
			Map<String, String> result = new HashMap<String, String>();
		 
			result = XMLParser.readStringXmlOut(outStr);
		    String ret = result.get("ret");
		    String memo = result.get("memo");
		    String ctamtStr = result.get("ctamt");
		    String caamtStr = result.get("caamt");
		    String cuamtStr = result.get("cuamt");
		    String cfamtStr = result.get("cfamt");
		    String mac = result.get("mac");
			
		    String md5SourceRe = MCHNTCD+"|"+ret+"|"+memo+"|"+ctamtStr+"|"+caamtStr+"|"+cuamtStr+"|"+cfamtStr+"|"+mchntkey;
		    String encode = MD5Code.MD5Encode(md5SourceRe, "UTF-8");
		    if(!StringUtils.equals(encode, mac)){
		      logger.error("查询富友账户详情，校验签名失败");
		      model.addAttribute("fyfailed", true);
		      return "admin/nfs/account/queryThirdAct";
		    }
		    BigDecimal caamt = new BigDecimal(caamtStr).divide(new BigDecimal(100));
		    BigDecimal ctamt = new BigDecimal(ctamtStr).divide(new BigDecimal(100));
		    BigDecimal cuamt = new BigDecimal(cuamtStr).divide(new BigDecimal(100));
		    BigDecimal cfamt = new BigDecimal(cfamtStr).divide(new BigDecimal(100));
		    merchantAccountDetail.setCaamt(caamt);
		    merchantAccountDetail.setCfamt(cfamt);
		    merchantAccountDetail.setCtamt(ctamt);
		    merchantAccountDetail.setCuamt(cuamt);
		    
		    Map<String, String> queryResultMap = fuiouWithdrawService.queryAccount();
		    if(queryResultMap == null) {
		    	logger.error("查询富友代付账户详情，校验签名失败");
		    	model.addAttribute("fytxfailed", true);
		    }else {
		    	BigDecimal txctamt = new BigDecimal(queryResultMap.get("ctamt")).divide(new BigDecimal(100));
		    	BigDecimal txcaamt = new BigDecimal(queryResultMap.get("caamt")).divide(new BigDecimal(100));
			    BigDecimal txcuamt = new BigDecimal(queryResultMap.get("cuamt")).divide(new BigDecimal(100));
			    BigDecimal txcfamt = new BigDecimal(queryResultMap.get("cfamt")).divide(new BigDecimal(100));
			    merchantAccountDetail.setTxctamt(txctamt);
			    merchantAccountDetail.setTxcaamt(txcaamt);
			    merchantAccountDetail.setTxcuamt(txcuamt);
			    merchantAccountDetail.setTxcfamt(txcfamt);
		    }
		    
		    //连连账户详情
		    QueryPaymentRequestBean queryRequestBean = new QueryPaymentRequestBean();
			PluginConfig lianlianConfig = lianlianPaymentPlugin.getPluginConfig();
			Map<String,String> lianlianConfigAttr = lianlianConfig.getAttributeMap();
			String oid_partner = lianlianConfigAttr.get(LianlianPaymentPlugin.PAYMENT_OID_PARTNER_ATTRIBUTE_NAME);
			String version = lianlianConfigAttr.get(LianlianPaymentPlugin.PAYMENT_API_VERSION_ATTRIBUTE_NAME);
			String signType = lianlianConfigAttr.get(LianlianPaymentPlugin.PAYMENT_SIGN_TYPE_ATTRIBUTE_NAME);
			String key = lianlianConfigAttr.get(LianlianPaymentPlugin.PAYMENT_BUSINESS_PRIVATE_KEY_ATTRIBUTE_NAME);
			String queryUrl = lianlianConfigAttr.get(LianlianPaymentPlugin.PAYMENT_QUERYTRADERACCOUNT_ATTRIBUTE_NAME);
			
			queryRequestBean.setOid_partner(oid_partner);
			queryRequestBean.setApi_version(version);
			queryRequestBean.setSign_type(signType);
			queryRequestBean.setSign(SignUtil.genRSASign(JSONObject.parseObject(JSON.toJSONString(queryRequestBean)),key));
			JSONObject json = JSON.parseObject(JSON.toJSONString(queryRequestBean));
			String queryResult = HttpUtils.doPost(queryUrl, json.toJSONString());
			logger.info("查询连连账户返回信息[{}]",queryResult);
			@SuppressWarnings("unchecked")
			Map<String,String> map_1  = (Map<String, String>) JSON.parse(queryResult);
			String lianlianBalance = map_1.get("amt_balance");
			if(StringUtils.isBlank(lianlianBalance) || StringUtils.equals(lianlianBalance, "null")) {
				 model.addAttribute("llfailed", true);
				 model.addAttribute("merchantAccountDetail", merchantAccountDetail);
				 return "admin/nfs/account/queryThirdAct";
			}
			merchantAccountDetail.setLianlianBalance(new BigDecimal(lianlianBalance));
			model.addAttribute("merchantAccountDetail", merchantAccountDetail);
		  } catch (Exception e) {
			  model.addAttribute("failed", true);
		    logger.error(Exceptions.getStackTraceAsString(e));
		  }
			
		return "admin/nfs/account/queryThirdAct";
	}
}