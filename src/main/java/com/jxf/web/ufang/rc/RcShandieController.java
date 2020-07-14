package com.jxf.web.ufang.rc;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

import java.util.Date;

import java.util.List;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSON;
import com.jxf.nfs.constant.ActSubConstant;
import com.jxf.nfs.constant.TrxRuleConstant;
import com.jxf.nfs.service.NfsActService;
import com.jxf.rc.entity.RcShandie;
import com.jxf.rc.service.RcShandieService;
import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.utils.Encodes;
import com.jxf.svc.utils.SnowFlake;
import com.jxf.svc.utils.StringUtils;
import com.jxf.ufang.entity.UfangUser;
import com.jxf.ufang.entity.ZZCRequest;
import com.jxf.ufang.entity.ZZCRequestParameter;
import com.jxf.ufang.service.UfangUserActService;

import com.jxf.ufang.util.UfangUserUtils;
import com.jxf.web.admin.sys.BaseController;

/**
 * 闪谍报告（中智诚）Controller
 * @author XIAORONGDIAN
 * @version 2019-03-21
 */
@Controller
@RequestMapping(value = "${ufangPath}/rcShandie")
public class RcShandieController extends BaseController {

	@Autowired
	private RcShandieService rcShandieService;
    @Autowired
    private UfangUserActService ufangUserActService;
	@Autowired
	private NfsActService actService;
	@Autowired
	private MongoTemplate mongoTemplate;
	
	
//	@ModelAttribute
//	public RcShandie get(@RequestParam(required=false) Long id) {
//		RcShandie entity = null;
//		if (id!=null){
//			entity = rcShandieService.get(id);
//		}
//		if (entity == null){
//			entity = new RcShandie();
//		}
//		return entity;
//	}
	
