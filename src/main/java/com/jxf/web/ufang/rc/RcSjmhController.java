package com.jxf.web.ufang.rc;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.jxf.nfs.constant.ActSubConstant;
import com.jxf.nfs.constant.TrxRuleConstant;
import com.jxf.nfs.service.NfsActService;
import com.jxf.rc.entity.RcSjmh;
import com.jxf.rc.service.RcSjmhService;
import com.jxf.rc.utils.SecurityUtil;
import com.jxf.svc.config.Constant;
import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.plugin.PluginConfig;
import com.jxf.svc.plugin.shujumohe.SjmhYysPlugin;
import com.jxf.svc.utils.Encodes;
import com.jxf.svc.utils.FileUtils;
import com.jxf.svc.utils.JSONUtil;
import com.jxf.ufang.entity.UfangUser;
import com.jxf.ufang.service.UfangUserActService;
import com.jxf.ufang.util.UfangUserUtils;
import com.jxf.web.ufang.UfangBaseController;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 风控 数据魔盒Controller
 *
 * @author Administrator
 * @version 2018-10-16
 */
@Controller
@RequestMapping(value = "${ufangPath}/rcSjmh")
public class RcSjmhController extends UfangBaseController {

	@Autowired
	private RcSjmhService rcSjmhService;
	@Autowired
	private SjmhYysPlugin sjmhYysPlugin;
	@Autowired
	private UfangUserActService ufangUserActService;
	@Autowired
	private NfsActService actService;

	@ModelAttribute
	public RcSjmh get(@RequestParam(required = false) Long id) {
		RcSjmh entity = null;
		if (id != null) {
			entity = rcSjmhService.get(id);
		}
		if (entity == null) {
			entity = new RcSjmh();
		}
		return entity;
	}

	@RequestMapping(value = "report")
	public String report(String taskId) {
		PluginConfig config = sjmhYysPlugin.getPluginConfig();
		Map<String, String> configAttr = config.getAttributeMap();
		String tokenUrl = configAttr.get(SjmhYysPlugin.YYS_TOKEN_URL_NAME);
		String reportUrl = configAttr.get(SjmhYysPlugin.REPORT_URL_NAME);
		String partner_code = configAttr.get(SjmhYysPlugin.PARTNER_CODE_NAME);
		String partner_key = configAttr.get(SjmhYysPlugin.PARTNER_KEY_NAME);
		RestTemplate restTemplate = new RestTemplate();
		String result = restTemplate
				.exchange(tokenUrl + "?partner_code=" + partner_code + "&partner_key=" + partner_key, HttpMethod.GET,
						null, String.class)
				.getBody();
		String token = JSON.parseObject(result, new TypeReference<Map<String, String>>() {}).get("data");

		return "redirect:" + reportUrl + "/" + taskId + "/" + token;
	}

	@RequestMapping(value = "return")
	public String ret() {
		return "ufang/rc/sjmh/return";
	}

