package com.ntsako.ecommerce.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ntsako.ecommerce.exception.ProductException;
import com.ntsako.ecommerce.exception.UserException;
import com.ntsako.ecommerce.model.Product;
import com.ntsako.ecommerce.model.Review;
import com.ntsako.ecommerce.model.User;
import com.ntsako.ecommerce.repository.ReviewRepository;
import com.ntsako.ecommerce.request.ReviewRequest;
import com.ntsako.ecommerce.service.ProductService;
import com.ntsako.ecommerce.service.ReviewService;
import com.ntsako.ecommerce.service.UserService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ReviewServiceImplementation implements ReviewService {
	
	private ReviewRepository reviewRepository;
	private ProductService productService;
	private UserService userService;
	
	@Override
	public Review createReview(ReviewRequest reviewRequest) throws ProductException, UserException {
		
		Product product = productService.findProductById(reviewRequest.getProductId());
		User user = userService.findUserById(reviewRequest.getUserId());
		
		Review review = new Review();
		review.setProduct(product);
		review.setUser(user);
		review.setReview(reviewRequest.getReview());
		review.setCreatedAt(LocalDateTime.now());
		
		return reviewRepository.save(review);
	}

	@Override
	public List<Review> getAllProductReviews(Long productId) {
		
		return reviewRepository.getAllProductRatings(productId);
	}

}
