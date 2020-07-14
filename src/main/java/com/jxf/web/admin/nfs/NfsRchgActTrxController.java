package com.jxf.web.admin.nfs;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jxf.nfs.entity.NfsRchgActTrx;
import com.jxf.nfs.service.NfsRchgActTrxService;
import com.jxf.svc.config.Global;
import com.jxf.svc.excel.ExportExcel;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.utils.DateUtils;
import com.jxf.web.admin.sys.BaseController;


/**
 * 充值流水Controller
 * @author wo
 * @version 2019-03-24
 */
@Controller
@RequestMapping(value = "${adminPath}/rchgActTrx")
public class NfsRchgActTrxController extends BaseController {

	@Autowired
	private NfsRchgActTrxService nfsRchgActTrxService;
	
	@ModelAttribute
	public NfsRchgActTrx get(@RequestParam(required=false) Long id) {
		NfsRchgActTrx entity = null;
		if (id!=null){
			entity = nfsRchgActTrxService.get(id);
		}
		if (entity == null){
			entity = new NfsRchgActTrx();
		}
		return entity;
	}
	
	@RequiresPermissions("rchgtrx:nfsRchgActTrx:view")
	@RequestMapping(value = {"list", ""})
	public String list(NfsRchgActTrx nfsRchgActTrx, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<NfsRchgActTrx> page = nfsRchgActTrxService.findPage(new Page<NfsRchgActTrx>(request, response), nfsRchgActTrx); 
		model.addAttribute("page", page);
		return "admin/nfs/rchgtrx/nfsRchgActTrxList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("rchgtrx:nfsRchgActTrx:view")
	@RequestMapping(value = "add")
	public String add(NfsRchgActTrx nfsRchgActTrx, Model model) {
		model.addAttribute("nfsRchgActTrx", nfsRchgActTrx);
		return "nfs/rchgtrx/nfsRchgActTrxAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("rchgtrx:nfsRchgActTrx:view")
	@RequestMapping(value = "query")
	public String query(NfsRchgActTrx nfsRchgActTrx, Model model) {
		model.addAttribute("nfsRchgActTrx", nfsRchgActTrx);
		return "nfs/rchgtrx/nfsRchgActTrxQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("rchgtrx:nfsRchgActTrx:view")
	@RequestMapping(value = "update")
	public String update(NfsRchgActTrx nfsRchgActTrx, Model model) {
		model.addAttribute("nfsRchgActTrx", nfsRchgActTrx);
		return "nfs/rchgtrx/nfsRchgActTrxUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("rchgtrx:nfsRchgActTrx:edit")
	@RequestMapping(value = "save")
	public String save(NfsRchgActTrx nfsRchgActTrx, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, nfsRchgActTrx)){
			return add(nfsRchgActTrx, model);
		}
		nfsRchgActTrxService.save(nfsRchgActTrx);
		addMessage(redirectAttributes, "保存充值流水成功");
		return "redirect:"+Global.getAdminPath()+"/rchgActTrx/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("rchgtrx:nfsRchgActTrx:edit")
	@RequestMapping(value = "delete")
	public String delete(NfsRchgActTrx nfsRchgActTrx, RedirectAttributes redirectAttributes) {
		nfsRchgActTrxService.delete(nfsRchgActTrx);
		addMessage(redirectAttributes, "删除充值流水成功");
		return "redirect:"+Global.getAdminPath()+"/rchgActTrx/?repage";
	}

	/**
	 * 导出充值账户流水数据
	 * @param rchgRecord
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("rchgtrx:nfsRchgActTrx:edit")
    @RequestMapping(value = "exportRchgActTrxData", method=RequestMethod.POST)
    public String exportRchgFile(NfsRchgActTrx nfsRchgActTrx, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            if(nfsRchgActTrx.getBeginTime()==null||nfsRchgActTrx.getEndTime()==null) {
				addMessage(redirectAttributes, "起始时间和结束时间不能为空");
				return "redirect:"+Global.getAdminPath()+"/rchgActTrx?repage";
            }
			int days = DateUtils.getDifferenceOfTwoDate(nfsRchgActTrx.getBeginTime(),nfsRchgActTrx.getEndTime());
			if(days>5){
				addMessage(redirectAttributes, "导出数据的时间跨度不能超过5天");
				return "redirect:"+Global.getAdminPath()+"/rchgActTrx?repage";
			}
            List<NfsRchgActTrx> rchgActTrxList = nfsRchgActTrxService.findList(nfsRchgActTrx);
            
            String fileName = "充值账户流水数据"+DateUtils.getDate("yyyy-MM-dd")+".xlsx";
            ExportExcel exprotExcel = new ExportExcel("充值账户流水数据", NfsRchgActTrx.class);
            exprotExcel.setDataList(rchgActTrxList);
            exprotExcel.write(response, fileName);
            exprotExcel.dispose();
            return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出充值账户流水失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/rchgActTrx?repage";
    }
}