package com.jxf.ufang.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.jxf.mem.entity.Member;
import com.jxf.mem.utils.VerifiedUtils;
import com.jxf.rc.entity.RcCaData;
import com.jxf.rc.entity.RcCaSourceData;
import com.jxf.rc.entity.RcSjmh;
import com.jxf.rc.service.RcCaDataService;
import com.jxf.rc.service.RcCaSourceDataService;
import com.jxf.rc.service.RcSjmhService;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
import com.jxf.svc.utils.CalendarUtil;
import com.jxf.svc.utils.Collections3;
import com.jxf.svc.utils.StringUtils;
import com.jxf.ufang.dao.UfangLoanMarketApplyerDao;
import com.jxf.ufang.entity.UfangLoanMarketApplyer;
import com.jxf.ufang.entity.UfangLoanMarketApplyer.OperatorStatus;
import com.jxf.ufang.entity.UfangLoanMarketApplyer.RealNameStatus;
import com.jxf.ufang.entity.UfangLoanMarketApplyer.SesameStatus;
import com.jxf.ufang.service.UfangLoanMarketApplyerService;
/**
 * 优放贷申请人ServiceImpl
 * @author suHuimin
 * @version 2019-03-27
 */
@Service("ufangLoanMarketApplyerService")
@Transactional(readOnly = true)
public class UfangLoanMarketApplyerServiceImpl extends CrudServiceImpl<UfangLoanMarketApplyerDao, UfangLoanMarketApplyer> implements UfangLoanMarketApplyerService{

	@Autowired
	private UfangLoanMarketApplyerDao applyerDao;
	@Autowired
	private RcSjmhService sjmhService;
	@Autowired
	private RcCaDataService rcCaDataService;
	@Autowired
	private RcCaSourceDataService rcCaSourceDataService;
	
    @Override
	public UfangLoanMarketApplyer get(Long id) {
		return super.get(id);
	}
	
	@Override
	public List<UfangLoanMarketApplyer> findList(UfangLoanMarketApplyer ufangLoanMarketApplyer) {
		return super.findList(ufangLoanMarketApplyer);
	}
	