	@RequiresPermissions("rcSjmh:view")
	@RequestMapping(value = { "list", "" })
	public String list(RcSjmh rcSjmh, HttpServletRequest request, HttpServletResponse response, Model model) {
		UfangUser ufangUser = UfangUserUtils.getUser();
		rcSjmh.setUser(ufangUser);
		rcSjmh.setChannelType(RcSjmh.ChannelType.yunyingshang);
		Page<RcSjmh> page = rcSjmhService.findPageByEmpNo(new Page<RcSjmh>(request, response), rcSjmh);
		model.addAttribute("page", page);
		return "ufang/rc/sjmh/rcSjmhList";
	}

	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("rcSjmh:view")
	@RequestMapping(value = "getToken")
	public String getToken(Model model, String name, String phone, String idcard, Integer channel) {
		
		String realName = StringUtils.trimAllWhitespace(name);
		UfangUser ufangUser = UfangUserUtils.getUser();
		BigDecimal curBal = ufangUserActService.getUserAct(ufangUser, ActSubConstant.UFANG_USER_AVL_BAL).getCurBal();
		if (curBal.compareTo(new BigDecimal("1.00")) < 0) {
			String msg = Encodes.urlEncode("余额不足，请先充值");
			return "redirect:/ufang/rcSjmh/token?channel=" +channel+ "&msg=" + msg;
		}
		
		String h5url = "";
		RcSjmh rcSjmh = new RcSjmh();
		rcSjmh.setProdType(RcSjmh.ProdType.ufang);
		
		if (channel == 0) {// 运营商
			h5url = Global.getConfig("yysurl") + Global.getConfig("sjmhtoken");
			rcSjmh.setChannelType(RcSjmh.ChannelType.yunyingshang);
		} else if (channel == 1) {// 淘宝
			h5url = Global.getConfig("tburl") + Global.getConfig("sjmhtoken");
			rcSjmh.setChannelType(RcSjmh.ChannelType.taobao);
		} else if (channel == 2) {// 银行卡
			h5url = Global.getConfig("wyurl") + Global.getConfig("sjmhtoken");
			rcSjmh.setChannelType(RcSjmh.ChannelType.wangyin);
		} else if (channel == 3) {// 社保
			h5url = Global.getConfig("sburl") + Global.getConfig("sjmhtoken");
			rcSjmh.setChannelType(RcSjmh.ChannelType.shebao);
		} else if (channel == 4) {// 公积金
			h5url = Global.getConfig("gjjurl") + Global.getConfig("sjmhtoken");
			rcSjmh.setChannelType(RcSjmh.ChannelType.gongjijin);
		} else if (channel == 5) {// 学信网
			h5url = Global.getConfig("xxurl") + Global.getConfig("sjmhtoken");
			rcSjmh.setChannelType(RcSjmh.ChannelType.xuexinwang);
		} else if (channel == 6) {// 京东
			h5url = Global.getConfig("jdurl") + Global.getConfig("sjmhtoken");
			rcSjmh.setChannelType(RcSjmh.ChannelType.jingdong);
		}

		rcSjmh.setUser(ufangUser);
		rcSjmh.setPhoneNo(phone);
		rcSjmh.setRealName(realName);
		rcSjmh.setIdNo(idcard);
		rcSjmh.setDataStatus(RcSjmh.DataStatus.task_created);
		rcSjmh.setReportStatus(RcSjmh.ReportStatus.task_created);
		rcSjmhService.save(rcSjmh);
		
		int code = actService.updateAct(TrxRuleConstant.UFANG_CONSUME, new BigDecimal("1.00"), ufangUser,rcSjmh.getId());
		
		if (code == Constant.UPDATE_FAILED) {
			logger.error("账户处理失败，优放用户ID{}", ufangUser.getId());
			String msg = Encodes.urlEncode("账户处理失败");
			return "redirect:/ufang/rcSjmh/token?channel=" +channel+ "&msg=" + msg;
		}
		
		
		String info = "";
		if (channel == 0) {
			info = "&cb="+ Encodes.urlEncode(Global.getConfig("ufangHost")+"ufang/rcSjmh/return")+"&real_name="+Encodes.urlEncode(realName)+"&identity_code="+idcard+"&user_mobile="+phone
					+ "&arr_pass_hide=real_name,identity_code,user_mobile&passback_params=ufang_" + rcSjmh.getId();
		} else {
			info = "&cb="+ Encodes.urlEncode(Global.getConfig("ufangHost")+"/ufang/rcSjmh/token?channel="+channel)+"&arr_pass_hide=passback_params&passback_params=ufang_" + rcSjmh.getId();
		}
		logger.debug(h5url + info);
		return "redirect:" + h5url + info;
		
	}

	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("rcSjmh:view")
	@RequestMapping(value = "add")
	public String add(Model model, Integer channel) {
		model.addAttribute("channel", channel);
		return "ufang/rc/sjmh/rcSjmhAdd";
	}

