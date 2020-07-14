package com.jxf.web.api;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.jxf.loan.entity.NfsLoanRecord;
import com.jxf.loan.service.NfsLoanRecordService;

import com.jxf.svc.utils.DateUtils;
import com.jxf.web.admin.sys.BaseController;

/**
 * 给第三方查询流量Controller
 * 
 * @author XIAORONGDIAN
 * @version 2019-04-17
 */
@Controller
@RequestMapping(value = "${wyjtApi}/loanData")
public class LoaneeDataController extends BaseController {

	@Autowired
	private NfsLoanRecordService loanRecordService;

	@RequestMapping(value = "fetch", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getData(HttpServletRequest request, HttpServletResponse response) {

		JSONObject json = getRequestJson(request);
		String dateStr = json.getString("dateStr");
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		try {
			Calendar beginCalendar = DateUtils.toCalendar(DateUtils.parseDate(dateStr));
			beginCalendar.set(Calendar.HOUR_OF_DAY, beginCalendar.getActualMinimum(Calendar.HOUR_OF_DAY));
			beginCalendar.set(Calendar.MINUTE, beginCalendar.getActualMinimum(Calendar.MINUTE));
			beginCalendar.set(Calendar.SECOND, beginCalendar.getActualMinimum(Calendar.SECOND));
			Date beginTime = beginCalendar.getTime();

			Calendar endCalendar = DateUtils.toCalendar(DateUtils.parseDate(dateStr));
			endCalendar.set(Calendar.HOUR_OF_DAY, endCalendar.getActualMaximum(Calendar.HOUR_OF_DAY));
			endCalendar.set(Calendar.MINUTE, endCalendar.getActualMaximum(Calendar.MINUTE));
			endCalendar.set(Calendar.SECOND, endCalendar.getActualMaximum(Calendar.SECOND));
			Date endTime = endCalendar.getTime();

			NfsLoanRecord loanRecord = new NfsLoanRecord();
			loanRecord.setBeginUpdateTime(beginTime);
			loanRecord.setEndUpdateTime(endTime);
			List<NfsLoanRecord> loanList = loanRecordService.findList(loanRecord);

			for (NfsLoanRecord loan : loanList) {
				Map<String, Object> data = new HashMap<String, Object>();
				data.put("loanId", loan.getId());
				data.put("loaneeName", loan.getLoaneeName());
				data.put("loaneePhoneNo", loan.getLoaneePhoneNo());
//				data.put("loaneeIdNo", loan.getLoaneeIdNo());
				data.put("loaneeIdNo", loan.getLoanee().getIdNo());
				data.put("trxType", loan.getTrxType().ordinal());
				data.put("repayType", loan.getRepayType().ordinal());
				data.put("amount", loan.getAmount());
				data.put("intRate", loan.getIntRate());
				data.put("interest", loan.getInterest());
				data.put("overdueInterest", loan.getOverdueInterest());
				data.put("dueRepayAmount", loan.getDueRepayAmount());
//				data.put("startDate", loan.getStartDate());
				data.put("startDate", loan.getLoanStart());
				data.put("term", loan.getTerm());
				data.put("dueRepayDate", loan.getDueRepayDate());
				data.put("completeDate", loan.getCompleteDate());
				data.put("status", loan.getStatus().ordinal());
				data.put("createTime", loan.getCreateTime());
				data.put("updateTime", loan.getUpdateTime());
				dataList.add(data);
			}
		} catch (Exception e) {

		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", "0");
		map.put("msg", "数据获取成功");
		map.put("data", dataList);
		return map;
	}

	/**
	 * 获取json对象
	 * 
	 * @param request
	 * @return
	 */
	private static JSONObject getRequestJson(HttpServletRequest request) {
		InputStream in;
		JSONObject json = null;
		try {
			in = request.getInputStream();
			byte[] b = new byte[10240];
			int len;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while ((len = in.read(b)) > 0) {
				baos.write(b, 0, len);
			}
			String bodyText = new String(baos.toByteArray(), "UTF-8");
			json = (JSONObject) JSONObject.parse(bodyText);
		} catch (IOException e) {

		}
		return json;
	}

}