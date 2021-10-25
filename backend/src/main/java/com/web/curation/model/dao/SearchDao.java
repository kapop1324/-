package com.web.curation.model.dao;

import java.util.List;

import com.web.curation.model.UserVO;

public interface SearchDao {
	
	public List<UserVO> searchByNickname(String nickname) throws Exception;
	
}
