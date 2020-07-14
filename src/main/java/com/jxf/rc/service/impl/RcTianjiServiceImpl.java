package com.jxf.rc.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.FileUtils;
import com.jxf.svc.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jxf.mem.entity.Member;
import com.jxf.mem.service.MemberService;
import com.jxf.rc.dao.RcTianjiDao;
import com.jxf.rc.entity.RcTianji;
import com.jxf.rc.entity.RcTianji.ChannelType;
import com.jxf.rc.entity.RcTianji.DataStatus;
import com.jxf.rc.entity.TianjiConstant;
import com.jxf.rc.service.RcTianjiService;
import com.jxf.rc.utils.TianJiHttpUtils;
/**
 * 天机ServiceImpl
 * @author suhuimin
 * @version 2019-07-31
 */
@Service("rcTianjiService")
@Transactional(readOnly = true)
public class RcTianjiServiceImpl extends CrudServiceImpl<RcTianjiDao, RcTianji> implements RcTianjiService{
	private static final Logger logger = LoggerFactory.getLogger(RcTianjiServiceImpl.class);
	
	@Autowired
	private RcTianjiDao rcTianjiDao;
	@Autowired
	private MemberService memberService;
	
	
    @Override
	public RcTianji get(Long id) {
		return super.get(id);
	}
	
	@Override
	public List<RcTianji> findList(RcTianji rcTianji) {
		return super.findList(rcTianji);
	}
	
