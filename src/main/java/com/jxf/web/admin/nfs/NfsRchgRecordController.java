package com.jxf.web.admin.nfs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberCard;
import com.jxf.mem.service.MemberService;
import com.jxf.nfs.entity.NfsRchgRecord;
import com.jxf.nfs.entity.NfsRchgRecord.Status;
import com.jxf.nfs.service.NfsRchgRecordService;
import com.jxf.pay.service.FuyouPayService;
import com.jxf.payment.entity.Payment;
import com.jxf.payment.service.PaymentService;
import com.jxf.pwithdraw.service.LianlianPayService;
import com.jxf.svc.config.Constant;
import com.jxf.svc.config.Global;
import com.jxf.svc.model.AjaxRsp;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.plugin.PluginConfig;
import com.jxf.svc.plugin.fuyouPayment.FyPaymentPlugin;
import com.jxf.svc.utils.EncryptUtils;
import com.jxf.svc.utils.StringUtils;
import com.jxf.svc.utils.EncryptUtils.Type;
import com.jxf.svc.utils.Exceptions;
import com.jxf.svc.utils.MD5Code;
import com.jxf.web.admin.sys.BaseController;

/**
 *       充值记录Controller
 * @author suhuimin
 * @version 2018-10-10
 */
@Controller
@RequestMapping(value = "${adminPath}/rchgRecord")
public class NfsRchgRecordController extends BaseController {

	@Autowired
	private NfsRchgRecordService rchgRecordService;
	@Autowired
	private FuyouPayService fuyouPayService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private PaymentService paymentService;
	@Autowired
	private FyPaymentPlugin fyPaymentPlugin;
	@Autowired
	private LianlianPayService	lianlianPayService;
	
	@ModelAttribute
	public NfsRchgRecord get(@RequestParam(required=false) Long id) {
		NfsRchgRecord entity = null;
		if (id!=null){
			entity = rchgRecordService.get(id);
		}
		if (entity == null){
			entity = new NfsRchgRecord();
		}
		return entity;
	}
	
