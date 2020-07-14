package com.jxf.rc.service;


import com.jxf.svc.sys.crud.service.CrudService;

import com.jxf.rc.entity.RcTxxy;

/**
 * 风控 天下信用Service
 * @author wo
 * @version 2019-6-16
 */
public interface RcTxxyService extends CrudService<RcTxxy> {

	RcTxxy findByReportNo(String reportNo);
    
    
    void saveTaskReport(RcTxxy rcTxxy);
	
}