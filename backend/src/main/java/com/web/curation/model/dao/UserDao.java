package com.web.curation.model.dao;

import java.util.List;
import java.util.Map;

import com.web.curation.model.Changepw;
import com.web.curation.model.FollowInfo;
import com.web.curation.model.Requestfollow;
import com.web.curation.model.Snsreg;
import com.web.curation.model.UserVO;
import com.web.curation.model.Waiting;

public interface UserDao {
	

	public boolean join(Map map) throws Exception;
	
	public UserVO login(Map map) throws Exception;
	
	public boolean join(UserVO user) throws Exception;
	
	public boolean kakaoreg(Snsreg sns) throws Exception;
	
	public String checkid(Map map) throws Exception;
	
	public String checknickname(Map map) throws Exception;
	
	public boolean updateuser(UserVO user) throws Exception;

	public boolean updatepw(Changepw changepw) throws Exception;
	
	public String oldpw(Changepw changepw) throws Exception;
	
	public boolean findpw(Map map) throws Exception;
	
	public UserVO userInfo(String id) throws Exception;
	
	public Integer countfollowing(String id) throws Exception;
	
	public Integer countfollower(String id) throws Exception;
	
	public Integer countwaiting(String id) throws Exception;
	
	public List<FollowInfo> followinglist(String id) throws Exception;
	
	public List<FollowInfo> followerlist(String id) throws Exception;
	
	public List<FollowInfo> waitlist(String id) throws Exception;
	
	public int followingdelete(Map map) throws Exception;
	
	public int followerdelete(Map map) throws Exception;
	
	public boolean enrollfollower(Waiting wait) throws Exception;
	
	public boolean deletewait(Waiting wait) throws Exception;
	
	public List<UserVO> getallmember(Map map) throws Exception;
	
	public List<String> follow_wait(Map map) throws Exception;
	
	public List<String> follow_already(Map map) throws Exception;
	
	public boolean requestfollow(Requestfollow rf) throws Exception;
	
	public boolean requestdeletefollow(Requestfollow rf) throws Exception;
	
	public boolean deletefollowing(Requestfollow rf) throws Exception;
	
}
