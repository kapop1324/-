package com.web.curation.model;

public class Achievecurrent {
	
	private String id;
	private String title;
	private int clear_cnt;
	private int achieve_cnt;
	private int percent;
	private String clear_img;
	private String fail_img;
	
	public String getClear_img() {
		return clear_img;
	}
	public void setClear_img(String clear_img) {
		this.clear_img = clear_img;
	}
	public String getFail_img() {
		return fail_img;
	}
	public void setFail_img(String fail_img) {
		this.fail_img = fail_img;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getClear_cnt() {
		return clear_cnt;
	}
	public void setClear_cnt(int clear_cnt) {
		this.clear_cnt = clear_cnt;
	}
	public int getAchieve_cnt() {
		return achieve_cnt;
	}
	public void setAchieve_cnt(int achieve_cnt) {
		this.achieve_cnt = achieve_cnt;
	}
	public int getPercent() {
		return percent;
	}
	public void setPercent(int percent) {
		this.percent = percent;
	}

}
