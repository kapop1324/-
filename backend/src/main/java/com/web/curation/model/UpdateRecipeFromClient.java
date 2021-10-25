package com.web.curation.model;

import java.util.List;

public class UpdateRecipeFromClient {
	
   private int recipe_idx;
   private String title;
   private List<Integer> content_idx;
   private List<String> img;
   private List<String> des;
   private List<String> thumbnail;
   private List<Integer> content_order;
   private List<Integer> deleteContents;
   private String achieve_master;
   private String achieve_slave;
   private List<Integer> hashtag_idx;
   private List<String> tag;
   private List<Integer> deletehashtag;
   

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
   public List<Integer> getHashtag_idx() {
      return hashtag_idx;
   }
   public void setHashtag_idx(List<Integer> hashtag_idx) {
      this.hashtag_idx = hashtag_idx;
   }
   public List<String> getTag() {
      return tag;
   }
   public void setTag(List<String> tag) {
      this.tag = tag;
   }
   public List<Integer> getDeletehashtag() {
      return deletehashtag;
   }
   public void setDeletehashtag(List<Integer> deletehashtag) {
      this.deletehashtag = deletehashtag;
   }
   public List<Integer> getDeleteContents() {
      return deleteContents;
   }
   public void setDeleteContents(List<Integer> deleteContents) {
      this.deleteContents = deleteContents;
   }
   public int getRecipe_idx() {
      return recipe_idx;
   }
   public void setRecipe_idx(int recipe_idx) {
      this.recipe_idx = recipe_idx;
   }
   public String getTitle() {
      return title;
   }
   public void setTitle(String title) {
      this.title = title;
   }
   public List<Integer> getContent_idx() {
      return content_idx;
   }
   public void setContent_idx(List<Integer> content_idx) {
      this.content_idx = content_idx;
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
   public List<Integer> getContent_order() {
      return content_order;
   }
   public void setContent_order(List<Integer> content_order) {
      this.content_order = content_order;
   }
}