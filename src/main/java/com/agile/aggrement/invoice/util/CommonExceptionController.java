package com.agile.aggrement.invoice.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.java.Log;

/**
 * The GenericException class used to handle exception
 * 
 * @author agsuser
 *
 */
@ControllerAdvice
@Log
public class CommonExceptionController extends ResponseEntityExceptionHandler{

	@Autowired
	private InvoiceUtility waasAppUtils;

	@ExceptionHandler(InvoiceException.class)
	@ResponseBody
	public ResponseEntity<?> wazooExeception(InvoiceException e) {
		// handles generic exception and returns "service failed" message
        log.info("Inside global wazzo error handle :"+e.getMessage());
		return ResponseEntity.ok(waasAppUtils.createResponseEntityDTO(e.getCode(), e.getMessage(), null));

	}
	/**
	 * 
	 * @param request
	 *            Http Request
	 * @param e
	 *            The Exception
	 * @return The Http Response with failure message and code
	 */
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public ResponseEntity<?> genericExeception(HttpServletRequest request, Exception e) {
		// handles generic exception and returns "service failed" message
		log.info("Inside global exception error handle");
		e.printStackTrace();
		return ResponseEntity
				.ok(waasAppUtils.createResponseEntityDTO(HttpStatusCodes.INTERNAL_SERVER_ERROR, e.getMessage(), null));

	}

	/**
	 * 
	 * @param request
	 * @param e
	 *            AccessDeniedException exception
	 * @return The Http Response with failure(access denied) message
	 */
	@ExceptionHandler(AccessDeniedException.class)
	@ResponseBody
	public ResponseEntity<?> genericExeception(HttpServletRequest request, AccessDeniedException e) {
		// handles AccessDeniedException exception and returns "access denied " message
		return ResponseEntity
				.ok(waasAppUtils.createResponseEntityDTO(HttpStatusCodes.INTERNAL_SERVER_ERROR, e.getMessage(), null));
	}

	

}
