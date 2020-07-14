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
public class JunZiQianAuthTask implements Job {
	
	private static final Logger log = LoggerFactory.getLogger(JunZiQianAuthTask.class);
	
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
		
		if(StringUtils.equals(Global.getConfig("contractSwitch"), "ON")) {
	        log.info("=========君子签借条合同生成开始=====");
	    	//借款单签章
		 	NfsLoanContract loanContract = new NfsLoanContract();
		 	loanContract.setCount(500);
		 	loanContract.setSignatureType(NfsLoanContract.SignatureType.junziqian);
		 	loanContract.setStatus(NfsLoanContract.Status.notCreate);
	    	List<NfsLoanContract> unsignLoans = loanContractService.findList(loanContract);
			if(unsignLoans != null && unsignLoans.size()>0){
				for(NfsLoanContract item : unsignLoans){
					try{
						NfsLoanRecord nfsLoanRecord = loanRecordService.get(item.getLoanId());
						loanContractService.createPdf(nfsLoanRecord,item.getId());
					}catch (Exception e){
						log.error("借款合同签章错误：{}",Exceptions.getStackTraceAsString(e));
						
					}
				}
			}
	    	//债转合同签章
			log.info("=========君子签债转合同生成开始=====");
		 	NfsCrContract crContract = new NfsCrContract();
		 	crContract.setSignatureType(NfsCrContract.SignatureType.junziqian);
		 	crContract.setStatus(NfsCrContract.Status.notCreate);
		 	crContract.setCount(500);
	    	List<NfsCrContract> unsignCrAuctions = crContractService.findList(crContract);
			if(unsignCrAuctions != null && unsignCrAuctions.size()>0){
				for(NfsCrContract item : unsignCrAuctions){
					try{
						NfsCrAuction crAuction = crAuctionService.get(item.getCrId());
						crContractService.createPdf(crAuction);
					}catch (Exception e){
						log.error("债转合同签章错误：{}",Exceptions.getStackTraceAsString(e));
						
					}
				}
			}
			try {
				Thread.sleep(1000 * 180);
			} catch (InterruptedException e) {
				log.error("君子签合同等待下载异常",Exceptions.getStackTraceAsString(e));
			}
			//下载PDF
			log.info("=========君子签借条合同下载开始=====");
			NfsLoanContract nfsLoanContract1 = new NfsLoanContract();
		 	nfsLoanContract1.setCount(500);
		 	nfsLoanContract1.setSignatureType(NfsLoanContract.SignatureType.junziqian);
		 	nfsLoanContract1.setStatus(NfsLoanContract.Status.created);
		 	// 传个参数判断出是否已经盖章
	    	List<NfsLoanContract> loans = loanContractService.findList(nfsLoanContract1);
			if(loans != null && loans.size() > 0){
				for(NfsLoanContract item : loans){
					try{
						loanContractService.updateAuth(item.getId());
					}catch (Exception e){
						log.error("借款合同下载错误：{}",Exceptions.getStackTraceAsString(e));
					}
				}
			}
			
			
			//下载PDF
			log.info("=========君子签债转合同下载开始=====");
			NfsCrContract crContract1 = new NfsCrContract();
			crContract1.setSignatureType(NfsCrContract.SignatureType.junziqian);
			crContract1.setStatus(NfsCrContract.Status.created);
			crContract1.setCount(500);
		 	// 传个参数判断出是否已经盖章
	    	List<NfsCrContract> crContracts = crContractService.findList(crContract1);
			if(crContracts != null && crContracts.size() > 0){
				for(NfsCrContract item : crContracts){
					try{
						crContractService.updateAuth(item.getId());
					}catch (Exception e){
						log.error("债转合同下载错误：{}",Exceptions.getStackTraceAsString(e));
					}
				}
			}
		}
	
	}

}