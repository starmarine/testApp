package com.example.tmac.testapp.dto.vo;

import com.example.tmac.testapp.utils.AbstractBaseObject;

import java.io.Serializable;

public class ScanQrcodeVO extends AbstractBaseObject implements Serializable{
	private String appName;
	private String accountName;
	private String label;
	private String message;
	private String authId;

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getAuthId() {
		return authId;
	}

	public void setAuthId(String authId) {
		this.authId = authId;
	}
}