	@RequestMapping(value = "token")
	public String token(String msg, Model model, Integer channel) {
		model.addAttribute("msg", msg);
		model.addAttribute("channel", channel);
		return "ufang/rc/sjmh/token";
	}

	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("rcSjmh:view")
	@RequestMapping(value = "query")
	public String query(RcSjmh rcSjmh, Model model) {
		model.addAttribute("rcSjmh", rcSjmh);
		return "ufang/rc/sjmh/rcSjmhQuery";
	}

	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("rcSjmh:view")
	@RequestMapping(value = "update")
	public String update(RcSjmh rcSjmh, Model model) {
		model.addAttribute("rcSjmh", rcSjmh);
		return "ufang/rc/sjmh/rcSjmhUpdate";
	}

	/**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("rcSjmh:edit")
	@RequestMapping(value = "save")
	public String save(RcSjmh rcSjmh, Model model, RedirectAttributes redirectAttributes, Integer channel) {
		if (!beanValidator(model, rcSjmh)) {
			return add(model, channel);
		}
		rcSjmhService.save(rcSjmh);
		addMessage(redirectAttributes, "保存风控 数据魔盒成功");
		return "redirect:" + Global.getAdminPath() + "/rcSjmh/?repage";
	}

	/**
	 * 真删除提交
	 */
	@RequiresPermissions("rcSjmh:edit")
	@RequestMapping(value = "delete")
	public String delete(RcSjmh rcSjmh, RedirectAttributes redirectAttributes) {
		rcSjmhService.delete(rcSjmh);
		addMessage(redirectAttributes, "删除风控 数据魔盒成功");
		return "redirect:" + Global.getAdminPath() + "/rcSjmh/?repage";
	}

	/**
	 * 淘宝/京东/社保/学信网/公积金/网银
	 * 
	 * @param rcSjmh
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("rcSjmh:view")
	@RequestMapping(value = { "otherlist", "" })
	public String otherlist(RcSjmh rcSjmh, HttpServletRequest request, HttpServletResponse response, Model model,
			Integer channel) {
		UfangUser ufangUser = UfangUserUtils.getUser();
		rcSjmh.setUser(ufangUser);
		if (channel == 0) {// 运营商
			rcSjmh.setChannelType(RcSjmh.ChannelType.yunyingshang);
		} else if (channel == 1) {// 淘宝
			rcSjmh.setChannelType(RcSjmh.ChannelType.taobao);
		} else if (channel == 2) {// 银行卡
			rcSjmh.setChannelType(RcSjmh.ChannelType.wangyin);
		} else if (channel == 3) {// 社保
			rcSjmh.setChannelType(RcSjmh.ChannelType.shebao);
		} else if (channel == 4) {// 公积金
			rcSjmh.setChannelType(RcSjmh.ChannelType.gongjijin);
		} else if (channel == 5) {// 学信网
			rcSjmh.setChannelType(RcSjmh.ChannelType.xuexinwang);
		} else if (channel == 6) {// 京东
			rcSjmh.setChannelType(RcSjmh.ChannelType.jingdong);
		}
		Page<RcSjmh> page = rcSjmhService.findPageByEmpNo(new Page<RcSjmh>(request, response), rcSjmh);
		model.addAttribute("page", page);
		model.addAttribute("channel", channel);
		return "ufang/rc/sjmh/rcSjmhOtherList";
	}

	/**
	 * 淘宝详情
	 * 
	 * @param rcSjmhId
	 * @param model
	 * @return
	 */
	@RequiresPermissions("rcSjmh:view")
	@RequestMapping(value = "tblist")
	public String queryTBlist(Long rcSjmhId, Model model) {

		RcSjmh rcSjmh = rcSjmhService.get(rcSjmhId);
		try {
			String	sjmhData = FileUtils.readFileToString(new File(Global.getConfig("uploadPath")+rcSjmh.getDataPath()),"utf-8");
			JSONObject task_data_obj = JSONObject.parseObject(sjmhData);		
//			JSONObject base_info_obj = task_data_obj.getJSONObject("base_info");
//			JSONObject account_info_obj = task_data_obj.getJSONObject("account_info");
			JSONArray receiver_list_arr = task_data_obj.getJSONArray("receiver_list");
			JSONArray order_list_arr = task_data_obj.getJSONArray("order_list");
			JSONArray shopping_cart_arr = task_data_obj.getJSONArray("shopping_cart");
			
			JSONArray new_order_list_arr = new JSONArray();
			for(int i = 0 ;i<order_list_arr.size();i++){  
				JSONObject order_obj = order_list_arr.getJSONObject(i);
				Map<String, Object> order_map = new HashMap<String, Object>();
				order_map.put("order_amount", order_obj.get("order_amount"));
				order_map.put("receiver_name", order_obj.get("receiver_name"));
				JSONArray product_list_arr = order_obj.getJSONArray("product_list");
				StringBuffer product_name_buf = new StringBuffer();
				for(int j = 0;j<product_list_arr.size();j++){  
					JSONObject product_obj = product_list_arr.getJSONObject(j);
					product_name_buf.append(product_obj.getString("product_name"));
					product_name_buf.append("<br>");
				}
				order_map.put("product_name", product_name_buf.toString());
				order_map.put("order_time", order_obj.get("order_time"));
				new_order_list_arr.add(order_map);
			}
			model.addAttribute("order_list", new_order_list_arr);
			model.addAttribute("receiver_list", receiver_list_arr);
			model.addAttribute("shopping_cart", shopping_cart_arr);
		} catch (IOException e) {

			e.printStackTrace();
		}
		
		return "ufang/rc/sjmh/rcSjmhTBList";
	}

