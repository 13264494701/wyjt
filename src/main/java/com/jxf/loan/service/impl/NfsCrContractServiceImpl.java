package com.jxf.loan.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.svc.config.Constant;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
import com.jxf.svc.utils.Exceptions;
import com.jxf.loan.entity.NfsCrAuction;
import com.jxf.loan.entity.NfsCrContract;
import com.jxf.loan.dao.NfsCrContractDao;
import com.jxf.loan.service.NfsCrContractService;
import com.jxf.loan.signature.junziqian.JunZiQianUtil;
import com.jxf.loan.signature.youdun.YouDunESignature;
import com.jxf.mem.entity.Member;
import com.jxf.mem.service.MemberService;
/**
 * 债转合同ServiceImpl
 * @author suHuimin
 * @version 2019-03-12
 */
@Service("nfsCrContractService")
@Transactional(readOnly = true)
public class NfsCrContractServiceImpl extends CrudServiceImpl<NfsCrContractDao, NfsCrContract> implements NfsCrContractService{
	@Autowired
	private NfsCrContractDao crContractDao;
	@Autowired
	private MemberService memberService;
	
	public NfsCrContract get(Long id) {
		return super.get(id);
	}
	
	public List<NfsCrContract> findList(NfsCrContract nfsCrContract) {
		return super.findList(nfsCrContract);
	}
	
	public Page<NfsCrContract> findPage(Page<NfsCrContract> page, NfsCrContract nfsCrContract) {
		return super.findPage(page, nfsCrContract);
	}
	
	@Transactional(readOnly = false)
	public void save(NfsCrContract nfsCrContract) {
		super.save(nfsCrContract);
	}
	
	@Transactional(readOnly = false)
	public void delete(NfsCrContract nfsCrContract) {
		super.delete(nfsCrContract);
	}


	@Override
	@Transactional(readOnly = false)
	public void updateAuth(Long crContractId) {
		NfsCrContract crContract = get(crContractId);
		if (crContract == null) {
			return;
		}
		if (!crContract.getStatus().equals(NfsCrContract.Status.created)) {
			return;
		}
		String downUrl = JunZiQianUtil.getPdfUrl(crContract.getSignatureNo());
		if (StringUtils.isBlank(downUrl)) {
			return;
		}
		try {
			String contractUrl = JunZiQianUtil.doDownloadJunziqianPdf(downUrl,crContractId+"",crContract.getCreateTime(),Constant.CONTRACT_CR);
			if (StringUtils.isNotBlank(contractUrl)) {
				crContract.setContractUrl(contractUrl);
				crContract.setStatus(NfsCrContract.Status.signatured);
				update(crContract);
			}
		} catch (Exception e) {
			logger.error("债转合同【{}】下载君子签PDF时发生异常{}",crContractId,Exceptions.getStackTraceAsString(e));
		}
	}

	@Override
	public NfsCrContract getCrContractByCrId(Long crId) {
		return crContractDao.getCrContractByCrId(crId);
	}

	@Override
	@Transactional(readOnly = false)
	public void createYDPdf(NfsCrAuction crAuction) {
		Member buyer = memberService.get(crAuction.getCrBuyer());
		Member seller = memberService.get(crAuction.getCrSeller());
		crAuction.setCrSeller(seller);
		crAuction.setCrBuyer(buyer);
		try {
			String signatureNo =YouDunESignature.autoSignCrContract(crAuction);
			if (StringUtils.isNoneBlank(signatureNo)) {
				NfsCrContract crContract = getCrContractByCrId(crAuction.getId());
				crContract.setSignatureNo(signatureNo);
				crContract.setStatus(NfsCrContract.Status.created);
				update(crContract);
			}
		} catch (Exception e) {
			logger.error("债转记录【{}】生成君子签签章时发生异常{}",crAuction.getId(),Exceptions.getStackTraceAsString(e));
		}
	}

	@Override
	@Transactional(readOnly = false)
	public void updateYDUrl(Long crContractId) {
		NfsCrContract crContract = get(crContractId);
		if (crContract == null) {
			return;
		}
		if (!crContract.getStatus().equals(NfsCrContract.Status.created)) {
			return;
		}
		try {
			String contractUrl = YouDunESignature.downloadCrPdf(crContract);
			if (StringUtils.isNotBlank(contractUrl)) {
				contractUrl = "/" + contractUrl.substring(contractUrl.indexOf("cr_contract"));
				crContract.setContractUrl(contractUrl);
				crContract.setStatus(NfsCrContract.Status.signatured);
				update(crContract);
			}
		} catch (Exception e) {
			logger.error("债转合同【{}】下载有盾PDF时发生异常{}",crContractId,Exceptions.getStackTraceAsString(e));
		}
	}

	@Override
	@Transactional(readOnly = false)
	public void createContract(NfsCrAuction crAuction) {
		NfsCrContract crContract = new NfsCrContract();
		crContract.setCrId(crAuction.getId());
		crContract.setSignatureType(NfsCrContract.SignatureType.youdun);
		crContract.setStatus(NfsCrContract.Status.notCreate);
		save(crContract);
	}

	@Override
	@Transactional(readOnly = false)
	public void createPdf(NfsCrAuction crAuction) {
		Member buyer = memberService.get(crAuction.getCrBuyer());
		Member seller = memberService.get(crAuction.getCrSeller());
		crAuction.setCrSeller(seller);
		crAuction.setCrBuyer(buyer);
		try {
			String signatureNo =JunZiQianUtil.createCrCertification(crAuction);
			if (StringUtils.isNoneBlank(signatureNo)) {
				NfsCrContract crContract = getCrContractByCrId(crAuction.getId());
				crContract.setSignatureNo(signatureNo);
				crContract.setStatus(NfsCrContract.Status.created);
				update(crContract);
			}
		} catch (Exception e) {
			logger.error("债转记录【{}】生成君子签签章时发生异常{}",crAuction.getId(),Exceptions.getStackTraceAsString(e));
		}
	}
	
}