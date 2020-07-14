package com.jxf.mem.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.jxf.mem.dao.MemberVideoVerifyDao;
import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberResetPayPwd;
import com.jxf.mem.entity.MemberVideoVerify;
import com.jxf.mem.entity.MemberVideoVerify.Status;
import com.jxf.mem.service.MemberResetPayPwdService;
import com.jxf.mem.service.MemberService;
import com.jxf.mem.service.MemberVideoVerifyService;
import com.jxf.mem.utils.VerifiedUtils;
import com.jxf.mem.utils.YouDunEncryptUtils;
import com.jxf.mms.msg.SendSmsMsgService;
import com.jxf.nfs.entity.NfsTransferRecord;
import com.jxf.nfs.service.NfsTransferRecordService;
import com.jxf.svc.cache.RedisUtils;
import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.security.MD5Utils;
import com.jxf.svc.security.PasswordUtils;
import com.jxf.svc.sys.area.entity.Area;
import com.jxf.svc.sys.area.service.AreaService;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.EncryptUtils;
import com.jxf.svc.utils.EncryptUtils.Type;
import com.jxf.svc.utils.Exceptions;
import com.jxf.svc.utils.FileUploadUtils;
import com.jxf.svc.utils.FileUtils;
import com.jxf.svc.utils.IdCardUtils;
import com.jxf.svc.utils.PingYinUtils;
import com.jxf.svc.utils.StringUtils;
import com.jxf.web.model.wyjt.app.member.VideoVerifyResponseResult;
/**
 * 视频认证ServiceImpl
 * @author XIAORONGDIAN
 * @version 2018-10-10
 */
@Service("memberVideoVerifyService")
@Transactional(readOnly = true)
public class MemberVideoVerifyServiceImpl extends CrudServiceImpl<MemberVideoVerifyDao, MemberVideoVerify> implements MemberVideoVerifyService{
   
	private static char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
	
    //商户开户的 pub_key
	static final String PUB_KEY = Global.getConfig("ud.pub_key");

	//商户开户的security_key
	static final String SECURITY_KEY = Global.getConfig("ud.security_key");
	
	//订单查询接口地址
    static final String Order_Query = "https://idsafe-auth.udcredit.com/front/4.3/api/order_query/pub_key/" + PUB_KEY;
	
	static final String CHARSET_UTF_8 = "UTF-8";
	
	static final String ENCRYPT_KEY = "4c43a8be85b64563a32244db9caf8454";
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private MemberResetPayPwdService resetPayPwdService;
	
	@Autowired
	private MemberVideoVerifyDao videoVerifyDao;
	@Autowired
	private NfsTransferRecordService transferRecordService;
	@Autowired
    private SendSmsMsgService sendSmsMsgService;
	@Autowired 
	private AreaService areaService;
	
	@Resource(name = "taskExecutor")
	private TaskExecutor taskExecutor;

	@Override
	public MemberVideoVerify get(Long id) {
		return super.get(id);
	}
	
	@Override
	public List<MemberVideoVerify> findList(MemberVideoVerify memberVideoVerify) {
		return super.findList(memberVideoVerify);
	}
	
