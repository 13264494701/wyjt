package com.jxf.loan.signature.junziqian;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.ebaoquan.rop.thirdparty.com.alibaba.fastjson.JSONArray;
import org.ebaoquan.rop.thirdparty.com.alibaba.fastjson.JSONObject;
import org.ebaoquan.rop.thirdparty.com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.junziqian.api.bean.Signatory;
import com.junziqian.api.common.AuthLevel;
import com.junziqian.api.common.DealType;
import com.junziqian.api.common.IdentityType;
import com.junziqian.api.request.ApplySignHtmlRequest;
import com.junziqian.api.request.FileLinkRequest;
import com.junziqian.api.response.ApplySignResponse;
import com.junziqian.api.response.SignLinkResponse;
import com.jxf.loan.entity.NfsCrAuction;
import com.jxf.loan.entity.NfsLoanRecord;
import com.jxf.mem.entity.Member;
import com.jxf.svc.config.Constant;
import com.jxf.svc.config.Global;
import com.jxf.svc.utils.CalendarUtil;
import com.jxf.svc.utils.Exceptions;
import com.jxf.svc.utils.HtmlUtils;
import com.jxf.svc.utils.StringUtils;

public class JunZiQianUtil extends JunziqianClientInit {
	private static final Logger log = LoggerFactory.getLogger(JunZiQianUtil.class);
	
    private static TrustManager myX509TrustManager = new X509TrustManager() {
        public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    };
    
	 /**
	  * 君子签注册邮箱
	  */
	 private static final String comEmail ="sunpeiya@51jt.com";
	 
	/**
	 * 获取合同的下载地址
	 * 
	 * @param applyNo
	 * @return
	 * @throws Exception
	 */
	public static String getPdfUrl(String applyNo){
		FileLinkRequest request = new FileLinkRequest();
		request.setApplyNo(applyNo);// 签约编号
		SignLinkResponse response = getClient().fileLink(request);
		if(!response.isSuccess()){
			log.error("获取合同的下载地址失败");
			return null;
		}
		return response.getLink();
	}
	
