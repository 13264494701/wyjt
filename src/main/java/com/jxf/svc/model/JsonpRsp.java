package com.jxf.svc.model;

/***
 * 
 * @类功能说明： jsonp 返回结果集
 * @类修改者： 
 * @修改日期： 
 * @修改说明： 
 * @公司名称：北京金信服科技有限公司
 * @作者：HUOJIABAO 
 * @创建时间：2016年12月17日 下午5:04:51 
 * @版本：V1.0
 */
public class JsonpRsp {
	
	private Integer errno;
	private Object obj;
	

	public Integer getErrno() {
		return errno;
	}

	public void setErrno(Integer errno) {
		this.errno = errno;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}	
}
