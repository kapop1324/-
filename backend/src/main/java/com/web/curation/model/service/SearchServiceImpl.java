package com.web.curation.model.service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.curation.model.UserVO;
import com.web.curation.model.dao.SearchDao;

@Service
public class SearchServiceImpl implements SearchService {

	@Autowired
	SqlSession sqlsession;
	
	@Override
	public List<UserVO> searchByNickname(String nickname) throws Exception {
		return (List<UserVO>) sqlsession.getMapper(SearchDao.class).searchByNickname(nickname);
	}

}
