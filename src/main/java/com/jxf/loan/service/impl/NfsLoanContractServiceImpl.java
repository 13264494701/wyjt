package com.jxf.loan.service.impl;


import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.svc.config.Constant;
import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
import com.jxf.svc.utils.Exceptions;
import com.jxf.loan.dao.NfsLoanContractDao;

import com.jxf.loan.entity.NfsLoanContract;
import com.jxf.loan.entity.NfsLoanContract.SignatureType;
import com.jxf.loan.entity.NfsLoanContract.Status;
import com.jxf.loan.entity.NfsLoanRecord;

import com.jxf.loan.service.NfsLoanContractService;
import com.jxf.loan.signature.junziqian.JunZiQianUtil;
import com.jxf.loan.signature.youdun.YouDunESignature;
import com.jxf.loan.signature.youdun.YouDunQianZhang;
import com.jxf.mem.entity.Member;
import com.jxf.mem.service.MemberService;

/**
 * 合同表ServiceImpl
 * 
 * @author lmy
 * @version 2019-01-08
 */
@Service("nfsLoanContractService")
@Transactional(readOnly = false)
public class NfsLoanContractServiceImpl extends CrudServiceImpl<NfsLoanContractDao, NfsLoanContract>
		implements NfsLoanContractService {

	@Autowired
	private NfsLoanContractDao loanContractDao;
	@Autowired
	private MemberService memberService;
	
	
	public NfsLoanContract get(Long id) {
		return super.get(id);
	}

	public List<NfsLoanContract> findList(NfsLoanContract nfsLoanContract) {
		return super.findList(nfsLoanContract);
	}

	public Page<NfsLoanContract> findPage(Page<NfsLoanContract> page, NfsLoanContract nfsLoanContract) {
		return super.findPage(page, nfsLoanContract);
	}

	@Transactional(readOnly = false)
	public void save(NfsLoanContract nfsLoanContract) {
		super.save(nfsLoanContract);
	}

	@Transactional(readOnly = false)
	public void delete(NfsLoanContract nfsLoanContract) {
		super.delete(nfsLoanContract);
	}

	@Override
	public void createYDPdf(NfsLoanRecord loan, Long loanNum) {

		// 从有盾去签章
		String url = YouDunQianZhang.detecttest(loan, loanNum);
		if (StringUtils.isNoneBlank(url)) {
			NfsLoanContract nfsLoanContract = get(loanNum);
			nfsLoanContract.setSignatureUrl(url);
			nfsLoanContract.setStatus(NfsLoanContract.Status.created);
			update(nfsLoanContract);
		}
	}

	@Override
	public void updateYDUrl(NfsLoanRecord loan, Long loanNum) {
		NfsLoanContract nfsLoanContract = get(loanNum);
		if (nfsLoanContract == null) {
			return;
		}
		if (nfsLoanContract.getStatus().ordinal() != 1) {
			return;
		}
		if (StringUtils.isBlank(nfsLoanContract.getSignatureUrl())) {
			return;
		}
//		 获取下载地址
		String resultsss = YouDunQianZhang.doDownloadYouDunPdf(nfsLoanContract.getSignatureUrl(), loanNum);
		if (resultsss != null) {
			nfsLoanContract.setContractUrl(resultsss);
			nfsLoanContract.setStatus(NfsLoanContract.Status.signatured);
			update(nfsLoanContract);
		}
	}

	@Override
	public void createContract(NfsLoanRecord loanRecord,SignatureType type) {
		NfsLoanContract nfsLoanContract =new NfsLoanContract();
		nfsLoanContract.setLoanId(loanRecord.getId());
		nfsLoanContract.setSignatureType(type);
		nfsLoanContract.setStatus(NfsLoanContract.Status.notCreate);
		save(nfsLoanContract);		
	}
	
	@Override
	public void createPdf(NfsLoanRecord loan,Long loanNum) {

		Member loanee = memberService.get(loan.getLoanee());
		
		// 放款人
		Member loanner = memberService.get(loan.getLoaner());
		String appNum =JunZiQianUtil.certificationNew(loan, loanner, loanee);
		if (StringUtils.isNoneBlank(appNum)) {
			NfsLoanContract nfsLoanContract = get(loanNum);
			nfsLoanContract.setSignatureNo(appNum);
			nfsLoanContract.setStatus(NfsLoanContract.Status.created);
			update(nfsLoanContract);
		}
		
	}

	@Override
	public void updateAuth(Long contractId) {
		NfsLoanContract loanContract = get(contractId);
		if (loanContract == null) {
			return;
		}
		if (loanContract.getStatus().ordinal() != 1) {
			return;
		}
//		 获取下载地址
		String downUrl = JunZiQianUtil.getPdfUrl(loanContract.getSignatureNo());
		if (StringUtils.isBlank(downUrl)) {
			return;
		}
		try {
			String contractUrl = JunZiQianUtil.doDownloadJunziqianPdf(downUrl,contractId.toString(),loanContract.getCreateTime(),Constant.CONTRACT_LOAN);
			if (StringUtils.isNotBlank(contractUrl)) {
				loanContract.setContractUrl(contractUrl);
				loanContract.setStatus(NfsLoanContract.Status.signatured);
				update(loanContract);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public List<NfsLoanContract> getContractByLoanId(NfsLoanContract loanContract) {
		return loanContractDao.getContractByLoanId(loanContract);
	}

	@Override
	public NfsLoanContract getCurrentContractByLoanId(Long loanId) {
		return loanContractDao.getCurrentContractByLoanId(loanId);
	}

	@Override
	public void createYDPdfV2(NfsLoanRecord loanRecord, Long loanContractId) {
		try {
			String contractCode = YouDunESignature.autoSign(loanRecord, loanContractId);
			if (StringUtils.isNoneBlank(contractCode)) {
				NfsLoanContract nfsLoanContract = get(loanContractId);
				nfsLoanContract.setSignatureNo(contractCode);
				nfsLoanContract.setStatus(NfsLoanContract.Status.created);
				update(nfsLoanContract);
			}else {
				logger.error("借条合同：{}生成签章出错",loanContractId);
			}
		} catch (IOException e) {
			logger.error("借条合同：{}有盾签章异常:{}",loanContractId,Exceptions.getStackTraceAsString(e));
		}
		
	}

	@Override
	public void updateYDUrlV2(NfsLoanContract loanContract) {
		if(!loanContract.getStatus().equals(NfsLoanContract.Status.created)) {
			logger.error("借条合同：{}有盾签章状态：{}，不能下载pdf",loanContract.getId(),loanContract.getStatus());
			return ;
		}
		if(StringUtils.isBlank(loanContract.getSignatureNo())) {
			logger.error("借条合同：{}有盾签章出错，没有SignatureNo",loanContract.getId());
			return ;
		}
		String contractUrl = YouDunESignature.download(loanContract);
		if(StringUtils.isBlank(contractUrl)) {
			logger.error("借条合同：{}有盾签章PDF下载出错",loanContract.getId());
			return ;
		}
		contractUrl = "/" + contractUrl.substring(contractUrl.indexOf("loan_contract"));
		loanContract.setContractUrl(contractUrl);
		loanContract.setStatus(Status.signatured);
		save(loanContract);
		
	}
}