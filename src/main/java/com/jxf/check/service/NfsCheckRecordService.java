package com.jxf.check.service;

import com.jxf.check.entity.NfsCheckRecord;
import com.jxf.mem.entity.MemberCancellation;
import com.jxf.nfs.entity.NfsFundAddReduce;
import com.jxf.nfs.entity.NfsWdrlRecord;
import com.jxf.svc.sys.crud.service.CrudService;
import com.jxf.ufang.entity.UfangFundAddReduce;

/**
 * 审核记录Service
 * @author suHuimin
 * @version 2019-01-26
 */
public interface NfsCheckRecordService extends CrudService<NfsCheckRecord> {

	/**
	 *   记录提现审核日志
	 * @param nfsWdrlRecord
	 */
	NfsCheckRecord saveWdrlCheckLog(NfsWdrlRecord nfsWdrlRecord);

	/**
	 *      会员加减款审核日志
	 * @param fundAddReduce
	 * @return
	 */
	NfsCheckRecord saveMemberFundAddReduceCheckLog(NfsFundAddReduce fundAddReduce);
	/**
	 * 优放机构加减款日志
	 * @param ufangFundAddReduce
	 * @return
	 */
	NfsCheckRecord saveUfangFundAddReduceCheckLog(UfangFundAddReduce ufangFundAddReduce);
	
	
	/**
	 *   记录注销申请审核日志
	 * @param nfsWdrlRecord
	 */
	NfsCheckRecord saveCancellationCheckLog(MemberCancellation memberCancellation);
}