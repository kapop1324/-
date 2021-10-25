package com.web.curation.controller.account;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.web.curation.model.Achieve;
import com.web.curation.model.AchieveTitleDetail;
import com.web.curation.model.AchieveTitleList;
import com.web.curation.model.Achievecurrent;
import com.web.curation.model.RecipeContent;
import com.web.curation.model.service.AchieveService;
import com.web.curation.model.service.JwtService;
import com.web.curation.model.service.UserService;

@CrossOrigin("*")
@RequestMapping("/achieve")
@RestController
public class AchieveController {

	@Autowired
	AchieveService achieveservice;
	

	@GetMapping("list")
	public ResponseEntity<?> getachievelist() {

		HashMap resultmap = new HashMap();

		String result = "";

		try {
			result = "success";

			List<Achieve> masterlist = achieveservice.getachievemasterlist();
			List<Achieve> slavelist = achieveservice.getachieveslavelist();

			resultmap.put("masterlist", masterlist);
			resultmap.put("slavelist", slavelist);
			resultmap.put("result", result);

		} catch (Exception e) {
			e.printStackTrace();
			result = "error";
			resultmap.put("result", result);
		}

		return new ResponseEntity<>(resultmap, HttpStatus.OK);
	}

	@GetMapping("/mastercount")
	public ResponseEntity<Map<String, Object>> getAchieveMasterCount() {
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		String result = "SUCCESS";
		try {
			List<Integer> count = achieveservice.getMasterCount();

			resultMap.put("masterCount", count);

			if (count == null) {
				result = "FAIL";
			} else {
				result = "SUCCESS";
			}

			resultMap.put("message", result);
			status = HttpStatus.ACCEPTED;

		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	}
	
	@GetMapping("/getAchievecurrent")
	public ResponseEntity<Map<String, Object>> getachievecurrent(@RequestParam Map map) {
		
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		String result = "SUCCESS";
		try {
			String id = (String) map.get("id");
			
			List<Achievecurrent> achieve = achieveservice.getAchievecurrent(id);
			if(achieve == null) {
				result = "fail";
			}else {
				resultMap.put("achieve", achieve);
			}
			
			resultMap.put("message", result);
			status = HttpStatus.ACCEPTED;

		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("message", "error");
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	}

	@GetMapping("/categorylist")
	public ResponseEntity<Map<String, Object>> getAchieveCategorylistMasterCount(@RequestParam String master) {
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		String result = "SUCCESS";
		try {
			List<Achieve> list = achieveservice.getMasterCategoryCount(master);

			resultMap.put("list", list);

			if (list.isEmpty()) {
				result = "FAIL";
			} else {
				result = "SUCCESS";
			}

			resultMap.put("message", result);
			status = HttpStatus.ACCEPTED;

		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	}

	@GetMapping("/myRecipe")
	public ResponseEntity<Map<String, Object>> getCategoryRecipe(@RequestParam String id,@RequestParam String master) {
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		String result = "SUCCESS";
		Map<String, String> map = new HashMap<>();
		map.put("id", id);
		map.put("master", master);

		try {
			List<RecipeContent> recipe = achieveservice.getCategoryRecipe(map);
			
			resultMap.put("latestFeed", recipe);

			if (recipe == null) {
				result = "FAIL";
			} else {
				result = "SUCCESS";
			}

			resultMap.put("message", result);
			status = HttpStatus.ACCEPTED;

		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	}
	
	@GetMapping("/titleList")
	public ResponseEntity<Map<String, Object>> getTitleList(@RequestParam String id) {
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		String result = "SUCCESS";

		try {
			List<AchieveTitleList> TitleList = achieveservice.getTitleList(id);
			
			resultMap.put("list", TitleList);

			if (TitleList == null) {
				result = "FAIL";
			} else {
				result = "SUCCESS";
			}

			resultMap.put("message", result);
			status = HttpStatus.ACCEPTED;

		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	}
	
	@GetMapping("/titleOne")
	public ResponseEntity<Map<String, Object>> getTitleOne(@RequestParam String id,@RequestParam String title) {
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		String result = "SUCCESS";
		Map<String, String> map = new HashMap<>();
		map.put("id", id);
		map.put("title", title);

		try {
			List<AchieveTitleList> TitleDetailOne = achieveservice.getTitleOne(map);
			
			resultMap.put("list", TitleDetailOne);

			if (TitleDetailOne == null) {
				result = "FAIL";
			} else {
				result = "SUCCESS";
			}

			resultMap.put("message", result);
			status = HttpStatus.ACCEPTED;

		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	}
	
	@GetMapping("/titleDetail")
	public ResponseEntity<Map<String, Object>> getTitleDetail(@RequestParam String id,@RequestParam String title) {
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		String result = "SUCCESS";
		Map<String, String> map = new HashMap<>();
		map.put("id", id);
		map.put("title", title);

		try {
			List<AchieveTitleDetail> TitleDetail = achieveservice.getTitleDetail(map);
			

			
			resultMap.put("list", TitleDetail);

			if (TitleDetail == null) {
				result = "FAIL";
			} else {
				result = "SUCCESS";
			}

			resultMap.put("message", result);
			status = HttpStatus.ACCEPTED;

		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	}
}