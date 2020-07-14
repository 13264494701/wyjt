package com.jxf.task.tasks.update;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jxf.svc.sys.area.entity.Area;
import com.jxf.svc.sys.area.service.AreaService;
import com.jxf.svc.utils.Exceptions;
import com.jxf.svc.utils.StringUtils;
import com.jxf.transplantation.dbo.NowDBOperator;
import com.jxf.transplantation.dbo.NowDBOperatorFactory;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *   获取视频认证通过会员所在的城市
 * @author suHuimin
 */
@DisallowConcurrentExecution
public class GetMemberCityTask implements Job {
	private static final Logger logger = LoggerFactory.getLogger(GetMemberCityTask.class);
    @Autowired
    private AreaService areaService;
    
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
    	logger.error("开始处理会员所在市");
    	//获取所有的会员的id;
    	Long memberSize = 2780000L;
    	int pageSize = 5000;
    	int allPageNo = (int) (memberSize/pageSize) + 1;
	   	List<Area> ciytAreas = areaService.getAreaByType("2");
	   	List<Area> countyAreas = areaService.getAreaByType("3");
		NowDBOperator nowOperator = NowDBOperatorFactory.getNowDBOperator();
		PreparedStatement psmt = null;
		ResultSet rs = null;
		PreparedStatement updatePsmt = null;
		String countsql = "SELECT * FROM MEM_MEMBER  WHERE  area_id is null  ORDER BY id limit ?,?";
		
		String updateSql = "UPDATE `MEM_MEMBER` SET area_id=? where id=?";
		for (int i = 1; i <= allPageNo; i++) {
			logger.error("开始处理第{}批会员",i);
			try {
				psmt = nowOperator.getPreparedStatement(countsql);
				psmt.setInt(1, i);
				psmt.setInt(2, pageSize);
				rs = psmt.executeQuery();
				updatePsmt = nowOperator.getPreparedStatement(updateSql);
				while (rs.next()) {
					Long id = rs.getLong("id");
					String phoneNo = rs.getString("username");
					if(StringUtils.isBlank(phoneNo)) {
						logger.error("会员{}手机号错误",id);
						continue;
					}
					Long areaId = getMemberAreaByPhoneNo(phoneNo, ciytAreas,countyAreas);
					if(areaId == null) {
						continue;
					}
					updatePsmt.setLong(1, areaId);
					updatePsmt.setLong(2, Long.parseLong(rs.getString("id")));
					updatePsmt.executeUpdate();
				}
				psmt.close();
				updatePsmt.close();
			} catch (SQLException e) {
				logger.error("异常：{}", Exceptions.getStackTraceAsString(e));
			}
		}
    	logger.error("处理会员所在市完毕");
	}
   private Long getMemberAreaByPhoneNo(String phoneNo,List<Area> ciytAreas,List<Area> countyAreas) {
	   String areaName = getAreaName(phoneNo);
	   if(StringUtils.isBlank(areaName)) {
		   return null;
	   }
	   String areaNamePrefix = "";
	   if(areaName.length() > 2) {
		   areaNamePrefix = StringUtils.substring(areaName, 0, 2);
	   }else {
		   areaNamePrefix = StringUtils.substring(areaName, 0);
	   }
		for (Area area : ciytAreas) {
			String namePrefix = area.getName().substring(0, 2);
			if (StringUtils.equals(namePrefix, areaNamePrefix)) {
				return area.getId();
			}
		}
		for (Area area : countyAreas) {
			String namePrefix = "";
			if(area.getName().length() > 2) {
				namePrefix = area.getName().substring(0,2);
			}else {
				namePrefix = area.getName().substring(0);
			}
			if(StringUtils.equals(namePrefix, areaNamePrefix)) {
				return area.getId();
			}
		}
		logger.error("手机号{}没有找到对应地区",phoneNo);
		return null;
   }
   
   private String getAreaName(String phoneNo ) {
		try {
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpGet get = new HttpGet("http://mobsec-dianhua.baidu.com/dianhua_api/open/location?tel="+phoneNo);
			CloseableHttpResponse response = httpClient.execute(get);
			HttpEntity entity = response.getEntity();
			String result = EntityUtils.toString(entity);
			JSONObject jsonObject = JSONObject.parseObject(result);
			String res1 = jsonObject.get("response").toString();
			JSONObject jsonObject2 = JSONObject.parseObject(res1);
			JSONObject jsonObject3 = (JSONObject) jsonObject2.get(phoneNo);
			if(jsonObject3 == null) {
				return "";
			}
			JSONObject jsonObject4 = (JSONObject) jsonObject3.get("detail");
			JSONArray jsonObject5 = (JSONArray) jsonObject4.get("area");
			JSONObject jsonObject6 = (JSONObject) jsonObject5.get(0);
			String jsonObject7 = (String) jsonObject6.get("city");
			return jsonObject7;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
   }


}
