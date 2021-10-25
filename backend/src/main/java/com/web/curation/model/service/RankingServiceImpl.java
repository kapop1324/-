package com.web.curation.model.service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.curation.model.Rankinglist;
import com.web.curation.model.Rankupdate;
import com.web.curation.model.dao.RankingDao;

@Service
public class RankingServiceImpl implements RankingService {
	
	@Autowired
	SqlSession sqlsession;

	@Override
	public int recipe_cnt(String id) throws Exception {
		
		return sqlsession.getMapper(RankingDao.class).recipe_cnt(id);
	}

	@Override
	public int recipe_like_cnt(String id) throws Exception {

		return sqlsession.getMapper(RankingDao.class).recipe_like_cnt(id);
	}

	@Override
	public int follower_cnt(String id) throws Exception {

		return sqlsession.getMapper(RankingDao.class).follower_cnt(id);
	}

	@Override
	public int achieve_cnt(String id) throws Exception {

		return sqlsession.getMapper(RankingDao.class).achieve_cnt(id);
	}

	@Override
	public int recipe_comment_cnt(String id) throws Exception {

		return sqlsession.getMapper(RankingDao.class).recipe_comment_cnt(id);
	}

	@Override
	public int recipe_comment_like_cnt(String id) throws Exception {

		return sqlsession.getMapper(RankingDao.class).recipe_comment_like_cnt(id);
	}

	@Override
	public boolean updatemyrank(Rankupdate rk) throws Exception {
	
		return sqlsession.getMapper(RankingDao.class).updatemyrank(rk);
	}

	@Override
	public List<Rankinglist> getrankinglist() throws Exception {
		
		return sqlsession.getMapper(RankingDao.class).getrankinglist();
	}

}