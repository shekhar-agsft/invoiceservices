
package com.agile.aggrement.invoice.jwt;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.agile.aggrement.invoice.util.HttpStatusCodes;
import com.agile.aggrement.invoice.util.InvoiceUtility;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author
 *
 */
@Component
public class JwtAuthEntry implements AuthenticationEntryPoint, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	InvoiceUtility InvoiceUtility;

	@Override
	public void commence(HttpServletRequest arg0, HttpServletResponse arg1, AuthenticationException arg2)
			throws IOException, ServletException {
		arg1.getWriter().print(new ObjectMapper().writeValueAsString(InvoiceUtility
				.createResponseEntityDTO(HttpStatusCodes.UNAUTHORIZED, "Unauthorzied Access to API", null)));
		arg1.setContentType("application/json");
		arg1.setCharacterEncoding("UTF-8");
		arg1.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

	}

}
