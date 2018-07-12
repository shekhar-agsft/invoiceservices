package com.agile.aggrement.invoice.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Proxy;

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
@Entity
@Table(name="user_roles")
public class UserRole{
	
	@Id
	@Column(name="role_id")
	String roleId;
	
	@Column(name="role_name")
	String roleName;
	
	@ManyToMany(mappedBy = "userRoles")
	@LazyCollection(LazyCollectionOption.FALSE)
	@JsonIgnore
	private List<User> users;

}
