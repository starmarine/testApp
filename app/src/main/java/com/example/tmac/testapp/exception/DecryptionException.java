package com.example.tmac.testapp.exception;

public class DecryptionException extends ApplicationException {

	private static final long serialVersionUID = 4330097988753101123L;

	public DecryptionException(String message) {
		super(message);
	}
	
	public DecryptionException(String message,Exception e) {
		super(message,e);
	}


}
