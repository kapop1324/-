package com.web.curation.controller.account;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.web.curation.model.Rank;
import com.web.curation.model.Rankinglist;
import com.web.curation.model.Rankupdate;
import com.web.curation.model.UserVO;
import com.web.curation.model.service.JwtService;
import com.web.curation.model.service.RankingService;

@CrossOrigin("*")
@RequestMapping("/ranking")
@RestController
public class RankingController {
	
	@Autowired
	RankingService rankingservice;
	
	@GetMapping("/myrank")
	public ResponseEntity<?> myranking(@RequestParam Map map) throws Exception{
		
		String id = (String) map.get("id");
		
		HashMap resultmap = new HashMap();
		String result = "";
		
		try {
			
			Rank myrank = new Rank();
			int recipe_cnt = rankingservice.recipe_cnt(id);
			int recipe_like_cnt = rankingservice.recipe_like_cnt(id);
			int follower_cnt = rankingservice.follower_cnt(id);
			int achieve_cnt = rankingservice.achieve_cnt(id);
			int recipe_comment_cnt = rankingservice.recipe_comment_cnt(id);
			int recipe_comment_like_cnt = rankingservice.recipe_comment_like_cnt(id);
			
			myrank.setRecipe_cnt(recipe_cnt);
			myrank.setRecipe_like_cnt(recipe_like_cnt);
			myrank.setFollower_cnt(follower_cnt);
			myrank.setAchieve_cnt(achieve_cnt);
			myrank.setRecipe_comment_cnt(recipe_comment_cnt);
			myrank.setRecipe_comment_like_cnt(recipe_comment_like_cnt);
		
			resultmap.put("myrank", myrank);
			result = "success";
			resultmap.put("result", result);
			
			
		} catch (Exception e) {
			result = "error";
			resultmap.put("result", result);
		}
		
		return new ResponseEntity(resultmap,HttpStatus.OK);
	}
	
	@PostMapping("/updatemyrank")
	public ResponseEntity<?> updatemyrank(@RequestBody Rankupdate rk){
		
		String result = "";
		
		try {
			
			if(rankingservice.updatemyrank(rk)) {
				
				result = "success";
				
			}else {
				result = "false";
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			result = "error";
		}
		
		return new ResponseEntity(result,HttpStatus.OK);
		
	}
	
	@GetMapping("rankinglist")
	public ResponseEntity<?> getrankinglist(){
		
		HashMap resultmap = new HashMap();
		String result = "";
				
		try {
			
			List<Rankinglist> rankinglist = rankingservice.getrankinglist();
			
			if(rankinglist != null) {
				resultmap.put("rankinglist", rankinglist);
				
				result = "success";
				resultmap.put("result", result);
			}else {
				result = "fail";
				resultmap.put("result", result);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			result = "error";
			resultmap.put("result", result);
		}
		
		
		return new ResponseEntity(resultmap,HttpStatus.OK);
	}

}
