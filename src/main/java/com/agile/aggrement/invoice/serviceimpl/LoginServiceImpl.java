package com.agile.aggrement.invoice.serviceimpl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.agile.aggrement.invoice.jwt.JwtAuthRequest;
import com.agile.aggrement.invoice.jwt.JwtTokenUtil;
import com.agile.aggrement.invoice.jwt.JwtUserDetailsServiceImpl;
import com.agile.aggrement.invoice.model.User;
import com.agile.aggrement.invoice.model.UserLoginToken;
import com.agile.aggrement.invoice.repo.LoginUserTokenRepo;
import com.agile.aggrement.invoice.repo.UserRepository;
import com.agile.aggrement.invoice.services.LoginService;
import com.agile.aggrement.invoice.util.InvoiceUtility;

@Component
public class LoginServiceImpl implements LoginService {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUserDetailsServiceImpl userDetailsService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private InvoiceUtility invoiceUtility;

	@Value("${pp.passwordEnforce}")
	private int passWordEnforceThreshold;

	@Autowired
	UserRepository userRepo;

	@Autowired
	LoginUserTokenRepo loginTokenRepo;

	@Override
	public Object getAuthenticatedUser(JwtAuthRequest authenticationRequest) {
		/*
		 * if
		 * (!invoiceUtility.isValidEmailAddress(authenticationRequest.getEmail()
		 * )) { throw new WazooException(201, "Invalid email"); } else {
		 */
		final Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail().toLowerCase(),
						authenticationRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);

		final User user = userDetailsService.getUserByEmail(authenticationRequest.getEmail().toLowerCase());
		final String token = jwtTokenUtil.generateToken(user);

		UserLoginToken userLoginToken = new UserLoginToken();
		userLoginToken.setToken(token);
		userLoginToken.setUserEmailId(user.getUsername());
		loginTokenRepo.save(userLoginToken);
		return invoiceUtility.successfullyLoggedIn(user, token);

	}

	@Override
	public void removeLoginToken(String userEmail, String userToken) {

		UserLoginToken userLoginToken = loginTokenRepo.findUserToken(userEmail, userToken);
		if (userLoginToken != null) {
			loginTokenRepo.delete(userLoginToken);
		}

	}

}
