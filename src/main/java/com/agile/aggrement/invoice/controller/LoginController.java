package com.agile.aggrement.invoice.controller;

import java.util.Map;
import java.util.logging.Level;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.agile.aggrement.invoice.jwt.JwtAuthRequest;
import com.agile.aggrement.invoice.model.User;
import com.agile.aggrement.invoice.services.LoginService;
import com.agile.aggrement.invoice.util.HttpStatusCodes;
import com.agile.aggrement.invoice.util.InvoiceUtility;

import lombok.extern.java.Log;

/**
 * @author shekhar
 *
 */

@Log
@CrossOrigin
@RestController
public class LoginController {

	@Value("${jwt.header}")
	private String tokenHeader;

	@Autowired
	LoginService loginService;

	@Autowired
	private InvoiceUtility invoiceUtility;

	@RequestMapping(value = "${api.route.login}", method = RequestMethod.POST)

	public ResponseEntity<?> secureLogin(@Valid @RequestBody JwtAuthRequest authenticationRequest, BindingResult result)
			throws UsernameNotFoundException {

		if (result.hasErrors()) {
			log.log(Level.SEVERE, "Create Authentication Token: Validation Errors");
			return ResponseEntity.ok(invoiceUtility.createResponseEntityDTO(HttpStatusCodes.VALIDATION_ERROR,
					result.getAllErrors().get(0).getDefaultMessage(), null));
		} else {
			try {
				// Return the token
				/*if (loginService.getAuthenticatedUser(authenticationRequest) instanceof Map<?, ?>) {
					@SuppressWarnings("unchecked")
					Map<String, Object> enforceMap = (Map<String, Object>) loginService
							.getAuthenticatedUser(authenticationRequest);

					return ResponseEntity.ok(invoiceUtility.createResponseEntityDTO((Integer) enforceMap.get("status"),
							enforceMap.get("message").toString(), enforceMap.get("body")));
				} else {*/

					return ResponseEntity.ok(invoiceUtility.createResponseEntityDTO(HttpStatusCodes.OK, "login.success",
							loginService.getAuthenticatedUser(authenticationRequest)));
				//}
			} catch (BadCredentialsException e) {
				return ResponseEntity.ok(invoiceUtility.createResponseEntityDTO(HttpStatusCodes.VALIDATION_ERROR,
						"invalid password", null));
			}
		}
	}

	@RequestMapping(value = "logOut", method = RequestMethod.GET)
	public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {

		User loginUser = invoiceUtility.loggedInUser(request.getHeader(this.tokenHeader));
		if (invoiceUtility.loggedInUser() != null) {
			loginService.removeLoginToken(loginUser.getUsername(), request.getHeader(this.tokenHeader));
			return ResponseEntity
					.ok(invoiceUtility.createResponseEntityDTO(HttpStatusCodes.OK, "Logout success", null));
		} else {
			return ResponseEntity
					.ok(invoiceUtility.createResponseEntityDTO(HttpStatusCodes.FAILED, "loginuser failed", null));
		}

	}

}
