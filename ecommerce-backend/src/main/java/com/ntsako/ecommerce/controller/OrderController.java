package com.ntsako.ecommerce.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ntsako.ecommerce.exception.OrderException;
import com.ntsako.ecommerce.exception.ProductException;
import com.ntsako.ecommerce.model.Address;
import com.ntsako.ecommerce.model.Order;
import com.ntsako.ecommerce.service.OrderService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/orders")
public class OrderController {

	private OrderService orderService;

	@PostMapping("/order/{userId}")
	public ResponseEntity<Order> createOrder(@PathVariable Long userId, @RequestBody Address shippingAdress) {

		Order order = orderService.createOrder(userId, shippingAdress);

		return new ResponseEntity<>(order, HttpStatus.ACCEPTED);
	}

	@GetMapping("/order/{orderId}")
	public ResponseEntity<Order> getOrderById(@PathVariable Long orderId) throws OrderException {

		Order order = orderService.findOrderById(orderId);

		return new ResponseEntity<>(order, HttpStatus.ACCEPTED);
	}

	@GetMapping("/history/{userId}")
	public ResponseEntity<List<Order>> userOrderHistory(@PathVariable Long userId) {

		List<Order> userOrders = orderService.userOrderHistory(userId);

		return new ResponseEntity<>(userOrders, HttpStatus.ACCEPTED);
	}

	@GetMapping("/order/pace/{orderId}")
	public ResponseEntity<Order> placedOrder(@PathVariable Long orderId) throws OrderException, ProductException {

		Order order = orderService.placedOrder(orderId);

		return new ResponseEntity<>(order, HttpStatus.ACCEPTED);
	}

	@GetMapping("/order/confirm/{orderId}")
	public ResponseEntity<Order> confirmedOrder(@PathVariable Long orderId) throws OrderException {

		Order order = orderService.confirmedOrder(orderId);

		return new ResponseEntity<>(order, HttpStatus.ACCEPTED);
	}

	@GetMapping("/order/ship/{orderId}")
	public ResponseEntity<Order> shippedOrder(@PathVariable Long orderId) throws OrderException {

		Order order = orderService.shippedOrder(orderId);

		return new ResponseEntity<>(order, HttpStatus.ACCEPTED);
	}

	@GetMapping("/order/out/{orderId}")
	public ResponseEntity<Order> outOnDeliveryOrder(@PathVariable Long orderId) throws OrderException {

		Order order = orderService.outOnDeliveryOrder(orderId);

		return new ResponseEntity<>(order, HttpStatus.ACCEPTED);
	}

	@GetMapping("/order/deliver/{orderId}")
	public ResponseEntity<Order> deliveredOrder(@PathVariable Long orderId) throws OrderException {

		Order order = orderService.deliveredOrder(orderId);

		return new ResponseEntity<>(order, HttpStatus.ACCEPTED);
	}

	@GetMapping("/order/cancel/{orderId}")
	public ResponseEntity<Order> canceledOrder(@PathVariable Long orderId) throws OrderException {

		Order order = orderService.canceledOrder(orderId);

		return new ResponseEntity<>(order, HttpStatus.ACCEPTED);
	}

	@GetMapping
	public ResponseEntity<List<Order>> getAllOrders() {

		List<Order> allOrders = orderService.getAllOrders();

		return new ResponseEntity<>(allOrders, HttpStatus.ACCEPTED);
	}

	@DeleteMapping("/order/{orderId}")
	public ResponseEntity<String> deleteOrder(@PathVariable Long orderId) throws OrderException {

		orderService.deleteOrder(orderId);

		return new ResponseEntity<>("Order deleted successfully", HttpStatus.ACCEPTED);
	}

}
