package com.jxf.web.admin.report;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jxf.report.service.ReportWdrlDailyService;
import com.jxf.ufang.entity.UfangRchgRecord;
import com.jxf.web.admin.sys.BaseController;


/**
 * 借条后台管理系统优放统计报表Controller
 * @author wo
 * @version 2019-04-08
 */
@Controller
@RequestMapping(value = "${adminPath}/reportUfang")
public class ReportUfangController extends BaseController {

	
	/**
	 * @description 充值统计
	 * @param reportWdrlDaily
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("reportUfang:view")
	@RequestMapping(value = "rechargeList")
	public String rechargeList(UfangRchgRecord ufangRchgRecord, HttpServletRequest request, HttpServletResponse response, Model model) {
		return "admin/report/wdrl/reportWdrlDailyList";
	}
	/**
	 * @description 流量统计
	 * @param reportWdrlDaily
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("reportUfang:view")
	@RequestMapping(value = "loanDataList")
	public String loanDataList(UfangRchgRecord ufangRchgRecord, HttpServletRequest request, HttpServletResponse response, Model model) {
		return "admin/report/wdrl/reportWdrlDailyList";
	}
}