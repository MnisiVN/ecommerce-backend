package com.ntsako.ecommerce.service;

import java.util.List;

import com.ntsako.ecommerce.exception.ProductException;
import com.ntsako.ecommerce.exception.UserException;
import com.ntsako.ecommerce.model.Rating;
import com.ntsako.ecommerce.model.User;
import com.ntsako.ecommerce.request.RatingRequest;

public interface RatingService {
	
	public Rating createRating(RatingRequest ratingRequest) throws ProductException, UserException;
	
	public List<Rating> getProductRatings(Long productId);
	

}