	@Override
	public Page<RcTianji> findPage(Page<RcTianji> page, RcTianji rcTianji) {
		return super.findPage(page, rcTianji);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void save(RcTianji rcTianji) {
		super.save(rcTianji);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void delete(RcTianji rcTianji) {
		super.delete(rcTianji);
	}
	
	@Override	
	public JSONObject getCrawlData(String searchId, String accountType,String method,String  userId) {
		// searchId:h5接入：回调参数中的search_id参数作为user_id sdk接入：回调参数中的session的值
		Map<String, String> map = new HashMap<String, String>();
		map.put("merchan_id",TianjiConstant.tj_app_id);
		if(StringUtils.equals(accountType, "alipay_crawl")) {
			map.put("session", searchId);
		}else if(StringUtils.equals(accountType, "ibank_crawl")) {
			//TODO 需要分APP：取member数据， 优放 ：取rctianji里的数据
			Member member = memberService.get(Long.valueOf(userId));
//			RcTianji rcTianji = get(Long.valueOf(userId));
//			map.put("cellphone", rcTianji.getPhoneNo());
//			map.put("id_card", rcTianji.getIdNo());
//			map.put("real_name", rcTianji.getRealName());
			map.put("cellphone", member.getUsername());
			map.put("id_card", member.getIdNo());
			map.put("real_name", member.getName());
			map.put("user_id", searchId);
		}else {
			map.put("user_id", searchId);
		}
		TianJiHttpUtils tianJiHttpUtils = new TianJiHttpUtils();
		tianJiHttpUtils.setBizData(JSON.toJSONString(map));
		tianJiHttpUtils.setMethod(method);
		tianJiHttpUtils.setTimestamp(String.valueOf(System.currentTimeMillis()));
		tianJiHttpUtils.setSign();
		logger.info("天机查询抓取数据method:{}",method);
		JSONObject response = tianJiHttpUtils.post();
		if(response != null) {
			logger.info("天机数据获取返回结果码：{}",response.getString("error"));
		}
		return response;
	}

	@Override
	public RcTianji findByTaskId(String taskId) {
		return rcTianjiDao.findByTaskId(taskId);
	}

	@Override
	public RcTianji findByPhoneNoChannelTypeDataStatus(String phoneNo, ChannelType channelType, DataStatus dataStatus) {
		RcTianji rcTianji = new RcTianji();
		rcTianji.setPhoneNo(phoneNo);
		rcTianji.setChannelType(channelType);
		rcTianji.setDataStatus(dataStatus);
		return rcTianjiDao.findByPhoneNoChannelTypeDataStatus(rcTianji);
	}

	@Override
	@Transactional(readOnly = false)
	public void saveTaskData(RcTianji rcTianji) {
		if(rcTianji.getIsNewRecord()) {
			rcTianji.preInsert();
			String path = "/tianji/task_data/"+DateUtils.getDate("yyyy/MM/dd")+"/";
			String dir = Global.getConfig("uploadPath") + path ;
			FileUtils.createDirectory(dir);
			String taskDataFileName = rcTianji.getId()+".txt";
			String taskData = rcTianji.getTaskData();

			FileUtils.writeToFile(dir+taskDataFileName,taskData,false);
			String taskDataPath = path+taskDataFileName;
			rcTianji.setDataPath(taskDataPath);
			rcTianjiDao.insert(rcTianji);
		}else {
			String path = "/tianji/task_data/"+DateUtils.getDate("yyyy/MM/dd")+"/";
			String dir = Global.getConfig("uploadPath") + path ;
			FileUtils.createDirectory(dir);
			String taskDataFileName = rcTianji.getId()+".txt";
			String taskData = rcTianji.getTaskData();

			FileUtils.writeToFile(dir+taskDataFileName,taskData,false);
			String taskDataPath = path+taskDataFileName;
			rcTianji.setDataPath(taskDataPath);
			
			rcTianji.preUpdate();
			rcTianjiDao.update(rcTianji);
		}	
	
		
	}

	@Override
	@Transactional(readOnly = false)
	public void saveTaskReport(RcTianji rcTianji) {
		if(rcTianji.getIsNewRecord()) {
			rcTianji.preInsert();
			String path = "/tianji/report_data/"+DateUtils.getDate("yyyy/MM/dd")+"/";
			String dir = Global.getConfig("uploadPath") + path ;
			FileUtils.createDirectory(dir);
			String taskReportFileName = rcTianji.getId()+".txt";
			String taskReport = rcTianji.getReportData();

			FileUtils.writeToFile(dir+taskReportFileName,taskReport,false);
			String taskReportPath = path+taskReportFileName;
			rcTianji.setReportPath(taskReportPath);

			String htmlPath = "/tianji/html/"+DateUtils.getDate("yyyy/MM/dd")+"/";
			String htmlDir = Global.getConfig("uploadPath") + htmlPath;
			FileUtils.createDirectory(htmlDir);
			String htmlName = rcTianji.getId() + ".html";
			String htmlStr = rcTianji.getHtmlStr();
			
			FileUtils.writeToFile(htmlDir+htmlName, htmlStr, false);
			htmlPath = htmlPath + htmlName;
			rcTianji.setHtmlPath(htmlPath);
			rcTianjiDao.insert(rcTianji);
		}else {
			String path = "/tianji/report_data/"+DateUtils.getDate("yyyy/MM/dd")+"/";
			String dir = Global.getConfig("uploadPath") + path ;
			FileUtils.createDirectory(dir);
			String taskReportFileName = rcTianji.getId()+".txt";
			String taskReport = rcTianji.getReportData();

			FileUtils.writeToFile(dir+taskReportFileName,taskReport,false);
			String taskReportPath = path+taskReportFileName;
			rcTianji.setReportPath(taskReportPath);
			
			String htmlPath = "/tianji/html/"+DateUtils.getDate("yyyy/MM/dd")+"/";
			String htmlDir = Global.getConfig("uploadPath") + htmlPath;
			FileUtils.createDirectory(htmlDir);
			String htmlName = rcTianji.getId() + ".html";
			String htmlStr = rcTianji.getHtmlStr();
			
			FileUtils.writeToFile(htmlDir+htmlName, htmlStr, false);
			htmlPath = htmlPath + htmlName;
			rcTianji.setHtmlPath(htmlPath);
			
			rcTianji.preUpdate();
			rcTianjiDao.update(rcTianji);
		}
	}

	@Override
	public List<RcTianji> findListByEmpNo(RcTianji rcTianji) {
		return rcTianjiDao.findListByEmpNo(rcTianji);
	}

	@Override
	public Page<RcTianji> findPageByEmpNo(Page<RcTianji> page, RcTianji rcTianji) {
		rcTianji.setPage(page);
		page.setList(rcTianjiDao.findListByEmpNo(rcTianji));
		return page;
	}

	@Override
	public JSONObject getReportDetail(String userId, String accountType, String outUniqueId) {
		//组建请求参数biz_data
		Map<String, String> map = new HashMap<String, String>();
		map.put("userId",userId);
		map.put("outUniqueId", outUniqueId);
		if(StringUtils.equals(accountType, "jd") || StringUtils.equals(accountType, "mobile")) {
			map.put("reportType", "html");
		}
		TianJiHttpUtils tianJiHttpUtils = new TianJiHttpUtils();
		tianJiHttpUtils.setBizData(JSON.toJSONString(map));
		tianJiHttpUtils.setMethod("tianji.api.tianjireport.detail");
		tianJiHttpUtils.setTimestamp(String.valueOf(System.currentTimeMillis()));
		tianJiHttpUtils.setSign();
		JSONObject response = tianJiHttpUtils.post();
		return response;
	}
	
}