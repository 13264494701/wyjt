package com.jxf.web.app.wyjt;



import java.text.MessageFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jxf.cms.entity.CmsAd;
import com.jxf.cms.entity.CmsIcon;
import com.jxf.cms.service.CmsAdService;
import com.jxf.cms.service.CmsNoticeService;
import com.jxf.cms.utils.CmsUtils;
import com.jxf.svc.config.Global;
import com.jxf.svc.sys.app.entity.AppInst;
import com.jxf.svc.sys.app.service.AppInstService;
import com.jxf.svc.utils.RandomUtils;
import com.jxf.svc.utils.StringUtils;
import com.jxf.web.minipro.BaseController;
import com.jxf.web.model.AuditAfterPage;
import com.jxf.web.model.AuditBeforePage;
import com.jxf.web.model.AuditPage;
import com.jxf.web.model.ResponseData;
import com.jxf.web.model.wyjt.app.IndexInfoResponseResult;
import com.jxf.web.model.wyjt.app.IndexInfoResponseResult.Ad;
import com.jxf.web.model.wyjt.app.IndexInfoResponseResult.ArbitrationCase;
import com.jxf.web.model.wyjt.app.IndexInfoResponseResult.Icon;
import com.jxf.web.model.wyjt.app.IndexInfoResponseResult.Marquee;


/**
 * Controller - 首页
 * 
 * @author JINXINFU
 * @version 2.0
 */
@Controller("wyjtAppIndexController")
@RequestMapping(value="${wyjtApp}/index")
public class IndexController extends BaseController {
	
	@Autowired
	private CmsNoticeService noticeService;
	@Autowired
	private AppInstService appInstService;	
	@Autowired
	private CmsAdService adService;	



