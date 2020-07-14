package com.jxf.web.ufang;


import java.io.File;
import java.io.IOException;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.jxf.mms.consts.MmsConstant;
import com.jxf.mms.msg.SendSmsMsgService;
import com.jxf.mms.msg.utils.VerifyCodeUtils;
import com.jxf.mms.record.entity.MmsSmsRecord;
import com.jxf.mms.record.service.MmsSmsRecordService;
import com.jxf.svc.model.JsonpRsp;
import com.jxf.svc.model.Message;
import com.jxf.svc.security.rsa.RSAService;
import com.jxf.svc.servlet.ValidateCodeServlet;
import com.jxf.svc.sys.area.entity.Area;
import com.jxf.svc.sys.area.service.AreaService;
import com.jxf.svc.sys.image.entity.Image;
import com.jxf.svc.sys.image.service.ImageService;
import com.jxf.svc.utils.CalendarUtil;
import com.jxf.svc.utils.FileUploadUtils;
import com.jxf.svc.utils.JsonpUtils;
import com.jxf.web.model.ResponseData;



/**
 * 
 * @类功能说明： 公用
 * @类修改者： 
 * @修改日期： 
 * @修改说明： 
 * @公司名称：北京金信服科技有限公司
 * @作者：Administrator 
 * @创建时间：2016年12月19日 上午10:56:53 
 * @版本：V1.0
 */
@Controller("ufangCommonController")
@RequestMapping(value="${ufangPath}/common")
public class UfangCommonController {

	@Autowired
	private RSAService rsaService;
	@Autowired
	private AreaService areaService;

	@Autowired
	private SendSmsMsgService sendSmsMsgService;
	@Autowired
	private MmsSmsRecordService mmsSmsRecordService;
    @Autowired
    private ImageService imageService;

	/**
	 * 公钥
	 */
	@RequestMapping(value = "/public_key", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, String> publicKey(HttpServletRequest request) {
		RSAPublicKey publicKey = rsaService.generateKey(request);
		Map<String, String> data = new HashMap<String, String>();
		data.put("modulus", Base64.encodeBase64String(publicKey.getModulus().toByteArray()));
		data.put("exponent", Base64.encodeBase64String(publicKey.getPublicExponent().toByteArray()));
		return data;
	}

	/**
	 * 地区
	 */
	@RequestMapping(value = "/area", method = RequestMethod.GET)
	public @ResponseBody
	ResponseData area(Long parentId) {
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		Area parent = areaService.get(parentId);
		Collection<Area> areas = parent != null ? areaService.getChildren(parent) : areaService.getRoot();
		for (Area area : areas) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("name", area.getName());
			item.put("value", area.getId());
			data.add(item);
		}
		return ResponseData.success("success",data);
	}

	/**
	 * 验证图形验证码
	 */
	@RequestMapping(value="checkImageCaptcha")
	public void checkImageCaptcha(HttpServletRequest req, HttpServletResponse response,HttpSession session) {
		JsonpRsp jsonpRsp = new JsonpRsp();
		String checkcode = req.getParameter("image_captcha");
		String code = (String)session.getAttribute(ValidateCodeServlet.VALIDATE_CODE);
		if(!checkcode.toUpperCase().equals(code)){
			jsonpRsp.setObj("验证码错误，请重新填写！");
			jsonpRsp.setErrno(-1);
		}else{
			jsonpRsp.setErrno(0);
		}	
		JsonpUtils.callback(req, response, jsonpRsp);
	}
	/**
	 * 发送短信验证码
	 */
	@ResponseBody
	@RequestMapping(value="sendSMSCaptcha")
	public Message sendSMSCaptcha(HttpServletRequest request, HttpServletResponse response,HttpSession session){
		
		String phoneNo = request.getParameter("phoneNo");

		String msgType = request.getParameter("msgType");
		//生成验证码
		String smsCode = VerifyCodeUtils.genSmsValidCode();
		//将验证码存入session中
		request.getSession().setAttribute(VerifyCodeUtils.SMS_CODE+phoneNo, smsCode);
		

		sendSmsMsgService.sendSms(msgType,phoneNo,smsCode);
		return Message.success("发送成功");
	
	}
	/**
	 * 验证短信验证码
	 */
	@RequestMapping(value="checkSMSCaptcha")
	public void checkSMSCaptcha(HttpServletRequest req, HttpServletResponse response,HttpSession session){
		
		JsonpRsp jsonpRsp = new JsonpRsp();
		String phoneNo = req.getParameter("mobile");
		String captcha = req.getParameter("captcha");
		String msgType = req.getParameter("msgType");
		MmsSmsRecord smsRecord = mmsSmsRecordService.getSendedVerifyCode(msgType,phoneNo, captcha);
		if(smsRecord == null){
			jsonpRsp.setErrno(-1);
			jsonpRsp.setObj("短信验证码错误");
		}else{
			if(CalendarUtil.addSecond(smsRecord.getSendTime(), MmsConstant.SMS_OUTTIME).before(new Date())){
				jsonpRsp.setErrno(-1);
				jsonpRsp.setObj("短信验证码过期");
			}else{
				jsonpRsp.setErrno(0);
				jsonpRsp.setObj(smsRecord.getId());
			}
		}
		JsonpUtils.callback(req, response, jsonpRsp);
	}
	