	@RequiresPermissions("recharge:nfsRchgRecord:view")
	@RequestMapping(value = {"list", ""})
	public String list(NfsRchgRecord nfsRchgRecord, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<NfsRchgRecord> page = rchgRecordService.findPage(new Page<NfsRchgRecord>(request, response), nfsRchgRecord); 
		List<NfsRchgRecord> rchgRecords = page.getList();
		for (NfsRchgRecord nfsRchgRecord2 : rchgRecords) {
			MemberCard card = nfsRchgRecord2.getCard();
			String cardNo = card.getCardNo();
			card.setCardNo(EncryptUtils.encryptString(cardNo, Type.CARD));
			nfsRchgRecord2.setCard(card);
		}
//		for (NfsWdrlRecord wdrlRecord2 : wdrlRecords) {
//			Member member = wdrlRecord2.getMember();
//			String username = EncryptUtils.encryptString(member.getUsername(), EncryptUtils.Type.PHONE);
//			member.setUsername(username);
//			wdrlRecord2.setMember(member);
//			wdrlRecord2.setCardNo(StringUtils.replacePattern(wdrlRecord2.getCardNo(), "(?<=[\\d]{4})\\d(?=[\\d]{4})", "*"));
//		}
		
		model.addAttribute("page", page);
		return "admin/nfs/recharge/nfsRchgRecordList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("recharge:nfsRchgRecord:view")
	@RequestMapping(value = "add")
	public String add(NfsRchgRecord nfsRchgRecord, Model model) {
		model.addAttribute("nfsRchgRecord", nfsRchgRecord);
		return "admin/nfs/recharge/nfsRchgRecordAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("recharge:nfsRchgRecord:view")
	@RequestMapping(value = "query")
	public String query(NfsRchgRecord nfsRchgRecord, Model model) {
		model.addAttribute("nfsRchgRecord", nfsRchgRecord);
		return "admin/nfs/recharge/nfsRchgRecordQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("recharge:nfsRchgRecord:view")
	@RequestMapping(value = "update")
	public String update(NfsRchgRecord nfsRchgRecord, Model model) {
		model.addAttribute("nfsRchgRecord", nfsRchgRecord);
		return "admin/nfs/recharge/nfsRchgRecordUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("recharge:nfsRchgRecord:edit")
	@RequestMapping(value = "save")
	public String save(NfsRchgRecord nfsRchgRecord, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, nfsRchgRecord)){
			return add(nfsRchgRecord, model);
		}
		rchgRecordService.save(nfsRchgRecord);
		addMessage(redirectAttributes, "保存充值记录成功");
		return "redirect:"+Global.getAdminPath()+"/rchgRecord/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("recharge:nfsRchgRecord:edit")
	@RequestMapping(value = "delete")
	public String delete(NfsRchgRecord nfsRchgRecord, RedirectAttributes redirectAttributes) {
		rchgRecordService.delete(nfsRchgRecord);
		addMessage(redirectAttributes, "删除充值记录成功");
		return "redirect:"+Global.getAdminPath()+"/rchgRecord/?repage";
	}
	
	
	/**
	 * 确认充值成功
	 */
	@RequiresPermissions("recharge:nfsRchgRecord:edit")
	@RequestMapping(value = "successConfirm")
	public String successConfirm(NfsRchgRecord nfsRchgRecord, RedirectAttributes redirectAttributes) {
		NfsRchgRecord rchgRecord = rchgRecordService.get(nfsRchgRecord);
		Payment payment = paymentService.getByPaymentNo(rchgRecord.getPaymentNo());
		if(payment.getStatus().equals(Payment.Status.success)) {
			addMessage(redirectAttributes, "充值记录状态已更新，请勿重复操作！");
			return "redirect:"+Global.getAdminPath()+"/rchgRecord/?repage";
		}
		if(rchgRecord.getStatus().equals(NfsRchgRecord.Status.success)) {
			addMessage(redirectAttributes, "充值记录状态已更新，请勿重复操作！");
			return "redirect:"+Global.getAdminPath()+"/rchgRecord/?repage";
		}
		String orderNo = rchgRecord.getPayment().getPaymentNo();
		Map<String, String> queryResultMap = fuyouPayService.queryPayResultByMchOrderNo(orderNo);
		String respcode = queryResultMap.get("success");
		if(StringUtils.equals(respcode, "0")) {
			rchgRecord.setStatus(Status.success);
			Payment payment2 = paymentService.getByPaymentNo(payment.getPaymentNo());
			payment2.setStatus(Payment.Status.success);
			paymentService.save(payment2);
			rchgRecord.setPayment(payment);
			Member member = memberService.get(rchgRecord.getMember());
			try {
				int code = rchgRecordService.confirmSuccess(rchgRecord,member);
				if(code == Constant.UPDATE_SUCCESS) {
					addMessage(redirectAttributes, "状态更新成功！");
					return "redirect:"+Global.getAdminPath()+"/rchgRecord/?repage";
				}else {
					addMessage(redirectAttributes, "状态更新失败！");
					return "redirect:"+Global.getAdminPath()+"/rchgRecord/?repage";
				}
			} catch (Exception e) {
				logger.error("会员[{}]充值记录id[{}]账户更新异常，异常信息：{}",member.getId(),rchgRecord.getId(),Exceptions.getStackTraceAsString(e));
				addMessage(redirectAttributes, "会员账户更新异常，请联系系统管理员！");
				return "redirect:"+Global.getAdminPath()+"/rchgRecord/?repage";
			}
		}else {
			addMessage(redirectAttributes, "查询反馈充值失败!");
			return "redirect:"+Global.getAdminPath()+"/rchgRecord/?repage";
		}
	}
	
	/**
	 * 查询订单
	 */
	@RequiresPermissions("recharge:nfsRchgRecord:view")
	@RequestMapping(value = "queryOrder")
	public String queryOrder(NfsRchgRecord nfsRchgRecord,Model model) {
		NfsRchgRecord rchgRecord = rchgRecordService.get(nfsRchgRecord);
		String orderNo = rchgRecord.getPayment().getPaymentNo();
		Map<String, String> queryResultMap = new HashMap<String, String>();
		if(rchgRecord.getType().equals(NfsRchgRecord.Type.lianlian)) {
			queryResultMap = lianlianPayService.queryRchgRecordByOrderNo(orderNo);
			if(queryResultMap.isEmpty()) {
				model.addAttribute("result", "没有记录！");
			}else {
				model.addAttribute("result", queryResultMap.get("resultPay"));
				model.addAttribute("thirdOrderNo", queryResultMap.get("oid_paybill"));
			}
		}else if(rchgRecord.getType().equals(NfsRchgRecord.Type.fuiou)) {
			queryResultMap = fuyouPayService.queryPayResultByMchOrderNo(orderNo);
			if(queryResultMap.isEmpty()) {
				model.addAttribute("result", "没有记录！");
			}else {
				model.addAttribute("result",queryResultMap.get("success").equals("0")?"支付成功":"支付失败");
			}
		}
		return "admin/nfs/recharge/queryThirdOrder";
	}
	
	/**
	 * 下载充值凭证
	 * @param rchgId
	 * @return
	 */
	@RequiresPermissions("recharge:nfsRchgRecord:view")
	@ResponseBody
    @RequestMapping(value = "downLoadReceipt")
    public AjaxRsp downLoadReceipt(Long rchgId) {
		AjaxRsp rsp  = new AjaxRsp();
		
		try {	
			NfsRchgRecord rchgRecord = rchgRecordService.get(rchgId);
			PluginConfig config = fyPaymentPlugin.getPluginConfig();
			Map<String,String> configAttr = config.getAttributeMap();
			String MCHNTCD = configAttr.get(FyPaymentPlugin.PAYMENT_MCHNTCD_ATTRIBUTE_NAME);
			String key = configAttr.get(FyPaymentPlugin.PAYMENT_KEY_ATTRIBUTE_NAME);
			
			String macSource = "1.0|" + MCHNTCD + "|" 
                    + rchgRecord.getPaymentNo() + "|PY36|MD5|"+key;
	        String sign = MD5Code.MD5Encode(macSource, "UTF-8");
			String fm = "<ORDER>"+
	    			"<VERSION>1.0</VERSION>"+
	    			"<MCHNTCD>" + MCHNTCD + "</MCHNTCD>"+
	    			"<MCHNTORDERID>" + rchgRecord.getPaymentNo() + "</MCHNTORDERID>"+    			
	    			"<BUSICD>PY36</BUSICD>"+
	    			"<SIGNTP>MD5</SIGNTP>"+
	    			"<SIGN>" + sign + "</SIGN>"+
	    			"</ORDER>";
			
//	        Map<String, String> queryMap = new HashMap<String, String>();
//	        queryMap.put("FM", fm);
//	        logger.info("FM={}",fm);
//	        String result = new HttpPoster("https://mpay.fuiou.com:16128/checkInfo/createReceipt.pay").postStr(queryMap);
//	        logger.info("result={}",result);
//	        byte[] data = result.getBytes();  
//	        String fileName = rchgRecord.getId()+".pdf";
//	        fileName = URLEncoder.encode(fileName, "UTF-8");  
//	        response.reset();  
//	        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");  
//	        response.addHeader("Content-Length", "" + data.length);  
//	        response.setContentType("application/octet-stream;charset=UTF-8");  
//	        OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());  
//	        outputStream.write(data);  
//	        outputStream.flush();  
//	        outputStream.close(); 
			rsp.setResult(fm);
			rsp.setStatus(true);

		} catch (Exception e) {
			rsp.setStatus(false);
			rsp.setMessage("下载充值凭证失败");
			logger.error(Exceptions.getStackTraceAsString(e));
		}
		return rsp;
    }
}