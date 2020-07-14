package com.jxf.ufang.service.impl;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.ebaoquan.rop.thirdparty.org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.mem.entity.Member;
import com.jxf.mem.service.MemberService;
import com.jxf.mem.utils.VerifiedUtils;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
import com.jxf.svc.utils.AreaUtils;
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.IdCardUtils;
import com.jxf.ufang.dao.UfangLoaneeDataDao;
import com.jxf.ufang.entity.UfangLoanMarketApplyer;
import com.jxf.ufang.entity.UfangLoaneeData;
import com.jxf.ufang.entity.UfangLoanMarketApplyer.OperatorStatus;
import com.jxf.ufang.entity.UfangLoanMarketApplyer.SesameStatus;
import com.jxf.ufang.entity.UfangLoaneeData.YunyingshangStatus;
import com.jxf.ufang.service.UfangLoaneeDataService;
import com.jxf.ufang.util.UfangLoaneeDataUtils;

/**
 * 流量管理ServiceImpl
 * @author wo
 * @version 2018-11-24
 */
@Service("ufangLoaneeDataService")
@Transactional(readOnly = true)
public class UfangLoaneeDataServiceImpl extends CrudServiceImpl<UfangLoaneeDataDao, UfangLoaneeData> implements UfangLoaneeDataService{

	@Autowired
	private UfangLoaneeDataDao ufangLoaneeDataDao;
	@Autowired
	private MemberService memberService;

	public UfangLoaneeData get(Long id) {
		return super.get(id);
	}
	
	public List<UfangLoaneeData> findList(UfangLoaneeData ufangLoaneeData) {
		return super.findList(ufangLoaneeData);
	}
	
	public List<UfangLoaneeData> findListByEmpNo(UfangLoaneeData ufangLoaneeData) {
		return ufangLoaneeDataDao.findListByEmpNo(ufangLoaneeData);
	}
	public Page<UfangLoaneeData> findPage(Page<UfangLoaneeData> page, UfangLoaneeData ufangLoaneeData) {
		return super.findPage(page, ufangLoaneeData);
	}
	
	public Page<UfangLoaneeData> findPageByEmpNo(Page<UfangLoaneeData> page, UfangLoaneeData ufangLoaneeData) {
		if(StringUtils.equals(ufangLoaneeData.getProdCode(), "001")||StringUtils.equals(ufangLoaneeData.getProdCode(), "003")) {
			page.setOrderBy("a.apply_time DESC");
		}
		ufangLoaneeData.setPage(page);
		List<UfangLoaneeData> loaneeDataList = ufangLoaneeDataDao.findListByEmpNo(ufangLoaneeData);
		page.setList(loaneeDataList);
		return page;				
	}
	
	@Transactional(readOnly = false)
	public void save(UfangLoaneeData ufangLoaneeData) {	
		if(ufangLoaneeData.isValidPhoneNo()) {
			super.save(ufangLoaneeData);
		}	
	}
	
	@Transactional(readOnly = false)
	public void delete(UfangLoaneeData ufangLoaneeData) {
		super.delete(ufangLoaneeData);
	}
	
	@Override
	public UfangLoaneeData findByPhoneNo(String phoneNo) {

		return ufangLoaneeDataDao.findByPhoneNo(phoneNo);
	}
	
	@Override
	public int selectWeekUpdateCount(String phoneNo) {
		return ufangLoaneeDataDao.selectWeekUpdateCount(phoneNo);
	}

