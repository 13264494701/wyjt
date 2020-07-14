package com.jxf.nfs.service;

import com.jxf.nfs.entity.NfsBankInfo;
import com.jxf.svc.sys.crud.service.CrudService;


/**
 * 银行编码Service
 *
 * @author wo
 * @version 2018-09-29
 */
public interface NfsBankInfoService extends CrudService<NfsBankInfo> {

    
    NfsBankInfo getBankInfoByAbbrName(String abbrName);

}