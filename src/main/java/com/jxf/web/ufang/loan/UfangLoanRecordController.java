package com.jxf.web.ufang.loan;


import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jxf.loan.constant.RecordMessage;
import com.jxf.loan.entity.NfsLoanApply;
import com.jxf.loan.entity.NfsLoanArbitration;
import com.jxf.loan.entity.NfsLoanArbitrationDetail;
import com.jxf.loan.entity.NfsLoanCollection;
import com.jxf.loan.entity.NfsLoanCollectionDetail;
import com.jxf.loan.entity.NfsLoanDetailMessage;
import com.jxf.loan.entity.NfsLoanRecord;
import com.jxf.loan.service.NfsLoanArbitrationDetailService;
import com.jxf.loan.service.NfsLoanArbitrationService;
import com.jxf.loan.service.NfsLoanCollectionDetailService;
import com.jxf.loan.service.NfsLoanCollectionService;
import com.jxf.loan.service.NfsLoanDetailMessageService;
import com.jxf.loan.service.NfsLoanRecordService;
import com.jxf.loan.utils.LoanUtils;
import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberMessage;
import com.jxf.mem.service.MemberActService;
import com.jxf.mem.service.MemberMessageService;
import com.jxf.mem.service.MemberService;
import com.jxf.mms.msg.SendSmsMsgService;
import com.jxf.nfs.constant.TrxRuleConstant;
import com.jxf.nfs.service.NfsActService;
import com.jxf.svc.cache.RedisUtils;
import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.security.PasswordUtils;
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.StringUtils;
import com.jxf.ufang.entity.UfangUser;
import com.jxf.ufang.util.UfangUserUtils;
import com.jxf.web.ufang.UfangBaseController;


/**
 * 借条记录Controller
 *
 * @author wo
 * @version 2018-10-10
 */
@Controller("ufangLoanRecordController")
@RequestMapping(value = "${ufangPath}/loanRecord")
public class UfangLoanRecordController extends UfangBaseController {

    @Autowired
    private NfsLoanRecordService loanRecordService;
    @Autowired
    private NfsActService actService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberActService memberActService;

    @Autowired
    private NfsLoanArbitrationService loanArbitrationService;
    @Autowired
    private NfsLoanArbitrationDetailService loanArbitrationDetailService;
    @Autowired
    private NfsLoanDetailMessageService loanDetailMessageService;
    @Autowired
    private SendSmsMsgService sendSmsMsgService;
    @Autowired
    private MemberMessageService memberMessageService;
    @Autowired
    private NfsLoanCollectionService loanCollectionService;
    @Autowired
    private NfsLoanCollectionDetailService loanCollectionDetailService;

    @ModelAttribute
    public NfsLoanRecord get(@RequestParam(required = false) Long id) {
        NfsLoanRecord entity = null;
        if (id != null) {
            entity = loanRecordService.get(id);
        }
        if (entity == null) {
            entity = new NfsLoanRecord();
        }
        return entity;
    }

    @RequiresPermissions("ufang:loanRecord:view")
    @RequestMapping(value = {"list", ""})
    public String list(NfsLoanRecord loanRecord, HttpServletRequest request, HttpServletResponse response, Model model) {
        UfangUser ufangUser = UfangUserUtils.getUser();
        if (ufangUser.getBindStatus().equals(UfangUser.BindStatus.binded) && ufangUser.getMember() != null && ufangUser.getMember().getId() != null) {
            loanRecord.setLoaner(ufangUser.getMember());
            Page<NfsLoanRecord> page = loanRecordService.findPageForUfang(new Page<NfsLoanRecord>(request, response), loanRecord);
            model.addAttribute("page", page);
        }
        return "ufang/loan/record/loanRecordList";
    }

    /**
     * 添加页面跳转
     */
    @RequiresPermissions("ufang:loanRecord:view")
    @RequestMapping(value = "add")
    public String add(NfsLoanRecord loanRecord, Model model) {
        model.addAttribute("loanRecord", loanRecord);
        return "ufang/loan/record/loanRecordAdd";
    }

