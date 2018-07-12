package com.agile.aggrement.invoice.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(includeFieldNames = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = false)
public class LoginResponseDTO {

	String token;
	
	User userProfile;

}
