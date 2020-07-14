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
import com.jxf.svc.utils.SnowFlake;
import com.jxf.transplantation.dbo.OldDBOperator;
import com.jxf.transplantation.dbo.OldDBOperatorFactory;
import com.jxf.transplantation.dbo.NowDBOperator;
import com.jxf.transplantation.dbo.NowDBOperatorFactory;

/**
 * @作者: gaobo
 * @创建时间 :2019年 1 月22日 下午2:24:43
 * @功能说明:会员可用余额 借款账户迁移
 */
public class ImportMemberAvailableAct {
	
	private static Logger log = LoggerFactory.getLogger(ImportMemberAvailableAct.class);

	public static void main(String[] args) {
		importAvailableAct(1L, 5000L);
	}

	public static int importAvailableAct(Long startId, Long endId) {
		OldDBOperator oldOperator = OldDBOperatorFactory.getDBOperator();
		NowDBOperator nowOperator = NowDBOperatorFactory.getNowDBOperator();
		PreparedStatement psmt = null;
		PreparedStatement psmtForAvailableMoney = null;
		PreparedStatement psmtForLoanMoney = null;
		PreparedStatement psmtForRedBagMoney = null;
		ResultSet rs = null;

		String querysql = "select *  from  t_user where id >=? AND id <=? GROUP BY username ORDER BY id  ";
		
		Date startDate = new Date();
		log.debug("开始时间:" + DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
		int i = 0;
		try {
			psmt = oldOperator.getPreparedStatement(querysql);
			psmt.setLong(1, startId);
			psmt.setLong(2, endId);
			rs = psmt.executeQuery();
			
			//可用余额
			String availableMoneySql = "INSERT INTO `MEM_MEMBER_ACT` VALUES (?, ?, '0001', '可用余额','CNY',  ?, '1', 0, '999999', ?, '999999', ?, 0,'0');";
			psmtForAvailableMoney = nowOperator.getPreparedStatement(availableMoneySql);
			
			//借款账户
			String loanAccountSql = "INSERT INTO `MEM_MEMBER_ACT` VALUES (?, ?, '0002', '借款账户','CNY',  ?, '0', 0, '999999', ?, '999999', ?,0 ,'0');";
			psmtForLoanMoney = nowOperator.getPreparedStatement(loanAccountSql);
			
			//红包账户
			String redBagSql = "INSERT INTO `MEM_MEMBER_ACT` VALUES (?, ?, '0004', '红包账户','CNY',  '0', '0', 0, '999999', ?, '999999', ?,0 ,'0');";
			psmtForRedBagMoney = nowOperator.getPreparedStatement(redBagSql);
			
			while (rs.next()) {
				++i;
				log.debug("开始第" + i + "条数据迁移");
				boolean success = transplantAct(rs,psmtForAvailableMoney,psmtForLoanMoney,psmtForRedBagMoney);
				if (success) {
					log.debug("第" + i + "条迁移成功");
				} else {
					i--;
				}
			}
			psmt.close();
			psmtForAvailableMoney.close();
			psmtForLoanMoney.close();
			psmtForRedBagMoney.close();
			OldDBOperatorFactory.addDBOperator(oldOperator);
			NowDBOperatorFactory.addNowDBOperator(nowOperator);
			log.debug("结束,共" + i + "条数据迁移成功");
			log.debug("用时:" + DateUtils.pastMinutes(startDate) + "分钟");
		} catch (Exception e) {
			log.error(Exceptions.getStackTraceAsString(e));
		}
		return i;

	}

	private static boolean transplantAct(ResultSet rs, PreparedStatement psmtForAvailableMoney,
			PreparedStatement psmtForLoanMoney, PreparedStatement psmtForRedBagMoney) throws SQLException {
		String usernameStr = rs.getString("username");
    	if(StringUtils.isBlank(usernameStr)){
    		return false;
    	} 
    	String yxbToken = rs.getString("yxb_token");//用户唯一标识
    	if(StringUtils.isBlank(yxbToken)){
    		return false;
    	} 
    	
    	String spareMobileStrStr = rs.getString("spare_mobile");//备用手机号
    	if(StringUtils.isNotBlank(spareMobileStrStr) && spareMobileStrStr.length() == 11){
    		usernameStr = spareMobileStrStr;
    	}
    	
    	String nowDate = DateUtils.getDate("yyyy-MM-dd HH:mm:ss");
		Long oldId = rs.getLong("id");
		String remainMoneyStr = rs.getString("remain_money");//可用金额
		String redbagStr = rs.getString("redbag");//红包 原来的红包就是借款金额
		BigDecimal remainMoney = new BigDecimal(remainMoneyStr   == null ? "0" : remainMoneyStr);
		BigDecimal redbag = new BigDecimal(redbagStr == null ? "0" : redbagStr);
		
		Long keyongyueId = SnowFlake.nextId(1, 1);
        psmtForAvailableMoney.setLong(1, keyongyueId);
        psmtForAvailableMoney.setLong(2, oldId);
        psmtForAvailableMoney.setString(3, remainMoney.toString());
        psmtForAvailableMoney.setString(4, nowDate);
        psmtForAvailableMoney.setString(5, nowDate);
        psmtForAvailableMoney.executeUpdate();
//		log.debug("可用余额账户创建成功,金额:"+remainMoney.toString());
		
        Long loanAccountSqlId = SnowFlake.nextId(1, 1);
        psmtForLoanMoney.setLong(1, loanAccountSqlId);
        psmtForLoanMoney.setLong(2, oldId);
        psmtForLoanMoney.setString(3, redbag.toString());
        psmtForLoanMoney.setString(4, nowDate);
        psmtForLoanMoney.setString(5, nowDate);
        psmtForLoanMoney.executeUpdate();
//      log.debug("借款账户账户创建成功,金额:"+redbag.toString());
		
        Long redBagId = SnowFlake.nextId(1, 1);
        psmtForRedBagMoney.setLong(1, redBagId);
        psmtForRedBagMoney.setLong(2, oldId);
        psmtForRedBagMoney.setString(3, nowDate);
        psmtForRedBagMoney.setString(4, nowDate);
        psmtForRedBagMoney.executeUpdate();
//      log.debug("红包账户账户创建成功,金额:0");
		
		return true;
		
	}

}
