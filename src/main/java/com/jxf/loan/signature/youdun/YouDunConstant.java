package com.jxf.loan.signature.youdun;

/**
 * 	有盾云慧签2.0 接口常量
 * @author xrd
 *
 */
public class YouDunConstant {
	
	  /** 商户号*/
    public static final String PARTNERCODE = "201705154014";
    
    /** 商户pub_key(下发到商户联系人邮箱)  */
    public static final String PUB_KEY = "3f39fc1c-eca4-4fc3-aeb1-e8dafcdca65b";
    
    /** 有盾签章用户编码*/
    public static final String USERCODE = "UD461063540953120768";
    /** 公司公章*/
    public static final String COMMON_SEAL_ID = "482215121012326400";
    /** 云相接口产品码*/
    public static final String CLOUD_PHASE_PRODUCT_CODE = "Y1001006";
    
	/** 商户secret_key(下发到商户联系人邮箱) */
    public static final String SECURITY_KEY= "3934d22d-8bff-461c-9328-0141629bbafe";

    /** 开户接口地址 */
    public static final String OPEN_CONSTRUCT_REQUEST_URL = "https://esignature.udcredit.com/api/2.0/user/establish/partner-code/";	
 
    /** 开户查询地址 */
    public static final String QUERY_CONSTRUCT_REQUEST_URL = "https://esignature.udcredit.com/api/2.0/user/query-user-information/partner-code/";	
    
    /** 自动签署请求地址 */
    public static final String AUTO_SIGN_REQUEST_URL = "https://esignature.udcredit.com/api/2.0/contract/auto-sign/partner-code/";	
    
    /** 合同查询地址 */
    public static final String QUERY_SIGN_REQUEST_URL = "https://esignature.udcredit.com/api/2.0/contract/query/partner-code/";	
    
    /** 合同下载地址 */
    public static final String DOWNLOAD_SIGN_REQUEST_URL = "https://esignature.udcredit.com/api/2.0/contract/download/partner-code/";	
    
    /** 业务保全地址 */
    public static final String PRESERVATION_REQUEST_URL = "https://esignature.udcredit.com/api/2.0/preservation/preservation/partner-code/" + PARTNERCODE;	
    /** 云相接口地址 */
    public static final String CLOUDPHASE_REQUEST_URL = "https://api4.udcredit.com/dsp-front/4.1/dsp-front/default/pubkey/" + PUB_KEY 
    					+ "/product_code/" + CLOUD_PHASE_PRODUCT_CODE + "/out_order_id/%s/signature/%s";	
    
    
    
    /** 商户RSA私钥 */
    public static final String RSA_PRIVATE_KEY= "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDAUm7nbMOEJeMi" + 
    		"RBSvMC33zVrWOKMrsQLHb+Iw9jVC/VekfWW0SGSaZcLxO6hpWJmmJ1f1PeoBuJzV" + 
    		"+Uyum/gmPScW+Wf0JN+0fiIyFtz7L+YCxowQSf69le2/lPweajbGEXy+FdMxqWt4" + 
    		"ubREAsxWdOay1isbTw01bSQERbvrMQBSs6ZsfiEYshHV6l4hBnMttM5rhRvk9qdW" + 
    		"hBWKnQUBLKsXkBm3Onpn3qG4/O5fYpYSGFTROEOkgC3sG9PRdOmyePDXvD32fo8w" + 
    		"9dbrtsx5gnekKoq+9cdVCHj72JQ641pEmWX1B548HrmZH7Lkr4lI+3yc4eM/1P4D" + 
    		"8cVdnXpBAgMBAAECggEBAIL5oL3KvHxKsRys8Mk+LpAMMkih2b3vlszrzUc65Zrx" + 
    		"nbZQD+tvQdREaTaQQmk8Bae6M5S4zZzOdUDjvu1LGvwvuWdUAw+p7Q+mtvMVzvjh" + 
    		"7esF/G07d1j1uiw28lxkSVkqn6F0i5fOoXpePf3zSeW3R0WTxVebxosUAOtH8mqA" + 
    		"jd5clquI9cCmleEX3B45eN8LXUAQrkyXX/JpcYgvgVAxOCGHhFOMuBNA/rT+39Hp" + 
    		"tRvNo5b8Ggp7KY/53v5zIy5GCVOSxo4dLm+PWWewZEUyzceXdkeiV5084SH+Y24Q" + 
    		"efEjT3IHtqbBgMX+m5h5W1Qn3KnD326uKxGFCBKfIOECgYEA0eIdMXvcB4LIDlA7" + 
    		"MSu9C4XDHTOL3mCQH8mfTwDsmPJXLxSyNbwMlPrr5fVu+k56y512ATSotme0t0iP" + 
    		"3RmkjFocoITvmo72aYYixG97jbqxbhzlIEN4NwnOtc3lex8GQb8nDMbW623FmMVs" + 
    		"8vVsTA6Li2qjMM/zLXHaHOwxPcMCgYEA6pSArYRjr+Zrd7VTyTZST+JJij3QIrB2" + 
    		"+gbORMMKiMjXuyKThWRMhwmZGyxTsiswhr3aW3o9UUWquSf0f6/dBRj8C+XLtVdE" + 
    		"QU8/fwMUxQXO3Qz/9Mn/nCKLZ1JLWjIwGqsuV2f3SmxhQLiJfSEc2zqxvQ9eQo0r" + 
    		"3YMtxB1RU6sCgYEAsuOqBEpQwOocf+6sdP2VtON+6T3NIm5liWJ9cYYVI0uzOcLH" + 
    		"Hnu4saapOCiYPap0iWYddWufvVDpPCsRy98WXDZWHg8QdkkSB+E3cvyeb6/HmVXx" + 
    		"iS1YoniuglELHoTT/Wfi7cXw21uFxu/HK9I5Sx4+nEb47xrfZ0WaJXq/gacCgYBX" + 
    		"XzyWZIskxzQcAEPW3A7IhkZR4TgW05ddBQ/q+KKB1BzWLMi5lGQUn/SQ9G+wGvoW" + 
    		"Fbyw+dTA4qv2DvW4YW53KZknbU30my5nDPdffWxM/h7QorjrGhnEs/EsaLB8Rdk4" + 
    		"9a1rDOREqh2HF7TOEHlJBpGRE5pOO3qtJOD0/N5/xQKBgHMZRHcHRSWDNe95pTa4" + 
    		"hZoAjsHgdS410z+bRkx9E87CoM9tQ/VA6cON16VJROfpzYl5UGbhuSoB90ySjvyr" + 
    		"2V7qMpRz81uDxiHrFAke+akACcLB45Ry0O/vEQecryz618HTGfLZZzhpEfxBgsyv" + 
    		"7NaRds9FNtR41VCM7LlFOv8E";
	
	
    
    /**
	 * 节点类型
	 */
	public static enum NodeType {

	    /** 认证*/
		identificationInfo,
		
		/** 合同*/
		contractInfo,
		
		/** 付款*/
		loanInfo,
		
		/** 还款*/
		repayInfo,
		
		/** 续签*/
		renewalInfo
		
	}
	
}
