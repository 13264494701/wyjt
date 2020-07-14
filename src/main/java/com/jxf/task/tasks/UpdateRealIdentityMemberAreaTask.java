package com.jxf.task.tasks;

import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberVideoVerify;
import com.jxf.mem.service.MemberService;
import com.jxf.mem.service.MemberVideoVerifyService;
import com.jxf.svc.sys.area.entity.Area;
import com.jxf.svc.sys.area.service.AreaService;
import com.jxf.svc.utils.CalendarUtil;
import com.jxf.svc.utils.DateUtils;

import java.util.Date;
import java.util.List;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 用户实名认证通过后更新用户的地址
 *
 * @author suHuimin
 */
@DisallowConcurrentExecution
public class UpdateRealIdentityMemberAreaTask implements Job {
	private static final Logger logger = LoggerFactory.getLogger(UpdateRealIdentityMemberAreaTask.class);
    @Autowired
    private MemberVideoVerifyService memberVideoVerifyService;
    @Autowired
    private MemberService memberService;
    @Autowired 
	private AreaService areaService;
    
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
		//查询最近1个小时内实名认证通过的用户
    	String createTime = DateUtils.getDateStr(CalendarUtil.addHour(new Date(), -5), "yyyy-MM-dd HH:mm:ss");
    	List<MemberVideoVerify> realIdentityList = memberVideoVerifyService.getLast5hRealIdentityRecords(createTime);
    	
    	for (MemberVideoVerify memberVideoVerify : realIdentityList) {
    		//TODO 同一用户多次认证的，从SQL上区分
    		Member member = memberVideoVerify.getMember();
    		member = memberService.get(member);
    		if(member.getArea() != null) {
    			continue;
    		}
    		String address = memberVideoVerify.getAddress();
    		Area area = areaService.getCityByIDCardAddress(address);
    		if(area!=null) {
    			member = memberService.get(member);
    			member.setArea(area);
    			memberService.save(member);
    		}else {
    			//数据库地址跟身份证地址对应不上，需要做调整
    			logger.error("会员{}实名认证返回地址{},数据库没有对应的市区县",member.getId(),address);
    		}
    	}
	}
    
   
}