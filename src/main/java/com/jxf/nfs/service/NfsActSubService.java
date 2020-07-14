package com.jxf.nfs.service;

import java.util.List;

import com.jxf.nfs.entity.NfsActSub;
import com.jxf.nfs.entity.NfsActSub.TrxRole;
import com.jxf.svc.sys.crud.service.CrudService;


/**
 * 账户科目Service
 * @author wo
 * @version 2018-09-18
 */
public interface NfsActSubService extends CrudService<NfsActSub> {

	
	public  List<NfsActSub> findSubsByTrxRole(TrxRole role);
	
}