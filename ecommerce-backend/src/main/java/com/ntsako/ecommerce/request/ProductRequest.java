package com.ntsako.ecommerce.request;

import java.util.HashSet;
import java.util.Set;

import com.ntsako.ecommerce.model.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {
	
	private String title;
	private String description;
	private int price;
	private int discountedPrice;
	private int discountedPercent;
	private int quantity;
	private String brand;
	private String color;
	private Set<Size> sizes = new HashSet<>();
	private String imageUrl;
	private String topLevelCategory;
	private String secondLevelCategory;
	private String thirdLevelCategory;
}
