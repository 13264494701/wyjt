package com.jxf.svc.persistence.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import com.alibaba.fastjson.JSON;

public class BasicTypeListTypeHandler extends BaseTypeHandler<Object> {

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i,
			Object parameter, JdbcType jdbcType) throws SQLException {
		if(parameter == null){
		     ps.setString(i, null);	
		}else{
			String  jsonString = JSON.toJSONString(parameter);
		    ps.setString(i, jsonString);	
		}	
	}

	@Override
	public List<Object> getNullableResult(ResultSet rs, String columnName)
			throws SQLException {
		 String  jsonString = rs.getString(columnName);
		 if(jsonString==null){
			 return null;
		 }
	     return JSON.parseArray(jsonString, Object.class);
	}


	@Override
	public List<Object> getNullableResult(ResultSet rs, int columnIndex)
			throws SQLException {
		 String  jsonString = rs.getString(columnIndex);
		 if(jsonString==null){
			 return null;
		 }
		 return JSON.parseArray(jsonString, Object.class);
	}

	@Override
	public List<Object> getNullableResult(CallableStatement cs, int columnIndex)
			throws SQLException {
		 String  jsonString = cs.getString(columnIndex);
		 if(jsonString==null){
			 return null;
		 }
		 return JSON.parseArray(jsonString, Object.class);
	}

}
