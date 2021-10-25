package com.web.curation.controller.search;

import java.util.ArrayList;
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

import com.web.curation.model.UserVO;
import com.web.curation.model.service.JwtService;
import com.web.curation.model.service.SearchService;
import com.web.curation.model.service.UserService;

@CrossOrigin("*")
@RequestMapping("/search")
@RestController
public class SearchController {
	@Autowired
	UserService userservice;

	@Autowired
	SearchService searchservice;
	
	@GetMapping("/nickname")
	public ResponseEntity<Map<String, Object>> searchByNickname(@RequestParam Map map) {
		String nickname = (String)map.get("nickname");
		String result = "SUCCESS";
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		
		try {
			List<UserVO> nicknameList = searchservice.searchByNickname(nickname);
			
			resultMap.put("nicknameList", nicknameList);
			
			if(nicknameList == null) {
				result = "FAIL";
			} else {
				result = "SUCCESS";
			}
			
			resultMap.put("message", result);
			status = HttpStatus.ACCEPTED;
			
		} catch(Exception e) {
			e.printStackTrace();
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
}
