package com.ntsako.ecommerce.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.ntsako.ecommerce.exception.ProductException;
import com.ntsako.ecommerce.model.Order;
import com.ntsako.ecommerce.model.Product;
import com.ntsako.ecommerce.request.ProductRequest;

public interface ProductService {

	public Product createProduct(ProductRequest productRequest);

	public String deleteProduct(Long productId) throws ProductException;

	public Product updateProduct(Long productId, Product product) throws ProductException;

	public Product findProductById(Long productId) throws ProductException;

	public List<Product> findProductByCategory(String category);

	public Page<Product> getAllProducts(String category, List<String> colors, List<String> sizes, Integer minPrice, Integer maxPrice,
			Integer minDiscount, String sort, String stock, Integer pageNumber, Integer pageSize);

	public void updateProductQuantities(Order order) throws ProductException;
}
