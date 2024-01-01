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
import com.ntsako.ecommerce.model.Rating;
import com.ntsako.ecommerce.request.RatingRequest;
import com.ntsako.ecommerce.service.RatingService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/ratings")
public class RatingController {
	
	private RatingService ratingService;
	
	@PostMapping("/rating")
	public ResponseEntity<Rating> createRating(@RequestBody RatingRequest ratingRequest) throws ProductException, UserException {
		
		Rating rating = ratingService.createRating(ratingRequest);
		
		return new ResponseEntity<> (rating, HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/{productId}")
	public ResponseEntity<List<Rating>> getProductRatings(@PathVariable Long productId) {
		
		List<Rating> ratings = ratingService.getProductRatings(productId);
		
		return new ResponseEntity<> (ratings, HttpStatus.ACCEPTED);
	}

}
