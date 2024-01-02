package com.ntsako.ecommerce.service.impl;

import org.springframework.stereotype.Service;

import com.ntsako.ecommerce.exception.CartItemException;
import com.ntsako.ecommerce.exception.ProductException;
import com.ntsako.ecommerce.exception.UserException;
import com.ntsako.ecommerce.model.Cart;
import com.ntsako.ecommerce.model.CartItem;
import com.ntsako.ecommerce.model.Product;
import com.ntsako.ecommerce.model.User;
import com.ntsako.ecommerce.repository.CartRepository;
import com.ntsako.ecommerce.request.ItemRequest;
import com.ntsako.ecommerce.service.CartItemService;
import com.ntsako.ecommerce.service.CartService;
import com.ntsako.ecommerce.service.ProductService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class CartServiceImplementation implements CartService{
	
	private CartRepository cartRepository;
	private CartItemService cartItemService;
	private ProductService productService;

	@Override
	public Cart creatCart(User user) {
		
		Cart cart = new Cart();
		cart.setUser(user);
		return cartRepository.save(cart);
	}

	@Override
	public String addCartItem(Long userId, ItemRequest itemRequest) throws ProductException, CartItemException, UserException {
		
		Cart cart = cartRepository.findByUserId(userId);
		Product product = productService.findProductById(itemRequest.getProductId());
		
		CartItem cartItemPresent = cartItemService.isCartItemExist(cart, product, itemRequest.getSize(), userId);
		
		if (cartItemPresent == null) {
			
			CartItem cartItem = new CartItem();
			cartItem.setProduct(product);
			cartItem.setCart(cart);
			cartItem.setQuantity(itemRequest.getQuantity());
			cartItem.setUserId(userId);
			
			int price = itemRequest.getQuantity() * product.getDiscountedPrice();
			cartItem.setPrice(price);
			cartItem.setSize(itemRequest.getSize());
			
			CartItem createdCartItem = cartItemService.createCartItem(cartItem);
			cart.getCartItems().add(createdCartItem);
		}  else {
			
			cartItemPresent.setQuantity(cartItemPresent.getQuantity() + itemRequest.getQuantity());
	
			cartItemService.updateCartItem(userId, cartItemPresent.getId(), cartItemPresent);
		}
		
		return "Item Added To Cart";
	}

	@Override
	public Cart findUserCart(Long userId) {
		
		Cart cart = cartRepository.findByUserId(userId);
		
		int totalPrice = 0;
		int totalDiscountedPrice = 0;
		int totalItem = 0;
		
		for(CartItem cartItem: cart.getCartItems()) {
			totalPrice = totalPrice + cartItem.getPrice();
			totalDiscountedPrice = totalDiscountedPrice + cartItem.getDiscountedPrice();
			totalItem = totalItem + cartItem.getQuantity();
		}
		
		cart.setTotalItem(totalItem);
		cart.setTotalPrice(totalPrice);
		cart.setTotalDicountedPrice(totalDiscountedPrice);
		cart.setDiscount(totalPrice - totalDiscountedPrice);
				
		return cartRepository.save(cart);
	}
	
    public void clearCartAndItems(Long userId) {
    	
    	Cart cart = cartRepository.findByUserId(userId);

        cart.getCartItems().clear();

        cart.setTotalPrice(0.0);
        cart.setTotalItem(0);
        cart.setTotalDicountedPrice(0);
        cart.setDiscount(0);

        cartRepository.save(cart);
    }

}
