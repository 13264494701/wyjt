package com.jxf.svc.utils.enumUtils;


/**
 * @作者: xiaorongdian
 * @创建时间 :2018年12月26日 下午9:37:09
 * @功能说明:将数值转换为枚举实例
 */
public class CodeEnumUtil {
	 public static <E extends Enum<?> & BaseCodeEnum> E codeOf(Class<E> enumClass, int code) {
	  E[] enumConstants = enumClass.getEnumConstants();
	  for (E e : enumConstants) {
	   if (e.getCode() == code)
	    return e;
	  }
	  return null;
	 }
}
