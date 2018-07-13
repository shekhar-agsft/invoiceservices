package com.agile.aggrement.invoice.model;

import java.util.Date;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@ToString(includeFieldNames = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = false)
public class CustomerInvoiceResponseDTO {

	String customerName;
	
	String custAddress;
	
	String poagreement;
	
	Date period;
	
	int invoiceNumber;
}
