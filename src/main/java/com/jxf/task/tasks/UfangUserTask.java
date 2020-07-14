package com.jxf.task.tasks;


import com.jxf.svc.utils.DateUtils;
import com.jxf.ufang.entity.UfangUser;

import com.jxf.ufang.service.UfangUserService;

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
 * 统计任务
 *
 * @author Administrator
 */
@DisallowConcurrentExecution
public class UfangUserTask implements Job {

	private static final Logger log = LoggerFactory.getLogger(UfangUserTask.class);

	@Autowired
	private UfangUserService userService;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		
		List<UfangUser> userList = userService.findList(new UfangUser());
		Date date = new Date();
		for (UfangUser user : userList) {
			int days = DateUtils.getDifferenceOfTwoDate(user.getLoginDate(),date);
			if(days<-30) {
				user.setIsLocked(true);
				userService.save(user);
				log.warn("用户{}已超过30天未登录,基于安全规则锁定用户账号",user.getEmpNam());
			}
		}

	}

}