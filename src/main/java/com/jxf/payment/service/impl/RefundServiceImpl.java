package com.jxf.payment.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.mem.entity.Member;
import com.jxf.pay.service.WxRefundService;
import com.jxf.payment.dao.RefundDao;
import com.jxf.payment.entity.Payment;
import com.jxf.payment.entity.Refund;
import com.jxf.payment.service.PaymentService;
import com.jxf.payment.service.RefundService;
import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.persistence.sequence.utils.SequenceUtils;
import com.jxf.svc.plugin.PluginConfig;
import com.jxf.svc.plugin.weixinPayment.WxPaymentPlugin;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
import com.jxf.svc.utils.DateUtils;

/**
 * 退款ServiceImpl
 * @author wo
 * @version 2018-08-11
 */
@Service("refundService")
@Transactional(readOnly = true)
public class RefundServiceImpl extends CrudServiceImpl<RefundDao, Refund> implements RefundService{

	@Autowired
	private RefundDao refundDao;
	@Autowired
	private WxPaymentPlugin wxPaymentPlugin;
	@Autowired
	private WxRefundService wxRefundService;
	@Autowired
	private PaymentService paymentService;
	
	public Refund get(Long id) {
		return super.get(id);
	}
	
	public List<Refund> findList(Refund refund) {
		return super.findList(refund);
	}
	
	public Page<Refund> findPage(Page<Refund> page, Refund refund) {
		return super.findPage(page, refund);
	}
	
	@Transactional(readOnly = false)
	public void save(Refund refund) {
		if (refund.getIsNewRecord()) {
			refund.setRefundNo("0");
			refund.preInsert();
			refundDao.insert(refund);
		} else {
			refund.preUpdate();
			refundDao.update(refund);
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(Refund refund) {
		super.delete(refund);
	}

	@Override
	@Transactional(readOnly = false)
	public void refundFinishProcess(Refund refund) {

		save(refund);

		switch (refund.getPayment().getType()) {
		case recharge:
			break;
		case loanDone:
			break;	
		case loanDelay:

			break;
		case arbitration:

			break;
		case execution:

			break;
		default:
			break;
		}
	}

	@Override
	public Refund getByRefundNo(String refundNo) {

		return refundDao.getByRefundNo(refundNo);
	}
	
	@Override
	@Transactional(readOnly = false)
	public Boolean wxRefund(Member member , Payment.Type type, Long orgId, BigDecimal amount) {
		
		Payment payment = paymentService.getByTypeAndOrgId(type, orgId);
		Refund refund= new Refund();
		refund.setMember(member);
		refund.setOrgId(orgId+"");
		refund.setPayment(payment);
		refund.setRefundAmount(payment.getPaymentAmount());
		
		//获取插件参数
		PluginConfig config = wxPaymentPlugin.getPluginConfig();
		Map<String,String> configAttr = config.getAttributeMap();
		String key = configAttr.get(WxPaymentPlugin.PAYMENT_KEY_ATTRIBUTE_NAME);
		String mchId=configAttr.get(WxPaymentPlugin.PAYMENT_ACCTID_ATTRIBUTE_NAME);
		String url=configAttr.get(WxPaymentPlugin.PAYMENT_REFUND_REQUEST_URL_ATTRIBUTE_NAME);
		refund.setRequestUrl(url);
		refund.setMchId(mchId);
		refund.setKey(key);	
		refund.setStatus(Refund.Status.pendingRefund);
		save(refund);//
				
		String notifyUrl = Global.getConfig("domain") +"/callback/wx/refundNotify";
		logger.info("退款回调地址:{}",notifyUrl);
		refund.setNotifyUrl(notifyUrl);
		Boolean flag  = wxRefundService.payRefundApply(refund);
		save(refund);////更新refund_id
        return flag;
	}
	
}