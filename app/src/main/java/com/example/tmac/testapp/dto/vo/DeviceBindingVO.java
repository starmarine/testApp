package com.example.tmac.testapp.dto.vo;

import com.example.tmac.testapp.utils.AbstractBaseObject;

import java.io.Serializable;

public class DeviceBindingVO extends AbstractBaseObject implements Serializable {
	
	private String userId;
	private String displayName;
	private String phone;

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
}
