package com.jxf.transplantation.temp.message;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.ebaoquan.rop.thirdparty.org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.Exceptions;
import com.jxf.transplantation.dbo.OldDBOperator;
import com.jxf.transplantation.dbo.OldDBOperatorFactory;
import com.jxf.transplantation.dbo.NowDBOperator;
import com.jxf.transplantation.dbo.NowDBOperatorFactory;

/**
 * @作者: gaobo
 * @创建时间 :2019年1月14日
 * @功能说明:消息数据迁移
 */
public class ImportMessageUtils {

	private static Logger log = LoggerFactory.getLogger(ImportMessageUtils.class);

	public static void main(String[] args) {
		importMessage(0L, 5000L);
	}

	public static int importMessage(Long startId, Long endId) {
		OldDBOperator oldOperator = OldDBOperatorFactory.getDBOperator();
		NowDBOperator nowOperator = NowDBOperatorFactory.getNowDBOperator();
		PreparedStatement psmt = null;
		PreparedStatement psmtLoan = null;
		PreparedStatement psmtMessage = null;
		PreparedStatement psmtCmsNotice = null;
		ResultSet rs = null;
		String countsql = "select * from t_message where id >=? AND id <=? And "
				+ "type in ('loanV2','myLoan','multiplayerborrow','multiplayerlend','completemultiplayerlend','toTransferPag','authorization','FriendDetail','FriendDetailNew','authorizationNew','oneMyWEIPay','onePay','none') "
				+ "and category in ('0','1') and readFlag != '-1' and title not in ('友借款','确认收款','转账提醒','借款申请失败','借款申请失效','退款')   ORDER BY id ";
		Date startDate = new Date();
		log.debug("开始时间:" + DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
		int i = 0;
		try {
			psmt = oldOperator.getPreparedStatement(countsql);
			psmt.setLong(1, startId);
			psmt.setLong(2, endId);
			rs = psmt.executeQuery();
			
			String loanSql = "SELECT * FROM t_new_loan where id = ?";
			psmtLoan = oldOperator.getPreparedStatement(loanSql);
			
			
			String insertMessageSql = " INSERT INTO MEM_MEMBER_MESSAGE ( id," + // 1
					"member_id," + // 2
					"groups," + // 3
					"title," + // 4
					"content," + // 5
					"is_read," + // 6
					"type," + // 7
					"org_id," + // 8
					"org_type," + // 9
					"create_by,create_time,update_by,update_time,del_flag) VALUES (?,?,?,?,?,?,?,?,?,999999,?,999999,?,'0')";
			psmtMessage = nowOperator.getPreparedStatement(insertMessageSql);
			
			String insertCmsNoticeSql = " INSERT INTO CMS_NOTICE ( id," + // 1
					"title," + // 2
					"content," + // 3
					"is_pub," + // 4
					"create_by,create_time,update_by,update_time,del_flag) VALUES (?,?,?,?,999999,?,999999,?,'0')";
			psmtCmsNotice = nowOperator.getPreparedStatement(insertCmsNoticeSql);
			
			while (rs.next()) {
				++i;
				log.debug("开始第" + i + "条数据迁移");
				boolean success = true;
				if (StringUtils.equals(rs.getString("category"), "0")) {
					success = transplantMessage(rs, psmtMessage,psmtLoan);
				} else if (StringUtils.equals(rs.getString("category"), "1")) {
					success = transplantCmsNotice(rs, psmtCmsNotice);
				}
				if (success) {
					log.debug("第" + i + "条迁移成功");
				} else {
					i--;
				}
			}
			psmt.close();
			psmtLoan.close();
			psmtMessage.close();
			psmtCmsNotice.close();
			log.debug("结束,共" + i + "条数据迁移成功");
			log.debug("用时:" + DateUtils.pastMinutes(startDate) + "分钟");
		} catch (SQLException e) {
			log.error(Exceptions.getStackTraceAsString(e));
		}
		return i;

	}

	private static boolean transplantMessage(ResultSet rs, PreparedStatement psmt, PreparedStatement psmtLoan) throws SQLException {
		
		// id
		String oldId = rs.getString("id");
		// 用户id
		String user_id = rs.getString("userId");

		String title = rs.getString("title");

		String content = rs.getString("content");

		String type = rs.getString("type");

		String readFlag = rs.getString("readFlag");

		String val = rs.getString("val");
		
		String createTime = rs.getString("createTime");
		
		// 最后操作时间
		String updateTime = DateUtils.formatDateTime(new Date());
		
		psmt.setLong(1, Long.parseLong(oldId));
		psmt.setLong(2, Long.parseLong(user_id));
		psmt.setInt(3, 0);
		psmt.setString(5, content);
		psmt.setString(6, readFlag);
		psmt.setString(10, createTime);
		psmt.setString(11, updateTime);
		
		if("loanV2,myLoan".contains(type)) {
			
			psmt.setString(4, title);
			if(StringUtils.isBlank(val)) {
				return false;
			}
			if(content.contains("向您发起了借款申请，快去帮帮ta吧")) {
				psmt.setInt(7, 0); 
				psmt.setLong(8, Long.parseLong(val));
				psmt.setString(9, "1");
			}else if(content.contains("取消了借款申请")) {
				psmt.setInt(7, 1); 
				psmt.setLong(8, Long.parseLong(val));
				psmt.setString(9, "1");
			}else if(content.contains("提醒您尽快去处理借款申请啦")) {
				psmt.setInt(7, 2); 
				psmt.setLong(8, Long.parseLong(val));
				psmt.setString(9, "1");
			}else if(content.contains("提醒您尽快录制信誉视频")) {
				psmt.setInt(7, 3); 
				psmt.setLong(8, Long.parseLong(val));
				psmt.setString(9, "1");
			}else if(content.contains("请尽快确认视频完成借款")) {
				psmt.setInt(7, 4); 
				psmt.setLong(8, Long.parseLong(val));
				psmt.setString(9, "1");
			}else if(content.contains("提醒您尽快确认视频")) {
				psmt.setInt(7, 5); 
				psmt.setLong(8, Long.parseLong(val));
				psmt.setString(9, "1");
			}else if(content.contains("请重新上传进行审核")) {
				psmt.setInt(7, 6); 
				psmt.setLong(8, Long.parseLong(val));
				psmt.setString(9, "1");
			}else if(content.contains("重新录制")) {
				psmt.setInt(7, 7); 
				psmt.setLong(8, Long.parseLong(val));
				psmt.setString(9, "1");
			}else if(content.contains("审核通过您上传的视频")) {
				psmt.setInt(7, 8); 
				psmt.setLong(8, Long.parseLong(val));
				psmt.setString(9, "1");
			}else if(content.contains("您的人品真不错呢")) {
				psmt.setInt(7, 9); 
				psmt.setLong(8, Long.parseLong(val));
				psmt.setString(9, "2");
			}else if(content.contains("视频冻结资金账户！提醒对方尽快录制信誉视频")) {
				psmt.setInt(7, 10); 
				psmt.setLong(8, Long.parseLong(val));
				psmt.setString(9, "1");
			}else if(content.contains("上传信誉视频审核通过后借款金额就到账了")) {
				psmt.setInt(7, 11); 
				psmt.setLong(8, Long.parseLong(val));
				psmt.setString(9, "1");
			}else if(content.contains("拒绝了您的借款申请")) {
				psmt.setInt(7, 12); 
				psmt.setLong(8, Long.parseLong(val));
				psmt.setString(9, "1");
			}else if(content.contains("给您拨来了一笔银子，赶快去接收吧")) {
				psmt.setInt(7, 13); 
				psmt.setLong(8, Long.parseLong(val));
				psmt.setString(9, "1");
			}else if(content.contains("放款申请，对方已拒绝")) {
				psmt.setInt(7, 14); 
				psmt.setLong(8, Long.parseLong(val));
				psmt.setString(9, "1");
			}else if(content.contains("放款申请，对方已同意")) {
				psmt.setInt(7, 15); 
				psmt.setLong(8, Long.parseLong(val));
				psmt.setString(9, "2");
			}else if(content.contains("向您的借款，已全部还款")) {
				psmt.setInt(7, 16); 
				psmt.setLong(8, Long.parseLong(val));
				psmt.setString(9, "2");
			}else if(content.contains("使用其他付款方式")) {
				psmt.setInt(7, 17); 
				psmt.setLong(8, Long.parseLong(val));
				psmt.setString(9, "2");
			}else if(content.contains("确认未收到您的还款")) {
				psmt.setInt(7, 18); 
				psmt.setLong(8, Long.parseLong(val));
				psmt.setString(9, "2");
			}else if(content.contains("向您的借款申请部分还款")) {
				psmt.setInt(7, 19); 
				psmt.setLong(8, Long.parseLong(val));
				psmt.setString(9, "2");
			}else if(content.contains("已取消部分还款申请")) {
				psmt.setInt(7, 20); 
				psmt.setLong(8, Long.parseLong(val));
				psmt.setString(9, "2");
			}else if(content.contains("提醒您去查看部分还款申请啦")) {
				psmt.setInt(7, 22); 
				psmt.setLong(8, Long.parseLong(val));
				psmt.setString(9, "2");
			}else if(content.contains("已同意您的部分还款申请")) {
				val = getNextId(val,psmtLoan);
				psmt.setInt(7, 23); 
				if(StringUtils.isBlank(val)) {
					return false;
				}
				psmt.setLong(8, Long.parseLong(val));
				psmt.setString(9, "2");
			}else if(content.contains("已拒绝您的部分还款申请")) {
				psmt.setInt(7, 24); 
				psmt.setLong(8, Long.parseLong(val));
				psmt.setString(9, "2");
			}else if(content.contains("提醒您尽快去处理放款单啦")) {
				psmt.setInt(7, 25); 
				psmt.setLong(8, Long.parseLong(val));
				psmt.setString(9, "1");
			}else if(content.contains("确认已收到您的还款")) {
				psmt.setInt(7, 26); 
				psmt.setLong(8, Long.parseLong(val));
				psmt.setString(9, "2");
			}else if(content.contains("向您的借款，已部分还款")) {
				psmt.setInt(7, 27); 
				psmt.setLong(8, Long.parseLong(val));
				psmt.setString(9, "2");
			}else if(content.contains("向您的借款申请延期")) {
				psmt.setInt(7, 28); 
				psmt.setLong(8, Long.parseLong(val));
				psmt.setString(9, "2");
			}else if(content.contains("已取消延期申请")) {
				psmt.setInt(7, 29); 
				psmt.setLong(8, Long.parseLong(val));
				psmt.setString(9, "2");
			}else if(content.contains("提醒您去查看延期申请啦")) {
				psmt.setInt(7, 31); 
				psmt.setLong(8, Long.parseLong(val));
				psmt.setString(9, "2");
			}else if(content.contains("已同意您的延期申请")) {
				val = getNextId(val,psmtLoan);
				psmt.setInt(7, 32);
				if(StringUtils.isBlank(val)) {
					return false;
				}
				psmt.setLong(8, Long.parseLong(val));
				psmt.setString(9, "2");
			}else if(content.contains("已拒绝您的延期申请")) {
				psmt.setInt(7, 33); 
				psmt.setLong(8, Long.parseLong(val));
				psmt.setString(9, "2");
			}else if(content.contains("3天后到期")) {
				psmt.setInt(7, 54); 
				psmt.setLong(8, Long.parseLong(val));
				psmt.setString(9, "3");
			}else if(content.contains("今天到期")) {
				psmt.setInt(7, 55); 
				psmt.setLong(8, Long.parseLong(val));
				psmt.setString(9, "3");
			}else if(content.contains("已逾期1天")) {
				psmt.setInt(7, 56); 
				psmt.setLong(8, Long.parseLong(val));
				psmt.setString(9, "3");
			}else if(content.contains("我们已通过短信和电话")) {
				psmt.setInt(7, 57); 
				psmt.setLong(8, Long.parseLong(val));
				psmt.setString(9, "3");
			}else if(content.contains("已逾期7天")) {
				psmt.setInt(7, 58); 
				psmt.setLong(8, Long.parseLong(val));
				psmt.setString(9, "3");
			}else if(content.contains("已逾期15天")) {
				psmt.setInt(7, 59); 
				psmt.setLong(8, Long.parseLong(val));
				psmt.setString(9, "3");
			}else {
				return false;
			}
			
		}else if("multiplayerborrow,multiplayerlend,completemultiplayerlend".contains(type)) {// ok
			psmt.setString(4, title);
			if(StringUtils.equals(type, "multiplayerlend")) {
				psmt.setInt(7, 34);
				psmt.setString(9, "1");
			}else if(StringUtils.equals(type, "multiplayerborrow")) {
				psmt.setInt(7, 35);
				psmt.setString(9, "0");
			}else if(StringUtils.equals(type, "completemultiplayerlend")) {
				psmt.setInt(7, 36);
				psmt.setString(9, "3");
			}
			if(StringUtils.isBlank(val)) {
				return false;
			}else {
				psmt.setLong(8, Long.parseLong(val));
			}
		}else if(StringUtils.equals(type, "toTransferPag")) {///ok
			psmt.setString(4, title);
			psmt.setInt(7, 37);
			val = getMemberId(val);
			if(StringUtils.isBlank(val)) {
				return false;
			}else {
				psmt.setLong(8, Long.parseLong(val));
			}
			psmt.setString(9, "3");
		}else if(("authorization,FriendDetail,FriendDetailNew,authorizationNew").contains(type)) {//ok
			if(StringUtils.equals(title,"申请授权")) {
				psmt.setString(4, title);
			}else {
				psmt.setString(4, "信用报告");
			}
			psmt.setInt(7, 40); 
			if(StringUtils.isBlank(val)) {
				return false;
			}else {
				psmt.setLong(8, Long.parseLong(val));
			}
			psmt.setString(9, "3");
		}else if(("oneMyWEIPay,onePay").contains(type)) {///ok
			if(StringUtils.equals(title,"申请授权")) {
				psmt.setString(4, title);
			}else {
				psmt.setString(4, "信用报告");
			}
			if(StringUtils.equals(type, "oneMyWEIPay")) {
				psmt.setInt(7, 42); 
			}else {
				psmt.setInt(7, 43); 
			}
			if(StringUtils.isBlank(val)) {
				return false;
			}else {
				psmt.setLong(8, Long.parseLong(val));
			}
			psmt.setString(9, "3");
		}else if(StringUtils.equals(type, "none")) {//ok
			psmt.setString(4, "风险提醒");
			psmt.setInt(7, 50); 
			psmt.setLong(8, 0L);
			psmt.setString(9, "3");
		}
		
		log.debug("-----------------------------" + psmt);
		try {
			psmt.executeUpdate();
		} catch (Exception e) {
			log.error("=========出错了==========" + psmt);
			log.error(Exceptions.getStackTraceAsString(e));
		}
		return true;
	}

	public static String getNextId(String val,PreparedStatement psmtLoan) throws SQLException{
		psmtLoan.setString(1, val);
		ResultSet rs = psmtLoan.executeQuery();
		if(rs.next()) {
			int nextId = rs.getInt("nextLoanId");
			if(nextId > 0) {
				return getNextId(nextId+"",psmtLoan);
			}
			
			return rs.getString("id");
		}
		return "";
	}
	
	private static String getMemberId(String val) throws SQLException {
		OldDBOperator oldOperator = OldDBOperatorFactory.getDBOperator();
		String sql = "select * from t_user where yxb_id = ?";
		PreparedStatement psmt = oldOperator.getPreparedStatement(sql);
		psmt.setString(1,val);
		ResultSet rs = psmt.executeQuery();
		while (rs.next()) {
			val = rs.getString("id");
		}
		psmt.close();
		return val;
	}

	private static boolean transplantCmsNotice(ResultSet rs, PreparedStatement psmt) throws SQLException {
		// id
		String oldId = rs.getString("id");

		String title = rs.getString("title");

		String content = rs.getString("content");

		String createTime = rs.getString("createTime");
		
		// 最后操作时间
		String updateTime = DateUtils.formatDateTime(new Date());
		
		psmt.setLong(1, Long.parseLong(oldId));
		psmt.setString(2, title);
		psmt.setString(3, content);
		psmt.setString(4, "1");
		psmt.setString(5, createTime);
		psmt.setString(6, updateTime);
		
		log.debug("-----------------------------" + psmt);
		psmt.executeUpdate();
		return true;
	}

	
}
