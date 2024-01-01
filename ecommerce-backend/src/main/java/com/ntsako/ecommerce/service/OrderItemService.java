package com.ntsako.ecommerce.service;


import com.ntsako.ecommerce.exception.OrderItemException;
import com.ntsako.ecommerce.exception.UserException;
import com.ntsako.ecommerce.model.Order;
import com.ntsako.ecommerce.model.OrderItem;
import com.ntsako.ecommerce.model.Product;

public interface OrderItemService {

	public OrderItem createOrderItem(OrderItem orderItem) ;
	
	public OrderItem updateOrderItem(Long userId, Long orderItemId, OrderItem orderItem) throws OrderItemException, UserException;
	
	public OrderItem isOrderItemExist(Order order, Product product, String size, Long userId);
	
	public void removeOrderItem(Long userId, Long orderItemId) throws OrderItemException, UserException;
	
	public OrderItem findOrderItemById(Long orderItemId) throws OrderItemException;
}
