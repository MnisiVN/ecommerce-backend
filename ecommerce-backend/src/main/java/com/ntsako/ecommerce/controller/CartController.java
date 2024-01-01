package com.ntsako.ecommerce.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ntsako.ecommerce.exception.ProductException;
import com.ntsako.ecommerce.model.Cart;
import com.ntsako.ecommerce.request.ItemRequest;
import com.ntsako.ecommerce.service.CartService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/cart")
public class CartController {

	private CartService cartService;

	@PostMapping("/add/{userId}")
	public ResponseEntity<String> addCartItem(@PathVariable Long userId, @RequestBody ItemRequest itemRequest) throws ProductException {
		
		String addedCartItem = cartService.addCartItem(userId, itemRequest); 
		
		return new ResponseEntity<> (addedCartItem, HttpStatus.ACCEPTED);
	}

	@GetMapping("/{userId}")
	public ResponseEntity<Cart> getUserCart(@PathVariable Long userId) {
		
		Cart cart = cartService.findUserCart(userId);
		
		return new ResponseEntity<> (cart, HttpStatus.ACCEPTED);
	}

}
