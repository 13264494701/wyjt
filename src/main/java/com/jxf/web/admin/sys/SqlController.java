package com.jxf.web.admin.sys;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.ebaoquan.rop.thirdparty.org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jxf.svc.config.Global;
import com.jxf.svc.sys.user.entity.User;
import com.jxf.svc.sys.util.UserUtils;
import com.jxf.transplantation.dbo.OldDBOperator;
import com.jxf.transplantation.dbo.NowDBOperator;



/**
 * 标签Controller
 * @author jxf
 * @version 2015-07-28
 */
@Controller("adminSqlController")
@RequestMapping(value = "${adminPath}/sql")
public class SqlController extends BaseController {
	
	/**
	 * 新库查询
	 */
	@RequiresPermissions("admin")
	@RequestMapping(value = "newdb")
	public String newdb(HttpServletRequest request,RedirectAttributes redirectAttributes,Model model) {

		String sqlStr = request.getParameter("sqlStr");

		if(sqlStr==null)
		return "admin/sys/sql/newdb";
		
		String regex = "(UPDATE)|(DELETE)|(insert)|(create)|(Drop)|(alter)|(Truncate)";
		Pattern pattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(sqlStr);
		if(matcher.find()){
			addMessage(redirectAttributes, "SQL语句不合法");
		}
		String loginIp = Global.getRemoteAddr(request);
		logger.warn("当前IP为{}",loginIp);
		User user = UserUtils.getUser();
		if(StringUtils.contains(user.getAllowIps(), loginIp)) {
			if(sqlStr.indexOf("limit")==-1){
				sqlStr = sqlStr + " limit 0,10";
			}
			Map<String,Object> retMap = getNewDbResultMap(sqlStr);
			model.addAttribute("sqlStr", sqlStr);
			model.addAttribute("userList", (List<List<Object>>)retMap.get("val"));
			model.addAttribute("keyList", (List<String>)retMap.get("key"));
			
		}		
		return "admin/sys/sql/newdb";
	}
	/**
	 * 新库更新
	 */
	@RequiresPermissions("admin")
	@RequestMapping(value = "newdbupdate")
	public String newdbupdate(HttpServletRequest request,RedirectAttributes redirectAttributes,Model model) {

		String sqlStr = request.getParameter("sqlStr");

		if(sqlStr==null)
		return "admin/sys/sql/newdbupdate";
		
		String regex = "(create)|(Drop)|(alter)|(Truncate)";
		Pattern pattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(sqlStr);
		if(matcher.find()){
			addMessage(redirectAttributes, "SQL语句不合法");
		}
		String loginIp = Global.getRemoteAddr(request);
		logger.warn("当前IP为{}",loginIp);
		User user = UserUtils.getUser();
		if(StringUtils.contains(user.getAllowIps(), loginIp)) {
			model.addAttribute("sqlStr", sqlStr);
			int updatelines = executeUpdate(sqlStr);
			addMessage(redirectAttributes, "成功更新"+updatelines+"条数据");
		}else {
			
		}		
		return "admin/sys/sql/newdbupdate";
	}
	/**
	 * 老库查询
	 */
	@RequiresPermissions("admin")
	@RequestMapping(value = "olddb")
	public String olddb(HttpServletRequest request,RedirectAttributes redirectAttributes,Model model) {

		String sqlStr = request.getParameter("sqlStr");

		if(sqlStr==null)
		return "admin/sys/sql/olddb";
		
		String regex = "(UPDATE)|(DELETE)|(insert)|(create)|(Drop)|(alter)|(Truncate)";
		Pattern pattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(sqlStr);
		if(matcher.find()){
			addMessage(redirectAttributes, "SQL语句不合法");
		}
		String loginIp = Global.getRemoteAddr(request);
		logger.warn("当前IP为{}",loginIp);
		User user = UserUtils.getUser();
		if(StringUtils.contains(user.getAllowIps(), loginIp)) {
			if(sqlStr.indexOf("limit")==-1){
				sqlStr = sqlStr + " limit 0,10";
			}
			Map<String,Object> retMap = getOldDbResultMap(sqlStr);
			model.addAttribute("sqlStr", sqlStr);
			model.addAttribute("userList", (List<List<Object>>)retMap.get("val"));
			model.addAttribute("keyList", (List<String>)retMap.get("key"));
			
		}		
		return "admin/sys/sql/olddb";
	}
	

	
	public static  Map<String,Object> getNewDbResultMap(String sql){
		
		NowDBOperator operator = new NowDBOperator();		
		List<List<Object>> userList = new ArrayList<List<Object>>();
		List<String> keyList = new ArrayList<String>();
		try
		{
			PreparedStatement pstmt = operator.getPreparedStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			ResultSetMetaData md = rs.getMetaData();
			
			int columnCount = md.getColumnCount();
			
			for (int i = 1; i <= columnCount; i++) {
				
				keyList.add(md.getColumnName(i));
			}		
			while(rs.next())
			{
				List<Object> valList = new ArrayList<Object>();
				for(String key : keyList){
					valList.add(rs.getString(key));
				}
				userList.add(valList);
			}	
			rs.close();
			pstmt.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}finally{
			operator.close();	
		}
		Map<String,Object> retMap =new HashMap<String ,Object>();
		retMap.put("key", keyList);
		retMap.put("val", userList);
		return retMap;
	}
	
	public static  int executeUpdate(String sql){
		
		NowDBOperator operator = new NowDBOperator();		
		PreparedStatement pstmt = null;
		int updatelines = 0;
		try
		{
			pstmt = operator.getPreparedStatement(sql);
			updatelines = pstmt.executeUpdate();
			pstmt.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();			
		}finally{		
			operator.close();
		}

		return updatelines;
	}
    public static  Map<String,Object> getOldDbResultMap(String sql){
		
		OldDBOperator operator = new OldDBOperator();		
		List<List<Object>> userList = new ArrayList<List<Object>>();
		List<String> keyList = new ArrayList<String>();
		
		try
		{
			PreparedStatement pstmt = operator.getPreparedStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			ResultSetMetaData md = rs.getMetaData();
			
			int columnCount = md.getColumnCount();
			
			for (int i = 1; i <= columnCount; i++) {
				
				keyList.add(md.getColumnName(i));
			}					
			while(rs.next())
			{
				List<Object> valList = new ArrayList<Object>();
				for(String key : keyList){
					valList.add(rs.getString(key));
				}
				userList.add(valList);
			}		
			rs.close();
			pstmt.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}finally{
			operator.close();	
		}
		Map<String,Object> retMap =new HashMap<String ,Object>();
		retMap.put("key", keyList);
		retMap.put("val", userList);
		return retMap;
	}
	
}
