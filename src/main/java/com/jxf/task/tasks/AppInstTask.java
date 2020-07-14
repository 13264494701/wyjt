package com.jxf.task.tasks;


import com.jxf.mem.entity.MemberLoginInfo;
import com.jxf.mem.service.MemberLoginInfoService;
import com.jxf.svc.sys.app.entity.AppInst;
import com.jxf.svc.sys.app.service.AppInstService;
import java.util.List;

import org.ebaoquan.rop.thirdparty.org.apache.commons.lang3.StringUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 统计任务
 *
 * @author Administrator
 */
@DisallowConcurrentExecution
public class AppInstTask implements Job {

	@Autowired
	private AppInstService appInstService;	
    @Autowired
    private MemberLoginInfoService memberLoginInfoService;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		
		List<MemberLoginInfo> loginInfoList = memberLoginInfoService.findList(new MemberLoginInfo());
		for (MemberLoginInfo loginInfo : loginInfoList) {
			if(StringUtils.isBlank(loginInfo.getDeviceToken())) {
				continue;
			}
			AppInst appInst = new AppInst();
			appInst.setOsType(loginInfo.getOsType());
			appInst.setOsVersion(loginInfo.getOsVersion());
			appInst.setAppVersion(loginInfo.getAppVersion());
			appInst.setAk(loginInfo.getAk());
			appInst.setDeviceModel(loginInfo.getDeviceModel());
			appInst.setDeviceToken(loginInfo.getDeviceToken());
			appInst.setChanneId(loginInfo.getChanneId());
			appInst.setPushToken(loginInfo.getPushToken());
			appInst.setLoginIp(loginInfo.getLoginIp());
			appInstService.save(appInst);
		}

	}

}