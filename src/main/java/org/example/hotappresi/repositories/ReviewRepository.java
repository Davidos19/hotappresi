package org.example.hotappresi.repositories;

import org.example.hotappresi.models.Review;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ReviewRepository {
    private List<Review> reviews = new ArrayList<>();
    private Long currentId = 1L;

    public List<Review> getAllReviews() {
        return reviews;
    }

    public Review save(Review review) {
        if (review.getId() == null) {
            review.setId(currentId++);
            reviews.add(review);
        } else {
            // Aktualizacja istniejącej recenzji – prosta implementacja
            reviews = reviews.stream()
                    .map(r -> r.getId().equals(review.getId()) ? review : r)
                    .collect(Collectors.toList());
        }
        return review;
    }

    public List<Review> findByHotelId(Long hotelId) {
        return reviews.stream()
                .filter(r -> r.getHotelId() != null && r.getHotelId().equals(hotelId))
                .collect(Collectors.toList());
    }
}
