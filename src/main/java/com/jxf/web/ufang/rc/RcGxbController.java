package com.jxf.web.ufang.rc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.jxf.nfs.constant.ActSubConstant;
import com.jxf.nfs.constant.TrxRuleConstant;
import com.jxf.nfs.service.NfsActService;
import com.jxf.rc.entity.RcGxb;
import com.jxf.rc.entity.RcGxb.SubItem;
import com.jxf.rc.service.RcGxbService;
import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.utils.Encodes;
import com.jxf.svc.utils.HttpUtils;
import com.jxf.svc.utils.SnowFlake;
import com.jxf.ufang.entity.UfangUser;
import com.jxf.ufang.service.UfangUserActService;
import com.jxf.ufang.util.UfangUserUtils;
import com.jxf.web.ufang.UfangBaseController;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 风控 公信宝Controller
 *
 * @author Administrator
 * @version 2018-10-16
 */
@Controller
@RequestMapping(value = "${ufangPath}/rcGxb")
public class RcGxbController extends UfangBaseController {

    @Autowired
    private RcGxbService rcGxbService;
    @Autowired
    private UfangUserActService ufangUserActService;
	@Autowired
	private NfsActService actService;
	@Autowired
	private MongoTemplate mongoTemplate;

    @ModelAttribute
    public RcGxb get(@RequestParam(required = false) Long id) {
        RcGxb entity = null;
        if (id != null) {
            entity = rcGxbService.get(id);
        }
        if (entity == null) {
            entity = new RcGxb();
        }
        return entity;
    }

    @RequestMapping(value = "return")
    public String ret(Model model) {
        model.addAttribute("authItem","sesame_multiple");
        return "ufang/rc/return";
    }
    @RequestMapping(value = "returnDebit")
    public String returnDebit(Model model,String subItem) {
    	model.addAttribute("authItem","debit");
    	model.addAttribute("subItem",subItem);
    	return "ufang/rc/return";
    }

    @RequestMapping(value = "token")
    public String token(String authItem,String subItem, String msg, Model model) {
        model.addAttribute("authItem", authItem);
        model.addAttribute("msg", msg);
        model.addAttribute("subItem", subItem);
        switch (authItem) {
            case "sesame_multiple":
                return "ufang/rc/gxb/token";
            case "wechat_phone":
                return "ufang/rc/gxb/wechatToken";
            case "debit":
                return "ufang/rc/gxb/token";
            default:
                return "ufang/rc/gxb/token";
        }
    }

    @RequestMapping(value = "getToken")
    public String getToken(String authItem, String name, String phone, String idcard , Model model,String subItem) {
        String appId = Global.getConfig("gxb.appId");
        String appSecret = Global.getConfig("gxb.appSecret");
        String tokenUrl = Global.getConfig("gxb.tokenUrl");
        String wechatUrl = Global.getConfig("gxb.wechatUrl");
        String sesameUrl = "";
        Integer gxbAuthType = 0;
        
        UfangUser ufangUser = UfangUserUtils.getUser();
        if("sesame_multiple".equals(authItem)){
        	sesameUrl = Global.getConfig("gxb.sesameUrl") + "sesame_multiple";
        }else if("debit".equals(authItem)){//借贷
        	gxbAuthType = 2;
        	if(StringUtils.equals(subItem, "jdb")){
        		sesameUrl = Global.getConfig("gxb.debitUrl") + subItem +"&subItem=jiedaibao";
        	}else if(StringUtils.equals(subItem, "hhd")){
        		sesameUrl = Global.getConfig("gxb.debitUrl") + subItem +"&subItem=mifang";
        	}else{
        		sesameUrl = Global.getConfig("gxb.debitUrl") + subItem +"&subItem=jinjiedao";
        	}
        }
        
        RcGxb rcGxb = new RcGxb();
        rcGxb.setUserEmpNo(ufangUser.getEmpNo());
        rcGxb.setAuthType(RcGxb.GxbAuthType.values()[gxbAuthType]);
        rcGxb.setName(name);
        rcGxb.setPhoneNo(phone);
        rcGxb.setIdNo(idcard);
        rcGxb.setCreateTime(new Date());
       
        if(gxbAuthType == 0){//芝麻分还是操作mysql
        	rcGxbService.save(rcGxb);
        }else{
        	rcGxb.setId(SnowFlake.getId());
        	mongoTemplate.save(rcGxb);
        }
        
        
        Long timestamp = System.currentTimeMillis();
        String sign = DigestUtils.md5Hex(String.format("%s%s%s%s%s", appId, appSecret, authItem, timestamp, rcGxb.getId().toString()));

        Map<String, Object> map = new HashMap<>();
        map.put("appId", appId);
        map.put("sign", sign);
        map.put("sequenceNo", rcGxb.getId().toString());
        map.put("authItem", authItem);
        map.put("timestamp", timestamp);
        map.put("phone", phone);
        if ("wechat_phone".equals(authItem)) {
            map.put("name", "小明");
            map.put("idcard", "110101199101011090");
        }else {
            map.put("idcard", idcard);
            map.put("name", name);
        }

        String json = JSON.toJSONString(map);
//        RestTemplate restTemplate = new RestTemplate();
        String token;
        try {
            String result = HttpUtils.doPost(tokenUrl, json);
            Map<String, String> res = JSON.parseObject(result, new TypeReference<Map<String, String>>() {});
            token = JSON.parseObject(res.get("data"), new TypeReference<Map<String, String>>() {}).get("token");
        } catch (Exception e) {
        	String msg = Encodes.urlEncode("输入的信息未通过验证");
            return "redirect:/ufang/rcGxb/token?authItem="+authItem+"&msg="+msg;
        }

        BigDecimal bigDecimal = ufangUserActService.getUserAct(ufangUser,ActSubConstant.UFANG_USER_AVL_BAL).getCurBal();

        if(bigDecimal.compareTo(new BigDecimal("1.00")) < 0){
        	String msg = Encodes.urlEncode("余额不足，请先充值");
            return "redirect:/ufang/rcGxb/token?authItem=" + authItem + "&msg=" + msg;
        }

        actService.updateAct(TrxRuleConstant.UFANG_CONSUME, new BigDecimal("1.00"), ufangUser,rcGxb.getId());
        if ("wechat_phone".equals(authItem)) {
//            String url = wechatUrl + "/" + token + "/" + phone;
//            String res = restTemplate.getForEntity(url, String.class).getBody();
            model.addAttribute("authItem","wechat_phone");
            return "ufang/rc/return";
        }else if("sesame_multiple".equals(authItem)){
        	
            return "redirect:" + sesameUrl + "&token=" + token + "&username=" + phone + "&editdisable=true";
            
        }else if("debit".equals(authItem)){
        	
        	return "redirect:" + sesameUrl+ "&token=" + token;
        }
        
        return "ufang/rc/return";
    }

