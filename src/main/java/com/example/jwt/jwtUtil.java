package com.example.jwt;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.bean.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class jwtUtil {

	@Value("${app.jwtSecret}")
	private String secretKey;

	@Value("${app.jwtExpiry}")
	private int jwtExpiry;

	public String generateToken(User user) {
		Map<String, Object> claims = new HashMap<>();
		return this.createToken(claims, user.getUsername());
	}

	private String createToken(Map<String, Object> claims, String username) {
		return Jwts.builder().setClaims(claims).setSubject(username).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + jwtExpiry))
				.signWith(SignatureAlgorithm.HS256, secretKey).compact();
	}

	public String getUserName(String token) {
		return this.extractclaims(token, Claims::getSubject);

	}

	private <T> T extractclaims(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaiims(token);
		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaiims(String token) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
	}

	public Date extractExpiration(String token) {
		return this.extractclaims(token, Claims::getExpiration);
	}

	private boolean isTokenExpired(String token) {
		return this.extractExpiration(token).before(new Date());
	}

	public boolean validateToken(String token, UserDetails userDetails) {
		return (this.getUserName(token).equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
}
