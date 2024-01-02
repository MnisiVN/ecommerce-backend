package com.ntsako.ecommerce.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ntsako.ecommerce.constant.StockConstant;
import com.ntsako.ecommerce.exception.ProductException;
import com.ntsako.ecommerce.model.Category;
import com.ntsako.ecommerce.model.Order;
import com.ntsako.ecommerce.model.OrderItem;
import com.ntsako.ecommerce.model.Product;
import com.ntsako.ecommerce.model.Size;
import com.ntsako.ecommerce.repository.CategoryRepository;
import com.ntsako.ecommerce.repository.ProductRepository;
import com.ntsako.ecommerce.request.ProductRequest;
import com.ntsako.ecommerce.service.ProductService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ProductServiceImplementation implements ProductService {

	private ProductRepository productRepository;
	private CategoryRepository categoryRepository;

	@Override
	public Product createProduct(ProductRequest productRequest) {

		Category topLevelCategory = categoryRepository.findByName(productRequest.getTopLevelCategory());

		if (topLevelCategory == null) {

			topLevelCategory = new Category();
			topLevelCategory.setName(productRequest.getTopLevelCategory());
			topLevelCategory.setLevel(1);

			topLevelCategory = categoryRepository.save(topLevelCategory);
		}

		Category secondLevelCategory = categoryRepository.findByNameAndParant(productRequest.getSecondLevelCategory(),
				productRequest.getTopLevelCategory());

		if (secondLevelCategory == null) {

			secondLevelCategory = new Category();
			secondLevelCategory.setName(productRequest.getSecondLevelCategory());
			secondLevelCategory.setParentCategory(topLevelCategory);
			secondLevelCategory.setLevel(2);

			secondLevelCategory = categoryRepository.save(secondLevelCategory);
		}

		Category thirdLevelCategory = categoryRepository.findByNameAndParant(productRequest.getThirdLevelCategory(),
				productRequest.getSecondLevelCategory());

		if (thirdLevelCategory == null) {

			thirdLevelCategory = new Category();
			thirdLevelCategory.setName(productRequest.getThirdLevelCategory());
			thirdLevelCategory.setParentCategory(secondLevelCategory);
			thirdLevelCategory.setLevel(3);

			thirdLevelCategory = categoryRepository.save(thirdLevelCategory);
		}

		Product product = new Product();
		product.setTitle(productRequest.getTitle());
		product.setColor(productRequest.getColor());
		product.setDescription(productRequest.getDescription());
		product.setDiscountedPrice(productRequest.getDiscountedPrice());
		product.setDiscountedPercent(productRequest.getDiscountedPercent());
		product.setImageUrl(productRequest.getImageUrl());
		product.setBrand(productRequest.getBrand());
		product.setPrice(productRequest.getPrice());
		product.setSizes(productRequest.getSizes());
		product.setQuantity(productRequest.getQuantity());
		product.setCategory(thirdLevelCategory);
		product.setCreatedAt(LocalDateTime.now());

		Product savedProduct = productRepository.save(product);

		return savedProduct;
	}

	@Override
	public String deleteProduct(Long productId) throws ProductException {

		Product product = findProductById(productId);
		product.getSizes().clear();
		productRepository.delete(product);

		return "Product Deleted Successfully...";
	}

	@Override
	public Product updateProduct(Long productId, Product productRequest) throws ProductException {

		Product product = findProductById(productId);

		product.setTitle(productRequest.getTitle());
		product.setDescription(productRequest.getDescription());
		product.setPrice(productRequest.getPrice());
		product.setDiscountedPrice(productRequest.getDiscountedPrice());
		product.setDiscountedPercent(productRequest.getDiscountedPercent());
		if (productRequest.getQuantity() != 0) {
			product.setQuantity(productRequest.getQuantity());
			product.setSizes(productRequest.getSizes());
		}

		return productRepository.save(product);
	}

	@Override
	public Product findProductById(Long productId) throws ProductException {

		Optional<Product> optional = productRepository.findById(productId);

		if (optional.isPresent()) {
			return optional.get();
		}

		throw new ProductException("Product With Product ID : " + productId + " Doesnt Exist.");
	}

	@Override
	public List<Product> findProductByCategory(String category) {

		return productRepository.findProductByCategory(category);
	}

	@Override
	public Page<Product> getAllProducts(String category, List<String> colors, List<String> sizes, Integer minPrice,
			Integer maxPrice, Integer minDiscount, String sort, String stock, Integer pageNumber, Integer pageSize) {

		Pageable pageable = PageRequest.of(pageNumber, pageSize);

		List<Product> products = productRepository.filterProducts(category, minPrice, maxPrice, minDiscount, sort);

		if (!colors.isEmpty()) {

			products = products.stream().filter(p -> colors.stream().anyMatch(c -> c.equalsIgnoreCase(p.getColor())))
					.collect(Collectors.toList());
		}

		if (!sizes.isEmpty()) {

			products = products.stream()
					.filter(p -> sizes.stream()
							.anyMatch(s -> p.getSizes().stream().anyMatch(ps -> s.equalsIgnoreCase(ps.getName()))))
					.collect(Collectors.toList());
		}

		if (stock != null) {

			if (stock.equals(StockConstant.IN_STOCK)) {

				products = products.stream().filter(p -> p.getQuantity() > 0).collect(Collectors.toList());
			} else if (stock.equals(StockConstant.OUT_OF_STOCK)) {

				products = products.stream().filter(p -> p.getQuantity() < 1).collect(Collectors.toList());
			}
		}

		int startIndex = (int) pageable.getOffset();

		int endIndex = Math.min(startIndex + pageable.getPageSize(), products.size());

		List<Product> pageContent = products.subList(startIndex, endIndex);

		Page<Product> filteredProducts = new PageImpl<>(pageContent, pageable, products.size());

		return filteredProducts;
	}

	@Override
	public void updateProductQuantities(Order order) throws ProductException {

		List<OrderItem> orderItems = order.getOrderItems();

		Map<Product, Map<String, List<OrderItem>>> productsMap = orderItems.stream()
				.collect(Collectors.groupingBy(OrderItem::getProduct, Collectors.groupingBy(OrderItem::getSize)));

		productsMap.forEach((product, sizeToOrderItemsMap) -> {

			sizeToOrderItemsMap.forEach((size, orderItemsForSize) -> {

				int totalQuantityOrderedForSize = orderItemsForSize.stream().mapToInt(OrderItem::getQuantity).sum();

				int remainingQuantityForSize = getProductSizeQuantity(product, size) - totalQuantityOrderedForSize;

				if (remainingQuantityForSize >= 0) {

					updateProductSizeQuantity(product, size, remainingQuantityForSize);
				}
			});

			int totalQuantityOrdered = sizeToOrderItemsMap.values().stream().flatMap(List::stream)
					.mapToInt(OrderItem::getQuantity).sum();

			int remainingQuantity = product.getQuantity() - totalQuantityOrdered;

			if (remainingQuantity >= 0) {

				product.setQuantity(remainingQuantity);

				productRepository.save(product);
			}
		});

	}

	private int getProductSizeQuantity(Product product, String size) {

		Set<Size> sizes = product.getSizes();

		return sizes.stream().filter(s -> s.getName().equals(size)).findFirst().map(Size::getQuantity).orElse(0);
	}

	private void updateProductSizeQuantity(Product product, String size, int newQuantity) {

		Set<Size> sizes = product.getSizes();

		sizes.stream().filter(s -> s.getName().equals(size)).findFirst().ifPresent(s -> s.setQuantity(newQuantity));
	}

}
