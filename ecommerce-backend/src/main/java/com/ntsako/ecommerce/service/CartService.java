package com.ntsako.ecommerce.service;

import com.ntsako.ecommerce.exception.ProductException;
import com.ntsako.ecommerce.model.Cart;
import com.ntsako.ecommerce.model.User;
import com.ntsako.ecommerce.request.ItemRequest;

public interface CartService {
	
	public Cart creatCart(User user);
	
	public String addCartItem(Long userId, ItemRequest itemRequest) throws ProductException;
	
	public Cart findUserCart(Long userId);
	
	public void clearCartAndItems(Long userId);

}
