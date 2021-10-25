package com.web.curation.model;

public class RecipeInfo {
	
	private String id;
	private String nickname;
	private String title;
	private String achieve_master;
	private String achieve_slave;

	public String getAchieve_master() {
		return achieve_master;
	}

	public void setAchieve_master(String achieve_master) {
		this.achieve_master = achieve_master;
	}

	public String getAchieve_slave() {
		return achieve_slave;
	}

	public void setAchieve_slave(String achieve_slave) {
		this.achieve_slave = achieve_slave;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}