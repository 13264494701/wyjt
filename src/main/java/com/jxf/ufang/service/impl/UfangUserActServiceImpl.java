package com.jxf.ufang.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.svc.config.Constant;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
import com.jxf.ufang.dao.UfangUserActDao;
import com.jxf.ufang.entity.UfangUser;
import com.jxf.ufang.entity.UfangUserAct;

import com.jxf.ufang.service.UfangUserActService;


/**
 * 优放账户ServiceImpl
 * @author wo
 * @version 2018-06-29
 */
@Service("ufangUserActService")
@Transactional(readOnly = true)
public class UfangUserActServiceImpl extends CrudServiceImpl<UfangUserActDao, UfangUserAct> implements UfangUserActService{

	private static Logger log = LoggerFactory.getLogger(UfangBrnActServiceImpl.class);
	@Autowired
	private UfangUserActDao userActDao;
	
	public UfangUserAct get(Long id) {
		return super.get(id);
	}
	
	public List<UfangUserAct> findList(UfangUserAct ufangUserAct) {
		return super.findList(ufangUserAct);
	}
	
	public Page<UfangUserAct> findPage(Page<UfangUserAct> page, UfangUserAct ufangUserAct) {
		return super.findPage(page, ufangUserAct);
	}
	
	@Transactional(readOnly = false)
	public void save(UfangUserAct ufangUserAct) {
		if(ufangUserAct.getIsNewRecord()) {
			ufangUserAct.setCurBal(new BigDecimal("0.00"));
			ufangUserAct.setStatus(UfangUserAct.Status.enabled);
			ufangUserAct.preInsert();
			userActDao.insert(ufangUserAct);
		}else {
			ufangUserAct.preUpdate();
			int numAttempts = 0;
			do {
				numAttempts++;			
				int updateLines = userActDao.update(ufangUserAct);// 乐观锁重试
				if(updateLines>0) {break;}
			} while (numAttempts <= 10);
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(UfangUserAct ufangUserAct) {
		super.delete(ufangUserAct);
	}


	@Override
	public UfangUserAct getUserAct(UfangUser user, String subNo) {
		UfangUserAct ufangUserAct = new UfangUserAct();
		ufangUserAct.setUser(user);
		ufangUserAct.setSubNo(subNo);
		return userActDao.getByUserAndSubNo(ufangUserAct);
	}

	@Override
	@Transactional(readOnly=false)
	public int updateUfangUserAct(UfangUserAct ufangUserAct, BigDecimal trxAmt) {
		if (trxAmt.compareTo(BigDecimal.ZERO) == 0) {
			return 0;
		}

		ufangUserAct.setCurBal(ufangUserAct.getCurBal().add(trxAmt));
		ufangUserAct.preUpdate();
		int numAttempts = 1;
		int updateLines = 0;
		do {
			updateLines = userActDao.update(ufangUserAct);// 乐观锁重试
			if (updateLines > 0 || numAttempts > Constant.MAX_ATTEMPTS) {
				break;
			} else {
				log.warn("优放用户账户[{}]更新失败，发起第{}次重试", ufangUserAct.getId(), numAttempts);
				ufangUserAct = userActDao.get(ufangUserAct);
				ufangUserAct.setCurBal(ufangUserAct.getCurBal().add(trxAmt));
				numAttempts++;
			}
		} while (updateLines == 0);
		return 0;
	}

}