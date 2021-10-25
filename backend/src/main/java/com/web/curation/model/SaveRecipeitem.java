package com.web.curation.model;

import java.util.List;

public class SaveRecipeitem {
	
	private String title;
	private String id;
	private String nickname;
	private List<String> img;
	private List<String> des;
	private List<String> thumbnail;
	private List<String> hashtags;
	private int likes;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public List<String> getImg() {
		return img;
	}

	public void setImg(List<String> img) {
		this.img = img;
	}

	public List<String> getDes() {
		return des;
	}

	public void setDes(List<String> des) {
		this.des = des;
	}

	public List<String> getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(List<String> thumbnail) {
		this.thumbnail = thumbnail;
	}

	public List<String> getHashtags() {
		return hashtags;
	}

	public void setHashtags(List<String> hashtags) {
		this.hashtags = hashtags;
	}
	
	public int getLikes() {
		return likes;
	}

	public void setLikes(int likes) {
		this.likes = likes;
	}


}
