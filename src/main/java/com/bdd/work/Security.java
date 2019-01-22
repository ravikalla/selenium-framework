package com.bdd.work;

import org.apache.log4j.Logger;
import com.bdd.utilities.ExecutionContext;
import com.bdd.utilities.MyContainer;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Security {
	
	private String characterEncoding = "UTF-8";
	private String cipherTransformaation = "AES/CBC/PKCS5PADDING";
	private String aesEncryptionAlgorithem = "AES";
	
	static final Logger logger = Logger.getLogger(Security.class);
	ExecutionContext context = (ExecutionContext)MyContainer.getInstance(Thread.currentThread().getId());	
	
	public Security() {
		
		
	}
	
	public String encryptPassword(String plainText) {
		
		
		String encryptedText = "";
		try {
			
			String encryptionKey = System.getProperty("key1") + System.getProperty("key2");
			Cipher cipher = Cipher.getInstance(this.cipherTransformaation);
			byte[] key = encryptionKey.getBytes(this.characterEncoding);
			SecretKeySpec secretkey = new SecretKeySpec(key, this.aesEncryptionAlgorithem);
			IvParameterSpec ivParameterSpec = new IvParameterSpec(key);
			cipher.init(1, secretkey, ivParameterSpec);
			Encoder encoder = Base64.getEncoder();
			byte[] ciphterText = encoder.encode(encryptedText.getBytes("UTF8"));
			
			encryptedText = new String(cipher.doFinal(ciphterText),"UTF8");
			
		}catch(Exception var3) {
			logger.info("Exception - " + var3);
		}
		return encryptedText;
		
	}
	
	public String decryptPassword(String encryptText) {
		String decryptedText = "";
		try {
			
			String encryptionKey = System.getProperty("key1") + System.getProperty("key2");
			Cipher cipher = Cipher.getInstance(this.cipherTransformaation);
			byte[] key = encryptionKey.getBytes(this.characterEncoding);
			SecretKeySpec secretkey = new SecretKeySpec(key, this.aesEncryptionAlgorithem);
			IvParameterSpec ivParameterSpec = new IvParameterSpec(key);
			cipher.init(2, secretkey, ivParameterSpec);
			Decoder decoder = Base64.getDecoder();
			byte[] ciphterText = decoder.decode(encryptText.getBytes("UTF8"));
			
			decryptedText = new String(cipher.doFinal(ciphterText),"UTF8");
			
		}catch(Exception var3) {
			logger.info("Exception - " + var3);
		}
		return decryptedText;
	}


}
