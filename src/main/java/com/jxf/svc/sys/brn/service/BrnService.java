package com.jxf.svc.sys.brn.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.svc.sys.brn.dao.BrnDao;
import com.jxf.svc.sys.brn.entity.Brn;
import com.jxf.svc.sys.tree.service.impl.TreeServiceImpl;
import com.jxf.svc.sys.util.UserUtils;
import com.jxf.svc.utils.StringUtils;



/**
 * 机构Service
 * @author jxf
 * @version 2015-07-28
 */
@Service
@Transactional(readOnly = true)
public class BrnService extends TreeServiceImpl<BrnDao, Brn> {

	@Autowired
	private BrnDao brnDao;
	
	public List<Brn> findAll(){
		return UserUtils.getBrnList();
	}

	public List<Brn> findList(Boolean isAll){
		if (isAll != null && isAll){
			return UserUtils.getBrnAllList();
		}else{
			return UserUtils.getBrnList();
		}
	}
	
	@Transactional(readOnly = true)
	public List<Brn> findList(Brn brn){
		brn.setParentIds(brn.getParentIds()+"%");
		return brnDao.findByParentIdsLike(brn);
	}
	
	@Transactional(readOnly = false)
	public void save(Brn brn) {
		if(brn.getIsNewRecord()) {
			Brn pBrn = brnDao.get(brn.getParent());
			String brnNo = generateBrnNo(pBrn);
			brn.setBrnNo(brnNo);
			brn.setBrnGrade((Integer.valueOf(pBrn.getBrnGrade()) + 1)+"");
		}
	    super.save(brn);
		UserUtils.removeCache(UserUtils.CACHE_BRN_LIST);
	}
	
	@Transactional(readOnly = false)
	public void delete(Brn brn) {
		super.delete(brn);
		UserUtils.removeCache(UserUtils.CACHE_BRN_LIST);
	}
	
	public String generateBrnNo(Brn pBrn){
		String no = "";
		String brnGrade = pBrn.getBrnGrade();
		String parentNo = pBrn.getBrnNo();
		String maxChildNo= brnDao.getChildMaxNo(pBrn.getId());
		
		if(StringUtils.equals(brnGrade, "1")){
			if(StringUtils.isNullOrEmpty(maxChildNo)){
				String subStr = parentNo.substring(0, 2);				
				no = String.format("%02x", Long.parseLong(subStr,16)+1).toUpperCase() + "00";
			}else{
				String subStr = maxChildNo.substring(0, 2);
				no = String.format("%02x", Long.parseLong(subStr,16)+1).toUpperCase() + "00";
			}

		}else if(StringUtils.equals(brnGrade, "2")){
			if(!StringUtils.isNullOrEmpty(maxChildNo)){
				no = parentNo.substring(0, 2)+String.format("%02x", Long.parseLong(maxChildNo.substring(2, 4),16)+1).toUpperCase();
			}else{
				no = parentNo.substring(0, 2)+"01";
			}
			
		}
		return no;
	}
	
}