	/**
	 * 网银
	 * 
	 * @param rcSjmhId
	 * @param model
	 * @return
	 */
	@RequiresPermissions("rcSjmh:view")
	@RequestMapping(value = { "banklist", "" })
	public String queryBanklist(Long rcSjmhId, Model model) {
		
		List<Map> list = new ArrayList<Map>();
		// 根据Id查询数据
		RcSjmh rcSjmh = rcSjmhService.get(rcSjmhId);
		
		try {
			String taskData = FileUtils.readFileToString(new File(Global.getConfig("uploadPath")+rcSjmh.getDataPath()),"utf-8");
			Map<String, Object> map = JSONUtil.toMap(taskData);
			// 借记卡
			List<Map> debit_card_accounts = JSON.parseArray(map.get("debit_card_accounts") + "", Map.class);
			model.addAttribute("debit_card_accounts", debit_card_accounts);
			if (debit_card_accounts.size() > 0) {
				for (Map li : debit_card_accounts) {
					// 获取子账号
					List<Map> sub_accounts = JSON.parseArray(li.get("sub_accounts") + "", Map.class);
					// 获取子账号详情
					if (sub_accounts.size() > 0) {
						for (Map vv : sub_accounts) {
							List<Map> account_detail = JSON.parseArray(vv.get("account_detail") + "", Map.class);
							if (account_detail.size() > 0) {
								for (Map vi : account_detail) {
									int income=0;
									int outcom=0;
									if(vi.get("income") !=null && !vi.get("income").toString().equals("null")) {
										income=Integer.parseInt(vi.get("income").toString());
									}
									if(vi.get("outcome") !=null && !vi.get("outcome").toString().equals("null")) {
										outcom=Integer.parseInt(vi.get("outcome").toString());
									}
									Map map1 = new HashMap<>();
									map1.put("trade_date", vi.get("trade_date"));
									map1.put("currency", vi.get("currency"));
									map1.put("income", outcom+income);
									map1.put("balance", vi.get("balance"));
									map1.put("trade_type", vi.get("trade_type"));
									map1.put("remark", vi.get("remark"));
									map1.put("counterpart", vi.get("counterpart"));
									list.add(map1);
								}

							}
						}
					}
					model.addAttribute("account_detail", sub_accounts);
				}

			}
			model.addAttribute("sub_accounts", list);
		} catch (IOException e) {

			e.printStackTrace();
		}

		
		return "ufang/rc/sjmh/rcSjmhBankList";

	}

