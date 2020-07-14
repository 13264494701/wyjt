package com.jxf.web.home;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jxf.svc.sys.version.entity.SysVersion;
import com.jxf.svc.sys.version.service.SysVersionService;


@Controller("homeController")
@RequestMapping(value ="/home")
public class HomeController extends BaseController {

	
	@Autowired
	private SysVersionService sysVersionService;
	
	@RequestMapping(value ="index")
	public String index(Model model){
		

		SysVersion	androidVersion = sysVersionService.getByType(SysVersion.Type.android);		
		SysVersion  iosVersion = sysVersionService.getByType(SysVersion.Type.ios);
	
		model.addAttribute("androidVersion", androidVersion.getUrl());
		model.addAttribute("iosVersion", iosVersion.getUrl());
		return "home/index";
	}
	
	@RequestMapping(value ="charge")
	public String charge(){
		return "home/helpcenter/h-charge";
	}
	      
	@RequestMapping(value="help_con1")
	public String help_con1(Model model, String str) {
		model.addAttribute("str", str);
		return "home/helpcenter/help-con1";
	}
	
	@RequestMapping(value="help_con1_cont")
	public String help_con1_cont(Model model, String v) {
		model.addAttribute("str", v);
		return "home/helpcenter/help-con1-cont";
	}
	
	@RequestMapping(value="intro")
	public String intro() {
		return "home/aboutUs/intro";
	}
	
	@RequestMapping(value="join")
	public String join() {
		return "home/aboutUs/join";
	}
	
	@RequestMapping(value="download")
	public String download(Model model) {
		
		SysVersion	androidVersion = sysVersionService.getByType(SysVersion.Type.android);		
		SysVersion  iosVersion = sysVersionService.getByType(SysVersion.Type.ios);
	
		model.addAttribute("androidVersion", androidVersion.getUrl());
		model.addAttribute("iosVersion", iosVersion.getUrl());
		return "home/download";
	}
	@RequestMapping(value="cooperation")
	public String cooperation() {
		return "home/aboutUs/cooperation";
	}
	@RequestMapping(value="third_pf")
	public String third_pf() {
		return "home/aboutUs/third-pf";
	}
	@RequestMapping(value="agreement")
	public String agreement() {
		return "home/aboutUs/agreement";
	}
	
	@RequestMapping(value="scan")
	public String scan(HttpServletRequest request,HttpServletResponse response,Model model) {
		
	    String iosDLUrl = "https://itunes.apple.com/cn/app/%E6%97%A0%E5%BF%A7%E5%80%9F%E6%9D%A1-%E7%94%B5%E5%AD%90%E5%80%9F%E6%9D%A1%E5%80%9F%E6%8D%AE/id1321795027?l=zh&ls=1&mt=8";
	    String agent  =  request.getHeader("user-agent");
	    if(agent == null)agent="";
	    agent = agent.toUpperCase();
	    String src_img = "";
	    if(agent.indexOf("MICROMESSENGER")!=-1){
			if(agent.indexOf("IPHONE") != -1){        	
				src_img = "https://www.51jt.com/upload/2015/8/13/yxbao_144211397464612.jpg";        	
	        }else{	
	        	src_img = "https://www.51jt.com/upload/2015/8/13/yxbao_14421139746461.jpg";        	
	        }
	    }else{
	    	String url ="http://www.51jt.com/app/";
	    	 try {
				response.sendRedirect(url);
			} catch (IOException e) {
				e.printStackTrace();
			}
	    	
	    }
		model.addAttribute("src_img", src_img);
		return "home/scan";
	}
}
