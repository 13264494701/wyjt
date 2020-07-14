package com.jxf.transplantation.temp.member;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.Exceptions;
import com.jxf.transplantation.dbo.OldDBOperator;
import com.jxf.transplantation.dbo.OldDBOperatorFactory;
import com.jxf.transplantation.dbo.NowDBOperator;
import com.jxf.transplantation.dbo.NowDBOperatorFactory;


/**
 * @作者: xiaorongdian
 * @创建时间 :2019年01月15日 下午16:38:09
 * @功能说明:会员的账单流水迁移
 */
public class ImportMemberActTrxUtils2017 {
	
	private static Logger log = LoggerFactory.getLogger(ImportMemberActTrxUtils2017.class);
	
	public static void main(String[] args) {
		importTrx(0L, 1000000L);
	}
    
	public static int importTrx(Long startId, Long endId) {
		OldDBOperator oldOperator = OldDBOperatorFactory.getDBOperator();
		NowDBOperator nowOperator = NowDBOperatorFactory.getNowDBOperator();
		PreparedStatement psmt = null;
		PreparedStatement psmtCountSql = null;
		ResultSet rs = null;
		String countsql = " SELECT * from t_fund_detail a where a.id >= ? AND id <=? ORDER BY id";
		Date startDate = new Date();
		log.debug("开始时间:{}",DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
		int i = 0;
		try {
			psmt = oldOperator.getPreparedStatement(countsql);
			psmt.setLong(1, startId);
			psmt.setLong(2, endId);
			rs = psmt.executeQuery();
			
			//去重
			String countSql = "select count(1) as count from MEM_MEMBER_ACT_TRX a where a.id = ? ";
			psmtCountSql = nowOperator.getPreparedStatement(countSql);
			
			String insertTrxSql = " INSERT INTO `MEM_MEMBER_ACT_TRX_2017` ("
					+"`id`,"//1
					+"`trx_code`,"//2
					+"`member_id`,"//3
					+"`sub_no`,"//4
					+"`drc`,"//5
					+"`trx_amt`,"//6
					+"`cur_bal`,"//7
					+"`title`,"//8
					+"`org_id`,"//9
					+"`trx_group`,"//10
					+"`img_url`,"//11
					+"`curr_code`,"//12
					+"`rmk`,"//13
					+"`status`,"//14
					+"`create_by`,"
					+"`create_time`,"//15
					+"`update_by`,"
					+"`update_time`,"//16
					+"`del_flag`"
					+")VALUES(?,?,?,?,?,?,?,?,?,?, ?,?,?,?,'999999', ?,'999999',?,'0')";
			psmt = nowOperator.getPreparedStatement(insertTrxSql);
			while (rs.next()) {
				++i;
				log.debug("开始第" + i + "条数据迁移");
				boolean success = true;
				success = transplantTrx(rs, psmt, psmtCountSql);
				if (success) {
					log.debug("第{}条迁移成功",i);
				} else {
					i--;
				}
			}
			psmt.close();
			psmtCountSql.close();
			log.debug("共{}条数据迁移成功",i);
			log.debug("用时:{}分钟",DateUtils.pastMinutes(startDate));
		} catch (SQLException e) {
			log.error(Exceptions.getStackTraceAsString(e));
		}
		return i;

	}

	private static boolean transplantTrx(ResultSet rs, PreparedStatement psmt, PreparedStatement psmtCountSql) throws SQLException {
		String oldId = rs.getString("id");
		//去重
//		boolean repeat = getRepeat(psmtCountSql,oldId);
//		if(repeat){
//			return false;
//		}
		
		// 用户id
		String userId = rs.getString("userId");
		//此次操作涉及的金额
		String money = rs.getString("money");
		if(StringUtils.isBlank(money)) {
			money = "0";
		}
		//此次操作涉及的红包金额
		String profitMoney = rs.getString("profitMoney");
		if(StringUtils.isBlank(profitMoney)) {
			profitMoney = "0";
		}
		//操作后用户的账户余额
		String resultMoney = rs.getString("resultMoney");
		//表示资金是进帐还是出帐 0进账 1出账
		Integer mode = rs.getInt("mode");
		//资金类型
		/***
		 * 无 0
			快速放款4
			放款5
			还款6
			充值13
			提现14
			还款利息25
			还款补偿金26
			管理员28
			无忧借条借款手续费39
			催收服务手续费40
			法院诉讼手续费41
			部分还款42
			充值手续费43
			富友提现44
			连连提现45
			转账46
			信用报告（一元查看更多）47
		 */
		Integer type = rs.getInt("type");
		//创建时间
		String createTime = rs.getString("createTime");
		//备注
		String remark = rs.getString("remark");
		//客户端标题
		String title = rs.getString("title");
		//优惠券id
		/*2017年2月22日后 改为客服加款是否通过标志
		0为正常 9999为待审核加款 审核通过后改变用户金额*/
//		String couponId = rs.getString("couponId");
		//图片
		String imgUrl = rs.getString("imgUrl");
		//资金类型 0 正常类型 1视频冻结类型 2理财冻结类型 3部分还款冻结类型 4催收服务费冻结资金 5法院诉讼服务费
		String fundType = rs.getString("fundType");

		if(type == 40 && StringUtils.equals(fundType, "4") 
				|| type == 41 && StringUtils.equals(fundType, "5")){
			return false;
		}
		
		
		psmt.setLong(1, Long.parseLong(oldId));
		String trxCode = getTrxCode(type,fundType,mode);
		psmt.setString(2, trxCode);
		psmt.setLong(3, Long.parseLong(userId));
		psmt.setString(4, "0001");
		psmt.setString(5, mode == 0 ? "D" : "C");
		psmt.setString(6, new BigDecimal(money).add(new BigDecimal(profitMoney)).toString());
		psmt.setString(7, resultMoney);
		psmt.setString(8, title);
		psmt.setString(9, "1");//业务ID老数据没存 取不到了
		Integer trxGroup = getTrxGroup(type,fundType,mode);
		psmt.setInt(10, trxGroup);
		psmt.setString(11, imgUrl);
		
		psmt.setString(12, "CNY");
		psmt.setString(13, remark);
		psmt.setString(14, "1");
		psmt.setString(15, createTime);
		psmt.setString(16, createTime);
		//log.debug("-----------------------------" + psmt);
		psmt.executeUpdate();
		
		return true;
	}
	
	
//	private static boolean getRepeat(PreparedStatement psmt,
//			String oldId) throws SQLException {
//		log.debug("去重 开始");
//		psmt.setString(1, oldId);
//		ResultSet rs = psmt.executeQuery();
//		Integer count = 0;
//		while(rs.next()){
//			count = rs.getInt("count");
//		}
//		log.debug("去重 结束>0是重复:" + count);
//		if(count > 0){
//			return true;
//		}else{
//			return false;
//		}
//	}

	private static Integer getTrxGroup(Integer type, String fundType, Integer mode) {
		if(type == 13 || type == 14 || type == 43 || type == 44 || type == 45){
			return 0;
		} else if(type == 46){//快速放款
			return 1;
		} else if(type == 4 || type == 5 || type == 39 ){//快速放款
			return 2;
		}else if(type == 6 || type == 42){
			return 3;
		}else if(type == 41){
			return 4;
		}else if(type == 40){
			return 5;
		}else if(type == 47){
			return 7;
		}else{
			return 6;
		}
		/** 0充值提现记录 1转账 2借款记录 3还款记录  4法律仲裁  5催收  6不显示*/
		/**
		 * 无 0  快速放款4  放款5  还款6  充值13  提现14  还款利息25  还款补偿金26  管理员28  无忧借条借款手续费39  催收服务手续费40  法院诉讼手续费41
			部分还款42  充值手续费43  富友提现44  连连提现45  转账46  信用报告（一元查看更多）47
		 */
	}
	private static String getTrxCode(Integer type, String fundType, Integer mode) {
		if(type == 4){//快速放款
			return "MB010";
		}else if(type == 5){
			return "LN040";
		}else if(type == 6){
			return "RP010";
		}else if(type == 14){
			return "MB021";
		}else if(type == 28 && mode == 0){
			return "MB040";
		}else if(type == 28 && mode == 1){
			return "MB050";
		}else if(type == 39){
			return "LN030";
		}else if(type == 40){
			return "CL010";
		}else if(type == 41){//仲裁
			return "LF010";
		}else if(type == 42){//部分还款
			return "RP020";
		}else if(type == 43){//充值手续费
			return "LN030";
		}else if(type == 44 || type == 45){
			return "MB021"; 
		}else if(type == 46){
			return "MB031";
		}else if(type == 47){
			return "CA020";
		}
		return "MB010";
		
	}
	

}
