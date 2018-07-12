package com.agile.aggrement.invoice.jwt;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Vishal
 *
 */
@Getter
@AllArgsConstructor
public class JwtAuthResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4322404214501494494L;

	private String token;

}
