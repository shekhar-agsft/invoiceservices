package com.agile.aggrement.invoice.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.validation.ObjectError;

import com.agile.aggrement.invoice.jwt.JwtTokenUtil;
import com.agile.aggrement.invoice.model.LoginResponseDTO;
import com.agile.aggrement.invoice.model.User;
import com.agile.aggrement.invoice.repo.UserRepository;

@Component
public class InvoiceUtility {

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	UserRepository userRepo;

	public ResponseEntityDTO createResponseEntityDTO(HttpStatusCodes httpStatusCodes, String message, Object body) {
		return ResponseEntityDTO.response().withResponseCode(httpStatusCodes).withResponseMessage(message)
				.withResponseBody(body).build();
	}

	public ResponseEntityDTO createResponseEntityDTO(HttpStatusCodes validationError, List<ObjectError> allErrors,
			Object body) {
		return ResponseEntityDTO.response().withResponseCode(validationError).withResponseBody(allErrors)
				.withResponseBody(body).build();
	}

	public ResponseEntityDTO createResponseEntityDTO(int code, String message, Object body) {
		return ResponseEntityDTO.response().withResponseCode(code).withResponseMessage(message).withResponseBody(body)
				.build();
	}

	public ResponseEntityDTO createResponseEntityDTO(HttpStatusCodes httpStatusCodes, String message, Object body,
			String name) {
		return ResponseEntityDTO.response().withResponseCode(httpStatusCodes).withResponseMessage(message)
				.withResponseBody(body).withResponseBody(name).build();
	}

	public LoginResponseDTO successfullyLoggedIn(User user, String token) {

		/*LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
		loginResponseDTO.setToken(token);
		User userobject = new User();
		userobject.setUsername(user.getUsername());
		userobject.setUserRoles(user.get);
		loginResponseDTO.setUserProfile(user);*/
		return new LoginResponseDTO(token, user);
	}

	public User loggedInUser(String headerToken) {
		String email = jwtTokenUtil.getUsernameFromToken(headerToken);
		return userRepo.findUserByUsername(email);
	}

	public User loggedInUser() {

		return userRepo.findUserByUsername(findLoggedInUsername());
	}
	
	public String findLoggedInUsername() {
		Object userDetails = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (userDetails instanceof UserDetails) {
			return ((UserDetails) userDetails).getUsername();
		}
		return null;
	}

}
