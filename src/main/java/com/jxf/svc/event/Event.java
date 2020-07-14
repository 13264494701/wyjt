package com.jxf.svc.event;

import org.springframework.context.ApplicationEvent;

import com.jxf.mem.entity.MemberMessage;

public class Event extends ApplicationEvent{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private MemberMessage.Type type;
	
	private Long id;
	
	public Event(Object source, MemberMessage.Type type, Long id) {
		super(source);
		this.setType(type);
		this.setId(id);
	}

	public MemberMessage.Type getType() {
		return type;
	}

	public void setType(MemberMessage.Type type) {
		this.type = type;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
	
	
}
