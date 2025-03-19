package org.example.hotappresi.services;

import org.example.hotappresi.models.Review;
import org.example.hotappresi.repositories.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public List<Review> getReviewsForHotel(Long hotelId) {
        return reviewRepository.findByHotelId(hotelId);
    }

    public double getAverageRating(Long hotelId) {
        List<Review> reviews = getReviewsForHotel(hotelId);
        if (reviews.isEmpty()) {
            return 0;
        }
        double sum = reviews.stream().mapToInt(Review::getRating).sum();
        return sum / reviews.size();
    }

    public Review addReview(Review review) {
        return reviewRepository.save(review);
    }
}
