package com.jxf.ufang.service.impl;

import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
import com.jxf.svc.utils.CalendarUtil;
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.StringUtils;
import com.jxf.ufang.dao.UfangLoaneeDataOrderDao;
import com.jxf.ufang.entity.UfangLoaneeDataOrder;
import com.jxf.ufang.entity.UfangUser;
import com.jxf.ufang.service.UfangLoaneeDataOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 流量订单ServiceImpl
 * @author wo
 * @version 2018-11-24
 */
@Service("ufangLoaneeDataOrderService")
@Transactional(readOnly = true)
public class UfangLoaneeDataOrderServiceImpl extends CrudServiceImpl<UfangLoaneeDataOrderDao, UfangLoaneeDataOrder> implements UfangLoaneeDataOrderService{

	@Autowired
	private UfangLoaneeDataOrderDao ufangLoaneeDataOrderDao;

	public UfangLoaneeDataOrder get(Long id) {
		return super.get(id);
	}
	
	public List<UfangLoaneeDataOrder> findList(UfangLoaneeDataOrder ufangLoaneeDataOrder) {
		return super.findList(ufangLoaneeDataOrder);
	}
	
	public Page<UfangLoaneeDataOrder> findPage(Page<UfangLoaneeDataOrder> page, UfangLoaneeDataOrder ufangLoaneeDataOrder) {
		return super.findPage(page, ufangLoaneeDataOrder);
	}
	
	@Transactional(readOnly = false)
	public void save(UfangLoaneeDataOrder ufangLoaneeDataOrder) {
		super.save(ufangLoaneeDataOrder);
	}
	
	@Transactional(readOnly = false)
	public void delete(UfangLoaneeDataOrder ufangLoaneeDataOrder) {
		super.delete(ufangLoaneeDataOrder);
	}

	@Override
	public List<UfangLoaneeDataOrder> findListByEmpNo(UfangLoaneeDataOrder ufangLoaneeDataOrder) {
		return ufangLoaneeDataOrderDao.findListByEmpNo(ufangLoaneeDataOrder);
	}

	@Override
	public List<UfangLoaneeDataOrder> findListByCompanyNo(UfangLoaneeDataOrder ufangLoaneeDataOrder) {
		return ufangLoaneeDataOrderDao.findListByCompanyNo(ufangLoaneeDataOrder);
	}

	@Override
	public Map<String, String> countByEmpNo(UfangUser user, int daysAgo) {

		Date beginTime = CalendarUtil.addDay(new Date(), -daysAgo);
		Date endTime = CalendarUtil.addDay(new Date(), -daysAgo);
		if (beginTime != null) {
			Calendar calendar = DateUtils.toCalendar(beginTime);
			calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMinimum(Calendar.HOUR_OF_DAY));
			calendar.set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE));
			calendar.set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND));
			beginTime = calendar.getTime();
		}
		if (endTime != null) {
			Calendar calendar = DateUtils.toCalendar(endTime);
			calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMaximum(Calendar.HOUR_OF_DAY));
			calendar.set(Calendar.MINUTE, calendar.getActualMaximum(Calendar.MINUTE));
			calendar.set(Calendar.SECOND, calendar.getActualMaximum(Calendar.SECOND));
			endTime = calendar.getTime();
		}
		
		UfangLoaneeDataOrder ufangLoaneeDataOrder =  new UfangLoaneeDataOrder();
		ufangLoaneeDataOrder.setUser(user);
		ufangLoaneeDataOrder.setBeginTime(beginTime);
		ufangLoaneeDataOrder.setEndTime(endTime);
	    List<UfangLoaneeDataOrder> loaneeDataOrderList= findListByEmpNo(ufangLoaneeDataOrder);
	    BigDecimal amount = BigDecimal.ZERO;
	    for(UfangLoaneeDataOrder order : loaneeDataOrderList) {
	    	amount = amount.add(order.getAmount());
	    }
	    Map<String, String> loanee_data_order_count_map = new HashMap<String, String>();
	    loanee_data_order_count_map.put("cnt", loaneeDataOrderList.size()+"");
	    loanee_data_order_count_map.put("amt", StringUtils.decimalToStr(amount, 2));
		return loanee_data_order_count_map;
	}
}