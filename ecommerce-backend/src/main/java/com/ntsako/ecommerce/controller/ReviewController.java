package com.ntsako.ecommerce.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ntsako.ecommerce.exception.ProductException;
import com.ntsako.ecommerce.exception.UserException;
import com.ntsako.ecommerce.model.Review;
import com.ntsako.ecommerce.request.ReviewRequest;
import com.ntsako.ecommerce.service.ReviewService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("api/reviews")
public class ReviewController {

	private ReviewService reviewService;

	@PostMapping("/review")
	public ResponseEntity<Review> createReview(@RequestBody ReviewRequest reviewRequest) throws ProductException, UserException {
		
		Review review = reviewService.createReview(reviewRequest);
		
		return new ResponseEntity<>(review, HttpStatus.ACCEPTED);
	}

	@GetMapping("/{productId}")
	public ResponseEntity<List<Review>> getAllProductReviews(@PathVariable Long productId) {
		
		List<Review> reviews = reviewService.getAllProductReviews(productId);
		
		return new ResponseEntity<>(reviews, HttpStatus.ACCEPTED);
	}

}
