package com.agile.aggrement.invoice.serviceimpl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.allcolor.yahp.converter.CYaHPConverter;
import org.allcolor.yahp.converter.IHtmlToPdfTransformer;
import org.allcolor.yahp.converter.IHtmlToPdfTransformer.CConvertException;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.agile.aggrement.invoice.model.AccountDetails;
import com.agile.aggrement.invoice.model.Customer;
import com.agile.aggrement.invoice.model.CustomerDTO;
import com.agile.aggrement.invoice.model.CustomerInvoiceResponseDTO;
import com.agile.aggrement.invoice.model.CustomerResponseDTO;
import com.agile.aggrement.invoice.model.Invoice;
import com.agile.aggrement.invoice.model.InvoiceProjectDetails;
import com.agile.aggrement.invoice.model.ProjectInvoice;
import com.agile.aggrement.invoice.repo.AccountRepo;
import com.agile.aggrement.invoice.repo.CustomerRepository;
import com.agile.aggrement.invoice.repo.InvoiceRepository;
import com.agile.aggrement.invoice.repo.ProjectRepository;
import com.agile.aggrement.invoice.services.CustomerService;
import com.agile.aggrement.invoice.util.InvoiceException;
import com.agile.aggrement.invoice.util.InvoiceProjectResponseDTO;
import com.agile.aggrement.invoice.util.VelocityUtility;

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

	@Autowired
	AccountRepo accountRepo;

	@Autowired
	VelocityUtility velocityUtility;

	@Autowired
	private VelocityEngine velocityEngine;

	@Value("${defaultPdfPath}")
	String defaultPdfPath;

	@Override
	public void save(Customer requestDTO) throws InvoiceException {
		if (requestDTO != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			String day = new Integer(cal.get(Calendar.DAY_OF_MONTH)).toString();
			if (cal.get(Calendar.DAY_OF_MONTH) < 9) {
				day = "0" + day;
			}
			String month = new Integer(cal.get(Calendar.MONTH) + 1).toString();
			if ((cal.get(Calendar.MONTH) + 1) < 9) {
				month = "0" + month;
			}
			int year = cal.get(Calendar.YEAR);
			requestDTO.setPeriod(month + "" + year);
			customerRepository.save(requestDTO);
		} else {
			throw new InvoiceException(500, "No input data");
		}

	}

	@Override
	public void saveInvoice(Invoice requestDTO, int custId) throws InvoiceException {

		Customer customer = customerRepository.findOne(custId);
		requestDTO.setCustId(customer);

		int invoiceSeries = customer.getInvoiceSeries();
		int invoiceNumber = 0;
		List<Invoice> invoice = (List<Invoice>) invoiceRepository.findAll();
		Invoice invoiceObj = null;
		if (!invoice.isEmpty()) {
			int size = invoice.size();
			invoiceObj = invoice.get(size - 1);
			if (invoiceObj.getCustId() == customer) {
				throw new InvoiceException(500, "Please add the customer first");
			}
		}
		if (invoiceObj != null) {
			invoiceNumber = invoiceObj.getInvoiceNumber();
		}
		if (invoiceNumber != 0) {
			requestDTO.setInvoiceNumber(invoiceSeries + 1);
		} else {
			requestDTO.setInvoiceNumber(invoiceSeries);
		}

		invoiceRepository.save(requestDTO);
	}

	@Override
	public void saveInvoiceProjects(InvoiceProjectDetails requestDTO, int invoiceId) throws InvoiceException {

		Invoice invoice = invoiceRepository.findOne(invoiceId);
		if (invoice != null) {
			requestDTO.setInvoiceId(invoice);
			if (invoice.getAmount() < requestDTO.getAmountBilled()) {
				throw new InvoiceException(500, "Amount for single resource should not exceed total invoice amount");
			}
			List<InvoiceProjectDetails> invoiceProjectDetails = projectRepository.findByInvoiceId(invoice);
			double totalInvoice = 0.0;
			if (!invoiceProjectDetails.isEmpty()) {
				for (InvoiceProjectDetails invoiceProjectDetail : invoiceProjectDetails) {
					totalInvoice = totalInvoice + invoiceProjectDetail.getAmountBilled();

				}
			}

			double newAmount = requestDTO.getAmountBilled() + totalInvoice;

			if (newAmount > invoice.getAmount()) {
				throw new InvoiceException(500, "Total invoice amount exceeded");
			}

			if (totalInvoice > invoice.getAmount()) {
				throw new InvoiceException(500, "Total invoice amount exceeded");
			}
			if (totalInvoice == invoice.getAmount()) {
				throw new InvoiceException(500, "Total invoice amount exceeded");
			}
			projectRepository.save(requestDTO);
		} else {
			throw new InvoiceException(500, "Please add the invoice first");
		}

	}

	@Override
	public CustomerResponseDTO getCustomerDetails(int custId, int invoiceId) throws InvoiceException {

		CustomerResponseDTO customerResponseDTO = new CustomerResponseDTO();

		Customer customer = customerRepository.findOne(custId);

		Invoice invoice = invoiceRepository.findOne(invoiceId);

		customerResponseDTO.setName(customer.getName());
		customerResponseDTO.setAddress(customer.getAddress());
		customerResponseDTO.setPeriod(new Date());
		customerResponseDTO.setPoagreement(customer.getPoagreement());
		if (invoice != null) {
			customerResponseDTO.setInvoiceDate(invoice.getInvoiceDate());
			customerResponseDTO.setInvoiceDue(invoice.getInvoiceDue());
			customerResponseDTO.setInvoiceNumber(invoice.getInvoiceNumber());
			customerResponseDTO.setAmount(invoice.getAmount());

			customerResponseDTO.setInvoiceNumber(invoice.getInvoiceNumber());
		}
		customerResponseDTO.setInvoiceProjectDetails(projectRepository.findByInvoiceId(invoice));

		List<AccountDetails> accountDetails = (List<AccountDetails>) accountRepo.findAll();
		customerResponseDTO.setAccountDetails(accountDetails.get(0));

		double totalInvoice = 0.0;
		List<InvoiceProjectDetails> invoiceProjectDetails = customerResponseDTO.getInvoiceProjectDetails();
		if (!invoiceProjectDetails.isEmpty()) {
			for (InvoiceProjectDetails invoiceProjectDetail : invoiceProjectDetails) {
				totalInvoice = totalInvoice + invoiceProjectDetail.getAmountBilled();

			}
		}
		/*
		 * if (customerResponseDTO.getAmount() != totalInvoice) { throw new
		 * InvoiceException(500, "Invoice amount does not match"); }
		 */
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
	public List<ProjectInvoice> getInvoiceDetails(int custId) {
		Customer customer = customerRepository.findOne(custId);
		List<InvoiceProjectDetails> invoiceProjectDetails = null;
		if (customer != null) {
			List<ProjectInvoice> projectInvoice = new ArrayList<>();
			List<Invoice> invoice = invoiceRepository.findByCustId(customer);

			for (Invoice invobj : invoice) {
				ProjectInvoice projectInvoi = new ProjectInvoice();
				invoiceProjectDetails = projectRepository.findByInvoiceId(invobj);
				projectInvoi.setInvoice(invobj);
				projectInvoi.setInvoiceProjectDetails(invoiceProjectDetails);
				projectInvoice.add(projectInvoi);
			}

			return projectInvoice;

		} else {
			return null;
		}

	}

	@Override
	public CustomerDTO getAllCustomers() throws InvoiceException {

		CustomerDTO customerDTO = new CustomerDTO();
		List<Customer> customers = (List<Customer>) customerRepository.findAll();
		if (!customers.isEmpty()) {
			customerDTO.setCustomers(customers);
		} else {
			throw new InvoiceException(500, "Please add the customer First");
		}
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

		int invoiceSeries = customer.getInvoiceSeries();

		int invoiceNumber = 0;
		List<Invoice> invoice = (List<Invoice>) invoiceRepository.findAll();
		Invoice invoiceObj = null;
		if (!invoice.isEmpty()) {
			int size = invoice.size();
			invoiceObj = invoice.get(size - 1);
		}
		if (invoiceObj != null) {
			if (invoiceObj.getCustId() == customer) {
				invoiceNumber = invoiceObj.getInvoiceNumber();
			} else {
				invoiceNumber = invoiceSeries + 1;
			}
		}
		if (invoiceNumber != 0) {
			customerInvoiceResponseDTO.setInvoiceNumber(invoiceSeries + 1);
		} else {
			customerInvoiceResponseDTO.setInvoiceNumber(invoiceSeries);
		}

		return customerInvoiceResponseDTO;
	}

	@Override
	public void export(int custId, int invoiceId, HttpServletResponse httpServletResponse)
			throws CConvertException, IOException, InvoiceException {

		File pdfFile = generateTemplate(getCustomerDetails(custId, invoiceId));

		httpServletResponse.setContentType("application/pdf");
		httpServletResponse.setHeader("Content-Disposition", "filename=" + pdfFile.getName());

		FileInputStream inputStream = new FileInputStream(new File(pdfFile.getPath()));
		BufferedInputStream inStrem = new BufferedInputStream(new FileInputStream(pdfFile));
		BufferedOutputStream outStream = new BufferedOutputStream(httpServletResponse.getOutputStream());

		byte[] buffer = new byte[2048];
		int bytesRead = 0;
		while ((bytesRead = inStrem.read(buffer)) != -1) {
			outStream.write(buffer, 0, bytesRead);
		}
		outStream.flush();
		inStrem.close();

	}

	public String getMonth(Date userDate) {

		String[] monthName = { "January", "February", "March", "April", "May", "June", "July", "August", "September",
				"October", "November", "December" };

		Calendar cal = Calendar.getInstance();
		cal.setTime(userDate);
		String day = new Integer(cal.get(Calendar.DAY_OF_MONTH)).toString();
		if (cal.get(Calendar.DAY_OF_MONTH) < 9) {
			day = "0" + day;
		}
		String month = monthName[cal.get(Calendar.MONTH)];
		return month;
	}

	public int getYear(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		String day = new Integer(cal.get(Calendar.DAY_OF_MONTH)).toString();
		if (cal.get(Calendar.DAY_OF_MONTH) < 9) {
			day = "0" + day;
		}
		String month = new Integer(cal.get(Calendar.MONTH) + 1).toString();
		if ((cal.get(Calendar.MONTH) + 1) < 9) {
			month = "0" + month;
		}
		int year = cal.get(Calendar.YEAR);
		return year;
	}

	public File generateTemplate(CustomerResponseDTO customerResponseDTO) throws CConvertException, IOException {

		Map<String, Object> props = new HashMap<String, Object>();
		props.put("name", customerResponseDTO.getName());
		props.put("address", customerResponseDTO.getAddress());
		props.put("poagreement", customerResponseDTO.getPoagreement());
		props.put("invoiceNumber", customerResponseDTO.getInvoiceNumber());

		String monthName = getMonth(customerResponseDTO.getPeriod());
		String period = monthName + " " + getYear(customerResponseDTO.getPeriod());

		props.put("period", period);
		props.put("amount", customerResponseDTO.getAmount());
		props.put("invoiceDue", customerResponseDTO.getInvoiceDue());
		String strDate="";
		if(customerResponseDTO.getInvoiceDate()!=null){
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");  
	     strDate= formatter.format(customerResponseDTO.getInvoiceDate());  
		}

		
		props.put("invoiceDate",strDate);
		props.put("accountDetails", customerResponseDTO.getAccountDetails());

		double totalInvoice = 0.0; 
		List<InvoiceProjectResponseDTO> invoiceProject = new ArrayList<>();
		List<InvoiceProjectDetails> invoiceProjectDetails = customerResponseDTO.getInvoiceProjectDetails();
		for (InvoiceProjectDetails invoiceProjectDetail : invoiceProjectDetails) {
			String month = getMonth(invoiceProjectDetail.getPeriod());
			String perio = month + " " + getYear(invoiceProjectDetail.getPeriod());
			InvoiceProjectResponseDTO invoiceProjectResponseDTO = new InvoiceProjectResponseDTO();
			invoiceProjectResponseDTO.setPeriod(perio);
			invoiceProjectResponseDTO.setProjectName(invoiceProjectDetail.getProjectName());
			invoiceProjectResponseDTO.setResources(invoiceProjectDetail.getResources());
			invoiceProjectResponseDTO.setType(invoiceProjectDetail.getType());
			invoiceProjectResponseDTO.setAmountBilled(invoiceProjectDetail.getAmountBilled());
			invoiceProject.add(invoiceProjectResponseDTO);
			totalInvoice = totalInvoice + invoiceProjectDetail.getAmountBilled();

		}
		ClassPathResource agileLogo = new ClassPathResource("images/agile.jpeg");
		props.put("invoiceProjectDetails", invoiceProject);
		// ByteArrayResource mainImage = new
		// ByteArrayResource(IOUtils.toByteArray(agileLogo));

		props.put("image", agileLogo);

		props.put("totalInvoice", totalInvoice);

		String pdfTemplate = velocityUtility.getTemplatetoTextUsingProps("invoice.vm", props);

		CYaHPConverter converter = new CYaHPConverter();
		Map properties = new HashMap();
		properties.put(IHtmlToPdfTransformer.PDF_RENDERER_CLASS, IHtmlToPdfTransformer.FLYINGSAUCER_PDF_RENDERER);

		List headerFooterList = new ArrayList();

		String dateTime = new SimpleDateFormat("yyyMMdd_hhmmss").format(new Date());
		String fileName = customerResponseDTO.getName() + "_" + dateTime;

		// @Cleanup
		FileOutputStream fos = new FileOutputStream(
				new File(defaultPdfPath.concat("/").concat(fileName).concat(".pdf")));

		String baseUrlPath = new StringBuffer().append("file://").append(System.getProperty("java.io.tmpdir"))
				.toString();

		converter.convertToPdf(pdfTemplate, IHtmlToPdfTransformer.A4P, headerFooterList, baseUrlPath, fos, properties);

		Path filePath = Paths.get(defaultPdfPath.concat("/")).resolve((fileName).concat(".pdf"));
		return filePath.toFile();

	}
}
