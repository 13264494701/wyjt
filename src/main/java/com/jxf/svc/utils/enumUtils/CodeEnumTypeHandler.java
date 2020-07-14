package com.jxf.svc.utils.enumUtils;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

/**
 * @作者: xiaorongdian
 * @创建时间 :2018年12月26日 下午9:39:47
 * @功能说明:自定义转换器
 */
public class CodeEnumTypeHandler<E extends Enum<?> & BaseCodeEnum> extends BaseTypeHandler<BaseCodeEnum> {
	 
	 private Class<E> type;
	 
	 public CodeEnumTypeHandler(Class<E> type) {
	  if (type == null) {
	   throw new IllegalArgumentException("Type argument cannot be null");
	  }
	  this.type = type;
	 }
	 
	 /** 用于定义设置参数时，该如何把Java类型的参数转换为对应的数据库类型 */
	 @Override
	 public void setNonNullParameter(PreparedStatement ps, int i, BaseCodeEnum parameter, JdbcType jdbcType)
	   throws SQLException {
	  ps.setInt(i, parameter.getCode());
	 }
	 
	 /** 用于定义通过字段名称获取字段数据时，如何把数据库类型转换为对应的Java类型 */
	 @Override
	 public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
	  int i = rs.getInt(columnName);
	  if (rs.wasNull()) {
	   return null;
	  } else {
	   try {
	    return CodeEnumUtil.codeOf(type, i);
	   } catch (Exception ex) {
	    throw new IllegalArgumentException("Cannot convert " + i + " to " + type.getSimpleName() + " by ordinal value.",
	      ex);
	   }
	  }
	 }
	 
	 /** 用于定义通过字段索引获取字段数据时，如何把数据库类型转换为对应的Java类型 */
	 @Override
	 public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
	  int i = rs.getInt(columnIndex);
	  if (rs.wasNull()) {
	   return null;
	  } else {
	   try {
	    return CodeEnumUtil.codeOf(type, i);
	   } catch (Exception ex) {
	    throw new IllegalArgumentException("Cannot convert " + i + " to " + type.getSimpleName() + " by ordinal value.",
	      ex);
	   }
	  }
	 }
	 
	 /** 用定义调用存储过程后，如何把数据库类型转换为对应的Java类型 */
	 @Override
	 public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
	  int i = cs.getInt(columnIndex);
	  if (cs.wasNull()) {
	   return null;
	  } else {
	   try {
	    return CodeEnumUtil.codeOf(type, i);
	   } catch (Exception ex) {
	    throw new IllegalArgumentException("Cannot convert " + i + " to " + type.getSimpleName() + " by ordinal value.",
	      ex);
	   }
	  }
	 }
}
