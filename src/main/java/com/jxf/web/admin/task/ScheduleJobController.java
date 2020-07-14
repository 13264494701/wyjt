package com.jxf.web.admin.task;

import com.jxf.svc.config.Global;
import com.jxf.svc.utils.StringUtils;
import com.jxf.task.entity.ScheduleJob;
import com.jxf.task.service.ScheduleJobService;
import com.jxf.web.admin.sys.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;


/**
 * 定时任务 controller
 * @author ty
 * @date 2015年1月14日
 */
@Controller
@RequestMapping("${adminPath}/task")
public class ScheduleJobController extends BaseController {
	
	@Autowired
	private ScheduleJobService scheduleJobService;
	
	
//	@ModelAttribute
//	public ScheduleJob get() {
//		
//		return new ScheduleJob();
//	}
	/**
	 * 获取定时任务 json
	 */
	@RequestMapping(value={"list",""})
	public String  getAllJobs(Model model){
		List<ScheduleJob> scheduleJobs = scheduleJobService.getAllScheduleJob();
		model.addAttribute("tasks", scheduleJobs);
		return "admin/sys/task/taskList";
	}
	
	/**
	 * 获取正在运行的定时任务
	 */
//	@RequiresRoles("admin")
	@RequestMapping("runningTasks")
	public String getAllJobsRun(Model model){
		List<ScheduleJob> scheduleJobs = scheduleJobService.getAllRuningScheduleJob();
		model.addAttribute("tasks", scheduleJobs);
		return "admin/sys/task/runningTaskList";
	}
	
	/**
	 * 添加跳转
	 * @param model
	 */
//	@RequiresRoles("admin")
	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String createForm(Model model) {
		model.addAttribute("scheduleJob", new ScheduleJob());
		return "admin/sys/task/taskAdd";
	}

	/**
	 * 添加
	 * @param scheduleJob
	 */
//	@RequiresRoles("admin")
	@RequestMapping(value = "add", method = RequestMethod.POST)
	public String create(ScheduleJob scheduleJob,HttpServletRequest request, RedirectAttributes redirectAttributes) {
		scheduleJob.setStatus("1");
		if(scheduleJobService.add(scheduleJob)){
			addMessage(redirectAttributes, "任务添加成功");	
		}else{
			addMessage(redirectAttributes, "任务添加失败");	
		}
		
		return "redirect:"+Global.getAdminPath()+"/task/?repage";
	}
	
	/**
	 * 暂停任务
	 */
//	@RequiresRoles("admin")
	@RequestMapping("stop")
	public String stop(ScheduleJob scheduleJob,RedirectAttributes redirectAttributes) {
		if(scheduleJobService.stopJob(scheduleJob.getName(), scheduleJob.getGroup())){
			addMessage(redirectAttributes, "任务" +scheduleJob.getName() + "已暂停" );	
		}else{
			addMessage(redirectAttributes, "任务" +scheduleJob.getName() + "暂停失败" );
		}
		
		return "redirect:"+Global.getAdminPath()+"/task/?repage";
	}

	/**
	 * 删除任务
	 */
//	@RequiresRoles("admin")
	@RequestMapping("delete")
	public String delete(ScheduleJob scheduleJob,RedirectAttributes redirectAttributes) {
		if(scheduleJobService.delJob(scheduleJob.getName(), scheduleJob.getGroup())){
			addMessage(redirectAttributes, "任务" +scheduleJob.getName() + "已删除" );
		}else{
			addMessage(redirectAttributes, "任务" +scheduleJob.getName() + "删除失败" );
		}
		
		return "redirect:"+Global.getAdminPath()+"/task/?repage";
	}
	/**
	 * 跳转修改页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "update", method = RequestMethod.GET)
	public String updateForm(String name,String group,Model model) {
		model.addAttribute("scheduleJob", scheduleJobService.getJob(name, group));
		return "admin/sys/task/taskUpdate";
	}
	
	/**
	 * 修改表达式
	 */
	@RequestMapping(value="update",method = RequestMethod.POST)
	public String update(ScheduleJob scheduleJob,RedirectAttributes redirectAttributes) {
		scheduleJobService.modifyTrigger(scheduleJob);
		addMessage(redirectAttributes, "任务" +scheduleJob.getName() + "修改成功" );
		return "redirect:"+Global.getAdminPath()+"/task/?repage";
	}

	/**
	 * 立即运行一次
	 */
	@RequestMapping("startNow")
	public String stratNow(ScheduleJob scheduleJob,RedirectAttributes redirectAttributes) {
		
		if(scheduleJobService.startNowJob(scheduleJob.getName(), scheduleJob.getGroup())){
			addMessage(redirectAttributes, "任务" +scheduleJob.getName() + "已运行" );	
		}else{
			addMessage(redirectAttributes, "任务" +scheduleJob.getName() + "启动失败" );	
		}
				
		return "redirect:"+Global.getAdminPath()+"/task/?repage";
	}

	/**
	 * 恢复
	 */
//	@RequiresRoles("admin")
	@RequestMapping("resume")
	public String resume(ScheduleJob scheduleJob,RedirectAttributes redirectAttributes) {
		
		if(scheduleJobService.restartJob(scheduleJob.getName(), scheduleJob.getGroup())){
			addMessage(redirectAttributes, "任务" +scheduleJob.getName() + "已恢复" );
		}else{
			addMessage(redirectAttributes, "任务" +scheduleJob.getName() + "恢复失败" );
		}
		
		return "redirect:"+Global.getAdminPath()+"/task/?repage";
	}
	
	/**
	 * 获取所有trigger
	 */
	public void getTriggers(HttpServletRequest request) {
		List<ScheduleJob> scheduleJobs = scheduleJobService.getTriggersInfo();
		System.out.println(scheduleJobs.size());
		request.setAttribute("triggers", scheduleJobs);
	}
	
	/**
	 * cron表达式生成页
	 */
	@RequestMapping("quartzCron")
	public String quartzCronCreate(){
		return "admin/system/quartzCron";
	}
	
	@RequestMapping("check")
	@ResponseBody
	public String check(String name,String group){
		if(StringUtils.isNotEmpty(name) && StringUtils.isNotEmpty(group)){
			ScheduleJob scheduleJob = scheduleJobService.getJob(name, group);
			if(scheduleJob != null){
				return "false";
			}
		}
		return "true"; 
	}
}
