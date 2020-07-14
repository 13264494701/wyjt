package com.jxf.transplantation.temp.member;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxf.mem.utils.VerifiedUtils;
import com.jxf.svc.security.PasswordUtils;
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.Exceptions;
import com.jxf.svc.utils.PingYinUtils;
import com.jxf.transplantation.dbo.OldDBOperator;
import com.jxf.transplantation.dbo.OldDBOperatorFactory;
import com.jxf.transplantation.dbo.NowDBOperator;
import com.jxf.transplantation.dbo.NowDBOperatorFactory;

/**
 * @作者: gaobo
 * @创建时间 :2018年11月19日 上午9:38:09
 * @功能说明:会员表的数据迁移
 */
public class ImportFailMemberUtils {
	
	private static Logger log = LoggerFactory.getLogger(ImportFailMemberUtils.class);

	static HashMap<String,String> map = new HashMap<String , String>();
	
	public static void main(String[] args) {
    	importMember();
    }
	

	public static int importMember() {
		OldDBOperator oldOperator = OldDBOperatorFactory.getDBOperator();
		NowDBOperator nowOperator = NowDBOperatorFactory.getNowDBOperator();
		PreparedStatement psmt = null;
		PreparedStatement psmtForCheckRepeat = null;
		PreparedStatement psmtForBankCard = null;
		PreparedStatement psmtForVerifiedList = null;
		PreparedStatement psmtForIdNo = null;
		PreparedStatement psmtForRankName = null;
		PreparedStatement psmtForEmailAndAddr = null;
		ResultSet rs = null;
	
		String querysql = "select *  from  t_user where id = '1842209'  ";	
		
	   	Date startDate = new Date();
	   	log.debug("开始时间:"+DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
	   	int i = 0;
	   	try {
	   		
			psmt = oldOperator.getPreparedStatement(querysql);
			rs = psmt.executeQuery();
			
            //会员                                              1    2         3         4             5             6      7        8      9          10     11    12    13              14       15         16         17         18       19            20            21                    22            23               24                 25
			String insertMemberSql =" INSERT INTO MEM_MEMBER ( id, username, password, pay_password, verified_list, name, nickname, id_no, head_image,gender,email,addr,rank_no,is_enabled,	is_locked,lock_key,	locked_date,register_ip,login_date,	login_failure_count,login_ip,safe_key_expire,safe_key_value,create_by,create_time,update_by,update_time,del_flag) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,999999,?,999999,?,0)";
			psmt = nowOperator.getPreparedStatement(insertMemberSql);
			
			//检查是否重复
			String queryForCheckRepeatSql = "select * from  MEM_MEMBER where username = ?" ;	
			psmtForCheckRepeat = nowOperator.getPreparedStatement(queryForCheckRepeatSql);
			
			//查银行卡号
			String queryForBankCardsql = "select count(1) as count from  t_user_bank_info_v2 where userId = ? and delFlag = 0";	
			psmtForBankCard = oldOperator.getPreparedStatement(queryForBankCardsql);
			
			//1紧急联系人   2 淘宝  3运营商 4芝麻认证 5学信网 6 社保 7公积金  9 网银 10运营商报告（新2.0）11一元信用报告
			String queryForVerifiedListSql = "SELECT a.sourceType AS sourceType FROM t_creditfile_data a WHERE a.userId = ? GROUP BY sourceType";
			psmtForVerifiedList = oldOperator.getPreparedStatement(queryForVerifiedListSql);
			
			//获取身份证号
			String queryForIdNoSql = "select idcard_no as idNo from  t_video_authentication where user_id = ? and status = 100";	
			psmtForIdNo = oldOperator.getPreparedStatement(queryForIdNoSql);

			//获取信用等级
			String queryForRankNameSql = "select creditDegree_score as balancePoints,creditDegree_level as rankName from t_credit_center_data where user_id = ?";
			psmtForRankName = oldOperator.getPreparedStatement(queryForRankNameSql);
			
			//Email和地址
			String queryForEmailAndAddrSql = "select email,addr from  t_user_info where userId = ?";	
			psmtForEmailAndAddr = oldOperator.getPreparedStatement(queryForEmailAndAddrSql);
			
			
			while (rs.next()) {
				++i;
				log.debug("开始第"+i+"条数据迁移");
				boolean success = transplantMember(rs,psmt,psmtForCheckRepeat,psmtForBankCard, 
						psmtForVerifiedList,psmtForIdNo,psmtForRankName,psmtForEmailAndAddr);
				if(success){
					log.debug("第"+i+"条迁移成功");
				}else{
					i--;
				}
			}
		    psmt.close();	
		    oldOperator.close();
		    nowOperator.close();
		    psmtForCheckRepeat.close();
			psmtForBankCard.close();
			psmtForVerifiedList.close();
			psmtForIdNo.close();
			psmtForRankName.close();
			psmtForEmailAndAddr.close();
			OldDBOperatorFactory.addDBOperator(oldOperator);
			NowDBOperatorFactory.addNowDBOperator(nowOperator);
			log.debug("结束,共"+i+"条数据迁移成功");
			log.debug("用时:"+ DateUtils.pastMinutes(startDate) + "分钟");
			return i;
		} catch (SQLException e) {
			log.error(Exceptions.getStackTraceAsString(e));	
			return i;
		}

	}
	
    private static boolean transplantMember(ResultSet rs, PreparedStatement psmt,  
    		PreparedStatement psmtForCheckRepeat,PreparedStatement psmtForBankCard, PreparedStatement psmtForVerifiedList,
    	    PreparedStatement psmtForIdNo,PreparedStatement psmtForRankName, PreparedStatement psmtForEmailAndAddr) throws SQLException {
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
    	boolean repeat = checkRepeat(usernameStr,psmtForCheckRepeat);//检查重复
    	
    	if(repeat){
    		return false;
    	}
		Long oldId = rs.getLong("id");
		String pwdStr = rs.getString("pwd");
		String realnameStr = rs.getString("realname");
		String genderStr = rs.getString("gender");
		String iconAddrStr = rs.getString("icon_addr");//头像怎么搞
		String videoAuthenticationIdStr = rs.getString("videoAuthenticationId");//为空就是没实名认证
		String userStatusStr = rs.getString("user_status");//是否为0-普通用户; 1-信用黑名单用户; 2-禁用用户
		String regTimeStr = rs.getString("reg_time");//注册时间
		String payPwdStr = rs.getString("pay_pwd");
		String readAgreementStr = rs.getString("readAgreement");//是否阅读协议标记
		String infoModfiyTimeStr = rs.getString("infoModfiyTime");//资料更新时间
		
		int gender = 0;
		if(StringUtils.equals(genderStr, "0")){//女
			gender = 1;
		}
		
		String nickname = "";
		realnameStr = realnameStr.replace(" ", "");
		if(StringUtils.isNotBlank(realnameStr)){
			nickname = PingYinUtils.getPingYin(realnameStr);
		}
		//获取认证状态
		map = getEmailAndAddr(oldId,psmtForEmailAndAddr);
		String email = map.get("email");
		int verifiedList = getVerifiedList(oldId,videoAuthenticationIdStr,usernameStr,pwdStr,payPwdStr,readAgreementStr,psmtForBankCard,
				psmtForVerifiedList,email);
		
	    psmt.setLong(1, oldId);
		
		if(StringUtils.isNotBlank(spareMobileStrStr) && spareMobileStrStr.length() == 11){
			psmt.setString(2, spareMobileStrStr);
		}else{
			psmt.setString(2, usernameStr);
		}
		
		if(StringUtils.isNotBlank(pwdStr)&&pwdStr.length()==32){
			psmt.setString(3, PasswordUtils.entryptPassword(pwdStr));
		}else {
			psmt.setString(3, pwdStr);
		}
		
		if(StringUtils.isNotBlank(payPwdStr)&&payPwdStr.length()==32){
			psmt.setString(4, PasswordUtils.entryptPassword(payPwdStr));
		}else {
			psmt.setString(4, payPwdStr);
		}
		
		psmt.setInt(5, verifiedList);
		psmt.setString(6, realnameStr);
		psmt.setString(7, nickname);
		
		//获取身份证号
		String idNo = getIdNo(oldId,psmtForIdNo);
		psmt.setString(8, idNo);
		psmt.setString(9, iconAddrStr);
		psmt.setInt(10, gender);
		psmt.setString(11, email);
		psmt.setString(12, map.get("addr"));
		
		Map<String,String> m = getCreditDegreeLevel(oldId,psmtForRankName);
		psmt.setString(13, m.get("rankName"));
		
		if(StringUtils.equals(userStatusStr, "1") || StringUtils.equals(userStatusStr, "2")){
			psmt.setInt(14, 0);
		}else{
			psmt.setInt(14, 1);
		}
		
		psmt.setInt(15, 0);
		psmt.setString(16, "");
		psmt.setString(17, null);
		psmt.setString(18, "0.0.0");
		psmt.setString(19, null);
		psmt.setInt(20, 0);
		psmt.setString(21, "0.0.0");
		psmt.setString(22, null);
		psmt.setString(23, "");
		String nowDate = DateUtils.getDate("yyyy-MM-dd HH:mm:ss");
		psmt.setString(24, regTimeStr == null ? nowDate : regTimeStr);
		psmt.setString(25, infoModfiyTimeStr == null ? nowDate : infoModfiyTimeStr);
		psmt.executeUpdate();
		return true;
	}
    
	private static boolean checkRepeat(String usernameStr, PreparedStatement psmtForCheckRepeat) throws SQLException {
//		log.debug("检查重复开始");
		ResultSet rs = null;
		psmtForCheckRepeat.setString(1, usernameStr);
		rs = psmtForCheckRepeat.executeQuery();
		
		boolean repect = false;
		while (rs.next()) {
			repect = true;
		}
//		log.debug("检查重复结束:是否重复:" + repect);
		return repect;
	}

	private static HashMap<String, String> getEmailAndAddr(Long oldId, PreparedStatement psmtForEmailAndAddr) throws SQLException {
//		log.debug("获取Email和常用地址开始");
		ResultSet rs = null;
		psmtForEmailAndAddr.setLong(1, oldId);
		rs = psmtForEmailAndAddr.executeQuery();
		map.clear();
		while (rs.next()) {
			String email = rs.getString("email");
			String addr = rs.getString("addr");
			map.put("email", email);
			map.put("addr", addr);
		}
//		log.debug("获取Email和常用地址结束,email:" + map.get("email") + ";常用地址:" +map.get("addr"));
		return map;
	}
	
	//获取idNo
	private static String getIdNo(Long oldId, PreparedStatement psmtForIdNo) throws SQLException {
//		log.debug("获取身份证开始");
		ResultSet rs = null;
		psmtForIdNo.setLong(1, oldId);
		rs = psmtForIdNo.executeQuery();
		String idNo = "";
		while (rs.next()) {
			idNo = rs.getString("idNo");
		}
//		log.debug("获取身份证结束,身份证:"+idNo);
		return idNo;
	}

	//获取信用等级
	private static Map<String, String> getCreditDegreeLevel(Long oldId, PreparedStatement psmtForRankName) throws SQLException {
		ResultSet rs = null;
		psmtForRankName.setLong(1, oldId);
		rs = psmtForRankName.executeQuery();
		String rankName = "";
		String balancePoints = "";
		Map<String, String> m = new HashMap<>();
		while (rs.next()) {
			rankName = rs.getString("rankName");
			balancePoints = rs.getString("balancePoints");
		}
		m.put("rankName",StringUtils.isNotBlank(rankName)?rankName:"A");
		m.put("balancePoints",StringUtils.isNotBlank(balancePoints)?balancePoints:"0");
//		log.debug("获取信用等级成功："+rankName+"积分："+balancePoints);
		return m;
	}
	
	//获取认证List
	private static int getVerifiedList(Long oldId, String videoAuthenticationIdStr, String usernameStr, String pwdStr, String payPwdStr, 
			String readAgreementStr, PreparedStatement psmtForBankCard, PreparedStatement psmtForVerifiedList, String email
			) throws SQLException {
//		log.debug("实名认证开始");
		int verifiedList = 0;
		//实名认证
		if(StringUtils.isNotBlank(videoAuthenticationIdStr) && Integer.parseInt(videoAuthenticationIdStr) > 0){
			verifiedList = VerifiedUtils.addVerified(verifiedList, 1);
			verifiedList = VerifiedUtils.addVerified(verifiedList, 2);
		}  
	
		//手机
		if(StringUtils.isNotBlank(usernameStr)){
			verifiedList = VerifiedUtils.addVerified(verifiedList, 0);
		}
		//银行卡
		ResultSet rs = null;
		psmtForBankCard.setLong(1, oldId);
		rs = psmtForBankCard.executeQuery();
		while (rs.next()) {
			String count = rs.getString("count");
			if(Integer.parseInt(count)>0){//有绑定银行卡
				verifiedList = VerifiedUtils.addVerified(verifiedList, 3);
			}
		}
		
		
		//1紧急联系人   2 淘宝  3运营商 4芝麻认证 5学信网 6 社保 7公积金  9 网银 10运营商报告（新2.0）11一元信用报告
		psmtForVerifiedList.setLong(1, oldId);
		rs = psmtForVerifiedList.executeQuery();
		while (rs.next()) {
			String sourceType = rs.getString("sourceType");
			if(StringUtils.equals(sourceType, "1")){
				verifiedList = VerifiedUtils.addVerified(verifiedList, 24);
			}else if(StringUtils.equals(sourceType, "2")){
				verifiedList = VerifiedUtils.addVerified(verifiedList, 5);
			}else if(StringUtils.equals(sourceType, "3")){
				verifiedList = VerifiedUtils.addVerified(verifiedList, 6);
			}else if(StringUtils.equals(sourceType, "4")){
				verifiedList = VerifiedUtils.addVerified(verifiedList, 4);
			}else if(StringUtils.equals(sourceType, "5")){
				verifiedList = VerifiedUtils.addVerified(verifiedList, 8);
			}else if(StringUtils.equals(sourceType, "6")){
				verifiedList = VerifiedUtils.addVerified(verifiedList, 9);
			}else if(StringUtils.equals(sourceType, "7")){
				verifiedList = VerifiedUtils.addVerified(verifiedList, 10);
			}else if(StringUtils.equals(sourceType, "9")){
				verifiedList = VerifiedUtils.addVerified(verifiedList, 7);
			}else if(StringUtils.equals(sourceType, "10")){
				verifiedList = VerifiedUtils.addVerified(verifiedList, 6);
			}
		}
		
		
		//已设置登录密码
		if(StringUtils.isNotBlank(pwdStr)&&pwdStr.length()==32){
			verifiedList = VerifiedUtils.addVerified(verifiedList, 21);
		}
		
		//已设置支付密码
		if(StringUtils.isNotBlank(payPwdStr)&&payPwdStr.length()==32){
			verifiedList = VerifiedUtils.addVerified(verifiedList, 22);
		}
		
		//已设置邮箱地址
		if(StringUtils.isNotBlank(email)){
			verifiedList = VerifiedUtils.addVerified(verifiedList, 23);
		}
		
		//是否阅读协议
		if(Integer.parseInt(readAgreementStr) == 1){
			verifiedList = VerifiedUtils.addVerified(verifiedList, 26);
		}
//		log.debug("实名认证结束,verifiedList = "+verifiedList);
		return verifiedList;
	}
	

}
