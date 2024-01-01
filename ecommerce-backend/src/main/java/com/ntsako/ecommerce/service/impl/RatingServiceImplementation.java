package com.ntsako.ecommerce.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ntsako.ecommerce.exception.ProductException;
import com.ntsako.ecommerce.exception.UserException;
import com.ntsako.ecommerce.model.Product;
import com.ntsako.ecommerce.model.Rating;
import com.ntsako.ecommerce.model.User;
import com.ntsako.ecommerce.repository.RatingRepository;
import com.ntsako.ecommerce.request.RatingRequest;
import com.ntsako.ecommerce.service.ProductService;
import com.ntsako.ecommerce.service.RatingService;
import com.ntsako.ecommerce.service.UserService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class RatingServiceImplementation implements RatingService {

	private RatingRepository ratingRepository;
	private ProductService productService;
	private UserService userService;

	@Override
	public Rating createRating(RatingRequest ratingRequest) throws ProductException, UserException {

		Product product = productService.findProductById(ratingRequest.getProductId());
		User user = userService.findUserById(ratingRequest.getUserId());
		
		Rating rating = new Rating();
		rating.setProduct(product);
		rating.setUser(user);
		rating.setRating(ratingRequest.getRating());
		rating.setCreatedAt(LocalDateTime.now());

		return ratingRepository.save(rating);
	}

	@Override
	public List<Rating> getProductRatings(Long productId) {

		return ratingRepository.getAllProductsRatings(productId);
	}

}
