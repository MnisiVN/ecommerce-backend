package com.ntsako.ecommerce.service.impl;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ntsako.ecommerce.constant.StatusConstant;
import com.ntsako.ecommerce.exception.OrderException;
import com.ntsako.ecommerce.model.Address;
import com.ntsako.ecommerce.model.Cart;
import com.ntsako.ecommerce.model.CartItem;
import com.ntsako.ecommerce.model.Order;
import com.ntsako.ecommerce.model.OrderItem;
import com.ntsako.ecommerce.model.User;
import com.ntsako.ecommerce.repository.OrderRepository;
import com.ntsako.ecommerce.service.CartService;
import com.ntsako.ecommerce.service.OrderItemService;
import com.ntsako.ecommerce.service.OrderService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class OrderServiceImplementation implements OrderService {

	private CartService cartService;
	private OrderItemService orderItemService;
	private OrderRepository orderRepository;

	@Override
	public Order createOrder(User user, Address shippingAdress) {

		Cart cart = cartService.findUserCart(user.getId());

		Order order = new Order();
		order.setUser(user);
		order.setShippingAddress(shippingAdress);
		order.setTotalPrice(cart.getTotalPrice());
		order.setTotalDiscountedPrice(cart.getTotalDicountedPrice());
		order.setDiscount(cart.getDiscount());
		order.setTotalItem(cart.getTotalItem());
		order.setOrderStatus(StatusConstant.CREATED);
		order.setCreatedAt(LocalDateTime.now());

		Order createdOrder = orderRepository.save(order);

		List<OrderItem> orderItems = mapAndPersistCartItemsToOrderItems(cart.getCartItems(), createdOrder);
		createdOrder.setOrderItems(orderItems);

		return createdOrder;
	}

	@Override
	public Order findOrderById(Long orderId) throws OrderException {

		Optional<Order> optional = orderRepository.findById(orderId);

		if (optional.isPresent()) {
			return optional.get();
		}

		throw new OrderException("Order not found with orderId : " + orderId);
	}

	@Override
	public List<Order> userOrderHistory(Long userId) {

		return orderRepository.findOrderHistoryByUserId(userId);
	}

	@Override
	public Order placedOrder(Long orderId) throws OrderException {

		// TODO: email updates could be embedded to notify user on status

		Order order = findOrderById(orderId);

		order.setOrderDate(LocalDateTime.now());
		order.setOrderStatus(StatusConstant.PLACED);
		order.setOrderNumber(generateOrderNumber(order.getId()));
		
		cartService.clearCartAndItems(order.getUser().getId());

		return orderRepository.save(order);
	}

	@Override
	public Order confirmedOrder(Long orderId) throws OrderException {

		// TODO: email updates could be embedded to notify user on status

		Order order = findOrderById(orderId);

		order.setOrderStatus(StatusConstant.ORDER_CONFIRMED);

		return orderRepository.save(order);
	}

	@Override
	public Order shippedOrder(Long orderId) throws OrderException {

		// TODO: email updates could be embedded to notify user on status

		Order order = findOrderById(orderId);

		order.setOrderStatus(StatusConstant.SHIPPED);

		return orderRepository.save(order);
	}

	@Override
	public Order outOnDeliveryOrder(Long orderId) throws OrderException {

		// TODO: email updates could be embedded to notify user on status

		Order order = findOrderById(orderId);

		order.setOrderStatus(StatusConstant.OUT_FOR_DELIVERY);

		return orderRepository.save(order);
	}

	@Override
	public Order deliveredOrder(Long orderId) throws OrderException {

		// TODO: email updates could be embedded to notify user on status

		Order order = findOrderById(orderId);

		order.setDeliveryDate(LocalDateTime.now());
		order.setOrderStatus(StatusConstant.DELIVERED);

		return orderRepository.save(order);
	}

	@Override
	public Order canceledOrder(Long orderId) throws OrderException {

		// TODO: email updates could be embedded to notify user on status

		Order order = findOrderById(orderId);

		order.setOrderStatus(StatusConstant.CANCELLED);

		return orderRepository.save(order);
	}

	@Override
	public List<Order> getAllOrders() {
		
		return orderRepository.findAll();
	}

	@Override
	public void deleteOrder(Long orderId) throws OrderException {
		
		Order order = findOrderById(orderId);
		
		orderRepository.delete(order);
	}

	private List<OrderItem> mapAndPersistCartItemsToOrderItems(Set<CartItem> cartItems, Order order) {
		return cartItems.stream().map(cartItem -> mapCartItemToOrderItem(cartItem, order))
				.map(orderItemService::createOrderItem).collect(Collectors.toList());
	}

	private OrderItem mapCartItemToOrderItem(CartItem cartItem, Order order) {
		OrderItem orderItem = new OrderItem();

		orderItem.setOrder(order);
		orderItem.setProduct(cartItem.getProduct());
		orderItem.setSize(cartItem.getSize());
		orderItem.setQuantity(cartItem.getQuantity());
		orderItem.setPrice(cartItem.getPrice());
		orderItem.setDiscountedPrice(cartItem.getDiscountedPrice());
		orderItem.setUserId(cartItem.getUserId());

		return orderItem;
	}

	public String generateOrderNumber(long orderId) {
		String orderNumber = "";

		String year = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
		String month = Integer.toString(Calendar.getInstance().get(Calendar.MONTH));
		String day = Integer.toString(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
		orderNumber = "O" + "-" + year + month + day + "-" + orderId;

		return orderNumber;
	}

}