	/**
	 * 创建君子签  v20
	 * @param loan 借条
	 * @param loanner 出借人
	 * @param loanee 借款人
	 * @return
	 */
	public static String certificationNew(NfsLoanRecord loan, Member loanner, Member loanee) {
		String html = HtmlUtils.getContent(Global.getConfig("domain")+"/callback/contract/genContract?loanId=" + loan.getId());
		if (log.isDebugEnabled()){
			log.debug(html);
		}		
		ApplySignHtmlRequest.Builder builder = new ApplySignHtmlRequest.Builder();
		// html文件必须设置meta.charset为utf-8|否则会出现乱码。表单域请使用input
		// type=text的，且注明name属性，宽高设置为0
		builder.withHtml(loan.getLoanNo() + ".pdf", html);
		builder.withContractName(loan.getLoanNo()); // 合同名称，必填
		builder.withContractAmount(Double.valueOf(StringUtils.decimalToStr(loan.getAmount(), 2))); // 合同金额
		// 1、本地文件方式
		builder.withServerCa(1);
		builder.withDealType(DealType.AUTH_SIGN);
		TreeSet<Integer> set = new TreeSet<Integer>();
		set.add(AuthLevel.BANKTHREE.getCode());
		builder.withAuthLevel(set);
		builder.withForceAuthentication(1);
		HashSet<Signatory> signatories = Sets.newHashSet();
		Signatory signatory = new Signatory();
		signatory.setFullName(loanner.getName()); // 姓名
		signatory.setSignatoryIdentityType(IdentityType.IDCARD); // 证件类型
		signatory.setIdentityCard(loanner.getIdNo()); // 证件号码
		signatory.setMobile(loanner.getUsername());

		JSONArray chapteJsonArray_1 = new JSONArray();
		JSONObject pageJson_1 = new JSONObject();
		pageJson_1.put("page", 0);

		JSONArray chaptes_1 = new JSONArray();
		pageJson_1.put("chaptes", chaptes_1);
		JSONObject chapte_1 = new JSONObject();
		chapte_1.put("offsetX", 0.13);
		chapte_1.put("offsetY", 0.10);
		chaptes_1.add(chapte_1);
		chapteJsonArray_1.add(pageJson_1);

		signatory.withChapteJson(chapteJsonArray_1);

		signatories.add(signatory);

		signatory = new Signatory();
		signatory.setFullName(loanee.getName()); // 姓名
		signatory.setSignatoryIdentityType(IdentityType.IDCARD); // 证件类型
		signatory.setIdentityCard(loanee.getIdNo());// 证件号码
		signatory.setMobile(loanee.getUsername());
		// signatory.setOrderNum(1);
		signatories.add(signatory);
		signatory.setServerCaAuto(1);// 0 手动签，1 自动签

		JSONArray chapteJsonArray_2 = new JSONArray();
		JSONObject pageJson_2 = new JSONObject();
		pageJson_2.put("page", 0);

		JSONArray chaptes_2 = new JSONArray();
		pageJson_2.put("chaptes", chaptes_2);
		JSONObject chapte_2 = new JSONObject();
		chapte_2.put("offsetX", 0.13);
		chapte_2.put("offsetY", 0.15);
		chaptes_2.add(chapte_2);
		chapteJsonArray_2.add(pageJson_2);

		signatory.withChapteJson(chapteJsonArray_2);
		builder.withSignatories(signatories); // 添加签约人

		ApplySignResponse response = getClient().applySignHtml(builder.build());
		return response.getApplyNo();
	}
	
	
	/**
	 * 创建债转合同君子签PDF  v20
	 * @param crAuction 债转记录
	 * @param seller 转让人
	 * @param buyer 受让人
	 * @return
	 */
	public static String createCrCertification(NfsCrAuction crAuction) {
		Member seller = crAuction.getCrSeller(); 
		Member buyer = crAuction.getCrBuyer();
		String html = HtmlUtils.getContent(Global.getConfig("domain")+"/callback/contract/genCrAuctionContract?crId=" + crAuction.getId());
		if (log.isDebugEnabled()){
			log.debug(html);
		}		
		ApplySignHtmlRequest.Builder builder = new ApplySignHtmlRequest.Builder();
		// html文件必须设置meta.charset为utf-8|否则会出现乱码。表单域请使用input
		// type=text的，且注明name属性，宽高设置为0
		builder.withHtml(crAuction.getId() + ".pdf", html);
		builder.withContractName(crAuction.getId()+""); // 合同名称，必填
		builder.withContractAmount(Double.valueOf(StringUtils.decimalToStr(crAuction.getCrBuyPrice(), 2))); // 合同金额
		// 1、本地文件方式
		builder.withServerCa(1);
		builder.withDealType(DealType.AUTH_SIGN);
		TreeSet<Integer> set = new TreeSet<Integer>();
		set.add(AuthLevel.BANKTHREE.getCode());
		builder.withAuthLevel(set);
		builder.withForceAuthentication(1);
		HashSet<Signatory> signatories = Sets.newHashSet();
		Signatory signatory = new Signatory();
		signatory.setFullName(seller.getName()); // 姓名
		signatory.setSignatoryIdentityType(IdentityType.IDCARD); // 证件类型
		signatory.setIdentityCard(seller.getIdNo()); // 证件号码
		signatory.setMobile(seller.getUsername());

		JSONArray chapteJsonArray_1 = new JSONArray();
		JSONObject pageJson_1 = new JSONObject();
		pageJson_1.put("page", 1);

		JSONArray chaptes_1 = new JSONArray();
		pageJson_1.put("chaptes", chaptes_1);
		JSONObject chapte_1 = new JSONObject();
		chapte_1.put("offsetX", 0.10);
		chapte_1.put("offsetY", 0.24);
		chaptes_1.add(chapte_1);
		chapteJsonArray_1.add(pageJson_1);

		signatory.withChapteJson(chapteJsonArray_1);
		signatories.add(signatory);

		signatory = new Signatory();
		signatory.setFullName(buyer.getName()); // 姓名
		signatory.setSignatoryIdentityType(IdentityType.IDCARD); // 证件类型
		signatory.setIdentityCard(buyer.getIdNo());// 证件号码
		signatory.setMobile(buyer.getUsername());
		signatory.setServerCaAuto(1);// 0 手动签，1 自动签

		JSONArray chapteJsonArray_2 = new JSONArray();
		JSONObject pageJson_2 = new JSONObject();
		pageJson_2.put("page", 1);
		JSONArray chaptes_2 = new JSONArray();
		pageJson_2.put("chaptes", chaptes_2);
		JSONObject chapte_2 = new JSONObject();
		chapte_2.put("offsetX", 0.10);
		chapte_2.put("offsetY", 0.29);
		chaptes_2.add(chapte_2);
		chapteJsonArray_2.add(pageJson_2);
		signatory.withChapteJson(chapteJsonArray_2);
		signatories.add(signatory);
		
		signatory = new Signatory();
		signatory.setFullName(Constant.COM_NAME); //企业名
		signatory.setSignatoryIdentityType(IdentityType.BIZLIC); //证件类型
		signatory.setIdentityCard(Constant.COM_CREDIT_CODE);// 证件号码
		signatory.setEmail(comEmail);
		signatory.setServerCaAuto(1);// 0 手动签，1 自动签

		JSONArray chapteJsonArray_3 = new JSONArray();
		JSONObject pageJson_3 = new JSONObject();
		pageJson_3.put("page", 1);
		JSONArray chaptes_3 = new JSONArray();
		pageJson_3.put("chaptes", chaptes_3);
		JSONObject chapte_3 = new JSONObject();
		chapte_3.put("offsetX", 0.25);
		chapte_3.put("offsetY", 0.38);
		chaptes_3.add(chapte_3);
		chapteJsonArray_3.add(pageJson_3);
		signatory.withChapteJson(chapteJsonArray_3);
		signatories.add(signatory);
		
		
		builder.withSignatories(signatories); // 添加签约人

		ApplySignResponse response = getClient().applySignHtml(builder.build());
		return response.getApplyNo();
	}
	
	
	
