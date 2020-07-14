package com.jxf.nfs.utils;

import java.util.List;

import com.jxf.nfs.dao.NfsActSubDao;
import com.jxf.nfs.dao.NfsTrxRuleDao;
import com.jxf.nfs.entity.NfsActSub;
import com.jxf.svc.init.SpringContextHolder;


/**
 * 会员工具类
 * @author wo
 *
 */
public class NfsUtils {
	
	
	private static  NfsActSubDao actSubDao = SpringContextHolder.getBean(NfsActSubDao.class);
	private static  NfsTrxRuleDao trxRuleDao = SpringContextHolder.getBean(NfsTrxRuleDao.class);
	
	
	/**
	 * 根据交易代码获取交易名称
	 * @return
	 */
	public static String getTrxRuleName(String trxCode) {

		return trxRuleDao.getByTrxCode(trxCode).getName();
	}
	
	/**
	 * 获取科目列表
	 * @return
	 */
	public static String getSubName(String trxRole,String subNo) {
		NfsActSub nfsActSub = new NfsActSub();
		nfsActSub.setTrxRole(NfsActSub.TrxRole.valueOf(trxRole));
		nfsActSub.setSubNo(subNo);
		return actSubDao.getSubName(nfsActSub);
	}
	
	/**
	 * 获取科目列表
	 * @return
	 */
	public static List<NfsActSub> findSubList() {
		NfsActSub nfsActSub = new NfsActSub();
		return actSubDao.findList(nfsActSub);
	}
	
	/**
	 * 根据业务角色获取科目列表
	 * @return
	 */
	public static List<NfsActSub> findSubsByTrxRole(String trxRole) {
		NfsActSub nfsActSub = new NfsActSub();
		nfsActSub.setTrxRole(NfsActSub.TrxRole.valueOf(trxRole));
		return actSubDao.findList(nfsActSub);
	}
}
