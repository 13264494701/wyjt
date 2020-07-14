package com.jxf.web.ufang.rc;


import com.jxf.loan.entity.NfsLoanRecord;
import com.jxf.loan.service.NfsLoanRecordService;
import com.jxf.mem.entity.Member;
import com.jxf.mem.service.MemberService;
import com.jxf.nfs.constant.ActSubConstant;
import com.jxf.nfs.constant.TrxRuleConstant;
import com.jxf.nfs.service.NfsActService;

import com.jxf.svc.config.Constant;

import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.Encodes;
import com.jxf.svc.utils.IdCardUtils;
import com.jxf.svc.utils.JSONUtil;
import com.jxf.svc.utils.StringUtils;
import com.jxf.ufang.entity.UfangLoaneeRcOrder;
import com.jxf.ufang.entity.UfangUser;
import com.jxf.ufang.service.UfangLoaneeRcOrderService;
import com.jxf.ufang.service.UfangUserActService;
import com.jxf.ufang.util.UfangUserUtils;
import com.jxf.web.ufang.UfangBaseController;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;




import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 风控 无忧借条Controller
 *
 * @author wo
 * @version 2019-08-16
 */
@Controller
@RequestMapping(value = "${ufangPath}/rcWyjt")
public class RcWyjtController extends UfangBaseController {


    @Autowired
    private UfangUserActService ufangUserActService;
	@Autowired
	private NfsActService actService;
	@Autowired
	private UfangLoaneeRcOrderService loaneeRcOrderService;
	@Autowired
	private NfsLoanRecordService loanRecordService;
	@Autowired
	private MemberService memberService;



	
	@RequiresPermissions("rcWyjt:view")
    @RequestMapping(value = "/blacklist/query", method = RequestMethod.GET)
    public String query(String msg,Model model) {

		model.addAttribute("msg", msg);
        return "ufang/rc/wyjt/blacklist/query";
    }
	
	
    @RequestMapping(value = "/blacklist/query/submit",method = RequestMethod.POST)
    public String submit(String qPhoneNo,String qIdNo,String qName,Model model) {
    	BigDecimal price = new BigDecimal("1.99");
    	UfangUser ufangUser = UfangUserUtils.getUser();
	    BigDecimal curBal = ufangUserActService.getUserAct(ufangUser,ActSubConstant.UFANG_USER_AVL_BAL).getCurBal();

        if(curBal.compareTo(price) < 0){
        	String msg = Encodes.urlEncode("余额不足，请先充值");
        	return "redirect:/ufang/rcWyjt/blacklist/query?msg=" + msg; 	
        }
        
        Member loanee = memberService.findByUsername(qPhoneNo);
        if(loanee==null) {
            String msg = Encodes.urlEncode("该用户在无忧借条平台暂无借条交易");
            return "redirect:/ufang/rcWyjt/blacklist/query?msg=" + msg;
        }
        if(!StringUtils.equals(qIdNo, loanee.getIdNo())) {
            String msg = Encodes.urlEncode("手机号码和身份证号不一致");
            return "redirect:/ufang/rcWyjt/blacklist/query?msg=" + msg;
        }
        if(!StringUtils.equals(qName, loanee.getName())) {
            String msg = Encodes.urlEncode("手机号码和姓名不一致");
            return "redirect:/ufang/rcWyjt/blacklist/query?msg=" + msg;
        }
        
        int code = actService.updateAct(TrxRuleConstant.UFANG_CONSUME, price, ufangUser,1L);
        if (code == Constant.UPDATE_FAILED) {
            String msg = Encodes.urlEncode("账户处理失败");
            return "redirect:/ufang/rcWyjt/blacklist/query?msg=" + msg;
        }
        
		BigDecimal overDueAmt = BigDecimal.ZERO;
		Integer overDueCnt = 0;

		NfsLoanRecord loanRecord = new NfsLoanRecord();
		loanRecord.setLoanee(loanee);
		loanRecord.setStatus(NfsLoanRecord.Status.overdue);
		Date endDueRepayDate = DateUtils.addCalendarByDate(new Date(), -15);
		loanRecord.setEndDueRepayDate(endDueRepayDate);
		List<NfsLoanRecord> loanList = loanRecordService.findList(loanRecord);
		for (NfsLoanRecord loan : loanList) {
			overDueAmt = overDueAmt.add(loan.getAmount());
			overDueCnt++;
		}
        
		Map<String, Object> overDueInfoMap = new HashMap<String, Object>();
		overDueInfoMap.put("overDueCnt", overDueCnt);
		overDueInfoMap.put("overDueAmt", StringUtils.decimalToStr(overDueAmt, 2));
        UfangLoaneeRcOrder order = new UfangLoaneeRcOrder();
        order.setUser(ufangUser);
        order.setqPhoneNo(qPhoneNo);
        order.setqIdNo(qIdNo);
        order.setqName(qName);
        order.setType(UfangLoaneeRcOrder.Type.wyjt);
        order.setPrice(price);
        order.setData(JSONUtil.toJson(overDueInfoMap));
        order.setPayStatus(UfangLoaneeRcOrder.PayStatus.payed);
        order.setStatus(UfangLoaneeRcOrder.Status.success);
        loaneeRcOrderService.save(order);
        int age = StringUtils.isNotBlank(qIdNo)?IdCardUtils.getAge(qIdNo):0;
        model.addAttribute("isMatch", overDueCnt==0?false:true);
        model.addAttribute("age", age);
        model.addAttribute("overDueAmt", StringUtils.decimalToStr(overDueAmt, 2));
        model.addAttribute("overDueCnt", overDueCnt);
        model.addAttribute("order", order);
    	return "ufang/rc/wyjt/blacklist/result";
    }






}