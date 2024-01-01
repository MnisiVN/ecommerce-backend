package com.ntsako.ecommerce.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequest {
	
	private Long productId;
	private String size;
	private int quantity;
	private Integer price;

}
