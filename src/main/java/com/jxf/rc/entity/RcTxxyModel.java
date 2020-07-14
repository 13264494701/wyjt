package com.jxf.rc.entity;

public class RcTxxyModel {

	
	private String randomKey;
	
	private String charset;
	
	private String data;
	
	private String sign;
	
	private String format;
	
	private String signType;
	
	private String timestamp;
	
	private String code;

	public RcTxxyModel(String randomKey, String charset, String data, String sign, String format, String signType,
			String timestamp, String code) {
		super();
		this.randomKey = randomKey;
		this.charset = charset;
		this.data = data;
		this.sign = sign;
		this.format = format;
		this.signType = signType;
		this.timestamp = timestamp;
		this.code = code;
	}

	public String getRandomKey() {
		return randomKey;
	}

	public void setRandomKey(String randomKey) {
		this.randomKey = randomKey;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	
}
