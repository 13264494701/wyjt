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
 * @功能说明:会员冻结账户迁移
 */
public class ImportMemberFreezenAct {
	
	private static Logger log = LoggerFactory.getLogger(ImportMemberFreezenAct.class);

	public static void main(String[] args) {
		importFreezenAct(1L, 5000L);
	}

	public static int importFreezenAct(Long startId, Long endId) {
		OldDBOperator oldOperator = OldDBOperatorFactory.getDBOperator();
		NowDBOperator nowOperator = NowDBOperatorFactory.getNowDBOperator();
		PreparedStatement psmt = null;
		PreparedStatement psmtForFreezenMoney = null;
		PreparedStatement psmtForLoan = null;
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
			
			//冻结资金
			String freezenMoneySql = "INSERT INTO `MEM_MEMBER_ACT` VALUES (?, ?, '0003','冻结账户', 'CNY',  ?, '0', 0, '999999', ?, '999999', ?,0, '0');";
			psmtForFreezenMoney = nowOperator.getPreparedStatement(freezenMoneySql);
			
			//主动放款待确认的钱
			String queryForLoanSql = "select SUM(a.money) sum from  t_prepayment a where userId = ? and  lastModifyTime is null and createFundDetailId <> -100 and cancelFundDetailId = 0 and type = 4";
			psmtForLoan = oldOperator.getPreparedStatement(queryForLoanSql);
			
			while (rs.next()) {
				++i;
				log.debug("开始第" + i + "条数据迁移");
				boolean success = transplantAct(rs,psmtForFreezenMoney,psmtForLoan);
				if (success) {
					log.debug("第" + i + "条迁移成功");
				} else {
					i--;
				}
			}
			psmt.close();
			psmtForFreezenMoney.close();
			psmtForLoan.close();
			OldDBOperatorFactory.addDBOperator(oldOperator);
			NowDBOperatorFactory.addNowDBOperator(nowOperator);
			log.debug("结束,共" + i + "条数据迁移成功");
			log.debug("用时:" + DateUtils.pastMinutes(startDate) + "分钟");
		} catch (Exception e) {
			log.error(Exceptions.getStackTraceAsString(e));
		}
		return i;

	}

	private static boolean transplantAct(ResultSet rs, PreparedStatement psmtForFreezenMoney,PreparedStatement psmtForLoan){
		try {
		
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
		String lawsuitFreezenMoneyStr = rs.getString("lawsuitFreezenMoney");//法院仲裁冻结资金
		BigDecimal lawsuitFreezenMoney = new BigDecimal(lawsuitFreezenMoneyStr == null ? "0" : lawsuitFreezenMoneyStr);
		String urgeFreezenMoneyStr = rs.getString("urgeFreezenMoney");//申请催收冻结资金
		BigDecimal urgeFreezenMoney = new BigDecimal(urgeFreezenMoneyStr == null ? "0" : urgeFreezenMoneyStr);
		String partRepayFreezenMoneyStr = rs.getString("partRepayFreezenMoney");//部分还款冻结资金
		BigDecimal partRepayFreezenMoney = new BigDecimal(partRepayFreezenMoneyStr == null ? "0" : partRepayFreezenMoneyStr);
		String freezenMoneyStr = rs.getString("freezen_money");//要求录视频的冻结资金
		BigDecimal videoFreezenMoney = new BigDecimal(freezenMoneyStr  == null ? "0" : freezenMoneyStr);
		BigDecimal freezenMoney = lawsuitFreezenMoney.add(urgeFreezenMoney).add(partRepayFreezenMoney).add(videoFreezenMoney);

		//冻结资金 t_user表中冻结资金之和 + t_transfer中待对方收款的钱 + t_prepayment中主动放款的钱
		BigDecimal totalFreezenMoney = getFreezenMoney(oldId,freezenMoney,psmtForLoan);

		Long freezenMoneyId = SnowFlake.nextId(1, 1);
		psmtForFreezenMoney.setLong(1, freezenMoneyId);
		psmtForFreezenMoney.setLong(2, oldId);
		psmtForFreezenMoney.setString(3, totalFreezenMoney.toString());
		psmtForFreezenMoney.setString(4, nowDate);
		psmtForFreezenMoney.setString(5, nowDate);
		psmtForFreezenMoney.executeUpdate();
		log.debug("冻结资金账户创建成功,金额:"+totalFreezenMoney.toString());
		
		return true;
		
		} catch (Exception e) {
			log.error(Exceptions.getStackTraceAsString(e));
			return false;
		}
	}

	private static BigDecimal getFreezenMoney(Long oldUserId, BigDecimal freezenMoney,
			PreparedStatement psmtForLoan) throws SQLException {
	
		ResultSet rs = null;
		//主动放款待确认的钱
		psmtForLoan.setLong(1, oldUserId);
		rs = psmtForLoan.executeQuery();
		while (rs.next()) {
			String sum = rs.getString("sum");
			BigDecimal dontLoanMoney = new BigDecimal(sum == null ? "0" : sum);
			freezenMoney = freezenMoney.add(dontLoanMoney);
		}
		return freezenMoney;
	}


}
