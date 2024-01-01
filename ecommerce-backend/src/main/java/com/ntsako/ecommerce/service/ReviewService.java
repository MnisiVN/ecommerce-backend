package com.ntsako.ecommerce.service;

import java.util.List;

import com.ntsako.ecommerce.exception.ProductException;
import com.ntsako.ecommerce.exception.UserException;
import com.ntsako.ecommerce.model.Review;
import com.ntsako.ecommerce.request.ReviewRequest;

public interface ReviewService {

	public Review createReview(ReviewRequest reviewRequest) throws ProductException, UserException;
	
	public List<Review> getAllProductReviews(Long productId);
}
