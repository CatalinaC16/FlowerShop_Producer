package com.example.flowerShop.utils.review;

import com.example.flowerShop.dto.review.ReviewDetailedDTO;
import com.example.flowerShop.entity.Review;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@NoArgsConstructor
public class ReviewUtils {

    public boolean validateReviewMap(ReviewDetailedDTO reviewDetailedDTO) {
        return !Objects.equals(reviewDetailedDTO.getText(), null)
                && reviewDetailedDTO.getRating() >= 1
                && reviewDetailedDTO.getRating() <= 10
                && !Objects.equals(reviewDetailedDTO.getId_user(), null)
                && !Objects.equals(reviewDetailedDTO.getId_product(), null);
    }

    public void updateReview(Review review, ReviewDetailedDTO reviewDetailedDTO) {

        if (Objects.nonNull(reviewDetailedDTO.getText())) {
            review.setText(reviewDetailedDTO.getText());
        }
        if (!(reviewDetailedDTO.getRating() < 1 || reviewDetailedDTO.getRating() > 10)) {
            review.setRating(reviewDetailedDTO.getRating());
        }
    }
}
