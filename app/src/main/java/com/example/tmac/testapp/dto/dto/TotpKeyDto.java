package com.example.tmac.testapp.dto.dto;

import com.example.tmac.testapp.utils.AbstractBaseObject;

public class TotpKeyDto extends AbstractBaseObject {
	private String oldKey;
	
	private String deviceCode;

	public TotpKeyDto(String oldKey,String deviceCode){
		this.oldKey = oldKey;
		this.deviceCode = deviceCode;
	}

	public String getOldKey() {
		return oldKey;
	}

	public void setOldKey(String oldKey) {
		this.oldKey = oldKey;
	}

	public String getDeviceCode() {
		return deviceCode;
	}

	public void setDeviceCode(String deviceCode) {
		this.deviceCode = deviceCode;
	}
}
