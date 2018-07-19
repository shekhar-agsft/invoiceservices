package com.agile.aggrement.invoice.serviceimpl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
import org.apache.poi.util.IOUtils;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
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

			if (requestDTO.getInvoiceSeries() != null && !requestDTO.getInvoiceSeries().isEmpty()) {

				int index = -1;
				for (int i = 0; i < requestDTO.getInvoiceSeries().length(); i++) {
					char c = requestDTO.getInvoiceSeries().charAt(i);
					if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
						index = i;

					}
				}
				if (index == requestDTO.getInvoiceSeries().length() - 1) {
					throw new InvoiceException(500, "Last unit should be digit in invoice series");
				}
				if (index == -1) {
					throw new InvoiceException(500, "Atleast one charcter should be there in invoice series");
				}

			} else {
				throw new InvoiceException(500, "Please add the invoice series");
			}

			customerRepository.save(requestDTO);
		} else {
			throw new InvoiceException(500, "No input data");
		}

	}

	@Override
	public void update(Customer requestDTO, int custId) throws InvoiceException {

		if (requestDTO != null) {
			if (custId != 0) {
				Customer customer = customerRepository.findOne(custId);
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

				if (requestDTO.getInvoiceSeries() != null && !requestDTO.getInvoiceSeries().isEmpty()) {

					int index = -1;
					for (int i = 0; i < requestDTO.getInvoiceSeries().length(); i++) {
						char c = requestDTO.getInvoiceSeries().charAt(i);
						if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
							index = i;

						}
					}
					if (index == requestDTO.getInvoiceSeries().length() - 1) {
						throw new InvoiceException(500, "Last unit should be digit in invoice series");
					}
					if (index == -1) {
						throw new InvoiceException(500, "Atleast one charcter should be there in invoice series");
					}

				} else {
					throw new InvoiceException(500, "Please add the invoice series");
				}
				customer.setAddress(requestDTO.getAddress());
				customer.setInvoiceSeries(requestDTO.getInvoiceSeries());
				customer.setName(requestDTO.getName());
				customer.setPoagreement(requestDTO.getPoagreement());
				customerRepository.save(customer);

			} else {
				throw new InvoiceException(500, "Please provide custID");
			}

		} else {
			throw new InvoiceException(500, "No input data");
		}
	}

	@Override
	public void saveInvoice(Invoice requestDTO, int custId) throws InvoiceException {

		Customer customer = customerRepository.findOne(custId);
		requestDTO.setCustId(customer);
		int invoiceLastDigit = 0;
		String prefix = "";
		String invoiceSeries = customer.getInvoiceSeries();
		if (invoiceSeries != null && !invoiceSeries.isEmpty()) {

			int index = -1;
			for (int i = 0; i < invoiceSeries.length(); i++) {
				char c = invoiceSeries.charAt(i);
				if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
					index = i;

				}
			}
			if (index == invoiceSeries.length()) {
				throw new InvoiceException(500, "Last unit should be digit in invoice series");
			}
			prefix = invoiceSeries.substring(0, index + 1);
			invoiceLastDigit = Integer.parseInt(invoiceSeries.substring(index + 1, invoiceSeries.length()));
		} else {
			throw new InvoiceException(500, "Please add the invoice series");
		}
		String invoiceNumber = "";
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
				invoiceLastDigit++;
				invoiceNumber = prefix.concat("" + invoiceLastDigit);
			}
		}
		if (invoiceNumber != null && !invoiceNumber.isEmpty()) {
			int invoiceadd = invoiceLastDigit + 1;
			requestDTO.setInvoiceNumber(prefix.concat("" + invoiceadd));
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

			List<InvoiceProjectDetails> invoiceProjectDetails = projectRepository.findByInvoiceId(invoice);
			double totalInvoice = 0.0;
			if (!invoiceProjectDetails.isEmpty()) {
				for (InvoiceProjectDetails invoiceProjectDetail : invoiceProjectDetails) {
					totalInvoice = totalInvoice + invoiceProjectDetail.getAmountBilled();

				}
			}

			double newAmount = requestDTO.getAmountBilled() + totalInvoice;

			invoice.setAmount(newAmount);
			invoiceRepository.save(invoice);

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
		customerResponseDTO.setPeriod(invoice.getPeriod());
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
		AccountDetails accountDetail = accountDetails.get(0);
		if (accountDetail != null)
			customerResponseDTO.setAccountDetails(accountDetail);

		return customerResponseDTO;
	}

	@Override
	public List<InvoiceProjectDetails> getProjectDetails(int invoiceId) throws InvoiceException {

		if (invoiceId != 0) {
			Invoice invoice = invoiceRepository.findOne(invoiceId);
			List<InvoiceProjectDetails> invoiceProjectDetails = projectRepository.findByInvoiceId(invoice);
			return invoiceProjectDetails;
		} else {
			throw new InvoiceException(500, "Please provide invoice Number");
		}

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
	public Invoice getInvoice(int invoiceId) throws InvoiceException {

		if (invoiceId != 0) {
			return invoiceRepository.findOne(invoiceId);
		} else {
			throw new InvoiceException(500, "Please provide invoice Id");
		}

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

		String invoiceSeries = customer.getInvoiceSeries();
		int index = -1;
		System.out.println(index);
		for (int i = 0; i < invoiceSeries.length(); i++) {
			char c = invoiceSeries.charAt(i);
			if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
				index = i;

			}
		}
		System.out.println(index);

		String prefix = invoiceSeries.substring(0, index + 1);
		System.out.println(prefix);
		int invoiceLastDigit = Integer.parseInt(invoiceSeries.substring(index + 1, invoiceSeries.length()));

		String invoiceNumber = "";
		List<Invoice> invoice = (List<Invoice>) invoiceRepository.findAll();
		Invoice invoiceObj = null;
		if (!invoice.isEmpty()) {
			int size = invoice.size();
			invoiceObj = invoice.get(size - 1);
		}
		if (invoiceObj != null) {
			if (invoiceObj.getCustId() == customer) {

				invoiceNumber = invoiceObj.getInvoiceNumber();
			}
		}
		if (invoiceNumber != null && !invoiceNumber.isEmpty()) {
			int invoiceadd = invoiceLastDigit + 1;
			customerInvoiceResponseDTO.setInvoiceNumber(prefix.concat("" + invoiceadd));
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

		props.put("period", customerResponseDTO.getPeriod());
		props.put("amount", customerResponseDTO.getAmount());
		props.put("invoiceDue", customerResponseDTO.getInvoiceDue());
		String strDate = "";
		if (customerResponseDTO.getInvoiceDate() != null) {
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			strDate = formatter.format(customerResponseDTO.getInvoiceDate());
		}

		props.put("invoiceDate", strDate);
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
		ClassPathResource agileLogo = new ClassPathResource("classpath:images/agile.jpeg");
		// ClassPathResource wazooDualLogo = new
		// ClassPathResource("classpath:images/logo.png");

		props.put("invoiceProjectDetails", invoiceProject);
		// ByteArrayResource mainImage = new
		// ByteArrayResource(IOUtils.toByteArray(agileLogo));

		props.put("agileLogo", agileLogo.getPath());

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
