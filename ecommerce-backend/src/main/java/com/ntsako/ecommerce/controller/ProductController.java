package com.ntsako.ecommerce.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ntsako.ecommerce.exception.ProductException;
import com.ntsako.ecommerce.model.Product;
import com.ntsako.ecommerce.request.ProductRequest;
import com.ntsako.ecommerce.service.ProductService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/products")
public class ProductController {

	private ProductService productService;

	@GetMapping
	public ResponseEntity<Page<Product>> getProductsByFilterHandler(@RequestParam(required = false) String category,
			@RequestParam(required = false) List<String> color, @RequestParam(required = false) List<String> size, @RequestParam(required = false) Integer minPrice,
			@RequestParam(required = false) Integer maxPrice, @RequestParam(required = false) Integer minDiscount, @RequestParam(required = false) String sort,
			@RequestParam(required = false) String stock, @RequestParam Integer pageNumber, @RequestParam Integer pageSize) {
		
		Page<Product> products = productService.getAllProducts(category, color, size, minPrice, maxPrice, minDiscount, sort, stock, pageNumber, pageSize);

		return new ResponseEntity<> (products, HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/category")
	public ResponseEntity<List<Product>> getProductsByCategory(@RequestParam String category) {
		
		List <Product> products = productService.findProductByCategory(category);
		
		return new ResponseEntity<> (products, HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/{productId}")
	public ResponseEntity<Product> getProductByProductId(@PathVariable Long productId) throws ProductException {
		
		Product product = productService.findProductById(productId);
		
		return new ResponseEntity<> (product, HttpStatus.ACCEPTED);
	}
	
	@DeleteMapping("/{productId}")
	public ResponseEntity<String> deleteProduct(@PathVariable Long productId) throws ProductException {
		
		String deleted = productService.deleteProduct(productId);
		
		return new ResponseEntity<> (deleted, HttpStatus.ACCEPTED);
	}
	
	@PutMapping("/{productId}")
	public ResponseEntity<Product> updateProduct(@PathVariable Long productId, @RequestBody Product product) throws ProductException {
		
		Product updatedProduct = productService.updateProduct(productId, product);
		
		return new ResponseEntity<> (updatedProduct, HttpStatus.ACCEPTED);
	}
	
	@PostMapping
	public ResponseEntity<Product> createProduct(@RequestBody ProductRequest productRequest) {
		
		Product updatedProduct = productService.createProduct(productRequest);
		
		return new ResponseEntity<> (updatedProduct, HttpStatus.ACCEPTED);
	}

}
