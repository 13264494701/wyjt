package com.jxf.mms.record.service;

import com.jxf.mms.record.entity.MmsInternalMsg;
import com.jxf.svc.sys.crud.service.CrudService;
/**
 * @类功能说明： 站内信service层接口
 * @类修改者： 
 * @修改日期： 
 * @修改说明： 
 * @公司名称：北京金信服科技有限公司
 * @作者：zhuhuijie 
 * @创建时间：2016年3月18日 下午6:15:30 
 * @版本：V1.0
 */
public interface MmsInternalMsgService extends CrudService<MmsInternalMsg>{
	/**
	 * 发送站内信
	 * zhuhuijie  2016年3月18日 
	 * 修改者名字 修改日期 
	 * 修改内容 
	 * @参数： @param sender
	 * @参数： @param receiver
	 * @参数： @param msgType
	 * @参数： @param subject
	 * @参数： @param content     
	 * @return void    
	 * @throws
	 */
	public void sendInternalMsg(String sender,String receiver,String tmplCode,String subject,String content);
}
