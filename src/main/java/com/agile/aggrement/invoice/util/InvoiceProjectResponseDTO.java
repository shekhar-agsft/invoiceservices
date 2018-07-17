package com.agile.aggrement.invoice.util;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@ToString(includeFieldNames = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = false)
public class InvoiceProjectResponseDTO {
	String projectName;

	String resources;

	String period;

	String type;

	Double amountBilled;
}
