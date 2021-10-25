package com.web.curation.model;

import java.sql.Timestamp;
import java.util.List;

public class RecipeContent {
	
	private int idx;
	private String title;
	private Timestamp regdate;
	private String nickname;
	private String id;
	private String img;
	private int likes;
	private boolean isLiked;
	private String achieve_master;
	private String achieve_slave;
	private String achieve_title_name;
	private String hashtag;
	private int comment;
	private List<String> hashtags;
	private String profileImg;
	
	public String getHashtag() {
		return hashtag;
	}
	public void setHashtag(String hashtag) {
		this.hashtag = hashtag;
	}
	public String getAchieve_title_name() {
		return achieve_title_name;
	}
	public void setAchieve_title_name(String achieve_title_name) {
		this.achieve_title_name = achieve_title_name;
	}
	public int getIdx() {
		return idx;
	}
	public void setIdx(int idx) {
		this.idx = idx;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Timestamp getRegdate() {
		return regdate;
	}
	public void setRegdate(Timestamp regdate) {
		this.regdate = regdate;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public int getLikes() {
		return likes;
	}
	public void setLikes(int likes) {
		this.likes = likes;
	}
	public boolean isLiked() {
		return isLiked;
	}
	public void setLiked(boolean isLiked) {
		this.isLiked = isLiked;
	}
	
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
	public int getComment() {
		return comment;
	}
	public void setComment(int comment) {
		this.comment = comment;
	}
	public List<String> getHashtags() {
		return hashtags;
	}
	public void setHashtags(List<String> hashtags) {
		this.hashtags = hashtags;
	}
	public String getProfileImg() {
		return profileImg;
	}
	public void setProfileImg(String profileImg) {
		this.profileImg = profileImg;
	}
}
