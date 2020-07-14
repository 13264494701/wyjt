package com.jxf.transplantation.temp.member;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.Exceptions;
import com.jxf.svc.utils.SnowFlake;
import com.jxf.svc.utils.StringUtils;
import com.jxf.transplantation.dbo.OldDBOperator;
import com.jxf.transplantation.dbo.OldDBOperatorFactory;
import com.jxf.transplantation.dbo.NowDBOperator;
import com.jxf.transplantation.dbo.NowDBOperatorFactory;

/**
 * @作者: xiaorongdian
 * @创建时间 :2018年11月19日 上午9:38:09
 * @功能说明:会员银行卡 和 视频认证 的数据迁移
 */
public class ImportMemberCardUtils {

	private static Logger log = LoggerFactory.getLogger(ImportMemberCardUtils.class);
	
	public static void main(String[] args) {
    	importCard(499000L,500000L);
    }
	
	public static int importCard(Long startId,Long endId) {
		OldDBOperator oldOperator = OldDBOperatorFactory.getDBOperator();
		NowDBOperator nowOperator = NowDBOperatorFactory.getNowDBOperator();
		PreparedStatement psmt = null;
		PreparedStatement psmtCard = null;
		PreparedStatement psmtBank = null;
		ResultSet rs = null;
		String countsql = "SELECT * FROM t_user_bank_info_v2  WHERE  id >=? AND id <=?  ORDER BY id";
		
		Date startDate = new Date();
		log.debug("开始时间:"+DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
		int i = 0;
		try {
			psmt = oldOperator.getPreparedStatement(countsql);
			psmt.setLong(1, startId);
			psmt.setLong(2, endId);
			rs = psmt.executeQuery();
			
			//          1        2           3                         4           5                         6            7                         8                         9             10
			String insertMemberCardSql ="INSERT INTO `MEM_MEMBER_CARD` (`id`, `member_id`, `card_no`, `card_type`, `bank_id`, `bank_name`, `bank_logo`, `protocol_num`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES (?, ?, ?, '0', ?, ?, '/wyjt/upload/icon/CITIC.png', ?, ?, '999999', ?, '999999', ?, ?);";
			psmtCard = nowOperator.getPreparedStatement(insertMemberCardSql);
			
			String querySql = "select id  from  NFS_BANK_INFO where name like ('%' ? '%')"; 
			psmtBank = nowOperator.getPreparedStatement(querySql);
			
			while (rs.next()) {
				++i;
				log.debug("开始第"+i+"条数据迁移");
				boolean updateMember = transplantMemberCard(rs,psmtCard,psmtBank);
				if(updateMember){
					log.debug("第"+i+"条迁移成功");
				}else{
					i--;
				}
			}
		    psmt.close();	
		    psmtCard.close();
		    psmtBank.close();
		    log.debug("结束,共"+i+"条数据迁移成功");
		    log.debug("用时:"+ DateUtils.pastMinutes(startDate) + "分钟");
		} catch (SQLException e) {
			log.debug(Exceptions.getStackTraceAsString(e));			
		}
		return i;
		
	}

	private static boolean transplantMemberCard(ResultSet rs, PreparedStatement psmtCard, PreparedStatement psmtBank) throws SQLException {
		String userIdStr = rs.getString("userId");
		if(StringUtils.isBlank(userIdStr)){
			return false;
		}
		userIdStr = userIdStr.replace(" ", "");
		log.debug("userId:" + userIdStr);
		
		String bankCardNo = rs.getString("bankCardNo");
		String bankName = rs.getString("bankName");
		if(StringUtils.isBlank(bankName)) {
			return false;
		}
		Long bankId = getBankId(bankName,psmtBank);
		
		String createTime = rs.getString("createTime");
		String lastModifyTime = rs.getString("lastModifyTime");
		String delFlag = rs.getString("delFlag");//这个是 是否解绑
		

		String protocolNum = rs.getString("protocolNum");//银行卡支付协议
		Long memberCardId = SnowFlake.nextId(1, 1);
		psmtCard.setLong(1, memberCardId);
		psmtCard.setString(2, userIdStr);
		psmtCard.setString(3, bankCardNo);
		psmtCard.setLong(4, bankId);
		psmtCard.setString(5, bankName);
		psmtCard.setString(6, protocolNum);
		if(StringUtils.equals(delFlag, "0")){
			psmtCard.setLong(7, 0);
		}else{
			psmtCard.setLong(7, 1);
		}
		psmtCard.setString(8, createTime);
		if(StringUtils.isNotBlank(lastModifyTime)) {
			psmtCard.setString(9, lastModifyTime);
		}else {
			psmtCard.setString(9, createTime);
		}
		
		psmtCard.setString(10, delFlag);
		psmtCard.executeUpdate();
		
		return true;
	}


	private static Long getBankId(String bankName, PreparedStatement psmtBank) throws SQLException {
		log.debug("获取bankId开始");
		ResultSet rs = null;
		psmtBank.setString(1, bankName);
		rs = psmtBank.executeQuery();
		String binkId = "";
		while (rs.next()) {
			binkId = rs.getString("id");
		}
		log.debug("获取bankId结束,binkId:" + binkId);
		if(StringUtils.isNotBlank(binkId)){
			return Long.valueOf(binkId);
		}
		return 1L;
	}

}
