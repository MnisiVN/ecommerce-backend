package com.ntsako.ecommerce.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ntsako.ecommerce.exception.OrderItemException;
import com.ntsako.ecommerce.exception.UserException;
import com.ntsako.ecommerce.model.Order;
import com.ntsako.ecommerce.model.OrderItem;
import com.ntsako.ecommerce.model.Product;
import com.ntsako.ecommerce.model.User;
import com.ntsako.ecommerce.repository.OrderItemRepository;
import com.ntsako.ecommerce.service.OrderItemService;
import com.ntsako.ecommerce.service.UserService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class OrderItemServiceImpelementation implements OrderItemService {

	private OrderItemRepository orderItemRepository;
	private UserService userService;

	@Override
	public OrderItem createOrderItem(OrderItem orderItem) {

		orderItem.setQuantity(1);
		orderItem.setPrice(orderItem.getProduct().getPrice() * orderItem.getQuantity());
		orderItem.setDiscountedPrice(orderItem.getProduct().getDiscountedPrice() * orderItem.getQuantity());

		OrderItem createdOrderItem = orderItemRepository.save(orderItem);

		return createdOrderItem;
	}

	@Override
	public OrderItem updateOrderItem(Long userId, Long orderItemId, OrderItem orderItem)
			throws OrderItemException, UserException {

		OrderItem item = findOrderItemById(orderItemId);
		User user = userService.findUserById(item.getUserId());

		if (user == null) {
			throw new UserException("User not found with userId : " + userId);
		}

		if (user.getId().equals(userId)) {
			item.setQuantity(orderItem.getQuantity());
			item.setPrice(item.getQuantity() * item.getProduct().getPrice());
			item.setDiscountedPrice(item.getProduct().getDiscountedPrice() * item.getQuantity());
		}

		return orderItemRepository.save(item);
	}

	@Override
	public OrderItem isOrderItemExist(Order order, Product product, String size, Long userId) {

		OrderItem orderItem = orderItemRepository.isOrderItemExist(order, product, size, userId);

		return orderItem;
	}

	@Override
	public void removeOrderItem(Long userId, Long orderItemId) throws OrderItemException, UserException {
		
		OrderItem orderItem = findOrderItemById(orderItemId);

		User user = userService.findUserById(orderItem.getUserId());

		if (user == null) {
			throw new UserException("User not found with userId : " + userId);
		}
		
		if (user.getId().equals(userId)) {
			orderItemRepository.delete(orderItem);
		} else {
			throw new UserException("You cannot remove another user's Item");
		}
	}

	@Override
	public OrderItem findOrderItemById(Long orderItemId) throws OrderItemException {
		
		Optional<OrderItem> optional = orderItemRepository.findById(orderItemId);
		
		if(optional.isPresent()) {
			return optional.get();
		}
		throw new OrderItemException("OrderItem not found with orderItemId : " + orderItemId);
	}

}
