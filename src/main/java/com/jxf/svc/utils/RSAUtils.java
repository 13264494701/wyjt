package com.jxf.svc.utils;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;



/**
 * Utils - RSA加密/解密
 * 
 * @author JINXINFU
 * @version 2.0
 */
public final class RSAUtils {

	/** 密钥算法 */
	private static final String KEY_ALGORITHM = "RSA";

	/** 加密/解密算法 */
	private static final String TRANSFORMATION = "RSA/ECB/PKCS1Padding";

	/** 安全服务提供者 */
	private static final Provider PROVIDER = new BouncyCastleProvider();

	/**
	 * 不可实例化
	 */
	private RSAUtils() {
	}

	/**
	 * 生成密钥对
	 * 
	 * @param keySize
	 *            密钥大小
	 * @return 密钥对
	 */
	public static KeyPair generateKeyPair(int keySize) {
	

		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM, PROVIDER);
			keyPairGenerator.initialize(keySize);
			return keyPairGenerator.generateKeyPair();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * 生成私钥
	 * 
	 * @param encodedKey
	 *            密钥编码
	 * @return 私钥
	 */
	public static PrivateKey generatePrivateKey(byte[] encodedKey) {
		

		try {
			KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM, PROVIDER);
			return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(encodedKey));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (InvalidKeySpecException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * 生成私钥
	 * 
	 * @param keyString
	 *            密钥字符串(BASE64编码)
	 * @return 私钥
	 */
	public static PrivateKey generatePrivateKey(String keyString) {
	

		return generatePrivateKey(Base64.decodeBase64(keyString));
	}

	/**
	 * 生成公钥
	 * 
	 * @param encodedKey
	 *            密钥编码
	 * @return 公钥
	 */
	public static PublicKey generatePublicKey(byte[] encodedKey) {


		try {
			KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM, PROVIDER);
			return keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (InvalidKeySpecException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * 生成公钥
	 * 
	 * @param keyString
	 *            密钥字符串(BASE64编码)
	 * @return 公钥
	 */
	public static PublicKey generatePublicKey(String keyString) {


		return generatePublicKey(Base64.decodeBase64(keyString));
	}

	/**
	 * 获取密钥字符串
	 * 
	 * @param key
	 *            密钥
	 * @return 密钥字符串(BASE64编码)
	 */
	public static String getKeyString(Key key) {
	

		return Base64.encodeBase64String(key.getEncoded());
	}

	/**
	 * 获取密钥
	 * 
	 * @param type
	 *            类型
	 * @param inputStream
	 *            输入流
	 * @param password
	 *            密码
	 * @return 密钥
	 */
	public static Key getKey(String type, InputStream inputStream, String password) {


		try {
			KeyStore keyStore = KeyStore.getInstance(type, PROVIDER);
			keyStore.load(inputStream, password != null ? password.toCharArray() : null);
			String alias = keyStore.aliases().hasMoreElements() ? keyStore.aliases().nextElement() : null;
			return keyStore.getKey(alias, password != null ? password.toCharArray() : null);
		} catch (KeyStoreException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (CertificateException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (UnrecoverableKeyException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * 获取证书
	 * 
	 * @param type
	 *            类型
	 * @param inputStream
	 *            输入流
	 * @return 证书
	 */
	public static Certificate getCertificate(String type, InputStream inputStream) {


		try {
			CertificateFactory certificateFactory = CertificateFactory.getInstance(type, PROVIDER);
			return certificateFactory.generateCertificate(inputStream);
		} catch (CertificateException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * 生成签名
	 * 
	 * @param algorithm
	 *            签名算法
	 * @param privateKey
	 *            私钥
	 * @param data
	 *            数据
	 * @return 签名
	 */
	public static byte[] sign(String algorithm, PrivateKey privateKey, byte[] data) {


		try {
			Signature signature = Signature.getInstance(algorithm, PROVIDER);
			signature.initSign(privateKey);
			signature.update(data);
			return signature.sign();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (InvalidKeyException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (SignatureException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * 验证签名
	 * 
	 * @param algorithm
	 *            签名算法
	 * @param publicKey
	 *            公钥
	 * @param sign
	 *            签名
	 * @param data
	 *            数据
	 * @return 是否验证通过
	 */
	public static boolean verify(String algorithm, PublicKey publicKey, byte[] sign, byte[] data) {


		try {
			Signature signature = Signature.getInstance(algorithm, PROVIDER);
			signature.initVerify(publicKey);
			signature.update(data);
			return signature.verify(sign);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (InvalidKeyException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (SignatureException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * 验证签名
	 * 
	 * @param algorithm
	 *            签名算法
	 * @param certificate
	 *            证书
	 * @param sign
	 *            签名
	 * @param data
	 *            数据
	 * @return 是否验证通过
	 */
	public static boolean verify(String algorithm, Certificate certificate, byte[] sign, byte[] data) {


		try {
			Signature signature = Signature.getInstance(algorithm, PROVIDER);
			signature.initVerify(certificate);
			signature.update(data);
			return signature.verify(sign);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (InvalidKeyException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (SignatureException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * 加密
	 * 
	 * @param publicKey
	 *            公钥
	 * @param data
	 *            数据
	 * @return 密文
	 */
	public static byte[] encrypt(PublicKey publicKey, byte[] data) {
	

		try {
			Cipher cipher = Cipher.getInstance(TRANSFORMATION, PROVIDER);
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			return cipher.doFinal(data);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (NoSuchPaddingException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (InvalidKeyException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (IllegalBlockSizeException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (BadPaddingException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * 解密
	 * 
	 * @param privateKey
	 *            私钥
	 * @param data
	 *            数据
	 * @return 明文
	 */
	public static byte[] decrypt(PrivateKey privateKey, byte[] data) {
	
		try {
			Cipher cipher = Cipher.getInstance(TRANSFORMATION, PROVIDER);
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			return cipher.doFinal(data);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (NoSuchPaddingException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (InvalidKeyException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (IllegalBlockSizeException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (BadPaddingException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	
	/**
	 * 加密
	 * 
	 * @param publicKey
	 *            公钥
	 * @param data
	 *            数据
	 * @return 密文
	 */
	public static String encrypt(PublicKey publicKey, String data) {
	

		try {
			Cipher cipher = Cipher.getInstance(TRANSFORMATION, PROVIDER);
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			return Encodes.encodeBase64(cipher.doFinal(data.getBytes()));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (NoSuchPaddingException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (InvalidKeyException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (IllegalBlockSizeException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (BadPaddingException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	/**
	 * 解密
	 * 
	 * @param privateKey
	 *            私钥
	 * @param data
	 *            数据
	 * @return 明文
	 */
	public static String decrypt(PrivateKey privateKey, String data) {
	
		try {
			Cipher cipher = Cipher.getInstance(TRANSFORMATION, PROVIDER);
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			return new String(cipher.doFinal(Encodes.decodeBase64(data)));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (NoSuchPaddingException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (InvalidKeyException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (IllegalBlockSizeException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (BadPaddingException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	/**
	 * 私钥签名
	 * @param src 源字符串
	 * @param priKey 私钥
	 * @param charset 编码方式
	 * @return byte[] 加密后的字节数组
	 * @throws Exception 异常
	 */
    public static byte[] generateSHA1withRSASigature(String src, String priKey, String charset) throws Exception
    {
    	Signature sigEng = Signature.getInstance("SHA1withRSA");
    	byte[] pribyte = Encodes.decodeBase64(priKey);
    	PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pribyte);
    	KeyFactory fac = KeyFactory.getInstance("RSA");
    	RSAPrivateKey privateKey = (RSAPrivateKey) fac.generatePrivate(keySpec);
    	sigEng.initSign(privateKey);
    	sigEng.update(src.getBytes(charset));
    	byte[] signature = sigEng.sign();
    	return signature;
    }

}