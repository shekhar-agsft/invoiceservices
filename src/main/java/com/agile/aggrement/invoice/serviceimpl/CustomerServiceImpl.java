package com.agile.aggrement.invoice.serviceimpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.agile.aggrement.invoice.model.Customer;
import com.agile.aggrement.invoice.model.CustomerDTO;
import com.agile.aggrement.invoice.model.CustomerInvoiceResponseDTO;
import com.agile.aggrement.invoice.model.CustomerResponseDTO;
import com.agile.aggrement.invoice.model.Invoice;
import com.agile.aggrement.invoice.model.InvoiceProjectDetails;
import com.agile.aggrement.invoice.model.ProjectInvoice;
import com.agile.aggrement.invoice.repo.CustomerRepository;
import com.agile.aggrement.invoice.repo.InvoiceRepository;
import com.agile.aggrement.invoice.repo.ProjectRepository;
import com.agile.aggrement.invoice.services.CustomerService;
import com.agile.aggrement.invoice.util.InvoiceException;

import lombok.extern.java.Log;

@Component
@Log
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	InvoiceRepository invoiceRepository;

	@Autowired
	ProjectRepository projectRepository;

	@Override
	public void save(Customer requestDTO) {

		customerRepository.save(requestDTO);

	}

	@Override
	public void saveInvoice(Invoice requestDTO, int custId) throws InvoiceException {

		Customer customer = customerRepository.findOne(custId);
		requestDTO.setCustId(customer);
		int number = 1801;
		int invoiceNumber = 0;
		List<Invoice> invoice = (List<Invoice>) invoiceRepository.findAll();
		Invoice invoiceObj = null;
		if (!invoice.isEmpty()) {
			int size = invoice.size();
			invoiceObj = invoice.get(size - 1);
			if(invoiceObj.getCustId()==customer){
				throw new InvoiceException(500,"Please add the customer first");
			}
		}
		if (invoiceObj != null) {
			invoiceNumber = invoiceObj.getInvoiceNumber();
		}
		if (invoiceNumber != 0) {
			requestDTO.setInvoiceNumber(invoiceNumber + 1);
		} else {
			requestDTO.setInvoiceNumber(number);
		}

		invoiceRepository.save(requestDTO);
	}

	@Override
	public void saveInvoiceProjects(InvoiceProjectDetails requestDTO, int invoiceId) {

		Invoice invoice = invoiceRepository.findOne(invoiceId);
		
		
		requestDTO.setInvoiceId(invoice);
		projectRepository.save(requestDTO);

	}

	@Override
	public CustomerResponseDTO getCustomerDetails(int custId, int invoiceId) {

		CustomerResponseDTO customerResponseDTO = new CustomerResponseDTO();

		Customer customer = customerRepository.findOne(custId);

		Invoice invoice = invoiceRepository.findByInvoiceNumber(invoiceId);

		
		customerResponseDTO.setName(customer.getName());
		customerResponseDTO.setAddress(customer.getAddress());
		customerResponseDTO.setPeriod(new Date());
		customerResponseDTO.setPoagreement(customer.getPoagreement());
		
		customerResponseDTO.setInvoiceDate(invoice.getInvoiceDate());
		customerResponseDTO.setInvoiceDue(invoice.getInvoiceDue());
		customerResponseDTO.setInvoiceNumber(invoice.getInvoiceNumber());
		customerResponseDTO.setAmount(invoice.getAmount());
		
		customerResponseDTO.setInvoiceNumber(invoice.getInvoiceNumber());
		
		
		
		customerResponseDTO.setInvoiceProjectDetails( projectRepository.findByInvoiceId(invoice));
		
		
		

		return customerResponseDTO;
	}

	@Override
	public List<InvoiceProjectDetails> getProjectDetails(int invoiceNumber) {

		if (invoiceNumber != 0) {
			Invoice invoice = invoiceRepository.findByInvoiceNumber(invoiceNumber);
			List<InvoiceProjectDetails> invoiceProjectDetails = projectRepository.findByInvoiceId(invoice);
			return invoiceProjectDetails;
		}

		return null;
	}

	@Override
	public List<Invoice> getInvoiceDetails(int custId) {
		Customer customer = customerRepository.findOne(custId);

		if (customer != null) {
			return invoiceRepository.findByCustId(customer);
		} else {
			return null;
		}

	}

	@Override
	public CustomerDTO getAllCustomers() {

		CustomerDTO customerDTO = new CustomerDTO();
		List<Customer> customers = (List<Customer>) customerRepository.findAll();
		customerDTO.setCustomers(customers);
		return customerDTO;
	}

	@Override
	public Invoice getInvoice(int invoiceId) {

		return invoiceRepository.findByInvoiceNumber(invoiceId);

	}

	@Override
	public CustomerInvoiceResponseDTO getSingleCustomerDetails(int custId) {

		Customer customer = customerRepository.findOne(custId);

		CustomerInvoiceResponseDTO customerInvoiceResponseDTO = new CustomerInvoiceResponseDTO();

		customerInvoiceResponseDTO.setCustomerName(customer.getName());
		customerInvoiceResponseDTO.setCustAddress(customer.getAddress());
		customerInvoiceResponseDTO.setPeriod(new Date());
		customerInvoiceResponseDTO.setPoagreement(customer.getPoagreement());
		// invoice
		int number = 1801;
		int invoiceNumber = 0;
		List<Invoice> invoice = (List<Invoice>) invoiceRepository.findAll();
		Invoice invoiceObj = null;
		if (!invoice.isEmpty()) {
			int size = invoice.size();
			invoiceObj = invoice.get(size - 1);
		}
		if (invoiceObj != null) {
			invoiceNumber = invoiceObj.getInvoiceNumber();
		}
		if (invoiceNumber != 0) {
			customerInvoiceResponseDTO.setInvoiceNumber(invoiceNumber + 1);
		} else {
			customerInvoiceResponseDTO.setInvoiceNumber(number);
		}

		return customerInvoiceResponseDTO;
	}

}