	/**
	 * 社保
	 * 
	 * @param rcSjmhId
	 * @param model
	 * @return
	 */
	@RequiresPermissions("rcSjmh:view")
	@RequestMapping(value = { "sblist", "" })
	public String querySBlist(Long rcSjmhId, Model model) {
		// 根据Id查询数据
		RcSjmh rcSjmh = rcSjmhService.get(rcSjmhId);

		try {
			String taskData = FileUtils.readFileToString(new File(Global.getConfig("uploadPath")+rcSjmh.getDataPath()),"utf-8");
			Map<String, Object> map = JSONUtil.toMap(taskData);
			Map<String, Object> mapbase = JSONUtil.toMap(map.get("user_info").toString());
			model.addAttribute("user_info", mapbase);
			Map<String, Object> endowment_overview = JSONUtil.toMap(map.get("endowment_overview").toString());
			model.addAttribute("endowment_overview", endowment_overview);
			Map<String, Object> medical_overview = JSONUtil.toMap(map.get("medical_overview").toString());
			model.addAttribute("medical_overview", medical_overview);
			Map<String, Object> unemployment_overview = JSONUtil.toMap(map.get("unemployment_overview").toString());
			model.addAttribute("unemployment_overview", unemployment_overview);
			Map<String, Object> accident_overview = JSONUtil.toMap(map.get("accident_overview").toString());
			model.addAttribute("accident_overview", accident_overview);
			Map<String, Object> maternity_overview = JSONUtil.toMap(map.get("maternity_overview").toString());
			model.addAttribute("maternity_overview", maternity_overview);
			// 养老
			List<Map> endowment_insurance = JSON.parseArray(map.get("endowment_insurance") + "", Map.class);
			model.addAttribute("endowment_insurance", endowment_insurance);
			// 医疗
			List<Map> medical_insurance = JSON.parseArray(map.get("medical_insurance") + "", Map.class);
			model.addAttribute("medical_insurance", medical_insurance);
			// 事业
			List<Map> unemployment_insurance = JSON.parseArray(map.get("unemployment_insurance") + "", Map.class);
			model.addAttribute("unemployment_insurance", unemployment_insurance);
			// 工伤
			List<Map> accident_insurance = JSON.parseArray(map.get("accident_insurance") + "", Map.class);
			model.addAttribute("accident_insurance", accident_insurance);
			// 生育
			List<Map> maternity_insurance = JSON.parseArray(map.get("maternity_insurance") + "", Map.class);
			model.addAttribute("maternity_insurance", maternity_insurance);
		} catch (IOException e) {

			e.printStackTrace();
		}
		
		return "ufang/rc/sjmh/rcSjmhSBList";
	}

	/**
	 * 公积金
	 * 
	 * @param rcSjmhId
	 * @param model
	 * @return
	 */
	@RequiresPermissions("rcSjmh:view")
	@RequestMapping(value = { "gjjlist", "" })
	public String queryGJJlist(Long rcSjmhId, Model model) {
		List<Map> list = new ArrayList<Map>();
		// 根据Id查询数据
		RcSjmh rcSjmh = rcSjmhService.get(rcSjmhId);

		try {
			String taskData = FileUtils.readFileToString(new File(Global.getConfig("uploadPath")+rcSjmh.getDataPath()),"utf-8");
			Map<String, Object> map = JSONUtil.toMap(taskData);
			Map<String, Object> base_info = JSONUtil.toMap(map.get("base_info").toString());
			model.addAttribute("base_info", base_info);
			// 存缴记录
			List<Map> bill_record = JSON.parseArray(map.get("bill_record") + "", Map.class);

			if (bill_record.size() > 0) {
				for (Map li : bill_record) {
					Map map1 = new HashMap<>();
					map1.put("month", li.get("deal_time"));
					map1.put("corp_name", li.get("corp_name"));
					map1.put("income", Integer.parseInt(li.get("income") + "") + Integer.parseInt(li.get("outcome") + ""));
					map1.put("desc", li.get("desc"));
					list.add(map1);
				}
			}
			model.addAttribute("bill_record", list);
		} catch (IOException e) {

			e.printStackTrace();
		}
		
		return "ufang/rc/sjmh/rcSjmhGJJList";
	}

