package com.agile.aggrement.invoice.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "user_jwt_token")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(includeFieldNames = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = false)
public class UserLoginToken {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id")
	int id;
	
	@Column(name="token")
	String token;
	
	@Column(name = "user_email_id")
	String userEmailId;

}
