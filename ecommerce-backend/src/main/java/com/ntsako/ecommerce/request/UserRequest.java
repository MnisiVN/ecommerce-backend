package com.ntsako.ecommerce.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {

	private String email;
	private String password;
	private String firstName;
	private String lastName;
	private String mobile;
}
