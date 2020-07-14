package com.jxf.svc.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.jxf.mem.entity.MemberMessage;
import com.jxf.mem.service.MemberMessageService;
import com.jxf.mms.service.SendMsgService;
import com.jxf.transplantation.temp.member.UpdateMemberVideoVerify;

@Component
@Async
public class SmsListener implements ApplicationListener<Event>{

	private static Logger logger = LoggerFactory.getLogger(UpdateMemberVideoVerify.class);
	
	@Autowired
	private MemberMessageService memberMessageService;
	
	@Autowired
	private SendMsgService sendMsgService;
	
	@Override
	public void onApplicationEvent(Event event) {
		
		logger.debug("======收到通知准备发送消息======");
		MemberMessage sendMessage = memberMessageService.sendMessage(event.getType(),event.getId());
		
		sendMsgService.beforeSendAppMsg(sendMessage);
	}

}
