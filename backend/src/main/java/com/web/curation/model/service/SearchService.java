package com.web.curation.model.service;

import java.util.List;

import com.web.curation.model.UserVO;

public interface SearchService {
	public List<UserVO> searchByNickname(String nickname) throws Exception;
}
