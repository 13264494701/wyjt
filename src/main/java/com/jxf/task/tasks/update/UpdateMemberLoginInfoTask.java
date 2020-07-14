package com.jxf.task.tasks.update;


import com.jxf.mem.dao.MemberLoginInfoDao;
import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberLoginInfo;
import com.jxf.svc.cache.RedisUtils;
import com.jxf.svc.sys.data.entity.SysDataImport;
import com.jxf.svc.sys.data.entity.SysDataTask;
import com.jxf.svc.sys.data.service.SysDataImportService;
import com.jxf.svc.sys.data.service.SysDataTaskService;
import com.jxf.svc.sys.util.UserUtils;
import com.jxf.svc.utils.DateUtils;
import com.jxf.transplantation.dbo.NowDBOperator;
import com.jxf.transplantation.dbo.NowDBOperatorFactory;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.List;


import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;


/**
 *   更新会员
 *
 * @author wo
 */
@DisallowConcurrentExecution
public class UpdateMemberLoginInfoTask implements Job {

    
	

	@Autowired
	private SysDataImportService dataImportService;
	@Autowired
	private SysDataTaskService sysDataTaskService;
	@Autowired
	private MemberLoginInfoDao loginInfoDao;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

    	SysDataImport dataImport = dataImportService.getByHandler("updateLoginInfo");
    	SysDataTask task = new SysDataTask();
    	task.setData(dataImport);
    	task.setStatus(SysDataTask.Status.waitDo);
    	task.setIsOn(false); 
    	List<SysDataTask> taskList = sysDataTaskService.findList(task);
    	
    	for(SysDataTask dataTask:taskList) {
    		
    		dataTask = sysDataTaskService.get(dataTask);
    		if(dataTask==null||dataTask.getStatus().equals(SysDataTask.Status.closed)) {
    			continue;
    		}
    		
    		dataTask.setStatus(SysDataTask.Status.doing);
    		dataTask.setIsOn(true);   		
    		sysDataTaskService.save(dataTask);
    		Long startId = dataTask.getStartId();
    		Long endId =dataTask.getEndId();
    		int quantity = importLoginInfo(startId, endId);    
    		dataTask.setQuantity(quantity);
    		dataTask.setStatus(SysDataTask.Status.closed);
    		dataTask.setIsOn(false);
    		sysDataTaskService.save(dataTask);
    		dataImport.setImportQuantity(dataImport.getImportQuantity()+quantity);
    		dataImportService.save(dataImport);
    	}
    	
    	importLoginInfo(292706959524761600L, 335219181194711040L);
    }
    

    private int importLoginInfo(Long startId, Long endId) {
    	
		NowDBOperator nowOperator = NowDBOperatorFactory.getNowDBOperator();
		PreparedStatement psmt = null;
		ResultSet rs = null;

		String countsql = "SELECT id FROM MEM_MEMBER  WHERE  id >=? AND id <=?";
		int i = 0;
		try {
			psmt = nowOperator.getPreparedStatement(countsql);
			psmt.setLong(1, startId);
			psmt.setLong(2, endId);
			rs = psmt.executeQuery();
			while (rs.next()) {
				++i;
				String memberId = rs.getString("id");
				if(RedisUtils.hasKey("loginInfo" + memberId)) {
					
					String osType = (String) RedisUtils.getHashKey("loginInfo" + memberId, "osType");
					String osVersion = (String) RedisUtils.getHashKey("loginInfo" + memberId, "osVersion");
					String appVersion = (String) RedisUtils.getHashKey("loginInfo" + memberId, "appVersion");
					String ak = (String) RedisUtils.getHashKey("loginInfo" + memberId, "ak");
					String deviceModel = (String) RedisUtils.getHashKey("loginInfo" + memberId, "deviceModel");
					String deviceToken = (String) RedisUtils.getHashKey("loginInfo" + memberId, "deviceToken");
					String channeId = (String) RedisUtils.getHashKey("loginInfo" + memberId, "channeId");
					String pushToken = (String) RedisUtils.getHashKey("loginInfo" + memberId, "pushToken");
					String loginIp = (String) RedisUtils.getHashKey("loginInfo" + memberId, "loginIp");		
					String loginTime = (String) RedisUtils.getHashKey("loginInfo" + memberId, "loginTime");
					
					int n = DateUtils.getDifferenceOfTwoDate(DateUtils.parseDate("2019-06-04 20:07:56"), DateUtils.parseDate(loginTime));
					if(n>0) {
						MemberLoginInfo loginInfo = new MemberLoginInfo();
						loginInfo.setId(Long.valueOf(memberId));
						loginInfo.setMember(new Member(Long.valueOf(memberId)));
						loginInfo.setOsType(osType);
						loginInfo.setOsVersion(osVersion);
						loginInfo.setAppVersion(appVersion);
						loginInfo.setAk(ak);
						loginInfo.setDeviceModel(deviceModel);
						loginInfo.setDeviceToken(deviceToken);
						loginInfo.setChanneId(channeId);
						loginInfo.setPushToken(pushToken);
						loginInfo.setLoginIp(loginIp);
						loginInfo.setCreateBy(UserUtils.getUser());
						loginInfo.setCreateTime(DateUtils.parseDate(loginTime));
						loginInfo.setUpdateBy(UserUtils.getUser());
						loginInfo.setUpdateTime(DateUtils.parseDate(loginTime));
						loginInfo.setDelFlag("0");
						loginInfoDao.insert(loginInfo);
					}else {
						i--;
					}

				}else {
					i--;
				}
				
			}

		    psmt.close();


		} catch (SQLException e) {
	
		}
		return i;
	}
    
    
}