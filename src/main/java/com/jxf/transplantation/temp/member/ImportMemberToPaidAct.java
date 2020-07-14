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
 * @功能说明:会员待收 待还账户迁移
 */
public class ImportMemberToPaidAct {
	
	private static Logger log = LoggerFactory.getLogger(ImportMemberToPaidAct.class);

	public static void main(String[] args) {
		importToPaidAct(1L, 5000L);
	}

	public static int importToPaidAct(Long startId, Long endId) {
		OldDBOperator oldOperator = OldDBOperatorFactory.getDBOperator();
		NowDBOperator nowOperator = NowDBOperatorFactory.getNowDBOperator();
		PreparedStatement psmt = null;
		PreparedStatement psmtForToPaidMoneyOld = null;
		PreparedStatement psmtForToPaidMoney = null;
		PreparedStatement psmtDueInMoneyOld = null;
		PreparedStatement psmtDueInMoney = null;
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
			
			//待还金额
			String toPaidSql  = "select sum(t.remainMoney+t.remainInterest+t.compensationSum+t.partRepaySum) sum from t_new_loan t where t.borrowerId=? and t.status>=500 and t.status<600 ";
			psmtForToPaidMoneyOld = oldOperator.getPreparedStatement(toPaidSql);
			
			String toPaidInsertSql = "INSERT INTO `MEM_MEMBER_ACT` VALUES (?, ?, '0005', '待还账户','CNY',  ? , '0', 0, '999999', ?, '999999', ?, 0,'0');";
			psmtForToPaidMoney = nowOperator.getPreparedStatement(toPaidInsertSql);
			
			//待收金额
			String dueInSql = "select sum(t.remainMoney+t.remainInterest+t.compensationSum+t.partRepaySum) sum from t_new_loan t where t.lenderId=? and t.status>=500 and t.status<600 ";// 待收金额
			psmtDueInMoneyOld = oldOperator.getPreparedStatement(dueInSql);
			
			String dueInInsertSql = "INSERT INTO `MEM_MEMBER_ACT` VALUES (?, ?, '0006','待收账户', 'CNY',  ? , '0', 0, '999999', ?, '999999', ?, 0,'0');";
			psmtDueInMoney = nowOperator.getPreparedStatement(dueInInsertSql);
			
			while (rs.next()) {
				++i;
				log.debug("开始第" + i + "条数据迁移");
				boolean success = transplantAct(rs,psmtForToPaidMoneyOld,psmtForToPaidMoney,psmtDueInMoneyOld,psmtDueInMoney);
				if (success) {
					log.debug("第" + i + "条迁移成功");
				} else {
					i--;
				}
			}
			psmt.close();
			psmtForToPaidMoneyOld.close();
			psmtForToPaidMoney.close();
			psmtDueInMoneyOld.close();
			psmtDueInMoney.close();
			OldDBOperatorFactory.addDBOperator(oldOperator);
			NowDBOperatorFactory.addNowDBOperator(nowOperator);
			log.debug("结束,共" + i + "条数据迁移成功");
			log.debug("用时:" + DateUtils.pastMinutes(startDate) + "分钟");
		} catch (Exception e) {
			log.error(Exceptions.getStackTraceAsString(e));
		}
		return i;

	}

	private static boolean transplantAct(ResultSet rs, PreparedStatement psmtForToPaidMoneyOld,
			PreparedStatement psmtForToPaidMoney, PreparedStatement psmtDueInMoneyOld,
			PreparedStatement psmtDueInMoney) throws SQLException {
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
		//判断会员是否进行实名认证 如果未实名 不查待收 待还
		String nowDate = DateUtils.getDate("yyyy-MM-dd HH:mm:ss");
    	Long oldId = rs.getLong("id");
    	Long toPaidId = SnowFlake.nextId(1, 1);
    	Long dueInId = SnowFlake.nextId(1, 1);
		Integer videoId = rs.getInt("videoAuthenticationId");
		if(videoId == null || videoId == 0) {
			psmtForToPaidMoney.setLong(1, toPaidId);
			psmtForToPaidMoney.setLong(2, oldId);
			psmtForToPaidMoney.setString(3, "0");
			psmtForToPaidMoney.setString(4, nowDate);
			psmtForToPaidMoney.setString(5, nowDate);
			psmtForToPaidMoney.executeUpdate();
			
			psmtDueInMoney.setLong(1, dueInId);
			psmtDueInMoney.setLong(2, oldId);
			psmtDueInMoney.setString(3, "0");
			psmtDueInMoney.setString(4, nowDate);
			psmtDueInMoney.setString(5, nowDate);
			psmtDueInMoney.executeUpdate();
		}else {
			psmtForToPaidMoneyOld.setLong(1, oldId);
			rs= psmtForToPaidMoneyOld.executeQuery();
			String toPaidStr = null;
			while(rs.next()){
				toPaidStr = rs.getString("sum");
			}
			BigDecimal toPaid = new BigDecimal(toPaidStr == null ? "0" : toPaidStr);
			psmtForToPaidMoney.setLong(1, toPaidId);
			psmtForToPaidMoney.setLong(2, oldId);
			psmtForToPaidMoney.setString(3, toPaid.toString());
			psmtForToPaidMoney.setString(4, nowDate);
			psmtForToPaidMoney.setString(5, nowDate);
			psmtForToPaidMoney.executeUpdate();
//			log.debug("待还金额账户创建成功,金额:"+toPaid.toString());
			
			psmtDueInMoneyOld.setLong(1, oldId);
			rs= psmtDueInMoneyOld.executeQuery();
			String dueInStr = null;
			while(rs.next()){
				dueInStr = rs.getString("sum");
			}
			BigDecimal dueIn = new BigDecimal(dueInStr == null ? "0" : dueInStr);
			psmtDueInMoney.setLong(1, dueInId);
			psmtDueInMoney.setLong(2, oldId);
			psmtDueInMoney.setString(3, dueIn.toString());
			psmtDueInMoney.setString(4, nowDate);
			psmtDueInMoney.setString(5, nowDate);
			psmtDueInMoney.executeUpdate();
//			log.debug("待收金额账户创建成功,金额:"+dueIn.toString());
		}
		
		return true;
	}

}
