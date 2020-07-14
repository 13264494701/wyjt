package com.jxf.nfs.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.nfs.dao.NfsBrnActDao;
import com.jxf.nfs.entity.NfsBrnAct;
import com.jxf.nfs.service.NfsBrnActService;
import com.jxf.svc.config.Constant;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.brn.entity.Brn;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;

/**
 * 机构账户ServiceImpl
 * @author jinxinfu
 * @version 2018-06-29
 */
@Service("nfsBrnActService")
@Transactional(readOnly = true)
public class NfsBrnActServiceImpl extends CrudServiceImpl<NfsBrnActDao, NfsBrnAct> implements NfsBrnActService{

	private static Logger log = LoggerFactory.getLogger(NfsBrnActServiceImpl.class);
	
	@Autowired
	private NfsBrnActDao brnActDao;
		
	public NfsBrnAct get(Long id) {
		return super.get(id);
	}
	
	public List<NfsBrnAct> findList(NfsBrnAct nfsBrnAct) {
		return super.findList(nfsBrnAct);
	}
	
	public Page<NfsBrnAct> findPage(Page<NfsBrnAct> page, NfsBrnAct nfsBrnAct) {
		return super.findPage(page, nfsBrnAct);
	}
	
	@Transactional(readOnly = false)
	public void save(NfsBrnAct nfsBrnAct) {
		if(nfsBrnAct.getIsNewRecord()) {
			nfsBrnAct.setCurBal(new BigDecimal("0.00"));
			nfsBrnAct.setStatus(NfsBrnAct.Status.enabled);
			nfsBrnAct.preInsert();
			brnActDao.insert(nfsBrnAct);
		}else {
			nfsBrnAct.preUpdate();
			brnActDao.update(nfsBrnAct);
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(NfsBrnAct nfsBrnAct) {
		super.delete(nfsBrnAct);
	}
	/**
	 * 更新账户余额
	 */
	@Transactional(readOnly= false)
	public int updateBrnActBal(NfsBrnAct nfsBrnAct,BigDecimal trxAmt) {

		if (trxAmt.compareTo(BigDecimal.ZERO) == 0) {
			return 0;
		}

		nfsBrnAct.setCurBal(nfsBrnAct.getCurBal().add(trxAmt));
		nfsBrnAct.preUpdate();
		int numAttempts = 1;
		int updateLines = 0;
		do {
			updateLines = brnActDao.update(nfsBrnAct);// 乐观锁重试
			if (updateLines > 0 || numAttempts > Constant.MAX_ATTEMPTS) {
				break;
			} else {
				log.warn("平台账户[{}]更新失败，发起第{}次重试", nfsBrnAct.getId(), numAttempts);
				nfsBrnAct = brnActDao.get(nfsBrnAct);
				nfsBrnAct.setCurBal(nfsBrnAct.getCurBal().add(trxAmt));
				numAttempts++;
			}
		} while (updateLines == 0);
		return 0;
	}

	@Override
	public NfsBrnAct getBrnAct(Brn brn, String subNo) {
		NfsBrnAct nfsBrnAct = new NfsBrnAct();
		nfsBrnAct.setCompany(brn);
		nfsBrnAct.setSubNo(subNo);
		return brnActDao.getByCompanyAndSubNo(nfsBrnAct);
	}

}