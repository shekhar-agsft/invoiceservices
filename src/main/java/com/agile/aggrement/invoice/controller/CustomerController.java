package com.agile.aggrement.invoice.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.allcolor.yahp.converter.IHtmlToPdfTransformer.CConvertException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.agile.aggrement.invoice.model.Customer;
import com.agile.aggrement.invoice.model.CustomerDTO;
import com.agile.aggrement.invoice.model.CustomerInvoiceResponseDTO;
import com.agile.aggrement.invoice.model.CustomerResponseDTO;
import com.agile.aggrement.invoice.model.Invoice;
import com.agile.aggrement.invoice.model.InvoiceProjectDetails;
import com.agile.aggrement.invoice.model.ProjectInvoice;
import com.agile.aggrement.invoice.services.CustomerService;
import com.agile.aggrement.invoice.util.HttpStatusCodes;
import com.agile.aggrement.invoice.util.InvoiceException;
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
	public ResponseEntity<?> saveCustomer(
			 @Valid @RequestBody Customer requestDTO,
			BindingResult bindingResult) throws InvoiceException {
		if (bindingResult.hasErrors()) {
			return ResponseEntity.ok(invoiceUtility.createResponseEntityDTO(HttpStatusCodes.VALIDATION_ERROR,
					bindingResult.getAllErrors().get(0).getDefaultMessage(), null));
		} else {
			log.log(Level.FINEST, "Inside save customer request controller");
			customerService.save(requestDTO);

			return ResponseEntity.ok(invoiceUtility.createResponseEntityDTO(HttpStatusCodes.OK,
					"Customer added successfully", null));
		}
	}
	
	
	@RequestMapping(value = "${api.route.invoice.add}/{custId}", method = RequestMethod.POST)	
	public ResponseEntity<?> saveInvoice(@PathVariable int custId,
			 @Valid @RequestBody Invoice requestDTO,
			BindingResult bindingResult) throws InvoiceException {
		if (bindingResult.hasErrors()) {
			return ResponseEntity.ok(invoiceUtility.createResponseEntityDTO(HttpStatusCodes.VALIDATION_ERROR,
					bindingResult.getAllErrors().get(0).getDefaultMessage(), null));
		} else {
			log.log(Level.FINEST, "Inside save invoice request controller");
			customerService.saveInvoice(requestDTO,custId);

			return ResponseEntity.ok(invoiceUtility.createResponseEntityDTO(HttpStatusCodes.OK,
					"Invoice added successfully", null));
		}
	}
	
	
	@RequestMapping(value = "${api.route.projects.add}/{invoiceId}", method = RequestMethod.POST)	
	public ResponseEntity<?> saveProject(@PathVariable int invoiceId,
			 @Valid @RequestBody InvoiceProjectDetails requestDTO,
			BindingResult bindingResult) throws InvoiceException {
		if (bindingResult.hasErrors()) {
			return ResponseEntity.ok(invoiceUtility.createResponseEntityDTO(HttpStatusCodes.VALIDATION_ERROR,
					bindingResult.getAllErrors().get(0).getDefaultMessage(), null));
		} else {
			log.log(Level.FINEST, "Inside general request controller");
			customerService.saveInvoiceProjects(requestDTO,invoiceId);

			return ResponseEntity.ok(invoiceUtility.createResponseEntityDTO(HttpStatusCodes.OK,
					"Projects added successfully", null));
		}
	}
	
	
	@RequestMapping(value = "${api.route.customer.fetch}/{custId}/{invoiceId}", method = RequestMethod.GET)	
	public ResponseEntity<?> getCustomerDetails(@PathVariable int custId,@PathVariable int invoiceId) throws InvoiceException {
		
			log.log(Level.FINEST, "Inside general request controller");
			CustomerResponseDTO customer = customerService.getCustomerDetails(custId,invoiceId);

			return ResponseEntity.ok(invoiceUtility.createResponseEntityDTO(HttpStatusCodes.OK,
					"Customers fetched  successfully", customer));
		}
	
	
	@RequestMapping(value = "${api.route.customer.fetch.one}/{custId}", method = RequestMethod.GET)	
	public ResponseEntity<?> getSingleCustomerDetails(@PathVariable int custId) {
		
			log.log(Level.FINEST, "Inside general request controller");
			CustomerInvoiceResponseDTO customer = customerService.getSingleCustomerDetails(custId);

			return ResponseEntity.ok(invoiceUtility.createResponseEntityDTO(HttpStatusCodes.OK,
					"Customers fetched  successfully", customer));
		}
	
	@RequestMapping(value = "${api.route.customer.all.fetch}", method = RequestMethod.GET)	
	public ResponseEntity<?> getAllCustomers() throws InvoiceException {
		
			log.log(Level.FINEST, "Inside general request controller");
			CustomerDTO customer = customerService.getAllCustomers();

			return ResponseEntity.ok(invoiceUtility.createResponseEntityDTO(HttpStatusCodes.OK,
					"Customers fetched  successfully", customer));
		}
	
	@RequestMapping(value = "${api.route.project.fetch}/{invoiceId}", method = RequestMethod.GET)	
	public ResponseEntity<?> getProjectDetails(@PathVariable int invoiceId) {
		
			log.log(Level.FINEST, "Inside general request controller");
			List<InvoiceProjectDetails> invoiceProjectDetails = customerService.getProjectDetails(invoiceId);

			return ResponseEntity.ok(invoiceUtility.createResponseEntityDTO(HttpStatusCodes.OK,
					"Projects fetched successfully", invoiceProjectDetails));
		}
	
	@RequestMapping(value = "${api.route.invoice.one.fetch}/{invoiceId}", method = RequestMethod.GET)	
	public ResponseEntity<?> getInvoice(@PathVariable int invoiceId) {
		
			log.log(Level.FINEST, "Inside general request controller");
			Invoice invoice = customerService.getInvoice(invoiceId);

			return ResponseEntity.ok(invoiceUtility.createResponseEntityDTO(HttpStatusCodes.OK,
					"Invoice fetched successfully", invoice));
		}
	
	@RequestMapping(value = "${api.route.invoice.fetch}/{custId}", method = RequestMethod.GET)	
	public ResponseEntity<?> getInvoiceDetails(@PathVariable int custId) {
		
			log.log(Level.FINEST, "Inside general request controller");
			List<ProjectInvoice> invoices = customerService.getInvoiceDetails(custId);

			return ResponseEntity.ok(invoiceUtility.createResponseEntityDTO(HttpStatusCodes.OK,
					"Invoice fetched successfully", invoices));
		}
	
	@RequestMapping(value = "${api.route.invoice.export}/{custId}/{invoiceId}", method = RequestMethod.GET)	
	public ResponseEntity<?> export(@PathVariable int custId, @PathVariable int invoiceId,
			HttpServletResponse httpServletResponse) throws CConvertException, IOException, InvoiceException {
		
			log.log(Level.FINEST, "Inside general request controller");
	customerService.export(custId,invoiceId, httpServletResponse);

			return ResponseEntity.ok(invoiceUtility.createResponseEntityDTO(HttpStatusCodes.OK,
					"Invoice fetched successfully", null));
		}
	
}
