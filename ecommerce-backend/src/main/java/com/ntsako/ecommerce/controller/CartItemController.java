package com.ntsako.ecommerce.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ntsako.ecommerce.exception.CartItemException;
import com.ntsako.ecommerce.exception.UserException;
import com.ntsako.ecommerce.model.CartItem;
import com.ntsako.ecommerce.service.CartItemService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("api/cartItems")
public class CartItemController {

	private CartItemService itemService;

	@PutMapping("/cartItem")
	public ResponseEntity<CartItem> updateCartItem(@RequestParam Long userId, @RequestParam Long cartItemId,
			@RequestBody CartItem cartItem) throws CartItemException, UserException {

		CartItem updatedCartItem = itemService.updateCartItem(userId, cartItemId, cartItem);

		return new ResponseEntity<>(updatedCartItem, HttpStatus.ACCEPTED);
	}

	@DeleteMapping("/cartItem")
	public ResponseEntity<String> removeCartItem(@RequestParam Long userId, @RequestParam Long cartItemId) throws CartItemException, UserException {
		
		itemService.removeCartItem(userId, cartItemId);
		
		return new ResponseEntity<>("Item Removed Successfully", HttpStatus.ACCEPTED);
	}

	@GetMapping("/cartItem/{cartItemId}")
	public ResponseEntity<CartItem> getCartItemById(@PathVariable Long cartItemId) throws CartItemException {
		
		CartItem foundCartItem = itemService.findCartItemById(cartItemId);

		return new ResponseEntity<>(foundCartItem, HttpStatus.ACCEPTED);
	}

}