	@Override
	public Page<MemberVideoVerify> findPage(Page<MemberVideoVerify> page, MemberVideoVerify memberVideoVerify) {
		return super.findPage(page, memberVideoVerify);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void save(MemberVideoVerify memberVideoVerify) {
		super.save(memberVideoVerify);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void delete(MemberVideoVerify memberVideoVerify) {
		super.delete(memberVideoVerify);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void clear(MemberVideoVerify videoVerify) {

		
	}
	@Override
	public Long getMemberIdByIdcard(String idNo) {
		return videoVerifyDao.getMemberIdByIdcard(idNo);
	}
	@Override
	public Integer checkIdcardNo(String id_number) {
		return videoVerifyDao.checkIdcardNo(id_number);
	}

	@Override
	public MemberVideoVerify getMemberVideoVerifyByMemberId(Long memberId) {
		return videoVerifyDao.getMemberVideoVerifyByMemberId(memberId);
	}

	@Override
	public MemberVideoVerify getByTrxId(Long id) {
		
		return videoVerifyDao.getByTrxId(id);
	}
	
	@Override
	public Integer countFailure(Long memberId) {
		return videoVerifyDao.countFailure(memberId);
	}
	
	@Override
	@Transactional(readOnly = false)
	public Map<String,String> notifyProcess(HttpServletRequest request){
		/** 有盾要求返回格式
		 *  code： 0-接收失败； 1-接收成功；
		 *	message 如：签名错误，接收失败
		 *	注意：返回其他code，或非json格式都认为是通知失败，系统将重新推送
		 */
		Map<String, String> result = new HashMap<String,String>();
		JSONObject reqObject = getRequestJson(request);
		if( reqObject == null ){
			result.put("code", "0");
			result.put("message", "视频认证回调接收失败");
			logger.error("message", "有盾视频认证回调接收失败");
			return result;
		}
//		logger.debug("有盾视频认证回调返回:{}",reqObject.toJSONString());
		//验签
		String sign = reqObject.getString("sign");
		String sign_time = reqObject.getString("sign_time");
		String order_id = reqObject.getString("partner_order_id");
		String signMD5 = getMD5Sign(PUB_KEY, order_id, sign_time, SECURITY_KEY);
		if(!StringUtils.equalsIgnoreCase(sign, signMD5)){
			result.put("code", "0");
			result.put("message", "视频认证回调签名错误");
			logger.error("message", "有盾视频认证回调签名错误");
			return result;
		}else{
			identityAuthentication(reqObject);
		}
		result.put("code", "1");
		result.put("message", "视频认证回调接收成功");
		return result;
	}
	/**
	 * 	生成MD5签名
	 */
	@Override
	public String getMD5Sign(String pub_key, String partner_order_id, String sign_time, String security_key) {
		String signStr = String.format("pub_key=%s|partner_order_id=%s|sign_time=%s|security_key=%s", pub_key,
				partner_order_id, sign_time, security_key);
		return MD5Utils.EncoderByMd5(signStr);
	}


	public void identityAuthentication(JSONObject reqObject){
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				taskExecutor.execute(new Runnable() {
					@Override
					public void run() {
						try{
							
						//商户订单号
					    String order_id = reqObject.getString("partner_order_id");				
						Long orderId = Long.valueOf(order_id);

						MemberVideoVerify videoVerify = get(orderId);
						Member member = memberService.get(videoVerify.getMember());				
						if (videoVerify.getStatus().equals(MemberVideoVerify.Status.verified)) {
							return;
						}
						if(videoVerify.getType().equals(MemberVideoVerify.Type.realIdentity)){//实名认证
							String name = reqObject.getString("id_name"); //姓名
							String idNo = reqObject.getString("id_number"); //身份证号	
							
							String gender = reqObject.getString("gender"); //性别
							String nation = reqObject.getString("nation"); //民族
							//String age = object.getString("age"); //年龄
							//String birthday = object.getString("birthday"); //生日
							String address = reqObject.getString("address");
							String verify_status = reqObject.getString("verify_status"); //身份验证结果
							String auth_result = reqObject.getString("auth_result"); //人像比对结果
							String fail_reason = reqObject.getString("fail_reason"); //失败原因
							boolean imgSaveSuccess = true;
							//正面
							byte[] idcard_front_photo = Base64.decodeBase64(reqObject.getString("idcard_front_photo").getBytes());
							File idcardFrontPhotoFile = new File(FileUtils.getTempDirectory(), UUID.randomUUID() + ".tmp");	
							FileUtils.saveToFile(idcard_front_photo,idcardFrontPhotoFile);
							String idcardFrontPhoto = FileUploadUtils.upload(idcardFrontPhotoFile, "idcard", "image", "jpg");
							if (StringUtils.isBlank(idcardFrontPhoto)) {
								imgSaveSuccess = false;
							}
							//头像
							byte[] idcard_portrait_photo = Base64.decodeBase64(reqObject.getString("idcard_portrait_photo").getBytes());
							File idcardPortraitPhotoFile = new File(FileUtils.getTempDirectory(), UUID.randomUUID() + ".tmp");	
							FileUtils.saveToFile(idcard_portrait_photo,idcardPortraitPhotoFile);
							String idcardPortraitPhoto = FileUploadUtils.upload(idcardPortraitPhotoFile, "idcard", "image", "jpg");
							if (StringUtils.isBlank(idcardPortraitPhoto)) {
								imgSaveSuccess = false;
							}
							//背面
							byte[] idcard_back_photo = Base64.decodeBase64(reqObject.getString("idcard_back_photo").getBytes());
							File idcardBackPhotoFile = new File(FileUtils.getTempDirectory(), UUID.randomUUID() + ".tmp");	
							FileUtils.saveToFile(idcard_back_photo,idcardBackPhotoFile);
							String idcardBackPhoto = FileUploadUtils.upload(idcardBackPhotoFile, "idcard", "image", "jpg");
							if (StringUtils.isBlank(idcardBackPhoto)) {
								imgSaveSuccess = false;
							}
							//活体照片
							byte[] living_photo = Base64.decodeBase64(reqObject.getString("living_photo").getBytes());
							File livingPhotoFile = new File(FileUtils.getTempDirectory(), UUID.randomUUID() + ".tmp");	
							FileUtils.saveToFile(living_photo,livingPhotoFile);
							String livingPhoto = FileUploadUtils.upload(livingPhotoFile, "idcard", "image", "jpg");
							if (StringUtils.isBlank(livingPhoto)) {
								imgSaveSuccess = false;
							}
							if (!imgSaveSuccess) {
								videoVerify.setRmk("图像保存失败。");
							}
							//人脸验证视频下载地址(7天内有效)
							videoVerify.setRealName(name);
							videoVerify.setIdNo(idNo);
							videoVerify.setIdcardFrontPhoto(idcardFrontPhoto);
							videoVerify.setIdcardPortraitPhoto(idcardPortraitPhoto);
							videoVerify.setIdcardBackPhoto(idcardBackPhoto);
							videoVerify.setLivingPhoto(livingPhoto);
							videoVerify.setVideoUrl(reqObject.getString("living_video"));
							if (StringUtils.isNotBlank(nation)) {
								videoVerify.setNation(nation);
							}
							if (StringUtils.isNotBlank(address)) {
								videoVerify.setAddress(address);
							}					
							if (verify_status.equals("1") && auth_result.equals("T")) {	
								videoVerify.setStatus(MemberVideoVerify.Status.verified);
								videoVerify.setFailReason("认证通过。");
								member.setIdNo(idNo);
								member.setName(name);
								member.setNickname(PingYinUtils.getPingYin(name));
								member.setGender(StringUtils.equals(gender, "女")?Member.Gender.female:Member.Gender.male);
								Area area = areaService.getCityByIDCardAddress(address);
								if(area!=null) {
									member.setArea(area);
								}else {
									logger.error("会员{}实名认证返回地址{},数据库没有对应的市区县",member.getId(),address);
								}
								String realIdentityStatus = "1";
					            
								String idCard = idNo.length() == 15 ? IdCardUtils.updateIdCard15To18(idNo):idNo;
								Date birthDay = new SimpleDateFormat("yyyyMMdd").parse(idCard.substring(6, 14));
								if (IdCardUtils.getAge(birthDay) < 18) {
									videoVerify.setRmk("年龄未满18周岁。");
									videoVerify.setStatus(MemberVideoVerify.Status.failure);
									videoVerify.setFailReason("年龄未满18周岁。");
									realIdentityStatus = "0";
								} 
								
								Integer verifiedList = member.getVerifiedList();
						        if (VerifiedUtils.isVerified(verifiedList, 1)&&VerifiedUtils.isVerified(verifiedList, 2)) {
									videoVerify.setRmk("身份认证已通过，重复认证。");
									videoVerify.setStatus(MemberVideoVerify.Status.failure);
									videoVerify.setFailReason("身份认证已通过，重复认证。");
									realIdentityStatus = "0";
								} 
						        
						        Member other = memberService.getByIdNo(idNo);	
					            if (other!=null&&(!other.equals(member))) {
									videoVerify.setRmk("您的身份证号曾被其他用户使用旧版实名认证,memberId：" + other.getId());
									videoVerify.setStatus(MemberVideoVerify.Status.failure);
									videoVerify.setFailReason("该身份证已被"+ EncryptUtils.encryptString(other.getUsername(), Type.PHONE) +"用户认证，您可使用该账号直接登录，如非本人账号，请联系客服处理");
									realIdentityStatus = "0";
								}
						        
		                        if(StringUtils.equals(realIdentityStatus, "1")) {
		        					verifiedList = VerifiedUtils.addVerified(verifiedList, 1);
		    						verifiedList = VerifiedUtils.addVerified(verifiedList, 2);
		    						member.setVerifiedList(verifiedList);	
		    						RedisUtils.put("memberInfo"+member.getId(), "realIdentityStatus", realIdentityStatus);
		    						RedisUtils.put("memberInfo"+member.getId(), "name", name);
		    						RedisUtils.put("memberInfo"+member.getId(), "idNo", idNo);
		    						memberService.save(member);
		                        }
							} else {
								//未通过
								videoVerify.setStatus(MemberVideoVerify.Status.failure);
								videoVerify.setFailReason("认证失败，" + fail_reason);							
							}					
							
						}else if(videoVerify.getType().equals(MemberVideoVerify.Type.setPayPwd)){//修改支付密码

							MemberResetPayPwd memberResetPayPwd = resetPayPwdService.get(videoVerify.getTrxId());
							String verify_status = reqObject.getString("verify_status"); //身份验证结果
							String auth_result = reqObject.getString("auth_result"); //人像比对结果
							String fail_reason = reqObject.getString("fail_reason"); //失败原因
							if(verify_status.equals("1") && auth_result.equals("T")){
								boolean imgSaveSuccess = true;
								//活体照片
								byte[] living_photo = Base64.decodeBase64(reqObject.getString("living_photo").getBytes());
								File livingPhotoFile = new File(FileUtils.getTempDirectory(), UUID.randomUUID() + ".tmp");	
								FileUtils.saveToFile(living_photo,livingPhotoFile);
								String livingPhoto = FileUploadUtils.upload(livingPhotoFile, "idcard", "image", "jpg");
								if (StringUtils.isBlank(livingPhoto)) {
									imgSaveSuccess = false;
								}
								if (!imgSaveSuccess) {
									videoVerify.setRmk("图像保存失败。");
								}
								String name = reqObject.getString("id_name"); //姓名
								videoVerify.setRealName(name);
								videoVerify.setLivingPhoto(livingPhoto);
								videoVerify.setStatus(MemberVideoVerify.Status.verified);
								videoVerify.setFailReason("认证通过。");
								
							}else{
								memberResetPayPwd.setStatus(MemberResetPayPwd.Status.failure);
								memberResetPayPwd.setFailReason(fail_reason);
								resetPayPwdService.save(memberResetPayPwd);
								
								videoVerify.setStatus(MemberVideoVerify.Status.failure);
								videoVerify.setFailReason("认证失败，" + fail_reason);
							}
							
						}else if(videoVerify.getType().equals(MemberVideoVerify.Type.transfer)){//转账
							logger.debug("转账异步开始");
							Long orgId = videoVerify.getTrxId();
							NfsTransferRecord transferRecord = transferRecordService.get(orgId);
							String verify_status = reqObject.getString("verify_status"); //身份验证结果
							String auth_result = reqObject.getString("auth_result"); //人像比对结果
							String fail_reason = reqObject.getString("fail_reason"); //失败原因
							if(StringUtils.equals(verify_status, "1")&& StringUtils.equals(auth_result, "T")){
								try {
									BigDecimal curBal = memberService.getCulBal(transferRecord.getMember());
									BigDecimal amount = transferRecord.getAmount();//转账金额
									if(curBal.compareTo(amount) < 0) {
										logger.error("转账业务ID：" + transferRecord.getId() + " 用户账户余额不足，转账失败！当前余额： curBal= " + curBal + " 转账金额： amount=" + amount);
										transferRecord.setStatus(NfsTransferRecord.Status.failure);
										transferRecord.setRmk("账户余额不足！");
										transferRecordService.save(transferRecord);
									}else {
										transferRecordService.updateActForTransfer(transferRecord);
									}
								} catch (Exception e) {
									logger.error(Exceptions.getStackTraceAsString(e));
									transferRecord.setStatus(NfsTransferRecord.Status.failure);
									transferRecord.setFailReason("扣款不成功,转账失败!");
									transferRecordService.save(transferRecord);
								}
								boolean imgSaveSuccess = true;
								//活体照片
								byte[] living_photo = Base64.decodeBase64(reqObject.getString("living_photo").getBytes());
								File livingPhotoFile = new File(FileUtils.getTempDirectory(), UUID.randomUUID() + ".tmp");	
								FileUtils.saveToFile(living_photo,livingPhotoFile);
								String livingPhoto = FileUploadUtils.upload(livingPhotoFile, "idcard", "image", "jpg");
								if (StringUtils.isBlank(livingPhoto)) {
									imgSaveSuccess = false;
								}
								if (!imgSaveSuccess) {
									videoVerify.setRmk("图像保存失败。");
								}
								String name = reqObject.getString("id_name"); //姓名
								videoVerify.setRealName(name);
								videoVerify.setLivingPhoto(livingPhoto);
								videoVerify.setStatus(MemberVideoVerify.Status.verified);
								videoVerify.setFailReason("认证通过。");
							}else{
								logger.error("转账异步失败");
								transferRecord.setStatus(NfsTransferRecord.Status.failure);
								transferRecord.setFailReason(fail_reason);
								transferRecordService.save(transferRecord);
								
								videoVerify.setStatus(MemberVideoVerify.Status.failure);
								videoVerify.setFailReason("认证失败，" + fail_reason);
							}
						}else if (videoVerify.getType().equals(MemberVideoVerify.Type.changePhone)) {//更换手机号
							String verify_status = reqObject.getString("verify_status"); //身份验证结果
							String auth_result = reqObject.getString("auth_result"); //人像比对结果
							String fail_reason = reqObject.getString("fail_reason"); //失败原因
							if(verify_status.equals("1") && auth_result.equals("T")){
								String realName = reqObject.getString("id_name");
								String idNo = reqObject.getString("id_number");

								// 活体照片
								byte[] living_photo = Base64.decodeBase64(reqObject.getString("living_photo").getBytes());
								File livingPhotoFile = new File(FileUtils.getTempDirectory(),
										UUID.randomUUID() + ".tmp");
								FileUtils.saveToFile(living_photo, livingPhotoFile);
								String livingPhoto = FileUploadUtils.upload(livingPhotoFile, "idcard", "image", "jpg");
								if (StringUtils.isBlank(livingPhoto)) {
									videoVerify.setRmk("图像保存失败。");
								}

								videoVerify.setRealName(realName);
								videoVerify.setIdNo(idNo);
								videoVerify.setLivingPhoto(livingPhoto);
								videoVerify.setStatus(MemberVideoVerify.Status.verified);
								videoVerify.setFailReason("认证通过。");
							}else {
								//未通过
								videoVerify.setStatus(MemberVideoVerify.Status.failure);
								videoVerify.setFailReason("认证失败，" + fail_reason);							
							}	
						}
						save(videoVerify);
					}catch(Exception e){
						logger.error(Exceptions.getStackTraceAsString(e));
					}	
					}
				});
			}
		});
	}
	
	
	/**
	 * 	获取json对象
	 * @param request
	 * @return
	 */
	private static JSONObject getRequestJson(HttpServletRequest request) {
		InputStream in;
		JSONObject json = null;
		try {
			in = request.getInputStream();
			byte[] b = new byte[10240];
			int len;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while ((len = in.read(b)) > 0) {
				baos.write(b, 0, len);
			}
			String bodyText = new String(baos.toByteArray(), CHARSET_UTF_8);
			json = (JSONObject) JSONObject.parse(bodyText);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return json;
	}

	@Override
	public VideoVerifyResponseResult getResult(int type, String orderId, Member member) {
		VideoVerifyResponseResult result = new VideoVerifyResponseResult();
		result.setOrderId(orderId);	
		result.setIdNo(member.getIdNo());
		String host = Global.getConfig("domain");
		String notifyUrl = host + Global.getWyjtAppPath()+"/member/videoVerify/notify";
		logger.debug("notifyUrl:"+notifyUrl);
		result.setNotifyUrl(notifyUrl);
		result.setEncryptKey(Global.getConfig("ud.pub_key"));
		result.setUsername(member.getUsername());
		return result;
	}
	
	@Override
	@Transactional(readOnly = false)
	public void dealPayPassword(Member member, String payPassword, String orderId) {
	
		MemberVideoVerify videoVerify = get(Long.valueOf(orderId));
		
		MemberResetPayPwd memberResetPayPwd = resetPayPwdService.get(videoVerify.getTrxId());
		memberResetPayPwd.setPayPwd(payPassword);
		memberResetPayPwd.setStatus(MemberResetPayPwd.Status.verified);
		resetPayPwdService.save(memberResetPayPwd);
		
		member.setVerifiedList(VerifiedUtils.addVerified(member.getVerifiedList(), 22));
		member.setPayPassword(PasswordUtils.entryptPassword(payPassword));
		memberService.save(member);
		
		sendSmsMsgService.sendMessage("changePayPasswd", member.getUsername(), null);
		RedisUtils.put("memberInfo"+member.getId(), "payPwStatus", 1);
		
	}

	@Override
	public String getGxtResult(int type, String orderId, Member member) {
		String return_url = "";
		if(type == 0){//实名认证
			return_url = Global.getConfig("domain") + "/gxt/smrz";
		}else if(type == 1) {//修改交易密码
			return_url = Global.getConfig("domain") + "/gxt/deal_pwd";
		}else if(type == 2) {//修改手机号
			return_url = Global.getConfig("domain") + "/gxt/phone";
		}
		String pub_key = Global.getConfig("ud.pub_key");
		String partner_order_id = orderId;
		
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String sign_time = sdf.format(date);
	
		String security_key = Global.getConfig("ud.security_key");
		
		String signStr = String.format("pub_key=%s|partner_order_id=%s|sign_time=%s|security_key=%s", pub_key, partner_order_id, sign_time, security_key);
		
		String sign = MD5Encrpytion(signStr);
		
		String callback_url =  Global.getConfig("domain") + Global.getConfig("gxtH5") + "/member/videoVerify/notify";
		logger.info("公信堂实名认证回调地址：{}",callback_url);
		
		String params = "pub_key=" + pub_key + "&partner_order_id=" + orderId + "&sign_time=" + sign_time +
				"&sign=" + sign + "&return_url=" + return_url + "&callback_url=" + callback_url;
		if(type != 0) {
			params = params + "&id_name=" + member.getName() +"&id_number=" + member.getIdNo();
		}
		logger.info("公信堂实名认证参数：{}",params);
		String encrypt = "";
		try {
			encrypt = YouDunEncryptUtils.aesEncrypt(params, ENCRYPT_KEY);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("混淆失败");
		}
		String url = "https://static.udcredit.com/id/v43/index.html?apiparams=" + encrypt;
		return url;
	}

	@Override
	public Map<String, String> notifyProcessForGxt(HttpServletRequest request) {
		
		String partner_order_id = request.getParameter("partner_order_id");
		String result_auth = request.getParameter("result_auth");
		String result_status = request.getParameter("result_status");
		Map<String, String> result = new HashMap<String,String>();	
		if(StringUtils.isBlank(partner_order_id)){
			result.put("code", "0");
			result.put("message", "视频认证回调接收失败");
			logger.error("有盾视频认证回调接收失败");
			return result;
		}
		JSONObject reqObject = new JSONObject();
		reqObject.put("partner_order_id", partner_order_id);
		reqObject.put("result_auth", result_auth);
		reqObject.put("result_status", result_status);
		
		identityAuthenticationGxt(reqObject);
		
		result.put("code", "1");
		result.put("message", "视频认证回调接收成功");
		return result;
	}

	private void identityAuthenticationGxt(JSONObject reqObject) {
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				taskExecutor.execute(new Runnable() {
					@Override
					public void run() {
						try {
							// 商户订单号
							String order_id = reqObject.getString("partner_order_id");
							String resultAuth = reqObject.getString("result_auth"); // 认证结果

							MemberVideoVerify videoVerify = get(Long.valueOf(order_id));

							if (videoVerify.getStatus().equals(MemberVideoVerify.Status.verified)) {
								logger.warn("{}订单已经实名认证通过",order_id);
								return;
							}
							if (!StringUtils.equals(resultAuth, "T")) {
								String result_status = reqObject.getString("result_status");
								if (StringUtils.equals(result_status, "02")) {
									videoVerify.setFailReason("系统判断为不同人");
								} else if (StringUtils.equals(result_status, "03")) {
									videoVerify.setFailReason("不能确定是否为同一人");
								} else if (StringUtils.equals(result_status, "04")) {
									videoVerify.setFailReason("系统无法比对(公安网系统无法比对)");
								} else if (StringUtils.equals(result_status, "05")) {
									videoVerify.setFailReason("库中无照片（公安库中没有网格照）");
								}else {
									videoVerify.setFailReason("认证不通过，原因未知）");
								}
								videoVerify.setStatus(Status.failure);
								save(videoVerify);
								return;
							}
							
							JSONObject orderQuery = orderQuery(order_id);
							if (videoVerify.getType().equals(MemberVideoVerify.Type.realIdentity)) {

								JSONObject data = orderQuery.getJSONObject("data");
								String name = (String) data.get("id_name");
								String idNo = (String) data.get("id_number");
								String gender = (String) data.get("gender");
								String address = (String) data.get("address");

								boolean imgSaveSuccess = true;
								// 正面
								byte[] idcard_front_photo = Base64
										.decodeBase64(data.getString("idcard_front_photo").getBytes());
								File idcardFrontPhotoFile = new File(FileUtils.getTempDirectory(),
										UUID.randomUUID() + ".tmp");
								FileUtils.saveToFile(idcard_front_photo, idcardFrontPhotoFile);
								String idcardFrontPhoto = FileUploadUtils.upload(idcardFrontPhotoFile, "idcard",
										"image", "jpg");
								if (StringUtils.isBlank(idcardFrontPhoto)) {
									imgSaveSuccess = false;
								}

								if (!imgSaveSuccess) {
									videoVerify.setRmk("图像保存失败。");
								}
								
								String idcardBackPhotoBase64 =  data.getString("idcard_back_photo");
								String idcardBackPhoto = "";
								if(!StringUtils.isBlank(idcardBackPhotoBase64)) {
									byte[] idcard_back_photo = Base64.decodeBase64(idcardBackPhotoBase64.getBytes());
									File idcardBackPhotoFile = new File(FileUtils.getTempDirectory(),UUID.randomUUID() + ".tmp");
									FileUtils.saveToFile(idcard_back_photo, idcardBackPhotoFile);
									idcardBackPhoto = FileUploadUtils.upload(idcardBackPhotoFile, "idcard",
											"image", "jpg");
									if (StringUtils.isBlank(idcardBackPhoto)) {
										imgSaveSuccess = false;
									}

									if (!imgSaveSuccess) {
										videoVerify.setRmk("身份证背面图像保存失败。");
									}
								}else {
									logger.error("用户[{}]公信堂实名认证查询数据里没有参数[idcard_back_photo]",videoVerify.getMember().getId());
								}
								
								String livingPhotoBase64 = data.getString("living_photo");
								String livingPhoto = "";
								if(!StringUtils.isBlank(livingPhotoBase64)) {
									byte[] living_photo = Base64.decodeBase64(livingPhotoBase64.getBytes());
									File livingPhotoFile = new File(FileUtils.getTempDirectory(),UUID.randomUUID() + ".tmp");
									FileUtils.saveToFile(living_photo, livingPhotoFile);
									livingPhoto = FileUploadUtils.upload(livingPhotoFile, "idcard",
											"image", "jpg");
									if (StringUtils.isBlank(livingPhoto)) {
										imgSaveSuccess = false;
									}

									if (!imgSaveSuccess) {
										videoVerify.setRmk("人脸图像保存失败。");
									}
								}else {
									logger.error("用户[{}]公信堂实名认证查询数据里没有参数[living_photo]",videoVerify.getMember().getId());
								}
								
								videoVerify.setRealName(name);
								videoVerify.setIdNo(idNo);
								videoVerify.setIdcardFrontPhoto(idcardFrontPhoto);
								videoVerify.setIdcardBackPhoto(idcardBackPhoto);
								videoVerify.setLivingPhoto(livingPhoto);
								videoVerify.setNation(data.getString("nation"));
								videoVerify.setAddress(address);
								videoVerify.setFailReason("认证通过。");
								videoVerify.setStatus(MemberVideoVerify.Status.verified);

								Member member = memberService.get(videoVerify.getMember());
								member.setIdNo(idNo);
								member.setName(name);
								member.setNickname(PingYinUtils.getPingYin(name));
								member.setGender(
										StringUtils.equals(gender, "女") ? Member.Gender.female : Member.Gender.male);
								Area area = areaService.getCityByIDCardAddress(address);
								if (area != null) {
									member.setArea(area);
								} else {
									logger.error("会员{}实名认证返回地址{},数据库没有对应的市区县", member.getId(), address);
								}
								Boolean realIdentityFlag = true;

								String idCard = idNo.length() == 15 ? IdCardUtils.updateIdCard15To18(idNo) : idNo;
								Date birthDay = new SimpleDateFormat("yyyyMMdd").parse(idCard.substring(6, 14));
								if (IdCardUtils.getAge(birthDay) < 18) {
									videoVerify.setRmk("年龄未满18周岁。");
									videoVerify.setStatus(MemberVideoVerify.Status.failure);
									videoVerify.setFailReason("年龄未满18周岁。");
									realIdentityFlag = false;
								}

								Integer verifiedList = member.getVerifiedList();
								if (VerifiedUtils.isVerified(verifiedList, 1)
										&& VerifiedUtils.isVerified(verifiedList, 2)) {
									videoVerify.setRmk("身份认证已通过，重复认证。");
									videoVerify.setStatus(MemberVideoVerify.Status.failure);
									videoVerify.setFailReason("身份认证已通过，重复认证。");
									realIdentityFlag = false;
								}

								Member other = memberService.getByIdNo(idNo);
								if (other != null && (!other.equals(member))) {
									videoVerify.setRmk("您的身份证号曾被其他用户使用旧版实名认证,memberId：" + other.getId());
									videoVerify.setStatus(MemberVideoVerify.Status.failure);
									videoVerify.setFailReason("该身份证已被"+ EncryptUtils.encryptString(other.getUsername(), Type.PHONE) +"的无忧借条用户认证，您可使用借条账户直接登录，如非本人账号，请联系客服处理");
									realIdentityFlag = false;
								}
								save(videoVerify);

								if (realIdentityFlag) {
									verifiedList = VerifiedUtils.addVerified(verifiedList, 1);
									verifiedList = VerifiedUtils.addVerified(verifiedList, 2);
									member.setVerifiedList(verifiedList);
									memberService.save(member);
									RedisUtils.put("memberInfo" + member.getId(), "realIdentityStatus", "1");
									RedisUtils.put("memberInfo" + member.getId(), "name", name);
									RedisUtils.put("memberInfo" + member.getId(), "idNo", idNo);
								}

							} else if (videoVerify.getType().equals(MemberVideoVerify.Type.setPayPwd)) {
								JSONObject data = orderQuery.getJSONObject("data");
								String realName = data.getString("id_name");
								String idNo = data.getString("id_number");

								// 活体照片
								byte[] living_photo = Base64.decodeBase64(data.getString("living_photo").getBytes());
								File livingPhotoFile = new File(FileUtils.getTempDirectory(),
										UUID.randomUUID() + ".tmp");
								FileUtils.saveToFile(living_photo, livingPhotoFile);
								String livingPhoto = FileUploadUtils.upload(livingPhotoFile, "idcard", "image", "jpg");
								if (StringUtils.isBlank(livingPhoto)) {
									videoVerify.setRmk("图像保存失败。");
								}

								videoVerify.setRealName(realName);
								videoVerify.setIdNo(idNo);
								videoVerify.setLivingPhoto(livingPhoto);
								videoVerify.setStatus(MemberVideoVerify.Status.verified);
								videoVerify.setFailReason("认证通过。");
								save(videoVerify);
							} else if (videoVerify.getType().equals(MemberVideoVerify.Type.changePhone)) {
								JSONObject data = orderQuery.getJSONObject("data");
								String realName = data.getString("id_name");
								String idNo = data.getString("id_number");

								// 活体照片
								byte[] living_photo = Base64.decodeBase64(data.getString("living_photo").getBytes());
								File livingPhotoFile = new File(FileUtils.getTempDirectory(),
										UUID.randomUUID() + ".tmp");
								FileUtils.saveToFile(living_photo, livingPhotoFile);
								String livingPhoto = FileUploadUtils.upload(livingPhotoFile, "idcard", "image", "jpg");
								if (StringUtils.isBlank(livingPhoto)) {
									videoVerify.setRmk("图像保存失败。");
								}

								videoVerify.setRealName(realName);
								videoVerify.setIdNo(idNo);
								videoVerify.setLivingPhoto(livingPhoto);
								videoVerify.setStatus(MemberVideoVerify.Status.verified);
								videoVerify.setFailReason("认证通过。");
								save(videoVerify);
							}

						} catch (Exception e) {
							logger.error(Exceptions.getStackTraceAsString(e));
						}
					}
				});
			}
		});
	}
	
	/**
     * 订单查询
     *
     * @param partner_order_id 商户唯一订单号
     */
	@Override
    public JSONObject orderQuery(String partner_order_id) throws Exception {
        JSONObject reqJson = new JSONObject();
        JSONObject header = new JSONObject();
        Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String sign_time = sdf.format(date);
        String sign = getMD5Sign(Global.getConfig("ud.pub_key"), partner_order_id, sign_time, Global.getConfig("ud.security_key"));
        header.put("partner_order_id", partner_order_id);
        header.put("sign", sign);
        header.put("sign_time", sign_time);
        reqJson.put("header", header);

        JSONObject resJson = doHttpRequest(Order_Query, reqJson);
       
        return resJson;
    }
    
    /**
     * Http请求
     */
    public static JSONObject doHttpRequest(String url, JSONObject reqJson) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        //设置传入参数
        StringEntity entity = new StringEntity(reqJson.toJSONString(),"UTF-8");
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        System.out.println(url);
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(entity);

        HttpResponse resp = client.execute(httpPost);
        if (resp.getStatusLine().getStatusCode() == 200) {
            HttpEntity he = resp.getEntity();
            String respContent = EntityUtils.toString(he, "UTF-8");
            return (JSONObject) JSONObject.parse(respContent);
        }
        return null;
    }
    public static final String MD5Encrpytion(String source) {
        try {
            byte[] strTemp = source.getBytes(Charset.forName("UTF-8"));
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; ++i) {
                byte byte0 = md[i];
                str[(k++)] = hexDigits[(byte0 >>> 4 & 0xF)];
                str[(k++)] = hexDigits[(byte0 & 0xF)];
            }
            for (int m = 0; m < str.length; ++m) {
                if ((str[m] >= 'a') && (str[m] <= 'z')) {
                    str[m] = (char) (str[m] - ' ');
                }
            }
            return new String(str);
        } catch (Exception e) {
        }
        return null;
    }

	@Override
	public int getChangePhoneNoCounts(Member member) {
		
		String beginDate = DateUtils.getYear()+"-01-01 00:00:00";
		String endDate = DateUtils.getYear()+"-12-31 23:59:59";
		
		return videoVerifyDao.getChangePhoneNoCounts(member, beginDate, endDate);
	}

	@Override
	public List<MemberVideoVerify> getLast5hRealIdentityRecords(String createTime) {
		return videoVerifyDao.getLast5hRealIdentityRecords(createTime);
	}


}