	 /**
     * 上传会员图片
     *
     * @return result
     */
    @RequestMapping("uploadMemberImage")
    @ResponseBody
    public ResponseData uploadMemberImage(MultipartHttpServletRequest multiRequest) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
	        // 获取多个文件
	        for (Iterator<String> it = multiRequest.getFileNames(); it.hasNext(); ) {
	            // 文件名
	            String key = (String) it.next();
	            // 根据key得到文件
	            MultipartFile multipartFile = multiRequest.getFile(key);
	            if (multipartFile.getSize() <= 0) {
	                continue;
	            }	            
	            String extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
				File tempFile = new File(FileUtils.getTempDirectory(), UUID.randomUUID() + ".tmp");				
				multipartFile.transferTo(tempFile);		
	            String imageUploadPath = FileUploadUtils.upload(tempFile, "/member/headImage", "image", extension);
	            result.put("image", imageUploadPath);
	        }
		} catch (IllegalStateException | IOException e) {
			
			e.printStackTrace();
		}
        return ResponseData.success("上传成功", result);
    }
    /**
     * 上传主题图片
     *
     * @return result
     */
    @RequestMapping("uploadSubjectImage")
    @ResponseBody
    public ResponseData uploadSubjectImage(MultipartHttpServletRequest multiRequest) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
	        // 获取多个文件
	        for (Iterator<String>  it = multiRequest.getFileNames(); it.hasNext(); ) {
	            // 文件名
	            String key = (String) it.next();
	            // 根据key得到文件
	            MultipartFile multipartFile = multiRequest.getFile(key);
	            if (multipartFile.getSize() <= 0) {
	                continue;
	            }	            
	            String extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
				File tempFile = new File(FileUtils.getTempDirectory(), UUID.randomUUID() + ".tmp");				
				multipartFile.transferTo(tempFile);		
	            String imageUploadPath = FileUploadUtils.upload(tempFile, "/cms/subject", "image", extension);
	            result.put("image", imageUploadPath);
	        }
		} catch (IllegalStateException | IOException e) {
			
			e.printStackTrace();
		}
        return ResponseData.success("上传成功", result);
    }
    

    
    /**
     * 上传图标
     *
     * @return result
     */
    @RequestMapping("uploadIcon")
    @ResponseBody
    public ResponseData uploadIcon(MultipartHttpServletRequest multiRequest) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
	        // 获取多个文件
	        for (Iterator<String>  it = multiRequest.getFileNames(); it.hasNext(); ) {
	            // 文件名
	            String key = (String) it.next();
	            // 根据key得到文件
	            MultipartFile multipartFile = multiRequest.getFile(key);
	            if (multipartFile.getSize() <= 0) {
	                continue;
	            }	            
	            String extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
				File tempFile = new File(FileUtils.getTempDirectory(), UUID.randomUUID() + ".tmp");				
				multipartFile.transferTo(tempFile);		
	            String imageUploadPath = FileUploadUtils.upload(tempFile, "/icon", "image", extension);
	            result.put("image", imageUploadPath);
	        }
		} catch (IllegalStateException | IOException e) {
			
			e.printStackTrace();
		}
        return ResponseData.success("上传成功", result);
    }
	   /**
  * 上传图片
  *
  * @return result
  */
 @RequestMapping("uploadArticleImage")
 @ResponseBody
 public ResponseData uploadArticleImage(MultipartHttpServletRequest multiRequest) {
     Map<String, Object> result = new HashMap<String, Object>();
     // 获取多个文件
     for (Iterator<String>  it = multiRequest.getFileNames(); it.hasNext(); ) {
         // 文件名
         String key = (String) it.next();
         // 根据key得到文件
         MultipartFile imageFile = multiRequest.getFile(key);
         if (imageFile.getSize() <= 0) {
             continue;
         }
         Image image = new Image();
         image.setFile(imageFile);
         image.setDir("/article");
         image.setAsync(false);
         imageService.generate(image);
         result.put("image", image.getImagePath());
     }
     return ResponseData.success("上传成功", result);
 }

 /**
  * 上传视频
  * @param multiRequest
  * @return
  * @throws Exception
  */
 @RequestMapping("uploadVideo")
 @ResponseBody
 public ResponseData upload(MultipartHttpServletRequest multiRequest) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
     try {
	        // 获取多个文件
	        for (Iterator<String>  it = multiRequest.getFileNames(); it.hasNext(); ) {
	            // 文件名
	            String key = (String) it.next();
	            // 根据key得到文件
	            MultipartFile multipartFile = multiRequest.getFile(key);
	            if (multipartFile.getSize() <= 0) {
	                continue;
	            }	            
	            String extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
				File tempFile = new File(FileUtils.getTempDirectory(), UUID.randomUUID() + ".tmp");				
				multipartFile.transferTo(tempFile);		
	            String videoUploadPath = FileUploadUtils.upload(tempFile, "/video", "video", extension);
	            result.put("video", videoUploadPath);
	        }
		} catch (IllegalStateException | IOException e) {
			
			e.printStackTrace();
		}
     return ResponseData.success("上传成功", result);
 }
}