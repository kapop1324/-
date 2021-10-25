package com.web.curation.model;

public class Changepw {
	
	private String id;
	private String newpw;
	private String oldpw;
	private String token;
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNewpw() {
		return newpw;
	}
	public void setNewpw(String newpw) {
		this.newpw = newpw;
	}
	public String getOldpw() {
		return oldpw;
	}
	public void setOldpw(String oldpw) {
		this.oldpw = oldpw;
	}

}