	/**
	 * 学信网
	 * 
	 * @param rcSjmhId
	 * @param model
	 * @return
	 */
	@RequiresPermissions("rcSjmh:view")
	@RequestMapping(value = { "xxlist", "" })
	public String queryXXlist(Long rcSjmhId, Model model) throws UnsupportedEncodingException {
		// 根据Id查询数据
		RcSjmh rcSjmh = rcSjmhService.get(rcSjmhId);
		try {
			String taskData = FileUtils.readFileToString(new File(Global.getConfig("uploadPath")+rcSjmh.getDataPath()),"utf-8");
			Map<String, Object> map = JSONUtil.toMap(taskData);
			List<Map> school_info = JSON.parseArray(map.get("school_info") + "", Map.class);
			if (school_info.size() > 0) {
				for (Map li : school_info) {
					String detail_img;
					try {
						detail_img = SecurityUtil.Base64Decode(li.get("detail_img").toString());
						li.put("detail_img", detail_img);
					} catch (IOException e) {
						e.printStackTrace();
					}

				}
			}
			model.addAttribute("school_info", school_info);
			List<Map> education_info = JSON.parseArray(map.get("education_info") + "", Map.class);
			if (education_info.size() > 0) {
				for (Map li : education_info) {
					String detail_img;
					try {
						detail_img = SecurityUtil.Base64Decode(li.get("detail_img").toString());
						li.put("detail_img", detail_img);
					} catch (IOException e) {
						e.printStackTrace();
					}

				}
			}
			model.addAttribute("education_info", education_info);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		return "ufang/rc/sjmh/rcSjmhXXList";
	}
	/**
	 * 京东详情
	 * 
	 * @param rcSjmhId
	 * @param model
	 * @return
	 */
	@RequiresPermissions("rcSjmh:view")
	@RequestMapping(value = { "jdlist", "" })
	public String queryJDlist(Long rcSjmhId, Model model) {
		List<Map> order_listnew = new ArrayList<Map>();
		// 根据Id查询数据
		RcSjmh rcSjmh = rcSjmhService.get(rcSjmhId);

		try {
			String taskData = FileUtils.readFileToString(new File(Global.getConfig("uploadPath")+rcSjmh.getDataPath()),"utf-8");
			Map<String, Object> map = JSONUtil.toMap(taskData);
			Map<String, Object> mapbase = JSONUtil.toMap(map.get("base_info").toString());
			model.addAttribute("base_info", mapbase);
			Map<String, Object> mapaccount = JSONUtil.toMap(map.get("account_info").toString());
			model.addAttribute("account_info", mapaccount);
			List<Map> order_list = JSON.parseArray(map.get("order_list") + "", Map.class);
			if (order_list.size() > 0) {
				for (Map li : order_list) {
					Map map1 = new HashMap<>();
					String name = "";
					map1.put("order_amount", li.get("order_amount"));
					map1.put("receiver_name", li.get("receiver_name"));
					List<Map> product_list = JSON.parseArray(li.get("product_list") + "", Map.class);
					for (Map ww : product_list) {
						name = ww.get("product_name") + "<br>" + name;
					}
					map1.put("product_name", name);
					map1.put("order_time", li.get("order_time"));
					order_listnew.add(map1);
				}

			}
			model.addAttribute("order_list", order_listnew);
			List<Map> receiver_list = JSON.parseArray(map.get("receiver_list") + "", Map.class);
			model.addAttribute("receiver_list", receiver_list);
			//购物车详情
			List<Map> shopping_cart = JSON.parseArray(map.get("shopping_cart") + "", Map.class);
			model.addAttribute("shopping_cart", shopping_cart);
			// 白条
			List<Map> baitiao_bill_list = JSON.parseArray(map.get("baitiao_bill_list") + "", Map.class);
			model.addAttribute("baitiao_bill_list", baitiao_bill_list);
		} catch (IOException e) {

			e.printStackTrace();
		}
	
		return "ufang/rc/sjmh/rcSjmhJDList";
	}

}