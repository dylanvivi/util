package com.dylan.entity;

/**
 * 通过数据库内表的字段动态生成 javabean
 * @author dylan
 **/
public class WaasSetting {
	private int id;

	private String mtContent;

	private String moContent;

	private int dadSid;

	private int activityId;

	public WaasSetting() {

	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	public void setMtContent(String mtContent) {
		this.mtContent = mtContent;
	}

	public String getMtContent() {
		return this.mtContent;
	}

	public void setMoContent(String moContent) {
		this.moContent = moContent;
	}

	public String getMoContent() {
		return this.moContent;
	}

	public void setDadSid(int dadSid) {
		this.dadSid = dadSid;
	}

	public int getDadSid() {
		return this.dadSid;
	}

	public void setActivityId(int activityId) {
		this.activityId = activityId;
	}

	public int getActivityId() {
		return this.activityId;
	}
}
