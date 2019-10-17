package com.tpadsz.after.util;

import com.alibaba.fastjson.JSON;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encryption {
	
	private static Logger logger = Logger.getLogger(Encryption.class);
	private static final int INTERATIONS = 1024;
	private static final int SALT_SIZE = 8;


	public static class HashPassword {
		private String salt;
		private String password;

		public HashPassword(String salt, String password) {
			super();
			this.salt = salt;
			this.password = password;
		}

		public String getSalt() {
			return salt;
		}

		public String getPassword() {
			return password;
		}

	}

	public static String getMD5Str(String str) {
		MessageDigest messageDigest = null;

		try {
			messageDigest = MessageDigest.getInstance("MD5");

			messageDigest.reset();

			messageDigest.update(str.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
//			System.out.println("NoSuchAlgorithmException caught!");
			return null;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}

		byte[] byteArray = messageDigest.digest();

		StringBuffer md5StrBuff = new StringBuffer();

		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
				md5StrBuff.append("0").append(
						Integer.toHexString(0xFF & byteArray[i]));
			else
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
		}

		return md5StrBuff.toString();
	}

	public static String getMD5(File file) {
		String name = "";
		try {
			InputStream inputStream = new FileInputStream(file);
			byte[] bytes = new byte[1024];
			int len = 0;
			MessageDigest messagedigest = MessageDigest.getInstance("MD5");
			while ((len = inputStream.read(bytes)) > 0) {
				messagedigest.update(bytes, 0, len);
			}
			name = MD5Utils.bufferToHex(messagedigest.digest());
			inputStream.close();
		} catch (MalformedURLException e) {
			logger.warn(e);
		} catch (IOException e) {
			logger.warn(e);
		} catch (NoSuchAlgorithmException e) {
			logger.warn(e);
		}
		return name;
	}
	
	public static String encode(String params){
		try {
			return URLEncoder.encode(Encodes.encodeBase64(DESCoder.encrypt(params.getBytes())), "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static byte[] decode(String params){
		try {
			return DESCoder.decrypt(Encodes.decodeBase64(URLDecoder.decode(params, "utf-8")));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static HashPassword encrypt(String plainText) {
		byte[] salt = Digests.generateSalt(SALT_SIZE);
		String hashsalt = Encodes.encodeHex(salt);
		String hashpassword = encrypt(plainText, salt);
		HashPassword result = new HashPassword(hashsalt, hashpassword);
		return result;
	}

	public static String encrypt(String actual, String salt) {
		return encrypt(actual, Encodes.decodeHex(salt));
	}

	public static String encrypt(String actual, byte[] salt) {
		byte[] hashPassword = Digests.sha1(actual.getBytes(), salt, INTERATIONS);
		return Encodes.encodeHex(hashPassword);
	}



	public static void main(String[] args) {
		String str="odelic";
		String account="odelic";
		Encryption.HashPassword password = Encryption.encrypt(Encryption.getMD5Str(str));
		String confirm = Encryption.encrypt(Encryption.getMD5Str(str), "5785e2ed9a98db28");
		System.out.println(JSON.toJSONString(password)+"\t"+"f4883e9673bb5c46c862d8625d386064d66690e2".equals(confirm));
	}
}
