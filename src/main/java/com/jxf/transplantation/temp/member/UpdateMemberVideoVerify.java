package com.jxf.transplantation.temp.member;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.jxf.mem.service.impl.MemberVideoVerifyServiceImpl;
import com.jxf.svc.init.SpringContextHolder;
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.Exceptions;
import com.jxf.svc.utils.FileUploadUtils;
import com.jxf.svc.utils.FileUtils;
import com.jxf.svc.utils.StringUtils;
import com.jxf.transplantation.dbo.NowDBOperator;
import com.jxf.transplantation.dbo.NowDBOperatorFactory;

/**
 * @作者: gaobo
 * @功能说明:更新 公信堂人脸认证 身份证背面照
 */
public class UpdateMemberVideoVerify {
	
	private static Logger log = LoggerFactory.getLogger(UpdateMemberVideoVerify.class);
	
	private MemberVideoVerifyServiceImpl memberVideoVerifyServiceImpl = SpringContextHolder.getBean(MemberVideoVerifyServiceImpl.class);
	
	public int updateMemberVideoVerify() {
		NowDBOperator nowOperator = NowDBOperatorFactory.getNowDBOperator();
		PreparedStatement psmt = null;
		PreparedStatement updatePsmt = null;
		ResultSet rs = null;
		String sql = "select * from MEM_MEMBER_VIDEO_VERIFY where channel = '1' and type = '0' and status = '1' and idcard_back_photo is null";
		String updateSql = "UPDATE MEM_MEMBER_VIDEO_VERIFY set idcard_back_photo = ?, living_photo = ? where id = ?";
		
		Date startDate = new Date();
		log.debug("开始时间:"+DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
		int i = 0;
		try {
			psmt = nowOperator.getPreparedStatement(sql);
			rs = psmt.executeQuery();
			
			updatePsmt = nowOperator.getPreparedStatement(updateSql);
			while (rs.next()) {
				
				++i;
				log.debug("开始第" + i + "条数据迁移");
				 boolean success = tradeVideoVerify(rs, updatePsmt);
				if (success) {
					log.debug("第" + i + "条迁移成功");
				} else {
					i--;
				}
			}
		    psmt.close();	
		    log.debug("结束,共"+i+"条数据迁移成功");
		    log.debug("用时:"+ DateUtils.pastMinutes(startDate) + "分钟");
		} catch (SQLException e) {
			log.debug(Exceptions.getStackTraceAsString(e));			
		}
		return i;
		
	}

	private boolean tradeVideoVerify(ResultSet rs, PreparedStatement updatePsmt) throws SQLException {
		String id = rs.getString("id");
		try {
			JSONObject orderQuery = memberVideoVerifyServiceImpl.orderQuery(id);
			JSONObject data = orderQuery.getJSONObject("data");
			
			String idcardBackPhotoBase64 =  data.getString("idcard_back_photo");
			String idcardBackPhoto = "";
			if(!StringUtils.isBlank(idcardBackPhotoBase64)) {
				byte[] idcard_back_photo = Base64.decodeBase64(idcardBackPhotoBase64.getBytes());
				File idcardBackPhotoFile = new File(FileUtils.getTempDirectory(),UUID.randomUUID() + ".tmp");
				FileUtils.saveToFile(idcard_back_photo, idcardBackPhotoFile);
				idcardBackPhoto = FileUploadUtils.upload(idcardBackPhotoFile, "idcard",
						"image", "jpg");
			}else {
				log.error("用户[{}]公信堂实名认证查询数据里没有参数[idcard_back_photo]",id);
			}
			
			String livingPhotoBase64 = data.getString("living_photo");
			String livingPhoto = "";
			if(!StringUtils.isBlank(livingPhotoBase64)) {
				byte[] living_photo = Base64.decodeBase64(livingPhotoBase64.getBytes());
				File livingPhotoFile = new File(FileUtils.getTempDirectory(),UUID.randomUUID() + ".tmp");
				FileUtils.saveToFile(living_photo, livingPhotoFile);
				livingPhoto = FileUploadUtils.upload(livingPhotoFile, "idcard",
						"image", "jpg");
			}else {
				log.error("用户[{}]公信堂实名认证查询数据里没有参数[living_photo]",id);
			}
			updatePsmt.setString(1, idcardBackPhoto);
			updatePsmt.setString(2, livingPhoto);
			updatePsmt.setString(3, id);
			updatePsmt.executeUpdate();
		} catch (Exception e) {
			Exceptions.getStackTraceAsString(e);
		}
		
		
		return true;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
