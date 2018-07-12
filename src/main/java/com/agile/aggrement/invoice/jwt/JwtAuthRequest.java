package com.agile.aggrement.invoice.jwt;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * @author 
 *
 */
@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = false)
@JsonPropertyOrder({ "email", "password" })
public class JwtAuthRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 702226705529588388L;

	@NotNull
	String email;

	@NotNull
	String password;

}
