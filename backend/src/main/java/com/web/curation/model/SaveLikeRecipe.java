package com.web.curation.model;

public class SaveLikeRecipe {
	
	private int recipe_idx;
	private String recipe_user_id;
	
	public int getRecipe_idx() {
		return recipe_idx;
	}
	public void setRecipe_idx(int recipe_idx) {
		this.recipe_idx = recipe_idx;
	}
	public String getRecipe_user_id() {
		return recipe_user_id;
	}
	public void setRecipe_user_id(String recipe_user_id) {
		this.recipe_user_id = recipe_user_id;
	}
}
