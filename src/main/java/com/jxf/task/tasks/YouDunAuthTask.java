package com.jxf.task.tasks;
import com.jxf.loan.entity.NfsCrAuction;
import com.jxf.loan.entity.NfsCrContract;
import com.jxf.loan.entity.NfsLoanContract;
import com.jxf.loan.entity.NfsLoanRecord;
import com.jxf.loan.service.NfsCrAuctionService;
import com.jxf.loan.service.NfsCrContractService;
import com.jxf.loan.service.NfsLoanContractService;
import com.jxf.loan.service.NfsLoanRecordService;
import com.jxf.svc.config.Global;
import com.jxf.svc.utils.Collections3;
import com.jxf.svc.utils.Exceptions;

import java.util.List;

import org.apache.commons.codec.binary.StringUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 认证签章任务
 *
 * @author Administrator
 */
@DisallowConcurrentExecution
public class YouDunAuthTask implements Job {
	private static final Logger logger = LoggerFactory.getLogger(SendWithdrawTask.class);
	@Autowired
	private NfsLoanRecordService loanRecordService;
	@Autowired
	private NfsLoanContractService loanContractService;
	@Autowired
	private NfsCrAuctionService crAuctionService;
	@Autowired
	private NfsCrContractService crContractService;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		if (StringUtils.equals(Global.getConfig("contractSwitch"), "ON")) {

			// 借款单签章
			NfsLoanContract loanContract = new NfsLoanContract();
			loanContract.setCount(10);
			loanContract.setSignatureType(NfsLoanContract.SignatureType.youdun);
			loanContract.setStatus(NfsLoanContract.Status.notCreate);
			List<NfsLoanContract> unsignLoans = loanContractService.findList(loanContract);
			if (!Collections3.isEmpty(unsignLoans)) {
				for (NfsLoanContract item : unsignLoans) {
					NfsLoanRecord nfsLoanRecord = loanRecordService.get(item.getLoanId());
					loanContractService.createYDPdfV2(nfsLoanRecord, item.getId());
				}
			}
			logger.info("============================开始债转签章");;
			NfsCrContract crContract = new NfsCrContract();
		 	crContract.setSignatureType(NfsCrContract.SignatureType.youdun);
		 	crContract.setStatus(NfsCrContract.Status.notCreate);
		 	crContract.setCount(500);
	    	List<NfsCrContract> unsignCrAuctions = crContractService.findList(crContract);
			if(unsignCrAuctions != null && unsignCrAuctions.size()>0){
				for(NfsCrContract item : unsignCrAuctions){
					try{
						NfsCrAuction crAuction = crAuctionService.get(item.getCrId());
						crContractService.createYDPdf(crAuction);
					}catch (Exception e){
						logger.error("债转合同签章错误：{}",Exceptions.getStackTraceAsString(e));
						
					}
				}
			}
			try {
				Thread.sleep(1000 * 60);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			logger.error("==================债转开始下载=================");
			
			// 下载PDF
			NfsLoanContract nfsLoanContract1 = new NfsLoanContract();
			nfsLoanContract1.setCount(10);
			nfsLoanContract1.setSignatureType(NfsLoanContract.SignatureType.youdun);
			nfsLoanContract1.setStatus(NfsLoanContract.Status.created);
			// 传个参数判断出是否已经盖章
			List<NfsLoanContract> loanContracts = loanContractService.findList(nfsLoanContract1);
			if (!Collections3.isEmpty(loanContracts)) {
				for (NfsLoanContract item : loanContracts) {
					loanContractService.updateYDUrlV2(item);
				}
			}
			logger.info("=========有盾债转合同下载开始=====");
			NfsCrContract crContract1 = new NfsCrContract();
			crContract1.setSignatureType(NfsCrContract.SignatureType.youdun);
			crContract1.setStatus(NfsCrContract.Status.created);
			crContract1.setCount(500);
		 	// 传个参数判断出是否已经盖章
	    	List<NfsCrContract> crContracts = crContractService.findList(crContract1);
			if(crContracts != null && crContracts.size() > 0){
				for(NfsCrContract item : crContracts){
					try{
						crContractService.updateYDUrl(item.getId());
					}catch (Exception e){
						logger.error("债转合同下载错误：{}",Exceptions.getStackTraceAsString(e));
					}
				}
			}
		}

	}

}