	@RequiresPermissions("ufang:rcShandie:view")
	@RequestMapping(value = {"list", ""})
	public String list(RcShandie rcShandie, HttpServletRequest request, HttpServletResponse response, Model model) {
		UfangUser ufangUser = UfangUserUtils.getUser();
		Criteria criteria = Criteria.where("ufangUserId").is(ufangUser.getId());
		Criteria criteria2 = null;
		if(StringUtils.isNotBlank(rcShandie.getPhoneNo())){
			criteria.andOperator(Criteria.where("phoneNo").is(rcShandie.getPhoneNo()));
		}
		
	    if (StringUtils.isNotBlank(rcShandie.getName())) {
            Pattern pattern = Pattern.compile(rcShandie.getName(), Pattern.CASE_INSENSITIVE); //模糊查询
            criteria2 = new Criteria().andOperator(Criteria.where("name").regex(pattern));
        }
	    
	    Query query = new Query().addCriteria(criteria);
	    if(criteria2!=null){
	    	query.addCriteria(criteria2);
	    }
	    
	    Sort sort = new Sort(Sort.Direction.DESC, "createTime");//多条件DEVID、time
	    
		long total = mongoTemplate.count(query.with(sort), RcShandie.class);
		
		Page<RcShandie> page = rcShandie.getPage();
		int skip = (page.getPageNo() - 1) * 30;
    	query.skip(skip);// skip相当于从那条记录开始
    	query.limit(30);// 从skip开始,取多少条记录
    	List<RcShandie> findList = mongoTemplate.find(query, RcShandie.class);
    	
		for (RcShandie shanDie : findList) {
			shanDie.setPhoneNo(StringUtils.replacePattern(shanDie.getPhoneNo(), "(?<=[\\d]{3})\\d(?=[\\d]{4})", "*"));
			shanDie.setIdNo(StringUtils.replacePattern(shanDie.getIdNo(), "(?<=[\\d]{4})\\d(?=[\\d]{4})", "*")); 
		}
		page = new Page<RcShandie>(page.getPageNo(), 30 ,total, findList);
		model.addAttribute("page", page);
		return "ufang/rc/shandie/rcShandieList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("ufang:rcShandie:view")
	@RequestMapping(value = "add")
	public String add(RcShandie rcShandie, Model model) {
		model.addAttribute("rcShandie", rcShandie);
		return "ufang/rc/shandie/rcShandieAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("ufang:rcShandie:view")
	@RequestMapping(value = "query")
	public String query(RcShandie rcShandie, Model model) {
		Criteria criteria =Criteria.where("id").is(rcShandie.getId());
	    Query query = BasicQuery.query(criteria);  
		rcShandie =  mongoTemplate.findOne(query , RcShandie.class);
		
		model.addAttribute("idNo", StringUtils.replacePattern(rcShandie.getIdNo(), "(?<=[\\d]{3})\\d(?=[\\d]{4})", "*"));
		model.addAttribute("phoneNo", rcShandie.getPhoneNo());
		model.addAttribute("name", rcShandie.getName());
		model.addAttribute("content", JSON.parseObject(rcShandie.getContent()));
		
		return "ufang/rc/shandie/list";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("ufang:rcShandie:view")
	@RequestMapping(value = "update")
	public String update(RcShandie rcShandie, Model model) {
		model.addAttribute("rcShandie", rcShandie);
		return "ufang/rc/shandie/rcShandieUpdate";
	}
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("ufang:rcShandie:edit")
	@RequestMapping(value = "save")
	public String save(RcShandie rcShandie, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, rcShandie)){
			return add(rcShandie, model);
		}
		rcShandieService.save(rcShandie);
		addMessage(redirectAttributes, "保存闪谍报告成功");
		return "redirect:"+Global.getAdminPath()+"/ufang/rcShandie/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("ufang:rcShandie:edit")
	@RequestMapping(value = "delete")
	public String delete(RcShandie rcShandie, RedirectAttributes redirectAttributes) {
		rcShandieService.delete(rcShandie);
		addMessage(redirectAttributes, "删除闪谍报告成功");
		return "redirect:"+Global.getAdminPath()+"/ufang/rcShandie/?repage";
	}
	
	/**
	 * 查询页
	 */
	@RequiresPermissions("ufang:rcShandie:view")
	@RequestMapping(value = "search")
	public String search(String msg, Model model) {
		model.addAttribute("msg", msg);
		return "ufang/rc/shandie/search";
	}
	/**
	 * 请求中智诚的接口
	 */
	@RequiresPermissions("ufang:rcShandie:view")
	@RequestMapping(value = "zzcRequest", method = RequestMethod.POST)
	public String zzcRequest(String name,String phoneNo,String idNo,String type, Model model, RedirectAttributes redirectAttributes) {

	    UfangUser ufangUser = UfangUserUtils.getUser();

		RcShandie rcShandie = new RcShandie();
		rcShandie.setIdNo(idNo);
		rcShandie.setPhoneNo(phoneNo);
		rcShandie.setUfangUserId(ufangUser.getId());
		rcShandie.setName(name);
		rcShandie.setId(SnowFlake.getId());
		rcShandie.setCreateTime(new Date());
	    
	    BigDecimal bigDecimal = ufangUserActService.getUserAct(ufangUser,ActSubConstant.UFANG_USER_AVL_BAL).getCurBal();

        if(bigDecimal.compareTo(new BigDecimal("1.00")) < 0){
        	String msg = Encodes.urlEncode("余额不足，请先充值");
        	return "redirect:/ufang/rcShandie/search?msg=" + msg;
        }
		        
		try {

			HttpClient httpClient = HttpClients.createDefault();
			ZZCRequestParameter parameter = new ZZCRequestParameter();
			parameter.setLoan_type("22");
			parameter.setMobile(phoneNo);
			parameter.setName(name);
			parameter.setPid(idNo);
			ZZCRequest zzcRequest = new ZZCRequest(Global.getConfig("zzc.url"),JSON.toJSONString(parameter));
			
			HttpPost post = zzcRequest.getPost();
			HttpResponse response = httpClient.execute(post);
			
			String content = EntityUtils.toString(response.getEntity());
			if(content.contains("ERR_")){
				String msg = Encodes.urlEncode("输入有误,请重新输入");
				return "redirect:/ufang/rcShandie/search?msg=" + msg;
			}
							
			rcShandie.setContent(content);
			
			mongoTemplate.save(rcShandie);
			actService.updateAct(TrxRuleConstant.UFANG_CONSUME, new BigDecimal("1.00"), ufangUser,1L);
			
			model.addAttribute("idNo", StringUtils.replacePattern(idNo, "(?<=[\\d]{3})\\d(?=[\\d]{4})", "*"));
			model.addAttribute("phoneNo", phoneNo);
			model.addAttribute("name", name);
            model.addAttribute("content", JSON.parseObject(content));
        }catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return "ufang/rc/shandie/list";
	}

}
