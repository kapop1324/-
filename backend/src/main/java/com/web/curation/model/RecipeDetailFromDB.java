package com.web.curation.model;

public class RecipeDetailFromDB {
	
	private int idx;
	private String img;
	private String des;
	private String thumbnail;
	private int recipe_idx;
	private int likes;
	private boolean isLiked;
	private int content_order;
	
	public int getContent_order() {
		return content_order;
	}
	public void setContent_order(int content_order) {
		this.content_order = content_order;
	}
	public int getIdx() {
		return idx;
	}
	public void setIdx(int idx) {
		this.idx = idx;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getDes() {
		return des;
	}
	public void setDes(String des) {
		this.des = des;
	}
	public String getThumbnail() {
		return thumbnail;
	}
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
	public int getRecipe_idx() {
		return recipe_idx;
	}
	public void setRecipe_idx(int recipe_idx) {
		this.recipe_idx = recipe_idx;
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
	
	
}
