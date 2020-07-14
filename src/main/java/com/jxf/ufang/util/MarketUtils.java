package com.jxf.ufang.util;

import java.util.List;


import com.jxf.svc.init.SpringContextHolder;
import com.jxf.ufang.dao.UfangLoanMarketDao;
import com.jxf.ufang.entity.UfangLoanMarket;

/**
 * 贷超工具类
 * @author jxf
 * @version 2019-05-28
 */
public class MarketUtils {

	private static UfangLoanMarketDao marketDao = SpringContextHolder.getBean(UfangLoanMarketDao.class);


	/**
	 * 获取所有贷超
	 * @return
	 */
	public static List<UfangLoanMarket> findMarketList(){
		List<UfangLoanMarket> markets = marketDao.findList(new UfangLoanMarket());
		return markets;
	}

}
