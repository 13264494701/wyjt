package com.jxf.web.wx;

import com.jxf.mem.entity.Member;
import com.jxf.mem.service.MemberService;
import com.jxf.ufang.entity.UfangLoaneeData;
import com.jxf.ufang.service.UfangLoaneeDataService;
import com.jxf.web.app.BaseController;

import org.ebaoquan.rop.thirdparty.org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("wyjtWxLoaneeController")
@RequestMapping(value = "${wyjtWx}/loanee")
public class LoaneeController extends BaseController {

    @Autowired
    private UfangLoaneeDataService ufangLoaneeDataService;
    @Autowired
    private MemberService memberService;

    @RequestMapping(value = "/add")
    public String addProfessionalLoan(Model model, Integer userZM, String userQQCode, String userWxCode, String userName, Integer userAge, String userTel) {

        //获取当前用户
        Member member = memberService.getCurrent();
        
        //每周一个用户只能提交一次
        if (ufangLoaneeDataService.selectWeekUpdateCount(member.getUsername()) > 0) {
            model.addAttribute("status", 0);
            return "wx/otherLoanResult";
        }
        
        //获取表单参数
        UfangLoaneeData ufangLoaneeData = ufangLoaneeDataService.findByPhoneNo(member.getUsername());
        
        if(ufangLoaneeData==null) {
        	ufangLoaneeData = new UfangLoaneeData();
        }
        ufangLoaneeData.setZhimafen(userZM);
        ufangLoaneeData.setQqNo(userQQCode);
        ufangLoaneeData.setWeixinNo(userWxCode);
        ufangLoaneeData.setYunyingshangStatus(UfangLoaneeData.YunyingshangStatus.unverified);
        ufangLoaneeData.setLoaneeId(member.getId());
        ufangLoaneeData.setChannel(UfangLoaneeData.Channel.weixin);
        ufangLoaneeData.setStatus(UfangLoaneeData.Status.fresh);

        ufangLoaneeData.setName(userName);
        ufangLoaneeData.setPhoneNo(member.getUsername());
        ufangLoaneeData.setIdNo(member.getIdNo());

        ufangLoaneeData.setAge(userAge);

        ufangLoaneeData.setApplyAmount("1000");
        ufangLoaneeData.setSales("0");


        //校验
        if (StringUtils.isBlank(userWxCode)||StringUtils.isBlank(userQQCode)) {
            model.addAttribute("status", 0);
            return "wx/otherLoanResult";
        }

        //保存
        ufangLoaneeDataService.save(ufangLoaneeData);
        model.addAttribute("status", 1);
        return "wx/otherLoanResult";
    }

}