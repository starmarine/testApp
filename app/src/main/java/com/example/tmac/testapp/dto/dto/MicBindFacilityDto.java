package com.example.tmac.testapp.dto.dto;

import com.example.tmac.testapp.utils.AbstractBaseObject;

/**
 *
 * 
 * @author tmac
 *
 */
public class MicBindFacilityDto extends AbstractBaseObject {

	private String ticket;
	private String publicKey;

	public MicBindFacilityDto(String ticket,String publicKey){
		this.ticket = ticket;
		this.publicKey = publicKey;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

}
