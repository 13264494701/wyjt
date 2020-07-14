package com.jxf.nfs.service;

import java.util.Date;
import java.util.List;

import com.jxf.mem.entity.Member;
import com.jxf.nfs.entity.NfsRchgRecord;
import com.jxf.svc.sys.crud.service.CrudService;

/**
 * 充值记录Service
 * @author suhuimin
 * @version 2018-10-10
 */
public interface NfsRchgRecordService extends CrudService<NfsRchgRecord> {
	
	/**
	 * 根据memberId查询放款人的充值记录
	 * @param memberId
	 * @param loanTime
	 * @return
	 */
	List<NfsRchgRecord> getRchgRecordByMemberId(Long memberId,Date loanTime);
	
	/**
	 * 确认充值结果
	 * @return
	 */
	int confirmSuccess(NfsRchgRecord rchgRecord,Member member);
}