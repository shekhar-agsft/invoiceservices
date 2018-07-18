package com.agile.aggrement.invoice.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "account_information")
public class AccountDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	int id;
	
	@Column(name = "bank_name")
	String bankName;
	
	@Column(name = "account_number")
	String accountNumber;
	
	@Column(name = "info_number")
	String infoNumber; 
	
	@Column(name = "company_name")
	String companyName;
	
	String address;
	
	@Column(name = "cust_id")
	int custId;
}
