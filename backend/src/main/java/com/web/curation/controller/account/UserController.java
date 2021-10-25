package com.web.curation.controller.account;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.net.HttpHeaders;
import com.web.curation.model.Changepw;
import com.web.curation.model.FollowInfo;
import com.web.curation.model.Requestfollow;
import com.web.curation.model.Snsreg;
import com.web.curation.model.UserInfo;
import com.web.curation.model.UserVO;
import com.web.curation.model.Waiting;
import com.web.curation.model.service.JwtService;
import com.web.curation.model.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import net.bytebuddy.utility.RandomString;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@CrossOrigin("*")
@RequestMapping("/user")
@RestController
public class UserController {

	@Autowired
	UserService userservice;

	@Autowired
	JwtService jwtservice;

	@Autowired
	PasswordEncoder passwordencoder;

	@GetMapping("/login")
	public ResponseEntity<?> login(@RequestParam Map map) {

		String result = "";

		Map resultmap = new HashMap<>();

		try {

			UserVO user = userservice.login(map);

			boolean match = passwordencoder.matches((String) map.get("pw"), user.getPw());
			if (match == true) {
				String token = jwtservice.create("user_id", user.getId(), "access-token");
				resultmap.put("access-token", token);
				resultmap.put("id", user.getId());
				resultmap.put("nickname", user.getNickname());
				resultmap.put("img",user.getImg());
				result = "success";
				resultmap.put("result", result);

			} else {
				result = "fail";
				resultmap.put("result", result);
			}

		} catch (Exception e) {
			e.printStackTrace();
			result = "error";
			resultmap.put("result", result);
		}

		return new ResponseEntity<Map>(resultmap, HttpStatus.OK);
	}
	
