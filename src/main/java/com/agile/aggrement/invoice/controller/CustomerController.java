package com.agile.aggrement.invoice.controller;

import java.util.logging.Level;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.agile.aggrement.invoice.model.Customer;
import com.agile.aggrement.invoice.services.CustomerService;
import com.agile.aggrement.invoice.util.HttpStatusCodes;
import com.agile.aggrement.invoice.util.InvoiceUtility;

import lombok.extern.java.Log;

@Log
@CrossOrigin
@RestController
public class CustomerController {

	@Autowired
	private InvoiceUtility invoiceUtility;
	
	@Autowired
	CustomerService customerService;
	
	@RequestMapping(value = "${api.route.customer.add}", method = RequestMethod.POST)	
	public ResponseEntity<?> enquiryRequest(
			 @Valid @RequestBody Customer requestDTO,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResponseEntity.ok(invoiceUtility.createResponseEntityDTO(HttpStatusCodes.VALIDATION_ERROR,
					bindingResult.getAllErrors().get(0).getDefaultMessage(), null));
		} else {
			log.log(Level.FINEST, "Inside general request controller");
			customerService.save(requestDTO);

			return ResponseEntity.ok(invoiceUtility.createResponseEntityDTO(HttpStatusCodes.OK,
					"Customer added successfully", null));
		}
	}
}
