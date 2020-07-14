package com.jxf.ufang.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;

import com.jxf.svc.model.HandleRsp;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
import com.jxf.ufang.dao.UfangLoaneeRcOrderDao;
import com.jxf.ufang.entity.UfangBrn;
import com.jxf.ufang.entity.UfangLoaneeRcOrder;
import com.jxf.ufang.entity.UfangUser;
import com.jxf.ufang.service.UfangLoaneeRcOrderService;
/**
 * 风控订单ServiceImpl
 * @author wo
 * @version 2019-07-14
 */
@Service("rcDataOrderService")
@Transactional(readOnly = true)
public class UfangLoaneeRcOrderServiceImpl extends CrudServiceImpl<UfangLoaneeRcOrderDao, UfangLoaneeRcOrder> implements UfangLoaneeRcOrderService{

	@Autowired
	private UfangLoaneeRcOrderDao orderDao;
	
    @Override
	public UfangLoaneeRcOrder get(Long id) {
		return super.get(id);
	}
	
	@Override
	public List<UfangLoaneeRcOrder> findList(UfangLoaneeRcOrder rcDataOrder) {
		return super.findList(rcDataOrder);
	}
	
	@Override
	public Page<UfangLoaneeRcOrder> findPage(Page<UfangLoaneeRcOrder> page, UfangLoaneeRcOrder rcDataOrder) {
		return super.findPage(page, rcDataOrder);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void save(UfangLoaneeRcOrder rcDataOrder) {
		super.save(rcDataOrder);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void delete(UfangLoaneeRcOrder rcDataOrder) {
		super.delete(rcDataOrder);
	}

	@Override
	public Page<UfangLoaneeRcOrder> findMemberOrderPage(UfangUser user, UfangLoaneeRcOrder.Status status, Integer pageNo, Integer pageSize) {

		Page<UfangLoaneeRcOrder> page = new Page<UfangLoaneeRcOrder>(pageNo == null?1:pageNo, pageSize == null?10:pageSize);	
		UfangLoaneeRcOrder order = new UfangLoaneeRcOrder();	
		page.setOrderBy("a.id DESC");
		order.setPage(page);
		order.setUser(user);
		order.setStatus(status);
		List<UfangLoaneeRcOrder> orderList = orderDao.findList(order);
		page.setList(orderList);
		return page;
	}

	@Override
	public HandleRsp queryRcData(UfangBrn brn, JSONObject dataJson) {

		String qName = dataJson.getString("qName");
		String qPhoneNo = dataJson.getString("qPhoneNo");
		String qIdNo = dataJson.getString("qIdNo");
		return null;
	}
	
}