package com.jxf.ufang.service;

import java.math.BigDecimal;
import java.util.List;

import com.jxf.mem.entity.Member;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.CrudService;
import com.jxf.ufang.entity.UfangLoanMarketApplyer;
import com.jxf.ufang.entity.UfangLoaneeData;

/**
 * 流量管理Service
 * @author wo
 * @version 2018-11-24
 */
public interface UfangLoaneeDataService extends CrudService<UfangLoaneeData> {

	UfangLoaneeData findByPhoneNo(String phoneNo);
	
    int selectWeekUpdateCount(String phoneNo);
    
    int updatesales(Long id);

    /**
     * @description 将来自贷超的申请人转换为流量
     * @param applyer
     */
    void pushLoaneeData(UfangLoanMarketApplyer applyer);
    
    /**
     * @description 平台借款人转换为优淘流量
     * @param member
     */
    void pushLoaneeData(Member member,BigDecimal price);
    
    
    List<UfangLoaneeData> findListByEmpNo(UfangLoaneeData ufangLoaneeData);
    
    Page<UfangLoaneeData> findPageByEmpNo(Page<UfangLoaneeData> page, UfangLoaneeData ufangLoaneeData);
}