    @RequiresPermissions("rcGxb:view")
    @RequestMapping(value = {"list", ""})
    public String list(RcGxb rcGxb, HttpServletRequest request, HttpServletResponse response, Model model, String type,String subType) {
        UfangUser ufangUser = UfangUserUtils.getUser();
        Criteria criteria = Criteria.where("userEmpNo").is(ufangUser.getEmpNo());
        Criteria criteria2 = null;
        Criteria criteria3 = null;
        switch (type) {
            case "sesame_multiple":
                criteria.andOperator(Criteria.where("authType").is(RcGxb.GxbAuthType.sesame_multiple.name()));
                break;
            case "wechat_phone":
                criteria.andOperator(Criteria.where("authType").is(RcGxb.GxbAuthType.wechat_phone.name()));
                break;
            case "debit":
                criteria.andOperator(Criteria.where("subItem").is(subType),
                		Criteria.where("authType").is(RcGxb.GxbAuthType.debit.name()));
                break;
            default:
                break;
        }
        
        if(StringUtils.isNotBlank(rcGxb.getIdNo()) && StringUtils.isNotBlank(rcGxb.getPhoneNo())){
        	criteria2 = new Criteria().andOperator(Criteria.where("idNo").is(rcGxb.getIdNo()),Criteria.where("phoneNo").is(rcGxb.getPhoneNo()));
        }else if(StringUtils.isNotBlank(rcGxb.getIdNo())){
        	criteria2 = new Criteria().andOperator(Criteria.where("idNo").is(rcGxb.getIdNo()));
        }else if(StringUtils.isNotBlank(rcGxb.getPhoneNo())){
        	criteria2 = new Criteria().andOperator(Criteria.where("phoneNo").is(rcGxb.getPhoneNo()));
        }
        Query query = new Query().addCriteria(criteria);
        if(criteria2 != null){
        	query.addCriteria(criteria2);
        }
        if(rcGxb.getId() != null){
        	criteria3=new Criteria().andOperator(Criteria.where("_id").is(rcGxb.getId()));
        	query.addCriteria(criteria3);
        }
        
	    Sort sort = new Sort(Sort.Direction.DESC, "createTime");//多条件DEVID、time
	    
		Page<RcGxb> page = null ;
    	List<RcGxb> rcGxbList = null;
    	long total = 0;
    	if(StringUtils.equals(type, "sesame_multiple")){//芝麻分还是从mysql中取
    		rcGxb.setUserEmpNo(ufangUser.getEmpNo());
            rcGxb.setAuthType(RcGxb.GxbAuthType.sesame_multiple);
    		page = rcGxbService.findPage(new Page<RcGxb>(request, response), rcGxb);
    		rcGxbList = page.getList();
    	}else{
    		total = mongoTemplate.count(query, RcGxb.class);
    		page = rcGxb.getPage();
    		int skip = (page.getPageNo() - 1) * 30;
        	query.skip(skip);// skip相当于从那条记录开始
        	query.limit(30);// 从skip开始,取多少条记录
    		rcGxbList = mongoTemplate.find(query.with(sort), RcGxb.class);
    	}
       
        switch (type) {
            case "sesame_multiple":
                if(rcGxbList != null && rcGxbList.size() > 0){
                	for (int i = 0; i < rcGxbList.size(); i++) {
                		String result = rcGxbList.get(i).getAuthResult();
                		if(result != null){
                			Map<String, String> res = JSON.parseObject(result, new TypeReference<Map<String, String>>() {});
                			rcGxbList.get(i).setSesameScore(res.get("sesameScore"));
                			rcGxbList.get(i).setStatus(res.get("status"));
                		}
                	}
                }
                break;
            case "wechat_phone":
            	if(rcGxbList != null && rcGxbList.size() > 0){
            		for (int i = 0; i < rcGxbList.size(); i++) {
            			String result = rcGxbList.get(i).getAuthResult();
            			Map<String, String> res = JSON.parseObject(result, new TypeReference<Map<String, String>>() {});
            			rcGxbList.get(i).setArea(res.get("area"));
            			rcGxbList.get(i).setRemark(res.get("remark"));
            			rcGxbList.get(i).setAvatar(res.get("avatar"));
            			rcGxbList.get(i).setNickname(res.get("nickname"));
            			rcGxbList.get(i).setQueryTime(res.get("queryTime"));
            			rcGxbList.get(i).setSex(res.get("sex"));
            			rcGxbList.get(i).setSignature(res.get("signature"));
            		}
            	}
                break;
            case "debit":
            	
            	break;
            default:
                break;
        }
        model.addAttribute("type",type);
        model.addAttribute("subType",subType);
        if(StringUtils.equals(type, "sesame_multiple")){//芝麻分还是从mysql中取
        	page.setList(rcGxbList);
        	model.addAttribute("page", page);
        }else{
        	page = new Page<RcGxb>(page.getPageNo(), 30 ,total, rcGxbList);
        }
		
        model.addAttribute("page", page);
        return "ufang/rc/gxb/rcGxbList";
    }

