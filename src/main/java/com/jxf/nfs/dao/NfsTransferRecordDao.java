package com.jxf.nfs.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jxf.mem.entity.Member;
import com.jxf.nfs.entity.NfsTransferRecord;
import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;

/**
 * 转账DAO接口
 * @author XIAORONGDIAN
 * @version 2018-11-09
 */
@MyBatisDao
public interface NfsTransferRecordDao extends CrudDao<NfsTransferRecord> {

	/**
	 * 根据会员ID 好友ID查从 earliestTime到现在的转账记录
	 * @param member
	 * @param friend
	 * @param earliestTime
	 * @return
	 */
	List<NfsTransferRecord> findByMemberIdAndFriendIdAndCreateDate(
			@Param("member")Member member,@Param("friend") Member friend,@Param("earliestTime") Date earliestTime);

	/**
	 * 获得当天转账失败次数
	 * @param member
	 * @param startTime
	 * @return
	 */
	Integer todayHasTransferFailedTimes(@Param("member")Member member,@Param("startTime") Date startTime);

	/**
	 * 查询俩人之间转账记录
	 * @param 用户Id
	 * @param 好友Id
	 * @return
	 */
	List<NfsTransferRecord> findByMemberIdAndFriendId(@Param("memberId")Long memberId,
			@Param("friendId")Long friendId);
	/**
	 * 查询我的所有
	 * @param 用户Id
	 * @param 好友Id
	 * @return
	 */
	List<NfsTransferRecord> findAllTransferPageList(
			NfsTransferRecord nfsTransferRecord);
	/**
	 * 查询一年该用户转账累计 从1月1号开始
	 * @param transfer
	 * @return
	 */
	BigDecimal getTotalTransferOneYear(NfsTransferRecord transfer);
}