package com.agile.aggrement.invoice.model;

import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@ToString(includeFieldNames = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = false)
public class InvoiceResponseDTO {

	
	
	List<ProjectInvoice> projectInvoices;
}