    /**
     * 添加页面跳转
     */
    @RequiresPermissions("rcGxb:view")
    @RequestMapping(value = "add")
    public String add(RcGxb rcGxb, Model model, String authItem,String subItem) {
        model.addAttribute("rcGxb", rcGxb);
        model.addAttribute("authItem", authItem);
        model.addAttribute("subItem", subItem);
        return "ufang/rc/gxb/rcGxbAdd";
    }

    /**
     * 查看页面跳转
     */
    @RequiresPermissions("rcGxb:view")
    @RequestMapping(value = "query")
    public String query(RcGxb rcGxb, Model model,String type) {
    	Criteria criteria = null;
    	if(StringUtils.equals("debit", type)){//借贷
    		Long id = rcGxb.getId();
    		criteria = Criteria.where("_id").is(id);
    		Query query = BasicQuery.query(criteria);  
    		rcGxb =  mongoTemplate.findOne(query , RcGxb.class);
    		String authResult = rcGxb.getAuthResult();
    		Object result = JSON.parse(authResult);
    		
    		model.addAttribute("rcGxb", rcGxb);
    		model.addAttribute("result", result);
    		
    		SubItem subItem = rcGxb.getSubItem();
    		if(subItem.equals(SubItem.jdb)){
    			return "ufang/rc/gxb/queryJdb";
    		}else if(subItem.equals(SubItem.hhd)){
    			return "ufang/rc/gxb/queryHhd";
    		}else{
    			return "ufang/rc/gxb/queryJjd";
    		}
    	}
    	return "";
    }

    /**
     * 修改页面跳转
     */
    @RequiresPermissions("rcGxb:view")
    @RequestMapping(value = "update")
    public String update(RcGxb rcGxb, Model model) {
        model.addAttribute("rcGxb", rcGxb);
        return "ufang/rc/gxb/rcGxbUpdate";
    }

    /**
     * 新增与修改的提交
     */
    @RequiresPermissions("rcGxb:edit")
    @RequestMapping(value = "save")
    public String save(RcGxb rcGxb, Model model, RedirectAttributes redirectAttributes) {
//        if (!beanValidator(model, rcGxb)) {
//            return add(rcGxb, model,);
//        }
        rcGxbService.save(rcGxb);
        addMessage(redirectAttributes, "保存风控 公信宝成功");
        return "redirect:" + Global.getAdminPath() + "/rcGxb/?repage";
    }

    /**
     * 真删除提交
     */
    @RequiresPermissions("rcGxb:edit")
    @RequestMapping(value = "delete")
    public String delete(RcGxb rcGxb, RedirectAttributes redirectAttributes) {
        rcGxbService.delete(rcGxb);
        addMessage(redirectAttributes, "删除风控 公信宝成功");
        return "redirect:" + Global.getAdminPath() + "/rcGxb/?repage";
    }

}