	@GetMapping("/snslogin")
	public ResponseEntity<?> snslogin(@RequestParam Map map){
		
		String result = "";

		Map resultmap = new HashMap<>();
		try { 
			map.put("id", map.get("email"));
			UserVO user = userservice.login(map);


			if(user.getSns().equals("kakao")) {
				String email = (String) map.get("email");
				
				String token = jwtservice.create("user_email", email, "access-token");
				resultmap.put("access-token", token);
				resultmap.put("id", user.getId());
				resultmap.put("nickname",user.getNickname());
				resultmap.put("img",user.getImg());
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
		
		
		return new ResponseEntity<Map>(resultmap,HttpStatus.OK);
	}


	@PostMapping("/join")
	public ResponseEntity<String> join(@RequestBody UserVO user) {

		String result = "";

		try {

			String encodepassword = passwordencoder.encode(user.getPw());
			user.setPw(encodepassword);

			boolean join = userservice.join(user);
			if (join == true) {
				result = "success";

			} else {
				result = "fail";
			}

		} catch (Exception e) {
			result = "error";
			e.printStackTrace();
		}

		return new ResponseEntity<String>(result, HttpStatus.OK);
	}

	@GetMapping("/checkid")
	public ResponseEntity<String> checkid(@RequestParam Map map) {

		String result = "success";

		try {

			String findid = userservice.checkid(map);

			if (findid == null) {

			} else {

				result = "fail";

			}

		} catch (Exception e) {
			e.printStackTrace();
			result = "error";

		}

		return new ResponseEntity<String>(result, HttpStatus.OK);
	}

	@GetMapping("/checknickname")
	public ResponseEntity<String> checknickname(@RequestParam Map map) {

		String result = "success";
		
		try {

			String findnickname = userservice.checknickname(map);

			if (findnickname == null) {

				result = "success";

			} else {

				result = "fail";

			}

		} catch (Exception e) {
			e.printStackTrace();
			result = "error";

		}

		return new ResponseEntity<String>(result, HttpStatus.OK);
	}
	@GetMapping("/updateuser")
	public ResponseEntity<Map<String, Object>> updateinfo(HttpServletRequest request) {

		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
	
			try {
				UserVO user = userservice.userInfo(request.getHeader("id"));
				resultMap.put("userInfo", user);
				resultMap.put("message", "SUCCESS");
				status = HttpStatus.ACCEPTED;
			} catch (Exception e) {
				resultMap.put("message", e.getMessage());
				status = HttpStatus.INTERNAL_SERVER_ERROR;
			}
	
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	@PutMapping("/updateuser")
	public ResponseEntity<String> updatename(@RequestBody UserVO user) {

		String result = "";

		
			try {

				if (userservice.updateuser(user) == true) {
					result = "success";
				} else {
					result = "fail";
				}

			} catch (Exception e) {
				e.printStackTrace();
				result = "error";
			}
		

		return new ResponseEntity<String>(result, HttpStatus.OK);
	}

	@PostMapping("/updatepw")
	public ResponseEntity<String> updatepw(@RequestBody Changepw changepw) {

		String result = "error";

		
			try {
				String old = userservice.oldpw(changepw);

				boolean match = passwordencoder.matches(changepw.getOldpw(), old);

				if (match == true) {

					String newpassword = passwordencoder.encode(changepw.getNewpw());

					changepw.setNewpw(newpassword);

					if (userservice.updatepw(changepw) == true) {
						result = "success";

					} else {
						result = "fail2";
					}

				} else {
					result = "fail1";

				}

			} catch (Exception e) {
				e.printStackTrace();
				result = "error";
			}

		

		return new ResponseEntity<String>(result, HttpStatus.OK);
	}

	@GetMapping("/findpw")
	public ResponseEntity<String> findpw(@RequestParam Map map) {
		String result = "";

		try {

			String id = (String) map.get("id");
			String email = (String) map.get("email");

			if (id.equals(userservice.checkid(map))) {

				String newpassword = RandomStringUtils.randomAlphanumeric(10);
				String pass = passwordencoder.encode(newpassword);

				map.put("newpw", pass);
				result = "success";

			} else {
				result = "fail";
			}

		} catch (Exception e) {
			e.printStackTrace();
			result = "error";
		}

		return new ResponseEntity<String>(result, HttpStatus.OK);
	}

	@GetMapping("/profile")
	public ResponseEntity<UserInfo> profile(@RequestParam String id, HttpServletRequest request) throws Exception {
		
		
			UserInfo result = new UserInfo();

			UserVO user = userservice.userInfo(id);

			Integer follower = userservice.countfollower(id);
			Integer following = userservice.countfollowing(id);
			result.setId(user.getId());
			result.setNickname(user.getNickname());
			result.setDes(user.getDes());
			result.setRegdate(user.getRegdate());
			result.setRole(user.getRole());
			result.setFollower(follower);
			result.setFollowing(following);
			result.setImg(user.getImg());
			result.setRankpoint(user.getRankpoint());
			result.setRecipecnt(user.getRecipecnt());

			return new ResponseEntity<UserInfo>(result, HttpStatus.OK);
		

	}

	@GetMapping("/profile/followinglist")
	public ResponseEntity<List<FollowInfo>> followinglist(@RequestParam String id) throws Exception {
		List<FollowInfo> result = userservice.followinglist(id);

		return new ResponseEntity<List<FollowInfo>>(result, HttpStatus.OK);
	}

	@GetMapping("/profile/followerlist")
	public ResponseEntity<List<FollowInfo>> followerlist(@RequestParam String id) throws Exception {
		List<FollowInfo> result = userservice.followerlist(id);
	
		return new ResponseEntity<List<FollowInfo>>(result, HttpStatus.OK);
	}

	@DeleteMapping("/profile/followinglist")
	public ResponseEntity<String> followingdelete(@RequestParam String loginid, String followingid) throws Exception {
		Map map = new HashMap<>();
		map.put("loginid", loginid);
		map.put("followingid", followingid);
		if (userservice.followingdelete(map) == 1) {
			return new ResponseEntity<String>("Success", HttpStatus.OK);
		}
		return new ResponseEntity<String>("Fail", HttpStatus.NO_CONTENT);
	}

	@DeleteMapping("/profile/followerlist")
	public ResponseEntity<String> followerdelete(@RequestParam String loginid, String followerid) throws Exception {
		Map map = new HashMap<>();
		map.put("loginid", loginid);
		map.put("followerid", followerid);
		if (userservice.followerdelete(map) == 1) {
		
			return new ResponseEntity<String>("Success", HttpStatus.OK);
		}
		return new ResponseEntity<String>("Fail", HttpStatus.NO_CONTENT);
	}


	@GetMapping("/getallmember")
	public ResponseEntity<List<UserVO>> getallmember(@RequestParam Map map) {

		String result = "success";
		List<UserVO> list = null;
		try {

			list = userservice.getallmember(map);

		} catch (Exception e) {
			e.printStackTrace();
			result = "error";

		}

		return new ResponseEntity<List<UserVO>>(list, HttpStatus.OK);
	}

	@GetMapping("/follow_already")
	public ResponseEntity<List<String>> follow_already(@RequestParam Map map) {
		
		List<String> result = null;
		try {

			result = userservice.follow_already(map);

		} catch (Exception e) {
			e.printStackTrace();

		}

		return new ResponseEntity<List<String>>(result, HttpStatus.OK);
	}

	@PostMapping("/sendfollowrequest")
	public ResponseEntity<String> sendfollowrequest(@RequestBody Requestfollow rf) {

		String result = "";

		
			try {

				if (userservice.requestfollow(rf) == true) {
					result = "success";
				} else {
					result = "fail";
				}

			} catch (Exception e) {
				e.printStackTrace();
				result = "error";
			}
	

		return new ResponseEntity<String>(result, HttpStatus.OK);
	}

	@PostMapping("/sendfollowdelete")
	public ResponseEntity<String> sendfollowdelete(@RequestBody Requestfollow rf) {

		String result = "";

		if (jwtservice.isUsable(rf.getToken())) {
			try {

				if (userservice.deletefollowing(rf) == true) {
					result = "success";
				} else {
					result = "fail";
				}

			} catch (Exception e) {
				e.printStackTrace();
				result = "error";
			}
		} else {
			result = "error";
		}
		return new ResponseEntity<String>(result, HttpStatus.OK);

	}


	@PostMapping("/snsregister")
	public ResponseEntity<String> snsregister(@RequestBody Snsreg sns) {

		String result = "";

		try {
			if (userservice.kakaoreg(sns) == true) {
				result = "success";
			} else {
				result = "fail";
			}

		} catch (Exception e) {
			e.printStackTrace();
			result = "error";
		}

		return new ResponseEntity<String>(result, HttpStatus.OK);
	}


}