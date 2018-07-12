package com.agile.aggrement.invoice.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.agile.aggrement.invoice.model.UserLoginToken;
import com.agile.aggrement.invoice.repo.LoginUserTokenRepo;



/**
 * @author 
 *
 */
public class JwtAuthFilter extends OncePerRequestFilter {

	@Value("${jwt.header}")
	private String tokenHeader;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	LoginUserTokenRepo loginTokenRepo;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		String authToken = request.getHeader(this.tokenHeader);
		String username = jwtTokenUtil.getUsernameFromToken(authToken);
		
		System.out.println("Filter "+username);
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
			
			UserLoginToken loginUserToken=loginTokenRepo.findUserToken(username, authToken);
			
			System.out.println("Filter "+loginUserToken);
			
			if (jwtTokenUtil.validateToken(authToken, userDetails)&&(loginUserToken!=null)) {
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				logger.info("authenticated user " + username + ", setting security context");
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}

		chain.doFilter(request, response);
	}

}
