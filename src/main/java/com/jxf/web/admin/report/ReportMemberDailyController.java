package com.jxf.web.admin.report;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.web.admin.sys.BaseController;
import com.jxf.report.entity.ReportMemberDaily;
import com.jxf.report.service.ReportMemberDailyService;

/**
 * 用户统计Controller
 *
 * @author Administrator
 * @version 2018-09-06
 */
@Controller
@RequestMapping(value = "${adminPath}/reportMemberDaily")
public class ReportMemberDailyController extends BaseController {

    @Autowired
    private ReportMemberDailyService reportMemberDailyService;

    @ModelAttribute
    public ReportMemberDaily get(@RequestParam(required = false) Long id) {
        ReportMemberDaily entity = null;
        if (id!=null) {
            entity = reportMemberDailyService.get(id);
        }
        if (entity == null) {
            entity = new ReportMemberDaily();
        }
        return entity;
    }

    @RequiresPermissions("reportMemberDaily:view")
    @RequestMapping(value = {"list", ""})
    public String list(ReportMemberDaily reportMemberDaily, HttpServletRequest request, HttpServletResponse response, Model model) {

        Page<ReportMemberDaily> page = reportMemberDailyService.findPage(new Page<ReportMemberDaily>(request, response), reportMemberDaily);
        model.addAttribute("page", page);
        return "admin/report/member/reportMemberDailyList";
    }

    /**
     * 添加页面跳转
     */
    @RequiresPermissions("member:reportMemberDaily:view")
    @RequestMapping(value = "add")
    public String add(ReportMemberDaily reportMemberDaily, Model model) {
        model.addAttribute("reportMemberDaily", reportMemberDaily);
        return "admin/report/member/reportMemberDailyAdd";
    }

    /**
     * 查看页面跳转
     */
    @RequiresPermissions("member:reportMemberDaily:view")
    @RequestMapping(value = "query")
    public String query(ReportMemberDaily reportMemberDaily, Model model) {
        model.addAttribute("reportMemberDaily", reportMemberDaily);
        return "admin/report/member/reportMemberDailyQuery";
    }

    /**
     * 修改页面跳转
     */
    @RequiresPermissions("member:reportMemberDaily:view")
    @RequestMapping(value = "update")
    public String update(ReportMemberDaily reportMemberDaily, Model model) {
        model.addAttribute("reportMemberDaily", reportMemberDaily);
        return "admin/report/member/reportMemberDailyUpdate";
    }

    /**
     * 新增与修改的提交
     */
    @RequiresPermissions("member:reportMemberDaily:edit")
    @RequestMapping(value = "save")
    public String save(ReportMemberDaily reportMemberDaily, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, reportMemberDaily)) {
            return add(reportMemberDaily, model);
        }
        reportMemberDailyService.save(reportMemberDaily);
        addMessage(redirectAttributes, "保存用户统计成功");
        return "redirect:" + Global.getAdminPath() + "/reportMemberDaily/?repage";
    }

    /**
     * 真删除提交
     */
    @RequiresPermissions("member:reportMemberDaily:edit")
    @RequestMapping(value = "delete")
    public String delete(ReportMemberDaily reportMemberDaily, RedirectAttributes redirectAttributes) {
        reportMemberDailyService.delete(reportMemberDaily);
        addMessage(redirectAttributes, "删除用户统计成功");
        return "redirect:" + Global.getAdminPath() + "/reportMemberDaily/?repage";
    }

}