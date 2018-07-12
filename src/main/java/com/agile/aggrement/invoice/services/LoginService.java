package com.agile.aggrement.invoice.services;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.agile.aggrement.invoice.jwt.JwtAuthRequest;

@Service
public interface LoginService {

	public Object getAuthenticatedUser(JwtAuthRequest authenticationRequest) throws UsernameNotFoundException;
	
	public void removeLoginToken(String userEmail,String userToken);
	
}
