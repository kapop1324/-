package com.web.curation.model;

import java.sql.Timestamp;

public class CommentToClient {
	
	private int idx;
	private String comment;
	private Timestamp regdate;
	private String id;
	private String nickname;
	private int like;
	private boolean likecheck;

	
	public int getIdx() {
		return idx;
	}

	public void setIdx(int idx) {
		this.idx = idx;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Timestamp getRegdate() {
		return regdate;
	}

	public void setRegdate(Timestamp regdate) {
		this.regdate = regdate;
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

	public int getLike() {
		return like;
	}

	public void setLike(int like) {
		this.like = like;
	}

	public boolean isLikecheck() {
		return likecheck;
	}

	public void setLikecheck(boolean likecheck) {
		this.likecheck = likecheck;
	}
}
