package com.jxf.rc.service;

import com.jxf.svc.sys.crud.service.CrudService;
import com.jxf.rc.entity.RcXinyan;

/**
 * 新颜雷达报告Service
 * @author lmy
 * @version 2018-12-18
 */
public interface RcXinyanService extends CrudService<RcXinyan> {
	
	/**
	 * 查看自己是否购买过新颜雷达报告
	 *@return
	 */
	RcXinyan getxinyanData(RcXinyan rcXinyan);
	
	
}