	 /**
     * 下载君子签pdf
     * @param orgId 借条或债转记录Id
     * @return
	 * @throws Exception 
     */
    public static String doDownloadJunziqianPdf(String fileUrl, String orgId,Date createTime,int orgType) throws Exception {
        if (StringUtils.isBlank(fileUrl)) {
            return "下载地址不能为空!";
        }
        SSLContext sslcontext = SSLContext.getInstance("SSL", "SunJSSE");
        sslcontext.init(null, new TrustManager[] { myX509TrustManager }, new java.security.SecureRandom());
        URL url = new URL(fileUrl);
        HostnameVerifier ignoreHostnameVerifier = new HostnameVerifier() {
        	@Override
            public boolean verify(String s, SSLSession sslsession) {
                return true;
            }
        };
        HttpsURLConnection.setDefaultHostnameVerifier(ignoreHostnameVerifier);
        HttpsURLConnection.setDefaultSSLSocketFactory(sslcontext.getSocketFactory());
        HttpsURLConnection urlCon = (HttpsURLConnection) url.openConnection();
        urlCon.setConnectTimeout(6000);
        urlCon.setReadTimeout(6000);
        
        
        int code = urlCon.getResponseCode();
        if (code != HttpURLConnection.HTTP_OK) {
            throw new Exception("文件读取失败");
        }
        
        String date = CalendarUtil.DateToString(createTime, "yyyy/MM/dd");
        String pdfName = "";
        if(orgType == Constant.CONTRACT_LOAN) {
        	pdfName = "junziqian_signed_" + orgId + ".pdf";
        }else {
        	pdfName = "junziqian_signed_cr_" + orgId + ".pdf";
        }
        InputStream in = null;
        FileOutputStream fos = null;
        in = url.openStream();
        String contentType = url.openConnection().getContentType();
        if (StringUtils.isNotBlank(contentType) && contentType.contains("json")) {
        	throw new Exception("返回错误json");
        }
        try {
        	File destDir = null;
        	if(orgType == Constant.CONTRACT_LOAN) {
        		destDir = new File(Global.getBaseStaticPath()+Global.getConfig("junziqianContractPath")+ date);
        	}else {
        		destDir = new File(Global.getBaseStaticPath()+Global.getConfig("crAuctionContractPath")+ date);
        	}
	        if (!destDir.exists()) {
	            destDir.mkdirs();
	        }
	        fos = new FileOutputStream(new File(destDir, pdfName));
	        int length = -1;
	        byte[] buffer = new byte[1024];
	        while ((length = in.read(buffer)) > -1) {
	            fos.write(buffer, 0, length);
	        }
        } catch (Exception e) {
			throw new Exception("pdf下载失败");
        } finally {
            try {
                fos.close();
                in.close();
            } catch (Exception e) {
            	log.error(Exceptions.getStackTraceAsString(e));
            }
        }
        String queryCondition = "";
        if(orgType == Constant.CONTRACT_LOAN) {
        	queryCondition = "cd " +Global.getBaseStaticPath()+Global.getConfig("junziqianContractPath") + " && ln -sf " + pdfName;
        }else {
        	queryCondition = "cd " +Global.getBaseStaticPath()+Global.getConfig("crAuctionContractPath") + " && ln -sf " + pdfName;
        }
        doShell(queryCondition);
        if(orgType == Constant.CONTRACT_LOAN) {
			return Global.getConfig("junziqianContractPath") + date + "/" + pdfName;
		} else {
			return Global.getConfig("crAuctionContractPath") + date + "/" + pdfName;
		}
    }
    private static void doShell(String queryCondition) throws Exception {
        List<String> cmds = new ArrayList<String>();
        cmds.add("sh");
        cmds.add("-c");
        cmds.add(queryCondition);
        ProcessBuilder pb = new ProcessBuilder(cmds);
        pb.start();
    }
	
    
    /**
	 * 创建补充协议君子签签章  
	 * @param loan 借条
	 * @param loanner 出借人
	 * @param loanee 借款人
	 * @return
	 */
	public static String createSupplementaryAgreement(int index) {
		
		Member loaner = null;
		Member loanee = null;
		String contractNo = "";
		if(index == 1) {
			loaner = new Member();
			loaner.setName("王小帆");
			loaner.setIdNo("620522199809140326");
			loaner.setUsername("17801059931");
			loaner.setEmail("2640518301@qq.com");
			
			loanee = new Member();
			loanee.setName("陈胜男");
			loanee.setIdNo("430923199203072923");
			loanee.setUsername("18078826887");
			loanee.setEmail("1058646861@qq.com");
			
			contractNo = "047572213550334727";
			
		}else if(index == 2) {
			loaner = new Member();
			loaner.setName("吴献");
			loaner.setIdNo("34292119860113263X");
			loaner.setUsername("18611573002");
			loaner.setEmail("2153483383@qq.com");
			
			loanee = new Member();
			loanee.setName("王梦丹");
			loanee.setIdNo("410105199107200083");
			loanee.setUsername("18638668522");
			loanee.setEmail("9307203@qq.com");
			
			contractNo = "037436378034575228";
		}else {
			loaner = new Member();
			loaner.setName("尤泽群");
			loaner.setIdNo("330226198612011593");
			loaner.setUsername("13586664464");
			loaner.setEmail("77696653@qq.com");
			
			loanee = new Member();
			loanee.setName("赵元科");
			loanee.setIdNo("500222199303114717");
			loanee.setUsername("17623099609");
			loanee.setEmail("965106737@qq.com");
			
			contractNo = "2019022313673124";
		}
		
		String html = HtmlUtils.getContent(Global.getConfig("domain")+"/callback/contract/genSupplementaryAgreement?index="+index);
		if (log.isDebugEnabled()){
			log.debug(html);
		}		
		ApplySignHtmlRequest.Builder builder = new ApplySignHtmlRequest.Builder();
		// html文件必须设置meta.charset为utf-8|否则会出现乱码。表单域请使用input
		// type=text的，且注明name属性，宽高设置为0
		builder.withHtml(contractNo + ".pdf", html);
		builder.withContractName(contractNo); // 合同名称，必填
		builder.withContractAmount(0D); // 合同金额
		// 1、本地文件方式
		builder.withServerCa(1);
		builder.withDealType(DealType.AUTH_SIGN);
		TreeSet<Integer> set = new TreeSet<Integer>();
		set.add(AuthLevel.BANKTHREE.getCode());
		builder.withAuthLevel(set);
		builder.withForceAuthentication(1);
		HashSet<Signatory> signatories = Sets.newHashSet();
		Signatory signatory = new Signatory();
		signatory.setFullName(loaner.getName()); // 姓名
		signatory.setSignatoryIdentityType(IdentityType.IDCARD); // 证件类型
		signatory.setIdentityCard(loaner.getIdNo()); // 证件号码
		signatory.setMobile(loaner.getUsername());

		JSONArray chapteJsonArray_1 = new JSONArray();
		JSONObject pageJson_1 = new JSONObject();
		pageJson_1.put("page", 0);

		JSONArray chaptes_1 = new JSONArray();
		pageJson_1.put("chaptes", chaptes_1);
		JSONObject chapte_1 = new JSONObject();
		chapte_1.put("offsetX", 0.12);
		chapte_1.put("offsetY", 0.11);
		chaptes_1.add(chapte_1);
		chapteJsonArray_1.add(pageJson_1);

		signatory.withChapteJson(chapteJsonArray_1);

		signatories.add(signatory);

		signatory = new Signatory();
		signatory.setFullName(loanee.getName()); // 姓名
		signatory.setSignatoryIdentityType(IdentityType.IDCARD); // 证件类型
		signatory.setIdentityCard(loanee.getIdNo());// 证件号码
		signatory.setMobile(loanee.getUsername());
		// signatory.setOrderNum(1);
		signatories.add(signatory);
		signatory.setServerCaAuto(1);// 0 手动签，1 自动签

		JSONArray chapteJsonArray_2 = new JSONArray();
		JSONObject pageJson_2 = new JSONObject();
		pageJson_2.put("page", 0);

		JSONArray chaptes_2 = new JSONArray();
		pageJson_2.put("chaptes", chaptes_2);
		JSONObject chapte_2 = new JSONObject();
		chapte_2.put("offsetX", 0.12);
		chapte_2.put("offsetY", 0.18);
		chaptes_2.add(chapte_2);
		chapteJsonArray_2.add(pageJson_2);

		signatory.withChapteJson(chapteJsonArray_2);
		builder.withSignatories(signatories); // 添加签约人

		ApplySignResponse response = getClient().applySignHtml(builder.build());
		return response.getApplyNo();
	}
    
}
