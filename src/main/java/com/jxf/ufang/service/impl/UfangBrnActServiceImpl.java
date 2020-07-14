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
import com.jxf.ufang.dao.UfangBrnActDao;

import com.jxf.ufang.entity.UfangBrn;
import com.jxf.ufang.entity.UfangBrnAct;
import com.jxf.ufang.service.UfangBrnActService;

/**
 * 优放账户ServiceImpl
 * @author wo
 * @version 2018-06-29
 */
@Service("ufangBrnActService")
@Transactional(readOnly = true)
public class UfangBrnActServiceImpl extends CrudServiceImpl<UfangBrnActDao, UfangBrnAct> implements UfangBrnActService{

	private static Logger log = LoggerFactory.getLogger(UfangBrnActServiceImpl.class);
	
	@Autowired
	private UfangBrnActDao brnActDao;
	
	public UfangBrnAct get(Long id) {
		return super.get(id);
	}
	
	public List<UfangBrnAct> findList(UfangBrnAct ufangBrnAct) {
		return super.findList(ufangBrnAct);
	}
	
	public Page<UfangBrnAct> findPage(Page<UfangBrnAct> page, UfangBrnAct ufangBrnAct) {
		return super.findPage(page, ufangBrnAct);
	}
	
	@Transactional(readOnly = false)
	public void save(UfangBrnAct ufangBrnAct) {
		if(ufangBrnAct.getIsNewRecord()) {
			ufangBrnAct.setCurBal(BigDecimal.ZERO);
			ufangBrnAct.setStatus(UfangBrnAct.Status.enabled);
			ufangBrnAct.preInsert();
			brnActDao.insert(ufangBrnAct);
		}else {
			ufangBrnAct.preUpdate();
			int numAttempts = 0;
			do {
				numAttempts++;			
				int updateLines = brnActDao.update(ufangBrnAct);// 乐观锁重试
				if(updateLines>0) {break;}
			} while (numAttempts <= 10);
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(UfangBrnAct ufangBrnAct) {
		super.delete(ufangBrnAct);
	}


	@Override
	public UfangBrnAct getBrnAct(UfangBrn brn, String subNo) {
		UfangBrnAct ufangBrnAct = new UfangBrnAct();
		ufangBrnAct.setCompany(brn);
		ufangBrnAct.setSubNo(subNo);
		return brnActDao.getByCompanyAndSubNo(ufangBrnAct);
	}

	@Override
	@Transactional(readOnly=false)
	public int updateUfangBrnAct(UfangBrnAct brnAct, BigDecimal trxAmt) {

		if(trxAmt.compareTo(BigDecimal.ZERO)==0) {return 0;}
		brnAct.setCurBal(brnAct.getCurBal().add(trxAmt));	
		brnAct.preUpdate();
		int numAttempts = 1;
		int updateLines = 0;
		do {		
			updateLines = brnActDao.update(brnAct);// 乐观锁重试				
			if(updateLines > 0 || numAttempts > Constant.MAX_ATTEMPTS) {
				break;
			}else {
				log.warn("优放机构账户[{}]更新失败，发起第{}次重试",brnAct.getId(),numAttempts);
				brnAct = brnActDao.get(brnAct);
				brnAct.setCurBal(brnAct.getCurBal().add(trxAmt));	
				numAttempts++;
			}											
		} while (updateLines == 0);
		return 0;
	}
}