package com.jxf.rc.service;

import com.jxf.svc.sys.crud.service.CrudService;

import java.util.List;

import com.jxf.rc.entity.RcGxb;

/**
 * 风控 公信宝Service
 * @author Administrator
 * @version 2018-10-16
 */
public interface RcGxbService extends CrudService<RcGxb> {
	List<RcGxb> findListWithoutEmpNo(RcGxb rcGxb);
    RcGxb findByToken(String token);
    void saveTaskData(RcGxb rcGxb);
    void saveTaskReport(RcGxb rcGxb);
}