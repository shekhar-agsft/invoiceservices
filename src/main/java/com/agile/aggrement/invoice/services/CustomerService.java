package com.agile.aggrement.invoice.services;

import java.util.List;

import com.agile.aggrement.invoice.model.Customer;
import com.agile.aggrement.invoice.model.CustomerDTO;
import com.agile.aggrement.invoice.model.CustomerInvoiceResponseDTO;
import com.agile.aggrement.invoice.model.CustomerResponseDTO;
import com.agile.aggrement.invoice.model.Invoice;
import com.agile.aggrement.invoice.model.InvoiceProjectDetails;
import com.agile.aggrement.invoice.util.InvoiceException;

public interface CustomerService {

	void save(Customer requestDTO);

	void saveInvoice(Invoice requestDTO, int custId) throws InvoiceException;

	void saveInvoiceProjects(InvoiceProjectDetails requestDTO, int invoiceId);

	CustomerResponseDTO getCustomerDetails(int custId, int invoiceId);

	List<InvoiceProjectDetails> getProjectDetails(int invoiceNumber);

	List<Invoice> getInvoiceDetails(int custId);

	CustomerDTO getAllCustomers();

	Invoice getInvoice(int invoiceId);

	CustomerInvoiceResponseDTO getSingleCustomerDetails(int custId);

}
