package com.ntsako.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ntsako.ecommerce.model.Order;
import com.ntsako.ecommerce.model.OrderItem;
import com.ntsako.ecommerce.model.Product;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

	@Query("SELECT oi FROM OrderItem oi WHERE oi.order =: order AND oi.product =: product AND oi.size =: size AND oi.userId =: userId")
	public OrderItem isOrderItemExist(@Param("order") Order order, @Param("product") Product product,
			@Param("size") String size, @Param("userId") Long userId);

}
