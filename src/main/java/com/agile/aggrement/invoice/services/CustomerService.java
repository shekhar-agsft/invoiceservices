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

/**
 * @author Shekhar
 *
 */
public interface CustomerService {

	/**
	 * @param requestDTO
	 * @throws InvoiceException
	 */
	void save(Customer requestDTO) throws InvoiceException;

	/**
	 * @param requestDTO
	 * @throws InvoiceException
	 */
	void update(Customer requestDTO,int custId) throws InvoiceException;

	/**
	 * @param requestDTO
	 * @param custId
	 * @throws InvoiceException
	 */
	void saveInvoice(Invoice requestDTO, int custId) throws InvoiceException;

	/**
	 * @param requestDTO
	 * @param invoiceId
	 * @throws InvoiceException
	 */
	void saveInvoiceProjects(InvoiceProjectDetails requestDTO, int invoiceId) throws InvoiceException;

	/**
	 * @param custId
	 * @param invoiceId
	 * @return
	 * @throws InvoiceException
	 */
	CustomerResponseDTO getCustomerDetails(int custId, int invoiceId) throws InvoiceException;

	/**
	 * @param invoiceNumber
	 * @return
	 * @throws InvoiceException 
	 */
	List<InvoiceProjectDetails> getProjectDetails(int invoiceId) throws InvoiceException;

	/**
	 * @param custId
	 * @return
	 */
	List<ProjectInvoice> getInvoiceDetails(int custId);

	/**
	 * @return
	 * @throws InvoiceException
	 */
	CustomerDTO getAllCustomers() throws InvoiceException;

	/**
	 * @param invoiceId
	 * @return
	 * @throws InvoiceException 
	 */
	Invoice getInvoice(int invoiceId) throws InvoiceException;

	/**
	 * @param custId
	 * @return
	 */
	CustomerInvoiceResponseDTO getSingleCustomerDetails(int custId);

	/**
	 * @param custId
	 * @param invoiceId
	 * @param httpServletResponse
	 * @throws FileNotFoundException
	 * @throws CConvertException
	 * @throws IOException
	 * @throws InvoiceException
	 */
	void export(int custId, int invoiceId, HttpServletResponse httpServletResponse)
			throws FileNotFoundException, CConvertException, IOException, InvoiceException;

}