    /**
     * 查看页面跳转
     */
    @RequiresPermissions("ufang:loanRecord:view")
    @RequestMapping(value = "query")
    public String query(NfsLoanRecord loanRecord, Model model) {
        model.addAttribute("loanRecord", loanRecord);
        return "ufang/loan/record/loanRecordQuery";
    }

    /**
     * 修改页面跳转
     */
    @RequiresPermissions("ufang:loanRecord:view")
    @RequestMapping(value = "update")
    public String update(NfsLoanRecord loanRecord, Model model) {
        model.addAttribute("loanRecord", loanRecord);
        return "ufang/loan/record/loanRecordUpdate";
    }

    /**
     * 新增与修改的提交
     */
    @RequiresPermissions("ufang:loanRecord:edit")
    @RequestMapping(value = "save")
    public String save(NfsLoanRecord loanRecord, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, loanRecord)) {
            return add(loanRecord, model);
        }
        loanRecordService.save(loanRecord);
        addMessage(redirectAttributes, "保存借条记录成功");
        return "redirect:" + Global.getAdminPath() + "/loanRecord/?repage";
    }

    /**
     * 真删除提交
     */
    @RequiresPermissions("ufang:loanRecord:edit")
    @RequestMapping(value = "delete")
    public String delete(NfsLoanRecord loanRecord, RedirectAttributes redirectAttributes) {
        loanRecordService.delete(loanRecord);
        addMessage(redirectAttributes, "删除借条记录成功");
        return "redirect:" + ufangPath + "/loanRecord/?repage";
    }

    /**
     * 申请催收
     */
    @RequestMapping(value = "collectionApply")
    public String collectionApply(Model model, String loanId, RedirectAttributes redirectAttributes) {

        NfsLoanRecord loanRecord = loanRecordService.get(Long.parseLong(loanId));
        if (loanRecord.getCollectionStatus().equals(NfsLoanRecord.CollectionStatus.doing)) {
            addMessage(redirectAttributes, "该借条正在催收中");
            return "redirect:" + ufangPath + "/loanRecord";
        }
        if (loanRecord.getArbitrationStatus().equals(NfsLoanRecord.ArbitrationStatus.doing)) {
            addMessage(redirectAttributes, "该借条正在仲裁中");
            return "redirect:" + ufangPath + "/loanRecord";
        }
        NfsLoanRecord.Status status = loanRecord.getStatus();
        if (!status.equals(NfsLoanRecord.Status.overdue)) {

            addMessage(redirectAttributes, "此借款单当前无法申请催收");
            return "redirect:" + ufangPath + "/loanRecord";
        }
        int decTotalMoney = loanRecord.getDueRepayAmount().intValue();
        if (decTotalMoney < 100) {

            addMessage(redirectAttributes, "争议金额低于100元无法申请该服务");
            return "redirect:" + ufangPath + "/loanRecord";
        }
        if (loanRecord.getCollectionTimes() != null && loanRecord.getCollectionTimes() > 3) {

            addMessage(redirectAttributes, "此借款单已经催收三次，无法再次申请");
            return "redirect:" + ufangPath + "/loanRecord";
        }
        //计算逾期利息
        int days = DateUtils.getDistanceOfTwoDate(new Date(), loanRecord.getDueRepayDate());
        BigDecimal overdueDaysBig = new BigDecimal(days);
        //计算利息 默认给24
        BigDecimal overMoney = LoanUtils.calOverdueInterest(loanRecord.getDueRepayAmount(), overdueDaysBig);
        //总金额
        BigDecimal dueRepayMoney = loanRecord.getDueRepayAmount();
        BigDecimal totalMoney = dueRepayMoney.add(overMoney);
        Date date = new Date();
        int delayDays = DateUtils.getDistanceOfTwoDate(date, loanRecord.getDueRepayDate());
        //M1(≤30天)的费率为10%；
        //M2(31<逾期≤60天)的费率为15%；
        //M3(61<逾期≤90天)的费率为20%；
        //M4(91<逾期≤120天)的费率为25%；
        //M5(121<逾期≤150天)的费率为30%；
        //M6(151<逾期≤180天)的费率为35%；
        //M6+(181<逾期<365天)的费率为40%；
        BigDecimal res = new BigDecimal(0.0);
        if (delayDays >= 1 && delayDays <= 30) {
            res = new BigDecimal(0.1).multiply(totalMoney);
        }
        if (delayDays >= 31 && delayDays <= 60) {
            res = new BigDecimal(0.15).multiply(totalMoney);
        }
        if (delayDays >= 61 && delayDays <= 90) {
            res = new BigDecimal(0.2).multiply(totalMoney);
        }
        if (delayDays >= 91 && delayDays <= 120) {
            res = new BigDecimal(0.25).multiply(totalMoney);
        }
        if (delayDays >= 121 && delayDays <= 150) {
            res = new BigDecimal(0.3).multiply(totalMoney);
        }
        if (delayDays >= 151 && delayDays <= 180) {
            res = new BigDecimal(0.35).multiply(totalMoney);
        }
        if (delayDays >= 181) {
            res = new BigDecimal(0.4).multiply(totalMoney);
        }
        BigDecimal avlBal = memberActService.getAvlBal(UfangUserUtils.getUser().getMember());
        model.addAttribute("loan", loanRecord);
        //预支付
        model.addAttribute("payMoney", StringUtils.decimalToStr(res, 2));
        //未还金额
        model.addAttribute("totalMoney", totalMoney);
        //未还期数
        model.addAttribute("repayNum", loanRecord.getDueRepayTerm());
        //账户余额
        model.addAttribute("remainMoney", avlBal);

        return "ufang/loan/record/collectionPay";
    }


    /**
     * 申请仲裁
     */
    @RequestMapping(value = "arbitrationApply")
    public String arbitrationApply(Model model, String loanId, RedirectAttributes redirectAttributes) {

        NfsLoanRecord loanRecord = loanRecordService.get(Long.parseLong(loanId));

        String idNo = loanRecord.getLoanee().getIdNo();
        //获取年份
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
        //获取月份
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("MMdd");
        String createTime = simpleDateFormat.format(loanRecord.getCreateTime());
        String createMMdd = simpleDateFormat1.format(loanRecord.getCreateTime());
        //截取借款人出生日期
        String loaneeAge = idNo.substring(6, 10);
        String loaneeMMdd = idNo.substring(10, 14);
        int age = Integer.parseInt(createTime) - Integer.parseInt(loaneeAge);
        if (age < 23) {
            addMessage(redirectAttributes, "对方未满23岁，暂不支持仲裁");
            return "redirect:" + ufangPath + "/loanRecord";
        }
        if (age == 23 && Integer.parseInt(createMMdd) < Integer.parseInt(loaneeMMdd)) {
            addMessage(redirectAttributes, "对方未满23岁，暂不支持仲裁");
            return "redirect:" + ufangPath + "/loanRecord";
        }

        if (loanRecord.getArbitrationStatus().equals(NfsLoanRecord.ArbitrationStatus.doing)) {
            addMessage(redirectAttributes, "此借款单已申请过仲裁，无法再次申请。");
            return "redirect:" + ufangPath + "/loanRecord";
        }
        if (loanRecord.getCollectionStatus().equals(NfsLoanRecord.CollectionStatus.doing)) {

            addMessage(redirectAttributes, "该借条正在催收中");
            return "redirect:" + ufangPath + "/loanRecord";
        }

        BigDecimal decTotalMoney = loanRecord.getDueRepayAmount();
        if (decTotalMoney.compareTo(new BigDecimal(100)) < 0) {
            addMessage(redirectAttributes, "争议金额低于100元无法申请该服务");
            return "redirect:" + ufangPath + "/loanRecord";
        }

        // 计算逾期利息
        BigDecimal overMoney;
        int days = DateUtils.getDistanceOfTwoDate(new Date(), loanRecord.getDueRepayDate());
        BigDecimal overdueDaysBig = new BigDecimal(days);
        //计算利息 默认给24
        overMoney = LoanUtils.calOverdueInterest(loanRecord.getDueRepayAmount(), overdueDaysBig);
        //总金额
        BigDecimal totalMoney = decTotalMoney.add(overMoney);

//      仲裁费 2018/10/20日后使用
//      <=1000  140
//      1k-5w  40+超过1000部分的4%+100
//      5w-10w 2000+超过5w部分的3%+100
//      10w-20w 3500+超过10w部分的2%+100
//      20w-50w 5500+超过10w部分的1%+100
//      500001 - 100W 8500 + 超过50W元部分的0.5%
//      1000001以上 11000 + 超过100W元部分的0.25%

        double totalMoneyD = totalMoney.doubleValue();
        BigDecimal slmoney = new BigDecimal(0.0);//仲裁受理费
        BigDecimal lawyerMoney;//律师代理执行费
        if (totalMoneyD <= 1000) {
            slmoney = new BigDecimal(140);
        }
        if (totalMoneyD > 1000 && totalMoneyD <= 50000) {
            lawyerMoney = new BigDecimal(100);
            slmoney = new BigDecimal(totalMoneyD).subtract(new BigDecimal(1000));
            slmoney = slmoney.multiply(new BigDecimal(0.04));
            slmoney = slmoney.add(lawyerMoney).add(new BigDecimal(40)).setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        if (totalMoneyD > 50000 && totalMoneyD <= 100000) {
            lawyerMoney = new BigDecimal(100);
            slmoney = new BigDecimal(totalMoneyD).subtract(new BigDecimal(50000));
            slmoney = slmoney.multiply(new BigDecimal(0.03));
            slmoney = slmoney.add(lawyerMoney).add(new BigDecimal(2000)).setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        if (totalMoneyD > 100000 && totalMoneyD <= 200000) {
            lawyerMoney = new BigDecimal(100);
            slmoney = new BigDecimal(totalMoneyD).subtract(new BigDecimal(100000));
            slmoney = slmoney.multiply(new BigDecimal(0.02));
            slmoney = slmoney.add(lawyerMoney).add(new BigDecimal(3500)).setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        if (totalMoneyD > 200000 && totalMoneyD <= 500000) {
            lawyerMoney = new BigDecimal(100);
            slmoney = new BigDecimal(totalMoneyD).subtract(new BigDecimal(200000));
            slmoney = slmoney.multiply(new BigDecimal(0.01));
            slmoney = slmoney.add(lawyerMoney).add(new BigDecimal(5500)).setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        if (totalMoneyD > 500000 && totalMoneyD <= 1000000) {
            lawyerMoney = new BigDecimal(100);
            slmoney = new BigDecimal(totalMoneyD).subtract(new BigDecimal(500000));
            slmoney = slmoney.multiply(new BigDecimal(0.005));
            slmoney = slmoney.add(lawyerMoney).add(new BigDecimal(8500)).setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        if (totalMoneyD > 1000000) {
            lawyerMoney = new BigDecimal(100);
            slmoney = new BigDecimal(totalMoneyD).subtract(new BigDecimal(1000000));
            slmoney = slmoney.multiply(new BigDecimal(0.0025));
            slmoney = slmoney.add(lawyerMoney).add(new BigDecimal(11000)).setScale(2, BigDecimal.ROUND_HALF_UP);
        }

        slmoney = slmoney.setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal avlBal = memberActService.getAvlBal(UfangUserUtils.getUser().getMember());
        model.addAttribute("loan", loanRecord);

        //借款人姓名
        model.addAttribute("loaneeName", loanRecord.getLoanee().getName());
        //账户余额
        model.addAttribute("accountBalance", avlBal);
        //逾期金额
        model.addAttribute("amount", totalMoney);
        //仲裁费用
        model.addAttribute("slmoney", slmoney);

        return "ufang/loan/record/arbitrationPay";
    }


    @RequestMapping(value = "arbitrationPay")
    public String arbitrationPay(String loanId, RedirectAttributes redirectAttributes, String password) {
        Member member = memberService.get(UfangUserUtils.getUser().getMember());

        String isLocked = (String) RedisUtils.getHashKey("payPassword" + member.getId(), "isLocked");
        if (StringUtils.equals(isLocked, "1")) {
            Long nowTime = System.currentTimeMillis();
            String lockedTimeStr = (String) RedisUtils.getHashKey("payPassword" + member.getId(), "lockedTime");
            Long lockedTime = Long.valueOf(lockedTimeStr);
            if (nowTime - lockedTime > 1000 * 60 * 30) {
                RedisUtils.put("payPassword" + member.getId(), "isLocked", "0");
                if (PasswordUtils.validatePassword(password, member.getPayPassword())) {
                    RedisUtils.put("payPassword" + member.getId(), "wrongTimes", "0");
                } else {
                    RedisUtils.put("payPassword" + member.getId(), "wrongTimes", "1");
                    addMessage(redirectAttributes, "密码输入错误，您还有4次机会");
                    return "redirect:" + ufangPath + "/loanRecord";
                }
            } else {
                addMessage(redirectAttributes, "账户已冻结，请30分钟后再试");
                return "redirect:" + ufangPath + "/loanRecord";
            }

        }

        if (!PasswordUtils.validatePassword(password, member.getPayPassword())) {
            String wrongTimesStr = (String) RedisUtils.getHashKey("payPassword" + member.getId(), "wrongTimes");
            Integer wrongTimes = StringUtils.toInteger(wrongTimesStr) + 1;
            if (wrongTimes >= 5) {
                RedisUtils.put("payPassword" + member.getId(), "wrongTimes", String.valueOf(wrongTimes));
                RedisUtils.put("payPassword" + member.getId(), "isLocked", "1");
                RedisUtils.put("payPassword" + member.getId(), "lockedTime", System.currentTimeMillis() + "");
                addMessage(redirectAttributes, "账户已冻结，请30分钟后再试");
            } else {
                RedisUtils.put("payPassword" + member.getId(), "wrongTimes", String.valueOf(wrongTimes));
                addMessage(redirectAttributes, "密码输入错误，您还有" + (5 - wrongTimes) + "次机会");
            }
            return "redirect:" + ufangPath + "/loanRecord";
        }
        NfsLoanRecord loanRecord = loanRecordService.get(Long.parseLong(loanId));
        BigDecimal decTotalMoney = loanRecord.getDueRepayAmount();
        // 计算逾期利息
        BigDecimal overMoney;
        int days = DateUtils.getDistanceOfTwoDate(new Date(), loanRecord.getDueRepayDate());
        BigDecimal overdueDaysBig = new BigDecimal(days);
        //计算利息 默认给24
        overMoney = LoanUtils.calOverdueInterest(loanRecord.getDueRepayAmount(), overdueDaysBig);
        //总金额
        BigDecimal totalMoney = decTotalMoney.add(overMoney);
        double totalMoneyD = totalMoney.doubleValue();
        BigDecimal slmoney = new BigDecimal(0.0);//仲裁受理费
        BigDecimal lawyerMoney;//律师代理执行费
        if (totalMoneyD <= 1000) {
            slmoney = new BigDecimal(140);
        }
        if (totalMoneyD > 1000 && totalMoneyD <= 50000) {
            lawyerMoney = new BigDecimal(100);
            slmoney = new BigDecimal(totalMoneyD).subtract(new BigDecimal(1000));
            slmoney = slmoney.multiply(new BigDecimal(0.04));
            slmoney = slmoney.add(lawyerMoney).add(new BigDecimal(40)).setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        if (totalMoneyD > 50000 && totalMoneyD <= 100000) {
            lawyerMoney = new BigDecimal(100);
            slmoney = new BigDecimal(totalMoneyD).subtract(new BigDecimal(50000));
            slmoney = slmoney.multiply(new BigDecimal(0.03));
            slmoney = slmoney.add(lawyerMoney).add(new BigDecimal(2000)).setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        if (totalMoneyD > 100000 && totalMoneyD <= 200000) {
            lawyerMoney = new BigDecimal(100);
            slmoney = new BigDecimal(totalMoneyD).subtract(new BigDecimal(100000));
            slmoney = slmoney.multiply(new BigDecimal(0.02));
            slmoney = slmoney.add(lawyerMoney).add(new BigDecimal(3500)).setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        if (totalMoneyD > 200000 && totalMoneyD <= 500000) {
            lawyerMoney = new BigDecimal(100);
            slmoney = new BigDecimal(totalMoneyD).subtract(new BigDecimal(200000));
            slmoney = slmoney.multiply(new BigDecimal(0.01));
            slmoney = slmoney.add(lawyerMoney).add(new BigDecimal(5500)).setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        if (totalMoneyD > 500000 && totalMoneyD <= 1000000) {
            lawyerMoney = new BigDecimal(100);
            slmoney = new BigDecimal(totalMoneyD).subtract(new BigDecimal(500000));
            slmoney = slmoney.multiply(new BigDecimal(0.005));
            slmoney = slmoney.add(lawyerMoney).add(new BigDecimal(8500)).setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        if (totalMoneyD > 1000000) {
            lawyerMoney = new BigDecimal(100);
            slmoney = new BigDecimal(totalMoneyD).subtract(new BigDecimal(1000000));
            slmoney = slmoney.multiply(new BigDecimal(0.0025));
            slmoney = slmoney.add(lawyerMoney).add(new BigDecimal(11000)).setScale(2, BigDecimal.ROUND_HALF_UP);
        }

        slmoney = slmoney.setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal avlBal = memberActService.getAvlBal(member);
        if (avlBal.compareTo(slmoney) < 0) {
            addMessage(redirectAttributes, "账户余额不足，请充值后再操作!");
            return "redirect:" + ufangPath + "/loanRecord";
        }

        loanRecord.setArbitrationStatus(NfsLoanRecord.ArbitrationStatus.doing);
        loanRecordService.save(loanRecord);

        NfsLoanArbitration arbitration = new NfsLoanArbitration();
        arbitration.setApplyAmount(totalMoney);
        arbitration.setFee(String.valueOf(slmoney));
        arbitration.setLoan(loanRecord);
        arbitration.setMember(member);
        arbitration.setStatus(NfsLoanArbitration.Status.application);
        arbitration.setChannel(NfsLoanArbitration.Channel.zhangsan);
        arbitration.setStrongStatus(NfsLoanArbitration.StrongStatus.notApplyStrong);

        loanArbitrationService.save(arbitration);

        NfsLoanArbitrationDetail arbitrationDetail = new NfsLoanArbitrationDetail();
        arbitrationDetail.setArbitrationId(arbitration.getId());
        arbitrationDetail.setStatus(NfsLoanArbitrationDetail.Status.underReview);
        arbitrationDetail.setTask(NfsLoanArbitrationDetail.Task.manualReview);
        arbitrationDetail.setType(NfsLoanArbitrationDetail.Type.auditProcess);
        loanArbitrationDetailService.save(arbitrationDetail);

        int code = actService.updateAct(TrxRuleConstant.ARBITRATION_PREPAY, slmoney, member, arbitration.getId());

        if (code == 0) {
            //借款人
            String loaneePhoneNo = loanRecord.getLoanee().getUsername();

            sendSmsMsgService.sendMessage("applicationArbitrationSmsLoanee", loaneePhoneNo, null);
            //放款人
            String loanerPhoneNo = loanRecord.getLoaner().getUsername();
            sendSmsMsgService.sendMessage("applicationArbitrationSmsLoaner", loanerPhoneNo, null);

            //发送会员消息
            memberMessageService.sendMessage(MemberMessage.Type.applicationArbitrationImsLoanee,loanRecord.getId());
            //生成对话
            NfsLoanDetailMessage nfsLoanDetailMessage = new NfsLoanDetailMessage();
            nfsLoanDetailMessage.setDetail(loanRecord.getLoanApplyDetail());
            nfsLoanDetailMessage.setMember(member);
            nfsLoanDetailMessage.setMessageId(RecordMessage.CHAT_5005);
            nfsLoanDetailMessage.setType(RecordMessage.SEND_REMIND);
            loanDetailMessageService.save(nfsLoanDetailMessage);
            redirectAttributes.addFlashAttribute("messageType", "success");
            addMessage(redirectAttributes, "仲裁申请预缴费成功！");
        } else {

            addMessage(redirectAttributes, "系统错误");
        }
        return "redirect:" + ufangPath + "/loanRecord";

    }


    /**
     * 催收支付
     */
    @RequestMapping(value = "collectionPay")
    public String collectionPay(String loanId, RedirectAttributes redirectAttributes, String password) {

        Member member = memberService.get(UfangUserUtils.getUser().getMember());

        String isLocked = (String) RedisUtils.getHashKey("payPassword" + member.getId(), "isLocked");
        if (StringUtils.equals(isLocked, "1")) {
            Long nowTime = System.currentTimeMillis();
            String lockedTimeStr = (String) RedisUtils.getHashKey("payPassword" + member.getId(), "lockedTime");
            Long lockedTime = Long.valueOf(lockedTimeStr);
            if (nowTime - lockedTime > 1000 * 60 * 30) {
                RedisUtils.put("payPassword" + member.getId(), "isLocked", "0");
                if (PasswordUtils.validatePassword(password, member.getPayPassword())) {
                    RedisUtils.put("payPassword" + member.getId(), "wrongTimes", "0");
                } else {
                    RedisUtils.put("payPassword" + member.getId(), "wrongTimes", "1");
                    addMessage(redirectAttributes, "密码输入错误，您还有4次机会");
                    return "redirect:" + ufangPath + "/loanRecord";
                }
            } else {
                addMessage(redirectAttributes, "账户已冻结，请30分钟后再试");
                return "redirect:" + ufangPath + "/loanRecord";
            }

        }

        if (!PasswordUtils.validatePassword(password, member.getPayPassword())) {
            String wrongTimesStr = (String) RedisUtils.getHashKey("payPassword" + member.getId(), "wrongTimes");
            Integer wrongTimes = StringUtils.toInteger(wrongTimesStr) + 1;
            if (wrongTimes >= 5) {
                RedisUtils.put("payPassword" + member.getId(), "wrongTimes", String.valueOf(wrongTimes));
                RedisUtils.put("payPassword" + member.getId(), "isLocked", "1");
                RedisUtils.put("payPassword" + member.getId(), "lockedTime", System.currentTimeMillis() + "");
                addMessage(redirectAttributes, "账户已冻结，请30分钟后再试");
            } else {
                RedisUtils.put("payPassword" + member.getId(), "wrongTimes", String.valueOf(wrongTimes));
                addMessage(redirectAttributes, "密码输入错误，您还有" + (5 - wrongTimes) + "次机会");
            }
            return "redirect:" + ufangPath + "/loanRecord";
        }

        NfsLoanRecord loanRecord = loanRecordService.get(Long.valueOf(loanId));
        if (!NfsLoanRecord.Status.overdue.equals(loanRecord.getStatus())) {

            addMessage(redirectAttributes, "借条状态不支持催收服务");
            return "redirect:" + ufangPath + "/loanRecord";
        }
        int decTotalMoney = loanRecord.getDueRepayAmount().intValue();
        if (decTotalMoney < 100) {

            addMessage(redirectAttributes, "争议金额低于100元无法申请该服务");
            return "redirect:" + ufangPath + "/loanRecord";
        }
        if (loanRecord.getCollectionStatus() != null && NfsLoanRecord.CollectionStatus.doing.equals(loanRecord.getCollectionStatus())) {

            addMessage(redirectAttributes, "您已经申请了催收服务，请耐心等待催收结果！");
            return "redirect:" + ufangPath + "/loanRecord";
        }
        Integer collectionTimes = loanRecord.getCollectionTimes();
        if (collectionTimes != null && collectionTimes >= 3) {

            addMessage(redirectAttributes, "您已经申请催收服务三次了，不能再申请催收了！");
            return "redirect:" + ufangPath + "/loanRecord";
        }
        //计算逾期利息
        int days = DateUtils.getDistanceOfTwoDate(new Date(), loanRecord.getDueRepayDate());
        BigDecimal overdueDaysBig = new BigDecimal(days);
        //计算利息 默认给24
        BigDecimal overMoney = LoanUtils.calOverdueInterest(loanRecord.getDueRepayAmount(), overdueDaysBig);
        //总金额
        BigDecimal dueRepayMoney = loanRecord.getDueRepayAmount();
        BigDecimal totalMoney = dueRepayMoney.add(overMoney);
        Date date = new Date();
        int delayDays = DateUtils.getDistanceOfTwoDate(date, loanRecord.getDueRepayDate());
        //M1(≤30天)的费率为10%；
        //M2(31<逾期≤60天)的费率为15%；
        //M3(61<逾期≤90天)的费率为20%；
        //M4(91<逾期≤120天)的费率为25%；
        //M5(121<逾期≤150天)的费率为30%；
        //M6(151<逾期≤180天)的费率为35%；
        //M6+(181<逾期<365天)的费率为40%；
        BigDecimal amount = new BigDecimal(0.0);
        if (delayDays >= 1 && delayDays <= 30) {
            amount = new BigDecimal(0.1).multiply(totalMoney);
        }
        if (delayDays >= 31 && delayDays <= 60) {
            amount = new BigDecimal(0.15).multiply(totalMoney);
        }
        if (delayDays >= 61 && delayDays <= 90) {
            amount = new BigDecimal(0.2).multiply(totalMoney);
        }
        if (delayDays >= 91 && delayDays <= 120) {
            amount = new BigDecimal(0.25).multiply(totalMoney);
        }
        if (delayDays >= 121 && delayDays <= 150) {
            amount = new BigDecimal(0.3).multiply(totalMoney);
        }
        if (delayDays >= 151 && delayDays <= 180) {
            amount = new BigDecimal(0.35).multiply(totalMoney);
        }
        if (delayDays >= 181) {
            amount = new BigDecimal(0.4).multiply(totalMoney);
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {

            addMessage(redirectAttributes, "催收服务费预缴金额错误，请重新操作！");
            return "redirect:" + ufangPath + "/loanRecord";
        }
        //更改loanrecord的状态
        if (collectionTimes != null) {
            collectionTimes = collectionTimes + 1;
        } else {
            collectionTimes = 1;
        }
        loanRecord.setCollectionTimes(collectionTimes);
        loanRecord.setCollectionStatus(NfsLoanRecord.CollectionStatus.doing);
        loanRecordService.save(loanRecord);

        //生成催收记录
        NfsLoanCollection collection = new NfsLoanCollection();
        collection.setLoan(loanRecord);
        collection.setFee(amount);
        collection.setStatus(NfsLoanCollection.Status.auditing);
        loanCollectionService.save(collection);

        //催收详情
        NfsLoanCollectionDetail collectionDetail = new NfsLoanCollectionDetail();
        collectionDetail.setCollectionId(collection.getId());
        collectionDetail.setStatus(NfsLoanCollectionDetail.Status.underReview);
        if (NfsLoanApply.RepayType.oneTimePrincipalAndInterest.equals(loanRecord.getRepayType())) {
            collectionDetail.setType(NfsLoanCollectionDetail.Type.fullAmount);
        } else {
            collectionDetail.setType(NfsLoanCollectionDetail.Type.staging);
        }
        loanCollectionDetailService.save(collectionDetail);
        int code = actService.updateAct(TrxRuleConstant.COLLECTION_PREPAY, amount, member, collection.getId());
        if (code == 0) {
            //生成对话
            NfsLoanDetailMessage nfsLoanDetailMessage = new NfsLoanDetailMessage();
            nfsLoanDetailMessage.setDetail(loanRecord.getLoanApplyDetail());
            nfsLoanDetailMessage.setMember(member);
            nfsLoanDetailMessage.setMessageId(RecordMessage.CHAT_2201);
            nfsLoanDetailMessage.setType(RecordMessage.SEND_REMIND);
            loanDetailMessageService.save(nfsLoanDetailMessage);
            redirectAttributes.addFlashAttribute("messageType", "success");
            addMessage(redirectAttributes, "催收服务费预支付成功！");
        } else {
            addMessage(redirectAttributes, "系统错误！");
        }
        return "redirect:" + ufangPath + "/loanRecord";
    }


}