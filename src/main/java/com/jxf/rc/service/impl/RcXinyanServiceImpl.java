package com.jxf.rc.service.impl;


import java.util.Calendar;

import java.util.List;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;

import com.jxf.rc.entity.RcXinyan;
import com.jxf.rc.service.RcXinyanService;
import com.jxf.rc.dao.RcXinyanDao;
/**
 * 新颜雷达报告ServiceImpl
 * @author lmy  
 * @version 2018-12-18
 */
@Service("rcXinyanService")
@Transactional(readOnly = true)
public class RcXinyanServiceImpl extends CrudServiceImpl<RcXinyanDao, RcXinyan> implements RcXinyanService{

	public RcXinyan get(Long id) {
		return super.get(id);
	}
	
	public List<RcXinyan> findList(RcXinyan rcXinyan) {
		return super.findList(rcXinyan);
	}
	
	public Page<RcXinyan> findPage(Page<RcXinyan> page, RcXinyan rcXinyan) {
		return super.findPage(page, rcXinyan);
	}
	
	@Transactional(readOnly = false)
	public void save(RcXinyan rcXinyan) {
		super.save(rcXinyan);
	}
	
	@Transactional(readOnly = false)
	public void delete(RcXinyan rcXinyan) {
		super.delete(rcXinyan);
	}
	
	@Override
	public RcXinyan getxinyanData(RcXinyan rcXinyan) {
		RcXinyan result=null;
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH,-1);
		List<RcXinyan> findList = findList(rcXinyan);
		if(findList.size()>0) {
			result= findList.get(0);
			if (cal.getTimeInMillis() > result.getCreateTime().getTime()) {
				result=null;
			}
		}
		return result;
	} 
	
}