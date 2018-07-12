package com.agile.aggrement.invoice.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
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
@Table(name = "invoice_project")
public class InvoiceProjectDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "project_id")
	int projectId;
	
	String projectName;
	
	String resources;
	
	Date period;
	
	String type;
	
	Double amountBilled;
	
	@OneToOne(cascade=CascadeType.ALL, targetEntity=Invoice.class)
	@JoinColumn(name="invoice_id")
	int invoiceId;
	
}
