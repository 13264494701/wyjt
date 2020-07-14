package com.jxf.ufang.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.nfs.constant.ActSubConstant;
import com.jxf.svc.sys.tree.service.impl.TreeServiceImpl;
import com.jxf.svc.sys.util.UserUtils;
import com.jxf.svc.utils.StringUtils;
import com.jxf.ufang.dao.UfangBrnDao;
import com.jxf.ufang.entity.UfangBrn;
import com.jxf.ufang.entity.UfangBrnAct;
import com.jxf.ufang.entity.UfangUser;
import com.jxf.ufang.service.UfangBrnActService;
import com.jxf.ufang.service.UfangBrnService;
import com.jxf.ufang.service.UfangRoleService;
import com.jxf.ufang.service.UfangUserService;
import com.jxf.ufang.util.UfangUserUtils;



/**
 * 机构Service
 * @author jxf
 * @version 2015-07-28
 */
@Service("ufangBrnService")
@Transactional(readOnly = true)
public class UfangBrnServiceImpl extends TreeServiceImpl<UfangBrnDao, UfangBrn> implements UfangBrnService{

	@Autowired
	private UfangBrnDao brnDao;
	@Autowired
	private UfangBrnActService brnActService;
	@Autowired
	private UfangUserService userService;
	@Autowired
	private UfangRoleService roleService;
	
	public List<UfangBrn> findAll(){
		return UfangUserUtils.getBrnList();
	}

	public List<UfangBrn> findList(Boolean isAll){
		if (isAll != null && isAll){
			return brnDao.findAllList(new UfangBrn());
		}else{
			return UfangUserUtils.getBrnList();
		}
	}
	
	@Transactional(readOnly = true)
	public List<UfangBrn> findList(UfangBrn brn){
		return brnDao.findList(brn);
	}

	@Override
	@Transactional(readOnly = false)
	public int updateFreeData(int freeData, String brnNo) {
		return brnDao.updateFreeData(freeData,brnNo);
	}

	@Transactional(readOnly = false)
	public void save(UfangBrn brn) {
		if(brn.getIsNewRecord()) {
			UfangBrn pBrn = brnDao.get(brn.getParent());
			String brnNo = generateBrnNo(pBrn);
			brn.setBrnNo(brnNo);
			brn.setGrade(pBrn.getGrade() + 1);
			brn.setFreeData(0);
			brn.setIsLocked(false);
			super.save(brn);
			//如果是公司级别，创建公司账户,创建默认部门及负责人用户
			if(brn.getGrade()==2) {
				//
				UfangBrnAct brnAct = new UfangBrnAct();
				brnAct.setCompany(brn);
				brnAct.setSubNo(ActSubConstant.UFANG_BRN_AVL_BAL);
				brnAct.setCurrCode("CNY");
				brnActService.save(brnAct);
				//创建默认部门
				UfangBrn cBrn =  new UfangBrn();
				String cBrnNo = generateBrnNo(brn);
				cBrn.setBrnNo(cBrnNo);
				cBrn.setBrnName("管理部");
				cBrn.setType(UfangBrn.Type.predefine);
				cBrn.setGrade(brn.getGrade() + 1);
				cBrn.setParent(brn);
				cBrn.setArea(brn.getArea());
				cBrn.setIsLocked(false);
				super.save(cBrn);
				//负责人用户
				UfangUser user = new UfangUser();
				user.setType(UfangUser.Type.predefine);
				user.setBrn(cBrn);
				user.setEmpNam(brn.getPrimaryPerson());
				user.setUsername(brn.getPhoneNo());
				user.setRoleList(roleService.findAllRole());
				userService.saveUser(user);
				
			}
		}else {
			 super.save(brn);
		}
	   
		UserUtils.removeCache(UfangUserUtils.CACHE_BRN_LIST);
	}
	
	@Transactional(readOnly = false)
	public void delete(UfangBrn brn) {
		UfangBrnAct brnAct = new UfangBrnAct();
		brnAct.setCompany(brn);
		List<UfangBrnAct> actList = brnActService.findList(brnAct);
		for(UfangBrnAct act:actList) {
			brnActService.delete(act);
		}	
		super.delete(brn);
		UserUtils.removeCache(UfangUserUtils.CACHE_BRN_LIST);
	}
	
	public String generateBrnNo(UfangBrn pBrn){
		String no = "";
		Integer brnGrade = pBrn.getGrade();
		String parentNo = pBrn.getBrnNo();
		String maxChildNo= brnDao.getChildMaxNo(pBrn.getId());
		
		if(brnGrade==1){
			if(StringUtils.isNullOrEmpty(maxChildNo)){
				String subStr = parentNo.substring(0, 4);				
				no = String.format("%04x", Long.parseLong(subStr,16)+1).toUpperCase() + "00";
			}else{
				String subStr = maxChildNo.substring(0, 4);
				no = String.format("%04x", Long.parseLong(subStr,16)+1).toUpperCase() + "00";
			}

		}else if(brnGrade==2){
			if(!StringUtils.isNullOrEmpty(maxChildNo)){
				no = parentNo.substring(0, 4)+String.format("%02x", Long.parseLong(maxChildNo.substring(4, 6),16)+1).toUpperCase();
			}else{
				no = parentNo.substring(0, 4)+"01";
			}
			
		}
		return no;
	}
	
}
