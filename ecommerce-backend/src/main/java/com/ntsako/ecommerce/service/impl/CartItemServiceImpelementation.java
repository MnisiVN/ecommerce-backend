package com.ntsako.ecommerce.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ntsako.ecommerce.exception.CartItemException;
import com.ntsako.ecommerce.exception.UserException;
import com.ntsako.ecommerce.model.Cart;
import com.ntsako.ecommerce.model.CartItem;
import com.ntsako.ecommerce.model.Product;
import com.ntsako.ecommerce.model.User;
import com.ntsako.ecommerce.repository.CartItemRepository;
import com.ntsako.ecommerce.service.CartItemService;
import com.ntsako.ecommerce.service.UserService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class CartItemServiceImpelementation implements CartItemService {

	private CartItemRepository cartItemRepository;
	private UserService userService;

	@Override
	public CartItem createCartItem(CartItem cartItem) {

		cartItem.setQuantity(1);
		cartItem.setPrice(cartItem.getProduct().getPrice() * cartItem.getQuantity());
		cartItem.setDiscountedPrice(cartItem.getProduct().getDiscountedPrice() * cartItem.getQuantity());

		CartItem createdCartItem = cartItemRepository.save(cartItem);

		return createdCartItem;
	}

	@Override
	public CartItem updateCartItem(Long userId, Long cartItemId, CartItem cartItem)
			throws CartItemException, UserException {

		CartItem item = findCartItemById(cartItemId);
		User user = userService.findUserById(item.getUserId());

		if (user == null) {
			throw new UserException("User not found with userId : " + userId);
		}

		if (user.getId().equals(userId)) {
			item.setQuantity(cartItem.getQuantity());
			item.setPrice(item.getQuantity() * item.getProduct().getPrice());
			item.setDiscountedPrice(item.getProduct().getDiscountedPrice() * item.getQuantity());
		}

		return cartItemRepository.save(item);
	}

	@Override
	public CartItem isCartItemExist(Cart cart, Product product, String size, Long userId) {

		CartItem cartItem = cartItemRepository.isCartItemExist(cart, product, size, userId);

		return cartItem;
	}

	@Override
	public void removeCartItem(Long userId, Long cartItemId) throws CartItemException, UserException {
		
		CartItem cartItem = findCartItemById(cartItemId);

		User user = userService.findUserById(cartItem.getUserId());

		if (user == null) {
			throw new UserException("User not found with userId : " + userId);
		}
		
		if (user.getId().equals(userId)) {
			cartItemRepository.delete(cartItem);
		} else {
			throw new UserException("You cant remove another user's Item");
		}
	}

	@Override
	public CartItem findCartItemById(Long cartItemId) throws CartItemException {
		
		Optional<CartItem> optional = cartItemRepository.findById(cartItemId);
		
		if(optional.isPresent()) {
			return optional.get();
		}
		throw new CartItemException("CartItem not found with cartItemId : " + cartItemId);
	}

}
