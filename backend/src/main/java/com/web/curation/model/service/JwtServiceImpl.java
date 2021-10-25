package com.web.curation.model.service;

import java.io.UnsupportedEncodingException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.web.curation.error.UnauthorizedException;

import io.jsonwebtoken.*;

@Component
public class JwtServiceImpl implements JwtService {

	private static final String TK = "ssafySecret";
	private static final int EXPIRE_MINUTES = 60;

	@Override
	public <T> String create(String key, T data, String subject) {
		String jwt = Jwts.builder().setHeaderParam("typ", "JWT").setHeaderParam("regDate", System.currentTimeMillis())
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * EXPIRE_MINUTES))
				.setSubject(subject).claim(key, data).signWith(SignatureAlgorithm.HS256, this.generateKey()).compact();
		return jwt;
	}

	private byte[] generateKey() {
		byte[] key = null;
		try {
			key = TK.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {

				e.printStackTrace();

		}

		return key;
	}

//	전달 받은 토큰이 제대로 생성된것인지 확인 하고 문제가 있다면 UnauthorizedException을 발생.
	@Override
	public boolean isUsable(String jwt) {
		try {
			Jws<Claims> claims = Jwts.parser().setSigningKey(this.generateKey()).parseClaimsJws(jwt);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public Map<String, Object> get(String key) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
				.getRequest();
		String jwt = request.getHeader("access-token");
		Jws<Claims> claims = null;
		try {
			claims = Jwts.parser().setSigningKey(TK.getBytes("UTF-8")).parseClaimsJws(jwt);
		} catch (Exception e) {

			throw new UnauthorizedException();

		}
		Map<String, Object> value = claims.getBody();

		return value;
	}


}
