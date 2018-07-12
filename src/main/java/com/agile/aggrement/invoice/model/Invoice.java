package com.agile.aggrement.invoice.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@ToString(includeFieldNames = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = false)
@Entity
@Table(name = "invoice")
public class Invoice {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "invoice_id")
	int invoiceId;

	Date invoiceDate;

	Double amount;

	String invoiceDue;

	@ManyToOne
	@JoinColumn(name = "cust_id")
	Customer custId;

}