	/**
	 * 首页信息
	 */
	@RequestMapping(value = "info")
	public @ResponseBody
	ResponseData info(HttpServletRequest request) {
		String deviceModel = request.getHeader("x-deviceModel");
		String deviceToken = request.getHeader("x-deviceToken");
		String appVersion = request.getHeader("x-appVersion");
		String osType = request.getHeader("x-osType");
		String osVersion = request.getHeader("x-osVersion");
		String channeId = request.getHeader("x-channeId");
		String ak = request.getHeader("x-ak");
		String pushToken = request.getHeader("x-pushToken");
		String loginIp = Global.getRemoteAddr(request);
		if(StringUtils.isBlank(deviceToken)) {
			return ResponseData.error("版本过期,请下载无忧借条新版本后使用！");
		}
		
		//logger.warn("版本{},平台{}",appVersion,pt);
		IndexInfoResponseResult result = new IndexInfoResponseResult();
		List<CmsIcon>  iconList = CmsUtils.getIconList("01");
		for(CmsIcon cmsIcon:iconList) {
			String imageUrl = Global.getConfig("domain")+cmsIcon.getImagePath();
			Icon icon = new IndexInfoResponseResult().new Icon();
			icon.setPositionNo(cmsIcon.getPositionNo());
			icon.setName(cmsIcon.getIconName());
			icon.setImage(imageUrl);
			icon.setRedirecType(cmsIcon.getRedirectType().ordinal());//内部跳转
			icon.setRedirectUrl(cmsIcon.getRedirectUrl());
			result.getIconList().add(icon);
		}
		
//		CmsNotice notice = new CmsNotice();
//		notice.setPosition(CmsNotice.Position.marquee);
//		notice.setIsPub(true);
//		Page<CmsNotice> page = noticeService.findNoticePage(notice, 1, 5);
//		for(CmsNotice cmsNotice:page.getList()) {
//			Marquee marquee = new IndexInfoResponseResult().new Marquee();
//			marquee.setTitle(cmsNotice.getTitle());
//			result.getMarqueeList().add(marquee);
//		}
		for(int i = 0;i < 10 ; i++ ) {
			Marquee marquee = new IndexInfoResponseResult().new Marquee();
			String firstSurname = RandomUtils.getChineseSurname();
			String secondSurname = RandomUtils.getChineseSurname();
			String title=MessageFormat.format("{0}*和{1}*刚刚在无忧借条完成借款",firstSurname,secondSurname);
			marquee.setTitle(title);
			result.getMarqueeList().add(marquee);
		}
		
		String adPositionNo = "0001";
		if(StringUtils.equals(appVersion, "4.10")) {
			adPositionNo = "0002";
		}
		List<CmsAd>  adList = adService.findListByPosition(adPositionNo);
		for(CmsAd cmsAd:adList) {
			String imageUrl = Global.getConfig("domain")+cmsAd.getImagePath();
			Ad ad = new IndexInfoResponseResult().new Ad();
			ad.setImage(imageUrl);
			ad.setRedirecType(cmsAd.getRedirectType().ordinal());//内部跳转
			ad.setRedirectUrl(cmsAd.getRedirectUrl());
			result.getBannerList().add(ad);
		}

		adPositionNo = "0003";
		List<CmsAd>  adPositionThreeList = adService.findListByPosition(adPositionNo);
		CmsAd adPositionThree = adPositionThreeList.get(0);
		String imageUrl = Global.getConfig("domain")+adPositionThree.getImagePath();
		Ad positionThree = new IndexInfoResponseResult().new Ad();
		positionThree.setImage(imageUrl);
		positionThree.setRedirecType(adPositionThree.getRedirectType().ordinal());
		positionThree.setRedirectUrl(adPositionThree.getRedirectUrl());
		result.setAdPositionThree(positionThree);
		
		adPositionNo = "0004";
		List<CmsAd>  adPositionFourList = adService.findListByPosition(adPositionNo);
		CmsAd adPositionFour = adPositionFourList.get(0);
		String image = Global.getConfig("domain")+adPositionFour.getImagePath();
		Ad positionFour = new IndexInfoResponseResult().new Ad();
		positionFour.setImage(image);
		positionFour.setRedirecType(adPositionFour.getRedirectType().ordinal());
		positionFour.setRedirectUrl(adPositionFour.getRedirectUrl());
		result.setAdPositionFour(positionFour);
		
	    if(StringUtils.equals(appVersion, "4.10")){
			AuditPage auditPage = new AuditBeforePage();
			result.setAuditPage(auditPage);		
			result.setShowThirdLogin("0");
			result.setShowFlag("0");
		}else {
			AuditPage auditPage = new AuditAfterPage();
			result.setAuditPage(auditPage);	
			result.setShowThirdLogin("1");
			result.setShowFlag("1");
		}
		
		//为了线上不报错 将来可以删掉 start TODO
		ArbitrationCase arbitrationCase = new IndexInfoResponseResult().new ArbitrationCase();
		arbitrationCase.setName("姚**");
		arbitrationCase.setAmount("5450元");
		arbitrationCase.setDaysOverdue(47);
		arbitrationCase.setStatus("已完成");
		result.getArbitrationCaseList().add(arbitrationCase);
		
		arbitrationCase = new IndexInfoResponseResult().new ArbitrationCase();
		arbitrationCase.setName("李**");
		arbitrationCase.setAmount("2100元");
		arbitrationCase.setDaysOverdue(4);
		arbitrationCase.setStatus("已完成");
		result.getArbitrationCaseList().add(arbitrationCase);
		
		arbitrationCase = new IndexInfoResponseResult().new ArbitrationCase();
		arbitrationCase.setName("郝**");
		arbitrationCase.setAmount("1600元");
		arbitrationCase.setDaysOverdue(12);
		arbitrationCase.setStatus("已完成");
		result.getArbitrationCaseList().add(arbitrationCase);
		
		arbitrationCase = new IndexInfoResponseResult().new ArbitrationCase();
		arbitrationCase.setName("刘**");
		arbitrationCase.setAmount("1507元");
		arbitrationCase.setDaysOverdue(7);
		arbitrationCase.setStatus("已完成");
		result.getArbitrationCaseList().add(arbitrationCase);
		//为了线上不报错 将来可以删掉 end TODO

		result.setDirectLoginFlag("1");
		
		AppInst appInst = new AppInst();
		appInst.setOsType(osType);
		appInst.setOsVersion(osVersion);
		appInst.setAppVersion(appVersion);
		appInst.setAk(ak);
		appInst.setDeviceModel(deviceModel);
		appInst.setDeviceToken(deviceToken);
		appInst.setChanneId(channeId);
		appInst.setPushToken(pushToken);
		appInst.setLoginIp(loginIp);
		appInstService.save(appInst);
		return ResponseData.success("加载成功",result);
	}

}