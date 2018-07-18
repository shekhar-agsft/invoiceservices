package com.agile.aggrement.invoice.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@ToString(includeFieldNames = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = false)
public class CustomerResponseDTO {

	String name;

	String address;

	String poagreement;

	
	@JsonIgnore
	String period;

	String invoiceNumber;

	Date invoiceDate;

	Double amount;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	String invoiceDue;

	List<InvoiceProjectDetails> invoiceProjectDetails;

	AccountDetails accountDetails;

}
