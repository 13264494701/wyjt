package com.jxf.task.tasks;

import com.alibaba.fastjson.JSONObject;
import com.jxf.nfs.entity.NfsWdrlRecord;
import com.jxf.nfs.entity.NfsWdrlRecord.Status;
import com.jxf.nfs.entity.NfsWdrlRecord.Type;
import com.jxf.nfs.service.NfsWdrlRecordService;
import com.jxf.pwithdraw.entity.QueryPaymentRequestBean;
import com.jxf.pwithdraw.entity.QueryPaymentResponseBean;
import com.jxf.pwithdraw.entity.RetCodeEnum;
import com.jxf.pwithdraw.service.LianlianPayService;
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.Exceptions;
import com.jxf.svc.utils.StringUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 连连提现提交状态确认处理定时任务
 *
 * @author suHuimin
 */
@DisallowConcurrentExecution
public class HandleWithdrawStatusConfirmTask implements Job {
	private static final Logger logger = LoggerFactory.getLogger(SendWithdrawTask.class);
    @Autowired
    private NfsWdrlRecordService nfsWdrlRecordService;
    @Autowired
    private LianlianPayService lianlianPayService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
		
    	/**
		 * 旧系统提现状态
	     * 100申请中待审核 150 客服推迟审核 200 待提交  300 已提交
	     * 310 疑似重复订单 320 重复订单发送(人工查询) 330 未响应待查询后发送(人工查询)
	     * 350 富友已发送 400 已成功 500客服审核失败(已退换用户金额)
	     * 600反馈失败(未退还用户金额)  700已取消
	     */
		String teshuid = ",649736,1177665,1211823,1324102,1043100,966344,1120895,1328789,1275168,999360,986361,"
				+ "954512,1087659,1247003,1144127,1272473,1243829,1109931,1241223,702143,1237914,1327856,1107410,1328604,1286821,"
				+ "1021847,1028089,1156737,704588,1236012,1113145,1305423,1104970,1299049,1034203,1328441,1165627,"
				+ "1328427,1328692,1224896,1268057,1241961,1302449,1328298,902045,1328572,909933,1254099,1328679,"
				+ "1292053,1203714,922768,1226921,1245613,1265363,1200807,1328211,1328361,1049968,1137776,1210985,"
				+ "1259336,1194016,1137842,1169402,909933,1304582,1328407,597787,1282780,1214759,1287491,1180334,"
				+ "913042,1300690,886988,1208442,1328424,1255953,1328499,1234621,";
		
		
		teshuid = teshuid + ",811591,1023999,1082473,1085239,1100757,1122845,1126655,1149293,1149871,1176282,1222563,"
				+ "1260193,1282350,1307201,1312499,1325965,1399703,1400515,1419253,1421273,1424158,1433663,1447439,1452949,"
				+ "1466371,1469141,1517711,1524760,1528680,1535083,1535108,1540480,1552397,1566349,1566941,1570962,1580237,"
				+ "1583193,1583520,1587356,1592659,1593873,1594490,1602703,1605763,1616314,1618307,1619261,1621063,1621605,1622743,"
				+ "1623341,1629971,1638628,1640375,1642693,1644544,1645301,1650560,1650752,1652946,1655956,1667364,1667472,1669938,1672356,"
				+ "1676617,1679720,1680713,1685893,1687486,1689064,1690044,1693154,1693298,1696246,1696355,1699768,1700156,1701065,1707767,"
				+ "1708166,1709953,1710409,1715460,1722060,1722684,1723809,1725207,1725265,1725302,1726294,1727078,1727154,1727385,1727733,"
				+ "1727868,1727908,1728062,1728118,1728435,1728501,1728503,1728520,1728541,1728574,1728651,1728677,1728687,1728781,1728790,"
				+ "1728823,1728994,1729007,1729033,1729042,1729043,1729133,1729173,1538206,";
		
		
		// 处理提交成功没有返回状态
		withdrawStatusConfirm(teshuid);
	}

    private void withdrawStatusConfirm(String teshuid) {
		//处理提交成功没有返回状态
		Calendar cal_date = Calendar.getInstance();
		cal_date.add(Calendar.DATE, -2);   
		Calendar cal_hour = Calendar.getInstance();
		cal_hour.add(Calendar.HOUR, -6);
	 	
		List<NfsWdrlRecord> wdrlRecords = nfsWdrlRecordService.findSubmitedNoRespCodeOrder(cal_date.getTime(),cal_hour.getTime(),Type.lianlian.ordinal());
		logger.info("lianlian-zhuangtai-"+wdrlRecords.size());
		for(NfsWdrlRecord wdrlRecord : wdrlRecords ){
			if(teshuid.indexOf((","+wdrlRecord.getMember().getId()+","))!=-1){
				logger.error("连连提现-特殊id提现失败-id: "+ wdrlRecord.getMember().getId());
				continue;
			}
			QueryPaymentRequestBean requestBean = new QueryPaymentRequestBean();
			requestBean.setNo_order(wdrlRecord.getId()+"");
			String queryResult = lianlianPayService.queryOrder(requestBean);
			QueryPaymentResponseBean queryPaymentResponseBean = JSONObject.parseObject(queryResult, QueryPaymentResponseBean.class);
			 if(StringUtils.equals(RetCodeEnum.SUCC.code, queryPaymentResponseBean.getRet_code()) &&
					 StringUtils.equals("SUCCESS", queryPaymentResponseBean.getResult_pay())){
				 	try {
						wdrlRecord.setPayTime(DateUtils.parse(queryPaymentResponseBean.getDt_order()));
						wdrlRecord.setStatus(Status.madeMoney);
						wdrlRecord.setPayAmount(new BigDecimal(queryPaymentResponseBean.getMoney_order()));
						wdrlRecord.setThirdOrderNo(queryPaymentResponseBean.getOid_paybill());
						wdrlRecord.setRmk((wdrlRecord.getRmk()==null?"提交后经状态确认接口确认付款成功":(wdrlRecord.getRmk()+"#状态确认#"))+queryPaymentResponseBean.getResult_pay());
						nfsWdrlRecordService.save(wdrlRecord);
					} catch (ParseException e) {
						logger.error("连连提现订单查询接口返回下单时间为{}，解析异常：{}",queryPaymentResponseBean.getDt_order(),Exceptions.getStackTraceAsString(e));
					}
			}else if(StringUtils.equals(RetCodeEnum.NO_RECORD.code, queryPaymentResponseBean.getRet_code())){
				//查询结果返回没有记录 说明在提交订单时候出现异常，给用户退款
				NfsWdrlRecord nfsWdrlRecord = nfsWdrlRecordService.get(wdrlRecord.getId());
				nfsWdrlRecordService.failure(nfsWdrlRecord, "订单提交时出现异常");
			}else {
				//其他错误情况，暂时人工处理
			}
		}
	}
}