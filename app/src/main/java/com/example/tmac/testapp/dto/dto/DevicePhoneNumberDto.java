package com.example.tmac.testapp.dto.dto;

import com.example.tmac.testapp.utils.AbstractBaseObject;

public class DevicePhoneNumberDto extends AbstractBaseObject {

	private String ticket;
	private String phoneNumber;

	public DevicePhoneNumberDto(String ticket,String phoneNumber){
		this.ticket = ticket;
		this.phoneNumber = phoneNumber;
	}

	public String getTicket() {
		return ticket;
	}
	public void setTicket(String bindId) {
		this.ticket = bindId;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
}