	@Override
	public Page<UfangLoanMarketApplyer> findPage(Page<UfangLoanMarketApplyer> page, UfangLoanMarketApplyer ufangLoanMarketApplyer) {
		return super.findPage(page, ufangLoanMarketApplyer);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void save(UfangLoanMarketApplyer ufangLoanMarketApplyer) {
		if(ufangLoanMarketApplyer.getIsNewRecord()) {
			ufangLoanMarketApplyer.setApplyTimes(0);
		}
		super.save(ufangLoanMarketApplyer);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void delete(UfangLoanMarketApplyer ufangLoanMarketApplyer) {
		super.delete(ufangLoanMarketApplyer);
	}

	@Override
	@Transactional(readOnly = false)
	public UfangLoanMarketApplyer saveApplyerByMemberInfo(UfangLoanMarketApplyer applyer,Member member) {
		applyer.setAppRegister("1");
		applyer.setMember(member);
		Integer verifiedList = member.getVerifiedList();
		boolean zmfAuth = VerifiedUtils.isVerified(verifiedList, 4);
		boolean yysAuth = VerifiedUtils.isVerified(verifiedList, 6);

		if (VerifiedUtils.isVerified(verifiedList, 1) && VerifiedUtils.isVerified(verifiedList, 2)) {
			// 已实名认证
			String idNo = member.getIdNo();
			applyer.setIdNo(idNo);
			applyer.setName(member.getName());
			applyer.setRealNameStatus(RealNameStatus.authed);
		}else {
			// 没有实名认证
			applyer.setRealNameStatus(RealNameStatus.unauth);
		}
		if (zmfAuth) {
			// 获取申请人的芝麻分
			RcCaData rcCaData = new RcCaData();
			rcCaData.setType(RcCaData.Type.zhimafen);
			rcCaData.setPhoneNo(applyer.getPhoneNo());
			List<RcCaData> rcCaDataList = rcCaDataService.findList(rcCaData);
			RcCaData rcCaData2 = rcCaDataList.get(0);
			Date authTime = rcCaData2.getCreateTime();
			if(CalendarUtil.addDay(authTime, 30).compareTo(new Date()) < 0) {
				//已过期 重新认证
				applyer.setSesameStatus(SesameStatus.unauth);
			}else {
				String authResult = rcCaData2.getContent();
				JSONObject jsonObject = JSONObject.parseObject(authResult);
				Integer sesameScore = jsonObject.getInteger("zmFen");
				applyer.setSesameScore(sesameScore);
				applyer.setSesameStatus(SesameStatus.authed);
			}
		}else {
			applyer.setSesameStatus(SesameStatus.unauth);
		}

		if (yysAuth) {
			//运营商均已认证 直接申请成功
			// 获取运营商认证报告地址
			RcSjmh rcSjmh = sjmhService.findByPhoneNoChannelTypeDataStatus(applyer.getPhoneNo(),RcSjmh.ChannelType.yunyingshang,RcSjmh.DataStatus.data_created);
		    if(rcSjmh!=null){
		    	applyer.setReportTaskId(rcSjmh.getTaskId());
		    	applyer.setOperatorStatus(OperatorStatus.authed);
		    }else {
				RcCaSourceData rcCaSourceData = new RcCaSourceData();
				rcCaSourceData.setMemberId(member.getId());
				rcCaSourceData.setType(RcCaData.Type.yunyingshang);
				List<RcCaSourceData> rcCaSourceDataList = rcCaSourceDataService.findList(rcCaSourceData);
				RcCaSourceData sourceData = null;
				if (rcCaSourceDataList == null || (rcCaSourceDataList != null && rcCaSourceDataList.size() == 0)) {
					logger.error("会员{}已认证运营商，但是没有取到数据！", member.getId());
					applyer.setOperatorStatus(OperatorStatus.unauth);
				} else {
					sourceData = rcCaSourceDataList.get(0);
					String conten = sourceData.getContent();
					JSONObject operatorData = JSONObject.parseObject(conten);
					String taskId = operatorData.getString("task_id");
					applyer.setReportTaskId(taskId);
					applyer.setOperatorStatus(OperatorStatus.authed);
				}
		    }
			
		} else {	
			applyer.setOperatorStatus(OperatorStatus.unauth);
		}

		save(applyer);
		return applyer;
	}

	@Override
	public int getCountByCondition(UfangLoanMarketApplyer applyer) {
		return applyerDao.getCountByCondition(applyer);
	}

	@Override
	@Transactional(readOnly = false)
	public UfangLoanMarketApplyer getApplyerAuthStatus(UfangLoanMarketApplyer applyer, Member member) {
		Integer verifiedList = member.getVerifiedList();
		if((VerifiedUtils.isVerified(verifiedList, 1) && VerifiedUtils.isVerified(verifiedList, 2))) {
			applyer.setRealNameStatus(RealNameStatus.authed);
		}else {
			applyer.setRealNameStatus(RealNameStatus.unauth);
		}
			
		if(applyer.getSesameStatus().equals(UfangLoanMarketApplyer.SesameStatus.unauth)) {
			if( VerifiedUtils.isVerified(verifiedList, 4)) {
				RcCaData rcCaData = new RcCaData();
				rcCaData.setType(RcCaData.Type.zhimafen);
				rcCaData.setPhoneNo(applyer.getPhoneNo());
				List<RcCaData> rcCaDataList = rcCaDataService.findList(rcCaData);
				if(Collections3.isEmpty(rcCaDataList)) {
					logger.warn("会员{}申请优放贷芝麻分认证已过期或未认证！",member.getId());
					applyer.setSesameStatus(SesameStatus.unauth);
				}else {
					RcCaData rcCaData2 = rcCaDataList.get(0);
					String authResult = rcCaData2.getContent();
					JSONObject jsonObject = JSONObject.parseObject(authResult);
					Integer sesameScore = jsonObject.getInteger("zmFen");
					applyer.setSesameStatus(SesameStatus.authed);
					applyer.setSesameScore(sesameScore);
				}
			}else {
				logger.warn("芝麻分未认证或认证已过期，请认证后再操作!");
				applyer.setSesameStatus(SesameStatus.unauth);
			}
		}else {
			if(applyer.getSesameScore() <= 0) {
				logger.warn("芝麻分未认证或认证已过期，请认证后再操作!");
				applyer.setSesameStatus(SesameStatus.unauth);
			}
		}
			
		if(applyer.getOperatorStatus().equals(UfangLoanMarketApplyer.OperatorStatus.unauth)) {				
				RcSjmh rcSjmh = sjmhService.findByPhoneNoChannelTypeDataStatus(applyer.getPhoneNo(),RcSjmh.ChannelType.yunyingshang,RcSjmh.DataStatus.data_arranged);
			    if(rcSjmh!=null){
			    	applyer.setReportTaskId(rcSjmh.getTaskId());
			    	applyer.setOperatorStatus(OperatorStatus.authed);
			    }else {		
					RcCaSourceData rcCaSourceData = new RcCaSourceData();
					rcCaSourceData.setMemberId(member.getId());
					rcCaSourceData.setType(RcCaData.Type.yunyingshang);
					List<RcCaSourceData> rcCaSourceDataList = rcCaSourceDataService.findList(rcCaSourceData);
					RcCaSourceData sourceData = null;
					if(rcCaSourceDataList == null || (rcCaSourceDataList != null && rcCaSourceDataList.size() == 0)) {
						logger.error("会员{}已认证运营商，但是没有取到数据！",member.getId());
						applyer.setOperatorStatus(OperatorStatus.unauth);
					}else {
						sourceData = rcCaSourceDataList.get(0);
						String conten = sourceData.getContent();
						JSONObject operatorData = JSONObject.parseObject(conten);
						String taskId = operatorData.getString("task_id");
						applyer.setOperatorStatus(OperatorStatus.authed);
						applyer.setReportTaskId(taskId);
					}
			    }
		}			
		if(StringUtils.isBlank(applyer.getReportTaskId())) {
			logger.error("运营商未认证或认证已过期，请认证后再操作!");
			applyer.setOperatorStatus(OperatorStatus.unauth);
		}
		return applyer;
	}

	@Override
	public int selectWeekUpdateCount(Long marketId,String phoneNo) {
		return applyerDao.selectWeekUpdateCount(marketId,phoneNo);
	}

	@Override
	public UfangLoanMarketApplyer getByPhoneNoAndMarketId(UfangLoanMarketApplyer applyer) {
		return applyerDao.getByPhoneNoAndMarketId(applyer);
	}
}