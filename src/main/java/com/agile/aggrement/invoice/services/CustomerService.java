package com.agile.aggrement.invoice.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.allcolor.yahp.converter.IHtmlToPdfTransformer.CConvertException;

import com.agile.aggrement.invoice.model.Customer;
import com.agile.aggrement.invoice.model.CustomerDTO;
import com.agile.aggrement.invoice.model.CustomerInvoiceResponseDTO;
import com.agile.aggrement.invoice.model.CustomerResponseDTO;
import com.agile.aggrement.invoice.model.Invoice;
import com.agile.aggrement.invoice.model.InvoiceProjectDetails;
import com.agile.aggrement.invoice.model.ProjectInvoice;
import com.agile.aggrement.invoice.util.InvoiceException;

public interface CustomerService {

	void save(Customer requestDTO) throws InvoiceException;

	void saveInvoice(Invoice requestDTO, int custId) throws InvoiceException;

	void saveInvoiceProjects(InvoiceProjectDetails requestDTO, int invoiceId);

	CustomerResponseDTO getCustomerDetails(int custId, int invoiceId) throws InvoiceException;

	List<InvoiceProjectDetails> getProjectDetails(int invoiceNumber);

	List<ProjectInvoice> getInvoiceDetails(int custId);

	CustomerDTO getAllCustomers();

	Invoice getInvoice(int invoiceId);

	CustomerInvoiceResponseDTO getSingleCustomerDetails(int custId);

	void export(int custId, int invoiceId, HttpServletResponse httpServletResponse)
			throws FileNotFoundException, CConvertException, IOException, InvoiceException;

}
