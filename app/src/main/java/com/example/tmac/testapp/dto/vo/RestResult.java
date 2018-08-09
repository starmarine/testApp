package com.example.tmac.testapp.dto.vo;

import com.example.tmac.testapp.constants.HttpStatus;
import com.example.tmac.testapp.utils.AbstractBaseObject;

import java.io.Serializable;

public class RestResult extends AbstractBaseObject implements Serializable {

	protected String data;
	protected String httpStatus;
	protected String detail;
	private boolean success;
	private String message;

	public boolean isHttpStatusOK(){
		return HttpStatus.OK.equalsIgnoreCase(httpStatus);
	}

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

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
