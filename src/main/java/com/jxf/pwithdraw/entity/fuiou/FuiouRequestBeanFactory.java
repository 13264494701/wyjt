package com.jxf.pwithdraw.entity.fuiou;

import com.jxf.svc.utils.MD5Code;

/**
 * @description this factory is used to generate FuiouRequestBean
 * @author Administrator
 *
 */
public class FuiouRequestBeanFactory{
	
	private static FuiouRequestBeanFactory instance = new FuiouRequestBeanFactory();
	
	private FuiouRequestBeanFactory () {}
	
	public static FuiouRequestBeanFactory getInstance() {
		return instance;
	}
	
	public FuiouRequestBean getRequestBean(FuiouRequestBean requestBean) {
		FuiouRequestBean fuiouRequestBean = new FuiouRequestBean();
		fuiouRequestBean.setMerid(FuiouMerchantDataBean.MERID);
		String xml = "";
		String mac = "";
		if(requestBean instanceof FuiouPaymentRequestBean) {
			fuiouRequestBean.setReqtype(FuiouMerchantDataBean.PAY_REQUEST_TYPE);
			FuiouPaymentRequestBean fuiouPaymentRequestBean = (FuiouPaymentRequestBean)requestBean;
			xml = getPaymentRequestXmlStr(fuiouPaymentRequestBean);
			mac = getPayRequestMacStr(xml);
		}else if (requestBean instanceof FuiouQueryPaymentRequestBean) {
			FuiouQueryPaymentRequestBean fuiouQueryPaymentRequestBean = (FuiouQueryPaymentRequestBean)requestBean;
			xml = getQueryRequestXmlStr(fuiouQueryPaymentRequestBean);
			mac = getQueryRequestMacStr(xml);
		}else if (requestBean instanceof FuiouReceiptRequestBean) {
			FuiouReceiptRequestBean fuiouReceiptRequestBean = (FuiouReceiptRequestBean)requestBean;
			String sign = getReceiptRequestMacStr(fuiouReceiptRequestBean);
			fuiouReceiptRequestBean.setSIGN(sign);
			xml = getReceiptRequestXmlStr(fuiouReceiptRequestBean);	
		}else {
			//查询账户余额
			xml = getAccountQueryRequestXmlStr();
		}
		fuiouRequestBean.setXml(xml);
		fuiouRequestBean.setMac(mac);
		return fuiouRequestBean;
	}
	
	private String getPaymentRequestXmlStr(FuiouPaymentRequestBean fuiouPaymentRequestBean) {
		String ver = fuiouPaymentRequestBean.getVer();
		String merdt = fuiouPaymentRequestBean.getMerdt();
		String orderno = fuiouPaymentRequestBean.getOrderno();
		String accntno = fuiouPaymentRequestBean.getAccntno();
		String accntnm = fuiouPaymentRequestBean.getAccntnm();
		String amt = fuiouPaymentRequestBean.getAmt();
		String addDesc = fuiouPaymentRequestBean.getAddDesc();
		
		String xml = "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"yes\"?>"+
    			"<payforreq>"+
    			"<ver>" + ver + "</ver>"+
    			"<merdt>" + merdt + "</merdt>"+
    			"<orderno>" + orderno + "</orderno>"+
    			"<accntno>" + accntno + "</accntno>"+
    			"<accntnm>" + accntnm + "</accntnm>"+
    			"<amt>" + amt + "</amt>"+
    			"<addDesc>" + addDesc + "</addDesc>"+
    			"</payforreq>";
		return xml;
	}

	private String getQueryRequestXmlStr(FuiouQueryPaymentRequestBean fuiouQueryPaymentRequestBean) {
		String orderno = fuiouQueryPaymentRequestBean.getOrderno();
		String startdt = fuiouQueryPaymentRequestBean.getStartdt();
		String enddt = fuiouQueryPaymentRequestBean.getEnddt();
		String ver = fuiouQueryPaymentRequestBean.getVer();
		String busicd = fuiouQueryPaymentRequestBean.getBusicd();
	    String xml = "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"yes\"?>"+
	    			"<qrytransreq>"+
	    			"<ver>"+ ver +"</ver>"+
	    			"<busicd>"+ busicd +"</busicd>"+   
	    			"<orderno>"+orderno+"</orderno>"+      
	    			"<startdt>"+startdt+"</startdt>"+  
	    			"<enddt>"+enddt+"</enddt>"+
	    			"</qrytransreq>";
		return xml;
	}
	
	private String getReceiptRequestXmlStr(FuiouReceiptRequestBean fuiouReceiptRequestBean) {
		
		String xml = "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"yes\"?>"+
    			"<ORDER>"+
    			"<VERSION>" + fuiouReceiptRequestBean.getVERSION() + "</VERSION>"+
    			"<MCHNTORDERID>" + fuiouReceiptRequestBean.getMCHNTORDERID() + "</MCHNTORDERID>"+
    			"<MCHNTCD>" + fuiouReceiptRequestBean.getMCHNTCD() + "</MCHNTCD>"+
    			"<BUSICD>" + fuiouReceiptRequestBean.getBUSICD() + "</BUSICD>"+
    			"<SIGNTP>" + fuiouReceiptRequestBean.getSIGNTP() + "</SIGNTP>"+
    			"<SIGN>" + fuiouReceiptRequestBean.getSIGN() + "</SIGN>"+
    			"</ORDER>";
		return xml;
	}
	
	private String getAccountQueryRequestXmlStr() {
		String mac = getQueryAccountRequestMacStr();
		String xml = "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"yes\"?>"+
    			"<qryacnt>"+
    			"<ver>1.0</ver>"+
    			"<merid>" + FuiouMerchantDataBean.MERID + "</merid>"+
    			"<mac>"+mac+"</mac>"+
    			"</qryacnt>";
		return xml;
	}
	private String getPayRequestMacStr(String xml) {
		String macSource = FuiouMerchantDataBean.MERID + "|" + FuiouMerchantDataBean.KEY + "|" + FuiouMerchantDataBean.PAY_REQUEST_TYPE + "|" + xml;
		String mac = MD5Code.MD5Encode(macSource, "UTF-8");
		return mac;
	}
	private String getQueryRequestMacStr(String xml) {
		String macSource = FuiouMerchantDataBean.MERID + "|" + FuiouMerchantDataBean.KEY + "|" + xml;
		String mac = MD5Code.MD5Encode(macSource, "UTF-8");
		return mac;
	}
	
	private String getQueryAccountRequestMacStr() {
		String macSource = "1.0|" + FuiouMerchantDataBean.MERID + "|" + FuiouMerchantDataBean.KEY;
		String mac = MD5Code.MD5Encode(macSource, "UTF-8");
		return mac;
	}
	
	private String getReceiptRequestMacStr(FuiouReceiptRequestBean fuiouReceiptRequestBean) {
		String macSource = fuiouReceiptRequestBean.getVERSION()+ "|" 
	                      + fuiouReceiptRequestBean.getMCHNTCD() + "|" 
	                      + fuiouReceiptRequestBean.getMCHNTORDERID() + "|" 
	                      + fuiouReceiptRequestBean.getBUSICD() + "|" 
	                      + fuiouReceiptRequestBean.getSIGNTP() + "|" 
				          + FuiouMerchantDataBean.KEY;
		String mac = MD5Code.MD5Encode(macSource, "UTF-8");
		return mac;
	}
}
