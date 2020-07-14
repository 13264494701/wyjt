package com.jxf.svc.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jxf.svc.config.Constant;
import com.jxf.svc.config.Global;

import com.jxf.web.model.ResponseData;

import java.security.Key;

public class JwtUtil {

	public static String sercetKey = Global.getConfig("memberTokenKey");
//	public static String sercetKey = "XX#$%()(#*!()!KL<><MQLMNQNQJQK sdfkjsdrow32234545fdf>?N<:{LWPW";
	public static final String TOKEN_PREFIX = "gjjz";

	public static String generToken(Map<String, Object> payLoad, long ttlMillis) {

		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
		long nowMillis = System.currentTimeMillis();
		// 生成签名密钥 就是一个base64加密后的字符串
		byte[] apiKeySecretBytes = DatatypeConverter
				.parseBase64Binary(sercetKey);
		Key signingKey = new SecretKeySpec(apiKeySecretBytes,
				signatureAlgorithm.getJcaName());

		JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JWT")
				.setHeaderParam("alg", "HS256").setClaims(payLoad)
				.signWith(signatureAlgorithm, signingKey);

		if (ttlMillis >= 0) {
			long expMillis = nowMillis + ttlMillis;
			Date exp = new Date(expMillis);
			builder.setExpiration(exp);
		}
		// 生成JWT
		String jwt = TOKEN_PREFIX + builder.compact();
		return jwt;
	}
    /***
     *  验证TOKEN,如果成功则更新TOKEN,并返回解密的数据
     * @param token
     * @param ttlMillis
     * @return
     */
	public static ResponseData verifyAndUpdateToken(String token,long ttlMillis) {

		ResponseData responseData = verifyToken(token);
		if (responseData.getCode()==Constant.JWT_SUCCESS) {
			Map<String, Object> data = new HashMap<String, Object>();
			Claims claims = (Claims) responseData.getResult();
			data.put("token", generToken(claims,ttlMillis));
			data.put("payload", claims);
			responseData.setResult(data);
		}
		return responseData;
	}

	public static ResponseData verifyToken(String token) {
		ResponseData responseData = new ResponseData();
		try {
			Claims claims = Jwts
					.parser()
					.setSigningKey(DatatypeConverter.parseBase64Binary(sercetKey))
					.parseClaimsJws(token.replace(TOKEN_PREFIX, "")).getBody();
			responseData.setCode(Constant.JWT_SUCCESS);
			responseData.setMessage("验证通过");
			responseData.setResult(claims);

		} catch (ExpiredJwtException e) {
			responseData.setCode(100);
			responseData.setMessage("已过期，请重新登录");
		} catch (SignatureException e) {
			responseData.setCode(100);
			responseData.setMessage("权限验证失败，请重新登录");
		} catch (Exception e) {
			responseData.setCode(100);
			responseData.setMessage("权限验证失败，请重新登录");
		}
		return responseData;
	}

	
    public static void main(String args[])
    {
    	
    	String token = "gjjzeyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6MjA5NjIyMiwiZXhwIjoxNTQ3NjI5NTgwLCJkZXZpY2VUb2tlbiI6IjQzNjA0NUI2LTc0MzMtNEYyNy04NUEyLThDQTBGM0VCOEFCNCJ9.wWzF2xq5-UwVHsICtW0FuBn69dkcuOWV_OP0CMW7JoE";
          
    	ResponseData data = verifyToken(token);
		JSONObject payload = (JSONObject) JSON.toJSON(data.getResult());
		Long id = payload.getLong("id");
		String deviceToken= payload.getString("deviceToken");
		System.out.println(id);
		System.out.println(deviceToken);
    	
    	
    }
}
