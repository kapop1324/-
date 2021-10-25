package com.web.curation.model.service;

import java.util.List;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.web.curation.model.Changepw;
import com.web.curation.model.FollowInfo;
import com.web.curation.model.Requestfollow;
import com.web.curation.model.Snsreg;
import com.web.curation.model.UserVO;
import com.web.curation.model.Waiting;
import com.web.curation.model.dao.UserDao;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	SqlSession sqlsession;

	@Override
	public boolean join(UserVO user) throws Exception {
		
		return sqlsession.getMapper(UserDao.class).join(user);
	}

	@Override
	public String checkid(Map map) throws Exception {
		
		return sqlsession.getMapper(UserDao.class).checkid(map);
	}
	
	@Override
	public String checknickname(Map map) throws Exception {
		
		return sqlsession.getMapper(UserDao.class).checknickname(map);
	}

	@Override
	public UserVO login(Map map) throws Exception {
		
		return sqlsession.getMapper(UserDao.class).login(map);
	}

	@Override
	public boolean updateuser(UserVO user) throws Exception {
		
		return sqlsession.getMapper(UserDao.class).updateuser(user);
	}
	

	public boolean updatepw(Changepw changepw) throws Exception {
		
		return sqlsession.getMapper(UserDao.class).updatepw(changepw);
	}

	@Override
	public String oldpw(Changepw changepw) throws Exception {
		
		return sqlsession.getMapper(UserDao.class).oldpw(changepw);
	}
	
	@Autowired
	JavaMailSender mailsender;
	
	@Override
	public void emailsend(String email, String pass) throws Exception {
		
		MimeMessage mimeMessage = mailsender.createMimeMessage();
		MimeMessageHelper message = new MimeMessageHelper(mimeMessage,true,"UTF-8");
		
		String title="임시비밀번호입니다.";
		String from = "rko3507@gmail.com";
		
		message.setFrom(from);
		message.setTo(email);
		message.setSubject(title);
		String content=
				 System.getProperty("line.separator")+ System.getProperty("line.separator")+
				 "발급된 비밀번호는"+pass+"입니다."+ System.getProperty("line.separator")+
				  System.getProperty("line.separator");
		message.setText(content);
		mailsender.send(mimeMessage);
		
	}

	@Override
	public boolean findpw(Map map) throws Exception {
		
		return sqlsession.getMapper(UserDao.class).findpw(map);
	}
	
	@Override
	public UserVO userInfo(String id) throws Exception {

		return sqlsession.getMapper(UserDao.class).userInfo(id);
	}

	@Override
	public Integer countfollower(String id) throws Exception {

		return sqlsession.getMapper(UserDao.class).countfollower(id);
	}

	@Override
	public Integer countfollowing(String id) throws Exception {

		return sqlsession.getMapper(UserDao.class).countfollowing(id);
	}
	
	@Override
	public Integer countwaiting(String id) throws Exception {

		return sqlsession.getMapper(UserDao.class).countwaiting(id);
	}
	
	@Override
	public List<FollowInfo> followinglist(String id) throws Exception {

		return sqlsession.getMapper(UserDao.class).followinglist(id);
	}

	@Override
	public List<FollowInfo> followerlist(String id) throws Exception {

		return sqlsession.getMapper(UserDao.class).followerlist(id);
	}

	@Override
	public int followingdelete(Map map) throws Exception {

		return sqlsession.getMapper(UserDao.class).followingdelete(map);
	}

	@Override
	public int followerdelete(Map map) throws Exception {
		return sqlsession.getMapper(UserDao.class).followerdelete(map);
	}

	@Override
	public List<FollowInfo> waitlist(String id) throws Exception {
		
		return sqlsession.getMapper(UserDao.class).waitlist(id);
	}

	@Override
	public boolean enrollfollower(Waiting wait) throws Exception {
	
		return sqlsession.getMapper(UserDao.class).enrollfollower(wait);
	}

	@Override
	public boolean deletewait(Waiting wait) throws Exception {
		
		return sqlsession.getMapper(UserDao.class).deletewait(wait);
	}

	@Override
	public List<UserVO> getallmember(Map map) throws Exception {
		
		return sqlsession.getMapper(UserDao.class).getallmember(map);
	}

	@Override
	public boolean requestfollow(Requestfollow rf) throws Exception {
		
		return sqlsession.getMapper(UserDao.class).requestfollow(rf);
	}

	@Override
	public List<String> follow_wait(Map map) throws Exception {
		
		return sqlsession.getMapper(UserDao.class).follow_wait(map);
	}

	@Override
	public List<String> follow_already(Map map) throws Exception {
		
		return sqlsession.getMapper(UserDao.class).follow_already(map);
	}

	@Override
	public boolean requestdeletefollow(Requestfollow rf) throws Exception {
		
		return sqlsession.getMapper(UserDao.class).requestdeletefollow(rf);
	}

	@Override
	public boolean deletefollowing(Requestfollow rf) throws Exception {
		
		return sqlsession.getMapper(UserDao.class).deletefollowing(rf);
	}
	
	@Override
	public boolean kakaoreg(Snsreg sns) throws Exception {
		
		return sqlsession.getMapper(UserDao.class).kakaoreg(sns);
	}




	
}
