package com.agile.aggrement.invoice.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
@Table(name = "customer")
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "cust_id")
	int custId;

	String name;

	String address;

	String poagreement;

	Date period;

	/*@OneToMany(mappedBy = "")
	Invoice invoice;*/
	@OneToMany(mappedBy = "custId")
	List<Invoice> invoice;

}
