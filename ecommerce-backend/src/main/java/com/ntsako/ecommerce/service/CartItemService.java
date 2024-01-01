package com.ntsako.ecommerce.service;


import com.ntsako.ecommerce.exception.CartItemException;
import com.ntsako.ecommerce.exception.UserException;
import com.ntsako.ecommerce.model.Cart;
import com.ntsako.ecommerce.model.CartItem;
import com.ntsako.ecommerce.model.Product;

public interface CartItemService {

	public CartItem createCartItem(CartItem cartItem) ;
	
	public CartItem updateCartItem(Long userId, Long cartItemId, CartItem cartItem) throws CartItemException, UserException;
	
	public CartItem isCartItemExist(Cart cart, Product product, String size, Long userId);
	
	public void removeCartItem(Long userId, Long cartItemId) throws CartItemException, UserException;
	
	public CartItem findCartItemById(Long cartItemId) throws CartItemException;
}
