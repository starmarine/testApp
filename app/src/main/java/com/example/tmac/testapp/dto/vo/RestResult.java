package com.example.tmac.testapp.dto.vo;

import com.example.tmac.testapp.utils.AbstractBaseObject;

import java.io.Serializable;

public class RestResult extends AbstractBaseObject implements Serializable {

	protected String data;
	protected String httpStatus;
	protected String detail;

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(String httpStatus) {
		this.httpStatus = httpStatus;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}
}
