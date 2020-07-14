package com.jxf.mem.entity;

import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotNull;

import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 积分明细Entity
 * @author zhj
 * @version 2016-05-13
 */
public class MemberPointDetail extends CrudEntity<MemberPointDetail> {
	
	private static final long serialVersionUID = 1L;
	

	private Member member;		// 会员
	private MemberPointRule.Type type;		// 交易类型
	private Long creditPoints;		// 获取积分
	private Long debitPoints;		// 扣除积分
	private Long currBalPoints;		// 当前积分
	private String trxNo;		// 交易编号
	
	
	public MemberPointDetail() {
		super();
	}

	public MemberPointDetail(Long id){
		super(id);
	}
	
	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}
	
	public MemberPointRule.Type getType() {
		return type;
	}

	public void setType(MemberPointRule.Type type) {
		this.type = type;
	}
	
	public Long getCreditPoints() {
		return creditPoints;
	}

	public void setCreditPoints(Long creditPoints) {
		this.creditPoints = creditPoints;
	}
	
	public Long getDebitPoints() {
		return debitPoints;
	}

	public void setDebitPoints(Long debitPoints) {
		this.debitPoints = debitPoints;
	}
	
	@NotNull(message="当前积分不能为空")
	public Long getCurrBalPoints() {
		return currBalPoints;
	}

	public void setCurrBalPoints(Long currBalPoints) {
		this.currBalPoints = currBalPoints;
	}
	
	@Length(min=0, max=16, message="交易编号长度必须介于 0 和 16 之间")
	public String getTrxNo() {
		return trxNo;
	}

	public void setTrxNo(String trxNo) {
		this.trxNo = trxNo;
	}
	



		
}