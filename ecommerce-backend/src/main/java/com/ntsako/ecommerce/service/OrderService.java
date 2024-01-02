package com.ntsako.ecommerce.service;

import java.util.List;

import com.ntsako.ecommerce.exception.OrderException;
import com.ntsako.ecommerce.exception.ProductException;
import com.ntsako.ecommerce.model.Address;
import com.ntsako.ecommerce.model.Order;

public interface OrderService {
	
	public Order createOrder(Long userId, Address shippingAdress);
	
	public Order findOrderById(Long orderId) throws OrderException;
	
	public List<Order> userOrderHistory(Long userId);
	
	public Order placedOrder(Long orderId) throws OrderException, ProductException;
	
	public Order confirmedOrder(Long orderId) throws OrderException;
	
	public Order shippedOrder(Long orderId) throws OrderException;
	
	public Order outOnDeliveryOrder(Long orderId) throws OrderException;
	
	public Order deliveredOrder(Long orderId) throws OrderException;
	
	public Order canceledOrder(Long orderId) throws OrderException;
	
	public List<Order> getAllOrders();
	
	public void deleteOrder(Long orderId) throws OrderException;

}
