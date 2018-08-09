package com.example.tmac.testapp.dto.vo;

import com.example.tmac.testapp.utils.AbstractBaseObject;

public class TotpKeyVO extends AbstractBaseObject{
	// 服务器时间
	private long time;
	private short step;
	private String newKey;

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public short getStep() {
		return step;
	}

	public void setStep(short step) {
		this.step = step;
	}

	public String getNewKey() {
		return newKey;
	}

	public void setNewKey(String newKey) {
		this.newKey = newKey;
	}
}
