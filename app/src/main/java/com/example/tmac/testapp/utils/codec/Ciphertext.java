package com.example.tmac.testapp.utils.codec;

import org.apache.commons.codec.binary.Base64;

/**
 * 密文
 * 
 * @author zx
 *
 */
public class Ciphertext {
	private byte[] content;

	public Ciphertext(byte[] content) {
		this.content = content;
	}

	public String getHex() {
		return HexUtils.parseByte2HexStr(content);
	}

	public String getBase64() {
		return new String(Base64.encodeBase64(content));
	}

	public String getString() {
		return new String(content);
	}
}
