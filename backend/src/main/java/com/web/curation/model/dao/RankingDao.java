
package com.web.curation.model.dao;

import java.util.List;

import com.web.curation.model.Rankinglist;
import com.web.curation.model.Rankupdate;

public interface RankingDao {
	
	public int recipe_cnt(String id) throws Exception;
	
	public int recipe_like_cnt(String id) throws Exception;
	
	public int follower_cnt(String id) throws Exception;
	
	public int achieve_cnt(String id) throws Exception;
	
	public int recipe_comment_cnt(String id) throws Exception;
	
	public int recipe_comment_like_cnt(String id) throws Exception;
	
	public boolean updatemyrank(Rankupdate rk) throws Exception;
	
	public List<Rankinglist> getrankinglist() throws Exception;

}