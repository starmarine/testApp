package com.example.tmac.testapp.utils;

import com.example.tmac.testapp.utils.codec.HexUtils;

import java.lang.reflect.UndeclaredThrowableException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.util.Date;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * 基于时间的动态口令
 * 
 * TOTP(Time-Based One-Time Password Algorithm) — 时间同步型动态口令
 * 
 * TOTP(RFC 6238)[https://tools.ietf.org/html/rfc6238]
 * 
 * @author zrkj09
 *
 */
public class TOTPPasswordGenerator {


	private static final int[] DIGITS_POWER = { 1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000 };

	/**
	 * This method uses the JCE to provide the crypto algorithm. HMAC computes a
	 * Hashed Message Authentication Code with the crypto hash algorithm as a
	 * parameter.
	 *
	 * @param crypto
	 *            the crypto algorithm (HmacSHA1, HmacSHA256, HmacSHA512)
	 * @param keyBytes
	 *            the bytes to use for the HMAC key
	 * @param text
	 *            the message or text to be authenticated.
	 */
	private static byte[] hmac_sha1(String crypto, byte[] keyBytes, byte[] text) {
		try {
			Mac hmac;
			hmac = Mac.getInstance(crypto);
			SecretKeySpec macKey = new SecretKeySpec(keyBytes, "RAW");
			hmac.init(macKey);
			return hmac.doFinal(text);
		} catch (GeneralSecurityException gse) {
			throw new UndeclaredThrowableException(gse);
		}
	}

	/**
	 * This method converts HEX string to Byte[]
	 *
	 * @param hex
	 *            the HEX string
	 * @return A byte array
	 */
	private static byte[] hexStr2Bytes(String hex) {
		// Adding one byte to get the right conversion
		// values starting with "0" can be converted
		byte[] bArray = new BigInteger("10" + hex, 16).toByteArray();

		// Copy all the REAL bytes, not the "first"
		byte[] ret = new byte[bArray.length - 1];
		for (int i = 0; i < ret.length; i++)
			ret[i] = bArray[i + 1];
		return ret;
	}

	public static String generateTOTP(String key, String time, String returnDigits) {
		return generateTOTP(key, time, returnDigits, "HmacSHA1");
	}


	public static String generateTOTP256(String key, String time, String returnDigits) {
		return generateTOTP(key, time, returnDigits, "HmacSHA256");
	}

	public static String generateTOTP512(String key, String time, String returnDigits) {
		return generateTOTP(key, time, returnDigits, "HmacSHA512");
	}

	private static String generateTOTP(String key, String time, String returnDigits, String crypto) {
		int codeDigits = Integer.decode(returnDigits).intValue();
		String result = null;
		byte[] hash;

		// Using the counter
		// First 8 bytes are for the movingFactor
		// Complaint with base RFC 4226 (HOTP)
		while (time.length() < 16)
			time = "0" + time;

		// Get the HEX in a Byte[]
		byte[] msg = hexStr2Bytes(time);

		// Adding one byte to get the right conversion
		byte[] k = hexStr2Bytes(key);

		hash = hmac_sha1(crypto, k, msg);
		// put selected bytes into result int
		int offset = hash[hash.length - 1] & 0xf;

		int binary = ((hash[offset] & 0x7f) << 24) | ((hash[offset + 1] & 0xff) << 16) | ((hash[offset + 2] & 0xff) << 8) | (hash[offset + 3] & 0xff);

		int otp = binary % DIGITS_POWER[codeDigits];

		result = Integer.toString(otp);
		while (result.length() < codeDigits) {
			result = "0" + result;
		}
		return result;
	}

	public static String generateOTP(String key, Date date, int duration, int digits) {
		String time = (date.getTime() / 1000) / duration + "";
		return generateTOTP(HexUtils.parseByte2HexStr(key.getBytes(Charset.forName("utf-8"))), time, "" + digits);
	}

	public static String generateOTP(String key,long diff, int duration) {
		Date date = new Date();
		String steps = ((date.getTime() - diff)/ 1000) / duration + "";
		String code = generateTOTP(HexUtils.parseByte2HexStr(key.getBytes(Charset.forName("utf-8"))), steps, "6");
		return code;
	}
}
