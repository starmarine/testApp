package com.example.tmac.testapp.dto.dto;

public class DeviceInfo {
	private String info;
	private String type;
	private int year;

	public DeviceInfo(String info,String type,int year){
		this.info = info;
		this.type = type;
		this.year = year;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

}
