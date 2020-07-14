package com.jxf.task.tasks;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jxf.loan.entity.NfsLoanApply;
import com.jxf.loan.entity.NfsLoanRecord;
import com.jxf.loan.entity.NfsLoanRecord.Status;
import com.jxf.loan.service.NfsLoanRecordService;
import com.jxf.mem.entity.Member;
import com.jxf.mem.service.MemberService;
import com.jxf.svc.config.Global;
import com.jxf.svc.utils.CalendarUtil;
import com.jxf.svc.utils.DateUtils;
import com.jxf.ufang.entity.ZZCRequest;
import com.jxf.ufang.entity.ZZCUpload;
import com.jxf.ufang.service.ZZCUploadService;


/**
 * @作者: xiaorongdian
 * @创建时间 :2019年4月22日 上午11:16:16
 * @功能说明:中智诚的上报接口 上报分2部分 申请前数据/逾期数据
 */
@DisallowConcurrentExecution
public class ZZCUploadTask implements Job {
	
	private static Log log = LogFactory.getLog(ZZCUploadTask.class);
	
	@Autowired
	private NfsLoanRecordService nfsLoanRecordService;
	@Autowired
	private ZZCUploadService zZCUploadService;
	@Autowired
	private MemberService memberService;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException { 
		/**
		 * 中智诚要求上传的类型有3个分别:
		 * 1 批核数据上报 申请时点，批核贷款产品时调用
		 * 2 贷后数据上报 贷款批核后，借贷人账户发生变动（比如：还贷，逾期等）时调用(贷后)
		 * 3 展期数据上报 账单日前，同借贷人约定新的还款时间时调用(有延期)
		 */
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		Date startDate = DateUtils.addCalendarByDate(new Date(),-7);
		
		//目前只逾期借条上报(最近一周)
		NfsLoanRecord nfsLoanRecord = new NfsLoanRecord();
		nfsLoanRecord.setStatus(NfsLoanRecord.Status.overdue);
		nfsLoanRecord.setRepayType(NfsLoanApply.RepayType.oneTimePrincipalAndInterest);
		nfsLoanRecord.setDueRepayDate(startDate);
		List<NfsLoanRecord> latestOverDueRecordList = nfsLoanRecordService.findList(nfsLoanRecord);
		
		nfsLoanRecord = new NfsLoanRecord();
		nfsLoanRecord.setNowDate(startDate);
		Integer daihouCount = 0;
		Integer piheCount = 0;
		Boolean daihouSuccess = false;
		Boolean piheSuccess = false;
		if(latestOverDueRecordList != null && latestOverDueRecordList.size() > 0){
			for (NfsLoanRecord loanRecord : latestOverDueRecordList) {
				String loaneeUsername = loanRecord.getLoanee().getUsername();
				/**
				 * 18603061657 
				 * 18201660917 
				 * 15910691574 
				 * 17310238801
				 * 
				 */
				if(StringUtils.equals(loaneeUsername, "18603061657") || StringUtils.equals(loaneeUsername, "18201660917") 
						|| StringUtils.equals(loaneeUsername, "15910691574") || StringUtils.equals(loaneeUsername, "17310238801") ){
					continue;
				}
				Member member = memberService.get(loanRecord.getLoanee());
				Map<String,Object> daihouRequestBody = new HashMap<String,Object>();
				Map<String,Object> piheRequestBody = new HashMap<String,Object>();
				Status status = loanRecord.getStatus();
				Date dueRepayDate = loanRecord.getDueRepayDate();
				
				piheRequestBody.put("loanId",loanRecord.getId().toString());
				piheRequestBody.put("originalLoanId","");
				piheRequestBody.put("name", member.getName());
				piheRequestBody.put("pid",member.getIdNo());
				piheRequestBody.put("mobile",member.getUsername());
				piheRequestBody.put("loanType","22");
				piheRequestBody.put("accountOpenDate",member.getCreateTime());
				piheRequestBody.put("applyDate",loanRecord.getCreateTime());
				piheRequestBody.put("issueDate",loanRecord.getCreateTime());
				piheRequestBody.put("loanAmount",loanRecord.getAmount().add(loanRecord.getInterest()));
				piheRequestBody.put("totalTerm","1");
				piheRequestBody.put("firstRepaymentDate",DateUtils.formatDate(loanRecord.getDueRepayDate(), "yyyy-MM-dd"));//首次还款日=放款日期+放款时长
				piheRequestBody.put("termPeriod",loanRecord.getTerm());
				
				if(!status.equals(Status.penddingRepay)){
					daihouRequestBody.put("loanId",loanRecord.getId().toString());
					daihouRequestBody.put("termNo",1);
					int pastMonth = CalendarUtil.getIntervalDays(new Date(),dueRepayDate)/30+1;
					
					String nowDateStr = DateUtils.formatDate(new Date(), "yyyy-MM-dd");
					nowDateStr = nowDateStr + " 00:00:00";
					if(status.equals(Status.overdue)){
						daihouRequestBody.put("termStatus","overdue");
						daihouRequestBody.put("realRepaymentDate","");
						daihouRequestBody.put("realRepayment",0);
						daihouRequestBody.put("loanStatus","open");
						daihouRequestBody.put("overdueStatus","M" + pastMonth);
						daihouRequestBody.put("statusConfirmAt",nowDateStr.substring(0,10)+"T"+nowDateStr.substring(11,19));
					}else{
						daihouRequestBody.put("termStatus","normal");
						daihouRequestBody.put("realRepaymentDate",formatDate.format(loanRecord.getCompleteDate())
								.substring(0,10)+"T"+formatDate.format(loanRecord.getCompleteDate()).substring(11,19));
						daihouRequestBody.put("realRepayment",loanRecord.getAmount().add(loanRecord.getInterest()));
						daihouRequestBody.put("loanStatus","closed");
						daihouRequestBody.put("overdueStatus","");
						daihouRequestBody.put("statusConfirmAt",formatDate.format(loanRecord.getCompleteDate())
								.substring(0,10)+"T"+formatDate.format(loanRecord.getCompleteDate()).substring(11,19));
					}
					daihouRequestBody.put("targetRepaymentDate",DateUtils.formatDate(loanRecord.getDueRepayDate(), "yyyy-MM-dd"));
					daihouRequestBody.put("targetRepayment",loanRecord.getAmount().add(loanRecord.getInterest()));
					
				}
				
				try {
					HttpClient httpClient = HttpClients.createDefault();
					String piheRequestStr = JSON.toJSONString(piheRequestBody);
					ZZCRequest zzcRequest = new ZZCRequest(Global.getConfig("zzc.piheUrl"),piheRequestStr);
					
					HttpPost post = zzcRequest.getPost();
					HttpResponse response = httpClient.execute(post);
					String content = EntityUtils.toString(response.getEntity());
					JSONObject responseObjrct = JSON.parseObject(content);
					if(StringUtils.equals((String)responseObjrct.get("status"), "success")){
						log.debug("批核数据上报成功");
						piheSuccess = true;
						piheCount++;
					}else{
						log.error("批核数据上报失败,原因:{}"+responseObjrct);
					}
					
					httpClient = HttpClients.createDefault();
					String daihouRequestStr = JSON.toJSONString(daihouRequestBody);
					zzcRequest = new ZZCRequest(Global.getConfig("zzc.daihouUrl"),daihouRequestStr);
					
					post = zzcRequest.getPost();
					response = httpClient.execute(post);
					content = EntityUtils.toString(response.getEntity());
					responseObjrct = JSON.parseObject(content);
					if(StringUtils.equals((String)responseObjrct.get("status"), "success")){
						log.debug("贷后数据上报成功");
						daihouSuccess = true;
						daihouCount++;
					}else{
						log.error("贷后数据上报失败原因:{}"+responseObjrct);
					}
					
				}catch (UnsupportedEncodingException e) {
						e.printStackTrace();
				}catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(daihouSuccess){
				ZZCUpload zzcUpload = new ZZCUpload();
				zzcUpload.setCount(daihouCount);
				zzcUpload.setStartTime(startDate);
				zzcUpload.setEndTime(new Date());
				zzcUpload.setType(ZZCUpload.Type.record);
				zZCUploadService.save(zzcUpload);
			}
			if(piheSuccess){
				ZZCUpload zzcUpload = new ZZCUpload();
				zzcUpload.setCount(piheCount);
				zzcUpload.setStartTime(startDate);
				zzcUpload.setEndTime(new Date());
				zzcUpload.setType(ZZCUpload.Type.apply);
				zZCUploadService.save(zzcUpload);
			}
		}
		
	}
	
	public static void main(String[] args) {
		System.out.println(31/30+1);
	}
}
