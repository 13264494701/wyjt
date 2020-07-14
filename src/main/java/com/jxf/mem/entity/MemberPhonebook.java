package com.jxf.mem.entity;



import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 手机通讯录Entity
 * @author wo
 * @version 2019-03-07
 */
public class MemberPhonebook extends CrudEntity<MemberPhonebook> {
	
	private static final long serialVersionUID = 1L;
	/** 会员*/
	private Member member;		
	
	/** 手机号列表 */
	private String phoneList;		
	/** 通讯录 */
	private String phoneBook;		
	
	public MemberPhonebook() {
		super();
	}

	public MemberPhonebook(Long id){
		super(id);
	}


	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}
	
	public String getPhoneList() {
		return phoneList;
	}

	public void setPhoneList(String phoneList) {
		this.phoneList = phoneList;
	}
	
	public String getPhoneBook() {
		return phoneBook;
	}

	public void setPhoneBook(String phoneBook) {
		this.phoneBook = phoneBook;
	}

	
}