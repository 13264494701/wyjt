package com.jxf.nfs.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.jxf.mem.entity.Member;
import com.jxf.nfs.entity.NfsTransferRecord;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.CrudService;

/**
 * 转账Service
 * @author XIAORONGDIAN
 * @version 2018-11-09
 */
public interface NfsTransferRecordService extends CrudService<NfsTransferRecord> {

	/***
	 * 根据会员ID 好友ID查从 earliestTime到现在的转账记录
	 * @param 会员
	 * @param 好友
	 * @param 从什么时间开始
	 * @return
	 */
	List<NfsTransferRecord> findByMemberIdAndFriendIdAndCreateDate(Member member,
			Member friend, Date earliestTime);

	/**
	 * 获得当天转账失败次数
	 * @param member
	 * @param startTime
	 * @return
	 */
	Integer todayHasTransferFailedTimes(Member member, Date startTime);

	/**
	 * 查询俩人之间转账记录 按update_time倒序
	 * @param 用户Id
	 * @param 好友Id
	 * @return
	 */
	List<NfsTransferRecord> findByMemberIdAndFriendId(Long memberId, Long friendId);

	Page<NfsTransferRecord> findPageList(NfsTransferRecord nfsTransferRecord,
			Integer pageNo, Integer pageSize);

	/**
	 * 查询我的全部转账记录
	 * @param 用户Id
	 * @return
	 */
	Page<NfsTransferRecord> findAllTransferPageList( Member me, Integer pageNo, Integer pageSize);

	void updateActForTransfer(NfsTransferRecord transferRecord);

	/**
	 * 查一年转账金额 从1月1号累计
	 * @param transfer
	 * @return
	 */
	BigDecimal getTotalTransferOneYear(NfsTransferRecord transfer);
	
}