	@Override
	@Transactional(readOnly = false)
	public int updatesales(Long id) {
		return ufangLoaneeDataDao.updatesales(id);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void pushLoaneeData(UfangLoanMarketApplyer applyer) {
				
		UfangLoaneeData ufangLoaneeData = findByPhoneNo(applyer.getPhoneNo());
		
		if(ufangLoaneeData==null) {
			ufangLoaneeData = new UfangLoaneeData();
			ufangLoaneeData.setProdCode("001");
			ufangLoaneeData.setZhimafen(applyer.getSesameStatus().equals(SesameStatus.authed) ? applyer.getSesameScore():0);
			ufangLoaneeData.setYunyingshangStatus(applyer.getOperatorStatus().equals(OperatorStatus.authed)?YunyingshangStatus.verified:YunyingshangStatus.unverified);
			ufangLoaneeData.setChannel(UfangLoaneeData.Channel.ufang);
			ufangLoaneeData.setStatus(UfangLoaneeData.Status.fresh);
			ufangLoaneeData.setName(applyer.getName());
			ufangLoaneeData.setPhoneNo(applyer.getPhoneNo());
			ufangLoaneeData.setIdNo(applyer.getIdNo());
			ufangLoaneeData.setQqNo(applyer.getQqNo());
			ufangLoaneeData.setReportTaskId(applyer.getReportTaskId());
			// 年龄计算
			int userAge = StringUtils.isNotBlank(applyer.getIdNo())?IdCardUtils.getAge(applyer.getIdNo()):0;
			ufangLoaneeData.setAge(userAge);

			ufangLoaneeData.setApplyAmount("1000");
			ufangLoaneeData.setSales("0");
			ufangLoaneeData.setApplyIp(applyer.getApplyIp());
			ufangLoaneeData.setApplyArea(applyer.getApplyArea());
			ufangLoaneeData.setPhoneArea(applyer.getPhoneArea());
			ufangLoaneeData.setPrice(UfangLoaneeDataUtils.calPrice(applyer.getSesameScore(), userAge));
			ufangLoaneeData.setApplyTime(new Date());
			save(ufangLoaneeData);
		}else {
			ufangLoaneeData.setProdCode("001");
			ufangLoaneeData.setZhimafen(applyer.getSesameStatus().equals(SesameStatus.authed) ? applyer.getSesameScore():0);
			ufangLoaneeData.setYunyingshangStatus(applyer.getOperatorStatus().equals(OperatorStatus.authed)?YunyingshangStatus.verified:YunyingshangStatus.unverified);
			ufangLoaneeData.setChannel(UfangLoaneeData.Channel.ufang);
			ufangLoaneeData.setStatus(UfangLoaneeData.Status.fresh);
			ufangLoaneeData.setName(applyer.getName());
			ufangLoaneeData.setIdNo(applyer.getIdNo());
			ufangLoaneeData.setReportTaskId(applyer.getReportTaskId());
			// 年龄计算
			int userAge = StringUtils.isNotBlank(applyer.getIdNo())?IdCardUtils.getAge(applyer.getIdNo()):0;
			ufangLoaneeData.setAge(userAge);
			ufangLoaneeData.setApplyIp(applyer.getApplyIp());
			if(StringUtils.isNotBlank(applyer.getApplyArea())) {
				ufangLoaneeData.setApplyArea(applyer.getApplyArea());
			}
			if(StringUtils.isNotBlank(applyer.getPhoneArea())) {
				ufangLoaneeData.setPhoneArea(applyer.getPhoneArea());
			}
			ufangLoaneeData.setPrice(UfangLoaneeDataUtils.calPrice(applyer.getSesameScore(), userAge));	
			ufangLoaneeData.setApplyTime(new Date());
			save(ufangLoaneeData);
		}
		

	}

	@Override
	@Transactional(readOnly = false)
	public void pushLoaneeData(Member member,BigDecimal price) {

		member = memberService.get(member);
		if(!member.getIsAuth()) {
			return;
		}
		UfangLoaneeData ufangLoaneeData = findByPhoneNo(member.getUsername());
		Integer verifiedList = member.getVerifiedList();
		if(ufangLoaneeData==null) {
			ufangLoaneeData = new UfangLoaneeData();
			ufangLoaneeData.setProdCode("002");
			ufangLoaneeData.setZhimafen(0);
			ufangLoaneeData.setYunyingshangStatus(VerifiedUtils.isVerified(verifiedList,6)?YunyingshangStatus.verified:YunyingshangStatus.unverified);
			ufangLoaneeData.setChannel(UfangLoaneeData.Channel.wyjt);
			ufangLoaneeData.setStatus(UfangLoaneeData.Status.fresh);
			ufangLoaneeData.setName(member.getName());
			ufangLoaneeData.setPhoneNo(member.getUsername());
			ufangLoaneeData.setIdNo(member.getIdNo());
			ufangLoaneeData.setQqNo("");
			ufangLoaneeData.setReportTaskId("");
			// 年龄计算
			int userAge = StringUtils.isNotBlank(member.getIdNo())?IdCardUtils.getAge(member.getIdNo()):0;
			ufangLoaneeData.setAge(userAge);

			ufangLoaneeData.setApplyAmount("1000");
			ufangLoaneeData.setSales("0");
			ufangLoaneeData.setApplyIp(member.getLoginIp());
    		if(StringUtils.isNoneBlank(member.getLoginIp())) {
    			String ip = StringUtils.contains(member.getLoginIp(), ",")?StringUtils.substringBefore(member.getLoginIp(), ","):member.getLoginIp();
    			String applyArea = AreaUtils.ipToAreaByGeoLite2(ip);
    			ufangLoaneeData.setApplyArea(applyArea);
    		}
    		if(StringUtils.isNoneBlank(member.getUsername())) {
    			String phoneArea = AreaUtils.getAreaByPhoneNo(member.getUsername());
    			ufangLoaneeData.setPhoneArea(phoneArea);
    		}  		
			ufangLoaneeData.setPrice(price);
			ufangLoaneeData.setApplyTime(new Date());
			save(ufangLoaneeData);
		}else {
			long pastDays = DateUtils.pastDays(ufangLoaneeData.getApplyTime());
			if(pastDays<8){
				return;
			}
			ufangLoaneeData.setProdCode("002");
			ufangLoaneeData.setYunyingshangStatus(VerifiedUtils.isVerified(verifiedList,6)?YunyingshangStatus.verified:YunyingshangStatus.unverified);
			ufangLoaneeData.setChannel(UfangLoaneeData.Channel.wyjt);
			ufangLoaneeData.setStatus(UfangLoaneeData.Status.fresh);
			ufangLoaneeData.setName(member.getName());
			ufangLoaneeData.setIdNo(member.getIdNo());
			// 年龄计算
			int userAge = StringUtils.isNotBlank(member.getIdNo())?IdCardUtils.getAge(member.getIdNo()):0;
			ufangLoaneeData.setAge(userAge);
			ufangLoaneeData.setApplyIp(member.getLoginIp());

    		if(StringUtils.isNoneBlank(member.getLoginIp())) {
    			String ip = StringUtils.contains(member.getLoginIp(), ",")?StringUtils.substringBefore(member.getLoginIp(), ","):member.getLoginIp();
    			String applyArea = AreaUtils.ipToAreaByGeoLite2(ip);
    			ufangLoaneeData.setApplyArea(applyArea);
    		}
    		if(StringUtils.isBlank(ufangLoaneeData.getPhoneArea())) {
    			String phoneArea = AreaUtils.getAreaByPhoneNo(member.getUsername());
    			ufangLoaneeData.setPhoneArea(phoneArea);
    		}  	
			ufangLoaneeData.setPrice(price);	
			ufangLoaneeData.setApplyTime(new Date());
			save(ufangLoaneeData);
		}
	}


}