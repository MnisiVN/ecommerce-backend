package com.ntsako.ecommerce.config;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.ntsako.ecommerce.constant.JwtConstant;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtProvider {
	
	SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());

	public String generateToken(Authentication auth) {
		
		String jwt = Jwts.builder()
				.issuedAt(new Date())
				.expiration(new Date(new Date().getTime()+846000000))
				.claim("email", auth.getName())
				.signWith(key).compact();
		
		return jwt;		
	}
	
	public String getEmailFromToken(String jwt) {
		
		Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(jwt).getPayload();
		
		String email = String.valueOf(claims.get("email"));
		
		return email;
	}
}
