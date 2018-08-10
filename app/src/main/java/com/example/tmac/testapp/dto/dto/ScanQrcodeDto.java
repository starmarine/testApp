package com.example.tmac.testapp.dto.dto;

import com.example.tmac.testapp.utils.AbstractBaseObject;

import java.io.Serializable;

public class ScanQrcodeDto extends AbstractBaseObject implements Serializable {

	private String authId;
	
	private String deviceCode;

	public ScanQrcodeDto(String authId,String deviceCode){
		this.authId = authId;
		this.deviceCode = deviceCode;
	}

	public String getAuthId() {
		return authId;
	}

	public void setAuthId(String authId) {
		this.authId = authId;
	}

	public String getDeviceCode() {
		return deviceCode;
	}

	public void setDeviceCode(String deviceCode) {
		this.deviceCode = deviceCode;
	}
}
