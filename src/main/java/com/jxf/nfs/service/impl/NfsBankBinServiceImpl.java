package com.jxf.nfs.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.jxf.mem.entity.MemberCard;
import com.jxf.nfs.dao.NfsBankBinDao;
import com.jxf.nfs.entity.AliPayCardInfo;
import com.jxf.nfs.entity.NfsBankBin;
import com.jxf.nfs.entity.NfsBankInfo;
import com.jxf.nfs.service.NfsBankBinService;
import com.jxf.nfs.service.NfsBankInfoService;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
import com.jxf.svc.utils.Exceptions;



/**
 * 卡BINServiceImpl
 * @author wo
 * @version 2018-09-29
 */
@Service("nfsBankBinService")
@Transactional(readOnly = true)
public class NfsBankBinServiceImpl extends CrudServiceImpl<NfsBankBinDao, NfsBankBin> implements NfsBankBinService{

	private static final Logger log = LoggerFactory.getLogger(NfsBankBinServiceImpl.class);
	
	@Autowired
	private NfsBankBinDao bankBinDao;
	@Autowired
	private NfsBankInfoService bankInfoService;
	
	public NfsBankBin get(Long id) {
		return super.get(id);
	}
	
	public List<NfsBankBin> findList(NfsBankBin nfsBankBin) {
		return super.findList(nfsBankBin);
	}
	
	public Page<NfsBankBin> findPage(Page<NfsBankBin> page, NfsBankBin nfsBankBin) {

		return super.findPage(page, nfsBankBin);
	}
	
	@Transactional(readOnly = false)
	public void save(NfsBankBin nfsBankBin) {
		super.save(nfsBankBin);
	}
	
	@Transactional(readOnly = false)
	public void delete(NfsBankBin nfsBankBin) {
		super.delete(nfsBankBin);
	}

	@Override
	@Transactional(readOnly = false)
	public Boolean checkCardNo(String cardNo) {
		
		String cardBin = StringUtils.left(cardNo, 6);
		Integer length = StringUtils.length(cardNo);
        NfsBankBin bankBin = bankBinDao.getByCardBin(cardBin,length);
        if(null==bankBin) {
        	bankBin=validateAndCacheCardInfo(cardNo);
        }
		if(bankBin!=null&&bankBin.getCardType().equals(MemberCard.CardType.DC)&&bankBin.getBank().getIsSupport()) {
			return true;
		}
		return false;
	}

	@Override
	@Transactional(readOnly = false)
	public NfsBankBin getByCardNo(String cardNo) {

		String cardBin = StringUtils.left(cardNo, 6);
		Integer length = StringUtils.length(cardNo);
        NfsBankBin bankBin = bankBinDao.getByCardBin(cardBin,length);
        if(null==bankBin) {
        	bankBin=validateAndCacheCardInfo(cardNo);
        }
        
		return bankBin;
	}
	
	/***
	 * 通过联网阿里API检测银行卡号是否合法
	 * @param cardNo
	 * @return
	 */
	@Transactional(readOnly = false)
	private NfsBankBin validateAndCacheCardInfo(String cardNo) {
	    
		String cardBin = StringUtils.left(cardNo, 6);
        int length = cardNo.length();

        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(10000);
        factory.setReadTimeout(10000);
        RestTemplate restTemplate = new RestTemplate(factory);
        NfsBankBin bankBin = new NfsBankBin();
        try {
            String result = restTemplate.getForEntity("https://ccdcapi.alipay.com/validateAndCacheCardInfo.json?cardNo=" + cardNo + "&cardBinCheck=true", String.class).getBody();
            AliPayCardInfo aliPayCardInfo = JSON.parseObject(result, AliPayCardInfo.class);
            if (aliPayCardInfo != null && "true".equals(aliPayCardInfo.getValidated())) {               
            	NfsBankInfo bankInfo = bankInfoService.getBankInfoByAbbrName(aliPayCardInfo.getBank());                
                bankBin.setBank(bankInfo);
                switch (aliPayCardInfo.getCardType()) {
                    case "DC":
                        bankBin.setCardType(MemberCard.CardType.DC);
                        break;
                    case "SCC":
                        bankBin.setCardType(MemberCard.CardType.SCC);
                        break;
                    case "PC":
                        bankBin.setCardType(MemberCard.CardType.PC);
                        break;
                    case "CC":
                        bankBin.setCardType(MemberCard.CardType.CC);
                        break;
                    default:
                        break;
                }
                bankBin.setCardBin(cardBin);
                bankBin.setLength(length);
                save(bankBin);    
				return bankBin;
            } 
        } catch (ResourceAccessException e) {
        	log.warn(Exceptions.getStackTraceAsString(e));
        }
        return null;
	}
}